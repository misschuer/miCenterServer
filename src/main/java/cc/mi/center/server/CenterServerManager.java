package cc.mi.center.server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.mi.center.handler.AddTagWatchCallHandler;
import cc.mi.center.handler.AddWatchCallHandler;
import cc.mi.center.handler.CreateConnectionHandler;
import cc.mi.center.handler.DestroyConnectionHandler;
import cc.mi.center.handler.IdentityServerTypeHandler;
import cc.mi.center.handler.RegOpcodeHandler;
import cc.mi.center.task.DealBinlogDataTask;
import cc.mi.center.task.DealClientDataTask;
import cc.mi.center.task.SendToInnerTask;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.Opcodes;
import cc.mi.core.generate.msg.IdentityServerMsg;
import cc.mi.core.generate.msg.ServerStartFinishMsg;
import cc.mi.core.handler.Handler;
import cc.mi.core.log.CustomLogger;
import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.Task;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public enum CenterServerManager {
	INSTANCE;
	
	private static final CustomLogger logger = CustomLogger.getLogger(CenterServerManager.class);
	
	// 逻辑线程组 给它进行负载均衡
	// 还能保证每个客户端的消息一定是有序的
	private final ExecutorService[] clientGroup;
	private static final int GROUP_SIZE = 4;
	private static final int MOD = GROUP_SIZE - 1;
	
	// 通道的id属性
	private final AttributeKey<Integer> CHANNEL_ID = AttributeKey.valueOf("channel_id");
	// 
	private final AttributeKey<ServerOpcode> SERVER_OPCODE = AttributeKey.valueOf("ServerOpcode");
	
	// 内部服务器通道列表
	private final Map<Integer, Channel> innerChannelHash = new ConcurrentHashMap<>();
	
	// 句柄
	private final Handler[] handlers = new Handler[1<<6];
	
	private Channel gateChannel;
	
	private boolean centerBootstrap = false;
	
	private CenterServerManager() {
		// 初始化线程组, 数量一定要2的幂, 否则会导致分配线程逻辑错误
		clientGroup = new ExecutorService[GROUP_SIZE];
		for (int i = 0; i< GROUP_SIZE; ++ i) {
			clientGroup[ i ] = Executors.newFixedThreadPool(1);
		}
		
		handlers[Opcodes.MSG_SERVERREGOPCODE] = new RegOpcodeHandler();
		handlers[Opcodes.MSG_CREATECONNECTION] = new CreateConnectionHandler();
		handlers[Opcodes.MSG_DESTROYCONNECTION] = new DestroyConnectionHandler();
		handlers[Opcodes.MSG_IDENTITYSERVERMSG] = new IdentityServerTypeHandler();
		handlers[Opcodes.MSG_ADDWATCHANDCALL] = new AddWatchCallHandler();
		handlers[Opcodes.MSG_ADDTAGWATCHANDCALL] = new AddTagWatchCallHandler();
	}
	
	public void invokeHandler(Channel channel, Packet decoder) {
		Handler handle = handlers[decoder.getOpcode()];
		if (handle != null) {
			handle.handle(null, channel, decoder);
		}
	}

	/**
	 * 
	 * @param fd
	 * @param task
	 */
	public void submitTask(int fd, Task task) {
		clientGroup[fd & MOD].submit(task);
	}
	
	/**
	 * 只需要登录服, 应用服,场景服,(日志服非必须)
	 */
	private void checkAllServerFound() {
		int servers = 3;
		boolean vist = true;
		if (!innerChannelHash.containsKey(IdentityConst.SERVER_TYPE_LOGIN)) {
			vist = false;
		}
		
		if (!innerChannelHash.containsKey(IdentityConst.SERVER_TYPE_APP)) {
			vist = false;
		}
		
		if (!innerChannelHash.containsKey(IdentityConst.SERVER_TYPE_RECORD)) {
			servers --;
		}
		
		if (innerChannelHash.size() == servers) {
			vist = false;
		}
		
		if (!this.centerBootstrap && vist) {
			logger.devLog("all server found");
		} else if (this.centerBootstrap && !vist) {
			logger.devLog("some servers is not found");
		}
		
		if (vist != this.centerBootstrap) {
			this.centerBootstrap = vist;
			this.noticeGateBootstrap();
		}
	}
	
	private void noticeGateBootstrap() {
		ServerStartFinishMsg msg = new ServerStartFinishMsg();
		msg.setBootstrap(this.centerBootstrap);
		if (this.gateChannel != null && this.gateChannel.isActive()) {
			this.gateChannel.writeAndFlush(msg);
		}
	}
	
	/**
	 * 内部服务器连进来了
	 * @param channel
	 */
	public void onInnerServerIdentity(Channel channel, int serverType) {
		int fd = serverType;
		if (serverType == IdentityConst.SERVER_TYPE_SCENE) {
			boolean vist = false;
			for (int i = fd; i < 100; ++ i) {
				if (!innerChannelHash.containsKey(i)) {
					fd = i;
					vist = true;
					break;
				}
			}
			if (!vist)
				throw new RuntimeException(String.format("serverType = %d, has too many", serverType));
		}
		
		if (innerChannelHash.containsKey(fd)) {
			throw new RuntimeException(String.format("serverType = %d, has duplicate fd %d", serverType, fd));
		}
		
		channel.attr(CHANNEL_ID).set(fd);
		innerChannelHash.put(fd, channel);
		
		logger.devLog("identity fd = {} serverType = {}", fd, serverType);
		this.checkAllServerFound();
	}
	
	/**
	 * 内部服务器注册消息
	 * @param channel
	 */
	public void onInnerServerRegisterOpcode(Channel channel, List<Integer> opcodes) {
		ServerOpcode serverOpcode = new ServerOpcode();
		serverOpcode.addOpcodes(opcodes);
		channel.attr(SERVER_OPCODE).set(serverOpcode);
		logger.devLog("add opcodes fd = {} opcodes = {}", channel.attr(CHANNEL_ID).get(), Arrays.toString(opcodes.toArray()));
	}
	
	
	public void sendToInnerServerWhenRegisted(Packet packet) {
		int opcode = packet.getOpcode();
		for (Entry<Integer, Channel> entry : innerChannelHash.entrySet()) {
			Channel channel = entry.getValue();
			ServerOpcode so = channel.attr(SERVER_OPCODE).get();
			if (so.contains(opcode)) {
				channel.writeAndFlush(packet);
			}
		}
	}
	
	
	/**
	 * 内部服务器断开连接了
	 * @param channel
	 */
	public void onInnerServertDisconnected(Channel channel) {
		int fd = getChannelFd(channel);
		innerChannelHash.remove(fd);
		this.checkAllServerFound();
	}
	
	public int getChannelFd(Channel channel) {
		return channel.attr(CHANNEL_ID).get();
	}
	
	/**
	 * 和网关服连接上以后触发的操作
	 * @param gateChannel
	 */
	public void onGateConnected(Channel gateChannel) {
		if (this.gateChannel != null) 
			throw new RuntimeException("duplicate connected gate");
		if (gateChannel == null)
			throw new RuntimeException("set gate channel with null");
		this.gateChannel = gateChannel;
		this.indentityServer(this.gateChannel);
		// 连上以后
		if (this.centerBootstrap) {
			this.noticeGateBootstrap();
		}
	}
	
	public void onGateDisconnected(Channel gateChannel) {
		if (this.gateChannel == gateChannel) {
			this.gateChannel = null;
			return;
		}
		throw new RuntimeException("与网关服断开连接, 但是断开的channel不是this.gateChannel");
	}
	
	public void receiveDataFromGate(Packet packet) {
		int fd = packet.getFD();
		CenterServerManager.INSTANCE.submitTask(fd, new DealClientDataTask(null, packet));
	}
	
	/**
	 * 
	 * @param channel
	 * @param packet
	 */
	public void dealBinlogData(Channel channel, Packet packet) {
		int fd = this.getChannelFd(channel);
		// 这里可能会有多个服一起同步的情况需要有先后顺序
		this.submitTask(0, new DealBinlogDataTask(fd, packet));
	}
	
	public void sendToInnerServer(int fd, Packet packet) {
		Channel channel = innerChannelHash.get(fd);
		if (channel != null) {
			this.submitTask(fd, new SendToInnerTask(channel, packet));
		}
	}
	
	/**
	 * 告诉对方自己的身份
	 * @param channel
	 */
	private void indentityServer(Channel channel) {
		IdentityServerMsg ism = new IdentityServerMsg();
		ism.setServerType(IdentityConst.SERVER_TYPE_CENTER);
		channel.writeAndFlush(ism);
	}
}
