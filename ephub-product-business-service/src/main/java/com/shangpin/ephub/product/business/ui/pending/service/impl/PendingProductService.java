package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.TaskImportTemplate;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.service.supplier.SupplierService;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;

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
    private static String comma = ",";
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


    @Override
    public HSSFWorkbook exportSku(PendingQuryDto pendingQuryDto){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("产品信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle  style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        String[] row0 = TaskImportTemplate.getPendingSkuTemplate();
        for(int i= 0;i<row0.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(row0[i]);
            cell.setCellStyle(style);
        }
        try {
        	String[] rowTemplate = TaskImportTemplate.getPendingSkuValueTemplate();
            PendingProducts products = findPendingProducts(pendingQuryDto);
            if(null != products && null != products.getProduts() && products.getProduts().size()>0){
                int j = 0;
                for(PendingProductDto product : products.getProduts()){
                    for(HubSkuPendingDto sku : product.getHubSkus()){
                        try {
                            j++;
                            row = sheet.createRow(j);
                            row.setHeight((short) 1500);
                            insertProductSkuOfRow(row,product,sku,rowTemplate);
                        } catch (Exception e) {
                        	log.error("insertProductSkuOfRow异常："+e.getMessage(),e);
                            j--;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("待处理页导出sku异常："+e.getMessage(),e);
        }

        return wb;
    }
    @Override
    public HSSFWorkbook exportSpu(PendingQuryDto pendingQuryDto){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("产品信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle  style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
        String[] row0 = TaskImportTemplate.getPendingSpuTemplate();
        for(int i= 0;i<row0.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(row0[i]);
            cell.setCellStyle(style);
        }
        try {
        	String[] rowTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
            List<PendingProductDto> products = findPengdingSpu(pendingQuryDto);
            if(null != products && products.size()>0){
                int j = 0;
                for(PendingProductDto product : products){
                    try {
                        j++;
                        row = sheet.createRow(j);
                        insertProductSpuOfRow(row,product,rowTemplate);
                    } catch (Exception e) {
                    	 log.error("insertProductSpuOfRow异常："+e.getMessage(),e);
                        j--;
                    }
                }
            }
        } catch (Exception e) {
            log.error("待处理页导出spu异常："+e.getMessage(),e);
        }

        return wb;
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
                        pendingProduct.setHubCategoryName(getHubCategoryName(pendingProduct.getSupplierId(),pendingProduct.getHubCategoryNo()));
                        pendingProduct.setHubBrandName(getHubBrandName(pendingProduct.getSupplierId(),pendingProduct.getHubBrandNo()));
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
                        pendingProduct.setHubCategoryName(getHubCategoryName(pendingProduct.getSupplierId(),pendingProduct.getHubCategoryNo()));
                        pendingProduct.setHubBrandName(getHubBrandName(pendingProduct.getSupplierId(),pendingProduct.getHubBrandNo()));
                        List<HubSkuPendingDto> hubSkus = findPendingSkuBySpuPendingId(pendingSpu.getSpuPendingId());
                        pendingProduct.setHubSkus(hubSkus);
                        pendingProduct.setSpPicUrl(findSpPicUrl(pendingSpu.getSupplierId(),pendingSpu.getSupplierSpuNo()));
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
                HubPendingSpuCheckResult spuResult = pendingSpuCheckGateWay.checkSpu(pendingProductDto);
                if(spuResult.isPassing()){
                	pendingProductDto.setSpuModel(spuResult.getResult());
                	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
                    hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
                }else{
                    log.info("pending spu校验失败，不更新："+spuResult.getResult()+"|原始数据："+JsonUtil.serialize(pendingProductDto));
                    throw new Exception(spuResult.getResult());
                }
                List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
                if(null != pengdingSkus && pengdingSkus.size()>0){
                    for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus){
                        HubPendingSkuCheckResult result = pendingSkuCheckGateWay.checkSku(hubSkuPendingDto);
                        if(result.isPassing()){
                        	hubSkuPendingDto.setSkuState(SkuState.INFO_IMPECCABLE.getIndex());
                            hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
                        }else{
                            log.info("pending sku校验失败，不更新："+result.getResult()+"|原始数据："+JsonUtil.serialize(hubSkuPendingDto));
                            //回滚spu状态
                            pendingProductDto.setSpuState(SpuState.INFO_PECCABLE.getIndex());
                            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
                            throw new Exception(result.getResult());
                        }
                    }
                }else{//TODO 这块要去掉！！！！！
                	HubSkuPendingWithCriteriaDto criteria  = new HubSkuPendingWithCriteriaDto();
                	HubSkuPendingCriteriaDto dto = new HubSkuPendingCriteriaDto();
                	dto.createCriteria().andSpuPendingIdEqualTo(pendingProductDto.getSpuPendingId());
                	criteria.setCriteria(dto);
                	HubSkuPendingDto hubSkuPending  = new HubSkuPendingDto();
                	hubSkuPending.setSkuState(SkuState.INFO_IMPECCABLE.getIndex()); 
                	criteria.setHubSkuPending(hubSkuPending);
                	hubSkuPendingGateWay.updateByCriteriaSelective(criteria);
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
     * 根据供应商门户编号/供应商spu编号查询图片地址
     * @param supplierId
     * @param supplierSpuNo
     * @return
     */
    private String findSpPicUrl(String supplierId,String supplierSpuNo){
    	HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("sp_pic_url");
    	criteria.createCriteria().andSuupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	if(CollectionUtils.isNotEmpty(spuPendingPics)){
    		return spuPendingPics.get(0).getSpPicUrl();
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
     * 将sku信息插入Excel的一行
     * @param row
     * @param product
     * @throws Exception
     */
    public void insertProductSkuOfRow(HSSFRow row,PendingProductDto product,HubSkuPendingDto sku,String[] rowTemplate) throws Exception{
    	Class<?> spuClazz = product.getClass();
    	Class<?> skuClazz = sku.getClass();
    	Method fieldSetMet = null;
		Object value = null;
    	for(int i=0;i<rowTemplate.length;i++){
    		try {
    			String fileName = JavaUtil.parSetName(rowTemplate[i]);
    			if("supplierSkuNo".equals(rowTemplate[i]) || "skuName".equals(rowTemplate[i]) || "supplierBarcode".equals(rowTemplate[i]) || "supplyPrice".equals(rowTemplate[i])
            			|| "supplyPriceCurrency".equals(rowTemplate[i]) || "marketPrice".equals(rowTemplate[i]) || "marketPriceCurrencyorg".equals(rowTemplate[i]) || "hubSkuSize".equals(rowTemplate[i])){
    				//所有sku的属性
    				fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
            	}else if("seasonYear".equals(rowTemplate[i])){
            		setRowOfSeasonYear(row, product, spuClazz, i);
            	}else if("seasonName".equals(rowTemplate[i])){
            		setRowOfSeasonName(row, product, spuClazz, i); 
            	}else if("specification".equals(rowTemplate[i]) || "originalProductSizeType".equals(rowTemplate[i]) || "originalProductSizeValue".equals(rowTemplate[i]) ){
            		//TODO 规格类型 原尺码类型 原尺码值 从哪取值？
            		row.createCell(i).setCellValue("");
            	}else{
            		//所有spu的属性
            		fieldSetMet = spuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
            	}
			} catch (Exception e) {
				log.error("待处理页导出sku时异常："+e.getMessage(),e); 
			}        	
        }    	
    }
    /**
     * 将spu信息插入Excel的一行
     * @param row
     * @param product
     * @param rowTemplate 导入模板
     * @throws Exception
     */
    private void insertProductSpuOfRow(HSSFRow row,PendingProductDto product,String[] rowTemplate) throws Exception{		
		Class<?> cls = product.getClass();
		StringBuffer buffer = new StringBuffer();  
		Method fieldSetMet = null;
		Object value = null;
		for (int i=0;i<rowTemplate.length;i++) {
			try {
				String fileName = JavaUtil.parSetName(rowTemplate[i]);
				if("seasonYear".equals(rowTemplate[i])){
					setRowOfSeasonYear(row, product, cls, i);
				}else if("seasonName".equals(rowTemplate[i])){
					setRowOfSeasonName(row, product, cls, i); 
				}else if("memo".equals(rowTemplate[i])){
					if((null != product.getPicState() && PicState.NO_PIC.getIndex() == product.getPicState()) || (null != product.getPicState() && PicState.PIC_INFO_NOT_COMPLETED.getIndex() == product.getPicState())){
			            buffer = buffer.append("图片").append(comma);
			        }
			        if(CatgoryState.PERFECT_MATCHED.equals(product.getCatgoryState())){
			            buffer = buffer.append("品类").append(comma);
			        }
			        row.createCell(i).setCellValue(buffer.toString()); 
				}else{
					fieldSetMet = cls.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				}				
			} catch (Exception e) {
				log.error("待处理页导出spu时异常："+e.getMessage(),e); 
				continue;
			}
		}
    }	
    /**
     * 设置导出上市季节的值，这个字段比较特殊，是从hubSeason字段拆解出来的
     * @param row
     * @param product
     * @param clazz
     * @param i
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void setRowOfSeasonName(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i).setCellValue((null != value && value.toString().contains("_")) ? value.toString().split("_")[1] : (null != value ? value.toString() : ""));
	}
    /**
     * 设置导出上市年份的值，这个字段比较特殊，是从hubSeason字段拆解出来的
     * @param row
     * @param product
     * @param clazz
     * @param i
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	private void setRowOfSeasonYear(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i).setCellValue((null != value && value.toString().contains("_")) ? value.toString().split("_")[0] : "");
	}

}
