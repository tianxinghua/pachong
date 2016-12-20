package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubSupplierBrandDicGateWay;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.gateway.HubGenderDicGateWay;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicItemDto;
import com.shangpin.ephub.client.data.mysql.material.gateway.HubMaterialDicGateWay;
import com.shangpin.ephub.client.data.mysql.material.gateway.HubMaterialDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleCriteriaDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.rule.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.gateway.HubSeasonDicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.DataBusinessStatus;
import com.shangpin.pending.product.consumer.common.enumeration.DataStatus;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.conf.clients.mysql.spu.bean.HubSpuPending;
import com.shangpin.pending.product.consumer.supplier.dto.ColorDTO;
import com.shangpin.pending.product.consumer.supplier.dto.MaterialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by loyalty on 16/12/16.
 */
@Service
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class DataServiceHandler {

    @Autowired
    private HubBrandDicGateway brandDicGateway;

    @Autowired
    private HubSupplierBrandDicGateWay supplierBrandDicGateWay;

    @Autowired
    private HubSpuGateWay hubSpuGateWay;

    @Autowired
    private HubGenderDicGateWay hubGenderDicGateWay;

    @Autowired
    private HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;

    @Autowired
    private HubColorDicItemGateWay hubColorDicItemGateWay;

    @Autowired
    private HubColorDicGateWay hubColorDicGateWay;

    @Autowired
    private HubSeasonDicGateWay hubSeasonDicGateWay;

    @Autowired
    private HubMaterialDicGateWay hubMaterialDicGateWay;

    @Autowired
    private HubMaterialDicItemGateWay hubMaterialDicItemGateWay;

    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;

    @Autowired
    private HubBrandModelRuleGateWay hubBrandModelRuleGateWay;


    public void saveBrand(String supplierId,String supplierBrandName) throws Exception{
//        HubBrandDicDto brandDicDto = new HubBrandDicDto();
//        brandDicDto.setCreateTime(new Date());
//        brandDicDto.setSupplierBrand(supplierBrandName);
//        brandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
//        brandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
//        int insert = brandDicGateway.insert(brandDicDto);

        HubSupplierBrandDicDto supplierBrandDicDto = new HubSupplierBrandDicDto();
        supplierBrandDicDto.setSupplierId(supplierId);
        supplierBrandDicDto.setSupplierBrand(supplierBrandName);
        supplierBrandDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        supplierBrandDicDto.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
        try {
            supplierBrandDicGateWay.insert(supplierBrandDicDto);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }

    }

    public List<HubBrandDicDto> getBrand() throws Exception{
        HubBrandDicCriteriaDto criteria = new HubBrandDicCriteriaDto();
        HubBrandDicCriteriaDto.Criteria criterion =criteria.createCriteria();

        return brandDicGateway.selectByCriteria(criteria);

    }

    public HubSpuDto getHubSpuByBrandNoAndProductModel(String brandNo,String spuModel){
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        HubSpuCriteriaDto.Criteria criterion = criteria.createCriteria();

        criterion.andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel);

        List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(criteria);
        if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
            return hubSpuDtos.get(0);
        }else{
            return null;
        }
    }

    public HubSpuDto getHubSpuByProductModel(String spuModel){
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        HubSpuCriteriaDto.Criteria criterion = criteria.createCriteria();

        criterion.andSpuModelEqualTo(spuModel);

        List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(criteria);
        if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
            return hubSpuDtos.get(0);
        }else{
            return null;
        }
    }

    public List<HubGenderDicDto> getHubGenderDicBySupplierId(String supplierId){
        HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andPushStateEqualTo(DataBusinessStatus.PUSH.getIndex().byteValue());
        return hubGenderDicGateWay.selectByCriteria(criteria);
    }

    public void saveHubGender(String  supplierId,String supplierGender) throws Exception{
        HubGenderDicDto hubGenderDicDto = new HubGenderDicDto();
        hubGenderDicDto.setCreateTime(new Date());
        hubGenderDicDto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        hubGenderDicDto.setSupplierId(supplierId);
        hubGenderDicDto.setSupplierGender(supplierGender);
        hubGenderDicDto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
        try {
            hubGenderDicGateWay.insert(hubGenderDicDto);
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof DuplicateKeyException){

            }else{
                throw e;
            }

        }
    }

    public HubGenderDicDto  getHubGenderDicBySupplierIdAndSupplierGender(String supplierId,String supplierGender){
        HubGenderDicCriteriaDto criteria = new HubGenderDicCriteriaDto();
        HubGenderDicCriteriaDto.Criteria criterion = criteria.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId)
                .andSupplierGenderEqualTo(supplierGender);
        List<HubGenderDicDto> hubGenderDicDtos = hubGenderDicGateWay.selectByCriteria(criteria);
        if(null!=hubGenderDicDtos&&hubGenderDicDtos.size()>0){
            return hubGenderDicDtos.get(0);
        }else{
            return null;
        }
    }

    public List<HubSupplierCategroyDicDto> getSupplierCategoryBySupplierId(String supplierId){
        HubSupplierCategroyDicCriteriaDto criteria = new HubSupplierCategroyDicCriteriaDto();
        HubSupplierCategroyDicCriteriaDto.Criteria criterion = criteria.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId);
        return hubSupplierCategroyDicGateWay.selectByCriteria(criteria);
    }

    public void saveHubCategory(String supplierId,String supplierCategory,String  supplierGender) throws Exception{
        HubGenderDicDto hubGenderDicDto = this.getHubGenderDicBySupplierIdAndSupplierGender(supplierId, supplierGender);

        HubSupplierCategroyDicDto dto = new HubSupplierCategroyDicDto();
        dto.setSupplierId(supplierId);
        dto.setSupplierCategory(supplierCategory);
        dto.setMappingState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        dto.setGenderDicId(null==hubGenderDicDto?null:hubGenderDicDto.getGenderDicId());
        try {
            hubSupplierCategroyDicGateWay.insert(dto);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }

    }

    /**
     * 组装颜色数据
     * @return
     */
    public List<ColorDTO> getColorDTO(){

        HubColorDicItemCriteriaDto criteria = new HubColorDicItemCriteriaDto();

        List<HubColorDicItemDto> hubColorDicItemDtos = hubColorDicItemGateWay.selectByCriteria(criteria);

        HubColorDicCriteriaDto colorDicCriteriaDto = new HubColorDicCriteriaDto();
        List<HubColorDicDto> dicDtos = hubColorDicGateWay.selectByCriteria(colorDicCriteriaDto);
        Map<Long,HubColorDicDto> colorDicMap = new HashMap<>();
        for(HubColorDicDto colorDicDto:dicDtos){
            colorDicMap.put(colorDicDto.getColorDicId(),colorDicDto);
        }

        List<ColorDTO>  colorDTOS = new ArrayList<>();

        for(HubColorDicItemDto itemDto:hubColorDicItemDtos){
            ColorDTO colorDTO = new ColorDTO();
            colorDTO.setColorItemId(itemDto.getColorDicItemId());
            colorDTO.setSupplierColor(itemDto.getColorItemName());
            if (colorDicMap.containsKey(itemDto.getColorDicId())) {
               colorDTO.setColorDicId(itemDto.getColorDicId());
               colorDTO.setHubColorNo(colorDicMap.get(itemDto.getColorDicId()).getColorNo());
               colorDTO.setHubColorName(colorDicMap.get(itemDto.getColorDicId()).getColorName());
            }
            colorDTOS.add(colorDTO);
        }
        return colorDTOS;

    }


    public void saveColorItem(String supplierColor)  throws  Exception{
        HubColorDicItemDto dto = new HubColorDicItemDto();
        dto.setCreateTime(new Date());
        dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        dto.setColorItemName(supplierColor);
        try {
            hubColorDicItemGateWay.insert(dto);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }
    }

    public List<HubSeasonDicDto> getHubSeasonDic(){
        HubSeasonDicCriteriaDto criteria = new HubSeasonDicCriteriaDto();
        HubSeasonDicCriteriaDto.Criteria criterion= criteria.createCriteria();
        return  hubSeasonDicGateWay.selectByCriteria(criteria);

    }

    public void saveSeason(String supplierId,String supplierSeason){
        HubSeasonDicDto dto = new HubSeasonDicDto();
        dto.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        dto.setCreateTime(new Date());
        dto.setPushState(DataBusinessStatus.NO_PUSH.getIndex().byteValue());
        dto.setSupplierid(supplierId);
        dto.setSupplierSeason(supplierSeason);
        try {
            hubSeasonDicGateWay.insert(dto);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }
    }


    public List<MaterialDTO> getMaterialDTO(){

        HubMaterialDicItemCriteriaDto criteria = new HubMaterialDicItemCriteriaDto();
        HubMaterialDicItemCriteriaDto.Criteria criterio =criteria.createCriteria();
        criteria.setOrderByClause("memo");// 完全的最小  单个单词最大
        List<HubMaterialDicItemDto> hubMaterialDicItemDtos = hubMaterialDicItemGateWay.selectByCriteria(criteria);

        HubMaterialDicCriteriaDto dicCriteriaDto = new HubMaterialDicCriteriaDto();
        List<HubMaterialDicDto> dicDtos = hubMaterialDicGateWay.selectByCriteria(dicCriteriaDto);
        Map<Long,HubMaterialDicDto> materialDicMap = new HashMap<>();
        for(HubMaterialDicDto dto:dicDtos){
            materialDicMap.put(dto.getMaterialDicId(),dto);
        }

        List<MaterialDTO>  materialDTOS = new ArrayList<>();

        for(HubMaterialDicItemDto itemDto:hubMaterialDicItemDtos){
            MaterialDTO materialDTO = new MaterialDTO();
            materialDTO.setMaterialItemId(itemDto.getMaterialDicItemId());
            materialDTO.setSupplierMaterial(itemDto.getMaterialItemName());
            if (materialDicMap.containsKey(itemDto.getMaterialDicId())) {
                materialDTO.setMaterialDicId(itemDto.getMaterialDicId());
                materialDTO.setHubMaterial(materialDicMap.get(itemDto.getMaterialDicId()).getMaterialName());
            }

            materialDTOS.add(materialDTO);
        }
        return materialDTOS;

    }


    public void savePendingSpu(HubSpuPendingDto spuPending) throws  Exception{
        try {
            hubSpuPendingGateWay.insert(spuPending);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }
    }


    public HubSpuPendingDto getSpuPendingDTO(String supplierId,String supplierSpuNo) {
        HubSpuPendingCriteriaDto criterial = new HubSpuPendingCriteriaDto();
        HubSpuPendingCriteriaDto.Criteria criterion= criterial.createCriteria();
        criterion.andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
        List<HubSpuPendingDto> hubSpuPendingDtos = hubSpuPendingGateWay.selectByCriteria(criterial);
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            return hubSpuPendingDtos.get(0);
        }else{

          return null;
        }
    }

    public void savePendingSku(HubSkuPendingDto skuPendingDto) throws Exception{
        try {
            hubSkuPendingGateWay.insert(skuPendingDto);
        } catch (Exception e) {
            if(e instanceof DuplicateKeyException){

            }else{
                e.printStackTrace();
                throw e;
            }
        }
    }

    public List<HubBrandModelRuleDto> getBrandModle(String hubBrandNo){

        HubBrandModelRuleCriteriaDto criterial = new HubBrandModelRuleCriteriaDto();
        criterial.createCriteria().andHubBrandNoEqualTo(hubBrandNo).andRuleStateEqualTo(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        return  hubBrandModelRuleGateWay.selectByCriteria(criterial);

    }

}
