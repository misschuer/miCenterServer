package cc.mi.center.task;

import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.AbstractCoderTask;
import io.netty.channel.Channel;

public class SendToInnerTask extends AbstractCoderTask {
	private final Channel channel;
	
	public SendToInnerTask(Channel channel, Packet coder) {
		super(coder);
		this.channel = channel;
	}
	
	@Override
	protected void doTask() {
		channel.writeAndFlush(coder);
	}
}
