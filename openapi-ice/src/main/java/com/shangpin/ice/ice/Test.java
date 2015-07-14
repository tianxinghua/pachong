package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lizhongren on 2015/7/10.
 */
public class Test {
    public static void main(String[] args){
        System.out.println("----start-----");
        int pageIndex=1,pageSize=100;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean hasNext=true;
        Set<String> skuIds = new HashSet<String>();
        while(hasNext){
            SopProductSkuPageQuery query = new SopProductSkuPageQuery("2015-01-01 00:00","2015-07-10 00:00",pageIndex,pageSize);
            SopProductSkuPage products = null;
            try {
                products = servant.FindCommodityInfoPage("2015061901300", query);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            List<SopProductSkuIce> skus = products.SopProductSkuIces;
            for (SopProductSkuIce sku : skus) {
                List<SopSkuIce> skuIces = sku.SopSkuIces;
                for (SopSkuIce ice : skuIces) {
                    if(ice.SupplierSkuNo.equals("151151NCX000014-F0009#1")) {
                        System.out.println(ice.SupplierSkuNo+ "---"+ ice.SkuNo);
                    }

                }
            }
            pageIndex++;
            System.out.println("----page-----"+pageIndex);
            hasNext=(pageSize==skus.size());
        }
        System.out.println("----end-----");
    }

}
