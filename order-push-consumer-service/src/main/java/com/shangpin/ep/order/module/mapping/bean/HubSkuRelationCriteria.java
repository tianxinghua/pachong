package com.shangpin.ep.order.module.mapping.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <p>Title:HubSkuRelationCriteria.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午5:52:07
 */
public class HubSkuRelationCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSkuRelationCriteria() {
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

        public Criteria andSupplierIdIsNull() {
            addCriterion("SUPPLIER_ID is null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIsNotNull() {
            addCriterion("SUPPLIER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierIdEqualTo(String value) {
            addCriterion("SUPPLIER_ID =", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotEqualTo(String value) {
            addCriterion("SUPPLIER_ID <>", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThan(String value) {
            addCriterion("SUPPLIER_ID >", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdGreaterThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_ID >=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThan(String value) {
            addCriterion("SUPPLIER_ID <", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLessThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_ID <=", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdLike(String value) {
            addCriterion("SUPPLIER_ID like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotLike(String value) {
            addCriterion("SUPPLIER_ID not like", value, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdIn(List<String> values) {
            addCriterion("SUPPLIER_ID in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotIn(List<String> values) {
            addCriterion("SUPPLIER_ID not in", values, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdBetween(String value1, String value2) {
            addCriterion("SUPPLIER_ID between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSupplierIdNotBetween(String value1, String value2) {
            addCriterion("SUPPLIER_ID not between", value1, value2, "supplierId");
            return (Criteria) this;
        }

        public Criteria andSopNoIsNull() {
            addCriterion("SOP_NO is null");
            return (Criteria) this;
        }

        public Criteria andSopNoIsNotNull() {
            addCriterion("SOP_NO is not null");
            return (Criteria) this;
        }

        public Criteria andSopNoEqualTo(String value) {
            addCriterion("SOP_NO =", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoNotEqualTo(String value) {
            addCriterion("SOP_NO <>", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoGreaterThan(String value) {
            addCriterion("SOP_NO >", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoGreaterThanOrEqualTo(String value) {
            addCriterion("SOP_NO >=", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoLessThan(String value) {
            addCriterion("SOP_NO <", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoLessThanOrEqualTo(String value) {
            addCriterion("SOP_NO <=", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoLike(String value) {
            addCriterion("SOP_NO like", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoNotLike(String value) {
            addCriterion("SOP_NO not like", value, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoIn(List<String> values) {
            addCriterion("SOP_NO in", values, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoNotIn(List<String> values) {
            addCriterion("SOP_NO not in", values, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoBetween(String value1, String value2) {
            addCriterion("SOP_NO between", value1, value2, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopNoNotBetween(String value1, String value2) {
            addCriterion("SOP_NO not between", value1, value2, "sopNo");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdIsNull() {
            addCriterion("SOP_SKU_ID is null");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdIsNotNull() {
            addCriterion("SOP_SKU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdEqualTo(String value) {
            addCriterion("SOP_SKU_ID =", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdNotEqualTo(String value) {
            addCriterion("SOP_SKU_ID <>", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdGreaterThan(String value) {
            addCriterion("SOP_SKU_ID >", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdGreaterThanOrEqualTo(String value) {
            addCriterion("SOP_SKU_ID >=", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdLessThan(String value) {
            addCriterion("SOP_SKU_ID <", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdLessThanOrEqualTo(String value) {
            addCriterion("SOP_SKU_ID <=", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdLike(String value) {
            addCriterion("SOP_SKU_ID like", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdNotLike(String value) {
            addCriterion("SOP_SKU_ID not like", value, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdIn(List<String> values) {
            addCriterion("SOP_SKU_ID in", values, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdNotIn(List<String> values) {
            addCriterion("SOP_SKU_ID not in", values, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdBetween(String value1, String value2) {
            addCriterion("SOP_SKU_ID between", value1, value2, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSopSkuIdNotBetween(String value1, String value2) {
            addCriterion("SOP_SKU_ID not between", value1, value2, "sopSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdIsNull() {
            addCriterion("SUPPLIER_SKU_ID is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdIsNotNull() {
            addCriterion("SUPPLIER_SKU_ID is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdEqualTo(String value) {
            addCriterion("SUPPLIER_SKU_ID =", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotEqualTo(String value) {
            addCriterion("SUPPLIER_SKU_ID <>", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdGreaterThan(String value) {
            addCriterion("SUPPLIER_SKU_ID >", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdGreaterThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_SKU_ID >=", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdLessThan(String value) {
            addCriterion("SUPPLIER_SKU_ID <", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdLessThanOrEqualTo(String value) {
            addCriterion("SUPPLIER_SKU_ID <=", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdLike(String value) {
            addCriterion("SUPPLIER_SKU_ID like", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotLike(String value) {
            addCriterion("SUPPLIER_SKU_ID not like", value, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdIn(List<String> values) {
            addCriterion("SUPPLIER_SKU_ID in", values, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotIn(List<String> values) {
            addCriterion("SUPPLIER_SKU_ID not in", values, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdBetween(String value1, String value2) {
            addCriterion("SUPPLIER_SKU_ID between", value1, value2, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSkuIdNotBetween(String value1, String value2) {
            addCriterion("SUPPLIER_SKU_ID not between", value1, value2, "supplierSkuId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("CREATE_TIME is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("CREATE_TIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("CREATE_TIME =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("CREATE_TIME <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("CREATE_TIME >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("CREATE_TIME <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATE_TIME <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("CREATE_TIME in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("CREATE_TIME not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATE_TIME not between", value1, value2, "createTime");
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