package com.shangpin.ephub.product.business;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierChannelDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplirerChannelTest {

    @Autowired
    SupplierInHubService supplierInHubService;

    @Test
    public void supplierChannelTest(){
        String scd =  supplierInHubService.getSupplierChannelByMap("111","");
        System.out.println(scd);
    }
}
