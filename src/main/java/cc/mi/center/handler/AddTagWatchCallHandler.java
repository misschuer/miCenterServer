package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.AddTagWatchAndCall;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class AddTagWatchCallHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext nil, Channel channel, Packet decoder) {
		AddTagWatchAndCall packet = (AddTagWatchAndCall)decoder;
		int fd = packet.getFd();
		String ownerId = packet.getOwnerTag();
		if (fd > 0) {
			CenterServerManager.INSTANCE.addOuterTagWatchAndCall(fd, ownerId);
			return;
		}
		CenterServerManager.INSTANCE.addInnerTagWatchAndCall(channel, ownerId);
	}

}
