package com.shangpin.iog.tony.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.tony.common.MyJsonClient;
import com.shangpin.iog.tony.stock.common.MyStringUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
public class TonyStockImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private long start = 0;//计时开始时间
    private long end = 0;//计时结束时间
   // @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法开始！");
        logger.info("Sku 条数："+skuNo.size());
        start = System.currentTimeMillis();
        String json = new MyJsonClient().getEvents();
        //String json = "rtnData=={\"status\":\"ok\",\"data\":{\"events\":[{\"_id\":{\"$id\":\"561e6e2be4b0b71d53085f0f\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":1,\"date\":{\"sec\":1444834859,\"usec\":487000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e6e21e4b0b71d53085f07\"}}},{\"_id\":{\"$id\":\"561e745fe4b0269c2a643f68\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444836447,\"usec\":622000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":2,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7455e4b0269c2a643f60\"}}},{\"_id\":{\"$id\":\"561e775ce4b0b5f049525a41\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837212,\"usec\":802000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":1,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7752e4b0b5f049525a38\"}}},{\"_id\":{\"$id\":\"561e77a2e4b0b5f049525a4b\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837282,\"usec\":801000},\"additional_info\":{\"qty_diff\":-3,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7798e4b0b5f049525a42\"}}},{\"_id\":{\"$id\":\"561e7d36e4b0c01df26d77e9\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444838710,\"usec\":853000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e7d2ce4b0c01df26d77e0\"}}}]}}";
        end = System.currentTimeMillis();
        logger.info("获取events事件信息："+json+"，耗时："+(end-start)/1000+"秒");
        //定义三方
        Map returnMap = new HashMap();
        String key = "";
        Integer value = null;
         String[] strArr = json.split("additional_info");
        System.out.println(strArr.length);
        logger.info("为TESSABIT供应商产品库存循环赋值");
        start = System.currentTimeMillis();
        for (String item:strArr){
            System.out.println(item+"===========");
            key = MyStringUtil.getSkuId(item);
            value = MyStringUtil.getQty(item);
            System.out.println("Sku ID is - :"+key+",stock is "+value);
            logger.info("Sku ID is "+key+",stock is "+value);
            returnMap.put(key,value);
        }
        end = System.currentTimeMillis();
        logger.info("产品库存赋值总共耗时："+(end-start)/1000+"秒");
        System.out.println("为产品库存赋值总共耗时："+(end-start)/1000+"秒");
        logger.info(this.getClass()+" 调用grabStock(Collection<String> skuNo)方法结束！");
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
        skuNo.add("356530S1533_1500-40");
        skuNo.add("356530S1533_1000-40");
        Map returnMap = impl.grabStock(skuNo);
        System.out.println("test return size is "+returnMap.keySet().size());
/*        for (Object key:returnMap.keySet()){
            System.out.print("key is " + key);
            System.out.println(",value is "+returnMap.get(key));
        }*/
    }
}
