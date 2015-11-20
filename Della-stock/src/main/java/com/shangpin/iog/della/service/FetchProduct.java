package com.shangpin.iog.della.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.della.dto.Item;
import com.shangpin.iog.della.utils.CSVUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

public class FetchProduct extends AbsUpdateProductStock {
	
	private static Logger logError = Logger.getLogger("error");
	private static Logger logInfo  = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String filePath = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filePath = bdl.getString("filepath");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		List<Item> items = CSVUtil.readLocalCSV(filePath, Item.class, ";");
		for(Item item:items){
			
			stockMap.put(item.getItem_code(), item.getQuantity());
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
	
	public static void main(String[] args){
		
		AbsUpdateProductStock fetchProduct = new FetchProduct();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("della更新数据库开始");
		System.out.println("della更新数据库开始");
		try {
			fetchProduct.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("della更新数据库结束");
		System.out.println("della更新数据库结束");
		System.exit(0);
		
	}
	
}
