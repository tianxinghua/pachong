package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/15.
 */
@Getter
@Setter
public class PaymentMethod {

    @SerializedName("po_number")
    @Expose
    private String poNumber;

    @SerializedName("method")
    @Expose
    private String method;

    @SerializedName("additional_data")
    @Expose
    private List<String> additionalData = new ArrayList<String>();

    @SerializedName("additional_data")
    @Expose
    private ExtensionAttributes extensionAttributes;
}
