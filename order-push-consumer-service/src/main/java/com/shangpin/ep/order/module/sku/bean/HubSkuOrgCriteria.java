package com.shangpin.ep.order.module.sku.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <p>Title:HubSkuOrgCriteria.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:44:25
 */
public class HubSkuOrgCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSkuOrgCriteria() {
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

        public Criteria andSkuorgidIsNull() {
            addCriterion("SkuOrgId is null");
            return (Criteria) this;
        }

        public Criteria andSkuorgidIsNotNull() {
            addCriterion("SkuOrgId is not null");
            return (Criteria) this;
        }

        public Criteria andSkuorgidEqualTo(Long value) {
            addCriterion("SkuOrgId =", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidNotEqualTo(Long value) {
            addCriterion("SkuOrgId <>", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidGreaterThan(Long value) {
            addCriterion("SkuOrgId >", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidGreaterThanOrEqualTo(Long value) {
            addCriterion("SkuOrgId >=", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidLessThan(Long value) {
            addCriterion("SkuOrgId <", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidLessThanOrEqualTo(Long value) {
            addCriterion("SkuOrgId <=", value, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidIn(List<Long> values) {
            addCriterion("SkuOrgId in", values, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidNotIn(List<Long> values) {
            addCriterion("SkuOrgId not in", values, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidBetween(Long value1, Long value2) {
            addCriterion("SkuOrgId between", value1, value2, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSkuorgidNotBetween(Long value1, Long value2) {
            addCriterion("SkuOrgId not between", value1, value2, "skuorgid");
            return (Criteria) this;
        }

        public Criteria andSupplieridIsNull() {
            addCriterion("SupplierId is null");
            return (Criteria) this;
        }

        public Criteria andSupplieridIsNotNull() {
            addCriterion("SupplierId is not null");
            return (Criteria) this;
        }

        public Criteria andSupplieridEqualTo(String value) {
            addCriterion("SupplierId =", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotEqualTo(String value) {
            addCriterion("SupplierId <>", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridGreaterThan(String value) {
            addCriterion("SupplierId >", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridGreaterThanOrEqualTo(String value) {
            addCriterion("SupplierId >=", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLessThan(String value) {
            addCriterion("SupplierId <", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLessThanOrEqualTo(String value) {
            addCriterion("SupplierId <=", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLike(String value) {
            addCriterion("SupplierId like", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotLike(String value) {
            addCriterion("SupplierId not like", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridIn(List<String> values) {
            addCriterion("SupplierId in", values, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotIn(List<String> values) {
            addCriterion("SupplierId not in", values, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridBetween(String value1, String value2) {
            addCriterion("SupplierId between", value1, value2, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotBetween(String value1, String value2) {
            addCriterion("SupplierId not between", value1, value2, "supplierid");
            return (Criteria) this;
        }

        public Criteria andProductorgidIsNull() {
            addCriterion("ProductOrgId is null");
            return (Criteria) this;
        }

        public Criteria andProductorgidIsNotNull() {
            addCriterion("ProductOrgId is not null");
            return (Criteria) this;
        }

        public Criteria andProductorgidEqualTo(Long value) {
            addCriterion("ProductOrgId =", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidNotEqualTo(Long value) {
            addCriterion("ProductOrgId <>", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidGreaterThan(Long value) {
            addCriterion("ProductOrgId >", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidGreaterThanOrEqualTo(Long value) {
            addCriterion("ProductOrgId >=", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidLessThan(Long value) {
            addCriterion("ProductOrgId <", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidLessThanOrEqualTo(Long value) {
            addCriterion("ProductOrgId <=", value, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidIn(List<Long> values) {
            addCriterion("ProductOrgId in", values, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidNotIn(List<Long> values) {
            addCriterion("ProductOrgId not in", values, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidBetween(Long value1, Long value2) {
            addCriterion("ProductOrgId between", value1, value2, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductorgidNotBetween(Long value1, Long value2) {
            addCriterion("ProductOrgId not between", value1, value2, "productorgid");
            return (Criteria) this;
        }

        public Criteria andProductnoorgIsNull() {
            addCriterion("ProductNoOrg is null");
            return (Criteria) this;
        }

        public Criteria andProductnoorgIsNotNull() {
            addCriterion("ProductNoOrg is not null");
            return (Criteria) this;
        }

        public Criteria andProductnoorgEqualTo(String value) {
            addCriterion("ProductNoOrg =", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgNotEqualTo(String value) {
            addCriterion("ProductNoOrg <>", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgGreaterThan(String value) {
            addCriterion("ProductNoOrg >", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgGreaterThanOrEqualTo(String value) {
            addCriterion("ProductNoOrg >=", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgLessThan(String value) {
            addCriterion("ProductNoOrg <", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgLessThanOrEqualTo(String value) {
            addCriterion("ProductNoOrg <=", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgLike(String value) {
            addCriterion("ProductNoOrg like", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgNotLike(String value) {
            addCriterion("ProductNoOrg not like", value, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgIn(List<String> values) {
            addCriterion("ProductNoOrg in", values, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgNotIn(List<String> values) {
            addCriterion("ProductNoOrg not in", values, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgBetween(String value1, String value2) {
            addCriterion("ProductNoOrg between", value1, value2, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andProductnoorgNotBetween(String value1, String value2) {
            addCriterion("ProductNoOrg not between", value1, value2, "productnoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgIsNull() {
            addCriterion("SupplierSkuNoOrg is null");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgIsNotNull() {
            addCriterion("SupplierSkuNoOrg is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgEqualTo(String value) {
            addCriterion("SupplierSkuNoOrg =", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgNotEqualTo(String value) {
            addCriterion("SupplierSkuNoOrg <>", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgGreaterThan(String value) {
            addCriterion("SupplierSkuNoOrg >", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgGreaterThanOrEqualTo(String value) {
            addCriterion("SupplierSkuNoOrg >=", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgLessThan(String value) {
            addCriterion("SupplierSkuNoOrg <", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgLessThanOrEqualTo(String value) {
            addCriterion("SupplierSkuNoOrg <=", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgLike(String value) {
            addCriterion("SupplierSkuNoOrg like", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgNotLike(String value) {
            addCriterion("SupplierSkuNoOrg not like", value, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgIn(List<String> values) {
            addCriterion("SupplierSkuNoOrg in", values, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgNotIn(List<String> values) {
            addCriterion("SupplierSkuNoOrg not in", values, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgBetween(String value1, String value2) {
            addCriterion("SupplierSkuNoOrg between", value1, value2, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSupplierskunoorgNotBetween(String value1, String value2) {
            addCriterion("SupplierSkuNoOrg not between", value1, value2, "supplierskunoorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgIsNull() {
            addCriterion("SkuNameOrg is null");
            return (Criteria) this;
        }

        public Criteria andSkunameorgIsNotNull() {
            addCriterion("SkuNameOrg is not null");
            return (Criteria) this;
        }

        public Criteria andSkunameorgEqualTo(String value) {
            addCriterion("SkuNameOrg =", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgNotEqualTo(String value) {
            addCriterion("SkuNameOrg <>", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgGreaterThan(String value) {
            addCriterion("SkuNameOrg >", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgGreaterThanOrEqualTo(String value) {
            addCriterion("SkuNameOrg >=", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgLessThan(String value) {
            addCriterion("SkuNameOrg <", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgLessThanOrEqualTo(String value) {
            addCriterion("SkuNameOrg <=", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgLike(String value) {
            addCriterion("SkuNameOrg like", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgNotLike(String value) {
            addCriterion("SkuNameOrg not like", value, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgIn(List<String> values) {
            addCriterion("SkuNameOrg in", values, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgNotIn(List<String> values) {
            addCriterion("SkuNameOrg not in", values, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgBetween(String value1, String value2) {
            addCriterion("SkuNameOrg between", value1, value2, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andSkunameorgNotBetween(String value1, String value2) {
            addCriterion("SkuNameOrg not between", value1, value2, "skunameorg");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIsNull() {
            addCriterion("MarketPrice is null");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIsNotNull() {
            addCriterion("MarketPrice is not null");
            return (Criteria) this;
        }

        public Criteria andMarketpriceEqualTo(BigDecimal value) {
            addCriterion("MarketPrice =", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotEqualTo(BigDecimal value) {
            addCriterion("MarketPrice <>", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceGreaterThan(BigDecimal value) {
            addCriterion("MarketPrice >", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MarketPrice >=", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceLessThan(BigDecimal value) {
            addCriterion("MarketPrice <", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MarketPrice <=", value, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceIn(List<BigDecimal> values) {
            addCriterion("MarketPrice in", values, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotIn(List<BigDecimal> values) {
            addCriterion("MarketPrice not in", values, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MarketPrice between", value1, value2, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MarketPrice not between", value1, value2, "marketprice");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgIsNull() {
            addCriterion("MarketPriceCurrencyOrg is null");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgIsNotNull() {
            addCriterion("MarketPriceCurrencyOrg is not null");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgEqualTo(String value) {
            addCriterion("MarketPriceCurrencyOrg =", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgNotEqualTo(String value) {
            addCriterion("MarketPriceCurrencyOrg <>", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgGreaterThan(String value) {
            addCriterion("MarketPriceCurrencyOrg >", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgGreaterThanOrEqualTo(String value) {
            addCriterion("MarketPriceCurrencyOrg >=", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgLessThan(String value) {
            addCriterion("MarketPriceCurrencyOrg <", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgLessThanOrEqualTo(String value) {
            addCriterion("MarketPriceCurrencyOrg <=", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgLike(String value) {
            addCriterion("MarketPriceCurrencyOrg like", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgNotLike(String value) {
            addCriterion("MarketPriceCurrencyOrg not like", value, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgIn(List<String> values) {
            addCriterion("MarketPriceCurrencyOrg in", values, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgNotIn(List<String> values) {
            addCriterion("MarketPriceCurrencyOrg not in", values, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgBetween(String value1, String value2) {
            addCriterion("MarketPriceCurrencyOrg between", value1, value2, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andMarketpricecurrencyorgNotBetween(String value1, String value2) {
            addCriterion("MarketPriceCurrencyOrg not between", value1, value2, "marketpricecurrencyorg");
            return (Criteria) this;
        }

        public Criteria andSalespriceIsNull() {
            addCriterion("SalesPrice is null");
            return (Criteria) this;
        }

        public Criteria andSalespriceIsNotNull() {
            addCriterion("SalesPrice is not null");
            return (Criteria) this;
        }

        public Criteria andSalespriceEqualTo(BigDecimal value) {
            addCriterion("SalesPrice =", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceNotEqualTo(BigDecimal value) {
            addCriterion("SalesPrice <>", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceGreaterThan(BigDecimal value) {
            addCriterion("SalesPrice >", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("SalesPrice >=", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceLessThan(BigDecimal value) {
            addCriterion("SalesPrice <", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("SalesPrice <=", value, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceIn(List<BigDecimal> values) {
            addCriterion("SalesPrice in", values, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceNotIn(List<BigDecimal> values) {
            addCriterion("SalesPrice not in", values, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SalesPrice between", value1, value2, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SalesPrice not between", value1, value2, "salesprice");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyIsNull() {
            addCriterion("SalesPriceCurrency is null");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyIsNotNull() {
            addCriterion("SalesPriceCurrency is not null");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyEqualTo(String value) {
            addCriterion("SalesPriceCurrency =", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyNotEqualTo(String value) {
            addCriterion("SalesPriceCurrency <>", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyGreaterThan(String value) {
            addCriterion("SalesPriceCurrency >", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("SalesPriceCurrency >=", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyLessThan(String value) {
            addCriterion("SalesPriceCurrency <", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyLessThanOrEqualTo(String value) {
            addCriterion("SalesPriceCurrency <=", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyLike(String value) {
            addCriterion("SalesPriceCurrency like", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyNotLike(String value) {
            addCriterion("SalesPriceCurrency not like", value, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyIn(List<String> values) {
            addCriterion("SalesPriceCurrency in", values, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyNotIn(List<String> values) {
            addCriterion("SalesPriceCurrency not in", values, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyBetween(String value1, String value2) {
            addCriterion("SalesPriceCurrency between", value1, value2, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSalespricecurrencyNotBetween(String value1, String value2) {
            addCriterion("SalesPriceCurrency not between", value1, value2, "salespricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypriceIsNull() {
            addCriterion("SupplyPrice is null");
            return (Criteria) this;
        }

        public Criteria andSupplypriceIsNotNull() {
            addCriterion("SupplyPrice is not null");
            return (Criteria) this;
        }

        public Criteria andSupplypriceEqualTo(BigDecimal value) {
            addCriterion("SupplyPrice =", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceNotEqualTo(BigDecimal value) {
            addCriterion("SupplyPrice <>", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceGreaterThan(BigDecimal value) {
            addCriterion("SupplyPrice >", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("SupplyPrice >=", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceLessThan(BigDecimal value) {
            addCriterion("SupplyPrice <", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceLessThanOrEqualTo(BigDecimal value) {
            addCriterion("SupplyPrice <=", value, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceIn(List<BigDecimal> values) {
            addCriterion("SupplyPrice in", values, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceNotIn(List<BigDecimal> values) {
            addCriterion("SupplyPrice not in", values, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SupplyPrice between", value1, value2, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypriceNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("SupplyPrice not between", value1, value2, "supplyprice");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyIsNull() {
            addCriterion("SupplyPriceCurrency is null");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyIsNotNull() {
            addCriterion("SupplyPriceCurrency is not null");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyEqualTo(String value) {
            addCriterion("SupplyPriceCurrency =", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyNotEqualTo(String value) {
            addCriterion("SupplyPriceCurrency <>", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyGreaterThan(String value) {
            addCriterion("SupplyPriceCurrency >", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyGreaterThanOrEqualTo(String value) {
            addCriterion("SupplyPriceCurrency >=", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyLessThan(String value) {
            addCriterion("SupplyPriceCurrency <", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyLessThanOrEqualTo(String value) {
            addCriterion("SupplyPriceCurrency <=", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyLike(String value) {
            addCriterion("SupplyPriceCurrency like", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyNotLike(String value) {
            addCriterion("SupplyPriceCurrency not like", value, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyIn(List<String> values) {
            addCriterion("SupplyPriceCurrency in", values, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyNotIn(List<String> values) {
            addCriterion("SupplyPriceCurrency not in", values, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyBetween(String value1, String value2) {
            addCriterion("SupplyPriceCurrency between", value1, value2, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andSupplypricecurrencyNotBetween(String value1, String value2) {
            addCriterion("SupplyPriceCurrency not between", value1, value2, "supplypricecurrency");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgIsNull() {
            addCriterion("BarcodeOrg is null");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgIsNotNull() {
            addCriterion("BarcodeOrg is not null");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgEqualTo(String value) {
            addCriterion("BarcodeOrg =", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgNotEqualTo(String value) {
            addCriterion("BarcodeOrg <>", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgGreaterThan(String value) {
            addCriterion("BarcodeOrg >", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgGreaterThanOrEqualTo(String value) {
            addCriterion("BarcodeOrg >=", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgLessThan(String value) {
            addCriterion("BarcodeOrg <", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgLessThanOrEqualTo(String value) {
            addCriterion("BarcodeOrg <=", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgLike(String value) {
            addCriterion("BarcodeOrg like", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgNotLike(String value) {
            addCriterion("BarcodeOrg not like", value, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgIn(List<String> values) {
            addCriterion("BarcodeOrg in", values, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgNotIn(List<String> values) {
            addCriterion("BarcodeOrg not in", values, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgBetween(String value1, String value2) {
            addCriterion("BarcodeOrg between", value1, value2, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andBarcodeorgNotBetween(String value1, String value2) {
            addCriterion("BarcodeOrg not between", value1, value2, "barcodeorg");
            return (Criteria) this;
        }

        public Criteria andColororgIsNull() {
            addCriterion("ColorOrg is null");
            return (Criteria) this;
        }

        public Criteria andColororgIsNotNull() {
            addCriterion("ColorOrg is not null");
            return (Criteria) this;
        }

        public Criteria andColororgEqualTo(String value) {
            addCriterion("ColorOrg =", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgNotEqualTo(String value) {
            addCriterion("ColorOrg <>", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgGreaterThan(String value) {
            addCriterion("ColorOrg >", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgGreaterThanOrEqualTo(String value) {
            addCriterion("ColorOrg >=", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgLessThan(String value) {
            addCriterion("ColorOrg <", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgLessThanOrEqualTo(String value) {
            addCriterion("ColorOrg <=", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgLike(String value) {
            addCriterion("ColorOrg like", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgNotLike(String value) {
            addCriterion("ColorOrg not like", value, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgIn(List<String> values) {
            addCriterion("ColorOrg in", values, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgNotIn(List<String> values) {
            addCriterion("ColorOrg not in", values, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgBetween(String value1, String value2) {
            addCriterion("ColorOrg between", value1, value2, "colororg");
            return (Criteria) this;
        }

        public Criteria andColororgNotBetween(String value1, String value2) {
            addCriterion("ColorOrg not between", value1, value2, "colororg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgIsNull() {
            addCriterion("SkuSizeOrg is null");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgIsNotNull() {
            addCriterion("SkuSizeOrg is not null");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgEqualTo(String value) {
            addCriterion("SkuSizeOrg =", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgNotEqualTo(String value) {
            addCriterion("SkuSizeOrg <>", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgGreaterThan(String value) {
            addCriterion("SkuSizeOrg >", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgGreaterThanOrEqualTo(String value) {
            addCriterion("SkuSizeOrg >=", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgLessThan(String value) {
            addCriterion("SkuSizeOrg <", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgLessThanOrEqualTo(String value) {
            addCriterion("SkuSizeOrg <=", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgLike(String value) {
            addCriterion("SkuSizeOrg like", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgNotLike(String value) {
            addCriterion("SkuSizeOrg not like", value, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgIn(List<String> values) {
            addCriterion("SkuSizeOrg in", values, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgNotIn(List<String> values) {
            addCriterion("SkuSizeOrg not in", values, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgBetween(String value1, String value2) {
            addCriterion("SkuSizeOrg between", value1, value2, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andSkusizeorgNotBetween(String value1, String value2) {
            addCriterion("SkuSizeOrg not between", value1, value2, "skusizeorg");
            return (Criteria) this;
        }

        public Criteria andStockIsNull() {
            addCriterion("Stock is null");
            return (Criteria) this;
        }

        public Criteria andStockIsNotNull() {
            addCriterion("Stock is not null");
            return (Criteria) this;
        }

        public Criteria andStockEqualTo(Integer value) {
            addCriterion("Stock =", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotEqualTo(Integer value) {
            addCriterion("Stock <>", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThan(Integer value) {
            addCriterion("Stock >", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockGreaterThanOrEqualTo(Integer value) {
            addCriterion("Stock >=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThan(Integer value) {
            addCriterion("Stock <", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockLessThanOrEqualTo(Integer value) {
            addCriterion("Stock <=", value, "stock");
            return (Criteria) this;
        }

        public Criteria andStockIn(List<Integer> values) {
            addCriterion("Stock in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotIn(List<Integer> values) {
            addCriterion("Stock not in", values, "stock");
            return (Criteria) this;
        }

        public Criteria andStockBetween(Integer value1, Integer value2) {
            addCriterion("Stock between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andStockNotBetween(Integer value1, Integer value2) {
            addCriterion("Stock not between", value1, value2, "stock");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("Memo is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("Memo is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("Memo =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("Memo <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("Memo >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("Memo >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("Memo <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("Memo <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("Memo like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("Memo not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("Memo in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("Memo not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("Memo between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("Memo not between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("CreateTime is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("CreateTime is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("CreateTime =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("CreateTime <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("CreateTime >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CreateTime >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("CreateTime <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("CreateTime <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("CreateTime in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("CreateTime not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("CreateTime between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("CreateTime not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("UpdateTime is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("UpdateTime is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("UpdateTime =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("UpdateTime <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("UpdateTime >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UpdateTime >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("UpdateTime <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("UpdateTime <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("UpdateTime in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("UpdateTime not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("UpdateTime between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("UpdateTime not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andLasttimeIsNull() {
            addCriterion("LastTime is null");
            return (Criteria) this;
        }

        public Criteria andLasttimeIsNotNull() {
            addCriterion("LastTime is not null");
            return (Criteria) this;
        }

        public Criteria andLasttimeEqualTo(Date value) {
            addCriterion("LastTime =", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeNotEqualTo(Date value) {
            addCriterion("LastTime <>", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeGreaterThan(Date value) {
            addCriterion("LastTime >", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("LastTime >=", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeLessThan(Date value) {
            addCriterion("LastTime <", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeLessThanOrEqualTo(Date value) {
            addCriterion("LastTime <=", value, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeIn(List<Date> values) {
            addCriterion("LastTime in", values, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeNotIn(List<Date> values) {
            addCriterion("LastTime not in", values, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeBetween(Date value1, Date value2) {
            addCriterion("LastTime between", value1, value2, "lasttime");
            return (Criteria) this;
        }

        public Criteria andLasttimeNotBetween(Date value1, Date value2) {
            addCriterion("LastTime not between", value1, value2, "lasttime");
            return (Criteria) this;
        }

        public Criteria andSpskunoIsNull() {
            addCriterion("SpSkuNo is null");
            return (Criteria) this;
        }

        public Criteria andSpskunoIsNotNull() {
            addCriterion("SpSkuNo is not null");
            return (Criteria) this;
        }

        public Criteria andSpskunoEqualTo(String value) {
            addCriterion("SpSkuNo =", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoNotEqualTo(String value) {
            addCriterion("SpSkuNo <>", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoGreaterThan(String value) {
            addCriterion("SpSkuNo >", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoGreaterThanOrEqualTo(String value) {
            addCriterion("SpSkuNo >=", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoLessThan(String value) {
            addCriterion("SpSkuNo <", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoLessThanOrEqualTo(String value) {
            addCriterion("SpSkuNo <=", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoLike(String value) {
            addCriterion("SpSkuNo like", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoNotLike(String value) {
            addCriterion("SpSkuNo not like", value, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoIn(List<String> values) {
            addCriterion("SpSkuNo in", values, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoNotIn(List<String> values) {
            addCriterion("SpSkuNo not in", values, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoBetween(String value1, String value2) {
            addCriterion("SpSkuNo between", value1, value2, "spskuno");
            return (Criteria) this;
        }

        public Criteria andSpskunoNotBetween(String value1, String value2) {
            addCriterion("SpSkuNo not between", value1, value2, "spskuno");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeIsNull() {
            addCriterion("EventStartTime is null");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeIsNotNull() {
            addCriterion("EventStartTime is not null");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeEqualTo(Date value) {
            addCriterion("EventStartTime =", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeNotEqualTo(Date value) {
            addCriterion("EventStartTime <>", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeGreaterThan(Date value) {
            addCriterion("EventStartTime >", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeGreaterThanOrEqualTo(Date value) {
            addCriterion("EventStartTime >=", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeLessThan(Date value) {
            addCriterion("EventStartTime <", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeLessThanOrEqualTo(Date value) {
            addCriterion("EventStartTime <=", value, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeIn(List<Date> values) {
            addCriterion("EventStartTime in", values, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeNotIn(List<Date> values) {
            addCriterion("EventStartTime not in", values, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeBetween(Date value1, Date value2) {
            addCriterion("EventStartTime between", value1, value2, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventstarttimeNotBetween(Date value1, Date value2) {
            addCriterion("EventStartTime not between", value1, value2, "eventstarttime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeIsNull() {
            addCriterion("EventEndTime is null");
            return (Criteria) this;
        }

        public Criteria andEventendtimeIsNotNull() {
            addCriterion("EventEndTime is not null");
            return (Criteria) this;
        }

        public Criteria andEventendtimeEqualTo(Date value) {
            addCriterion("EventEndTime =", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeNotEqualTo(Date value) {
            addCriterion("EventEndTime <>", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeGreaterThan(Date value) {
            addCriterion("EventEndTime >", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("EventEndTime >=", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeLessThan(Date value) {
            addCriterion("EventEndTime <", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeLessThanOrEqualTo(Date value) {
            addCriterion("EventEndTime <=", value, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeIn(List<Date> values) {
            addCriterion("EventEndTime in", values, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeNotIn(List<Date> values) {
            addCriterion("EventEndTime not in", values, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeBetween(Date value1, Date value2) {
            addCriterion("EventEndTime between", value1, value2, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andEventendtimeNotBetween(Date value1, Date value2) {
            addCriterion("EventEndTime not between", value1, value2, "eventendtime");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNull() {
            addCriterion("Measurement is null");
            return (Criteria) this;
        }

        public Criteria andMeasurementIsNotNull() {
            addCriterion("Measurement is not null");
            return (Criteria) this;
        }

        public Criteria andMeasurementEqualTo(String value) {
            addCriterion("Measurement =", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotEqualTo(String value) {
            addCriterion("Measurement <>", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThan(String value) {
            addCriterion("Measurement >", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementGreaterThanOrEqualTo(String value) {
            addCriterion("Measurement >=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThan(String value) {
            addCriterion("Measurement <", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLessThanOrEqualTo(String value) {
            addCriterion("Measurement <=", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementLike(String value) {
            addCriterion("Measurement like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotLike(String value) {
            addCriterion("Measurement not like", value, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementIn(List<String> values) {
            addCriterion("Measurement in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotIn(List<String> values) {
            addCriterion("Measurement not in", values, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementBetween(String value1, String value2) {
            addCriterion("Measurement between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andMeasurementNotBetween(String value1, String value2) {
            addCriterion("Measurement not between", value1, value2, "measurement");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("Status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("Status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("Status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("Status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("Status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("Status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("Status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("Status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("Status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("Status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("Status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("Status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("Status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("Status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andSelstatusIsNull() {
            addCriterion("SelStatus is null");
            return (Criteria) this;
        }

        public Criteria andSelstatusIsNotNull() {
            addCriterion("SelStatus is not null");
            return (Criteria) this;
        }

        public Criteria andSelstatusEqualTo(Integer value) {
            addCriterion("SelStatus =", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusNotEqualTo(Integer value) {
            addCriterion("SelStatus <>", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusGreaterThan(Integer value) {
            addCriterion("SelStatus >", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("SelStatus >=", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusLessThan(Integer value) {
            addCriterion("SelStatus <", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusLessThanOrEqualTo(Integer value) {
            addCriterion("SelStatus <=", value, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusIn(List<Integer> values) {
            addCriterion("SelStatus in", values, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusNotIn(List<Integer> values) {
            addCriterion("SelStatus not in", values, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusBetween(Integer value1, Integer value2) {
            addCriterion("SelStatus between", value1, value2, "selstatus");
            return (Criteria) this;
        }

        public Criteria andSelstatusNotBetween(Integer value1, Integer value2) {
            addCriterion("SelStatus not between", value1, value2, "selstatus");
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