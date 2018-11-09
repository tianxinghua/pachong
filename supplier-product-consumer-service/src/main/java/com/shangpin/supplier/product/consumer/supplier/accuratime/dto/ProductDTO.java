package com.shangpin.supplier.product.consumer.supplier.accuratime.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lizhongren on 2017/2/18.
 */
@Getter
@Setter
@ToString
public class ProductDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3339978607502926167L;

    private String id;
    private String supplierId;
    private String supplierName;
    private String spuId;
    private String spuName;
    private String skuId;
    private String productName;
    private String categoryGender;
    private String categoryId;
    private String categoryName;
    private String subCategoryId;
    private String subCategoryName;
    private String spuPicture;//商品的公共图片
    private String skuPicture;//SKU的个性图片
    private String memo;
    private String spSkuId;
    private String spStatus;
    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;
    private String seasonName;
    private String productCode;//货号
    private String skuUrl;//供应商产品地址
    private String picUrl;//图片地址
    private String marketPrice;//市场价
    private String salePrice;//销售价
    private String supplierPrice;//供货价
    private String saleCurrency;//币种
    private String productDescription;//描述


    private String itemPictureUrl1 = "";

    private String itemPictureUrl2 = "";

    private String itemPictureUrl3 = "";

    private String itemPictureUrl4 = "";

    private String itemPictureUrl5 = "";

    private String itemPictureUrl6 = "";

    private String itemPictureUrl7 = "";

    private String itemPictureUrl8 = "";
    private String material;//材质
    private String productOrigin;//产地
    private String color;
    //尺码
    private String size;
    private String sizeClass;
    //条形码
    private String barcode;

    private String stock;//库存


    private String newMarketPrice;
    private String newSalePrice;
    private String newSupplierPrice;
    private String newseasonId;

    private String spCategory;
    private String spBrand;


}
