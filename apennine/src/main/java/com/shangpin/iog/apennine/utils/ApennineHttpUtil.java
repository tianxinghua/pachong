package com.shangpin.iog.apennine.utils;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.iog.apennine.domain.StockDTO;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.conf.ApiUrl;
import com.shangpin.iog.apennine.convert.ApennineProductConvert;
import com.shangpin.iog.apennine.domain.ApennineProductDTO;
import com.shangpin.iog.apennine.domain.ApennineProductPictureDTO;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by sunny on 2015/6/2.
 */
@Component("apennine")
public class ApennineHttpUtil {
    @Autowired
    ProductFetchService fetchService;
    static Logger logger =LoggerFactory.getLogger(ApennineHttpUtil.class);
    private ApennineProductDTO getObjectByJsonString(String jsonStr) {
        ApennineProductDTO obj =null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, ApennineProductDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("get ApennineProduct fail :"+e);
        }
        return obj;
    }
    private StockDTO getStockByJsonString(String jsonStr){
        StockDTO obj=null;
        Gson gson = new Gson();
        try {
            jsonStr=jsonStr.substring(1, jsonStr.length()-1);
            obj=gson.fromJson(jsonStr, StockDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("get Stock fail :"+e);
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
            logger.info("get List<ApennineProductDTO> fail :"+e);
        }
        return objs;
    }
    private List<ApennineProductPictureDTO> getPicsByjsonString(String jsonStr){
    	Gson gson = new Gson();
    	List<ApennineProductPictureDTO>objs=new ArrayList<ApennineProductPictureDTO>();
    	try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<ApennineProductPictureDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("get List<ApennineProductPictureDTO> fail :"+e);
        }
        return objs;
    }
    private List<ApennineProductDTO>getAllProducts(String url){
        List<ApennineProductDTO>list=new ArrayList<>();
        String jsonStr=HttpUtils.get(url);
        list=this.getObjectsByJsonString(jsonStr);
        return list;
    }
    public int getHkstockByScode(String url, String Scode) throws Exception{
    	Map<String,String> param = new HashMap<String, String>();
    	param = formatParam(param,Scode);
    	String jsonStr = HttpUtils.post(url, param);
        StockDTO dto = getStockByJsonString(jsonStr);
    	return dto.getHkstock();
    }
    public List<ApennineProductDTO>getProductsByUrlAndParam(String url, NameValuePair[] data){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr= HttpUtil45.get(url, new OutTimeConfig(), null);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("get List<ApennineProduct> fail :"+e);
        }
        return list;
    }
    public List<ApennineProductDTO>getProductsDetailsByUrl(String url){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr=HttpUtil45.get(url, new OutTimeConfig(), null);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("get List<ApennineProduct> fail :"+e);
        }
        return list;
    }
    /**
     * 产品实体转为SPU实体
     * @param list
     * @return
     */
    private List<SpuDTO> formatToSpu(List<ApennineProductDTO>list) throws Exception {
        List<SpuDTO>spuList=new ArrayList<>();
        for (int i = 0;i<list.size();i++){
            ApennineProductDTO dto=list.get(i);
            SpuDTO spuDTO = ApennineProductConvert.product2spu(dto);
            spuList.add(spuDTO);
        }
        return spuList;
    }
    /**
     * 产品实体转为SKU实体
     * @param list
     * @return
     */
    private List<SkuDTO>formatToSku(List<ApennineProductDTO>list) throws Exception {
        List<SkuDTO>skuList=new ArrayList<>();
        int stock=0;
        for (int i=0;i<list.size();i++){
            ApennineProductDTO dto=list.get(i);
            SkuDTO skuDTO=ApennineProductConvert.product2sku(dto);
            stock = this.getHkstockByScode(ApiUrl.STOCK,skuDTO.getSkuId().split("-")[0]);
            skuDTO.setStock(stock+"");
            skuList.add(skuDTO);
        }
        return skuList;
    }
    /**
     * 产品实体转为商品图片实体
     * @param list
     * @return
     */
    private List<ProductPictureDTO> formatToPic(List<ApennineProductDTO>list){
        List<ProductPictureDTO>picList=new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            ApennineProductDTO dto = list.get(i);
            List<ApennineProductPictureDTO> picurlList = this.getPictureUrlsByScode("http://112.74.74.98:8082/api/GetProductImg?userName=spin&userPwd=spin112233&scode="+dto.getScode());
            for (int j = 0; j < picurlList.size(); j++) {
            	 ProductPictureDTO picDTO=new ProductPictureDTO();
                 picDTO.setId(UUIDGenerator.getUUID());
                 picDTO.setSupplierId("2015070701319 ");
                 picDTO.setSkuId(dto.getScode());
                 picDTO.setPicUrl(picurlList.get(j).getScodePicSrc());
                 picList.add(picDTO);
			}
        }
        return picList;
    }
    public List<ApennineProductPictureDTO> getPictureUrlsByScode(String url){
    	String jsonStr=HttpUtils.get(url);
    	List<ApennineProductPictureDTO> list=new ArrayList<>();
    	list= this.getPicsByjsonString(jsonStr);
    	return list;
    }
    /**
     * 插入所有商品信息
     * @param url
     * @throws ServiceException
     */
    public void insertApennineProducts(String url) throws Exception {
        List<ApennineProductDTO>dtos = getAllProducts(url);
        List<SkuDTO>skuDTOList=formatToSku(dtos);
        List<SpuDTO>spuDTOList=formatToSpu(dtos);
        List<ProductPictureDTO>picList=formatToPic(dtos);
       for (int i = 0; i < skuDTOList.size(); i++) {
           try{
               fetchService.saveSKU(skuDTOList.get(i));
           }catch (Exception e){
               e.printStackTrace();
           }

		}
        for (int i = 0; i < spuDTOList.size(); i++) {
        	fetchService.saveSPU(spuDTOList.get(i));
		}
        for (int i = 0; i < picList.size(); i++) {
        	 fetchService.savePictureForMongo(picList.get(i));
		}
    }
    private Map<String,String> formatParam(Map<String,String> param,String scode){
    	param.put("ScodeAll", scode);
    	param.put("UserName", ApiUrl.userName);
    	param.put("UserPwd", ApiUrl.password);
    	return param;
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
  /*public static void main(String[] args) {
    	String url = "http://112.74.74.98:8082/api/GetProductPorperty?userName=spin&userPwd=spin112233&scode=COSEPIAF";
    	NameValuePair[] data = {
                new NameValuePair("scode", "")
        };
    	Map<String,String>map = new HashMap();
    	map.put("ScodeAll", "TUSKBLKF");
    	map.put("UserName", "spin");
    	map.put("UserPwd", "spin112233");
    	try {
			//String kk = HttpUtils.post(url,map);
    		String kk = HttpUtils.get(url);
			System.out.println("商品属性"+kk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
