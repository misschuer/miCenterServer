package cc.mi.center.task;

import cc.mi.center.system.SystemManager;
import cc.mi.core.coder.Coder;
import cc.mi.core.task.base.AbstractCoderTask;

public class SendDataTask extends AbstractCoderTask {
	private final int fd;
	
	public SendDataTask(int fd, Coder coder) {
		super(coder);
		this.fd = fd;
	}
	
	@Override
	protected void doTask() {
		SystemManager.sendMsgByFd(fd, coder);
	}
}
