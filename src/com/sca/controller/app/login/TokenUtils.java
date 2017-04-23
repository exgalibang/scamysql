package com.sca.controller.app.login;

import com.sca.util.MD5;

/**
 * Token操作类
 */
public class TokenUtils {
	//用于app与服务端交互的私钥字符串，可自定义，必须与app客户端的一致
    private static final String privateKey = "scaas34ljfrsja@#8$%dfkl;js&4*daklfjsdl;akfjsa342";
    /**
     * 获取md5加密后的token
     * @param paraname,dateTime
     * @return
     */
    public static String getToken(String paraname,String dateTime) {
    	return MD5.md5(paraname + privateKey + dateTime);
    }
    /**
     * 检验传过来的token是否与当前登录用户匹配
     * @param token
     * @param paraname,dateTime
     * @return
     */
    public static boolean validToken(String token, String paraname,String dateTime) {
        String confirm = getToken(paraname,dateTime);
        if (confirm.equals(token)) {
            return true;
        } else {
            return false;
        }
    }
 
}