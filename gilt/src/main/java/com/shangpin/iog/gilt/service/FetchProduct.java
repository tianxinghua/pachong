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
    final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    ProductFetchService productFetchService;
    String supplierId = "";//
    String skusUrl="https://api-sandbox.gilt.com/global/skus";
    public void fetchProductAndSave(String url){
        /*String jsonStr = getSkus(url);*/
        List<GiltSkuDTO> list = this.getSkus(url);
        for(int i =0;i<list.size();i++){
            SkuDTO dto = new SkuDTO();
            SpuDTO spuDTO = new SpuDTO();
            GiltSkuDTO giltSkuDTO=list.get(i);
            dto.setId(UUIDGenerator.getUUID());
            dto.setSupplierId("201508081715");
            dto.setProductDescription(giltSkuDTO.getDescription());
            dto.setSkuId(giltSkuDTO.getId());
            dto.setSpuId(giltSkuDTO.getProduct_id());
            dto.setBarcode(giltSkuDTO.getProduct_look_id());
            dto.setColor(giltSkuDTO.getAttributes().get(0).getColor().getName());
            dto.setMarketPrice(giltSkuDTO.getPrices().getRetail().getValue());
            dto.setSalePrice(giltSkuDTO.getPrices().getSale().getValue());
            dto.setSaleCurrency(giltSkuDTO.getPrices().getSale().getCurrency());
            dto.setSupplierPrice(giltSkuDTO.getPrices().getCost().getValue());
            dto.setProductCode(giltSkuDTO.getProduct_codes().toString());
            dto.setProductName(giltSkuDTO.getName());
            dto.setProductSize(giltSkuDTO.getAttributes().get(3).getSize().getValue());
            dto.setStock(getInventory(giltSkuDTO.getId()));
            spuDTO.setId(UUIDGenerator.getUUID());
            spuDTO.setSupplierId("201508081715");
            spuDTO.setSpuId(giltSkuDTO.getProduct_id());
            spuDTO.setMaterial(giltSkuDTO.getAttributes().get(2).getMaterial().getValue());
            spuDTO.setCategoryId(giltSkuDTO.getCategories().get(0).getId());
            spuDTO.setCategoryName(giltSkuDTO.getCategories().get(0).getName());
            spuDTO.setSubCategoryId(giltSkuDTO.getCategories().get(3).getId());
            spuDTO.setSubCategoryName(giltSkuDTO.getCategories().get(3).getName());
            spuDTO.setBrandId(giltSkuDTO.getBrand().getId());
            spuDTO.setBrandName(giltSkuDTO.getBrand().getName());
            spuDTO.setProductOrigin(giltSkuDTO.getCountry_code());
            try{
                productFetchService.saveSKU(dto);
                productFetchService.saveSPU(spuDTO);
            }catch (ServiceException e) {
                e.printStackTrace();
            }
            for(int j =0;j<giltSkuDTO.getImages().size();j++){
                ProductPictureDTO productPictureDTO= new ProductPictureDTO();
                String picUrl=giltSkuDTO.getImages().get(j).getUrl();
                productPictureDTO.setId(UUIDGenerator.getUUID());
                productPictureDTO.setPicUrl(picUrl);
                productPictureDTO.setSkuId(giltSkuDTO.getId());
                productPictureDTO.setSpuId("");
                productPictureDTO.setSupplierId("201508081715");
                try{
                    productFetchService.savePictureForMongo(productPictureDTO);
                }catch (ServiceException e){
                    e.printStackTrace();
                }

            }
        }
    }
    private static String getInventory(String skuId){
        String url = "https://api-sandbox.gilt.com/global/inventory/";
        Map<String,String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        String jsonStr = HttpUtil45.get(url+skuId,outTimeConf,param,"fb8ea6839b486dba8c5cabb374c03d9d","");
        String inventory = getInventoryByJsonString(jsonStr);
        return inventory;
    }
    private static List<GiltSkuDTO> getSkus(String skusUrl){
        String result="";
        StringBuffer str=new StringBuffer();
        List<GiltSkuDTO>list = new ArrayList<>();
        List<GiltSkuDTO>returnList = new ArrayList<>();
        try {
            Map<String,String> param = new HashMap<>();
       // param.put("since","");
            //param.put("sku_ids","144740");
        int offset = 1;
        OutTimeConfig outTimeConf = new OutTimeConfig();
        do {
            param.put("limit","50");
            param.put("offset",offset+"");
            result=HttpUtil45.get(skusUrl, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","");
            list=getObjectsByJsonString(result);
            returnList.addAll(list);
            offset++;
            str.append(result);
        }while (list.size()<50);
            System.out.println("总页数："+offset);
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
        /*String url = "https://api-sandbox.gilt.com/global/skus";
        try {
            Map<String,String> param = new HashMap<>();
            param.put("sku_id","144740");
            OutTimeConfig outTimeConf = new OutTimeConfig();
            String result=HttpUtil45.get(url, outTimeConf, param,"fb8ea6839b486dba8c5cabb374c03d9d","");
            List<GiltSkuDTO> list  = getObjectsByJsonString(result);
            System.out.println("商品分类ID：" + list.get(0).getCategories().get(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        String inven = getInventoryByJsonString(getInventory("144740"));
        System.out.println("库存"+inven);
    }
}
