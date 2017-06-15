package com.shangpin.ephub.client.data.mysql.studio.pic.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSlotSpuPicCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSlotSpuPicCriteriaDto() {
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

        public Criteria andSlotSpuPicIdIsNull() {
            addCriterion("slot_spu_pic_id is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdIsNotNull() {
            addCriterion("slot_spu_pic_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdEqualTo(Long value) {
            addCriterion("slot_spu_pic_id =", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdNotEqualTo(Long value) {
            addCriterion("slot_spu_pic_id <>", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdGreaterThan(Long value) {
            addCriterion("slot_spu_pic_id >", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdGreaterThanOrEqualTo(Long value) {
            addCriterion("slot_spu_pic_id >=", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdLessThan(Long value) {
            addCriterion("slot_spu_pic_id <", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdLessThanOrEqualTo(Long value) {
            addCriterion("slot_spu_pic_id <=", value, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdIn(List<Long> values) {
            addCriterion("slot_spu_pic_id in", values, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdNotIn(List<Long> values) {
            addCriterion("slot_spu_pic_id not in", values, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdBetween(Long value1, Long value2) {
            addCriterion("slot_spu_pic_id between", value1, value2, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuPicIdNotBetween(Long value1, Long value2) {
            addCriterion("slot_spu_pic_id not between", value1, value2, "slotSpuPicId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIsNull() {
            addCriterion("slot_spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIsNotNull() {
            addCriterion("slot_spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdEqualTo(Long value) {
            addCriterion("slot_spu_id =", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotEqualTo(Long value) {
            addCriterion("slot_spu_id <>", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdGreaterThan(Long value) {
            addCriterion("slot_spu_id >", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("slot_spu_id >=", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdLessThan(Long value) {
            addCriterion("slot_spu_id <", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdLessThanOrEqualTo(Long value) {
            addCriterion("slot_spu_id <=", value, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdIn(List<Long> values) {
            addCriterion("slot_spu_id in", values, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotIn(List<Long> values) {
            addCriterion("slot_spu_id not in", values, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdBetween(Long value1, Long value2) {
            addCriterion("slot_spu_id between", value1, value2, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuIdNotBetween(Long value1, Long value2) {
            addCriterion("slot_spu_id not between", value1, value2, "slotSpuId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdIsNull() {
            addCriterion("slot_spu_supplier_id is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdIsNotNull() {
            addCriterion("slot_spu_supplier_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id =", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id <>", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdGreaterThan(Long value) {
            addCriterion("slot_spu_supplier_id >", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdGreaterThanOrEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id >=", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdLessThan(Long value) {
            addCriterion("slot_spu_supplier_id <", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdLessThanOrEqualTo(Long value) {
            addCriterion("slot_spu_supplier_id <=", value, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdIn(List<Long> values) {
            addCriterion("slot_spu_supplier_id in", values, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotIn(List<Long> values) {
            addCriterion("slot_spu_supplier_id not in", values, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdBetween(Long value1, Long value2) {
            addCriterion("slot_spu_supplier_id between", value1, value2, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuSupplierIdNotBetween(Long value1, Long value2) {
            addCriterion("slot_spu_supplier_id not between", value1, value2, "slotSpuSupplierId");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoIsNull() {
            addCriterion("slot_spu_no is null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoIsNotNull() {
            addCriterion("slot_spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoEqualTo(String value) {
            addCriterion("slot_spu_no =", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotEqualTo(String value) {
            addCriterion("slot_spu_no <>", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoGreaterThan(String value) {
            addCriterion("slot_spu_no >", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("slot_spu_no >=", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLessThan(String value) {
            addCriterion("slot_spu_no <", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLessThanOrEqualTo(String value) {
            addCriterion("slot_spu_no <=", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoLike(String value) {
            addCriterion("slot_spu_no like", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotLike(String value) {
            addCriterion("slot_spu_no not like", value, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoIn(List<String> values) {
            addCriterion("slot_spu_no in", values, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotIn(List<String> values) {
            addCriterion("slot_spu_no not in", values, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoBetween(String value1, String value2) {
            addCriterion("slot_spu_no between", value1, value2, "slotSpuNo");
            return (Criteria) this;
        }

        public Criteria andSlotSpuNoNotBetween(String value1, String value2) {
            addCriterion("slot_spu_no not between", value1, value2, "slotSpuNo");
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

        public Criteria andSpPicUrlIsNull() {
            addCriterion("sp_pic_url is null");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlIsNotNull() {
            addCriterion("sp_pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlEqualTo(String value) {
            addCriterion("sp_pic_url =", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlNotEqualTo(String value) {
            addCriterion("sp_pic_url <>", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlGreaterThan(String value) {
            addCriterion("sp_pic_url >", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("sp_pic_url >=", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlLessThan(String value) {
            addCriterion("sp_pic_url <", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlLessThanOrEqualTo(String value) {
            addCriterion("sp_pic_url <=", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlLike(String value) {
            addCriterion("sp_pic_url like", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlNotLike(String value) {
            addCriterion("sp_pic_url not like", value, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlIn(List<String> values) {
            addCriterion("sp_pic_url in", values, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlNotIn(List<String> values) {
            addCriterion("sp_pic_url not in", values, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlBetween(String value1, String value2) {
            addCriterion("sp_pic_url between", value1, value2, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andSpPicUrlNotBetween(String value1, String value2) {
            addCriterion("sp_pic_url not between", value1, value2, "spPicUrl");
            return (Criteria) this;
        }

        public Criteria andPicTypeIsNull() {
            addCriterion("pic_type is null");
            return (Criteria) this;
        }

        public Criteria andPicTypeIsNotNull() {
            addCriterion("pic_type is not null");
            return (Criteria) this;
        }

        public Criteria andPicTypeEqualTo(Byte value) {
            addCriterion("pic_type =", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotEqualTo(Byte value) {
            addCriterion("pic_type <>", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeGreaterThan(Byte value) {
            addCriterion("pic_type >", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("pic_type >=", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeLessThan(Byte value) {
            addCriterion("pic_type <", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeLessThanOrEqualTo(Byte value) {
            addCriterion("pic_type <=", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeIn(List<Byte> values) {
            addCriterion("pic_type in", values, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotIn(List<Byte> values) {
            addCriterion("pic_type not in", values, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeBetween(Byte value1, Byte value2) {
            addCriterion("pic_type between", value1, value2, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("pic_type not between", value1, value2, "picType");
            return (Criteria) this;
        }

        public Criteria andSortValueIsNull() {
            addCriterion("sort_value is null");
            return (Criteria) this;
        }

        public Criteria andSortValueIsNotNull() {
            addCriterion("sort_value is not null");
            return (Criteria) this;
        }

        public Criteria andSortValueEqualTo(Byte value) {
            addCriterion("sort_value =", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotEqualTo(Byte value) {
            addCriterion("sort_value <>", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueGreaterThan(Byte value) {
            addCriterion("sort_value >", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueGreaterThanOrEqualTo(Byte value) {
            addCriterion("sort_value >=", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueLessThan(Byte value) {
            addCriterion("sort_value <", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueLessThanOrEqualTo(Byte value) {
            addCriterion("sort_value <=", value, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueIn(List<Byte> values) {
            addCriterion("sort_value in", values, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotIn(List<Byte> values) {
            addCriterion("sort_value not in", values, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueBetween(Byte value1, Byte value2) {
            addCriterion("sort_value between", value1, value2, "sortValue");
            return (Criteria) this;
        }

        public Criteria andSortValueNotBetween(Byte value1, Byte value2) {
            addCriterion("sort_value not between", value1, value2, "sortValue");
            return (Criteria) this;
        }

        public Criteria andPicExtensionIsNull() {
            addCriterion("pic_extension is null");
            return (Criteria) this;
        }

        public Criteria andPicExtensionIsNotNull() {
            addCriterion("pic_extension is not null");
            return (Criteria) this;
        }

        public Criteria andPicExtensionEqualTo(String value) {
            addCriterion("pic_extension =", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionNotEqualTo(String value) {
            addCriterion("pic_extension <>", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionGreaterThan(String value) {
            addCriterion("pic_extension >", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionGreaterThanOrEqualTo(String value) {
            addCriterion("pic_extension >=", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionLessThan(String value) {
            addCriterion("pic_extension <", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionLessThanOrEqualTo(String value) {
            addCriterion("pic_extension <=", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionLike(String value) {
            addCriterion("pic_extension like", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionNotLike(String value) {
            addCriterion("pic_extension not like", value, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionIn(List<String> values) {
            addCriterion("pic_extension in", values, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionNotIn(List<String> values) {
            addCriterion("pic_extension not in", values, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionBetween(String value1, String value2) {
            addCriterion("pic_extension between", value1, value2, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicExtensionNotBetween(String value1, String value2) {
            addCriterion("pic_extension not between", value1, value2, "picExtension");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateIsNull() {
            addCriterion("pic_handle_state is null");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateIsNotNull() {
            addCriterion("pic_handle_state is not null");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateEqualTo(Byte value) {
            addCriterion("pic_handle_state =", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateNotEqualTo(Byte value) {
            addCriterion("pic_handle_state <>", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateGreaterThan(Byte value) {
            addCriterion("pic_handle_state >", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("pic_handle_state >=", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateLessThan(Byte value) {
            addCriterion("pic_handle_state <", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateLessThanOrEqualTo(Byte value) {
            addCriterion("pic_handle_state <=", value, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateIn(List<Byte> values) {
            addCriterion("pic_handle_state in", values, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateNotIn(List<Byte> values) {
            addCriterion("pic_handle_state not in", values, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateBetween(Byte value1, Byte value2) {
            addCriterion("pic_handle_state between", value1, value2, "picHandleState");
            return (Criteria) this;
        }

        public Criteria andPicHandleStateNotBetween(Byte value1, Byte value2) {
            addCriterion("pic_handle_state not between", value1, value2, "picHandleState");
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