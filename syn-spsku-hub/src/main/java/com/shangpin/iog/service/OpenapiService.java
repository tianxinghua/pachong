package com.shangpin.iog.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.syn.enumeration.SpuState;
import com.shangpin.iog.syn.sku.pending.po.HubSkuPending;
import com.shangpin.iog.syn.sku.pending.po.HubSkuPendingCriteria;
import com.shangpin.iog.syn.spu.pending.po.HubSpuPending;
import com.shangpin.iog.syn.spu.pending.po.HubSpuPendingCriteria;
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
	OutTimeConfig outTimeConfig = new OutTimeConfig(1000*10,1000*30,1000*30);
	ObjectMapper mapper =new ObjectMapper();
	public void dotheJob(String hostUrl,String suppliers,String startDate,String endDate){
		try {
			
			if(StringUtils.isNotBlank(suppliers)){
				for(String supplier : suppliers.split(",")){
					try {
						loggerInfo.info("=================供应商"+supplier+"开始同步========================");
						synchAndSaveRalation(hostUrl,supplier,startDate, endDate);
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
						synchAndSaveRalation(hostUrl,supplier.getSupplierId(), startDate, endDate);
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
	
	private void synchAndSaveRalation(String hostUrl ,String supplier,String start,String end) throws Exception{

		//获取hub待处理的信息
		int page =1;
		//获取已有的SPSKUID
		Map<String,String> map = new HashMap<>();

		boolean con = true;
		boolean searchData = true;
		while(con) {
			List<Long> spuPendingIdList = getSpuPendingIdList(hostUrl, supplier, page);

			if (null != spuPendingIdList && spuPendingIdList.size() > 0) {
				if(searchData){  //
					if(null!=skuRelationService){
						//获取映射关系
						List<SkuRelationDTO> skuRelationDTOList = skuRelationService.findListBySupplierId(supplier);
						for(SkuRelationDTO skuRelationDTO:skuRelationDTOList){
							map.put(skuRelationDTO.getSupplierSkuId(),skuRelationDTO.getSopSkuId());
						}
					}
					searchData = false;
				}
				//获取skuPending
				List<HubSkuPending> skuPendings = getSkuPending(hostUrl, spuPendingIdList);

				if (null != skuPendings && skuPendings.size() > 0) {

					updateSpuStateAndSpSkuNo(hostUrl, skuPendings, map);
				}
				if(spuPendingIdList.size()<10){
					con = false;
				}else{
					page++;
				}

			}else{
				con = false;
			}
		}

	}

	private void updateSpuStateAndSpSkuNo(String hostUrl,List<HubSkuPending> skuPendings, Map<String, String> map)  {
		String updateSpuUrl = hostUrl+"/hub-spu-pending/update-by-primary-key-selective";
		String updateSkuUrl = hostUrl+"/hub-sku-pending/update-by-primary-key-selective";
		String updateSpuJson = "",updateSkuJson="";
		for(HubSkuPending skuPending :skuPendings){
             if(map.containsKey(skuPending.getSupplierSkuNo())){

				 try {
					 HubSpuPending tmp = new HubSpuPending();
					 tmp.setSpuPendingId(skuPending.getSpuPendingId());
					 tmp.setSpuState(SpuState.NOHAND.getIndex());
					 tmp.setUpdateTime(new Date());
					 updateSpuJson =  mapper.writeValueAsString(tmp) ;
					 HttpUtil45.operateData("post","json",updateSpuUrl,outTimeConfig,null,updateSpuJson,"","");

					 HubSkuPending skuTmp = new HubSkuPending();
					 skuTmp.setSkuPendingId(skuPending.getSkuPendingId());
					 skuTmp.setSpSkuNo(map.get(skuPending.getSupplierSkuNo()));
					 skuTmp.setInfoFrom(new Integer(1).byteValue());
					 skuTmp.setUpdateTime(new Date());
					 updateSkuJson =  mapper.writeValueAsString(skuTmp) ;
					 HttpUtil45.operateData("post","json",updateSkuUrl,outTimeConfig,null,updateSkuJson,"","");

				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			 }
        }
	}

	private List<HubSkuPending> getSkuPending(String hostUrl, List<Long> spuPendingIdList) throws IOException, ServiceException {
		String skuPendingUrl = hostUrl+"/hub-sku-pending/select-by-criteria";
		HubSkuPendingCriteria criteria =new HubSkuPendingCriteria();
		criteria.setPageNo(1);
		criteria.setPageSize(5000);
		criteria.setFields(" spu_pending_id,sku_pending_id,supplier_sku_no");
		criteria.createCriteria().andSpuPendingIdIn(spuPendingIdList);
		String jsonQuery =  mapper.writeValueAsString(criteria) ;
		String skuPendingResult = HttpUtil45.operateData("post","json",skuPendingUrl,outTimeConfig,null,jsonQuery,"","");
		if(StringUtils.isNotBlank(skuPendingResult)){
			return  mapper.readValue(skuPendingResult,new TypeReference<List<HubSkuPending>>(){});
        }else{
			return  null;
		}
	}

	private List<Long>  getSpuPendingIdList(String hostUrl, String supplier,int page) throws ServiceException, java.io.IOException {
		String pendingUrl = hostUrl+"/hub-spu-pending/select-by-criteria";
		HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
		criteria.setPageNo(page);
		criteria.setFields(" spu_pending_id ");
		criteria.createCriteria().andSupplierIdEqualTo(supplier).andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());

		String jsonQuery =  mapper.writeValueAsString(criteria) ;
		String spuPendingResult = HttpUtil45.operateData("post","json",pendingUrl,outTimeConfig,null,jsonQuery,"","");
		if(StringUtils.isNotBlank(spuPendingResult)){
			List<Long> spuIds = new ArrayList<>();
			List<HubSpuPending> spuPendings =   mapper.readValue(spuPendingResult,new TypeReference<List<HubSpuPending>>(){});
			if(null!=spuPendings&&spuPendings.size()>0){
				for(HubSpuPending spu :spuPendings){
					spuIds.add(spu.getSpuPendingId());
				}
			}
			return  spuIds;
		}else{
			return null;
		}


	}
}
