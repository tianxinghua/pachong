package com.shangpin.iog.apennine.conf;
/**
 * 
 * @author sunny
 *
 */
public class ApiUrl {
	public static final String userName="spin";
	public static final String password="spin112233";
	
	/**
	 * 图片
	 */
	public static final String PICTURE="http://112.74.74.98:8082/api/GetProductImg?userName=spin&userPwd=spin112233&scode=";
	/**
	 * 所有产品
	 */
	public static final String ALLPRODUCT="http://112.74.74.98:8082/api/GetProductDetails?userName=spin&userPwd=spin112233";
	/**
	 * 库存
	 */
	public static final String STOCK="http://112.74.74.98:8082/api/GetProductStocks?userName=spin&userPwd=spin112233&scode=";
	/**
	 * 属性集合
	 */
	public static final String ATTRIBUTES="http://112.74.74.98:8082/api/GetProductPorperty?userName=spin&userPwd=spin112233&scode=";
	/**
	 * 按季节查询
	 */
	public static final String DETAILSBYCAT="http://112.74.74.98:8082/api/GetProductDetailsCat?userName=spin&userPwd=spin112233&&cat1=";
	/**
	 * 获取商品总条数
	 */
	public static final String PRODUCTCOUNT="http:// 112.74.74.98:8082/api/GetProductCount?userName=spin&userPwd=spin112233";
}
