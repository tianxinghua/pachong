package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by fanhaiying on 2015/6/15.
 */
@Getter
@Setter
public class MaterialContrastDTO {
    private String material;
    private String materialCh;
    public String getMaterialCh() {
        return materialCh;
    }

    public void setMaterialCh(String materialCh) {
        this.materialCh = materialCh;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }





}
