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

import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.MaterialState;
import com.shangpin.ephub.client.data.mysql.enumeration.OriginState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
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
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.BrandDom;
import com.shangpin.ephub.product.business.common.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
import com.shangpin.ephub.product.business.common.service.gms.BrandService;
import com.shangpin.ephub.product.business.common.service.gms.CategoryService;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.rest.model.controller.HubBrandModelRuleController;
import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.SupplierProductVo;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PendingProductService </p>
 * <p>Description: 待处理页面Service实现类</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:17:57
 *
 */
@Service
@Slf4j
public class PendingProductService implements IPendingProductService{

    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;
    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;
    @Autowired
    private SupplierService supplierService;
    @Autowired
	private HubCheckService hubCheckService;
    @Autowired
    private HubPendingSpuCheckService hubPendingSpuCheckService;
    @Autowired
    private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
    @Autowired
    private HubBrandModelRuleController hubBrandModelRule;
    @Autowired
    private HubSpuGateWay hubSpuGateway;
    @Autowired 
	private HubSpuImportTaskGateWay spuImportGateway;
    @Autowired 
    private ProductImportTaskStreamSender tastSender;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HubSupplierSpuGateWay hubSupplierSpuGateWay;
    @Autowired
    private HubSupplierSkuGateWay hubSupplierSkuGateWay;
    @Autowired
    private PengdingToHubGateWay pendingToHubGateWay;

