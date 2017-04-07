package com.shangpin.ephub.price.consumer.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by lizhongren on 2017/4/5.
 * 价格处理返回结果对象
 */
@Getter
@Setter
@NoArgsConstructor
public class PriceHandleMessageDTO {

    private List<String> successSpSkuNoList;

    private List<String> errorSpSkuNoList;

}
