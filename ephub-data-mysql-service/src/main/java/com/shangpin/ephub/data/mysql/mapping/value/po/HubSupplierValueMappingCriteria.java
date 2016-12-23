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

        public Criteria andHubSupplierValMappingIdIsNull() {
            addCriterion("hub_supplier_val_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdIsNotNull() {
            addCriterion("hub_supplier_val_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdEqualTo(Long value) {
            addCriterion("hub_supplier_val_mapping_id =", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdNotEqualTo(Long value) {
            addCriterion("hub_supplier_val_mapping_id <>", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdGreaterThan(Long value) {
            addCriterion("hub_supplier_val_mapping_id >", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("hub_supplier_val_mapping_id >=", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdLessThan(Long value) {
            addCriterion("hub_supplier_val_mapping_id <", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("hub_supplier_val_mapping_id <=", value, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdIn(List<Long> values) {
            addCriterion("hub_supplier_val_mapping_id in", values, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdNotIn(List<Long> values) {
            addCriterion("hub_supplier_val_mapping_id not in", values, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdBetween(Long value1, Long value2) {
            addCriterion("hub_supplier_val_mapping_id between", value1, value2, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andHubSupplierValMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("hub_supplier_val_mapping_id not between", value1, value2, "hubSupplierValMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdIsNull() {
            addCriterion("column_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdIsNotNull() {
            addCriterion("column_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdEqualTo(Long value) {
            addCriterion("column_mapping_id =", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdNotEqualTo(Long value) {
            addCriterion("column_mapping_id <>", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdGreaterThan(Long value) {
            addCriterion("column_mapping_id >", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("column_mapping_id >=", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdLessThan(Long value) {
            addCriterion("column_mapping_id <", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("column_mapping_id <=", value, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdIn(List<Long> values) {
            addCriterion("column_mapping_id in", values, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdNotIn(List<Long> values) {
            addCriterion("column_mapping_id not in", values, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdBetween(Long value1, Long value2) {
            addCriterion("column_mapping_id between", value1, value2, "columnMappingId");
            return (Criteria) this;
        }

        public Criteria andColumnMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("column_mapping_id not between", value1, value2, "columnMappingId");
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

        public Criteria andSupplierValNoIsNull() {
            addCriterion("supplier_val_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoIsNotNull() {
            addCriterion("supplier_val_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoEqualTo(String value) {
            addCriterion("supplier_val_no =", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoNotEqualTo(String value) {
            addCriterion("supplier_val_no <>", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoGreaterThan(String value) {
            addCriterion("supplier_val_no >", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_val_no >=", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoLessThan(String value) {
            addCriterion("supplier_val_no <", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_val_no <=", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoLike(String value) {
            addCriterion("supplier_val_no like", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoNotLike(String value) {
            addCriterion("supplier_val_no not like", value, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoIn(List<String> values) {
            addCriterion("supplier_val_no in", values, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoNotIn(List<String> values) {
            addCriterion("supplier_val_no not in", values, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoBetween(String value1, String value2) {
            addCriterion("supplier_val_no between", value1, value2, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValNoNotBetween(String value1, String value2) {
            addCriterion("supplier_val_no not between", value1, value2, "supplierValNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoIsNull() {
            addCriterion("supplier_val_parent_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoIsNotNull() {
            addCriterion("supplier_val_parent_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoEqualTo(String value) {
            addCriterion("supplier_val_parent_no =", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoNotEqualTo(String value) {
            addCriterion("supplier_val_parent_no <>", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoGreaterThan(String value) {
            addCriterion("supplier_val_parent_no >", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_val_parent_no >=", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoLessThan(String value) {
            addCriterion("supplier_val_parent_no <", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_val_parent_no <=", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoLike(String value) {
            addCriterion("supplier_val_parent_no like", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoNotLike(String value) {
            addCriterion("supplier_val_parent_no not like", value, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoIn(List<String> values) {
            addCriterion("supplier_val_parent_no in", values, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoNotIn(List<String> values) {
            addCriterion("supplier_val_parent_no not in", values, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoBetween(String value1, String value2) {
            addCriterion("supplier_val_parent_no between", value1, value2, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValParentNoNotBetween(String value1, String value2) {
            addCriterion("supplier_val_parent_no not between", value1, value2, "supplierValParentNo");
            return (Criteria) this;
        }

        public Criteria andSupplierValIsNull() {
            addCriterion("supplier_val is null");
            return (Criteria) this;
        }

        public Criteria andSupplierValIsNotNull() {
            addCriterion("supplier_val is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierValEqualTo(String value) {
            addCriterion("supplier_val =", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValNotEqualTo(String value) {
            addCriterion("supplier_val <>", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValGreaterThan(String value) {
            addCriterion("supplier_val >", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_val >=", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValLessThan(String value) {
            addCriterion("supplier_val <", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValLessThanOrEqualTo(String value) {
            addCriterion("supplier_val <=", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValLike(String value) {
            addCriterion("supplier_val like", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValNotLike(String value) {
            addCriterion("supplier_val not like", value, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValIn(List<String> values) {
            addCriterion("supplier_val in", values, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValNotIn(List<String> values) {
            addCriterion("supplier_val not in", values, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValBetween(String value1, String value2) {
            addCriterion("supplier_val between", value1, value2, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andSupplierValNotBetween(String value1, String value2) {
            addCriterion("supplier_val not between", value1, value2, "supplierVal");
            return (Criteria) this;
        }

        public Criteria andHubValNoIsNull() {
            addCriterion("hub_val_no is null");
            return (Criteria) this;
        }

        public Criteria andHubValNoIsNotNull() {
            addCriterion("hub_val_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubValNoEqualTo(String value) {
            addCriterion("hub_val_no =", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoNotEqualTo(String value) {
            addCriterion("hub_val_no <>", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoGreaterThan(String value) {
            addCriterion("hub_val_no >", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_val_no >=", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoLessThan(String value) {
            addCriterion("hub_val_no <", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoLessThanOrEqualTo(String value) {
            addCriterion("hub_val_no <=", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoLike(String value) {
            addCriterion("hub_val_no like", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoNotLike(String value) {
            addCriterion("hub_val_no not like", value, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoIn(List<String> values) {
            addCriterion("hub_val_no in", values, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoNotIn(List<String> values) {
            addCriterion("hub_val_no not in", values, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoBetween(String value1, String value2) {
            addCriterion("hub_val_no between", value1, value2, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValNoNotBetween(String value1, String value2) {
            addCriterion("hub_val_no not between", value1, value2, "hubValNo");
            return (Criteria) this;
        }

        public Criteria andHubValIsNull() {
            addCriterion("hub_val is null");
            return (Criteria) this;
        }

        public Criteria andHubValIsNotNull() {
            addCriterion("hub_val is not null");
            return (Criteria) this;
        }

        public Criteria andHubValEqualTo(String value) {
            addCriterion("hub_val =", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValNotEqualTo(String value) {
            addCriterion("hub_val <>", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValGreaterThan(String value) {
            addCriterion("hub_val >", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValGreaterThanOrEqualTo(String value) {
            addCriterion("hub_val >=", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValLessThan(String value) {
            addCriterion("hub_val <", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValLessThanOrEqualTo(String value) {
            addCriterion("hub_val <=", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValLike(String value) {
            addCriterion("hub_val like", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValNotLike(String value) {
            addCriterion("hub_val not like", value, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValIn(List<String> values) {
            addCriterion("hub_val in", values, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValNotIn(List<String> values) {
            addCriterion("hub_val not in", values, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValBetween(String value1, String value2) {
            addCriterion("hub_val between", value1, value2, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValNotBetween(String value1, String value2) {
            addCriterion("hub_val not between", value1, value2, "hubVal");
            return (Criteria) this;
        }

        public Criteria andHubValTypeIsNull() {
            addCriterion("hub_val_type is null");
            return (Criteria) this;
        }

        public Criteria andHubValTypeIsNotNull() {
            addCriterion("hub_val_type is not null");
            return (Criteria) this;
        }

        public Criteria andHubValTypeEqualTo(Byte value) {
            addCriterion("hub_val_type =", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeNotEqualTo(Byte value) {
            addCriterion("hub_val_type <>", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeGreaterThan(Byte value) {
            addCriterion("hub_val_type >", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("hub_val_type >=", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeLessThan(Byte value) {
            addCriterion("hub_val_type <", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeLessThanOrEqualTo(Byte value) {
            addCriterion("hub_val_type <=", value, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeIn(List<Byte> values) {
            addCriterion("hub_val_type in", values, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeNotIn(List<Byte> values) {
            addCriterion("hub_val_type not in", values, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeBetween(Byte value1, Byte value2) {
            addCriterion("hub_val_type between", value1, value2, "hubValType");
            return (Criteria) this;
        }

        public Criteria andHubValTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("hub_val_type not between", value1, value2, "hubValType");
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

        public Criteria andSortValIsNull() {
            addCriterion("sort_val is null");
            return (Criteria) this;
        }

        public Criteria andSortValIsNotNull() {
            addCriterion("sort_val is not null");
            return (Criteria) this;
        }

        public Criteria andSortValEqualTo(Short value) {
            addCriterion("sort_val =", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValNotEqualTo(Short value) {
            addCriterion("sort_val <>", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValGreaterThan(Short value) {
            addCriterion("sort_val >", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValGreaterThanOrEqualTo(Short value) {
            addCriterion("sort_val >=", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValLessThan(Short value) {
            addCriterion("sort_val <", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValLessThanOrEqualTo(Short value) {
            addCriterion("sort_val <=", value, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValIn(List<Short> values) {
            addCriterion("sort_val in", values, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValNotIn(List<Short> values) {
            addCriterion("sort_val not in", values, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValBetween(Short value1, Short value2) {
            addCriterion("sort_val between", value1, value2, "sortVal");
            return (Criteria) this;
        }

        public Criteria andSortValNotBetween(Short value1, Short value2) {
            addCriterion("sort_val not between", value1, value2, "sortVal");
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