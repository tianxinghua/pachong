package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioPendingProductVo;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioPendingProducts;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/6/8.
 */
public class StudioServiceImpl implements IStudioService {

    HubSlotSpuSupplierGateway hubSlotSpuSupplierGateway;
    HubSupplierSpuGateWay hubSupplierSpuGateWay;


    public StudioPendingProducts getPendingProductList(StudioQueryDto queryDto){

        StudioPendingProducts products = new StudioPendingProducts();

        HubSlotSpuSupplierCriteriaDto cdto = new HubSlotSpuSupplierCriteriaDto();
        cdto.createCriteria().andSlotSpuSupplierIdEqualTo(queryDto.getSupplierId());
        //cdto.createCriteria().andStateEqualTo(queryDto.getStatus());
        int total = hubSlotSpuSupplierGateway.countByCriteria(cdto);
        if(total>0) {
            List<HubSlotSpuSupplierDto> results = hubSlotSpuSupplierGateway.selectByCriteria(cdto);

            List<StudioPendingProductVo> hubProducts = productList(results);
            products.setHubProducts(hubProducts);
        }
        products.setTotal(total);

        return products;
    }

    private  List<StudioPendingProductVo> productList(List<HubSlotSpuSupplierDto> results){
        List<StudioPendingProductVo> hubProducts = new ArrayList<StudioPendingProductVo>();

        if (null != results && results.size() > 0) {
            List<Long> filtered = results.stream().map(n-> n.getSpuPendingId()).collect(Collectors.toList());
            HubSupplierSpuCriteriaDto dto = new HubSupplierSpuCriteriaDto();
            dto.createCriteria().andSupplierSpuIdIn(filtered);
            List<HubSupplierSpuDto> spuDtoList =  hubSupplierSpuGateWay.selectByCriteria(dto);

            for (HubSlotSpuSupplierDto x : results){

                Optional<HubSupplierSpuDto> spuDto = spuDtoList.stream().filter(spu -> spu.getSupplierSpuId() == x.getSupplierSpuId()).findFirst();

                StudioPendingProductVo product = new StudioPendingProductVo();

                product.setSupplierId(x.getSupplierId());
                product.setSlotNo(x.getSlotNo());
                product.setSlotSpuId(x.getSlotSpuId());
                if(spuDto.isPresent()){
                    HubSupplierSpuDto spu = spuDto.get();
                    product.setBrandName(spu.getSupplierBrandname());
                    product.setBrandNo(spu.getSupplierBrandno());
                    product.setCategoryNo(spu.getSupplierCategoryno());
                    product.setCategoryName(spu.getSupplierCategoryname());
                    product.setSupplierOrigin(spu.getSupplierOrigin());
                    product.setProductName(spu.getSupplierSpuName());
                }
                product.setRepeatMarker(x.getRepeatMarker());
                product.setVersion(x.getVersion());
                hubProducts.add(product);
            }
        }
        return hubProducts;
    }



    public HubResponse<?> getSupplierSlotList(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> getSlotInfo(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> addProductIntoSlot(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> delProductFromSlot(StudioQueryDto queryDto){
        return null;
    }
    public HubResponse<?> checkProductAndSendSlot(StudioQueryDto queryDto){
        return null;
    }
}
