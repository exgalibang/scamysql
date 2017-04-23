package com.sca.controller.app.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sca.controller.base.BaseController;
import com.sca.httpClient.Reslut;
import com.sca.service.system.appuser.AppuserService;
import com.sca.util.AppUtil;
import com.sca.util.DateUtil;
import com.sca.util.MD5;
import com.sca.util.PageData;
import com.sca.controller.app.login.AppMemory;
import com.sca.controller.app.login.TokenUtils;

/**
  * 会员登录-接口类 
 */
@Controller
@RequestMapping(value="/restful/appU")
public class AppLoginController extends BaseController {
	private static Logger loggers = LogManager.getLogger(AppLoginController.class.getName());
	@Resource(name="appuserService")
	private AppuserService appuserService;
	@Autowired
	private AppMemory appMemory;
	
	/**
	 * 客户端注册用户接口  
	 * 接口调用地址：http://localhost:8080/scamysql/appU/registerUser.do
	 * 接口测试用地址：http://localhost:8080/scamysql/appU/registerUser.do?mobileNumber=18545678988&password=123&mobileUserName=km31
	 * json字符串，格式为{"mobileNumber":"手机号码", mobileUserName":"姓名","password":"密码","smsCode":"短信随机码"}
	 * 【返回值】：返回JSON格式字符串，格式为：
	 * {“result":x,"userEeid":"y"}，x为调用结果，当注册成功时，y值为系统分配的唯一ee号码，
	 * 当注册失败时，y值为0，x返回值说明如下：
	 * x = 10：注册成功；
	 * x = 20：注册失败，注册手机号格式不正确；
	 * x = 21：注册失败，密码格式不正确；
	 * x = 29：注册失败，注册信息填写不完整
	 * x = 30：注册失败，手机号码已注册；
	 * x = 99：注册失败，未知原因
	 */
	@RequestMapping(value="/registerUser",produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> registerUser(){
		logBefore(logger, "APP客户端注册会员请求！");
		PageData pd = new PageData();
		pd = this.getPageData();
		Reslut reslut_View =  new Reslut();
		String jsonResult = "";
		try{
			if(AppUtil.checkParam("registerUser", pd)){	//传方法名和参数pd去检查参数是否都包含
				Object regName = pd.get("mobileNumber");
				Object regPS = pd.get("password");
				//验证手机号格式是否正确
				if(null != regName && isMobile(regName.toString())) {
					//验证密码格式是否正确，长度必须大于0
					if(null != regPS &&  regPS.toString().length() >= 1) {
						pd.put("USERNAME", pd.get("mobileNumber")); //将手机号作为用户账号注册
						//检查该账号在数据库中是否已经存在
						PageData pd1 = appuserService.findByUId(pd);
						if(null == pd1) {
							String uid =  this.get32UUID();
							pd.put("USER_ID", uid);	//ID
							pd.put("NAME", pd.get("mobileUserName")); //用户姓名
							pd.put("PHONE", pd.get("mobileNumber"));  //手机号
							pd.put("PASSWORD", MD5.md5(pd.getString("password")));
							pd.put("RIGHTS", "");					//权限
							pd.put("LAST_LOGIN", "");				//最后登录时间
							pd.put("IP", "");						//IP
							pd.put("ROLE_ID", "f944a9df72634249bbcb8cb73b0c9b86"); //默认初级会员角色id
							pd.put("STATUS", "1");	//状态为正常、0为冻结
							pd.put("BZ", "");	//备注为空
							pd.put("NUMBER", null == pd.get("smsCode") ? "" : pd.get("smsCode"));  //短信验证码
							pd.put("YEARS", "0");	//年限
							pd.put("START_TIME", "");	//冻结开始时间
							pd.put("END_TIME", "");	//冻结结束时间
							pd.put("EMAIL", "");	//email
							pd.put("SFID", "");	//email
							
							appuserService.saveU(pd); 
							reslut_View.setReslut_code(10);
							pd.clear();
							pd.put("userEeid", uid);
							jsonResult = "registered success";
						} else {
							reslut_View.setReslut_code(30);
							jsonResult = "Mobile phone has been registered";
						}
					} else {
						reslut_View.setReslut_code(21);
						jsonResult = "password is null";
					}
				} else {
					reslut_View.setReslut_code(20);
					jsonResult = "Mobile number format error";
				}
			}else {
				reslut_View.setReslut_code(29);
				jsonResult = "reigester information incomplete";
			}
			reslut_View.setReslut_message(jsonResult);
			reslut_View.setData(pd);
		}catch (Exception e){
			logger.error(e.toString(), e);
			reslut_View.setReslut_code(99);
			reslut_View.setReslut_message("登录操作失败！");
		}finally{
			logAfter(logger);
		}
		return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
	}
	
	/**
	 * APP登录接口，验证用户
	 * app用户登录成功后，把用户appUser对象和token保存到ehcahe缓存中，以便app客户端请求资源时进行验证。
	 * 接口调用地址：http://localhost:8080/scamysql/appU/loginApp.do
	 * 接口测试用地址：http://localhost:8080/scamysql/appU/loginApp.do?loginName=18545678988&password=123&token=7d6208779bb7d2f7fbe9dde7e683d121
		【请求参数】：
		strJson：json字符串，格式为 {
		“loginName":" 用户手机号码/用户ee号；",“password":"登录密码",
		“sysType":1(手机操作系统类型：1为安卓系统，2为iOS系统),
		“sysVer":"手机操作系统版本（9.1.2）",
		“mobileModel":"手机型号（IPHONE 6）",
		“token":"用于验证请求url是否合法,由loginName+password+privateKey+当前YYYYMMDD组成，MD5加密"
		}
		prsivateKey = "scaas34ljfrsja@#8$%dfkl;js&4*daklfjsdl;akfjsa342";(在TokenUtils类中可自定义)
		【返回值】：返回JSON格式字符串，格式为
		{"status":x,"mobileUserId":"手机用户Id","mobileNumber":"手机号码","mobileName":"手机用户姓名",
		"userEeid":"用户ee号","token":"token由loginName+password+privateKey+当前YYYYMMDD组成，MD5加密，
		用于app与服务端交互的凭证，app每次请求都需要携带此token令牌""}，
		x 为调用结果，x返回值说明如下：
		x = 10：登录成功；
		x = 20：登录失败，登录名或密码不正确；
		x = 90：登录失败，请求token验证不通过；
		x = 99：登录失败，未知原因；
	 */
	@RequestMapping(value="/loginApp" ,method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> loginApp(
			@RequestParam(value="username",required = true) String username
			,@RequestParam(value="password",required = true) String password)throws Exception{
		PageData pd = new PageData();
		Reslut reslut_View =  new Reslut();
		String jsonResult = "";
//		String token = pd.getString("token"); //获取客户端传过来的token
//		String loginName = pd.getString("loginName");
//		String password = pd.getString("password");
		//检验登录token是否合法（时间戳参数用的是yyyyMMDD格式）
		try {
			if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
//			if(TokenUtils.validToken(token, username+password,DateUtil.getDays())) {
				pd.put("USERNAME", username);
				String passwd = MD5.md5(password);	//密码加密
				pd.put("PASSWORD", passwd);
				//验证用户名和密码是否正确
				pd = appuserService.getUserByNameAndPwd(pd);
				if(pd != null){
					String status = pd.getString("STATUS");
					//判定该用户是否被冻结
					if("1".equals(status)) {
						pd.put("LAST_LOGIN",DateUtil.getTime().toString());
						//将手机系统类型、版本、手机类型作为ip字段保存起来
						String ips = null == pd.getString("sysType") ? "" : pd.getString("sysType")
								+ null == pd.getString("sysVer") ? "" : pd.getString("sysVer") 
										+ null == pd.getString("mobileModel") ? "" : pd.getString("mobileModel");
						pd.put("IP",ips);
						appuserService.updateLastLogin(pd);
						AppUser user = new AppUser();
						user.setSTATUS(pd.getString("STATUS"));
						user.setUSER_ID(pd.getString("USER_ID"));
						user.setUSERNAME(pd.getString("USERNAME"));
						user.setPASSWORD(pd.getString("PASSWORD"));
						user.setNAME(pd.getString("NAME"));
						user.setPhone(pd.getString("PHONE"));
						user.setRIGHTS(pd.getString("RIGHTS"));
						user.setROLE_ID(pd.getString("ROLE_ID"));
						user.setIP(ips);
						user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
						//设置登录用户的token,时间为一天
						String tokens = TokenUtils.getToken(username+password,DateUtil.getDays());
						user.setToken(tokens);
						pd.put("token", tokens);
						pd.remove("PASSWORD");
						//将用户登录信息保存到ehcache缓存中
						appMemory.saveLoginUser(MD5.md5(username), user);
						reslut_View.setReslut_code(1);
						jsonResult = "Login success";
					} else {
						reslut_View.setReslut_code(20);
						jsonResult = "Your account is frozen"; //用户名被冻结
					}
				}else{
					reslut_View.setReslut_code(20);
					jsonResult = "Login UserName or Password is error";	//用户名或密码有误			
				}
			} else{
				reslut_View.setReslut_code(90);
				jsonResult = "Login UserName or Password is error";	//用户名或密码有误		
			}
			reslut_View.setReslut_message(jsonResult);
			reslut_View.setData(pd);
//		} else{
//			jsonResult = "{\"result\":20,\"errorInfo\":\"Login UserName or password is null\"}";	//缺少参数
//		}
		} catch (Exception e) {
			reslut_View.setReslut_code(0);
			reslut_View.setReslut_message("登录操作失败！");
			e.printStackTrace();
		}
	    return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
	}
	
	/** 
     * 手机号验证 
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    } 
}
	
 