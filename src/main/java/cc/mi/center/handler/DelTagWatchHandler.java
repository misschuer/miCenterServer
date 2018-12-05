package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.DelTagWatch;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class DelTagWatchHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext nil, Channel channel, Packet decoder) {
		DelTagWatch packet = (DelTagWatch)decoder;
		String ownerId = packet.getOwnerTag();
		CenterServerManager.INSTANCE.delTagWatch(ownerId);
	}

}
