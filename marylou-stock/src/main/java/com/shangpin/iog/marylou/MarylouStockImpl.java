package com.shangpin.iog.marylou;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.onsite.base.utils.StringUtil;
import com.shangpin.iog.util.MapUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/18.
 */
@Component("marylouStock")
public class MarylouStockImpl  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
//        String json = new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
    	String json = HttpUtil45.get(Constant.URL_MARYLOU, new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60), null);// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
        Map<String ,String> tmpMap =  MapUtil.grabStock(skuNo,json);
        Map<String,Integer> stockMap= new HashMap<>();
        String key ="";
        for(Iterator iterator= tmpMap.keySet().iterator();iterator.hasNext();){
            key = (String)iterator.next();
            stockMap.put(key,Integer.valueOf(tmpMap.get(key)));
        }
        return stockMap;
    }

    public static void main(String[] args){
    	//加载spring
        loadSpringContext();
        MarylouStockImpl marylouStockImpl = (MarylouStockImpl)factory.getBean("marylouStock");
        String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("更新库存开始");
        try{
        	marylouStockImpl.updateProductStock(host,app_key,app_secret, "2015-01-01 00:00", format.format(new Date()));
        }catch(Exception ex){
        	loggerError.error("更新库存失败"+ex.getMessage());
        	ex.printStackTrace();
        }
        logger.info("更新库存结束");
        System.exit(0);
    }
}
