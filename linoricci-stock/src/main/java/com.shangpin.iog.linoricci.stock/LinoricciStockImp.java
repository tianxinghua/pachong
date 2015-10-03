package com.shangpin.iog.linoricci.stock;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.linoricci.stock.common.Constant;
import com.shangpin.iog.linoricci.stock.common.MyFtpClient;
import com.shangpin.iog.linoricci.stock.common.MyStringUtil;
import org.apache.log4j.Logger;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class LinoricciStockImp  extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        new MyFtpClient().downLoad();
        String stocks = new MyStringUtil().parseXml2Str(Constant.LOCAL_STOCK_FILE);
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
        LinoricciStockImp impl = new LinoricciStockImp();

/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock(Constant.SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/
    }
}
