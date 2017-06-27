package com.shangpin.ephub.product.business.ui.studio.common.operation.enumeration;
/**
 * <p>Title: OperationQueryType</p>
 * <p>Description: 指定页面 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月26日 下午3:51:30
 *
 */
public enum OperationQueryType {
	
	/**
	 * OPEN_BOX页
	 */
	OPEN_BOX(0,"OPEN_BOX"),
	/**
	 * IMAGE_UPLOAD页
	 */
	IMAGE_UPLOAD(1,"IMAGE_UPLOAD"),
	/**
	 * INFO_CORRECTING页
	 */
	INFO_CORRECTING(2,"INFO_CORRECTING");
	
	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    OperationQueryType(Integer index, String description){
		this.index = index;
		this.description = description;
	}
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

}
