package com.shangpin.iog.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.IcePrxHelper;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SkuMapper;

@Component("openapiService")
public class OpenapiService {
	
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");

	@Autowired
	SkuRelationService skuRelationService;
	@Autowired
	SupplierService SupplierService;
	@Autowired
	private ProductFetchService productFetchService;
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
						synchAndSaveRalation(supplier,startDate, endDate);
						loggerInfo.info("=================供应商"+supplier+"同步结束========================");
					} catch (Exception e) {
						e.printStackTrace();
						loggerError.error(e.getMessage());
					}
				}
			}else{
				List<SupplierDTO> sus = supplierService.findByState("1"); 
				for(SupplierDTO supplier : sus){
					try {
						loggerInfo.info("=================供应商"+supplier.getSupplierId()+"开始同步========================");
						synchAndSaveRalation(supplier.getSupplierId(), startDate, endDate);
						loggerInfo.info("=================供应商"+supplier.getSupplierId()+"同步结束========================");
					} catch (Exception e) {
						e.printStackTrace();
						loggerError.error(e.getMessage());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e.getMessage());
		}
	}
	
	private void synchAndSaveRalation(String supplier,String start,String end) throws Exception{
		int pageIndex=1,pageSize=100;
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);;		
		boolean hasNext=true;		
		//获取已有的SPSKUID
		Map<String,String> map = new HashMap<>();
		if(null!=skuRelationService){
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);
			for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
				map.put(skuRelationDTO.getSopSkuId(),null);
			}
		}
		Map<String,String> skuSpSkuMap = new HashMap<String,String>();
		Map<String,String> skuSpProductCodeMap = new HashMap<String,String>();
		if(null != skuDAO){
			List<SkuDTO> skuList = skuDAO.findSpSkuIdsBySupplier(supplier);
			if(null != skuList && skuList.size()>0){
				for(SkuDTO dto : skuList){
					skuSpSkuMap.put(dto.getSkuId(), dto.getSpSkuId());
					skuSpProductCodeMap.put(dto.getSkuId(), dto.getSpProductCode());
				}				
			}
		}
		Date date  = new Date();
		while(hasNext){
			long startDate = System.currentTimeMillis();
			SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
			List<SopProductSkuIce> skus = null;
			//如果异常次数超过5次就跳出
			for(int i=0;i<5;i++){
				startDate = System.currentTimeMillis();
				try {
					SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);					
					loggerInfo.info("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + (System.currentTimeMillis() - startDate));
					skus = products.SopProductSkuIces;
					if(skus!=null){
						i=5;
					}
				} catch (ApiException e1) {
					loggerError.error("openAPI 获取产品信息出错 -ApiException -  "+e1.Message);
				}  catch (Exception e1) {
					loggerError.error("openAPI 获取产品信息出错",e1);
					loggerError.error("openAPI 获取产品信息出错"+e1.getMessage());
				}
			}
			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {
					if (null!=skuRelationService&&!map.containsKey(ice.SkuNo)){ //海外库保留尚品SKU和供货商SKU对照关系
						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
						skuRelationDTO.setSupplierId(supplier);
						skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
						skuRelationDTO.setSopSkuId(ice.SkuNo);
						skuRelationDTO.setCreateTime(date);
						try {							
							skuRelationService.saveSkuRelateion(skuRelationDTO);							
							loggerInfo.info("保存SKU对应关系耗时 " + (System.currentTimeMillis() - startDate));
						} catch (ServiceException e) {
							loggerError.error(skuRelationDTO.toString() + "保存失败");
						}
					}
					if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
						if(1!=ice.IsDeleted){
							try {
								if(!skuSpSkuMap.containsKey(ice.SupplierSkuNo) || !skuSpSkuMap.get(ice.SupplierSkuNo).equals(ice.SkuNo)){ 
									productFetchService.updateSpSkuIdBySupplier(supplier, ice.SupplierSkuNo, ice.SkuNo,String.valueOf(ice.SkuStatus),null);
									loggerInfo.info(ice.SupplierSkuNo+"------------------"+ice.SkuNo);
								}
								if(!skuSpProductCodeMap.containsKey(ice.SupplierSkuNo)){
									productFetchService.updateSpSkuIdBySupplier(supplier, ice.SupplierSkuNo, null,String.valueOf(ice.SkuStatus),sku.ProductModel);
									loggerInfo.info(ice.SupplierSkuNo+"------------------"+sku.ProductModel);
								}
							} catch (Exception e) {
								e.printStackTrace();
								loggerError.error(e.getMessage()); 
							}
						}
					}

				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
	}
}
