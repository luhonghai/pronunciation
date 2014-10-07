package com.cmg.vrc.properties;

import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	private static final Logger logger = Logger.getLogger(Configuration.class);
	public static final String SYSTEM_PROPERTIES = "system.properties";

	public static final String XML_URL_PENSIONER = "xml.url.pensioner";
	public static final String XML_URL_EMPLOYEE = "xml.url.employee";
	public static final String NEWSLETTER_IMAGE_URL = "newsletter.image.url";
	
	public static final String PROJECT_LIST = "project.list";
	public static final String PROJECT_DIR = "project.dir";
	public static final String TEMP_FOLDER="project.temp.folder.dir";
	public static final String ROOT_FOLDER = "project.root.folder.dir";
	public static final String ASPN_KEYSTORE = "aspn.keystore";
	public static final String ASPN_KEY_PASSWD = "aspn.key.passwd";

    public static final String CONTACT_US_EMAIL = "contact.us.email";
    public static final String CONTACT_US_EMAIL_SENDER = "contact.us.email.sender";
    public static final String CONTACT_US_EMAIL_PASSWORD = "contact.us.email.password";

    public static final String VOICE_RECORD_DIR = "voice.record.dir";

    public static final String ACOUSTIC_MODEL_PATH ="acousticModelPath";
    public static final String DICTIONARY_PATH = "dictionaryPath";
    public static final String LANGUAGE_MODEL_PATH = "languageModelPath";
	
	private static Properties prop;

	public static String getValue(String key) {
		if (prop == null)
			getProperties(SYSTEM_PROPERTIES);
		return prop != null ? prop.getProperty(key) : "";
	}

	public static void getProperties(String propName) {
		prop = new Properties();
		try {
			// load a properties file from class path, inside static method
			prop.load(Configuration.class.getClassLoader().getResourceAsStream(
					propName));			
		} catch (Exception ex) {
			logger.error("Cannot load properties", ex);
		}
	}
}
