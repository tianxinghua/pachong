package com.shangpin.iog.tony.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.onsite.base.utils.MyJsonUtil;
import com.shangpin.iog.tessabit.stock.common.Constant;
import com.shangpin.iog.tessabit.stock.common.FtpUtil;
import com.shangpin.iog.tessabit.stock.common.StringUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class TonyStockImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
        String json = MyJsonUtil.getTonyJson();
        //定义三方
        Map returnMap = new HashMap();
        String itemId = "";
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        while (iterator.hasNext()){
            itemId = iterator.next();
            returnMap.put(itemId, StringUtil.getTonyByQty(json,itemId,itemId,Constant.ITEM_LENTH));
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
        TonyStockImp impl = new TonyStockImp();
/*        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("TESSABIT更新数据库开始");
        impl.updateProductStock("2015091501331", "2015-01-01 00:00", format.format(new Date()));
        logger.info("TESSABIT更新数据库结束");
        System.exit(0);*/

        List<String> skuNo = new ArrayList<>();
        skuNo.add("1986242872_10");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        System.out.println("test return value is "+returnMap.get("1986242872_10"));

    }
}
