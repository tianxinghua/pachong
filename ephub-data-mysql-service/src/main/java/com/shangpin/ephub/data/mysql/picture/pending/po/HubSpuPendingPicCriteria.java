package com.shangpin.ephub.data.mysql.picture.pending.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSpuPendingPicCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSpuPendingPicCriteria() {
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

        public Criteria andSpuPendingPicIdIsNull() {
            addCriterion("spu_pending_pic_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdIsNotNull() {
            addCriterion("spu_pending_pic_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdEqualTo(Long value) {
            addCriterion("spu_pending_pic_id =", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdNotEqualTo(Long value) {
            addCriterion("spu_pending_pic_id <>", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdGreaterThan(Long value) {
            addCriterion("spu_pending_pic_id >", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spu_pending_pic_id >=", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdLessThan(Long value) {
            addCriterion("spu_pending_pic_id <", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdLessThanOrEqualTo(Long value) {
            addCriterion("spu_pending_pic_id <=", value, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdIn(List<Long> values) {
            addCriterion("spu_pending_pic_id in", values, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdNotIn(List<Long> values) {
            addCriterion("spu_pending_pic_id not in", values, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdBetween(Long value1, Long value2) {
            addCriterion("spu_pending_pic_id between", value1, value2, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingPicIdNotBetween(Long value1, Long value2) {
            addCriterion("spu_pending_pic_id not between", value1, value2, "spuPendingPicId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIsNull() {
            addCriterion("spu_pending_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIsNotNull() {
            addCriterion("spu_pending_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdEqualTo(Long value) {
            addCriterion("spu_pending_id =", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotEqualTo(Long value) {
            addCriterion("spu_pending_id <>", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdGreaterThan(Long value) {
            addCriterion("spu_pending_id >", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spu_pending_id >=", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdLessThan(Long value) {
            addCriterion("spu_pending_id <", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdLessThanOrEqualTo(Long value) {
            addCriterion("spu_pending_id <=", value, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdIn(List<Long> values) {
            addCriterion("spu_pending_id in", values, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotIn(List<Long> values) {
            addCriterion("spu_pending_id not in", values, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdBetween(Long value1, Long value2) {
            addCriterion("spu_pending_id between", value1, value2, "spuPendingId");
            return (Criteria) this;
        }

        public Criteria andSpuPendingIdNotBetween(Long value1, Long value2) {
            addCriterion("spu_pending_id not between", value1, value2, "spuPendingId");
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

        public Criteria andPicUrlIsNull() {
            addCriterion("pic_url is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNotNull() {
            addCriterion("pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlEqualTo(String value) {
            addCriterion("pic_url =", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotEqualTo(String value) {
            addCriterion("pic_url <>", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThan(String value) {
            addCriterion("pic_url >", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pic_url >=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThan(String value) {
            addCriterion("pic_url <", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThanOrEqualTo(String value) {
            addCriterion("pic_url <=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLike(String value) {
            addCriterion("pic_url like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotLike(String value) {
            addCriterion("pic_url not like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIn(List<String> values) {
            addCriterion("pic_url in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotIn(List<String> values) {
            addCriterion("pic_url not in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlBetween(String value1, String value2) {
            addCriterion("pic_url between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotBetween(String value1, String value2) {
            addCriterion("pic_url not between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicNoIsNull() {
            addCriterion("pic_no is null");
            return (Criteria) this;
        }

        public Criteria andPicNoIsNotNull() {
            addCriterion("pic_no is not null");
            return (Criteria) this;
        }

        public Criteria andPicNoEqualTo(String value) {
            addCriterion("pic_no =", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoNotEqualTo(String value) {
            addCriterion("pic_no <>", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoGreaterThan(String value) {
            addCriterion("pic_no >", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoGreaterThanOrEqualTo(String value) {
            addCriterion("pic_no >=", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoLessThan(String value) {
            addCriterion("pic_no <", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoLessThanOrEqualTo(String value) {
            addCriterion("pic_no <=", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoLike(String value) {
            addCriterion("pic_no like", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoNotLike(String value) {
            addCriterion("pic_no not like", value, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoIn(List<String> values) {
            addCriterion("pic_no in", values, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoNotIn(List<String> values) {
            addCriterion("pic_no not in", values, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoBetween(String value1, String value2) {
            addCriterion("pic_no between", value1, value2, "picNo");
            return (Criteria) this;
        }

        public Criteria andPicNoNotBetween(String value1, String value2) {
            addCriterion("pic_no not between", value1, value2, "picNo");
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

        public Criteria andSupplierSpuIdIsNull() {
            addCriterion("supplier_spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdIsNotNull() {
            addCriterion("supplier_spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdEqualTo(Long value) {
            addCriterion("supplier_spu_id =", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotEqualTo(Long value) {
            addCriterion("supplier_spu_id <>", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdGreaterThan(Long value) {
            addCriterion("supplier_spu_id >", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("supplier_spu_id >=", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdLessThan(Long value) {
            addCriterion("supplier_spu_id <", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdLessThanOrEqualTo(Long value) {
            addCriterion("supplier_spu_id <=", value, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdIn(List<Long> values) {
            addCriterion("supplier_spu_id in", values, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotIn(List<Long> values) {
            addCriterion("supplier_spu_id not in", values, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdBetween(Long value1, Long value2) {
            addCriterion("supplier_spu_id between", value1, value2, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuIdNotBetween(Long value1, Long value2) {
            addCriterion("supplier_spu_id not between", value1, value2, "supplierSpuId");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoIsNull() {
            addCriterion("supplier_spu_no is null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoIsNotNull() {
            addCriterion("supplier_spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoEqualTo(String value) {
            addCriterion("supplier_spu_no =", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotEqualTo(String value) {
            addCriterion("supplier_spu_no <>", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoGreaterThan(String value) {
            addCriterion("supplier_spu_no >", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("supplier_spu_no >=", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLessThan(String value) {
            addCriterion("supplier_spu_no <", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLessThanOrEqualTo(String value) {
            addCriterion("supplier_spu_no <=", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoLike(String value) {
            addCriterion("supplier_spu_no like", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotLike(String value) {
            addCriterion("supplier_spu_no not like", value, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoIn(List<String> values) {
            addCriterion("supplier_spu_no in", values, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotIn(List<String> values) {
            addCriterion("supplier_spu_no not in", values, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoBetween(String value1, String value2) {
            addCriterion("supplier_spu_no between", value1, value2, "supplierSpuNo");
            return (Criteria) this;
        }

        public Criteria andSupplierSpuNoNotBetween(String value1, String value2) {
            addCriterion("supplier_spu_no not between", value1, value2, "supplierSpuNo");
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