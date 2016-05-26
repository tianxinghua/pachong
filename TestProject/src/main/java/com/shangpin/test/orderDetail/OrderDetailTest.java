package com.shangpin.test.orderDetail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.OrderDetailMapper;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppContext.class})
public class OrderDetailTest {
	@Autowired
	OrderDetailMapper dao;
	@Test
	public void test() {
		OrderDetailDTO dto = dao.findSubOrderByPurchaseNo("987");
		System.out.println(dto.getSpOrderNo());
		Assert.hasText(dto.getSpOrderNo());
	
	}
}
