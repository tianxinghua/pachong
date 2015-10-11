package com.shangpin.iog.marylou;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.onsite.base.utils.StringUtil;
import com.shangpin.iog.util.MapUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/18.
 */
public class MarylouStockImpl  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");

    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        String json = new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
        Map<String ,String> tmpMap =  MapUtil.grabStock(skuNo,json);
        Map<String,Integer> stockMap= new HashMap<>();
        String key ="";
        for(Iterator iterator= tmpMap.keySet().iterator();iterator.hasNext();){
            key = (String)iterator.next();
            stockMap.put(key,Integer.valueOf(tmpMap.get(key)));
        }
        return stockMap;
    }

    public static void main(String[] args) throws Exception {
//
        String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }



        MarylouStockImpl impl = new MarylouStockImpl();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("更新数据库开始");
        impl.updateProductStock(host,app_key,app_secret, "2015-01-01 00:00", format.format(new Date()));
        logger.info("更新数据库结束");
        System.exit(0);
//        List<String> skuNo = new ArrayList<>();
//        skuNo.add("1986242872_10");
//        Map returnMap = impl.grabStock(skuNo);
//        System.out.println("test return size is "+returnMap.keySet().size());
//        System.out.println("test return value is "+returnMap.get("1986242872_10"));

    }
}
