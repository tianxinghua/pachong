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
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.*;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/6/8.
 */
@Service
@Slf4j
public class StudioServiceImpl implements IStudioService {

    @Autowired
    HubSlotSpuSupplierGateway hubSlotSpuSupplierGateway;
    @Autowired
    HubSupplierSpuGateWay hubSupplierSpuGateWay;

    @Autowired
    StudioSlotGateWay studioSlotGateWay;

    @Autowired
    HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    StudioDicSlotGateWay studioDicSlotGateWay;

    @Autowired
    StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
    /*
        获取待拍照商品列表
    * */
    public StudioPendingProductVo getPendingProductList(StudioQueryDto queryDto){

        StudioPendingProductVo products = new StudioPendingProductVo();
        HubSlotSpuSupplierCriteriaDto cdto = new HubSlotSpuSupplierCriteriaDto();
        cdto.createCriteria().andSupplierIdEqualTo(queryDto.getSupplierId());
        int total = hubSlotSpuSupplierGateway.countByCriteria(cdto);
        if(total>0) {
            List<HubSlotSpuSupplierDto> results = hubSlotSpuSupplierGateway.selectByCriteria(cdto);

            List<StudioPendingProduct> hubProducts = productList(results);
            products.setHubProducts(hubProducts);
        }
        products.setTotal(total);

        return products;
    }

    /*
    * 组织待拍照商品数据
    *
    * */
    private  List<StudioPendingProduct> productList(List<HubSlotSpuSupplierDto> results){
        List<StudioPendingProduct> hubProducts = new ArrayList<StudioPendingProduct>();

        if (null != results && results.size() > 0) {
            List<Long> filteredSpuId = results.stream().map(HubSlotSpuSupplierDto :: getSupplierSpuId).distinct().collect(Collectors.toList());

            List<Long> filteredPendingId = results.stream().map(HubSlotSpuSupplierDto :: getSpuPendingId).distinct().collect(Collectors.toList());
            HubSupplierSpuCriteriaDto dto = new HubSupplierSpuCriteriaDto();
            dto.createCriteria().andSupplierSpuIdIn(filteredSpuId);
            List<HubSupplierSpuDto> spuDtoList =  hubSupplierSpuGateWay.selectByCriteria(dto);

            HubSpuPendingCriteriaDto pendDto = new HubSpuPendingCriteriaDto();
            pendDto.createCriteria().andSpuPendingIdIn(filteredPendingId);
            List<HubSpuPendingDto>  pendingDtoList =  hubSpuPendingGateWay.selectByCriteria(pendDto);

            for (HubSlotSpuSupplierDto x : results){

                Optional<HubSupplierSpuDto> spuDto = spuDtoList.stream().filter(spu -> spu.getSupplierSpuId() .equals(x.getSupplierSpuId()) ).findFirst();
                Optional<HubSpuPendingDto> pendingDto = pendingDtoList.stream().filter(spu -> spu.getSpuPendingId() .equals(x.getSpuPendingId()) ).findFirst();
                StudioPendingProduct product = new StudioPendingProduct();
                product.setSlotSpuSupplierId(x.getSlotSpuSupplierId());
                product.setSupplierId(x.getSupplierId());
                product.setSupplierNo(x.getSupplierNo());
                product.setSpuPendingId(x.getSpuPendingId());
                product.setSupplierSpuId(x.getSupplierSpuId());
                product.setSlotNo(x.getSlotNo());
                product.setSlotSpuId(x.getSlotSpuId());
                product.setState(x.getState());
                product.setCreateTime(x.getCreateTime());
                product.setSupplierOperateSign(x.getSupplierOperateSign());
                spuDto.ifPresent((spu)->{
                    product.setBrandName(spu.getSupplierBrandname());
                    product.setCategoryName(spu.getSupplierCategoryname());
                    product.setSupplierOrigin(spu.getSupplierOrigin());
                    product.setProductName(spu.getSupplierSpuName());
                    product.setProductDesc(spu.getSupplierSpuDesc());
                });
                spuDto.ifPresent((pending)->{
                    product.setBrandNo(pending.getSupplierBrandno());
                    product.setCategoryNo(pending.getSupplierCategoryno());
                });
                product.setRepeatMarker(x.getRepeatMarker());
                product.setVersion(x.getVersion());
                hubProducts.add(product);
            }
        }
        return hubProducts;
    }


