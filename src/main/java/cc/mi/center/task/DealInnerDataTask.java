package cc.mi.center.task;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.packet.Packet;
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
		CenterServerManager.INSTANCE.invokeHandler(channel, coder);
	}
}
