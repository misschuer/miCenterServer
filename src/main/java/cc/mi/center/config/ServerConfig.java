package cc.mi.center.config;

import java.io.IOException;
import java.net.URL;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import cc.mi.core.constance.NetConst;

public class ServerConfig {
	private static final String SCENES = "scenes";
	private static final String CENTER_SERVER = "centerServer";
	private static int port;
	private static int sceneCount;
	
	public static void loadConfig() throws NumberFormatException, Exception {
		Config cfg = new Config();
		URL url = ServerConfig.class.getResource("/config.ini");
		Ini ini = new Ini();
        ini.setConfig(cfg);
        try {
        	// 加载配置文件  
        	ini.load(url);

        	Section section = ini.get(CENTER_SERVER);
        	port = Integer.parseInt(section.get(NetConst.PORT));
        	sceneCount = Integer.parseInt(section.get(SCENES));
        } catch (IOException e) {
        	e.printStackTrace();
	    }  
	}

	public static int getPort() {
		return port;
	}

	public static int getSceneCount() {
		return sceneCount;
	}
}
