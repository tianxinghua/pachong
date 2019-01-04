package com.shangpin.api.airshop.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * 采购单实体类
 * 
 * @author wanghua
 *
 */
@Getter
@Setter
public class DeliveryOrdersRQ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 发货单结束时间
	private String updateTimeBegin;
	// 发货单更新开始时间
	private String updateTimeEnd;
	// 物流单号
	private String logisticsOrderNo;
	// 依据发货单状态查询发货单； 格式：List<int>； 状态值描述：1=待发货，2=待收货， 4=已完成
	private List<String> deliveryStatus = new ArrayList<String>();
	// 发货单号(包裹单号)
	private String sopDeliverOrderNo;
	// 当前页
	private String start;
	// 每页大小
	private String length;
	// 请求参数
	private String draw;
	// 系统用户编码
	private String sopUserNo;

	//该商品所在采购单的采购单号
	private String sopPurchaseOrderNo;

	//品牌编号
	private String brandNo;

	
    /***
     * 转换日期格式 dd/MM/yyyy 为 yyyy-MM-dd
     * @param dateString
     * @return
     */
	public static String covertDate(String dateString) {
		try {
			if (!StringUtils.isEmpty(dateString) && dateString.indexOf("/") != -1) {
				SimpleDateFormat f2 = new SimpleDateFormat("MM/dd/yyyy");
				SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
				return f1.format(f2.parse(dateString));
			}else if (!StringUtils.isEmpty(dateString) && dateString.indexOf("-") != -1) {
				SimpleDateFormat f2 = new SimpleDateFormat("MM-dd-yyyy");
				SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
				return f1.format(f2.parse(dateString));
			}
			return "";
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	} 
}
