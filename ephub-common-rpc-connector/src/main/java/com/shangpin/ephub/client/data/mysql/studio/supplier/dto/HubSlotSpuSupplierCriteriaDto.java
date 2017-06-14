package com.shangpin.ephub.client.data.mysql.studio.supplier.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSlotSpuSupplierCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSlotSpuSupplierCriteriaDto() {
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

        public Criteria andSlotSpuSupplierIdIsNull() {
            addCriterion("slot_spu_supplier_id is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdIsNotNull() {
            addCriterion("slot_spu_supplier_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id =", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id <>", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdGreaterThan(Long value) {
            addCriterion("slot_spu_supplier_id >", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdGreaterThanOrEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id >=", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdLessThan(Long value) {
            addCriterion("slot_spu_supplier_id <", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdLessThanOrEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id <=", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdIn(List<Long> values) {
            addCriterion("slot_spu_supplier_id in", values, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotIn(List<Long> values) {
            addCriterion("slot_spu_supplier_id not in", values, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdBetween(Long value1, Long value2) {
            addCriterion("slot_spu_supplier_id between", value1, value2, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotBetween(Long value1, Long value2) {
            addCriterion("slot_spu_supplier_id not between", value1, value2, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIsNull() {
            addCriterion("slot_spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIsNotNull() {
            addCriterion("slot_spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdEqualTo(Long value) {
            addCriterion("slot_spu_id =", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotEqualTo(Long value) {
            addCriterion("slot_spu_id <>", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdGreaterThan(Long value) {
            addCriterion("slot_spu_id >", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("slot_spu_id >=", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdLessThan(Long value) {
            addCriterion("slot_spu_id <", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdLessThanOrEqualTo(Long value) {
            addCriterion("slot_spu_id <=", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIn(List<Long> values) {
            addCriterion("slot_spu_id in", values, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotIn(List<Long> values) {
            addCriterion("slot_spu_id not in", values, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdBetween(Long value1, Long value2) {
            addCriterion("slot_spu_id between", value1, value2, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotBetween(Long value1, Long value2) {
            addCriterion("slot_spu_id not between", value1, value2, "slotSpuId");
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

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Byte value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Byte value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Byte value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Byte value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Byte value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Byte> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Byte> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Byte value1, Byte value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Byte value1, Byte value2) {
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

        public Criteria andRepeatMarkerIsNull() {
            addCriterion("repeat_marker is null");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerIsNotNull() {
            addCriterion("repeat_marker is not null");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerEqualTo(Byte value) {
            addCriterion("repeat_marker =", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerNotEqualTo(Byte value) {
            addCriterion("repeat_marker <>", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerGreaterThan(Byte value) {
            addCriterion("repeat_marker >", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerGreaterThanOrEqualTo(Byte value) {
            addCriterion("repeat_marker >=", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerLessThan(Byte value) {
            addCriterion("repeat_marker <", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerLessThanOrEqualTo(Byte value) {
            addCriterion("repeat_marker <=", value, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerIn(List<Byte> values) {
            addCriterion("repeat_marker in", values, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerNotIn(List<Byte> values) {
            addCriterion("repeat_marker not in", values, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerBetween(Byte value1, Byte value2) {
            addCriterion("repeat_marker between", value1, value2, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andRepeatMarkerNotBetween(Byte value1, Byte value2) {
            addCriterion("repeat_marker not between", value1, value2, "repeatMarker");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignIsNull() {
            addCriterion("supplier_operate_sign is null");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignIsNotNull() {
            addCriterion("supplier_operate_sign is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignEqualTo(Byte value) {
            addCriterion("supplier_operate_sign =", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignNotEqualTo(Byte value) {
            addCriterion("supplier_operate_sign <>", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignGreaterThan(Byte value) {
            addCriterion("supplier_operate_sign >", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignGreaterThanOrEqualTo(Byte value) {
            addCriterion("supplier_operate_sign >=", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignLessThan(Byte value) {
            addCriterion("supplier_operate_sign <", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignLessThanOrEqualTo(Byte value) {
            addCriterion("supplier_operate_sign <=", value, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignIn(List<Byte> values) {
            addCriterion("supplier_operate_sign in", values, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignNotIn(List<Byte> values) {
            addCriterion("supplier_operate_sign not in", values, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignBetween(Byte value1, Byte value2) {
            addCriterion("supplier_operate_sign between", value1, value2, "supplierOperateSign");
            return (Criteria) this;
        }

        public Criteria andSupplierOperateSignNotBetween(Byte value1, Byte value2) {
            addCriterion("supplier_operate_sign not between", value1, value2, "supplierOperateSign");
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