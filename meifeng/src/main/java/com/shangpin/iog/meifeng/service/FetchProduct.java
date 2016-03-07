package com.shangpin.iog.meifeng.service;


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
import com.shangpin.iog.meifeng.dto.MeiFengSPU;
import com.shangpin.iog.meifeng.dto.MeiFengSku;
import com.shangpin.iog.meifeng.util.Excel2DTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by houkun on 2016/01/25.
 */
@Component("meifeng")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String filepath;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		filepath = bdl.getString("filepath");
		day = Integer.valueOf(bdl.getString("day"));
		
	}
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private ProductFetchService productFetchService;
    public void fetchProductAndSave(){
        	Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			System.out.println("请求数据");
			logger.info("请求数据");
			Excel2DTO excel2dto = new Excel2DTO();
			Short[] needColsNo = new Short[]{1,2,3,4,5,6,7,24,25,27,29,32,33,34,35};
			List<MeiFengSPU> spuList = excel2dto.toDTO(filepath, 0, needColsNo, MeiFengSPU.class);
			
			Map<String,MeiFengSPU> spuMap = new HashMap<String, MeiFengSPU>();
			
			Short[] stockneedColsNo = new Short[]{0,1,4,5};
			List<MeiFengSku> skuList = excel2dto.toDTO(filepath, 1, stockneedColsNo , MeiFengSku.class);
			
			logger.info("开始保存spu");
			System.out.println("开始保存spu");
			
			for (MeiFengSPU product : spuList) {
				spuMap.put(product.getSpuId(), product);
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(product.getSpuId());
				spu.setBrandName(product.getBrandName());
				spu.setCategoryName(product.getCategory());
				spu.setCategoryGender(product.getGender().contains("男")?"男":"女");
				spu.setSeasonName(product.getYear()+""+product.getSeason());
				spu.setMaterial(product.getMaterial());
				spu.setProductOrigin(product.getMadeIn());
				spu.setSpuName(product.getSpuName());
				//保存spu
				try {
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					e.printStackTrace();
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			//保存sku
			MeiFengSPU detail = null;
			for (MeiFengSku item : skuList) {
				SkuDTO sku = new SkuDTO();
				detail = spuMap.get(item.getSpuId());
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(item.getSpuId());
				sku.setSkuId(item.getSkuId());
				sku.setProductSize(item.getSize());
				sku.setProductCode(detail.getProductCode());
				sku.setStock(item.getQty());
				sku.setColor(detail.getColor());
				sku.setMarketPrice(detail.getMarketPrice());
				sku.setSupplierPrice(detail.getSupplierPrice());
				sku.setSaleCurrency("HDK");
				try {
					
					if(skuDTOMap.containsKey(sku.getSkuId())){
						skuDTOMap.remove(sku.getSkuId());
					}
					productFetchService.saveSKU(sku);
				} catch (ServiceException e) {
					try {
	    				if (e.getMessage().equals("数据插入失败键重复")) {
	    					//更新价格和库存
	    					productFetchService.updatePriceAndStock(sku);
	    				} else {
	    					e.printStackTrace();
	    				}
	    			} catch (ServiceException e1) {
	    				e1.printStackTrace();
	    			}			
				}
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
    }
    public static void main(String[] args) {
    }
}
