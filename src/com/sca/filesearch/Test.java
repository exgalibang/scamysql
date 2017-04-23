package com.sca.filesearch;

import java.io.File;

import com.alibaba.fastjson.JSONException;
import com.taobao.api.ApiException;

public class Test {
	public static void main (String[] args) {
		TextFileSearch tSearch=new TextFileSearch();
		tSearch.SearchKeyword(new File("E:\\test.txt"), "iu");;
		try {
			SmsSender.SendSms(null, null, tSearch.sum);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
