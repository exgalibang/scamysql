package com.sca.filesearch;
import java.io.Closeable;  
import java.io.File;  
import java.io.FileReader;  
import java.io.IOException;  
import java.io.LineNumberReader;  
  
/** 
 * 对文本文件的关键词进行搜索 
 */  
public class TextFileSearch {  
	
	public int sum=0;	
    public void SearchKeyword(File file,String keyword) {  
        //参数校验  
        verifyParam(file, keyword);  
        //行读取  
        LineNumberReader lineReader = null;  
        try {  
            lineReader = new LineNumberReader(new FileReader(file));  
            String readLine = null;           
            while((readLine =lineReader.readLine()) != null){  
                //判断每一行中,出现关键词的次数  
                int index = 0;  
                int next = 0;  
                //判断次数  
                while((index = readLine.indexOf(keyword,next)) != -1) {  
                    next = index + keyword.length();  
                    sum++;  
                }  
                //if(sum > 0) {  
                //    System.out.println("第"+ lineReader.getLineNumber() +"行" + "出现 "+keyword+" 次数: "+sum);  
                //    sum=0;
                //}  
            } 
            System.out.println("出现 "+keyword+" 次数: "+sum);
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            //关闭流  
            close(lineReader);  
        }  
    }  
  
    /** 
     * 参数校验 
     *  
     */  
    private void verifyParam(File file, String keyword) {  
        //对参数进行校验证  
        if(file == null ){  
            throw new NullPointerException("the file is null");  
        }  
        if(keyword == null || keyword.trim().equals("")){  
            throw new NullPointerException("the keyword is null or \"\" ");  
        }  
          
        if(!file.exists()) {  
            throw new RuntimeException("the file is not exists");  
        }  
        //非目录  
        if(file.isDirectory()){  
            throw new RuntimeException("the file is a directory,not a file");  
        }  
          
        //可读取  
        if(!file.canRead()) {  
            throw new RuntimeException("the file can't read");  
        }  
    }  
      
    /** 
     * 关闭流 
     * <br> 
     * Date: 2014年11月5日 
     */  
    private void close(Closeable able){  
        if(able != null){  
            try {  
                able.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
                able = null;  
            }  
        }  
    }  
  
}  