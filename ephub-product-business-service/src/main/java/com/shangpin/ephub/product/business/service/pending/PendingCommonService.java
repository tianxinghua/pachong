package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;

/**
 * Created by lizhongren on 2017/1/19.
 */
public interface PendingCommonService {



    /**
     * 处理pending 产品数据  主要负责验证 合并 hub 插入hubsku 以及映射
     * @param pendingSpu
     * @return
     * @throws Exception
     */
    public boolean handlePendingProduct(PendingSpu pendingSpu) throws Exception;
}
