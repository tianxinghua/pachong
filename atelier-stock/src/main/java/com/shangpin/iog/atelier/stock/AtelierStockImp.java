package com.shangpin.iog.atelier.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.atelier.common.MyStringUtil;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class AtelierStockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //new WS_Sito_P15().getAllAvailabilityMarketplaceBySoap();
        String stocks = new WS_Sito_P15().getAllAvailabilityStr();
        //定义三方
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        logger.info("为供应商循环赋值");
        while (iterator.hasNext()){
            itemId = iterator.next();
            String stock = "0";
            if (stocks.contains(itemId)){
                stock = MyStringUtil.getStockBySkuId(stocks.substring(stocks.indexOf(itemId), stocks.indexOf(itemId) + 20));
            }
            returnMap.put(itemId, stock);
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        AtelierStockImp impl = new AtelierStockImp();

/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("ATELIER更新数据库开始");
        impl.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        logger.info("ATELIER更新数据库结束");
        System.exit(0);*/


        List<String> skuNo = new ArrayList<>();
        skuNo.add("446703");
        skuNo.add("448277");
        skuNo.add("443636");
        skuNo.add("443636");
        skuNo.add("888888");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()) {
            System.out.println(key+" test return value is " + returnMap.get(key));
        }

    }
}
