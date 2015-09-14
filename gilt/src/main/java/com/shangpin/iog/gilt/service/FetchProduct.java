package com.shangpin.iog.gilt.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import com.shangpin.iog.gilt.dto.InventoryDTO;
import com.shangpin.iog.gilt.dto.SaleDTO;
import com.shangpin.iog.product.service.ProductFetchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by sunny on 2015/8/5.
 */
@Component("gilt")
public class FetchProduct {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }
    @Autowired
    ProductFetchService productFetchService;
    //String supplierId = "";//

    public void fetchProductAndSave(){
        /*String jsonStr = getSkus(url);*/
        //List<String>mlist=new ArrayList<>();

        //获取做活动的产品信息
        String skusUrl="https://api-sandbox.gilt.com/global/skus/";
        String  sale = "https://api-sandbox.gilt.com/global/sales";
        String  singleSku = "https://api-sandbox.gilt.com/global/skus/";
        List<SaleDTO>  saleList = this.getSaleMessage(sale);
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*5,1000*5,1000*5);
        //获取单个产品的信息
        String skuMsg = "";
        Gson gson = new Gson();
        Map<String,String> spuMap = new HashMap<>();
        for(SaleDTO saleDTO:saleList){
           List<String> skuIdList  =  saleDTO.getSku_ids();
            if(null!=skuIdList){
                for(String skuId:skuIdList){
                    skuMsg =  HttpUtil45.get(skusUrl+skuId, outTimeConf, null,key,"");
                    if(skuMsg.equals(HttpUtil45.errorResult)){//链接异常
                         continue;
                    }else{
                        GiltSkuDTO giltSkuDTO =    gson.fromJson(skuMsg, new TypeToken<GiltSkuDTO>() {
                        }.getType());
                        this.saveProduct(spuMap,giltSkuDTO);

                    }

                }
            }
        }



        //TODO  现只取活动的产品 ，不做活动的先不取
