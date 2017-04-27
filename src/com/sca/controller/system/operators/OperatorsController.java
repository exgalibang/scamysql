package com.sca.controller.system.operators;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sca.controller.base.BaseController;
import com.sca.entity.Page;
import com.sca.service.system.operators.OperatorsService;
import com.sca.service.system.menu.MenuService;
import com.sca.util.AppUtil;
import com.sca.util.PageData;
/** 
 * 类名称：OperatorController
 * @version
 */
@Controller
@RequestMapping(value="/operators")

public class OperatorsController extends BaseController {

	@Resource(name="menuService")
	private MenuService menuService;
	@Resource(name="operatorsService")
	private OperatorsService operatorsService;
	
	
	
	/**
	 * 保存运营商
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(PrintWriter out) throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdp = new PageData();
		pdp = this.getPageData();
		
		String PARENT_ID = pd.getString("PARENT_ID");
		pdp.put("OP_ID", PARENT_ID);
		
		if(null == pd.getString("OP_ID") || "".equals(pd.getString("OP_ID"))){
			if(null != PARENT_ID && "0".equals(PARENT_ID)){
				pd.put("JB", 1);
				pd.put("P_BM", pd.getString("BIANMA"));
			}else{
				pdp = operatorsService.findById(pdp);
				pd.put("JB", Integer.parseInt(pdp.get("JB").toString()) + 1);
				pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
			}
			pd.put("OP_ID", this.get32UUID());	//ID
			operatorsService.save(pd);
		}else{
			pdp = operatorsService.findById(pdp);
			if(null != PARENT_ID && "0".equals(PARENT_ID)){
				pd.put("P_BM",  pd.getString("BIANMA"));
			}else{
				pd.put("P_BM", pdp.getString("BIANMA") + "_" + pd.getString("BIANMA"));
			}
			
			operatorsService.edit(pd);
		}
		
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
		
	}

	
	/**
	 * 列表
	 */
	List<PageData> sopList;
	@RequestMapping
	public ModelAndView list(Page page)throws Exception{
		
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String PARENT_ID = pd.getString("PARENT_ID");
		
		
		if(null != PARENT_ID && !"".equals(PARENT_ID) && !"0".equals(PARENT_ID)){
			
			//返回按钮用
			PageData pdp = new PageData();
			pdp = this.getPageData();
			
			pdp.put("OP_ID", PARENT_ID);
			pdp = operatorsService.findById(pdp);
			mv.addObject("pdp", pdp);
			
			//头部导航
			sopList = new ArrayList<PageData>();
			this.getOPname(PARENT_ID);	//	逆序
			Collections.reverse(sopList);
			
		}
		
		
		String NAME = pd.getString("NAME");
		if(null != NAME && !"".equals(NAME)){
			NAME = NAME.trim();
			pd.put("NAME", NAME);
		}
		page.setShowCount(5);	//设置每页显示条数
		page.setPd(pd);				
		List<PageData> varList = operatorsService.operatorlistPage(page);
		
		mv.setViewName("system/operators/op_list");
		mv.addObject("varList", varList);
		mv.addObject("varsList", sopList);
		mv.addObject("pd", pd);
		
		return mv;
	}
	
	//递归
	public void getOPname(String PARENT_ID) {
		logBefore(logger, "递归");
		try {
			PageData pdps = new PageData();;
			pdps.put("OP_ID", PARENT_ID);
			pdps = operatorsService.findById(pdps);
			if(pdps != null){
				sopList.add(pdps);
				String PARENT_IDs = pdps.getString("PARENT_ID");
				this.getOPname(PARENT_IDs);
			}
		} catch (Exception e) {
			logger.error(e.toString(),e);
		}
	}
	
	/**
	 * 去新增页面
	 */
	@RequestMapping(value="/toAdd")
	public ModelAndView toAdd(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mv.setViewName("system/operators/op_edit");
			mv.addObject("pd", pd);
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
	}
	
	
	
	/**
	 * 去编辑页面
	 */
	@RequestMapping(value="/toEdit")
	public ModelAndView toEdit( String ROLE_ID )throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = operatorsService.findById(pd);
		if(Integer.parseInt(operatorsService.findCount(pd).get("ZS").toString()) != 0){
			mv.addObject("msg", "no");
		}else{
			mv.addObject("msg", "ok");
		}
		mv.setViewName("system/operators/op_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	
	/**
	 * 判断编码是否存在
	 */
	@RequestMapping(value="/has")
	public void has(PrintWriter out){
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(operatorsService.findBmCount(pd) != null){
				out.write("error");
			}else{
				out.write("success");
			}
			out.close();
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping(value="/del")
	@ResponseBody
	public Object del(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		String errInfo = "";
		try{
			pd = this.getPageData();
			
			if(Integer.parseInt(operatorsService.findCount(pd).get("ZS").toString()) != 0){
				errInfo = "false";
			}else{
				operatorsService.delete(pd);
				errInfo = "success";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
		map.put("result", errInfo);
		return AppUtil.returnObject(new PageData(), map);
		
	}
	

}
