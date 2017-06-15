package com.shangpin.ephub.product.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.mail.message.ShangpinMail;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeStandardItem;

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

	public void testGetProductSize(){
		try {
			MatchSizeResult matchSizeResult = new MatchSizeResult();
			String sizeValue = "36";
			SizeRequestDto request = new SizeRequestDto();
			request.setBrandNo("B0200");
			request.setCategoryNo("A03B02C02D09");
			
	        HttpEntity<SizeRequestDto> requestEntity = new HttpEntity<SizeRequestDto>(request);
			ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = httpClient.exchange(ApiAddressProperties.getGmsSizeUrl(), HttpMethod.POST,
	                requestEntity, new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
	                });
			
			@SuppressWarnings("unused")
			HubResponseDto<CategoryScreenSizeDom> cate  = entity.getBody();
			CategoryScreenSizeDom size = entity.getBody().getResDatas().get(0);
			boolean sizeIsExist = false;
			String result = null;
			boolean isNotTemplate = false;
			Map<String,String> screenSizeMap = new HashMap<String,String>();
			Map<String,String> standardSizeMap = new HashMap<String,String>();
			if(size!=null){
				List<SizeStandardItem> list = size.getSizeStandardItemList();	
				if(list!=null&&list.size()>0){
					//获取筛选尺码和标准尺码map集合
					getSizeMap(list,screenSizeMap,standardSizeMap);
					//第一步：从标准尺码中查找匹配尺码
					sizeIsExist = matchStandardSize(sizeValue,standardSizeMap,matchSizeResult);
					if(!sizeIsExist){
						//第二步：从标准尺码中未匹配到尺码。继续从筛选尺码中匹配
						sizeIsExist = matchScreenSize(sizeValue,screenSizeMap,matchSizeResult);
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
				matchSizeResult.setPassing(false);
				if(isNotTemplate){
					matchSizeResult.setNotTemplate(isNotTemplate);
					result = "scm没有尺码模板";	
				}else{
					//sizeIsExist为true，说明匹配到尺码并且匹配到多个
					if(sizeIsExist){
						matchSizeResult.setMultiSizeType(true);
						result = "含有多个尺码模板";		
					}else{
						if(standardSizeMap.size()>0){
							result = "尺码："+""+"未匹配成功";		
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
	
	@Autowired
	private ShangpinMailSender shangpinMailSender;
	
//	@Test
//	public void testMail(){
//		String text = "供价记录推送消息队列失败，supplierPriceChangeRecordId：3333333333333333，异常信息：异常"
//		+"<br>"
//		+"【推送失败的消息是：{\"pageIndex\":4,\"pageSize\":200,\"supplierId\":\"2016050401882\",\"marketSeason\":\"春夏\"}】"; 
//		ShangpinMail shangpinMail = new ShangpinMail();
//		shangpinMail.setFrom("chengxu@shangpin.com");
//		shangpinMail.setSubject("testMail");
//		shangpinMail.setText(text);
//		shangpinMail.setTo("lubaijiang@shangpin.com");
//		try {
//			shangpinMailSender.sendShangpinMail(shangpinMail);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}

	@Autowired
	HubSlotSpuService hubSlotSpuService;

	@Autowired
	HubSpuPendingGateWay gateWay;

	@Test
	public void testAddSlotSpu(){

		PendingProductDto pendingProductDto = new PendingProductDto();
		HubSpuPendingCriteriaDto criteriaDto = new HubSpuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(4786L);
		List<HubSpuPendingDto> spuPendingDtos   = gateWay.selectByCriteria(criteriaDto);
		if(null!=spuPendingDtos&&spuPendingDtos.size()>0){
			BeanUtils.copyProperties(spuPendingDtos.get(0),pendingProductDto);

			try {
				hubSlotSpuService.addSlotSpuAndSupplier(pendingProductDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}



}
