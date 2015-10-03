package com.shangpin.iog.levelgroup.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.levelgroup.stock.dto.Item;
import com.shangpin.iog.levelgroup.stock.dto.Items;
import com.shangpin.iog.levelgroup.stock.dto.Product;
import com.shangpin.iog.levelgroup.stock.dto.Products;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by huxia on 2015/8/12.
 */
public class LevelGroupStockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = "2015092901551";
    }


    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();

        Map<String,String> mongMap = new HashMap<>();
        String url = "http://www.thelevelgroup-ftp.com/uploads/TLG_GooglePLA_lncc_GB.txt";
        Products products = null;
        List<String> list = null;

        list = HttpUtils.getContentListByInputSteam(url,null,"UTF-8",0);
        mongMap.put("supplierId",supplierId);
        mongMap.put("supplierName","levelgroup");

        products = txt2Ojb(list);
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
        logger.info("levelgroup赋值库存数据成功");
        return skustock;
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
                if (obj.indexOf("id") != 0){
                    String[] p = obj.split("[\t]");

                    String pic = "";
                    if (p.length > 8){
                        pic = p[8];
                    }
                    if (p.length > 21) {
                        for (int i=21;i<p.length;i++){
                            pic = pic + p[i];
                        }
                    }

                    if (p.length > 11){
                        Map<String,String> map = new LinkedHashMap<>();
                        map.put("id", p[0]);
                        map.put("price", p[11]);
                        map.put("saleprice", p[12]);
                        map.put("picture", pic);
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }


    private static Products completeProduct(List<Map<String,String>> list){
        if (list == null || list.size() == 0) return null;
        Products products = new Products();
        List<Product> plist = new ArrayList<Product>();
        for (Map<String, String> map : list) {
            String url = "http://www.ln-cc.com/dw/shop/v15_8/products/"
                    + map.get("id") + "/availability";
            Header[] header = new Header[1];
            Header h = new Header();
            h.setName("x-dw-client-id");
            h.setValue("8b29abea-8177-4fd9-ad79-2871a4b06658");
            header[0] = h;
            String jsonstr = HttpUtils.get(url, header,0);
            if( jsonstr != null){
                JSONObject json = JSONObject.fromObject(jsonstr);
                if (!json.containsKey("fault")) {
                    int instock = json.getJSONObject("inventory").getInt(
                            "stock_level");
                    boolean orderable = json.getJSONObject("inventory").getBoolean("orderable");
                    System.out.println(orderable);
                    if (instock > 0 && orderable) {
                        Item item = new Item();
                        Product product = new Product();
                        List<Item> itemslist = new ArrayList<Item>();
                        Items items = new Items();
                        product.setProducer_id(map.get("id"));
                        product.setProductId(map.get("id"));
                        product.setProduct_brand(json.getString("brand"));
                        product.setProduct_name(json.getString("c_title"));
                        product.setCategory(json
                                .getString("c_categoryName"));
                        product.setProduct_material(json
                                .getString("c_material"));
                        product.setDescription(json
                                .getString("long_description"));
                        product.setSeason_code(json.getString("c_season"));
                        item.setColor(json.getString("c_colorDescription"));
                        item.setDescription(json
                                .getString("long_description"));

                        item.setPicture(map.get("picture"));

                        item.setItem_size(json.getString("c_size"));
                        item.setItem_id(json.getString("ean"));
                        String price_f = map.get("price");
                        String saleprice_f = map.get("saleprice");
                        //解析货币单位和价格
                        item.setSaleCurrency(map.get("price").substring(price_f.indexOf(" ")+1, price_f.length()));
                        item.setMarket_price(map.get("price").substring(0,price_f.indexOf(" ")+1));
                        item.setSupply_price(map.get("saleprice").substring(0,saleprice_f.indexOf(" ")+1));
                        itemslist.add(item);
                        items.setItems(itemslist);
                        product.setItems(items);
                        plist.add(product);
                    }
                }
            }
        }
        products.setProducts(plist);
        return products;
    }

    public static void main(String args[]) throws Exception {

        AbsUpdateProductStock levelGroupStockImp = new LevelGroupStockImp();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("levelgroup更新数据库开始");
        //2015081401431
        levelGroupStockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
        logger.info("levelgroup更新数据库结束");
        System.exit(0);
    }
}
