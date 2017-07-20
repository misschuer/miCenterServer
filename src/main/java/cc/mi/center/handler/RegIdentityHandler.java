package cc.mi.center.handler;

import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.generate.msg.ServerRegIdentity;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class RegIdentityHandler extends AbstractHandler {

	@Override
	public void handle(ServerContext player, Channel channel, Coder decoder) {
		ServerRegIdentity coder = (ServerRegIdentity) decoder;
		SystemManager.channelRegIdentity(channel, coder.getIdentity());
	}
}
