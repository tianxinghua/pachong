package com.shangpin.iog.deliberti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.deliberti.dto.Product;
import com.shangpin.iog.deliberti.util.MyUtil;
import com.shangpin.product.SupplierProduct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.deliberti.dto.DelibertiSkuDto;
import com.shangpin.iog.deliberti.dto.DelibertiSpuDto;

import com.shangpin.product.AbsSaveProduct;

@Component("deliberti2")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String supplierName;
	private static String supplierNo;
	private static String url;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("productUrl");
		supplierName=bdl.getString("supplierName");
		supplierNo=bdl.getString("supplierNo");
		day = Integer.valueOf(bdl.getString("day"));
		supp = new SupplierProduct();
		supp.setMessageType("json");
		supp.setSupplierName(supplierName);
		supp.setSupplierId(supplierId);
		supp.setSupplierNo(supplierNo);
		supp.setMessageId(UUIDGenerator.getUUID());
	}
	
	private static Gson gson = new Gson();

	
	public Map<String, Object>  fetchProductAndSave() {

		try {
			System.out.println("url"+url);
			List<Product> allProducts = MyUtil.readLocalCSV(url,Product.class);

			System.out.println("List<Product>的大小是=============="+allProducts.size());
			logger.info("List<Product>的大小是=============="+allProducts.size());
			        for (Product pro : allProducts) {
						supp.setData(gson.toJson(pro));
						pushMessage(null);
					}
			logger.info("抓取结束");
		 }catch (Exception e2) {
			e2.printStackTrace();
			logError.error(e2);
		}
		return null;
	}
}

