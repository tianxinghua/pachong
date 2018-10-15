package com.shangpin.supplier.product.consumer.supplier.vipgroup.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/13.
 */
@Getter
@Setter
public class PictureResp {
    private String productId;
    private String mainPic;
    private List<String> detailPics = new ArrayList<String>();
}