//        List<GiltSkuDTO> list = this.getSkus(url);
//
//        for(int i =0;i<list.size();i++){
//
//            GiltSkuDTO giltSkuDTO=list.get(i);
//
//            saveProduct(spuMap, giltSkuDTO);
//        }
    }

    private void saveProduct(Map<String,String> spuMap, GiltSkuDTO giltSkuDTO) {
        SkuDTO dto = new SkuDTO();
        SpuDTO spuDTO = new SpuDTO();

        String inventory=getInventory(giltSkuDTO.getId());
        if(!"0".equals(inventory)){
            String productCode="";

            spuDTO.setId(UUIDGenerator.getUUID());
            spuDTO.setSupplierId(supplierId);
            spuDTO.setSpuId(giltSkuDTO.getProduct_id());
            spuDTO.setMaterial(giltSkuDTO.getAttributes().get(2).getMaterial().getValue());
            spuDTO.setCategoryId(giltSkuDTO.getCategories().get(0).getId());
            spuDTO.setCategoryName(giltSkuDTO.getCategories().get(0).getName());
            spuDTO.setSubCategoryId(giltSkuDTO.getCategories().get(giltSkuDTO.getCategories().size()-1).getId());
            spuDTO.setSubCategoryName(giltSkuDTO.getCategories().get(giltSkuDTO.getCategories().size()-1).getName());
            spuDTO.setBrandId(giltSkuDTO.getBrand().getId());
            spuDTO.setBrandName(giltSkuDTO.getBrand().getName());
            spuDTO.setProductOrigin(giltSkuDTO.getCountry_code());
            try{
                if(!spuMap.containsKey("")){
                    productFetchService.saveSPU(spuDTO);
                    spuMap.put(spuDTO.getSpuId(),null);
                }

            }catch (ServiceException e) {
                e.printStackTrace();
                loggerError.error("保存SPU失败：" + e.getMessage());
            }
            try{
                dto.setId(UUIDGenerator.getUUID());
                dto.setSupplierId(supplierId);
                dto.setProductDescription(giltSkuDTO.getDescription());
                dto.setSkuId(giltSkuDTO.getId());
                dto.setSpuId(giltSkuDTO.getProduct_id());
                dto.setBarcode(giltSkuDTO.getProduct_look_id());
                dto.setColor(giltSkuDTO.getAttributes().get(0).getColor().getName());
                dto.setMarketPrice(giltSkuDTO.getPrices().getRetail().getValue());
                dto.setSalePrice(giltSkuDTO.getPrices().getSale().getValue());
                dto.setSaleCurrency(giltSkuDTO.getPrices().getSale().getCurrency());
                dto.setSupplierPrice(giltSkuDTO.getPrices().getCost().getValue());
                String[]str=giltSkuDTO.getProduct_codes();
                for (int j=0;j<str.length;j++){
                    productCode+=str[j]+",";
                }
                dto.setProductCode(productCode.substring(0,productCode.length()-1));
                dto.setProductName(giltSkuDTO.getName());
                dto.setProductSize(giltSkuDTO.getAttributes().get(3).getSize().getValue());
                dto.setStock(inventory);
                productFetchService.saveSKU(dto);
                for(int j =0;j<giltSkuDTO.getImages().size();j++){
                    ProductPictureDTO productPictureDTO= new ProductPictureDTO();
                    String picUrl=giltSkuDTO.getImages().get(j).getUrl();
                    productPictureDTO.setId(UUIDGenerator.getUUID());
                    productPictureDTO.setPicUrl(picUrl);
                    productPictureDTO.setSkuId(giltSkuDTO.getId());
                    productPictureDTO.setSpuId("");
                    productPictureDTO.setSupplierId(supplierId);
                    try{
                        productFetchService.savePictureForMongo(productPictureDTO);
                    }catch (ServiceException e){
                        e.printStackTrace();
                        loggerError.error("保存图片失败：" +  e.getMessage());
                    }
                }
            }catch (ServiceException e){
                if(e.getMessage().equals(ProductFetchServiceImpl.REPEAT_MESSAGE)){

                    try {
                        productFetchService.updatePriceAndStock(dto);
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                        loggerError.error("更新价格失败");

                    }
                }else{
                    loggerError.error("保存SKU失败：" + e.getMessage());
                }

            }

        }
    }

    private static List<SaleDTO> getSaleMessage(String saleUrl){
        String result="";
        StringBuffer str=new StringBuffer();
        List<SaleDTO> returnList = new ArrayList<>();
        int limit = 50;
        try {

            OutTimeConfig outTimeConf = new OutTimeConfig(1000*5,1000*5,1000*5);

            result=HttpUtil45.get(saleUrl, outTimeConf, null,key,"");
            Gson gson = new Gson();
            try {
                returnList=gson.fromJson(result, new TypeToken<List<SaleDTO>>(){}.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }



    private static String getInventory(String skuId){
        String url = "https://api-sandbox.gilt.com/global/inventory/";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        String jsonStr = HttpUtil45.get(url+skuId,outTimeConf,param,key,"");
        String inventory = getInventoryByJsonString(jsonStr);
        return inventory;
    }

    private static List<GiltSkuDTO> getSkus(String skusUrl){
        String result="";
        StringBuffer str=new StringBuffer();
        List<GiltSkuDTO>list = new ArrayList<>();
        List<GiltSkuDTO>returnList = new ArrayList<>();
        int limit = 50;
        try {
            Map<String,String> param = new HashMap<>();
       // param.put("since","");
            //param.put("sku_ids","144740");
        int offset = 50;
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*5,1000*5,1000*5);
        do {
            param.put("offset",offset+"");
            result=HttpUtil45.get(skusUrl, outTimeConf, param,key,"");
            list=getObjectsByJsonString(result);
            returnList.addAll(list);
            offset=offset+50;
            str.append(result);
        }while (list.size()==50);
            System.out.println("sku的index："+offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }
    private static String getInventoryByJsonString(String jsonStr){
        InventoryDTO obj=null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, InventoryDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.getQuantity();
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private static List<GiltSkuDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<GiltSkuDTO> objs = new ArrayList<GiltSkuDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<GiltSkuDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("get List<ApennineProductDTO> fail :"+e);
        }
        return objs;
    }
    public static void main(String[] args) {
        String url = "https://api-sandbox.gilt.com/global/skus";
        List<GiltSkuDTO>list = new ArrayList<>();
        List<GiltSkuDTO>returnList = new ArrayList<>();
        List<String>testList=new ArrayList<>();
        try {
            Map<String,String> param = new HashMap<>();
            /*param.put("sku_id","144740");*/
            OutTimeConfig outTimeConf = new OutTimeConfig();
            String result=""/*HttpUtil45.get(url, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","")*/;
            int offset=0;
            int limit=50;
            do {
                //param.put("limit","50");
                param.put("offset",offset+"");
                result=HttpUtil45.get(url, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","");
                list=getObjectsByJsonString(result);
                returnList.addAll(list);
                offset=offset+50;
                System.out.println("当前sku index："+offset);
            }while (list.size()==limit);
            for (int i =0;i<returnList.size();i++){
                if(!testList.contains(returnList.get(i).getId())){
                    testList.add(returnList.get(i).getId());
                }
            }
            System.out.println("不重复的ID有："+testList.size());
            for(int i=0;i<testList.size();i++){
                System.out.println(testList.get(i));
            }
            //System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* String inven = getInventoryByJsonString(getInventory("144740"));
        System.out.println("库存"+inven);*/
    }
}
