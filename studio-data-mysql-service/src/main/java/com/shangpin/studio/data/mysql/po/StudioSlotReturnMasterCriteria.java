package com.shangpin.studio.data.mysql.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudioSlotReturnMasterCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public StudioSlotReturnMasterCriteria() {
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

        public Criteria andStudioSendNoIsNull() {
            addCriterion("studio_send_no is null");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoIsNotNull() {
            addCriterion("studio_send_no is not null");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoEqualTo(String value) {
            addCriterion("studio_send_no =", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoNotEqualTo(String value) {
            addCriterion("studio_send_no <>", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoGreaterThan(String value) {
            addCriterion("studio_send_no >", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoGreaterThanOrEqualTo(String value) {
            addCriterion("studio_send_no >=", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoLessThan(String value) {
            addCriterion("studio_send_no <", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoLessThanOrEqualTo(String value) {
            addCriterion("studio_send_no <=", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoLike(String value) {
            addCriterion("studio_send_no like", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoNotLike(String value) {
            addCriterion("studio_send_no not like", value, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoIn(List<String> values) {
            addCriterion("studio_send_no in", values, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoNotIn(List<String> values) {
            addCriterion("studio_send_no not in", values, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoBetween(String value1, String value2) {
            addCriterion("studio_send_no between", value1, value2, "studioSendNo");
            return (Criteria) this;
        }

        public Criteria andStudioSendNoNotBetween(String value1, String value2) {
            addCriterion("studio_send_no not between", value1, value2, "studioSendNo");
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

        public Criteria andQuantityIsNull() {
            addCriterion("quantity is null");
            return (Criteria) this;
        }

        public Criteria andQuantityIsNotNull() {
            addCriterion("quantity is not null");
            return (Criteria) this;
        }

        public Criteria andQuantityEqualTo(Integer value) {
            addCriterion("quantity =", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotEqualTo(Integer value) {
            addCriterion("quantity <>", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThan(Integer value) {
            addCriterion("quantity >", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("quantity >=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThan(Integer value) {
            addCriterion("quantity <", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("quantity <=", value, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityIn(List<Integer> values) {
            addCriterion("quantity in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotIn(List<Integer> values) {
            addCriterion("quantity not in", values, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityBetween(Integer value1, Integer value2) {
            addCriterion("quantity between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("quantity not between", value1, value2, "quantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityIsNull() {
            addCriterion("actual_quantity is null");
            return (Criteria) this;
        }

        public Criteria andActualQuantityIsNotNull() {
            addCriterion("actual_quantity is not null");
            return (Criteria) this;
        }

        public Criteria andActualQuantityEqualTo(Integer value) {
            addCriterion("actual_quantity =", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityNotEqualTo(Integer value) {
            addCriterion("actual_quantity <>", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityGreaterThan(Integer value) {
            addCriterion("actual_quantity >", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityGreaterThanOrEqualTo(Integer value) {
            addCriterion("actual_quantity >=", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityLessThan(Integer value) {
            addCriterion("actual_quantity <", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityLessThanOrEqualTo(Integer value) {
            addCriterion("actual_quantity <=", value, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityIn(List<Integer> values) {
            addCriterion("actual_quantity in", values, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityNotIn(List<Integer> values) {
            addCriterion("actual_quantity not in", values, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityBetween(Integer value1, Integer value2) {
            addCriterion("actual_quantity between", value1, value2, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andActualQuantityNotBetween(Integer value1, Integer value2) {
            addCriterion("actual_quantity not between", value1, value2, "actualQuantity");
            return (Criteria) this;
        }

        public Criteria andTrackNoIsNull() {
            addCriterion("track_no is null");
            return (Criteria) this;
        }

        public Criteria andTrackNoIsNotNull() {
            addCriterion("track_no is not null");
            return (Criteria) this;
        }

        public Criteria andTrackNoEqualTo(String value) {
            addCriterion("track_no =", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoNotEqualTo(String value) {
            addCriterion("track_no <>", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoGreaterThan(String value) {
            addCriterion("track_no >", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoGreaterThanOrEqualTo(String value) {
            addCriterion("track_no >=", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoLessThan(String value) {
            addCriterion("track_no <", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoLessThanOrEqualTo(String value) {
            addCriterion("track_no <=", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoLike(String value) {
            addCriterion("track_no like", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoNotLike(String value) {
            addCriterion("track_no not like", value, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoIn(List<String> values) {
            addCriterion("track_no in", values, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoNotIn(List<String> values) {
            addCriterion("track_no not in", values, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoBetween(String value1, String value2) {
            addCriterion("track_no between", value1, value2, "trackNo");
            return (Criteria) this;
        }

        public Criteria andTrackNoNotBetween(String value1, String value2) {
            addCriterion("track_no not between", value1, value2, "trackNo");
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