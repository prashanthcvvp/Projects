package com.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.backend.java.JsonBackend;
import com.backend.java.SearchData;
import com.search.bean.ResultBean;

/**
 * Servlet implementation class DatabasePublisher
 */
@WebServlet("/Search")
public class DatabasePublisher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/*********************************************************************************************/
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DatabasePublisher() {
		super();
	}

	/*********************************************************************************************/
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// used to search bar-codes in the database.
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	
		String browser = request.getParameter("html");
		boolean ifBrowser = Boolean.valueOf(browser);

		String bar_code = request.getParameter("name");

		SearchData search_data = new SearchData();
		JSONObject final_json = search_data.getInfo(bar_code);
		JSONObject product_json= (JSONObject) final_json.get("product");
		String gtin_cd = (String) product_json.get("GTIN_CD");
		if (!gtin_cd.equals("")) { // to chekc if the product map is null
			sendResponse(response, request, final_json.toString(),ifBrowser, true);
		} else {
			sendResponse(response, request, final_json.toString(),ifBrowser, false);
		}
		
	}

	/*********************************************************************************************/
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// This function is called when data add request is triggered
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String[] values_label = { "product", "manufacture" };
		String[] table_names = { "gtin","brand","brand_owner_bsin","brand_owner" };
		String bsin = null;
		
		SearchData search_data = new SearchData();
		JsonBackend json_manipulation = new JsonBackend();
		
				
		String product_str=request.getParameter("details").toString();
		try {
			JSONParser parser = new JSONParser();
			Object obj= parser.parse(product_str);
			JSONArray all_values_jarray = (JSONArray)obj;
			
			String browser = (String)((JSONObject)all_values_jarray.get(3)).get("html");
			boolean ifBrowser = Boolean.valueOf(browser);
			
			String bar_code = (String)((JSONObject)all_values_jarray.get(2)).get("gtin_cd");
			bsin=search_data.getBsin(bar_code);
			
			JSONObject final_values_json = json_manipulation.concatJson(all_values_jarray.get(0), all_values_jarray.get(1));
			System.out.println(final_values_json.toString());
			
			int table_length=0;
			if(search_data.getBsin(bar_code).equalsIgnoreCase(bar_code.substring(1,7))){
				table_length=table_names.length;
			}else {
				table_length=1;
			}
		
			HashMap<String, HashMap<String, String>> values_map_insert = new HashMap<String, HashMap<String, String>>();
		
			if (ifBrowser) {
				
				for (int i = 0; i < table_length ; i++) {
					HashMap<String,String> col_name = search_data.getMetaData(table_names[i]);
					Iterator<String> col_iterator = col_name.keySet().iterator();
					HashMap<String, String> col_value = new HashMap<String, String>();
					while (col_iterator.hasNext()) {
						String key = col_iterator.next();
						//String value = "";
						String value = (String)final_values_json.get(key);
						col_value.put(key, value);
					}
					values_map_insert.put(table_names[i], col_value);
					col_name.clear();
				}
				String result = search_data.insertInfo(values_map_insert,bsin);
				PrintWriter pw = response.getWriter();
				pw.write(result);
				
			} else if (!ifBrowser) {

			}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

	/*********************************************************************************************/
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

	/*********************************************************************************************/
	void sendResponse(HttpServletResponse response, HttpServletRequest request,String payload, boolean ifBrowser, boolean ifData)
			throws ServletException, IOException {
		if (ifBrowser) {
			if (ifData) { // when the product data is available then the it loads the success.jsp page 
				RequestDispatcher dispatcher = request.getRequestDispatcher("success.jsp");
				request.setAttribute("name", payload);
				dispatcher.forward(request, response);
			} else { //When the product is not available in the database it lets the user add the product.
				RequestDispatcher dispatcher = request.getRequestDispatcher("AddData.jsp");
				request.setAttribute("name", payload);
				dispatcher.forward(request, response);
			}
		} else if (!ifBrowser) {
			System.out.println(payload);
			response.getOutputStream().write(payload.getBytes());
		}
	}
	/*********************************************************************************************/
}
