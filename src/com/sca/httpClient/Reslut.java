package com.sca.httpClient;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.sca.util.PageData;

/**
 * 
 * @ClassName：Reslut     
 * @Description: 调用接口返回对象    
 *
 */
public class Reslut implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 接口返回状态1为成功，0为失败
	 */
	private int reslut_code;        	
	private String reslut_message;  	//接口返回信息
	private int totalResult;			//接口返回数据总记录数
	/**
	 * 接口返回单条数据,设置此属性
	 */
	private Map data;	
	/**
	 * 接口返回多条数据，设置此属性
	 */
	private List<PageData> dataList;    
	
	public int getReslut_code() {
		return reslut_code;
	}
	/**
	 * 接口返回状态1为成功，0为失败
	 */
	public void setReslut_code(int reslut_code) {
		this.reslut_code = reslut_code;
	}
	public String getReslut_message() {
		return reslut_message;
	}
	public void setReslut_message(String reslut_message) {
		this.reslut_message = reslut_message;
	}
	public Map getData() {
		return data;
	}
	/**
	 * 接口返回单条数据,设置此属性
	 */
	public void setData(Map data) {
		this.data = data;
	}
	public int getTotalResult() {
		return totalResult;
	}
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	public List<PageData> getDataList() {
		return dataList;
	}
	/**
	 * 接口返回多条数据集合，设置此属性
	 */
	public void setDataList(List<PageData> dataList) {
		this.dataList = dataList;
	}

}
