package com.shangpin.ephub.client.data.mysql.enumeration;
/**

 *  供货商价格状态
 */
public enum SupplierPriceState {

	/**
	 * 没有价格
	 */
	NO_PRICE(Byte.valueOf("0")),
	/**
	 * 有价格
	 */
	HAVE_PRICE(Byte.valueOf("1"));
;

	private Byte index;

	private SupplierPriceState(Byte index) {
		this.index = index;
	}

	public Byte getIndex() {
		return index;
	}

	public void setIndex(Byte index) {
		this.index = index;
	}
}
