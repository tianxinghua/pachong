package com.shangpin.iog.theclutcher.stock.dto;

public class Product
{
  private String skuID;
  private String stock;

  public void setSkuID(String skuID)
  {
    this.skuID = skuID; } 
  public void setStock(String stock) { this.stock = stock;
  }

  public String getSkuID() {
    return this.skuID; } 
  public String getStock() { return this.stock;
  }
}