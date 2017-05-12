package com.shangpin.ephub.product.business.service.hub.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.common.enumeration.ScmGenderType;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.HubResponseDto;
import com.shangpin.ephub.product.business.rest.gms.service.SopSkuService;
import com.shangpin.ephub.product.business.service.ServiceConstant;
import com.shangpin.ephub.product.business.service.hub.HubProductService;
import com.shangpin.ephub.product.business.service.hub.dto.ApiProductOrgExtendDom;
import com.shangpin.ephub.product.business.service.hub.dto.ApiSkuOrgDom;
import com.shangpin.ephub.product.business.service.hub.dto.HubProductDto;
import com.shangpin.ephub.product.business.service.hub.dto.HubProductIdDto;
import com.shangpin.ephub.product.business.service.hub.dto.PlaceOrigin;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuDto;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuQueryDto;
import com.shangpin.ephub.product.business.service.hub.dto.SpProductOrgInfoEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Service("hubCommonProductServiceImpl")
@Slf4j
public class HubProductServiceImpl implements HubProductService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HubSpuGateWay hubSpuGateWay;

    @Autowired
    HubSkuGateWay hubSkuGateWay;

    @Autowired
    HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
    HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

    @Autowired
    ApiAddressProperties apiAddressProperties;
    @Autowired
    SopSkuService sopSkuService;

    @Autowired
    private TaskExecutor executor;

    ObjectMapper objectMapper =new ObjectMapper();

    @Override
    public void sendHubProuctToScm(HubProductIdDto hubProductIdDto) throws Exception {
        try {
            Long  spuId = hubProductIdDto.getId();
            List<HubProductIdDto> skus = hubProductIdDto.getSubProduct();

            //推送对象初始化
            SpProductOrgInfoEntity spSpuInfo = new SpProductOrgInfoEntity(); //SCM需要的SPU对象
            ApiProductOrgExtendDom spSpuExtendInfo = new ApiProductOrgExtendDom();//  扩展对象

            //获取HUBSPU
            HubSpuDto hubSpuDto = hubSpuGateWay.selectByPrimaryKey(spuId);
            if(null!=hubSpuDto){
                //scmspu 对象赋值
                setScmSpu(spSpuInfo, hubSpuDto);
                //scmspu 扩展对象赋值
                setScmSpuExtendProperty(spSpuExtendInfo, hubSpuDto);

                Map<String,Map<String,ApiSkuOrgDom>> supplierSizeMap = new HashMap<>();
                //scmsku 对方赋值
                setScmSkuValue(skus, spSpuInfo, hubSpuDto, supplierSizeMap);

                Set<String> supplierNoSet = supplierSizeMap.keySet();
                if(null!=supplierNoSet){//以供货商的维度推送数据
                    for(String supplierId :supplierNoSet){
                        Map<String, ApiSkuOrgDom> apiSkuOrgDomMap = supplierSizeMap.get(supplierId);

                        List<ApiSkuOrgDom> skuOrgDoms = new ArrayList<>();
                        Set<Map.Entry<String, ApiSkuOrgDom>> entries = apiSkuOrgDomMap.entrySet();
                        for(Map.Entry<String,ApiSkuOrgDom> entry:entries){
                            skuOrgDoms.add(entry.getValue());
                        }

                        //推送
                        //---------------------------------- 推送前先调用接口  看是否存在  存在则不用推送
                        Map<String,SopSkuDto> existSopSkuMap = new HashMap<>();
                        List<ApiSkuOrgDom> existSkuOrgDoms = getExistSku(supplierId,skuOrgDoms,existSopSkuMap);
                        //处理已经存在的
                        if(existSkuOrgDoms.size()>0){
                            log.info("SPSKUNO已存在");
                            handleExistSku(existSkuOrgDoms,existSopSkuMap,hubSpuDto);
                        }

                        if(skuOrgDoms.size()>0){
                            log.info("SPSKUNO 不存在");
                            handleSendToScm(spSpuInfo, spSpuExtendInfo, skuOrgDoms);
                        }

                    }
                }

            }
        } catch (Exception e) {
            ObjectMapper mapper = new ObjectMapper();
            String para = mapper.writeValueAsString(hubProductIdDto);
            log.error("HubProductServiceImpl.sendHubProuctToScm has exception .Parameter :" + para + " reason :" + e.getMessage(),e);
        }


    }

    private void handleExistSku(List<ApiSkuOrgDom> existSkuOrgDoms,Map<String,SopSkuDto> existSopSkuMap,HubSpuDto hubSpuDto) {
        for(ApiSkuOrgDom skuOrgDom:existSkuOrgDoms){
            SopSkuDto sopSkuDto = existSopSkuMap.get(skuOrgDom.getSupplierSkuNo());
            log.info("sopSkuDto  = " + sopSkuDto.toString());
            if(StringUtils.isBlank(sopSkuDto.getSkuNo())){
                updateSkuMappingStatus(Long.valueOf(skuOrgDom.getSkuOrginalFromId()), SupplierSelectState.SELECTE_FAIL,"SOP未审核通过，需要人工处理");
            }else{
                //货号判断
                if(!sopSkuDto.getProductModel().equals(hubSpuDto.getSpuModel())){
                    updateSkuMappingStatus(Long.valueOf(skuOrgDom.getSkuOrginalFromId()), SupplierSelectState.EXIST,"SOP已存在此商品，货号不同");
                }else{
                    updateSkuMappingStatus(Long.valueOf(skuOrgDom.getSkuOrginalFromId()), SupplierSelectState.EXIST, ServiceConstant.HUB_SEND_TO_SCM_EXIST);
                }

            }

            //获取 sku pending 的值  更新状态
            updateSkuPendingStatus(sopSkuDto);
            // 更新 hubsku
            updateHubSkuSpSkuNo(skuOrgDom.getHubSkuNo(),sopSkuDto.getSkuNo());
        }
    }

    private void handleSendToScm(SpProductOrgInfoEntity spSpuInfo, ApiProductOrgExtendDom spSpuExtendInfo, List<ApiSkuOrgDom> skuOrgDoms) throws JsonProcessingException {

        for(ApiSkuOrgDom skuOrg:skuOrgDoms){
        	String hubSkuNo = skuOrg.getHubSkuNo();
        	String supplierNo = skuOrg.getSupplierNo();
        	//判断是否同一供应商下含有多个相同的hubSkuNo
        	if(checkIsExistMulHubSkuNo(hubSkuNo,supplierNo)){
        		continue;
        	}
            SendToScmTask task = new  SendToScmTask( skuSupplierMappingGateWay, skuOrg, restTemplate,
                     apiAddressProperties, spSpuInfo,  spSpuExtendInfo);
            executor.execute(task);
        }
    }

    private boolean checkIsExistMulHubSkuNo(String hubSkuNo, String supplierNo) {
    	if(hubSkuNo!=null&&supplierNo!=null){
    		HubSkuSupplierMappingCriteriaDto criteria = new HubSkuSupplierMappingCriteriaDto();
    		criteria.createCriteria().andSupplierNoEqualTo(supplierNo).andSkuNoEqualTo(hubSkuNo);
    		List<HubSkuSupplierMappingDto> list = skuSupplierMappingGateWay.selectByCriteria(criteria);
    		if(list!=null&&list.size()>1){
    			HubSkuSupplierMappingWithCriteriaDto criteriaDto = new HubSkuSupplierMappingWithCriteriaDto();
        		criteria.createCriteria().andSupplierSelectStateNotEqualTo((byte)SupplierSelectState.SELECTED.getIndex());
        		criteriaDto.setCriteria(criteria);
        		
        		HubSkuSupplierMappingDto mapp = new HubSkuSupplierMappingDto();
        		mapp.setSupplierSelectState((byte)6);
        		mapp.setUpdateTime(new Date());
        		mapp.setUpdateUser("chenxu");
        		mapp.setMemo("同一供应商下含有多个相同的hubSkuNo");
        		criteriaDto.setHubSkuSupplierMapping(mapp);
        		skuSupplierMappingGateWay.updateByCriteriaSelective(criteriaDto);
        		return true;
    		}
    	}
    	return false;
	}

	private List<ApiSkuOrgDom>  getExistSku(String supplierId,List<ApiSkuOrgDom> skuOrgDoms,Map<String,SopSkuDto> existSopSkuMap) throws JsonProcessingException {
        SopSkuQueryDto queryDto = new SopSkuQueryDto();
        queryDto.setSopUserNo(supplierId);
        List<String> supplierSkuNoList = new ArrayList<>();
        for(ApiSkuOrgDom apiSkuOrgDom:skuOrgDoms){
            //因为拉取后 存在的要改成其它的状态 所以 没有可以在推送前就查询 (咱不开启)
           // if(apiSkuOrgDom.isRetry()){

                supplierSkuNoList.add(apiSkuOrgDom.getSupplierSkuNo());
           // }
        }
        queryDto.setLstSupplierSkuNo(supplierSkuNoList);

        HubResponseDto<SopSkuDto> sopSkuResponseDto = null;
        try {
            if(supplierSkuNoList.size()>0){

                sopSkuResponseDto = sopSkuService.querySpSkuNoFromScm(queryDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null!=sopSkuResponseDto){
            log.info("　get  spSku　" + objectMapper.writeValueAsString(sopSkuResponseDto) +
                    " query parameter: " + objectMapper.writeValueAsString(queryDto));
        }

        List<ApiSkuOrgDom> existApiSkuOrgDoms = new ArrayList<>();
        if(null!=sopSkuResponseDto&&sopSkuResponseDto.getIsSuccess()){
            List<SopSkuDto> sopSkuDtos =  sopSkuResponseDto.getResDatas();
            if(null!=sopSkuDtos&&sopSkuDtos.size()>0){
               for( SopSkuDto  sopSkuDto:sopSkuDtos ){
                   existSopSkuMap.put(sopSkuDto.getSupplierSkuNo(),sopSkuDto);
               }
               for(int i=0 ;i<skuOrgDoms.size();i++){
                   ApiSkuOrgDom skuOrgDom = skuOrgDoms.get(i);
                   if(existSopSkuMap.containsKey(skuOrgDom.getSupplierSkuNo())){
                       existApiSkuOrgDoms.add(skuOrgDom);
                       skuOrgDoms.remove(i);
                       i--;
                   }
               }
            }
        }
        return existApiSkuOrgDoms;
    }

    private void setScmSkuValue(List<HubProductIdDto> skus, SpProductOrgInfoEntity spSpuInfo, HubSpuDto hubSpuDto, Map<String, Map<String, ApiSkuOrgDom>> supplierSizeMap) {
        Long skuId;
        Long supplierMappingId;
        for(HubProductIdDto idDto :skus){
            skuId = idDto.getId();
            HubSkuDto hubSkuDto = hubSkuGateWay.selectByPrimaryKey(skuId);

            //设置spu中的尺码类型
            setSpuSizeType(spSpuInfo,hubSkuDto.getSkuSizeType());

            List<HubProductIdDto> supplierSkuMapping =  idDto.getSubProduct();
            for(HubProductIdDto supplierIdDto :supplierSkuMapping) {

                supplierMappingId  = supplierIdDto.getId();
                HubSkuSupplierMappingDto hubSkuSupplierMappingDto = skuSupplierMappingGateWay.selectByPrimaryKey(supplierMappingId);
                if(null!=hubSkuSupplierMappingDto){
                    //组装sku并返回
                    ApiSkuOrgDom apiSkuOrgDom = setScmSku(hubSpuDto,hubSkuDto,spSpuInfo, hubSkuSupplierMappingDto);

                    //以供货商为维度
                    if(supplierSizeMap.containsKey(hubSkuSupplierMappingDto.getSupplierId())){ //相同供货商 肯定不同尺码 直接赋值
                        supplierSizeMap.get(hubSkuSupplierMappingDto.getSupplierId()).put(hubSkuDto.getSkuSize(),apiSkuOrgDom);
                    }else{
                        Map<String,ApiSkuOrgDom> supplierApiSkuMap = new HashMap<>();
                        supplierApiSkuMap.put(hubSkuDto.getSkuSize(),apiSkuOrgDom);
                        supplierSizeMap.put(hubSkuSupplierMappingDto.getSupplierId(),supplierApiSkuMap);
                    }
                }

            }
        }
    }

    @SuppressWarnings("unused")
	private HubResponseDto<String> sendToScm(HubProductDto productDto) throws JsonProcessingException {
        HttpEntity<HubProductDto> requestEntity = new HttpEntity<HubProductDto>(productDto);
        ObjectMapper mapper = new ObjectMapper();
        log.info("send scm parameter: " + mapper.writeValueAsString(productDto));

        ResponseEntity<HubResponseDto<String>> entity = restTemplate.exchange(apiAddressProperties.getGmsAddProductUrl(), HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<HubResponseDto<String>>() {
                });
        return entity.getBody();
    }

    private void setSpuSizeType(SpProductOrgInfoEntity spSpuInfo,String sizeType) {
        spSpuInfo.setSkuDyaAttr(1);
        if(StringUtils.isNotBlank(sizeType)) {
            if (sizeType.equals(GlobalConstant.REDIS_HUB_MEASURE_SIGN_KEY)) {
                spSpuInfo.setSkuDyaAttr(0);
            }
        }
    }

    private ApiSkuOrgDom setScmSku(HubSpuDto hubSpuDto,HubSkuDto hubSkuDto,SpProductOrgInfoEntity spSpuInfo, HubSkuSupplierMappingDto hubSkuSupplierMappingDto) {
        ApiSkuOrgDom  skuOrgDom =new ApiSkuOrgDom();
        skuOrgDom.setProductOrgInfoId(0L);
        skuOrgDom.setSkuOrgInfoId(0L);
        skuOrgDom.setSkuOrgName(hubSpuDto.getSpuName());
        skuOrgDom.setBarCode(null==hubSkuSupplierMappingDto.getBarcode()?"":hubSkuSupplierMappingDto.getBarcode());
//        log.info("barcode ="+ skuOrgDom.getBarCode());
        skuOrgDom.setSupplierSkuNo(hubSkuSupplierMappingDto.getSupplierSkuNo());
        skuOrgDom.setSkuNo("");
        skuOrgDom.setSupplierNo(hubSkuSupplierMappingDto.getSupplierNo());

        HubSkuPendingDto hubSkuPendingDto = getHubSkuPendingBySupplierIdAndSuppierSkuNo(hubSkuSupplierMappingDto.getSupplierId(), hubSkuSupplierMappingDto.getSupplierSkuNo());
        if(null!=hubSkuPendingDto){
//            skuOrgDom.setSkuOrgName("");
            spSpuInfo.setProductMarketPrice(hubSkuPendingDto.getMarketPrice());
            spSpuInfo.setMarketPriceCurreny(hubSkuPendingDto.getMarketPriceCurrencyorg());
            //TODO 如果没有 填默认值
            skuOrgDom.setMarketPrice(hubSkuPendingDto.getMarketPrice());
            skuOrgDom.setMarketPriceUnit(hubSkuPendingDto.getMarketPriceCurrencyorg());

            skuOrgDom.setScreenSize(hubSkuPendingDto.getScreenSize());
            List<String> sizeList = new ArrayList<>();
            if(StringUtils.isNotBlank(hubSkuDto.getSkuSizeType())) {
                if (hubSkuDto.getSkuSizeType().equals(GlobalConstant.REDIS_HUB_MEASURE_SIGN_KEY)) {
                	   sizeList.add(hubSkuDto.getSkuSize());
                }else{
                	   sizeList.add(hubSkuDto.getSkuSizeType()+":"+hubSkuDto.getSkuSize());
                }
            }
//            sizeList.add(hubSkuPendingDto.getHubSkuSize());
            skuOrgDom.setProductSize(sizeList);
        }

        skuOrgDom.setColourScheme("");
        skuOrgDom.setMemo("");
        List<String> colorList = new ArrayList<>();
        colorList.add(hubSpuDto.getHubColor());
        skuOrgDom.setProColor(colorList);

        List<String> materialList = new ArrayList<>();
        materialList.add(hubSpuDto.getMaterial());
        skuOrgDom.setMaterialList(materialList);

        List<PlaceOrigin> originList = new ArrayList<>();
        PlaceOrigin placeOrigin =new PlaceOrigin();
        placeOrigin.setPlaceOriginId(0);
        placeOrigin.setPlaceOriginValue(hubSpuDto.getOrigin());
        originList.add(placeOrigin);
        skuOrgDom.setPlaceOriginList(originList);
        skuOrgDom.setSkuOrginalFromId(hubSkuSupplierMappingDto.getSkuSupplierMappingId().toString());
        if(null!=hubSkuSupplierMappingDto.getRetryNum()){
             skuOrgDom.setRetry(true);
        }else{
            skuOrgDom.setRetry(false);
        }
        skuOrgDom.setHubSkuNo(hubSkuDto.getSkuNo());
        return skuOrgDom;

    }

    private void setScmSpuExtendProperty(ApiProductOrgExtendDom spSpuExtendInfo, HubSpuDto hubSpuDto) {
        spSpuExtendInfo.setMarketTime(hubSpuDto.getMarketTime());
        spSpuExtendInfo.setMarketSeason(hubSpuDto.getSeason());
        spSpuExtendInfo.setHeigth("");
        spSpuExtendInfo.setHeigthUnit("");
        spSpuExtendInfo.setPackingList("");
        spSpuExtendInfo.setShelfLife("");
        spSpuExtendInfo.setWidth("");
        spSpuExtendInfo.setWidthUnit("");
        spSpuExtendInfo.setWeight("");
        spSpuExtendInfo.setWeightUnit("");
        spSpuExtendInfo.setFragile("");
        spSpuExtendInfo.setLength("");
        spSpuExtendInfo.setLengthUnit("");
        spSpuExtendInfo.setUsefulTime("");
        spSpuExtendInfo.setAirTransport("");
        spSpuExtendInfo.setProductOrgInfoId(0L);
    }

    /**
     * 大部分的的long 型按固定要求填写的
     * @param spSpuInfo
     * @param hubSpuDto
     */
    private void setScmSpu(SpProductOrgInfoEntity spSpuInfo, HubSpuDto hubSpuDto) {
        spSpuInfo.setProductOrgInfoId(0L);
        spSpuInfo.setProductOriginalName(null==hubSpuDto.getSpuName()?"":hubSpuDto.getSpuName());
        spSpuInfo.setProductOriginalModel(hubSpuDto.getSpuModel());
        spSpuInfo.setProductOriginalUnit( 3);
        spSpuInfo.setCategoryOriginalNo(hubSpuDto.getCategoryNo());
        spSpuInfo.setBrandOriginalNo(hubSpuDto.getBrandNo());
        spSpuInfo.setProductOriginalFromId(hubSpuDto.getSpuId().toString());
        spSpuInfo.setProductOriginalFrom(6);
        spSpuInfo.setIsVCode(0);
        if("男士".equals(hubSpuDto.getGender())){
            spSpuInfo.setProductOriginalSex(ScmGenderType.MAN.getIndex());
        }else  if("女士".equals(hubSpuDto.getGender())){
            spSpuInfo.setProductOriginalSex(ScmGenderType.WOMAN.getIndex());
        }else  if("中性".equals(hubSpuDto.getGender())||"男童".equals(hubSpuDto.getGender())||"女童".equals(hubSpuDto.getGender())){
            spSpuInfo.setProductOriginalSex(ScmGenderType.WOMAN.getIndex());
        }else{
            spSpuInfo.setProductOriginalSex(ScmGenderType.UNKNOWN.getIndex());
        }
        spSpuInfo.setIsSupportCod(2);
        spSpuInfo.setIsSupportReturn(2);
        spSpuInfo.setIsSupportChange(2);
        spSpuInfo.setSizeTmpNo("");
        spSpuInfo.setDepartmentNo("");
        spSpuInfo.setUserGroupNo("");
        spSpuInfo.setSkuDyaAttr(1);
        spSpuInfo.setStaffNo("");
        spSpuInfo.setMerchant("");
       //TODO 市场价
        spSpuInfo.setProductType(1);
        spSpuInfo.setAuditUserName("");
        spSpuInfo.setAuditTime("1900-01-01");
        spSpuInfo.setMemo("");
        spSpuInfo.setExStandard("");
        spSpuInfo.setSecurityCategory("");
        //TODO 币种
        spSpuInfo.setSaleType(0);
    }


    public HubSkuPendingDto getHubSkuPendingBySupplierIdAndSuppierSkuNo(String supplierId,String supplierSkuNo){
        HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
        List<HubSkuPendingDto> hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            return hubSkuPendingDtos.get(0);
        }else{
            return null;
        }

    }


    private void updateSkuMappingStatus(Long id,SupplierSelectState status,String reason){
        HubSkuSupplierMappingDto skuSupplierMapping = new HubSkuSupplierMappingDto();
        skuSupplierMapping.setSkuSupplierMappingId(id);
        skuSupplierMapping.setSupplierSelectState(Integer.valueOf(status.getIndex()).byteValue());
        skuSupplierMapping.setUpdateTime(new Date());
        skuSupplierMapping.setMemo(reason);

        skuSupplierMappingGateWay.updateByPrimaryKeySelective(skuSupplierMapping);
    }

    private void updateSkuPendingStatus(SopSkuDto sopSkuDto){
        HubSkuPendingDto skuPendingDto = new HubSkuPendingDto();
        if(StringUtils.isBlank(sopSkuDto.getSkuNo())){
        }else{
            skuPendingDto.setSpSkuNo(sopSkuDto.getSkuNo());
        }

        skuPendingDto.setUpdateTime(new Date());

        HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
        criteria.createCriteria().andSupplierIdEqualTo(sopSkuDto.getSopUserNo().toString()).andSupplierSkuNoEqualTo(sopSkuDto.getSupplierSkuNo());
        HubSkuPendingWithCriteriaDto skuPendingWithCriteriaDto = new HubSkuPendingWithCriteriaDto(skuPendingDto,criteria);
        skuPendingGateWay.updateByCriteriaSelective(skuPendingWithCriteriaDto);
    }


    private void updateHubSkuSpSkuNo(String hubSkuNo,String spSkuNo) {

            HubSkuDto hubSku = new HubSkuDto();
            hubSku.setSpSkuNo(spSkuNo);
            HubSkuCriteriaDto skuCriteria = new HubSkuCriteriaDto();
            skuCriteria.createCriteria().andSkuNoEqualTo(hubSkuNo);
            HubSkuWithCriteriaDto criteriaWithSku = new HubSkuWithCriteriaDto(hubSku,skuCriteria);
            hubSkuGateWay.updateByCriteriaSelective(criteriaWithSku);

    }

}

