package com.shangpin.ephub.product.business.service.studio.hubslot;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;

import java.util.List;

/**
 * Created by loyalty on 17/6/10.
 */
public interface HubSlotSpuSupplierService {




    /**
     * 添加
     * 需要判断是否存在其它供货商的数据 ，如果存在需要修改此SLOTSPU下所有的是否多家供货商供货状态，本身状态以及供货商的操作标记
     * @param dto
     * @param slotSpuState
     * @return
     * @throws Exception
     */
    public boolean addHubSlotSpuSupplier(HubSlotSpuSupplierDto dto,Integer slotSpuState) throws  Exception;


    /**
     * 根据 slotSpuNo 和 供货商ID，查询未删除状态下是否存在
     * @param slotSpuNo
     * @param supplierId
     * @return
     */
    public HubSlotSpuSupplierDto getSlotSpuSupplierOfValidBySpuNoAndSupplierId(String slotSpuNo,String supplierId);

    /**
     * 根据 slotSpuNo 和 供货商ID，查询未删除状态下是否存在其它供货商
     * @param slotSpuNo
     * @param supplierId
     * @return
     */
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListOfOtherSupplierValidBySpuNoAndSupplierId(String slotSpuNo, String supplierId);

    /**
     * 是否多家供货的标记
     * @param slotSpuSupplierDtos
     * @param slotSpuState
     */
    public void updateOtherSupplierSignWhenHaveSomeSupplier(List<HubSlotSpuSupplierDto> slotSpuSupplierDtos,Integer slotSpuState) ;


    /**
     * 恢复独家状态
     * @param slotSpuSupplierId
     */
    public void resumeRepeatMarker(Long slotSpuSupplierId);

    /**
     * 获取同一个SPU下的所有供货商
     * @param slotSpuId
     * @return
     */
    public List<HubSlotSpuSupplierDto> findSlotSpuSupplierListBySlotSpuId(Long slotSpuId);

    /**
     * 逻辑删除slotspusupplier
     * @param id
     */
    public void deleteSlotSpuSupplierForLogic(Long id);

    /**
     * 当供货商操作数据后 更新slotSpuSupplier和slotSpu
     */
    public boolean  updateSlotSpuSupplierWhenSupplierHandle(Long slotSpuSupplierId,Integer supplierHandleState) throws Exception;

    /**
     *
     * @param slotSpuSupplierDtos
     * @return
     * @throws Exception
     */
    public boolean updateSlotSpuSupplierStateWhenModifyHubData(List<HubSlotSpuSupplierDto> slotSpuSupplierDtos,HubSlotSpuDto slotSpuDto)  throws Exception;

}
