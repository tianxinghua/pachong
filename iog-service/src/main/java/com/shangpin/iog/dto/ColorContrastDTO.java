package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by huxia on 2015/6/15.
 */
@Getter
@Setter
public class ColorContrastDTO {
    private String color;
    private String colorCh;


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getColorCh() {
        return colorCh;
    }

    public void setColorCh(String colorCh) {
        this.colorCh = colorCh;
    }
}
