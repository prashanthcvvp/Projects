/* This class performs all the database operation such as insert,search.
 * 
 * */

package com.backend.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database {
	private Connection database_conn;

	/*********************************************************************************************/
	public Database() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			database_conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/search_engine", "prashanth",
					"pradeep89!");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/*********************************************************************************************/
	/*
	 * Input :- bar_code
	 * Output:- Returns the array list of the result set of the queries of the manufacturer details and product details. 
	 * 
	 * */
	public ArrayList<ResultSet> getData(String predicate) { 
		String sql_select = "SELECT * FROM gtin WHERE GTIN_CD=?";
		ArrayList<ResultSet> resultset_list = new ArrayList<ResultSet>();
		ResultSet rs_product = null;
		ResultSet rs_manufacture = null;
		try {
			PreparedStatement psmt = database_conn.prepareStatement(sql_select);
			psmt.setString(1, predicate);
			rs_product = psmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String bsin=getBsin(predicate);
			String sql_manufacture = "SELECT * FROM brand natural join brand_owner_bsin natural join brand_owner WHERE BSIN=?";
			PreparedStatement psmt = database_conn.prepareStatement(sql_manufacture);
			psmt.setString(1, bsin);
			rs_manufacture = psmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		resultset_list.add(rs_product);
		resultset_list.add(rs_manufacture);

		return resultset_list;
	}

	/*********************************************************************************************/
	public int rowCount(ResultSet rs) {
		int i = 0;
		try {
			while (rs.next()) {
				i++;
			}			rs.beforeFirst();
		} catch (SQLException e) {
		}
		return i;
	}

	/*********************************************************************************************/
	public HashMap<String, String> getMetaData(String table_name) {
		HashMap<String, String> map_col_nm = new HashMap<String, String>();
		String desc_sql = "describe " + table_name;
		PreparedStatement psmt;
		try {
			psmt = database_conn.prepareStatement(desc_sql);
			ResultSet rs_desc = psmt.executeQuery();
			while (rs_desc.next()) {
				map_col_nm.put(rs_desc.getString(1), rs_desc.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map_col_nm;
	}

	/*********************************************************************************************/
	public int getOwner_cd(){
		String select_str= "select MAX(OWNER_CD) from brand_owner";
		ResultSet rs =null;
		try{
		PreparedStatement psmt = database_conn.prepareStatement(select_str);
		rs= psmt.executeQuery();
		rs.next();
		return (rs.getInt(1)+1);
		}catch(SQLException e){e.printStackTrace();};
		return 0;
	}
	/*********************************************************************************************/
	/*
	 * Input :- Array list of values to be added to the database and the table name in which the data should be added.
	 * Output:- Returns the number of rows that been inserted. 
	 * 
	 * */
	public int addData(ArrayList<String> list_values, String table_name) {
		int row_insert =0;
		try {
			HashMap<String, String> map_col_name = getMetaData(table_name);
			StringBuilder query_builder = new StringBuilder("Insert into "+ table_name + " VALUES(");
			for (int i = 0; i < (list_values.size()); i++) {
				System.out.println(list_values.get(i));
				if (i < (list_values.size() - 1)) {
					query_builder.append("?,");
				} else {
					query_builder.append("?");
				}
			}
			query_builder.append(");");
			System.out.println(query_builder.toString());
			Iterator<String> list_values_iterator = list_values.iterator();
			PreparedStatement psmt = database_conn.prepareStatement(query_builder.toString());
			int psmt_index = 1;
			Iterator<String> map_col_nm_iterator = map_col_name.keySet().iterator();
			while (list_values_iterator.hasNext()) {
				String key = map_col_nm_iterator.next();
				System.out.println(key);
				String type = map_col_name.get(key);
				String value = list_values_iterator.next();
				if (type.equalsIgnoreCase("int")) {
					psmt.setInt(psmt_index, Integer.parseInt(value));
				} else {
					psmt.setString(psmt_index, value);
				}
				psmt_index++;

			}
			row_insert = psmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		list_values.clear();
		return row_insert;
	}

	/*********************************************************************************************/
	public String getBsin(String bar_code) {
		String sql_bind_bsin = "SELECT distinct BSIN FROM gtin WHERE GTIN_CD LIKE '"+ bar_code.substring(0, 7) + "%' && BSIN is not null";
		String bsin = "";
		try {
			PreparedStatement psmt = database_conn.prepareStatement(sql_bind_bsin);
			ResultSet rs = psmt.executeQuery();
			int row_cnt = rowCount(rs);

			if (row_cnt > 0) {
				rs.next();
				bsin = rs.getString(rs.findColumn("BSIN"));
			} else {
				bsin = bar_code.substring(1, 7);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bsin;
	}
}
