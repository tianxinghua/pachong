package com.shangpin.iog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dao.Item;
import com.shangpin.iog.dao.Rss;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.utils.XMLUtil;

@Component("PullSku")
public class PullSku {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String url = "";
    private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("pullUrl");
    }
    
    @Autowired
    ProductFetchService productFetchService;
    
    public void pullAndSave(){
    	try{
    		
    		if(StringUtils.isNotBlank(url)){
        		url = url + XMLUtil.random();
        		System.out.println("url=="+url);
    			String result = HttpUtil45.get(url, outTimeConf,null);
//    			System.out.println(result);
    			if(!result.equals(HttpUtil45.errorResult)){
    				Rss rss = XMLUtil.gsonXml2Obj(Rss.class, result);
    				if(null != rss && null != rss.getChannel()){
    					
    					List<Item> items = rss.getChannel().getItem();
    					for(Item item : items){
    						//库存不为0，入库
    						String stock = item.getQty();
    						if(StringUtils.isNotBlank(stock) && Integer.parseInt(stock)>0){
    							SkuDTO sku = new SkuDTO();
    			                sku.setId(UUIDGenerator.getUUID());
    			                sku.setSupplierId(supplierId);
    			                sku.setSkuId(item.getSku());
    			                sku.setSpuId(item.getItem_group_id());
    			                sku.setProductName(item.getTitle());
    			                sku.setSupplierPrice(item.getPrice().replace("EUR", ""));
    			                sku.setProductCode(item.getItem_group_id());
    			                sku.setColor(item.getColor());
    			                sku.setProductDescription(item.getDescription());
    			                sku.setSaleCurrency("EUR");
    			                sku.setProductSize(item.getSize());
    			                sku.setStock(item.getQty());
    			                try {
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
			                    String imags = item.getImages();
			                    if(StringUtils.isNotBlank(imags)){
			                    	for(String imag : imags.split(";")){
			                    		if(StringUtils.isNotBlank(imag)){
			                    			list.add(imag);			                    			
			                    		}
			                    	}
			                    }
			                    productFetchService.savePicture(supplierId, null, sku.getSkuId(), list);
			                    
    			                //spu
    			                SpuDTO spu = new SpuDTO();
    			                spu.setId(UUIDGenerator.getUUID());
    			                spu.setSupplierId(supplierId);
    			                spu.setSpuId(item.getItem_group_id());
    			                spu.setCategoryName(item.getCategory());
    			                spu.setBrandName(item.getBrand());
    			                spu.setMaterial(item.getMaterial());
    			                spu.setCategoryGender("male");
    			                try {
    			                    productFetchService.saveSPU(spu);
    			                } catch (ServiceException e) {
    			                    e.printStackTrace();
    			                }
    			                
    						}
    					}
    				}else{
    					loggerError.error("无rss 或 channel");
    				}
    			}else{
    				loggerError.error("下载失败"+result);
    			}    			
    			
        	}else{
        		loggerError.error("url为空");
        	}
    		
    	}catch(Exception ex){
    		loggerError.error(ex);
    		ex.printStackTrace();
    	}
    	
    	
    }
    
}
