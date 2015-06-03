package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by loyalty on 15/5/25.
 */
@Getter
@Setter
public class SpinnakerProductDTO implements Serializable{

    private Long id;//代理主键

    //SPU ID;
    private String  productId;
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
    private String supplyPrice;
    //具体信息 产地 材质
    private String  productDetail;
    //skuID
    private String itemId;
    //尺码
    private String  itemSize;
    //条形码
    private String  barcode;
    //颜色
    private String color;
    //库存
    private String stock;
    //明细图片地址
    private String itemPictureUrl;

    private String itemPictureUrl1="";

    private String itemPictureUrl2="";

    private String itemPictureUrl3="";

    private String itemPictureUrl4="";

    private String itemPictureUrl5="";

    private String itemPictureUrl6="";

    private String itemPictureUrl7="";

    private String itemPictureUrl8="";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    public void setProductName(String productName) {
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

    public String getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(String supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
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

    public String getItemPictureUrl() {
        return itemPictureUrl;
    }

    public void setItemPictureUrl(String itemPictureUrl) {
        this.itemPictureUrl = itemPictureUrl;
    }

    public String getItemPictureUrl1() {
        return itemPictureUrl1;
    }

    public void setItemPictureUrl1(String itemPictureUrl1) {
        this.itemPictureUrl1 = itemPictureUrl1;
    }

    public String getItemPictureUrl2() {
        return itemPictureUrl2;
    }

    public void setItemPictureUrl2(String itemPictureUrl2) {
        this.itemPictureUrl2 = itemPictureUrl2;
    }

    public String getItemPictureUrl3() {
        return itemPictureUrl3;
    }

    public void setItemPictureUrl3(String itemPictureUrl3) {
        this.itemPictureUrl3 = itemPictureUrl3;
    }

    public String getItemPictureUrl4() {
        return itemPictureUrl4;
    }

    public void setItemPictureUrl4(String itemPictureUrl4) {
        this.itemPictureUrl4 = itemPictureUrl4;
    }

    public String getItemPictureUrl5() {
        return itemPictureUrl5;
    }

    public void setItemPictureUrl5(String itemPictureUrl5) {
        this.itemPictureUrl5 = itemPictureUrl5;
    }

    public String getItemPictureUrl6() {
        return itemPictureUrl6;
    }

    public void setItemPictureUrl6(String itemPictureUrl6) {
        this.itemPictureUrl6 = itemPictureUrl6;
    }

    public String getItemPictureUrl7() {
        return itemPictureUrl7;
    }

    public void setItemPictureUrl7(String itemPictureUrl7) {
        this.itemPictureUrl7 = itemPictureUrl7;
    }

    public String getItemPictureUrl8() {
        return itemPictureUrl8;
    }

    public void setItemPictureUrl8(String itemPictureUrl8) {
        this.itemPictureUrl8 = itemPictureUrl8;
    }

    public  void setItemPic(){
        if(null==itemPictureUrl||"".equals(itemPictureUrl)){
            return ;
        }
        String[] urlArray = itemPictureUrl.split(",");
        if(null!=urlArray&&urlArray.length>0){
            for(int i=0;i<urlArray.length;i++){
                switch (i){
                    case 0:
                        itemPictureUrl1=urlArray[i];
                        break;
                    case 1:
                        itemPictureUrl2=urlArray[i];
                        break;
                    case 2:
                        itemPictureUrl3=urlArray[i];
                        break;
                    case 3:
                        itemPictureUrl4=urlArray[i];
                        break;
                    case 4:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 5:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 6:
                        itemPictureUrl7=urlArray[i];
                        break;
                    case 7:
                        itemPictureUrl8=urlArray[i];
                        break;


                }
            }
        }
    }


}
