package com.shangpin.asynchronous.task.consumer.productexport.template;

public class TaskImportTemplate2 {
    public static String[] getColorTemplate() {
        String[] headers = { "colorDicItemId","供应商颜色","尚品颜色","创建时间","修改时间","修改人"};
        return headers;
    }
    public static String[] getColorValueTemplate() {
        String[] headers = { "colorDicItemId","colorItemName","colorDicId","createTime","updateTime","updateUser"};
        return headers;
    }
    public static String[] getCategoryTemplate() {
        String[] headers = {"供应商品类Id", "供应商品类","供应商性别","匹配完成度","mapping-state","品类编码","创建时间","更新时间","更新人","供应商ID","供应商编号","供应商名称","genderDicId"};
        return headers;
    }
    public static String[] getCategoryValueTemplate() {
        String[] headers = { "supplierCategoryDicId","supplierCategory","supplierGender","categoryType","mappingState","hubCategoryNo","createTime","updateTime","updateUser","supplierId","supplierNo","supplierName","genderDicId"};
        return headers;
    }
    public static String[] getMaterialTemplate() {
        String[] headers = {"材质Id", "尚品材质名","供应商材质名","级别","创建时间","修改时间","修改人"};
        return headers;
    }
    public static String[] getMaterialValueTemplate() {
        String[] headers = {"materialMappingId", "hubMaterial","supplierMaterial","mappingLevel","createTime","updateTime","updateUser"};
        return headers;
    }
    public static String[] getMadeTemplate() {
        String[] headers = {"产地字典ID","供应商产地","尚品产地","创建时间","更新时间","更新人"};
        return headers;
    }
    public static String[] getMadeValueTemplate() {
        String[] headers = {"hubSupplierValMappingId","supplierVal","hubVal","createTime","updateTime","updateUser"};
        return headers;
    }
    public static String[] getBrandTemplate() {
        String[] headers = {"supplierBrandDicId","供应商","供应商品牌","品牌编码","尚品品牌","创建时间","更新时间","更新人"};
        return headers;
    }
    public static String[] getBrandValueTemplate() {
        String[] headers = {"supplierBrandDicId","supplierId","supplierBrand","hubBrandNo","hubBrand","createTime","updateTime","updateUser"};
        return headers;
    }


}
