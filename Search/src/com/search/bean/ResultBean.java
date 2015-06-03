package com.search.bean;

import java.io.Serializable;

public class ResultBean implements Serializable {
	String bsin,brand_nm,brand_type_cd,brand_link,insert_status;
	boolean ifData=false;
	/*********************************************************************************************/
	public boolean isIfData() {
		return ifData;
	}	
	/*********************************************************************************************/
	public void setIfData(boolean ifData) {
		this.ifData = ifData;
	}
	/*********************************************************************************************/
	public String getInsert_status() {
		return insert_status;
	}
	/*********************************************************************************************/
	public void setInsert_status(String insert_status) {
		this.insert_status = insert_status;
	}
	/*********************************************************************************************/
	public ResultBean() {
		super();
	}
	/*********************************************************************************************/
	public String getBsin() {
		return bsin;
	}
	/*********************************************************************************************/
	public void setBsin(String bsin) {
		this.bsin = bsin;
		
	}
	/*********************************************************************************************/
	public String getBrand_nm() {
		return brand_nm;
	}
	/*********************************************************************************************/
	public void setBrand_nm(String brand_nm) {
		this.brand_nm = brand_nm;
	}
	/*********************************************************************************************/
	public String getBrand_type_cd() {
		return brand_type_cd;
	}
	/*********************************************************************************************/
	public void setBrand_type_cd(String brand_type_cd) {
		this.brand_type_cd = brand_type_cd;
	}
	/*********************************************************************************************/
	public String getBrand_link() {
		return brand_link;
	}
	/*********************************************************************************************/
	public void setBrand_link(String brand_link) {
		this.brand_link = brand_link;
	}
	/*********************************************************************************************/
}
