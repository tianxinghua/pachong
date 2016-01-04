package com.shangpin.iog.levelgroup.purchase.common;

public enum OrderState {
	
    /** 支付成功 */
	CONFIRMED 
	{public String getName(){return "confirmed";}},
    /** 退款成功 */
	REFUNDED {public String getName(){return "refunded";}};
    
    public abstract String getName();

}
