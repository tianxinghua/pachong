package com.shangpin.ephub.data.mysql.sku.supplier.po;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class HubSupplierSkuCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSupplierSkuCriteria() {
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
        @JsonIgnore
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

        public Criteria andSupplierSkuIdIsNull() {
            addCriterion("supplier_sku_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdIsNotNull() {
            addCriterion("supplier_sku_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdEqualTo(Long value) {
            addCriterion("supplier_sku_id =", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotEqualTo(Long value) {
            addCriterion("supplier_sku_id <>", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdGreaterThan(Long value) {
            addCriterion("supplier_sku_id >", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("supplier_sku_id >=", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdLessThan(Long value) {
            addCriterion("supplier_sku_id <", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdLessThanOrEqualTo(Long value) {
            addCriterion("supplier_sku_id <=", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdIn(List<Long> values) {
            addCriterion("supplier_sku_id in", values, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotIn(List<Long> values) {
            addCriterion("supplier_sku_id not in", values, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdBetween(Long value1, Long value2) {
            addCriterion("supplier_sku_id between", value1, value2, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotBetween(Long value1, Long value2) {
            addCriterion("supplier_sku_id not between", value1, value2, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdIsNull() {
            addCriterion("supplier_spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdIsNotNull() {
            addCriterion("supplier_spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdEqualTo(Long value) {
            addCriterion("supplier_spu_id =", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotEqualTo(Long value) {
            addCriterion("supplier_spu_id <>", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdGreaterThan(Long value) {
            addCriterion("supplier_spu_id >", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("supplier_spu_id >=", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdLessThan(Long value) {
            addCriterion("supplier_spu_id <", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdLessThanOrEqualTo(Long value) {
            addCriterion("supplier_spu_id <=", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdIn(List<Long> values) {
            addCriterion("supplier_spu_id in", values, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotIn(List<Long> values) {
            addCriterion("supplier_spu_id not in", values, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdBetween(Long value1, Long value2) {
            addCriterion("supplier_spu_id between", value1, value2, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotBetween(Long value1, Long value2) {
            addCriterion("supplier_spu_id not between", value1, value2, "supplierSpuId");
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

        public Criteria andSupplierSkuNameIsNull() {
            addCriterion("supplier_sku_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameIsNotNull() {
            addCriterion("supplier_sku_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameEqualTo(String value) {
            addCriterion("supplier_sku_name =", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameNotEqualTo(String value) {
            addCriterion("supplier_sku_name <>", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameGreaterThan(String value) {
            addCriterion("supplier_sku_name >", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_sku_name >=", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameLessThan(String value) {
            addCriterion("supplier_sku_name <", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_sku_name <=", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameLike(String value) {
            addCriterion("supplier_sku_name like", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameNotLike(String value) {
            addCriterion("supplier_sku_name not like", value, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameIn(List<String> values) {
            addCriterion("supplier_sku_name in", values, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameNotIn(List<String> values) {
            addCriterion("supplier_sku_name not in", values, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameBetween(String value1, String value2) {
            addCriterion("supplier_sku_name between", value1, value2, "supplierSkuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuNameNotBetween(String value1, String value2) {
            addCriterion("supplier_sku_name not between", value1, value2, "supplierSkuName");
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

        public Criteria andSupplierSkuSizeIsNull() {
            addCriterion("supplier_sku_size is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeIsNotNull() {
            addCriterion("supplier_sku_size is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeEqualTo(String value) {
            addCriterion("supplier_sku_size =", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeNotEqualTo(String value) {
            addCriterion("supplier_sku_size <>", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeGreaterThan(String value) {
            addCriterion("supplier_sku_size >", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_sku_size >=", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeLessThan(String value) {
            addCriterion("supplier_sku_size <", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeLessThanOrEqualTo(String value) {
            addCriterion("supplier_sku_size <=", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeLike(String value) {
            addCriterion("supplier_sku_size like", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeNotLike(String value) {
            addCriterion("supplier_sku_size not like", value, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeIn(List<String> values) {
            addCriterion("supplier_sku_size in", values, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeNotIn(List<String> values) {
            addCriterion("supplier_sku_size not in", values, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeBetween(String value1, String value2) {
            addCriterion("supplier_sku_size between", value1, value2, "supplierSkuSize");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuSizeNotBetween(String value1, String value2) {
            addCriterion("supplier_sku_size not between", value1, value2, "supplierSkuSize");
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

        public Criteria andPushStateIsNull() {
            addCriterion("push_state is null");
            return (Criteria) this;
        }

        public Criteria andPushStateIsNotNull() {
            addCriterion("push_state is not null");
            return (Criteria) this;
        }

        public Criteria andPushStateEqualTo(Byte value) {
            addCriterion("push_state =", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotEqualTo(Byte value) {
            addCriterion("push_state <>", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateGreaterThan(Byte value) {
            addCriterion("push_state >", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("push_state >=", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateLessThan(Byte value) {
            addCriterion("push_state <", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateLessThanOrEqualTo(Byte value) {
            addCriterion("push_state <=", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateIn(List<Byte> values) {
            addCriterion("push_state in", values, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotIn(List<Byte> values) {
            addCriterion("push_state not in", values, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateBetween(Byte value1, Byte value2) {
            addCriterion("push_state between", value1, value2, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotBetween(Byte value1, Byte value2) {
            addCriterion("push_state not between", value1, value2, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushResultIsNull() {
            addCriterion("push_result is null");
            return (Criteria) this;
        }

        public Criteria andPushResultIsNotNull() {
            addCriterion("push_result is not null");
            return (Criteria) this;
        }

        public Criteria andPushResultEqualTo(String value) {
            addCriterion("push_result =", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultNotEqualTo(String value) {
            addCriterion("push_result <>", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultGreaterThan(String value) {
            addCriterion("push_result >", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultGreaterThanOrEqualTo(String value) {
            addCriterion("push_result >=", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultLessThan(String value) {
            addCriterion("push_result <", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultLessThanOrEqualTo(String value) {
            addCriterion("push_result <=", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultLike(String value) {
            addCriterion("push_result like", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultNotLike(String value) {
            addCriterion("push_result not like", value, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultIn(List<String> values) {
            addCriterion("push_result in", values, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultNotIn(List<String> values) {
            addCriterion("push_result not in", values, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultBetween(String value1, String value2) {
            addCriterion("push_result between", value1, value2, "pushResult");
            return (Criteria) this;
        }

        public Criteria andPushResultNotBetween(String value1, String value2) {
            addCriterion("push_result not between", value1, value2, "pushResult");
            return (Criteria) this;
        }

        public Criteria andFilterFlagIsNull() {
            addCriterion("filter_flag is null");
            return (Criteria) this;
        }

        public Criteria andFilterFlagIsNotNull() {
            addCriterion("filter_flag is not null");
            return (Criteria) this;
        }

        public Criteria andFilterFlagEqualTo(Byte value) {
            addCriterion("filter_flag =", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagNotEqualTo(Byte value) {
            addCriterion("filter_flag <>", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagGreaterThan(Byte value) {
            addCriterion("filter_flag >", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("filter_flag >=", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagLessThan(Byte value) {
            addCriterion("filter_flag <", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagLessThanOrEqualTo(Byte value) {
            addCriterion("filter_flag <=", value, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagIn(List<Byte> values) {
            addCriterion("filter_flag in", values, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagNotIn(List<Byte> values) {
            addCriterion("filter_flag not in", values, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagBetween(Byte value1, Byte value2) {
            addCriterion("filter_flag between", value1, value2, "filterFlag");
            return (Criteria) this;
        }

        public Criteria andFilterFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("filter_flag not between", value1, value2, "filterFlag");
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