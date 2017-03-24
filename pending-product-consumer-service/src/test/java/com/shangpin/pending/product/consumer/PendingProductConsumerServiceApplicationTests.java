package com.shangpin.pending.product.consumer;

import com.shangpin.pending.product.consumer.supplier.common.DataOfPendingServiceHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PendingProductConsumerServiceApplicationTests {
    @Autowired
    DataOfPendingServiceHandler dataOfPendingServiceHandler;

	@Test
	public void contextLoads() {
        int totalStock = dataOfPendingServiceHandler.getStockTotalBySpuPendingId(25L);
        System.out.println("totalStock  =" + totalStock);
    }

}
