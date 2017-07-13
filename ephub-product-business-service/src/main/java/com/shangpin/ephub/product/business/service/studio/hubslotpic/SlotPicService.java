package com.shangpin.ephub.product.business.service.studio.hubslotpic;

import com.shangpin.ephub.product.business.service.studio.hubslotpic.dto.SlotSpuPic;

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

}
