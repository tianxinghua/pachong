package com.shangpin.iog.spider;

import us.codecraft.webmagic.ResultItems;

public class SetGoods {


    public GoodsEntity setgoods(ResultItems resultItems,String[] sizes,String[] qtys,int i) {
        GoodsEntity goods = new GoodsEntity();
        goods.setGender(resultItems.get("gender"));
        goods.setBrand(resultItems.get("brand"));
        goods.setCategory(resultItems.get("category"));
        goods.setSPU(resultItems.get("SPU"));
        goods.setProductModel(resultItems.get("productModel"));
        goods.setSeason(resultItems.get("season"));
        goods.setMaterial(resultItems.get("material"));
        goods.setColor(resultItems.get("color"));
        if (sizes[i] == null || sizes[i].equals("")) {
            goods.setSize("U");
        } else {
            goods.setSize(sizes[i]);
        }
        goods.setProName(resultItems.get("proName"));
        goods.setForeignPrice(resultItems.get("foreignPrice"));
        goods.setInlandPrice(resultItems.get("inlandPrice"));
        goods.setSalePrice(resultItems.get("salePrice"));
        goods.setQty(qtys[i]);
        goods.setMade(resultItems.get("made"));
        goods.setDesc(resultItems.get("desc"));
        goods.setPics(resultItems.get("pics"));
        goods.setDetailLink(resultItems.get("detailLink"));
        goods.setMeasurement(resultItems.get("measurement"));
        goods.setSupplierId(resultItems.get("supplierId"));
        goods.setSupplierNo(resultItems.get("supplierNo"));
        goods.setSupplierSkuNo(resultItems.get("supplierSkuNo"));
        goods.setChannel(resultItems.get("channel"));

        return goods;
    }
}
