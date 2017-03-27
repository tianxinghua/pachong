package com.shangpin.picture.product.consumer.e;
/**
 * <p>Title:PicHandleState.java </p>
 * <p>Description: 图片处理状态枚举类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月3日 下午3:37:03
 */
public enum PicHandleState {
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

	private PicHandleState(Byte index) {
		this.index = index;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
}
