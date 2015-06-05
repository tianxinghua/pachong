package com.shangpin.iog.apennine.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil;
import com.shangpin.iog.dto.ApennineProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ApennineProductService;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sunny on 2015/6/2.
 */
public class ApennineHttpUtil {
    @Autowired
    HttpUtil httpUtilService;
    @Autowired
    ApennineProductService productService;
    private ApennineProductDTO getObjectByJsonString(String jsonStr) {
        ApennineProductDTO obj =null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, ApennineProductDTO.class);
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

    private  List<ApennineProductDTO>  getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<ApennineProductDTO> objs = new ArrayList<ApennineProductDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<ApennineProductDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }
    private List<ApennineProductDTO>getAllProducts(String url){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.getData(url, false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }

    private  List<ApennineProductDTO>getProductsByUrlAndParam(String url,NameValuePair[] data){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.getData(url, false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }
    private static List<SpuDTO> formatToSpu(List<ApennineProductDTO>list){
        List<SpuDTO>spuList=new ArrayList<>();
        SpuDTO spuDTO=new SpuDTO();
        String id="";
        for (int i = 0;i<list.size();i++){
            ApennineProductDTO dto=list.get(i);
            id=UUID.randomUUID().toString();
            spuDTO.setId(id.substring(0,8)+id.substring(9,13)+id.substring(14,18)+id.substring(19,23)+id.substring(24));
            spuDTO.setSeasonId(dto.getCat1());
            spuDTO.setBrandName(dto.getCat());
            spuDTO.setSpuId(dto.getScode());
            spuDTO.setSupplierId("1");
            spuList.add(spuDTO);
        }
        return spuList;
    }
    private static List<SkuDTO>formatToSku(List<ApennineProductDTO>list){
        List<SkuDTO>skuList=new ArrayList<>();
        SkuDTO skuDTO=new SkuDTO();
        String id="";
        for (int i=0;i<list.size();i++){
            ApennineProductDTO dto=list.get(i);
            id=UUID.randomUUID().toString();
            skuDTO.setId(id.substring(0,8)+id.substring(9,13)+id.substring(14,18)+id.substring(19,23)+id.substring(24));
            skuDTO.setProductCode(dto.getScode());
            skuDTO.setProductSize(dto.getSize());
            skuDTO.setColor(dto.getColor());
            skuDTO.setProductName(dto.getCdescript());
            skuDTO.setSupplierPrice(dto.getPricec());
            skuDTO.setSkuId(dto.getScode());
            skuDTO.setSpuId(dto.getScode());
            skuDTO.setSupplierId("1");
            skuList.add(skuDTO);
        }
        return skuList;
    }
    private static List<ProductPictureDTO> formatToPic(List<ApennineProductDTO>list){
        List<ProductPictureDTO>picList=new ArrayList<>();
        ProductPictureDTO picDTO=new ProductPictureDTO();
        String id="";
        for (int i=0;i<list.size();i++) {
            ApennineProductDTO dto = list.get(i);
            id=UUID.randomUUID().toString();
            picDTO.setId(id.substring(0,8)+id.substring(9,13)+id.substring(14,18)+id.substring(19,23)+id.substring(24));
            picDTO.setSupplierId("1");
            picDTO.setSkuId(dto.getScode());
            picDTO.setPicUrl("");
            picList.add(picDTO);
        }
        return picList;
    }
    public void insertApennineProducts(String url) throws ServiceException {
        List<ApennineProductDTO>dtos = getAllProducts(url);
        List<SkuDTO>skuDTOList=formatToSku(dtos);
        List<SpuDTO>spuDTOList=formatToSpu(dtos);
        List<ProductPictureDTO>picList=formatToPic(dtos);
        productService.insertSKU(skuDTOList);
        productService.insertSPU(spuDTOList);
        productService.insertPicture(picList);
    }

    /* public static void main(String args[]) {

         String url = "http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233";
         *//*NameValuePair[] data = {
                new NameValuePair("access_token", "6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd01c51b13b2dc624f8496fc85de3a7f6ce72554196bc78f1e0a0c78dfe433c1ace4"),
                new NameValuePair("page", "10"),
                new NameValuePair("limit", "100")


        };*//*
        try {
//          String kk= HttpUtil.getData("https://api.orderlink.it/v1/products?access_token=6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd010da5437c42e13b17de93a90c3fa2bee5e11d1723eb68026b1bc26f37152c8a38&page=10&limit=100",false);// HttpUtil.getData(url,false,true,"SHANGPIN","12345678");
//            String kk = HttpUtil.postData(url, null, false, true, "SHANGPIN", "12345678");
            String kk=HttpUtil.getData(url,false);
            String str="";
            List<ApennineProductDTO>list=new ArrayList<>();
            Gson gson = new Gson();
            list = gson.fromJson(kk, new TypeToken<List<ApennineProductDTO>>(){}.getType());
            List<SkuDTO>skuDTOList=formatToSku(list);
            List<SpuDTO>spuDTOList=formatToSpu(list);
            List<ProductPictureDTO>picList=formatToPic(list);
           // System.out.println("content = " + kk);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }*/
   /* private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args)
    {
        System.out.println("test start");
        loadSpringContext();
        System.out.println("test end");
    }*/
}
