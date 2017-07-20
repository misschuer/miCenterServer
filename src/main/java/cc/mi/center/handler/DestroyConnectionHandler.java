package cc.mi.center.handler;

import cc.mi.center.CenterContext;
import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.msg.DestroyConnection;
import cc.mi.core.handler.AbstractHandler;
import cc.mi.core.server.ContextManager;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class DestroyConnectionHandler extends AbstractHandler {
	
	@Override
	public void handle(ServerContext player, Channel channel, Coder decoder) {
		if (SystemManager.getChannelId(channel) == IdentityConst.IDENDITY_GATE) {
			DestroyConnection dc = (DestroyConnection)decoder;
			
			CenterContext context = (CenterContext) ContextManager.getContext(dc.getFd());
			if (context == null) {
				return;
			}
			ContextManager.removeContext(dc.getFd());
			
			// 发给其他服务端
			SystemManager.sendMsgToInner(decoder);
		}
	}

}
