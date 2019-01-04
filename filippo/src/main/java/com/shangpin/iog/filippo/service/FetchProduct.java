package com.shangpin.iog.filippo.service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.filippo.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;
import com.shangpin.product.AbsSaveProduct;

@Component("filippo")
public class FetchProduct extends AbsSaveProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String picurl;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		picurl = bdl.getString("picurl");
		day = Integer.valueOf(bdl.getString("day"));
	}
	
	private static Gson gson = new Gson();
	
	public Map<String, Object> fetchProductAndSave() {
		try {
			logger.info("开始抓取");
			List<CsvDTO> csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\\|");
			for (CsvDTO csvDTO : csvLists) {
				csvDTO.setIMG(picurl+csvDTO.getIMG());
				supp.setData(gson.toJson(csvDTO));  
            	pushMessage(null); 
			}
			logger.info("抓取结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
