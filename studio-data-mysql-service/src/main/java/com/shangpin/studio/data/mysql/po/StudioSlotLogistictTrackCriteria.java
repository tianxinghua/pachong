package com.shangpin.studio.data.mysql.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StudioSlotLogistictTrackCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public StudioSlotLogistictTrackCriteria() {
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

        public Criteria andStudioSlotLogistictTrackIdIsNull() {
            addCriterion("studio_slot_logistict_track_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdIsNotNull() {
            addCriterion("studio_slot_logistict_track_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdEqualTo(Long value) {
            addCriterion("studio_slot_logistict_track_id =", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdNotEqualTo(Long value) {
            addCriterion("studio_slot_logistict_track_id <>", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdGreaterThan(Long value) {
            addCriterion("studio_slot_logistict_track_id >", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_slot_logistict_track_id >=", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdLessThan(Long value) {
            addCriterion("studio_slot_logistict_track_id <", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_slot_logistict_track_id <=", value, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdIn(List<Long> values) {
            addCriterion("studio_slot_logistict_track_id in", values, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdNotIn(List<Long> values) {
            addCriterion("studio_slot_logistict_track_id not in", values, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdBetween(Long value1, Long value2) {
            addCriterion("studio_slot_logistict_track_id between", value1, value2, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andStudioSlotLogistictTrackIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_slot_logistict_track_id not between", value1, value2, "studioSlotLogistictTrackId");
            return (Criteria) this;
        }

        public Criteria andTrackNameIsNull() {
            addCriterion("track_name is null");
            return (Criteria) this;
        }

        public Criteria andTrackNameIsNotNull() {
            addCriterion("track_name is not null");
            return (Criteria) this;
        }

        public Criteria andTrackNameEqualTo(String value) {
            addCriterion("track_name =", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameNotEqualTo(String value) {
            addCriterion("track_name <>", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameGreaterThan(String value) {
            addCriterion("track_name >", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameGreaterThanOrEqualTo(String value) {
            addCriterion("track_name >=", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameLessThan(String value) {
            addCriterion("track_name <", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameLessThanOrEqualTo(String value) {
            addCriterion("track_name <=", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameLike(String value) {
            addCriterion("track_name like", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameNotLike(String value) {
            addCriterion("track_name not like", value, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameIn(List<String> values) {
            addCriterion("track_name in", values, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameNotIn(List<String> values) {
            addCriterion("track_name not in", values, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameBetween(String value1, String value2) {
            addCriterion("track_name between", value1, value2, "trackName");
            return (Criteria) this;
        }

        public Criteria andTrackNameNotBetween(String value1, String value2) {
            addCriterion("track_name not between", value1, value2, "trackName");
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

        public Criteria andTrackStatusIsNull() {
            addCriterion("track_status is null");
            return (Criteria) this;
        }

        public Criteria andTrackStatusIsNotNull() {
            addCriterion("track_status is not null");
            return (Criteria) this;
        }

        public Criteria andTrackStatusEqualTo(Byte value) {
            addCriterion("track_status =", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusNotEqualTo(Byte value) {
            addCriterion("track_status <>", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusGreaterThan(Byte value) {
            addCriterion("track_status >", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("track_status >=", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusLessThan(Byte value) {
            addCriterion("track_status <", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusLessThanOrEqualTo(Byte value) {
            addCriterion("track_status <=", value, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusIn(List<Byte> values) {
            addCriterion("track_status in", values, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusNotIn(List<Byte> values) {
            addCriterion("track_status not in", values, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusBetween(Byte value1, Byte value2) {
            addCriterion("track_status between", value1, value2, "trackStatus");
            return (Criteria) this;
        }

        public Criteria andTrackStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("track_status not between", value1, value2, "trackStatus");
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

        public Criteria andActualNumberIsNull() {
            addCriterion("actual_number is null");
            return (Criteria) this;
        }

        public Criteria andActualNumberIsNotNull() {
            addCriterion("actual_number is not null");
            return (Criteria) this;
        }

        public Criteria andActualNumberEqualTo(Integer value) {
            addCriterion("actual_number =", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberNotEqualTo(Integer value) {
            addCriterion("actual_number <>", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberGreaterThan(Integer value) {
            addCriterion("actual_number >", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("actual_number >=", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberLessThan(Integer value) {
            addCriterion("actual_number <", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberLessThanOrEqualTo(Integer value) {
            addCriterion("actual_number <=", value, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberIn(List<Integer> values) {
            addCriterion("actual_number in", values, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberNotIn(List<Integer> values) {
            addCriterion("actual_number not in", values, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberBetween(Integer value1, Integer value2) {
            addCriterion("actual_number between", value1, value2, "actualNumber");
            return (Criteria) this;
        }

        public Criteria andActualNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("actual_number not between", value1, value2, "actualNumber");
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

        public Criteria andTrackReceiverIsNull() {
            addCriterion("track_receiver is null");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverIsNotNull() {
            addCriterion("track_receiver is not null");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverEqualTo(String value) {
            addCriterion("track_receiver =", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverNotEqualTo(String value) {
            addCriterion("track_receiver <>", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverGreaterThan(String value) {
            addCriterion("track_receiver >", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverGreaterThanOrEqualTo(String value) {
            addCriterion("track_receiver >=", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverLessThan(String value) {
            addCriterion("track_receiver <", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverLessThanOrEqualTo(String value) {
            addCriterion("track_receiver <=", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverLike(String value) {
            addCriterion("track_receiver like", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverNotLike(String value) {
            addCriterion("track_receiver not like", value, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverIn(List<String> values) {
            addCriterion("track_receiver in", values, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverNotIn(List<String> values) {
            addCriterion("track_receiver not in", values, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverBetween(String value1, String value2) {
            addCriterion("track_receiver between", value1, value2, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTrackReceiverNotBetween(String value1, String value2) {
            addCriterion("track_receiver not between", value1, value2, "trackReceiver");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdIsNull() {
            addCriterion("send_master_id is null");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdIsNotNull() {
            addCriterion("send_master_id is not null");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdEqualTo(Long value) {
            addCriterion("send_master_id =", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdNotEqualTo(Long value) {
            addCriterion("send_master_id <>", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdGreaterThan(Long value) {
            addCriterion("send_master_id >", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdGreaterThanOrEqualTo(Long value) {
            addCriterion("send_master_id >=", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdLessThan(Long value) {
            addCriterion("send_master_id <", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdLessThanOrEqualTo(Long value) {
            addCriterion("send_master_id <=", value, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdIn(List<Long> values) {
            addCriterion("send_master_id in", values, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdNotIn(List<Long> values) {
            addCriterion("send_master_id not in", values, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdBetween(Long value1, Long value2) {
            addCriterion("send_master_id between", value1, value2, "sendMasterId");
            return (Criteria) this;
        }

        public Criteria andSendMasterIdNotBetween(Long value1, Long value2) {
            addCriterion("send_master_id not between", value1, value2, "sendMasterId");
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