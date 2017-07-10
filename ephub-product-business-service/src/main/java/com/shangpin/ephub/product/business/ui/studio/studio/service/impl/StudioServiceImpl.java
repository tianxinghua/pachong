package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.SlotSpuSupplierState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.dto.SlotSpuSupplierExtendQueryDto;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.gateway.HubSlotSpuSupplierExtendGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.result.HubSlotSpuSupplierExtend;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.client.data.studio.dic.dto.*;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicCategoryGateWay;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicSlotGateWay;
import com.shangpin.ephub.client.data.studio.enumeration.*;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackDto;
import com.shangpin.ephub.client.data.studio.slot.logistic.gateway.StudioSlotLogistictTrackGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.common.dto.CommonResult;
import com.shangpin.ephub.product.business.common.exception.EphubException;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuSendDetailCheckDto;
import com.shangpin.ephub.product.business.service.studio.slotspusend.SlotSpuSendDetailService;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.StudioSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.*;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/6/8.
 */
@Service
@Slf4j
public class StudioServiceImpl implements IStudioService {

    @Autowired
    StudioGateWay studioGateWay;

    @Autowired
    HubSlotSpuSupplierGateway hubSlotSpuSupplierGateway;
    @Autowired
    HubSlotSpuSupplierExtendGateWay hubSlotSpuSupplierExtendGateWay;
    @Autowired
    HubSupplierSpuGateWay hubSupplierSpuGateWay;

    @Autowired
    StudioSlotGateWay studioSlotGateWay;

    @Autowired
    HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    StudioDicSlotGateWay studioDicSlotGateWay;

    @Autowired
    StudioDicCategoryGateWay studioDicCategoryGateWay;

    @Autowired
    StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;

    @Autowired
    StudioSlotLogistictTrackGateWay studioSlotLogistictTrackGateWay;


    @Autowired
    HubSlotSpuSupplierService hubSlotSpuSupplierService;
    @Autowired
    SlotSpuSendDetailService slotSpuSendDetailService;

