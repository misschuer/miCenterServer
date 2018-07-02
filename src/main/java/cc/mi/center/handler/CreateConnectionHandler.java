package cc.mi.center.handler;

import cc.mi.center.CenterContext;
import cc.mi.center.server.CenterServerManager;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.msg.CreateConnection;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class CreateConnectionHandler extends AbstractHandler {

	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
//		if (CenterSystemManager.getChannelId(channel) == IdentityConst.SERVER_TYPE_GATE) {
//			CreateConnection cc = (CreateConnection)decoder;
//			CenterContext context = new CenterContext(cc.getFd());
//			context.setRemoteIp(cc.getRemoteIp());
//			context.setRemotePort(cc.getRemotePort());
//			
//			// 发给其他服务端
//			CenterSystemManager.sendMsgToInner(decoder);
//		}
	}

}
