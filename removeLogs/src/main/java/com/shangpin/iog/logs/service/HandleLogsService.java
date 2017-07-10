package com.shangpin.iog.logs.service;

import java.io.File;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.shangpin.iog.common.utils.DateTimeUtil;

@Service
public class HandleLogsService {

	private static Logger log = Logger.getLogger("info");
	
	private static ResourceBundle bdl = null;	
	private static String filepath = null;
	private static String setMaxValue = null;	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		filepath = bdl.getString("filepath");
		setMaxValue = bdl.getString("setMaxValue");
	}
	
	/**
	 * 定时压缩每天的日志，然后删除源文件
	 */
	public void zipLogs(){
		try {
			File file = new File(filepath);
			if(!file.isDirectory()){
				throw new Exception("没有找到该文件夹 "+filepath);
			}
			//file.listFiles()
			
		} catch (Exception e) {
			log.info(e.toString()); 
		}
	}
	
	private void dothejob(){
		File file = new File(filepath);
		double size = getDirSize(file);
		
	}
	
	private double getDirSize(File file) {     
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                double size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                return size;     
            } else {//如果是文件则直接返回其大小,以“g”为单位   
                double size = (double) file.length() / 1024 / 1024 /1024;        
                return size;     
            }     
        }else{ 
        	log.info("文件或者文件夹不存在，请检查路径是否正确！"); 
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");     
            return 0.0;     
        }     
    }
	
	public static void main(String[] args) {
		System.out.println(DateTimeUtil.getPreviouseDate());
	}
}


