package com.shangpin.ephub.client.data.mysql.config.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSupplierConfigCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSupplierConfigCriteriaDto() {
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

        public Criteria andSupplierConfigIdIsNull() {
            addCriterion("supplier_config_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdIsNotNull() {
            addCriterion("supplier_config_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdEqualTo(Long value) {
            addCriterion("supplier_config_id =", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdNotEqualTo(Long value) {
            addCriterion("supplier_config_id <>", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdGreaterThan(Long value) {
            addCriterion("supplier_config_id >", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdGreaterThanOrEqualTo(Long value) {
            addCriterion("supplier_config_id >=", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdLessThan(Long value) {
            addCriterion("supplier_config_id <", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdLessThanOrEqualTo(Long value) {
            addCriterion("supplier_config_id <=", value, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdIn(List<Long> values) {
            addCriterion("supplier_config_id in", values, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdNotIn(List<Long> values) {
            addCriterion("supplier_config_id not in", values, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdBetween(Long value1, Long value2) {
            addCriterion("supplier_config_id between", value1, value2, "supplierConfigId");
            return (Criteria) this;
        }

        public Criteria andSupplierConfigIdNotBetween(Long value1, Long value2) {
            addCriterion("supplier_config_id not between", value1, value2, "supplierConfigId");
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

        public Criteria andConfigStringIsNull() {
            addCriterion("config_string is null");
            return (Criteria) this;
        }

        public Criteria andConfigStringIsNotNull() {
            addCriterion("config_string is not null");
            return (Criteria) this;
        }

        public Criteria andConfigStringEqualTo(String value) {
            addCriterion("config_string =", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringNotEqualTo(String value) {
            addCriterion("config_string <>", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringGreaterThan(String value) {
            addCriterion("config_string >", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringGreaterThanOrEqualTo(String value) {
            addCriterion("config_string >=", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringLessThan(String value) {
            addCriterion("config_string <", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringLessThanOrEqualTo(String value) {
            addCriterion("config_string <=", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringLike(String value) {
            addCriterion("config_string like", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringNotLike(String value) {
            addCriterion("config_string not like", value, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringIn(List<String> values) {
            addCriterion("config_string in", values, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringNotIn(List<String> values) {
            addCriterion("config_string not in", values, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringBetween(String value1, String value2) {
            addCriterion("config_string between", value1, value2, "configString");
            return (Criteria) this;
        }

        public Criteria andConfigStringNotBetween(String value1, String value2) {
            addCriterion("config_string not between", value1, value2, "configString");
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