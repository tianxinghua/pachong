package com.shangpin.iog.articoli.service;

import com.shangpin.iog.articoli.dto.Products;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

import javax.xml.bind.JAXBException;
import java.util.ResourceBundle;


/**
 * Created by huxia on 2015/10/15.
 */
public class XmlTest {
    private static String local_file;
    private static ResourceBundle bdl=null;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            local_file = bdl.getString("local_file");
    }

    public static void main(String args[]){
        String json = "rtnData=={\"status\":\"ok\",\"data\":{\"" +
                "events\":[" +
                "{\"_id\":{\"$id\":\"561e6e2be4b0b71d53085f0f\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":1,\"date\":{\"sec\":1444834859,\"usec\":487000},\"" +
                "additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e6e21e4b0b71d53085f07\"}}}," +
                "{\"_id\":{\"$id\":\"561e745fe4b0269c2a643f68\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444836447,\"usec\":622000}," + "\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":2,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7455e4b0269c2a643f60\"}}}," +
                "\":{\"$id\":\"561e775ce4b0b5f049525a41\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837212,\"usec\":802000},\"" +
                "additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":1,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7752e4b0b5f049525a38\"}}}," +
                "{\"_id\":{\"$id\":\"561e77a2e4b0b5f049525a4b\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837282,\"usec\":801000},\"" +
                "additional_info\":{\"qty_diff\":-3,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7798e4b0b5f049525a42\"}}}," +
                "{\"_id\":{\"$id\":\"561e7d36e4b0c01df26d77e9\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444838710,\"usec\":853000},\"" +
                "additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e7d2ce4b0c01df26d77e0\"}}}]}}";

        System.out.println("json = " + json);

        /*Products products = null;
        try{
            products = ObjectXMLUtil.xml2Obj(Products.class,local_file);
            System.out.println(products.getProducts().length);
        } catch (JAXBException e) {
            e.printStackTrace();*/
    }
        /*File f = new File("F:/articoli.xml");
        System.out.println(f.length());*/
        //}

}
