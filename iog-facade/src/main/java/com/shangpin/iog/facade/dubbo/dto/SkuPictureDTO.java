package com.shangpin.iog.facade.dubbo.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/16.
 */
@Setter
@Getter
public class SkuPictureDTO {

    private String skuId;

    private String imageUrl; // 多张图片已 || 分割


    @Override
    public String toString() {
        return "SkuPictureDTO{" +
                "skuId='" + skuId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
