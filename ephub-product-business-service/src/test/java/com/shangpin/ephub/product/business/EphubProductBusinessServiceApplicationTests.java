package com.shangpin.ephub.product.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.dto.BrandRequstDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubProductBusinessServiceApplicationTests {

	@Autowired
	private RestTemplate httpClient;
	
	@Test
	public void contextLoads() {
		try {
			BrandRequstDto request = new BrandRequstDto();
			request.setBrandNo("B0005");
			System.out.println(JsonUtil.serialize(request)); 
			HttpEntity<String> entity  = new HttpEntity<String>("{\"BrandNo\":\"B0005\"}");
			ResponseEntity<BasicDataResponse> response =  httpClient.exchange("http://qa.scmapi.shangpin.com/gms/ProductBasicData/GetBrands",
					HttpMethod.POST, entity, BasicDataResponse.class);
			System.out.println(response.getBody().isSuccess()+"   "+response.getBody().getResMsg());
//			FourLevelCategory fourLevle = response.getBody();
//			System.out.println(JsonUtil.serialize(fourLevle));
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		 
	}

}
