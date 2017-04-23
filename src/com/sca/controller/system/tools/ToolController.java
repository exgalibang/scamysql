package com.sca.controller.system.tools;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.sca.controller.base.BaseController;
import com.sca.httpClient.HttpClientUtils;
import com.sca.httpClient.ServerInfoStatus;
import com.sca.util.AppUtil;
import com.sca.util.Const;
import com.sca.util.MapDistance;
import com.sca.util.PageData;
import com.sca.util.PathUtil;
import com.sca.util.TwoDimensionCode;

/** 
 * 类名称：ToolController
 * @version
 */
@Controller
@RequestMapping(value="/tool")
public class ToolController extends BaseController {
	
	
	/**
	 * 去接口测试页面
	 */
	@RequestMapping(value="/interfaceTest")
	public ModelAndView editEmail() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/interfaceTest");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 *	接口内部请求
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/severTest")
	@ResponseBody
	public Object severTest(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success",str = "",rTime="";
		try{
			long startTime = System.currentTimeMillis(); 					//请求起始时间_毫秒
			URL url = new URL(pd.getString("serverUrl"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(pd.getString("requestMethod"));		//请求类型  POST or GET	
			// 填入apikey到HTTP header
			if(StringUtils.isNotBlank(pd.getString("apikey")))
				connection.setRequestProperty("apikey",  pd.getString("apikey"));
	        connection.connect();
	        InputStream is = connection.getInputStream();  //Charset.forName("UTF-8")
	        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			long endTime = System.currentTimeMillis(); 						//请求结束时间_毫秒
			String temp = "";
			while((temp = in.readLine()) != null){ 
				str = str + temp;
			}
			rTime = String.valueOf(endTime - startTime); 
		}
		catch(Exception e){
			errInfo = "error";
		}
		map.put("errInfo", errInfo);	//状态信息
		map.put("result", str);			//返回结果
		map.put("rTime", rTime);		//服务器请求时间 毫秒
		return AppUtil.returnObject(new PageData(), map);
	}
	/**
	 *	接口内部代理请求测试
	 * @param 
	 * @throws Exception
	 */
	@RequestMapping(value="/apiTest",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object testRestfulAPI() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success",rTime="";
		ServerInfoStatus statue = new ServerInfoStatus();
		try{
			StringBuffer postURL = new StringBuffer(pd.getString("serverUrl"));
			long startTime = System.currentTimeMillis(); 	//请求起始时间_毫秒
			String postMethod = pd.getString("requestMethod");
			if("GET".equalsIgnoreCase(postMethod)) {
				if(StringUtils.isNotBlank(pd.getString("keyWords")))
					postURL.append("&keyWords=" + pd.getString("keyWords")); 
				if(StringUtils.isNotBlank(pd.getString("showCount")))
					postURL.append("&showCount=" + pd.getString("showCount")); 
				if(StringUtils.isNotBlank(pd.getString("currentPage")))
					postURL.append("&currentPage=" + pd.getString("currentPage")); 
				statue = HttpClientUtils.getServerInfoStatus(postURL.toString(),pd.getString("apikey"));
			} else if("POST".equalsIgnoreCase(postMethod)) {
				if(StringUtils.isNotBlank(pd.getString("jsonData"))) {
					statue = HttpClientUtils.postServerJSONStatus(postURL.toString(),pd.getString("jsonData"),pd.getString("apikey"));
				}
			} else if("PUT".equalsIgnoreCase(postMethod)) {
				if(StringUtils.isNotBlank(pd.getString("jsonData"))) {
					statue = HttpClientUtils.putServerJSONStatus(postURL.toString(),pd.getString("jsonData"),pd.getString("apikey"));
				}
			} else if("DELETE".equalsIgnoreCase(postMethod)) {
				statue = HttpClientUtils.deleteServerInfoStatus(postURL.toString(),pd.getString("apikey"));
			}
			long endTime = System.currentTimeMillis(); 						//请求结束时间_毫秒
			rTime = String.valueOf(endTime - startTime); 
			System.out.println("postURL--------: " + postURL + "----------post time: " + rTime);
		} catch(Exception e){
			errInfo = "error";
			e.printStackTrace();
		} 
		JSONObject json=new JSONObject();
		json.put("errInfo", errInfo);	//状态信息
		json.put("result", statue.getServerInfo());			//返回结果
		json.put("rTime", rTime);		//服务器请求时间 毫秒
		
		System.out.println(json);
		return json.toJSONString();
//		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 发送邮件页面
	 */
	@RequestMapping(value="/goSendEmail")
	public ModelAndView goSendEmail() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/email");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 二维码页面
	 */
	@RequestMapping(value="/goTwoDimensionCode")
	public ModelAndView goTwoDimensionCode() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/twoDimensionCode");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 *	生成二维码
	 * @param args
	 * @throws Exception
	 */
	@RequestMapping(value="/createTwoDimensionCode")
	@ResponseBody
	public Object createTwoDimensionCode(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success", encoderImgId = this.get32UUID()+".png"; //encoderImgId此处二维码的图片名
		String encoderContent = pd.getString("encoderContent");				//内容
		if(null == encoderContent){
			errInfo = "error";
		}else{
			try {
				String filePath = PathUtil.getClasspath() + Const.FILEPATHTWODIMENSIONCODE + encoderImgId;  //存放路径
				TwoDimensionCode.encoderQRCode(encoderContent, filePath, "png");							//执行生成二维码
			} catch (Exception e) {
				errInfo = "error";
			}
		}
		map.put("result", errInfo);						//返回结果
		map.put("encoderImgId", encoderImgId);			//二维码图片名
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 *	解析二维码
	 * @param args
	 * @throws Exception
	 */
	@RequestMapping(value="/readTwoDimensionCode")
	@ResponseBody
	public Object readTwoDimensionCode(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success",readContent="";
		String imgId = pd.getString("imgId");//内容
		if(null == imgId){
			errInfo = "error";
		}else{
			try {
				String filePath = PathUtil.getClasspath() + Const.FILEPATHTWODIMENSIONCODE + imgId;  //存放路径
				readContent = TwoDimensionCode.decoderQRCode(filePath);//执行读取二维码
			} catch (Exception e) {
				errInfo = "error";
			}
		}
		map.put("result", errInfo);						//返回结果
		map.put("readContent", readContent);			//读取的内容
		return AppUtil.returnObject(new PageData(), map);
	}
	
	
	/**
	 * 多级别树页面
	 */
	@RequestMapping(value="/ztree")
	public ModelAndView ztree() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/ztree");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 地图页面
	 */
	@RequestMapping(value="/map")
	public ModelAndView map() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/map");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 获取地图坐标页面
	 */
	@RequestMapping(value="/mapXY")
	public ModelAndView mapXY() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/mapXY");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 *	根据经纬度计算距离
	 * @param args
	 * @throws Exception
	 */
	@RequestMapping(value="/getDistance")
	@ResponseBody
	public Object getDistance(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success",distance="";
		try {
			distance  = MapDistance.getDistance(pd.getString("ZUOBIAO_Y"),pd.getString("ZUOBIAO_X"),pd.getString("ZUOBIAO_Y2"),pd.getString("ZUOBIAO_X2"));
		} catch (Exception e) {
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		map.put("distance", distance);			//距离
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 打印测试页面
	 */
	@RequestMapping(value="/printTest")
	public ModelAndView printTest() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/printTest");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 打印预览页面
	 */
	@RequestMapping(value="/printPage")
	public ModelAndView printPage() throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/tools/printPage");
		mv.addObject("pd", pd);
		return mv;
	}
	
}
