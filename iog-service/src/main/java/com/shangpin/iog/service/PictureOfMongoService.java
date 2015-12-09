package com.shangpin.iog.service;

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
    public boolean isHavePic(String supplierId,String picUrl) ;
}
