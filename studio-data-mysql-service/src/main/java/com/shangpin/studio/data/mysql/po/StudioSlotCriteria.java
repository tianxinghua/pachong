package com.shangpin.studio.data.mysql.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StudioSlotCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public StudioSlotCriteria() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andStudioSlotIdIsNull() {
            addCriterion("studio_slot_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdIsNotNull() {
            addCriterion("studio_slot_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdEqualTo(Long value) {
            addCriterion("studio_slot_id =", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdNotEqualTo(Long value) {
            addCriterion("studio_slot_id <>", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdGreaterThan(Long value) {
            addCriterion("studio_slot_id >", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_slot_id >=", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdLessThan(Long value) {
            addCriterion("studio_slot_id <", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_slot_id <=", value, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdIn(List<Long> values) {
            addCriterion("studio_slot_id in", values, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdNotIn(List<Long> values) {
            addCriterion("studio_slot_id not in", values, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdBetween(Long value1, Long value2) {
            addCriterion("studio_slot_id between", value1, value2, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_slot_id not between", value1, value2, "studioSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioIdIsNull() {
            addCriterion("studio_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioIdIsNotNull() {
            addCriterion("studio_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioIdEqualTo(Long value) {
            addCriterion("studio_id =", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdNotEqualTo(Long value) {
            addCriterion("studio_id <>", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdGreaterThan(Long value) {
            addCriterion("studio_id >", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_id >=", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdLessThan(Long value) {
            addCriterion("studio_id <", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_id <=", value, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdIn(List<Long> values) {
            addCriterion("studio_id in", values, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdNotIn(List<Long> values) {
            addCriterion("studio_id not in", values, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdBetween(Long value1, Long value2) {
            addCriterion("studio_id between", value1, value2, "studioId");
            return (Criteria) this;
        }

        public Criteria andStudioIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_id not between", value1, value2, "studioId");
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

        public Criteria andSlotStatusIsNull() {
            addCriterion("slot_status is null");
            return (Criteria) this;
        }

        public Criteria andSlotStatusIsNotNull() {
            addCriterion("slot_status is not null");
            return (Criteria) this;
        }

        public Criteria andSlotStatusEqualTo(Byte value) {
            addCriterion("slot_status =", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusNotEqualTo(Byte value) {
            addCriterion("slot_status <>", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusGreaterThan(Byte value) {
            addCriterion("slot_status >", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("slot_status >=", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusLessThan(Byte value) {
            addCriterion("slot_status <", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusLessThanOrEqualTo(Byte value) {
            addCriterion("slot_status <=", value, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusIn(List<Byte> values) {
            addCriterion("slot_status in", values, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusNotIn(List<Byte> values) {
            addCriterion("slot_status not in", values, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusBetween(Byte value1, Byte value2) {
            addCriterion("slot_status between", value1, value2, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andSlotStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("slot_status not between", value1, value2, "slotStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusIsNull() {
            addCriterion("apply_status is null");
            return (Criteria) this;
        }

        public Criteria andApplyStatusIsNotNull() {
            addCriterion("apply_status is not null");
            return (Criteria) this;
        }

        public Criteria andApplyStatusEqualTo(Byte value) {
            addCriterion("apply_status =", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusNotEqualTo(Byte value) {
            addCriterion("apply_status <>", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusGreaterThan(Byte value) {
            addCriterion("apply_status >", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("apply_status >=", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusLessThan(Byte value) {
            addCriterion("apply_status <", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusLessThanOrEqualTo(Byte value) {
            addCriterion("apply_status <=", value, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusIn(List<Byte> values) {
            addCriterion("apply_status in", values, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusNotIn(List<Byte> values) {
            addCriterion("apply_status not in", values, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusBetween(Byte value1, Byte value2) {
            addCriterion("apply_status between", value1, value2, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplyStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("apply_status not between", value1, value2, "applyStatus");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdIsNull() {
            addCriterion("apply_supplier_id is null");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdIsNotNull() {
            addCriterion("apply_supplier_id is not null");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdEqualTo(String value) {
            addCriterion("apply_supplier_id =", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdNotEqualTo(String value) {
            addCriterion("apply_supplier_id <>", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdGreaterThan(String value) {
            addCriterion("apply_supplier_id >", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdGreaterThanOrEqualTo(String value) {
            addCriterion("apply_supplier_id >=", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdLessThan(String value) {
            addCriterion("apply_supplier_id <", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdLessThanOrEqualTo(String value) {
            addCriterion("apply_supplier_id <=", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdLike(String value) {
            addCriterion("apply_supplier_id like", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdNotLike(String value) {
            addCriterion("apply_supplier_id not like", value, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdIn(List<String> values) {
            addCriterion("apply_supplier_id in", values, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdNotIn(List<String> values) {
            addCriterion("apply_supplier_id not in", values, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdBetween(String value1, String value2) {
            addCriterion("apply_supplier_id between", value1, value2, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplySupplierIdNotBetween(String value1, String value2) {
            addCriterion("apply_supplier_id not between", value1, value2, "applySupplierId");
            return (Criteria) this;
        }

        public Criteria andApplyUserIsNull() {
            addCriterion("apply_user is null");
            return (Criteria) this;
        }

        public Criteria andApplyUserIsNotNull() {
            addCriterion("apply_user is not null");
            return (Criteria) this;
        }

        public Criteria andApplyUserEqualTo(String value) {
            addCriterion("apply_user =", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotEqualTo(String value) {
            addCriterion("apply_user <>", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserGreaterThan(String value) {
            addCriterion("apply_user >", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserGreaterThanOrEqualTo(String value) {
            addCriterion("apply_user >=", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLessThan(String value) {
            addCriterion("apply_user <", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLessThanOrEqualTo(String value) {
            addCriterion("apply_user <=", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserLike(String value) {
            addCriterion("apply_user like", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotLike(String value) {
            addCriterion("apply_user not like", value, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserIn(List<String> values) {
            addCriterion("apply_user in", values, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotIn(List<String> values) {
            addCriterion("apply_user not in", values, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserBetween(String value1, String value2) {
            addCriterion("apply_user between", value1, value2, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyUserNotBetween(String value1, String value2) {
            addCriterion("apply_user not between", value1, value2, "applyUser");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNull() {
            addCriterion("apply_time is null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIsNotNull() {
            addCriterion("apply_time is not null");
            return (Criteria) this;
        }

        public Criteria andApplyTimeEqualTo(Date value) {
            addCriterion("apply_time =", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotEqualTo(Date value) {
            addCriterion("apply_time <>", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThan(Date value) {
            addCriterion("apply_time >", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("apply_time >=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThan(Date value) {
            addCriterion("apply_time <", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeLessThanOrEqualTo(Date value) {
            addCriterion("apply_time <=", value, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeIn(List<Date> values) {
            addCriterion("apply_time in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotIn(List<Date> values) {
            addCriterion("apply_time not in", values, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeBetween(Date value1, Date value2) {
            addCriterion("apply_time between", value1, value2, "applyTime");
            return (Criteria) this;
        }

        public Criteria andApplyTimeNotBetween(Date value1, Date value2) {
            addCriterion("apply_time not between", value1, value2, "applyTime");
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

        public Criteria andPlanArriveTimeIsNull() {
            addCriterion("plan_arrive_time is null");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeIsNotNull() {
            addCriterion("plan_arrive_time is not null");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeEqualTo(Date value) {
            addCriterionForJDBCDate("plan_arrive_time =", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeNotEqualTo(Date value) {
            addCriterionForJDBCDate("plan_arrive_time <>", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeGreaterThan(Date value) {
            addCriterionForJDBCDate("plan_arrive_time >", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("plan_arrive_time >=", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeLessThan(Date value) {
            addCriterionForJDBCDate("plan_arrive_time <", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("plan_arrive_time <=", value, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeIn(List<Date> values) {
            addCriterionForJDBCDate("plan_arrive_time in", values, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeNotIn(List<Date> values) {
            addCriterionForJDBCDate("plan_arrive_time not in", values, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("plan_arrive_time between", value1, value2, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andPlanArriveTimeNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("plan_arrive_time not between", value1, value2, "planArriveTime");
            return (Criteria) this;
        }

        public Criteria andArriveStatusIsNull() {
            addCriterion("arrive_status is null");
            return (Criteria) this;
        }

        public Criteria andArriveStatusIsNotNull() {
            addCriterion("arrive_status is not null");
            return (Criteria) this;
        }

        public Criteria andArriveStatusEqualTo(Byte value) {
            addCriterion("arrive_status =", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusNotEqualTo(Byte value) {
            addCriterion("arrive_status <>", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusGreaterThan(Byte value) {
            addCriterion("arrive_status >", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("arrive_status >=", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusLessThan(Byte value) {
            addCriterion("arrive_status <", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusLessThanOrEqualTo(Byte value) {
            addCriterion("arrive_status <=", value, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusIn(List<Byte> values) {
            addCriterion("arrive_status in", values, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusNotIn(List<Byte> values) {
            addCriterion("arrive_status not in", values, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusBetween(Byte value1, Byte value2) {
            addCriterion("arrive_status between", value1, value2, "arriveStatus");
            return (Criteria) this;
        }

        public Criteria andArriveStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("arrive_status not between", value1, value2, "arriveStatus");
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

        public Criteria andShotStatusIsNull() {
            addCriterion("shot_status is null");
            return (Criteria) this;
        }

        public Criteria andShotStatusIsNotNull() {
            addCriterion("shot_status is not null");
            return (Criteria) this;
        }

        public Criteria andShotStatusEqualTo(Byte value) {
            addCriterion("shot_status =", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusNotEqualTo(Byte value) {
            addCriterion("shot_status <>", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusGreaterThan(Byte value) {
            addCriterion("shot_status >", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("shot_status >=", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusLessThan(Byte value) {
            addCriterion("shot_status <", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusLessThanOrEqualTo(Byte value) {
            addCriterion("shot_status <=", value, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusIn(List<Byte> values) {
            addCriterion("shot_status in", values, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusNotIn(List<Byte> values) {
            addCriterion("shot_status not in", values, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusBetween(Byte value1, Byte value2) {
            addCriterion("shot_status between", value1, value2, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andShotStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("shot_status not between", value1, value2, "shotStatus");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeIsNull() {
            addCriterion("plan_shot_time is null");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeIsNotNull() {
            addCriterion("plan_shot_time is not null");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeEqualTo(Date value) {
            addCriterion("plan_shot_time =", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeNotEqualTo(Date value) {
            addCriterion("plan_shot_time <>", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeGreaterThan(Date value) {
            addCriterion("plan_shot_time >", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("plan_shot_time >=", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeLessThan(Date value) {
            addCriterion("plan_shot_time <", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeLessThanOrEqualTo(Date value) {
            addCriterion("plan_shot_time <=", value, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeIn(List<Date> values) {
            addCriterion("plan_shot_time in", values, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeNotIn(List<Date> values) {
            addCriterion("plan_shot_time not in", values, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeBetween(Date value1, Date value2) {
            addCriterion("plan_shot_time between", value1, value2, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andPlanShotTimeNotBetween(Date value1, Date value2) {
            addCriterion("plan_shot_time not between", value1, value2, "planShotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeIsNull() {
            addCriterion("shot_time is null");
            return (Criteria) this;
        }

        public Criteria andShotTimeIsNotNull() {
            addCriterion("shot_time is not null");
            return (Criteria) this;
        }

        public Criteria andShotTimeEqualTo(Date value) {
            addCriterion("shot_time =", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeNotEqualTo(Date value) {
            addCriterion("shot_time <>", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeGreaterThan(Date value) {
            addCriterion("shot_time >", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("shot_time >=", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeLessThan(Date value) {
            addCriterion("shot_time <", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeLessThanOrEqualTo(Date value) {
            addCriterion("shot_time <=", value, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeIn(List<Date> values) {
            addCriterion("shot_time in", values, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeNotIn(List<Date> values) {
            addCriterion("shot_time not in", values, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeBetween(Date value1, Date value2) {
            addCriterion("shot_time between", value1, value2, "shotTime");
            return (Criteria) this;
        }

        public Criteria andShotTimeNotBetween(Date value1, Date value2) {
            addCriterion("shot_time not between", value1, value2, "shotTime");
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