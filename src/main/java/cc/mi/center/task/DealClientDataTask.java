package cc.mi.center.task;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.AbstractCoderTask;
import io.netty.channel.Channel;

public class DealClientDataTask extends AbstractCoderTask {
	
	private final Channel channel;
	public DealClientDataTask(Channel channel, Packet coder) {
		super(coder);
		this.channel = channel;
	}
	
	@Override
	protected void doTask() {
		CenterServerManager.INSTANCE.invokeHandler(channel, coder);
		CenterServerManager.INSTANCE.sendToInnerServerWhenRegisted(coder);
	}

}
