package cc.mi.center;

import java.io.IOException;
import java.net.URL;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import cc.mi.center.net.Server;
import cc.mi.center.system.SystemManager;

public class Startup {
	private static final String CENTER_SERVER = "centerServer";
	private static final String PORT = "port";
	private static final String SCENES = "scenes";
	
	private static void loadConfig() throws NumberFormatException, Exception {
		Config cfg = new Config();
		URL url = Startup.class.getResource("/config.ini");
		Ini ini = new Ini();
        ini.setConfig(cfg);
        try {
        	// 加载配置文件  
        	ini.load(url);

        	Section section = ini.get(CENTER_SERVER);
        	int sceneCount = Integer.parseInt(section.get(SCENES));
        	SystemManager.setSceneCount(sceneCount);
        	Server.run(Integer.parseInt(section.get(PORT)));
        } catch (IOException e) {
        	e.printStackTrace();
	    }  
	}
	
	public static void main(String[] args) throws NumberFormatException, Exception {
		loadConfig();
	}
}
