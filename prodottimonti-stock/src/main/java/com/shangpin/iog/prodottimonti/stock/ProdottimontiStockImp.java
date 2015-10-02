package com.shangpin.iog.prodottimonti.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.prodottimonti.stock.dto.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
public class ProdottimontiStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = "2015092910000";
    }


    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();

        Map<String,String> mongMap = new HashMap<>();
        String url = "http://www.communicationislife.com/shanping/prodotti/";
        try{
            OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
            timeConfig.confRequestOutTime(3600000);
            timeConfig.confSocketOutTime(3600000);
            String jsonstr = HttpUtil45.get(url,timeConfig,null);
            if (jsonstr == null) {
                logMongo.info("获取供应商商品列表失败");
                return null;
            }
            mongMap.put("supplierId",supplierId);
            mongMap.put("supplierName","prodottimonti");
            List<ItemsTmp> itemslist = new Gson().fromJson(jsonstr, new TypeToken<List<ItemsTmp>>() {}.getType());
            Products products = txt2Ojb(itemslist);

            List<Product> productList = products.getProducts();
            String skuId = "";
            for (Product product : productList) {

                Items items = product.getItems();
                if (null == items) {
                    continue;
                }
                List<Item> itemList = items.getItems();
                if(null==itemList) continue;
                for(Item item:items.getItems()){
                    if(StringUtils.isNotBlank(item.getStock()) ){

                        skuId = item.getItem_id();
                        if(skuId.indexOf("½")>0){
                            skuId = skuId.replace("½","+");
                        }
                        stockMap.put(skuId,item.getStock());
                    }
                }
            }

            for (String skuno : skuNo) {

                if(stockMap.containsKey(skuno)){
                    skustock.put(skuno, stockMap.get(skuno));
                } else{
                    skustock.put(skuno, "0");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        } finally {
            HttpUtil45.closePool();
        }
        logger.info("prodottimonti赋值库存数据成功");
        return skustock;
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

    public static void main(String args[]) throws Exception {

        AbsUpdateProductStock levelGroupStockImp = new ProdottimontiStockImp();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("prodottimonti更新数据库开始");
        //2015081401431
        levelGroupStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("prodottimonti更新数据库结束");
        System.exit(0);
    }
}
