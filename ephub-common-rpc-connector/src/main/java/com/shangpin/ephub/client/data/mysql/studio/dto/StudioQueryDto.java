package com.shangpin.ephub.client.data.mysql.studio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Administrator on 2017/6/8.
 */
@Setter
@Getter
@ToString
public class StudioQueryDto {

    private String supplierId;
    private String slotNo;
    private String status;
    private String spuNo;
    private String type;
}
