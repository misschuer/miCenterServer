package cc.mi.center.task;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.AbstractCoderTask;

public class DealClientDataTask extends AbstractCoderTask {
	public DealClientDataTask(Packet coder) {
		super(coder);
	}
	
	@Override
	protected void doTask() {
//		CenterSystemManager.sendMsgToInner(coder);
	}

}
