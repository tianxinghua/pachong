package com.shangpin.iog.aspesi.service;

import java.net.MalformedURLException;
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
import com.shangpin.iog.aspesi.dto.TxtDTO;
import com.shangpin.iog.aspesi.util.MyTxtUtil;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

@Component("aspesi")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	private SkuPriceService skuPriceService;
	public void fetchProductAndSave() {
		Map<String,TxtDTO> spuMap = new HashMap<String,TxtDTO>();
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
			List<TxtDTO> txtlist = getList();
			
		    for(TxtDTO product:txtlist) {
		    	if (product.getMANUFACTURER().equalsIgnoreCase("Aspesi")) {
					
		    		if (!spuMap.containsKey(product.getMASTER_SKU())) {
		    			spuMap.put(product.getMASTER_SKU(), product);
		    		}
		    		///////////////////////////////保存SKU//////////////////////////////////
		    		SkuDTO sku = new SkuDTO();
		    		String spuId = product.getMASTER_SKU();
		    		try {
		    			sku.setId(UUIDGenerator.getUUID());
		    			sku.setSupplierId(supplierId);
		    			sku.setSpuId(spuId);
		    			sku.setSkuId(product.getVARIANT_SKU());
		    			sku.setProductSize(product.getSIZE());
		    			sku.setMarketPrice(product.getPRICE());
		    			sku.setSalePrice(product.getSALEPRICE());
		    			sku.setSupplierPrice(product.getPRICE());
		    			sku.setColor(product.getCOLOR());
		    			sku.setProductDescription(product.getDESCRIPTION());
		    			sku.setStock(product.getSTOCK_LEVEL());
		    			sku.setProductCode(spuId);
		    			sku.setSaleCurrency("euro");
		    			
		    			if(skuDTOMap.containsKey(sku.getSkuId())){
		    				skuDTOMap.remove(sku.getSkuId());
		    			}
		    			productFetchService.saveSKU(sku);
		    		} catch (ServiceException e) {
		    			try {
		    				if (e.getMessage().equals("数据插入失败键重复")) {
		    					System.out.println("saveSKU数据插入失败键重复");
		    					//更新价格和库存
		    					productFetchService.updatePriceAndStock(sku);
		    				} else {
		    					System.out.println("saveSKU异常");
		    					e.printStackTrace();
		    				}
		    			} catch (ServiceException e1) {
		    				System.out.println("updatePriceAndStock异常");
		    				e1.printStackTrace();
		    			}
		    		}
		    		
		    		///////////////////////////////////保存图片///////////////////////////////////
		    		String picUrl = product.getIMAGEURL();
		    		productFetchService.savePicture(supplierId, null, product.getVARIANT_SKU(), Arrays.asList(picUrl));
				}
	        }
		    String category = "";
			for (Entry<String, TxtDTO> entry : spuMap.entrySet()) {
				///////////////////////////////////保存SPU/////////////////////////////////
            	SpuDTO spu = new SpuDTO();
            	
	            try {
	                spu.setId(UUIDGenerator.getUUID());
	                spu.setSupplierId(supplierId);
	                spu.setSpuId(entry.getKey());
	                spu.setSpuName(entry.getValue().getNAME());
	                spu.setBrandName(entry.getValue().getMANUFACTURER());
	                category=entry.getValue().getADVERTISERCATEGORY();
	                category = category.substring(category.lastIndexOf(">")+1);
	                spu.setCategoryName(category);
	                spu.setMaterial(entry.getValue().getMATERIAL());
	                spu.setCategoryGender(entry.getValue().getGENDER());
	                spu.setProductOrigin(MyTxtUtil.getOrigin(entry.getValue().getVARIANT_SKU()));
	                productFetchService.saveSPU(spu);
	            } catch (ServiceException e) {
	            	productFetchService.updateMaterial(spu);
	                e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<TxtDTO> getList(){
		  //download
        try {
            MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            loggerError.error("拉取数据失败！");
            e.printStackTrace();
        }
        //read .csv file
        List<TxtDTO> list = null;
        try {
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            loggerError.error("解析文件失败！");
            e.printStackTrace();
        }
        logger.info("解析数据条数："+list.size());
        return list;
	}
}