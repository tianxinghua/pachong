package com.shangpin.ephub.product.business.ui.hub.selected.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.dto.BrandDom;
import com.shangpin.ephub.product.business.common.dto.BrandRequstDto;
import com.shangpin.ephub.product.business.common.dto.CategoryRequestDto;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.util.ExportExcelUtils;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.HubResponseDto;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.rest.hubpending.sku.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.service.hub.impl.HubProductServiceImpl;
import com.shangpin.ephub.product.business.ui.hub.waitselected.dto.HubWaitSelectStateDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java Company: www.shangpin.com
 * 
 * @author zhaogenchun
 * @date 2016年12月21日 下午4:06:52
 */
@Service
@Slf4j
public class HubSelectedService {

	@Autowired
	ApiAddressProperties apiAddressProperties;
	@Autowired
	SupplierService supplierService;
	@Autowired
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	HubSpuPicGateWay hubSpuPicGateWay;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	HubProductServiceImpl hubCommonProductServiceImpl;

	public void exportExcel(List<HubWaitSelectResponseDto> list, OutputStream ouputStream) throws Exception {
		String[] headers = { "尚品skuNo","供应商编号","供应商skuNo", "商品名称", "品牌", "货号", "四级品类", "颜色", "材质", "产地", "性别","尺码","供价","供价币种","市场价","市场价币种", "最后更新时间" };
		String[] columns = {"spSkuNo","supplierName","supplierSkuNo","spuName", "brandName", "spuModel", "categoryName", "color", "material", "origin", "gender",
				 "size","supplyPrice","supplyCurry","marketPrice","marketCurry", "updateTime" };
		
		Map<String, String> map = null;
		
		Map<String, List<HubWaitSelectResponseDto>> mapSupplier = new HashMap<String, List<HubWaitSelectResponseDto>>();
		for (HubWaitSelectResponseDto response : list) {
			String supplierNo = response.getSupplierNo();
			if (mapSupplier.containsKey(supplierNo)) {
				mapSupplier.get(supplierNo).add(response);
			} else {
				List<HubWaitSelectResponseDto> listSupplier = new ArrayList<HubWaitSelectResponseDto>();
				listSupplier.add(response);
				mapSupplier.put(supplierNo, listSupplier);
			}
		}
		 HSSFWorkbook workbook = new HSSFWorkbook();
		for (Map.Entry<String, List<HubWaitSelectResponseDto>> entry : mapSupplier.entrySet()) {
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			String supplierNo = entry.getKey();
			String supplierName = null;
			SupplierDTO supp = supplierService.getSupplier(supplierNo);
			log.info("供应商实体：{}",supp);
			if(supp!=null){
				supplierName = supp.getSupplierName();
				if(supplierName==null){
					supplierName = supplierNo;
				}
			}else{
				supplierName = supplierNo;
			}
			List<HubWaitSelectResponseDto> listSupp =  entry.getValue();
			
			for(HubWaitSelectResponseDto response:listSupp){
				map = new HashMap<String, String>();
				convertTOExcel(response,map);
				result.add(map);
			}
			ExportExcelUtils.createSheet(workbook,supplierName, headers, columns, result);
		}
		workbook.write(ouputStream); 
	}

