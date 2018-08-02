package com.shangpin.pending.product.consumer.supplier.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.rule.dto.HubBrandModelRuleDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingNohandleReasonDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.product.business.gms.gateway.GmsGateWay;
import com.shangpin.ephub.client.product.business.gms.result.*;
import com.shangpin.ephub.client.product.business.gms.result.BrandDom;
import com.shangpin.ephub.client.product.business.model.dto.BrandModelDto;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.*;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import com.shangpin.pending.product.consumer.conf.rpc.ApiAddressProperties;
import com.shangpin.pending.product.consumer.service.MaterialService;
import com.shangpin.pending.product.consumer.supplier.dto.*;
import com.shangpin.pending.product.consumer.supplier.dto.CategoryScreenSizeDom;
import com.shangpin.pending.product.consumer.supplier.dto.HubResponseDto;
import com.shangpin.pending.product.consumer.supplier.dto.SizeStandardItem;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    HubBrandModelRuleGateWay brandModelRuleGateWay;

    @Autowired
    MatchSizeGateWay matchSizeGateWay;


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

    @Autowired
    MaterialService materialService;

    @Autowired
    GmsGateWay gmsGateWay;

    @Autowired
    SpuPendingMsgHandleService spuPendingMsgHandleService;

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


//    private Lock seasonlock = new ReentrantLock();


