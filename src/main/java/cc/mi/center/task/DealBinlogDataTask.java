package cc.mi.center.task;

import cc.mi.core.packet.Packet;
import cc.mi.core.task.base.AbstractCoderTask;

public class DealBinlogDataTask extends AbstractCoderTask {
	private final int serverFd;
	public DealBinlogDataTask(int fd, Packet coder) {
		super(coder);
		this.serverFd = fd;
	}
	
	@Override
	protected void doTask() {
		//TODO: 不会同步自己发来的数据
	}
}
