package com.backend.java;


import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.search.bean.ResultBean;

public class ToXml {
	public static String ToXmlString(ArrayList<HashMap<String, String>> values) {
		String xml = null;		
		try {
			//
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XMLEncoder encoder = new XMLEncoder(out);
			encoder.writeObject(values);
			encoder.close();
			xml = out.toString();
			
		} catch (Exception e) {
			e.printStackTrace();

		}
		return xml;
	}
}
