package com.shangpin.ephub.product.business.common.enumeration;

/**
 * Created by loyalty on 16/12/23.
 */
public enum ColorDic {

	
	Multicolor(1,"多色"),
    BLACK(2,"黑色" ),
    RED(3,"红色" ),
    brown(5,"棕色" ),
    BLUE(6,"蓝色" ),
    Pink(11,"粉色" ),
    Beige(15,"米色" ),
    GREEN(21,"绿色" ),
    white(22,"白色" ),
    gray(23,"灰色" ),
    YELLOW(28,"黄色" ),
    Violet(42,"紫色" ),
    orange(60,"橙色" ),
    Metallic (65,"金属色");

    /**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    private ColorDic(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
