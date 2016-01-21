package com.shangpin.iog.paoloFirillo.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.shangpin.iog.paoloFirillo.dto.TxtDTO;
import com.shangpin.iog.paoloFirillo.util.TXTUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

@Component("paoloFirillo")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private SkuPriceService skuPriceService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void fetchProductAndSave() {
		
		Map<String,TxtDTO> spuMap = new HashMap<String, TxtDTO>();
		logger.info("开始抓取");
		
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		//获取所有的sku
		List<TxtDTO> dataList= null;
		try {
			dataList = TXTUtil.readLocalCSV(TxtDTO.class, ";");
		} catch (Exception e2) {
			logger.info("获取datalist失败");
			e2.printStackTrace();
		}
		
		logger.info("开始保存sku");
		System.out.println("开始保存sku");
		for (TxtDTO TxtDTO : dataList) {
			if (!spuMap.containsKey(TxtDTO.getProduct_id())) {
				spuMap.put(TxtDTO.getProduct_id(), TxtDTO);
			}
			SkuDTO skuDTO = new SkuDTO();
			// id skuid spuid supplierid productName marketPrice salePrice supplierPrice color productCode saleCurrency productSize stock
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(TxtDTO.getItem_id());
			skuDTO.setSpuId(TxtDTO.getProduct_id());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(TxtDTO.getItem_detailed_info());
			skuDTO.setMarketPrice(TxtDTO.getRetail_price());
			skuDTO.setSupplierPrice(TxtDTO.getSold_price());
			skuDTO.setColor(TxtDTO.getColour_description());
		
			skuDTO.setProductCode(TxtDTO.getProduct_id());
			
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(TxtDTO.getSize());
			skuDTO.setStock(TxtDTO.getQuantity());
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
			productFetchService.savePicture(supplierId, TxtDTO.getProduct_id(), null, Arrays.asList(TxtDTO.getPhoto_links()));
		}
		//保存spu
		System.out.println("开始保存spu");
		logger.info("开始保存spu");
		TxtDTO spuDTO = null;
		for (Entry<String, TxtDTO> entry : spuMap.entrySet()) {
			SpuDTO spu = new SpuDTO();
			spuDTO = entry.getValue();
			try {
				spu.setId(UUIDGenerator.getUUID());
				spu.setSpuId(spuDTO.getProduct_id());
				spu.setSupplierId(supplierId);
				spu.setBrandName(spuDTO.getBrand_name());
				spu.setCategoryName(spuDTO.getCategory());
				spu.setCategoryGender(spuDTO.getSex());
				spu.setMaterial(spuDTO.getMaterial());
				spu.setSeasonName(spuDTO.getSeason());
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
		
		//更新网站不再给信息的老数据
		System.out.println("更新网站不再给信息的老数据");
		logger.info("更新网站不再给信息的老数据");
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

