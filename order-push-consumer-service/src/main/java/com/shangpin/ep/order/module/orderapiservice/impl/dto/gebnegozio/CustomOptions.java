package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class CustomOptions {
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("option_value")
    @Expose
    private String optionValue;
    @SerializedName("extension_attributes")
    @Expose
    private List<ExtensionAttributes> extensionAttributes = new ArrayList<ExtensionAttributes>();
}
