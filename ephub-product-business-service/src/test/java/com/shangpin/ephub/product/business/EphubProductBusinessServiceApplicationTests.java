package com.shangpin.ephub.product.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.BasicDataResponse;
import com.shangpin.ephub.product.business.common.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.common.dto.HubResponseDto;
import com.shangpin.ephub.product.business.common.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.common.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubProductBusinessServiceApplicationTests {

	@Autowired
	private RestTemplate httpClient;
	@Autowired
	ApiAddressProperties ApiAddressProperties;
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
	
//	@Test
//	public void testBrands() {
//		try {
//			BrandRequstDto request = new BrandRequstDto();
//			request.setBrandNo("B");
//			HttpEntity<BrandRequstDto> requestEntity = new HttpEntity<BrandRequstDto>(request);
//			
//			ResponseEntity<BasicDataResponse<BrandDom>> entity = httpClient.exchange(ApiAddressProperties.getGmsBrandUrl(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<BasicDataResponse<BrandDom>>() {
//			});
//			BasicDataResponse<BrandDom> body = entity.getBody();
//			System.out.println(body.getResDatas());
//			
//			String response = httpClient.postForObject(ApiAddressProperties.getGmsBrandUrl(), request, String.class);
//			System.out.println(response);
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
//			BasicDataResponse<?> response = httpClient.postForObject(ApiAddressProperties.getGmsCategoryUrl(), request, BasicDataResponse.class);
//			System.out.println(response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	@Test
	public void testGetProductSize(){
		try {
			MatchSizeResult matchSizeResult = new MatchSizeResult();
			String size = "36";
			SizeRequestDto request = new SizeRequestDto();
			request.setBrandNo("B0200");
			request.setCategoryNo("A01B01C01D20");
			
	        HttpEntity<SizeRequestDto> requestEntity = new HttpEntity<SizeRequestDto>(request);
			ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = httpClient.exchange(ApiAddressProperties.getGmsSizeUrl(), HttpMethod.POST,
	                requestEntity, new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
	                });
			
			HubResponseDto<CategoryScreenSizeDom> cate  = entity.getBody();
			CategoryScreenSizeDom sizes = cate.getResDatas().get(0);
			boolean flag = false;
			String result = null;
			boolean sizeIsExist = false;
			boolean isNotTemplate = false;
			Map<String,String> screenSizeMap = new HashMap<String,String>();
			Map<String,String> standardSizeMap = new HashMap<String,String>();
			if(sizes!=null){
				
				List<SizeStandardItem> list = sizes.getSizeStandardItemList();	
				if(list!=null&&list.size()>0){
					//获取筛选尺码和标准尺码map集合
					getSizeMap(list,screenSizeMap,standardSizeMap);
					//第一步：从标准尺码中查找匹配尺码
					sizeIsExist = matchStandardSize(size,standardSizeMap,matchSizeResult);
					if(!sizeIsExist){
						sizeIsExist = matchScreenSize(size,screenSizeMap,matchSizeResult);
					}
				}else{
					isNotTemplate = true;
				}
			}else{
				isNotTemplate = true;
			}
			
			if(matchSizeResult.isPassing()){
				matchSizeResult.setPassing(true);
			}else{
				
				if(isNotTemplate){
					matchSizeResult.setNotTemplate(isNotTemplate);
					result = "scm没有尺码模板";	
				}else{
					//sizeIsExist为true，说明匹配到尺码
					if(sizeIsExist){
						matchSizeResult.setMultiSizeType(true);
						result = "含有多个尺码模板";		
					}else{
						if(standardSizeMap.size()>0){
							result = "尺码："+size+"未匹配成功";		
							matchSizeResult.setFilter(true);	
						}else{
							result = "scm没有尺码模板";	
							matchSizeResult.setNotTemplate(true);		
						}
					}
				}
			}
			matchSizeResult.setResult(result);
		} catch (Exception e) {
			e.printStackTrace(); 
		}				
	}

	private boolean matchScreenSize(String size, Map<String, String> map2, MatchSizeResult matchSizeResult) {
		boolean sizeIsExist = false;
		if (map2.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map2.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				if (size.equals(value)) {
					sizeIsExist = true;
					i++;
					String[] arr = key.split(":", -1);
					if (arr.length == 2) {
						matchSizeResult.setSizeType(arr[0]);
						matchSizeResult.setSizeValue(arr[1]);
						matchSizeResult.setPassing(true);
						if (i >= 2) {
							matchSizeResult.setPassing(false);
							break;
						}
					}
				}
			}
		}
		return sizeIsExist;

	}

	private boolean matchStandardSize(String size, Map<String, String> map0, MatchSizeResult matchSizeResult) {
		boolean isExist = false;
		if (map0.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map0.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				if (size.equals(value)) {
					String[] arr = key.split(":", -1);
					isExist = true;
					i++;
					if (arr.length == 2) {
						matchSizeResult.setSizeType(arr[0]);
						matchSizeResult.setSizeValue(arr[1]);
						matchSizeResult.setPassing(true);
						if (i >= 2) {
							matchSizeResult.setPassing(false);
							break;
						}
					}
				}
			}
		}
		return isExist;
	}

	private void getSizeMap(List<SizeStandardItem> list, Map<String, String> map2, Map<String, String> map0) {
		for (SizeStandardItem item : list) {
			String sizeValue = item.getSizeStandardValue();
			String sizeStandardName = item.getSizeStandardName();
			// 筛选尺码
			if (item.getIsScreening() == (byte) 2) {
				map2.put(sizeStandardName + ":" + sizeValue, sizeValue);
			}
			// 标准尺码
			if (item.getIsScreening() == (byte) 0) {
				map0.put(sizeStandardName + ":" + sizeValue, sizeValue);
			}
		}

	}
	
//	@Autowired
//	SupplierService supplierService;
//	@Test
//	public void testSupplier(){
//		SupplierDTO dto = supplierService.getSupplier("S0000802");
//		System.out.println(dto);
//	}
	
//	public static void main(String[] args) {
//		SupplierDTO dto = new SupplierDTO();
//		dto.setAccount("account");
//		dto.setAccountBank("accountBank");
//		String json = JsonUtil.serialize(dto);
//		SupplierDTO dto2 = JsonUtil.deserialize(json, SupplierDTO.class);
//		System.out.println(dto2);
//	}

}
