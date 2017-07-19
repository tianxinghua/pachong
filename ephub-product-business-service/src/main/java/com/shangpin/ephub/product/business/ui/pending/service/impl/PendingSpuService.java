package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.product.business.service.supplier.SupplierInHubService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;
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
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.rest.gms.dto.BrandDom;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.BrandService;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;
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
    private TaskStreamSender tastSender;
    @Autowired
	protected HubSupplierSpuGateWay hubSupplierSpuGateWay;
    @Autowired
    private HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
    @Autowired
    private IHubSpuPendingPicService  hubSpuPendingPicService;

    @Autowired
	private SupplierInHubService supplierInHubService;



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
    	Task productImportTask = new Task();
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
        		} else if(ProductState.HAVEOPERATOR.getIndex() == inconformities.get(i)){
        			criteria.andUpdateUserIsNull();
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

		if(pendingQuryDto.isShoot()){
			//获取需要拍照的供货商
			List<String> needShootSupplierIds = supplierInHubService.getNeedShootSupplierId();
			if(null!=needShootSupplierIds&&needShootSupplierIds.size()>0){
				criteria.andSupplierIdIn(needShootSupplierIds);
			}
			criteria.andSlotStateEqualTo(SpuPendingStudioState.WAIT_HANDLED.getIndex().byteValue());
			criteria.andStockStateEqualTo(StockState.HANDLED.getIndex()).andPicStateEqualTo(PicState.HANDLED.getIndex());
		}else{
			if(StringUtils.isEmpty(pendingQuryDto.getSpuState()) || "0".equals(pendingQuryDto.getSpuState())){

				criteria.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());

			}else if("1".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.INFO_IMPECCABLE.getIndex());
			}else if("2".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.HANDLED.getIndex());
			}else if("3".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.UNABLE_TO_PROCESS.getIndex());
			}else if("4".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.FILTER.getIndex());
			}else if("5".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.HANDLING.getIndex());
			}else if("6".equals(pendingQuryDto.getSpuState())){
				criteria.andSpuStateEqualTo(SpuState.EXISTED_IN_HUB.getIndex());
			}else if("16".equals(pendingQuryDto.getSpuState())){
//			criteria.andSpuStateEqualTo(SpuState.ALL_EXISTED_IN_HUB.getIndex());
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
			if(null!=pendingQuryDto.getPicState()&&-1 != pendingQuryDto.getPicState()){
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
					}else if(ProductState.HAVEOPERATOR.getIndex() == conformities.get(i)){
						criteria.andUpdateUserIsNotNull();
					}
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
    
    /**
     * 查找一个主图
     * @param picurls
     * @return
     */
    protected String findMainUrl(List<HubSpuPendingPicDto> picurls) {
		if(CollectionUtils.isNotEmpty(picurls)){
			for(HubSpuPendingPicDto dto : picurls){
				if(!StringUtils.isEmpty(dto.getSpPicUrl()) && PicHandleState.HANDLED.getIndex() == dto.getPicHandleState()){
					return dto.getSpPicUrl();
				}
			}
		}
		return "";
	}
    
	@Override
    public HubResponse<?> exportSpu(PendingQuryDto pendingQuryDto,TaskType taskType){
    	try {
    		HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
            int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
            pendingQuryDto.setPageSize(total);
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser(),taskType.getIndex());
        	sendMessageToTask(taskDto.getTaskNo(),taskType.getIndex(),JsonUtil.serialize(pendingQuryDto)); 
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
                        String hubCategoryName = categoryService.getHubCategoryNameByHubCategory(pendingProduct.getHubCategoryNo(), category);
                        pendingProduct.setHubCategoryName(null != hubCategoryName ? hubCategoryName : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<HubSpuPendingPicDto> picurls = hubSpuPendingPicService.findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
                        pendingProduct.setSpPicUrl(findMainUrl(picurls)); 
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
	
	@Override
	public HubBrandModelRuleDto findHubBrandModelRule(String hubBrandNo){
		long start = System.currentTimeMillis();
		HubBrandModelRuleCriteriaDto criterraDto = new HubBrandModelRuleCriteriaDto();
		criterraDto.setFields("model_rex,model_rule");
		criterraDto.createCriteria().andHubBrandNoEqualTo(hubBrandNo);
		List<HubBrandModelRuleDto> lists = hubBrandModelRuleGateWay.selectByCriteria(criterraDto);
		log.info("--->货号示例查询总耗时{}",System.currentTimeMillis()-start); 
		if(CollectionUtils.isNotEmpty(lists)){
			return lists.get(0);
		}else{
			return null;
		}
	}
}
