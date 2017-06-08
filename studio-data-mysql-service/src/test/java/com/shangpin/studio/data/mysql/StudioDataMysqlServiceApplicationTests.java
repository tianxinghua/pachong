package com.shangpin.studio.data.mysql;//package com.shangpin.ephub.data.mysql;
//

import com.shangpin.studio.data.mysql.po.Studio;
import com.shangpin.studio.data.mysql.service.StudioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudioDataMysqlServiceApplicationTests {

	@Autowired
    StudioService studioService;
	@Test
	public void contextLoads() {
		Studio dto = new Studio();
		try {
			dto.setStudioName("摄影棚2");
            studioService.insert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
