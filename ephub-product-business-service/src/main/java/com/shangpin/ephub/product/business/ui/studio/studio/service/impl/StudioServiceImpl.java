package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.SlotSpuSupplierState;
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
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotApplyState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotSendState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotState;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;
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
    public StudioPendingProductVo getPendingProductList(String supplierId){

        StudioPendingProductVo products = new StudioPendingProductVo();
        HubSlotSpuSupplierCriteriaDto cdto = new HubSlotSpuSupplierCriteriaDto();
        cdto.createCriteria().andSupplierIdEqualTo(supplierId);
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
    public SlotsVo getSupplierSlotList(String supplierId){

        SlotsVo products = new SlotsVo();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.createCriteria().andApplySupplierIdEqualTo(supplierId);
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
    public SlotInfoExtends getSlotInfo(String supplierId ,String slotNo){
        SlotInfoExtends slot  = new SlotInfoExtends();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.createCriteria().andApplySupplierIdEqualTo(supplierId);
        cdto.createCriteria().andSlotNoEqualTo(slotNo);
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

    /*批次内商品整理*/
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


    /**
     * 添加商品
     * @param supplierId 供货商编号
     * @param slotNo   批次号
     * @param slotSSId 待拍照的SPU下挂供货商 表字段Id
     * @param createUser 操作人
     * @return HubResponse<SlotProductEditVo>
     */
    public HubResponse<SlotProductEditVo> addProductIntoSlot(String supplierId ,String slotNo,Long slotSSId,String createUser){

        HubResponse<SlotProductEditVo> response = new HubResponse<SlotProductEditVo>();
        response.setCode("0");
        SlotProductEditVo updatedVo = null;
       try{
           //验证是否slot是否可以添加
           SlotInfoExtends slotInfo  = getSlotInfo(supplierId , slotNo);
           //验证slot是否存在
           if(slotInfo==null){
               updatedVo = setErrorMsg(response,slotNo,"C1","发货单没有找到");
           }else {
                //判断是否处于可添加商品状态
               if (slotInfo.getApplyStatus() != StudioSlotApplyState.APPLYED.getIndex().byteValue() ||
                       slotInfo.getSendState() != StudioSlotSendState.WAIT_SEND.getIndex().byteValue()) {
                   updatedVo = setErrorMsg(response, slotInfo.getSlotNo(), "C2", "发货单状态不正确");
               }
               if(slotInfo.getMaxNum()<= slotInfo.getSlotProductList().size()){
                   updatedVo = setErrorMsg(response, slotInfo.getSlotNo(), "C3", "发货单已经达到最大商品数量");
               }

               HubSlotSpuSupplierDto product = hubSlotSpuSupplierGateway.selectByPrimaryKey(slotSSId);
               if(product == null){
                   updatedVo = setErrorMsg(response, slotNo, "C4", "该商品不存在");
               }else {
                   if (product.getState() == SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue()) {
                       updatedVo = setErrorMsg(response, slotNo, "C5", "该商品已经加入发货单了");
                   }
                   if (product.getState() == SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue()) {
                       updatedVo = setErrorMsg(response, slotNo, "C6", "该商品已经发货了");
                   }

                   if(updatedVo ==null) {
                       HubSupplierSpuDto supProduct = hubSupplierSpuGateWay.selectByPrimaryKey(product.getSupplierSpuId());
                       //验证成功后，进行添加
                       StudioSlotSpuSendDetailDto data = new StudioSlotSpuSendDetailDto();
                       data.setSlotNo(slotNo);
                       data.setSupplierId(supplierId);
                       data.setSupplierNo(product.getSupplierNo());
                       data.setSpuPendingId(product.getSpuPendingId());
                       data.setSupplierSpuId(product.getSupplierSpuId());
                       data.setSlotSpuNo(product.getSlotNo());
                       data.setSupplierSpuName(supProduct.getSupplierSpuName());
                       data.setSupplierSpuModel(supProduct.getSupplierSpuModel());
                       data.setSupplierBrandName(supProduct.getSupplierBrandname());
                       data.setSupplierCategoryName(supProduct.getSupplierCategoryname());
                       data.setSupplierSeasonName(supProduct.getSupplierSeasonname());
                       data.setCreateTime(new Date());
                       data.setCreateUser(createUser);
                       Long id = studioSlotSpuSendDetailGateWay.insert(data);
                       if (id > 0) {
                           HubSlotSpuSupplierDto upSlotSpu = new HubSlotSpuSupplierDto();
                           upSlotSpu.setSlotSpuSupplierId(product.getSlotSpuSupplierId());
                           upSlotSpu.setState(SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue());
                           hubSlotSpuSupplierGateway.updateByPrimaryKeySelective(upSlotSpu);
                           slotInfo.setCountNum(slotInfo.getCountNum() + 1);
                           SlotProductEditVo successVo = new SlotProductEditVo();
                           successVo.setSupplierId(supplierId);
                           successVo.setSlotNo(slotNo);
                           successVo.setSlotInfo(slotInfo);
                           response.setContent(successVo);
                       } else {
                           updatedVo = setErrorMsg(response, slotNo, "I0", "商品加入发货单失败");
                       }
                   }
               }
           }

           response.setErrorMsg(updatedVo);
       }catch (Exception ex){
           log.info("addProductIntoSlot Exception " + ex.getMessage());
           setErrorMsg(response,slotNo, "S0","发货单添加商品时,发生服务器错误");
       }
        return  response;

    }
     /**
     * 设置校验失败结果
     * @param response
     * @param slotNo
     * @param errorMsg
     */
    private SlotProductEditVo setErrorMsg(HubResponse<SlotProductEditVo> response,String slotNo,String errorMsg){
        return  setErrorMsg(response,slotNo,null,errorMsg);
    }
    private SlotProductEditVo setErrorMsg(HubResponse<SlotProductEditVo> response,String slotNo,String errorCode ,String errorMsg){
        response.setCode("1");

        SlotProductEditVo updatedVo = new SlotProductEditVo();
        //updatedVo.setSupplierId(spuPengdingId);
        updatedVo.setSlotNo(slotNo);
        updatedVo.setErrorCode(errorCode);
        updatedVo.setErrorMsg(errorMsg);
        response.setErrorMsg(updatedVo);
        return updatedVo;
    }


    /**
     * 移除发货单中的商品
     * @param supplierId
     * @param slotNo
     * @param slotSSId
     * @param createUser
     * @return
     */
    public HubResponse<SlotProductEditVo> delProductFromSlot(String supplierId ,String slotNo,Long slotSSId,String createUser){
        HubResponse<SlotProductEditVo> response = new HubResponse<SlotProductEditVo>();
        response.setCode("0");
        SlotProductEditVo updatedVo = null;
        try {

            HubSlotSpuSupplierDto product = hubSlotSpuSupplierGateway.selectByPrimaryKey(slotSSId);
           int count =   studioSlotSpuSendDetailGateWay.deleteByPrimaryKey(slotSSId);
           if(count>0){
               HubSlotSpuSupplierDto upSlotSpu = new HubSlotSpuSupplierDto();
               upSlotSpu.setSlotSpuSupplierId(product.getSlotSpuSupplierId());
               upSlotSpu.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());
               hubSlotSpuSupplierGateway.updateByPrimaryKeySelective(upSlotSpu);
           }else {
               updatedVo = setErrorMsg(response, slotNo, "D0", "商品从发货单移除失败");
           }
            response.setErrorMsg(updatedVo);
        }
        catch (Exception ex){
            log.info("delProductFromSlot Exception " + ex.getMessage());
            setErrorMsg(response,slotNo, "S0","发商品从发货单移除时,发生服务器错误");
        }
        return response;
    }



    /**
     * 验证发货单是否可以发货
     * @param supplierId
     * @param slotNo
     * @return
     */
    public HubResponse<SlotProductEditVo> checkProductAndSendSlot(String supplierId ,String slotNo){
        HubResponse<SlotProductEditVo> response = new HubResponse<SlotProductEditVo>();
        response.setCode("0");
        SlotProductEditVo updatedVo = null;
        try{
            SlotInfoExtends slotInfo  = getSlotInfo(supplierId , slotNo);
            //验证slot是否存在
            if(slotInfo==null){
                updatedVo = setErrorMsg(response,slotNo,"C1","发货单没有找到");
                response.setErrorMsg(updatedVo);
                return response;
            }else {
                if(slotInfo.getSendState() == StudioSlotSendState.SEND.getIndex().byteValue()){
                    updatedVo = setErrorMsg(response,slotNo,"C7","发货单已经发货");
                    response.setErrorMsg(updatedVo);
                    return response;
                }
                long countNm = slotInfo.getSlotProductList().size();
                if(slotInfo.getMinNum() > countNm ){
                    updatedVo = setErrorMsg(response,slotNo,"C8","发货单商品数量不足最小发货数");
                    response.setErrorMsg(updatedVo);
                    return response;
                }
                if(slotInfo.getMaxNum() < countNm ){
                    updatedVo = setErrorMsg(response,slotNo,"C9","发货单商品数量超出最大发货数");
                    response.setErrorMsg(updatedVo);
                    return response;
                }
                if(slotInfo.getMaxNum() > countNm ){
                    response.setMsg("发货单商品未满，但可以发货");
                }
                if(slotInfo.getMaxNum() == countNm ){
                    response.setMsg("发货单商品已满，可以发货");
                }
            }

        }catch (Exception ex){

            setErrorMsg(response,slotNo, "S0","发商品从发货单移除时,发生服务器错误");
        }
        return  response;
    }



}
