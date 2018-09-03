package com.shangpin.iog.styleside.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.styleside.dto.SkuDTO;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "",usr="",pwd="",recordCount="",language="";
	static {
		if (null == bdl){
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");

		usr = bdl.getString("usr");
		pwd = bdl.getString("pwd");
		recordCount = bdl.getString("recordCount");
		language = bdl.getString("language");
		supplierId = bdl.getString("supplierId");
	}
	ObjectMapper mapper = new ObjectMapper();
	OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

	/**
	 * 程序配置：
	 * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
	 * 时间配置 获取当前时间
	 * 页码为程序中循环增加，
	 * 程序的循环结束（while循环）标志：
	 * 拉取的信息的 list size()为 0 的时候结束
	 */
	public  Map<String,String> getProductStock(Collection<String> skuNos) {
		//定义供应商 skuNo （key） Quantita(value) Map集合
		logger.info("===============Collection<String> skuNos size()================"+skuNos.size());
		Map<String, String> spStockMap = new HashMap<>();
        String data = "";
		try {

            for (String str : skuNos) {
                String   URL=    String.format("https://www.viettishop.com/api/rest/marketplaces/stock_check/%s", str);
                data = OAuth.getData ("GET",URL,"de924d99f10d11f0f881ee77cbdf254e","85f2923d3f8fdc376cda2633f10ee204","6637e3905e90dedb3cc177f930eba2dc","1.0","HMAC-SHA1");
                Gson gson = new Gson();
                SkuDTO skuDTO = gson.fromJson(data, SkuDTO.class);
                String qty = skuDTO.getQty();
                spStockMap.put(str,qty.substring(0,qty.indexOf(".")));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("成功获取到的map大小  spStockMap.size======"+spStockMap.size());
		return spStockMap;
	}

}
