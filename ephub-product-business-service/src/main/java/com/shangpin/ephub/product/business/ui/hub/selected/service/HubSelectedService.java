package com.shangpin.ephub.product.business.ui.hub.selected.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.dto.BrandDom;
import com.shangpin.ephub.product.business.common.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.gms.BrandService;
import com.shangpin.ephub.product.business.common.service.gms.CategoryService;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.util.ExportExcelUtils;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
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
	BrandService brandService;
	@Autowired
	CategoryService categoryService;
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
//	@Autowired
//	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	HubProductServiceImpl hubCommonProductServiceImpl;

	public void exportExcel(List<HubWaitSelectResponseDto> list, OutputStream ouputStream) throws Exception {
		String[] headers = {"尚品Sku编号","门户Sku编号","供应商SKU", "商品名称","品类", "品牌","品牌中文", "品牌编号", "货号","商品状态","生效价格","价格状态","操作人","供价*","供价币种*","阶段供价","阶段供价生效时间","阶段供价失效时间","市场价","市场价币种"};
		String[] columns = {"spSkuNo","skuNo","supplierSkuNo","spuName","categoryName", "brandName","brandChName","brandNo", "spuModel","productState","param1","param1","param1","supplyPrice","supplyCurry","param1","param1","param1","marketPrice","marketCurry"};
		
		Map<String, String> map = null;
		
		//拆分数据，以供应商为维度
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
			List<HubWaitSelectResponseDto> listSupp =  entry.getValue();
			for(HubWaitSelectResponseDto response:listSupp){
				map = new HashMap<String, String>();
				convertTOExcel(response,map);
				result.add(map);
			}
			StringBuffer supplierName = new StringBuffer();
			SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
			if(supplierDto!=null&&supplierDto.getSupplierName()!=null){
				String name = supplierDto.getSupplierName();
				 String reg ="[A-Za-z]+";
			        Pattern pattern = Pattern.compile(reg);
			        Matcher matcher = pattern.matcher(name);
			        while (matcher.find()) {
			            String fqdnId = matcher.group();
			            supplierName.append(fqdnId).append(" ");
			        }
			}else{
				supplierName.append(supplierNo);
			}
			ExportExcelUtils.createSheet(workbook,supplierName.toString(), headers, columns, result);
		}
		workbook.write(ouputStream); 
	}

	private void convertTOExcel(HubWaitSelectResponseDto response, Map<String, String> map) {
		if(response.getSupplierSkuId()!=null){
			log.info("supplierSkuId查询:"+response.getSupplierSkuId());
			HubSupplierSkuDto listSku = hubSupplierSkuGateWay.selectByPrimaryKey(response.getSupplierSkuId());
			log.info("返回结果：{}",listSku);
			BigDecimal supplyPrice = null;
			BigDecimal marketPrice = null;
			String supplyCurry = null;
			String marketCurry = null;
			if (listSku != null) {
				supplyPrice = listSku.getSupplyPrice();
				marketPrice = listSku.getMarketPrice();
				supplyCurry = listSku.getSupplyPriceCurrency();
				marketCurry = listSku.getMarketPriceCurrencyorg();
			}
			long start = System.currentTimeMillis();
			BrandDom brandDom = getBrand(response.getBrandNo());
			String categoryName = getCategoryName(response.getCategoryNo());
			log.info("获取品类、品牌名称总耗时："+(System.currentTimeMillis()-start));
			map.put("brandName", brandDom.getBrandEnName());
			map.put("brandChName", brandDom.getBrandCnName());
			map.put("brandNo", brandDom.getBrandNo());
			if(supplyPrice!=null){
				map.put("supplyPrice", supplyPrice+"");	
			}
			if(marketPrice!=null){
				map.put("marketPrice", marketPrice+"");	
			}
			map.put("supplyCurry", supplyCurry);
			map.put("marketCurry", marketCurry);
			map.put("spSkuNo", response.getSpSkuNo());
			map.put("spuName", response.getSpuName());
			map.put("supplierSkuNo", response.getSupplierSkuNo());
			map.put("spuModel", response.getSpuModel());
			map.put("categoryName",categoryName);
//			map.put("color", response.getHubColor());
//			map.put("material", response.getMaterial());
//			map.put("origin", response.getOrigin());
//			map.put("gender", response.getGender());
			map.put("supplierNo", response.getSupplierNo());
//			map.put("size", response.getSkuSize());
			map.put("updateTime", DateTimeUtil.getTime(response.getUpdateTime()));
		}
		
		
	}
	private String getCategoryName(String categoryNo) {
		FourLevelCategory category = categoryService.getGmsCateGory(categoryNo);
        if(category!=null){
           return category.getFourthName();
        }else{
        	return categoryNo;
        }
	}
	private BrandDom getBrand(String brandNo) {
		BrandDom brand = brandService.getGmsBrand(brandNo);
        if(brand!=null){
            return 	brand;
        }else{
        	return null;
        }
	}

	public void exportPicExcel(List<HubWaitSelectResponseDto> list, OutputStream ouputStream) throws Exception {

		String[] headers = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String[] columns = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String title = "图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		Map<Long,String> mapTemp = new HashMap<Long,String>();
		log.info("list总大小："+list.size());
		long start = System.currentTimeMillis();
		for (HubWaitSelectResponseDto response : list) {
			log.info("");
			Long hubSpuId = response.getSpuId();
			if(mapTemp.containsKey(hubSpuId)){
				continue;
			}
			
			map = new HashMap<String, String>();
			long start1 = System.currentTimeMillis();
			String spSkuNo = response.getSpSkuNo();
			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			log.info("查询图片时间："+(System.currentTimeMillis()-start1));
			int i = 1;
			if (listPic == null || listPic.size() <= 0) {
				continue;
			}
			mapTemp.put(hubSpuId, null);
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				i++;
			}
			map.put("spSkuNo", spSkuNo);
			map.put("hubSpuNo", "HUB-"+response.getSpuNo());
			result.add(map);
		}
		log.info("生成excel时间："+(System.currentTimeMillis()-start));
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
		log.info("导出时间："+(System.currentTimeMillis()-start));
	}

	public void exportSelectPicExcel(List<HubWaitSelectStateDto> list, OutputStream ouputStream) throws Exception {

		String[] headers = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String[] columns = { "spSkuNo", "hubSpuNo", "url1", "url2", "url3", "url4", "url5", "url6", "url7", "url8",
				"url9", "url10" };
		String title = "选中图片导出";

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		Map<String, String> mapTemp = new HashMap<String,String>();
		for (HubWaitSelectStateDto response : list) {
			map = new HashMap<String, String>();
			
			String spuNo = response.getSpuNo();
			if(mapTemp.containsKey(spuNo)){
				continue;
			}
			Long hubSpuId = response.getSpuId();
			Long hubSkuId = response.getSkuId();
			HubSpuPicCriteriaDto criteria = new HubSpuPicCriteriaDto();
			criteria.createCriteria().andSpuIdEqualTo(hubSpuId);
			List<HubSpuPicDto> listPic = hubSpuPicGateWay.selectByCriteria(criteria);
			int i = 1;
			if (listPic == null || listPic.size() <= 0) {
				continue;
			}
			mapTemp.put(spuNo, null);
			for (HubSpuPicDto pic : listPic) {
				if (i == 11) {
					break;
				}
				map.put("url" + i, pic.getSpPicUrl());
				i++;
			}
			HubSkuDto skuDto = hubSkuGateWay.selectByPrimaryKey(hubSkuId);
			map.put("spSkuNo", skuDto.getSpSkuNo());
			map.put("hubSpuNo", "HUB-"+skuDto.getSpuNo());
			result.add(map);
		}
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
	}

}
