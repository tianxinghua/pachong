package com.shangpin.supplier.product.consumer.supplier.mclables.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Variantinfo {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Size")
    @Expose
    private String size;
    @SerializedName("SizeType")
    @Expose
    private String sizeType;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("LastUpdatedDatetimeUtc")
    @Expose
    private String lastUpdatedDatetimeUtc;

}
