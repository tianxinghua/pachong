package com.shangpin.ephub.client.data.mysql.enumeration;
/**
 * <p>Title:PicState </p>
 * <p>Description: 图片状态</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月19日 下午7:34:37
 *
 */
public enum PicState {
	
	/**
	 * 未处理
	 */
	UNHANDLED(Byte.valueOf("0")),
	/**
	 * 处理异常
	 */
	HANDLE_ERROR(Byte.valueOf("1")),
	/**
	 * 处理完成
	 */
	HANDLED(Byte.valueOf("2"));
	private Byte index;

	private PicState(Byte index) {
		this.index = index;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
}
