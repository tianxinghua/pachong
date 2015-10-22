package com.shangpin.iog.levelgroup.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.levelgroup.dto.Item;
import com.shangpin.iog.levelgroup.dto.Items;
import com.shangpin.iog.levelgroup.dto.Product;
import com.shangpin.iog.levelgroup.dto.Products;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.sf.json.JSONObject;
import java.util.*;

/**
 * Created by loyalty on 15/9/22.
 */
@Component("levelgroup")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;

    private static ResourceBundle bdl=ResourceBundle.getBundle("conf");;

    public void fetchProductAndSave(String filepath){

        String supplierId = bdl.getString("supplierId");
        try {

            Map<String,String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig =new OutTimeConfig(1000*100*60,1000*100*60,1000*100*60);

//            List<String> list = HttpUtil45.getContentListByInputSteam(filepath,timeConfig,null,null,null);
            List<String> list = LevlelgroupFtpUtil.readConfigFileForFTP(filepath);
//            HttpUtil45.closePool();
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","levelgroup");


            if (list == null || list.size() == 0) {
                logMongo.info("获取供应商商品列表失败");
                return;
            }
            Products products = txt2Ojb(list);

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
                    spu.setCategoryGender(product.getCategoryGender());
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Products txt2Ojb(List<String> rowlist){
        //从原始数据list集合，从解析价格，商品ID等
        List<Map<String,String>> list = getProductPartInfoList(rowlist);
        //调用详情接口补全材质，图片等信息
        if (list != null){
            return completeProduct(list);
        }
        return null;
    }


    protected static List<Map<String,String>> getProductPartInfoList(List<String> rowlist){
        List<Map<String,String>> list = new ArrayList<>();
        for (String row : rowlist){
            String[] rows = row.split("[\n]");
            for (String obj : rows){
                String[] p = obj.split("[\t]");
                String pic = "";
                if (p.length > 8){
                    pic = p[8];
                }
                if (p.length > 23) {
                    for (int i=24;i<p.length;i++){
                        if (StringUtils.isNotEmpty(p[i]) && p[i].length() > 6)
                            pic = pic +"|"+ p[i].replaceAll(",","|");
                    }
                }
                if (p[0].length() != 3 && p.length > 11){
                    Map<String,String> map = new LinkedHashMap<>();
                    map.put("id", p[0]);
                    map.put("price", p[11]);
                    map.put("saleprice", p[12]);
                    map.put("c_title", p[16]);
                    map.put("gender", p[18]);
                    map.put("picture", pic);
                    map.put("sku", p[17].length() == 0 ? p[20] : p[17] );
                    list.add(map);
                }
            }
        }
        return list;
    }


    private static Products completeProduct(List<Map<String,String>> list){
        if (list == null || list.size() == 0) return null;
        Products products = new Products();
        List<Product> plist = new ArrayList<Product>();

        int i = 0;
        for (Map<String, String> map : list) {
            String url = "http://www.ln-cc.com/dw/shop/v15_8/products/"+map.get("id")+"/availability?inventory_ids=09&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
            OutTimeConfig timeConfig =new OutTimeConfig(1000*160,1000*160,1000*160);
            String jsonstr = HttpUtils.get(url,3);
                    //(url,timeConfig,null,null,null);
            if( jsonstr != null && jsonstr.length() >0){
                JSONObject json = JSONObject.fromObject(jsonstr);
                if (!json.isNullObject() && !json.containsKey("fault")) {
                    JSONObject inveObj = json.getJSONObject("inventory");
                    int instock = 0;
                    boolean orderable = false;
                    if (!inveObj.isNullObject()){
                        instock = inveObj.getInt(
                                "stock_level");
                        orderable = inveObj.getBoolean("orderable");
                    }

                    if (instock > 0 && orderable) {
                        Item item = new Item();
                        Product product = new Product();
                        List<Item> itemslist = new ArrayList<Item>();
                        Items items = new Items();
                        product.setProductId(map.get("id"));
                        product.setProducer_id(map.get("sku"));
                        product.setCategoryGender(map.get("gender"));
                        if (!json.has("brand"))
                            continue;
                        product.setProduct_brand(json.getString("brand"));

                        product.setProduct_name(map.get("c_title"));
                        if (!json.has("c_madeIn"))
                            continue;
                        product.setProductOrigin(json.getString("c_madeIn"));
                        if (!json.has("c_categoryName"))
                            continue;
                        product.setCategory(json.getString("c_categoryName").replace(",",""));
                        if (!json.has("c_material"))
                            continue;
                        product.setProduct_material(json
                                .getString("c_material"));
                        if (!json.has("long_description"))
                            continue;
                        product.setDescription(json
                                .getString("long_description"));
                        if (!json.has("c_season"))
                            continue;
                        product.setSeason_code(json.getString("c_season"));
                        if (!json.has("c_colorDescription"))
                            continue;
                        item.setColor(json.getString("c_colorDescription"));

                        if (!json.has("long_description"))
                            continue;
                        item.setDescription(json
                                .getString("long_description"));

                        item.setPicture(map.get("picture"));
                        if (!json.has("c_size"))
                            continue;
                        item.setItem_size(json.getString("c_size"));
                        if (!json.has("ean"))
                            continue;

                        item.setItem_id(json.getString("ean"));
                        String price_f = map.get("price");
                        String saleprice_f = map.get("saleprice");
                        item.setStock(instock+"");
                        //解析货币单位和价格

                        item.setSaleCurrency(map.get("price").substring(price_f.indexOf(" ") + 1, price_f.length()));
                        item.setMarket_price(map.get("price").substring(0,price_f.indexOf(" ")+1));
                        item.setSell_price(map.get("saleprice").length() > 0 ? map.get("saleprice").substring(0, saleprice_f.indexOf(" ") + 1) : map.get("price").substring(0, price_f.indexOf(" ") + 1));
                        itemslist.add(item);
                        items.setItems(itemslist);
                        product.setItems(items);
                        plist.add(product);
                    }
                    i++;
                }
                System.out.println(i);
            }
        }
        products.setProducts(plist);
        return products;
    }
}
