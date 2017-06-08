package com.shangpin.ephub.data.mysql.studio.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/8.
 */
@Setter
@Getter
@ToString
public class StudioSpu implements Serializable {

    private static final long serialVersionUID = 7692545019994478857L;

    private String supplierNo;
    private String spuNo;
    private String brandNo;
    private String brandName;
    private String categoryNo;
    private String categoryName;
    private String Season;
    private Date spuDate;

}
