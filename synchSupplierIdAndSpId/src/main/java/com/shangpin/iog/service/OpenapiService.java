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
	
	public void dotheJob(String suppliers,String startDate,String endDate,String full){
		try {
			
			if(StringUtils.isNotBlank(suppliers)){
				for(String supplier : suppliers.split(",")){
					try {
						loggerInfo.info("=================供应商"+supplier+"开始同步========================");
						synchAndSaveRalation(supplier,startDate, endDate,full);
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
						synchAndSaveRalation(supplier.getSupplierId(), startDate, endDate,full);
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
	
	private void synchAndSaveRalation(String supplier,String start,String end,String full) throws Exception{
		int pageIndex=1,pageSize=100;
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);;		
		boolean hasNext=true;		
		//获取已有的SPSKUID
		Map<String,String> map = new HashMap<>();
		if(null!=skuRelationService){
			List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);
			for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
				map.put(skuRelationDTO.getSopSkuId(),skuRelationDTO.getSupplierSkuId());
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
					if(1==ice.IsDeleted){
						continue;
					}
					/**
					 * 如果对应关系不存在则新增
					 */
					if (!map.containsKey(ice.SkuNo)){ //海外库保留尚品SKU和供货商SKU对照关系
						
						SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
						skuRelationDTO.setSupplierId(supplier);
						skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
						skuRelationDTO.setSopSkuId(ice.SkuNo);
						skuRelationDTO.setCreateTime(date);
						try {							
							skuRelationService.saveSkuRelateion(skuRelationDTO);							
							loggerInfo.info(ice.SupplierSkuNo + "----"+ice.SkuNo+" 保存SKU对应关系耗时 " + (System.currentTimeMillis() - startDate));
						} catch (ServiceException e) {
							loggerError.error(skuRelationDTO.toString() + "保存失败");
						}
					}else{
						/**
						 * 如果对应关系存在，则用供应商门户编号、供应商sku编号、尚品sku编号去查询记录
						 * 如果记录不存在，则说明供应商sku编号在sop中发生了变化，需要更新EP库的供应商sku编号
						 * 最后将map中的key移除，以便在最后判断无效的尚品sku编号
						 */
						int count = skuRelationService.countSkuRelation(supplier, ice.SupplierSkuNo, ice.SkuNo);
						if(count <= 0){
							try {
								SkuRelationDTO skuRelationDTO = new SkuRelationDTO();
								skuRelationDTO.setSupplierId(supplier);
								skuRelationDTO.setSupplierSkuId(ice.SupplierSkuNo);
								skuRelationDTO.setSopSkuId(ice.SkuNo);
								skuRelationService.updateSupplierSkuNo(skuRelationDTO);
								loggerInfo.info(ice.SupplierSkuNo + "|"+ice.SkuNo+" 更新SKU对应关系 "); 
							} catch (Exception e) {
								loggerError.error("更新供应商sku编号异常："+e.getMessage(),e); 
							}
						}
						map.remove(ice.SkuNo);
					}

				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
		/**
		 * 全量更新时将作废的尚品sku编号从库里删除掉
		 */
		if ("1".equals(full) && map.size() > 0) {
			for (String spSkuId : map.keySet()) {
				try {
					skuRelationService.deleteSkuRelation(supplier, spSkuId);
					loggerInfo.info(supplier+"--"+ spSkuId+"已被删除"); 
				} catch (Exception e) {
					loggerError.error("删除作废的尚品sku编号异常：" + e.getMessage(), e);
				}
			}
		}
		
		
	}
}
