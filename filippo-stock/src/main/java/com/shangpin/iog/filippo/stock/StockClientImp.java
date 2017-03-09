package com.shangpin.iog.filippo.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.filippo.stock.schedule.AppContext;
import com.shangpin.iog.filippo.stock.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("filippostock")
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	
	@SuppressWarnings("unused")
	private static ApplicationContext factory;
	private static void loadSpringContext()
	{
	   factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	private static ResourceBundle bdl = null;
	private static String picurl;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		picurl = bdl.getString("picurl");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		String key = "";
		Map<String,CsvDTO> csvSkuMaps = new HashMap<String,CsvDTO>();
		CsvDTO dto = null;
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		List<CsvDTO> list = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\\|");
		logger.info("拉取供应商数据并转换完毕，list大小是======"+list.size()); 
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
					skustock.put(skuId, skuEntry.getValue().getQTY().replace("\"", ""));
				}
			}
			if(!skustock.containsKey(skuId)){
				skustock.put(skuId,"0");
			}
		}
		return skustock;
	}

	public static void main(String[] args) throws Exception {
		//加载spring
        loadSpringContext();
	}


}
