package com.shangpin.iog.filippo.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.filippo.stock.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
public class StockClientImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String picurl;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		picurl = bdl.getString("picurl");
	}

	@Autowired
	DownloadAndReadCSV csvUtil;

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		String key = "";
		Map<String,CsvDTO> csvSkuMaps = new HashMap<String,CsvDTO>();
		CsvDTO dto = null;
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		List<CsvDTO> list = csvUtil.readLocalCSV(CsvDTO.class, "\\|");
		for (CsvDTO csvDTO : list) {
			key = csvDTO.getVAR_ID()+"-"+csvDTO.getTG();
			//添加pic
			if (csvSkuMaps.containsKey(key)) {
				dto = csvSkuMaps.get(key);
				dto.setIMG(dto.getIMG()+","+picurl+csvDTO.getIMG());
			}else{
				//不是一个sku key作为skuid
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
		}
		return skustock;
	}

	public static void main(String[] args) throws Exception {
		AbsUpdateProductStock stockImp = new StockClientImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("更新数据库开始");
		stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		logger.info("更新数据库结束");
		System.exit(0);
	}


}
