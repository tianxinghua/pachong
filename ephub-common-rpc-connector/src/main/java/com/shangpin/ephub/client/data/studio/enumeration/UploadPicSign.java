package com.shangpin.ephub.client.data.studio.enumeration;
/**
 * <p>Title: UploadPicSign</p>
 * <p>Description: 标记一件产品是否已经上传了图片 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月6日 下午2:59:58
 *
 */
public enum UploadPicSign {
	
	 NOT_YET_UPLOAD(0,"未上传图片"),
	 
	 HAVE_UPLOADED(1,"已上传图片");

	/**
     * 数字索引标识
     */
    private Integer index;
    /**
     * 描述信息
     */
    private String description;

    UploadPicSign(Integer index, String description){
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
