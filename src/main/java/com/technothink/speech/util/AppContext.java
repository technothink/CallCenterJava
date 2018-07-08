package com.technothink.speech.util;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppContext {

	private static final Logger log = Logger.getLogger(AppContext.class);

	private ApplicationContext applicationContext = null;
	private static AppContext appContext = null;

	private AppContext() {
		try {
			applicationContext = new FileSystemXmlApplicationContext("SpringConfig.xml");
		} catch (Throwable th) {
			log.error("Erorr occurred while loading spring context file ", th);
		}

	}

	public static AppContext getInstance() {

		if (null == appContext) {
			synchronized (AppContext.class) {
				if (null == appContext) {
					appContext = new AppContext();
				}

			}
		}
		return appContext;
	}

	public ApplicationContext getContext() {
		return applicationContext;
	}
}
