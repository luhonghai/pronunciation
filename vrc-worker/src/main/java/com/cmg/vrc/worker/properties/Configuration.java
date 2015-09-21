package com.cmg.vrc.worker.properties;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Configuration {

	private static final Logger logger = Logger.getLogger(Configuration.class.getName());

	public static final String SYSTEM_PROPERTIES = "system.properties";

	public static final String PROJECT_VERSION = "project.version";
	
	private static Properties prop;

	public static String getValue(String key) {
		if (prop == null)
			getProperties(SYSTEM_PROPERTIES);
        try {
			String sysEnvKey = "VRC_" + key.replaceAll("\\.", "_").toUpperCase();
			if (System.getenv().containsKey(sysEnvKey)) {
				return System.getenv().get(sysEnvKey);
			}
            return prop != null ? prop.getProperty(key).trim() : "";
        } catch (Exception ex) {
            return "";
        }
	}

	public static void getProperties(String propName) {
		prop = new Properties();
		try {
			// load a properties file from class path, inside static method
			prop.load(Configuration.class.getClassLoader().getResourceAsStream(
					propName));			
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Cannot load properties", ex);
		}
	}
}
