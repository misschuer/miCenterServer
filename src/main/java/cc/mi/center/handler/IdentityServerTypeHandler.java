package cc.mi.center.handler;

import cc.mi.core.coder.Packet;
import cc.mi.core.generate.msg.IdentityServerMsg;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class IdentityServerTypeHandler extends AbstractHandler {
	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
		IdentityServerMsg msg = (IdentityServerMsg)decoder;
		
	}
}
