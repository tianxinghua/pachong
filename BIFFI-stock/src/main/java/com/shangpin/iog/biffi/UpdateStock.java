package com.shangpin.iog.biffi;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.biffi.dto.Detail;
import com.shangpin.iog.biffi.dto.Item;
import com.shangpin.iog.biffi.dto.Items;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

@Component("biffi")
public class UpdateStock extends AbsUpdateProductStock{

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60,
			1000 * 60 * 5, 1000 * 60 * 5);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
	
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();		
		int page = 1;		
		while(StringUtils.isNotBlank(uri)){
			String result = HttpUtil45.get(uri, outTimeConf, null);
			Items items = ObjectXMLUtil.xml2Obj(Items.class, result);
			List<Item> its = items.getItem();
			if(its.size()>0){
				for(Item item:its){
					try{							
						String spuId = item.getId();						
						String detail = HttpUtil45.get("http://www.biffi.com/b2citem-detail/?idarticolo="+spuId, outTimeConf, null);
						Item it = ObjectXMLUtil.xml2Obj(Item.class, detail);
						List<Detail> dets = it.getDetails().getDetail();
						for(Detail de :dets){
							try{								
								stockMap.put(de.getBarcode(), de.getQty());
							}catch(Exception e){
								logError.error(e);
								e.printStackTrace();
							}						
						}
						
					}catch(Exception ex){
						logError.error(ex);
						ex.printStackTrace();
					}	
					
				}
				if(its.size()>=50){
					page ++;
					uri = uri + page;
				}else{
					uri = null;
				}
			}
		}
		
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		
		return skustock;
	}
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
		UpdateStock grabStockImp = (UpdateStock)factory.getBean("biffi");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("biffi更新数据库开始 start");
		System.out.println("biffi更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("biffi更新数据库结束");
		System.out.println("biffi更新数据库结束 over");
		System.exit(0);
		
	}

}
