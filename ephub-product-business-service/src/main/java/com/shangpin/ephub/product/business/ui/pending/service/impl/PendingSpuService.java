package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.AuditState;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.MaterialState;
import com.shangpin.ephub.client.data.mysql.enumeration.OriginState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpSkuSizeState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuColorState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuGenderState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuSeasonState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.BrandService;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * 待处理页面逻辑中处理spu的实现类
 * <p>Title: PendingSpuService</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月1日 下午5:59:26
 *
 */
@Service
@Slf4j
public abstract class PendingSpuService implements IPendingProductService {
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	protected static String picReason = "该商品没有图片";
	
	@Autowired
	protected HubSpuPendingGateWay hubSpuPendingGateWay;
    @Autowired
    protected HubSkuPendingGateWay hubSkuPendingGateWay;
    @Autowired
	protected SupplierService supplierService;
    @Autowired
	protected BrandService brandService;
    @Autowired
	protected CategoryService categoryService;
    @Autowired 
	private HubSpuImportTaskGateWay spuImportGateway;
    @Autowired 
    private ProductImportTaskStreamSender tastSender;
    @Autowired
	protected HubSupplierSpuGateWay hubSupplierSpuGateWay;
    @Autowired
    private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;

    /**
     * 将任务记录保存到数据库
     * @param createUser
     * @return
     */
    protected HubSpuImportTaskDto saveTaskIntoMysql(String createUser,int taskType){
    	HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
    	Date date = new Date();
		hubSpuTask.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		hubSpuTask.setTaskState((byte)TaskState.HANDLEING.getIndex());
		hubSpuTask.setCreateTime(date);
		hubSpuTask.setUpdateTime(date);
		hubSpuTask.setImportType((byte)taskType);
		hubSpuTask.setCreateUser(createUser); 
		hubSpuTask.setTaskFtpFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		hubSpuTask.setSysFileName(createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		hubSpuTask.setResultFilePath("pending_export/"+createUser+"_" + hubSpuTask.getTaskNo()+".xls"); 
		Long spuImportTaskId = spuImportGateway.insert(hubSpuTask);
		hubSpuTask.setSpuImportTaskId(spuImportTaskId);
		return hubSpuTask;
    }
	/**
	 * 构造消息体，并发送消息队列
	 * @param taskNo
	 * @param type
	 * @param data
	 */
    protected void sendMessageToTask(String taskNo,int type,String data){
    	ProductImportTask productImportTask = new ProductImportTask();
    	productImportTask.setMessageId(UUID.randomUUID().toString());
    	productImportTask.setTaskNo(taskNo);
    	productImportTask.setMessageDate(DateTimeUtil.getTime(new Date())); 
    	productImportTask.setData(data);
    	productImportTask.setType(type);
    	tastSender.productExportTaskStream(productImportTask, null);
    }
    
    /**
     * 将UI查询条件转换成数据库查询条件对象
     * @param pendingQuryDto UI查询条件对象
     * @return
     */
    protected HubSpuPendingCriteriaDto findhubSpuPendingCriteriaFromPendingQury(PendingQuryDto pendingQuryDto){
    	HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto = new HubSpuPendingCriteriaDto();
    	if(null != pendingQuryDto.getSpuPendingId()){
    		hubSpuPendingCriteriaDto.createCriteria().andSpuPendingIdEqualTo(pendingQuryDto.getSpuPendingId());
    		return hubSpuPendingCriteriaDto;
    	}
    	hubSpuPendingCriteriaDto.setOrderByClause("update_time desc");
    	if(!StringUtils.isEmpty(pendingQuryDto.getPageIndex()) && !StringUtils.isEmpty(pendingQuryDto.getPageSize())){
            hubSpuPendingCriteriaDto.setPageNo(pendingQuryDto.getPageIndex());
            hubSpuPendingCriteriaDto.setPageSize(pendingQuryDto.getPageSize());
        }
    	List<Integer> inconformities = pendingQuryDto.getInconformities();
		if(CollectionUtils.isNotEmpty(inconformities)){
        	for (int i=0;i<inconformities.size();i++) {
        		Criteria criteria = getCriteria(pendingQuryDto, hubSpuPendingCriteriaDto);
        		if (ProductState.SPU_GENDER_STATE.getIndex() == inconformities.get(i)) {
        			criteria.andSpuGenderStateEqualTo(SpuGenderState.UNHANDLED.getIndex());
        		} else if (ProductState.SPU_BRAND_STATE.getIndex() == inconformities.get(i)) {
        			criteria.andSpuBrandStateEqualTo(SpuBrandState.UNHANDLED.getIndex());
        		} else if(ProductState.CATGORY_STATE.getIndex() == inconformities.get(i)){
        			criteria.andCatgoryStateNotEqualTo(CatgoryState.PERFECT_MATCHED.getIndex());
        		} else if(ProductState.SPU_MODEL_STATE.getIndex() == inconformities.get(i)){
        			criteria.andSpuModelStateEqualTo(SpuModelState.VERIFY_FAILED.getIndex());
        		} else if(ProductState.MATERIAL_STATE.getIndex() == inconformities.get(i)){
        			criteria.andMaterialStateEqualTo(MaterialState.UNHANDLED.getIndex());
        		} else if(ProductState.SPU_COLOR_STATE.getIndex() == inconformities.get(i)){
        			criteria.andSpuColorStateEqualTo(SpuColorState.UNHANDLED.getIndex());
        		} else if(ProductState.ORIGIN_STATE.getIndex() == inconformities.get(i)){
        			criteria.andOriginStateEqualTo(OriginState.UNHANDLED.getIndex());
        		} else if(ProductState.SPU_SEASON_STATE.getIndex() == inconformities.get(i)){
        			criteria.andSpuSeasonStateEqualTo(SpuSeasonState.UNHANDLED.getIndex());
        		} else if(ProductState.SIZE_STATE.getIndex() == inconformities.get(i)){
        			criteria.andSpSkuSizeStateEqualTo(SpSkuSizeState.UNHANDLED.getIndex());
        		}
        		if(i != 0){
        			hubSpuPendingCriteriaDto.or(criteria);
        		}
			}
        	return hubSpuPendingCriteriaDto;
        }else{
        	getCriteria(pendingQuryDto, hubSpuPendingCriteriaDto);
        	return hubSpuPendingCriteriaDto;
        }
    }
	private Criteria getCriteria(PendingQuryDto pendingQuryDto, HubSpuPendingCriteriaDto hubSpuPendingCriteriaDto) {
		Criteria criteria = hubSpuPendingCriteriaDto.createCriteria();
		if(StringUtils.isEmpty(pendingQuryDto.getSpuState()) || "0".equals(pendingQuryDto.getSpuState())){ 
			criteria.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());
		}else if("1".equals(pendingQuryDto.getSpuState())){
			criteria.andSpuStateEqualTo(SpuState.INFO_IMPECCABLE.getIndex());
		}
		if("0".equals(pendingQuryDto.getAuditState())){
			//再处理
			criteria.andAuditStateEqualTo(AuditState.DISAGREE.getIndex());
			if(!StringUtils.isEmpty(pendingQuryDto.getOperator())){
				criteria.andAuditUserLike(pendingQuryDto.getOperator()+"%");
			}
		}else if("1".equals(pendingQuryDto.getAuditState())){
			criteria.andAuditStateEqualTo(AuditState.AGREE.getIndex());
		}else{
			//待处理
			if(!StringUtils.isEmpty(pendingQuryDto.getOperator())){
				criteria.andUpdateUserLike(pendingQuryDto.getOperator()+"%");
			}
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getSupplierNo())){
			criteria.andSupplierNoEqualTo(pendingQuryDto.getSupplierNo());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getSpuModel())){
			criteria.andSpuModelLike("%"+pendingQuryDto.getSpuModel()+"%");
		}
		String hubCategoryNo = pendingQuryDto.getHubCategoryNo();
		if(!StringUtils.isEmpty(hubCategoryNo)){
			if(hubCategoryNo.length() < 12){
				criteria.andHubCategoryNoLike(hubCategoryNo+"%");
			}else{
				criteria.andHubCategoryNoEqualTo(hubCategoryNo);
			}
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubBrandNo())){
			criteria.andHubBrandNoEqualTo(pendingQuryDto.getHubBrandNo());
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason())){
			criteria.andHubSeasonLike("%"+pendingQuryDto.getHubSeason()+"%");
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getHubYear())){
			criteria.andHubSeasonLike(pendingQuryDto.getHubYear()+"%");
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getStatTime())){
			Date startTime = DateTimeUtil.convertFormat(pendingQuryDto.getStatTime(), dateFormat);
			criteria.andUpdateTimeGreaterThanOrEqualTo(startTime);
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getEndTime())){
			Date endTime = DateTimeUtil.convertFormat(pendingQuryDto.getEndTime(),dateFormat);
			criteria.andUpdateTimeLessThan(endTime);
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getCreateTimeStart())){
			Date startTime = DateTimeUtil.convertFormat(pendingQuryDto.getCreateTimeStart(), dateFormat);
			criteria.andCreateTimeGreaterThanOrEqualTo(startTime);
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getCreateTimeEnd())){
			Date endTime = DateTimeUtil.convertFormat(pendingQuryDto.getCreateTimeEnd(),dateFormat);
			criteria.andCreateTimeLessThan(endTime);
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getBrandName())){
			criteria.andHubBrandNoLike("%"+pendingQuryDto.getBrandName()+"%");
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getCategoryName())){
			criteria.andHubCategoryNoLike("%"+pendingQuryDto.getCategoryName()+"%");
		}
		if(-1 != pendingQuryDto.getPicState()){
			if(0 == pendingQuryDto.getPicState()){
				criteria.andPicStateEqualTo(PicState.UNHANDLED.getIndex());
			}else if(1 == pendingQuryDto.getPicState()){
				criteria.andPicStateEqualTo(PicState.HANDLE_ERROR.getIndex());
			}else if(2 == pendingQuryDto.getPicState()){
				criteria.andPicStateEqualTo(PicState.HANDLED.getIndex());
			}
		}
		List<Integer> conformities = pendingQuryDto.getConformities();
		if(CollectionUtils.isNotEmpty(conformities)){
			for(int i = 0;i<conformities.size();i++){
				if (ProductState.SPU_GENDER_STATE.getIndex() == conformities.get(i)) {
        			criteria.andSpuGenderStateEqualTo(SpuGenderState.HANDLED.getIndex());
        		} else if (ProductState.SPU_BRAND_STATE.getIndex() == conformities.get(i)) {
        			criteria.andSpuBrandStateEqualTo(SpuBrandState.HANDLED.getIndex());
        		} else if(ProductState.CATGORY_STATE.getIndex() == conformities.get(i)){
        			criteria.andCatgoryStateEqualTo(CatgoryState.PERFECT_MATCHED.getIndex());
        		} else if(ProductState.SPU_MODEL_STATE.getIndex() == conformities.get(i)){
        			criteria.andSpuModelStateEqualTo(SpuModelState.VERIFY_PASSED.getIndex());
        		} else if(ProductState.MATERIAL_STATE.getIndex() == conformities.get(i)){
        			criteria.andMaterialStateEqualTo(MaterialState.HANDLED.getIndex());
        		} else if(ProductState.SPU_COLOR_STATE.getIndex() == conformities.get(i)){
        			criteria.andSpuColorStateEqualTo(SpuColorState.HANDLED.getIndex());
        		} else if(ProductState.ORIGIN_STATE.getIndex() == conformities.get(i)){
        			criteria.andOriginStateEqualTo(OriginState.HANDLED.getIndex());
        		} else if(ProductState.SPU_SEASON_STATE.getIndex() == conformities.get(i)){
        			criteria.andSpuSeasonStateEqualTo(SpuSeasonState.HANDLED.getIndex());
        		} else if(ProductState.SIZE_STATE.getIndex() == conformities.get(i)){
        			criteria.andSpSkuSizeStateEqualTo(SpSkuSizeState.HANDLED.getIndex());
        		}else if(ProductState.INFOCOMPLETE.getIndex() == conformities.get(i)){
        			criteria.andInfoStateEqualTo(InfoState.PERFECT.getIndex());
        		}else if(ProductState.HAVESTOCK.getIndex() == conformities.get(i)){
        			criteria.andStockStateEqualTo(StockState.HANDLED.getIndex());
        		}
			}
		}
		return criteria;
	}
	
	@Override
    public Map<Long,String> findSupplierCategoryname(List<Long> supplierSpuIds){
    	Map<Long,String> categories = new HashMap<Long,String>();
    	HubSupplierSpuCriteriaDto criteraDto = new HubSupplierSpuCriteriaDto();
    	criteraDto.setPageNo(1);
    	criteraDto.setPageSize(1000); 
    	criteraDto.setFields("supplier_spu_id,supplier_categoryname");
    	criteraDto.createCriteria().andSupplierSpuIdIn(supplierSpuIds);
    	List<HubSupplierSpuDto> spus = hubSupplierSpuGateWay.selectByCriteria(criteraDto);
    	if(CollectionUtils.isNotEmpty(spus)){
    		for(HubSupplierSpuDto dto : spus){
    			categories.put(dto.getSupplierSpuId(), dto.getSupplierCategoryname());
    		}
    	}
    	return categories;
    }
    
    @Override
    public List<HubSpuPendingPicDto> findSpPicUrl(String supplierId,String supplierSpuNo){
    	HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("sp_pic_url,memo,pic_url");
    	criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	return spuPendingPics;
    }
    
	@Override
    public HubResponse<?> exportSpu(PendingQuryDto pendingQuryDto){
    	try {
    		HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
            int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
            pendingQuryDto.setPageSize(total);
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser(),TaskImportTpye.EXPORT_PENDING_SPU.getIndex());
        	sendMessageToTask(taskDto.getTaskNo(),TaskImportTpye.EXPORT_PENDING_SPU.getIndex(),JsonUtil.serialize(pendingQuryDto)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":"+pendingQuryDto.getCreateUser()+"_" + taskDto.getTaskNo()+".xls");
		} catch (Exception e) {
			log.error("导出spu失败，服务器发生错误:"+e.getMessage(),e);
			return HubResponse.errorResp("导出失败，服务器发生错误");
		}
    }
	
	@Override
    public List<PendingProductDto> findPengdingSpu(PendingQuryDto pendingQuryDto){
        List<PendingProductDto> products = new ArrayList<PendingProductDto>();
        try {
            if(null !=pendingQuryDto){
                HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
                int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
                if(total>0){
                    List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteria(criteriaDto);
                    List<Long> supplierSpuIds = new ArrayList<>();
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                    	supplierSpuIds.add(pendingSpu.getSupplierSpuId());
                    }
                    Map<Long,String> categories = findSupplierCategoryname(supplierSpuIds);
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                		PendingProductDto pendingProduct = JavaUtil.convertHubSpuPendingDto2PendingProductDto(pendingSpu);                        
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : pendingSpu.getSupplierNo());
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<HubSpuPendingPicDto> picurls = findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
                        if(CollectionUtils.isNotEmpty(picurls)){
                        	pendingProduct.setSpPicUrl(picurls.get(0).getSpPicUrl()); 
                            pendingProduct.setPicReason(picurls.get(0).getMemo());
                        }else{
                        	pendingProduct.setSpPicUrl(""); 
                            pendingProduct.setPicReason(picReason);
                        }
                        String supplierCategoryname = categories.get(pendingSpu.getSupplierSpuId());
						pendingProduct.setSupplierCategoryname(StringUtils.isEmpty(supplierCategoryname) ? "" : supplierCategoryname);
                        products.add(pendingProduct);
                    }
                }
            }
        } catch (Exception e) {
            log.error("待处理页面查询pending_spu异常："+e.getMessage(),e);
        }
        return products;
    }
}
