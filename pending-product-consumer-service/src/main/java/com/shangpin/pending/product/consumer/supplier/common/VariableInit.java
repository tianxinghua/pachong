package com.shangpin.pending.product.consumer.supplier.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.DataStatus;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.common.enumeration.SeasonType;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import com.shangpin.pending.product.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.pending.product.consumer.supplier.dto.*;
import com.shangpin.pending.product.consumer.util.BurberryModelRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lizhongren on 2017/2/16.
 */
@Component
@Slf4j
public class VariableInit {
    @Autowired
    DataServiceHandler dataServiceHandler;

    @Autowired
    DataSverviceUtil dataSverviceUtil;

    @Autowired
    ValidationRuleUtil validationRuleUtil;

    @Autowired
    BurberryModelRule burberryModelRule;

    @Autowired
    HubBrandModelRuleGateWay brandModelRuleGateWay;



    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ApiAddressProperties apiAddressProperties;

    @Autowired
    SpuPendingHandler spuPendingHandler;

    @Autowired
    PendingCommonHandler pendingCommonHandler;

    @Autowired
    ObjectConvertCommon objectConvertCommon;

    static Map<String, String> genderStaticMap = null;

    static Map<String, Map<String, String>> supplierCategoryMappingStaticMap = null;

    static Map<String, String> brandStaticMap = null;

    static Map<String, String> colorStaticMap = null;

    static Map<String, String> seasonStaticMap = null;

    static Map<String, String> materialStaticMap = null;

    static Map<String, String> originStaticMap = null;

    /**
     * 品牌货号映射表 key : hub的品牌编号 value：Map<String,String> key:正则表单规则 value ： 品类值
     */
    static Map<String, Map<String, String>> brandModelStaticMap = null;//

    static Map<String, String> hubGenderStaticMap = null;

    static Map<String, String> hubBrandStaticMap = null;

    static Map<String, String> hubColorStaticMap = null;

    static Map<String, String> hubSeasonStaticMap = null;

    /**
     * 存放 supplierId_supplierBrand，filterFlag 对应关系
     */
    static Map<String, Byte> hubSupplierBrandFlag = null;
    /**
     * 存放 supplierId_supplierSeason，filterFlag 对应关系
     */
    static Map<String, Byte> hubSeasonFlag = null;

    static Integer isCurrentMin  =   DateUtils.getCurrentMin();

    ObjectMapper mapper =new ObjectMapper();

