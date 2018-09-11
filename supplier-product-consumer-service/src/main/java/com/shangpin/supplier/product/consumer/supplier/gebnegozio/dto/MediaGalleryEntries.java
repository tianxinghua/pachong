package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class MediaGalleryEntries {
    private String id;
    private String media_type;
    private String label;
    private String position;
    private String disabled;
    private List<String> types;
    private String file;
}
