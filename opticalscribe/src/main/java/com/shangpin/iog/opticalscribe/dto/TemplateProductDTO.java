package com.shangpin.iog.opticalscribe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 14:12 2018/7/28
 * @Description:
 */
@Setter
@Getter
@ToString
public class TemplateProductDTO {

    private String sex; //性别 男士女士儿童
    private String brand; // 品牌名称
    private String category;  // 品类名称 如：链条包、斜挎包、T恤

    private String spuNo; // 商品 spu
    private String productModelCode; // 商品 spu
    private String season;  //季节

    private String material; //材质
    private String colorName;  //颜色名称
    private String size;   // 尺码  U:均码

    private String productName; // 商品名称
    private String itemprice;   //国外市场价
    private String itemsaleprice; //国外市场价

    private String qty;  //库存数量
    private String qtyDesc;  // 库存描述  有货 无货 预售 请求库存信息失败
    private String made;   //商品产地

    private String descript;  //商品描述
    private String picUrls;  //商品 图片 url
    private String url;    //商品详情url

    private String sizeDesc;  //测量信息

    private String supplierId;  //供应商 门户账号编号
    private String supplierNo;  //供应商编号

    //供应商原始 skuNo
    private String supplierSkuNo;  //供应商 sku 编号


    /**
     * csv文件分隔符
     */
    private static String  splitSign = ",";

    /**
     * 拼接 单行csv文件数据
     * @return
     */
    public String getCSVString(){
        //继续追加
        StringBuffer buffer = new StringBuffer();
       
        buffer.append(this.sex==null?"":this.sex).append(splitSign);
        buffer.append(this.brand==null?"":this.brand).append(splitSign);

        buffer.append(this.category==null?"":this.category).append(splitSign);
        buffer.append(this.spuNo==null?"":this.spuNo).append(splitSign);

        buffer.append(this.productModelCode==null?"":this.productModelCode).append(splitSign);
        buffer.append(this.season==null?"":this.season).append(splitSign);

        buffer.append(this.material==null?"":this.material).append(splitSign);
        buffer.append(this.colorName==null?"":this.colorName).append(splitSign);

        buffer.append(this.size==null?"":this.size).append(splitSign);
        buffer.append(this.productName==null?"":this.productName).append(splitSign);

        buffer.append(this.itemprice==null?"":this.itemprice).append(splitSign);
        buffer.append(this.getItemsaleprice()==null?"":this.getItemsaleprice()).append(splitSign);

        buffer.append(this.getQty()==null?"":this.getQty()).append(splitSign);
        buffer.append(this.getQtyDesc()==null?"":this.getQtyDesc()).append(splitSign);


        buffer.append(this.getMade()==null?"":this.getMade()).append(splitSign);

        buffer.append(this.getDescript()==null?"":this.getDescript()).append(splitSign);
        buffer.append(this.getPicUrls()==null?"":this.getPicUrls()).append(splitSign);

        buffer.append(this.getUrl()==null?"":this.getUrl()).append(splitSign);
        buffer.append(this.getSizeDesc()==null?"":this.getSizeDesc()).append(splitSign);

        buffer.append(this.getSupplierId()==null?"":this.getSupplierId()).append(splitSign);
        buffer.append(this.getSupplierNo()==null?"":this.getSupplierNo()).append(splitSign);
        buffer.append(this.getSupplierSkuNo()==null?"":this.getSupplierSkuNo());
        buffer.append("\r\n");
        return buffer.toString();
    }

}
