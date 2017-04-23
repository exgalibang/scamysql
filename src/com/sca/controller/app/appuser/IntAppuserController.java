package com.sca.controller.app.appuser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sca.controller.app.entity.AppUserEntity;
import com.sca.controller.base.BaseController;
import com.sca.entity.Page;
import com.sca.httpClient.Reslut;
import com.sca.service.system.appuser.AppuserService;
import com.sca.util.AppUtil;
import com.sca.util.Const;
import com.sca.util.DateUtil;
import com.sca.util.FileUpload;
import com.sca.util.MD5;
import com.sca.util.PageData;
import com.sca.util.PathUtil;
import com.sca.util.Tools;

/**
  * 会员-接口类 
  * @author KM
 */
@Controller
@RequestMapping(value="/restful/appuser")
public class IntAppuserController extends BaseController {
	private static Logger loggers = LogManager.getLogger(IntAppuserController.class.getName());
	@Resource(name="appuserService")
	private AppuserService appuserService;
	
	/**
	 * 根据用户名获取会员信息
	 * 相关参数协议：
	 * 00	请求失败
	 * 01	请求成功
	 * 02	返回空值
	 * 03	请求协议参数不完整    
	 * 04  用户名或密码错误
	 * 05  FKEY验证失败
	 */
	@RequestMapping(value="/getAppuserByUm",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getAppuserByUsernmae(){
		logBefore(logger, "根据用户名获取会员信息");
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";
		try{
			if(Tools.checkKey("USERNAME", pd.getString("FKEY"))){	//检验请求key值是否合法
				if(AppUtil.checkParam("getAppuserByUsernmae", pd)){	//检查参数
					pd = appuserService.findByUId(pd);
					map.put("pd", pd);
					result = (null == pd) ?  "02" :  "01";
				}else {
					result = "03";
				}
			}else{
				result = "05";
			}
		}catch (Exception e){
			logger.error(e.toString(), e);
		}finally{
			map.put("result", result);
			logAfter(logger);
		}
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**
	 * 
	 * 分页查询用户数据接口
	 * @param  keyWords 用户名称关键字
	 * @param  showCount  每页显示数量
	 * @param  currentPage 当前页
	 * @return 用户数据的json 字符串
	 */
	@RequestMapping(value = "/listpage", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> listPage(
			@RequestParam(value="keyWords",required = false) String keyWords
			,@RequestParam(value="showCount",required = false) Integer showCount
			,@RequestParam(value="currentPage",required = false) Integer currentPage
			) {
    	PageData pd = new PageData();
    	Reslut reslut_View =  new Reslut();
		pd = this.getPageData();
		Page page = new Page();
		//默认每页取10条记录，最大条数
		if(null == showCount) 
			page.setShowCount(10);
		else if(showCount > Const.MAX_PAGE_SIZE) {
			page.setShowCount(Const.MAX_PAGE_SIZE);
		} else {
			page.setShowCount(showCount);
		}
		if(null != currentPage) page.setCurrentPage(currentPage);
		//去掉空字符串，并设置个体名称查询参数
		if(StringUtils.isNotBlank(keyWords)){
			keyWords = keyWords.trim();
			pd.put("USERNAME",keyWords);
		}
		page.setPd(pd);
        List<PageData> lists = null;
		try {
			lists = appuserService.listPdPageUser(page);
		} catch (Exception e) {
			e.printStackTrace();
		}
        if(null == lists || lists.isEmpty()){
        	reslut_View.setReslut_message("没有数据！");
        } else
        	reslut_View.setTotalResult( page.getTotalResult());
        reslut_View.setReslut_code(1);
    	reslut_View.setDataList(lists);
        return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
    }
	
	/**
	 * 
	 * 根据id获取单条用户数据信息接口
	 * @param  id   用户注册id
	 * @return 用户数据的json字符串
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> getEntity(@PathVariable String id) {
		Reslut reslut_View =  new Reslut();
    	PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USERNAME", id);
		try {
			pd = appuserService.findByUId(pd);
			reslut_View.setReslut_code(1);
			reslut_View.setReslut_message("获取数据操作完成！");
		} catch (Exception e) {
			reslut_View.setReslut_code(0);
    		reslut_View.setReslut_message("获取数据操作失败！");
    		pd.put("exMessage",e.toString());
    		e.printStackTrace();
		}
		reslut_View.setData(pd);
        return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
    }
	/**
	 * 新增用户数据接口
	 * @param  entityList  装有用户数据对象AppUserEntity的list集合
	 * @return 状态信息的 json字符串
	 */
    @RequestMapping(value = "/add", method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ResponseEntity<Reslut> addEntity(@RequestBody List<AppUserEntity> entityList) {
    	PageData pd = new PageData();
    	pd = this.getPageData();
    	Reslut reslut_View =  new Reslut();
    	try {
    		if(entityList != null && entityList.size() > 0){
    			for (AppUserEntity user : entityList) {
    				pd.put("USERNAME", user.getUsername());
    				PageData pd1 = new PageData();
    				pd1.put("USERNAME", user.getUsername());
	    			pd1.put("NAME", user.getName());
	    			pd1.put("ROLE_ID", user.getRole_id());
	    			pd1.put("PHONE", user.getPhone());
	    			pd1.put("EMAIL", user.getEmail());
	    			pd1.put("BZ", user.getBz());
    				pd1.put("USER_ID", this.get32UUID());	//ID
    				pd1.put("YEARS", 1);
    				pd1.put("RIGHTS", "");					//权限
    				pd1.put("LAST_LOGIN", "");				//最后登录时间
    				pd1.put("IP", "");						//IP
    				pd.put("STATUS", "0");				//状态
    				if(StringUtils.isNotBlank(user.getPassword()))
    					pd1.put("PASSWORD", MD5.md5(user.getPassword()));
    				else {
    					pd1.put("PASSWORD", MD5.md5("123456"));
    				}
    				try{
    					if(null == appuserService.findByUId(pd)){
    						appuserService.saveU(pd1);
    						pd.put(user.getUsername(),"success");
    					}else{
    						pd.put(user.getUsername(),"repeat");
    					}
    				} catch (Exception e) {
    					loggers.error(e.toString());
    					pd.put(user.getUsername(),"failure");
    				}
    			}
    		}
    		reslut_View.setReslut_code(1);
    		reslut_View.setReslut_message("数据新增操作完成！");
    	} catch (Exception e) {
    		reslut_View.setReslut_code(0);
    		reslut_View.setReslut_message("数据新增操作失败！");
    		loggers.error(e.toString());
    		e.printStackTrace();
    	}
    	reslut_View.setData(pd);
        return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
    }

    /**
	 * 修改用户数据接口
	 * @param  entity 用户数据对象AppUserEntity
	 * @return 状态信息的 json字符串
	 */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,produces="application/json;charset=UTF-8")
    public ResponseEntity<Reslut> modifyEntity(@PathVariable String id,@RequestBody AppUserEntity entity) {
        System.out.println("update id:"+id);
        PageData pd = new PageData();
        Reslut reslut_View =  new Reslut();
    	try {
    		if(null != id && entity != null){
    			pd.put("USER_ID", id);
    			pd.put("NAME", entity.getName());
    			pd.put("ROLE_ID", entity.getRole_id());
    			pd.put("PHONE", entity.getPhone());
    			pd.put("EMAIL", entity.getEmail());
    			pd.put("BZ", entity.getBz());
				pd.put("YEARS", 1);
				if(StringUtils.isNotBlank(entity.getPassword()))
					pd.put("PASSWORD", MD5.md5(entity.getPassword()));
				try{
					appuserService.editU(pd);
					pd.put("result","success");
				} catch (Exception e) {
					loggers.error(e.toString());
					pd.put("result","failure");
				}
				reslut_View.setReslut_code(1);
            	reslut_View.setReslut_message("数据更新操作完成！");
            } else{
            	reslut_View.setReslut_code(0);
            	reslut_View.setReslut_message("数据更新操作失败！数据为空");
            }
    	} catch (Exception e) {
    		reslut_View.setReslut_code(0);
    		reslut_View.setReslut_message("数据更新操作失败！");
    		loggers.error(e.toString());
    		e.printStackTrace();
    	}
        
        reslut_View.setData(pd);
        return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
    }

    /**
   	 * 删除用户数据接口
   	 * @param  id 用户数据的id
   	 * @return 状态信息的 json字符串
   	 */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,produces="application/json;charset=UTF-8")
    public ResponseEntity<Reslut> removeEntity(@PathVariable String id) {
        System.out.println("delete id:"+id);
        Reslut reslut_View =  new Reslut();
        PageData pd = new PageData();
        pd.put("USER_ID", id);
    	try {
    		appuserService.deleteU(pd);
    		pd.put("result","success");
    		reslut_View.setReslut_code(1);
            reslut_View.setReslut_message("数据删除操作完成！");
    	} catch (Exception e) {
    		pd.put("result","failure");
    		reslut_View.setReslut_code(0);
    		reslut_View.setReslut_message("数据删除操作失败！");
    		loggers.error(e.toString());
    		e.printStackTrace();
    	}
        reslut_View.setData(pd);
        return new ResponseEntity<Reslut>(reslut_View, HttpStatus.OK);
    }
	/**
   	 * 上传并导入文件接口
   	 * @param  MultipartFile file
   	 * @return 状态信息的 json字符串
   	 */
	@RequestMapping(value="/importfile",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> importFile(@RequestParam MultipartFile file){
//		MultipartFile file = upFile.getFile();
		PageData pd = new PageData();
		pd = this.getPageData();
		Reslut reslut_View =  new Reslut();
		String result = "";
    	try {
    		if(null != file && !file.isEmpty()) {
    			String  ffile = DateUtil.getDays();
    			String  fileName = file.getOriginalFilename();
    			loggers.info("文件大小: " + file.getSize()); 
    			loggers.info("文件原名: " + fileName); 
    			loggers.info("========================================"); 
    			try {
    				if (fileName.lastIndexOf(".") >= 0){
    					String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + ffile;		//文件上传路径
    					fileName = FileUpload.fileUp(file, filePath, this.get32UUID());	
    					pd.put("result","success");
    					pd.put("fileName",fileName);
//    					String[] strs = fileName.split("\\.");
//    					if("xls".equalsIgnoreCase(strs[1])) {
//    						//读取上传表格文档中的数据
//    						List<Map<String,String>> wordList = ExcelReader2.readExcel(file, 2);
//    						//读取用户函数将用户数据提取出来，保存到数据库中
//    					} else {
//    						result = "导入失败：只能导入Excel表格xls格式文件";
//							pd.put("isSuccess","false");
//    					}
    				} else {
    					result = "导入失败：文件格式不对！";
    					pd.put("result","failure");
    				}
    			} catch(Exception e) {
    				pd.put("result","failure");
    				reslut_View.setReslut_code(0);
    				reslut_View.setReslut_message("文件导入操作失败！");
    				loggers.error(e.toString());
    			}
    		} else {
    			result = "导入失败：请选择导入文件！";
    			pd.put("result","failure");
    		}
    		reslut_View.setReslut_code(1);
            reslut_View.setReslut_message("文件导入操作完成！");
    	} catch (Exception e) {
    		reslut_View.setReslut_code(0);
    		reslut_View.setReslut_message("文件导入操作失败！");
    		loggers.error(e.toString());
    		e.printStackTrace();
    	}
		pd.put("fileInfo",result);
		reslut_View.setData(pd);
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
	
 