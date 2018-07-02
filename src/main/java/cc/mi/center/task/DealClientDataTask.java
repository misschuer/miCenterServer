package cc.mi.center.task;

import cc.mi.center.system.CenterSystemManager;
import cc.mi.core.coder.Packet;
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
