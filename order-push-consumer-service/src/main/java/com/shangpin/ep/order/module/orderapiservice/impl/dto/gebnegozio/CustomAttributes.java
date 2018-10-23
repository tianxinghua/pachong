package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class CustomAttributes {
    @SerializedName("attribute_code")
    @Expose
    private String attributeCode;
    @SerializedName("value")
    @Expose
    private Object value;

    public CustomAttributes(String attributeCode, Object value){
        this.attributeCode = attributeCode;
        this.value = value;
    }

    @Override
    public String toString() {
        return "CustomAttributes{" +
                "attributeCode='" + attributeCode + '\'' +
                ", value=" + value +
                '}';
    }

}
