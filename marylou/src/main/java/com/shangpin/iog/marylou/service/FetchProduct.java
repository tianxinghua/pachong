package com.shangpin.iog.marylou.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.marylou.dto.Item;
import com.shangpin.iog.marylou.dto.Items;
import com.shangpin.iog.marylou.dto.Product;
import com.shangpin.iog.marylou.dto.Products;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/17.
 */
@Component("marylou")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){

        //获取产品信息
//        logMongo.info("get product starting....");
    	String json = HttpUtil45.get(Constant.URL_MARYLOU, new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60), null);// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
        logMongo.info("get product over");
        //解析产品信息
        Products products = null;
        try {
            products = ObjectXMLUtil.xml2Obj(Products.class, json);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(products.getProducts().size());
        //映射数据并保存
        logMongo.info("save product into DB begin");
        messMappingAndSave(products);
        logMongo.info("save product into DB success");
 
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Products products) {
    	
    	int day = 90;
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(Constant.SUPP_ID_MARYLOU,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	
        List<Product> productList = products.getProducts();
        for (Product product : productList) {
            SpuDTO spu = new SpuDTO();

            Items items = product.getItems();
            if (null == items) {//判断SKU
                continue;
            }

            List<Item> itemList = items.getItems();
            if (null == itemList) continue;
            String skuId = "";
            for (Item item : itemList) {
                SkuDTO sku = new SkuDTO();
                try {
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(Constant.SUPP_ID_MARYLOU);

                    sku.setSpuId(product.getProductId());
                    skuId = item.getItemId();
                    if(skuId.indexOf("½")>0){
                        skuId = skuId.replace("½","+");
                    }

                    sku.setSkuId(skuId);

                    String itemSize = item.getItem_size();
                    if(itemSize.indexOf("½")>0){
                    	itemSize = itemSize.replace("½","+");
                    }
                    sku.setProductSize(itemSize);
                    sku.setSaleCurrency(item.getPrice_currency());
                    sku.setSupplierPrice(item.getSupply_price());
                    sku.setMarketPrice(item.getMarket_price());
                    sku.setColor(product.getColor());
                    sku.setProductDescription(product.getDescription());
                    sku.setStock(item.getStock());
                    
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
                
                String[] picArray = item.getPicture().split("\\|");
                productFetchService.savePicture(Constant.SUPP_ID_MARYLOU, null, item.getItemId(), Arrays.asList(picArray));
                
            }

            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPP_ID_MARYLOU);
                spu.setSpuId(product.getProductId());
                spu.setBrandName(product.getBrand());
                spu.setCategoryName(product.getCategory());
                spu.setSpuName(product.getName());
                spu.setSeasonId(product.getSeason());
                spu.setCategoryGender(product.getGender());
                spu.setMaterial(product.getMaterial());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
            	try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					// TODO Auto-generated catch block
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

    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
