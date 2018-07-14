package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.AddWatch;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class AddWatchHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
		AddWatch packet = (AddWatch)decoder;
		int fd = packet.getFd();
		String binId = packet.getGuidType();
		if (fd > 0) {
			CenterServerManager.INSTANCE.addOuterWatch(fd, binId);
			return;
		}
		CenterServerManager.INSTANCE.addInnerWatch(fd, binId);
	}

}
