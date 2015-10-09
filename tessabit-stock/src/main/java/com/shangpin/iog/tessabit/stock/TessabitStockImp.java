package com.shangpin.iog.tessabit.stock;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.tessabit.stock.common.Constant;
import com.shangpin.iog.tessabit.stock.common.MyFtpClient;
import com.shangpin.iog.tessabit.stock.common.StringUtil;
import org.apache.log4j.Logger;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class TessabitStockImp  extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //拉取FTP文件
        logger.info("拉取TESSABIT数据开始");
        new MyFtpClient().downLoad();
        logger.info("拉取TESSABIT数据结束");
        //FTP文件转换成字符串
        logger.info("解析TESSABIT数据开始");
        String localFile = new StringUtil().parseXml2Str();
        logger.info("解析TESSABIT数据开始");
        //定义三方
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        logger.info("为供应商产品库存循环赋值");
        String stock = null;
        while (iterator.hasNext()){
            itemId = iterator.next();
            stock = StringUtil.getSubBySub(localFile,itemId,itemId, Constant.ITEM_LENTH);
            logger.info("SkuId is " +itemId + ",stock is " +stock);
            returnMap.put(itemId, stock);
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        TessabitStockImp impl = new TessabitStockImp();

/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock(Constant.SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/


        List<String> skuNo = new ArrayList<>();
        skuNo.add("1983991600_12");
        skuNo.add("1983991600_11");
        skuNo.add("1983991600_10");
        skuNo.add("1983904634_11");
        skuNo.add("1983904634_12");
        skuNo.add("1983933587_1985020934");
        skuNo.add("1983933587_1985020935");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()){
            System.out.println("skuId is "+key+",stock is "+returnMap.get(key));;
        }
    }
}
