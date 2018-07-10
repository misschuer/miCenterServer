package cc.mi.center.net;

import cc.mi.center.server.CenterServerManager;
import cc.mi.center.task.DealInnerDataTask;
import cc.mi.core.handler.ChannelHandlerGenerator;
import cc.mi.core.log.CustomLogger;
import cc.mi.core.packet.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CenterHandler extends SimpleChannelInboundHandler<Packet> implements ChannelHandlerGenerator {
	static final CustomLogger logger = CustomLogger.getLogger(CenterHandler.class);
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Packet msg) throws Exception {
		int fd = msg.getFD();
		if (fd > 0) {
			// send to inner server
		} else {
			CenterServerManager.INSTANCE.dealBinlogData(ctx.channel(), msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	 @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		 CenterServerManager.INSTANCE.onInnerServertDisconnected(ctx.channel());
		 ctx.fireChannelInactive();
    }

	@Override
	public ChannelHandler newChannelHandler() {
		return new CenterHandler();
	}
}
