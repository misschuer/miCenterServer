package cc.mi.center.net;

import cc.mi.center.system.SystemManager;
import cc.mi.center.task.DealCenterDataTask;
import cc.mi.center.task.DealClientDataTask;
import cc.mi.center.task.SendDataTask;
import cc.mi.core.coder.Coder;
import cc.mi.core.constance.IdentityConst;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerInboundHandler extends SimpleChannelInboundHandler<Coder> {
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("a socket connected" + System.currentTimeMillis());
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Coder msg) throws Exception {
		
		if (msg.getInternalDestFD() == 0) {
			// 处理内部传输给中心服的
			SystemManager.submitTask(new DealCenterDataTask(ctx.channel(), msg));
		} else if (msg.getInternalDestFD() == -2) {
			// 处理网关服来的
			SystemManager.submitTask(new DealClientDataTask(msg));
		} else {
			// 处理从内部服务器发过来的
			SystemManager.submitTask(new SendDataTask(msg.getInternalDestFD(), msg));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	 @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		 String name = "unknow";
		 Byte identity = SystemManager.getChannelId(ctx.channel());
		 if (identity != null) {
			 name = IdentityConst.getServerName(identity);
		 }
		 System.out.println(String.format("%s is disconnected", name));
		 SystemManager.channelInactived(ctx.channel());
		 ctx.fireChannelInactive();
    }
}
