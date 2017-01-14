package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:IsExportPic </p>
 * <p>Description: 是否导出图 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月14日 下午7:23:04
 *
 */
public enum IsExportPic {

	/**
	 * 带图片
	 */
	YES(1),
	/**
	 * 不带图片
	 */
	NO(0);
	
	
	private int index;

	private IsExportPic(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
