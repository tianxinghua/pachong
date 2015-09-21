package com.shangpin.iog.tony.service;

/**
 * Created by Administrator on 2015/9/21.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.onsite.base.utils.MyJsonUtil;
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
        for(Items item:array){
            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId("");
                spu.setSpuId(item.getAge());
                spu.setBrandName(item.getAge());
                spu.setCategoryName(item.getAge());
                spu.setSpuName(item.getAge());
                spu.setSeasonId(item.getAge());
                spu.setMaterial(item.getAge());
                spu.setCategoryGender(item.getAge());
                productFetchService.saveSPU(spu);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            //
            SkuDTO sku  = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId("");

                sku.setSpuId(item.getAge());
                sku.setSkuId("");
                sku.setProductSize(item.getAge());
                sku.setMarketPrice(item.getAge());
                sku.setSalePrice(item.getAge());
                sku.setSupplierPrice(item.getAge());
                sku.setColor(item.getColor());
                sku.setProductDescription(item.getAge());
                sku.setStock(item.getAge());
                sku.setBarcode(item.getBarcode());
                sku.setProductCode(item.getAge());
                productFetchService.saveSKU(sku);

                if(StringUtils.isNotBlank(item.getRemote_images()[0])){
                    String[] picArray = item.getRemote_images();

//                            List<String> picUrlList = Arrays.asList(picArray);
                    for(String picUrl :picArray){
                        ProductPictureDTO dto  = new ProductPictureDTO();
                        dto.setPicUrl(picUrl);
                        dto.setSupplierId("");
                        dto.setId(UUIDGenerator.getUUID());
                        dto.setSkuId("");
                        try {
//                                    productFetchService.savePicture(dto);
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
