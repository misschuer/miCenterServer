package cc.mi.center;

import cc.mi.core.packet.Packet;
import cc.mi.core.server.ServerContext;

public class CenterContext extends ServerContext {
	
	public CenterContext(int fd) {
		super(fd);
	}
	
	@Override
	protected void send(Packet coder) {
		// TODO Auto-generated method stub

	}

}
