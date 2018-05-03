package com.shangpin.pending.product.consumer;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PendingProductConsumerServiceApplicationTests {
//    @Autowired
//    DataOfPendingServiceHandler dataOfPendingServiceHandler;

    @Autowired
    IShangpinRedis shangpinRedis;

	@Test
	public void contextLoads() {
//        int totalStock = dataOfPendingServiceHandler.getStockTotalBySpuPendingId(25L);
//        System.out.println("totalStock  =" + totalStock);
        System.out.println("delet" + shangpinRedis.del(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528"));
        System.out.println("exist : " + shangpinRedis.exists(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528"));
        Map<String,String> allMap  = shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528");
        System.out.println("map size=" + allMap.size());

        List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528","Denim_man") ;

        System.out.println("mapValue size " + mapValue.size() +  "  value "+ mapValue.get(0));
        Map<String,String> map  = new HashMap<>();
        map.put("Denim_man_234","1234");
        shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528",map);
        List<String> mapValue1 = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528","Denim_man") ;

        System.out.println("mapValue1 size " + mapValue1.size() +  "  value "+ mapValue1.get(0));

        List<String> mapValue2 = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY + "_" + "01528","Denim_man_234") ;

        System.out.println("mapValue2 size " + mapValue2.size() +  "  value "+ mapValue2.get(0));


    }

}
