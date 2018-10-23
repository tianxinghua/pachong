package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/10/22.
 */
@Getter
@Setter
public class ProDetail {
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("custom_attributes")
    @Expose
    private List<CustomAttributes>  customAttributesList;
}
