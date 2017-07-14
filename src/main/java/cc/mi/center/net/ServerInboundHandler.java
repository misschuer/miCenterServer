package cc.mi.center.net;

import cc.mi.core.coder.Coder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerInboundHandler extends SimpleChannelInboundHandler<Coder> {
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("center server建立连接" + System.currentTimeMillis());
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Coder msg) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	 @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		 System.out.println("与网关服断开连接");
		 ctx.fireChannelInactive();
    }
}
