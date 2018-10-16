package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/29.
 */
@Getter
@Setter
public class OptionsConf {
    @SerializedName("attribute_id")
    @Expose
    private String attributeId;
    @SerializedName("options")
    @Expose
    private List<Options> options = new ArrayList<Options>();
}
