package com.shangpin.ephub.product.business.service.studio.hubslot.impl;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.SlotSpuSupplierRepeatMarker;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.gateway.HubSlotSpuSupplierGateway;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by loyalty on 17/6/10.
 */
@Service
@Slf4j
public class HubSlotSpuSupplierServiceImpl implements HubSlotSpuSupplierService {

    @Autowired
    HubSlotSpuSupplierGateway spuSupplierGateway;

    @Override
    public boolean addHubSloSpuSupplier(HubSlotSpuSupplierDto dto) throws Exception {

        try {
            List<HubSlotSpuSupplierDto> slotSpuSupplierDtos = this.findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(dto.getSlotSpuNo(),dto.getSupplierId());
            if(null!=slotSpuSupplierDtos&&slotSpuSupplierDtos.size()>0){
                this.updateRepeatMarker(slotSpuSupplierDtos);
                dto.setRepeatMarker(SlotSpuSupplierRepeatMarker.MULTI.getIndex().byteValue());
            }else{
                dto.setRepeatMarker(SlotSpuSupplierRepeatMarker.SINGLE.getIndex().byteValue());
            }
            spuSupplierGateway.insert(dto);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("save slotspusupplier error .reason :" +e.getMessage(),e);
        }
        return false;
    }

    @Override
    public HubSlotSpuSupplierDto getSlotSpuSupplierOfValidBySpuNoAndSupplierId(String slotSpuNo, String supplierId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo).andSupplierIdEqualTo(supplierId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtos = spuSupplierGateway.selectByCriteria(criteria);
        if(null!=hubSlotSpuSupplierDtos&&hubSlotSpuSupplierDtos.size()>0){
            return hubSlotSpuSupplierDtos.get(0);
        }
        return null;
    }

    @Override
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(String slotSpuNo, String supplierId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuNoEqualTo(slotSpuNo).andSupplierIdNotEqualTo(supplierId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtos = spuSupplierGateway.selectByCriteria(criteria);
        return  hubSlotSpuSupplierDtos;
    }

    @Override
    public void updateRepeatMarker(List<HubSlotSpuSupplierDto> slotSpuSupplierDtos) {
        if(null!=slotSpuSupplierDtos){
            Long slotSpuSupplierId = 0L;
            for(HubSlotSpuSupplierDto dto:slotSpuSupplierDtos){
                HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
                tmp.setSlotSpuSupplierId(dto.getSlotSpuSupplierId());
                tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.MULTI.getIndex().byteValue());
                spuSupplierGateway.updateByPrimaryKeySelective(tmp);
            }
        }
    }

    @Override
    public void resumeRepeatMarker(Long slotSpuSupplierId) {
        HubSlotSpuSupplierDto tmp = new HubSlotSpuSupplierDto();
        tmp.setSlotSpuSupplierId(slotSpuSupplierId);
        tmp.setRepeatMarker(SlotSpuSupplierRepeatMarker.SINGLE.getIndex().byteValue());
        spuSupplierGateway.updateByPrimaryKeySelective(tmp);
    }


    @Override
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListBySlotSpuId(Long slotSpuId) {
        HubSlotSpuSupplierCriteriaDto criteria = new HubSlotSpuSupplierCriteriaDto();
        criteria.createCriteria().andSlotSpuIdEqualTo(slotSpuId)
                .andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
        return  spuSupplierGateway.selectByCriteria(criteria);
    }

    @Override
    public void deleteSlotSpuSupplierForLogic(Long id) {
        HubSlotSpuSupplierDto dto = new HubSlotSpuSupplierDto();
        dto.setSlotSpuSupplierId(id);
        dto.setDataState(DataState.DELETED.getIndex());
        spuSupplierGateway.updateByPrimaryKeySelective(dto);
    }


}
