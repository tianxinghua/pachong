package com.shangpin.iog.tony.purchase.dto;

/**
 * Created by sunny on 2015/8/6.
 */
public class AttributeDTO {
    private ColorDTO color;
    private StyleDTO style;
    private SizeDTO size;
    private MaterialDTO material;

    public ColorDTO getColor() {
        return color;
    }

    public void setColor(ColorDTO color) {
        this.color = color;
    }

    public StyleDTO getStyle() {
        return style;
    }

    public void setStyle(StyleDTO style) {
        this.style = style;
    }

    public SizeDTO getSize() {
        return size;
    }

    public void setSize(SizeDTO size) {
        this.size = size;
    }

    public MaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(MaterialDTO material) {
        this.material = material;
    }
}
