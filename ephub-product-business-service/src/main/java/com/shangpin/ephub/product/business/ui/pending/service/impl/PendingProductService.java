package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
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
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.ProductImportTaskStreamSender;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
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
    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;
    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;
    @Autowired
    private HubSupplierCategroyDicGateWay categroyDicGateWay;
    @Autowired
    private HubSupplierBrandDicGateWay brandDicGateWay;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
    @Autowired
    private HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
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

    @Override
    public HubResponse<?> exportSku(PendingQuryDto pendingQuryDto){
    	try {
    		PendingProducts products = findPendingProducts(pendingQuryDto);
    		products.setCreateUser(pendingQuryDto.getCreateUser());
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser());
        	sendMessageToTask(taskDto.getTaskNo(),TaskImportTpye.EXPORT_PENDING_SKU.getIndex(),JsonUtil.serialize(products)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":"+"pending_product_" + taskDto.getTaskNo()+".xls");
		} catch (Exception e) {
			log.error("导出sku失败，服务器发生错误:"+e.getMessage(),e);
			return HubResponse.errorResp("导出失败，服务器发生错误");
		}
    }
    @Override
    public HubResponse<?> exportSpu(PendingQuryDto pendingQuryDto){
    	try {
    		PendingProducts products = new PendingProducts();
    		products.setCreateUser(pendingQuryDto.getCreateUser());
        	List<PendingProductDto> productList = findPengdingSpu(pendingQuryDto);
        	products.setProduts(productList); 
        	HubSpuImportTaskDto taskDto = saveTaskIntoMysql(pendingQuryDto.getCreateUser());
        	sendMessageToTask(taskDto.getTaskNo(),TaskImportTpye.EXPORT_PENDING_SPU.getIndex(),JsonUtil.serialize(products)); 
        	return HubResponse.successResp(taskDto.getTaskNo()+":"+"pending_product_" + taskDto.getTaskNo()+".xls");
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
                        pendingProduct.setSupplierName(null != supplierDTO ? supplierDTO.getSupplierName() : "");
                        String categoryName = getHubCategoryName(pendingProduct.getSupplierId(),pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(!StringUtils.isEmpty(categoryName) ? categoryName : pendingProduct.getHubCategoryNo());
                        String brandName = getHubBrandName(pendingProduct.getSupplierId(),pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(!StringUtils.isEmpty(brandName) ? brandName : pendingProduct.getHubBrandNo());
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
                        String categoryName = getHubCategoryName(pendingProduct.getSupplierId(),pendingProduct.getHubCategoryNo());
                        pendingProduct.setHubCategoryName(!StringUtils.isEmpty(categoryName) ? categoryName : pendingProduct.getHubCategoryNo());
                        String brandName = getHubBrandName(pendingProduct.getSupplierId(),pendingProduct.getHubBrandNo());
                        pendingProduct.setHubBrandName(!StringUtils.isEmpty(brandName) ? brandName : pendingProduct.getHubBrandNo());
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
            criteriaDto.setFields("sku_pending_id,hub_sku_size,sp_sku_size_state");
            return hubSkuPendingGateWay.selectByCriteria(criteriaDto);
        } catch (Exception e) {
            log.error("pending表根据spu id查询sku时出错："+e.getMessage(),e);
            throw new Exception("pending表根据spu id查询sku时出错："+e.getMessage(),e);
        }

    }
    @Override
    public boolean updatePendingProduct(PendingProductDto pendingProductDto) throws Exception{
        try {
            if(null != pendingProductDto){
    			BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);
    			if(brandModelResult.isPassing()){
    				pendingProductDto.setSpuModel(brandModelResult.getBrandMode());
    				List<HubSpuDto> hubSpus = selectHubSpu(pendingProductDto);
    				if(null != hubSpus && hubSpus.size()>0){
    					convertHubSpuDtoToPendingSpu(hubSpus.get(0),pendingProductDto);
                    	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
                        hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
    				}else{
    					HubPendingSpuCheckResult spuResult = pendingSpuCheckGateWay.checkSpu(pendingProductDto);
    	                if(spuResult.isPassing()){
    	                	pendingProductDto.setSpuModel(brandModelResult.getBrandMode());
    	                	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
    	                    hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
    	                }else{
    	                    log.info("pending spu校验失败，不更新："+spuResult.getResult()+"|原始数据："+JsonUtil.serialize(pendingProductDto));
    	                    throw new Exception(spuResult.getResult());
    	                }
    				}
        		}else{
        		     log.info("pending spu校验失败，不更新：货号校验不通过。|原始数据："+JsonUtil.serialize(pendingProductDto));
                     throw new Exception("货号校验不通过");
        		}
                List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
                log.info("pengdingSkus:{}",pengdingSkus);
                if(null != pengdingSkus && pengdingSkus.size()>0){
                    for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){
                        HubPendingSkuCheckResult result = pendingSkuCheckGateWay.checkSku(hubSkuPendingDto);
                        log.info("HubPendingSkuCheckResult:{}",result);
                        if(result.isPassing()){
                        	hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                        	hubSkuPendingDto.setSpSkuSizeState(SkuState.INFO_IMPECCABLE.getIndex());
                            hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
                        }else{
                            log.info("pending sku校验失败，不更新："+result.getResult()+"|原始数据："+JsonUtil.serialize(hubSkuPendingDto));
                            //回滚spu状态
                            pendingProductDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
                            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
                            throw new Exception(result.getResult());
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
    @Override
    public boolean batchUpdatePendingProduct(PendingProducts pendingProducts){
        try {
            if(null != pendingProducts && null != pendingProducts.getProduts() && pendingProducts.getProduts().size()>0){
                for(PendingProductDto pendingProductDto : pendingProducts.getProduts()){
                    updatePendingProduct(pendingProductDto);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("待更新页面批量提交异常："+e.getMessage());
            return false;
        }

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
     * @param hubPendingSpuDto
     * @return
     */
    private List<HubSpuDto> selectHubSpu(HubSpuPendingDto hubPendingSpuDto) {
		HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
		criteria.createCriteria().andSpuModelEqualTo(hubPendingSpuDto.getSpuModel()).andBrandNoEqualTo(hubPendingSpuDto.getHubBrandNo());
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
    	criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo).andPicHandleStateEqualTo(PicState.PIC_INFO_COMPLETED.getIndex());
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
    /**
     * 根据门户id和品牌编号查找品牌名称
     * @param supplierId
     * @param hubBrandNo
     * @return
     */
    private String getHubBrandName(String supplierId,String hubBrandNo){
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(hubBrandNo)){
            return "";
        }
        HubSupplierBrandDicCriteriaDto brandDicCriteriaDto = new HubSupplierBrandDicCriteriaDto();
        brandDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubBrandNoEqualTo(hubBrandNo);
        List<HubSupplierBrandDicDto> brandNames = brandDicGateWay.selectByCriteria(brandDicCriteriaDto);
        if(null != brandNames && brandNames.size()>0){
            return brandNames.get(0).getSupplierBrand();
        }else{
            return "";
        }
    }
    /**
     * 根据供应商门户id和品类编号查找品类名称
     * @param supplierId
     * @param categoryNo
     * @return
     */
    private String getHubCategoryName(String supplierId,String categoryNo){
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(categoryNo)){
            return "";
        }
        HubSupplierCategroyDicCriteriaDto categroyDicCriteriaDto = new HubSupplierCategroyDicCriteriaDto();
        categroyDicCriteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andHubCategoryNoEqualTo(categoryNo);
        List<HubSupplierCategroyDicDto> categoryNames = categroyDicGateWay.selectByCriteria(categroyDicCriteriaDto);
        if(null != categoryNames && categoryNames.size()>0){
            return categoryNames.get(0).getSupplierCategory();
        }else{
            return "";
        }
    }

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
        if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && !StringUtils.isEmpty(pendingQuryDto.getHubYear())){
            criteria = criteria.andHubSeasonEqualTo(pendingQuryDto.getHubYear()+"_"+pendingQuryDto.getHubSeason());
        }else if(!StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && StringUtils.isEmpty(pendingQuryDto.getHubYear())){
            criteria = criteria.andHubSeasonLike(pendingQuryDto.getHubSeason());
        }else if(StringUtils.isEmpty(pendingQuryDto.getHubSeason()) && !StringUtils.isEmpty(pendingQuryDto.getHubYear())){
            criteria = criteria.andHubSeasonLike(pendingQuryDto.getHubYear());
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
    private void screenProduct(PendingQuryDto pendingQuryDto,PendingProductDto pendingProduct,List<PendingProductDto> products){
        if(null != pendingQuryDto.getInconformities() && pendingQuryDto.getInconformities().size()>0){
            for(Integer item : pendingQuryDto.getInconformities()){
                if(item == ProductState.PICTURE_STATE.getIndex()){

                }else if(item == ProductState.SPU_MODEL_STATE.getIndex()){

                }else if(item == ProductState.CATGORY_STATE.getIndex()){

                }else if(item == ProductState.SPU_BRAND_STATE.getIndex()){

                }else if(item == ProductState.SPU_GENDER_STATE.getIndex()){

                }else if(item == ProductState.SPU_SEASON_STATE.getIndex()){

                }else if(item == ProductState.SPU_COLOR_STATE.getIndex()){

                }else if(item == ProductState.MATERIAL_STATE.getIndex()){

                }else if(item == ProductState.ORIGIN_STATE.getIndex()){

                }else if(item == ProductState.SIZE_STATE.getIndex()){

                }
            }
        }
    }
    /**
     * 将任务记录保存到数据库
     * @param createUser
     * @return
     */
    private HubSpuImportTaskDto saveTaskIntoMysql(String createUser){
    	HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
    	Date date = new Date();
		hubSpuTask.setTaskNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date));
		hubSpuTask.setTaskState((byte)TaskState.HANDLEING.getIndex());
		hubSpuTask.setCreateTime(date);
		hubSpuTask.setCreateUser(createUser); 
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
