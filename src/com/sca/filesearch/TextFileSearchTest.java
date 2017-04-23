package com.sca.filesearch;

import java.io.File;  

public class TextFileSearchTest {  
  
    public static void main(String[] args) {  
  
        TextFileSearch search = new TextFileSearch();  
        search.SearchKeyword(new File("E:\\test.txt"), "iu");  
    }  
  
}  
