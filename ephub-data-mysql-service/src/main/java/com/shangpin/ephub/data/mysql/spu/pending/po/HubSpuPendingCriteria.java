package com.shangpin.ephub.data.mysql.spu.pending.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSpuPendingCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSpuPendingCriteria() {
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

        public Criteria andSpuModelIsNull() {
            addCriterion("spu_model is null");
            return (Criteria) this;
        }

        public Criteria andSpuModelIsNotNull() {
            addCriterion("spu_model is not null");
            return (Criteria) this;
        }

        public Criteria andSpuModelEqualTo(String value) {
            addCriterion("spu_model =", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotEqualTo(String value) {
            addCriterion("spu_model <>", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelGreaterThan(String value) {
            addCriterion("spu_model >", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("spu_model >=", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLessThan(String value) {
            addCriterion("spu_model <", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLessThanOrEqualTo(String value) {
            addCriterion("spu_model <=", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLike(String value) {
            addCriterion("spu_model like", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotLike(String value) {
            addCriterion("spu_model not like", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelIn(List<String> values) {
            addCriterion("spu_model in", values, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotIn(List<String> values) {
            addCriterion("spu_model not in", values, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelBetween(String value1, String value2) {
            addCriterion("spu_model between", value1, value2, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotBetween(String value1, String value2) {
            addCriterion("spu_model not between", value1, value2, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuNameIsNull() {
            addCriterion("spu_name is null");
            return (Criteria) this;
        }

        public Criteria andSpuNameIsNotNull() {
            addCriterion("spu_name is not null");
            return (Criteria) this;
        }

        public Criteria andSpuNameEqualTo(String value) {
            addCriterion("spu_name =", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotEqualTo(String value) {
            addCriterion("spu_name <>", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameGreaterThan(String value) {
            addCriterion("spu_name >", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameGreaterThanOrEqualTo(String value) {
            addCriterion("spu_name >=", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLessThan(String value) {
            addCriterion("spu_name <", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLessThanOrEqualTo(String value) {
            addCriterion("spu_name <=", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLike(String value) {
            addCriterion("spu_name like", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotLike(String value) {
            addCriterion("spu_name not like", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameIn(List<String> values) {
            addCriterion("spu_name in", values, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotIn(List<String> values) {
            addCriterion("spu_name not in", values, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameBetween(String value1, String value2) {
            addCriterion("spu_name between", value1, value2, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotBetween(String value1, String value2) {
            addCriterion("spu_name not between", value1, value2, "spuName");
            return (Criteria) this;
        }

        public Criteria andHubGenderIsNull() {
            addCriterion("hub_gender is null");
            return (Criteria) this;
        }

        public Criteria andHubGenderIsNotNull() {
            addCriterion("hub_gender is not null");
            return (Criteria) this;
        }

        public Criteria andHubGenderEqualTo(String value) {
            addCriterion("hub_gender =", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderNotEqualTo(String value) {
            addCriterion("hub_gender <>", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderGreaterThan(String value) {
            addCriterion("hub_gender >", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderGreaterThanOrEqualTo(String value) {
            addCriterion("hub_gender >=", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderLessThan(String value) {
            addCriterion("hub_gender <", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderLessThanOrEqualTo(String value) {
            addCriterion("hub_gender <=", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderLike(String value) {
            addCriterion("hub_gender like", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderNotLike(String value) {
            addCriterion("hub_gender not like", value, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderIn(List<String> values) {
            addCriterion("hub_gender in", values, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderNotIn(List<String> values) {
            addCriterion("hub_gender not in", values, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderBetween(String value1, String value2) {
            addCriterion("hub_gender between", value1, value2, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubGenderNotBetween(String value1, String value2) {
            addCriterion("hub_gender not between", value1, value2, "hubGender");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIsNull() {
            addCriterion("hub_category_no is null");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIsNotNull() {
            addCriterion("hub_category_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoEqualTo(String value) {
            addCriterion("hub_category_no =", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotEqualTo(String value) {
            addCriterion("hub_category_no <>", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoGreaterThan(String value) {
            addCriterion("hub_category_no >", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_category_no >=", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLessThan(String value) {
            addCriterion("hub_category_no <", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLessThanOrEqualTo(String value) {
            addCriterion("hub_category_no <=", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLike(String value) {
            addCriterion("hub_category_no like", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotLike(String value) {
            addCriterion("hub_category_no not like", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIn(List<String> values) {
            addCriterion("hub_category_no in", values, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotIn(List<String> values) {
            addCriterion("hub_category_no not in", values, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoBetween(String value1, String value2) {
            addCriterion("hub_category_no between", value1, value2, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotBetween(String value1, String value2) {
            addCriterion("hub_category_no not between", value1, value2, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIsNull() {
            addCriterion("hub_brand_no is null");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIsNotNull() {
            addCriterion("hub_brand_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoEqualTo(String value) {
            addCriterion("hub_brand_no =", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotEqualTo(String value) {
            addCriterion("hub_brand_no <>", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoGreaterThan(String value) {
            addCriterion("hub_brand_no >", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_brand_no >=", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLessThan(String value) {
            addCriterion("hub_brand_no <", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLessThanOrEqualTo(String value) {
            addCriterion("hub_brand_no <=", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLike(String value) {
            addCriterion("hub_brand_no like", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotLike(String value) {
            addCriterion("hub_brand_no not like", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIn(List<String> values) {
            addCriterion("hub_brand_no in", values, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotIn(List<String> values) {
            addCriterion("hub_brand_no not in", values, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoBetween(String value1, String value2) {
            addCriterion("hub_brand_no between", value1, value2, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotBetween(String value1, String value2) {
            addCriterion("hub_brand_no not between", value1, value2, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIsNull() {
            addCriterion("hub_season is null");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIsNotNull() {
            addCriterion("hub_season is not null");
            return (Criteria) this;
        }

        public Criteria andHubSeasonEqualTo(String value) {
            addCriterion("hub_season =", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotEqualTo(String value) {
            addCriterion("hub_season <>", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonGreaterThan(String value) {
            addCriterion("hub_season >", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonGreaterThanOrEqualTo(String value) {
            addCriterion("hub_season >=", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLessThan(String value) {
            addCriterion("hub_season <", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLessThanOrEqualTo(String value) {
            addCriterion("hub_season <=", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLike(String value) {
            addCriterion("hub_season like", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotLike(String value) {
            addCriterion("hub_season not like", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIn(List<String> values) {
            addCriterion("hub_season in", values, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotIn(List<String> values) {
            addCriterion("hub_season not in", values, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonBetween(String value1, String value2) {
            addCriterion("hub_season between", value1, value2, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotBetween(String value1, String value2) {
            addCriterion("hub_season not between", value1, value2, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andSpuStateIsNull() {
            addCriterion("spu_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuStateIsNotNull() {
            addCriterion("spu_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuStateEqualTo(Byte value) {
            addCriterion("spu_state =", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotEqualTo(Byte value) {
            addCriterion("spu_state <>", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateGreaterThan(Byte value) {
            addCriterion("spu_state >", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_state >=", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateLessThan(Byte value) {
            addCriterion("spu_state <", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_state <=", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateIn(List<Byte> values) {
            addCriterion("spu_state in", values, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotIn(List<Byte> values) {
            addCriterion("spu_state not in", values, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_state between", value1, value2, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_state not between", value1, value2, "spuState");
            return (Criteria) this;
        }

        public Criteria andPicStateIsNull() {
            addCriterion("pic_state is null");
            return (Criteria) this;
        }

        public Criteria andPicStateIsNotNull() {
            addCriterion("pic_state is not null");
            return (Criteria) this;
        }

        public Criteria andPicStateEqualTo(Byte value) {
            addCriterion("pic_state =", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateNotEqualTo(Byte value) {
            addCriterion("pic_state <>", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateGreaterThan(Byte value) {
            addCriterion("pic_state >", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("pic_state >=", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateLessThan(Byte value) {
            addCriterion("pic_state <", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateLessThanOrEqualTo(Byte value) {
            addCriterion("pic_state <=", value, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateIn(List<Byte> values) {
            addCriterion("pic_state in", values, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateNotIn(List<Byte> values) {
            addCriterion("pic_state not in", values, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateBetween(Byte value1, Byte value2) {
            addCriterion("pic_state between", value1, value2, "picState");
            return (Criteria) this;
        }

        public Criteria andPicStateNotBetween(Byte value1, Byte value2) {
            addCriterion("pic_state not between", value1, value2, "picState");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonIsNull() {
            addCriterion("is_current_season is null");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonIsNotNull() {
            addCriterion("is_current_season is not null");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonEqualTo(Byte value) {
            addCriterion("is_current_season =", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonNotEqualTo(Byte value) {
            addCriterion("is_current_season <>", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonGreaterThan(Byte value) {
            addCriterion("is_current_season >", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_current_season >=", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonLessThan(Byte value) {
            addCriterion("is_current_season <", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonLessThanOrEqualTo(Byte value) {
            addCriterion("is_current_season <=", value, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonIn(List<Byte> values) {
            addCriterion("is_current_season in", values, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonNotIn(List<Byte> values) {
            addCriterion("is_current_season not in", values, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonBetween(Byte value1, Byte value2) {
            addCriterion("is_current_season between", value1, value2, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsCurrentSeasonNotBetween(Byte value1, Byte value2) {
            addCriterion("is_current_season not between", value1, value2, "isCurrentSeason");
            return (Criteria) this;
        }

        public Criteria andIsNewDataIsNull() {
            addCriterion("is_new_data is null");
            return (Criteria) this;
        }

        public Criteria andIsNewDataIsNotNull() {
            addCriterion("is_new_data is not null");
            return (Criteria) this;
        }

        public Criteria andIsNewDataEqualTo(Byte value) {
            addCriterion("is_new_data =", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataNotEqualTo(Byte value) {
            addCriterion("is_new_data <>", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataGreaterThan(Byte value) {
            addCriterion("is_new_data >", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_new_data >=", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataLessThan(Byte value) {
            addCriterion("is_new_data <", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataLessThanOrEqualTo(Byte value) {
            addCriterion("is_new_data <=", value, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataIn(List<Byte> values) {
            addCriterion("is_new_data in", values, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataNotIn(List<Byte> values) {
            addCriterion("is_new_data not in", values, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataBetween(Byte value1, Byte value2) {
            addCriterion("is_new_data between", value1, value2, "isNewData");
            return (Criteria) this;
        }

        public Criteria andIsNewDataNotBetween(Byte value1, Byte value2) {
            addCriterion("is_new_data not between", value1, value2, "isNewData");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIsNull() {
            addCriterion("hub_material is null");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIsNotNull() {
            addCriterion("hub_material is not null");
            return (Criteria) this;
        }

        public Criteria andHubMaterialEqualTo(String value) {
            addCriterion("hub_material =", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotEqualTo(String value) {
            addCriterion("hub_material <>", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialGreaterThan(String value) {
            addCriterion("hub_material >", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialGreaterThanOrEqualTo(String value) {
            addCriterion("hub_material >=", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLessThan(String value) {
            addCriterion("hub_material <", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLessThanOrEqualTo(String value) {
            addCriterion("hub_material <=", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLike(String value) {
            addCriterion("hub_material like", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotLike(String value) {
            addCriterion("hub_material not like", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIn(List<String> values) {
            addCriterion("hub_material in", values, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotIn(List<String> values) {
            addCriterion("hub_material not in", values, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialBetween(String value1, String value2) {
            addCriterion("hub_material between", value1, value2, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotBetween(String value1, String value2) {
            addCriterion("hub_material not between", value1, value2, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubOriginIsNull() {
            addCriterion("hub_origin is null");
            return (Criteria) this;
        }

        public Criteria andHubOriginIsNotNull() {
            addCriterion("hub_origin is not null");
            return (Criteria) this;
        }

        public Criteria andHubOriginEqualTo(String value) {
            addCriterion("hub_origin =", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginNotEqualTo(String value) {
            addCriterion("hub_origin <>", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginGreaterThan(String value) {
            addCriterion("hub_origin >", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginGreaterThanOrEqualTo(String value) {
            addCriterion("hub_origin >=", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginLessThan(String value) {
            addCriterion("hub_origin <", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginLessThanOrEqualTo(String value) {
            addCriterion("hub_origin <=", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginLike(String value) {
            addCriterion("hub_origin like", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginNotLike(String value) {
            addCriterion("hub_origin not like", value, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginIn(List<String> values) {
            addCriterion("hub_origin in", values, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginNotIn(List<String> values) {
            addCriterion("hub_origin not in", values, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginBetween(String value1, String value2) {
            addCriterion("hub_origin between", value1, value2, "hubOrigin");
            return (Criteria) this;
        }

        public Criteria andHubOriginNotBetween(String value1, String value2) {
            addCriterion("hub_origin not between", value1, value2, "hubOrigin");
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

        public Criteria andSpuDescIsNull() {
            addCriterion("spu_desc is null");
            return (Criteria) this;
        }

        public Criteria andSpuDescIsNotNull() {
            addCriterion("spu_desc is not null");
            return (Criteria) this;
        }

        public Criteria andSpuDescEqualTo(String value) {
            addCriterion("spu_desc =", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotEqualTo(String value) {
            addCriterion("spu_desc <>", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescGreaterThan(String value) {
            addCriterion("spu_desc >", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescGreaterThanOrEqualTo(String value) {
            addCriterion("spu_desc >=", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLessThan(String value) {
            addCriterion("spu_desc <", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLessThanOrEqualTo(String value) {
            addCriterion("spu_desc <=", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLike(String value) {
            addCriterion("spu_desc like", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotLike(String value) {
            addCriterion("spu_desc not like", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescIn(List<String> values) {
            addCriterion("spu_desc in", values, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotIn(List<String> values) {
            addCriterion("spu_desc not in", values, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescBetween(String value1, String value2) {
            addCriterion("spu_desc between", value1, value2, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotBetween(String value1, String value2) {
            addCriterion("spu_desc not between", value1, value2, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoIsNull() {
            addCriterion("hub_spu_no is null");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoIsNotNull() {
            addCriterion("hub_spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoEqualTo(String value) {
            addCriterion("hub_spu_no =", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoNotEqualTo(String value) {
            addCriterion("hub_spu_no <>", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoGreaterThan(String value) {
            addCriterion("hub_spu_no >", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_spu_no >=", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoLessThan(String value) {
            addCriterion("hub_spu_no <", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoLessThanOrEqualTo(String value) {
            addCriterion("hub_spu_no <=", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoLike(String value) {
            addCriterion("hub_spu_no like", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoNotLike(String value) {
            addCriterion("hub_spu_no not like", value, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoIn(List<String> values) {
            addCriterion("hub_spu_no in", values, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoNotIn(List<String> values) {
            addCriterion("hub_spu_no not in", values, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoBetween(String value1, String value2) {
            addCriterion("hub_spu_no between", value1, value2, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andHubSpuNoNotBetween(String value1, String value2) {
            addCriterion("hub_spu_no not between", value1, value2, "hubSpuNo");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateIsNull() {
            addCriterion("spu_model_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateIsNotNull() {
            addCriterion("spu_model_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateEqualTo(Byte value) {
            addCriterion("spu_model_state =", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateNotEqualTo(Byte value) {
            addCriterion("spu_model_state <>", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateGreaterThan(Byte value) {
            addCriterion("spu_model_state >", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_model_state >=", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateLessThan(Byte value) {
            addCriterion("spu_model_state <", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_model_state <=", value, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateIn(List<Byte> values) {
            addCriterion("spu_model_state in", values, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateNotIn(List<Byte> values) {
            addCriterion("spu_model_state not in", values, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_model_state between", value1, value2, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andSpuModelStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_model_state not between", value1, value2, "spuModelState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateIsNull() {
            addCriterion("catgory_state is null");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateIsNotNull() {
            addCriterion("catgory_state is not null");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateEqualTo(Byte value) {
            addCriterion("catgory_state =", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateNotEqualTo(Byte value) {
            addCriterion("catgory_state <>", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateGreaterThan(Byte value) {
            addCriterion("catgory_state >", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("catgory_state >=", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateLessThan(Byte value) {
            addCriterion("catgory_state <", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateLessThanOrEqualTo(Byte value) {
            addCriterion("catgory_state <=", value, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateIn(List<Byte> values) {
            addCriterion("catgory_state in", values, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateNotIn(List<Byte> values) {
            addCriterion("catgory_state not in", values, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateBetween(Byte value1, Byte value2) {
            addCriterion("catgory_state between", value1, value2, "catgoryState");
            return (Criteria) this;
        }

        public Criteria andCatgoryStateNotBetween(Byte value1, Byte value2) {
            addCriterion("catgory_state not between", value1, value2, "catgoryState");
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

        public Criteria andSpuBrandStateIsNull() {
            addCriterion("spu_brand_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateIsNotNull() {
            addCriterion("spu_brand_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateEqualTo(Byte value) {
            addCriterion("spu_brand_state =", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateNotEqualTo(Byte value) {
            addCriterion("spu_brand_state <>", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateGreaterThan(Byte value) {
            addCriterion("spu_brand_state >", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_brand_state >=", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateLessThan(Byte value) {
            addCriterion("spu_brand_state <", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_brand_state <=", value, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateIn(List<Byte> values) {
            addCriterion("spu_brand_state in", values, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateNotIn(List<Byte> values) {
            addCriterion("spu_brand_state not in", values, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_brand_state between", value1, value2, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuBrandStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_brand_state not between", value1, value2, "spuBrandState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateIsNull() {
            addCriterion("spu_gender_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateIsNotNull() {
            addCriterion("spu_gender_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateEqualTo(Byte value) {
            addCriterion("spu_gender_state =", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateNotEqualTo(Byte value) {
            addCriterion("spu_gender_state <>", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateGreaterThan(Byte value) {
            addCriterion("spu_gender_state >", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_gender_state >=", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateLessThan(Byte value) {
            addCriterion("spu_gender_state <", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_gender_state <=", value, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateIn(List<Byte> values) {
            addCriterion("spu_gender_state in", values, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateNotIn(List<Byte> values) {
            addCriterion("spu_gender_state not in", values, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_gender_state between", value1, value2, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuGenderStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_gender_state not between", value1, value2, "spuGenderState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateIsNull() {
            addCriterion("spu_season_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateIsNotNull() {
            addCriterion("spu_season_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateEqualTo(Byte value) {
            addCriterion("spu_season_state =", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateNotEqualTo(Byte value) {
            addCriterion("spu_season_state <>", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateGreaterThan(Byte value) {
            addCriterion("spu_season_state >", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_season_state >=", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateLessThan(Byte value) {
            addCriterion("spu_season_state <", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_season_state <=", value, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateIn(List<Byte> values) {
            addCriterion("spu_season_state in", values, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateNotIn(List<Byte> values) {
            addCriterion("spu_season_state not in", values, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_season_state between", value1, value2, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andSpuSeasonStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_season_state not between", value1, value2, "spuSeasonState");
            return (Criteria) this;
        }

        public Criteria andHubColorIsNull() {
            addCriterion("hub_color is null");
            return (Criteria) this;
        }

        public Criteria andHubColorIsNotNull() {
            addCriterion("hub_color is not null");
            return (Criteria) this;
        }

        public Criteria andHubColorEqualTo(String value) {
            addCriterion("hub_color =", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotEqualTo(String value) {
            addCriterion("hub_color <>", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorGreaterThan(String value) {
            addCriterion("hub_color >", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorGreaterThanOrEqualTo(String value) {
            addCriterion("hub_color >=", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLessThan(String value) {
            addCriterion("hub_color <", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLessThanOrEqualTo(String value) {
            addCriterion("hub_color <=", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLike(String value) {
            addCriterion("hub_color like", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotLike(String value) {
            addCriterion("hub_color not like", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorIn(List<String> values) {
            addCriterion("hub_color in", values, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotIn(List<String> values) {
            addCriterion("hub_color not in", values, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorBetween(String value1, String value2) {
            addCriterion("hub_color between", value1, value2, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotBetween(String value1, String value2) {
            addCriterion("hub_color not between", value1, value2, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIsNull() {
            addCriterion("hub_color_no is null");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIsNotNull() {
            addCriterion("hub_color_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubColorNoEqualTo(String value) {
            addCriterion("hub_color_no =", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotEqualTo(String value) {
            addCriterion("hub_color_no <>", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoGreaterThan(String value) {
            addCriterion("hub_color_no >", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_color_no >=", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLessThan(String value) {
            addCriterion("hub_color_no <", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLessThanOrEqualTo(String value) {
            addCriterion("hub_color_no <=", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLike(String value) {
            addCriterion("hub_color_no like", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotLike(String value) {
            addCriterion("hub_color_no not like", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIn(List<String> values) {
            addCriterion("hub_color_no in", values, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotIn(List<String> values) {
            addCriterion("hub_color_no not in", values, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoBetween(String value1, String value2) {
            addCriterion("hub_color_no between", value1, value2, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotBetween(String value1, String value2) {
            addCriterion("hub_color_no not between", value1, value2, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateIsNull() {
            addCriterion("spu_color_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateIsNotNull() {
            addCriterion("spu_color_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateEqualTo(Byte value) {
            addCriterion("spu_color_state =", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateNotEqualTo(Byte value) {
            addCriterion("spu_color_state <>", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateGreaterThan(Byte value) {
            addCriterion("spu_color_state >", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_color_state >=", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateLessThan(Byte value) {
            addCriterion("spu_color_state <", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_color_state <=", value, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateIn(List<Byte> values) {
            addCriterion("spu_color_state in", values, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateNotIn(List<Byte> values) {
            addCriterion("spu_color_state not in", values, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_color_state between", value1, value2, "spuColorState");
            return (Criteria) this;
        }

        public Criteria andSpuColorStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_color_state not between", value1, value2, "spuColorState");
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

        public Criteria andMaterialStateIsNull() {
            addCriterion("material_state is null");
            return (Criteria) this;
        }

        public Criteria andMaterialStateIsNotNull() {
            addCriterion("material_state is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialStateEqualTo(Byte value) {
            addCriterion("material_state =", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateNotEqualTo(Byte value) {
            addCriterion("material_state <>", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateGreaterThan(Byte value) {
            addCriterion("material_state >", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("material_state >=", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateLessThan(Byte value) {
            addCriterion("material_state <", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateLessThanOrEqualTo(Byte value) {
            addCriterion("material_state <=", value, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateIn(List<Byte> values) {
            addCriterion("material_state in", values, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateNotIn(List<Byte> values) {
            addCriterion("material_state not in", values, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateBetween(Byte value1, Byte value2) {
            addCriterion("material_state between", value1, value2, "materialState");
            return (Criteria) this;
        }

        public Criteria andMaterialStateNotBetween(Byte value1, Byte value2) {
            addCriterion("material_state not between", value1, value2, "materialState");
            return (Criteria) this;
        }

        public Criteria andOriginStateIsNull() {
            addCriterion("origin_state is null");
            return (Criteria) this;
        }

        public Criteria andOriginStateIsNotNull() {
            addCriterion("origin_state is not null");
            return (Criteria) this;
        }

        public Criteria andOriginStateEqualTo(Byte value) {
            addCriterion("origin_state =", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateNotEqualTo(Byte value) {
            addCriterion("origin_state <>", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateGreaterThan(Byte value) {
            addCriterion("origin_state >", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("origin_state >=", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateLessThan(Byte value) {
            addCriterion("origin_state <", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateLessThanOrEqualTo(Byte value) {
            addCriterion("origin_state <=", value, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateIn(List<Byte> values) {
            addCriterion("origin_state in", values, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateNotIn(List<Byte> values) {
            addCriterion("origin_state not in", values, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateBetween(Byte value1, Byte value2) {
            addCriterion("origin_state between", value1, value2, "originState");
            return (Criteria) this;
        }

        public Criteria andOriginStateNotBetween(Byte value1, Byte value2) {
            addCriterion("origin_state not between", value1, value2, "originState");
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

        public Criteria andInfoStateIsNull() {
            addCriterion("info_state is null");
            return (Criteria) this;
        }

        public Criteria andInfoStateIsNotNull() {
            addCriterion("info_state is not null");
            return (Criteria) this;
        }

        public Criteria andInfoStateEqualTo(Byte value) {
            addCriterion("info_state =", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateNotEqualTo(Byte value) {
            addCriterion("info_state <>", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateGreaterThan(Byte value) {
            addCriterion("info_state >", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("info_state >=", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateLessThan(Byte value) {
            addCriterion("info_state <", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateLessThanOrEqualTo(Byte value) {
            addCriterion("info_state <=", value, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateIn(List<Byte> values) {
            addCriterion("info_state in", values, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateNotIn(List<Byte> values) {
            addCriterion("info_state not in", values, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateBetween(Byte value1, Byte value2) {
            addCriterion("info_state between", value1, value2, "infoState");
            return (Criteria) this;
        }

        public Criteria andInfoStateNotBetween(Byte value1, Byte value2) {
            addCriterion("info_state not between", value1, value2, "infoState");
            return (Criteria) this;
        }

        public Criteria andStockStateIsNull() {
            addCriterion("stock_state is null");
            return (Criteria) this;
        }

        public Criteria andStockStateIsNotNull() {
            addCriterion("stock_state is not null");
            return (Criteria) this;
        }

        public Criteria andStockStateEqualTo(Byte value) {
            addCriterion("stock_state =", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateNotEqualTo(Byte value) {
            addCriterion("stock_state <>", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateGreaterThan(Byte value) {
            addCriterion("stock_state >", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("stock_state >=", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateLessThan(Byte value) {
            addCriterion("stock_state <", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateLessThanOrEqualTo(Byte value) {
            addCriterion("stock_state <=", value, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateIn(List<Byte> values) {
            addCriterion("stock_state in", values, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateNotIn(List<Byte> values) {
            addCriterion("stock_state not in", values, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateBetween(Byte value1, Byte value2) {
            addCriterion("stock_state between", value1, value2, "stockState");
            return (Criteria) this;
        }

        public Criteria andStockStateNotBetween(Byte value1, Byte value2) {
            addCriterion("stock_state not between", value1, value2, "stockState");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNull() {
            addCriterion("update_user is null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIsNotNull() {
            addCriterion("update_user is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateUserEqualTo(String value) {
            addCriterion("update_user =", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotEqualTo(String value) {
            addCriterion("update_user <>", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThan(String value) {
            addCriterion("update_user >", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserGreaterThanOrEqualTo(String value) {
            addCriterion("update_user >=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThan(String value) {
            addCriterion("update_user <", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLessThanOrEqualTo(String value) {
            addCriterion("update_user <=", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserLike(String value) {
            addCriterion("update_user like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotLike(String value) {
            addCriterion("update_user not like", value, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserIn(List<String> values) {
            addCriterion("update_user in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotIn(List<String> values) {
            addCriterion("update_user not in", values, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserBetween(String value1, String value2) {
            addCriterion("update_user between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andUpdateUserNotBetween(String value1, String value2) {
            addCriterion("update_user not between", value1, value2, "updateUser");
            return (Criteria) this;
        }

        public Criteria andAuditStateIsNull() {
            addCriterion("audit_state is null");
            return (Criteria) this;
        }

        public Criteria andAuditStateIsNotNull() {
            addCriterion("audit_state is not null");
            return (Criteria) this;
        }

        public Criteria andAuditStateEqualTo(Byte value) {
            addCriterion("audit_state =", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateNotEqualTo(Byte value) {
            addCriterion("audit_state <>", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateGreaterThan(Byte value) {
            addCriterion("audit_state >", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("audit_state >=", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateLessThan(Byte value) {
            addCriterion("audit_state <", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateLessThanOrEqualTo(Byte value) {
            addCriterion("audit_state <=", value, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateIn(List<Byte> values) {
            addCriterion("audit_state in", values, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateNotIn(List<Byte> values) {
            addCriterion("audit_state not in", values, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateBetween(Byte value1, Byte value2) {
            addCriterion("audit_state between", value1, value2, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditStateNotBetween(Byte value1, Byte value2) {
            addCriterion("audit_state not between", value1, value2, "auditState");
            return (Criteria) this;
        }

        public Criteria andAuditDateIsNull() {
            addCriterion("audit_date is null");
            return (Criteria) this;
        }

        public Criteria andAuditDateIsNotNull() {
            addCriterion("audit_date is not null");
            return (Criteria) this;
        }

        public Criteria andAuditDateEqualTo(Date value) {
            addCriterion("audit_date =", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateNotEqualTo(Date value) {
            addCriterion("audit_date <>", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateGreaterThan(Date value) {
            addCriterion("audit_date >", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateGreaterThanOrEqualTo(Date value) {
            addCriterion("audit_date >=", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateLessThan(Date value) {
            addCriterion("audit_date <", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateLessThanOrEqualTo(Date value) {
            addCriterion("audit_date <=", value, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateIn(List<Date> values) {
            addCriterion("audit_date in", values, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateNotIn(List<Date> values) {
            addCriterion("audit_date not in", values, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateBetween(Date value1, Date value2) {
            addCriterion("audit_date between", value1, value2, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditDateNotBetween(Date value1, Date value2) {
            addCriterion("audit_date not between", value1, value2, "auditDate");
            return (Criteria) this;
        }

        public Criteria andAuditUserIsNull() {
            addCriterion("audit_user is null");
            return (Criteria) this;
        }

        public Criteria andAuditUserIsNotNull() {
            addCriterion("audit_user is not null");
            return (Criteria) this;
        }

        public Criteria andAuditUserEqualTo(String value) {
            addCriterion("audit_user =", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotEqualTo(String value) {
            addCriterion("audit_user <>", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserGreaterThan(String value) {
            addCriterion("audit_user >", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserGreaterThanOrEqualTo(String value) {
            addCriterion("audit_user >=", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLessThan(String value) {
            addCriterion("audit_user <", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLessThanOrEqualTo(String value) {
            addCriterion("audit_user <=", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserLike(String value) {
            addCriterion("audit_user like", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotLike(String value) {
            addCriterion("audit_user not like", value, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserIn(List<String> values) {
            addCriterion("audit_user in", values, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotIn(List<String> values) {
            addCriterion("audit_user not in", values, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserBetween(String value1, String value2) {
            addCriterion("audit_user between", value1, value2, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditUserNotBetween(String value1, String value2) {
            addCriterion("audit_user not between", value1, value2, "auditUser");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionIsNull() {
            addCriterion("audit_opinion is null");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionIsNotNull() {
            addCriterion("audit_opinion is not null");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionEqualTo(String value) {
            addCriterion("audit_opinion =", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionNotEqualTo(String value) {
            addCriterion("audit_opinion <>", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionGreaterThan(String value) {
            addCriterion("audit_opinion >", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionGreaterThanOrEqualTo(String value) {
            addCriterion("audit_opinion >=", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionLessThan(String value) {
            addCriterion("audit_opinion <", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionLessThanOrEqualTo(String value) {
            addCriterion("audit_opinion <=", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionLike(String value) {
            addCriterion("audit_opinion like", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionNotLike(String value) {
            addCriterion("audit_opinion not like", value, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionIn(List<String> values) {
            addCriterion("audit_opinion in", values, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionNotIn(List<String> values) {
            addCriterion("audit_opinion not in", values, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionBetween(String value1, String value2) {
            addCriterion("audit_opinion between", value1, value2, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andAuditOpinionNotBetween(String value1, String value2) {
            addCriterion("audit_opinion not between", value1, value2, "auditOpinion");
            return (Criteria) this;
        }

        public Criteria andHandleFromIsNull() {
            addCriterion("handle_from is null");
            return (Criteria) this;
        }

        public Criteria andHandleFromIsNotNull() {
            addCriterion("handle_from is not null");
            return (Criteria) this;
        }

        public Criteria andHandleFromEqualTo(Byte value) {
            addCriterion("handle_from =", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromNotEqualTo(Byte value) {
            addCriterion("handle_from <>", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromGreaterThan(Byte value) {
            addCriterion("handle_from >", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromGreaterThanOrEqualTo(Byte value) {
            addCriterion("handle_from >=", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromLessThan(Byte value) {
            addCriterion("handle_from <", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromLessThanOrEqualTo(Byte value) {
            addCriterion("handle_from <=", value, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromIn(List<Byte> values) {
            addCriterion("handle_from in", values, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromNotIn(List<Byte> values) {
            addCriterion("handle_from not in", values, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromBetween(Byte value1, Byte value2) {
            addCriterion("handle_from between", value1, value2, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleFromNotBetween(Byte value1, Byte value2) {
            addCriterion("handle_from not between", value1, value2, "handleFrom");
            return (Criteria) this;
        }

        public Criteria andHandleStateIsNull() {
            addCriterion("handle_state is null");
            return (Criteria) this;
        }

        public Criteria andHandleStateIsNotNull() {
            addCriterion("handle_state is not null");
            return (Criteria) this;
        }

        public Criteria andHandleStateEqualTo(Byte value) {
            addCriterion("handle_state =", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateNotEqualTo(Byte value) {
            addCriterion("handle_state <>", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateGreaterThan(Byte value) {
            addCriterion("handle_state >", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("handle_state >=", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateLessThan(Byte value) {
            addCriterion("handle_state <", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateLessThanOrEqualTo(Byte value) {
            addCriterion("handle_state <=", value, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateIn(List<Byte> values) {
            addCriterion("handle_state in", values, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateNotIn(List<Byte> values) {
            addCriterion("handle_state not in", values, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateBetween(Byte value1, Byte value2) {
            addCriterion("handle_state between", value1, value2, "handleState");
            return (Criteria) this;
        }

        public Criteria andHandleStateNotBetween(Byte value1, Byte value2) {
            addCriterion("handle_state not between", value1, value2, "handleState");
            return (Criteria) this;
        }

        public Criteria andSlotStateIsNull() {
            addCriterion("slot_state is null");
            return (Criteria) this;
        }

        public Criteria andSlotStateIsNotNull() {
            addCriterion("slot_state is not null");
            return (Criteria) this;
        }

        public Criteria andSlotStateEqualTo(Byte value) {
            addCriterion("slot_state =", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateNotEqualTo(Byte value) {
            addCriterion("slot_state <>", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateGreaterThan(Byte value) {
            addCriterion("slot_state >", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("slot_state >=", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateLessThan(Byte value) {
            addCriterion("slot_state <", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateLessThanOrEqualTo(Byte value) {
            addCriterion("slot_state <=", value, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateIn(List<Byte> values) {
            addCriterion("slot_state in", values, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateNotIn(List<Byte> values) {
            addCriterion("slot_state not in", values, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateBetween(Byte value1, Byte value2) {
            addCriterion("slot_state between", value1, value2, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotStateNotBetween(Byte value1, Byte value2) {
            addCriterion("slot_state not between", value1, value2, "slotState");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateIsNull() {
            addCriterion("slot_handle_date is null");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateIsNotNull() {
            addCriterion("slot_handle_date is not null");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateEqualTo(Date value) {
            addCriterion("slot_handle_date =", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateNotEqualTo(Date value) {
            addCriterion("slot_handle_date <>", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateGreaterThan(Date value) {
            addCriterion("slot_handle_date >", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateGreaterThanOrEqualTo(Date value) {
            addCriterion("slot_handle_date >=", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateLessThan(Date value) {
            addCriterion("slot_handle_date <", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateLessThanOrEqualTo(Date value) {
            addCriterion("slot_handle_date <=", value, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateIn(List<Date> values) {
            addCriterion("slot_handle_date in", values, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateNotIn(List<Date> values) {
            addCriterion("slot_handle_date not in", values, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateBetween(Date value1, Date value2) {
            addCriterion("slot_handle_date between", value1, value2, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleDateNotBetween(Date value1, Date value2) {
            addCriterion("slot_handle_date not between", value1, value2, "slotHandleDate");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserIsNull() {
            addCriterion("slot_handle_user is null");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserIsNotNull() {
            addCriterion("slot_handle_user is not null");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserEqualTo(String value) {
            addCriterion("slot_handle_user =", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserNotEqualTo(String value) {
            addCriterion("slot_handle_user <>", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserGreaterThan(String value) {
            addCriterion("slot_handle_user >", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserGreaterThanOrEqualTo(String value) {
            addCriterion("slot_handle_user >=", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserLessThan(String value) {
            addCriterion("slot_handle_user <", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserLessThanOrEqualTo(String value) {
            addCriterion("slot_handle_user <=", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserLike(String value) {
            addCriterion("slot_handle_user like", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserNotLike(String value) {
            addCriterion("slot_handle_user not like", value, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserIn(List<String> values) {
            addCriterion("slot_handle_user in", values, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserNotIn(List<String> values) {
            addCriterion("slot_handle_user not in", values, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserBetween(String value1, String value2) {
            addCriterion("slot_handle_user between", value1, value2, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andSlotHandleUserNotBetween(String value1, String value2) {
            addCriterion("slot_handle_user not between", value1, value2, "slotHandleUser");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateIsNull() {
            addCriterion("msg_miss_handle_state is null");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateIsNotNull() {
            addCriterion("msg_miss_handle_state is not null");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateEqualTo(Byte value) {
            addCriterion("msg_miss_handle_state =", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateNotEqualTo(Byte value) {
            addCriterion("msg_miss_handle_state <>", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateGreaterThan(Byte value) {
            addCriterion("msg_miss_handle_state >", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("msg_miss_handle_state >=", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateLessThan(Byte value) {
            addCriterion("msg_miss_handle_state <", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateLessThanOrEqualTo(Byte value) {
            addCriterion("msg_miss_handle_state <=", value, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateIn(List<Byte> values) {
            addCriterion("msg_miss_handle_state in", values, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateNotIn(List<Byte> values) {
            addCriterion("msg_miss_handle_state not in", values, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateBetween(Byte value1, Byte value2) {
            addCriterion("msg_miss_handle_state between", value1, value2, "msgMissHandleState");
            return (Criteria) this;
        }

        public Criteria andMsgMissHandleStateNotBetween(Byte value1, Byte value2) {
            addCriterion("msg_miss_handle_state not between", value1, value2, "msgMissHandleState");
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