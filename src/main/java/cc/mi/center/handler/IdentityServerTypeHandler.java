package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.IdentityServerMsg;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class IdentityServerTypeHandler extends HandlerImpl {
	@Override
	public void handle(ServerContext nil, Channel channel, Packet decoder) {
		IdentityServerMsg msg = (IdentityServerMsg)decoder;
		CenterServerManager.INSTANCE.onInnerServerIdentity(channel, msg.getServerType());
	}
}
