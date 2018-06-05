package com.shangpin.ephub.product.business.ui.task.dic.DicExportDto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class HubColorDic implements Serializable{
    protected Integer pageNo = 1;
    protected Integer startRow;
    protected Integer pageSize = 10;
    protected String fields;
    protected String createName;
    protected String supplierColorName;
    protected String hubColorName;
    protected String startTime;
    protected String endTime;


}
