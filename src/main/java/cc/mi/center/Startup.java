package cc.mi.center;

import cc.mi.center.config.ServerConfig;
import cc.mi.center.net.CenterHandler;
import cc.mi.center.system.CenterSystemManager;
import cc.mi.core.net.ServerCore;

public class Startup {
	private static void start() throws NumberFormatException, Exception {
		ServerConfig.loadConfig();
		CenterSystemManager.setSceneCount(ServerConfig.getSceneCount());
    	ServerCore.run(ServerConfig.getPort(), new CenterHandler());
	}

	public static void main(String[] args) throws NumberFormatException, Exception {
		start();
	}
}
