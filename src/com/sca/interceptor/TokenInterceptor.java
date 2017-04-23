package com.sca.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sca.controller.app.login.AppMemory;

/**
 * Token拦截器，验证app用户是否登录
 */
 public class TokenInterceptor extends HandlerInterceptorAdapter {
	 private static Logger loggers = LogManager.getLogger(TokenInterceptor.class.getName());
     @Autowired
     private AppMemory appMemory;
 
     private List<String> allowList; // 放行的URL列表
 
//     private static final PathMatcher PATH_MATCHER = new AntPathMatcher();
 
     @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         // 判断请求的URI是否允许放行，如果不允许则校验请求的token信息
    	 request.setCharacterEncoding("UTF-8");
    	 response.setCharacterEncoding("UTF-8");
    	 //客户端请求ip地址
 		 String requestUrl = request.getRemoteAddr();
         if (!checkAllowAccess(request.getServletPath())) {
        	 loggers.info("Restful RemoteAddress-----：" + requestUrl + "-----ServletPath：" + request.getServletPath());
             // 检查请求的token值是否为空
             String token = getTokenFromRequest(request);
             response.setContentType(MediaType.APPLICATION_JSON_VALUE);
             response.setHeader("Cache-Control", "no-cache, must-revalidate");
             if (StringUtils.isEmpty(token)) {
            	 response.getWriter().flush();
                 response.getWriter().write("{\"reslut_code\":\"1\",\"result\":\"Token is null\"}");
                 response.getWriter().close();
                 return false;
             }
             if (!appMemory.checkLoginInfo(token)) {
            	 response.getWriter().flush();
                 response.getWriter().write("{\"reslut_code\":\"1\",\"result\":\"Session已过期，请重新登录\"}");
                 response.getWriter().close();
                 return false;
             }
//             ThreadTokenHolder.setToken(token); // 保存当前token，用于Controller层获取登录用户信息
         } 
         return super.preHandle(request, response, handler);
     }
 
     /**
      * 检查URI是否放行
      * 
      * @param URI
      * @return 返回检查结果
      */
     private boolean checkAllowAccess(String URI) {
         if (!URI.startsWith("/")) {
             URI = "/" + URI;
         }
         if(allowList.contains(URI))
        	 return true;
         /*for (String allow : allowList) {
             if (PATH_MATCHER.match(allow, URI)) {
                 return true;
             }
         }*/
         return false;
     }
 
     /**
      * 从请求信息中获取token值
      * 
      * @param request
      * @return token值
      */
     private String getTokenFromRequest(HttpServletRequest request) {
         // 默认从header里获取token值
         String token = request.getHeader("token");
         if (StringUtils.isEmpty(token)) {
             // 从请求信息中获取token值
             token = request.getParameter("token");
         }
         return token;
     }
 
     public List<String> getAllowList() {
         return allowList;
     }
 
     public void setAllowList(List<String> allowList) {
         this.allowList = allowList;
     }
 }
