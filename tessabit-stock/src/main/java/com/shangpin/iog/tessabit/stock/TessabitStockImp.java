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
        //构造返回集合
        Map returnMap = new HashMap();
        //获取本地FTP文件
        String localFile = StringUtil.parseXml2Str();
        Iterator<String> iterator=skuNo.iterator();
        while (iterator.hasNext()){
            String itemId = iterator.next().replace("+", "½");
            returnMap.put( itemId,StringUtil.getStockAndSupplyPrice(localFile.substring(
                    localFile.indexOf(itemId),localFile.indexOf(itemId)+ Constant.ITEM_LENTH)));
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        TessabitStockImp impl = new TessabitStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("BRUNAROSSO更新数据库开始");
        impl.updateProductStock("2015071701342", "2015-01-01 00:00", format.format(new Date()));
        logger.info("BRUNAROSSO更新数据库结束");
        System.exit(0);

    }
}
