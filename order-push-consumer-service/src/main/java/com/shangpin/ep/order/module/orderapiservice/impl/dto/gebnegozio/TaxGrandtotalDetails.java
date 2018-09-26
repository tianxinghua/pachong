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
public class TaxGrandtotalDetails {
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("rates")
    @Expose
    private List<Rates> rates;
    @SerializedName("group_id")
    @Expose
    private String groupId;
}
