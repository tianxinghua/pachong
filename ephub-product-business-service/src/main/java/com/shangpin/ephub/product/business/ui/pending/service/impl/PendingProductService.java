package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskImportTpye;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.BrandDom;
import com.shangpin.ephub.product.business.common.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.gms.BrandService;
import com.shangpin.ephub.product.business.common.service.gms.CategoryService;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.rest.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.sku.service.HubPendingSkuCheckService;
import com.shangpin.ephub.product.business.rest.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
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
    private static String common = ";";
    private static String symbol = "#";
    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;
    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;
    @Autowired
    private SupplierService supplierService;
	
	@Autowired
	private HubPendingSkuCheckService hubCheckRuleService;
    @Autowired
    private HubPendingSpuCheckService hubPendingSpuCheckService;
    @Autowired
    private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
    @Autowired
    private HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
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

    @Override
    public HubResponse<?> exportSku(PendingQuryDto pendingQuryDto){
    	try {
    		pendingQuryDto.setPageSize(100000); 
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
    		pendingQuryDto.setPageSize(100000); 
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
                        pendingProduct.setSpPicUrl(findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo()));
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
        log.info("待处理页面查询条件："+JsonUtil.serialize(pendingQuryDto));
        PendingProducts pendingProducts = new PendingProducts();
        List<PendingProductDto> products = new ArrayList<PendingProductDto>();
        try {
            if(null !=pendingQuryDto){
                HubSpuPendingCriteriaDto criteriaDto = findhubSpuPendingCriteriaFromPendingQury(pendingQuryDto);
                int total = hubSpuPendingGateWay.countByCriteria(criteriaDto);
                log.info("待处理页面查询返回数据个数================"+total);
                if(total>0){
                    List<HubSpuPendingDto> pendingSpus = hubSpuPendingGateWay.selectByCriteria(criteriaDto);
                    for(HubSpuPendingDto pendingSpu : pendingSpus){
                        PendingProductDto pendingProduct = convertHubSpuPendingDto2PendingProductDto(pendingSpu);
                        SupplierDTO supplierDTO = supplierService.getSupplier(pendingSpu.getSupplierNo());
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
                        FourLevelCategory category = categoryService.getGmsCateGory(pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(null != category ? category.getFourthName() : pendingProduct.getHubCategoryNo());
                        BrandDom brand = brandService.getGmsBrand(pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(null != brand ? brand.getBrandEnName() : pendingProduct.getHubBrandNo());
                        List<HubSkuPendingDto> hubSkus = findPendingSkuBySpuPendingId(pendingSpu.getSpuPendingId());
                        pendingProduct.setHubSkus(hubSkus);
                        pendingProduct.setSpPicUrl(findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo()));
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
        return pendingProducts;
    }
    @Override
    public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId) throws Exception{
        try {
            HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
            criteriaDto.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
            return hubSkuPendingGateWay.selectByCriteria(criteriaDto);
        } catch (Exception e) {
            log.error("pending表根据spu id查询sku时出错："+e.getMessage(),e);
            throw new Exception("pending表根据spu id查询sku时出错："+e.getMessage(),e);
        }

    }
    @Override
    public String updatePendingProduct(PendingProductDto pendingProductDto){
        try {
        	String returnStr = "";
            if(null != pendingProductDto){
            	BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);
            	if(brandModelResult.isPassing()){
            		if(!findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto)){
            			HubPendingSpuCheckResult spuResult = hubPendingSpuCheckService.checkHubPendingSpu(pendingProductDto);
            			if(spuResult.isPassing()){
    	                	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            			}else{
            				log.info("pending spu校验失败，不更新："+spuResult.getResult()+"|原始数据："+JsonUtil.serialize(pendingProductDto));
            				returnStr = pendingProductDto.getSpuPendingId()+symbol+spuResult.getResult();
            			}
            		}
            	}else{
            		log.info("pending spu校验失败，不更新：货号校验不通过。|原始数据："+JsonUtil.serialize(pendingProductDto));
            		returnStr = pendingProductDto.getSpuPendingId()+symbol+"货号校验不通过";
            	}
            	//TODO 校验sku
            }
            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
            return returnStr;
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            return pendingProductDto.getSpuPendingId()+symbol+"服务器错误";
        }
    }
    /**
     * 能根据品牌货号从hub标准库中找到记录，则直接用标准库中的属性更新pending库
     * @param spuModel 标准货号
     * @param pendingProductDto 待更新的pending spu
     * @return
     */
    private boolean findAndUpdatedFromHubSpu(String spuModel,PendingProductDto pendingProductDto){
    	pendingProductDto.setSpuModel(spuModel);
		List<HubSpuDto> hubSpus = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
		if(null != hubSpus && hubSpus.size()>0){
			convertHubSpuDtoToPendingSpu(hubSpus.get(0),pendingProductDto);
        	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            return true;
		}else{
			return false;
		}
    }
    
    @Override
    public String batchUpdatePendingProduct(PendingProducts pendingProducts){
    	StringBuffer result = new StringBuffer();
        if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){
            for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
                String singleResult = updatePendingProduct(pendingProductDto);
                if(!StringUtils.isEmpty(singleResult)){
                	result.append(singleResult).append(common);
                }
            }
        }
        return result.toString();
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

    /***************************************************************************************************************************/
    //以下为内部调用私有方法
    /**************************************************************************************************************************/
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
		BrandModelResult brandModelResult=  hubBrandModelRuleGateWay.verify(BrandModelDto);
		return brandModelResult;
	}
    /**
     * 将hub_spu中的信息付给pending_spu
     * @param hubSpuDto
     * @param pendingProductDto
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
    private String findSpPicUrl(String supplierId,String supplierSpuNo){
    	HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("sp_pic_url");
    	criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo).andPicHandleStateEqualTo(PicState.HANDLED.getIndex());
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	if(spuPendingPics!=null&&spuPendingPics.size()>0){
    		if(spuPendingPics.get(0)!=null){
    			return spuPendingPics.get(0).getSpPicUrl();
    		}else{
    			return "";
    		}
    	}else{
    		return "";
    	}
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
        hubSpuPendingCriteriaDto.setOrderByClause("update_time desc");
        if(!StringUtils.isEmpty(pendingQuryDto.getPageIndex()) && !StringUtils.isEmpty(pendingQuryDto.getPageSize())){
            hubSpuPendingCriteriaDto.setPageNo(pendingQuryDto.getPageIndex());
            hubSpuPendingCriteriaDto.setPageSize(pendingQuryDto.getPageSize());
        }
        Criteria criteria = hubSpuPendingCriteriaDto.createCriteria();
        criteria = criteria.andSpuStateEqualTo(SpuState.INFO_PECCABLE.getIndex());

        if(!StringUtils.isEmpty(pendingQuryDto.getSupplierNo())){
            criteria = criteria.andSupplierNoEqualTo(pendingQuryDto.getSupplierNo());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getSpuModel())){
            criteria = criteria.andSpuModelEqualTo(pendingQuryDto.getSpuModel());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubCategoryNo())){
            criteria = criteria.andHubCategoryNoLike(pendingQuryDto.getHubCategoryNo());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubBrandNo())){
            criteria = criteria.andHubBrandNoLike(pendingQuryDto.getHubBrandNo());
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason())){
            criteria = criteria.andHubSeasonLike(pendingQuryDto.getHubSeason()+"%");
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getHubYear())){
            criteria = criteria.andHubSeasonLike("%"+pendingQuryDto.getHubYear()+"%");
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getStatTime())){
            criteria = criteria.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.convertFormat(pendingQuryDto.getStatTime(), dateFormat));
        }
        if(!StringUtils.isEmpty(pendingQuryDto.getEndTime())){
            criteria = criteria.andUpdateTimeLessThan(DateTimeUtil.convertFormat(pendingQuryDto.getEndTime(),dateFormat));
        }
        return hubSpuPendingCriteriaDto;
    }
    /**
     * 跟据不符合項，筛选不符合的产品
     * @param pendingQuryDto UI查询条件
     * @param pendingProduct 待验证的产品，需要验证图片/品牌/颜色/货号等等是否不符合，如果不符合则需要返回
     * @param products 不符合项需要添加的List
     */
//    private void screenProduct(PendingQuryDto pendingQuryDto,PendingProductDto pendingProduct,List<PendingProductDto> products){
//        if(null != pendingQuryDto.getInconformities() && pendingQuryDto.getInconformities().size()>0){
//            for(Integer item : pendingQuryDto.getInconformities()){
//                if(item == ProductState.PICTURE_STATE.getIndex()){
//
//                }else if(item == ProductState.SPU_MODEL_STATE.getIndex()){
//
//                }else if(item == ProductState.CATGORY_STATE.getIndex()){
//
//                }else if(item == ProductState.SPU_BRAND_STATE.getIndex()){
//
//                }else if(item == ProductState.SPU_GENDER_STATE.getIndex()){
//
//                }else if(item == ProductState.SPU_SEASON_STATE.getIndex()){
//
//                }else if(item == ProductState.SPU_COLOR_STATE.getIndex()){
//
//                }else if(item == ProductState.MATERIAL_STATE.getIndex()){
//
//                }else if(item == ProductState.ORIGIN_STATE.getIndex()){
//
//                }else if(item == ProductState.SIZE_STATE.getIndex()){
//
//                }
//            }
//        }
//    }
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
