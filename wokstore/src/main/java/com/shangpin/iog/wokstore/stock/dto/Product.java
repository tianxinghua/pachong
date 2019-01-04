package com.shangpin.iog.wokstore.stock.dto;


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
    private String productName;
    //描述
    private String description;
    //分类
    private String category;
    //图片地址
    private String url;
    //供货商价格
    private BigDecimal supplyPrice;
    //skuID
    private String  itemId;
    //尺码
    private String  itemSize;
    //条形码
    private String  barcode;
    //颜色
    private String color;
    //库存
    private String stock;
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

    public String getProductName() {
        return productName;
    }

    public void setProductNme(String productName) {
        this.productName = productName;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
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
