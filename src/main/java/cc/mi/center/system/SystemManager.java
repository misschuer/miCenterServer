package cc.mi.center.system;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.mi.center.handler.RegIdentityHandler;
import cc.mi.center.handler.RegOpcodeHandler;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.Opcodes;
import cc.mi.core.handler.Handler;
import cc.mi.core.task.base.Task;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public final class SystemManager {
	// 通道的id属性
	private static final AttributeKey<Byte> CHANNEL_ID = AttributeKey.valueOf("channel_id");
	
	// 单线程逻辑
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	// 句柄
	public static final Handler<?>[] handlers = new Handler[1<<12];
	
	// 
	private static final Channel[] channels = new Channel[1<<4];
	
	private static final int[] serverCount = new int[ 5 ];
	// 场景服的数量
	private static int sceneCount = 0;
	
	protected static Channel gateChannel;
	
	// 注册的opcode
	private static final Map<Byte, Set<Integer>> serverOpcodeHash = new HashMap<>();
	
	static {
		handlers[Opcodes.MSG_SERVERREGOPCODE] = new RegOpcodeHandler();
		handlers[Opcodes.MSG_SERVERREGIDENTITY] = new RegIdentityHandler();
	}

	// 提交客户端过来的任务
	public static void submitTask(Task task) {
		executor.submit(task);
	}
	
	public static void channelRegIdentity(Channel channel, byte id) {
		System.out.printf("start to recognize identity = %d, server = %s\n", id, IdentityConst.getServerName(id));

		if (id == IdentityConst.IDENDITY_SCENE) {
			for (int i = 0; i < sceneCount; ++ i) {
				if (channels[i+id] == null || !channels[i+id].isActive()) {
					channels[i+id] = channel;
					serverCount[ id ] ++;
					bindFd(channel, (byte) (i+id));
					return;
				}
			}
			channel.close();
			return;
		} else if (id == IdentityConst.IDENDITY_GATE) {
			if (gateChannel != null && gateChannel.isActive()) {
				channel.close();
				return;
			}
			gateChannel = channel;
			bindFd(channel, id);
			return;
		}
		
		if (serverCount[ id ] != 0) {
			channel.close();
			return;
		}
		serverCount[ id ] ++;
		channels[ id ] = channel;
		bindFd(channel, id);
	}
	
	private static void bindFd(Channel channel, byte id) {
		System.out.printf("finish to bind fd = %d\n", id);
		channel.attr(CHANNEL_ID).set(id);
	}
	
	public static void channelInactived(Channel channel) {
		Attribute<Byte> attr = channel.attr(CHANNEL_ID);
		
		Byte id = attr.get();
		if (id != null) {
			attr.set(null);
			if (id > 0) {
				// 不是网关服的才需要这样做
				channels[ id ] = null;
				int serverId = id > IdentityConst.IDENDITY_SCENE ? IdentityConst.IDENDITY_SCENE : id;
				serverCount[serverId] --;
			} else {
				gateChannel = null;
			}
		}
		
		System.out.printf("unbind fd = %d\n", id);
	}
	
	public static void setSceneCount(int count) {
		if (sceneCount == 0 && count > 0) {
			sceneCount = count;
		}
	}
	
	public static Byte getChannelId(Channel channel) {
		return channel.attr(CHANNEL_ID).get();
	}
	
	public static void regOpcode(Channel channel, List<Integer> opcodes) {
		byte id = getChannelId(channel);
		Set<Integer> opcodeSet = new HashSet<>();
		opcodeSet.addAll(opcodes);
		serverOpcodeHash.put(id, opcodeSet);
	}
	
	public static Channel getGateChannel() {
		return gateChannel;
	}
}
