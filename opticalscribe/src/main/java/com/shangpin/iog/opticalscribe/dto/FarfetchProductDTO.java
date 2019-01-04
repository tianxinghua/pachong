package com.shangpin.iog.opticalscribe.dto;

import com.shangpin.iog.opticalscribe.service.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 11:32 2018/6/12
 * @Description:
 */
@Getter
@Setter
@ToString
public class FarfetchProductDTO{

    private String url;
    private String material; //材质
    private String productName;
    private String brand;

    private String productCode;
    private String barCode;
    private String descript;
    private String made;

    private String sex;
    private String size;
    private String sizeDesc;
    private String colorName;

    private String colorCode;
    private String itemprice;
    private String itemdiscountA;
    private String itemsaleprice;

    private String spuNo;
    private String category;
    private String parentCategory;
    private String picUrls;
    private String qty;

    private String qtyDesc;
    private String season;
    private String otherInfos;
    private String modelMeasurements;

    private String washMethod;



    /**
     * copyProduct data
     * @param product
     * @return
     */
    public static FarfetchProductDTO copyProductData(FarfetchProductDTO product){
        FarfetchProductDTO temPro = new FarfetchProductDTO();
        temPro.setUrl(product.getUrl());
        temPro.setMaterial(product.getMaterial());
        temPro.setProductName(product.getProductName());
        temPro.setBrand(product.getBrand());

        temPro.setProductCode(product.getProductCode());
        temPro.setBarCode(product.getBarCode());
        temPro.setDescript(product.getDescript());
        temPro.setMade(product.getMade());

        temPro.setSex(product.getSex());
        temPro.setSize(product.getSize());
        temPro.setSizeDesc(product.getSizeDesc());
        temPro.setColorName(product.getColorName());

        temPro.setColorCode(product.getColorCode());
        temPro.setItemprice(product.getItemprice());
        temPro.setItemdiscountA(product.getItemdiscountA());
        temPro.setItemsaleprice(product.getItemsaleprice());

        temPro.setSpuNo(product.getSpuNo());
        temPro.setCategory(product.getCategory());
        temPro.setParentCategory(product.getParentCategory());

        temPro.setPicUrls(product.getPicUrls());
        temPro.setQty(product.getQty());

        temPro.setQtyDesc(product.getQtyDesc());
        temPro.setSeason(product.getSeason());
        temPro.setOtherInfos(product.getOtherInfos());
        temPro.setModelMeasurements(product.getModelMeasurements());

        temPro.setWashMethod(product.getWashMethod());
        return temPro;
    }

}
