package com.shangpin.iog.spinnaker.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.product.service.ProductFetchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.spinnaker.stock.dto.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/8.
 */
public class StartSkuJob {

    private ProductFetchService pfs = new ProductFetchServiceImpl();

    public static void main(String[] args) throws Exception {
        //首先获取季节码  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=8IZk2x5tVN
        String season_json = HttpUtils.get("http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key=8IZk2x5tVN");
        Gson gson = new Gson();
        SeasoncodeList season_list = gson.fromJson(season_json, new TypeToken<SeasoncodeList>(){}.getType());

        for (Seasoncode obj : season_list.getSeasonCode()){
            int i = 1;
            while (true){
                //然后根据季节码抓取sku  http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN
                String producturl = "http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=[[seasoncode]]&StartIndex=[[startindex]]&EndIndex=[[endindex]]&key=8IZk2x5tVN";
                String url = producturl.replaceAll("\\[\\[seasoncode\\]\\]", obj.getSeasonCode())
                        .replaceAll("\\[\\[startindex\\]\\]", "" + i)
                        .replaceAll("\\[\\[endindex\\]\\]","" + (i + 100));

                String json = null;
                try {
                    json = HttpUtils.get(url);
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
                        for (Spu spu : list.getProduct()) {
                            //spu入库
                            SpuDTO spudto = new SpuDTO();
                            spudto.setBrandName(spu.getProducer_id());
                            spudto.setCategoryGender(spu.getType());
                            spudto.setCategoryName(spu.getCategory());
                            spudto.setCreateTime(new Date());
                            spudto.setSeasonId(obj.getSeasonCode());
                            spudto.setSupplierId("2015051300260");
                            spudto.setSpuId(spu.getProduct_id());
                            spudto.setId(UUIDGenerator.getUUID());
                            spudto.setMaterial(spu.getProduct_detail());
                            spudto.setPicUrl(spu.getUrl());
                            //spudto.setSpuName();

                            for (Sku sku : spu.getItems().getItem()) {
                                //sku入库操作
                                SkuDTO skudto = new SkuDTO();
                                skudto.setCreateTime(new Date());
                                skudto.setBarcode(sku.getBarcode());
                                skudto.setColor(sku.getColor());
                                skudto.setId(sku.getItem_id());
                                //skudto.setLastTime();
                                skudto.setProductCode(spu.getProduct_id());
                                skudto.setProductDescription(spu.getProduct_detail());
                                skudto.setProductName(spu.getProduct_name());
                                skudto.setProductSize(sku.getItem_size());
                                //skudto.setSaleCurrency();
                                skudto.setSalePrice(spu.getSupply_price());
                                skudto.setSkuId(sku.getItem_id());
                                skudto.setSpuId(spu.getProduct_id());
                                skudto.setProductCode(spu.getProducer_id());
                                skudto.setStock(sku.getStock());
                                skudto.setSupplierId("2015051300260");
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

        System.exit(0);
    }

}
