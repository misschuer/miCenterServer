package cc.mi.center.handler;

import cc.mi.center.CenterContext;
import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.msg.CreateConnection;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class CreateConnectionHandler extends AbstractHandler {

	@Override
	public void handle(ServerContext player, Channel channel, Coder decoder) {
		if (SystemManager.getChannelId(channel) == IdentityConst.IDENDITY_GATE) {
			CreateConnection cc = (CreateConnection)decoder;
			CenterContext context = new CenterContext(cc.getFd());
			context.setRemoteIp(cc.getRemoteIp());
			context.setRemotePort(cc.getRemotePort());
			
			// 发给其他服务端
			SystemManager.sendMsgToInner(decoder);
		}
	}

}
