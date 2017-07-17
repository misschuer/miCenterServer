package cc.mi.center.handler;

import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.generate.msg.ServerRegIdentity;
import cc.mi.core.handler.AbstractHandler;
import io.netty.channel.Channel;

public class RegIdentityHandler extends AbstractHandler<Void> {

	@Override
	public void handle(Void player, Channel channel, Coder decoder) {
		ServerRegIdentity coder = (ServerRegIdentity) decoder;
		SystemManager.channelRegIdentity(channel, coder.getIdentity());
	}
}
