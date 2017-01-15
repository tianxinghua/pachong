package com.shangpin.ephub.product.business.service.hub;

import com.shangpin.ephub.product.business.service.hub.dto.HubProductIdDto;

/**
 * Created by lizhongren on 2016/12/30.
 * hub的业务处理
 */
public interface HubProductService {

    /**
     * 根据传入的商品信息 查找 hubspu hubsku hubskusuppliermaping 查找到
     * @param hubProductIdDto
     * @throws Exception
     */
    public void sendHubProuctToScm(HubProductIdDto hubProductIdDto) throws Exception;
}
