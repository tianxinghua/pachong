package com.shangpin.api.airshop.product.v;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>Title:ProductVO </p>
 * <p>Description:商品视图 </p>
 * <p>Company: shangpin</p> 
 * @author : yanxiaobin
 * @date :2016年4月22日 下午3:12:44
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ProductVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 554711832507741986L;
	private Long supplierSkuId;
	/**
	 * 颜色
	 */
	private String colour;
	/**
	 * Sku材质
	 */
	private String material;
	private String type;//1save,2submmit
	private String addOrupdate;// add update
	/**
	 * 商品分类
	 */
	private String category;
	/**
	 * 商品品牌
	 */
	private String brand;
	/**
	 * 商品编码
	 */
	private String spuId;
	private String productCode;
	/**
	 * 商品名称
	 */
	private String spuName;
	/**
	 * 条形码
	 */
	private String barCode;
	/**
	 * 市场价格
	 */
	private String marketPrice;
	/**
	 * 供货价格
	 */
	private String supplyPrice;
	/**
	 * 币种
	 */
	private String currency;
	/**
	 * 	性别：0未选择 1男 2女 3中性
	 */
	private String gender;
	/**
	 * 上市年份 从2012年至2018年，默认为当前年份
	 */
	private String year;
	/**
	 * 1春，2夏，3秋，4冬，5春夏，6秋冬，默认为当前季节，春夏和秋冬优先
	 */
	private String season;
	/**
	 * 商品产地
	 */
	private String madeIn;
	/**
	 * sku集合
	 */
	private Set<Sku> skus;
	/**
	 * 商品图片，默认第一张为主图
	 */
	private List<String> images;
	/**
	 * 商品描述信息
	 */
	private String description;
}
