package com.shangpin.ephub.product.business.service.studio.hubslotpic;

import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.dto.SlotSpuPic;

import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/7/11.
 *
 * 图片业务处理
 *
 */
public interface SlotPicService {

    /**
     * 获取slotspu的图片
     * @param brandNo
     * @param spuModel
     * @return
     */
    public SlotSpuPic findSpuPicByBrandNoAndSpuModel(String brandNo,String spuModel);


    /**
     * 根据slotSpuId 按供货商门户ID  拆分数据

     * @param slotSpuId
     * @return
     */
    public Map<String,List<HubSlotSpuPicDto>>  findShootPic(Long slotSpuId);

}
