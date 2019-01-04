package com.shangpin.iog.atelier.purchase.dto;

/**
 * Created by Administrator on 2015/10/8.
 */
public class AtelierOrder {
    private String barCodeAll;
    private String soapRequestData;
    private String opName;
    private String skuNo;
    private String excCode;
    private String excDes;
    private String status;

    public String getBarCodeAll() {
        return barCodeAll;
    }

    public void setBarCodeAll(String barCodeAll) {
        this.barCodeAll = barCodeAll;
    }

    public String getSoapRequestData() {
        return soapRequestData;
    }

    public void setSoapRequestData(String soapRequestData) {
        this.soapRequestData = soapRequestData;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public String getExcCode() {
        return excCode;
    }

    public void setExcCode(String excCode) {
        this.excCode = excCode;
    }

    public String getExcDes() {
        return excDes;
    }

    public void setExcDes(String excDes) {
        this.excDes = excDes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
