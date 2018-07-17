package cc.mi.center.handler;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.generate.msg.PutObjects;
import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class PutObjectSHandler extends HandlerImpl {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
		PutObjects po = (PutObjects)decoder;
		int fd = CenterServerManager.INSTANCE.getChannelFd(channel);
		CenterServerManager.INSTANCE.onBinlogDatasUpdated(fd, po.getOwnerId(), po.getBinlogDataList());
	}
}
