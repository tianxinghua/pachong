package com.shangpin.ephub.data.mysql.product.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2017/1/6.
 */
@Getter
@Setter
@ToString
public class HubPendingDto implements Serializable {

    private Long hubSpuId;

    private Long hubSpuPendingId;



}
