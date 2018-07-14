package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.AddTagWatch;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class AddTagWatchHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
		AddTagWatch packet = (AddTagWatch)decoder;
		int fd = packet.getFd();
		String ownerId = packet.getOwnerTag();
		if (fd > 0) {
			CenterServerManager.INSTANCE.addOuterTagWatch(fd, ownerId);
			return;
		}
		CenterServerManager.INSTANCE.addInnerTagWatch(fd, ownerId);
	}

}
