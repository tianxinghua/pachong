package com.shangpin.ephub.product.business.ui.studio.studio.service.impl;

import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.result.HubSlotSpuSupplierExtend;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuGateWay;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.*;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnMasterGateWay;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.DefectiveSpuDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.DefectiveListVo;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ReturnSlotInfo;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/6/27.
 */
@Service
@Slf4j
public class ReturnSlotServiceImpl implements IReturnSlotService {

    @Autowired
    StudioSlotReturnMasterGateWay studioSlotReturnMasterGateWay;

    @Autowired
    StudioSlotReturnDetailGateWay studioSlotReturnDetailGateWay;

    @Autowired
    StudioSlotDefectiveSpuGateWay studioSlotDefectiveSpuGateWay;

    @Autowired
    StudioSlotDefectiveSpuPicGateWay studioSlotDefectiveSpuPicGateWay;


    /**
     * 获取未收回的返回单
     * @param queryDto
     * @return
     */
    public List<StudioSlotReturnMasterDto> getReturnSlotList(ReturnSlotQueryDto queryDto){
        StudioSlotReturnMasterCriteriaDto dto = new StudioSlotReturnMasterCriteriaDto();
        //TODO:没有supplier
        StudioSlotReturnMasterCriteriaDto.Criteria  criteria = dto.createCriteria()
                .andSupplierIdEqualTo(queryDto.getSupplierId()).andSendStateEqualTo((byte)1);
        if(StringUtils.isEmpty(queryDto.getArriveState())){
            criteria.andArriveStateEqualTo((byte)0);
        }else{
            criteria.andArriveStateEqualTo((byte)queryDto.getArriveState());
        }
        return studioSlotReturnMasterGateWay.selectByCriteria(dto);
   }

    /**
     * 供货商接收返回单
     * @param supplierId
     * @param id
     * @param userName
     * @return
     */
   public  boolean ReceiveReturnSlot(String supplierId,Long id,String userName){
       StudioSlotReturnMasterDto dto = new StudioSlotReturnMasterDto();
       dto.setStudioSlotReturnMasterId(id);
       dto.setArriveUser(userName);
       dto.setArriveTime(new Date());
       dto.setArriveState((byte)1);
       dto.setState((byte)2);
       return studioSlotReturnMasterGateWay.updateByPrimaryKeySelective(dto)>0;
   }

    /**
     * 获取返货单详情
     * @param supplierId
     * @param id
     * @return
     */
   public ReturnSlotInfo getReceivedSlotInfo(String supplierId, Long id){
       ReturnSlotInfo result = new ReturnSlotInfo();

       StudioSlotReturnMasterDto studioSlot = studioSlotReturnMasterGateWay.selectByPrimaryKey(id);

       result.setStudioSlotReturnMasterId(studioSlot.getStudioSlotReturnMasterId());
       result.setStudioSendNo(studioSlot.getStudioSendNo());
       result.setQuantity(studioSlot.getQuantity());
       result.setActualQuantity(studioSlot.getActualQuantity());
       result.setTrackNo(studioSlot.getTrackNo());

       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andStudioSlotReturnMasterIdEqualTo(id).andArriveStateEqualTo(StudioSlotArriveState.RECEIVED.getIndex().byteValue());

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       result.setDetailDtoList(detailDtoList);

      return result;

   }

