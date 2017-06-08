package com.shangpin.ephub.data.mysql.studio.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/8.
 */
@Setter
@Getter
@ToString
public class StudioCriteria implements Serializable {

    private String supplierId;
    private String slotNo;
    private String status;
    private String spuNo;
    private String type;
}
