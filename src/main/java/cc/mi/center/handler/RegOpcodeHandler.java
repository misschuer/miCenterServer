package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.ServerRegOpcode;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class RegOpcodeHandler extends AbstractHandler {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
//		ServerRegOpcode coder = (ServerRegOpcode) decoder;
//		CenterSystemManager.regOpcode(channel, coder.getOpcodes());
	}
}
