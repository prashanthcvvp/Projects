package com.projectk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.RetriveData;

@RestController
public class MongoController {

	@Autowired
	private @Qualifier("retrive") RetriveData retrive_data;

	@RequestMapping("/update_place")
	public String updatePlaces() {
		return retrive_data.getValuesFromDruple();
	}
	
	@RequestMapping("/get_places")
	public String getPlaces() {
		return retrive_data.sendValuesToClient();
	}


}
