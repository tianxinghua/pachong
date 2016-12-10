package com.shangpin.ep.order.module.order.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Getter
@Setter
public class SkuSearchResult {
    String responseCode; // 0 正确 非零 失败
    String responseMsg;
    List<SkuCountDTO> resutl;
}
