package com.shangpin.iog.havok.service;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

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
        String resultJson = null;
        String skuJson = null;
        String spuUrl ="http://webserv.havok.it/stock/v1/style.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin";
        String skuUrl = "http://webserv.havok.it/stock/v1/product.php?k=033bd94b1168d7e4f0d644c3c95e35bf&f=shangpin";
        Gson gson = new Gson();
        try{
            resultJson = HttpUtil45.get(spuUrl, new OutTimeConfig(), null);
            skuJson = HttpUtil45.get(skuUrl,new OutTimeConfig(),null);
            System.out.println("shuju======"+resultJson);
        }catch (Exception e){
            e.printStackTrace();
        }

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