    @Override
    public HubResponse<?> exportSku(PendingQuryDto pendingQuryDto){
    	try {
    		HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
            int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
            pendingQuryDto.setPageSize(total);
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser(),TaskImportTpye.EXPORT_PENDING_SKU.getIndex());
        	sendMessageToTask(taskDto.getTaskNo(),TaskImportTpye.EXPORT_PENDING_SKU.getIndex(),JsonUtil.serialize(pendingQuryDto)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":"+pendingQuryDto.getCreateUser()+"_" + taskDto.getTaskNo()+".xls");
		} catch (Exception e) {
			log.error("导出sku失败，服务器发生错误:"+e.getMessage(),e);
			return HubResponse.errorResp("导出失败，服务器发生错误");
		}
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
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                		PendingProductDto pendingProduct = convertHubSpuPendingDto2PendingProductDto(pendingSpu);                        
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : pendingSpu.getSupplierNo());
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<String> picurls = findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
                        pendingProduct.setSpPicUrl(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0) : ""); 
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
    public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto){
    	log.info("接收到的查询条件："+JsonUtil.serialize(pendingQuryDto)); 
    	long start = System.currentTimeMillis();
        PendingProducts pendingProducts = new PendingProducts();
        List<PendingProductDto> products = new ArrayList<PendingProductDto>();
        try {
            if(null !=pendingQuryDto){
                HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
                int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
                log.info("待处理页面查询返回数据个数================"+total);
                if(total>0){
                    List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteria(criteriaDto);
                    List<Long> spuPendingIds = new ArrayList<Long>();
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                    	spuPendingIds.add(pendingSpu.getSpuPendingId());
                    }
                    long start_sku = System.currentTimeMillis();
                    Map<Long,List<HubSkuPendingDto>> pendingSkus = findPendingSkuBySpuPendingId(spuPendingIds);
                    log.info("--->待处理查询sku耗时{}",System.currentTimeMillis()-start_sku); 
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                        PendingProductDto pendingProduct = convertHubSpuPendingDto2PendingProductDto(pendingSpu);
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<HubSkuPendingDto> skus = pendingSkus.get(pendingSpu.getSpuPendingId());
                        pendingProduct.setHubSkus(CollectionUtils.isNotEmpty(skus) ? skus : new ArrayList<HubSkuPendingDto>());
                        List<String> picurls = findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo());
                        pendingProduct.setSpPicUrl(CollectionUtils.isNotEmpty(picurls) ? picurls.get(0) : ""); 
                        pendingProduct.setPicUrls(picurls); 
                        pendingProduct.setUpdateTimeStr(null != pendingSpu.getUpdateTime() ? DateTimeUtil.getTime(pendingSpu.getUpdateTime()) : "");
                        products.add(pendingProduct);
                    }
                    pendingProducts.setProduts(products);
                }
                pendingProducts.setTotal(total);
            }
        } catch (Exception e) {
            log.error("待处理页面查询异常："+e.getMessage(),e);
        }
        log.info("--->待处理查询总耗时{}",System.currentTimeMillis()-start); 
        return pendingProducts;
    }
    @Override
    public Map<Long,List<HubSkuPendingDto>> findPendingSkuBySpuPendingId(List<Long> spuPendingIds) throws Exception{
    	Map<Long,List<HubSkuPendingDto>> pendigSkus = new HashMap<>();
    	try {
            HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
            criteriaDto.setPageNo(1);
            criteriaDto.setPageSize(1000); 
            criteriaDto.setOrderByClause("spu_pending_id,hub_sku_size");
            criteriaDto.createCriteria().andSpuPendingIdIn(spuPendingIds);
            List<HubSkuPendingDto> skus = hubSkuPendingGateWay.selectByCriteria(criteriaDto);
            if(CollectionUtils.isNotEmpty(skus)){
            	for(HubSkuPendingDto sku : skus){
            		Long spuPendingId = sku.getSpuPendingId();
					if(pendigSkus.containsKey(spuPendingId)){
						pendigSkus.get(spuPendingId).add(sku);
            		}else{
            			List<HubSkuPendingDto> list = new ArrayList<>();
            			list.add(sku);
            			pendigSkus.put(spuPendingId, list);
            		}
            	}
            }
        } catch (Exception e) {
            log.error("pending表根据spu id查询sku时出错："+e.getMessage(),e);
        }
    	return pendigSkus;
    }
    @Override
    public HubResponse<PendingUpdatedVo> updatePendingProduct(PendingProductDto pendingProductDto){
    	log.info("接收到的待校验的数据：{}",pendingProductDto);
    	HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
    	response.setCode("0"); //初始设置为成功
    	PendingUpdatedVo updatedVo = null;
    	boolean pass = true; //全局用来判断整条数据是否校验通过
    	HubSpuDto hubSpuDto = null;
    	try {
            if(null != pendingProductDto){
            	//开始校验spu
            	BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);
            	if(brandModelResult.isPassing()){
            		hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
            		if(null == hubSpuDto){
            			HubPendingSpuCheckResult spuResult = hubPendingSpuCheckService.checkHubPendingSpu(pendingProductDto);
            			if(!spuResult.isPassing()){
            				pass = false ;
            				log.info("pending spu校验失败，不更新："+spuResult.getResult());
            				updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),spuResult.getResult());
            			}
            		}
            	}else{
            		pass = false ;
            		log.info("pending spu校验失败，不更新：货号校验不通过。");
            		updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"货号校验不通过");
            	}
            	//开始校验sku
            	List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
            	if(null == updatedVo){
            		updatedVo = new PendingUpdatedVo();
            		updatedVo.setSpuResult(""); 
            		updatedVo.setSpuPendingId(pendingProductDto.getSpuPendingId()); 
            	}
            	List<PendingSkuUpdatedVo> skus = new ArrayList<PendingSkuUpdatedVo>();
                for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){
                	String hubSkuSize = hubSkuPendingDto.getHubSkuSize();
                	hubSkuSize = StringUtils.isEmpty(hubSkuSize) ? "" : hubSkuSize;
                	log.info("从页面接收到的尺码信息===="+hubSkuSize); 
                	if(hubSkuSize.startsWith("排除")){
                		hubSkuPendingDto.setHubSkuSizeType("排除");
                		hubSkuPendingDto.setHubSkuSize(null);//目的是不更新尺码值
                		hubSkuPendingDto.setFilterFlag(FilterFlag.INVALID.getIndex());
                		hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                	}else if(hubSkuSize.startsWith("尺寸")){
                		hubSkuPendingDto.setHubSkuSizeType("尺寸"); 
                		hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));
                		hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                		hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
                	}else{
                		String [] arr = hubSkuSize.split(":",-1);
                		String sizeType = null;
                		String sizeValue = null;
                		if(arr.length==2){
                			sizeType = arr[0];
                			sizeValue = arr[1];
                		}else{
                			sizeValue = hubSkuSize;
                		}
                		HubPendingSkuCheckResult result = hubCheckService.hubSizeExist(pendingProductDto.getHubCategoryNo(), pendingProductDto.getHubBrandNo(), sizeType,sizeValue);
    					if(result.isPassing()){
                        	hubSkuPendingDto.setScreenSize(result.getSizeId()); 
                        	hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                        	hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                        	hubSkuPendingDto.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
                        }else{
                        	pass = false ;
                            log.info("pending sku校验失败，不更新："+result.getMessage()+"|原始数据："+hubSkuSize);
                            response.setCode("1");
                            PendingSkuUpdatedVo skuUpdatedVo = new PendingSkuUpdatedVo();
                            skuUpdatedVo.setSkuPendingId(hubSkuPendingDto.getSkuPendingId());
                            skuUpdatedVo.setSkuResult(result.getMessage());
                            skus.add(skuUpdatedVo);
                        }
    					if(hubSkuSize.contains(":")){
                        	hubSkuPendingDto.setHubSkuSizeType(hubSkuSize.substring(0,hubSkuSize.indexOf(":")));
                        	hubSkuPendingDto.setHubSkuSize(hubSkuSize.substring(hubSkuSize.indexOf(":")+1));  
                        }
                	}
                    hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
                }
                updatedVo.setSkus(skus); 
                response.setErrorMsg(updatedVo);
            }
            if(0==pendingProductDto.getSupplierSpuId()){
                pendingProductDto.setSupplierSpuId(null);
            }
            if(pass && null != hubSpuDto){
            	HubPendingDto hubPendingDto = new HubPendingDto();
                hubPendingDto.setHubSpuId(hubSpuDto.getSpuId());
                hubPendingDto.setHubSpuPendingId(pendingProductDto.getSpuPendingId());
                pendingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
                pendingProductDto.setSpuState(SpuState.HANDLING.getIndex());
            }else if(pass){
            	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            }
            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
        }
    	log.info("返回的校验结果：+"+JsonUtil.serialize(response)); 
    	return response;
    }
    @Override
    public HubResponse<List<PendingUpdatedVo>> batchUpdatePendingProduct(PendingProducts pendingProducts){
    	HubResponse<List<PendingUpdatedVo>> response = new HubResponse<>();
    	response.setCode("0"); //初始设置为成功
    	if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){
        	List<PendingUpdatedVo> updatedVos = new ArrayList<PendingUpdatedVo>();
        	for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
            	HubResponse<PendingUpdatedVo> everyResponse = updatePendingProduct(pendingProductDto);
                if("1".equals(everyResponse.getCode())){
                	updatedVos.add(everyResponse.getErrorMsg());
                }
            }
        	if(updatedVos.size() > 0){
        		response.setCode("1");
        		response.setErrorMsg(updatedVos); 
        	}
        }
        return response;
    }
    @Override
    public boolean updatePendingProductToUnableToProcess(String spuPendingId) throws Exception{
        try {
            if(!StringUtils.isEmpty(spuPendingId)){
                HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
                hubSpuPendingDto.setSpuPendingId(Long.parseLong(spuPendingId));
                hubSpuPendingDto.setSpuState(SpuState.UNABLE_TO_PROCESS.getIndex());
                hubSpuPendingGateWay.updateByPrimaryKeySelective(hubSpuPendingDto);
            }
            return true;
        } catch (Exception e) {
            log.error("单个产品更新无法处理时异常："+e.getMessage(),e);
            throw new Exception("单个产品更新无法处理时异常："+e.getMessage(),e);
        }

    }
    @Override
    public boolean batchUpdatePendingProductToUnableToProcess(List<String> spuPendingIds){
        try {
            if(null != spuPendingIds && spuPendingIds.size()>0){
                for(String spuPendingId : spuPendingIds){
                    updatePendingProductToUnableToProcess(spuPendingId);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("批量更新无法处理时异常："+e.getMessage(),e);
            return false;
        }

    }
    @Override
	public SupplierProductVo findSupplierProduct(Long supplierSpuId) {
    	SupplierProductVo supplierProductVo = new SupplierProductVo();
    	try {
        	HubSupplierSpuDto spuDto = hubSupplierSpuGateWay.selectByPrimaryKey(supplierSpuId);
        	if(null != spuDto){
        		JavaUtil.fatherToChild(spuDto,supplierProductVo);
            	List<HubSupplierSkuDto> supplierSku = findHubSupplierSkuBySpu(supplierSpuId);
            	if(CollectionUtils.isNotEmpty(supplierSku)){
            		supplierProductVo.setSupplierSku(supplierSku);
            	}
            	supplierProductVo.setUpdateTimeStr(null != spuDto.getUpdateTime() ? DateTimeUtil.getTime(spuDto.getUpdateTime()) : ""); 
        	}
		} catch (Exception e) {
			log.error("查询原始信息时异常："+e.getMessage(),e); 
		}
		return supplierProductVo;
	}

    /***************************************************************************************************************************
     *       以下为内部调用私有方法
    /**************************************************************************************************************************/
    /**
     * 查找supplier sku
     * @param supplierSpuId
     * @return
     */
    private List<HubSupplierSkuDto> findHubSupplierSkuBySpu(Long supplierSpuId) {
		HubSupplierSkuCriteriaDto skuCriteria = new HubSupplierSkuCriteriaDto();
		skuCriteria.setPageNo(1);
		skuCriteria.setPageSize(100); 
		skuCriteria.setOrderByClause("supplier_sku_size"); 
    	skuCriteria.setFields("supplier_sku_size");
    	skuCriteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId); 
    	return hubSupplierSkuGateWay.selectByCriteria(skuCriteria);
	}
    /**
     * 设置校验失败结果
     * @param response
     * @param spuPengdingId
     * @param errorMsg
     */
    private PendingUpdatedVo setErrorMsg(HubResponse<PendingUpdatedVo> response,Long spuPengdingId,String errorMsg){
    	response.setCode("1");
		PendingUpdatedVo updatedVo = new PendingUpdatedVo();
		updatedVo.setSpuPendingId(spuPengdingId);
		updatedVo.setSpuResult(errorMsg);
		response.setErrorMsg(updatedVo);
		return updatedVo;
    }
    /**
     * 能根据品牌货号从hub标准库中找到记录，则直接用标准库中的属性更新pending库
     * @param spuModel 标准货号
     * @param pendingProductDto 待更新的pending spu
     * @return
     */
    private HubSpuDto findAndUpdatedFromHubSpu(String spuModel,PendingProductDto pendingProductDto){
    	pendingProductDto.setSpuModel(spuModel);
		List<HubSpuDto> hubSpus = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
		if(null != hubSpus && hubSpus.size()>0){
			convertHubSpuDtoToPendingSpu(hubSpus.get(0),pendingProductDto);
            return hubSpus.get(0);
		}else{
			return null;
		}
    }
    /**
     * 验证货号
     * @param pendingProductDto
     * @return
     */
    private BrandModelResult verifyProductModle(PendingProductDto pendingProductDto) {
		BrandModelDto BrandModelDto = new BrandModelDto();
		BrandModelDto.setBrandMode(pendingProductDto.getSpuModel());
		BrandModelDto.setHubBrandNo(pendingProductDto.getHubBrandNo());
		BrandModelDto.setHubCategoryNo(pendingProductDto.getHubCategoryNo());
		BrandModelResult brandModelResult=  hubBrandModelRule.verify(BrandModelDto);
		return brandModelResult;
	}
    /**
     * 将hub_spu中的信息付给pending_spu
     * @param hubSpuDto
     * @param
     */
    private void convertHubSpuDtoToPendingSpu(HubSpuDto hubSpuDto,PendingProductDto hubPendingSpuDto){
    	hubPendingSpuDto.setHubBrandNo(hubSpuDto.getBrandNo());
		hubPendingSpuDto.setHubCategoryNo(hubSpuDto.getCategoryNo());
		hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
		hubPendingSpuDto.setHubSeason(hubSpuDto.getSeason());
		hubPendingSpuDto.setHubSpuNo(hubSpuDto.getSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuDto.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuDto.getSpuName());
    }
    /**
     * 根据品牌和货号查找hub_spu表中的记录
     * @param spuModle
     * @param hubBrandNo
     * @return
     */
    private List<HubSpuDto> selectHubSpu(String spuModle,String hubBrandNo) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(spuModle).andBrandNoEqualTo(hubBrandNo);
		return  hubSpuGateway.selectByCriteria(criteria);
	}
    /**
     * 根据供应商门户编号/供应商spu编号查询图片地址
     * @param supplierId
     * @param supplierSpuNo
     * @return
     */
    private List<String> findSpPicUrl(String supplierId,String supplierSpuNo){
    	HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("sp_pic_url");
    	criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo).andPicHandleStateEqualTo(PicState.HANDLED.getIndex());
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	if(spuPendingPics!=null&&spuPendingPics.size()>0){
    		List<String> picUrls = new ArrayList<String>();
    		for(HubSpuPendingPicDto dto : spuPendingPics){
    			picUrls.add(dto.getSpPicUrl());
    		}
    		return picUrls;
    	}
    	return null;
    }
