package com.shangpin.test.orderDetail;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.product.dao.OrderDetailMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppContext.class })
public class OrderDetailTest {
	@Autowired
	OrderDetailMapper dao;

	@Test
	public void testInsert() {
		OrderDetailDTO oo = new OrderDetailDTO();
		oo.setUuid(UUIDGenerator.getUUID());
		oo.setId(new BigInteger("000000"));
		oo.setSupplierId("201605261511");
		oo.setDeliveryNo("1008611");
		oo.setSpSku("3003003001");
		oo.setSupplierSku("pop_sw5");
		oo.setQuantity("1");
		oo.setStatus("waitplaced");
		oo.setCreateTime(new Date());
		oo.setSpMasterOrderNo("2016999999");
		oo.setOrderNo("aaa111222333");
		dao.saveOrderDetailDTO(oo);
	}

	@Test
	public void testUpdate() {
		OrderDetailDTO oo = new OrderDetailDTO();
		oo.setUuid(UUIDGenerator.getUUID());
		oo.setId(new BigInteger("000000"));
		oo.setSupplierId("201605261511");
		oo.setDeliveryNo("1008610086");
		oo.setSpSku("3003003001");
		oo.setSupplierSku("pop_sw5");
		oo.setQuantity("1");
		oo.setStatus("waitplaced");
		oo.setCreateTime(new Date());
		oo.setSpMasterOrderNo("2016999999");
		oo.setOrderNo("aaa111222333");
		dao.updateByOrderNo(oo);
	}

	@Test
	public void findTest1() {
		List<OrderDetailDTO> list = dao.findSubOrderListByMOrderNo("123456");
		for (OrderDetailDTO l : list) {
			System.out.println(l.getId());
		}
	}

	@Test
	public void findTest2() {
		OrderDetailDTO dto = dao.findSubOrderByPurchaseNo("999");
		System.out.println(dto.toString());
	}

	@Test
	public void findTest3() {
		OrderDetailDTO dto = dao.findSubOrderByOrderNo("1");
		System.out.println(dto.toString());
	}

	@Test
	public void findTest4() {
		OrderDetailDTO dto = dao.findByUuId("3");
		System.out.println(dto.toString());
	}

	@Test
	public void findTest5() {
		OrderDetailDTO dto = dao.findBySIDAndSNOAndStatus("201605261511",
				"321", "waitplaced");
		System.out.println(dto.toString());
	}

	@Test
	public void findTest6() {
		OrderDetailDTO oo = new OrderDetailDTO();
	    List<OrderDetailDTO> list = dao.findBySupplierIdAndOrderStatusAndDateAndExcSatus("201605261511", "321", "waitplaced",oo.getStatus(),oo.getStatus(),1);
		System.out.println(list.toString());
	}
}
