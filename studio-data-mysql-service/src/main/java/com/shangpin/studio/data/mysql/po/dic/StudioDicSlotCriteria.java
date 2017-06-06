package com.shangpin.studio.data.mysql.po.dic;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudioDicSlotCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public StudioDicSlotCriteria() {
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

        public Criteria andStudioDicSlotIdIsNull() {
            addCriterion("studio_dic_slot_id is null");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdIsNotNull() {
            addCriterion("studio_dic_slot_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdEqualTo(Long value) {
            addCriterion("studio_dic_slot_id =", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdNotEqualTo(Long value) {
            addCriterion("studio_dic_slot_id <>", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdGreaterThan(Long value) {
            addCriterion("studio_dic_slot_id >", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdGreaterThanOrEqualTo(Long value) {
            addCriterion("studio_dic_slot_id >=", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdLessThan(Long value) {
            addCriterion("studio_dic_slot_id <", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdLessThanOrEqualTo(Long value) {
            addCriterion("studio_dic_slot_id <=", value, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdIn(List<Long> values) {
            addCriterion("studio_dic_slot_id in", values, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdNotIn(List<Long> values) {
            addCriterion("studio_dic_slot_id not in", values, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdBetween(Long value1, Long value2) {
            addCriterion("studio_dic_slot_id between", value1, value2, "studioDicSlotId");
            return (Criteria) this;
        }

        public Criteria andStudioDicSlotIdNotBetween(Long value1, Long value2) {
            addCriterion("studio_dic_slot_id not between", value1, value2, "studioDicSlotId");
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

        public Criteria andSlotNumberIsNull() {
            addCriterion("slot_number is null");
            return (Criteria) this;
        }

        public Criteria andSlotNumberIsNotNull() {
            addCriterion("slot_number is not null");
            return (Criteria) this;
        }

        public Criteria andSlotNumberEqualTo(Integer value) {
            addCriterion("slot_number =", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberNotEqualTo(Integer value) {
            addCriterion("slot_number <>", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberGreaterThan(Integer value) {
            addCriterion("slot_number >", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("slot_number >=", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberLessThan(Integer value) {
            addCriterion("slot_number <", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberLessThanOrEqualTo(Integer value) {
            addCriterion("slot_number <=", value, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberIn(List<Integer> values) {
            addCriterion("slot_number in", values, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberNotIn(List<Integer> values) {
            addCriterion("slot_number not in", values, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberBetween(Integer value1, Integer value2) {
            addCriterion("slot_number between", value1, value2, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("slot_number not between", value1, value2, "slotNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberIsNull() {
            addCriterion("slot_min_number is null");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberIsNotNull() {
            addCriterion("slot_min_number is not null");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberEqualTo(Integer value) {
            addCriterion("slot_min_number =", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberNotEqualTo(Integer value) {
            addCriterion("slot_min_number <>", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberGreaterThan(Integer value) {
            addCriterion("slot_min_number >", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberGreaterThanOrEqualTo(Integer value) {
            addCriterion("slot_min_number >=", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberLessThan(Integer value) {
            addCriterion("slot_min_number <", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberLessThanOrEqualTo(Integer value) {
            addCriterion("slot_min_number <=", value, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberIn(List<Integer> values) {
            addCriterion("slot_min_number in", values, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberNotIn(List<Integer> values) {
            addCriterion("slot_min_number not in", values, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberBetween(Integer value1, Integer value2) {
            addCriterion("slot_min_number between", value1, value2, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotMinNumberNotBetween(Integer value1, Integer value2) {
            addCriterion("slot_min_number not between", value1, value2, "slotMinNumber");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyIsNull() {
            addCriterion("slot_efficiency is null");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyIsNotNull() {
            addCriterion("slot_efficiency is not null");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyEqualTo(Integer value) {
            addCriterion("slot_efficiency =", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyNotEqualTo(Integer value) {
            addCriterion("slot_efficiency <>", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyGreaterThan(Integer value) {
            addCriterion("slot_efficiency >", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyGreaterThanOrEqualTo(Integer value) {
            addCriterion("slot_efficiency >=", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyLessThan(Integer value) {
            addCriterion("slot_efficiency <", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyLessThanOrEqualTo(Integer value) {
            addCriterion("slot_efficiency <=", value, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyIn(List<Integer> values) {
            addCriterion("slot_efficiency in", values, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyNotIn(List<Integer> values) {
            addCriterion("slot_efficiency not in", values, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyBetween(Integer value1, Integer value2) {
            addCriterion("slot_efficiency between", value1, value2, "slotEfficiency");
            return (Criteria) this;
        }

        public Criteria andSlotEfficiencyNotBetween(Integer value1, Integer value2) {
            addCriterion("slot_efficiency not between", value1, value2, "slotEfficiency");
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