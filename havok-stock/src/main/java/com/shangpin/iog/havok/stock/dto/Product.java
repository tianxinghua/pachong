package com.shangpin.iog.havok.stock.dto;

/**
 * Created by huxia on 2015/10/15.
 */
public class Product {
    private String SPUID;//spuId
    private String SKUID;//skuId
    private String productName;//商品名称
    private String marketPrice;//市场价
    private String barcode;
    private String productCode;
    private String color;
    private String productDescription;//产品描述
    private String saleCurrency;//货币类型
    private String productSize;//尺寸
    private String stock;//库存

    public String getSPUID() { return SPUID; }

    public void setSPUID(String SPUID) { this.SPUID = SPUID; }

    public String getSKUID() { return SKUID; }

    public void setSKUID(String SKUID) { this.SKUID = SKUID; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getMarketPrice() { return marketPrice; }

    public void setMarketPrice(String marketPrice) { this.marketPrice = marketPrice; }

    public String getBarcode() { return barcode; }

    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getProductCode() { return productCode; }

    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getProductDescription() { return productDescription; }

    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public String getSaleCurrency() { return saleCurrency; }

    public void setSaleCurrency(String saleCurrency) { this.saleCurrency = saleCurrency; }

    public String getProductSize() { return productSize; }

    public void setProductSize(String productSize) { this.productSize = productSize; }

    public String getStock() { return stock; }

    public void setStock(String stock) { this.stock = stock; }

}
