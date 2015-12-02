package com.shangpin.iog.tony.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.service.SupplierStockService;
import com.shangpin.iog.service.UpdateStockService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
@Service("tonyStock")
public class TonyStockImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private long start = 0;//计时开始时间
    private long end = 0;//计时结束时间


    public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    //供应商ID
    public static String SUPPLIER_ID = bundle.getString("supplierId");


    @Autowired
    SupplierStockService supplierStockService;


    @Autowired
    UpdateStockService updateStockService;

    @Override
    public Map<String,String> grabStock(Collection<String> skuNos) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("Sku 条数："+skuNos.size());



        Map<String,String>  supplierMap = new HashMap();

        Map<String,String> stockMap = new HashMap<>();


        List<SupplierStockDTO>  stockDTOList = supplierStockService.findBySupplierId(SUPPLIER_ID);
        logger.info("stockDTOList size ="+stockDTOList.size());
        for(SupplierStockDTO supplierStockDTO:stockDTOList){
            supplierMap.put(supplierStockDTO.getSupplierSkuId(),supplierStockDTO.getQuantity().toString());
        }


        for(String skuNo:skuNos){
            if(supplierMap.containsKey(skuNo)){//SOP产品包含产品
                stockMap.put(skuNo,supplierMap.get(skuNo));
            }else{
                stockMap.put(skuNo,"0");
            }
        }

        logger.info("returnMap size ="+stockMap.size());
        return  stockMap;
    }


}
