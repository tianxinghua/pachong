package com.shangpin.iog.gilt.dto;

/**
 * Created by sunny on 2015/8/6.
 */
public class AttributeDTO {
    private ColorDTO color;
    private String style;
    private SizeDTO size;
    private String material;

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public SizeDTO getSize() {
        return size;
    }

    public void setSize(SizeDTO size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
