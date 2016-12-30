package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
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
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
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
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.enumeration.ProductState;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
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
            PendingProducts products = findPendingProducts(pendingQuryDto);
            if(null != products && products.getProduts().size()>0){
                int j = 0;
                for(PendingProductDto product : products.getProduts()){
                    for(HubSkuPendingDto sku : product.getHubSkus()){
                        try {
                            j++;
                            row = sheet.createRow(j);
                            row.setHeight((short) 1500);
                            insertProductSkuOfRow(row,product,sku);
                        } catch (Exception e) {
                            e.printStackTrace();
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
            List<PendingProductDto> products = findPengdingSpu(pendingQuryDto);
            if(null != products && products.size()>0){
                int j = 0;
                for(PendingProductDto product : products){
                    try {
                        j++;
                        row = sheet.createRow(j);
                        insertProductSpuOfRow(row,product);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                	pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
                    hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);
                }else{
                    log.info("pending spu校验失败，不更新："+spuResult.getResult()+"|原始数据："+JsonUtil.serialize(pendingProductDto));
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
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage(),e);
            throw new Exception("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage(),e);
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
            log.error("待更新页面批量提交异常："+e.getMessage(),e);
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
        pendingProduct.setSpuPendingId(pendingSpu.getSpuPendingId());
        pendingProduct.setSupplierId(pendingSpu.getSupplierId());
        pendingProduct.setSupplierSpuNo(pendingSpu.getSupplierSpuNo());
        pendingProduct.setSpuModel(pendingSpu.getSpuModel());
        pendingProduct.setSpuName(pendingSpu.getSpuName());
        pendingProduct.setHubGender(pendingSpu.getHubGender());
        pendingProduct.setHubCategoryNo(pendingSpu.getHubCategoryNo());
        pendingProduct.setHubBrandNo(pendingSpu.getHubBrandNo());
        pendingProduct.setHubSeason(pendingSpu.getHubSeason());
        pendingProduct.setSpSkuSizeState(pendingSpu.getSpSkuSizeState());
        pendingProduct.setSpuState(pendingSpu.getSpuState());
        pendingProduct.setPicState(pendingSpu.getPicState());
        pendingProduct.setIsCurrentSeason(pendingSpu.getIsCurrentSeason());
        pendingProduct.setIsNewData(pendingSpu.getIsNewData());
        pendingProduct.setHubMaterial(pendingSpu.getHubMaterial());
        pendingProduct.setHubOrigin(pendingSpu.getHubOrigin());
        pendingProduct.setCreateTime(pendingSpu.getCreateTime());
        pendingProduct.setUpdateTime(pendingSpu.getUpdateTime());
        pendingProduct.setSpuDesc(pendingSpu.getSpuDesc());
        pendingProduct.setHubSpuNo(pendingSpu.getHubSpuNo());
        pendingProduct.setSpuModelState(pendingSpu.getSpuModelState());
        pendingProduct.setCatgoryState(pendingSpu.getCatgoryState());
        pendingProduct.setSupplierSpuId(pendingSpu.getSupplierSpuId());
        pendingProduct.setMemo(pendingSpu.getMemo());
        pendingProduct.setDataState(pendingSpu.getDataState());
        pendingProduct.setVersion(pendingSpu.getVersion());
        pendingProduct.setSpuBrandState(pendingSpu.getSpuBrandState());
        pendingProduct.setSpuGenderState(pendingSpu.getSpuGenderState());
        pendingProduct.setSpuSeasonState(pendingSpu.getSpuSeasonState());
        pendingProduct.setHubColorNo(pendingSpu.getHubColorNo());
        pendingProduct.setHubColor(pendingSpu.getHubColor());
        pendingProduct.setSpuColorState(pendingSpu.getSpuColorState());
//		JavaUtil.fatherToChild(pendingSpu, pendingProduct); 
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
     * 将spu信息插入Excel的一行
     * @param row
     * @param product
     * @throws Exception
     */
    private void insertProductSpuOfRow(HSSFRow row,PendingProductDto product) throws Exception{
        row.createCell(0).setCellValue(product.getSupplierId());
        row.createCell(1).setCellValue(product.getSupplierName());
        row.createCell(2).setCellValue(product.getSupplierSpuNo());
        row.createCell(3).setCellValue(product.getHubCategoryName());
        row.createCell(4).setCellValue(product.getHubCategoryNo());
        row.createCell(5).setCellValue(product.getHubBrandNo());
        row.createCell(6).setCellValue(product.getHubBrandName());
        row.createCell(7).setCellValue(product.getSpuModel());
        if(!StringUtils.isEmpty(product.getHubSeason())){
            if(product.getHubSeason().contains("-")){
                row.createCell(8).setCellValue(product.getHubSeason().substring(0, product.getHubSeason().indexOf("-")));
                row.createCell(9).setCellValue(product.getHubSeason().substring(product.getHubSeason().indexOf("-")+1));
            }else{
                row.createCell(9).setCellValue(product.getHubSeason());
            }
        }
        row.createCell(10).setCellValue(product.getHubGender());
        row.createCell(11).setCellValue(product.getSpuName());
        row.createCell(12).setCellValue(product.getHubColor());
        row.createCell(13).setCellValue(product.getHubMaterial());
        row.createCell(14).setCellValue(product.getHubOrigin());
        row.createCell(15).setCellValue(product.getSpuDesc());
        StringBuffer buffer = new StringBuffer();
        String comma = ",";
        if(PicState.NO_PIC.getIndex() == product.getPicState() || PicState.PIC_INFO_NOT_COMPLETED.getIndex() == product.getPicState()){
            buffer = buffer.append("图片").append(comma);
        }
        if(CatgoryState.PERFECT_MATCHED.equals(product.getCatgoryState())){
            buffer = buffer.append("品类").append(comma);
        }
        row.createCell(16).setCellValue(buffer.toString());
    }
    /**
     * 将sku信息插入Excel的一行
     * @param row
     * @param product
     * @throws Exception
     */
    public void insertProductSkuOfRow(HSSFRow row,PendingProductDto product,HubSkuPendingDto sku) throws Exception{
        insertProductSpuOfRow(row,product);
        row.createCell(11).setCellValue(sku.getSupplierSkuNo());
        row.createCell(12).setCellValue(product.getSupplierSpuNo());
        row.createCell(13).setCellValue(sku.getSupplierBarcode());
        row.createCell(14).setCellValue(product.getHubColor());
        row.createCell(15).setCellValue("规格类型");
        row.createCell(16).setCellValue("原尺码类型");
        row.createCell(17).setCellValue(sku.getHubSkuSize());
        row.createCell(18).setCellValue(product.getHubMaterial());
        row.createCell(19).setCellValue(product.getHubOrigin());
        row.createCell(20).setCellValue(sku.getSupplyPrice().toString());
        row.createCell(21).setCellValue(sku.getSupplyPriceCurrency());
        row.createCell(22).setCellValue(sku.getMarketPrice().toString());
        row.createCell(23).setCellValue(sku.getMarketPriceCurrencyorg());
        row.createCell(24).setCellValue("尺寸");
        row.createCell(25).setCellValue(product.getSpuDesc());
    }

}
