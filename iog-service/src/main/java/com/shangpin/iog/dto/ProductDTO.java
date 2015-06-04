package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */
@Getter
@Setter
public class ProductDTO {
    private String id;
    private String supplierId;
    private String spuId;
    private String spuName;
    private String skuID;
    private String productName;
    private String categoryId;
    private String categoryName;
    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;
    private String seasonName;
    private String picUrl;//图片地址


    private String itemPictureUrl1="";

    private String itemPictureUrl2="";

    private String itemPictureUrl3="";

    private String itemPictureUrl4="";

    private String itemPictureUrl5="";

    private String itemPictureUrl6="";

    private String itemPictureUrl7="";

    private String itemPictureUrl8="";
    private String material;//材质
    private String productOrigin;//产地
    private String color;
    //尺码
    private String  size;
    //条形码
    private String  barcode;

    private Date createTime;
    private Date lastTime;//修改时间

    public  void setItemPic(){
        if(null==picUrl||"".equals(picUrl)){
            return ;
        }
        String[] urlArray = picUrl.split(",");
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
