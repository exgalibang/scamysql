package com.sca.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sca.entity.system.User;
import com.sca.util.Const;
import com.sca.util.Jurisdiction;
/**
 * 
* 类名称：LoginHandlerInterceptor.java
* 类描述： 
* @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{
	private static Logger loggers = LogManager.getLogger(LoginHandlerInterceptor.class.getName());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String path = request.getServletPath();
		//客户端请求ip地址
		String requestUrl = request.getRemoteAddr();
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
        	 loggers.info("Request RemoteAddress-----：" + requestUrl + "-----ServletPath：" + path);
			//shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();  
			Session session = currentUser.getSession();
			User user = (User)session.getAttribute(Const.SESSION_USER);
			if(user!=null){
				path = path.substring(1, path.length());
				boolean b = Jurisdiction.hasJurisdiction(path);
				if(!b){
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}
				return b;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
				//return true;
			}
		}
	}
	
}
