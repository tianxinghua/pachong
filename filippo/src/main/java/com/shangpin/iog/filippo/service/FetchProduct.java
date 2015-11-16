package com.shangpin.iog.filippo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.filippo.stock.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SkuPriceService;

@Component("filippo")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	//private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
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
	private ProductFetchService productFetchService;
	@Autowired
	private SkuPriceService skuPriceService;
	public void fetchProductAndSave() {
		//更改状态存储，不要忘了填币种
		try {
			Map<String, String> mongMap = new HashMap<>();
			OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
			//String result = HttpUtil45.get(url, timeConfig, null);
			mongMap.put("supplierId", supplierId);
			mongMap.put("supplierName", "filippo");
			//mongMap.put("result", result);
			logMongo.info(mongMap);
			logger.info("开始抓取");
			List<CsvDTO> csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\\|");
			Map<String,CsvDTO> csvSkuMaps = new HashMap<String,CsvDTO>();
			Map<String,CsvDTO> csvSpuMaps = new HashMap<String,CsvDTO>();
			Map<String,String> picMaps = new HashMap<String,String>();
			String key = "";
			CsvDTO dto = null;
			for (CsvDTO csvDTO : csvLists) {
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
				//art作为spuid
				if (!csvSpuMaps.containsKey(csvDTO.getART())) {
					csvSpuMaps.put(csvDTO.getART(), csvDTO);
				}
			}
			
			for(Map.Entry<String, CsvDTO> skuEntry :csvSkuMaps.entrySet()){
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSkuId(skuEntry.getKey().replace("\"", ""));
				sku.setBarcode(skuEntry.getValue().getART()+"<>"+skuEntry.getValue().getART_VAR()+"<>"+skuEntry.getValue().getART_COL()+"<>"+skuEntry.getValue().getTG());
				sku.setSupplierId(supplierId);
				sku.setSpuId(skuEntry.getValue().getART().replace("\"", ""));
				sku.setColor(skuEntry.getValue().getCOL_DES().replace("\"", ""));
				sku.setSaleCurrency("EURO");
				sku.setMarketPrice(skuEntry.getValue().getREF().replace("\"", ""));
				sku.setSupplierPrice(skuEntry.getValue().getEUR().replace("\"", ""));
				sku.setStock(skuEntry.getValue().getQTY().replace("\"", ""));
				String size = skuEntry.getValue().getTG().replace("\"", "");
				if(size.indexOf("1/2")>0){
					size=size.replace("-1/2","+");
				}
				if (size.equals("-")) {
					size = "UNIQUE";
				}
				if (size.substring(size.length()-1, size.length()).equals("-")) {
					size = size.replace("-", ".5");
				}
				sku.setProductSize(size);
				sku.setProductCode(skuEntry.getValue().getART().replace("\"", ""));
				try {
					productFetchService.saveSKU(sku);
				} catch (ServiceException e1) {
					try {
						if (e1.getMessage().equals("数据插入失败键重复")) {
							// 更新价格和库存
							productFetchService.updatePriceAndStock(sku);
							e1.printStackTrace();
						}

					} catch (ServiceException e2) {
						e2.printStackTrace();
					}
				}
				//确定图片
				//if (!picMaps.containsKey(skuEntry.getValue().getVAR_ID().replace("\"", ""))) {
				//	picMaps.put(skuEntry.getValue().getVAR_ID().replace("\"", ""), "");
					ProductPictureDTO picture = new ProductPictureDTO();
					picture.setSupplierId(supplierId);
					picture.setId(UUIDGenerator.getUUID());
					picture.setSkuId(skuEntry.getKey().replace("\"", ""));
					//picture.setSpuId(product.getProductCode());
					picture.setPicUrl(skuEntry.getValue().getIMG().replace("\"", ""));
					try {
						productFetchService.savePictureForMongo(picture);
					} catch (ServiceException e) {
						e.printStackTrace();
					}
			//	}
			}
			
			for(Map.Entry<String, CsvDTO> spuEntry :csvSpuMaps.entrySet()){
				SpuDTO spu = new SpuDTO();
				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(spuEntry.getValue().getART().replace("\"", ""));
					spu.setSupplierId(supplierId);
					spu.setBrandName(spuEntry.getValue().getBND_NAME().replace("\"", ""));
					spu.setCategoryName(spuEntry.getValue().getGRP_DES().replace("\"", ""));
					spu.setSubCategoryName(spuEntry.getValue().getSUB_GRP_DES().replace("\"", ""));
					spu.setCategoryGender(spuEntry.getValue().getSR_DES().replace("\"", ""));
					spu.setMaterial(spuEntry.getValue().getCOMP().replace("\"", ""));
					System.out.println(spuEntry.getValue().getCOMP().replace("\"", ""));
					spu.setProductOrigin(spuEntry.getValue().getMADEIN().replace("\"", ""));
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			logger.info("抓取结束");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
