package com.shangpin.ephub.product.business.rest.picture.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/1.
 */
public interface ISelectHubSpuPicService {
    /**
     * 根据sp_spu_no查询图片url
     * @param spSpuNo
     * @return
     */
    public List<String> selectHubSpuPic(String spSpuNo);

}