    /*
    * 获取供货商已申请的商品列表
    * */
    public SlotsVo getSupplierSlotList(StudioQueryDto queryDto){

        SlotsVo products = new SlotsVo();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.createCriteria().andApplySupplierIdEqualTo(queryDto.getSupplierId());
        int total = studioSlotGateWay.countByCriteria(cdto);
        if(total>0) {
            List<StudioSlotDto> results = studioSlotGateWay.selectByCriteria(cdto);
            products.setSlotInfoList(SlotList(results));
        }
        products.setTotal(total);
        return products;
    }
    private List<SlotInfo> SlotList(List<StudioSlotDto> results ){
        List<SlotInfo>  list = new ArrayList<SlotInfo>();

        if (null != results && results.size() > 0) {
            //获取slot最大值，最小值
            List<Long> filteredStudioIds = results.stream().map(StudioSlotDto::getStudioId).distinct().collect(Collectors.toList());
            List<StudioDicSlotDto> studioDicSlotDtos = getStudioDicSlotDtos(filteredStudioIds);

            //获取所有批次的当前包含商品数量
            List<String> filteredSlotNos = results.stream().map(StudioSlotDto::getSlotNo).distinct().collect(Collectors.toList());
            List<StudioSlotSpuSendDetailDto> studioSlotSpuSendDetailDto = getStudioSlotSpuSendDetail(filteredSlotNos);

            for (StudioSlotDto x : results){
                SlotInfo s = new SlotInfo();
                s.setStudioSlotId(x.getStudioSlotId());
                s.setSlotNo(x.getSlotNo());
                s.setApplySupplierId(x.getApplySupplierId());
                s.setArriveStatus(x.getArriveStatus());
                s.setSlotStatus(x.getSlotStatus());
                Optional<StudioDicSlotDto> StudioDicSlot = studioDicSlotDtos.stream().filter(spu -> spu.getStudioId() .equals(x.getStudioId()) ).findFirst();
                StudioDicSlot.ifPresent((studio)->{
                    s.setMaxNum(studio.getSlotNumber());
                    s.setMinNum(studio.getSlotMinNumber());
                });
                long count = studioSlotSpuSendDetailDto.stream().filter(spu -> spu.getSlotNo() .equals(x.getSlotNo()) ).count();
                s.setCountNum(count);
                list.add(s);
            }
        }

        return  list;

    }
    /*
    * 获取slot最大值，最小值
    * */
    private  List<StudioDicSlotDto> getStudioDicSlotDtos( List<Long> filteredStudioIds ){
        StudioDicSlotCriteriaDto studioDto = new StudioDicSlotCriteriaDto();
        studioDto.createCriteria().andStudioIdIn(filteredStudioIds);
        return  studioDicSlotGateWay.selectByCriteria(studioDto);
    }
    /*
    * 获取slot最大值，最小值
    * */
    private  List<StudioDicSlotDto> getStudioDicSlotDtos( Long StudioIds ){
        StudioDicSlotCriteriaDto studioDto = new StudioDicSlotCriteriaDto();
        studioDto.createCriteria().andStudioIdEqualTo(StudioIds);
        return  studioDicSlotGateWay.selectByCriteria(studioDto);
    }
    /*
    * 获取所有批次的当前包含商品
    * */
    private List<StudioSlotSpuSendDetailDto> getStudioSlotSpuSendDetail(List<String> filteredSlotNos){

        StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
        dto.createCriteria().andSlotNoIn(filteredSlotNos);
        return studioSlotSpuSendDetailGateWay.selectByCriteria(dto);
    }
    /*
  * 获取当前批次的当前包含商品
  * */
    private List<StudioSlotSpuSendDetailDto> getStudioSlotSpuSendDetail(String SlotNos){

        StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
        dto.createCriteria().andSlotNoEqualTo(SlotNos);
        return studioSlotSpuSendDetailGateWay.selectByCriteria(dto);
    }


    /*
    * 获取批次详情
    * */
    public SlotInfoExtends getSlotInfo(StudioQueryDto queryDto){
        SlotInfoExtends slot  = new SlotInfoExtends();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.createCriteria().andApplySupplierIdEqualTo(queryDto.getSupplierId());
        cdto.createCriteria().andSlotNoEqualTo(queryDto.getSlotNo());
        List<StudioSlotDto> results = studioSlotGateWay.selectByCriteria(cdto);
        if (null != results && results.size() > 0) {
            StudioSlotDto studioSlot = results.get(0);

            List<StudioDicSlotDto> studioDicSlotDto  = getStudioDicSlotDtos(studioSlot.getStudioId());
            if(studioDicSlotDto!=null && studioDicSlotDto.size()>0){
                slot.setMaxNum(studioDicSlotDto.get(0).getSlotNumber());
                slot.setMinNum(studioDicSlotDto.get(0).getSlotMinNumber());
            }
            List<SlotProduct> listProduct = SlotProductList(studioSlot.getSlotNo());
            slot.setCountNum(listProduct.size());
            slot.setSlotProductList(listProduct);
        }
        return slot;
    }


    private List<SlotProduct> SlotProductList(String SlotNos ){

        List<StudioSlotSpuSendDetailDto> results = getStudioSlotSpuSendDetail(SlotNos);
        List<SlotProduct> list = new ArrayList<SlotProduct>();
        if(null!=results && results.size()>0){
            for (StudioSlotSpuSendDetailDto item :results){
                SlotProduct p = new SlotProduct();
                p.setId(item.getStudioSlotSpuSupplierMappingId());
                p.setSupplierNo(item.getSupplierNo());
                p.setSupplierId(item.getSupplierId());
                p.setSpuPendingId(item.getSpuPendingId());
                p.setSupplierSpuId(item.getSupplierSpuId());
                p.setSlotSpuNo(item.getSlotSpuNo());
                p.setSupplierSpuName(item.getSupplierSpuName());
                p.setSupplierBrandName(item.getSupplierBrandName());
                p.setSupplierCategoryName(item.getSupplierCategoryName());
                p.setSupplierSeasonName(item.getSupplierSeasonName());
                p.setSendState(item.getSendState());
                p.setArriveState(item.getArriveState());
                p.setVersion(item.getVersion());
                list.add(p);
            }
        }



        return  list;
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
