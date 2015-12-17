package com.shangpin.iog.forzieri.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.forzieri.dto.CategoryMap;
import com.shangpin.iog.forzieri.dto.CsvDTO;
import com.shangpin.iog.forzieri.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.TokenService;

@Component("forzieri")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	//private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private SkuPriceService skuPriceService;
	@Autowired
	private ProductSearchService productSearchService;
/*	@Autowired
	private TokenService tokenService;
	
	public void testDAO(){
		TokenDTO findToken = new TokenDTO();
		findToken.setSupplierId(supplierId);
		try {
			tokenService.refreshToken(findToken);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}*/
	public void fetchProductAndSave() {
		Map<String, String> mongMap = new HashMap<>();
		mongMap.put("supplierId", supplierId);
		mongMap.put("supplierName", "forzieri");
		logMongo.info(mongMap);
		logger.info("开始抓取");
		Map<String,CsvDTO> csvSpuMaps = new HashMap<String,CsvDTO>();
		List<String> skuIdList = new ArrayList<String>();
		List<CsvDTO> csvLists = null;
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		try {
			
			//得到can order 的skus
			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\t");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1、拉取can order的skus
		for (CsvDTO csvDTO : csvLists) {
			skuIdList.add(csvDTO.getSku());
			if (!csvSpuMaps.containsKey(csvDTO.getProduct_id())) {
				csvSpuMaps.put(csvDTO.getProduct_id(), csvDTO);
			}
			SkuDTO skuDTO = new SkuDTO();
			// id skuid spuid supplierid productName marketPrice salePrice supplierPrice color productCode saleCurrency productSize stock
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getSku());
			skuDTO.setSpuId(csvDTO.getProduct_id());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getProduct_name());
			skuDTO.setSalePrice(csvDTO.getSelling_price());
			skuDTO.setMarketPrice(csvDTO.getList_price());
			skuDTO.setSupplierPrice(csvDTO.getCost_price());
			skuDTO.setColor(csvDTO.getColor());
			if (StringUtils.isNotBlank(csvDTO.getManufacturer_id())) {
				skuDTO.setProductCode(csvDTO.getManufacturer_id());
			}else{
				skuDTO.setProductCode(csvDTO.getSku());
			}
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(csvDTO.getSize());
			skuDTO.setStock(csvDTO.getQuantity());
			skuDTO.setProductDescription(csvDTO.getDescription());
			try {
				
				if(skuDTOMap.containsKey(skuDTO.getSkuId())){
					skuDTOMap.remove(skuDTO.getSkuId());
				}
					productFetchService.saveSKU(skuDTO);
					
			} catch (ServiceException e) {
				if (e.getMessage().equals("数据插入失败键重复")) {
					// 更新价格和库存
					try {
						productFetchService.updatePriceAndStock(skuDTO);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			
			//保存图片
//			ProductPictureDTO pictureDTO = new ProductPictureDTO();
//			pictureDTO.setSupplierId(supplierId);
//			pictureDTO.setSkuId(csvDTO.getSku());
			String[] picUrl = new String[]{csvDTO.getVistaImagel0(),csvDTO.getVistaImagel1()
					,csvDTO.getVistaImagel2(),csvDTO.getVistaImagel3()
					,csvDTO.getVistaImagel4(),csvDTO.getVistaImagel5()};
			//判断是否是primary sku
			if (csvDTO.getSku().split("-")[2].equals("00")) {
				//是
				productFetchService.savePicture(supplierId, null, csvDTO.getSku(), Arrays.asList(picUrl));
//				for (String string : picUrl) {
//					if (StringUtils.isNotBlank(string)) {
//						pictureDTO.setId(UUIDGenerator.getUUID());
//						pictureDTO.setPicUrl(string);
//						try {
//							productFetchService.savePictureForMongo(pictureDTO);
//						} catch (ServiceException e) {
//							e.printStackTrace();
//						}
//					}
//				}
			}else{
//				pictureDTO.setId(UUIDGenerator.getUUID());
//				pictureDTO.setPicUrl(picUrl[0]);
					productFetchService.savePicture(supplierId, null, csvDTO.getSku(), Arrays.asList(picUrl[0]));
//					productFetchService.savePictureForMongo(pictureDTO);
				
			}
		}
		//保存spu
		CsvDTO spuDTO = null;
		Set<Entry<String,CsvDTO>> entrySet = csvSpuMaps.entrySet();
		Map<String, String> categoryMap = new CategoryMap(new HashMap<String,String>()).getCategoryMap();
		for (Entry<String, CsvDTO> entry : entrySet) {
			SpuDTO spu = new SpuDTO();
			spuDTO = entry.getValue();
			try {
				spu.setId(UUIDGenerator.getUUID());
				spu.setSpuId(spuDTO.getProduct_id());
				spu.setSupplierId(supplierId);
				spu.setBrandName(spuDTO.getBrand_name());
				String categoryid = spuDTO.getCategory_ids().split("\\|")[0];
				spu.setCategoryName(categoryMap.get(categoryid));
				spu.setCategoryGender(spuDTO.getGender());
				spu.setMaterial(spuDTO.getMaterial());
				productFetchService.saveSPU(spu);
			} catch (ServiceException e) {
				try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
		//设置卖完的sku的stock为0
		//得到当前库中所有的skuid
		try {
			String exportSkuIds = productSearchService.exportSkuId(supplierId, null, null);
			String[] skuIds = exportSkuIds.split(",");
			for (String skuId : skuIds) {
				if (!skuIdList.contains(skuId)) {
					SkuDTO dto = new SkuDTO();
					dto.setSupplierId(supplierId);
					dto.setSkuId(skuId);
					dto.setStock("0");
					productFetchService.updatePriceAndStock(dto);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
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
		
		logger.info("抓取结束");
	}
}

