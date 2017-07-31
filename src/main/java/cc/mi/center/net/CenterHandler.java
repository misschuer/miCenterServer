package cc.mi.center.net;

import cc.mi.center.system.CenterSystemManager;
import cc.mi.center.task.DealCenterDataTask;
import cc.mi.center.task.DealClientDataTask;
import cc.mi.center.task.SendDataTask;
import cc.mi.core.coder.Coder;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.constance.MsgConst;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CenterHandler extends SimpleChannelInboundHandler<Coder> {
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		System.out.println("a socket connected" + System.currentTimeMillis());
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}
	
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final Coder msg) throws Exception {
		
		if (msg.getInternalDestFD() == MsgConst.MSG_TO_CENTER) {
			// 处理内部传输给中心服的
			CenterSystemManager.submitTask(new DealCenterDataTask(ctx.channel(), msg));
		} else if (msg.getInternalDestFD() == MsgConst.MSG_FROM_GATE) {
			// 处理网关服来的
			CenterSystemManager.submitTask(new DealClientDataTask(msg));
		} else {
			// 处理从内部服务器发过来的
			CenterSystemManager.submitTask(new SendDataTask(msg.getInternalDestFD(), msg));
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
		 Byte identity = CenterSystemManager.getChannelId(ctx.channel());
		 if (identity != null) {
			 name = IdentityConst.getServerName(identity);
		 }
		 System.out.println(String.format("%s is disconnected", name));
		 CenterSystemManager.channelInactived(ctx.channel());
		 ctx.fireChannelInactive();
    }
}
