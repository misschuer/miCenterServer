package cc.mi.center.net;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.handler.ChannelHandlerGenerator;
import cc.mi.core.log.CustomLogger;
import cc.mi.core.packet.Packet;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CenterToGateHandler extends SimpleChannelInboundHandler<Packet> implements ChannelHandlerGenerator {
	static final CustomLogger logger = CustomLogger.getLogger(CenterToGateHandler.class);
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		CenterServerManager.INSTANCE.onGateConnected(ctx.channel());
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Packet msg) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		CenterServerManager.INSTANCE.onGateDisconnected(ctx.channel());
		ctx.fireChannelInactive();
    }

	@Override
	public ChannelHandler newChannelHandler() {
		return new CenterToGateHandler();
	}
}
