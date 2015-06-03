// This class contains all the database access.
// The instance of the class is either created in doPost or doGet method.

package com.backend.java;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.simple.JSONObject;

import com.search.bean.ResultBean;


public class SearchData {
	private Database db_access;
	private ToXml to_xml;
	private ResultBean r_bean;
	private static String[] values_label = { "product", "manufacture" };
	String[] table_names = { "gtin", "brand", "brand_owner_bsin", "brand_owner" };
	private String[] gtin = { "GTIN_CD", "GTIN_LEVEL_CD", "GCP_CD", "BSIN",
			"GPC_S_CD", "GPC_F_CD","GPC_C_CD","GPC_B_CD", "GTIN_NM", "PRODUCT_LINE",
			"M_G", "M_OZ", "M_ML", "M_FLOZ", "M_ABV", "M_ABW", "PKG_UNIT",
			"PKG_TYPE_CD", "REF_CD", "SOURCE", "IMG" };

	private String[] brand = { "BSIN", "BRAND_NM", "BRAND_TYPE_CD","BRAND_LINK" };

	private String[] brand_owner_bsin = { "BSIN", "OWNER_CD"};

	private String[] brand_owner = { "OWNER_CD", "OWNER_NM", "OWNER_LINK",
			"OWNER_WIKI_EN"};
	
	private HashMap<String, String[]> getColumnMap(){
		HashMap<String, String[]> hash_map_columns = new HashMap<String, String[]>();
		
		hash_map_columns.put(table_names[0], gtin);
		hash_map_columns.put(table_names[1], brand);
		hash_map_columns.put(table_names[2], brand_owner_bsin);
		hash_map_columns.put(table_names[3], brand_owner);
		return hash_map_columns;
		
	}

	/*********************************************************************************************/
	public SearchData() {
		db_access = new Database();
		to_xml = new ToXml();
		r_bean = new ResultBean();
	}

	/*********************************************************************************************/
	/*
	 * Input :- bar_code
	 * Output:- Json object of the product details and manufacturer details 
	 * 
	 * */
	public JSONObject getInfo(String bar_code) { 
		String xml = null;
		ArrayList<ResultSet> rs_list = db_access.getData(bar_code);
		Iterator<ResultSet> list_iterator = rs_list.iterator();
		JSONObject final_json = new JSONObject();

		int label_index = 0;
		while (list_iterator.hasNext()) {
			if (label_index < 2) {
				ResultSet rs = list_iterator.next();
				JSONObject json_search_values = new JSONObject();
				try {
					ResultSetMetaData meta_data_rs = rs.getMetaData();
					int column_count = meta_data_rs.getColumnCount();
					//System.out.println("Column count = " + column_count);
					if (db_access.rowCount(rs) > 0) {
						while (rs.next()) {
							for (int i = 1; i <= column_count; i++) {
								if (i <= column_count) {
									String key = meta_data_rs.getColumnName(i);
									String value = String.valueOf(rs.getObject(rs.findColumn(key)));
									json_search_values.put(key, value);
								}
							}

						}
					} else {
						for (int i = 1; i <= column_count; i++) {
							if (i <= column_count) {
								
								String key = meta_data_rs.getColumnName(i);
								if(key.equalsIgnoreCase("OWNER_CD")){
									String value = String.valueOf(db_access.getOwner_cd());
									json_search_values.put(key, value);
								}else{
									String value = "";
									json_search_values.put(key, value);
								}
							}
						}
					}
					final_json.put(values_label[label_index],json_search_values);
					rs.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				label_index++;
			}
		}
		//System.out.println(final_json.toString());
		return final_json;

	}

	/*********************************************************************************************/
	/*
	 * Input :- HashMap of the product details that should be add when the product is not found and bsin.
	 * Output:- Returns a string whether the insert was successful. 
	 * 
	 * */
	public String insertInfo(HashMap<String, HashMap<String, String>> val_insert_map,String bsin) {
		
		Iterator<String> key_set = val_insert_map.keySet().iterator();
		ArrayList<Integer>result_list=new ArrayList<Integer>();
		
		while (key_set.hasNext()) {
			
			String key = key_set.next();
			String[] column_array=getColumnMap().get(key);
			System.out.println("Key - "+key);
			ArrayList<String> insert_values = arrayListInitialize(column_array.length);
			System.out.println("Search Data size " + column_array.length);
			
			HashMap<String, String> val_col_map = val_insert_map.get(key);
			Iterator<String> key_col_map = val_col_map.keySet().iterator();
			while (key_col_map.hasNext()) {
				String key_value = key_col_map.next();
				String value = "";
				int i = 0;
				if (!key_value.equalsIgnoreCase("BSIN")) {
					value = val_col_map.get(key_value);
					for (i = 0; i < column_array.length; i++) {
						if (key_value.equalsIgnoreCase(column_array[i])) {
							break;
						}
					}
				} else {
					for (i = 0; i < column_array.length; i++) {
						if (key_value.equalsIgnoreCase(column_array[i])&&(column_array[i].equalsIgnoreCase("BSIN"))) {
							break;
						}
					}
					value = bsin;
				}
				insert_values.set(i, value);
			}
			result_list.add(db_access.addData(insert_values,key));
			insert_values.clear();
			System.out.println("------------------------------------------");
		}
		String result="";
		for(int index=0;index<result_list.size();index++){
			if(result_list.get(index)<=0){
				result="Insert Failed";
			}else{
				result= "Insert Success";
			}
		}
		return result;
	}

	/*********************************************************************************************/
	/*
	 * Input :- table name to find out the meta data
	 * Output:- Hash map with the column name and data type.
	 * 
	 * */
	public HashMap<String,String> getMetaData(String table_name) {
		return db_access.getMetaData(table_name);
	}

	/*********************************************************************************************/
	public ArrayList<String> arrayListInitialize(int size) {
		ArrayList<String> array_list = new ArrayList<String>(size);
		for (int i = 0; i <size; i++) {
			array_list.add("");
		}
		return array_list;
	}

	/*********************************************************************************************/
	public String getBsin(String bar_code){
		return db_access.getBsin(bar_code);
	}
	/*********************************************************************************************/

}
