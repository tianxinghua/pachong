package com.shangpin.ephub.data.mysql.mapping.value.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSupplierValueMappingCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSupplierValueMappingCriteria() {
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

        public Criteria andValueMappingIdIsNull() {
            addCriterion("value_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdIsNotNull() {
            addCriterion("value_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdEqualTo(Long value) {
            addCriterion("value_mapping_id =", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdNotEqualTo(Long value) {
            addCriterion("value_mapping_id <>", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdGreaterThan(Long value) {
            addCriterion("value_mapping_id >", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("value_mapping_id >=", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdLessThan(Long value) {
            addCriterion("value_mapping_id <", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("value_mapping_id <=", value, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdIn(List<Long> values) {
            addCriterion("value_mapping_id in", values, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdNotIn(List<Long> values) {
            addCriterion("value_mapping_id not in", values, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdBetween(Long value1, Long value2) {
            addCriterion("value_mapping_id between", value1, value2, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andValueMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("value_mapping_id not between", value1, value2, "valueMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdIsNull() {
            addCriterion("col_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andColMappingIdIsNotNull() {
            addCriterion("col_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andColMappingIdEqualTo(Long value) {
            addCriterion("col_mapping_id =", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdNotEqualTo(Long value) {
            addCriterion("col_mapping_id <>", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdGreaterThan(Long value) {
            addCriterion("col_mapping_id >", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("col_mapping_id >=", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdLessThan(Long value) {
            addCriterion("col_mapping_id <", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("col_mapping_id <=", value, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdIn(List<Long> values) {
            addCriterion("col_mapping_id in", values, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdNotIn(List<Long> values) {
            addCriterion("col_mapping_id not in", values, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdBetween(Long value1, Long value2) {
            addCriterion("col_mapping_id between", value1, value2, "colMappingId");
            return (Criteria) this;
        }

        public Criteria andColMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("col_mapping_id not between", value1, value2, "colMappingId");
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

        public Criteria andSupplierValueNoIsNull() {
            addCriterion("supplier_value_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoIsNotNull() {
            addCriterion("supplier_value_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoEqualTo(String value) {
            addCriterion("supplier_value_no =", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoNotEqualTo(String value) {
            addCriterion("supplier_value_no <>", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoGreaterThan(String value) {
            addCriterion("supplier_value_no >", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_value_no >=", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoLessThan(String value) {
            addCriterion("supplier_value_no <", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_value_no <=", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoLike(String value) {
            addCriterion("supplier_value_no like", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoNotLike(String value) {
            addCriterion("supplier_value_no not like", value, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoIn(List<String> values) {
            addCriterion("supplier_value_no in", values, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoNotIn(List<String> values) {
            addCriterion("supplier_value_no not in", values, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoBetween(String value1, String value2) {
            addCriterion("supplier_value_no between", value1, value2, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNoNotBetween(String value1, String value2) {
            addCriterion("supplier_value_no not between", value1, value2, "supplierValueNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoIsNull() {
            addCriterion("supplier_value_parent_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoIsNotNull() {
            addCriterion("supplier_value_parent_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoEqualTo(String value) {
            addCriterion("supplier_value_parent_no =", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoNotEqualTo(String value) {
            addCriterion("supplier_value_parent_no <>", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoGreaterThan(String value) {
            addCriterion("supplier_value_parent_no >", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_value_parent_no >=", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoLessThan(String value) {
            addCriterion("supplier_value_parent_no <", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_value_parent_no <=", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoLike(String value) {
            addCriterion("supplier_value_parent_no like", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoNotLike(String value) {
            addCriterion("supplier_value_parent_no not like", value, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoIn(List<String> values) {
            addCriterion("supplier_value_parent_no in", values, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoNotIn(List<String> values) {
            addCriterion("supplier_value_parent_no not in", values, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoBetween(String value1, String value2) {
            addCriterion("supplier_value_parent_no between", value1, value2, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueParentNoNotBetween(String value1, String value2) {
            addCriterion("supplier_value_parent_no not between", value1, value2, "supplierValueParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValueIsNull() {
            addCriterion("supplier_value is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueIsNotNull() {
            addCriterion("supplier_value is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValueEqualTo(String value) {
            addCriterion("supplier_value =", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNotEqualTo(String value) {
            addCriterion("supplier_value <>", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueGreaterThan(String value) {
            addCriterion("supplier_value >", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_value >=", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueLessThan(String value) {
            addCriterion("supplier_value <", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueLessThanOrEqualTo(String value) {
            addCriterion("supplier_value <=", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueLike(String value) {
            addCriterion("supplier_value like", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNotLike(String value) {
            addCriterion("supplier_value not like", value, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueIn(List<String> values) {
            addCriterion("supplier_value in", values, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNotIn(List<String> values) {
            addCriterion("supplier_value not in", values, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueBetween(String value1, String value2) {
            addCriterion("supplier_value between", value1, value2, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andSupplierValueNotBetween(String value1, String value2) {
            addCriterion("supplier_value not between", value1, value2, "supplierValue");
            return (Criteria) this;
        }

        public Criteria andHubValueNoIsNull() {
            addCriterion("hub_value_no is null");
            return (Criteria) this;
        }

        public Criteria andHubValueNoIsNotNull() {
            addCriterion("hub_value_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubValueNoEqualTo(String value) {
            addCriterion("hub_value_no =", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoNotEqualTo(String value) {
            addCriterion("hub_value_no <>", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoGreaterThan(String value) {
            addCriterion("hub_value_no >", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_value_no >=", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoLessThan(String value) {
            addCriterion("hub_value_no <", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoLessThanOrEqualTo(String value) {
            addCriterion("hub_value_no <=", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoLike(String value) {
            addCriterion("hub_value_no like", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoNotLike(String value) {
            addCriterion("hub_value_no not like", value, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoIn(List<String> values) {
            addCriterion("hub_value_no in", values, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoNotIn(List<String> values) {
            addCriterion("hub_value_no not in", values, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoBetween(String value1, String value2) {
            addCriterion("hub_value_no between", value1, value2, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueNoNotBetween(String value1, String value2) {
            addCriterion("hub_value_no not between", value1, value2, "hubValueNo");
            return (Criteria) this;
        }

        public Criteria andHubValueIsNull() {
            addCriterion("hub_value is null");
            return (Criteria) this;
        }

        public Criteria andHubValueIsNotNull() {
            addCriterion("hub_value is not null");
            return (Criteria) this;
        }

        public Criteria andHubValueEqualTo(String value) {
            addCriterion("hub_value =", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueNotEqualTo(String value) {
            addCriterion("hub_value <>", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueGreaterThan(String value) {
            addCriterion("hub_value >", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueGreaterThanOrEqualTo(String value) {
            addCriterion("hub_value >=", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueLessThan(String value) {
            addCriterion("hub_value <", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueLessThanOrEqualTo(String value) {
            addCriterion("hub_value <=", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueLike(String value) {
            addCriterion("hub_value like", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueNotLike(String value) {
            addCriterion("hub_value not like", value, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueIn(List<String> values) {
            addCriterion("hub_value in", values, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueNotIn(List<String> values) {
            addCriterion("hub_value not in", values, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueBetween(String value1, String value2) {
            addCriterion("hub_value between", value1, value2, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueNotBetween(String value1, String value2) {
            addCriterion("hub_value not between", value1, value2, "hubValue");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeIsNull() {
            addCriterion("hub_value_type is null");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeIsNotNull() {
            addCriterion("hub_value_type is not null");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeEqualTo(Byte value) {
            addCriterion("hub_value_type =", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeNotEqualTo(Byte value) {
            addCriterion("hub_value_type <>", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeGreaterThan(Byte value) {
            addCriterion("hub_value_type >", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("hub_value_type >=", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeLessThan(Byte value) {
            addCriterion("hub_value_type <", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeLessThanOrEqualTo(Byte value) {
            addCriterion("hub_value_type <=", value, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeIn(List<Byte> values) {
            addCriterion("hub_value_type in", values, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeNotIn(List<Byte> values) {
            addCriterion("hub_value_type not in", values, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeBetween(Byte value1, Byte value2) {
            addCriterion("hub_value_type between", value1, value2, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andHubValueTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("hub_value_type not between", value1, value2, "hubValueType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeIsNull() {
            addCriterion("mapping_type is null");
            return (Criteria) this;
        }

        public Criteria andMappingTypeIsNotNull() {
            addCriterion("mapping_type is not null");
            return (Criteria) this;
        }

        public Criteria andMappingTypeEqualTo(Byte value) {
            addCriterion("mapping_type =", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeNotEqualTo(Byte value) {
            addCriterion("mapping_type <>", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeGreaterThan(Byte value) {
            addCriterion("mapping_type >", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("mapping_type >=", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeLessThan(Byte value) {
            addCriterion("mapping_type <", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeLessThanOrEqualTo(Byte value) {
            addCriterion("mapping_type <=", value, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeIn(List<Byte> values) {
            addCriterion("mapping_type in", values, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeNotIn(List<Byte> values) {
            addCriterion("mapping_type not in", values, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeBetween(Byte value1, Byte value2) {
            addCriterion("mapping_type between", value1, value2, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("mapping_type not between", value1, value2, "mappingType");
            return (Criteria) this;
        }

        public Criteria andMappingStateIsNull() {
            addCriterion("mapping_state is null");
            return (Criteria) this;
        }

        public Criteria andMappingStateIsNotNull() {
            addCriterion("mapping_state is not null");
            return (Criteria) this;
        }

        public Criteria andMappingStateEqualTo(Byte value) {
            addCriterion("mapping_state =", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateNotEqualTo(Byte value) {
            addCriterion("mapping_state <>", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateGreaterThan(Byte value) {
            addCriterion("mapping_state >", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("mapping_state >=", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateLessThan(Byte value) {
            addCriterion("mapping_state <", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateLessThanOrEqualTo(Byte value) {
            addCriterion("mapping_state <=", value, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateIn(List<Byte> values) {
            addCriterion("mapping_state in", values, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateNotIn(List<Byte> values) {
            addCriterion("mapping_state not in", values, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateBetween(Byte value1, Byte value2) {
            addCriterion("mapping_state between", value1, value2, "mappingState");
            return (Criteria) this;
        }

        public Criteria andMappingStateNotBetween(Byte value1, Byte value2) {
            addCriterion("mapping_state not between", value1, value2, "mappingState");
            return (Criteria) this;
        }

        public Criteria andSortValueIsNull() {
            addCriterion("sort_value is null");
            return (Criteria) this;
        }

        public Criteria andSortValueIsNotNull() {
            addCriterion("sort_value is not null");
            return (Criteria) this;
        }

        public Criteria andSortValueEqualTo(Short value) {
            addCriterion("sort_value =", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotEqualTo(Short value) {
            addCriterion("sort_value <>", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueGreaterThan(Short value) {
            addCriterion("sort_value >", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueGreaterThanOrEqualTo(Short value) {
            addCriterion("sort_value >=", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueLessThan(Short value) {
            addCriterion("sort_value <", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueLessThanOrEqualTo(Short value) {
            addCriterion("sort_value <=", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueIn(List<Short> values) {
            addCriterion("sort_value in", values, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotIn(List<Short> values) {
            addCriterion("sort_value not in", values, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueBetween(Short value1, Short value2) {
            addCriterion("sort_value between", value1, value2, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotBetween(Short value1, Short value2) {
            addCriterion("sort_value not between", value1, value2, "sortValue");
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