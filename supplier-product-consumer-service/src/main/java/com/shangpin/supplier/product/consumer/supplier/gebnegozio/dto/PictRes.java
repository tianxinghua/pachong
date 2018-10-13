package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/13.
 */
@Getter
@Setter
public class PictRes {
    private String id;
    private String media_type;
    private String label;
    private String position;
    private String disabled;
    private List<String> types = new ArrayList<String>();
    private String file;
}
