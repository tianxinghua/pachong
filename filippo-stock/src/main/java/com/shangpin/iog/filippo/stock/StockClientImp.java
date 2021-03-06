package com.shangpin.iog.filippo.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.filippo.stock.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("filippostock")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	 private static ApplicationContext factory;
	    private static void loadSpringContext()
	    {
	        factory = new AnnotationConfigApplicationContext(AppContext.class);
	    }
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String picurl;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		picurl = bdl.getString("picurl");
	}




	@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		String key = "";
		Map<String,CsvDTO> csvSkuMaps = new HashMap<String,CsvDTO>();
		CsvDTO dto = null;
		Map<String, Integer> skustock = new HashMap<>(skuNo.size());
		List<CsvDTO> list = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\\|");
		for (CsvDTO csvDTO : list) {
			key = csvDTO.getVAR_ID()+"-"+csvDTO.getTG();
			//添加pic
			if (csvSkuMaps.containsKey(key)) {
				dto = csvSkuMaps.get(key);
				dto.setIMG(dto.getIMG()+";"+picurl+csvDTO.getIMG());
			}else{
				//不是一个sku key作为skuid
				csvDTO.setIMG(picurl+csvDTO.getIMG());
				csvSkuMaps.put(key, csvDTO);
			}
		}
		
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			String skuId = it.next();
			for(Map.Entry<String, CsvDTO> skuEntry :csvSkuMaps.entrySet()){
				key = skuEntry.getValue().getVAR_ID()+"-"+skuEntry.getValue().getTG();
				if (skuId.equals(key.replace("\"", ""))) {
					skustock.put(skuId, Integer.valueOf(skuEntry.getValue().getQTY().replace("\"", "")));
				}
			}
			if(!skustock.containsKey(skuId)){
				skustock.put(skuId,0);
			}
		}
		return skustock;
	}

	public static void main(String[] args) throws Exception {
		//加载spring
//        loadSpringContext();
//        StockClientImp stockImp =(StockClientImp)factory.getBean("filippostock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("更新数据库开始");
//		try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("更新库存数据库出错"+e.toString());
//		}
//		logger.info("更新数据库结束");
//		System.exit(0);
	}


}
