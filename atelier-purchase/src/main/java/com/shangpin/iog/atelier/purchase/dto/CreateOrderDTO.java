package com.shangpin.iog.atelier.purchase.dto;

/**
 * Created by Administrator on 2015/9/28.
 */
public class CreateOrderDTO {

    private String ID_ORDER_WEB;
    private String ID_CLIENTE_WEB;
    private String DESTINATIONROW1;
    private String DESTINATIONROW2;
    private String DESTINATIONROW3;
    private String BARCODE;
    private String QTY;
    private String PRICE;

    public String getID_ORDER_WEB() {
        return ID_ORDER_WEB;
    }

    public void setID_ORDER_WEB(String ID_ORDER_WEB) {
        this.ID_ORDER_WEB = ID_ORDER_WEB;
    }

    public String getID_CLIENTE_WEB() {
        return ID_CLIENTE_WEB;
    }

    public void setID_CLIENTE_WEB(String ID_CLIENTE_WEB) {
        this.ID_CLIENTE_WEB = ID_CLIENTE_WEB;
    }

    public String getDESTINATIONROW1() {
        return DESTINATIONROW1;
    }

    public void setDESTINATIONROW1(String DESTINATIONROW1) {
        this.DESTINATIONROW1 = DESTINATIONROW1;
    }

    public String getDESTINATIONROW2() {
        return DESTINATIONROW2;
    }

    public void setDESTINATIONROW2(String DESTINATIONROW2) {
        this.DESTINATIONROW2 = DESTINATIONROW2;
    }

    public String getDESTINATIONROW3() {
        return DESTINATIONROW3;
    }

    public void setDESTINATIONROW3(String DESTINATIONROW3) {
        this.DESTINATIONROW3 = DESTINATIONROW3;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }
}
