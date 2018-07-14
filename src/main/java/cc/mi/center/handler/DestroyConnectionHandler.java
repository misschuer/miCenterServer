package cc.mi.center.handler;

import cc.mi.core.handler.HandlerImpl;
import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;
import io.netty.channel.Channel;

public class DestroyConnectionHandler extends HandlerImpl {
	
	@Override
	public void handle(ServerContext player, Channel channel, Packet decoder) {
//		if (CenterSystemManager.getChannelId(channel) == IdentityConst.SERVER_TYPE_GATE) {
//			DestroyConnection dc = (DestroyConnection)decoder;
//			
//			CenterContext context = (CenterContext) ContextManager.getContext(dc.getFd());
//			if (context == null) {
//				return;
//			}
//			ContextManager.removeContext(dc.getFd());
//			
//			// 发给其他服务端
//			CenterSystemManager.sendMsgToInner(decoder);
//		}
	}

}