//    /**
//     * 根据门户id和品牌编号查找品牌名称
//     * @param supplierId
//     * @param hubBrandNo
//     * @return
//     */
//    private String getHubBrandName(String supplierId,String hubBrandNo){
//        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(hubBrandNo)){
//            return "";
//        }
//        HubSupplierBrandDicCriteriaDto brandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
//        brandDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubBrandNoEqualTo(hubBrandNo);
//        List<HubSupplierBrandDicDto> brandNames = brandDicGateWay.selectByCriteria(brandDicCriteriaDto);
//        if(null != brandNames && brandNames.size()>0){
//            return brandNames.get(0).getSupplierBrand();
//        }else{
//            return "";
//        }
//    }
//    /**
//     * 根据供应商门户id和品类编号查找品类名称
//     * @param supplierId
//     * @param categoryNo
//     * @return
//     */
//    private String getHubCategoryName(String supplierId,String categoryNo){
//        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(categoryNo)){
//            return "";
//        }
//        HubSupplierCategroyDicCriteriaDto categroyDicCriteriaDto = new HubSupplierCategroyDicCriteriaDto();
//        categroyDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubCategoryNoEqualTo(categoryNo);
//        List<HubSupplierCategroyDicDto> categoryNames = categroyDicGateWay.selectByCriteria(categroyDicCriteriaDto);
//        if(null != categoryNames && categoryNames.size()>0){
//            return categoryNames.get(0).getSupplierCategory();
//        }else{
//            return "";
//        }
//    }

    /**
     * 将pendingSpu转化为pendingProduct
     * @param pendingSpu
     * @return
     */
    private PendingProductDto convertHubSpuPendingDto2PendingProductDto(HubSpuPendingDto pendingSpu) throws Exception{
        PendingProductDto pendingProduct = new PendingProductDto();
		JavaUtil.fatherToChild(pendingSpu, pendingProduct); 
        return pendingProduct;
    }

    /**
     * 将UI查询条件转换成数据库查询条件对象
     * @param pendingQuryDto UI查询条件对象
     * @return
     */
    private HubSpuPendingCriteriaDto findhubSpuPendingCriteriaFromPendingQury(PendingQuryDto pendingQuryDto){
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
//		if(StringUtils.isEmpty(pendingQuryDto.getSpuState())){
//			
//		}
		criteria.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());
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
		if(!StringUtils.isEmpty(pendingQuryDto.getBrandName())){
			criteria.andHubBrandNoLike("%"+pendingQuryDto.getBrandName()+"%");
		}
		if(!StringUtils.isEmpty(pendingQuryDto.getCategoryName())){
			criteria.andHubCategoryNoLike("%"+pendingQuryDto.getCategoryName()+"%");
		}
		if(-1 != pendingQuryDto.getPicState()){
			if(0 == pendingQuryDto.getPicState()){
				criteria.andPicStateEqualTo(PicState.UNHANDLED.getIndex());
			}else{
				criteria.andPicStateNotEqualTo(PicState.UNHANDLED.getIndex());
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
    /**
     * 将任务记录保存到数据库
     * @param createUser
     * @return
     */
    private HubSpuImportTaskDto saveTaskIntoMysql(String createUser,int taskType){
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
    private void sendMessageToTask(String taskNo,int type,String data){
    	ProductImportTask productImportTask = new ProductImportTask();
    	productImportTask.setMessageId(UUID.randomUUID().toString());
    	productImportTask.setTaskNo(taskNo);
    	productImportTask.setMessageDate(DateTimeUtil.getTime(new Date())); 
    	productImportTask.setData(data);
    	productImportTask.setType(type);
    	tastSender.productExportTaskStream(productImportTask, null);
    }
	
}
