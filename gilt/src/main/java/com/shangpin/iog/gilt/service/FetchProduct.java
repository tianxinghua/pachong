package com.shangpin.iog.gilt.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.gilt.dto.AttributeDTO;
import com.shangpin.iog.gilt.dto.GiltSkuDTO;
import com.shangpin.iog.gilt.dto.InventoryDTO;
import com.shangpin.iog.gilt.dto.SaleDTO;
import com.shangpin.iog.product.service.ProductFetchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.lang.StringUtils;
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
    private static String url;


    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
        url = bdl.getString("url");
    }
    @Autowired
    ProductFetchService productFetchService;
    //String supplierId = "";//

    public void fetchProductAndSave(){
        /*String jsonStr = getSkus(url);*/
        //List<String>mlist=new ArrayList<>();
        String saleIds=bdl.getString("saleId");
        boolean useSaleId = true;
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isBlank(saleIds)){
            useSaleId=false;
        }else{
            String[] saleIdArray = saleIds.split(",");
            if(null!=saleIdArray||saleIdArray.length>0){
                for(int i=0;i<saleIdArray.length;i++){
                    map.put(saleIdArray[i],"");
                }
            }
        }



        //sale url
        String  sale = url + "/global/sales";
        //get sale message
        List<SaleDTO>  saleList = this.getSaleMessage(sale);



        OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10);

        //获取产品的信息
        String skuMsg = "",saleInventoryUrl="",saleSkuUrl="";
        String inventoryMsg="";
        Gson gson = new Gson();
        Map<String,String> spuMap = new HashMap<>();
        String saleId="";
        int offset = 0;
        Map<String,String> param = new HashMap<>();
        Map<String,String> inventoryMap = new HashMap<>();
        List<InventoryDTO> saleInventoryList = null;
        List<GiltSkuDTO> saleSkuList = null;
        for(SaleDTO saleDTO:saleList){
            saleId = saleDTO.getId();
            if(useSaleId){  //使用过滤
                //未包含的不执行
                if(!map.containsKey(saleId)) continue;
            }
            System.out.println("sale id " + saleId);
            saleInventoryUrl = sale + "/" + saleId+"/realtime-inventory";
            saleSkuUrl = sale + "/" + saleId + "/skus";


            //page call sku message ,and get inventory from map


            do {
                param.put("offset",offset+"");
                inventoryMsg=HttpUtil45.get(saleInventoryUrl, outTimeConf, param,key,"");
                try {
                    saleInventoryList=gson.fromJson(inventoryMsg, new TypeToken<List<InventoryDTO>>() {
                    }.getType());

                    System.out.println("saleInventoryList size  " + saleInventoryList.size() );
                    for(InventoryDTO inventoryDTO:saleInventoryList){
                        inventoryMap.put(inventoryDTO.getSku_id(),inventoryDTO.getQuantity()) ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                offset=offset+100;

            }while (null!=saleInventoryList&&saleInventoryList.size()==100);

            System.out.println("inventoryMap size " + inventoryMap.size() );


            offset=0;
             do {
                param.put("offset",offset+"");
                skuMsg=HttpUtil45.get(saleSkuUrl, outTimeConf, param,key,"");

                logger.info("sale id : " + saleId +" skuMsg value =" + skuMsg);
                System.out.println("sale id : " + saleId +" skuMsg value =" + skuMsg);
                try {

                    saleSkuList=gson.fromJson(skuMsg, new TypeToken<List<GiltSkuDTO>>() {
                    }.getType());
                    System.out.println(saleId + " sku = " + saleSkuList.size());

                    for(GiltSkuDTO giltSkuDTO:saleSkuList){
                        this.saveProduct(spuMap,giltSkuDTO,inventoryMap,saleDTO);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                offset=offset+100;
             }while (null!=saleSkuList&&saleSkuList.size()==100);






//           List<String> skuIdList  =  saleDTO.getSku_ids();
//            if(null!=skuIdList){
//                for(String skuId:skuIdList){
//                    skuMsg =  HttpUtil45.get(skusUrl+skuId, outTimeConf, null,key,"");
//                    if(skuMsg.equals(HttpUtil45.errorResult)){//链接异常
//                         continue;
//                    }else{
//                        try {
//                            GiltSkuDTO giltSkuDTO =    gson.fromJson(skuMsg, new TypeToken<GiltSkuDTO>() {
//                            }.getType());
//                            this.saveProduct(spuMap,giltSkuDTO);
//                        } catch (JsonSyntaxException e) {
//                            e.printStackTrace();
//                            continue;
//                        }
//
//                    }
//
//                }
//            }
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

    private void saveProduct(Map<String,String> spuMap, GiltSkuDTO giltSkuDTO,Map<String,String> inventoryMap,SaleDTO saleDTO) {
    	OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
    	String fileName = "";
    	SkuDTO dto = new SkuDTO();
        SpuDTO spuDTO = new SpuDTO();
        String inventory="";
        if(null==inventoryMap||inventoryMap.size()==0) {
             inventory = getInventory(giltSkuDTO.getId());
        }else{
            inventory = inventoryMap.containsKey(giltSkuDTO.getId())?inventoryMap.get(giltSkuDTO.getId()):"0";
        }

        if(!"0".equals(inventory)){

            String productCode="";
            String color ="",material="",size="",style="";
            List<AttributeDTO> attributes = giltSkuDTO.getAttributes();
            for(AttributeDTO attributeDTO:attributes){
                if(null!=attributeDTO.getColor()){
                    color= attributeDTO.getColor().getName();
                }
                if(null!=attributeDTO.getMaterial()) {
                    material = attributeDTO.getMaterial().getValue();
                }
                if(null!=attributeDTO.getSize()){
                    size = attributeDTO.getSize().getValue();
                }

            }

            spuDTO.setId(UUIDGenerator.getUUID());
            spuDTO.setSupplierId(supplierId);
            spuDTO.setSpuId(giltSkuDTO.getProduct_id());
            spuDTO.setMaterial(material);
            spuDTO.setCategoryId(giltSkuDTO.getCategories().get(0).getId());
            spuDTO.setCategoryName(giltSkuDTO.getCategories().get(0).getName());
            spuDTO.setSubCategoryId(giltSkuDTO.getCategories().get(giltSkuDTO.getCategories().size()-1).getId());
            spuDTO.setSubCategoryName(giltSkuDTO.getCategories().get(giltSkuDTO.getCategories().size()-1).getName());
            spuDTO.setBrandId(giltSkuDTO.getBrand().getId());
            spuDTO.setBrandName(giltSkuDTO.getBrand().getName());
            spuDTO.setProductOrigin(giltSkuDTO.getCountry_code());
            spuDTO.setCategoryGender(giltSkuDTO.getCategories().get(0).getName());

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
                dto.setColor(color);
                dto.setMarketPrice(giltSkuDTO.getPrices().getRetail().getValue());
                dto.setSalePrice(giltSkuDTO.getPrices().getSale().getValue());
                dto.setSaleCurrency(giltSkuDTO.getPrices().getRetail().getCurrency());
                dto.setSupplierPrice(giltSkuDTO.getPrices().getCost().getValue());
                String[]str=giltSkuDTO.getProduct_codes();
                for (int j=0;j<str.length;j++){
                    productCode+=str[j]+"-";
                }
                dto.setProductCode(productCode.substring(0,productCode.length()-1));
                dto.setProductName(giltSkuDTO.getName());
                dto.setProductSize(size);
                dto.setStock(inventory);
                dto.setEventStartDate(saleDTO.getStart_datetime());
                dto.setEventEndDate(saleDTO.getEnd_datetime());
                productFetchService.saveSKU(dto);
                for(int j =0;j<giltSkuDTO.getImages().size();j++){
                    ProductPictureDTO productPictureDTO= new ProductPictureDTO();
                    String picUrl=giltSkuDTO.getImages().get(j).getUrl();
                    productPictureDTO.setId(UUIDGenerator.getUUID());
                    productPictureDTO.setPicUrl(picUrl);
                    productPictureDTO.setSkuId(giltSkuDTO.getId());
                    productPictureDTO.setSupplierId(supplierId);
                    try{
                        productFetchService.savePictureForMongo(productPictureDTO);
                        fileName = giltSkuDTO.getId()+"-"+String.valueOf(j)+".jpg";
                        HttpUtil45.getPicture(picUrl, fileName, timeConfig, null);
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

        try {

            OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5,1000*60*5,1000*60*5);

            result=HttpUtil45.get(saleUrl, outTimeConf, null,key,"");
            logger.info("get result = " + result);

            Gson gson =   new Gson();
            try {
                returnList=gson.fromJson(result, new TypeToken<List<SaleDTO>>(){}.getType());
            } catch (Exception e) {
                loggerError.error("get sale message error :" + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }



    private static String getInventory(String skuId){
        String urlInventory = url+"/global/realtime-inventory/";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*10,1000*10,1000*10);
        String jsonStr = HttpUtil45.get(urlInventory+skuId,outTimeConf,param,key,"");
        logger.info("get skuId :"+skuId +" 库存返回值为："+jsonStr );
        System.out.println("get skuId :"+skuId +" 库存返回值为："+jsonStr );
        String inventory = null;
        try {
            inventory = getInventoryByJsonString(jsonStr);

        } catch (Exception e) {
            inventory="0";
            loggerError.error("");
            e.printStackTrace();
        }
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
     * JSON反序列化为Java对象集合
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
//        String url = "https://api-sandbox.gilt.com/global/skus";
//        List<GiltSkuDTO>list = new ArrayList<>();
//        List<GiltSkuDTO>returnList = new ArrayList<>();
//        List<String>testList=new ArrayList<>();
//        try {
//            Map<String,String> param = new HashMap<>();
//            /*param.put("sku_id","144740");*/
//            OutTimeConfig outTimeConf = new OutTimeConfig();
//            String result=""/*HttpUtil45.get(url, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","")*/;
//            int offset=0;
//            int limit=50;
//            do {
//                //param.put("limit","50");
//                param.put("offset",offset+"");
//                result=HttpUtil45.get(url, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","");
//                list=getObjectsByJsonString(result);
//                returnList.addAll(list);
//                offset=offset+50;
//                System.out.println("当前sku index："+offset);
//            }while (list.size()==limit);
//            for (int i =0;i<returnList.size();i++){
//                if(!testList.contains(returnList.get(i).getId())){
//                    testList.add(returnList.get(i).getId());
//                }
//            }
//            System.out.println("不重复的ID有："+testList.size());
//            for(int i=0;i<testList.size();i++){
//                System.out.println(testList.get(i));
//            }
//            //System.out.println(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
       /* String inven = getInventoryByJsonString(getInventory("144740"));
        System.out.println("库存"+inven);*/


        StringBuffer buffer = new StringBuffer();
        buffer.append("");
//        Gson gson =   new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//                .create();
        Gson gson =   new Gson();
        try {
            List<SaleDTO> returnList=gson.fromJson(buffer.toString(), new TypeToken<List<SaleDTO>>(){}.getType());
            System.out.println("list size = " + returnList.size());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
