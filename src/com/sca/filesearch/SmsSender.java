package com.sca.filesearch;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import com.alibaba.fastjson.JSONException;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SmsSender {
	public static void SendSms (String operator,String time,int sum) throws JSONException, ApiException {
		String rusult = null;
		//官网的url，正式环境http请求地址
		String url = "http://gw.api.taobao.com/router/rest";
		//TOP分配给应用的AppKey
		String appkey = "23730811";
		//TOP分配给应用的AppSecret
		String secret = "56cd5dfd14c693f1a3b12bf24d5927ec";	
		String name = "卢也";
		/*String operator = "啦啦啦";
		String time = "2017/4/2/19:50";
		String sum = "10";*/
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		//req.setExtend(alarmname);
		req.setSmsType("normal");
		req.setSmsFreeSignName("个人毕业设计");
		req.setSmsParamString("{\"name\":\""+name+"\",\"operator\":\""+operator+"\",\"time\":\""+time+"\",\"sum\":\""+sum+"\"}");
		req.setRecNum("18390928897");
		req.setSmsTemplateCode("SMS_59700102");
		try{
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			System.out.println(rsp.getBody());
			rusult = rsp.getSubMsg();
		} catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(rusult);
		System.out.println(sum);
		
	}
	

}

