package com.technothink.speech.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class AppProp {

	private static Logger log = Logger.getLogger(AppProp.class);

	private static AppProp instance = null;
	private static byte[] justForLock = new byte[1];

	private Properties prop = null;
	private boolean fileLoaded = false;

	public static AppProp getInstance() {
		if (instance == null) {
			synchronized (justForLock) {
				if (instance == null) {
					instance = new AppProp();
				}
			}
		}
		return instance;
	}

	private AppProp() {
		populateProperties(AppConstant.DIR_PATH + File.separator
				+ "app-config.properties");
	}

	public Properties getProperties() {
		return prop;
	}

	public void populateProperties(String path) {
		log.info("PROPERTY FILE PATH IS " + path);
		if (!this.fileLoaded) {
			if ((path != null) && (!"".equalsIgnoreCase(path.trim()))) {
				this.prop = new Properties();
				try {
					final File propertiesFile = new File(path);
					loadPropertiesFile(propertiesFile);
					this.fileLoaded = true;

				} catch (FileNotFoundException e) {
					log.warn("File Not Found:" + e.getMessage(), e);
				} catch (IOException e) {
					log.warn("I/O Exception:" + e.getMessage(), e);
				}
			} else {
				log.warn("Path Is Either null or blank...!!");
			}
		} else
			log.warn("Property File Is Already Loaded...!!");
	}

	private void loadPropertiesFile(File propertiesFile)
			throws FileNotFoundException, IOException {
		this.prop.load(new FileInputStream(propertiesFile));
	}

	public String getProperty(String name) {
		String propertyValue = null;
		if (this.prop != null)
			propertyValue = this.prop.getProperty(name);
		else {
			log.warn("property request found but propery object is not initialized yet");
		}
		return propertyValue;
	}

	public String getProperty(String name, String defaultValue) {
		String propertyValue = null;
		if (this.prop != null)
			propertyValue = this.prop.getProperty(name, defaultValue);
		else {
			log.warn("property request found but propery object is not initialized yet");
		}
		return propertyValue;
	}


}
