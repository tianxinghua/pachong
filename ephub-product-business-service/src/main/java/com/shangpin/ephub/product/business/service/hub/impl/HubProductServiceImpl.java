package com.shangpin.ephub.product.business.service.hub.impl;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.enumeration.ScmGenderType;
import com.shangpin.ephub.product.business.service.hub.HubProductService;
import com.shangpin.ephub.product.business.service.hub.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 */
@Service("hubCommonProductServiceImpl")
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
    HubSkuSupplierMappingGateWay supplierMappingGateWay;

    @Override
    public void sendHubProuctToScm(HubProductIdDto hubProductIdDto) throws Exception {
        Long  spuId = hubProductIdDto.getId();
        List<HubProductIdDto> skus = hubProductIdDto.getSubProduct();
        Long skuId = 0L;
        Long supplierId = 0L;
        //推送对象初始化
        HubProductDto productDto = new HubProductDto();
        SpProductOrgInfoEntity spSpuInfo = new SpProductOrgInfoEntity(); //SCM需要的SPU对象
        ApiProductOrgExtendDom spSpuExtendInfo = new ApiProductOrgExtendDom();//  扩展对象
        productDto.setProductOrgInfo(spSpuInfo);
        productDto.setProductOrgInfoExtend(spSpuExtendInfo);
        List<ApiSkuOrgDom> skuOrgDoms = new ArrayList<>();
        productDto.setSkuList(skuOrgDoms);


        //获取HUBSPU
        HubSpuDto hubSpuDto = hubSpuGateWay.selectByPrimaryKey(spuId);
        if(null!=hubSpuDto){
            //scmspu 对象赋值
            setScmSpu(spSpuInfo, hubSpuDto);
            //scmspu 扩展对象赋值
            setScmSpuExtendProperty(spSpuExtendInfo, hubSpuDto);

            for(HubProductIdDto idDto :skus){
                skuId = idDto.getId();
                HubSkuDto hubSkuDto = hubSkuGateWay.selectByPrimaryKey(skuId);
                List<HubProductIdDto> supplierSkuMapping =  idDto.getSubProduct();
                for(HubProductIdDto supplierIdDto :supplierSkuMapping) {

                    supplierId  = supplierIdDto.getId();
                    HubSkuSupplierMappingDto hubSkuSupplierMappingDto = supplierMappingGateWay.selectByPrimaryKey(supplierId);
                    ApiSkuOrgDom  skuOrgDom =new ApiSkuOrgDom();
                    skuOrgDom.setSkuOrgName("");
                    skuOrgDom.setBarCode(hubSkuSupplierMappingDto.getBarcode());
                    skuOrgDom.setSupplierSkuNo(hubSkuSupplierMappingDto.getSupplierSkuNo());
                    skuOrgDom.setSupplierNo(hubSkuSupplierMappingDto.getSupplierNo());
                    skuOrgDoms.add(skuOrgDom);

                }
            }
        }


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
        spSpuInfo.setProductOriginalName(hubSpuDto.getSpuName());
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
}
