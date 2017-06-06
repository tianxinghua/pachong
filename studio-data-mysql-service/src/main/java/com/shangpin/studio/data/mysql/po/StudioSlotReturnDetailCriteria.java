package com.shangpin.studio.data.mysql.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudioSlotReturnDetailCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public StudioSlotReturnDetailCriteria() {
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

        public Criteria andStudioSlotReturnDetailIdIsNull() {
            addCriterion("studio_slot_return_detail_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdIsNotNull() {
            addCriterion("studio_slot_return_detail_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdEqualTo(Long value) {
            addCriterion("studio_slot_return_detail_id =", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdNotEqualTo(Long value) {
            addCriterion("studio_slot_return_detail_id <>", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdGreaterThan(Long value) {
            addCriterion("studio_slot_return_detail_id >", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_slot_return_detail_id >=", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdLessThan(Long value) {
            addCriterion("studio_slot_return_detail_id <", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_slot_return_detail_id <=", value, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdIn(List<Long> values) {
            addCriterion("studio_slot_return_detail_id in", values, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdNotIn(List<Long> values) {
            addCriterion("studio_slot_return_detail_id not in", values, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdBetween(Long value1, Long value2) {
            addCriterion("studio_slot_return_detail_id between", value1, value2, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnDetailIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_slot_return_detail_id not between", value1, value2, "studioSlotReturnDetailId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdIsNull() {
            addCriterion("studio_slot_return_master_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdIsNotNull() {
            addCriterion("studio_slot_return_master_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdEqualTo(Long value) {
            addCriterion("studio_slot_return_master_id =", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdNotEqualTo(Long value) {
            addCriterion("studio_slot_return_master_id <>", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdGreaterThan(Long value) {
            addCriterion("studio_slot_return_master_id >", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_slot_return_master_id >=", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdLessThan(Long value) {
            addCriterion("studio_slot_return_master_id <", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_slot_return_master_id <=", value, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdIn(List<Long> values) {
            addCriterion("studio_slot_return_master_id in", values, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdNotIn(List<Long> values) {
            addCriterion("studio_slot_return_master_id not in", values, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdBetween(Long value1, Long value2) {
            addCriterion("studio_slot_return_master_id between", value1, value2, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotReturnMasterIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_slot_return_master_id not between", value1, value2, "studioSlotReturnMasterId");
            return (Criteria) this;
        }

        public Criteria andSlotNoIsNull() {
            addCriterion("slot_no is null");
            return (Criteria) this;
        }

        public Criteria andSlotNoIsNotNull() {
            addCriterion("slot_no is not null");
            return (Criteria) this;
        }

        public Criteria andSlotNoEqualTo(String value) {
            addCriterion("slot_no =", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoNotEqualTo(String value) {
            addCriterion("slot_no <>", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoGreaterThan(String value) {
            addCriterion("slot_no >", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoGreaterThanOrEqualTo(String value) {
            addCriterion("slot_no >=", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoLessThan(String value) {
            addCriterion("slot_no <", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoLessThanOrEqualTo(String value) {
            addCriterion("slot_no <=", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoLike(String value) {
            addCriterion("slot_no like", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoNotLike(String value) {
            addCriterion("slot_no not like", value, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoIn(List<String> values) {
            addCriterion("slot_no in", values, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoNotIn(List<String> values) {
            addCriterion("slot_no not in", values, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoBetween(String value1, String value2) {
            addCriterion("slot_no between", value1, value2, "slotNo");
            return (Criteria) this;
        }

        public Criteria andSlotNoNotBetween(String value1, String value2) {
            addCriterion("slot_no not between", value1, value2, "slotNo");
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

        public Criteria andSlotSpuNoIsNull() {
            addCriterion("slot_spu_no is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoIsNotNull() {
            addCriterion("slot_spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoEqualTo(String value) {
            addCriterion("slot_spu_no =", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotEqualTo(String value) {
            addCriterion("slot_spu_no <>", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoGreaterThan(String value) {
            addCriterion("slot_spu_no >", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("slot_spu_no >=", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLessThan(String value) {
            addCriterion("slot_spu_no <", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLessThanOrEqualTo(String value) {
            addCriterion("slot_spu_no <=", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLike(String value) {
            addCriterion("slot_spu_no like", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotLike(String value) {
            addCriterion("slot_spu_no not like", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoIn(List<String> values) {
            addCriterion("slot_spu_no in", values, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotIn(List<String> values) {
            addCriterion("slot_spu_no not in", values, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoBetween(String value1, String value2) {
            addCriterion("slot_spu_no between", value1, value2, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotBetween(String value1, String value2) {
            addCriterion("slot_spu_no not between", value1, value2, "slotSpuNo");
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

        public Criteria andSupplierBrandNameIsNull() {
            addCriterion("supplier_brand_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameIsNotNull() {
            addCriterion("supplier_brand_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameEqualTo(String value) {
            addCriterion("supplier_brand_name =", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameNotEqualTo(String value) {
            addCriterion("supplier_brand_name <>", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameGreaterThan(String value) {
            addCriterion("supplier_brand_name >", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_brand_name >=", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameLessThan(String value) {
            addCriterion("supplier_brand_name <", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_brand_name <=", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameLike(String value) {
            addCriterion("supplier_brand_name like", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameNotLike(String value) {
            addCriterion("supplier_brand_name not like", value, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameIn(List<String> values) {
            addCriterion("supplier_brand_name in", values, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameNotIn(List<String> values) {
            addCriterion("supplier_brand_name not in", values, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameBetween(String value1, String value2) {
            addCriterion("supplier_brand_name between", value1, value2, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierBrandNameNotBetween(String value1, String value2) {
            addCriterion("supplier_brand_name not between", value1, value2, "supplierBrandName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameIsNull() {
            addCriterion("supplier_category_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameIsNotNull() {
            addCriterion("supplier_category_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameEqualTo(String value) {
            addCriterion("supplier_category_name =", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameNotEqualTo(String value) {
            addCriterion("supplier_category_name <>", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameGreaterThan(String value) {
            addCriterion("supplier_category_name >", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_category_name >=", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameLessThan(String value) {
            addCriterion("supplier_category_name <", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_category_name <=", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameLike(String value) {
            addCriterion("supplier_category_name like", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameNotLike(String value) {
            addCriterion("supplier_category_name not like", value, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameIn(List<String> values) {
            addCriterion("supplier_category_name in", values, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameNotIn(List<String> values) {
            addCriterion("supplier_category_name not in", values, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameBetween(String value1, String value2) {
            addCriterion("supplier_category_name between", value1, value2, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierCategoryNameNotBetween(String value1, String value2) {
            addCriterion("supplier_category_name not between", value1, value2, "supplierCategoryName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameIsNull() {
            addCriterion("supplier_season_name is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameIsNotNull() {
            addCriterion("supplier_season_name is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameEqualTo(String value) {
            addCriterion("supplier_season_name =", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameNotEqualTo(String value) {
            addCriterion("supplier_season_name <>", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameGreaterThan(String value) {
            addCriterion("supplier_season_name >", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_season_name >=", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameLessThan(String value) {
            addCriterion("supplier_season_name <", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameLessThanOrEqualTo(String value) {
            addCriterion("supplier_season_name <=", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameLike(String value) {
            addCriterion("supplier_season_name like", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameNotLike(String value) {
            addCriterion("supplier_season_name not like", value, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameIn(List<String> values) {
            addCriterion("supplier_season_name in", values, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameNotIn(List<String> values) {
            addCriterion("supplier_season_name not in", values, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameBetween(String value1, String value2) {
            addCriterion("supplier_season_name between", value1, value2, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNameNotBetween(String value1, String value2) {
            addCriterion("supplier_season_name not between", value1, value2, "supplierSeasonName");
            return (Criteria) this;
        }

        public Criteria andSendStateIsNull() {
            addCriterion("send_state is null");
            return (Criteria) this;
        }

        public Criteria andSendStateIsNotNull() {
            addCriterion("send_state is not null");
            return (Criteria) this;
        }

        public Criteria andSendStateEqualTo(Byte value) {
            addCriterion("send_state =", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateNotEqualTo(Byte value) {
            addCriterion("send_state <>", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateGreaterThan(Byte value) {
            addCriterion("send_state >", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("send_state >=", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateLessThan(Byte value) {
            addCriterion("send_state <", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateLessThanOrEqualTo(Byte value) {
            addCriterion("send_state <=", value, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateIn(List<Byte> values) {
            addCriterion("send_state in", values, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateNotIn(List<Byte> values) {
            addCriterion("send_state not in", values, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateBetween(Byte value1, Byte value2) {
            addCriterion("send_state between", value1, value2, "sendState");
            return (Criteria) this;
        }

        public Criteria andSendStateNotBetween(Byte value1, Byte value2) {
            addCriterion("send_state not between", value1, value2, "sendState");
            return (Criteria) this;
        }

        public Criteria andArriveStateIsNull() {
            addCriterion("arrive_state is null");
            return (Criteria) this;
        }

        public Criteria andArriveStateIsNotNull() {
            addCriterion("arrive_state is not null");
            return (Criteria) this;
        }

        public Criteria andArriveStateEqualTo(Byte value) {
            addCriterion("arrive_state =", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateNotEqualTo(Byte value) {
            addCriterion("arrive_state <>", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateGreaterThan(Byte value) {
            addCriterion("arrive_state >", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("arrive_state >=", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateLessThan(Byte value) {
            addCriterion("arrive_state <", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateLessThanOrEqualTo(Byte value) {
            addCriterion("arrive_state <=", value, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateIn(List<Byte> values) {
            addCriterion("arrive_state in", values, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateNotIn(List<Byte> values) {
            addCriterion("arrive_state not in", values, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateBetween(Byte value1, Byte value2) {
            addCriterion("arrive_state between", value1, value2, "arriveState");
            return (Criteria) this;
        }

        public Criteria andArriveStateNotBetween(Byte value1, Byte value2) {
            addCriterion("arrive_state not between", value1, value2, "arriveState");
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

        public Criteria andSendUserIsNull() {
            addCriterion("send_user is null");
            return (Criteria) this;
        }

        public Criteria andSendUserIsNotNull() {
            addCriterion("send_user is not null");
            return (Criteria) this;
        }

        public Criteria andSendUserEqualTo(String value) {
            addCriterion("send_user =", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserNotEqualTo(String value) {
            addCriterion("send_user <>", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserGreaterThan(String value) {
            addCriterion("send_user >", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserGreaterThanOrEqualTo(String value) {
            addCriterion("send_user >=", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserLessThan(String value) {
            addCriterion("send_user <", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserLessThanOrEqualTo(String value) {
            addCriterion("send_user <=", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserLike(String value) {
            addCriterion("send_user like", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserNotLike(String value) {
            addCriterion("send_user not like", value, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserIn(List<String> values) {
            addCriterion("send_user in", values, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserNotIn(List<String> values) {
            addCriterion("send_user not in", values, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserBetween(String value1, String value2) {
            addCriterion("send_user between", value1, value2, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendUserNotBetween(String value1, String value2) {
            addCriterion("send_user not between", value1, value2, "sendUser");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNull() {
            addCriterion("send_time is null");
            return (Criteria) this;
        }

        public Criteria andSendTimeIsNotNull() {
            addCriterion("send_time is not null");
            return (Criteria) this;
        }

        public Criteria andSendTimeEqualTo(Date value) {
            addCriterion("send_time =", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotEqualTo(Date value) {
            addCriterion("send_time <>", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThan(Date value) {
            addCriterion("send_time >", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("send_time >=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThan(Date value) {
            addCriterion("send_time <", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeLessThanOrEqualTo(Date value) {
            addCriterion("send_time <=", value, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeIn(List<Date> values) {
            addCriterion("send_time in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotIn(List<Date> values) {
            addCriterion("send_time not in", values, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeBetween(Date value1, Date value2) {
            addCriterion("send_time between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andSendTimeNotBetween(Date value1, Date value2) {
            addCriterion("send_time not between", value1, value2, "sendTime");
            return (Criteria) this;
        }

        public Criteria andArriveUserIsNull() {
            addCriterion("arrive_user is null");
            return (Criteria) this;
        }

        public Criteria andArriveUserIsNotNull() {
            addCriterion("arrive_user is not null");
            return (Criteria) this;
        }

        public Criteria andArriveUserEqualTo(String value) {
            addCriterion("arrive_user =", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserNotEqualTo(String value) {
            addCriterion("arrive_user <>", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserGreaterThan(String value) {
            addCriterion("arrive_user >", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserGreaterThanOrEqualTo(String value) {
            addCriterion("arrive_user >=", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserLessThan(String value) {
            addCriterion("arrive_user <", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserLessThanOrEqualTo(String value) {
            addCriterion("arrive_user <=", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserLike(String value) {
            addCriterion("arrive_user like", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserNotLike(String value) {
            addCriterion("arrive_user not like", value, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserIn(List<String> values) {
            addCriterion("arrive_user in", values, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserNotIn(List<String> values) {
            addCriterion("arrive_user not in", values, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserBetween(String value1, String value2) {
            addCriterion("arrive_user between", value1, value2, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveUserNotBetween(String value1, String value2) {
            addCriterion("arrive_user not between", value1, value2, "arriveUser");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIsNull() {
            addCriterion("arrive_time is null");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIsNotNull() {
            addCriterion("arrive_time is not null");
            return (Criteria) this;
        }

        public Criteria andArriveTimeEqualTo(Date value) {
            addCriterion("arrive_time =", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotEqualTo(Date value) {
            addCriterion("arrive_time <>", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeGreaterThan(Date value) {
            addCriterion("arrive_time >", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("arrive_time >=", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeLessThan(Date value) {
            addCriterion("arrive_time <", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeLessThanOrEqualTo(Date value) {
            addCriterion("arrive_time <=", value, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeIn(List<Date> values) {
            addCriterion("arrive_time in", values, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotIn(List<Date> values) {
            addCriterion("arrive_time not in", values, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeBetween(Date value1, Date value2) {
            addCriterion("arrive_time between", value1, value2, "arriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveTimeNotBetween(Date value1, Date value2) {
            addCriterion("arrive_time not between", value1, value2, "arriveTime");
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