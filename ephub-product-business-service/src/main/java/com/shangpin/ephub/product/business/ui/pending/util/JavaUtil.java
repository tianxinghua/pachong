package com.shangpin.ephub.product.business.ui.pending.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JavaUtil {

	/**
	 * 将父类的属性的值全部复制给子类
	 * @param father 父类
	 * @param child 子类
	 */
	public static void fatherToChild (Object father,Object child){  
		
        if((child.getClass().getSuperclass()==father.getClass())){  
        	Class<?> fatherClass= father.getClass();  
            Field ff[]= fatherClass.getDeclaredFields();  
            for(int i=0;i<ff.length;i++){  
                Field f=ff[i];//取出每一个属性，如deleteDate
//                Class<?> type=f.getType();
                try {
                	if("serialVersionUID".equals(f.getName())){
                		continue;
                	}
                    Method m = fatherClass.getMethod("get"+upperHeadChar(f.getName()));//方法getDeleteDate  
                    Object obj=m.invoke(father);//取出属性值         
                    f.setAccessible(true);
                    f.set(child,obj);                    
                } catch (Exception e) {
                	e.printStackTrace(); 
                }
            }  
        }else{
        	System.out.println(father.getClass().getName()+" 不是 "+child.getClass().getName()+"的父类。");
        } 
        
    }  
    /** 
    * 首字母大写，in:deleteDate，out:DeleteDate 
    */  
    private static String upperHeadChar(String in){  
        String head=in.substring(0,1);  
        String out=head.toUpperCase()+in.substring(1,in.length());  
        return out;  
    } 
 
}
