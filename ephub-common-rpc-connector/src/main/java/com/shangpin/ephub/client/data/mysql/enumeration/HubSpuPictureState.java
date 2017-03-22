package com.shangpin.ephub.client.data.mysql.enumeration;

public enum HubSpuPictureState {
	/**
	 * 未处理
	 */
	UNHANDLED(Byte.valueOf("0")),
	/**
	 * 处理完成
	 */
	HANDLED(Byte.valueOf("1"));
	private Byte index;

	private HubSpuPictureState(Byte index) {
		this.index = index;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
}
