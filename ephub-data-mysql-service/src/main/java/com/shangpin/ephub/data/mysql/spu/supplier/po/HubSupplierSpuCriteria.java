package com.shangpin.ephub.data.mysql.spu.supplier.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSupplierSpuCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSupplierSpuCriteria() {
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

        public Criteria andSupplierSpuModelIsNull() {
            addCriterion("supplier_spu_model is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelIsNotNull() {
            addCriterion("supplier_spu_model is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelEqualTo(String value) {
            addCriterion("supplier_spu_model =", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelNotEqualTo(String value) {
            addCriterion("supplier_spu_model <>", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelGreaterThan(String value) {
            addCriterion("supplier_spu_model >", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_model >=", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelLessThan(String value) {
            addCriterion("supplier_spu_model <", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_model <=", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelLike(String value) {
            addCriterion("supplier_spu_model like", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelNotLike(String value) {
            addCriterion("supplier_spu_model not like", value, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelIn(List<String> values) {
            addCriterion("supplier_spu_model in", values, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelNotIn(List<String> values) {
            addCriterion("supplier_spu_model not in", values, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelBetween(String value1, String value2) {
            addCriterion("supplier_spu_model between", value1, value2, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuModelNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_model not between", value1, value2, "supplierSpuModel");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameIsNull() {
            addCriterion("supplier_spu_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameIsNotNull() {
            addCriterion("supplier_spu_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameEqualTo(String value) {
            addCriterion("supplier_spu_name =", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameNotEqualTo(String value) {
            addCriterion("supplier_spu_name <>", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameGreaterThan(String value) {
            addCriterion("supplier_spu_name >", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_name >=", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameLessThan(String value) {
            addCriterion("supplier_spu_name <", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_name <=", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameLike(String value) {
            addCriterion("supplier_spu_name like", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameNotLike(String value) {
            addCriterion("supplier_spu_name not like", value, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameIn(List<String> values) {
            addCriterion("supplier_spu_name in", values, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameNotIn(List<String> values) {
            addCriterion("supplier_spu_name not in", values, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameBetween(String value1, String value2) {
            addCriterion("supplier_spu_name between", value1, value2, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNameNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_name not between", value1, value2, "supplierSpuName");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorIsNull() {
            addCriterion("supplier_spu_color is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorIsNotNull() {
            addCriterion("supplier_spu_color is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorEqualTo(String value) {
            addCriterion("supplier_spu_color =", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorNotEqualTo(String value) {
            addCriterion("supplier_spu_color <>", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorGreaterThan(String value) {
            addCriterion("supplier_spu_color >", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_color >=", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorLessThan(String value) {
            addCriterion("supplier_spu_color <", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_color <=", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorLike(String value) {
            addCriterion("supplier_spu_color like", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorNotLike(String value) {
            addCriterion("supplier_spu_color not like", value, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorIn(List<String> values) {
            addCriterion("supplier_spu_color in", values, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorNotIn(List<String> values) {
            addCriterion("supplier_spu_color not in", values, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorBetween(String value1, String value2) {
            addCriterion("supplier_spu_color between", value1, value2, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuColorNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_color not between", value1, value2, "supplierSpuColor");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderIsNull() {
            addCriterion("supplier_gender is null");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderIsNotNull() {
            addCriterion("supplier_gender is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderEqualTo(String value) {
            addCriterion("supplier_gender =", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderNotEqualTo(String value) {
            addCriterion("supplier_gender <>", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderGreaterThan(String value) {
            addCriterion("supplier_gender >", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_gender >=", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderLessThan(String value) {
            addCriterion("supplier_gender <", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderLessThanOrEqualTo(String value) {
            addCriterion("supplier_gender <=", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderLike(String value) {
            addCriterion("supplier_gender like", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderNotLike(String value) {
            addCriterion("supplier_gender not like", value, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderIn(List<String> values) {
            addCriterion("supplier_gender in", values, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderNotIn(List<String> values) {
            addCriterion("supplier_gender not in", values, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderBetween(String value1, String value2) {
            addCriterion("supplier_gender between", value1, value2, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierGenderNotBetween(String value1, String value2) {
            addCriterion("supplier_gender not between", value1, value2, "supplierGender");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoIsNull() {
            addCriterion("supplier_categoryno is null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoIsNotNull() {
            addCriterion("supplier_categoryno is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoEqualTo(String value) {
            addCriterion("supplier_categoryno =", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoNotEqualTo(String value) {
            addCriterion("supplier_categoryno <>", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoGreaterThan(String value) {
            addCriterion("supplier_categoryno >", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_categoryno >=", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoLessThan(String value) {
            addCriterion("supplier_categoryno <", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoLessThanOrEqualTo(String value) {
            addCriterion("supplier_categoryno <=", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoLike(String value) {
            addCriterion("supplier_categoryno like", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoNotLike(String value) {
            addCriterion("supplier_categoryno not like", value, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoIn(List<String> values) {
            addCriterion("supplier_categoryno in", values, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoNotIn(List<String> values) {
            addCriterion("supplier_categoryno not in", values, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoBetween(String value1, String value2) {
            addCriterion("supplier_categoryno between", value1, value2, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynoNotBetween(String value1, String value2) {
            addCriterion("supplier_categoryno not between", value1, value2, "supplierCategoryno");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameIsNull() {
            addCriterion("supplier_categoryname is null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameIsNotNull() {
            addCriterion("supplier_categoryname is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameEqualTo(String value) {
            addCriterion("supplier_categoryname =", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameNotEqualTo(String value) {
            addCriterion("supplier_categoryname <>", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameGreaterThan(String value) {
            addCriterion("supplier_categoryname >", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_categoryname >=", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameLessThan(String value) {
            addCriterion("supplier_categoryname <", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameLessThanOrEqualTo(String value) {
            addCriterion("supplier_categoryname <=", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameLike(String value) {
            addCriterion("supplier_categoryname like", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameNotLike(String value) {
            addCriterion("supplier_categoryname not like", value, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameIn(List<String> values) {
            addCriterion("supplier_categoryname in", values, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameNotIn(List<String> values) {
            addCriterion("supplier_categoryname not in", values, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameBetween(String value1, String value2) {
            addCriterion("supplier_categoryname between", value1, value2, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierCategorynameNotBetween(String value1, String value2) {
            addCriterion("supplier_categoryname not between", value1, value2, "supplierCategoryname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoIsNull() {
            addCriterion("supplier_brandno is null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoIsNotNull() {
            addCriterion("supplier_brandno is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoEqualTo(String value) {
            addCriterion("supplier_brandno =", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoNotEqualTo(String value) {
            addCriterion("supplier_brandno <>", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoGreaterThan(String value) {
            addCriterion("supplier_brandno >", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_brandno >=", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoLessThan(String value) {
            addCriterion("supplier_brandno <", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoLessThanOrEqualTo(String value) {
            addCriterion("supplier_brandno <=", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoLike(String value) {
            addCriterion("supplier_brandno like", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoNotLike(String value) {
            addCriterion("supplier_brandno not like", value, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoIn(List<String> values) {
            addCriterion("supplier_brandno in", values, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoNotIn(List<String> values) {
            addCriterion("supplier_brandno not in", values, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoBetween(String value1, String value2) {
            addCriterion("supplier_brandno between", value1, value2, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnoNotBetween(String value1, String value2) {
            addCriterion("supplier_brandno not between", value1, value2, "supplierBrandno");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameIsNull() {
            addCriterion("supplier_brandname is null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameIsNotNull() {
            addCriterion("supplier_brandname is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameEqualTo(String value) {
            addCriterion("supplier_brandname =", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameNotEqualTo(String value) {
            addCriterion("supplier_brandname <>", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameGreaterThan(String value) {
            addCriterion("supplier_brandname >", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_brandname >=", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameLessThan(String value) {
            addCriterion("supplier_brandname <", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameLessThanOrEqualTo(String value) {
            addCriterion("supplier_brandname <=", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameLike(String value) {
            addCriterion("supplier_brandname like", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameNotLike(String value) {
            addCriterion("supplier_brandname not like", value, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameIn(List<String> values) {
            addCriterion("supplier_brandname in", values, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameNotIn(List<String> values) {
            addCriterion("supplier_brandname not in", values, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameBetween(String value1, String value2) {
            addCriterion("supplier_brandname between", value1, value2, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandnameNotBetween(String value1, String value2) {
            addCriterion("supplier_brandname not between", value1, value2, "supplierBrandname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoIsNull() {
            addCriterion("supplier_seasonno is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoIsNotNull() {
            addCriterion("supplier_seasonno is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoEqualTo(String value) {
            addCriterion("supplier_seasonno =", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoNotEqualTo(String value) {
            addCriterion("supplier_seasonno <>", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoGreaterThan(String value) {
            addCriterion("supplier_seasonno >", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_seasonno >=", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoLessThan(String value) {
            addCriterion("supplier_seasonno <", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoLessThanOrEqualTo(String value) {
            addCriterion("supplier_seasonno <=", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoLike(String value) {
            addCriterion("supplier_seasonno like", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoNotLike(String value) {
            addCriterion("supplier_seasonno not like", value, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoIn(List<String> values) {
            addCriterion("supplier_seasonno in", values, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoNotIn(List<String> values) {
            addCriterion("supplier_seasonno not in", values, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoBetween(String value1, String value2) {
            addCriterion("supplier_seasonno between", value1, value2, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnoNotBetween(String value1, String value2) {
            addCriterion("supplier_seasonno not between", value1, value2, "supplierSeasonno");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameIsNull() {
            addCriterion("supplier_seasonname is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameIsNotNull() {
            addCriterion("supplier_seasonname is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameEqualTo(String value) {
            addCriterion("supplier_seasonname =", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameNotEqualTo(String value) {
            addCriterion("supplier_seasonname <>", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameGreaterThan(String value) {
            addCriterion("supplier_seasonname >", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_seasonname >=", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameLessThan(String value) {
            addCriterion("supplier_seasonname <", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameLessThanOrEqualTo(String value) {
            addCriterion("supplier_seasonname <=", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameLike(String value) {
            addCriterion("supplier_seasonname like", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameNotLike(String value) {
            addCriterion("supplier_seasonname not like", value, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameIn(List<String> values) {
            addCriterion("supplier_seasonname in", values, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameNotIn(List<String> values) {
            addCriterion("supplier_seasonname not in", values, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameBetween(String value1, String value2) {
            addCriterion("supplier_seasonname between", value1, value2, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonnameNotBetween(String value1, String value2) {
            addCriterion("supplier_seasonname not between", value1, value2, "supplierSeasonname");
            return (Criteria) this;
        }

        public Criteria andIsexistpicIsNull() {
            addCriterion("isexistpic is null");
            return (Criteria) this;
        }

        public Criteria andIsexistpicIsNotNull() {
            addCriterion("isexistpic is not null");
            return (Criteria) this;
        }

        public Criteria andIsexistpicEqualTo(Byte value) {
            addCriterion("isexistpic =", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicNotEqualTo(Byte value) {
            addCriterion("isexistpic <>", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicGreaterThan(Byte value) {
            addCriterion("isexistpic >", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicGreaterThanOrEqualTo(Byte value) {
            addCriterion("isexistpic >=", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicLessThan(Byte value) {
            addCriterion("isexistpic <", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicLessThanOrEqualTo(Byte value) {
            addCriterion("isexistpic <=", value, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicIn(List<Byte> values) {
            addCriterion("isexistpic in", values, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicNotIn(List<Byte> values) {
            addCriterion("isexistpic not in", values, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicBetween(Byte value1, Byte value2) {
            addCriterion("isexistpic between", value1, value2, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andIsexistpicNotBetween(Byte value1, Byte value2) {
            addCriterion("isexistpic not between", value1, value2, "isexistpic");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIsNull() {
            addCriterion("supplier_material is null");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIsNotNull() {
            addCriterion("supplier_material is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialEqualTo(String value) {
            addCriterion("supplier_material =", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotEqualTo(String value) {
            addCriterion("supplier_material <>", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialGreaterThan(String value) {
            addCriterion("supplier_material >", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_material >=", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLessThan(String value) {
            addCriterion("supplier_material <", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLessThanOrEqualTo(String value) {
            addCriterion("supplier_material <=", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLike(String value) {
            addCriterion("supplier_material like", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotLike(String value) {
            addCriterion("supplier_material not like", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIn(List<String> values) {
            addCriterion("supplier_material in", values, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotIn(List<String> values) {
            addCriterion("supplier_material not in", values, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialBetween(String value1, String value2) {
            addCriterion("supplier_material between", value1, value2, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotBetween(String value1, String value2) {
            addCriterion("supplier_material not between", value1, value2, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginIsNull() {
            addCriterion("supplier_origin is null");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginIsNotNull() {
            addCriterion("supplier_origin is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginEqualTo(String value) {
            addCriterion("supplier_origin =", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginNotEqualTo(String value) {
            addCriterion("supplier_origin <>", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginGreaterThan(String value) {
            addCriterion("supplier_origin >", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_origin >=", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginLessThan(String value) {
            addCriterion("supplier_origin <", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginLessThanOrEqualTo(String value) {
            addCriterion("supplier_origin <=", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginLike(String value) {
            addCriterion("supplier_origin like", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginNotLike(String value) {
            addCriterion("supplier_origin not like", value, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginIn(List<String> values) {
            addCriterion("supplier_origin in", values, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginNotIn(List<String> values) {
            addCriterion("supplier_origin not in", values, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginBetween(String value1, String value2) {
            addCriterion("supplier_origin between", value1, value2, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierOriginNotBetween(String value1, String value2) {
            addCriterion("supplier_origin not between", value1, value2, "supplierOrigin");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescIsNull() {
            addCriterion("supplier_spu_desc is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescIsNotNull() {
            addCriterion("supplier_spu_desc is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescEqualTo(String value) {
            addCriterion("supplier_spu_desc =", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescNotEqualTo(String value) {
            addCriterion("supplier_spu_desc <>", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescGreaterThan(String value) {
            addCriterion("supplier_spu_desc >", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_desc >=", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescLessThan(String value) {
            addCriterion("supplier_spu_desc <", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_desc <=", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescLike(String value) {
            addCriterion("supplier_spu_desc like", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescNotLike(String value) {
            addCriterion("supplier_spu_desc not like", value, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescIn(List<String> values) {
            addCriterion("supplier_spu_desc in", values, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescNotIn(List<String> values) {
            addCriterion("supplier_spu_desc not in", values, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescBetween(String value1, String value2) {
            addCriterion("supplier_spu_desc between", value1, value2, "supplierSpuDesc");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuDescNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_desc not between", value1, value2, "supplierSpuDesc");
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