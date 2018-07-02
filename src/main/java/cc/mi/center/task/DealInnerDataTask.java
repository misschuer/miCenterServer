package cc.mi.center.task;

import cc.mi.center.system.CenterSystemManager;
import cc.mi.core.coder.Packet;
import cc.mi.core.task.base.AbstractCoderTask;
import io.netty.channel.Channel;

public class DealInnerDataTask extends AbstractCoderTask {
	private final Channel channel;
	public DealInnerDataTask(Channel channel, Packet coder) {
		super(coder);
		this.channel = channel;
	}
	
	@Override
	protected void doTask() {
		CenterSystemManager.INSTANCE.invokeHandler(channel, coder);
	}
}
