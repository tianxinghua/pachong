package com.shangpin.ephub.client.data.mysql.rule.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubBrandModelRuleCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubBrandModelRuleCriteriaDto() {
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

        public Criteria andBrandModelRuleIdIsNull() {
            addCriterion("brand_model_rule_id is null");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdIsNotNull() {
            addCriterion("brand_model_rule_id is not null");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdEqualTo(Long value) {
            addCriterion("brand_model_rule_id =", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdNotEqualTo(Long value) {
            addCriterion("brand_model_rule_id <>", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdGreaterThan(Long value) {
            addCriterion("brand_model_rule_id >", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("brand_model_rule_id >=", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdLessThan(Long value) {
            addCriterion("brand_model_rule_id <", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdLessThanOrEqualTo(Long value) {
            addCriterion("brand_model_rule_id <=", value, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdIn(List<Long> values) {
            addCriterion("brand_model_rule_id in", values, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdNotIn(List<Long> values) {
            addCriterion("brand_model_rule_id not in", values, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdBetween(Long value1, Long value2) {
            addCriterion("brand_model_rule_id between", value1, value2, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andBrandModelRuleIdNotBetween(Long value1, Long value2) {
            addCriterion("brand_model_rule_id not between", value1, value2, "brandModelRuleId");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIsNull() {
            addCriterion("hub_brand_no is null");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIsNotNull() {
            addCriterion("hub_brand_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoEqualTo(String value) {
            addCriterion("hub_brand_no =", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotEqualTo(String value) {
            addCriterion("hub_brand_no <>", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoGreaterThan(String value) {
            addCriterion("hub_brand_no >", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_brand_no >=", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLessThan(String value) {
            addCriterion("hub_brand_no <", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLessThanOrEqualTo(String value) {
            addCriterion("hub_brand_no <=", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoLike(String value) {
            addCriterion("hub_brand_no like", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotLike(String value) {
            addCriterion("hub_brand_no not like", value, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoIn(List<String> values) {
            addCriterion("hub_brand_no in", values, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotIn(List<String> values) {
            addCriterion("hub_brand_no not in", values, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoBetween(String value1, String value2) {
            addCriterion("hub_brand_no between", value1, value2, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andHubBrandNoNotBetween(String value1, String value2) {
            addCriterion("hub_brand_no not between", value1, value2, "hubBrandNo");
            return (Criteria) this;
        }

        public Criteria andModelRuleIsNull() {
            addCriterion("model_rule is null");
            return (Criteria) this;
        }

        public Criteria andModelRuleIsNotNull() {
            addCriterion("model_rule is not null");
            return (Criteria) this;
        }

        public Criteria andModelRuleEqualTo(String value) {
            addCriterion("model_rule =", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleNotEqualTo(String value) {
            addCriterion("model_rule <>", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleGreaterThan(String value) {
            addCriterion("model_rule >", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleGreaterThanOrEqualTo(String value) {
            addCriterion("model_rule >=", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleLessThan(String value) {
            addCriterion("model_rule <", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleLessThanOrEqualTo(String value) {
            addCriterion("model_rule <=", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleLike(String value) {
            addCriterion("model_rule like", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleNotLike(String value) {
            addCriterion("model_rule not like", value, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleIn(List<String> values) {
            addCriterion("model_rule in", values, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleNotIn(List<String> values) {
            addCriterion("model_rule not in", values, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleBetween(String value1, String value2) {
            addCriterion("model_rule between", value1, value2, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRuleNotBetween(String value1, String value2) {
            addCriterion("model_rule not between", value1, value2, "modelRule");
            return (Criteria) this;
        }

        public Criteria andModelRexIsNull() {
            addCriterion("model_rex is null");
            return (Criteria) this;
        }

        public Criteria andModelRexIsNotNull() {
            addCriterion("model_rex is not null");
            return (Criteria) this;
        }

        public Criteria andModelRexEqualTo(String value) {
            addCriterion("model_rex =", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexNotEqualTo(String value) {
            addCriterion("model_rex <>", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexGreaterThan(String value) {
            addCriterion("model_rex >", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexGreaterThanOrEqualTo(String value) {
            addCriterion("model_rex >=", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexLessThan(String value) {
            addCriterion("model_rex <", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexLessThanOrEqualTo(String value) {
            addCriterion("model_rex <=", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexLike(String value) {
            addCriterion("model_rex like", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexNotLike(String value) {
            addCriterion("model_rex not like", value, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexIn(List<String> values) {
            addCriterion("model_rex in", values, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexNotIn(List<String> values) {
            addCriterion("model_rex not in", values, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexBetween(String value1, String value2) {
            addCriterion("model_rex between", value1, value2, "modelRex");
            return (Criteria) this;
        }

        public Criteria andModelRexNotBetween(String value1, String value2) {
            addCriterion("model_rex not between", value1, value2, "modelRex");
            return (Criteria) this;
        }

        public Criteria andRuleStateIsNull() {
            addCriterion("rule_state is null");
            return (Criteria) this;
        }

        public Criteria andRuleStateIsNotNull() {
            addCriterion("rule_state is not null");
            return (Criteria) this;
        }

        public Criteria andRuleStateEqualTo(Byte value) {
            addCriterion("rule_state =", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateNotEqualTo(Byte value) {
            addCriterion("rule_state <>", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateGreaterThan(Byte value) {
            addCriterion("rule_state >", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("rule_state >=", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateLessThan(Byte value) {
            addCriterion("rule_state <", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateLessThanOrEqualTo(Byte value) {
            addCriterion("rule_state <=", value, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateIn(List<Byte> values) {
            addCriterion("rule_state in", values, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateNotIn(List<Byte> values) {
            addCriterion("rule_state not in", values, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateBetween(Byte value1, Byte value2) {
            addCriterion("rule_state between", value1, value2, "ruleState");
            return (Criteria) this;
        }

        public Criteria andRuleStateNotBetween(Byte value1, Byte value2) {
            addCriterion("rule_state not between", value1, value2, "ruleState");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryIsNull() {
            addCriterion("is_join_category is null");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryIsNotNull() {
            addCriterion("is_join_category is not null");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryEqualTo(Byte value) {
            addCriterion("is_join_category =", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryNotEqualTo(Byte value) {
            addCriterion("is_join_category <>", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryGreaterThan(Byte value) {
            addCriterion("is_join_category >", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_join_category >=", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryLessThan(Byte value) {
            addCriterion("is_join_category <", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryLessThanOrEqualTo(Byte value) {
            addCriterion("is_join_category <=", value, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryIn(List<Byte> values) {
            addCriterion("is_join_category in", values, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryNotIn(List<Byte> values) {
            addCriterion("is_join_category not in", values, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryBetween(Byte value1, Byte value2) {
            addCriterion("is_join_category between", value1, value2, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andIsJoinCategoryNotBetween(Byte value1, Byte value2) {
            addCriterion("is_join_category not between", value1, value2, "isJoinCategory");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeIsNull() {
            addCriterion("category_type is null");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeIsNotNull() {
            addCriterion("category_type is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeEqualTo(Byte value) {
            addCriterion("category_type =", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeNotEqualTo(Byte value) {
            addCriterion("category_type <>", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeGreaterThan(Byte value) {
            addCriterion("category_type >", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("category_type >=", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeLessThan(Byte value) {
            addCriterion("category_type <", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeLessThanOrEqualTo(Byte value) {
            addCriterion("category_type <=", value, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeIn(List<Byte> values) {
            addCriterion("category_type in", values, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeNotIn(List<Byte> values) {
            addCriterion("category_type not in", values, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeBetween(Byte value1, Byte value2) {
            addCriterion("category_type between", value1, value2, "categoryType");
            return (Criteria) this;
        }

        public Criteria andCategoryTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("category_type not between", value1, value2, "categoryType");
            return (Criteria) this;
        }

        public Criteria andExcludeRexIsNull() {
            addCriterion("exclude_rex is null");
            return (Criteria) this;
        }

        public Criteria andExcludeRexIsNotNull() {
            addCriterion("exclude_rex is not null");
            return (Criteria) this;
        }

        public Criteria andExcludeRexEqualTo(String value) {
            addCriterion("exclude_rex =", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexNotEqualTo(String value) {
            addCriterion("exclude_rex <>", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexGreaterThan(String value) {
            addCriterion("exclude_rex >", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexGreaterThanOrEqualTo(String value) {
            addCriterion("exclude_rex >=", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexLessThan(String value) {
            addCriterion("exclude_rex <", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexLessThanOrEqualTo(String value) {
            addCriterion("exclude_rex <=", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexLike(String value) {
            addCriterion("exclude_rex like", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexNotLike(String value) {
            addCriterion("exclude_rex not like", value, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexIn(List<String> values) {
            addCriterion("exclude_rex in", values, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexNotIn(List<String> values) {
            addCriterion("exclude_rex not in", values, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexBetween(String value1, String value2) {
            addCriterion("exclude_rex between", value1, value2, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andExcludeRexNotBetween(String value1, String value2) {
            addCriterion("exclude_rex not between", value1, value2, "excludeRex");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorIsNull() {
            addCriterion("format_separator is null");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorIsNotNull() {
            addCriterion("format_separator is not null");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorEqualTo(String value) {
            addCriterion("format_separator =", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorNotEqualTo(String value) {
            addCriterion("format_separator <>", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorGreaterThan(String value) {
            addCriterion("format_separator >", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorGreaterThanOrEqualTo(String value) {
            addCriterion("format_separator >=", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorLessThan(String value) {
            addCriterion("format_separator <", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorLessThanOrEqualTo(String value) {
            addCriterion("format_separator <=", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorLike(String value) {
            addCriterion("format_separator like", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorNotLike(String value) {
            addCriterion("format_separator not like", value, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorIn(List<String> values) {
            addCriterion("format_separator in", values, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorNotIn(List<String> values) {
            addCriterion("format_separator not in", values, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorBetween(String value1, String value2) {
            addCriterion("format_separator between", value1, value2, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andFormatSeparatorNotBetween(String value1, String value2) {
            addCriterion("format_separator not between", value1, value2, "formatSeparator");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIsNull() {
            addCriterion("hub_category_no is null");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIsNotNull() {
            addCriterion("hub_category_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoEqualTo(String value) {
            addCriterion("hub_category_no =", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotEqualTo(String value) {
            addCriterion("hub_category_no <>", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoGreaterThan(String value) {
            addCriterion("hub_category_no >", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_category_no >=", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLessThan(String value) {
            addCriterion("hub_category_no <", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLessThanOrEqualTo(String value) {
            addCriterion("hub_category_no <=", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoLike(String value) {
            addCriterion("hub_category_no like", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotLike(String value) {
            addCriterion("hub_category_no not like", value, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoIn(List<String> values) {
            addCriterion("hub_category_no in", values, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotIn(List<String> values) {
            addCriterion("hub_category_no not in", values, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoBetween(String value1, String value2) {
            addCriterion("hub_category_no between", value1, value2, "hubCategoryNo");
            return (Criteria) this;
        }

        public Criteria andHubCategoryNoNotBetween(String value1, String value2) {
            addCriterion("hub_category_no not between", value1, value2, "hubCategoryNo");
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

        public Criteria andPushTimeIsNull() {
            addCriterion("push_time is null");
            return (Criteria) this;
        }

        public Criteria andPushTimeIsNotNull() {
            addCriterion("push_time is not null");
            return (Criteria) this;
        }

        public Criteria andPushTimeEqualTo(Date value) {
            addCriterion("push_time =", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeNotEqualTo(Date value) {
            addCriterion("push_time <>", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeGreaterThan(Date value) {
            addCriterion("push_time >", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("push_time >=", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeLessThan(Date value) {
            addCriterion("push_time <", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeLessThanOrEqualTo(Date value) {
            addCriterion("push_time <=", value, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeIn(List<Date> values) {
            addCriterion("push_time in", values, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeNotIn(List<Date> values) {
            addCriterion("push_time not in", values, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeBetween(Date value1, Date value2) {
            addCriterion("push_time between", value1, value2, "pushTime");
            return (Criteria) this;
        }

        public Criteria andPushTimeNotBetween(Date value1, Date value2) {
            addCriterion("push_time not between", value1, value2, "pushTime");
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