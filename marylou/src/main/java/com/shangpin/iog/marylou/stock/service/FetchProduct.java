package com.shangpin.iog.marylou.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.marylou.stock.dto.Products;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/17.
 */
@Component("marylou")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    private ProductFetchService productFetchService;

    public void fetchProductAndSave(){

        String url = "http://shop.marylou.it/shangpin.aspx";
        String json = "";
        Products products = null;
        try {
            long startMili=System.currentTimeMillis();
            json = HttpUtil45.get(url,new OutTimeConfig(),null);
            long endMili=System.currentTimeMillis();
            System.out.println("source run times is "+(endMili-startMili)+" ms");
            System.out.println("----------"+json.substring(0,200));
            json = json.substring(json.indexOf("<products>"));
            // file convert into java bean
            products = ObjectXMLUtil.xml2Obj(Products.class, json);
            System.out.println(products.getProducts().size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            HttpUtil45.closePool();
        }
        System.out.println(json);
    }

    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }
}
