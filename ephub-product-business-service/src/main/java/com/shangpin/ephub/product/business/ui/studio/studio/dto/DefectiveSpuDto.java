package com.shangpin.ephub.product.business.ui.studio.studio.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Setter
@Getter
public class DefectiveSpuDto implements Serializable {
    private static final long serialVersionUID = 2463211654311012415L;

    private Long id;
    private String barcode;
    private List<String> images;
    private String userName;
    private String supplierId;

}
