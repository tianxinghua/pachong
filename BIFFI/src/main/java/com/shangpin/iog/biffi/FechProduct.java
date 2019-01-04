package com.shangpin.iog.biffi;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.biffi.dto.Detail;
import com.shangpin.iog.biffi.dto.Item;
import com.shangpin.iog.biffi.dto.Items;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("biffi")
public class FechProduct {
	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60,
			1000 * 60 * 5, 1000 * 60 * 5);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
	}

	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	
	public void fechAndSave(){
		
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		try{
			
			int page = 1;
			
			while(StringUtils.isNotBlank(uri)){
				String result = HttpUtil45.get(uri, outTimeConf, null);
				Items items = ObjectXMLUtil.xml2Obj(Items.class, result);
				List<Item> its = items.getItem();
				if(its.size()>0){
					for(Item item:its){
						try{							
							String spuId = item.getId();
							//入库spu
				            SpuDTO spu = new SpuDTO();
				            spu.setId(UUIDGenerator.getUUID());
				            spu.setSpuId(spuId);
				            spu.setSupplierId(supplierId);
				            spu.setBrandName(item.getBrand());
				            spu.setCategoryGender(item.getGender());
				            spu.setCategoryName(item.getCategory());
				            spu.setMaterial(item.getComposition());
				            spu.setProductOrigin(item.getMadein());
				            try {
				                productFetchService.saveSPU(spu);
				            } catch (ServiceException e) {
				            	logError.error(e.getMessage());
				            	try{
				            		productFetchService.updateMaterial(spu);
				            	}catch(ServiceException ex){
				            		logError.error(ex.getMessage());
				            		ex.printStackTrace();
				            	}
				            	
				                e.printStackTrace();
				            }
				            
				          //保存图片
				            try{
				            	
				            	productFetchService.savePicture(supplierId, spuId, null, item.getPhotos().getPhoto());
				            	
				            }catch(Exception ec){
				            	logError.error(ec);
				            	ec.printStackTrace();
				            }						
				            
							String detail = HttpUtil45.get("http://www.biffi.com/b2citem-detail/?idarticolo="+spuId, outTimeConf, null);
							Item it = ObjectXMLUtil.xml2Obj(Item.class, detail);
							List<Detail> dets = it.getDetails().getDetail();
							for(Detail de :dets){
								try{
									
									SkuDTO sku = new SkuDTO();
						            sku.setId(UUIDGenerator.getUUID());
						            sku.setSupplierId(supplierId);
									sku.setSkuId(de.getBarcode());
									sku.setSpuId(spuId);
									sku.setProductName(it.getName());
									sku.setMarketPrice(it.getPrezzo());
									sku.setBarcode(de.getBarcode());
									sku.setProductCode(spuId);
									sku.setColor(de.getColor());
									sku.setProductDescription(it.getDescription());
									sku.setProductSize(de.getSize());
									sku.setStock(de.getQty());
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
						                	logError.error(e1.getMessage());
						                    e1.printStackTrace();
						                }
						            }
									
								}catch(Exception e){
									logError.error(e);
									e.printStackTrace();
								}						
							}
							
						}catch(Exception ex){
							logError.error(ex);
							ex.printStackTrace();
						}	
						
					}
					if(its.size()>=50){
						page ++;
						uri = uri + page;
					}else{
						uri = null;
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
			
		}catch(Exception ex){
			ex.printStackTrace();
			logError.error(ex);
		}
	}
	
	
}
