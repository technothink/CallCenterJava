package com.technothink.speech.core;

import org.springframework.data.mongodb.core.MongoOperations;

public interface DBCore {

	public void saveData(MongoOperations mongoTemplate,Object object);
}
