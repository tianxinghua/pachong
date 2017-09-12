package com.shangpin.ephub.data.mysql;

import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.bean.SpuSupplierQueryDto;
import com.shangpin.ephub.data.mysql.slot.spusupplierunion.service.SlotSupplierSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubDataMysqlServiceApplicationTests {

	@Autowired
    SlotSupplierSearchService slotSupplierSearchService;
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
