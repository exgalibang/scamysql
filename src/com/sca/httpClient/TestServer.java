package com.sca.httpClient;

import java.util.ArrayList;
import java.util.List;

import com.sca.controller.app.login.AppUser;

/**
 * 
 * @ClassName：TestServer     
 * @Description: restful接口服务测试类     
 *
 */
public class TestServer {
	/**
	 * 
	 * get 获取数据使用
	 * @param  url:  http://localhost:8080/uicsr/restfulUser/listAll
	 * @return ServerInfoStatus
	 */
	public static ServerInfoStatus getInfo(String url,String apiKey) {
		ServerInfoStatus statue = HttpClientUtils.getServerInfoStatus(url,apiKey);
		return statue;
	}
	
	/**
	 * 
	 * post 创建数据使用
	 * @param  url:  http://127.0.0.1:8080/uicsr/restfulUser/create
	 * @param  obj 被创建对象，json参数，是jsonObject形式
	 * @return ServerInfoStatus
	 */
	public static ServerInfoStatus postInfo(String url,Object obj) {
		ServerInfoStatus statue = HttpClientUtils.postServerInfoStatus(url,obj);
		return statue;
	}
	
	/**
	 * 
	 * put 修改数据使用
	 * @param  url:  http://127.0.0.1:8080/uicsr/restfulUser/(id)
	 * @param  obj 被创建对象，json参数，是jsonObject形式
	 * @return ServerInfoStatus
	 */
	public static ServerInfoStatus putInfo(String url,Object obj) {
		ServerInfoStatus statue = HttpClientUtils.putServerInfoStatus(url,obj);
		return statue;
	}
	
	/**
	 * 
	 * delete 删除数据使用
	 * @param  url:  http://127.0.0.1:8080/uicsr/restfulUser/(id)
	 * @param  obj 被创建对象，json参数，是jsonObject形式
	 * @return ServerInfoStatus
	 */
	public static ServerInfoStatus deleteInfo(String url,Object obj) {
		ServerInfoStatus statue = HttpClientUtils.deleteServerInfoStatus(url,obj);
		return statue;
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("file.encoding"));
		String url1 = "http://localhost:8080/uicsr/v1/restfulSemantics/listPage";
		String url2 = "http://localhost:8080/uicsr/v1/restfulSemantics/3";
//		ServerInfoStatus info = getInfo(url1);
//		//测试远程接口，输出返回的json数据
//		System.out.println(info.getServerInfo());
//		System.out.println(info.getServerStatus());
		List<AppUser> users = new ArrayList<AppUser>();
		AppUser appUser = new AppUser();
		appUser.setNAME("测试");
		appUser.setUSERNAME("ceshi1");
		appUser.setPASSWORD("1123456");
		String url3 = "http://localhost:8070/hmzmysql/restful/appuser/listpage";
		ServerInfoStatus info1 = getInfo(url3,"");
		//测试远程接口，输出返回的json数据
		System.out.println(info1.getServerInfo());
		System.out.println(info1.getServerStatus().getStatusCode());
		System.out.println(info1.getServerStatus().getReasonPhrase());
		//测试读取config/下properties配置文件中的属性
		//System.out.println(DefaultConfigHolder.getProperty("redis.host"));
	}

}
