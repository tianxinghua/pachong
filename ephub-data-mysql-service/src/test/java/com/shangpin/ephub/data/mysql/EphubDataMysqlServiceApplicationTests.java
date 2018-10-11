package com.shangpin.ephub.data.mysql;

import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.SlotSupplierSearchService;
import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.service.SupplierChannelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubDataMysqlServiceApplicationTests {

	@Autowired
    SlotSupplierSearchService slotSupplierSearchService;

	@Autowired
	SupplierChannelService supplierChannelService;

	@Test
	public void getSupplierChannel(){
		Map<String ,String> map = new HashMap<String,String>();
		map.put("supplierNo","");
		map.put("supplierId","111");
		SupplierChannelPic sc = supplierChannelService.getSupplierChannelPicByMap(map);
		System.out.println(sc.toString());
	}


	@Test
	public void contextLoads() {
		SpuModelDto dto = new SpuModelDto();
		try {
			dto.setBrandNo("B03624");
			dto.setSpuModel("V5G21");
			int i;
			SpuSupplierQueryDto queryDto = new SpuSupplierQueryDto();
			queryDto.setSupplierNo("S0000712");
			queryDto.setStartRow(0);
			i = slotSupplierSearchService.countByQuery(queryDto);
			System.out.println("i= "+i);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
