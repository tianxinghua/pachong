package com.shangpin.iog.oupaiguoji.dto;

import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by loyalty on 15/6/5.
 */
public class Test {
    public static void main(String[] args){

        try {
            String kk=    HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx");
                Products products= ObjectXMLUtil.xml2Obj(Products.class, kk);
                List<Product> p=  products.getProducts();
            System.out.print("kk ========"+p.get(0).getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
