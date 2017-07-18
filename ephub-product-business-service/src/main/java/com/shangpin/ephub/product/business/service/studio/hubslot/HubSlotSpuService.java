package com.shangpin.ephub.product.business.service.studio.hubslot;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuExportLIst;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;

/**
 * Created by loyalty on 17/6/10.
 */
public interface HubSlotSpuService {
    /**
     * 获取hubslotspu 对象
     * @param brandNo
     * @param spuModel
     * @return
     */
    public HubSlotSpuDto findHubSlotSpu(String brandNo,String spuModel);

    /**
     * 添加slo_spu
     * @param pendingProductDto
     * @return
     * @throws Exception
     */
    public boolean addSlotSpuAndSupplier(PendingProductDto pendingProductDto) throws Exception;




    /**
     * 待处理修改
     * @param pendingProductDto
     * @return
     * @throws Exception
     */
    public  boolean  updateSlotSpu(PendingProductDto pendingProductDto) throws Exception;


    /**
     * 更新slotspu 中的品牌和货号
     * @param slotSpuId
     * @param pendingProductDto
     * @return
     * @throws Exception
     */
    public  boolean updateSpuModelAndBrandNo(Long slotSpuId,PendingProductDto pendingProductDto) throws Exception;


    /**
     * 查询
     * @param queryDto
     * @return
     */
    public List<SlotSpuDto>  findSlotSpu(SpuSupplierQueryDto queryDto);

    /**
     * 查询总数
     * @param queryDto
     * @return
     */
    public int countSlotSpu(SpuSupplierQueryDto queryDto);
    /**
     * 导出已提交
     * @param queryDto
     * @return
     */
    public SlotSpuExportLIst exportSlotSpu(SpuSupplierQueryDto queryDto);

}
