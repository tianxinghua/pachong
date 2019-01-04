package com.shangpin.api.airshop.dto.direct;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 18:10 2018/10/8
 * @Description: 真直发 发货列表页面 DTO
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectDeliveryOrderRS {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String total;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private List<DirectDeliverOrder> deliverOrder;

}
