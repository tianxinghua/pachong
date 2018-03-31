package com.shangpin.iog.redi.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("redi")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchUtil  productFetchUtil;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException, Exception {
        Map<String, String> stockMap = new HashMap<String, String>();//可以匹配上库存的Map
        Map<String, String> skuNoMap = new HashMap<>();//尚未匹配上SKU的MAP
        Map<String,String> supplierSkuStcokMap = new HashMap<>();//供货商查询过SPU下的所有SKU库存map
        Map<String,String>  spuNoMap = new HashMap<>();//查询过SPU的map;
        String spuNo="",multiSpuNo="";
        int i=0;
        boolean handleResut;

        for (String skuNo : skuNos) {

            if(supplierSkuStcokMap.containsKey(skuNo)){
                stockMap.put(skuNo,supplierSkuStcokMap.get(skuNo));
                continue;
            }else{
                skuNoMap.put(skuNo,null);//未匹配上,先存储，待全部查询完毕，需要再次匹配supplierStockMap
                if(skuNo.indexOf("_")>0) {
                    spuNo = skuNo.substring(0, skuNo.indexOf("_"));
                    if(spuNoMap.containsKey(spuNo)){
                        continue;
                    }else{
                        spuNoMap.put(spuNo,null);
                        multiSpuNo = multiSpuNo +spuNo+",";
                    }
                    i++;
                    if(i%100==0){
                        multiSpuNo  = multiSpuNo.substring(0,multiSpuNo.length()-1);
                        try {
                            handleResut =  productFetchUtil.setProductStock(multiSpuNo,supplierSkuStcokMap);
                            if(!handleResut){
                                //TODO  失败后如何处理 未想清楚
                                loggerError.error("拉取失败 ");
                            }
                            multiSpuNo = "";

                        } catch (Exception e) {
                            loggerError.error("拉取失败 "+e.getMessage());

                            e.printStackTrace();
                        }

                    }

                }else{
                    stockMap.put(skuNo,"0");
                }
            }
        }
        //补最后未到100的数据
        if(StringUtils.isNotBlank(multiSpuNo)){
            multiSpuNo  = multiSpuNo.substring(0,multiSpuNo.length()-1);
            try {
                handleResut =  productFetchUtil.setProductStock(multiSpuNo,supplierSkuStcokMap);
                if(!handleResut){
                    //TODO  失败后如何处理 未想清楚
                    loggerError.error("拉取失败 ");
                }
            } catch (Exception e) {
                loggerError.error("拉取失败 "+e.getMessage());
                e.printStackTrace();
            }
        }
        //更新查询前的skuno的库存
        String skuNo="";
        for(Iterator<String> itor = skuNoMap.keySet().iterator();itor.hasNext();){
              skuNo = itor.next();
              if(supplierSkuStcokMap.containsKey(skuNo)){
                  stockMap.put(skuNo,supplierSkuStcokMap.get(skuNo));
              }else{
                  //未找到
                  stockMap.put(skuNo,"0");
              }

        }

        logger.info("返回的map大小  stock_map.size======"+stockMap.size());
        return stockMap;
    }
}
