package com.shangpin.iog.tony.service;

/**
 * Created by wang on 2015/9/21.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.MyJsonClient;
import com.shangpin.iog.tony.common.StringUtil;
import com.shangpin.iog.tony.dto.Items;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("tony")
public class FetchProduct {

    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchService productFetchService;
    private MyJsonClient jsonClient = new MyJsonClient();
    private String itemsJson;
    private String categoriesJson;
    /**
     * fetch product and save into db
     */
    public void fetchProductAndSave() {
        //获取数据
        itemsJson =       jsonClient.httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        categoriesJson = jsonClient.httpPostOfJson(Constant.CATEGORIES_INPUT, Constant.CATEGORIES_URL);
        //解析数据
        Items[] array = new Gson().fromJson(new StringUtil().getItemsArray(itemsJson),
                new TypeToken<Items[]>() {}.getType());
        System.out.println("tony qty is ---------"+array.length);
        //保存数据
        messMappingAndSave(array);
    }
    /**
     * message mapping and save into DB
     */
    private void messMappingAndSave(Items[] array) {
        //get tony Category data
        String skuId = "";
        String spuId = "";
        String material = "";
        Map<String,String> map = new HashMap<>();

/*        for(Items item:array){
            skuId = item.get_id();
            map.put(StringUtil.getSkuID(skuId),"");
        }*/
        System.out.println(map.size());

        for(Items item:array){
            skuId = item.getSku();
            spuId = StringUtil.getSpuId(skuId);

            //
            SkuDTO sku  = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(Constant.SUPPLIER_ID);
                sku.setSpuId(spuId);
                sku.setSkuId(skuId);
                sku.setProductSize(StringUtil.getProductSize(skuId));
                sku.setMarketPrice(item.getStock_price());
                //sku.setSalePrice(item.getPurchase_price());
                //sku.setSupplierPrice(item.getAge());
                sku.setColor(item.getColor());
                sku.setProductDescription(item.getDesc());
                sku.setStock(item.getQty());
                sku.setBarcode(item.getBarcode());
                sku.setProductCode(StringUtil.getProductCode(skuId));
                sku.setSaleCurrency(item.getCur());
                productFetchService.saveSKU(sku);

            

            } catch (ServiceException e) {
                try {
                    if(e.getMessage().equals("数据插入失败键重复")){
                        //update
                        productFetchService.updatePriceAndStock(sku);
                    } else{
                        e.printStackTrace();
                    }

                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            } catch(Exception e){
                loggerError.error("tony 更新库存失败 " + e.getMessage());
            }
            
            
            if(null!=item.getImages()&& item.getImages().length>0&&StringUtils.isNotBlank(item.getImages()[0])){
                String[] picArray = item.getImages();
                productFetchService.savePicture(Constant.SUPPLIER_ID, null, skuId, Arrays.asList(picArray));
            }
            
            
/*            if (StringUtil.getMaterial(item.getDesc()) == null || "".equals(StringUtil.getMaterial(item.getDesc()))){
                System.out.println(skuId+"======================================================");
                continue;
            }*/
            //System.out.println(sku.getSupplierId()+"------------------------------------------");
            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(spuId);
                spu.setBrandName(item.getBrand());
                String categoryId = StringUtil.getCategoryID(item.getCat_id().toString());
                spu.setCategoryId(categoryId);
                spu.setCategoryName(StringUtil.getCategoryNameByID(categoryId, categoriesJson));

                //spu.setSpuName(item.getTitle_en());
                spu.setSeasonId(item.getSeason());
                String desc = item.getDesc();
                material = StringUtil.getMaterial(desc);
                if ("".equals(material)){
                    material = StringUtil.getMaterial(item.getDesc_en());
                }
                
                String productOrigin = null;
            	int index = desc.lastIndexOf("Made in");
            	if(index!=-1){
            		desc = desc.substring(desc.lastIndexOf("Made in"));
            		if(desc.indexOf("<br>")!=-1){
            			productOrigin = desc.substring(0,desc.indexOf("<br>"));
            		}
            	}
                spu.setProductOrigin(productOrigin);
                spu.setMaterial(material);
                spu.setCategoryGender(item.getSex());
                //System.out.println(spuId+"======================================");
                productFetchService.saveSPU(spu);

            } catch (ServiceException e) {
            	try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
            }
        }
    }

}
