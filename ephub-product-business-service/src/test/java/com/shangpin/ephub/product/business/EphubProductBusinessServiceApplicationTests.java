package com.shangpin.ephub.product.business;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.response.HubResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpMethod;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;


import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
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



	@Test
	public void testUserManager(){

		try {
			String reSupplierMsg = httpClient.getForObject(
					"http://192.168.20.121:8080/userLogin/login?username=admin&password=123456", String.class);
			JSONObject supplierDto = JsonUtil.deserialize2(reSupplierMsg, JSONObject.class);
			if(supplierDto.get("data")!=null){
				String data = supplierDto.get("data").toString();
				System.out.println(supplierDto.get("data"));
				LinkedMultiValueMap<String,String> map=new LinkedMultiValueMap<String,String>() ;
				map.add("params", "[\""+data+"\",\"SCM\"]")  ;
				HttpHeaders headers = new HttpHeaders();
				MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded");
				headers.setContentType(type);
				HttpEntity<LinkedMultiValueMap<String,String>> req = new HttpEntity<LinkedMultiValueMap<String,String>>(map,headers);
				ResponseEntity<String> res=httpClient.postForEntity("http://192.168.20.121:8080/facade/json/com.shangpin.uaas.api/User/findMenusByAppCode",req,String.class)  ;
				System.out.println(res.getBody());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// @Test
	// public void test(){
	// try {
	// PendingQuryDto request = new PendingQuryDto();
	// request.setPageIndex(1);
	// request.setPageSize(20);
	// HubResponse<PendingProducts> pendingProducts =
	// httpClient.postForObject("http://192.168.20.176:8003/pending-product/list",
	// request, HubResponse.class);
	// System.out.println(pendingProducts);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void testBrands() {
	// try {
	// BrandRequstDto request = new BrandRequstDto();
	// request.setBrandNo("B");
	// HttpEntity<BrandRequstDto> requestEntity = new
	// HttpEntity<BrandRequstDto>(request);
	//
	// ResponseEntity<BasicDataResponse<BrandDom>> entity =
	// httpClient.exchange(ApiAddressProperties.getGmsBrandUrl(),
	// HttpMethod.POST, requestEntity, new
	// ParameterizedTypeReference<BasicDataResponse<BrandDom>>() {
	// });
	// BasicDataResponse<BrandDom> body = entity.getBody();
	// System.out.println(body.getResDatas());
	//
	// String response =
	// httpClient.postForObject(ApiAddressProperties.getGmsBrandUrl(), request,
	// String.class);
	// System.out.println(response);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
//	 @Test
//	 public void testGetCategory(){
//	 try {
//	 CategoryRequestDto request = new CategoryRequestDto();
//	 request.setCategoryNo("A01B01C07D02");
//	 BasicDataResponse<?> response =
//	 httpClient.postForObject(ApiAddressProperties.getGmsCategoryUrl(),
//	 request, BasicDataResponse.class);
//	 System.out.println(response);
//	 } catch (Exception e) {
//	 e.printStackTrace();
//	 }
//	 }
	//
//	@Test
//	public void testGetProductSize() {
//
//		try {
//
//
//
//			// http://{host}/facade/json/com.shangpin.uaas.api/User/findMenusByAppCode
//			MatchSizeResult matchSizeResult = new MatchSizeResult();
//			String sizeValue = "36";
//			SizeRequestDto request = new SizeRequestDto();
//			request.setBrandNo("B0200");
//			request.setCategoryNo("A03B02C02D09");
//
//			HttpEntity<SizeRequestDto> requestEntity = new HttpEntity<SizeRequestDto>(request);
//			ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = httpClient.exchange(
//					ApiAddressProperties.getGmsSizeUrl(), HttpMethod.POST, requestEntity,
//					new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
//					});
//
//			@SuppressWarnings("unused")
//			HubResponseDto<CategoryScreenSizeDom> cate = entity.getBody();
//			CategoryScreenSizeDom size = entity.getBody().getResDatas().get(0);
//			boolean sizeIsExist = false;
//			String result = null;
//			boolean isNotTemplate = false;
//			Map<String, String> screenSizeMap = new HashMap<String, String>();
//			Map<String, String> standardSizeMap = new HashMap<String, String>();
//			if (size != null) {
//				List<SizeStandardItem> list = size.getSizeStandardItemList();
//				if (list != null && list.size() > 0) {
//					// 获取筛选尺码和标准尺码map集合
//					getSizeMap(list, screenSizeMap, standardSizeMap);
//					// 第一步：从标准尺码中查找匹配尺码
//					sizeIsExist = matchStandardSize(sizeValue, standardSizeMap, matchSizeResult);
//					if (!sizeIsExist) {
//						// 第二步：从标准尺码中未匹配到尺码。继续从筛选尺码中匹配
//						sizeIsExist = matchScreenSize(sizeValue, screenSizeMap, matchSizeResult);
//					}
//				} else {
//					isNotTemplate = true;
//				}
//			} else {
//				isNotTemplate = true;
//			}
//
//			if (matchSizeResult.isPassing()) {
//				matchSizeResult.setPassing(true);
//			} else {
//				matchSizeResult.setPassing(false);
//				if (isNotTemplate) {
//					matchSizeResult.setNotTemplate(isNotTemplate);
//					result = "scm没有尺码模板";
//				} else {
//					// sizeIsExist为true，说明匹配到尺码并且匹配到多个
//					if (sizeIsExist) {
//						matchSizeResult.setMultiSizeType(true);
//						result = "含有多个尺码模板";
//					} else {
//						if (standardSizeMap.size() > 0) {
//							result = "尺码：" + "" + "未匹配成功";
//							matchSizeResult.setFilter(true);
//						} else {
//							result = "scm没有尺码模板";
//							matchSizeResult.setNotTemplate(true);
//						}
//					}
//				}
//			}
//			matchSizeResult.setResult(result);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private boolean matchScreenSize(String size, Map<String, String> map2, MatchSizeResult matchSizeResult) {
//		boolean sizeIsExist = false;
//		if (map2.size() > 0) {
//			int i = 0;
//			for (Map.Entry<String, String> entry : map2.entrySet()) {
//				String value = entry.getValue();
//				String key = entry.getKey();
//				if (size.equals(value)) {
//					sizeIsExist = true;
//					i++;
//					String[] arr = key.split(":", -1);
//					if (arr.length == 2) {
//						matchSizeResult.setSizeType(arr[0]);
//						matchSizeResult.setSizeValue(arr[1]);
//						matchSizeResult.setPassing(true);
//						if (i >= 2) {
//							matchSizeResult.setPassing(false);
//							break;
//						}
//					}
//				}
//			}
//		}
//		return sizeIsExist;
//
//	}
//
//	private boolean matchStandardSize(String size, Map<String, String> map0, MatchSizeResult matchSizeResult) {
//		boolean isExist = false;
//		if (map0.size() > 0) {
//			int i = 0;
//			for (Map.Entry<String, String> entry : map0.entrySet()) {
//				String value = entry.getValue();
//				String key = entry.getKey();
//				if (size.equals(value)) {
//					String[] arr = key.split(":", -1);
//					isExist = true;
//					i++;
//					if (arr.length == 2) {
//						matchSizeResult.setSizeType(arr[0]);
//						matchSizeResult.setSizeValue(arr[1]);
//						matchSizeResult.setPassing(true);
//						if (i >= 2) {
//							matchSizeResult.setPassing(false);
//							break;
//						}
//					}
//				}
//			}
//		}
//		return isExist;
//	}
//
//	private void getSizeMap(List<SizeStandardItem> list, Map<String, String> map2, Map<String, String> map0) {
//		for (SizeStandardItem item : list) {
//			String sizeValue = item.getSizeStandardValue();
//			String sizeStandardName = item.getSizeStandardName();
//			// 筛选尺码
//			if (item.getIsScreening() == (byte) 2) {
//				map2.put(sizeStandardName + ":" + sizeValue, sizeValue);
//			}
//			// 标准尺码
//			if (item.getIsScreening() == (byte) 0) {
//				map0.put(sizeStandardName + ":" + sizeValue, sizeValue);
//			}
//		}
//

//	}

	@Autowired
	HubSlotSpuService hubSlotSpuService;

	@Autowired
	HubSpuPendingGateWay gateWay;

	@Test
	public void testAddSlotSpu(){

		PendingProductDto pendingProductDto = new PendingProductDto();
		HubSpuPendingCriteriaDto criteriaDto = new HubSpuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(258248L);
		List<HubSpuPendingDto> spuPendingDtos   = gateWay.selectByCriteria(criteriaDto);
		if(null!=spuPendingDtos&&spuPendingDtos.size()>0){
			BeanUtils.copyProperties(spuPendingDtos.get(0),pendingProductDto);

//			pendingProductDto.setSpuModel("AC03WR17 404002");

			try {
				hubSlotSpuService.addSlotSpuAndSupplier(pendingProductDto);
//				hubSlotSpuService.updateSlotSpu(pendingProductDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	@Autowired
	private UploadPicGateway uploadPicGateway;

	@Test
	public void testUpload(){

		try {
			File file = new File("E:\\other\\1.jpg");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            byte[] byteArray = bos.toByteArray();
            String base64 = new BASE64Encoder().encode(byteArray );
            UploadPicDto dto = new UploadPicDto();
            dto.setBase64(base64);
            dto.setExtension("jpg");
            String url = uploadPicGateway.upload(dto );
    		System.out.println("url======"+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private PictureService pictureService;
	@Test
	public void testDeletePic(){
		List<String> urls =  new ArrayList<String>();
		urls.add("http://192.168.9.71/group2/M00/01/58/wKgJR1lMmNWAD8KKAAEsQm-Q7jY392.jpg");
//		urls.add("11111.jpg");
		Map<String, Integer> map = pictureService.deletePics(urls);
		System.out.println(map);
	}
	@Autowired
	private StudioSlotDefectiveSpuPicGateWay defectiveSpuPicGateWay;

	@Test
	public void testDelete(){
		StudioSlotDefectiveSpuPicWithCriteriaDto withCriteria = new StudioSlotDefectiveSpuPicWithCriteriaDto();
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo("http://192.168.9.71:80/group2/M00/02/45/wKgJR1jSXnWAIbaAAAJt212IoZ8475.jpg");
		withCriteria.setCriteria(criteria );
		StudioSlotDefectiveSpuPicDto studioSlotDefectiveSpuPicDto = new StudioSlotDefectiveSpuPicDto();
		studioSlotDefectiveSpuPicDto.setDataState(DataState.DELETED.getIndex());
		withCriteria.setStudioSlotDefectiveSpuPic(studioSlotDefectiveSpuPicDto );
		int i = defectiveSpuPicGateWay.updateByCriteriaSelective(withCriteria );
		System.out.println(i);
	}


	@Autowired
	HubSlotSpuService slotSpuService;


	@Test
	public void testSloSupplierSearch(){

		SpuSupplierQueryDto quryDto = new SpuSupplierQueryDto();
//		quryDto.setSpuModel("ML07");
		quryDto.setSupplierNo("S0000766");
		List<SlotSpuDto> slotSpu = slotSpuService.findSlotSpu(quryDto);


        System.out.println("slot size = " + slotSpu.size());

	}



}
