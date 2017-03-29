package com.shangpin.ephub.data.mysql.sku.price.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSupplierPriceChangeRecordCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSupplierPriceChangeRecordCriteria() {
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

        public Criteria andSupplierPriceChangeRecordIdIsNull() {
            addCriterion("supplier_price_change_record_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdIsNotNull() {
            addCriterion("supplier_price_change_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdEqualTo(Long value) {
            addCriterion("supplier_price_change_record_id =", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdNotEqualTo(Long value) {
            addCriterion("supplier_price_change_record_id <>", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdGreaterThan(Long value) {
            addCriterion("supplier_price_change_record_id >", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("supplier_price_change_record_id >=", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdLessThan(Long value) {
            addCriterion("supplier_price_change_record_id <", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdLessThanOrEqualTo(Long value) {
            addCriterion("supplier_price_change_record_id <=", value, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdIn(List<Long> values) {
            addCriterion("supplier_price_change_record_id in", values, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdNotIn(List<Long> values) {
            addCriterion("supplier_price_change_record_id not in", values, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdBetween(Long value1, Long value2) {
            addCriterion("supplier_price_change_record_id between", value1, value2, "supplierPriceChangeRecordId");
            return (Criteria) this;
        }

        public Criteria andSupplierPriceChangeRecordIdNotBetween(Long value1, Long value2) {
            addCriterion("supplier_price_change_record_id not between", value1, value2, "supplierPriceChangeRecordId");
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

        public Criteria andSupplierNoIsNull() {
            addCriterion("supplier_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierNoIsNotNull() {
            addCriterion("supplier_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierNoEqualTo(String value) {
            addCriterion("supplier_no =", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoNotEqualTo(String value) {
            addCriterion("supplier_no <>", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoGreaterThan(String value) {
            addCriterion("supplier_no >", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_no >=", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoLessThan(String value) {
            addCriterion("supplier_no <", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_no <=", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoLike(String value) {
            addCriterion("supplier_no like", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoNotLike(String value) {
            addCriterion("supplier_no not like", value, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoIn(List<String> values) {
            addCriterion("supplier_no in", values, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoNotIn(List<String> values) {
            addCriterion("supplier_no not in", values, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoBetween(String value1, String value2) {
            addCriterion("supplier_no between", value1, value2, "supplierNo");
            return (Criteria) this;
        }

        public Criteria andSupplierNoNotBetween(String value1, String value2) {
            addCriterion("supplier_no not between", value1, value2, "supplierNo");
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

        public Criteria andSupplierSpuNoIsNull() {
            addCriterion("supplier_spu_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoIsNotNull() {
            addCriterion("supplier_spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoEqualTo(String value) {
            addCriterion("supplier_spu_no =", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotEqualTo(String value) {
            addCriterion("supplier_spu_no <>", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoGreaterThan(String value) {
            addCriterion("supplier_spu_no >", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_no >=", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLessThan(String value) {
            addCriterion("supplier_spu_no <", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_no <=", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLike(String value) {
            addCriterion("supplier_spu_no like", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotLike(String value) {
            addCriterion("supplier_spu_no not like", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoIn(List<String> values) {
            addCriterion("supplier_spu_no in", values, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotIn(List<String> values) {
            addCriterion("supplier_spu_no not in", values, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoBetween(String value1, String value2) {
            addCriterion("supplier_spu_no between", value1, value2, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_no not between", value1, value2, "supplierSpuNo");
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

        public Criteria andCurrencyIsNull() {
            addCriterion("currency is null");
            return (Criteria) this;
        }

        public Criteria andCurrencyIsNotNull() {
            addCriterion("currency is not null");
            return (Criteria) this;
        }

        public Criteria andCurrencyEqualTo(String value) {
            addCriterion("currency =", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotEqualTo(String value) {
            addCriterion("currency <>", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThan(String value) {
            addCriterion("currency >", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("currency >=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThan(String value) {
            addCriterion("currency <", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLessThanOrEqualTo(String value) {
            addCriterion("currency <=", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyLike(String value) {
            addCriterion("currency like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotLike(String value) {
            addCriterion("currency not like", value, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyIn(List<String> values) {
            addCriterion("currency in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotIn(List<String> values) {
            addCriterion("currency not in", values, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyBetween(String value1, String value2) {
            addCriterion("currency between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andCurrencyNotBetween(String value1, String value2) {
            addCriterion("currency not between", value1, value2, "currency");
            return (Criteria) this;
        }

        public Criteria andMarketYearIsNull() {
            addCriterion("market_year is null");
            return (Criteria) this;
        }

        public Criteria andMarketYearIsNotNull() {
            addCriterion("market_year is not null");
            return (Criteria) this;
        }

        public Criteria andMarketYearEqualTo(String value) {
            addCriterion("market_year =", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearNotEqualTo(String value) {
            addCriterion("market_year <>", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearGreaterThan(String value) {
            addCriterion("market_year >", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearGreaterThanOrEqualTo(String value) {
            addCriterion("market_year >=", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearLessThan(String value) {
            addCriterion("market_year <", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearLessThanOrEqualTo(String value) {
            addCriterion("market_year <=", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearLike(String value) {
            addCriterion("market_year like", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearNotLike(String value) {
            addCriterion("market_year not like", value, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearIn(List<String> values) {
            addCriterion("market_year in", values, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearNotIn(List<String> values) {
            addCriterion("market_year not in", values, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearBetween(String value1, String value2) {
            addCriterion("market_year between", value1, value2, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketYearNotBetween(String value1, String value2) {
            addCriterion("market_year not between", value1, value2, "marketYear");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonIsNull() {
            addCriterion("market_season is null");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonIsNotNull() {
            addCriterion("market_season is not null");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonEqualTo(String value) {
            addCriterion("market_season =", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonNotEqualTo(String value) {
            addCriterion("market_season <>", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonGreaterThan(String value) {
            addCriterion("market_season >", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonGreaterThanOrEqualTo(String value) {
            addCriterion("market_season >=", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonLessThan(String value) {
            addCriterion("market_season <", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonLessThanOrEqualTo(String value) {
            addCriterion("market_season <=", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonLike(String value) {
            addCriterion("market_season like", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonNotLike(String value) {
            addCriterion("market_season not like", value, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonIn(List<String> values) {
            addCriterion("market_season in", values, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonNotIn(List<String> values) {
            addCriterion("market_season not in", values, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonBetween(String value1, String value2) {
            addCriterion("market_season between", value1, value2, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andMarketSeasonNotBetween(String value1, String value2) {
            addCriterion("market_season not between", value1, value2, "marketSeason");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Boolean value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Boolean value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Boolean value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Boolean value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Boolean value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Boolean> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Boolean> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Boolean value1, Boolean value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Boolean value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Boolean value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Boolean value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Boolean value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Boolean value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Boolean value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Boolean> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Boolean> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Boolean value1, Boolean value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Boolean value1, Boolean value2) {
            addCriterion("state not between", value1, value2, "state");
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