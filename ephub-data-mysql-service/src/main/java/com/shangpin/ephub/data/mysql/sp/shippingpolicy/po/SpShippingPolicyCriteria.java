package com.shangpin.ephub.data.mysql.sp.shippingpolicy.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpShippingPolicyCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public SpShippingPolicyCriteria() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPolicyNameIsNull() {
            addCriterion("policy_name is null");
            return (Criteria) this;
        }

        public Criteria andPolicyNameIsNotNull() {
            addCriterion("policy_name is not null");
            return (Criteria) this;
        }

        public Criteria andPolicyNameEqualTo(String value) {
            addCriterion("policy_name =", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameNotEqualTo(String value) {
            addCriterion("policy_name <>", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameGreaterThan(String value) {
            addCriterion("policy_name >", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameGreaterThanOrEqualTo(String value) {
            addCriterion("policy_name >=", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameLessThan(String value) {
            addCriterion("policy_name <", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameLessThanOrEqualTo(String value) {
            addCriterion("policy_name <=", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameLike(String value) {
            addCriterion("policy_name like", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameNotLike(String value) {
            addCriterion("policy_name not like", value, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameIn(List<String> values) {
            addCriterion("policy_name in", values, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameNotIn(List<String> values) {
            addCriterion("policy_name not in", values, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameBetween(String value1, String value2) {
            addCriterion("policy_name between", value1, value2, "policyName");
            return (Criteria) this;
        }

        public Criteria andPolicyNameNotBetween(String value1, String value2) {
            addCriterion("policy_name not between", value1, value2, "policyName");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andDayNumIsNull() {
            addCriterion("day_num is null");
            return (Criteria) this;
        }

        public Criteria andDayNumIsNotNull() {
            addCriterion("day_num is not null");
            return (Criteria) this;
        }

        public Criteria andDayNumEqualTo(Short value) {
            addCriterion("day_num =", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumNotEqualTo(Short value) {
            addCriterion("day_num <>", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumGreaterThan(Short value) {
            addCriterion("day_num >", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumGreaterThanOrEqualTo(Short value) {
            addCriterion("day_num >=", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumLessThan(Short value) {
            addCriterion("day_num <", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumLessThanOrEqualTo(Short value) {
            addCriterion("day_num <=", value, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumIn(List<Short> values) {
            addCriterion("day_num in", values, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumNotIn(List<Short> values) {
            addCriterion("day_num not in", values, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumBetween(Short value1, Short value2) {
            addCriterion("day_num between", value1, value2, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDayNumNotBetween(Short value1, Short value2) {
            addCriterion("day_num not between", value1, value2, "dayNum");
            return (Criteria) this;
        }

        public Criteria andDateTypeIsNull() {
            addCriterion("date_type is null");
            return (Criteria) this;
        }

        public Criteria andDateTypeIsNotNull() {
            addCriterion("date_type is not null");
            return (Criteria) this;
        }

        public Criteria andDateTypeEqualTo(Byte value) {
            addCriterion("date_type =", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeNotEqualTo(Byte value) {
            addCriterion("date_type <>", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeGreaterThan(Byte value) {
            addCriterion("date_type >", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("date_type >=", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeLessThan(Byte value) {
            addCriterion("date_type <", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeLessThanOrEqualTo(Byte value) {
            addCriterion("date_type <=", value, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeIn(List<Byte> values) {
            addCriterion("date_type in", values, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeNotIn(List<Byte> values) {
            addCriterion("date_type not in", values, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeBetween(Byte value1, Byte value2) {
            addCriterion("date_type between", value1, value2, "dateType");
            return (Criteria) this;
        }

        public Criteria andDateTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("date_type not between", value1, value2, "dateType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeIsNull() {
            addCriterion("policy_type is null");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeIsNotNull() {
            addCriterion("policy_type is not null");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeEqualTo(Byte value) {
            addCriterion("policy_type =", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeNotEqualTo(Byte value) {
            addCriterion("policy_type <>", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeGreaterThan(Byte value) {
            addCriterion("policy_type >", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("policy_type >=", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeLessThan(Byte value) {
            addCriterion("policy_type <", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeLessThanOrEqualTo(Byte value) {
            addCriterion("policy_type <=", value, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeIn(List<Byte> values) {
            addCriterion("policy_type in", values, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeNotIn(List<Byte> values) {
            addCriterion("policy_type not in", values, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeBetween(Byte value1, Byte value2) {
            addCriterion("policy_type between", value1, value2, "policyType");
            return (Criteria) this;
        }

        public Criteria andPolicyTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("policy_type not between", value1, value2, "policyType");
            return (Criteria) this;
        }

        public Criteria andRegularMembersIsNull() {
            addCriterion("regular_members is null");
            return (Criteria) this;
        }

        public Criteria andRegularMembersIsNotNull() {
            addCriterion("regular_members is not null");
            return (Criteria) this;
        }

        public Criteria andRegularMembersEqualTo(Byte value) {
            addCriterion("regular_members =", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersNotEqualTo(Byte value) {
            addCriterion("regular_members <>", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersGreaterThan(Byte value) {
            addCriterion("regular_members >", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersGreaterThanOrEqualTo(Byte value) {
            addCriterion("regular_members >=", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersLessThan(Byte value) {
            addCriterion("regular_members <", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersLessThanOrEqualTo(Byte value) {
            addCriterion("regular_members <=", value, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersIn(List<Byte> values) {
            addCriterion("regular_members in", values, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersNotIn(List<Byte> values) {
            addCriterion("regular_members not in", values, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersBetween(Byte value1, Byte value2) {
            addCriterion("regular_members between", value1, value2, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andRegularMembersNotBetween(Byte value1, Byte value2) {
            addCriterion("regular_members not between", value1, value2, "regularMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersIsNull() {
            addCriterion("gold_members is null");
            return (Criteria) this;
        }

        public Criteria andGoldMembersIsNotNull() {
            addCriterion("gold_members is not null");
            return (Criteria) this;
        }

        public Criteria andGoldMembersEqualTo(Byte value) {
            addCriterion("gold_members =", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersNotEqualTo(Byte value) {
            addCriterion("gold_members <>", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersGreaterThan(Byte value) {
            addCriterion("gold_members >", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersGreaterThanOrEqualTo(Byte value) {
            addCriterion("gold_members >=", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersLessThan(Byte value) {
            addCriterion("gold_members <", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersLessThanOrEqualTo(Byte value) {
            addCriterion("gold_members <=", value, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersIn(List<Byte> values) {
            addCriterion("gold_members in", values, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersNotIn(List<Byte> values) {
            addCriterion("gold_members not in", values, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersBetween(Byte value1, Byte value2) {
            addCriterion("gold_members between", value1, value2, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andGoldMembersNotBetween(Byte value1, Byte value2) {
            addCriterion("gold_members not between", value1, value2, "goldMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersIsNull() {
            addCriterion("supreme_members is null");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersIsNotNull() {
            addCriterion("supreme_members is not null");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersEqualTo(Byte value) {
            addCriterion("supreme_members =", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersNotEqualTo(Byte value) {
            addCriterion("supreme_members <>", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersGreaterThan(Byte value) {
            addCriterion("supreme_members >", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersGreaterThanOrEqualTo(Byte value) {
            addCriterion("supreme_members >=", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersLessThan(Byte value) {
            addCriterion("supreme_members <", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersLessThanOrEqualTo(Byte value) {
            addCriterion("supreme_members <=", value, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersIn(List<Byte> values) {
            addCriterion("supreme_members in", values, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersNotIn(List<Byte> values) {
            addCriterion("supreme_members not in", values, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersBetween(Byte value1, Byte value2) {
            addCriterion("supreme_members between", value1, value2, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andSupremeMembersNotBetween(Byte value1, Byte value2) {
            addCriterion("supreme_members not between", value1, value2, "supremeMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersIsNull() {
            addCriterion("gold_plus_members is null");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersIsNotNull() {
            addCriterion("gold_plus_members is not null");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersEqualTo(Byte value) {
            addCriterion("gold_plus_members =", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersNotEqualTo(Byte value) {
            addCriterion("gold_plus_members <>", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersGreaterThan(Byte value) {
            addCriterion("gold_plus_members >", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersGreaterThanOrEqualTo(Byte value) {
            addCriterion("gold_plus_members >=", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersLessThan(Byte value) {
            addCriterion("gold_plus_members <", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersLessThanOrEqualTo(Byte value) {
            addCriterion("gold_plus_members <=", value, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersIn(List<Byte> values) {
            addCriterion("gold_plus_members in", values, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersNotIn(List<Byte> values) {
            addCriterion("gold_plus_members not in", values, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersBetween(Byte value1, Byte value2) {
            addCriterion("gold_plus_members between", value1, value2, "goldPlusMembers");
            return (Criteria) this;
        }

        public Criteria andGoldPlusMembersNotBetween(Byte value1, Byte value2) {
            addCriterion("gold_plus_members not between", value1, value2, "goldPlusMembers");
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