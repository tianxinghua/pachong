package com.shangpin.iog.leam.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.leam.dto.LeamDTO;
import com.shangpin.iog.leam.dto.TokenDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunny on 2015/8/18.
 */
@Component("leam")
public class FetchProduct {
    @Autowired
    ProductFetchService productFetchService;
    static String supplierId = "2015082511263";//供应商ID
    static String skuUrl="http://188.226.153.91/modules/api/v2/stock/";//请求sku地址
    static String tokenUrl="http://188.226.153.91/modules/api/v2/getToken/";
    static String user="shamping";
    static String password="PA#=k2xU^ddUc6Jm";
    public void fetchProductAndSave(String url){
        List<LeamDTO>list=getSkus(skuUrl);
        for(int i =0;i<list.size();i++){
            SkuDTO dto = new SkuDTO();
            SpuDTO spuDTO = new SpuDTO();
            LeamDTO leamDTO=list.get(i);
            dto.setId(UUIDGenerator.getUUID());
            dto.setSupplierId(supplierId);
            dto.setSkuId(leamDTO.getStock_id());
            dto.setColor(leamDTO.getColor());
            dto.setSupplierPrice(leamDTO.getPrice());
            dto.setProductDescription(leamDTO.getDescription());
            dto.setProductSize(leamDTO.getSize());
            dto.setStock(leamDTO.getQty());
            spuDTO.setId(UUIDGenerator.getUUID());
            spuDTO.setSpuId(leamDTO.getSupplier_sku());
            spuDTO.setCategoryGender(leamDTO.getNomenclature());
            spuDTO.setCategoryName(leamDTO.getCategory());
            spuDTO.setSubCategoryName(leamDTO.getSubcategory());
            spuDTO.setBrandName(leamDTO.getBrand());
            spuDTO.setMaterial(leamDTO.getComposition());
            spuDTO.setProductOrigin(leamDTO.getMadein());
            spuDTO.setSeasonName(leamDTO.getSeason());
            try{
                productFetchService.saveSKU(dto);
                productFetchService.saveSPU(spuDTO);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static List<LeamDTO> getSkus(String url){
        List<LeamDTO>list = new ArrayList<>();
        String result="";
        String token="";
        try {
            token=getToken(tokenUrl);
            Map<String, String> param = new HashMap<>();
            param.put("user",user);
            param.put("password",password);
            OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*5,1000*60*5,1000*60*5);
            result= HttpUtil45.post(url+"?t="+token, param, outTimeConf);
            System.out.println(" result = "+ result);
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
//        List<LeamDTO>list=getSkus(skuUrl);
//        for(LeamDTO leamDTO:list){
//            if(null!=leamDTO.getImage()&&leamDTO.getImage().size()>0) {
//                 System.out.println("image size " + leamDTO.getSupplier_sku()+" " + leamDTO.getImage().size());
//            }
//        }
//        System.out.println("品牌是"+list.get(0).getBrand());
      String kk =   HttpUtil45.get("https://api.channeladvisor.com/oauth2/authorize?" +
                "client_id=qwmmx12wu7ug39a97uter3dz29jbij3j&response_type=code&scope=orders%20inventory" +
                "&redirect_uri=https://49.213.13.167:8443/iog/download/code",new OutTimeConfig(1000*60,1000*60,1000*60),null);
        System.out.println("kk = " + kk);

        String kkk =   HttpUtil45.get("https://api.channeladvisor.com/oauth2/authorize ?\n" +
                "    client_id = qwmmx12wu7ug39a97uter3dz29jbij3j &\n" +
                "    response_type = code &\n" +
                "    scope = orders inventory &\n" +
                "    redirect_uri = https://49.213.13.167:8443/iog/download/code",new OutTimeConfig(1000*60,1000*60,1000*60),null);
        System.out.println("kkk = " + kkk);
    }

}
