package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by loyalty on 15/5/25.
 */
@Getter
@Setter
public class SpinnakerProductDTO implements Serializable{

    private Long id;//代理主键

    //SPU ID;
    private String  productId;
    //货号
    private String producerId;
    //类型
    private String type;
    //季节
    private String season;
    //产品名称
    private String productName;
    //描述
    private String description;
    //分类
    private String category;
    //图片地址
    private String url;
    //供货商价格
    private String supplyPrice;
    //skuID
    private String itemId;
    //尺码
    private String  itemSize;
    //条形码
    private String  barcode;
    //颜色
    private String color;
    //库存
    private String stock;
    //明细图片地址
    private String itemPictureUrl;

    private String itemPictureUrl1;

    private String itemPictureUrl2;

    private String itemPictureUrl3;

    private String itemPictureUrl4;

    private String itemPictureUrl5;

    private String itemPictureUrl6;

    private String itemPictureUrl7;

    private String itemPictureUrl8;




    public  void setItemPic(){
        if(null==itemPictureUrl||"".equals(itemPictureUrl)){
            return ;
        }
        String[] urlArray = itemPictureUrl.split(",");
        if(null!=urlArray&&urlArray.length>0){
            for(int i=0;i<urlArray.length;i++){
                switch (i){
                    case 0:
                        itemPictureUrl1=urlArray[i];
                        break;
                    case 1:
                        itemPictureUrl2=urlArray[i];
                        break;
                    case 2:
                        itemPictureUrl3=urlArray[i];
                        break;
                    case 3:
                        itemPictureUrl4=urlArray[i];
                        break;
                    case 4:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 5:
                        itemPictureUrl5=urlArray[i];
                        break;
                    case 6:
                        itemPictureUrl7=urlArray[i];
                        break;
                    case 7:
                        itemPictureUrl8=urlArray[i];
                        break;


                }
            }
        }
    }


}