    /**
     * 扫描 拣货
     * @param supplierId
     * @param id
     * @param barcode
     * @param userName
     * @return
     */
   public HubResponse<StudioSlotReturnDetailDto> addProductFromScan(String supplierId,Long id,String barcode,String userName){

       HubResponse<StudioSlotReturnDetailDto> result = new HubResponse<StudioSlotReturnDetailDto>();
       result.setCode("0");
       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andSupplierIdEqualTo(supplierId).andStudioSlotReturnMasterIdEqualTo(id).andBarcodeEqualTo(barcode);

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       StudioSlotReturnDetailDto returnDetailDto =null;
       int i = 0;
       if (detailDtoList!=null && detailDtoList.size()>0){
           returnDetailDto = detailDtoList.get(0);
           if(returnDetailDto.getArriveState() == StudioSlotArriveState.RECEIVED.getIndex().byteValue()) {
               result.setCode("1");
               result.setMsg("Repetitive scanning!");

           }else{
               StudioSlotReturnDetailDto detailDto = new StudioSlotReturnDetailDto();
               detailDto.setArriveState(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
               detailDto.setArriveUser(userName);
               detailDto.setArriveTime(new Date());
               detailDto.setStudioSlotReturnDetailId(returnDetailDto.getStudioSlotReturnDetailId());
               i = studioSlotReturnDetailGateWay.updateByPrimaryKeySelective(detailDto);
               if (i > 0) {
                   returnDetailDto.setArriveState(StudioSlotArriveState.RECEIVED.getIndex().byteValue());
               }
           }
           result.setContent(returnDetailDto);
       }else {
           result.setCode("1");
           result.setMsg("Product information does not exist!");
       }
       return result;
   }

    /**
     * 拣货结果确认
     * @param supplierId
     * @param id
     * @return
     */
   public ReturnSlotInfo confirmSlotInfo(String supplierId, Long id){
       ReturnSlotInfo result = new ReturnSlotInfo();

       StudioSlotReturnMasterDto studioSlot = studioSlotReturnMasterGateWay.selectByPrimaryKey(id);

       result.setStudioSlotReturnMasterId(studioSlot.getStudioSlotReturnMasterId());
       result.setStudioSendNo(studioSlot.getStudioSendNo());
       result.setQuantity(studioSlot.getQuantity());
       result.setActualQuantity(studioSlot.getActualQuantity());
       result.setTrackNo(studioSlot.getTrackNo());

       StudioSlotReturnDetailCriteriaDto  dto = new StudioSlotReturnDetailCriteriaDto();
       dto.createCriteria().andStudioSlotReturnMasterIdEqualTo(id).andArriveStateEqualTo(StudioSlotArriveState.NOT_ARRIVE.getIndex().byteValue());

       List<StudioSlotReturnDetailDto> detailDtoList = studioSlotReturnDetailGateWay.selectByCriteria(dto);
       result.setDetailDtoList(detailDtoList);

       return  result;
   }

    /**
     * 插入残品信息
     * @param queryDto
     * @return
     */
   public Long addDefective(DefectiveSpuDto queryDto){
       StudioSlotReturnDetailCriteriaDto criteriaDto = new StudioSlotReturnDetailCriteriaDto();
       criteriaDto.createCriteria().andBarcodeEqualTo(queryDto.getBarcode());

       List<StudioSlotReturnDetailDto> detailDtos = studioSlotReturnDetailGateWay.selectByCriteria(criteriaDto);
       if(detailDtos!=null && detailDtos.size()>0){
           StudioSlotReturnDetailDto detailDto = detailDtos.get(0);
           StudioSlotDefectiveSpuDto defectiveSpuDto = new StudioSlotDefectiveSpuDto();
           defectiveSpuDto.setDetailId(detailDto.getStudioSlotReturnDetailId());
           defectiveSpuDto.setDetailFrom((byte)1);
           defectiveSpuDto.setSlotNo(detailDto.getSlotNo());
           defectiveSpuDto.setSupplierId(detailDto.getSupplierId());
           defectiveSpuDto.setSupplierNo(defectiveSpuDto.getSupplierNo());
           defectiveSpuDto.setSpuPendingId(defectiveSpuDto.getSpuPendingId());
           defectiveSpuDto.setSupplierSpuId(detailDto.getSupplierSpuId());
           defectiveSpuDto.setSlotSpuNo(defectiveSpuDto.getSlotSpuNo());
           defectiveSpuDto.setDataState((byte)0);
           defectiveSpuDto.setCreateUser(queryDto.getUserName());
           defectiveSpuDto.setCreateTime(new Date());
           Long i = studioSlotDefectiveSpuGateWay.insertSelective(defectiveSpuDto);

           if(i>0 && queryDto.getImages().size()>0){
               for (String picUrl :queryDto.getImages()){
                   StudioSlotDefectiveSpuPicDto picDto = new StudioSlotDefectiveSpuPicDto();
                   picDto.setStudioSlotDefectiveSpuId(i);
                   picDto.setSupplierId(detailDto.getSupplierId());
                   picDto.setSupplierNo(defectiveSpuDto.getSupplierNo());
                   picDto.setSpPicUrl(picUrl);
                   picDto.setCreateUser(queryDto.getUserName());
                   picDto.setCreateTime(new Date());
                   studioSlotDefectiveSpuPicGateWay.insertSelective(picDto);
               }
           }
           return i;
       }

       return null;

   }

    /**
     * 获取残品信息
     * @param supplierId
     * @return
     */
   public List<DefectiveListVo> getDefectiveList(String supplierId){
       List<DefectiveListVo> defectiveListVoList = new ArrayList<>();
       StudioSlotDefectiveSpuCriteriaDto criteriaDto = new StudioSlotDefectiveSpuCriteriaDto();
       criteriaDto.createCriteria().andSupplierIdEqualTo(supplierId).andDataStateNotEqualTo((byte)1);
       criteriaDto.setOrderByClause("create_time desc");
       List<StudioSlotDefectiveSpuDto> defectiveSpuDtos = studioSlotDefectiveSpuGateWay.selectByCriteria(criteriaDto);

       if(defectiveSpuDtos!=null && defectiveSpuDtos.size()>0){

           List<Long> filteredId = defectiveSpuDtos.stream().map(StudioSlotDefectiveSpuDto:: getStudioSlotDefectiveSpuId).distinct().collect(Collectors.toList());
           StudioSlotDefectiveSpuPicCriteriaDto picDto = new StudioSlotDefectiveSpuPicCriteriaDto();
           picDto.createCriteria().andStudioSlotDefectiveSpuIdIn(filteredId);
           List<StudioSlotDefectiveSpuPicDto> picDtos = studioSlotDefectiveSpuPicGateWay.selectByCriteria(picDto);

           List<Long> filteredDetailId = defectiveSpuDtos.stream().map(StudioSlotDefectiveSpuDto:: getDetailId).distinct().collect(Collectors.toList());
           StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
           detailDto.createCriteria().andStudioSlotReturnDetailIdIn(filteredDetailId);
           List<StudioSlotReturnDetailDto>  detailDtos =  studioSlotReturnDetailGateWay.selectByCriteria(detailDto);

           for (StudioSlotDefectiveSpuDto spuDto : defectiveSpuDtos){
               Optional<StudioSlotReturnDetailDto> detailDto1 = detailDtos.stream().filter(x-> spuDto.getDetailId().equals(x.getStudioSlotReturnDetailId())).findFirst();
               List<StudioSlotDefectiveSpuPicDto> picDtos1 = picDtos.stream().filter(x-> spuDto.getStudioSlotDefectiveSpuId().equals(x.getStudioSlotDefectiveSpuId())).collect(Collectors.toList());

               DefectiveListVo vo = new DefectiveListVo();
               vo.setId(spuDto.getStudioSlotDefectiveSpuId());
               detailDto1.ifPresent(d->{
                   vo.setBarcode(d.getBarcode());
                   vo.setBrandName(d.getSupplierBrandName());
                   vo.setSpuName(d.getSupplierSpuName());
               });
               if(picDtos1!=null &&picDtos1.size()>0){
                   vo.setImages(picDtos1.stream().map(StudioSlotDefectiveSpuPicDto::getSpPicUrl).collect(Collectors.toList()));
               }
               vo.setCreateTime(spuDto.getCreateTime());
               vo.setCreateUser(spuDto.getCreateUser());
               defectiveListVoList.add(vo);
           }
       }


       return defectiveListVoList;
   }

    /**
     * 删除残品信息
     * @param supplierId
     * @param id
     * @return
     */
    public boolean DeleteDefective(String supplierId, Long id,String userName){

        StudioSlotDefectiveSpuDto spuDto = new StudioSlotDefectiveSpuDto();
        spuDto.setStudioSlotDefectiveSpuId(id);
        spuDto.setDataState((byte)1);
        spuDto.setUpdateTime(new Date());
        spuDto.setUpdateUser(userName);

        return studioSlotDefectiveSpuGateWay.updateByPrimaryKeySelective(spuDto)>0;
    }
}
