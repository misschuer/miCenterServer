package cc.mi.center.config;

import java.io.IOException;
import java.net.URL;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

import cc.mi.core.constance.NetConst;

public class ServerConfig {
	private static final String CENTER_SERVER = "center";
	private static final String GATE_SERVER = "gate";
	private static String gate_ip;
	private static int center_port;
	private static int gate_port;
	
	public static void loadConfig() throws NumberFormatException, Exception {
		Config cfg = new Config();
		URL url = ServerConfig.class.getResource("/config.ini");
		Ini ini = new Ini();
        ini.setConfig(cfg);
        try {
        	// 加载配置文件  
        	ini.load(url);

        	Section section = ini.get(CENTER_SERVER);
        	center_port = Integer.parseInt(section.get(NetConst.PORT));
        	
        	Section section2 = ini.get(GATE_SERVER);
        	gate_port = Integer.parseInt(section2.get(NetConst.PORT));
        	gate_ip = section2.get(NetConst.IP);
        } catch (IOException e) {
        	e.printStackTrace();
	    }  
	}

	public static int getCenterPort() {
		return center_port;
	}
	
	public static int getGatePort() {
		return gate_port;
	}
	
	public static String getGateIp() {
		return gate_ip;
	}
}
