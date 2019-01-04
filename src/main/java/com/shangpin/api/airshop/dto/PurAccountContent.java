package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**结算单列表返回详细信息
 * @author wanghua
 *
 */
@Getter
@Setter
public class PurAccountContent implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sopPurchaseOrderDetailNo;
	private String sopPurchaseOrderNo;
	private String skuNo;
	private String SupplierSkuNo;
	private String detailStatus;
	private String detailStatusName;
	private String productModel;
	private String barCode;	
	private String picUrl;
	private String warehouseNo;	
	private String warehouseName;
	private String warehouseAddress;
	private String warehousePost;
	private String warehouseContactPerson;
	private String warehouseContactMobile;
	private String skuPrice;
    private String skuPriceCurrency;
	private String sopDeliverOrderNo;
	private String logisticsName;
	private String logisticsOrderNo;
	private String isStock;
	private String brandNo;
	private String brandName;
    private String qty;
    private String createTime;
    private String dateArrival;
    private String dateCanceled; 
    private String productName;
    private String size;
    private String color;
    /**
     * 币转换
     * @param currency
     * @return
     */
    public static String converSkuPriceCurrency(String currency){
    	if (!StringUtils.isEmpty(currency)) {
			if(currency.equals("0")){
				return "RMB";
			}else if (currency.equals("1")) {
				return "Euro";
			}else if (currency.equals("2")){
				return "USD";
			}else if (currency.equals("3")){
				return "Pound";
			}else if (currency.equals("4")){
				return "HKD";
			}else if (currency.equals("5")){
				return "KRW";
			}else if (currency.equals("6")){
				return "AUD";
			}else if (currency.equals("7")){
				return "SGD";
			}else {
				return "";
			}
    	}
		return "";
    }

}
