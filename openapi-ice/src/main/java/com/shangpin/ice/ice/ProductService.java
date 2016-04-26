package com.shangpin.ice.ice;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.dto.SkuRelationDTO;

import java.util.*;

/**
 * Created by lizhongren on 2016/4/26.
 */
public class ProductService {
    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
    private static LoggerUtil loggerError = LoggerUtil.getLogger("error");

    /**
     * 获取产品信息
     * 产品状态 未导入SOP =0,  商品状态待审核= 1,  上架 = 2,  待上架 = 3,  审核不通过 = 4,  已下架 = 5
     * @param supplier
     * @param start
     * @param end

     * @param skuRelationMap  供货商SKU 和 尚品的SKU对应关系 增加 产品状态  例如  suppliersku1=spsku,1
     * @throws Exception
     */
    public Map<Integer,List<String>> grabProduct(String supplier, String start, String end,  Map<String,String> skuRelationMap) throws Exception{
        int pageIndex=1,pageSize=100;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            loggerError.error("ICE 代理失败");
//			e.printStackTrace();
            throw e;
        }
        boolean hasNext=true;




        Date date  = new Date();
        Map<Integer,List<String>> statusMap = new HashMap<>();
        List<String> list1= new ArrayList<>();
        List<String> list2= new ArrayList<>();
        List<String> list3= new ArrayList<>();
        List<String> list4= new ArrayList<>();
        List<String> list5= new ArrayList<>();
        while(hasNext){
            long startDate = System.currentTimeMillis();
            SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
            List<SopProductSkuIce> skus = null;

            //如果异常次数超过5次就跳出
            for(int i=0;i<5;i++){
                startDate = System.currentTimeMillis();
                try {
                    SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
                    loggerInfo.info("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + (System.currentTimeMillis() - startDate));
                    skus = products.SopProductSkuIces;
                    if(skus!=null){
                        i=5;
                    }
                } catch (ApiException e1) {
                    loggerError.error("openAPI 获取产品信息出错 -ApiException -  "+e1.Message);
                }  catch (Exception e1) {
                    loggerError.error("openAPI 获取产品信息出错"+e1.getMessage());
                }
            }
            for (SopProductSkuIce sku : skus) {
                List<SopSkuIce> skuIces = sku.SopSkuIces;
                for (SopSkuIce ice : skuIces) {

                    if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
                        skuRelationMap.put(ice.SupplierSkuNo,ice.SkuNo+","+ice.SkuStatus);
                        switch (ice.SkuStatus){
                            case 1:
                                list1.add(ice.SupplierSkuNo);
                                break;
                            case 2:
                                list2.add(ice.SupplierSkuNo);
                                break;
                            case 3:
                                list3.add(ice.SupplierSkuNo);
                                break;
                            case 4:
                                list4.add(ice.SupplierSkuNo);
                                break;
                            case 5:
                                list5.add(ice.SupplierSkuNo);
                                break;
                        }

                    }
                }
            }
            pageIndex++;
            hasNext=(pageSize==skus.size());
        }
        statusMap.put(1,list1);
        statusMap.put(2,list2);
        statusMap.put(3,list3);
        statusMap.put(4,list4);
        statusMap.put(5,list5);
        return statusMap;
    }
}