//    private Lock colorlock = new ReentrantLock();
//
//    private Lock materialLock = new ReentrantLock();
//
//    private Lock brandLock = new ReentrantLock();
//
//    private Lock supplierBrandLock = new ReentrantLock();
//
//    private Lock originLock = new ReentrantLock();
//
//    private Lock categoryLock = new ReentrantLock();

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
//            seasonlock.lock();
            try {
                if (pendingCommonHandler.isNeedHandleSeason()) {
                    setSeasonStaticMap();
                }
            }finally {
//                seasonlock.unlock();
            }

        }
        return seasonStaticMap;
    }

    private void setSeasonStaticMap() {
        List<HubSeasonDicDto> hubSeasonDics = dataServiceHandler.getHubSeasonDic();
        Map<String,String> keyMap = new HashMap<>();
        Map<String,String> keyMap1 = new HashMap<>();
        for (HubSeasonDicDto dicDto : hubSeasonDics) {
//        	System.out.println(dicDto.getSupplierid());
            if (StringUtils.isNotBlank(dicDto.getHubMarketTime()) && StringUtils.isNotBlank(dicDto.getHubSeason())
                    &&StringUtils.isNotBlank(dicDto.getSupplierSeason())) {
                keyMap.put(dicDto.getSupplierid() + "_" + dicDto.getSupplierSeason().trim(),"");
                seasonStaticMap.put(dicDto.getSupplierid() + "_" + dicDto.getSupplierSeason().trim(),
                        (null==dicDto.getHubMarketTime()?"":dicDto.getHubMarketTime().trim()) + "_" +
                                (null==dicDto.getHubSeason()?"":dicDto.getHubSeason().trim()) + "|"
                                + (null==dicDto.getFilterFlag()?"":dicDto.getFilterFlag().toString()));
                hubSeasonStaticMap.put(dicDto.getHubMarketTime() + "_" + dicDto.getHubSeason(), "");
                keyMap1.put(dicDto.getHubMarketTime() + "_" + dicDto.getHubSeason(), "");
            }
        }
        this.removeFromMap(seasonStaticMap,keyMap);
        this.removeFromMap(hubSeasonStaticMap,keyMap1);
    }

    protected Map<String, String> getColorMap() {
        if (null == colorStaticMap) {
            colorStaticMap = new HashMap<>();
            hubColorStaticMap = new HashMap<>();
            setColorStaticMap();

        } else {
//            colorlock.lock();
            try {
                if (pendingCommonHandler.isNeedHandleCorlor()) {
                    setColorStaticMap();
                }
            }finally {
//                colorlock.unlock();
            }
        }
        return colorStaticMap;
    }

    private void setColorStaticMap() {
        List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
        Map<String,String> keyMap = new HashMap<>();
        Map<String,String> keyMap1 = new HashMap<>();
        for (ColorDTO dto : colorDTOS) {
            if(dto.getSupplierColor()!=null){
                keyMap.put(dto.getSupplierColor().toUpperCase(),"");
                 colorStaticMap.put(dto.getSupplierColor().toUpperCase(), dto.getHubColorName());
                 hubColorStaticMap.put(dto.getHubColorName(), "");
                keyMap1.put(dto.getHubColorName(), "");
            }
        }
        this.removeFromMap(colorStaticMap,keyMap);
        this.removeFromMap(hubColorStaticMap,keyMap1);
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
            setMaterailStaticMap();
        } else {
//            materialLock.lock();
            try{
                if (pendingCommonHandler.isNeedHandleMaterial()) {
                    setMaterailStaticMap();
                }
            }finally {
//                materialLock.unlock();
            }
        }
        return materialStaticMap;
    }

    private void setMaterailStaticMap() {
        List<MaterialDTO> materialDTOS = dataServiceHandler.getMaterialMapping();
        Map<String,String> keyMap = new HashMap<>();
        for (MaterialDTO dto : materialDTOS) {
            keyMap.put(dto.getSupplierMaterial(), "");
            materialStaticMap.put(dto.getSupplierMaterial(), dto.getHubMaterial());
        }
        this.removeFromMap(materialStaticMap,keyMap);
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
            setBrandStaticMap();
        } else {
//            brandLock.lock();
            try{
                if (pendingCommonHandler.isNeedHandleBrand()) {
                    setBrandStaticMap();
                }
            }finally {
//                brandLock.unlock();
            }
        }
        return brandStaticMap;
    }

    private void setBrandStaticMap() throws Exception {
        List<HubBrandDicDto> brandDicDtos = dataServiceHandler.getAvailableBrand();
        Map<String,String> keyMap = new HashMap<>();
        Map<String,String> keyMap1 = new HashMap<>();
        for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
            brandStaticMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
                    hubBrandDicDto.getHubBrandNo());
            keyMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),"");
            if(hubBrandDicDto.getHubBrandNo()!=null){
                hubBrandStaticMap.put(hubBrandDicDto.getHubBrandNo().trim(), "");
                keyMap1.put(hubBrandDicDto.getHubBrandNo().trim(), "");
            }
        }
        this.removeFromMap(brandStaticMap,keyMap);
        this.removeFromMap(hubBrandStaticMap,keyMap1);
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
            if (pendingCommonHandler.isNeedHandleSupplierBrand()) {
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
        Map<String,String> keyMap = new HashMap<>();
        if (null != brandsDic && brandsDic.size() > 0) {
            for (HubSupplierBrandDicDto brandDic : brandsDic) {
                keyMap.put(brandDic.getSupplierId() + "_" + brandDic.getSupplierBrand(),"");
                hubSupplierBrandFlag.put(brandDic.getSupplierId() + "_" + brandDic.getSupplierBrand(),
                        brandDic.getFilterFlag());
            }
            this.removeFromMap(hubSupplierBrandFlag,keyMap);
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
            if (pendingCommonHandler.isNeedHandleSeasonFlag()) {
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
        Map<String,String> keyMap = new HashMap<>();
        if (null != supplierSeasons && supplierSeasons.size() > 0) {
            for (HubSeasonDicDto season : supplierSeasons) {
                keyMap.put(season.getSupplierid() + "_" + season.getSupplierSeason(), "");
                hubSeasonFlag.put(season.getSupplierid() + "_" + season.getSupplierSeason(), season.getFilterFlag());
            }
            this.removeFromMap(hubSeasonFlag,keyMap);
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

        getHubSupplierBrandFlag();//获取品牌信息
        if (hubSupplierBrandFlag.containsKey(supplierId + "_" + supplierBrandName)) {
            getHubSeasonFlag();//获取季节信息
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

            if (pendingCommonHandler.isNeedHandleGender()) {
                setGenderValueToMap(supplierId);
            }

        }

        return genderStaticMap;
    }

    protected void setGenderValueToMap(String supplierId) {
        List<HubGenderDicDto> hubGenderDics = dataServiceHandler.getHubGenderDicBySupplierId(supplierId);
        Map<String,String> keyMap = new HashMap<>();
        Map<String,String> keyMap1 = new HashMap<>();
        if (null != hubGenderDics && hubGenderDics.size() > 0) {

            Map<String, String> genderMap = new HashMap<>();
            for (HubGenderDicDto dto : hubGenderDics) {
                keyMap.put(dto.getSupplierGender().trim().toUpperCase(), "");
                genderStaticMap.put(dto.getSupplierGender().trim().toUpperCase(), dto.getHubGender().trim());
                keyMap1.put(dto.getHubGender(), "");
                hubGenderStaticMap.put(dto.getHubGender(), "");
            }
            this.removeFromMap(genderStaticMap,keyMap);
            this.removeFromMap(hubGenderStaticMap,keyMap1);
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


    protected String getHubSize(String hubCategoryNo, String hubBrandNo,  String supplierSize){
        MatchSizeDto sizeDto = new MatchSizeDto();
        sizeDto.setHubBrandNo(hubBrandNo);
        sizeDto.setHubCategoryNo(hubCategoryNo);
        sizeDto.setSize(supplierSize);
        MatchSizeResult matchSizeResult = matchSizeGateWay.matchSize(sizeDto);
        if(null==matchSizeResult){
            return "";
        }else{
            if(matchSizeResult.isPassing()){//通过
                return (null==matchSizeResult.getSizeId()||"null".equals(matchSizeResult.getSizeId())?"":matchSizeResult.getSizeId())+","+ matchSizeResult.getSizeType()+":"+matchSizeResult.getSizeValue();
            }else  if(matchSizeResult.isMultiSizeType()) {//多个匹配  失败 增加备注
                log.info("sku pending 含有多个匹配，不更新："+matchSizeResult.getResult()+"|原始数据："+supplierSize);
                return "";
            }else  if(matchSizeResult.isFilter()){//有模板没匹配上
                return ConstantProperty.SIZE_EXCLUDE;
            }else {//不做处理
                return "";
            }
        }

    }




    protected void setSkuPendingSizePropertyValue(HubSkuPendingDto hubSkuPending, String hubSize) {
        String[] sizeAndIdArray = hubSize.split(",");
        hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSkuPending.setSkuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());

        String spSizeTypeAndSize =   sizeAndIdArray[1];
        hubSkuPending.setHubSkuSizeType(spSizeTypeAndSize.substring(0,spSizeTypeAndSize.indexOf(":")));
        hubSkuPending.setHubSkuSize(spSizeTypeAndSize.substring(spSizeTypeAndSize.indexOf(":")+1,spSizeTypeAndSize.length()));

        hubSkuPending.setScreenSize(null==sizeAndIdArray[0]?"":sizeAndIdArray[0]);

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
//                categoryLock.lock();
                try {
                    if (pendingCommonHandler.isNeedHandleCategory()) {// 包含 需要重新拉取
                        this.setSupplierCategoryValueToMap(supplierId);
                    }
                }finally {
//                    categoryLock.unlock();
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
            Map<String,String> keyMap = new HashMap<>();
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
            hubSpuPending.setPicState(PicState.UNHANDLED.getIndex());
            return false;
        }
    }


    protected boolean  handlePicState(PendingSpu spu, SpuPending hubSpuPending) {
        Long supplierSpuId = spu.getSupplierSpuId();
        String picUrl = dataServiceHandler.getPicUrlBySupplierSpuId(supplierSpuId);
        if (StringUtils.isNotBlank(picUrl)) {
            hubSpuPending.setPicState(PicState.HANDLED.getIndex());
            return true;

        }else{
            if(hubSpuPending.isHavePic()){
                hubSpuPending.setPicState(PicState.UNHANDLED.getIndex());
            }else{
                hubSpuPending.setPicState(PicState.NO_PIC.getIndex());
            }
            return false;
        }
    }

    protected Map<String, String> getOriginMap() {
        if (null == originStaticMap) {
            originStaticMap = new HashMap<>();

            setOriginStaticMap();

        } else {
//            originLock.lock();
            try{
                if (pendingCommonHandler.isNeedHandleOrigin()) {

                    setOriginStaticMap();
                }

            }finally {
//                originLock.unlock();
            }
        }
        return originStaticMap;
    }

    private void setOriginStaticMap() {
        List<HubSupplierValueMappingDto> valueMappingDtos = dataServiceHandler
                .getHubSupplierValueMappingByType(SupplierValueMappingType.TYPE_ORIGIN.getIndex());

        Map<String, String> originMap = new HashMap<>();

        for (HubSupplierValueMappingDto dto : valueMappingDtos) {
            originMap.put(dto.getSupplierVal().toUpperCase(),"");
            originStaticMap.put(dto.getSupplierVal().toUpperCase(), dto.getHubVal());

        }
        this.removeFromMap(originStaticMap,originMap);

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

        //验证货号必须要有品牌
        if(StringUtils.isNotBlank(spu.getSpuModel())){
            if(StringUtils.isBlank(spu.getHubBrandNo())){
                spu.setHubBrandNo(spuPendingDto.getHubBrandNo());
            }
            if(!setBrandModel(spu, updateSpuPending)) allStatus =false;
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
            if(!materialService.changeSupplierToHub(spu, updateSpuPending)) allStatus = false;
        }
        // 产地映射
        if (StringUtils.isNotBlank(spu.getHubOrigin())) {
            if (!setOriginMapping(spu, updateSpuPending))
                allStatus = false;
        }
    }

    protected void setSpuPendingSeasonValueForUpdate(PendingSpu spu, HubSpuPendingDto updateSpuPending) throws Exception {
        // 获取季节
        if(StringUtils.isNotBlank(spu.getHubSeason())){

            setSeasonMapping(spu, updateSpuPending);

        }
    }

    protected void setOriginSpuPendingValueWhenUpdate(HubSpuPendingDto originSpuPending, PendingSpu spu, HubSpuPendingDto updateSpuPending) throws Exception {
        //映射材质
        if(StringUtils.isBlank(originSpuPending.getHubMaterial())&&StringUtils.isNotBlank(spu.getHubMaterial())){
            materialService.changeSupplierToHub(spu, updateSpuPending);
        }
        //映射产地
        if(StringUtils.isBlank(originSpuPending.getHubOrigin())&&StringUtils.isNotBlank(spu.getHubOrigin())){
            setOriginMapping(spu, updateSpuPending);
        }
    }



    protected void setSpuPendingValueForSupplierUpdate(PendingSpu spu, HubSpuPendingDto spuPendingDto, HubSpuPendingDto updateSpuPending,Map<Byte, List<HubSpuPendingNohandleReasonDto>> map) throws Exception {
        boolean allStatus = true;



        // 设置性别
        if (StringUtils.isNotBlank(spu.getHubGender())) {
            if(map.containsKey(ErrorType.GENDER_INFO_ERROR.getIndex())) {
                spuPendingMsgHandleService.updateSpuErrorMsgDateState(map.get(ErrorType.GENDER_INFO_ERROR.getIndex()));
                updateSpuPending.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
                if (!setGenderMapping(spu, updateSpuPending)) allStatus = false;
            }
        }



        //验证货号必须要有品牌
        if(StringUtils.isNotBlank(spu.getSpuModel())){
            if(map.containsKey(ErrorType.ITEM_CODE_ERROR.getIndex())) {
                spuPendingMsgHandleService.updateSpuErrorMsgDateState(map.get(ErrorType.ITEM_CODE_ERROR.getIndex()));
                updateSpuPending.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
                if (StringUtils.isBlank(spu.getHubBrandNo())) {
                    spu.setHubBrandNo(spuPendingDto.getHubBrandNo());
                }
                if (!setBrandModel(spu, updateSpuPending)) allStatus = false;
            }
        }



        // 获取材质
        if (StringUtils.isNotBlank(spu.getHubMaterial())) {
            if(map.containsKey(ErrorType.MATERIAL_INFO_ERROR.getIndex())){
                spuPendingMsgHandleService.updateSpuErrorMsgDateState(map.get(ErrorType.MATERIAL_INFO_ERROR.getIndex()));
                updateSpuPending.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
                if(!materialService.changeSupplierToHub(spu, updateSpuPending)) allStatus = false;
            }

        }
        // 产地映射
        if (StringUtils.isNotBlank(spu.getHubOrigin())) {
            if(map.containsKey(ErrorType.ORIGIN_INFO_ERROR.getIndex())) {
                spuPendingMsgHandleService.updateSpuErrorMsgDateState(map.get(ErrorType.ORIGIN_INFO_ERROR.getIndex()));
                updateSpuPending.setMsgMissHandleState(MsgMissHandleState.SUPPLIER_HAVE_HANDLED.getIndex());
                if (!setOriginMapping(spu, updateSpuPending)) allStatus = false;
            }
        }
    }




    protected boolean setBrandMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        Map<String, String> brandMap = pendingCommonHandler.getSupplierHubBrandMap();//   this.getBrandMap();
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

        //增加品牌的SCM的校验
        if(result){
            BrandDom brand = gmsGateWay.findBrand(hubSpuPending.getHubBrandNo());
            if(null!=brand){
                 if(brand.getIsValid()!=1){
                     result = false;
                     hubSpuPending.setSpuBrandState(SpuBrandState.UNUSEABLE.getIndex());
                 }
            }else{
                result = false;
                hubSpuPending.setSpuBrandState(SpuBrandState.UNUSEABLE.getIndex());
            }
        }

        return result;
    }

    protected boolean setOriginMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {


        if (StringUtils.isNotBlank(spu.getHubOrigin())) {
            String hubOrigin= pendingCommonHandler.getHubOriginFromRedis(spu.getHubOrigin());
            if (StringUtils.isNotBlank(hubOrigin)) {
                hubSpuPending.setHubOrigin(hubOrigin);
                hubSpuPending.setOriginState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                return true;
            } else {
                hubSpuPending.setHubOrigin(spu.getHubOrigin().trim());
                hubSpuPending.setOriginState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveSupplierOrigin(spu.getSupplierId(), spu.getHubOrigin().trim());
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
            hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            return false;
        }

        categoryAndStatus  = pendingCommonHandler.getSupplierHubCategoryFromRedis(spu.getSupplierId(),spu.getHubCategoryNo(),spu.getHubGender());
        if(StringUtils.isBlank(categoryAndStatus)){
            //控制时插入新的记录
            result = false;
            hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveHubCategory(spu.getSupplierId(), spu.getHubCategoryNo(), spu.getHubGender());
        }else{
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
                hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            }
        }
//        Map<String, Map<String, String>> supplierCategoryMappingMap = this.getCategoryMappingMap(spu.getSupplierId());

        return result;
    }

    protected boolean setBrandModel(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = false;


       if (StringUtils.isNotBlank(hubSpuPending.getHubBrandNo())&&StringUtils.isNotBlank(spu.getSpuModel())) {
//           log.info("校验货号参数： 货号:" + spu.getSpuModel().trim().toUpperCase() + " 品牌:" + hubSpuPending.getHubBrandNo() +" 品类:" + hubSpuPending.getHubCategoryNo()  );

           BrandModelDto queryDto = new BrandModelDto();
            queryDto.setBrandMode(spu.getSpuModel().trim().toUpperCase());
            queryDto.setHubBrandNo(hubSpuPending.getHubBrandNo());
            queryDto.setHubCategoryNo(hubSpuPending.getHubCategoryNo());
            log.info("进入调用校验货号参数： 货号:" + spu.getSpuModel().trim().toUpperCase() + " 品牌:" + hubSpuPending.getHubBrandNo() +" 品类:" + hubSpuPending.getHubCategoryNo()  );
            BrandModelResult verify = brandModelRuleGateWay.verifyWithCategory(queryDto);
            log.info("verfy value " + verify.toString());
            if (null != verify) {
                if (verify.isPassing()) {
                    hubSpuPending.setSpuModel(verify.getBrandMode());
                    hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                    result = true;
                } else {
                    //去掉特殊符号
                    hubSpuPending.setSpuModel(brandModelRuleGateWay.replaceSymbol(queryDto));
                    hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }

            } else {

                hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            }

        }else{

            hubSpuPending.setSpuModelState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }

        return result;
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

        if (StringUtils.isBlank(spu.getHubColor()) ) {
            hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            return false;
        }

        boolean result = true;

        String hubColor = pendingCommonHandler.getHubColorFromRedis(spu.getHubColor());

        if (StringUtils.isNotBlank(hubColor)) {
            // 包含时转化赋值
            hubSpuPending.setHubColor(hubColor);
            hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        } else {
            result = false;
            hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveColorItem(spu.getHubColor());

        }
        return result;
    }


    protected boolean setSeasonMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {


        boolean result = true;
        if (StringUtils.isNotBlank(spu.getHubSeason())) {
            String hubSeason = pendingCommonHandler.getHubSeasonFromRedis(spu.getSupplierId(),spu.getHubSeason());

            if (StringUtils.isNotBlank(hubSeason)) {
                // 包含时转化赋值
                hubSpuPending.setHubSeason(hubSeason);
                hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_CURRENT.getIndex().byteValue());
                hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

            } else {
                result = false;
                hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_NOT_CURRENT.getIndex().byteValue());
                hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveSeason(spu.getSupplierId(), spu.getHubSeason());
            }

        } else {//
            result = false;
            hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_NOT_CURRENT.getIndex().byteValue());
            hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }
        return result;
    }




    private void removeFromMap(Map<String,?> resourceMap,Map<String,String> keyMap){
        Iterator<String> iterator = resourceMap.keySet().iterator();
        String key= "";
        while(iterator.hasNext()){
            key =iterator.next();
            if(!keyMap.containsKey(key)){
                resourceMap.remove(key);
            }
        }
    }
}