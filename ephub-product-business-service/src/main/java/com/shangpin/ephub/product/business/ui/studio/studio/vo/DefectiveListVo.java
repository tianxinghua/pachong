package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Getter
@Setter
public class DefectiveListVo implements Serializable {
    private static final long serialVersionUID = 7142770898949521535L;

    private Long id;

    private String brandName;

    private String barcode;

    private String spuName;

    private List<String> images;

    private Date createTime;
    private String createUser;

}
