package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.PutObject;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class PutObjectHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
		PutObject po = (PutObject)decoder;
		int fd = CenterServerManager.INSTANCE.getChannelFd(channel);
		CenterServerManager.INSTANCE.onBinlogDataUpdated(fd, po.getOwnerId(), po.getBinlogData());
	}
}
