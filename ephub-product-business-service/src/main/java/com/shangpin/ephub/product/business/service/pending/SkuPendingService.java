package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;

import java.util.List;

/**
 * Created by lizhongren on 2017/11/23.
 */
public interface SkuPendingService {

    /**
     * 设置SKU尺码
     * @param spuPendingId
     * @param hubBrand
     * @param hubCategory
     * @return  如果获取不到信息 返回false
     */
    public boolean setWaitHandleSkuPendingSize(Long spuPendingId,String hubBrand,String hubCategory);


    /**
     * 设置传入的skupending 的尺码  0:成功 1:无映射的  2:有映射的但同时有无法处理的
     * @param spuPendingId :spuPendingId
     * @param hubBrand  : 尚品品牌编号
     * @param hubCategory : 尚品品类编号
     * @param skuPendings 传入的需要处理的SKU信息
     * @param skuVOs  返回信息中的SKU信息
     * @return    0:成功 1:无映射的  2:有映射的但同时有无法处理的
     */
    public String  setWaitHandleSkuPendingSize(Long spuPendingId,String hubBrand,String hubCategory,List<HubSkuPendingDto> skuPendings,List<PendingSkuUpdatedVo> skuVOs  );

    /**
     * 获取待处理的SKU
     * @param spuPendingId
     * @return
     */
    public List<HubSkuPendingDto> getWaitHandleSkuPending(Long spuPendingId);

    /**
     * 审核前尺码判断 状态直接赋值到传入参数中
     * @param hubSpuPending
     */
    public  void judgeSizeBeforeAudit(HubSpuPendingDto hubSpuPending);

}