    /**
     * key :supplierId+"_"+supplierSeason value:hub_year+"_"+hub_season+"_"+memo
     * meme = 1: current season 0: preview season
     *
     * @param supplierId
     * @return
     */
    protected Map<String, String> getSeasonMap(String supplierId) {
        if (null == seasonStaticMap) {
            seasonStaticMap = new HashMap<>();
            hubSeasonStaticMap = new HashMap<>();
            setSeasonStaticMap();

        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                setSeasonStaticMap();
            }

        }
        return seasonStaticMap;
    }

    private void setSeasonStaticMap() {
        List<HubSeasonDicDto> hubSeasonDics = dataServiceHandler.getHubSeasonDic();
        for (HubSeasonDicDto dicDto : hubSeasonDics) {
            if (StringUtils.isNotBlank(dicDto.getHubMarketTime()) && StringUtils.isNotBlank(dicDto.getHubSeason())
                    && StringUtils.isNotBlank(dicDto.getMemo())) {

                seasonStaticMap.put(dicDto.getSupplierid() + "_" + dicDto.getSupplierSeason().trim(),
                        dicDto.getHubMarketTime().trim() + "_" + dicDto.getHubSeason().trim() + "|"
                                + dicDto.getMemo().trim());
                hubSeasonStaticMap.put(dicDto.getHubMarketTime() + "_" + dicDto.getHubSeason(), "");
            }
        }
    }

    protected Map<String, String> getColorMap() {
        if (null == colorStaticMap) {
            colorStaticMap = new HashMap<>();
            hubColorStaticMap = new HashMap<>();
            List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
            for (ColorDTO dto : colorDTOS) {
                colorStaticMap.put(dto.getSupplierColor(), dto.getHubColorName());
                hubColorStaticMap.put(dto.getHubColorName(), "");
            }

        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                for (ColorDTO dto : dataServiceHandler.getColorDTO()) {
                    colorStaticMap.put(dto.getSupplierColor(), dto.getHubColorName());
                    hubColorStaticMap.put(dto.getHubColorName(), "");
                }

                // 无用的内容 暂时不考虑

            }
        }
        return colorStaticMap;
    }


    /**
     * 材质获取 做替换
     *
     *
     * @return
     */
    protected Map<String, String> getMaterialMap() {
        if (null == materialStaticMap) {
            materialStaticMap = new LinkedHashMap<>();
            List<MaterialDTO> materialDTOS = dataServiceHandler.getMaterialMapping();
            for (MaterialDTO dto : materialDTOS) {

                materialStaticMap.put(dto.getSupplierMaterial(), dto.getHubMaterial());
            }
        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                List<MaterialDTO> materialDTOS = dataServiceHandler.getMaterialMapping();
                for (MaterialDTO dto : materialDTOS) {
                    materialStaticMap.put(dto.getSupplierMaterial(), dto.getHubMaterial());
                }
            }
        }
        return materialStaticMap;
    }

    /**
     * key 供货商品牌名称 value 尚品的品牌编号
     *
     * @return
     */
    protected Map<String, String> getBrandMap() throws Exception {

        if (null == brandStaticMap) {
            brandStaticMap = new HashMap<>();
            hubBrandStaticMap = new HashMap<>();
            List<HubBrandDicDto> brandDicDtos = dataServiceHandler.getBrand();
            for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
                brandStaticMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
                        hubBrandDicDto.getHubBrandNo());
                hubBrandStaticMap.put(hubBrandDicDto.getHubBrandNo().trim(), "");
            }
            ;

        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                List<HubBrandDicDto> brandDicDtos = dataServiceHandler.getBrand();
                for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
                    brandStaticMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
                            hubBrandDicDto.getHubBrandNo());
                    hubBrandStaticMap.put(hubBrandDicDto.getHubBrandNo().trim(), "");
                }
                ;
                // 无用的内容 暂时不考虑

            }
        }
        return brandStaticMap;
    }

    /**
     * 初始化hubSupplierBrandFlag<br>
     * 获取有效的供应商品牌，key是supplierId_supplierBrand，value是filterFlag
     *
     * @return
     */
    protected Map<String, Byte> getHubSupplierBrandFlag() {
        if (null == hubSupplierBrandFlag) {
            hubSupplierBrandFlag = new ConcurrentHashMap<>();
            setHubSupplierBrandFlag();
        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                setHubSupplierBrandFlag();
            }
        }
        return hubSupplierBrandFlag;
    }

    /**
     * 设置 hubSupplierBrandFlag的值，key是supplierId_supplierBrand，value是filterFlag
     */
    private void setHubSupplierBrandFlag() {
        List<HubSupplierBrandDicDto> brandsDic = dataServiceHandler.getEffectiveHubSupplierBrands();
        if (null != brandsDic && brandsDic.size() > 0) {
            for (HubSupplierBrandDicDto brandDic : brandsDic) {
                hubSupplierBrandFlag.put(brandDic.getSupplierId() + "_" + brandDic.getSupplierBrand(),
                        brandDic.getFilterFlag());
            }
        }
    }

    /**
     * 初始化hubSeasonFlag<br>
     * 获取有效的供应商季节，key是supplierId_supplierSeason，value是filterFlag
     *
     * @return
     */
    protected Map<String, Byte> getHubSeasonFlag() {
        if (null == hubSeasonFlag) {
            hubSeasonFlag = new ConcurrentHashMap<>();
            setHubSeasonFlag();
        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                setHubSeasonFlag();
            }
        }

        return hubSeasonFlag;
    }

    /**
     * 设置有效的供应商季节，key是supplierId_supplierSeason，value是filterFlag
     */
    private void setHubSeasonFlag() {
        List<HubSeasonDicDto> supplierSeasons = dataServiceHandler.getEffectiveHubSeasons();
        if (null != supplierSeasons && supplierSeasons.size() > 0) {
            for (HubSeasonDicDto season : supplierSeasons) {
                hubSeasonFlag.put(season.getSupplierid() + "_" + season.getSupplierSeason(), season.getFilterFlag());
            }
        }
    }



    /**
     * 判断供应商的品牌和季节是不是有效
     *
     * @param supplierId
     *            供应商门户编号
     * @param supplierBrandName
     *            供应商品牌名称
     * @param supplierSeason
     *            供应商季节名称
     */
    protected byte screenSupplierBrandAndSeasonEffectiveOrNot(String supplierId, String supplierBrandName,
                                                            String supplierSeason) {

        getHubSupplierBrandFlag();
        if (hubSupplierBrandFlag.containsKey(supplierId + "_" + supplierBrandName)) {
            getHubSeasonFlag();
            if (hubSeasonFlag.containsKey(supplierId + "_" + supplierSeason)) {
                return FilterFlag.EFFECTIVE.getIndex();
            }
        }
        return FilterFlag.INVALID.getIndex();
    }

    /**
     * key : supplierId_supplierGender
     *
     * @param supplierId
     * @return
     */
    protected Map<String, String> getGenderMap(String supplierId) {
        if (null == genderStaticMap) {
            genderStaticMap = new HashMap<>();
            hubGenderStaticMap = new HashMap<>();
            setGenderValueToMap(supplierId);
        } else {

            if (pendingCommonHandler.isNeedHandle()) {
                setGenderValueToMap(supplierId);
            }

        }

        return genderStaticMap;
    }

    protected void setGenderValueToMap(String supplierId) {
        List<HubGenderDicDto> hubGenderDics = dataServiceHandler.getHubGenderDicBySupplierId(supplierId);
        if (null != hubGenderDics && hubGenderDics.size() > 0) {

            Map<String, String> genderMap = new HashMap<>();
            for (HubGenderDicDto dto : hubGenderDics) {
                genderStaticMap.put(dto.getSupplierGender().trim().toUpperCase(), dto.getHubGender().trim());
                hubGenderStaticMap.put(dto.getHubGender(), "");
            }

            // shangpinRedis.hset
        }
    }

    protected String getHubSize(String hubCategoryNo, String hubBrandNo, String supplierId, String supplierSize)
            throws IOException {
        String result = "";

        SizeRequestDto requestDto = new SizeRequestDto();
        requestDto.setBrandNo(hubBrandNo);
        requestDto.setCategoryNo(hubCategoryNo);

        HttpEntity<SizeRequestDto> requestEntity = new HttpEntity<SizeRequestDto>(requestDto);
        ResponseEntity<HubResponseDto<CategoryScreenSizeDom>> entity = restTemplate.exchange(
                apiAddressProperties.getGmsSizeUrl(), HttpMethod.POST, requestEntity,
                new ParameterizedTypeReference<HubResponseDto<CategoryScreenSizeDom>>() {
                });
        HubResponseDto<CategoryScreenSizeDom> responseDto = entity.getBody();

        try {
            List<CategoryScreenSizeDom> sizeDomList = responseDto.getResDatas();
            if (null != sizeDomList && sizeDomList.size() > 0) {
                List<SizeStandardItem> sizeStandardItemList = sizeDomList.get(0).getSizeStandardItemList();
                boolean find = false;
                for (SizeStandardItem sizeItem : sizeStandardItemList) {
                    if (sizeItem.getSizeStandardValue().equals(supplierSize)) {

                        if (!find) {
                            result = sizeItem.getScreenSizeStandardValueId() + "," + sizeItem.getSizeStandardName()
                                    + ":" + sizeItem.getSizeStandardValue();

                        } else {
                            log.error("品牌：" + hubBrandNo + " 品类: " + hubCategoryNo + " 的尺码对照有错误。");
                            return "";
                        }
                        find = true;

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void setSkuPendingSizePropertyValue(HubSkuPendingDto hubSkuPending, String hubSize) {
        String[] sizeAndIdArray = hubSize.split(",");
        hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());


        String spSizeTypeAndSize =   sizeAndIdArray[1];
        hubSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
        hubSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":")+1,spSizeTypeAndSize.length()));

        hubSkuPending.setScreenSize(sizeAndIdArray[0]);
    }



    /**
     * 按供货商获取品类的映射关系 主键 category_gender value 尚品的品类编号+"_"+匹配状态
     *
     * @param supplierId
     * @return
     */
    protected Map<String, Map<String, String>> getCategoryMappingMap(String supplierId) throws Exception {
        if (null == supplierCategoryMappingStaticMap) {// 初始化
            supplierCategoryMappingStaticMap = new HashMap<>();
            this.setSupplierCategoryValueToMap(supplierId);

        } else {
            if (!supplierCategoryMappingStaticMap.containsKey(supplierId)) {// 未包含
                this.setSupplierCategoryValueToMap(supplierId);
            } else {
                if (pendingCommonHandler.isNeedHandle()) {// 包含 需要重新拉取
                    this.setSupplierCategoryValueToMap(supplierId);
                }
            }
        }
        return supplierCategoryMappingStaticMap;
    }

    /**
     * 冗余供货商的性别，直接查询
     *
     * @param supplierId
     * @throws Exception
     */
    private void setSupplierCategoryValueToMap(String supplierId) throws Exception {

        // List<HubGenderDicDto> hubGenderDicDtos =
        // dataServiceHandler.getHubGenderDicBySupplierId(null);
        // Map<Long,String> hubGenderMap = new HashMap<>();
        // for(HubGenderDicDto dto:hubGenderDicDtos){
        // hubGenderMap.put(dto.getGenderDicId(),dto.getSupplierGender());
        // }

        List<HubSupplierCategroyDicDto> hubSupplierCategroyDicDtos = dataServiceHandler
                .getSupplierCategoryBySupplierId(supplierId);
        if (null != hubSupplierCategroyDicDtos && hubSupplierCategroyDicDtos.size() > 0) {
            Map<String, String> categoryMap = new HashMap<>();
            String spCategory = "";

            for (HubSupplierCategroyDicDto dto : hubSupplierCategroyDicDtos) {
                // map 的key 供货商的品类 + "_"+供货商的性别 ，value ： 尚品的品类 + "_"+ 匹配状态 (1
                // :匹配到4级 2：可以匹配但未匹配到4级）
                // if(hubGenderMap.containsKey(dto.getGenderDicId())){
                if (StringUtils.isBlank(dto.getSupplierCategory()))
                    continue;
                if (StringUtils.isBlank(dto.getSupplierGender()))
                    continue;
                spCategory = (null == dto.getHubCategoryNo() ? "" : dto.getHubCategoryNo());
                categoryMap.put(
                        dto.getSupplierCategory().trim().toUpperCase() + "_"
                                + dto.getSupplierGender().trim().toUpperCase(),
                        spCategory + "_" + dto.getMappingState());
                // }
            }
            supplierCategoryMappingStaticMap.put(supplierId, categoryMap);
        }

    }




    protected boolean  handlePicLink(PendingSpu spu, HubSpuPendingDto hubSpuPending) {
        Long supplierSpuId = spu.getSupplierSpuId();
        String picUrl = dataServiceHandler.getPicUrlBySupplierSpuId(supplierSpuId);
        if (StringUtils.isNotBlank(picUrl)) {
            hubSpuPending.setPicState(PicState.HANDLED.getIndex());
            return true;

        }else{
            return false;
        }
    }

    protected Map<String, String> getOriginMap() {
        if (null == originStaticMap) {
            originStaticMap = new HashMap<>();

            setOriginStaticMap();

        } else {
            if (pendingCommonHandler.isNeedHandle()) {
                setOriginStaticMap();
            }
        }
        return originStaticMap;
    }

    private void setOriginStaticMap() {
        List<HubSupplierValueMappingDto> valueMappingDtos = dataServiceHandler
                .getHubSupplierValueMappingByType(SupplierValueMappingType.TYPE_ORIGIN.getIndex());
        for (HubSupplierValueMappingDto dto : valueMappingDtos) {
            originStaticMap.put(dto.getSupplierVal(), dto.getHubVal());

        }
    }


    protected void setSkuPendingValue(SpuPending hubSpuPending, PendingSku supplierSku, byte filterFlag, HubSkuPendingDto hubSkuPending,Date date) {
        BeanUtils.copyProperties(supplierSku, hubSkuPending);
        // baracode 需要特殊处理
        if (StringUtils.isBlank(hubSkuPending.getSupplierBarcode())) {
            hubSkuPending.setSupplierBarcode(supplierSku.getSupplierSkuNo());
        }

        hubSkuPending.setCreateTime(date);
        hubSkuPending.setUpdateTime(date);
        hubSkuPending.setSupplierNo(hubSpuPending.getSupplierNo());
        hubSkuPending.setFilterFlag(filterFlag);
        hubSkuPending.setSpuPendingId(hubSpuPending.getSpuPendingId());
        hubSkuPending.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());

    }


    protected void setValueFromHubSuppierSpuToPendingSpu(HubSupplierSpuDto hubSpu, PendingSpu pendingSpu){
        pendingSpu.setSupplierId(hubSpu.getSupplierId());
        pendingSpu.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
        pendingSpu.setSupplierSpuId(hubSpu.getSupplierSpuId());
        pendingSpu.setHubBrandNo(hubSpu.getSupplierBrandname());
        pendingSpu.setHubCategoryNo(hubSpu.getSupplierCategoryname());
        pendingSpu.setHubColor(hubSpu.getSupplierSpuColor());
        pendingSpu.setHubGender(hubSpu.getSupplierGender());
        pendingSpu.setHubMaterial(hubSpu.getSupplierMaterial());
        pendingSpu.setHubOrigin(hubSpu.getSupplierOrigin());
        pendingSpu.setHubSeason(hubSpu.getSupplierSeasonname());
        pendingSpu.setSpuDesc(hubSpu.getSupplierSpuDesc());
        pendingSpu.setSpuModel(hubSpu.getSupplierSpuModel());
        pendingSpu.setSpuName(hubSpu.getSupplierSpuName());
    }


    protected void setSpuPendingValueForUpdate(PendingSpu spu, HubSpuPendingDto spuPendingDto, HubSpuPendingDto updateSpuPending) throws Exception {
        boolean allStatus = true;
        // 获取品牌

        if(StringUtils.isNotBlank(spu.getHubBrandNo())){

            boolean brandmapping = setBrandMapping(spu, updateSpuPending);
            if(!brandmapping){
                allStatus =false;
            }

        }
        //验证货号必须要有品牌
        if(StringUtils.isNotBlank(spu.getSpuModel())){
            if(StringUtils.isBlank(spu.getHubBrandNo())){
                spu.setHubBrandNo(spuPendingDto.getHubBrandNo());
            }
            if(!setBrandModel(spu, updateSpuPending)) allStatus =false;
        }

        // 设置性别
        if (StringUtils.isNotBlank(spu.getHubGender())) {
            if (!setGenderMapping(spu, updateSpuPending))
                allStatus = false;
        }

        // 获取品类
        if (StringUtils.isNotBlank(spu.getHubCategoryNo())&&StringUtils.isNotBlank(spu.getHubGender())) {
            if (!setCategoryMapping(spu, updateSpuPending))
                allStatus = false;
        }

        // 获取颜色
        if (StringUtils.isNotBlank(spu.getHubColor())) {

            if (!setColorMapping(spu, updateSpuPending))
                allStatus = false;
        }

        // 获取季节
        if(StringUtils.isNotBlank(spu.getHubSeason())){
            if (!setSeasonMapping(spu, updateSpuPending))
                allStatus = false;
        }

        // 获取材质
        if (StringUtils.isNotBlank(spu.getHubMaterial())) {
            if(!replaceMaterial(spu, updateSpuPending)) allStatus = false;
        }
        // 产地映射
        if (StringUtils.isNotBlank(spu.getHubOrigin())) {
            if (!setOriginMapping(spu, updateSpuPending))
                allStatus = false;
        }
    }

    protected boolean setBrandMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        Map<String, String> brandMap = this.getBrandMap();
        if (StringUtils.isNotBlank(spu.getHubBrandNo())) {

            if (brandMap.containsKey(spu.getHubBrandNo().trim().toUpperCase())) {
                // 包含时转化赋值
                hubSpuPending.setHubBrandNo(brandMap.get(spu.getHubBrandNo().trim().toUpperCase()));
                hubSpuPending.setSpuBrandState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

            } else {
                result = false;
                hubSpuPending.setSpuBrandState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveBrand(spu.getSupplierId(), spu.getHubBrandNo().trim());

            }
        } else{
            result = false;
            hubSpuPending.setSpuBrandState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }
        return result;
    }

    protected boolean setOriginMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        Map<String, String> originMap = this.getOriginMap();
        if (StringUtils.isNotBlank(spu.getHubOrigin())) {

            if (originMap.containsKey(spu.getHubOrigin().trim())) {
                hubSpuPending.setHubOrigin(originMap.get(spu.getHubOrigin().trim()));
                hubSpuPending.setOriginState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                return true;
            } else {
                hubSpuPending.setHubOrigin(spu.getHubOrigin().trim());
                hubSpuPending.setOriginState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                return false;
            }
        } else {
            hubSpuPending.setOriginState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            return false;
        }
    }



    public boolean setCategoryMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        String categoryAndStatus = "";
        Integer mapStatus = 0;
        // 无品类 无性别时 直接返回
        if (StringUtils.isBlank(spu.getHubCategoryNo()) || StringUtils.isBlank(spu.getHubGender())) {
            return false;
        }
        Map<String, Map<String, String>> supplierCategoryMappingMap = this.getCategoryMappingMap(spu.getSupplierId());
        if (supplierCategoryMappingMap.containsKey(spu.getSupplierId())) {
            Map<String, String> categoryMappingMap = supplierCategoryMappingMap.get(spu.getSupplierId());

            if (categoryMappingMap.containsKey(
                    spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase())) {
                // 包含时转化赋值
                categoryAndStatus = categoryMappingMap.get(
                        spu.getHubCategoryNo().trim().toUpperCase() + "_" + spu.getHubGender().trim().toUpperCase());
                if (categoryAndStatus.contains("_")) {
                    hubSpuPending.setHubCategoryNo(categoryAndStatus.substring(0, categoryAndStatus.indexOf("_")));
                    mapStatus = Integer.valueOf(categoryAndStatus.substring(categoryAndStatus.indexOf("_") + 1));
                    hubSpuPending.setCatgoryState(mapStatus.byteValue());
                    if(hubSpuPending.getCatgoryState().intValue()!=PropertyStatus.MESSAGE_HANDLED.getIndex()){
                        //未达到4级品类
                        result = false;
                    }

                } else {
                    result = false;
//					hubSpuPending.setHubCategoryNo(pendingCommonHandler.getSpCategoryValue(spu.getHubCategoryNo()));
                    hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }

            } else {

                result = false;
                hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveHubCategory(spu.getSupplierId(), spu.getHubCategoryNo(), spu.getHubGender());

            }
        } else {
            result = false;
//			hubSpuPending.setHubCategoryNo(pendingCommonHandler.getSpCategoryValue(spu.getHubCategoryNo()));
            hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveHubCategory(spu.getSupplierId(), spu.getHubCategoryNo(), spu.getHubGender());

        }
        return result;
    }

    protected boolean setBrandModel(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        String spuModel = "";

        if (!StringUtils.isEmpty(hubSpuPending.getHubBrandNo())) {
            BrandModelDto queryDto = new BrandModelDto();
            queryDto.setBrandMode(spu.getSpuModel());
            queryDto.setHubBrandNo(hubSpuPending.getHubBrandNo());
            queryDto.setHubCategoryNo(hubSpuPending.getHubCategoryNo());
            BrandModelResult verify = brandModelRuleGateWay.verify(queryDto);
            if (null != verify) {
                if (verify.isPassing()) {
                    hubSpuPending.setSpuModel(verify.getBrandMode());
                    hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                } else {
                    result = false;
                    hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }

            } else {
                result = false;
                hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            }

        }else{
            result = false;
            hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }

        return result;
    }


    private Map<String, Map<String, String>> getBrandModelMap(String hubBrandNo) {

        if (null == brandModelStaticMap) {// 初始化
            brandModelStaticMap = new HashMap<>();

            this.setBrandModelValueToMap(hubBrandNo);

        } else {
            if (!brandModelStaticMap.containsKey(hubBrandNo)) {// 未包含
                this.setBrandModelValueToMap(hubBrandNo);
            } else {
                if (pendingCommonHandler.isNeedHandle()) {// 包含 需要重新拉取
                    this.setBrandModelValueToMap(hubBrandNo);
                }
            }
        }
        return brandModelStaticMap;
    }

    private void setBrandModelValueToMap(String hubBrandNo) {
        List<HubBrandModelRuleDto> brandModles = dataServiceHandler.getBrandModle(hubBrandNo);
        if (null != brandModles && brandModles.size() > 0) {

            Map<String, String> brandModelMap = new HashMap<>();
            for (HubBrandModelRuleDto dto : brandModles) {
                if (dto.getRuleState().intValue() == PropertyStatus.MESSAGE_HANDLED.getIndex()) {// 已确认

                    brandModelMap.put(dto.getModelRex(), dto.getHubCategoryNo());
                }

            }
            brandModelStaticMap.put(hubBrandNo, brandModelMap);

        }

    }

    protected boolean setGenderMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        // 获取性别
        Map<String, String> genderMap = this.getGenderMap(null);

        if (null != spu.getHubGender() && genderMap.containsKey(spu.getHubGender().trim().toUpperCase())) {
            // 包含时转化赋值
            hubSpuPending.setHubGender(genderMap.get(spu.getHubGender().toUpperCase()));
            hubSpuPending.setSpuGenderState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        } else {
            result = false;
            hubSpuPending.setSpuGenderState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveHubGender(null, spu.getHubGender());

        }

        return result;
    }

    protected boolean setColorMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        Map<String, String> colorMap = this.getColorMap();
        if (colorMap.containsKey(spu.getHubColor()) & !StringUtils.isEmpty(colorMap.get(spu.getHubColor()))) {
            // 包含时转化赋值
            hubSpuPending.setHubColor(colorMap.get(spu.getHubColor()));
            hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        } else {
            result = false;
            hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveColorItem(spu.getHubColor());

        }
        return result;
    }


    protected boolean setSeasonMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        Map<String, String> seasonMap = this.getSeasonMap(spu.getSupplierId());
        boolean result = true;
        String spSeason = "", seasonSign = "";
        if (StringUtils.isNotBlank(spu.getHubSeason())) {
            if (seasonMap.containsKey(spu.getSupplierId() + "_" + spu.getHubSeason().trim())) {
                // 包含时转化赋值
                spSeason = seasonMap.get(spu.getSupplierId() + "_" + spu.getHubSeason().trim());
                if (StringUtils.isNotBlank(spSeason)) {
                    if (spSeason.indexOf("|") > 0) {
                        seasonSign = spSeason.substring(spSeason.indexOf("|") + 1, spSeason.length());
                        hubSpuPending.setHubSeason(spSeason.substring(0, spSeason.indexOf("|")));
                        if (SeasonType.SEASON_CURRENT.getIndex().toString().equals(seasonSign)) {
                            hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_CURRENT.getIndex().byteValue());
                        } else {
                            hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_NOT_CURRENT.getIndex().byteValue());
                        }
                    }
                    hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                } else {
                    result = false;
                    hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }

            } else {
                result = false;
                hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveSeason(spu.getSupplierId(), spu.getHubSeason());
            }

        } else {//
            result = false;
            hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }
        return result;
    }


    /**
     * 返回false 说明有英文
     *
     * @param spu
     * @param hubSpuPending
     * @return
     */
    protected boolean replaceMaterial(PendingSpu spu, HubSpuPendingDto hubSpuPending) {
        Map<String, String> materialMap = this.getMaterialMap();
        Set<String> materialSet = materialMap.keySet();
        String supplierMaterial = "";
        if (StringUtils.isNotBlank(spu.getHubMaterial())) {

            for (String material : materialSet) {
                if (StringUtils.isBlank(material))
                    continue;

                supplierMaterial = spu.getHubMaterial().toLowerCase();
                if (supplierMaterial.indexOf(material.toLowerCase()) >= 0) {

                    spu.setHubMaterial(supplierMaterial.replaceAll(material.toLowerCase(), materialMap.get(material)));
                }

            }
            hubSpuPending.setHubMaterial(spu.getHubMaterial());

            if (!RegexUtil.excludeLetter(hubSpuPending.getHubMaterial())) {
                hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                // 材质含有英文 返回false
                return false;
            } else {
                hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                return true;
            }

        } else {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            // 无材质 返回false
            return false;
        }

    }
}
