package com.shangpin.ephub.product.business.ui.pending.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaUtil {

	/**
	 * 将父类的属性的值全部复制给子类
	 * @param father 父类
	 * @param child 子类
	 */
	public static void fatherToChild (Object father,Object child){  
		if(null == father || null == child){
			log.info("父类对象或子类对象不能为空");
			return;
		}
        if((child.getClass().getSuperclass()==father.getClass())){  
        	Class<?> fatherClass= father.getClass();  
            Field ff[]= fatherClass.getDeclaredFields();  
            for(int i=0;i<ff.length;i++){  
                Field f=ff[i];
                try {
                	int modifiers = f.getModifiers();
                	if((modifiers & Modifier.FINAL) != 0){
                		continue;
                	}
                	Type type = f.getGenericType();
                	Method m = null;
                	String name = f.getName();
					if(type.getTypeName().equals("boolean")){
                		if(name.startsWith("is") && Character.isUpperCase(name.charAt(2))){
                			m = fatherClass.getMethod(name);
                		}else{
                			m = fatherClass.getMethod("is"+upperHeadChar(name));
                		}
                	}else{
                		m = fatherClass.getMethod("get"+upperHeadChar(name));
                	}
                    Object obj=m.invoke(father);
                    f.setAccessible(true);
                    f.set(child,obj);                    
                } catch (Exception e) {
                	e.printStackTrace(); 
                }
            }  
        }else{
        	log.info(father.getClass().getName()+" 不是 "+child.getClass().getName()+"的父类。");
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
    
    /**
     * 构造属性的get方法，比如传入name，返回getName
     * @param fieldName
     * @return
     */
    public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}
    
    /**
     * 将pendingSpu转化为pendingProduct
     * @param pendingSpu
     * @return
     */
    public static PendingProductDto convertHubSpuPendingDto2PendingProductDto(HubSpuPendingDto pendingSpu) throws Exception{
        PendingProductDto pendingProduct = new PendingProductDto();
		JavaUtil.fatherToChild(pendingSpu, pendingProduct); 
        return pendingProduct;
    }
 
}
