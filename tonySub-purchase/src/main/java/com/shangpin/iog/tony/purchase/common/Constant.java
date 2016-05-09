package com.shangpin.iog.tony.purchase.common;

import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class Constant {
    
	public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    //供应商ID
    public static String SUPPLIER_ID = bundle.getString("supplierId");
    //供应商编号 ""
    public static final String SUPPLIER_NO =bundle.getString("supplierNo");
    //供应商ID
    public static String MERCHANT_ID = bundle.getString("merchantId");
    //供应商ID
    public static String TOKEN = bundle.getString("token");
    //请求地址
    public static String url = bundle.getString("url");
    //订单状态
    public static String CANCELED = "CANCELED";
    public static String PENDING = "PENDING";
    public static String CONFIRMED = "CONFIRMED";

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
         System.out.println("Constant.SUPPLIER_ID=" + Constant.SUPPLIER_ID);
    }
}
