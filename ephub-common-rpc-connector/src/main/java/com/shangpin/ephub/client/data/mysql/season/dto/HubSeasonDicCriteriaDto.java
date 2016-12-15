package com.shangpin.ephub.client.data.mysql.season.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSeasonDicCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSeasonDicCriteriaDto() {
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

        protected GeneratedCriteria() {
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

        public Criteria andSeasonDicIdIsNull() {
            addCriterion("season_dic_id is null");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdIsNotNull() {
            addCriterion("season_dic_id is not null");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdEqualTo(Long value) {
            addCriterion("season_dic_id =", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdNotEqualTo(Long value) {
            addCriterion("season_dic_id <>", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdGreaterThan(Long value) {
            addCriterion("season_dic_id >", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdGreaterThanOrEqualTo(Long value) {
            addCriterion("season_dic_id >=", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdLessThan(Long value) {
            addCriterion("season_dic_id <", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdLessThanOrEqualTo(Long value) {
            addCriterion("season_dic_id <=", value, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdIn(List<Long> values) {
            addCriterion("season_dic_id in", values, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdNotIn(List<Long> values) {
            addCriterion("season_dic_id not in", values, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdBetween(Long value1, Long value2) {
            addCriterion("season_dic_id between", value1, value2, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSeasonDicIdNotBetween(Long value1, Long value2) {
            addCriterion("season_dic_id not between", value1, value2, "seasonDicId");
            return (Criteria) this;
        }

        public Criteria andSupplieridIsNull() {
            addCriterion("supplierid is null");
            return (Criteria) this;
        }

        public Criteria andSupplieridIsNotNull() {
            addCriterion("supplierid is not null");
            return (Criteria) this;
        }

        public Criteria andSupplieridEqualTo(String value) {
            addCriterion("supplierid =", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotEqualTo(String value) {
            addCriterion("supplierid <>", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridGreaterThan(String value) {
            addCriterion("supplierid >", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridGreaterThanOrEqualTo(String value) {
            addCriterion("supplierid >=", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLessThan(String value) {
            addCriterion("supplierid <", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLessThanOrEqualTo(String value) {
            addCriterion("supplierid <=", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridLike(String value) {
            addCriterion("supplierid like", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotLike(String value) {
            addCriterion("supplierid not like", value, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridIn(List<String> values) {
            addCriterion("supplierid in", values, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotIn(List<String> values) {
            addCriterion("supplierid not in", values, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridBetween(String value1, String value2) {
            addCriterion("supplierid between", value1, value2, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplieridNotBetween(String value1, String value2) {
            addCriterion("supplierid not between", value1, value2, "supplierid");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonIsNull() {
            addCriterion("supplier_season is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonIsNotNull() {
            addCriterion("supplier_season is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonEqualTo(String value) {
            addCriterion("supplier_season =", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNotEqualTo(String value) {
            addCriterion("supplier_season <>", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonGreaterThan(String value) {
            addCriterion("supplier_season >", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_season >=", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonLessThan(String value) {
            addCriterion("supplier_season <", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonLessThanOrEqualTo(String value) {
            addCriterion("supplier_season <=", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonLike(String value) {
            addCriterion("supplier_season like", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNotLike(String value) {
            addCriterion("supplier_season not like", value, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonIn(List<String> values) {
            addCriterion("supplier_season in", values, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNotIn(List<String> values) {
            addCriterion("supplier_season not in", values, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonBetween(String value1, String value2) {
            addCriterion("supplier_season between", value1, value2, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andSupplierSeasonNotBetween(String value1, String value2) {
            addCriterion("supplier_season not between", value1, value2, "supplierSeason");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeIsNull() {
            addCriterion("hub_market_time is null");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeIsNotNull() {
            addCriterion("hub_market_time is not null");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeEqualTo(String value) {
            addCriterion("hub_market_time =", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeNotEqualTo(String value) {
            addCriterion("hub_market_time <>", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeGreaterThan(String value) {
            addCriterion("hub_market_time >", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeGreaterThanOrEqualTo(String value) {
            addCriterion("hub_market_time >=", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeLessThan(String value) {
            addCriterion("hub_market_time <", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeLessThanOrEqualTo(String value) {
            addCriterion("hub_market_time <=", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeLike(String value) {
            addCriterion("hub_market_time like", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeNotLike(String value) {
            addCriterion("hub_market_time not like", value, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeIn(List<String> values) {
            addCriterion("hub_market_time in", values, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeNotIn(List<String> values) {
            addCriterion("hub_market_time not in", values, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeBetween(String value1, String value2) {
            addCriterion("hub_market_time between", value1, value2, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubMarketTimeNotBetween(String value1, String value2) {
            addCriterion("hub_market_time not between", value1, value2, "hubMarketTime");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIsNull() {
            addCriterion("hub_season is null");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIsNotNull() {
            addCriterion("hub_season is not null");
            return (Criteria) this;
        }

        public Criteria andHubSeasonEqualTo(String value) {
            addCriterion("hub_season =", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotEqualTo(String value) {
            addCriterion("hub_season <>", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonGreaterThan(String value) {
            addCriterion("hub_season >", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonGreaterThanOrEqualTo(String value) {
            addCriterion("hub_season >=", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLessThan(String value) {
            addCriterion("hub_season <", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLessThanOrEqualTo(String value) {
            addCriterion("hub_season <=", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonLike(String value) {
            addCriterion("hub_season like", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotLike(String value) {
            addCriterion("hub_season not like", value, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonIn(List<String> values) {
            addCriterion("hub_season in", values, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotIn(List<String> values) {
            addCriterion("hub_season not in", values, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonBetween(String value1, String value2) {
            addCriterion("hub_season between", value1, value2, "hubSeason");
            return (Criteria) this;
        }

        public Criteria andHubSeasonNotBetween(String value1, String value2) {
            addCriterion("hub_season not between", value1, value2, "hubSeason");
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

        public Criteria andPushStateIsNull() {
            addCriterion("push_state is null");
            return (Criteria) this;
        }

        public Criteria andPushStateIsNotNull() {
            addCriterion("push_state is not null");
            return (Criteria) this;
        }

        public Criteria andPushStateEqualTo(Byte value) {
            addCriterion("push_state =", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotEqualTo(Byte value) {
            addCriterion("push_state <>", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateGreaterThan(Byte value) {
            addCriterion("push_state >", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("push_state >=", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateLessThan(Byte value) {
            addCriterion("push_state <", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateLessThanOrEqualTo(Byte value) {
            addCriterion("push_state <=", value, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateIn(List<Byte> values) {
            addCriterion("push_state in", values, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotIn(List<Byte> values) {
            addCriterion("push_state not in", values, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateBetween(Byte value1, Byte value2) {
            addCriterion("push_state between", value1, value2, "pushState");
            return (Criteria) this;
        }

        public Criteria andPushStateNotBetween(Byte value1, Byte value2) {
            addCriterion("push_state not between", value1, value2, "pushState");
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
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
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