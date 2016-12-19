package com.shangpin.ephub.client.data.mysql.sku.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSkuPendingCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSkuPendingCriteriaDto() {
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

        public GeneratedCriteria() {
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

        public Criteria andSkuPendingIdIsNull() {
            addCriterion("sku_pending_id is null");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdIsNotNull() {
            addCriterion("sku_pending_id is not null");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdEqualTo(Long value) {
            addCriterion("sku_pending_id =", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdNotEqualTo(Long value) {
            addCriterion("sku_pending_id <>", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdGreaterThan(Long value) {
            addCriterion("sku_pending_id >", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sku_pending_id >=", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdLessThan(Long value) {
            addCriterion("sku_pending_id <", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdLessThanOrEqualTo(Long value) {
            addCriterion("sku_pending_id <=", value, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdIn(List<Long> values) {
            addCriterion("sku_pending_id in", values, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdNotIn(List<Long> values) {
            addCriterion("sku_pending_id not in", values, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdBetween(Long value1, Long value2) {
            addCriterion("sku_pending_id between", value1, value2, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSkuPendingIdNotBetween(Long value1, Long value2) {
            addCriterion("sku_pending_id not between", value1, value2, "skuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIsNull() {
            addCriterion("spu_pending_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIsNotNull() {
            addCriterion("spu_pending_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdEqualTo(Long value) {
            addCriterion("spu_pending_id =", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotEqualTo(Long value) {
            addCriterion("spu_pending_id <>", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdGreaterThan(Long value) {
            addCriterion("spu_pending_id >", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spu_pending_id >=", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdLessThan(Long value) {
            addCriterion("spu_pending_id <", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdLessThanOrEqualTo(Long value) {
            addCriterion("spu_pending_id <=", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIn(List<Long> values) {
            addCriterion("spu_pending_id in", values, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotIn(List<Long> values) {
            addCriterion("spu_pending_id not in", values, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdBetween(Long value1, Long value2) {
            addCriterion("spu_pending_id between", value1, value2, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotBetween(Long value1, Long value2) {
            addCriterion("spu_pending_id not between", value1, value2, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIsNull() {
            addCriterion("supplier_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIsNotNull() {
            addCriterion("supplier_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdEqualTo(String value) {
            addCriterion("supplier_id =", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotEqualTo(String value) {
            addCriterion("supplier_id <>", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThan(String value) {
            addCriterion("supplier_id >", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_id >=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThan(String value) {
            addCriterion("supplier_id <", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThanOrEqualTo(String value) {
            addCriterion("supplier_id <=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLike(String value) {
            addCriterion("supplier_id like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotLike(String value) {
            addCriterion("supplier_id not like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIn(List<String> values) {
            addCriterion("supplier_id in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotIn(List<String> values) {
            addCriterion("supplier_id not in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdBetween(String value1, String value2) {
            addCriterion("supplier_id between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotBetween(String value1, String value2) {
            addCriterion("supplier_id not between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoIsNull() {
            addCriterion("supplier_sku_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoIsNotNull() {
            addCriterion("supplier_sku_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoEqualTo(String value) {
            addCriterion("supplier_sku_no =", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoNotEqualTo(String value) {
            addCriterion("supplier_sku_no <>", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoGreaterThan(String value) {
            addCriterion("supplier_sku_no >", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_sku_no >=", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoLessThan(String value) {
            addCriterion("supplier_sku_no <", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_sku_no <=", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoLike(String value) {
            addCriterion("supplier_sku_no like", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoNotLike(String value) {
            addCriterion("supplier_sku_no not like", value, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoIn(List<String> values) {
            addCriterion("supplier_sku_no in", values, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoNotIn(List<String> values) {
            addCriterion("supplier_sku_no not in", values, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoBetween(String value1, String value2) {
            addCriterion("supplier_sku_no between", value1, value2, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNoNotBetween(String value1, String value2) {
            addCriterion("supplier_sku_no not between", value1, value2, "supplierSkuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNameIsNull() {
            addCriterion("sku_name is null");
            return (Criteria) this;
        }

        public Criteria andSkuNameIsNotNull() {
            addCriterion("sku_name is not null");
            return (Criteria) this;
        }

        public Criteria andSkuNameEqualTo(String value) {
            addCriterion("sku_name =", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameNotEqualTo(String value) {
            addCriterion("sku_name <>", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameGreaterThan(String value) {
            addCriterion("sku_name >", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameGreaterThanOrEqualTo(String value) {
            addCriterion("sku_name >=", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameLessThan(String value) {
            addCriterion("sku_name <", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameLessThanOrEqualTo(String value) {
            addCriterion("sku_name <=", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameLike(String value) {
            addCriterion("sku_name like", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameNotLike(String value) {
            addCriterion("sku_name not like", value, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameIn(List<String> values) {
            addCriterion("sku_name in", values, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameNotIn(List<String> values) {
            addCriterion("sku_name not in", values, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameBetween(String value1, String value2) {
            addCriterion("sku_name between", value1, value2, "skuName");
            return (Criteria) this;
        }

        public Criteria andSkuNameNotBetween(String value1, String value2) {
            addCriterion("sku_name not between", value1, value2, "skuName");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIsNull() {
            addCriterion("market_price is null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIsNotNull() {
            addCriterion("market_price is not null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceEqualTo(BigDecimal value) {
            addCriterion("market_price =", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotEqualTo(BigDecimal value) {
            addCriterion("market_price <>", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceGreaterThan(BigDecimal value) {
            addCriterion("market_price >", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("market_price >=", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceLessThan(BigDecimal value) {
            addCriterion("market_price <", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("market_price <=", value, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceIn(List<BigDecimal> values) {
            addCriterion("market_price in", values, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotIn(List<BigDecimal> values) {
            addCriterion("market_price not in", values, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("market_price between", value1, value2, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("market_price not between", value1, value2, "marketPrice");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgIsNull() {
            addCriterion("market_price_currencyOrg is null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgIsNotNull() {
            addCriterion("market_price_currencyOrg is not null");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgEqualTo(String value) {
            addCriterion("market_price_currencyOrg =", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgNotEqualTo(String value) {
            addCriterion("market_price_currencyOrg <>", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgGreaterThan(String value) {
            addCriterion("market_price_currencyOrg >", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgGreaterThanOrEqualTo(String value) {
            addCriterion("market_price_currencyOrg >=", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgLessThan(String value) {
            addCriterion("market_price_currencyOrg <", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgLessThanOrEqualTo(String value) {
            addCriterion("market_price_currencyOrg <=", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgLike(String value) {
            addCriterion("market_price_currencyOrg like", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgNotLike(String value) {
            addCriterion("market_price_currencyOrg not like", value, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgIn(List<String> values) {
            addCriterion("market_price_currencyOrg in", values, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgNotIn(List<String> values) {
            addCriterion("market_price_currencyOrg not in", values, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgBetween(String value1, String value2) {
            addCriterion("market_price_currencyOrg between", value1, value2, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketPriceCurrencyorgNotBetween(String value1, String value2) {
            addCriterion("market_price_currencyOrg not between", value1, value2, "marketPriceCurrencyorg");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIsNull() {
            addCriterion("sales_price is null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIsNotNull() {
            addCriterion("sales_price is not null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceEqualTo(BigDecimal value) {
            addCriterion("sales_price =", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotEqualTo(BigDecimal value) {
            addCriterion("sales_price <>", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceGreaterThan(BigDecimal value) {
            addCriterion("sales_price >", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("sales_price >=", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceLessThan(BigDecimal value) {
            addCriterion("sales_price <", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("sales_price <=", value, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceIn(List<BigDecimal> values) {
            addCriterion("sales_price in", values, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotIn(List<BigDecimal> values) {
            addCriterion("sales_price not in", values, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sales_price between", value1, value2, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("sales_price not between", value1, value2, "salesPrice");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyIsNull() {
            addCriterion("sales_price_currency is null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyIsNotNull() {
            addCriterion("sales_price_currency is not null");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyEqualTo(String value) {
            addCriterion("sales_price_currency =", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyNotEqualTo(String value) {
            addCriterion("sales_price_currency <>", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyGreaterThan(String value) {
            addCriterion("sales_price_currency >", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("sales_price_currency >=", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyLessThan(String value) {
            addCriterion("sales_price_currency <", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyLessThanOrEqualTo(String value) {
            addCriterion("sales_price_currency <=", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyLike(String value) {
            addCriterion("sales_price_currency like", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyNotLike(String value) {
            addCriterion("sales_price_currency not like", value, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyIn(List<String> values) {
            addCriterion("sales_price_currency in", values, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyNotIn(List<String> values) {
            addCriterion("sales_price_currency not in", values, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyBetween(String value1, String value2) {
            addCriterion("sales_price_currency between", value1, value2, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSalesPriceCurrencyNotBetween(String value1, String value2) {
            addCriterion("sales_price_currency not between", value1, value2, "salesPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceIsNull() {
            addCriterion("supply_price is null");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceIsNotNull() {
            addCriterion("supply_price is not null");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceEqualTo(BigDecimal value) {
            addCriterion("supply_price =", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceNotEqualTo(BigDecimal value) {
            addCriterion("supply_price <>", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceGreaterThan(BigDecimal value) {
            addCriterion("supply_price >", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("supply_price >=", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceLessThan(BigDecimal value) {
            addCriterion("supply_price <", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("supply_price <=", value, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceIn(List<BigDecimal> values) {
            addCriterion("supply_price in", values, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceNotIn(List<BigDecimal> values) {
            addCriterion("supply_price not in", values, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("supply_price between", value1, value2, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("supply_price not between", value1, value2, "supplyPrice");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyIsNull() {
            addCriterion("supply_price_currency is null");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyIsNotNull() {
            addCriterion("supply_price_currency is not null");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyEqualTo(String value) {
            addCriterion("supply_price_currency =", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyNotEqualTo(String value) {
            addCriterion("supply_price_currency <>", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyGreaterThan(String value) {
            addCriterion("supply_price_currency >", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("supply_price_currency >=", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyLessThan(String value) {
            addCriterion("supply_price_currency <", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyLessThanOrEqualTo(String value) {
            addCriterion("supply_price_currency <=", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyLike(String value) {
            addCriterion("supply_price_currency like", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyNotLike(String value) {
            addCriterion("supply_price_currency not like", value, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyIn(List<String> values) {
            addCriterion("supply_price_currency in", values, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyNotIn(List<String> values) {
            addCriterion("supply_price_currency not in", values, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyBetween(String value1, String value2) {
            addCriterion("supply_price_currency between", value1, value2, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplyPriceCurrencyNotBetween(String value1, String value2) {
            addCriterion("supply_price_currency not between", value1, value2, "supplyPriceCurrency");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeIsNull() {
            addCriterion("supplier_barcode is null");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeIsNotNull() {
            addCriterion("supplier_barcode is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeEqualTo(String value) {
            addCriterion("supplier_barcode =", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeNotEqualTo(String value) {
            addCriterion("supplier_barcode <>", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeGreaterThan(String value) {
            addCriterion("supplier_barcode >", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_barcode >=", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeLessThan(String value) {
            addCriterion("supplier_barcode <", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeLessThanOrEqualTo(String value) {
            addCriterion("supplier_barcode <=", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeLike(String value) {
            addCriterion("supplier_barcode like", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeNotLike(String value) {
            addCriterion("supplier_barcode not like", value, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeIn(List<String> values) {
            addCriterion("supplier_barcode in", values, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeNotIn(List<String> values) {
            addCriterion("supplier_barcode not in", values, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeBetween(String value1, String value2) {
            addCriterion("supplier_barcode between", value1, value2, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andSupplierBarcodeNotBetween(String value1, String value2) {
            addCriterion("supplier_barcode not between", value1, value2, "supplierBarcode");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeIsNull() {
            addCriterion("hub_sku_size is null");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeIsNotNull() {
            addCriterion("hub_sku_size is not null");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeEqualTo(String value) {
            addCriterion("hub_sku_size =", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeNotEqualTo(String value) {
            addCriterion("hub_sku_size <>", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeGreaterThan(String value) {
            addCriterion("hub_sku_size >", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeGreaterThanOrEqualTo(String value) {
            addCriterion("hub_sku_size >=", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeLessThan(String value) {
            addCriterion("hub_sku_size <", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeLessThanOrEqualTo(String value) {
            addCriterion("hub_sku_size <=", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeLike(String value) {
            addCriterion("hub_sku_size like", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeNotLike(String value) {
            addCriterion("hub_sku_size not like", value, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeIn(List<String> values) {
            addCriterion("hub_sku_size in", values, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeNotIn(List<String> values) {
            addCriterion("hub_sku_size not in", values, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeBetween(String value1, String value2) {
            addCriterion("hub_sku_size between", value1, value2, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andHubSkuSizeNotBetween(String value1, String value2) {
            addCriterion("hub_sku_size not between", value1, value2, "hubSkuSize");
            return (Criteria) this;
        }

        public Criteria andStockIsNull() {
            addCriterion("stock is null");
            return (Criteria) this;
        }

        public Criteria andStockIsNotNull() {
            addCriterion("stock is not null");
            return (Criteria) this;
        }

        public Criteria andStockEqualTo(Integer value) {
            addCriterion("stock =", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotEqualTo(Integer value) {
            addCriterion("stock <>", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThan(Integer value) {
            addCriterion("stock >", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThanOrEqualTo(Integer value) {
            addCriterion("stock >=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThan(Integer value) {
            addCriterion("stock <", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThanOrEqualTo(Integer value) {
            addCriterion("stock <=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockIn(List<Integer> values) {
            addCriterion("stock in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotIn(List<Integer> values) {
            addCriterion("stock not in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockBetween(Integer value1, Integer value2) {
            addCriterion("stock between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotBetween(Integer value1, Integer value2) {
            addCriterion("stock not between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeIsNull() {
            addCriterion("last_pull_time is null");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeIsNotNull() {
            addCriterion("last_pull_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeEqualTo(Date value) {
            addCriterion("last_pull_time =", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeNotEqualTo(Date value) {
            addCriterion("last_pull_time <>", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeGreaterThan(Date value) {
            addCriterion("last_pull_time >", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_pull_time >=", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeLessThan(Date value) {
            addCriterion("last_pull_time <", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_pull_time <=", value, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeIn(List<Date> values) {
            addCriterion("last_pull_time in", values, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeNotIn(List<Date> values) {
            addCriterion("last_pull_time not in", values, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeBetween(Date value1, Date value2) {
            addCriterion("last_pull_time between", value1, value2, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andLastPullTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_pull_time not between", value1, value2, "lastPullTime");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIsNull() {
            addCriterion("sp_sku_no is null");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIsNotNull() {
            addCriterion("sp_sku_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoEqualTo(String value) {
            addCriterion("sp_sku_no =", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotEqualTo(String value) {
            addCriterion("sp_sku_no <>", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoGreaterThan(String value) {
            addCriterion("sp_sku_no >", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoGreaterThanOrEqualTo(String value) {
            addCriterion("sp_sku_no >=", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLessThan(String value) {
            addCriterion("sp_sku_no <", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLessThanOrEqualTo(String value) {
            addCriterion("sp_sku_no <=", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLike(String value) {
            addCriterion("sp_sku_no like", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotLike(String value) {
            addCriterion("sp_sku_no not like", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIn(List<String> values) {
            addCriterion("sp_sku_no in", values, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotIn(List<String> values) {
            addCriterion("sp_sku_no not in", values, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoBetween(String value1, String value2) {
            addCriterion("sp_sku_no between", value1, value2, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotBetween(String value1, String value2) {
            addCriterion("sp_sku_no not between", value1, value2, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNull() {
            addCriterion("measurement is null");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNotNull() {
            addCriterion("measurement is not null");
            return (Criteria) this;
        }

        public Criteria andMeasurementEqualTo(String value) {
            addCriterion("measurement =", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotEqualTo(String value) {
            addCriterion("measurement <>", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThan(String value) {
            addCriterion("measurement >", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThanOrEqualTo(String value) {
            addCriterion("measurement >=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThan(String value) {
            addCriterion("measurement <", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThanOrEqualTo(String value) {
            addCriterion("measurement <=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLike(String value) {
            addCriterion("measurement like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotLike(String value) {
            addCriterion("measurement not like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementIn(List<String> values) {
            addCriterion("measurement in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotIn(List<String> values) {
            addCriterion("measurement not in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementBetween(String value1, String value2) {
            addCriterion("measurement between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotBetween(String value1, String value2) {
            addCriterion("measurement not between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andPriceStateIsNull() {
            addCriterion("price_state is null");
            return (Criteria) this;
        }

        public Criteria andPriceStateIsNotNull() {
            addCriterion("price_state is not null");
            return (Criteria) this;
        }

        public Criteria andPriceStateEqualTo(Byte value) {
            addCriterion("price_state =", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateNotEqualTo(Byte value) {
            addCriterion("price_state <>", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateGreaterThan(Byte value) {
            addCriterion("price_state >", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("price_state >=", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateLessThan(Byte value) {
            addCriterion("price_state <", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateLessThanOrEqualTo(Byte value) {
            addCriterion("price_state <=", value, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateIn(List<Byte> values) {
            addCriterion("price_state in", values, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateNotIn(List<Byte> values) {
            addCriterion("price_state not in", values, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateBetween(Byte value1, Byte value2) {
            addCriterion("price_state between", value1, value2, "priceState");
            return (Criteria) this;
        }

        public Criteria andPriceStateNotBetween(Byte value1, Byte value2) {
            addCriterion("price_state not between", value1, value2, "priceState");
            return (Criteria) this;
        }

        public Criteria andSkuStateIsNull() {
            addCriterion("sku_state is null");
            return (Criteria) this;
        }

        public Criteria andSkuStateIsNotNull() {
            addCriterion("sku_state is not null");
            return (Criteria) this;
        }

        public Criteria andSkuStateEqualTo(Byte value) {
            addCriterion("sku_state =", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateNotEqualTo(Byte value) {
            addCriterion("sku_state <>", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateGreaterThan(Byte value) {
            addCriterion("sku_state >", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("sku_state >=", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateLessThan(Byte value) {
            addCriterion("sku_state <", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateLessThanOrEqualTo(Byte value) {
            addCriterion("sku_state <=", value, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateIn(List<Byte> values) {
            addCriterion("sku_state in", values, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateNotIn(List<Byte> values) {
            addCriterion("sku_state not in", values, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateBetween(Byte value1, Byte value2) {
            addCriterion("sku_state between", value1, value2, "skuState");
            return (Criteria) this;
        }

        public Criteria andSkuStateNotBetween(Byte value1, Byte value2) {
            addCriterion("sku_state not between", value1, value2, "skuState");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoIsNull() {
            addCriterion("hub_sku_no is null");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoIsNotNull() {
            addCriterion("hub_sku_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoEqualTo(String value) {
            addCriterion("hub_sku_no =", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoNotEqualTo(String value) {
            addCriterion("hub_sku_no <>", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoGreaterThan(String value) {
            addCriterion("hub_sku_no >", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_sku_no >=", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoLessThan(String value) {
            addCriterion("hub_sku_no <", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoLessThanOrEqualTo(String value) {
            addCriterion("hub_sku_no <=", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoLike(String value) {
            addCriterion("hub_sku_no like", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoNotLike(String value) {
            addCriterion("hub_sku_no not like", value, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoIn(List<String> values) {
            addCriterion("hub_sku_no in", values, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoNotIn(List<String> values) {
            addCriterion("hub_sku_no not in", values, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoBetween(String value1, String value2) {
            addCriterion("hub_sku_no between", value1, value2, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andHubSkuNoNotBetween(String value1, String value2) {
            addCriterion("hub_sku_no not between", value1, value2, "hubSkuNo");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("memo is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("memo is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("memo =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("memo <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("memo >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("memo >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("memo <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("memo <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("memo like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("memo not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("memo in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("memo not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("memo between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("memo not between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andDataStateIsNull() {
            addCriterion("data_state is null");
            return (Criteria) this;
        }

        public Criteria andDataStateIsNotNull() {
            addCriterion("data_state is not null");
            return (Criteria) this;
        }

        public Criteria andDataStateEqualTo(Byte value) {
            addCriterion("data_state =", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotEqualTo(Byte value) {
            addCriterion("data_state <>", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateGreaterThan(Byte value) {
            addCriterion("data_state >", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("data_state >=", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateLessThan(Byte value) {
            addCriterion("data_state <", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateLessThanOrEqualTo(Byte value) {
            addCriterion("data_state <=", value, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateIn(List<Byte> values) {
            addCriterion("data_state in", values, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotIn(List<Byte> values) {
            addCriterion("data_state not in", values, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateBetween(Byte value1, Byte value2) {
            addCriterion("data_state between", value1, value2, "dataState");
            return (Criteria) this;
        }

        public Criteria andDataStateNotBetween(Byte value1, Byte value2) {
            addCriterion("data_state not between", value1, value2, "dataState");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andInfoFromIsNull() {
            addCriterion("info_from is null");
            return (Criteria) this;
        }

        public Criteria andInfoFromIsNotNull() {
            addCriterion("info_from is not null");
            return (Criteria) this;
        }

        public Criteria andInfoFromEqualTo(Byte value) {
            addCriterion("info_from =", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotEqualTo(Byte value) {
            addCriterion("info_from <>", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromGreaterThan(Byte value) {
            addCriterion("info_from >", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromGreaterThanOrEqualTo(Byte value) {
            addCriterion("info_from >=", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromLessThan(Byte value) {
            addCriterion("info_from <", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromLessThanOrEqualTo(Byte value) {
            addCriterion("info_from <=", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromIn(List<Byte> values) {
            addCriterion("info_from in", values, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotIn(List<Byte> values) {
            addCriterion("info_from not in", values, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromBetween(Byte value1, Byte value2) {
            addCriterion("info_from between", value1, value2, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotBetween(Byte value1, Byte value2) {
            addCriterion("info_from not between", value1, value2, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateIsNull() {
            addCriterion("sp_sku_size_state is null");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateIsNotNull() {
            addCriterion("sp_sku_size_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateEqualTo(Byte value) {
            addCriterion("sp_sku_size_state =", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateNotEqualTo(Byte value) {
            addCriterion("sp_sku_size_state <>", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateGreaterThan(Byte value) {
            addCriterion("sp_sku_size_state >", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("sp_sku_size_state >=", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateLessThan(Byte value) {
            addCriterion("sp_sku_size_state <", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateLessThanOrEqualTo(Byte value) {
            addCriterion("sp_sku_size_state <=", value, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateIn(List<Byte> values) {
            addCriterion("sp_sku_size_state in", values, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateNotIn(List<Byte> values) {
            addCriterion("sp_sku_size_state not in", values, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateBetween(Byte value1, Byte value2) {
            addCriterion("sp_sku_size_state between", value1, value2, "spSkuSizeState");
            return (Criteria) this;
        }

        public Criteria andSpSkuSizeStateNotBetween(Byte value1, Byte value2) {
            addCriterion("sp_sku_size_state not between", value1, value2, "spSkuSizeState");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

    	public Criteria() {
            super();
        }
    }

    public static class Criterion {
    	
        public Criterion() {
			super();
		}

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