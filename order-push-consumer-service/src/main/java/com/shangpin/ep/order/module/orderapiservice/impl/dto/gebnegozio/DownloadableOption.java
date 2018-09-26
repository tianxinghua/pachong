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
public class DownloadableOption {
    @SerializedName("downloadable_links")
    @Expose
    private List<String> downloadableLinks;
}
