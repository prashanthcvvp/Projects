package com.projectk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.util.JSON;

public class RetriveData {

	@Autowired
	private @Qualifier("monogodb") MongoOperations mongoOps;

	
	public String getValuesFromDruple(){
		
		
		 Document doc;
		 int pages_count=0;
		 String url_str = "http://villinois.museum.state.il.us/jquery-map?page=";
		 StringBuilder str_builder = new StringBuilder();
		try {
			doc = Jsoup.connect("http://villinois.museum.state.il.us/jquery-map").timeout(0).get();
			 Elements pages = doc.getElementsByAttributeValueContaining("title", "Go to last page");
			 String[] str_split = pages.attr("href").split("=");
			 pages_count= Integer.parseInt(str_split[1]);
			 mongoOps.dropCollection("place");
				for (int i = 1; i <= pages_count; i++) {
					//str_builder.append("\n\n/----------------------Page "+String.valueOf(i)+"-----------------/ \n\n");
					String url_str_final = url_str+String.valueOf(i);
					Connection conn = Jsoup.connect(url_str_final);
					conn.timeout(0);
					Document document = conn.get();
					Elements title_doc = document.getElementsByClass("views-field-title");
					Elements body_doc = document.getElementsByClass("views-field-body");
					Elements geo_doc = document.getElementsByClass("views-field-field-geolocation");
					Elements tags_doc = document.getElementsByClass("views-field-field-tags");
					Elements topic_doc = document.getElementsByClass("views-field-field-topic");
					Elements time_doc = document.getElementsByClass("views-field-field-time");
					Elements location_doc = document.getElementsByClass("views-field-field-location");
					Elements region_doc = document.getElementsByClass("views-field-field-region");
					
					Iterator<Element> title_docs_it= title_doc.listIterator();
					Iterator<Element> body_docs_it= body_doc.listIterator();
					Iterator<Element> geo_docs_it= geo_doc.listIterator();
					Iterator<Element> tags_docs_it= tags_doc.listIterator();
					Iterator<Element> topic_docs_it= topic_doc.listIterator();
					Iterator<Element> time_docs_it= time_doc.listIterator();
					Iterator<Element> location_docs_it= location_doc.listIterator();
					Iterator<Element> region_docs_it= region_doc.listIterator();
					
					while(title_docs_it.hasNext()){
						Place place = new Place();
						place.setTitle(title_docs_it.next().text());
						place.setBody(body_docs_it.next().text());
						String geo_locations=geo_docs_it.next().text();
						if(geo_locations.length()>0){
							geo_locations= geo_locations.substring(8, (geo_locations.length()-2));
						}
						place.setGeo(geo_locations);
						place.setTags(tags_docs_it.next().text());
						place.setTopic(topic_docs_it.next().text());
						place.setTime(time_docs_it.next().text());
						place.setLocation(location_docs_it.next().text());
						mongoOps.insert(place);					
					}
				}	
				str_builder.append("Success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str_builder.toString();

	}
	
	public String sendValuesToClient(){
		StringBuilder str_builder = new StringBuilder();
		DBCollection places = mongoOps.getCollection("place");
		DBCursor cursor = places.find();
		JSON json = new JSON();
		String serialize = json.serialize(cursor);
		str_builder.append(serialize.toString()+"\n");
		return str_builder.toString();
	}

}
