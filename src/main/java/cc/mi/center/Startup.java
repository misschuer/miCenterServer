package cc.mi.center;

import cc.mi.center.config.ServerConfig;
import cc.mi.center.net.CenterToGateHandler;
import cc.mi.core.net.ClientCore;

public class Startup {
	private static void start() throws NumberFormatException, Exception {
		ServerConfig.loadConfig();
    	
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ClientCore.INSTANCE.start(ServerConfig.getGateIp(), ServerConfig.getGatePort(), new CenterToGateHandler());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "center-to-gate").start();
		
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					ServerCore.run(ServerConfig.getCenterPort(), new CenterHandler(), IdentityConst.SERVER_TYPE_CENTER);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, "center-for-inner").start();
		
	}

	public static void main(String[] args) throws NumberFormatException, Exception {
		start();
	}
}