    HashMap<String, String > categoryMap = new HashMap<String, String>(){{
        put("cloth", "A01");
        put("shoes", "A02");
        put("bags", "A03");
        put("accessories", "A05");
    }};
    SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");
    /*
        获取待拍照商品列表
    * */
    public StudioPendingProductVo getPendingProductList(SlotSpuSupplierQueryDto queryDto) {
        StudioPendingProductVo products = new StudioPendingProductVo();
        try {

            SlotSpuSupplierExtendQueryDto cdto = setQueryDto(queryDto);
            int total = hubSlotSpuSupplierExtendGateWay.countByQuery(cdto);
            if (total > 0) {
                List<HubSlotSpuSupplierExtend> results = hubSlotSpuSupplierExtendGateWay.selectByQuery(cdto);

                List<StudioPendingProduct> hubProducts = productList(results);
                products.setHubProducts(hubProducts);
            }
            products.setTotal(total);


        } catch (Exception ex) {
            log.error("getPendingProductList err" + ex.getMessage());
            products.setTotal(0);
        }
        return products;
    }
    private SlotSpuSupplierExtendQueryDto setQueryDto(SlotSpuSupplierQueryDto queryDto) throws ParseException {
        SlotSpuSupplierExtendQueryDto cdto = new SlotSpuSupplierExtendQueryDto();
        if(null != queryDto){
            if(null != queryDto.getPageIndex() && null != queryDto.getPageSize()){
                cdto.setPageIndex(queryDto.getPageIndex());
                cdto.setPageSize(queryDto.getPageSize());
            }
            if(!StringUtils.isEmpty(queryDto.getSupplierId())){
                cdto.setSupplierId(queryDto.getSupplierId());
            }
            if(!StringUtils.isEmpty(queryDto.getBrandName())){
                cdto.setBrandName(queryDto.getBrandName());
            }
            if(!StringUtils.isEmpty(queryDto.getCategoryName())){
                cdto.setCategoryName(queryDto.getCategoryName());
            }
            if (!StringUtils.isEmpty(queryDto.getSlotSpuNo())){
                cdto.setSlotSpuNo(queryDto.getSlotSpuNo());
            }
            if(!StringUtils.isEmpty(queryDto.getSeasonName())){
                cdto.setSeasonName(queryDto.getSeasonName());
            }
            if(!StringUtils.isEmpty(queryDto.getState())){
                cdto.setState(queryDto.getState());
            }else {
                cdto.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());
            }
            if(!StringUtils.isEmpty(queryDto.getSupplierSpuNo())){
                cdto.setSupplierSpuNo(queryDto.getSupplierSpuNo());
            }
            if(!StringUtils.isEmpty(queryDto.getStartTime())){
                cdto.setStartTime(sdfomat.parse(queryDto.getStartTime()));
            }
            if(!StringUtils.isEmpty(queryDto.getEndTime())){
                cdto.setEndTime(sdfomat.parse(queryDto.getEndTime()));
            }
        }
        return cdto;
    }




    /*
    * 组织待拍照商品数据
    *
    * */
    private  List<StudioPendingProduct> productList(List<HubSlotSpuSupplierExtend> results){
        List<StudioPendingProduct> hubProducts = new ArrayList<StudioPendingProduct>();

        if (null != results && results.size() > 0) {
            List<Long> filteredSpuId = results.stream().map(HubSlotSpuSupplierExtend :: getSupplierSpuId).distinct().collect(Collectors.toList());

            List<Long> filteredPendingId = results.stream().map(HubSlotSpuSupplierExtend :: getSpuPendingId).distinct().collect(Collectors.toList());
            HubSupplierSpuCriteriaDto dto = new HubSupplierSpuCriteriaDto();
            dto.setPageSize(100000);
            dto.createCriteria().andSupplierSpuIdIn(filteredSpuId);
            List<HubSupplierSpuDto> spuDtoList =  hubSupplierSpuGateWay.selectByCriteria(dto);

            HubSpuPendingCriteriaDto pendDto = new HubSpuPendingCriteriaDto();
            pendDto.setPageSize(100000);
            pendDto.createCriteria().andSpuPendingIdIn(filteredPendingId);
            List<HubSpuPendingDto>  pendingDtoList =  hubSpuPendingGateWay.selectByCriteria(pendDto);

            for (HubSlotSpuSupplierExtend x : results){

               // Optional<HubSupplierSpuDto> spuDto = spuDtoList.stream().filter(spu -> spu.getSupplierSpuId() .equals(x.getSupplierSpuId()) ).findFirst();
                Optional<HubSpuPendingDto> pendingDto = pendingDtoList.stream().filter(spu -> spu.getSpuPendingId() .equals(x.getSpuPendingId()) ).findFirst();
                StudioPendingProduct product = new StudioPendingProduct();
                product.setSlotSpuSupplierId(x.getSlotSpuSupplierId());
                product.setSupplierId(x.getSupplierId());
                product.setSupplierNo(x.getSupplierNo());
                product.setSpuPendingId(x.getSpuPendingId());
                product.setSupplierSpuId(x.getSupplierSpuId());
                product.setSlotNo(x.getSlotNo());
                product.setSlotSpuId(x.getSlotSpuId());
                product.setSlotSpuNo(x.getSlotSpuNo());
                product.setState(x.getState());
                product.setCreateTime(x.getCreateTime());
                product.setSupplierOperateSign(x.getSupplierOperateSign());
                product.setBrandName(x.getBrandName());
                product.setCategoryName(x.getCategoryName());
                product.setProductName(x.getSupplierSpuName());
                product.setSeasonName(x.getSeasonName());
//                spuDto.ifPresent((spu)->{
//                    product.setBrandName(spu.getSupplierBrandname());
//                    product.setCategoryName(spu.getSupplierCategoryname());
//                    product.setSupplierOrigin(spu.getSupplierOrigin());
//                    product.setProductName(spu.getSupplierSpuName());
//                    product.setProductDesc(spu.getSupplierSpuDesc());
//                });
                pendingDto.ifPresent((pending)->{
                    product.setBrandNo(pending.getHubBrandNo());
                    product.setCategoryNo(pending.getHubCategoryNo());
                });
                product.setSupplierSpuNo(x.getSupplierSpuNo());
                product.setSupplierSpuModel(x.getSupplierSpuModel());
                product.setSupplierOperateSign(x.getSupplierOperateSign());
                product.setRepeatMarker(x.getRepeatMarker());
                hubProducts.add(product);
            }
        }
        return hubProducts;
    }


    /*
    * 获取供货商已申请的批次列表
    * */
    public SlotsVo getSupplierSlotList(String supplierId){

        SlotsVo products = new SlotsVo();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.setPageSize(1000);
        cdto.createCriteria().andApplySupplierIdEqualTo(supplierId)
                .andApplyStatusEqualTo(StudioSlotApplyState.APPLYED.getIndex().byteValue())
                .andSendStateNotEqualTo(StudioSlotSendState.SEND.getIndex().byteValue());
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

            //获取slot可以选择商品的分类
            List<StudioDicCategoryDto> studioDicCategoryDto = getStudioDicCategoryDtos(filteredStudioIds);

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

                List<StudioDicCategoryDto> studioDicCategory = studioDicCategoryDto.stream().filter(spu -> spu.getStudioId() .equals(x.getStudioId()) ).collect(Collectors.toList());
                if(studioDicCategory!=null && studioDicCategory.size()>0) {
                    List<String> firstCategory = studioDicCategory.stream().map(StudioDicCategoryDto::getCategoryFirst).distinct().collect(Collectors.toList());
                    List<String> SecondCategory = studioDicCategory.stream().map(StudioDicCategoryDto::getCategorySecond).distinct().collect(Collectors.toList());
                    s.setCategoryFirst(firstCategory);
                    s.setCategorySecond(SecondCategory);
                }
                s.setSlotDate(x.getSlotDate());
                s.setStudioId(x.getStudioId());
                s.setSlotStatus(x.getSlotStatus());
                s.setStudioSlotId(x.getStudioSlotId());
                s.setSlotNo(x.getSlotNo());
                s.setApplySupplierId(x.getApplySupplierId());
                s.setApplyUser(x.getApplyUser());
                s.setApplyTime(x.getApplyTime());
                s.setApplyStatus(x.getApplyStatus());
                s.setSendState(x.getSendState());
                s.setSendTime(x.getSendTime());
                s.setSendUser(x.getSendUser());
                s.setArriveStatus(x.getArriveStatus());
                s.setArriveTime(x.getArriveTime());
                s.setArriveUser(x.getArriveUser());
                s.setPlanArriveTime(x.getPlanArriveTime());
//                List<SlotProduct> listProduct = SlotProductList(x.getSlotNo());
//                s.setCountNum(listProduct.size());

                long count = studioSlotSpuSendDetailDto.stream().filter(spu -> spu.getSlotNo() .equals(x.getSlotNo()) ).count();
                s.setCountNum(count);
                list.add(s);
            }
        }

        return  list;

    }
    /*
    * 获取slot可选择的商品分类
    * */
    private  List<StudioDicCategoryDto> getStudioDicCategoryDtos(List<Long> filteredStudioIds ){
        StudioDicCategoryCriteriaDto studioDto = new StudioDicCategoryCriteriaDto();
        studioDto.setPageSize(100000);
        studioDto.createCriteria().andStudioIdIn(filteredStudioIds);
        return  studioDicCategoryGateWay.selectByCriteria(studioDto);
    }

    /*
   * 获取slot可选择的商品分类
   * */
    private  List<StudioDicCategoryDto> getStudioDicCategoryDtos(Long StudioIds ){
        StudioDicCategoryCriteriaDto studioDto = new StudioDicCategoryCriteriaDto();
        studioDto.setPageSize(100000);
        studioDto.createCriteria().andStudioIdEqualTo(StudioIds);
        return  studioDicCategoryGateWay.selectByCriteria(studioDto);
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
        studioDto.setPageSize(100000);
        studioDto.createCriteria().andStudioIdEqualTo(StudioIds);
        return  studioDicSlotGateWay.selectByCriteria(studioDto);
    }



    /*
    * 获取所有批次的当前包含商品
    * */
    private List<StudioSlotSpuSendDetailDto> getStudioSlotSpuSendDetail(List<String> filteredSlotNos){

        StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
        dto.setPageSize(10000);
        dto.createCriteria().andSlotNoIn(filteredSlotNos);
        return studioSlotSpuSendDetailGateWay.selectByCriteria(dto);
    }
    /*
  * 获取当前批次的当前包含商品
  * */
    private List<StudioSlotSpuSendDetailDto> getStudioSlotSpuSendDetail(String SlotNos){

        StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
        dto.setPageSize(10000);
        dto.createCriteria().andSlotNoEqualTo(SlotNos);
        return studioSlotSpuSendDetailGateWay.selectByCriteria(dto);
    }

    /*释放申请的批次*/
    public HubResponse<?> releaseStudioSlot(String supplierId ,Long slotId,String createUser){
        try
        {

        StudioSlotDto studioSlotDto =  studioSlotGateWay.selectByPrimaryKey(slotId);
        if(studioSlotDto==null || !studioSlotDto.getApplySupplierId().equals(supplierId)){
            return HubResponse.errorResp("slot not found");
        }
        if(studioSlotDto.getSendState() > 0){
            return HubResponse.errorResp("This slot can't be deleted!");
        }
        StudioSlotSpuSendDetailCriteriaDto detailCriteriaDto = new StudioSlotSpuSendDetailCriteriaDto();
        detailCriteriaDto.setPageSize(10000);
        detailCriteriaDto.createCriteria().andStudioSlotIdEqualTo(slotId);
        List<StudioSlotSpuSendDetailDto> listDetail =  studioSlotSpuSendDetailGateWay.selectByCriteria(detailCriteriaDto);

        if(listDetail !=null && listDetail.size()>0){
            //更新slot spu 状态
            List<Long> filterIds = listDetail.stream().map(StudioSlotSpuSendDetailDto::getSlotSpuSupplierId).collect(Collectors.toList());
            HubSlotSpuSupplierWithCriteriaDto spuSupplierDto = new HubSlotSpuSupplierWithCriteriaDto();

            HubSlotSpuSupplierDto slotDto = new HubSlotSpuSupplierDto();
            slotDto.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());
            spuSupplierDto.setHubSlotSpuSupplier(slotDto);

            HubSlotSpuSupplierCriteriaDto criteriaDto = new HubSlotSpuSupplierCriteriaDto();
            criteriaDto.createCriteria().andSlotSpuSupplierIdIn(filterIds);
            spuSupplierDto.setCriteria(criteriaDto);

            hubSlotSpuSupplierGateway.updateByCriteriaSelective(spuSupplierDto);
            StudioSlotSpuSendDetailCriteriaDto detailDto = new StudioSlotSpuSendDetailCriteriaDto();
            //删除发送详情
            List<Long> ids = listDetail.stream().map(StudioSlotSpuSendDetailDto::getStudioSlotSpuSendDetailId).collect(Collectors.toList());
            detailDto.createCriteria().andStudioSlotSpuSendDetailIdIn(ids);
            studioSlotSpuSendDetailGateWay.deleteByCriteria(detailDto);
        }

        studioSlotDto.setApplyStatus((byte)0);
        studioSlotDto.setUpdateTime(new Date());
        studioSlotDto.setApplyUser(createUser);
        int i=  studioSlotGateWay.updateByPrimaryKey(studioSlotDto);
        if(i>0){
            return HubResponse.successResp(null);
        }else {
            return HubResponse.errorResp("slot not fail,please try again!");
        }
        }
        catch (Exception ex){
            log.info("releaseStudioSlot Exception " + ex.getMessage());
            return HubResponse.errorResp("Server error");
        }
    }
    /**
     * 删除供货商商品
     * @param supplierId
     * @param slotSSId
     * @param createUser
     * @return
     */
    public  boolean delProductFromSlot(String supplierId ,Long slotSSId,String createUser){

        HubSlotSpuSupplierDto spuSupplierDto = hubSlotSpuSupplierGateway.selectByPrimaryKey(slotSSId);
        if(spuSupplierDto.getSupplierId().equals(supplierId)){
            spuSupplierDto.setUpdateUser(createUser);
            spuSupplierDto.setUpdateTime(new Date());
            spuSupplierDto.setDataState((byte)0);
            return hubSlotSpuSupplierGateway.updateByPrimaryKey(spuSupplierDto)>0;
        }

        return false;
    }


    /*
    * 获取批次详情
    * */
    public SlotInfoExtends getSlotInfo(String supplierId ,String slotNo){
        SlotInfoExtends slot  = new SlotInfoExtends();
        StudioSlotCriteriaDto cdto = new StudioSlotCriteriaDto();
        cdto.setPageSize(10000);
        cdto.createCriteria().andApplySupplierIdEqualTo(supplierId).andSlotNoEqualTo(slotNo);
        List<StudioSlotDto> results = studioSlotGateWay.selectByCriteria(cdto);

        if (null != results && results.size() > 0) {
            StudioSlotDto studioSlot = results.get(0);

            List<StudioDicSlotDto> studioDicSlotDto  = getStudioDicSlotDtos(studioSlot.getStudioId());
            List<StudioDicCategoryDto> studioDicCategoryDtos = getStudioDicCategoryDtos(studioSlot.getStudioId());

            if(studioDicSlotDto!=null && studioDicSlotDto.size()>0){
                slot.setMaxNum(studioDicSlotDto.get(0).getSlotNumber());
                slot.setMinNum(studioDicSlotDto.get(0).getSlotMinNumber());
            }

            if(studioDicCategoryDtos!=null && studioDicCategoryDtos.size()>0){
                List<String> firstCategory = studioDicCategoryDtos.stream().map(StudioDicCategoryDto::getCategoryFirst).distinct().collect(Collectors.toList());
                List<String> SecondCategory = studioDicCategoryDtos.stream().map(StudioDicCategoryDto::getCategorySecond).distinct().collect(Collectors.toList());
                slot.setCategoryFirst(firstCategory);
                slot.setCategorySecond(SecondCategory);
            }
            //region 属性封装
            slot.setStudioId(studioSlot.getStudioId());
            slot.setSlotStatus(studioSlot.getSlotStatus());
            slot.setStudioSlotId(studioSlot.getStudioSlotId());
            slot.setSlotNo(studioSlot.getSlotNo());
            slot.setApplySupplierId(studioSlot.getApplySupplierId());
            slot.setApplyUser(studioSlot.getApplyUser());
            slot.setApplyTime(studioSlot.getApplyTime());
            slot.setApplyStatus(studioSlot.getApplyStatus());
            slot.setSendState(studioSlot.getSendState());
            slot.setSendTime(studioSlot.getSendTime());
            slot.setSendUser(studioSlot.getSendUser());
            slot.setArriveStatus(studioSlot.getArriveStatus());
            slot.setArriveTime(studioSlot.getArriveTime());
            slot.setArriveUser(studioSlot.getArriveUser());
            slot.setPlanArriveTime(studioSlot.getPlanArriveTime());
            List<SlotProduct> listProduct = SlotProductList(studioSlot.getSlotNo());
            slot.setCountNum(listProduct.size());
            //endregion
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
                p.setId(item.getStudioSlotSpuSendDetailId());
                p.setSupplierNo(item.getSupplierNo());
                p.setSupplierId(item.getSupplierId());
                p.setSpuPendingId(item.getSpuPendingId());
                p.setSupplierSpuId(item.getSupplierSpuId());
                p.setSlotSpuNo(item.getSlotSpuNo());
                p.setSlotSpuSupplierId(item.getSlotSpuSupplierId());
                p.setSupplierSpuModel(item.getSupplierSpuModel());
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

    public HubResponse<SlotProductEditVo> addProductIntoSlot(String supplierId ,String slotNo, List<Long> slotSSIds,String createUser) {

        HubResponse<SlotProductEditVo> response = new HubResponse<SlotProductEditVo>();
        response.setCode("0");
        SlotProductEditVo updatedVo = new SlotProductEditVo();
        updatedVo.setSupplierId(supplierId);
        updatedVo.setSlotNo(slotNo);
        updatedVo.setCount(slotSSIds.size());
        int successNum = 0 ,failNum = 0;

        try {
            //验证是否slot是否可以添加
            SlotInfoExtends slotInfo = getSlotInfo(supplierId, slotNo);
            //验证slot是否存在
            if (slotInfo == null) {
                throw new EphubException("C1", "Slot is not found");
            } else {

                if (slotInfo.getApplyStatus() != StudioSlotApplyState.APPLYED.getIndex().byteValue() ||
                        (slotInfo.getSendState() !=null &&  slotInfo.getSendState() != StudioSlotSendState.WAIT_SEND.getIndex().byteValue())) {
                    throw new EphubException("C2", "Status of this slot is incorrect");
                }
                List<SlotProduct> productList = slotInfo.getSlotProductList();

                if (slotInfo.getMaxNum() <= productList.size()) {
                    throw new EphubException("C3", "Amount of the slot reaches the maximal capacity");
                }
                HubSlotSpuSupplierCriteriaDto ssdto = new HubSlotSpuSupplierCriteriaDto();
                ssdto.setPageSize(100000);
                ssdto.createCriteria().andSlotSpuSupplierIdIn(slotSSIds);

                List<HubSlotSpuSupplierDto> products = hubSlotSpuSupplierGateway.selectByCriteria(ssdto);

                if(products!=null && products.size()>0){
                    for (HubSlotSpuSupplierDto product :products){
                        Optional<SlotProduct> slotProduct = productList.stream().filter(x->x.getSlotSpuSupplierId().equals(product.getSlotSpuSupplierId())).findFirst();
                        String code= product.getSlotNo();
                        if(slotProduct.isPresent()){
                            code = slotProduct.get().getSupplierSpuModel();
                        }
                        //region  判断是否处于可添加商品状态
                        if (product.getState() == SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue()) {
                            //throw new EphubException("C5", "该商品已经加入发货单了");
                            updatedVo.addErrorConent(setCheckErrorMsg(code,product.getSlotSpuSupplierId(), "C5", "The product has already added to the slot"));
                            continue;
                        }
                        if (product.getState() == SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue()) {
                           // throw new EphubException("C6", "该商品已经发货了");
                            updatedVo.addErrorConent(setCheckErrorMsg(code,product.getSlotSpuSupplierId(),"C6", "The product has already been shipped"));
                            continue;
                        }
                        if (slotInfo.getCategoryFirst() != null) {
                            HubSpuPendingDto pendingDtoList = hubSpuPendingGateWay.selectByPrimaryKey(product.getSpuPendingId());


                            //Optional<HubSpuPendingDto> pendingDto = pendingDtoList.stream().filter(spu -> spu.getSpuPendingId() .equals(x.getSpuPendingId()) ).findFirst();

                            if (pendingDtoList != null && pendingDtoList.getHubCategoryNo() != null ) {
                                String categoryNo = this.categoryMap.get(pendingDtoList.getHubCategoryNo().toLowerCase())!=null ?this.categoryMap.get(pendingDtoList.getHubCategoryNo().toLowerCase()).toString(): pendingDtoList.getHubCategoryNo();
                                if (slotInfo.getCategoryFirst().stream().filter(x-> categoryNo.startsWith(x.trim())).count()<=0) {
                                    //throw new EphubException("C7", "该商品与目标发货单类型不符");
                                    updatedVo.addErrorConent(setCheckErrorMsg(code,product.getSlotSpuSupplierId(),"C7", "The categories of this product and the slot are not match"));
                                    continue;
                                }
                            }
                        }
                        //endregion

                        HubSupplierSpuDto supProduct = hubSupplierSpuGateWay.selectByPrimaryKey(product.getSupplierSpuId());

                        //region 验证成功后，进行添加
                        StudioSlotSpuSendDetailDto data = new StudioSlotSpuSendDetailDto();
                        data.setSlotNo(slotNo);
                        data.setStudioSlotId(slotInfo.getStudioSlotId());
                        data.setSlotSpuSupplierId(product.getSlotSpuSupplierId());
                        data.setSupplierId(supplierId);
                        data.setSupplierNo(product.getSupplierNo());
                        data.setSpuPendingId(product.getSpuPendingId());
                        data.setSupplierSpuId(product.getSupplierSpuId());
                        data.setSlotSpuNo(product.getSlotSpuNo());
                        data.setSupplierSpuName(supProduct.getSupplierSpuName());
                        data.setSupplierSpuModel(supProduct.getSupplierSpuModel());
                        data.setSupplierBrandName(supProduct.getSupplierBrandname());
                        data.setSupplierCategoryName(supProduct.getSupplierCategoryname());
                        data.setSupplierSeasonName(supProduct.getSupplierSeasonname());
                        data.setBarcode(slotNo + product.getSlotSpuNo());
                        data.setSendState(StudioSlotSendState.WAIT_SEND.getIndex().byteValue());
                        data.setArriveState(StudioSlotArriveState.NOT_ARRIVE.getIndex().byteValue());
                        data.setCreateTime(new Date());
                        data.setCreateUser(createUser);
                        Long id = studioSlotSpuSendDetailGateWay.insert(data);
                        if (id > 0) {
                            HubSlotSpuSupplierDto upSlotSpu = new HubSlotSpuSupplierDto();
                            upSlotSpu.setSlotSpuSupplierId(product.getSlotSpuSupplierId());
                            upSlotSpu.setSlotNo(slotNo);
                            upSlotSpu.setState(SlotSpuSupplierState.ADD_INVOICE.getIndex().byteValue());
                            hubSlotSpuSupplierGateway.updateByPrimaryKeySelective(upSlotSpu);

                           //TODO: 需要调用重任的添加接口未验证
                            SlotSpuSendDetailCheckDto checkDto = new SlotSpuSendDetailCheckDto();
                            checkDto.setSlotNo(product.getSlotNo());
                            checkDto.setSlotSpuSupplierId(product.getSlotSpuSupplierId());

                            CommonResult commonResult = hubSlotSpuSupplierService.updateSlotSpuSupplierWhenSupplierSelectProduct(checkDto);
                            if(!commonResult.isSuccess()){
                                //TODO:失败了怎么处理没想好
                            }

                            slotInfo.setCountNum(slotInfo.getCountNum() + 1);
                            successNum  = successNum +1;

                        } else {
                            //throw new EphubException("W0", "商品加入发货单失败");
                            updatedVo.addErrorConent(setCheckErrorMsg(code,product.getSlotSpuSupplierId(),"W0", "Add product to slot failed"));
                        }
                        //endregion
                    }


                    updatedVo.setSuccessCount(successNum);
                    if(updatedVo.getErrorConent()!=null){
                        failNum = updatedVo.getErrorConent().size();
                    }
                    updatedVo.setFailCount(failNum);
                    updatedVo.setSlotInfo(slotInfo);
                    response.setContent(updatedVo);
                }else {
                    throw new EphubException("C4", "The product is not exist");
                }
            }

        } catch (EphubException e) {
            log.info("addProductIntoSlot EphubException " + e.getErrcode() + e.getMessage());
            updatedVo = setErrorMsg(response, slotNo, e.getErrcode(), e.getMessage());
            response.setErrorMsg(updatedVo);
            response.setMsg(e.getMessage());
        } catch (Exception ex) {
            log.info("addProductIntoSlot Exception " + ex.getMessage());
            setErrorMsg(response, slotNo, "S0", "Server error occurs when adding product to slot");
        }
        return response;
    }


    private ErrorConent setCheckErrorMsg(String spuNo,Long ssid,String errorCode,String errorMsg){
        ErrorConent conent = new ErrorConent();
        conent.setSpuNo(spuNo);
        conent.setSsid(ssid);
        conent.setErrorCode(errorCode);
        conent.setErrorMsg(errorMsg);
        return conent;
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
        updatedVo.setSlotNo(slotNo);
        response.setMsg(errorMsg);
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
    public HubResponse<SlotProductEditVo> delProductFromSlot(String supplierId ,String slotNo,Long slotSSId,Long slotSSDId,String createUser){
        HubResponse<SlotProductEditVo> response = new HubResponse<SlotProductEditVo>();
        response.setCode("0");
        SlotProductEditVo updatedVo = null;
        try {
            HubSlotSpuSupplierDto product = hubSlotSpuSupplierGateway.selectByPrimaryKey(slotSSId);
            StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
            dto.createCriteria().andSupplierIdEqualTo(supplierId).andStudioSlotSpuSendDetailIdEqualTo(slotSSDId);
           int count =  studioSlotSpuSendDetailGateWay.deleteByCriteria(dto);
           if(count>0){
               HubSlotSpuSupplierDto upSlotSpu = new HubSlotSpuSupplierDto();
               upSlotSpu.setSlotSpuSupplierId(product.getSlotSpuSupplierId());
               upSlotSpu.setState(SlotSpuSupplierState.WAIT_SEND.getIndex().byteValue());
               hubSlotSpuSupplierGateway.updateByPrimaryKeySelective(upSlotSpu);

           }else {
               updatedVo = setErrorMsg(response, slotNo, "D0", "delete product to slot failed");
           }

            response.setErrorMsg(updatedVo);
        }
        catch (Exception ex){
            log.info("delProductFromSlot Exception " + ex.getMessage());
            setErrorMsg(response,slotNo, "S1","Server error occurs when removing product from slot");
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
                throw new EphubException("C1", "Slot is not found");
            }else {
                if(slotInfo.getSendState()!=null && slotInfo.getSendState() == StudioSlotSendState.SEND.getIndex().byteValue()){
                    throw new EphubException("C8", "The slot is shipped");
                }
                long countNm = slotInfo.getSlotProductList().size();
                if(slotInfo.getMinNum() > countNm ){
                    throw new EphubException("C9", "Amount of the slot doesn't reach the minimal limitation");
                }
                if(slotInfo.getMaxNum() < countNm ){
                    throw new EphubException("C10", "Amount of the slot is beyond the maximal limitation");
                }
                List<SlotProduct> slotProducts =slotInfo.getSlotProductList();

                // region 发货时调重任的东西，进行发货验证
                List<SlotSpuSendDetailCheckDto> checkDtos = new ArrayList<SlotSpuSendDetailCheckDto>();
                checkDtos = slotProducts.stream().collect(ArrayList<SlotSpuSendDetailCheckDto>::new,
                        (list, item) -> {
                            SlotSpuSendDetailCheckDto dto = new SlotSpuSendDetailCheckDto();
                            dto.setStudioSlotSpuSendDetailId(item.getId());
                            dto.setSlotSpuSupplierId(item.getSlotSpuSupplierId());
                            dto.setSlotNo(slotNo);
                            list.add(dto);
                        },(one, two)->one.addAll(two));

                List<SlotSpuSendDetailCheckDto> resCheckDtos = hubSlotSpuSupplierService.updateSlotSpuSupplierWhenSupplierSend(checkDtos);

                if(resCheckDtos.stream().filter(x-> !x.isResultSign()).count()>0){
                    List<Long> ids = resCheckDtos.stream().filter(x-> !x.isResultSign()).map(SlotSpuSendDetailCheckDto::getStudioSlotSpuSendDetailId).distinct().collect(Collectors.toList());
                    List<String> spuIds = slotProducts.stream().filter(x-> ids.contains(x.getId())).map(SlotProduct::getSlotSpuNo).collect(Collectors.toList());
                    throw new EphubException("C11",  String.join(",",spuIds) +"had been sent ,no need send!");
                }
                //endregion



                if(slotInfo.getMaxNum() > countNm ){
                    response.setCode("2");
                    response.setMsg("Slot is not full yet ready to ship");
                }
                if(slotInfo.getMaxNum() == countNm ){
                    response.setMsg("Slot is full and ready to ship");
                }



            }
        }catch (EphubException e){
            log.info("checkProductAndSendSlot EphubException " + e.getErrcode() +e.getMessage());
            updatedVo = setErrorMsg(response, slotNo, e.getErrcode(), e.getMessage());
            response.setErrorMsg(updatedVo);
        }catch (Exception ex){
            log.info("checkProductAndSendSlot Exception " +ex.getMessage());
            setErrorMsg(response,slotNo, "S3","Server error occurs when verifying the permission of shipping");
        }
        return  response;
    }

    public boolean slotPrint(Long slotId){
        StudioSlotDto studioSlotDto = new StudioSlotDto();
        studioSlotDto.setStudioSlotId(slotId);
        studioSlotDto.setSendState(StudioSlotSendState.ISPRINT.getIndex().byteValue());
        studioSlotDto.setUpdateTime(new Date());
        return studioSlotGateWay.updateByPrimaryKeySelective(studioSlotDto)>0;
    }


    /*
    * 添加物流单号
    * */
    public boolean insertSlotLogistic(Long studioSlotId,String logisticName,String trackingNo,String createUser){
       try {
           StudioSlotLogistictTrackDto trackDto = new StudioSlotLogistictTrackDto();
           trackDto.setTrackName(logisticName);
           trackDto.setTrackNo(trackingNo);
           trackDto.setSendMasterId(studioSlotId);
           trackDto.setType((byte) 0);
           trackDto.setCreateUser(createUser);
           trackDto.setCreateTime(new Date());
           if(studioSlotLogistictTrackGateWay.insertSelective(trackDto)>0){
               StudioSlotDto studioSlotDto = new StudioSlotDto();
               studioSlotDto.setStudioSlotId(studioSlotId);
               studioSlotDto.setSendTime(new Date());
               studioSlotDto.setSlotStatus(StudioSlotState.SEND.getIndex().byteValue());
               studioSlotDto.setSendState(StudioSlotSendState.SEND.getIndex().byteValue());
               studioSlotDto.setTrackNo(trackingNo);
               studioSlotDto.setUpdateTime(new Date());
               studioSlotDto.setSendUser(createUser);
               int i =  studioSlotGateWay.updateByPrimaryKeySelective(studioSlotDto);
               // 更新详情
               if(i>0){
                   StudioSlotSpuSendDetailWithCriteriaDto detailWithCriteriaDto = new StudioSlotSpuSendDetailWithCriteriaDto();
                   StudioSlotSpuSendDetailDto detailDto = new StudioSlotSpuSendDetailDto();
                   detailDto.setSendState(StudioSlotSupplierSendState.SEND.getIndex().byteValue());
                   detailDto.setSendUser(createUser);
                   detailDto.setSendTime(new Date());
                   detailWithCriteriaDto.setStudioSlotSpuSendDetail(detailDto);
                   StudioSlotSpuSendDetailCriteriaDto detailCriteriaDto = new StudioSlotSpuSendDetailCriteriaDto();
                   detailCriteriaDto.createCriteria().andStudioSlotIdEqualTo(studioSlotId);
                   detailWithCriteriaDto.setCriteria(detailCriteriaDto);
                   studioSlotSpuSendDetailGateWay.updateByCriteriaSelective(detailWithCriteriaDto);
               }

           }

           return false;

       }catch (Exception ex){
           log.info("insertSlotLogistic Exception " + ex.getMessage());
           return false;
       }
    }

    public StudioSlotLogistictTrackDto  getSlotLogisticInfo(Long studioSlotId){
        try {
            StudioSlotLogistictTrackCriteriaDto dto = new StudioSlotLogistictTrackCriteriaDto();
            dto.setPageSize(100000);
            dto.createCriteria().andSendMasterIdEqualTo(studioSlotId).andTypeEqualTo((byte)0);
            List<StudioSlotLogistictTrackDto> result = studioSlotLogistictTrackGateWay.selectByCriteria(dto);
            if(result!=null && result.size()>0){
                return result.get(0);
            }
            else {
                return new StudioSlotLogistictTrackDto();
            }
        }
        catch (Exception ex){

            log.info("getSlotLogisticInfo Exception " + ex.getMessage());
            return null;
        }

    }



    /**
     * 获取批次列表
     * @param StudioId
     * @param startTime
     * @param endTime
     * @return
     */
    public SlotsVo  getStudioSlot(Long StudioId,String startTime,String endTime,String categoryNos){
        return getStudioSlot(StudioId,startTime,endTime,categoryNos,1,10);
    }
    public SlotsVo  getStudioSlot(Long StudioId,String startTime,String endTime,String categoryNos,int pageIndex,int pageSize) {
        SlotsVo studioSlotsList = new SlotsVo();
        try {
            StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
            StudioSlotCriteriaDto.Criteria criteria = dto.createCriteria().andApplyStatusEqualTo(StudioSlotApplyState.WAIT_APPLY.getIndex().byteValue());
            if (StudioId != null) {
                criteria.andStudioIdEqualTo(StudioId);
            }
            //数据结构设计不合理，额外的需要验证分类
            if (!StringUtils.isEmpty(categoryNos)) {
                List<String> list = Arrays.asList(categoryNos.split(",")).stream().collect(Collectors.toList());
                StudioDicCategoryCriteriaDto studioDto = new StudioDicCategoryCriteriaDto();
                if (list != null && list.size() > 0) {
                    studioDto.createCriteria().andCategoryFirstIn(list);
                }
                List<StudioDicCategoryDto> studioDicCategoryDtoList = studioDicCategoryGateWay.selectByCriteria(studioDto);
                List<Long> studioIds = studioDicCategoryDtoList.stream().map(StudioDicCategoryDto::getStudioId).distinct().collect(Collectors.toList());
                criteria.andStudioIdIn(studioIds);
            }

            if (!StringUtils.isEmpty(startTime)) {
                criteria.andPlanArriveTimeGreaterThan(sdfomat.parse(startTime));
            }
            if ( !StringUtils.isEmpty(endTime)) {
                criteria.andPlanArriveTimeLessThanOrEqualTo(sdfomat.parse(endTime));
            }
            dto.setPageNo(pageIndex > 0 ? pageIndex : 1);
            dto.setPageSize(pageSize > 0 ? pageSize : 10);

            dto.setOrderByClause("plan_arrive_time");
            int total = studioSlotGateWay.countByCriteria(dto);
            if (total > 0) {
                List<StudioSlotDto> results = studioSlotGateWay.selectByCriteria(dto);
                studioSlotsList.setSlotInfoList(SlotList(results));
            }
            studioSlotsList.setTotal(total);
        } catch (Exception ex) {

        }
        return studioSlotsList;
    }

    /**
     * 供货商申请批次
     * @param upDto
     * @return
     */
    public List<ErrorConent> applyUpdateSlot(StudioSlotQueryDto upDto){
        List<Long> slotIds = Arrays.asList(upDto.getStudioSlotIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        StudioSlotDto studioSlotDto = new StudioSlotDto();
        studioSlotDto.setApplySupplierId(upDto.getSupplierId().toString());
        studioSlotDto.setApplyUser(upDto.getSupplierUser());
        studioSlotDto.setApplyTime(new Date());
        studioSlotDto.setSlotStatus(StudioSlotState.APPLYED.getIndex().byteValue());
        studioSlotDto.setApplyStatus(StudioSlotApplyState.APPLYED.getIndex().byteValue());
        List<ErrorConent> result = new ArrayList<ErrorConent>();
        for (Long slotId : slotIds){
            StudioSlotDto slotDto = studioSlotGateWay.selectByPrimaryKey(slotId);
            int i =0 ;
            byte status =slotDto.getApplyStatus();
            if(slotDto.getApplyStatus()==StudioSlotApplyState.WAIT_APPLY.getIndex().byteValue()){
                studioSlotDto.setStudioSlotId(slotId);
                 i = studioSlotGateWay.updateByPrimaryKeySelective(studioSlotDto);
            }
            if(status != StudioSlotApplyState.WAIT_APPLY.getIndex().byteValue() || i==0){
                ErrorConent errorConent = new ErrorConent();
                errorConent.setErrorCode(studioSlotDto.getSlotNo());
                errorConent.setErrorMsg(slotId.toString()+"Application failure");
                result.add(errorConent);
            }
        }

        return  result.size()>0 ? result :null;

    }

    public List<StudioDto> getStudioList(){
        StudioCriteriaDto dto = new  StudioCriteriaDto();
        dto.setPageSize(10000);
        return  studioGateWay.selectByCriteria(dto);

    }

    public List<StudioDto> getStudioListByCategory(List<String> categoryNos){
        List<StudioDto> studioDtos = new ArrayList<StudioDto>();
        StudioDicCategoryCriteriaDto studioDto = new StudioDicCategoryCriteriaDto();
        studioDto.setPageSize(10000);
        if(categoryNos!=null && categoryNos.size()>0) {
            studioDto.createCriteria().andCategoryFirstIn(categoryNos);
        }
        else{
                return getStudioList();
        }

        List<StudioDicCategoryDto> studioDicCategoryDtoList =  studioDicCategoryGateWay.selectByCriteria(studioDto);
        if(studioDicCategoryDtoList!=null && studioDicCategoryDtoList.size()>0){

            List<Long> filteredStudioId = studioDicCategoryDtoList.stream().map(StudioDicCategoryDto :: getStudioId).distinct().collect(Collectors.toList());
            StudioCriteriaDto dto = new  StudioCriteriaDto();
            if(filteredStudioId!=null &&filteredStudioId.size()>0) {
                dto.createCriteria().andStudioIdIn(filteredStudioId);
            }
            studioDtos =  studioGateWay.selectByCriteria(dto);
        }
        return studioDtos;

    }
}
