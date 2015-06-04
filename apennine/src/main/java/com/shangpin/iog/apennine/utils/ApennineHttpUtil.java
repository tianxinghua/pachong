package com.shangpin.iog.apennine.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.domain.Product;
import com.shangpin.iog.common.utils.httpclient.HttpUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/6/2.
 */
public class ApennineHttpUtil {
    @Autowired
    HttpUtil httpUtilService;
    private Product getObjectByJsonString(String jsonStr) {
        Product obj =null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, Product.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private  List<Product>  getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<Product> objs = new ArrayList<Product>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<Product>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }
    private  List<Product>getProductsByUrl(String url){
        List<Product>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.getData(url,false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }
    private  List<Product>getProductsByUrlAndParam(String url,NameValuePair[] data){
        List<Product>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.postData(url, data,false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }
}
