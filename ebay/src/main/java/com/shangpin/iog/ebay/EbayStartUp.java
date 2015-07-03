/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月3日
 */
public class EbayStartUp {
	private static ApplicationContext factory;
    private static void loadSpringContext(){
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	public static void main(String[] args) {
		if(args!=null &&args.length>0){
			loadSpringContext();
			if("u".equals(args[0])){//更新库存
				updateStock();
			}else{				
				grabProduct();
			}
		}else{
			System.out.println("参数：u表示更新库存，其他表示拉取数据");
		}
	}
	
	private static void grabProduct(){
		V1GrabUpdateMain grab=factory.getBean(V1GrabUpdateMain.class);
		String supplier="";
		grab.grabSaveProduct(supplier);
	}
	private static void updateStock(){
		V1GrabUpdateMain grab=factory.getBean(V1GrabUpdateMain.class);
		String supplier="";
		String start=DateTimeUtil.LongFmt(new Date());
		Calendar c=Calendar.getInstance();c.add(Calendar.MONTH, -3);
		String end=DateTimeUtil.LongFmt(c.getTime());
		try {
			grab.updateProductStock(supplier, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
