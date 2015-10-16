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
        Products products = null;
        try{
            products = ObjectXMLUtil.xml2Obj(Products.class,local_file);
            System.out.println(products.getProducts().length);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        /*File f = new File("F:/articoli.xml");
        System.out.println(f.length());*/
    }

}
