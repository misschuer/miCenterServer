package cc.mi.center.task;

import cc.mi.center.server.CenterServerManager;
import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.AbstractCoderTask;

public class SendDataTask extends AbstractCoderTask {
	private final int fd;
	
	public SendDataTask(int fd, Packet coder) {
		super(coder);
		this.fd = fd;
	}
	
	@Override
	protected void doTask() {
//		CenterSystemManager.sendMsgByFd(fd, coder);
	}
}
