package cc.mi.center.task;

import cc.mi.core.coder.Coder;
import cc.mi.core.task.base.AbstractCoderTask;
import io.netty.channel.Channel;

public class SendDataToClientTask extends AbstractCoderTask {
	private final Channel channel;
	
	public SendDataToClientTask(Channel channel, Coder coder) {
		super(coder);
		this.channel = channel;
	}
	
	@Override
	protected void doTask() {
		
	}
}
