package com.shangpin.ep.order.conf.supplier;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Setter
@Getter
@ToString
public class Supplier extends SupplierCommon implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1608829698675620854L;

	private String serviceUrl;

    private String orderSupplierMapping;

    private String stockSupplierMapping;
	/*
	延时时间
	 */
	private int delayTime;

	/**
	 * 重试次数
	 */
	private int retry;
	/**
	 * 获取PID的服务地址
	 */
	private String pidUrl;
	/**
	 * 获取产品的服务地址
	 */
	private String orderUrl;
	/**
	 * 从ephub获取尺码服务
	 */
	private String getSizeFromEphubUrl;

	//从ephub获取供货商原始信息

	private  String productSpuUrl;
}
