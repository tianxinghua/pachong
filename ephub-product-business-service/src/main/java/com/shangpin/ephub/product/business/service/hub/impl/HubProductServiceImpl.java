package com.shangpin.ephub.product.business.service.hub.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.dto.HubResponseDto;
import com.shangpin.ephub.product.business.common.enumeration.ScmGenderType;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.service.hub.HubProductService;
import com.shangpin.ephub.product.business.service.hub.dto.ApiProductOrgExtendDom;
import com.shangpin.ephub.product.business.service.hub.dto.ApiSkuOrgDom;
import com.shangpin.ephub.product.business.service.hub.dto.HubProductDto;
import com.shangpin.ephub.product.business.service.hub.dto.HubProductIdDto;
import com.shangpin.ephub.product.business.service.hub.dto.PlaceOrigin;
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

    @Override
    public void sendHubProuctToScm(HubProductIdDto hubProductIdDto) throws Exception {
        Long  spuId = hubProductIdDto.getId();
        List<HubProductIdDto> skus = hubProductIdDto.getSubProduct();
        Long skuId = 0L;
        Long supplierMappingId = 0L;
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
            for(HubProductIdDto idDto :skus){
                skuId = idDto.getId();
                HubSkuDto hubSkuDto = hubSkuGateWay.selectByPrimaryKey(skuId);
                List<HubProductIdDto> supplierSkuMapping =  idDto.getSubProduct();
                for(HubProductIdDto supplierIdDto :supplierSkuMapping) {

                    supplierMappingId  = supplierIdDto.getId();
                    HubSkuSupplierMappingDto hubSkuSupplierMappingDto = skuSupplierMappingGateWay.selectByPrimaryKey(supplierMappingId);
                    if(null!=hubSkuSupplierMappingDto){
                        //组装sku并返回
                        ApiSkuOrgDom apiSkuOrgDom = setScmSku(hubSpuDto,hubSkuDto,spSpuInfo, hubSkuSupplierMappingDto);

                        //以供货商为维度
                        if(supplierSizeMap.containsKey(hubSkuSupplierMappingDto.getSupplierNo())){ //相同供货商 肯定不同尺码 直接赋值
                            supplierSizeMap.get(hubSkuSupplierMappingDto.getSupplierNo()).put(hubSkuDto.getSkuSize(),apiSkuOrgDom);
                        }else{
                            Map<String,ApiSkuOrgDom> supplierApiSkuMap = new HashMap<>();
                            supplierApiSkuMap.put(hubSkuDto.getSkuSize(),apiSkuOrgDom);
                            supplierSizeMap.put(hubSkuSupplierMappingDto.getSupplierNo(),supplierApiSkuMap);
                        }
                    }

                }
            }
            Set<String> supplierNoSet = supplierSizeMap.keySet();
            if(null!=supplierNoSet){//以供货商的维度推送数据
                for(String supplierNo :supplierNoSet){
                    Map<String, ApiSkuOrgDom> apiSkuOrgDomMap = supplierSizeMap.get(supplierNo);

                    List<ApiSkuOrgDom> skuOrgDoms = new ArrayList<>();
                    Set<Map.Entry<String, ApiSkuOrgDom>> entries = apiSkuOrgDomMap.entrySet();
                    for(Map.Entry<String,ApiSkuOrgDom> entry:entries){
                        skuOrgDoms.add(entry.getValue());
                    }
                    HubProductDto productDto = new HubProductDto();
                    productDto.setProductOrgInfo(spSpuInfo);
                    productDto.setProductOrgInfoExtend(spSpuExtendInfo);
                    productDto.setSkuList(skuOrgDoms);
                    //推送
                    HttpEntity<HubProductDto> requestEntity = new HttpEntity<HubProductDto>(productDto);
                    ResponseEntity<HubResponseDto<String>> entity = restTemplate.exchange(apiAddressProperties.getGmsAddProductUrl(), HttpMethod.POST,
                            requestEntity, new ParameterizedTypeReference<HubResponseDto<String>>() {
                            });
                    HubResponseDto<String> responseDto = entity.getBody();
                    if(responseDto.getIsSuccess()){  //创建成功
                         for(ApiSkuOrgDom skuOrg:skuOrgDoms){
                             updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()),SupplierSelectState.WAIT_SCM_AUDIT,"");
                         }
                    }else{ //创建失败
                        for(ApiSkuOrgDom skuOrg:skuOrgDoms){
                            updateSkuMappingStatus(Long.valueOf(skuOrg.getSkuOrginalFromId()),SupplierSelectState.SELECTE_FAIL,responseDto.getResMsg());
                        }
                    }
                }
            }

        }


    }

    private ApiSkuOrgDom setScmSku(HubSpuDto hubSpuDto,HubSkuDto hubSkuDto,SpProductOrgInfoEntity spSpuInfo, HubSkuSupplierMappingDto hubSkuSupplierMappingDto) {
        ApiSkuOrgDom  skuOrgDom =new ApiSkuOrgDom();
        skuOrgDom.setProductOrgInfoId(0L);
        skuOrgDom.setSkuOrgInfoId(0L);
        skuOrgDom.setSkuOrgName(hubSpuDto.getSpuName());
        skuOrgDom.setBarCode(null==hubSkuSupplierMappingDto.getBarcode()?"":hubSkuSupplierMappingDto.getBarcode());
        log.info("barcode ="+ skuOrgDom.getBarCode());
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
            sizeList.add(hubSkuPendingDto.getHubSkuSize());
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

        skuSupplierMapping.setMemo(reason);

        skuSupplierMappingGateWay.updateByPrimaryKeySelective(skuSupplierMapping);
    }
}
