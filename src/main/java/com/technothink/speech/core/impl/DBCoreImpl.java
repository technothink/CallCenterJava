package com.technothink.speech.core.impl;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;

import com.technothink.speech.core.DBCore;

public class DBCoreImpl implements DBCore {

	private static final Logger log = Logger.getLogger(DBCoreImpl.class);

	public void saveData(MongoOperations mongoTemplate, Object object) {
		try {
			mongoTemplate.save(object);
		} catch (Throwable th) {
			log.error("Unable to save data into mongo db ", th);
		}
	}
	

}
