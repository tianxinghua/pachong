package com.shangpin.iog.leam.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.leam.dto.LeamDTO;
import com.shangpin.iog.leam.dto.TokenDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * Created by sunny on 2015/8/18.
 */
@Component("leam")
public class FetchProduct {
    @Autowired
    ProductFetchService productFetchService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    static String skuUrl="";//http://188.226.153.91/modules/api/v2/stock/";//请求sku地址
    static String tokenUrl="";//http://188.226.153.91/modules/api/v2/getToken/";
    static String user="shamping";
    static String password="PA#=k2xU^ddUc6Jm";
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
        tokenUrl = bdl.getString("token");
        skuUrl=bdl.getString("skuUrl");
    }
    public void fetchProductAndSave(String url){
        List<LeamDTO> list=getSkus(skuUrl);
        logger.info("拉到的数据量是:"+list.size());
        int rStock=0;
        int stockNum = 0;
        int picNum =0;
//        Map<String,String> skuMap = new HashMap<>();
        String size ="";
        for(int i =0;i<list.size();i++) {

            SkuDTO dto = new SkuDTO();
            SpuDTO spuDTO = new SpuDTO();

            LeamDTO leamDTO = list.get(i);

            try {
                if(Integer.valueOf(leamDTO.getQty())<=0){
                    stockNum++;
                    continue;
                } else{
                    rStock++;
                }
            } catch (NumberFormatException e) {
                loggerError.error(leamDTO.getStock_id() + ":" + leamDTO.getQty());
            }

            List<String> imageList = leamDTO.getImages();

            if (imageList!=null&&imageList.size()>0) {
                size = leamDTO.getSize();
                if(null==size) continue;
                size= size.replace(",", ".");
                dto.setId(UUIDGenerator.getUUID());
                dto.setSupplierId(supplierId);
                dto.setSkuId(leamDTO.getStock_id()+"-"+size);
                dto.setSpuId(leamDTO.getSupplier_sku());
                dto.setProductCode(leamDTO.getSupplier_sku());
                dto.setColor(leamDTO.getColor());
                dto.setMarketPrice(leamDTO.getDefault_price());
                dto.setSupplierPrice(leamDTO.getPrice());
                dto.setProductDescription(leamDTO.getDescription());
                dto.setProductSize(size);
                dto.setStock(leamDTO.getQty());
                try {
//                    if(skuMap.containsKey(dto.getSkuId())){
//                        loggerError.error("sku :" + leamDTO.getStock_id() + ",尺码：" + leamDTO.getSize());
//                        loggerError.error("sku :" + leamDTO.getStock_id() + ",原尺码：" +skuMap.get(leamDTO.getStock_id()));
//                    }
                    productFetchService.saveSKU(dto);
//                    skuMap.put(dto.getSkuId(),leamDTO.getSize());
                    for(String imgUrl:imageList){
                        ProductPictureDTO pictureDTO = new ProductPictureDTO();
                        pictureDTO.setId(UUIDGenerator.getUUID());
                        pictureDTO.setSkuId(leamDTO.getStock_id()+"-"+size);
                        pictureDTO.setSupplierId(supplierId);
                        pictureDTO.setPicUrl(imgUrl);
                        try {
                            productFetchService.savePictureForMongo(pictureDTO);
                        } catch (ServiceException e) {
                            loggerError.error("sku " + leamDTO.getStock_id() + " 保存图片失败 ");
                            e.printStackTrace();
                        }
                    }

                } catch (ServiceException e) {
                    try {
                        if (e.getMessage().equals("数据插入失败键重复")) {
                            productFetchService.updatePriceAndStock(dto);
                        } else {
                            e.printStackTrace();
                        }
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }

                spuDTO.setId(UUIDGenerator.getUUID());
                spuDTO.setSpuId(leamDTO.getSupplier_sku());
                spuDTO.setSupplierId(supplierId);
                //spuDTO.setCategoryGender(leamDTO.getNomenclature());
                spuDTO.setCategoryName(leamDTO.getCategory());
                spuDTO.setSubCategoryName(leamDTO.getSubcategory());
                spuDTO.setBrandName(leamDTO.getBrand());
                spuDTO.setMaterial(leamDTO.getComposition());
                spuDTO.setProductOrigin(leamDTO.getMadein());
                spuDTO.setSeasonName(leamDTO.getSeason());

                try {
                    productFetchService.saveSPU(spuDTO);
                } catch (Exception e) {
                    if (e.getMessage().equals("数据插入失败键重复")) {
                        loggerError.error(leamDTO.getSupplier_sku()+"保存错误.原因重复。");
                    } else {
                        loggerError.error(leamDTO.getSupplier_sku()+"保存错误.原因："+e.getMessage());
                    }

                }

            }else{
                picNum++;
            }
        }
        logger.info("正确的库存数据 = " + rStock);
        logger.info("无库存数据 ="+ stockNum);
        logger.info("无图片数据 ="+ picNum);
    }
    public  static List<LeamDTO> getSkus(String url){
        List<LeamDTO>list = new ArrayList<>();
        String result="";
        String token="";
        try {
            token=getToken(tokenUrl);
            Map<String, String> param = new HashMap<>();
            param.put("user",user);
            param.put("password",password);
            OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*15,1000*60*60,1000*60*60);
            result= HttpUtil45.post(url+"?t="+token, param, outTimeConf);
            System.out.println(" result = "+ result);
            logger.info(" result = "+ result);
            list = getObjectsByJsonString(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    private static String getToken(String tokenUrl){
        Map<String, String> param = new HashMap<>();
        OutTimeConfig outTimeConf = new OutTimeConfig();
        param.put("user",user);
        param.put("password",password);
        String result = HttpUtil45.post(tokenUrl,param,outTimeConf);
        result=getTokenByjsonstr(result);
        return result;
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private static List<LeamDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<LeamDTO> objs = new ArrayList<LeamDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<LeamDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }
    private static String getTokenByjsonstr(String jsonStr){
        Gson gson = new Gson();
        TokenDTO obj=null;
        try {
            obj=gson.fromJson(jsonStr, TokenDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj.getToken();
    }
    public static void main(String[] args){
        /*List<LeamDTO>list=getSkus(skuUrl);
        for(LeamDTO leamDTO:list){
            if(null!=leamDTO.getImage()&&leamDTO.getImage().size()>0) {
                 System.out.println("image size " + leamDTO.getSupplier_sku()+" " + leamDTO.getImage().size());
            }
        }*/
//        System.out.println("品牌是"+list.get(0).getBrand());
      /*String kk =   HttpUtil45.get("https://api.channeladvisor.com/oauth2/authorize?" +
                "client_id=qwmmx12wu7ug39a97uter3dz29jbij3j&response_type=code&scope=orders%20inventory" +
                "&redirect_uri=https://49.213.13.167:8443/iog/download/code",new OutTimeConfig(1000*60,1000*60,1000*60),null);
        System.out.println("kk = " + kk);*/

        /*String kkk =   HttpUtil45.get("https://api.channeladvisor.com/oauth2/authorize ?\n" +
                "    client_id = qwmmx12wu7ug39a97uter3dz29jbij3j &\n" +
                "    response_type = code &\n" +
                "    scope = orders inventory &\n" +
                "    redirect_uri = https://49.213.13.167:8443/iog/download/code",new OutTimeConfig(1000*60,1000*60,1000*60),null);
        System.out.println("kkk = " + kkk);*/
        FetchProduct product = new FetchProduct();
        product.getSkus(skuUrl);

    }

}
