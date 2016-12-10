package com.shangpin.ep.order.module.sku.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <p>Title:HubSkuCriteria.java </p>
 * <p>Description: HUB订单系统sku实体查询条件对象</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:36:59
 */
public class HubSkuCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSkuCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo=pageNo;
        this.startRow = (pageNo-1)*this.pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setStartRow(Integer startRow) {
        this.startRow=startRow;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize=pageSize;
        this.startRow = (pageNo-1)*this.pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setFields(String fields) {
        this.fields=fields;
    }

    public String getFields() {
        return fields;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("ID like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("ID not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIsNull() {
            addCriterion("SUPPLIER_ID is null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIsNotNull() {
            addCriterion("SUPPLIER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdEqualTo(String value) {
            addCriterion("SUPPLIER_ID =", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotEqualTo(String value) {
            addCriterion("SUPPLIER_ID <>", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThan(String value) {
            addCriterion("SUPPLIER_ID >", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_ID >=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThan(String value) {
            addCriterion("SUPPLIER_ID <", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_ID <=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLike(String value) {
            addCriterion("SUPPLIER_ID like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotLike(String value) {
            addCriterion("SUPPLIER_ID not like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIn(List<String> values) {
            addCriterion("SUPPLIER_ID in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotIn(List<String> values) {
            addCriterion("SUPPLIER_ID not in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdBetween(String value1, String value2) {
            addCriterion("SUPPLIER_ID between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotBetween(String value1, String value2) {
            addCriterion("SUPPLIER_ID not between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSpuIdIsNull() {
            addCriterion("SPU_ID is null");
            return (Criteria) this;
        }

        public Criteria andSpuIdIsNotNull() {
            addCriterion("SPU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSpuIdEqualTo(String value) {
            addCriterion("SPU_ID =", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotEqualTo(String value) {
            addCriterion("SPU_ID <>", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThan(String value) {
            addCriterion("SPU_ID >", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThanOrEqualTo(String value) {
            addCriterion("SPU_ID >=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThan(String value) {
            addCriterion("SPU_ID <", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThanOrEqualTo(String value) {
            addCriterion("SPU_ID <=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLike(String value) {
            addCriterion("SPU_ID like", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotLike(String value) {
            addCriterion("SPU_ID not like", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdIn(List<String> values) {
            addCriterion("SPU_ID in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotIn(List<String> values) {
            addCriterion("SPU_ID not in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdBetween(String value1, String value2) {
            addCriterion("SPU_ID between", value1, value2, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotBetween(String value1, String value2) {
            addCriterion("SPU_ID not between", value1, value2, "spuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdIsNull() {
            addCriterion("SKU_ID is null");
            return (Criteria) this;
        }

        public Criteria andSkuIdIsNotNull() {
            addCriterion("SKU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSkuIdEqualTo(String value) {
            addCriterion("SKU_ID =", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotEqualTo(String value) {
            addCriterion("SKU_ID <>", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThan(String value) {
            addCriterion("SKU_ID >", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdGreaterThanOrEqualTo(String value) {
            addCriterion("SKU_ID >=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThan(String value) {
            addCriterion("SKU_ID <", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLessThanOrEqualTo(String value) {
            addCriterion("SKU_ID <=", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdLike(String value) {
            addCriterion("SKU_ID like", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotLike(String value) {
            addCriterion("SKU_ID not like", value, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdIn(List<String> values) {
            addCriterion("SKU_ID in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotIn(List<String> values) {
            addCriterion("SKU_ID not in", values, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdBetween(String value1, String value2) {
            addCriterion("SKU_ID between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andSkuIdNotBetween(String value1, String value2) {
            addCriterion("SKU_ID not between", value1, value2, "skuId");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNull() {
            addCriterion("PRODUCT_NAME is null");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNotNull() {
            addCriterion("PRODUCT_NAME is not null");
            return (Criteria) this;
        }

        public Criteria andProductNameEqualTo(String value) {
            addCriterion("PRODUCT_NAME =", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotEqualTo(String value) {
            addCriterion("PRODUCT_NAME <>", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThan(String value) {
            addCriterion("PRODUCT_NAME >", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("PRODUCT_NAME >=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThan(String value) {
            addCriterion("PRODUCT_NAME <", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThanOrEqualTo(String value) {
            addCriterion("PRODUCT_NAME <=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLike(String value) {
            addCriterion("PRODUCT_NAME like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotLike(String value) {
            addCriterion("PRODUCT_NAME not like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameIn(List<String> values) {
            addCriterion("PRODUCT_NAME in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotIn(List<String> values) {
            addCriterion("PRODUCT_NAME not in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameBetween(String value1, String value2) {
            addCriterion("PRODUCT_NAME between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotBetween(String value1, String value2) {
            addCriterion("PRODUCT_NAME not between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIsNull() {
            addCriterion("MARKET_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIsNotNull() {
            addCriterion("MARKET_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceEqualTo(String value) {
            addCriterion("MARKET_PRICE =", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotEqualTo(String value) {
            addCriterion("MARKET_PRICE <>", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceGreaterThan(String value) {
            addCriterion("MARKET_PRICE >", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceGreaterThanOrEqualTo(String value) {
            addCriterion("MARKET_PRICE >=", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceLessThan(String value) {
            addCriterion("MARKET_PRICE <", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceLessThanOrEqualTo(String value) {
            addCriterion("MARKET_PRICE <=", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceLike(String value) {
            addCriterion("MARKET_PRICE like", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotLike(String value) {
            addCriterion("MARKET_PRICE not like", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIn(List<String> values) {
            addCriterion("MARKET_PRICE in", values, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotIn(List<String> values) {
            addCriterion("MARKET_PRICE not in", values, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceBetween(String value1, String value2) {
            addCriterion("MARKET_PRICE between", value1, value2, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotBetween(String value1, String value2) {
            addCriterion("MARKET_PRICE not between", value1, value2, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceIsNull() {
            addCriterion("SALE_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andSalePriceIsNotNull() {
            addCriterion("SALE_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andSalePriceEqualTo(String value) {
            addCriterion("SALE_PRICE =", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotEqualTo(String value) {
            addCriterion("SALE_PRICE <>", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceGreaterThan(String value) {
            addCriterion("SALE_PRICE >", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceGreaterThanOrEqualTo(String value) {
            addCriterion("SALE_PRICE >=", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceLessThan(String value) {
            addCriterion("SALE_PRICE <", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceLessThanOrEqualTo(String value) {
            addCriterion("SALE_PRICE <=", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceLike(String value) {
            addCriterion("SALE_PRICE like", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotLike(String value) {
            addCriterion("SALE_PRICE not like", value, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceIn(List<String> values) {
            addCriterion("SALE_PRICE in", values, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotIn(List<String> values) {
            addCriterion("SALE_PRICE not in", values, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceBetween(String value1, String value2) {
            addCriterion("SALE_PRICE between", value1, value2, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSalePriceNotBetween(String value1, String value2) {
            addCriterion("SALE_PRICE not between", value1, value2, "salePrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceIsNull() {
            addCriterion("SUPPLIER_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceIsNotNull() {
            addCriterion("SUPPLIER_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceEqualTo(String value) {
            addCriterion("SUPPLIER_PRICE =", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceNotEqualTo(String value) {
            addCriterion("SUPPLIER_PRICE <>", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceGreaterThan(String value) {
            addCriterion("SUPPLIER_PRICE >", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceGreaterThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_PRICE >=", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceLessThan(String value) {
            addCriterion("SUPPLIER_PRICE <", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceLessThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_PRICE <=", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceLike(String value) {
            addCriterion("SUPPLIER_PRICE like", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceNotLike(String value) {
            addCriterion("SUPPLIER_PRICE not like", value, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceIn(List<String> values) {
            addCriterion("SUPPLIER_PRICE in", values, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceNotIn(List<String> values) {
            addCriterion("SUPPLIER_PRICE not in", values, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceBetween(String value1, String value2) {
            addCriterion("SUPPLIER_PRICE between", value1, value2, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceNotBetween(String value1, String value2) {
            addCriterion("SUPPLIER_PRICE not between", value1, value2, "supplierPrice");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNull() {
            addCriterion("BARCODE is null");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNotNull() {
            addCriterion("BARCODE is not null");
            return (Criteria) this;
        }

        public Criteria andBarcodeEqualTo(String value) {
            addCriterion("BARCODE =", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotEqualTo(String value) {
            addCriterion("BARCODE <>", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThan(String value) {
            addCriterion("BARCODE >", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("BARCODE >=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThan(String value) {
            addCriterion("BARCODE <", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThanOrEqualTo(String value) {
            addCriterion("BARCODE <=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLike(String value) {
            addCriterion("BARCODE like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotLike(String value) {
            addCriterion("BARCODE not like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeIn(List<String> values) {
            addCriterion("BARCODE in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotIn(List<String> values) {
            addCriterion("BARCODE not in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeBetween(String value1, String value2) {
            addCriterion("BARCODE between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotBetween(String value1, String value2) {
            addCriterion("BARCODE not between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andProductCodeIsNull() {
            addCriterion("PRODUCT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andProductCodeIsNotNull() {
            addCriterion("PRODUCT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andProductCodeEqualTo(String value) {
            addCriterion("PRODUCT_CODE =", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotEqualTo(String value) {
            addCriterion("PRODUCT_CODE <>", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeGreaterThan(String value) {
            addCriterion("PRODUCT_CODE >", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("PRODUCT_CODE >=", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLessThan(String value) {
            addCriterion("PRODUCT_CODE <", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLessThanOrEqualTo(String value) {
            addCriterion("PRODUCT_CODE <=", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeLike(String value) {
            addCriterion("PRODUCT_CODE like", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotLike(String value) {
            addCriterion("PRODUCT_CODE not like", value, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeIn(List<String> values) {
            addCriterion("PRODUCT_CODE in", values, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotIn(List<String> values) {
            addCriterion("PRODUCT_CODE not in", values, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeBetween(String value1, String value2) {
            addCriterion("PRODUCT_CODE between", value1, value2, "productCode");
            return (Criteria) this;
        }

        public Criteria andProductCodeNotBetween(String value1, String value2) {
            addCriterion("PRODUCT_CODE not between", value1, value2, "productCode");
            return (Criteria) this;
        }

        public Criteria andColorIsNull() {
            addCriterion("COLOR is null");
            return (Criteria) this;
        }

        public Criteria andColorIsNotNull() {
            addCriterion("COLOR is not null");
            return (Criteria) this;
        }

        public Criteria andColorEqualTo(String value) {
            addCriterion("COLOR =", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorNotEqualTo(String value) {
            addCriterion("COLOR <>", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorGreaterThan(String value) {
            addCriterion("COLOR >", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorGreaterThanOrEqualTo(String value) {
            addCriterion("COLOR >=", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorLessThan(String value) {
            addCriterion("COLOR <", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorLessThanOrEqualTo(String value) {
            addCriterion("COLOR <=", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorLike(String value) {
            addCriterion("COLOR like", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorNotLike(String value) {
            addCriterion("COLOR not like", value, "color");
            return (Criteria) this;
        }

        public Criteria andColorIn(List<String> values) {
            addCriterion("COLOR in", values, "color");
            return (Criteria) this;
        }

        public Criteria andColorNotIn(List<String> values) {
            addCriterion("COLOR not in", values, "color");
            return (Criteria) this;
        }

        public Criteria andColorBetween(String value1, String value2) {
            addCriterion("COLOR between", value1, value2, "color");
            return (Criteria) this;
        }

        public Criteria andColorNotBetween(String value1, String value2) {
            addCriterion("COLOR not between", value1, value2, "color");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionIsNull() {
            addCriterion("PRODUCT_DESCRIPTION is null");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionIsNotNull() {
            addCriterion("PRODUCT_DESCRIPTION is not null");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionEqualTo(String value) {
            addCriterion("PRODUCT_DESCRIPTION =", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionNotEqualTo(String value) {
            addCriterion("PRODUCT_DESCRIPTION <>", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionGreaterThan(String value) {
            addCriterion("PRODUCT_DESCRIPTION >", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("PRODUCT_DESCRIPTION >=", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionLessThan(String value) {
            addCriterion("PRODUCT_DESCRIPTION <", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionLessThanOrEqualTo(String value) {
            addCriterion("PRODUCT_DESCRIPTION <=", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionLike(String value) {
            addCriterion("PRODUCT_DESCRIPTION like", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionNotLike(String value) {
            addCriterion("PRODUCT_DESCRIPTION not like", value, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionIn(List<String> values) {
            addCriterion("PRODUCT_DESCRIPTION in", values, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionNotIn(List<String> values) {
            addCriterion("PRODUCT_DESCRIPTION not in", values, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionBetween(String value1, String value2) {
            addCriterion("PRODUCT_DESCRIPTION between", value1, value2, "productDescription");
            return (Criteria) this;
        }

        public Criteria andProductDescriptionNotBetween(String value1, String value2) {
            addCriterion("PRODUCT_DESCRIPTION not between", value1, value2, "productDescription");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyIsNull() {
            addCriterion("SALE_CURRENCY is null");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyIsNotNull() {
            addCriterion("SALE_CURRENCY is not null");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyEqualTo(String value) {
            addCriterion("SALE_CURRENCY =", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyNotEqualTo(String value) {
            addCriterion("SALE_CURRENCY <>", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyGreaterThan(String value) {
            addCriterion("SALE_CURRENCY >", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("SALE_CURRENCY >=", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyLessThan(String value) {
            addCriterion("SALE_CURRENCY <", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyLessThanOrEqualTo(String value) {
            addCriterion("SALE_CURRENCY <=", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyLike(String value) {
            addCriterion("SALE_CURRENCY like", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyNotLike(String value) {
            addCriterion("SALE_CURRENCY not like", value, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyIn(List<String> values) {
            addCriterion("SALE_CURRENCY in", values, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyNotIn(List<String> values) {
            addCriterion("SALE_CURRENCY not in", values, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyBetween(String value1, String value2) {
            addCriterion("SALE_CURRENCY between", value1, value2, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andSaleCurrencyNotBetween(String value1, String value2) {
            addCriterion("SALE_CURRENCY not between", value1, value2, "saleCurrency");
            return (Criteria) this;
        }

        public Criteria andProductSizeIsNull() {
            addCriterion("PRODUCT_SIZE is null");
            return (Criteria) this;
        }

        public Criteria andProductSizeIsNotNull() {
            addCriterion("PRODUCT_SIZE is not null");
            return (Criteria) this;
        }

        public Criteria andProductSizeEqualTo(String value) {
            addCriterion("PRODUCT_SIZE =", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeNotEqualTo(String value) {
            addCriterion("PRODUCT_SIZE <>", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeGreaterThan(String value) {
            addCriterion("PRODUCT_SIZE >", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeGreaterThanOrEqualTo(String value) {
            addCriterion("PRODUCT_SIZE >=", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeLessThan(String value) {
            addCriterion("PRODUCT_SIZE <", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeLessThanOrEqualTo(String value) {
            addCriterion("PRODUCT_SIZE <=", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeLike(String value) {
            addCriterion("PRODUCT_SIZE like", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeNotLike(String value) {
            addCriterion("PRODUCT_SIZE not like", value, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeIn(List<String> values) {
            addCriterion("PRODUCT_SIZE in", values, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeNotIn(List<String> values) {
            addCriterion("PRODUCT_SIZE not in", values, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeBetween(String value1, String value2) {
            addCriterion("PRODUCT_SIZE between", value1, value2, "productSize");
            return (Criteria) this;
        }

        public Criteria andProductSizeNotBetween(String value1, String value2) {
            addCriterion("PRODUCT_SIZE not between", value1, value2, "productSize");
            return (Criteria) this;
        }

        public Criteria andStockIsNull() {
            addCriterion("STOCK is null");
            return (Criteria) this;
        }

        public Criteria andStockIsNotNull() {
            addCriterion("STOCK is not null");
            return (Criteria) this;
        }

        public Criteria andStockEqualTo(String value) {
            addCriterion("STOCK =", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotEqualTo(String value) {
            addCriterion("STOCK <>", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThan(String value) {
            addCriterion("STOCK >", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThanOrEqualTo(String value) {
            addCriterion("STOCK >=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThan(String value) {
            addCriterion("STOCK <", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThanOrEqualTo(String value) {
            addCriterion("STOCK <=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLike(String value) {
            addCriterion("STOCK like", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotLike(String value) {
            addCriterion("STOCK not like", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockIn(List<String> values) {
            addCriterion("STOCK in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotIn(List<String> values) {
            addCriterion("STOCK not in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockBetween(String value1, String value2) {
            addCriterion("STOCK between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotBetween(String value1, String value2) {
            addCriterion("STOCK not between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("MEMO is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("MEMO is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("MEMO =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("MEMO <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("MEMO >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("MEMO >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("MEMO <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("MEMO <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("MEMO like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("MEMO not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("MEMO in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("MEMO not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("MEMO between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("MEMO not between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeIsNull() {
            addCriterion("LAST_TIME is null");
            return (Criteria) this;
        }

        public Criteria andLastTimeIsNotNull() {
            addCriterion("LAST_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andLastTimeEqualTo(Date value) {
            addCriterion("LAST_TIME =", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeNotEqualTo(Date value) {
            addCriterion("LAST_TIME <>", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeGreaterThan(Date value) {
            addCriterion("LAST_TIME >", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LAST_TIME >=", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeLessThan(Date value) {
            addCriterion("LAST_TIME <", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeLessThanOrEqualTo(Date value) {
            addCriterion("LAST_TIME <=", value, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeIn(List<Date> values) {
            addCriterion("LAST_TIME in", values, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeNotIn(List<Date> values) {
            addCriterion("LAST_TIME not in", values, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeBetween(Date value1, Date value2) {
            addCriterion("LAST_TIME between", value1, value2, "lastTime");
            return (Criteria) this;
        }

        public Criteria andLastTimeNotBetween(Date value1, Date value2) {
            addCriterion("LAST_TIME not between", value1, value2, "lastTime");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceIsNull() {
            addCriterion("NEW_MARKET_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceIsNotNull() {
            addCriterion("NEW_MARKET_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceEqualTo(String value) {
            addCriterion("NEW_MARKET_PRICE =", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceNotEqualTo(String value) {
            addCriterion("NEW_MARKET_PRICE <>", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceGreaterThan(String value) {
            addCriterion("NEW_MARKET_PRICE >", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceGreaterThanOrEqualTo(String value) {
            addCriterion("NEW_MARKET_PRICE >=", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceLessThan(String value) {
            addCriterion("NEW_MARKET_PRICE <", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceLessThanOrEqualTo(String value) {
            addCriterion("NEW_MARKET_PRICE <=", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceLike(String value) {
            addCriterion("NEW_MARKET_PRICE like", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceNotLike(String value) {
            addCriterion("NEW_MARKET_PRICE not like", value, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceIn(List<String> values) {
            addCriterion("NEW_MARKET_PRICE in", values, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceNotIn(List<String> values) {
            addCriterion("NEW_MARKET_PRICE not in", values, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceBetween(String value1, String value2) {
            addCriterion("NEW_MARKET_PRICE between", value1, value2, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewMarketPriceNotBetween(String value1, String value2) {
            addCriterion("NEW_MARKET_PRICE not between", value1, value2, "newMarketPrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceIsNull() {
            addCriterion("NEW_SALE_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceIsNotNull() {
            addCriterion("NEW_SALE_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceEqualTo(String value) {
            addCriterion("NEW_SALE_PRICE =", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceNotEqualTo(String value) {
            addCriterion("NEW_SALE_PRICE <>", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceGreaterThan(String value) {
            addCriterion("NEW_SALE_PRICE >", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceGreaterThanOrEqualTo(String value) {
            addCriterion("NEW_SALE_PRICE >=", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceLessThan(String value) {
            addCriterion("NEW_SALE_PRICE <", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceLessThanOrEqualTo(String value) {
            addCriterion("NEW_SALE_PRICE <=", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceLike(String value) {
            addCriterion("NEW_SALE_PRICE like", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceNotLike(String value) {
            addCriterion("NEW_SALE_PRICE not like", value, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceIn(List<String> values) {
            addCriterion("NEW_SALE_PRICE in", values, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceNotIn(List<String> values) {
            addCriterion("NEW_SALE_PRICE not in", values, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceBetween(String value1, String value2) {
            addCriterion("NEW_SALE_PRICE between", value1, value2, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSalePriceNotBetween(String value1, String value2) {
            addCriterion("NEW_SALE_PRICE not between", value1, value2, "newSalePrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceIsNull() {
            addCriterion("NEW_SUPPLIER_PRICE is null");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceIsNotNull() {
            addCriterion("NEW_SUPPLIER_PRICE is not null");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceEqualTo(String value) {
            addCriterion("NEW_SUPPLIER_PRICE =", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceNotEqualTo(String value) {
            addCriterion("NEW_SUPPLIER_PRICE <>", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceGreaterThan(String value) {
            addCriterion("NEW_SUPPLIER_PRICE >", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceGreaterThanOrEqualTo(String value) {
            addCriterion("NEW_SUPPLIER_PRICE >=", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceLessThan(String value) {
            addCriterion("NEW_SUPPLIER_PRICE <", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceLessThanOrEqualTo(String value) {
            addCriterion("NEW_SUPPLIER_PRICE <=", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceLike(String value) {
            addCriterion("NEW_SUPPLIER_PRICE like", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceNotLike(String value) {
            addCriterion("NEW_SUPPLIER_PRICE not like", value, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceIn(List<String> values) {
            addCriterion("NEW_SUPPLIER_PRICE in", values, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceNotIn(List<String> values) {
            addCriterion("NEW_SUPPLIER_PRICE not in", values, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceBetween(String value1, String value2) {
            addCriterion("NEW_SUPPLIER_PRICE between", value1, value2, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andNewSupplierPriceNotBetween(String value1, String value2) {
            addCriterion("NEW_SUPPLIER_PRICE not between", value1, value2, "newSupplierPrice");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("UPDATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("UPDATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("UPDATE_TIME =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("UPDATE_TIME <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("UPDATE_TIME >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("UPDATE_TIME <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("UPDATE_TIME <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("UPDATE_TIME in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("UPDATE_TIME not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("UPDATE_TIME not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andEventStartDateIsNull() {
            addCriterion("EVENT_START_DATE is null");
            return (Criteria) this;
        }

        public Criteria andEventStartDateIsNotNull() {
            addCriterion("EVENT_START_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andEventStartDateEqualTo(String value) {
            addCriterion("EVENT_START_DATE =", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateNotEqualTo(String value) {
            addCriterion("EVENT_START_DATE <>", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateGreaterThan(String value) {
            addCriterion("EVENT_START_DATE >", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateGreaterThanOrEqualTo(String value) {
            addCriterion("EVENT_START_DATE >=", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateLessThan(String value) {
            addCriterion("EVENT_START_DATE <", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateLessThanOrEqualTo(String value) {
            addCriterion("EVENT_START_DATE <=", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateLike(String value) {
            addCriterion("EVENT_START_DATE like", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateNotLike(String value) {
            addCriterion("EVENT_START_DATE not like", value, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateIn(List<String> values) {
            addCriterion("EVENT_START_DATE in", values, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateNotIn(List<String> values) {
            addCriterion("EVENT_START_DATE not in", values, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateBetween(String value1, String value2) {
            addCriterion("EVENT_START_DATE between", value1, value2, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventStartDateNotBetween(String value1, String value2) {
            addCriterion("EVENT_START_DATE not between", value1, value2, "eventStartDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateIsNull() {
            addCriterion("EVENT_END_DATE is null");
            return (Criteria) this;
        }

        public Criteria andEventEndDateIsNotNull() {
            addCriterion("EVENT_END_DATE is not null");
            return (Criteria) this;
        }

        public Criteria andEventEndDateEqualTo(String value) {
            addCriterion("EVENT_END_DATE =", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateNotEqualTo(String value) {
            addCriterion("EVENT_END_DATE <>", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateGreaterThan(String value) {
            addCriterion("EVENT_END_DATE >", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateGreaterThanOrEqualTo(String value) {
            addCriterion("EVENT_END_DATE >=", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateLessThan(String value) {
            addCriterion("EVENT_END_DATE <", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateLessThanOrEqualTo(String value) {
            addCriterion("EVENT_END_DATE <=", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateLike(String value) {
            addCriterion("EVENT_END_DATE like", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateNotLike(String value) {
            addCriterion("EVENT_END_DATE not like", value, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateIn(List<String> values) {
            addCriterion("EVENT_END_DATE in", values, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateNotIn(List<String> values) {
            addCriterion("EVENT_END_DATE not in", values, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateBetween(String value1, String value2) {
            addCriterion("EVENT_END_DATE between", value1, value2, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andEventEndDateNotBetween(String value1, String value2) {
            addCriterion("EVENT_END_DATE not between", value1, value2, "eventEndDate");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNull() {
            addCriterion("MEASUREMENT is null");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNotNull() {
            addCriterion("MEASUREMENT is not null");
            return (Criteria) this;
        }

        public Criteria andMeasurementEqualTo(String value) {
            addCriterion("MEASUREMENT =", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotEqualTo(String value) {
            addCriterion("MEASUREMENT <>", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThan(String value) {
            addCriterion("MEASUREMENT >", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThanOrEqualTo(String value) {
            addCriterion("MEASUREMENT >=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThan(String value) {
            addCriterion("MEASUREMENT <", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThanOrEqualTo(String value) {
            addCriterion("MEASUREMENT <=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLike(String value) {
            addCriterion("MEASUREMENT like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotLike(String value) {
            addCriterion("MEASUREMENT not like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementIn(List<String> values) {
            addCriterion("MEASUREMENT in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotIn(List<String> values) {
            addCriterion("MEASUREMENT not in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementBetween(String value1, String value2) {
            addCriterion("MEASUREMENT between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotBetween(String value1, String value2) {
            addCriterion("MEASUREMENT not between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdIsNull() {
            addCriterion("SP_SKU_ID is null");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdIsNotNull() {
            addCriterion("SP_SKU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdEqualTo(String value) {
            addCriterion("SP_SKU_ID =", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdNotEqualTo(String value) {
            addCriterion("SP_SKU_ID <>", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdGreaterThan(String value) {
            addCriterion("SP_SKU_ID >", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdGreaterThanOrEqualTo(String value) {
            addCriterion("SP_SKU_ID >=", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdLessThan(String value) {
            addCriterion("SP_SKU_ID <", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdLessThanOrEqualTo(String value) {
            addCriterion("SP_SKU_ID <=", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdLike(String value) {
            addCriterion("SP_SKU_ID like", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdNotLike(String value) {
            addCriterion("SP_SKU_ID not like", value, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdIn(List<String> values) {
            addCriterion("SP_SKU_ID in", values, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdNotIn(List<String> values) {
            addCriterion("SP_SKU_ID not in", values, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdBetween(String value1, String value2) {
            addCriterion("SP_SKU_ID between", value1, value2, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andSpSkuIdNotBetween(String value1, String value2) {
            addCriterion("SP_SKU_ID not between", value1, value2, "spSkuId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("STATUS is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("STATUS =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("STATUS <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("STATUS >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("STATUS >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("STATUS <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("STATUS <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("STATUS like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("STATUS not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("STATUS in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("STATUS not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("STATUS between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("STATUS not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andSpStatusIsNull() {
            addCriterion("SP_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andSpStatusIsNotNull() {
            addCriterion("SP_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andSpStatusEqualTo(String value) {
            addCriterion("SP_STATUS =", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusNotEqualTo(String value) {
            addCriterion("SP_STATUS <>", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusGreaterThan(String value) {
            addCriterion("SP_STATUS >", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusGreaterThanOrEqualTo(String value) {
            addCriterion("SP_STATUS >=", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusLessThan(String value) {
            addCriterion("SP_STATUS <", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusLessThanOrEqualTo(String value) {
            addCriterion("SP_STATUS <=", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusLike(String value) {
            addCriterion("SP_STATUS like", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusNotLike(String value) {
            addCriterion("SP_STATUS not like", value, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusIn(List<String> values) {
            addCriterion("SP_STATUS in", values, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusNotIn(List<String> values) {
            addCriterion("SP_STATUS not in", values, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusBetween(String value1, String value2) {
            addCriterion("SP_STATUS between", value1, value2, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpStatusNotBetween(String value1, String value2) {
            addCriterion("SP_STATUS not between", value1, value2, "spStatus");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeIsNull() {
            addCriterion("SP_PRODUCT_CODE is null");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeIsNotNull() {
            addCriterion("SP_PRODUCT_CODE is not null");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeEqualTo(String value) {
            addCriterion("SP_PRODUCT_CODE =", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeNotEqualTo(String value) {
            addCriterion("SP_PRODUCT_CODE <>", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeGreaterThan(String value) {
            addCriterion("SP_PRODUCT_CODE >", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeGreaterThanOrEqualTo(String value) {
            addCriterion("SP_PRODUCT_CODE >=", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeLessThan(String value) {
            addCriterion("SP_PRODUCT_CODE <", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeLessThanOrEqualTo(String value) {
            addCriterion("SP_PRODUCT_CODE <=", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeLike(String value) {
            addCriterion("SP_PRODUCT_CODE like", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeNotLike(String value) {
            addCriterion("SP_PRODUCT_CODE not like", value, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeIn(List<String> values) {
            addCriterion("SP_PRODUCT_CODE in", values, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeNotIn(List<String> values) {
            addCriterion("SP_PRODUCT_CODE not in", values, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeBetween(String value1, String value2) {
            addCriterion("SP_PRODUCT_CODE between", value1, value2, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andSpProductCodeNotBetween(String value1, String value2) {
            addCriterion("SP_PRODUCT_CODE not between", value1, value2, "spProductCode");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultIsNull() {
            addCriterion("PIC_URL_DEFAULT is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultIsNotNull() {
            addCriterion("PIC_URL_DEFAULT is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultEqualTo(String value) {
            addCriterion("PIC_URL_DEFAULT =", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultNotEqualTo(String value) {
            addCriterion("PIC_URL_DEFAULT <>", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultGreaterThan(String value) {
            addCriterion("PIC_URL_DEFAULT >", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultGreaterThanOrEqualTo(String value) {
            addCriterion("PIC_URL_DEFAULT >=", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultLessThan(String value) {
            addCriterion("PIC_URL_DEFAULT <", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultLessThanOrEqualTo(String value) {
            addCriterion("PIC_URL_DEFAULT <=", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultLike(String value) {
            addCriterion("PIC_URL_DEFAULT like", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultNotLike(String value) {
            addCriterion("PIC_URL_DEFAULT not like", value, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultIn(List<String> values) {
            addCriterion("PIC_URL_DEFAULT in", values, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultNotIn(List<String> values) {
            addCriterion("PIC_URL_DEFAULT not in", values, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultBetween(String value1, String value2) {
            addCriterion("PIC_URL_DEFAULT between", value1, value2, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andPicUrlDefaultNotBetween(String value1, String value2) {
            addCriterion("PIC_URL_DEFAULT not between", value1, value2, "picUrlDefault");
            return (Criteria) this;
        }

        public Criteria andIsExcludeIsNull() {
            addCriterion("IS_EXCLUDE is null");
            return (Criteria) this;
        }

        public Criteria andIsExcludeIsNotNull() {
            addCriterion("IS_EXCLUDE is not null");
            return (Criteria) this;
        }

        public Criteria andIsExcludeEqualTo(String value) {
            addCriterion("IS_EXCLUDE =", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeNotEqualTo(String value) {
            addCriterion("IS_EXCLUDE <>", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeGreaterThan(String value) {
            addCriterion("IS_EXCLUDE >", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeGreaterThanOrEqualTo(String value) {
            addCriterion("IS_EXCLUDE >=", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeLessThan(String value) {
            addCriterion("IS_EXCLUDE <", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeLessThanOrEqualTo(String value) {
            addCriterion("IS_EXCLUDE <=", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeLike(String value) {
            addCriterion("IS_EXCLUDE like", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeNotLike(String value) {
            addCriterion("IS_EXCLUDE not like", value, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeIn(List<String> values) {
            addCriterion("IS_EXCLUDE in", values, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeNotIn(List<String> values) {
            addCriterion("IS_EXCLUDE not in", values, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeBetween(String value1, String value2) {
            addCriterion("IS_EXCLUDE between", value1, value2, "isExclude");
            return (Criteria) this;
        }

        public Criteria andIsExcludeNotBetween(String value1, String value2) {
            addCriterion("IS_EXCLUDE not between", value1, value2, "isExclude");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}