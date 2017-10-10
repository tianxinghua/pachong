package com.shangpin.ephub.client.data.mysql.enumeration;

public enum Isexistpic {

	/**
	 * 不存在
	 */
	NO(Byte.valueOf("0")),
	/**
	 * 存在
	 */
	YES(Byte.valueOf("1")),
	/**
	 * 通过Air Studio上传
	 */
	AIR_STUDIO_UPLOAD(Byte.valueOf("2"));
	
	
	
	private Byte index;

	private Isexistpic(Byte index) {
		this.index = index;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
}
