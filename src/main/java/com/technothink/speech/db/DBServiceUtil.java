package com.technothink.speech.db;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;

import com.technothink.speech.core.DBCore;
import com.technothink.speech.core.impl.DBCoreImpl;
import com.technothink.speech.util.AppContext;

public class DBServiceUtil {

	private static final Logger log = Logger.getLogger(DBServiceUtil.class);
	private static DBServiceUtil dbService;
	private MongoOperations operations;
	private DBCore dbCore;

	private DBServiceUtil() {

		try {
			operations = (MongoOperations) AppContext.getInstance().getContext().getBean("mongoTemplate");
			dbCore = new DBCoreImpl();

		} catch (Throwable th) {
			log.error("Unable to load mongo template  ", th);
		}
	}

	public static DBServiceUtil getInstance() {
		if (null == dbService) {
			synchronized (DBServiceUtil.class) {

				if (null == dbService) {
					dbService = new DBServiceUtil();
				}
			}
		}
		return dbService;
	}

	public void saveData(Object object) {
		dbCore.saveData(operations, object);
	}

//	public static void main(String[] args) {
//
//		// For XML
//		ApplicationContext ctx = new FileSystemXmlApplicationContext("SpringConfig.xml");
//
//		// For Annotation
//		/*
//		 * ApplicationContext ctx = new AnnotationConfigApplicationContext(
//		 * SpringMongoConfig.class);
//		 */
//		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
//
//		// save
//		// mongoOperation.save(user);
//
//		// now user object got the created id.
//		System.out.println("1. user : ");
//
//		// query to search user
//		Query searchUserQuery = new Query(Criteria.where("description").is("database"));
//
//		// find the saved user again.
//		AWSTranscript savedUser = mongoOperation.findOne(searchUserQuery, AWSTranscript.class);
//		System.out.println("2. find - savedUser : " + savedUser);
//
//		/*
//		 * // update password mongoOperation.updateFirst(searchUserQuery,
//		 * Update.update("password", "new password"), User.class);
//		 * 
//		 * // find the updated user object User updatedUser =
//		 * mongoOperation.findOne(searchUserQuery, User.class);
//		 * 
//		 * System.out.println("3. updatedUser : " + updatedUser);
//		 * 
//		 * // delete mongoOperation.remove(searchUserQuery, User.class);
//		 * 
//		 * // List, it should be empty now. List<User> listUser =
//		 * mongoOperation.findAll(User.class); System.out.println("4. Number of user = "
//		 * + listUser.size());
//		 */
//
//	}

}