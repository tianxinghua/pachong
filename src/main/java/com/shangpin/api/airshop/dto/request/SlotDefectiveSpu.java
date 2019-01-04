package com.shangpin.api.airshop.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
@Getter
@Setter
public class SlotDefectiveSpu  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String barcode;
    private List<String> images;
    private String supplierId;
    private String userName;
}
