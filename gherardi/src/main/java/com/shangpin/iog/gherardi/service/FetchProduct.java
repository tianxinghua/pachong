package com.shangpin.iog.gherardi.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

import com.shangpin.iog.prodottimonti.dto.*;
import com.shangpin.iog.service.ProductFetchService;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by loyalty on 15/9/22.
 */
@Component("gherardi")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    public void fetchProductAndSave(String url){

        String supplierId = "201509291000";
        try {

            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(3600000);
            timeConfig.confSocketOutTime(3600000);
            String jsonstr = HttpUtil45.get(url,timeConfig,null);

            mongMap.put("supplierId", supplierId);
            mongMap.put("supplierName","gherardi");
            if (jsonstr == null) {
                logMongo.info("获取供应商商品列表失败");
                return;
            }

            List<ItemsTmp> itemslist = new Gson().fromJson(jsonstr, new TypeToken<List<ItemsTmp>>() {}.getType());
            Products products = txt2Ojb(itemslist);
            List<Product> productList = products.getProducts();
            for(Product product:productList){
                SpuDTO spu = new SpuDTO();


                Items items = product.getItems();
                if(null==items){//无SKU
                    continue;
                }

                List<Item> itemList = items.getItems();
                if(null==itemList) continue;
                String skuId = "";
                for(Item item:itemList){
                    SkuDTO sku  = new SkuDTO();
                    try {
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSupplierId(supplierId);
                        sku.setSpuId(product.getProductId());
                        sku.setSkuId(item.getItem_id());
                        sku.setProductSize(item.getItem_size());
                        sku.setMarketPrice(item.getMarket_price());
                        sku.setSalePrice(item.getSell_price());
                        sku.setSupplierPrice(item.getSupply_price());
                        sku.setColor(item.getColor());
                        sku.setProductDescription(item.getDescription());
                        sku.setStock(item.getStock());
                        sku.setProductCode(product.getProducer_id());
                        sku.setSaleCurrency(item.getSaleCurrency());
                        productFetchService.saveSKU(sku);

                        if(StringUtils.isNotBlank(item.getPicture())){
                            String[] picArray = item.getPicture().split("\\|");
                            for(String picUrl :picArray){
                                ProductPictureDTO dto  = new ProductPictureDTO();
                                dto.setPicUrl(picUrl);
                                dto.setSupplierId(supplierId);
                                dto.setId(UUIDGenerator.getUUID());
                                dto.setSkuId(item.getItem_id());
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

                try {
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(product.getProductId());
                    spu.setBrandName(product.getProduct_brand());
                    spu.setCategoryName(product.getCategory());
                    spu.setSpuName(product.getProduct_name());
                    spu.setSeasonId(product.getSeason_code());
                    spu.setMaterial(product.getProduct_material());
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Products txt2Ojb(List<ItemsTmp> list) {

        Products products = new Products();
        List<Product> plist = new ArrayList<Product>();
        List<Item> itemsList = null;
        Items items = null;
        Product product = null;
        String spu = "";
        for (ItemsTmp itemsTmp : list){
            if (StringUtils.isNotEmpty(itemsTmp.getStock()) && Integer.valueOf(itemsTmp.getStock()) > 0){
                if (!itemsTmp.getSpu().equals(spu)){
                    itemsList = new ArrayList<Item>();
                    items = new Items();
                    product = new Product();
                    product.setCategory("");
                    product.setDescription(itemsTmp.getDescription());
                    product.setProduct_brand(itemsTmp.getBrand());
                    product.setProductId(itemsTmp.getSpu());
                    product.setProduct_name(itemsTmp.getName());
                    product.setProduct_material(itemsTmp.getFabric());
                    Item item = new Item();
                    item.setItem_id(itemsTmp.getSku());
                    item.setColor(itemsTmp.getColor());
                    item.setDescription(itemsTmp.getDescription());
                    item.setItem_size(itemsTmp.getProduct_size());
                    item.setPicture(itemsTmp.getImagelink());
                    item.setStock(itemsTmp.getStock());
                    item.setSaleCurrency(itemsTmp.getSaleCurrency());
                    itemsList.add(item);
                    items.setItems(itemsList);
                    product.setItems(items);
                    plist.add(product);
                    spu = itemsTmp.getSpu();
                }else{
                    Item item = new Item();
                    item.setItem_id(itemsTmp.getSku());
                    item.setColor(itemsTmp.getColor());
                    item.setDescription(itemsTmp.getDescription());
                    item.setItem_size(itemsTmp.getProduct_size());
                    item.setPicture(itemsTmp.getImagelink());
                    item.setStock(itemsTmp.getStock());
                    item.setSell_price(itemsTmp.getSell_price());
                    item.setSaleCurrency(itemsTmp.getSaleCurrency());
                    itemsList.add(item);
                    items.setItems(itemsList);
                    product.setItems(items);
                    plist.add(product);
                    spu = itemsTmp.getSpu();
                }
            }
        }
        products.setProducts(plist);
        return products;
    }
}
