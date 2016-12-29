package com.shangpin.ephub.data.mysql.product.common;

/**
 * Created by loyalty on 16/12/21.
 */
public enum HubSpuPendigStatus {

    WAIT_HANDLE(0,"信息待完善"),
    WAIT_AUDIT(1,"待复核"),
    HANDLED(2,"已处理"),
    NO_WAY_HANDLE(3,"无法处理"),
    FILTER(4,"过滤不处理"),
    HANDLING(5,"复核中");
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
    private HubSpuPendigStatus(Integer index, String description) {
        this.index = index;
        this.description = description;
    }
}
