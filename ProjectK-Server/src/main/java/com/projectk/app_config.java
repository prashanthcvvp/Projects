package com.projectk;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class app_config {

	@Bean
	@Qualifier("monogodb")
	public MongoOperations mongoInstance() throws Exception {
		Mongo mongo=new MongoClient(new MongoClientURI(System.getenv("OPENSHIFT_MONGODB_DB_URL")));
		String databaseName = System.getenv("OPENSHIFT_APP_NAME");
		MongoTemplate mongoTemplate = new MongoTemplate(mongo,databaseName);
		return mongoTemplate;
	}
//	public MongoOperations mongoInstance() throws Exception {
//		return new MongoTemplate(new Mongo(), "projectK");
//	}
	@Bean
	@Qualifier("retrive")
	public RetriveData retriveInstance() {
		return new RetriveData();
	}

}
