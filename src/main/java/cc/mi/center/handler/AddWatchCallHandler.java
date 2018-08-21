package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.AddWatchAndCall;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class AddWatchCallHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext nil, Channel channel, Packet decoder) {
		AddWatchAndCall packet = (AddWatchAndCall)decoder;
		int fd = packet.getFd();
		String binId = packet.getGuidType();
		if (fd > 0) {
			CenterServerManager.INSTANCE.addOuterWatchAndCall(fd, binId);
			return;
		}
		CenterServerManager.INSTANCE.addInnerWatchAndCall(channel, binId);
	}

}
