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
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.onsite.base.utils.MyJsonUtil;
import com.shangpin.iog.onsite.base.utils.StringUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.tony.dto.Items;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("tony")
public class FetchProduct {
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //get tony return date
        String json = MyJsonUtil.getTonyJson();
        //formart data
        Items[] array = new Gson().fromJson(json,new TypeToken<Items[]>() {}.getType());
        //message mapping and save into DB
        messMappingAndSave(array);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Items[] array) {
        //get tony Category data
        String categoriesJson = MyJsonUtil.getTonyCategoriesJson();
        for(Items item:array){
            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPP_ID_TONY);
                spu.setSpuId(item.getCode());
                spu.setBrandName(item.getBrand());
                spu.setCategoryId(item.getCat_id().toString());
                spu.setCategoryName(StringUtil.getCategoryNameByID(item.getCat_id().toString(),categoriesJson));
                spu.setSpuName(item.getTitle_en());
                spu.setSeasonId(item.getSeason());
                spu.setMaterial(item.getMaterial_en());
                spu.setCategoryGender(item.getSex());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            //
            SkuDTO sku  = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(Constant.SUPP_ID_TONY);
                sku.setSpuId(item.getCode());
                sku.setSkuId(item.getSku());
                sku.setProductSize(item.getSize());
                sku.setMarketPrice(item.getStock_price());
                //sku.setSalePrice(item.getPurchase_price());
                //sku.setSupplierPrice(item.getAge());
                sku.setColor(item.getColor());
                sku.setProductDescription(item.getDesc());
                sku.setStock(item.getQty());
                sku.setBarcode(item.getBarcode());
                sku.setProductCode(item.getCode());
                productFetchService.saveSKU(sku);

                if(StringUtils.isNotBlank(item.getImages()[0])){
                    String[] picArray = item.getImages();
                    for(String picUrl :picArray){
                        ProductPictureDTO dto  = new ProductPictureDTO();
                        dto.setPicUrl(picUrl);
                        dto.setSupplierId(Constant.SUPP_ID_TONY);
                        dto.setId(UUIDGenerator.getUUID());
                        dto.setSkuId(item.getSku());
                        try {
                            productFetchService.savePictureForMongo(dto);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (ServiceException e) {
                try {
                    if(e.getMessage().equals("数据插入失败键重复")){
                        //更新价格和库存
                        productFetchService.updatePriceAndStock(sku);
                    } else{
                        e.printStackTrace();
                    }

                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
