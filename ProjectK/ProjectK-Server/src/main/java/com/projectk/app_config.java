package com.projectk;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.backend.RetriveData;
import com.mongodb.Mongo;

@Configuration
public class app_config {
	
	@Bean
	@Qualifier("monogodb")
	public MongoOperations mongoInstance(){
		try {
			return new MongoTemplate(new Mongo(), "projectK");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Bean
	@Qualifier("retrive")
	public RetriveData retriveInstance(){
		return new RetriveData();
	}

}
