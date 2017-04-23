package com.sca.controller.app.info;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sca.controller.base.BaseController;
import com.sca.entity.Page;
import com.sca.httpClient.Reslut;
import com.sca.service.information.pictures.PicturesService;
import com.sca.util.Const;
import com.sca.util.DateUtil;
import com.sca.util.DelAllFile;
import com.sca.util.FileUpload;
import com.sca.util.PageData;
import com.sca.util.PathUtil;
import com.sca.util.Tools;

/**
  * 图片处理-接口类 
 */
@Controller
@RequestMapping(value="/restful/picture")
public class IntPicturesController extends BaseController {
	private static Logger loggers = LogManager.getLogger(IntPicturesController.class.getName());
	@Resource(name="picturesService")
	private PicturesService picturesService;
	
	/**
	 * 
	 * 分页查询图片数据接口
	 * 接口调用地址：http://ip地址:端口/scamysql/restful/picture/listpage
	 * @param  keyWords 图片名称关键字
	 * @param  showCount  每页显示数量
	 * @param  currentPage 当前页
	 * @return 图片数据的json 字符串
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
		if(null != keyWords && !"".equals(keyWords)){
			keyWords = keyWords.trim();
			pd.put("KEYW", keyWords);
		}
		page.setPd(pd);
        List<PageData> lists = null;
		try {
			lists = picturesService.list(page);
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
	 * 根据id获取单条图片数据信息接口
	 * 接口调用地址：http://ip地址:端口/scamysql/restful/picture/图片id
	 * @param  id   图片注册id
	 * @return 图片数据的json字符串
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,produces="application/json;charset=UTF-8")
	public ResponseEntity<Reslut> getEntity(@PathVariable String id) {
		Reslut reslut_View =  new Reslut();
    	PageData pd = new PageData();
		pd.put("PICTURES_ID", id);
		try {
			pd = picturesService.findById(pd);
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
	 * 新增图片数据接口
	 * 接口调用地址：http://ip地址:端口/scamysql/restful/picture/add
	 * 请求方式为POST
	 * @param  MultipartFile file 文件对象
	 * @return 状态信息的 json字符串
	 */
    @RequestMapping(value = "/add",method = RequestMethod.POST,produces="application/json;charset=UTF-8")
    public ResponseEntity<Reslut> addEntity(
    		@RequestParam(required=false) MultipartFile file) {
    	logBefore(logger, "新增Pictures");
    	PageData pd = new PageData();
    	Reslut reslut_View =  new Reslut();
    	try {
    		String  ffile = DateUtil.getDays(), fileName = "";
			if (null != file && !file.isEmpty()) {
				String orifileName=file.getOriginalFilename();// 文件原名称
				String type = orifileName.indexOf(".") != -1 ? 
						orifileName.substring(orifileName.lastIndexOf(".")+1, orifileName.length()) : null;
				if (type != null) {// 判断文件类型是否为空
					if ("GIF".equals(type.toUpperCase())||"PNG".equals(type.toUpperCase())||"BMP".equals(type.toUpperCase())
							||"JPG".equals(type.toUpperCase())||"JPEG".equals(type.toUpperCase())) {
						String filePath = PathUtil.getClasspath() + Const.FILEPATHIMG + ffile;		//文件上传路径
						fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
						pd.put("PICTURES_ID", this.get32UUID());			//主键
						pd.put("TITLE", orifileName);								//标题
						pd.put("NAME", file.getName());							//文件名
						pd.put("PATH", ffile + "/" + fileName);				//路径
						pd.put("CREATETIME", Tools.date2Str(new Date()));	//创建时间
						pd.put("MASTER_ID", "1");							//附属与
						pd.put("BZ", "接口上传图片");						//备注
						//加水印
//						Watermark.setWatemark(PathUtil.getClasspath() + Const.FILEPATHIMG + ffile + "/" + fileName);
						picturesService.save(pd);
						reslut_View.setReslut_code(1);
						reslut_View.setReslut_message("数据新增操作完成！");
					} else {
						reslut_View.setReslut_code(0);
						reslut_View.setReslut_message("图片新增失败，只能上传GIF、PNG、JPG、JPEG、BMP格式的图片文件！");
					}
				} else {
					reslut_View.setReslut_code(0);
					reslut_View.setReslut_message("图片新增失败，只能上传GIF、PNG、JPG、JPEG、BMP格式的图片文件！");
				}
			}else{
				reslut_View.setReslut_code(0);
	    		reslut_View.setReslut_message("数据新增操作失败！");
			}
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
   	 * 删除图片数据接口
   	 * 接口调用地址：http://ip地址:端口/scamysql/restful/picture/图片id
   	 * 请求方式为DELETE
   	 * @param  id 图片数据的id
   	 * @return 状态信息的 json字符串
   	 */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,produces="application/json;charset=UTF-8")
    public ResponseEntity<Reslut> removeEntity(@PathVariable String id) {
        System.out.println("delete id:"+id);
        Reslut reslut_View =  new Reslut();
        PageData pd = new PageData();
        pd = this.getPageData();
    	try {
    		pd.put("PICTURES_ID", id);
    		String PATH = pd.getString("PATH");													 		//图片路径
			DelAllFile.delFolder(PathUtil.getClasspath()+ Const.FILEPATHIMG + pd.getString("PATH")); 	//删除图片
			if(PATH != null){
				picturesService.delTp(pd);																//删除数据中图片数据
			}	
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
}
	
 