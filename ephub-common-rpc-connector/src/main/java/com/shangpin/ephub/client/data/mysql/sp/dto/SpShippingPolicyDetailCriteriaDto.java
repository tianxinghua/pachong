package com.shangpin.ephub.client.data.mysql.sp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpShippingPolicyDetailCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public SpShippingPolicyDetailCriteriaDto() {
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

        public Criteria andShippingPolicyIdIsNull() {
            addCriterion("shipping_policy_id is null");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdIsNotNull() {
            addCriterion("shipping_policy_id is not null");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdEqualTo(Long value) {
            addCriterion("shipping_policy_id =", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdNotEqualTo(Long value) {
            addCriterion("shipping_policy_id <>", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdGreaterThan(Long value) {
            addCriterion("shipping_policy_id >", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdGreaterThanOrEqualTo(Long value) {
            addCriterion("shipping_policy_id >=", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdLessThan(Long value) {
            addCriterion("shipping_policy_id <", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdLessThanOrEqualTo(Long value) {
            addCriterion("shipping_policy_id <=", value, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdIn(List<Long> values) {
            addCriterion("shipping_policy_id in", values, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdNotIn(List<Long> values) {
            addCriterion("shipping_policy_id not in", values, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdBetween(Long value1, Long value2) {
            addCriterion("shipping_policy_id between", value1, value2, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andShippingPolicyIdNotBetween(Long value1, Long value2) {
            addCriterion("shipping_policy_id not between", value1, value2, "shippingPolicyId");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromIsNull() {
            addCriterion("sp_category_from is null");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromIsNotNull() {
            addCriterion("sp_category_from is not null");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromEqualTo(Byte value) {
            addCriterion("sp_category_from =", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromNotEqualTo(Byte value) {
            addCriterion("sp_category_from <>", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromGreaterThan(Byte value) {
            addCriterion("sp_category_from >", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromGreaterThanOrEqualTo(Byte value) {
            addCriterion("sp_category_from >=", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromLessThan(Byte value) {
            addCriterion("sp_category_from <", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromLessThanOrEqualTo(Byte value) {
            addCriterion("sp_category_from <=", value, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromIn(List<Byte> values) {
            addCriterion("sp_category_from in", values, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromNotIn(List<Byte> values) {
            addCriterion("sp_category_from not in", values, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromBetween(Byte value1, Byte value2) {
            addCriterion("sp_category_from between", value1, value2, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryFromNotBetween(Byte value1, Byte value2) {
            addCriterion("sp_category_from not between", value1, value2, "spCategoryFrom");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoIsNull() {
            addCriterion("sp_category_no is null");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoIsNotNull() {
            addCriterion("sp_category_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoEqualTo(String value) {
            addCriterion("sp_category_no =", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoNotEqualTo(String value) {
            addCriterion("sp_category_no <>", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoGreaterThan(String value) {
            addCriterion("sp_category_no >", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoGreaterThanOrEqualTo(String value) {
            addCriterion("sp_category_no >=", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoLessThan(String value) {
            addCriterion("sp_category_no <", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoLessThanOrEqualTo(String value) {
            addCriterion("sp_category_no <=", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoLike(String value) {
            addCriterion("sp_category_no like", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoNotLike(String value) {
            addCriterion("sp_category_no not like", value, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoIn(List<String> values) {
            addCriterion("sp_category_no in", values, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoNotIn(List<String> values) {
            addCriterion("sp_category_no not in", values, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoBetween(String value1, String value2) {
            addCriterion("sp_category_no between", value1, value2, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpCategoryNoNotBetween(String value1, String value2) {
            addCriterion("sp_category_no not between", value1, value2, "spCategoryNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoIsNull() {
            addCriterion("sp_brand_no is null");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoIsNotNull() {
            addCriterion("sp_brand_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoEqualTo(String value) {
            addCriterion("sp_brand_no =", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoNotEqualTo(String value) {
            addCriterion("sp_brand_no <>", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoGreaterThan(String value) {
            addCriterion("sp_brand_no >", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoGreaterThanOrEqualTo(String value) {
            addCriterion("sp_brand_no >=", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoLessThan(String value) {
            addCriterion("sp_brand_no <", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoLessThanOrEqualTo(String value) {
            addCriterion("sp_brand_no <=", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoLike(String value) {
            addCriterion("sp_brand_no like", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoNotLike(String value) {
            addCriterion("sp_brand_no not like", value, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoIn(List<String> values) {
            addCriterion("sp_brand_no in", values, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoNotIn(List<String> values) {
            addCriterion("sp_brand_no not in", values, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoBetween(String value1, String value2) {
            addCriterion("sp_brand_no between", value1, value2, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andSpBrandNoNotBetween(String value1, String value2) {
            addCriterion("sp_brand_no not between", value1, value2, "spBrandNo");
            return (Criteria) this;
        }

        public Criteria andPromotionSignIsNull() {
            addCriterion("promotion_sign is null");
            return (Criteria) this;
        }

        public Criteria andPromotionSignIsNotNull() {
            addCriterion("promotion_sign is not null");
            return (Criteria) this;
        }

        public Criteria andPromotionSignEqualTo(Byte value) {
            addCriterion("promotion_sign =", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignNotEqualTo(Byte value) {
            addCriterion("promotion_sign <>", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignGreaterThan(Byte value) {
            addCriterion("promotion_sign >", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignGreaterThanOrEqualTo(Byte value) {
            addCriterion("promotion_sign >=", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignLessThan(Byte value) {
            addCriterion("promotion_sign <", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignLessThanOrEqualTo(Byte value) {
            addCriterion("promotion_sign <=", value, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignIn(List<Byte> values) {
            addCriterion("promotion_sign in", values, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignNotIn(List<Byte> values) {
            addCriterion("promotion_sign not in", values, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignBetween(Byte value1, Byte value2) {
            addCriterion("promotion_sign between", value1, value2, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andPromotionSignNotBetween(Byte value1, Byte value2) {
            addCriterion("promotion_sign not between", value1, value2, "promotionSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignIsNull() {
            addCriterion("delivery_area_sign is null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignIsNotNull() {
            addCriterion("delivery_area_sign is not null");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignEqualTo(Byte value) {
            addCriterion("delivery_area_sign =", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignNotEqualTo(Byte value) {
            addCriterion("delivery_area_sign <>", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignGreaterThan(Byte value) {
            addCriterion("delivery_area_sign >", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignGreaterThanOrEqualTo(Byte value) {
            addCriterion("delivery_area_sign >=", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignLessThan(Byte value) {
            addCriterion("delivery_area_sign <", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignLessThanOrEqualTo(Byte value) {
            addCriterion("delivery_area_sign <=", value, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignIn(List<Byte> values) {
            addCriterion("delivery_area_sign in", values, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignNotIn(List<Byte> values) {
            addCriterion("delivery_area_sign not in", values, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignBetween(Byte value1, Byte value2) {
            addCriterion("delivery_area_sign between", value1, value2, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andDeliveryAreaSignNotBetween(Byte value1, Byte value2) {
            addCriterion("delivery_area_sign not between", value1, value2, "deliveryAreaSign");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIsNull() {
            addCriterion("sp_sku_no is null");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIsNotNull() {
            addCriterion("sp_sku_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoEqualTo(String value) {
            addCriterion("sp_sku_no =", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotEqualTo(String value) {
            addCriterion("sp_sku_no <>", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoGreaterThan(String value) {
            addCriterion("sp_sku_no >", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoGreaterThanOrEqualTo(String value) {
            addCriterion("sp_sku_no >=", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLessThan(String value) {
            addCriterion("sp_sku_no <", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLessThanOrEqualTo(String value) {
            addCriterion("sp_sku_no <=", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoLike(String value) {
            addCriterion("sp_sku_no like", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotLike(String value) {
            addCriterion("sp_sku_no not like", value, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoIn(List<String> values) {
            addCriterion("sp_sku_no in", values, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotIn(List<String> values) {
            addCriterion("sp_sku_no not in", values, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoBetween(String value1, String value2) {
            addCriterion("sp_sku_no between", value1, value2, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpSkuNoNotBetween(String value1, String value2) {
            addCriterion("sp_sku_no not between", value1, value2, "spSkuNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoIsNull() {
            addCriterion("sp_activity_no is null");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoIsNotNull() {
            addCriterion("sp_activity_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoEqualTo(String value) {
            addCriterion("sp_activity_no =", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoNotEqualTo(String value) {
            addCriterion("sp_activity_no <>", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoGreaterThan(String value) {
            addCriterion("sp_activity_no >", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoGreaterThanOrEqualTo(String value) {
            addCriterion("sp_activity_no >=", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoLessThan(String value) {
            addCriterion("sp_activity_no <", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoLessThanOrEqualTo(String value) {
            addCriterion("sp_activity_no <=", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoLike(String value) {
            addCriterion("sp_activity_no like", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoNotLike(String value) {
            addCriterion("sp_activity_no not like", value, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoIn(List<String> values) {
            addCriterion("sp_activity_no in", values, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoNotIn(List<String> values) {
            addCriterion("sp_activity_no not in", values, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoBetween(String value1, String value2) {
            addCriterion("sp_activity_no between", value1, value2, "spActivityNo");
            return (Criteria) this;
        }

        public Criteria andSpActivityNoNotBetween(String value1, String value2) {
            addCriterion("sp_activity_no not between", value1, value2, "spActivityNo");
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