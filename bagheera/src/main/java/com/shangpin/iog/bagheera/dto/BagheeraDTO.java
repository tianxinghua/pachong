package com.shangpin.iog.bagheera.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunny on 2015/9/9.
 */
@Getter
@Setter
public class BagheeraDTO {
    private String LAST_UPDATE;
    private String SUPPLIER_CODE;
    private String CODE;
    private String DEPT;
    private String CATEGORY;
    private String ITEM_GROUP;
    private String MATERIAL;
    private String SIZE_AND_FIT;
    private String MADE;
    private String DESCRIPTION;
    private String COLOR;
    private String SIZE;
    private String STOCK;
    private String COLLECTION;
    private String LIST_PRICE;
    private String RETAIL_PRICE;
    private String DISCOUNTED;
    private String DISCOUNT;
    private String LASO_Price;
    private String CURRENCY;
    private String IMAGE_URL1;
    private String IMAGE_URL2;
    private String IMAGE_URL3;
    private String IMAGE_URL4;
    
    public String getLAST_UPDATE() {
        return LAST_UPDATE;
    }

    public void setLAST_UPDATE(String LAST_UPDATE) {
        this.LAST_UPDATE = LAST_UPDATE;
    }

    public String getSUPPLIER_CODE() {
        return SUPPLIER_CODE;
    }

    public void setSUPPLIER_CODE(String SUPPLIER_CODE) {
        this.SUPPLIER_CODE = SUPPLIER_CODE;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getDEPT() {
        return DEPT;
    }

    public void setDEPT(String DEPT) {
        this.DEPT = DEPT;
    }

    public String getITEM_GROUP() {
        return ITEM_GROUP;
    }

    public void setITEM_GROUP(String ITEM_GROUP) {
        this.ITEM_GROUP = ITEM_GROUP;
    }

    public String getMATERIAL() {
        return MATERIAL;
    }

    public void setMATERIAL(String MATERIAL) {
        this.MATERIAL = MATERIAL;
    }

    public String getSIZE_AND_FIT() {
        return SIZE_AND_FIT;
    }

    public void setSIZE_AND_FIT(String SIZE_AND_FIT) {
        this.SIZE_AND_FIT = SIZE_AND_FIT;
    }

    public String getMADE() {
        return MADE;
    }

    public void setMADE(String MADE) {
        this.MADE = MADE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    public String getSIZE() {
        return SIZE;
    }

    public void setSIZE(String SIZE) {
        this.SIZE = SIZE;
    }

    public String getSTOCK() {
        return STOCK;
    }

    public void setSTOCK(String STOCK) {
        this.STOCK = STOCK;
    }

    public String getCOLLECTION() {
        return COLLECTION;
    }

    public void setCOLLECTION(String COLLECTION) {
        this.COLLECTION = COLLECTION;
    }

    public String getLIST_PRICE() {
        return LIST_PRICE;
    }

    public void setLIST_PRICE(String LIST_PRICE) {
        this.LIST_PRICE = LIST_PRICE;
    }

    public String getDISCOUNTED() {
        return DISCOUNTED;
    }

    public void setDISCOUNTED(String DISCOUNTED) {
        this.DISCOUNTED = DISCOUNTED;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public void setDISCOUNT(String DISCOUNT) {
        this.DISCOUNT = DISCOUNT;
    }

    public String getLASO_Price() {
        return LASO_Price;
    }

    public void setLASO_Price(String LASO_Price) {
        this.LASO_Price = LASO_Price;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String CURRENCY) {
        this.CURRENCY = CURRENCY;
    }

   

	public String getCATEGORY() {
		return CATEGORY;
	}

	public void setCATEGORY(String cATEGORY) {
		CATEGORY = cATEGORY;
	}
    
}
