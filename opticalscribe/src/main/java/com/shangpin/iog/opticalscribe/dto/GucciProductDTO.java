package com.shangpin.iog.opticalscribe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 14:12 2018/6/21
 * @Description:
 */
@Setter
@Getter
@ToString
public class GucciProductDTO {

    private String url;
    private String material; //材质
    private String productname;
    private String price;
    private String brand;
    private String productCode;
    private String descript;
    private String barCode;
    private String made;
    private String sex;
    private String size;
    private String sizeDesc;
    private String memo;
    private String colorCode;
    private String itemprice;
    private String itemdiscountA;
    private String itemsaleprice;
    private String spuNo;
    private String category;
    private String picUrls;
    private String qty;
    private String qtyDesc;
    private String season;
    private String otherInfos;


    /**
     * copyProduct data
     * @param product
     * @return
     */
    public static GucciProductDTO copyProductData(GucciProductDTO product){
        GucciProductDTO temPro = new GucciProductDTO();
        temPro.setUrl(product.getUrl());
        temPro.setMaterial(product.getMaterial());
        temPro.setProductname(product.getProductname());
        temPro.setPrice(product.getPrice());
        temPro.setBrand(product.getBrand());
        temPro.setProductCode(product.getProductCode());
        temPro.setDescript(product.getDescript());
        temPro.setBarCode(product.getBarCode());
        temPro.setMade(product.getMade());
        temPro.setSex(product.getSex());
        temPro.setMemo(product.getMemo());
        temPro.setColorCode(product.getColorCode());
        temPro.setItemprice(product.getItemprice());
        temPro.setQty(product.getQty());
        temPro.setQtyDesc(product.getQtyDesc());
        temPro.setItemsaleprice(product.getItemsaleprice());
        temPro.setSpuNo(product.getSpuNo());
        temPro.setCategory(product.getCategory());
        temPro.setPicUrls(product.getPicUrls());
        temPro.setSize(product.getSize());
        temPro.setSizeDesc(product.getSizeDesc());
        temPro.setSeason(product.getSeason());
        temPro.setOtherInfos(product.getOtherInfos());
        return temPro;
    }

}