	private void convertTOExcel(HubWaitSelectResponseDto response, Map<String, String> map) {
		String supplierId = response.getSupplierId();
		String supplierSkuNo = response.getSupplierSkuNo();
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSkuPendingDto> listSku = hubSkuPendingGateWay.selectByCriteria(criteria);
		BigDecimal supplyPrice = null;
		BigDecimal marketPrice = null;
		String supplyCurry = null;
		String marketCurry = null;
		String spSkuNo = null;
		if (listSku != null && listSku.size() > 0) {
			supplyPrice = listSku.get(0).getSupplyPrice();
			marketPrice = listSku.get(0).getMarketPrice();
			supplyCurry = listSku.get(0).getSupplyPriceCurrency();
			marketCurry = listSku.get(0).getMarketPriceCurrencyorg();
			spSkuNo = listSku.get(0).getSpSkuNo();
		}
		
		String brandName = getBrand(response.getBrandNo());
		String supplierName = response.getSupplierNo();
		String categoryName = getCategoryName(response.getCategoryNo());
		
		map.put("brandName", brandName);
		map.put("supplyPrice", supplyPrice+"");
		map.put("marketPrice", marketPrice+"");
		map.put("supplyCurry", supplyCurry);
		map.put("marketCurry", marketCurry);
		map.put("spSkuNo", spSkuNo);
		map.put("spuName", response.getSpuName());
		map.put("supplierSkuNo", response.getSupplierSkuNo());
		map.put("spuModel", response.getSpuModel());
		map.put("categoryName", categoryName);
		map.put("color", response.getHubColor());
		map.put("material", response.getMaterial());
		map.put("origin", response.getOrigin());
		map.put("gender", response.getGender());
		map.put("supplierName", supplierName);
		map.put("size", response.getSkuSize());
		map.put("updateTime", DateTimeUtil.getTime(response.getUpdateTime()));
		
	}
	private String getCategoryName(String categoryNo) {
		CategoryRequestDto request = new CategoryRequestDto();
        request.setCategoryNo(categoryNo);

        HttpEntity<CategoryRequestDto> requestEntity = new HttpEntity<CategoryRequestDto>(request);

        ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = restTemplate.exchange(apiAddressProperties.getGmsBrandUrl(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
        });
        HubResponseDto<CategoryScreenSizeDom> body = entity.getBody();
        if(body.getIsSuccess()){
            List<CategoryScreenSizeDom> resDatas = body.getResDatas();
            if(resDatas!=null&&resDatas.size()>0){
            	 return resDatas.get(0).getFourLevelCategoryName();
            }else{
            	return categoryNo;
            }
           
        }else{
        	return categoryNo;
        }
	}
	@Autowired
	    RestTemplate restTemplate;
	private String getBrand(String brandNo) {
		// TODO Auto-generated method stub

		BrandRequstDto request = new BrandRequstDto();
        request.setBrandNo(brandNo);

        HttpEntity<BrandRequstDto> requestEntity = new HttpEntity<BrandRequstDto>(request);

        ResponseEntity<HubResponseDto<BrandDom>> entity = restTemplate.exchange(apiAddressProperties.getGmsBrandUrl(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<HubResponseDto<BrandDom>>() {
        });
        HubResponseDto<BrandDom> body = entity.getBody();
        if(body.getIsSuccess()){
            List<BrandDom> resDatas = body.getResDatas();
            if(resDatas!=null&&resDatas.size()>0){
            	 return resDatas.get(0).getBrandCnName();
            }else{
            	return brandNo;
            }
           
        }else{
        	return brandNo;
        }
	}

	public void exportPicExcel(List<HubWaitSelectResponseDto> list, OutputStream ouputStream) throws Exception {

		String[] headers = { "spSkuNo", "hubSpuId", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String[] columns = { "spSkuNo", "hubSpuId", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String title = "图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubWaitSelectResponseDto response : list) {
			map = new HashMap<String, String>();
			Long hubSpuId = response.getSpuId();
			String spSkuNo = response.getSpSkuNo();
			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			int i = 1;
			if (listPic == null && listPic.size() <= 0) {
				continue;
			}
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				i++;
			}
			map.put("spSkuNo", spSkuNo);
			map.put("hubSpuId", hubSpuId + "");
			result.add(map);
		}
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
	}

	public void exportSelectPicExcel(List<HubWaitSelectStateDto> list, OutputStream ouputStream) throws Exception {

		String[] headers = { "spSkuNo", "hubSpuId", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String[] columns = { "spSkuNo", "hubSpuId", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String title = "选中图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubWaitSelectStateDto response : list) {
			map = new HashMap<String, String>();
			Long hubSpuId = response.getSpuId();
			Long hubSkuId = response.getSkuId();

			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			int i = 1;
			if (listPic == null && listPic.size() <= 0) {
				continue;
			}
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				i++;
			}
			HubSkuDto skuDto = hubSkuGateWay.selectByPrimaryKey(hubSkuId);
			map.put("spSkuNo", skuDto.getSpSkuNo());
			map.put("hubSpuId", hubSpuId + "");
			result.add(map);
		}
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
	}

}
