package com.shangpin.ephub.data.mysql.mapping.sku.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSkuSupplierMappingCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSkuSupplierMappingCriteria() {
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

        public Criteria andSkuSupplierMappingIdIsNull() {
            addCriterion("sku_supplier_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdIsNotNull() {
            addCriterion("sku_supplier_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdEqualTo(Long value) {
            addCriterion("sku_supplier_mapping_id =", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdNotEqualTo(Long value) {
            addCriterion("sku_supplier_mapping_id <>", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdGreaterThan(Long value) {
            addCriterion("sku_supplier_mapping_id >", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sku_supplier_mapping_id >=", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdLessThan(Long value) {
            addCriterion("sku_supplier_mapping_id <", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("sku_supplier_mapping_id <=", value, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdIn(List<Long> values) {
            addCriterion("sku_supplier_mapping_id in", values, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdNotIn(List<Long> values) {
            addCriterion("sku_supplier_mapping_id not in", values, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdBetween(Long value1, Long value2) {
            addCriterion("sku_supplier_mapping_id between", value1, value2, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuSupplierMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("sku_supplier_mapping_id not between", value1, value2, "skuSupplierMappingId");
            return (Criteria) this;
        }

        public Criteria andSkuNoIsNull() {
            addCriterion("sku_no is null");
            return (Criteria) this;
        }

        public Criteria andSkuNoIsNotNull() {
            addCriterion("sku_no is not null");
            return (Criteria) this;
        }

        public Criteria andSkuNoEqualTo(String value) {
            addCriterion("sku_no =", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoNotEqualTo(String value) {
            addCriterion("sku_no <>", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoGreaterThan(String value) {
            addCriterion("sku_no >", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoGreaterThanOrEqualTo(String value) {
            addCriterion("sku_no >=", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoLessThan(String value) {
            addCriterion("sku_no <", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoLessThanOrEqualTo(String value) {
            addCriterion("sku_no <=", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoLike(String value) {
            addCriterion("sku_no like", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoNotLike(String value) {
            addCriterion("sku_no not like", value, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoIn(List<String> values) {
            addCriterion("sku_no in", values, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoNotIn(List<String> values) {
            addCriterion("sku_no not in", values, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoBetween(String value1, String value2) {
            addCriterion("sku_no between", value1, value2, "skuNo");
            return (Criteria) this;
        }

        public Criteria andSkuNoNotBetween(String value1, String value2) {
            addCriterion("sku_no not between", value1, value2, "skuNo");
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

        public Criteria andBarcodeIsNull() {
            addCriterion("barcode is null");
            return (Criteria) this;
        }

        public Criteria andBarcodeIsNotNull() {
            addCriterion("barcode is not null");
            return (Criteria) this;
        }

        public Criteria andBarcodeEqualTo(String value) {
            addCriterion("barcode =", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotEqualTo(String value) {
            addCriterion("barcode <>", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThan(String value) {
            addCriterion("barcode >", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeGreaterThanOrEqualTo(String value) {
            addCriterion("barcode >=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThan(String value) {
            addCriterion("barcode <", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLessThanOrEqualTo(String value) {
            addCriterion("barcode <=", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeLike(String value) {
            addCriterion("barcode like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotLike(String value) {
            addCriterion("barcode not like", value, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeIn(List<String> values) {
            addCriterion("barcode in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotIn(List<String> values) {
            addCriterion("barcode not in", values, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeBetween(String value1, String value2) {
            addCriterion("barcode between", value1, value2, "barcode");
            return (Criteria) this;
        }

        public Criteria andBarcodeNotBetween(String value1, String value2) {
            addCriterion("barcode not between", value1, value2, "barcode");
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

        public Criteria andNewSpuTypeIsNull() {
            addCriterion("new_spu_type is null");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeIsNotNull() {
            addCriterion("new_spu_type is not null");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeEqualTo(Byte value) {
            addCriterion("new_spu_type =", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeNotEqualTo(Byte value) {
            addCriterion("new_spu_type <>", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeGreaterThan(Byte value) {
            addCriterion("new_spu_type >", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("new_spu_type >=", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeLessThan(Byte value) {
            addCriterion("new_spu_type <", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeLessThanOrEqualTo(Byte value) {
            addCriterion("new_spu_type <=", value, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeIn(List<Byte> values) {
            addCriterion("new_spu_type in", values, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeNotIn(List<Byte> values) {
            addCriterion("new_spu_type not in", values, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeBetween(Byte value1, Byte value2) {
            addCriterion("new_spu_type between", value1, value2, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andNewSpuTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("new_spu_type not between", value1, value2, "newSpuType");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateIsNull() {
            addCriterion("supplier_select_state is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateIsNotNull() {
            addCriterion("supplier_select_state is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateEqualTo(Byte value) {
            addCriterion("supplier_select_state =", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateNotEqualTo(Byte value) {
            addCriterion("supplier_select_state <>", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateGreaterThan(Byte value) {
            addCriterion("supplier_select_state >", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("supplier_select_state >=", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateLessThan(Byte value) {
            addCriterion("supplier_select_state <", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateLessThanOrEqualTo(Byte value) {
            addCriterion("supplier_select_state <=", value, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateIn(List<Byte> values) {
            addCriterion("supplier_select_state in", values, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateNotIn(List<Byte> values) {
            addCriterion("supplier_select_state not in", values, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateBetween(Byte value1, Byte value2) {
            addCriterion("supplier_select_state between", value1, value2, "supplierSelectState");
            return (Criteria) this;
        }

        public Criteria andSupplierSelectStateNotBetween(Byte value1, Byte value2) {
            addCriterion("supplier_select_state not between", value1, value2, "supplierSelectState");
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

        public Criteria andIsNewSupplierIsNull() {
            addCriterion("is_new_supplier is null");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierIsNotNull() {
            addCriterion("is_new_supplier is not null");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierEqualTo(Byte value) {
            addCriterion("is_new_supplier =", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierNotEqualTo(Byte value) {
            addCriterion("is_new_supplier <>", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierGreaterThan(Byte value) {
            addCriterion("is_new_supplier >", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_new_supplier >=", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierLessThan(Byte value) {
            addCriterion("is_new_supplier <", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierLessThanOrEqualTo(Byte value) {
            addCriterion("is_new_supplier <=", value, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierIn(List<Byte> values) {
            addCriterion("is_new_supplier in", values, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierNotIn(List<Byte> values) {
            addCriterion("is_new_supplier not in", values, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierBetween(Byte value1, Byte value2) {
            addCriterion("is_new_supplier between", value1, value2, "isNewSupplier");
            return (Criteria) this;
        }

        public Criteria andIsNewSupplierNotBetween(Byte value1, Byte value2) {
            addCriterion("is_new_supplier not between", value1, value2, "isNewSupplier");
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