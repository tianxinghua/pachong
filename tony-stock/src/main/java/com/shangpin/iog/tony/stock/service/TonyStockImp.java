package com.shangpin.iog.tony.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.service.SupplierStockService;
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

    @Override
    public Map<String,String> grabStock(Collection<String> skuNos) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("Sku 条数："+skuNos.size());


        this.supplierSkuIdMain = true;//已供应商的SKUID为主 有什么更新什么  但尚品必须有此产品
        Map<String,String>  returnMap = new HashMap();

        Map skuNoMap = new HashMap();
        for(String skuNo:skuNos){
            skuNoMap.put(skuNo,"");
        }

        List<SupplierStockDTO>  stockDTOList = supplierStockService.findBySupplierId(SUPPLIER_ID);
        for(SupplierStockDTO supplierStockDTO:stockDTOList){
            if(skuNoMap.containsKey(supplierStockDTO.getSupplierSkuId())){//SOP产品包含产品
                returnMap.put(supplierStockDTO.getSupplierSkuId(),supplierStockDTO.getQuantity().toString());
            }
        }


        return  returnMap;
    }


}
