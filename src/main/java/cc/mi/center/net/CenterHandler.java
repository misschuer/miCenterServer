package cc.mi.center.net;

import cc.mi.center.system.CenterSystemManager;
import cc.mi.center.task.DealInnerDataTask;
import cc.mi.core.coder.Packet;
import cc.mi.core.log.CustomLogger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CenterHandler extends SimpleChannelInboundHandler<Packet> {
	static final CustomLogger logger = CustomLogger.getLogger(CenterHandler.class);
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		logger.devLog("an inner server connected to center");
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Packet msg) throws Exception {
		int fd = msg.getFD();
		if (fd > 0) {
			// send to inner server
		} else {
			CenterSystemManager.INSTANCE.submitTask(new DealInnerDataTask(ctx.channel(), msg));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	 @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
//		 String name = "unknow";
//		 Byte identity = CenterSystemManager.getChannelId(ctx.channel());
//		 if (identity != null) {
//			 name = IdentityConst.getServerName(identity);
//		 }
//		 System.out.println(String.format("%s is disconnected", name));
//		 CenterSystemManager.channelInactived(ctx.channel());
		 logger.devLog("an inner server channelInactive");
		 ctx.fireChannelInactive();
    }
}
