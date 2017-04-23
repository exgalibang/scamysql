//package com.sca.service.system.operators;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.stereotype.Service;
//
//import com.sca.dao.DaoSupport;
//import com.sca.entity.Page;
//import com.sca.util.PageData;
//
//@Service("opretorsService")
//public class OperatorsService{
//
//	@Resource(name = "daoSupport")
//	private DaoSupport dao;
//	
//	
//	//新增
//	public void save(PageData pd)throws Exception{
//		dao.save("OperatorsMapper.save", pd);
//	}
//	
//	//修改
//	public void edit(PageData pd)throws Exception{
//		dao.update("OperatorsMapper.edit", pd);
//	}
//	
//	//通过id获取数据
//	public PageData findById(PageData pd) throws Exception {
//		return (PageData) dao.findForObject("OperatorsMapper.findById", pd);
//	}
//	
//	//查询总数
//	public PageData findCount(PageData pd) throws Exception {
//		return (PageData) dao.findForObject("OperatorsMapper.findCount", pd);
//	}
//	
//	//查询某编码
//	public PageData findBmCount(PageData pd) throws Exception {
//		return (PageData) dao.findForObject("DictionariesMapper.findBmCount", pd);
//	}
//	
//	//列出同一父类id下的数据
//	public List<PageData> operatorlistPage(Page page) throws Exception {
//		return (List<PageData>) dao.findForList("OperatorsMapper.operatorlistPage", page);
//		
//	}
//	
//	//删除
//	public void delete(PageData pd) throws Exception {
//		dao.delete("OperatorsMapper.delete", pd);
//		
//	}
//
//	
//	
//}
