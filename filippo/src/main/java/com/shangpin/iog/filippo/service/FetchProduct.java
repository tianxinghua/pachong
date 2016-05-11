package com.shangpin.iog.filippo.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.filippo.dto.CsvDTO;
import com.shangpin.iog.filippo.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

@Component("filippo")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String picurl;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		picurl = bdl.getString("picurl");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private SkuPriceService skuPriceService;
	
	public void fetchProductAndSave() {
		//更改状态存储，不要忘了填币种
		try {
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
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
			for(Map.Entry<String, CsvDTO> spuEntry :csvSpuMaps.entrySet()){
				SpuDTO spu = new SpuDTO();
				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(spuEntry.getValue().getART().replace("\"", ""));
//					spu.setSpuId(spuEntry.getValue().getVAR_ID().replace("\"", ""));
					spu.setSupplierId(supplierId);
					spu.setBrandName(spuEntry.getValue().getBND_NAME().replace("\"", ""));
					spu.setCategoryName(spuEntry.getValue().getGRP_DES().replace("\"", ""));
					spu.setSubCategoryName(spuEntry.getValue().getSUB_GRP_DES().replace("\"", ""));
					spu.setCategoryGender(spuEntry.getValue().getSR_DES().replace("\"", ""));
					spu.setMaterial(spuEntry.getValue().getCOMP().replace("\"", ""));
					logger.info(spuEntry.getValue().getART().replace("\"", "")+spuEntry.getValue().getCOMP().replace("\"", ""));
					System.out.println(spuEntry.getValue().getART().replace("\"", "")+spuEntry.getValue().getCOMP().replace("\"", ""));
					spu.setProductOrigin(spuEntry.getValue().getMADEIN().replace("\"", ""));
					spu.setSeasonName(spuEntry.getValue().getSTG().replace("\"", ""));
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
					productFetchService.updateMaterial(spu);
				}
			}
			for(Map.Entry<String, CsvDTO> skuEntry :csvSkuMaps.entrySet()){
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSkuId(skuEntry.getKey().replace("\"", ""));
				sku.setBarcode(skuEntry.getValue().getART()+"<>"+skuEntry.getValue().getART_VAR()+"<>"+skuEntry.getValue().getART_COL()+"<>"+skuEntry.getValue().getTG().replace(",", "."));
				sku.setSupplierId(supplierId);
				sku.setSpuId(skuEntry.getValue().getART().replace("\"", ""));
				sku.setColor(skuEntry.getValue().getCOL_DES().replace("\"", ""));
				sku.setSaleCurrency("EURO");
				sku.setMarketPrice(skuEntry.getValue().getREF().replace("\"", ""));
				sku.setSupplierPrice(skuEntry.getValue().getEUR().replace("\"", ""));
				sku.setStock(skuEntry.getValue().getQTY().replace("\"", ""));
				
				sku.setProductDescription(skuEntry.getValue().getTG_ID());
				
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
				size = size.replace(",", ".");
				sku.setProductSize(size);
				sku.setProductCode(skuEntry.getValue().getART().replace("\"", ""));
				try {
					

					if(skuDTOMap.containsKey(sku.getSkuId())){
						skuDTOMap.remove(sku.getSkuId());
					}
					
					
					productFetchService.saveSKU(sku);
				} catch (ServiceException e1) {
					try {
						if (e1.getMessage().equals("数据插入失败键重复")) {
							// 更新价格和库存
							productFetchService.updatePriceAndStock(sku);
						}
					} catch (ServiceException e2) {
						e2.printStackTrace();
					}
				}
				
				//保存图片
				productFetchService.savePicture(supplierId, null, skuEntry.getKey().replace("\"", ""), Arrays.asList(skuEntry.getValue().getIMG().replace("\"", "").split(";")));
			}
			
			logger.info("抓取结束");
			//更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
