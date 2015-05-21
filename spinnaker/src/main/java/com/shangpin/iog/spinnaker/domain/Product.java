package com.shangpin.iog.spinnaker.domain;


import com.shangpin.iog.dao.serialize.StringSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * PromoProducts
 */
public class Product implements Serializable {

	private static final long serialVersionUID = -254033528933541425L;

    private Long id;//代理主键

    //SPU ID;
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL, using = StringSerializer.class)
    private Long  productId;
    //货号
    private String producerId;
    //类型
    private String type;
    //季节
    private String season;
	//产品名称
    private String productNme;
    //描述
    private String description;
    //分类
    private String category;
    //图片地址
    private String url;
    //供货商价格
    private BigDecimal supplyPrice;
    //skuID
    private Long itemId;
    //尺码
    private String  itemSize;
    //条形码
    private String  barcode;
    //颜色
    private String color;
    //库存
    private Integer stock;
    //创建时间
    private Date createDate;
    //最后修改时间
    private Date lastDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getProductNme() {
        return productNme;
    }

    public void setProductNme(String productNme) {
        this.productNme = productNme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
}
