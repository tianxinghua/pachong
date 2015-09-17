package com.shangpin.iog.facade.dubbo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/16.
 */
@Getter
@Setter
public class SpuPictureDTO {

    private String spuId;

    private String imageUrl;// 多张图片已 || 分割

    @Override
    public String toString() {
        return "SpuPictureDTO{" +
                "spuId='" + spuId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
