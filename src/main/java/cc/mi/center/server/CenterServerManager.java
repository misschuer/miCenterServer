package cc.mi.center.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.mi.center.handler.CreateConnectionHandler;
import cc.mi.center.handler.DestroyConnectionHandler;
import cc.mi.center.handler.IdentityServerTypeHandler;
import cc.mi.center.handler.RegIdentityHandler;
import cc.mi.center.handler.RegOpcodeHandler;
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
	
	// 固定线程线程逻辑
	private final ExecutorService executor = Executors.newFixedThreadPool(4);
	
	// 通道的id属性
	private final AttributeKey<Integer> CHANNEL_ID = AttributeKey.valueOf("channel_id");
	
	// 内部服务器通道列表
	private final Map<Integer, Channel> innerChannelHash = new ConcurrentHashMap<>();
	
	// 句柄
	private final Handler[] handlers = new Handler[1<<6];
	
	private Channel gateChannel;
	
	private boolean centerBootstrap = false;
	
	// 注册的opcode
	private final Map<Byte, Set<Integer>> serverOpcodeHash = new HashMap<>();
	
	private CenterServerManager() {
		handlers[Opcodes.MSG_SERVERREGOPCODE] = new RegOpcodeHandler();
		handlers[Opcodes.MSG_SERVERREGIDENTITY] = new RegIdentityHandler();
		handlers[Opcodes.MSG_CREATECONNECTION] = new CreateConnectionHandler();
		handlers[Opcodes.MSG_DESTROYCONNECTION] = new DestroyConnectionHandler();
		handlers[Opcodes.MSG_IDENTITYSERVERMSG] = new IdentityServerTypeHandler();
	}
	
	public void invokeHandler(Channel channel, Packet decoder) {
		Handler handle = handlers[decoder.getOpcode()];
		if (handle != null) {
			handle.handle(null, channel, decoder);
		}
	}

	// 提交客户端过来的任务
	public void submitTask(Task task) {
		executor.submit(task);
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
	
	/**
	 * 告诉对方自己的身份
	 * @param channel
	 */
	private void indentityServer(Channel channel) {
		logger.devLog("identity center to gate");
		IdentityServerMsg ism = new IdentityServerMsg();
		ism.setServerType(IdentityConst.SERVER_TYPE_CENTER);
		channel.writeAndFlush(ism);
	}
	
//	public void sendMsgToInner(Coder coder) {
////		int opcode = coder.getOpcode();
////		for (Entry<Byte, Set<Integer>> entry : serverOpcodeHash.entrySet()) {
////			if (entry.getValue().contains(opcode)) {
////				sendMsgByFd(entry.getKey(), coder);
////			}
////		}
//	}
//	
//	public void regOpcode(Channel channel, List<Integer> opcodes) {
////		byte id = getChannelId(channel);
////		Set<Integer> opcodeSet = new HashSet<>();
////		opcodeSet.addAll(opcodes);
////		serverOpcodeHash.put(id, opcodeSet);
//	}
	
//	private Channel getChannelByFd(int fd) {
//		if (fd == MsgConst.MSG_TO_GATE) {
//			return gateChannel;
//		}
//		return innerChannelHash.get(fd);
//	}
//	
//	public void sendMsgByFd(int fd, Coder coder) {
//		Channel channel = getChannelByFd(fd);
//		sendMsg(channel, coder);
//	}
//	
//	public void sendMsg(Channel channel, Coder coder) {
//		if (channel != null && !channel.isActive()) {
//			channel.writeAndFlush(coder);
//		}
//	}
}
