package com.shangpin.iog.biancabianca.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.biancabianca.dto.Product;
import com.shangpin.iog.biancabianca.util.MyTxtUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangyuzhi on 2015/11/11.
 */
public class BiancabiancaStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private long start = 0;//计时开始时间
    private long end = 0;//计时结束时间
    private String skuId = "";//单个skuId
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("BIANCABIANCA Sku 条数："+skuNo.size());
        start = System.currentTimeMillis();
        boolean flag = true;
        try {
            flag = MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            loggerError.error("下载BIANCABIANCA文件失败!"+e.getMessage());
        }
        end = System.currentTimeMillis();
        logger.info("下载BIANCABIANCA文件结果"+flag+"，耗时："+(end-start)/1000+"秒");
        List<Product> list = null;
        Map<String,String> map = new HashMap();
        if (flag){
            start = System.currentTimeMillis();
            try {
                list = MyTxtUtil.readTXTFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0;i<list.size();i++){
                map.put(list.get(i).getVARIANT_SKU(),list.get(i).getSTOCK_LEVEL());
            }
            end = System.currentTimeMillis();
            logger.info("解析BIANCABIANCA文件耗时："+(end-start)/1000+"秒");
        } else {
            loggerError.error("下载BIANCABIANCA文件失败!");
        }

        Map<String,String> returnMap = new HashMap();
        Iterator<String> iterator=skuNo.iterator();
        logger.info("为LEVELGROUP供应商产品库存循环赋值");
        start = System.currentTimeMillis();
        while (iterator.hasNext()){
            skuId = iterator.next();
            logger.info("SkuId is " +skuId + ",stock is " +map.get(skuId));
            returnMap.put(skuId, map.get(skuId));
        }
        end = System.currentTimeMillis();
        logger.info("为AMFEED产品库存赋值总共耗时："+(end-start)/1000+"秒");
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法结束！");
        return returnMap;
    }

    /**
     * test
     * */
    public static void main(String[] args) throws Exception {
        String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)|| StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }
        AbsUpdateProductStock impl = new BiancabiancaStockImp();

      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("AMFEED更新数据库开始");
        try{
            impl.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        }catch (Exception e){
            System.exit(0);
        }
        logger.info("AMFEED更新数据库结束");
        System.exit(0);

/*        AbsUpdateProductStock impl = new BiancabiancaStockImp();
        List<String> skuNo = new ArrayList<>();
        skuNo.add("1054-S");
        skuNo.add("1032-S");
        skuNo.add("1076-M");
        skuNo.add("1076-L");
        skuNo.add("1092-XS");
        skuNo.add("1150-8,5");
*//*        for (int i = 0;i<5000;i++){
            skuNo.add(i+"1983933587_1985020936");
        }*//*
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()){
           System.out.println("skuId is "+key+",stock is "+returnMap.get(key));;
        }*/
    }
}
