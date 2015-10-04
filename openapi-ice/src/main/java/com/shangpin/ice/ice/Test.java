package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.ice.dto.ICEWMSOrderDTO;
import com.shangpin.iog.ice.dto.ICEWMSOrderRequestDTO;

import java.util.*;

/**
 * Created by lizhongren on 2015/7/10.
 */
public class Test {
    public static void main(String[] args){
//        System.out.println("----start-----");
//        int pageIndex=1,pageSize=100;
//        OpenApiServantPrx servant = null;
//        try {
//            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        boolean hasNext=true;
//        Set<String> skuIds = new HashSet<String>();
//        while(hasNext){
//            SopProductSkuPageQuery query = new SopProductSkuPageQuery("2015-01-01 00:00","2015-07-10 00:00",pageIndex,pageSize);
//            SopProductSkuPage products = null;
//            try {
//                products = servant.FindCommodityInfoPage("2015061901300", query);
//            } catch (ApiException e) {
//                e.printStackTrace();
//            }
//            List<SopProductSkuIce> skus = products.SopProductSkuIces;
//            for (SopProductSkuIce sku : skus) {
//                List<SopSkuIce> skuIces = sku.SopSkuIces;
//                for (SopSkuIce ice : skuIces) {
//                    if(ice.SupplierSkuNo.equals("151151NCX000014-F0009#1")) {
//                        System.out.println(ice.SupplierSkuNo+ "---"+ ice.SkuNo);
//                    }
//
//                }
//            }
//            pageIndex++;
//            System.out.println("----page-----"+pageIndex);
//            hasNext=(pageSize==skus.size());
//        }
//        System.out.println("----end-----");


        try {
            //wmsinventory.liantiao.com
            Gson gson = new Gson();
            ICEWMSOrderRequestDTO dto = new ICEWMSOrderRequestDTO();
            dto.setBeginTime("2015-09-22T00:00:00");
            dto.setEndTime("2015-09-23T00:00:00");
            dto.setSupplierNo("S0000652");

            String jsonParameter= "="+ gson.toJson(dto);
           String kkk =  HttpUtil45.operateData("post","form","http://wmsinventory.liantiao.com/Api/StockQuery/SupplierInventoryLogQuery",new OutTimeConfig(1000*5,1000*5,1000*5),null,
                   jsonParameter,"","");
            String kk = "[{\"FormNo\":\"20150922050009\",\"SupplierNo\":\"S0000652\",\"SkuNo\":\"30003179001\",\"ChangeForOrderQuantity\":-1,\"CreateTime\":\"2015-09-22T13:52:46\"},{\"FormNo\":\"20150922050009\",\"SupplierNo\":\"S0000652\",\"SkuNo\":\"30003179001\",\"ChangeForOrderQuantity\":1,\"CreateTime\":\"2015-09-22T13:54:47\"},{\"FormNo\":\"20150922050107\",\"SupplierNo\":\"S0000652\",\"SkuNo\":\"30003179001\",\"ChangeForOrderQuantity\":-1,\"CreateTime\":\"2015-09-22T16:03:36\"},{\"FormNo\":\"20150922050107\",\"SupplierNo\":\"S0000652\",\"SkuNo\":\"30003179001\",\"ChangeForOrderQuantity\":1,\"CreateTime\":\"2015-09-22T17:04:28\"}]";
            System.out.println("kkk = " + kkk);
            System.out.println("kk = " + kk);
            kkk = kkk.substring(1,kkk.length()-1).replace("\\","");
            System.out.println("kkk = " + kkk);
            List<ICEWMSOrderDTO> orderDTOList  = gson.fromJson(kkk,new TypeToken<List<ICEWMSOrderDTO>>(){}.getType());
            System.out.println("orderDTOList = " + orderDTOList.size());
//            Map<String,String> map = new HashMap<>();
//            map.put("SupplierNo","S0000652");
//            map.put("BeginTime","2015-09-22T00:00:00");
//            map.put("EndTime","2015-09-23T00:00:00");
//            String ss = HttpUtil45.post("http://192.168.3.97:14279/Api/StockQuery/SupplierInventoryLogQuery",map,new OutTimeConfig());
//            System.out.println("kk = " + ss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
