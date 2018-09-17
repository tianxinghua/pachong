package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/16.
 */
@Getter
@Setter
public class ExtAttr {
    @SerializedName("return_to_stock_items")
    @Expose
    private List<Integer> returnToStockItems = new ArrayList<Integer>();
}
