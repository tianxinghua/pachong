package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.PictureDTO;

import java.util.List;

/**
 * Created by lizhongren on 2015/12/7.
 */
public interface PictureOfMongoService {
    /**
     * 供货商是否已有此图片
     * 为了供货商后补充的数据
     * @param supplierId
     * @param picUrl
     * @return
     */
    public boolean isHavePic(String supplierId, String picUrl) ;

    /**
     * 根据供货商门户id删除图片
     * @param supplierId
     */
    public void removePicBySupplierId(String supplierId) throws ServiceException;

}
