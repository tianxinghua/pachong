package com.shangpin.iog.vela.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.vela.dto.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("vela")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());



    @Autowired
    private ProductFetchService pfs;

    public void fetchProductAndSave() {

        String  supplierId = "NEW2015071701343";

        //首先获取季节码
        String season_json = HttpUtil45.get("http://185.58.119.177/velashopapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=MPm32XJp7M",new OutTimeConfig(),null);
        Gson gson = new Gson();
        SeasoncodeList season_list = gson.fromJson(season_json, new TypeToken<SeasoncodeList>(){}.getType());
        String url="";
        for (Seasoncode obj : season_list.getSeasonCode()){
            int i = 1;
            while (true){
                //然后根据季节码抓取sku
                String producturl = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=MPm32XJp7M";
                url = producturl.replaceAll("\\[\\[seasoncode\\]\\]", obj.getSeasonCode())
                        .replaceAll("\\[\\[startindex\\]\\]", "" + i)
                        .replaceAll("\\[\\[endindex\\]\\]","" + (i + 100));

                String json = null;
                try {
                    json = HttpUtil45.get(url,new OutTimeConfig(),null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (json != null && !json.isEmpty()) {
                    Products list = null;
                    try {
                        list = gson.fromJson(json, new TypeToken<Products>() {}.getType());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (list != null && list.getProduct() != null) {
                        String priceUrl;
                        for (Spu spu : list.getProduct()) {
                            //spu入库
                            SpuDTO spudto = new SpuDTO();
                            spudto.setBrandName(spu.getProducer_id());
                            spudto.setCategoryGender(spu.getType());
                            spudto.setCategoryName(spu.getCategory());
                            spudto.setCreateTime(new Date());
                            spudto.setSeasonId(obj.getSeasonCode());
                            spudto.setSupplierId(supplierId);
                            spudto.setSpuId(spu.getProduct_id());
                            spudto.setId(UUIDGenerator.getUUID());
                            spudto.setMaterial(spu.getProduct_detail());
                            spudto.setPicUrl(spu.getUrl());
                            spudto.setSpuName(spu.getDescription());
                            try {
                                pfs.saveSPU(spudto);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }

                            for (Sku sku : spu.getItems().getItem()) {
                                //sku入库操作
                                SkuDTO skudto = new SkuDTO();
                                skudto.setCreateTime(new Date());
                                skudto.setBarcode(sku.getBarcode());
                                skudto.setColor(sku.getColor());
                                skudto.setId(UUIDGenerator.getUUID());
                                skudto.setProductCode(spu.getProduct_name());
                                skudto.setProductDescription(spu.getProduct_detail());
                                skudto.setProductName(spu.getDescription());
                                if(sku.getItem_size().length()>4) {
                                    skudto.setProductSize(sku.getItem_size().substring(0,sku.getItem_size().length()-4));
                                }else{
                                    skudto.setProductSize(sku.getItem_size());
                                }
                                skudto.setSkuId(sku.getItem_id());
                                String itemID = sku.getItem_id();
                                priceUrl = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetPriceByItemID?DBContext=Default&ItemID="+itemID+"&key=MPm32XJp7M";
                                try {
                                    json = HttpUtil45.get(priceUrl, new OutTimeConfig(), null);
                                }catch (IllegalArgumentException e){
                                    e.printStackTrace();
                                }
                                if(json != null && !json.isEmpty()){
                                    Price price = null;
                                    price = gson.fromJson(json,new TypeToken<Price>() {}.getType());
                                    if(price!=null&&price.getMarket_price()!=null||price.getSuply_price()!=null){
                                        skudto.setMarketPrice(price.getMarket_price());
                                        skudto.setSupplierPrice(price.getSuply_price().replace(",","."));
                                    }
                                }
                                skudto.setSpuId(spu.getProduct_id());
                                skudto.setStock(sku.getStock());
                                skudto.setSupplierId(supplierId);
                                try {
                                    pfs.saveSKU(skudto);
                                    for(String image : sku.getPictures()){
                                        ProductPictureDTO pic = new ProductPictureDTO();
                                        pic.setPicUrl(image);
                                        pic.setId(UUIDGenerator.getUUID());
                                        pic.setSkuId(sku.getItem_id());
                                        pic.setSupplierId(supplierId);
                                        try {
                                            pfs.savePictureForMongo(pic);
                                        } catch (ServiceException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ServiceException e) {
                                    try {
                                        if(e.getMessage().equals("数据插入失败键重复")){
                                            //更新价格和库存
                                            pfs.updatePriceAndStock(skudto);
                                        } else{
                                            e.printStackTrace();
                                        }

                                    } catch (ServiceException e1) {
                                        e1.printStackTrace();
                                    }


                                }


                            }
                        }
                    } else {
                        break;
                    }

                    if (list != null && list.getProduct() != null && list.getProduct().length < 100) {
                        break;
                    }
                }
                i += 100;
            }
        }

        HttpUtil45.closePool();
    }
}
