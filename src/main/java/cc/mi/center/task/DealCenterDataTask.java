package cc.mi.center.task;

import cc.mi.center.system.CenterSystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.handler.Handler;
import cc.mi.core.task.base.AbstractCoderTask;
import io.netty.channel.Channel;

public class DealCenterDataTask extends AbstractCoderTask {
	private final Channel channel;
	public DealCenterDataTask(Channel channel, Coder coder) {
		super(coder);
		this.channel = channel;
	}
	
	@Override
	protected void doTask() {
		Handler handler = CenterSystemManager.getHandler(coder.getOpcode());
		handler.handle(null, this.channel, coder);
	}

}
