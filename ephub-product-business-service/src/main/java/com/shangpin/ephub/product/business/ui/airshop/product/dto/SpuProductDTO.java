
package com.shangpin.ephub.product.business.ui.airshop.product.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 */
@SuppressWarnings("serial")
@Setter
@Getter
public class SpuProductDTO implements Serializable{

    private String supplierId;//供货商门户编号
    private String spuId; //必须
    /**性别分类*/
    private String categoryGender;
    /**大类*/
    private String categoryName; //必须
    private String seasonName;//上市季节名称
    private String brandName;//品牌 必须
    private String material;//材质  必须
    private String productOrigin;//产地
    private String productCode;//货号 必须
    private String  spuPicture;//商品的公共图片
    private Date lastTime;//修改时间
    private String memo;    //备注
    private String color;//颜色 必须
    private String status;  //1:unsubmitted   2：editing  3：submitted  4:delete
    private List<SkuProductDTO> list;
    private String barcode;//条形码
    private String saleCurrency;//币种 必须
    private String productDescription;//描述
    private String marketPrice;//吊牌价（市场价）
    private String supplierPrice;//供货商价格
    private String productName;//产品展示名称 必须
    @Override
    public String toString() {
        return "ProductDTO{" +
                "supplierId='" + supplierId + '\'' +
                ", spuId='" + spuId + '\'' +
                ", categoryGender='" + categoryGender + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", seasonName='" + seasonName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", material='" + material + '\'' +
                ", productOrigin='" + productOrigin + '\'' +
                ", productCode='" + productCode + '\'' +
                ", spuPicture='" + spuPicture + '\'' +
                '}';
    }
}
