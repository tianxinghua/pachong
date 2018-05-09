package com.shangpin.ephub.data.mysql.mapping.material.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubMaterialMappingCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubMaterialMappingCriteria() {
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

        public Criteria andMaterialMappingIdIsNull() {
            addCriterion("material_mapping_id is null");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdIsNotNull() {
            addCriterion("material_mapping_id is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdEqualTo(Long value) {
            addCriterion("material_mapping_id =", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdNotEqualTo(Long value) {
            addCriterion("material_mapping_id <>", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdGreaterThan(Long value) {
            addCriterion("material_mapping_id >", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("material_mapping_id >=", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdLessThan(Long value) {
            addCriterion("material_mapping_id <", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdLessThanOrEqualTo(Long value) {
            addCriterion("material_mapping_id <=", value, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdIn(List<Long> values) {
            addCriterion("material_mapping_id in", values, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdNotIn(List<Long> values) {
            addCriterion("material_mapping_id not in", values, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdBetween(Long value1, Long value2) {
            addCriterion("material_mapping_id between", value1, value2, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andMaterialMappingIdNotBetween(Long value1, Long value2) {
            addCriterion("material_mapping_id not between", value1, value2, "materialMappingId");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIsNull() {
            addCriterion("supplier_material is null");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIsNotNull() {
            addCriterion("supplier_material is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialEqualTo(String value) {
            addCriterion("supplier_material =", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotEqualTo(String value) {
            addCriterion("supplier_material <>", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialGreaterThan(String value) {
            addCriterion("supplier_material >", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_material >=", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLessThan(String value) {
            addCriterion("supplier_material <", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLessThanOrEqualTo(String value) {
            addCriterion("supplier_material <=", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialLike(String value) {
            addCriterion("supplier_material like", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotLike(String value) {
            addCriterion("supplier_material not like", value, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialIn(List<String> values) {
            addCriterion("supplier_material in", values, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotIn(List<String> values) {
            addCriterion("supplier_material not in", values, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialBetween(String value1, String value2) {
            addCriterion("supplier_material between", value1, value2, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andSupplierMaterialNotBetween(String value1, String value2) {
            addCriterion("supplier_material not between", value1, value2, "supplierMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIsNull() {
            addCriterion("hub_material is null");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIsNotNull() {
            addCriterion("hub_material is not null");
            return (Criteria) this;
        }

        public Criteria andHubMaterialEqualTo(String value) {
            addCriterion("hub_material =", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotEqualTo(String value) {
            addCriterion("hub_material <>", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialGreaterThan(String value) {
            addCriterion("hub_material >", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialGreaterThanOrEqualTo(String value) {
            addCriterion("hub_material >=", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLessThan(String value) {
            addCriterion("hub_material <", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLessThanOrEqualTo(String value) {
            addCriterion("hub_material <=", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialLike(String value) {
            addCriterion("hub_material like", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotLike(String value) {
            addCriterion("hub_material not like", value, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialIn(List<String> values) {
            addCriterion("hub_material in", values, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotIn(List<String> values) {
            addCriterion("hub_material not in", values, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialBetween(String value1, String value2) {
            addCriterion("hub_material between", value1, value2, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andHubMaterialNotBetween(String value1, String value2) {
            addCriterion("hub_material not between", value1, value2, "hubMaterial");
            return (Criteria) this;
        }

        public Criteria andMappingLevelIsNull() {
            addCriterion("mapping_level is null");
            return (Criteria) this;
        }

        public Criteria andMappingLevelIsNotNull() {
            addCriterion("mapping_level is not null");
            return (Criteria) this;
        }

        public Criteria andMappingLevelEqualTo(Byte value) {
            addCriterion("mapping_level =", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelNotEqualTo(Byte value) {
            addCriterion("mapping_level <>", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelGreaterThan(Byte value) {
            addCriterion("mapping_level >", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelGreaterThanOrEqualTo(Byte value) {
            addCriterion("mapping_level >=", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelLessThan(Byte value) {
            addCriterion("mapping_level <", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelLessThanOrEqualTo(Byte value) {
            addCriterion("mapping_level <=", value, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelIn(List<Byte> values) {
            addCriterion("mapping_level in", values, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelNotIn(List<Byte> values) {
            addCriterion("mapping_level not in", values, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelBetween(Byte value1, Byte value2) {
            addCriterion("mapping_level between", value1, value2, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andMappingLevelNotBetween(Byte value1, Byte value2) {
            addCriterion("mapping_level not between", value1, value2, "mappingLevel");
            return (Criteria) this;
        }

        public Criteria andSourceIsNull() {
            addCriterion("source is null");
            return (Criteria) this;
        }

        public Criteria andSourceIsNotNull() {
            addCriterion("source is not null");
            return (Criteria) this;
        }

        public Criteria andSourceEqualTo(Byte value) {
            addCriterion("source =", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotEqualTo(Byte value) {
            addCriterion("source <>", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThan(Byte value) {
            addCriterion("source >", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceGreaterThanOrEqualTo(Byte value) {
            addCriterion("source >=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThan(Byte value) {
            addCriterion("source <", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceLessThanOrEqualTo(Byte value) {
            addCriterion("source <=", value, "source");
            return (Criteria) this;
        }

        public Criteria andSourceIn(List<Byte> values) {
            addCriterion("source in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotIn(List<Byte> values) {
            addCriterion("source not in", values, "source");
            return (Criteria) this;
        }

        public Criteria andSourceBetween(Byte value1, Byte value2) {
            addCriterion("source between", value1, value2, "source");
            return (Criteria) this;
        }

        public Criteria andSourceNotBetween(Byte value1, Byte value2) {
            addCriterion("source not between", value1, value2, "source");
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