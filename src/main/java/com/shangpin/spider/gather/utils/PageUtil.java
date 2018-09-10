package com.shangpin.spider.gather.utils;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月17日 上午10:40:13 
 * @version 1.0 
 * @parameter  
 */

public class PageUtil {
	public static Long getPage(Long totalCount,Integer rows){
		Long totalpages = 0L;
	    if(totalCount<rows&&totalCount>0){
	      totalpages = 1L;
	    }
	    if(totalCount>=rows){
	      if(totalCount%rows==0){
	        totalpages = totalCount/rows;
	      }else{
	        totalpages = totalCount/rows+1;
	      }
	    }
	    return totalpages;
	}
	
	public static int booleanFlag(boolean flag){
		return flag?1:0;
	}
}
