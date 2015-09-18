package com.shangpin.iog.marylou;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.onsite.base.utils.MapUtil;
import com.shangpin.iog.onsite.base.utils.StringUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Administrator on 2015/9/18.
 */
public class MarylouStockImpl  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //获取产品信息
        String json = new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
        //返回需要更新的信息
        return MapUtil.grabStock(skuNo,json);
    }

    public static void main(String[] args) throws Exception {
        MarylouStockImpl impl = new MarylouStockImpl();
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
