package cc.mi.center.handler;

import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.generate.msg.ServerRegOpcode;
import cc.mi.core.handler.AbstractHandler;
import io.netty.channel.Channel;

public class RegOpcodeHandler extends AbstractHandler<Void> {

	@Override
	public void handle(Void player, Channel channel, Coder decoder) {
		ServerRegOpcode coder = (ServerRegOpcode) decoder;
		SystemManager.regOpcode(channel, coder.getOpcodes());
	}
}
