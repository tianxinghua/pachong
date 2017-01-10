package com.shangpin.ephub.product.business.ui.hub.selected.service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.hub.dto.HubWaitSelectResponseDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.common.util.ExportExcelUtils;
import com.shangpin.ephub.product.business.service.hub.impl.HubProductServiceImpl;

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
	HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateway;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
//	@Autowired
	
	@Autowired
	HubProductServiceImpl hubCommonProductServiceImpl;

	public void exportExcel(List<HubWaitSelectResponseDto> list,OutputStream ouputStream) throws Exception{
		
		String []headers = {"品牌","货号","四级品类","颜色","材质","产地","性别","供应商","尺码","最后更新时间"};
		String []columns = {"brandName","spuModel","categoryName","color","material","origin","gender","supplierName","size","updateTime"};
		String title = "商品导出";
		
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		for(HubWaitSelectResponseDto response:list){
			
			map = new HashMap<String, String>();
			String brandName = response.getBrandNo();
			String supplierName = response.getSupplierNo();
			String categoryName = response.getCategoryNo();
			map.put("brandName",brandName);
			map.put("spuModel",response.getSpuModel());
			map.put("categoryName", categoryName);
			map.put("color",response.getHubColor());
			map.put("material",response.getMaterial());
			map.put("origin",response.getOrigin());
			map.put("gender",response.getGender());
			map.put("supplierName",supplierName);
			map.put("size",response.getSkuSize());
			map.put("updateTime",DateTimeUtil.getTime(response.getUpdateTime()));
			result.add(map);
		}
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
	}
	
public void exportPicExcel(List<HubWaitSelectResponseDto> list,OutputStream ouputStream) throws Exception{
		
		String []headers = {"品牌","货号","四级品类","颜色","材质","产地","性别","供应商","尺码","最后更新时间"};
		String []columns = {"brandName","spuModel","categoryName","color","material","origin","gender","supplierName","size","updateTime"};
		String title = "商品导出";
		
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		for(HubWaitSelectResponseDto response:list){
			
			String supplierId = response.getSupplierId();
			String supplierSkuNo = response.getSupplierSkuNo();
			
			
			
			map = new HashMap<String, String>();
			String brandName = response.getBrandNo();
			String supplierName = response.getSupplierNo();
			String categoryName = response.getCategoryNo();
			map.put("brandName",brandName);
			map.put("spuModel",response.getSpuModel());
			map.put("categoryName", categoryName);
			map.put("color",response.getHubColor());
			map.put("material",response.getMaterial());
			map.put("origin",response.getOrigin());
			map.put("gender",response.getGender());
			map.put("supplierName",supplierName);
			map.put("size",response.getSkuSize());
			map.put("updateTime",DateTimeUtil.getTime(response.getUpdateTime()));
			result.add(map);
		}
		ExportExcelUtils.exportExcel(title, headers, columns, result, ouputStream);
	}

}
