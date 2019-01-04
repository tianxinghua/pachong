package com.shangpin.iog.raffaelloNetwork.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.raffaelloNetwork.dto.Product;
import com.shangpin.iog.raffaelloNetwork.utils.Util;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("raffaelloNetwork")
public class FetchProduct  extends AbsSaveProduct{

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;
	private static Gson gson;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
		gson = new Gson();
	}
	
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	@Override
	public Map<String, Object> fetchProductAndSave() {
		
		Map<String, Object> returnMap = null;
		try {
			JSONArray list = Util.readCSV(uri);
			Iterator<JSONObject> ite = list.iterator();
			while(ite.hasNext()){
				JSONObject product =ite.next();
				supp.setData(gson.toJson(product));
				pushMessage(gson.toJson(supp));
				
				
			}
			if(flag){
				returnMap = fetchAndSave(list);
			}
			
		} catch (Exception e) {
		}
		return returnMap;
	}
	
	public static <T> List<T> readCSV(String uri,Class<T> clazz)
			throws Exception {
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为5分钟
		conn.setConnectTimeout(30 * 60 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream in = conn.getInputStream();
		if (in == null) {
			System.out.println("下载失败！！！！！！！！！！");
			System.exit(0);
		}
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		List<String> colValueList = new ArrayList<>();
		List<String> colValue = new ArrayList<String>();
		CsvReader cr = null;
		String[] split = null;
		// 解析csv文件
		cr = new CsvReader(in, Charset.forName("UTF-8"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		 cr.readRecord();
	        rowString = cr.getRawRecord();
	        split = rowString.split("\"\\|\"");
			List<String> colNameList = Arrays.asList(split);
			JSONArray arr=JSONArray.fromObject(colNameList);
			while(cr.readRecord()){
				rowString = cr.getRawRecord();
				split = rowString.split("\"\\|\"");
				colValueList = Arrays.asList(split);
				if(colValueList.size()==colNameList.size()){
					JSONObject obj = new JSONObject();
					int i=0;
					for(String name:colNameList){
						obj.put(name.replace("\"",""),colValueList.get(i).replace("\"",""));
						i++;
					}
					arr.add(obj);
				}
			}
			cr.close();
		return dtoList;
	}
	public Map<String, Object> fetchAndSave(List<Product> items){
		
		Map<String, Object> returnMap = new HashMap<String,Object>();
		
		try{
			if(items.size()>0){
				logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
				for(Product item :items){
					int beginIndex=item.getSize().indexOf(",");
					//保存sku	
					if(beginIndex!=-1){
					String size[] = item.getSize().split(",");
					String stock[] = item.getQuantity().split(",");
						if(size.length==stock.length){
							for (int i = 0; i < size.length; i++) {
								SkuDTO sku = new SkuDTO();
								sku.setId(UUIDGenerator.getUUID());
								sku.setSupplierId(supplierId);
								sku.setSpuId(item.getId());
								sku.setProductName(item.getTitle());
								sku.setMarketPrice(item.getProductcost());
								sku.setSupplierPrice(item.getProductcost());
								sku.setProductCode(item.getMpn());
								sku.setColor(item.getColor());
								sku.setProductDescription(item.getDescription()); 
								sku.setSaleCurrency("Euro");
								sku.setProductSize(size[i]);
								sku.setStock(stock[i]);
								sku.setSkuId(item.getMpn()+"_"+size[i]);
								sku.setBarcode(item.getId()+"_"+size[i]);
								// sku入库
								try {
									productFetchService.saveSKU(sku);						

								} catch (ServiceException e) {
									logInfo.info("入库失败========="+sku.getSkuId());
									logInfo.info("失败原因========="+e.getMessage());
									try {
										if (e.getMessage().equals("数据插入失败键重复")) {
											// 更新价格和库存
											productFetchService.updatePriceAndStock(sku);
										} else {
											e.printStackTrace();
										}
									} catch (ServiceException e1) {
										logError.error(e1.getMessage());
										e1.printStackTrace();
									}
								}
								
								//图片
								List<String> pics = new ArrayList<String>();
								int pic1Index=item.getImage_link().indexOf(",");
								int pic2Index=item.getAdditional_image_link().indexOf(",");
								if(pic1Index!=-1){
									String img1 [] = item.getImage_link().split(",");
									for (int j = 0; j < img1.length; j++) {
										pics.add(img1 [j]);
									}
								}else{
									pics.add(item.getImage_link());
								}
								if(pic2Index!=-1){
									String img2 [] = item.getAdditional_image_link().split(",");
									for (int j = 0; j < img2.length; j++) {
										pics.add(img2[j]);
									}
								}else{
									pics.add(item.getAdditional_image_link());
								}
								productFetchService.savePicture(supplierId, null, sku.getSkuId(), pics);
							}
						}else{
							System.out.println("id为:"+item.getId());
							continue;
						}
					
					}else{
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSkuId(item.getMpn()+"_"+item.getSize());
						sku.setSpuId(item.getId());
						sku.setProductName(item.getTitle());
						sku.setMarketPrice(item.getProductcost());
						sku.setSupplierPrice(item.getProductcost());
						sku.setProductCode(item.getMpn());
						sku.setColor(item.getColor());
						sku.setProductDescription(item.getDescription());
						sku.setSaleCurrency("Euro");
						sku.setBarcode(item.getId()+"_"+item.getSize());
						sku.setProductSize(item.getSize());
						sku.setStock(item.getQuantity());
						
						// sku入库
						try {
							productFetchService.saveSKU(sku);						

						} catch (ServiceException e) {
							logInfo.info("入库失败========="+sku.getSkuId());
							logInfo.info("失败原因========="+e.getMessage());
							try {
								if (e.getMessage().equals("数据插入失败键重复")) {
									// 更新价格和库存
									productFetchService.updatePriceAndStock(sku);
								} else {
									e.printStackTrace();
								}
							} catch (ServiceException e1) {
								logError.error(e1.getMessage());
								e1.printStackTrace();
							}
						}
						
						//图片
						List<String> pics = new ArrayList<String>();
						int pic1Index=item.getImage_link().indexOf(",");
						int pic2Index=item.getAdditional_image_link().indexOf(",");
						if(pic1Index!=-1){
							String img1 [] = item.getImage_link().split(",");
							for (int i = 0; i < img1.length; i++) {
								pics.add(img1 [i]);
							}
						}else{
							pics.add(item.getImage_link());
						}
						if(pic2Index!=-1){
							String img2 [] = item.getAdditional_image_link().split(",");
							for (int i = 0; i < img2.length; i++) {
								pics.add(img2[i]);
							}
						}else{
							pics.add(item.getAdditional_image_link());
						}
						productFetchService.savePicture(supplierId, null, sku.getSkuId(), pics);
						
					}
					
					//保存SPU
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getId());
					spu.setCategoryGender(item.getGender());
					spu.setCategoryName(item.getProduct_type());
					spu.setBrandName(item.getBrand());
					spu.setMaterial(item.getMaterial());
					spu.setProductOrigin(item.getMade_in_italy());
					spu.setSeasonName(item.getCustom_label_0());
					spu.setSeasonId(item.getCustom_label_0());
					try {
						productFetchService.saveSPU(spu);
					} catch (ServiceException e) {
						logError.error(e.getMessage());
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException ex) {
							logError.error(ex.getMessage());
							ex.printStackTrace();
						}

						e.printStackTrace();
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			logError.error(ex);
		}
		
		return returnMap;
		
	}
	
}
