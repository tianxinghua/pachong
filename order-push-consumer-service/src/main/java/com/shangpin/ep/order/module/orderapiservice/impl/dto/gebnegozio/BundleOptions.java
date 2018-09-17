package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class BundleOptions {
    @SerializedName("option_id")
    @Expose
    private String optionId;
    @SerializedName("option_qty")
    @Expose
    private String optionQty;
    @SerializedName("option_selections")
    @Expose
    private List<String> optionSelections;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
}
