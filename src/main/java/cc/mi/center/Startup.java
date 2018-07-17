package cc.mi.center;

import cc.mi.center.config.ServerConfig;
import cc.mi.center.net.CenterHandler;
import cc.mi.center.net.CenterToGateHandler;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.log.CustomLogger;
import cc.mi.core.net.ClientCore;
import cc.mi.core.net.ServerCore;

public class Startup {
	static final CustomLogger logger = CustomLogger.getLogger(Startup.class);
	private static void start() throws NumberFormatException, Exception {
		ServerConfig.loadConfig();
    	
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						ClientCore.INSTANCE.start(ServerConfig.getGateIp(), ServerConfig.getGatePort(), new CenterToGateHandler());
					} catch (Exception e) {
					} finally {
						logger.devLog("连接网关服错误,系统将在1秒钟后重新连接");
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, "center-to-gate").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						ServerCore.INSTANCE.run(ServerConfig.getCenterPort(), new CenterHandler(), IdentityConst.SERVER_TYPE_CENTER);
					} catch (Exception e) {
					} finally {
						logger.devLog("监听内部服务器端口发生错误,系统将在1秒钟后重新执行");
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, "center-for-inner").start();
		
	}

	public static void main(String[] args) throws NumberFormatException, Exception {
		start();
	}
}
