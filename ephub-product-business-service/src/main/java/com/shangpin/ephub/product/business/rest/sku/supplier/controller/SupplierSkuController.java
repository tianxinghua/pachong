package com.shangpin.ephub.product.business.rest.sku.supplier.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuQureyDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ResponseData;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ResponseModelData;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.StockQuery;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.StockResult;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiModelResult;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiQuery;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiResult;
import com.shangpin.ephub.product.business.rest.sku.supplier.dto.ZhiCaiSkuResult;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**

 */

@RestController
@RequestMapping("/supplier-sku")
@Slf4j
public class SupplierSkuController {
	
	@Autowired
	private HubSupplierSkuGateWay supplierSkuGateWay;
	@Autowired
	private HubSupplierSpuGateWay supplierSpuGateWay;
	@Autowired
	private HubSpuGateWay hubSpuGateWay;
	private final static String supplierId = "123456789";


	
	@RequestMapping(value="/list-stock",method=RequestMethod.POST)
	public List<StockResult> priceList(@RequestBody StockQuery stockQuery){
        List<StockResult> stockResults = new ArrayList<>();
        if(null==stockQuery) return stockResults;
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.setPageNo(stockQuery.getPageIndex());
		criteria.setPageSize(stockQuery.getPageSize());
		criteria.createCriteria().andSupplierIdEqualTo(stockQuery.getSupplierId()).andSpSkuNoIsNotNull();
		List<HubSupplierSkuDto> skuDtos = supplierSkuGateWay.selectByCriteria(criteria);
		if(null != skuDtos&&skuDtos.size()>0){
			for(HubSupplierSkuDto sku:skuDtos){
				StockResult stockResult = new StockResult();
				stockResult.setSupplierSkuNo(sku.getSupplierSkuNo());
				stockResult.setStock(sku.getStock());
				stockResult.setSize(sku.getSupplierSkuSize());
				stockResults.add(stockResult);
			}

		}
		return stockResults;
	}
	//根据品牌查询zhicai原始供应商商品详情url等信息
	@RequestMapping(value="/get-product",method=RequestMethod.POST)
	public HubResponse<?> getZhiCaiByBrand(@RequestBody ZhiCaiQuery zhiCaiQuery){
		HubSupplierSpuQureyDto dto = new HubSupplierSpuQureyDto();
		if(zhiCaiQuery.getPageIndex()==0||zhiCaiQuery.getPageSize()==0) {
			dto.setPageIndex(0);
			dto.setPageSize(20);
		}else {
			dto.setPageIndex(zhiCaiQuery.getPageIndex()-1);
			dto.setPageSize(zhiCaiQuery.getPageSize());
		}
		if(zhiCaiQuery.getBrandName()==null||zhiCaiQuery.getBrandName().equals(""))
			dto.setSupplierId(supplierId);
		else {
			dto.setSupplierId(supplierId);
			dto.setBrandName(zhiCaiQuery.getBrandName());
		}
		List<HubSupplierSpuDto> hubSupplierSpuDtoList = supplierSpuGateWay.selectByBrand(dto);
		int total = supplierSpuGateWay.count(dto);
		List<ZhiCaiResult> list = new ArrayList<>();
		for(HubSupplierSpuDto hubSupplierSpuDto : hubSupplierSpuDtoList) {
			ZhiCaiResult zhiCaiResult = new ZhiCaiResult();
			zhiCaiResult.setProductUrl(hubSupplierSpuDto.getProductUrl());
			zhiCaiResult.setSupplierSpuModel(hubSupplierSpuDto.getSupplierSpuModel());
			zhiCaiResult.setSupplierSpuNo(hubSupplierSpuDto.getSupplierSpuNo());
			HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
			criteria.createCriteria().andSupplierSpuIdEqualTo(hubSupplierSpuDto.getSupplierSpuId());
			List<HubSupplierSkuDto> skuDtos = supplierSkuGateWay.selectByCriteria(criteria);
			
			List<ZhiCaiSkuResult> skuList = new ArrayList<>();
			for(HubSupplierSkuDto hubSupplierSkuDto : skuDtos) {
				ZhiCaiSkuResult zhiCaiSkuResult = new ZhiCaiSkuResult();
				zhiCaiSkuResult.setSize(hubSupplierSkuDto.getSupplierSkuSize());
				zhiCaiSkuResult.setSpSkuNo(hubSupplierSkuDto.getSpSkuNo());
				zhiCaiSkuResult.setSupplierSkuNo(hubSupplierSkuDto.getSupplierSkuNo());
				skuList.add(zhiCaiSkuResult);
			}
			zhiCaiResult.setZhiCaiSkuResultList(skuList);
			list.add(zhiCaiResult);
		}
		ResponseData data = new ResponseData();
		data.setZhiCaiResultList(list);
		data.setTotal(total);
        return HubResponse.successResp(data);
	}
	//zhicai根据品牌查询hubspu下的尚品货号
	
	@RequestMapping(value="/get-spu-model",method=RequestMethod.POST)
	public HubResponse<?> getZhiCaiSpuModelByBrand(@RequestBody ZhiCaiQuery zhiCaiQuery){
		HubSupplierSpuQureyDto dto = new HubSupplierSpuQureyDto();
		if(zhiCaiQuery.getPageIndex()==0||zhiCaiQuery.getPageSize()==0) {
			dto.setPageIndex(0);
			dto.setPageSize(20);
		}else {
			dto.setPageIndex(zhiCaiQuery.getPageIndex()-1);
			dto.setPageSize(zhiCaiQuery.getPageSize());
		}
		if(zhiCaiQuery.getBrandNo()==null||zhiCaiQuery.getBrandNo().equals(""))
			dto.setSupplierId(supplierId);
		else {
			dto.setSupplierId(supplierId);
			dto.setBrandNo(zhiCaiQuery.getBrandNo());
		}
		List<HubSpuDto> hubSpuDtoList= hubSpuGateWay.selectByBrand(dto);
		int total = hubSpuGateWay.count(dto);
		List<ZhiCaiModelResult> list = new ArrayList<>();
		for(HubSpuDto hubSpuDto : hubSpuDtoList) {
			ZhiCaiModelResult rs = new ZhiCaiModelResult();
			rs.setProductModel(hubSpuDto.getSpuModel());
			rs.setSpSpu(hubSpuDto.getSpuNo());
			list.add(rs);
		}
		ResponseModelData data = new ResponseModelData();
		data.setZhiCaiResultList(list);
		data.setTotal(total);
        return HubResponse.successResp(data);
	}

}
