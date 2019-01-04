package com.shangpin.iog.ylati.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.ylati.dto.products;
import com.shangpin.product.SupplierProduct;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.ylati.dto.Count;
import com.shangpin.product.AbsSaveProduct;

import sun.misc.BASE64Encoder;
/**
 *
 */
@SuppressWarnings("restriction")
@Component("ylati2")
public class FetchProduct extends AbsSaveProduct{
	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;
	public static SupplierProduct supp = null;
	private static String url_products;
	private static String url_count;
	private static String supplierId;
	private static String supplierName;
	private static String supplierNo;
	private static Gson gson = new Gson();
	private static final Integer PAGESIZE = 100;

	static {
		if (bdl == null)
			bdl = ResourceBundle.getBundle("conf");
		url_products = bdl.getString("url_products");
		url_count = bdl.getString("url_count");
		supp = new SupplierProduct();
		supp.setMessageType("json");
		supp.setSupplierName(supplierName);
		supp.setSupplierId(supplierId);
		supp.setSupplierNo(supplierNo);
		supp.setMessageId(UUIDGenerator.getUUID());


	}
	public void sendMessage() {

	}

	@Override
	public Map<String, Object>  fetchProductAndSave() {
		try{
			OutTimeConfig timeConfig = new OutTimeConfig(1000 * 60 * 60,
					1000 * 60 * 60, 1000 * 60 * 60);
			/*BASE64Encoder base64Encoder = new BASE64Encoder();
			String authorization = "Basic "+base64Encoder.encode((user_name+":"+password).getBytes());
			System.out.println(authorization);
			logger.info("Authorization："+authorization);
			Map<String,String> header = new HashMap<String,String>();
			header.put("Authorization", authorization);*/

//			String login = HttpUtil45.get("https://ylatiweb.myshopify.com/admin/auth/login", timeConfig, null,header, user_name, password);
//			System.out.println(login);

		   String result = HttpUtil45.get(url_count, timeConfig, null);
			Count count = gson.fromJson(result, Count.class);
			System.out.println("count--------"+count.getCount());
			if(null != count && null != count.getCount()){
			    int total = count.getCount();
				int pageCount = getPageCount(total, PAGESIZE);// 页数
				logger.info("ylati拉取产品总数：" + total+" 拉去总页数："+pageCount);
			for (int i = 1; i <= pageCount; i++) {
					//String url = url_products.replace("@page@", String.valueOf(i)).replace("@limit@", String.valueOf(PAGESIZE));
				//	String products = HttpUtil45.get(url, timeConfig, null,header,null,null);
			 String product = HttpUtil45.get(url_products, new OutTimeConfig(1000 * 60 * 20,
					1000 * 60 * 20, 1000 * 60 * 20), null);
			 products json = gson.fromJson(product, products.class);

			       supp.setData(gson.toJson(json));
					pushMessage(null);
				}
			}else{
				logger.info("ylati拉取失败，获取的商品总数为空");
			}
		}catch(Exception ex){
			logger.error("拉去ylati异常："+ex.getMessage(),ex);
		}finally {
			HttpUtil45.closePool();
		}

		return null;
	}
	/**
	 * 获取总页数
	 *
	 * @param totalSize
	 *            总计路数
	 * @param
	 *       //     每页记录数
	 * @return
	 */
	private Integer getPageCount(Integer totalSize, Integer pageSize) {
		if (totalSize % pageSize == 0) {
			return totalSize / pageSize;
		} else {
			return (totalSize / pageSize) + 1;
		}
	}

	public static void main(String[] args) {
//		new FetchProduct().fetchProductAndSave();
		String ss = "idProdotto;idSKU;Codice Prodotto;Marca;Nome Modello;Colore ENG;Descrizione ENG;Materiale ENG;Sesso(unisex, uomo, donna);Categoria;Collezione / Anno;Prezzo Et (€);Prezzo Imp (€);Price to WS (€);Taglie;Foto";
		String[] sss = ss.split(";");
		for(int i = 0;i<sss.length;i++){
			System.out.println(i+"   "+sss[i].trim());
		}
	}

}
