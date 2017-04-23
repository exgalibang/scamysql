package com.sca.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/** 
 * 
 * @ClassName：ExcelReader2     
 * @Description: Excel表格操作类    
 *
 */
public class ExcelReader2 {
	private final static Integer WIDTH =80;
	private final static Integer HEIGHT= 100;
	private final static Integer COLUNMS =4;

		/**
		 * 
		 * @param file 读取文件对象
		 * @param rowNum 从第几行开始读，如果有一行表头则从第二行开始读
		 * @return
		 * @throws BiffException
		 * @throws IOException
		 */
		public static List readExcel(MultipartFile file,int rowNum) throws BiffException,
				IOException ,Exception{
			// 创建一个list 用来存储读取的内容
			List list = new ArrayList();
			Workbook rwb = null;
			Cell cell = null;
			// 创建输入流
			InputStream stream = file.getInputStream();
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(stream);
			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);
			// 行数(表头的目录不需要，从1开始)
			if(sheet.getRows()==0 ){
			throw( new Exception("文件为空或格式错误"));
			}
			for (int i = rowNum-1; i < sheet.getRows(); i++) {
				//UserModel model= new UserModel();
				 Map<String,String> map = new HashMap<String,String>(); 
				 if(sheet.getCell(0, i).getContents()!=null&&sheet.getCell(0, i).getContents().length()>0){
					 for (int j = 0; j < sheet.getColumns(); j++) {
							// 获取第i行，第j列的值
							cell = sheet.getCell(j, i);
							map.put("CELL"+j, cell.getContents());
						}
					list.add(map);
				 }else{
					 break;
				 }
			}
			// 返回值集合
			return list;
		}
		
	}