class SendToScmTask implements Runnable{

    HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;
    ApiSkuOrgDom skuOrg;
    RestTemplate restTemplate;
    ApiAddressProperties apiAddressProperties;
    SpProductOrgInfoEntity spSpuInfo;
    ApiProductOrgExtendDom spSpuExtendInfo;

    SendToScmTask(HubSkuSupplierMappingGateWay skuSupplierMappingGateWay,ApiSkuOrgDom skuOrg,RestTemplate restTemplate,
                  ApiAddressProperties apiAddressProperties,SpProductOrgInfoEntity spSpuInfo, ApiProductOrgExtendDom spSpuExtendInfo){
        this.skuSupplierMappingGateWay = skuSupplierMappingGateWay;
        this.skuOrg = skuOrg;
        this.restTemplate = restTemplate;
        this.apiAddressProperties = apiAddressProperties;
        this.spSpuInfo = spSpuInfo;
        this.spSpuExtendInfo = spSpuExtendInfo;
    }

    @Override
    public void run() {
        HubProductDto productDto = new HubProductDto();
        productDto.setProductOrgInfo(spSpuInfo);
        productDto.setProductOrgInfoExtend(spSpuExtendInfo);
        List<ApiSkuOrgDom> sendSkuList = new ArrayList<>();
        sendSkuList.add(skuOrg);
        productDto.setSkuList(sendSkuList);
        HubResponseDto<String> responseDto = null;
        try {
        	 Log.info("推送scm参数:"+productDto.toString());
            responseDto = sendToScm(productDto);
            Log.info("推送scm返回结果:"+responseDto.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==responseDto){
            updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()),SupplierSelectState.SELECTE_FAIL,"推送SCM时失败");
        }else{

            if(responseDto.getIsSuccess()){  //创建成功
                updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()), SupplierSelectState.WAIT_SCM_AUDIT,"");

            }else{ //创建失败
                updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()),SupplierSelectState.SELECTE_FAIL,responseDto.getResMsg());

            }
        }
    }
    private void updateSkuMappingStatus(Long id,SupplierSelectState status,String reason){
        HubSkuSupplierMappingDto skuSupplierMapping = new HubSkuSupplierMappingDto();
        skuSupplierMapping.setSkuSupplierMappingId(id);
        skuSupplierMapping.setSupplierSelectState(Integer.valueOf(status.getIndex()).byteValue());
        skuSupplierMapping.setUpdateTime(new Date());
        skuSupplierMapping.setMemo(reason);

        skuSupplierMappingGateWay.updateByPrimaryKeySelective(skuSupplierMapping);
    }

    @SuppressWarnings("unused")
	private HubResponseDto<String> sendToScm(HubProductDto productDto) throws JsonProcessingException {
        HttpEntity<HubProductDto> requestEntity = new HttpEntity<HubProductDto>(productDto);
        ObjectMapper mapper = new ObjectMapper();
//        log.info("send scm parameter: " + mapper.writeValueAsString(productDto));

        ResponseEntity<HubResponseDto<String>> entity = restTemplate.exchange(apiAddressProperties.getGmsAddProductUrl(), HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<HubResponseDto<String>>() {
                });
        return entity.getBody();
    }

}
