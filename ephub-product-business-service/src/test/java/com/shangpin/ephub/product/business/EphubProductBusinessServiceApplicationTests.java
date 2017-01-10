//package com.shangpin.ephub.product.business;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.client.RestTemplate;
//
//import com.shangpin.ephub.client.util.JsonUtil;
//import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
//import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
//import com.shangpin.ephub.product.business.dto.BrandRequstDto;
//import com.shangpin.ephub.product.business.dto.CategoryRequestDto;
//import com.shangpin.ephub.product.business.dto.SizeRequestDto;
//import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
//import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
//import com.shangpin.ephub.product.business.vo.BasicDataResponse;
//import com.shangpin.ephub.product.business.vo.BrandDom;
//import com.shangpin.ephub.response.HubResponse;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class EphubProductBusinessServiceApplicationTests {
//
//	@Autowired
//	private RestTemplate httpClient;
//	
//	@Test
//	public void test(){
//		try {
//			PendingQuryDto request = new PendingQuryDto();
//			request.setPageIndex(1);
//			request.setPageSize(20); 
//			HubResponse<PendingProducts> pendingProducts = httpClient.postForObject("http://192.168.20.176:8003/pending-product/list", request, HubResponse.class);
//			System.out.println(pendingProducts); 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testBrands() {
//		try {
//			BrandRequstDto request = new BrandRequstDto();
//			request.setBrandNo("B0005");
//			HttpEntity<BrandRequstDto> requestEntity = new HttpEntity<BrandRequstDto>(request);
//			
//			ResponseEntity<BasicDataResponse<BrandDom>> entity = httpClient.exchange("http://192.168.3.225/ShangPin.GMS.Api/ProductBasicData/GetBrands", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<BasicDataResponse<BrandDom>>() {
//			});
//			BasicDataResponse<BrandDom> body = entity.getBody();
//			System.out.println(body.getResDatas());
//			
////			String response = httpClient.postForObject("http://192.168.3.225/ShangPin.GMS.Api/ProductBasicData/GetBrands", request, String.class);
////			System.out.println(response);
//		} catch (Exception e) {
//			e.printStackTrace(); 
//		}		 
//	}
//	
//	@Test
//	public void testGetCategory(){
//		try {
//			CategoryRequestDto request = new CategoryRequestDto();
//			request.setCategoryNo("A01"); 
//			BasicDataResponse<?> response = httpClient.postForObject("http://qa.scmapi.shangpin.com/gms/ProductBasicData/GetCategory", request, BasicDataResponse.class);
//			System.out.println(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testGetProductSize(){
//		try {
//			SizeRequestDto request = new SizeRequestDto();
//			request.setBrandNo("B0005");
//			request.setCategoryNo("A01");
//			BasicDataResponse<?> response = httpClient.postForObject("http://qa.scmapi.shangpin.com/gms/ProductBasicData/GetProductSize", request, BasicDataResponse.class);
//			System.out.println(response);
//		} catch (Exception e) {
//			e.printStackTrace(); 
//		}				
//	}
//	
//	@Autowired
//	SupplierService supplierService;
//	@Test
//	public void testSupplier(){
//		SupplierDTO dto = supplierService.getSupplier("S0000802");
//		System.out.println(dto);
//	}
//	
//	public static void main(String[] args) {
//		SupplierDTO dto = new SupplierDTO();
//		dto.setAccount("account");
//		dto.setAccountBank("accountBank");
//		String json = JsonUtil.serialize(dto);
//		SupplierDTO dto2 = JsonUtil.deserialize(json, SupplierDTO.class);
//		System.out.println(dto2);
//	}
//
//}
