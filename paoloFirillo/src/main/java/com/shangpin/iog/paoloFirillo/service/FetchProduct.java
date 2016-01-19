package com.shangpin.iog.paoloFirillo.service;

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
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.paoloFirillo.dto.TxtDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

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
		Map<String,TxtDTO> csvSpuMaps = new HashMap<String,TxtDTO>();
		List<String> skuIdList = new ArrayList<String>();
		List<TxtDTO> csvLists = null;
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
			csvLists = DownloadAndReadCSV.readLocalCSV(TxtDTO.class, "\t");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//1、拉取can order的skus
		for (TxtDTO TxtDTO : csvLists) {
			skuIdList.add(TxtDTO.getSku());
			if (!csvSpuMaps.containsKey(TxtDTO.getProduct_id())) {
				csvSpuMaps.put(TxtDTO.getProduct_id(), TxtDTO);
			}
			SkuDTO skuDTO = new SkuDTO();
			// id skuid spuid supplierid productName marketPrice salePrice supplierPrice color productCode saleCurrency productSize stock
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(TxtDTO.getSku());
			skuDTO.setSpuId(TxtDTO.getProduct_id());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(TxtDTO.getProduct_name());
			skuDTO.setSalePrice(TxtDTO.getSelling_price());
			skuDTO.setMarketPrice(TxtDTO.getList_price());
			skuDTO.setSupplierPrice(TxtDTO.getCost_price());
			skuDTO.setColor(TxtDTO.getColor());
			if (StringUtils.isNotBlank(TxtDTO.getManufacturer_id())) {
				skuDTO.setProductCode(TxtDTO.getManufacturer_id());
			}else{
				skuDTO.setProductCode(TxtDTO.getSku());
			}
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(TxtDTO.getSize());
			skuDTO.setStock(TxtDTO.getQuantity());
			skuDTO.setProductDescription(TxtDTO.getDescription());
			skuDTO.setBarcode(TxtDTO.getPreorder()+" | "+TxtDTO.getShips_in_days());
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
//			pictureDTO.setSkuId(TxtDTO.getSku());
			String[] picUrl = new String[]{TxtDTO.getVistaImagel0(),TxtDTO.getVistaImagel1()
					,TxtDTO.getVistaImagel2(),TxtDTO.getVistaImagel3()
					,TxtDTO.getVistaImagel4(),TxtDTO.getVistaImagel5()};
			//判断是否是primary sku
			if (TxtDTO.getSku().split("-")[2].equals("00")) {
				//是
				productFetchService.savePicture(supplierId, null, TxtDTO.getSku(), Arrays.asList(picUrl));
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
					productFetchService.savePicture(supplierId, null, TxtDTO.getSku(), Arrays.asList(picUrl[0]));
//					productFetchService.savePictureForMongo(pictureDTO);
				
			}
		}
		//保存spu
		TxtDTO spuDTO = null;
		Set<Entry<String,TxtDTO>> entrySet = csvSpuMaps.entrySet();
		Map<String, String> categoryMap = new CategoryMap(new HashMap<String,String>()).getCategoryMap();
		for (Entry<String, TxtDTO> entry : entrySet) {
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

