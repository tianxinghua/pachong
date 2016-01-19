package com.shangpin.iog.woolrich.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
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
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.woolrich.dto.TxtDTO;
import com.shangpin.iog.woolrich.util.MyTxtUtil;

@Component("woolrich")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String urls;
	private static String brand;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		urls = bdl.getString("url");
		brand = bdl.getString("brand");
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
		List<TxtDTO> dtoLists = new ArrayList<TxtDTO>();
		try {
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			Map<String,SkuDTO> skuDTOMap = new HashMap<String,SkuDTO>();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			logger.info("开始抓取");
			
			for(String url : urls.split(",")){
				List<TxtDTO> txtlist = getList(url);
				if (txtlist!=null&&txtlist.size()>0) {
					dtoLists.addAll(txtlist);
				}
			}
		    for(TxtDTO product:dtoLists) {
		    	if (brand.contains(product.getMANUFACTURER().toLowerCase().trim())) {
		    		
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
		    		List<String> picList = new ArrayList<String>();
		    		picList.add(product.getIMAGEURL());
		    		picList.add(product.getALT_IMAGEURL_1().equals("&sm=fit")?"":product.getALT_IMAGEURL_1());
		    		picList.add(product.getALT_IMAGEURL_2().equals("&sm=fit")?"":product.getALT_IMAGEURL_2());
		    		picList.add(product.getALT_IMAGEURL_3().equals("&sm=fit")?"":product.getALT_IMAGEURL_3());
		    		picList.add(product.getALT_IMAGEURL_4().equals("&sm=fit")?"":product.getALT_IMAGEURL_4());
		    		productFetchService.savePicture(supplierId, null, product.getVARIANT_SKU(),picList);
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
	                spu.setSeasonName(entry.getValue().getSEASON());
	                spu.setProductOrigin(MyTxtUtil.getOrigin(entry.getValue().getVARIANT_SKU(),entry.getValue().getMANUFACTURER()));
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
	
	private List<TxtDTO> getList(String url){
		  //download
        try {
            MyTxtUtil.txtDownload(url);
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