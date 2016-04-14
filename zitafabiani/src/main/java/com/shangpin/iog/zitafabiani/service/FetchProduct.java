package com.shangpin.iog.zitafabiani.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
//import com.shangpin.iog.zitafabiani.util.zitafabianiUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.zitafabiani.dto.Item;
import com.shangpin.iog.zitafabiani.utils.CVSUtil;
import com.shangpin.iog.zitafabiani.utils.DownLoad;

@Component("zitafabiani")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	private static String url = null;
	private static String local = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		local = bdl.getString("local");
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	public void fetchProductAndSave() {
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
			logger.info("=======开始下载文件=======");
			DownLoad.downFromNet(url, local);
			logger.info("=======下载结束=======");
			File file = new File(local);
			List<Item> lists = CVSUtil.readCSV(file, Item.class, ';');
			logger.info("======转化对象成功========");
			for(Item item:lists){
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                String skuId = item.getSupplier_sku_no()+"-"+item.getProduct_size();
                sku.setSkuId(skuId);
                sku.setSpuId(item.getSupplier_sku_no());
                sku.setProductName(item.getProduct_name());
                sku.setMarketPrice(item.getMarker_price());
                sku.setSupplierPrice(item.getSupplier_price());
                sku.setProductCode(item.getProduct_model());
                sku.setColor(item.getProduct_color());
                sku.setProductDescription(item.getProduct_description());
                sku.setSaleCurrency(item.getCurrency());
                sku.setProductSize(item.getProduct_size());
                sku.setStock(item.getStock());
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
              //保存图片
                List<String> list = new ArrayList<String>();
                list.add(item.getProduct_url1());
                list.add(item.getProduct_url2());
                list.add(item.getProduct_url3());
                try {
                	productFetchService.savePicture(supplierId, null, skuId, list);
				} catch (Exception e) {
					
				}
              //保存SPU
                SpuDTO spu = new SpuDTO();
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(item.getSupplier_sku_no());
                spu.setSupplierId(supplierId);
                spu.setCategoryGender(item.getGender());
                spu.setCategoryName(item.getCategory_name());
                spu.setBrandName(item.getBrand_name());
                spu.setSeasonName(item.getSeason());
                spu.setMaterial(item.getMaterial());
                spu.setProductOrigin(item.getProduct_origin());
                try {
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                	try{
	            		productFetchService.updateMaterial(spu);
	            	}catch(ServiceException ex){
	            		ex.printStackTrace();
	            	}
                    e.printStackTrace();
                }
                
			}
			
		} catch (Exception e) {
			logger.info(e);
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

