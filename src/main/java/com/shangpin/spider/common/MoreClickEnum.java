package com.shangpin.spider.common;
/**
 * 多层点击的全限定名的枚举类
 * @author njt
 * @date 2018年10月9日 下午4:46:28
 * @desc
 * MoreClickEnum
 */
public enum MoreClickEnum {
	ONE("com.shangpin.spider.gather.utils.OneClick",1),
	TWO("com.shangpin.spider.gather.utils.TwoClick",2),
	THREE("com.shangpin.spider.gather.utils.ThreeClick",3);
	private String name;
	private int index;
	private MoreClickEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public static String getName(int index) {
		for (MoreClickEnum moreClick : MoreClickEnum.values()) {
			if(moreClick.getIndex()==index) {
				return moreClick.getName();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
