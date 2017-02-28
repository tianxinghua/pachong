package com.shangpin.iog.sanremo.stock.dto;


import java.util.List;

public class ResponseDTO
{
  private String TotalSku;
  private String TotalQty;
  private List<Stock> listStockData;

  public void setTotalSku(String TotalSku)
  {
    this.TotalSku = TotalSku; } 
  public void setTotalQty(String TotalQty) { this.TotalQty = TotalQty; } 
  public void setListStockData(List<Stock> listStockData) { this.listStockData = listStockData;
  }

  public String getTotalSku() {
    return this.TotalSku; } 
  public String getTotalQty() { return this.TotalQty; } 
  public List<Stock> getListStockData() { return this.listStockData;
  }
}