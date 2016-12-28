package com.shangpin.ephub.data.mysql;

import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubDataMysqlServiceApplicationTests {

	@Autowired
	PengingToHubService pengingToHubService;
	@Test
	public void contextLoads() {
		SpuModelDto dto = new SpuModelDto();
		try {
			dto.setBrandNo("B03624");
			dto.setSpuModel("V5G21");
			pengingToHubService.auditPending(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
