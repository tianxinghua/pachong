package com.shangpin.iog.anatwine.utils;

import java.util.ResourceBundle;

public class Constant {
	 public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
	    //供应商ID
	    public static String SUPPLIER_ID = bundle.getString("supplierId");
	    public static String merchantId =   bundle.getString("merchantId");
	    public static String token =   bundle.getString("token");
	    //public static String SUPPLIER_ID = "2015101501608";
	    public static String TEXT_JSON = "text/xml";
	    public static String APPLICATION_JSON = "application/xml";
	    public static String EVENTS_URL = "http://www.cs4b.eu/ws/getEvents";
	    /**
	     * test
	     * @param args
	     */
	    public static void main(String[] args) {


	    }
}
