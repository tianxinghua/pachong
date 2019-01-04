package com.shangpin.iog.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.openapi.api.sdk.client.SpClient;
import com.shangpin.openapi.api.sdk.model.ApiResponse;
import com.shangpin.openapi.api.sdk.model.SopProductSku;
import com.shangpin.openapi.api.sdk.model.SopProductSkuPage;
import com.shangpin.openapi.api.sdk.model.SopProductSkuPageQuery;
import com.shangpin.openapi.api.sdk.model.SopSku;

@Component("sopService")
public class SopService {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	
	@Autowired
	SkuRelationService skuRelationService;
	@Autowired
	SupplierService SupplierService;
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	SupplierService supplierService;
	@Autowired
	SkuMapper skuDAO;
	
	public void dotheJob(String suppliers,String startDate,String endDate){
		try {
			
			if(StringUtils.isNotBlank(suppliers)){
				for(String supplier : suppliers.split(",")){
					try {
						loggerInfo.info("=================供应商"+supplier+"开始同步========================");
						SupplierDTO dto = supplierService.hkFindBysupplierId(supplier);
						synchAndSaveRalation("http://open.shangpin.com:8080",dto.getAppKey(),dto.getAppSecret(),supplier,startDate, endDate);
						loggerInfo.info("=================供应商"+supplier+"同步结束========================");
					} catch (Exception e) {
						e.printStackTrace();
						loggerError.error(e.getMessage());
					}
				}
			}else{
				List<SupplierDTO> sus = supplierService.hkFindAllByState("1");
				for(SupplierDTO supplier : sus){
					try {
						loggerInfo.info("=================供应商"+supplier.getSupplierId()+"开始同步========================");
						synchAndSaveRalation("http://open.shangpin.com:8080",supplier.getAppKey(),supplier.getAppSecret(),supplier.getSupplierId(),startDate, endDate);
						loggerInfo.info("=================供应商"+supplier.getSupplierId()+"同步结束========================");
					} catch (Exception e) {
						e.printStackTrace();
						loggerError.error(e.getMessage());
					}
				}
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void synchAndSaveRalation(String host, String app_key,String app_secret, String supplierId, String start, String end) throws Exception{
		int pageIndex = 1, pageSize = 100;
		ApiResponse<SopProductSkuPage> result = null;
		SopProductSkuPageQuery request = null;
		boolean hasNext = true;			
		// 获取已有的SPSKUID
		Map<String, String> map = new HashMap<>();
		if (null != skuRelationService) {
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplierId);
			for (SkuRelationDTO skuRelationDTO : skuRelationDTOList) {
				map.put(skuRelationDTO.getSopSkuId(), null);
			}
		}
		Map<String,String> skuSpSkuMap = new HashMap<String,String>();
		Map<String,String> skuSpProductCodeMap = new HashMap<String,String>();
		if(null != skuDAO){
			List<SkuDTO> skuList = skuDAO.findSpSkuIdsBySupplier(supplierId);
			if(null != skuList && skuList.size()>0){
				for(SkuDTO dto : skuList){
					skuSpSkuMap.put(dto.getSkuId(), dto.getSpSkuId());
					skuSpProductCodeMap.put(dto.getSkuId(), dto.getSpProductCode());
				}				
			}
		}
		Date date = new Date();
		while (hasNext) {
			long startDate = System.currentTimeMillis();
			List<SopProductSku> skus = null;
			request = new SopProductSkuPageQuery();
			request.setPageIndex(pageIndex);
			request.setPageSize(pageSize);
			request.setStartTime(start);
			request.setEndTime(end);
			// 处理：通过openAPI 获取产品信息超时异常，如果异常次数超过5次就跳出
			for (int i = 0; i < 5; i++) {
				startDate = System.currentTimeMillis();
				Date timestamp = new Date(); //
				try {
					result = SpClient.FindCommodityByPage(host, app_key,
							app_secret, timestamp, request);
					SopProductSkuPage products = result.getResponse();
					skus = products.getSopProductSkuIces();
					loggerInfo.info("第" + (i + 1) + "次通过openAPI 获取第 "
							+ pageIndex + "页产品信息，信息耗时"
							+ (System.currentTimeMillis() - startDate));
					if (skus != null) {
						i = 5;
					}
				} catch (Exception e1) {
					loggerError.error("openAPI在异常中获取信息出错" + e1.getMessage(), e1);
				}

			}
			// 五次循环后 还有错误 不在处理 抛出异常
			for (SopProductSku sku : skus) {
				List<SopSku> skuIces = sku.getSopSkuIces();
				for (SopSku ice : skuIces) {

					if (null != skuRelationService	&& !map.containsKey(ice.getSkuNo())) { // 海外库保留尚品SKU和供货商SKU对照关系
						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
						skuRelationDTO.setSupplierId(supplierId);
						skuRelationDTO.setSupplierSkuId(ice.getSupplierSkuNo());
						skuRelationDTO.setSopSkuId(ice.getSkuNo());
						skuRelationDTO.setCreateTime(date);
						try {
							skuRelationService.saveSkuRelateion(skuRelationDTO);
						} catch (ServiceException e) {
							loggerError.error(skuRelationDTO.toString() + "保存失败");
						}
					}
					if (StringUtils.isNotBlank(ice.getSkuNo()) && StringUtils.isNotBlank(ice.getSupplierSkuNo())){						
						try {
							if(StringUtils.isBlank(skuSpSkuMap.get(ice.getSupplierSkuNo())) || StringUtils.isBlank(skuSpProductCodeMap.get(ice.getSupplierSkuNo()))){
								productFetchService.updateSpSkuIdBySupplier(supplierId, ice.getSupplierSkuNo(), ice.getSkuNo(),""+ice.getSkuStatus(),sku.getProductModel());
								loggerInfo.info(ice.getSupplierSkuNo()+"--------------"+ ice.getSkuNo());
							}							
						} catch (Exception e) {
							loggerError.error(e.getMessage()); 
						}
						
					}
				}
			}
			pageIndex++;
			hasNext = (pageSize == skus.size());
		}
	}	
	
}
