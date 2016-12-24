package com.shangpin.ephub.client.data.mysql.spu.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubSpuCriteriaDto {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer pageNo = 1;

    protected Integer startRow;

    protected Integer pageSize = 10;

    protected String fields;

    public HubSpuCriteriaDto() {
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

        public Criteria andSpuIdIsNull() {
            addCriterion("spu_id is null");
            return (Criteria) this;
        }

        public Criteria andSpuIdIsNotNull() {
            addCriterion("spu_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpuIdEqualTo(Long value) {
            addCriterion("spu_id =", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotEqualTo(Long value) {
            addCriterion("spu_id <>", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThan(Long value) {
            addCriterion("spu_id >", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdGreaterThanOrEqualTo(Long value) {
            addCriterion("spu_id >=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThan(Long value) {
            addCriterion("spu_id <", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdLessThanOrEqualTo(Long value) {
            addCriterion("spu_id <=", value, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdIn(List<Long> values) {
            addCriterion("spu_id in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotIn(List<Long> values) {
            addCriterion("spu_id not in", values, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdBetween(Long value1, Long value2) {
            addCriterion("spu_id between", value1, value2, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuIdNotBetween(Long value1, Long value2) {
            addCriterion("spu_id not between", value1, value2, "spuId");
            return (Criteria) this;
        }

        public Criteria andSpuModelIsNull() {
            addCriterion("spu_model is null");
            return (Criteria) this;
        }

        public Criteria andSpuModelIsNotNull() {
            addCriterion("spu_model is not null");
            return (Criteria) this;
        }

        public Criteria andSpuModelEqualTo(String value) {
            addCriterion("spu_model =", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotEqualTo(String value) {
            addCriterion("spu_model <>", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelGreaterThan(String value) {
            addCriterion("spu_model >", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("spu_model >=", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLessThan(String value) {
            addCriterion("spu_model <", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLessThanOrEqualTo(String value) {
            addCriterion("spu_model <=", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelLike(String value) {
            addCriterion("spu_model like", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotLike(String value) {
            addCriterion("spu_model not like", value, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelIn(List<String> values) {
            addCriterion("spu_model in", values, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotIn(List<String> values) {
            addCriterion("spu_model not in", values, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelBetween(String value1, String value2) {
            addCriterion("spu_model between", value1, value2, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuModelNotBetween(String value1, String value2) {
            addCriterion("spu_model not between", value1, value2, "spuModel");
            return (Criteria) this;
        }

        public Criteria andSpuNameIsNull() {
            addCriterion("spu_name is null");
            return (Criteria) this;
        }

        public Criteria andSpuNameIsNotNull() {
            addCriterion("spu_name is not null");
            return (Criteria) this;
        }

        public Criteria andSpuNameEqualTo(String value) {
            addCriterion("spu_name =", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotEqualTo(String value) {
            addCriterion("spu_name <>", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameGreaterThan(String value) {
            addCriterion("spu_name >", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameGreaterThanOrEqualTo(String value) {
            addCriterion("spu_name >=", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLessThan(String value) {
            addCriterion("spu_name <", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLessThanOrEqualTo(String value) {
            addCriterion("spu_name <=", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameLike(String value) {
            addCriterion("spu_name like", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotLike(String value) {
            addCriterion("spu_name not like", value, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameIn(List<String> values) {
            addCriterion("spu_name in", values, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotIn(List<String> values) {
            addCriterion("spu_name not in", values, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameBetween(String value1, String value2) {
            addCriterion("spu_name between", value1, value2, "spuName");
            return (Criteria) this;
        }

        public Criteria andSpuNameNotBetween(String value1, String value2) {
            addCriterion("spu_name not between", value1, value2, "spuName");
            return (Criteria) this;
        }

        public Criteria andGenderIsNull() {
            addCriterion("gender is null");
            return (Criteria) this;
        }

        public Criteria andGenderIsNotNull() {
            addCriterion("gender is not null");
            return (Criteria) this;
        }

        public Criteria andGenderEqualTo(String value) {
            addCriterion("gender =", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotEqualTo(String value) {
            addCriterion("gender <>", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThan(String value) {
            addCriterion("gender >", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThanOrEqualTo(String value) {
            addCriterion("gender >=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThan(String value) {
            addCriterion("gender <", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThanOrEqualTo(String value) {
            addCriterion("gender <=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLike(String value) {
            addCriterion("gender like", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotLike(String value) {
            addCriterion("gender not like", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderIn(List<String> values) {
            addCriterion("gender in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotIn(List<String> values) {
            addCriterion("gender not in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderBetween(String value1, String value2) {
            addCriterion("gender between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotBetween(String value1, String value2) {
            addCriterion("gender not between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andCategoryNoIsNull() {
            addCriterion("category_no is null");
            return (Criteria) this;
        }

        public Criteria andCategoryNoIsNotNull() {
            addCriterion("category_no is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryNoEqualTo(String value) {
            addCriterion("category_no =", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoNotEqualTo(String value) {
            addCriterion("category_no <>", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoGreaterThan(String value) {
            addCriterion("category_no >", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoGreaterThanOrEqualTo(String value) {
            addCriterion("category_no >=", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoLessThan(String value) {
            addCriterion("category_no <", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoLessThanOrEqualTo(String value) {
            addCriterion("category_no <=", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoLike(String value) {
            addCriterion("category_no like", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoNotLike(String value) {
            addCriterion("category_no not like", value, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoIn(List<String> values) {
            addCriterion("category_no in", values, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoNotIn(List<String> values) {
            addCriterion("category_no not in", values, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoBetween(String value1, String value2) {
            addCriterion("category_no between", value1, value2, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andCategoryNoNotBetween(String value1, String value2) {
            addCriterion("category_no not between", value1, value2, "categoryNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoIsNull() {
            addCriterion("brand_no is null");
            return (Criteria) this;
        }

        public Criteria andBrandNoIsNotNull() {
            addCriterion("brand_no is not null");
            return (Criteria) this;
        }

        public Criteria andBrandNoEqualTo(String value) {
            addCriterion("brand_no =", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoNotEqualTo(String value) {
            addCriterion("brand_no <>", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoGreaterThan(String value) {
            addCriterion("brand_no >", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoGreaterThanOrEqualTo(String value) {
            addCriterion("brand_no >=", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoLessThan(String value) {
            addCriterion("brand_no <", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoLessThanOrEqualTo(String value) {
            addCriterion("brand_no <=", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoLike(String value) {
            addCriterion("brand_no like", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoNotLike(String value) {
            addCriterion("brand_no not like", value, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoIn(List<String> values) {
            addCriterion("brand_no in", values, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoNotIn(List<String> values) {
            addCriterion("brand_no not in", values, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoBetween(String value1, String value2) {
            addCriterion("brand_no between", value1, value2, "brandNo");
            return (Criteria) this;
        }

        public Criteria andBrandNoNotBetween(String value1, String value2) {
            addCriterion("brand_no not between", value1, value2, "brandNo");
            return (Criteria) this;
        }

        public Criteria andMarketTimeIsNull() {
            addCriterion("market_time is null");
            return (Criteria) this;
        }

        public Criteria andMarketTimeIsNotNull() {
            addCriterion("market_time is not null");
            return (Criteria) this;
        }

        public Criteria andMarketTimeEqualTo(String value) {
            addCriterion("market_time =", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeNotEqualTo(String value) {
            addCriterion("market_time <>", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeGreaterThan(String value) {
            addCriterion("market_time >", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeGreaterThanOrEqualTo(String value) {
            addCriterion("market_time >=", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeLessThan(String value) {
            addCriterion("market_time <", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeLessThanOrEqualTo(String value) {
            addCriterion("market_time <=", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeLike(String value) {
            addCriterion("market_time like", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeNotLike(String value) {
            addCriterion("market_time not like", value, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeIn(List<String> values) {
            addCriterion("market_time in", values, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeNotIn(List<String> values) {
            addCriterion("market_time not in", values, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeBetween(String value1, String value2) {
            addCriterion("market_time between", value1, value2, "marketTime");
            return (Criteria) this;
        }

        public Criteria andMarketTimeNotBetween(String value1, String value2) {
            addCriterion("market_time not between", value1, value2, "marketTime");
            return (Criteria) this;
        }

        public Criteria andHubColorIsNull() {
            addCriterion("hub_color is null");
            return (Criteria) this;
        }

        public Criteria andHubColorIsNotNull() {
            addCriterion("hub_color is not null");
            return (Criteria) this;
        }

        public Criteria andHubColorEqualTo(String value) {
            addCriterion("hub_color =", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotEqualTo(String value) {
            addCriterion("hub_color <>", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorGreaterThan(String value) {
            addCriterion("hub_color >", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorGreaterThanOrEqualTo(String value) {
            addCriterion("hub_color >=", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLessThan(String value) {
            addCriterion("hub_color <", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLessThanOrEqualTo(String value) {
            addCriterion("hub_color <=", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorLike(String value) {
            addCriterion("hub_color like", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotLike(String value) {
            addCriterion("hub_color not like", value, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorIn(List<String> values) {
            addCriterion("hub_color in", values, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotIn(List<String> values) {
            addCriterion("hub_color not in", values, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorBetween(String value1, String value2) {
            addCriterion("hub_color between", value1, value2, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNotBetween(String value1, String value2) {
            addCriterion("hub_color not between", value1, value2, "hubColor");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIsNull() {
            addCriterion("hub_color_no is null");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIsNotNull() {
            addCriterion("hub_color_no is not null");
            return (Criteria) this;
        }

        public Criteria andHubColorNoEqualTo(String value) {
            addCriterion("hub_color_no =", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotEqualTo(String value) {
            addCriterion("hub_color_no <>", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoGreaterThan(String value) {
            addCriterion("hub_color_no >", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoGreaterThanOrEqualTo(String value) {
            addCriterion("hub_color_no >=", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLessThan(String value) {
            addCriterion("hub_color_no <", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLessThanOrEqualTo(String value) {
            addCriterion("hub_color_no <=", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoLike(String value) {
            addCriterion("hub_color_no like", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotLike(String value) {
            addCriterion("hub_color_no not like", value, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoIn(List<String> values) {
            addCriterion("hub_color_no in", values, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotIn(List<String> values) {
            addCriterion("hub_color_no not in", values, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoBetween(String value1, String value2) {
            addCriterion("hub_color_no between", value1, value2, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andHubColorNoNotBetween(String value1, String value2) {
            addCriterion("hub_color_no not between", value1, value2, "hubColorNo");
            return (Criteria) this;
        }

        public Criteria andSeasonIsNull() {
            addCriterion("season is null");
            return (Criteria) this;
        }

        public Criteria andSeasonIsNotNull() {
            addCriterion("season is not null");
            return (Criteria) this;
        }

        public Criteria andSeasonEqualTo(String value) {
            addCriterion("season =", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotEqualTo(String value) {
            addCriterion("season <>", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonGreaterThan(String value) {
            addCriterion("season >", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonGreaterThanOrEqualTo(String value) {
            addCriterion("season >=", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonLessThan(String value) {
            addCriterion("season <", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonLessThanOrEqualTo(String value) {
            addCriterion("season <=", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonLike(String value) {
            addCriterion("season like", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotLike(String value) {
            addCriterion("season not like", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonIn(List<String> values) {
            addCriterion("season in", values, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotIn(List<String> values) {
            addCriterion("season not in", values, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonBetween(String value1, String value2) {
            addCriterion("season between", value1, value2, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotBetween(String value1, String value2) {
            addCriterion("season not between", value1, value2, "season");
            return (Criteria) this;
        }

        public Criteria andSpuDescIsNull() {
            addCriterion("spu_desc is null");
            return (Criteria) this;
        }

        public Criteria andSpuDescIsNotNull() {
            addCriterion("spu_desc is not null");
            return (Criteria) this;
        }

        public Criteria andSpuDescEqualTo(String value) {
            addCriterion("spu_desc =", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotEqualTo(String value) {
            addCriterion("spu_desc <>", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescGreaterThan(String value) {
            addCriterion("spu_desc >", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescGreaterThanOrEqualTo(String value) {
            addCriterion("spu_desc >=", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLessThan(String value) {
            addCriterion("spu_desc <", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLessThanOrEqualTo(String value) {
            addCriterion("spu_desc <=", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescLike(String value) {
            addCriterion("spu_desc like", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotLike(String value) {
            addCriterion("spu_desc not like", value, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescIn(List<String> values) {
            addCriterion("spu_desc in", values, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotIn(List<String> values) {
            addCriterion("spu_desc not in", values, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescBetween(String value1, String value2) {
            addCriterion("spu_desc between", value1, value2, "spuDesc");
            return (Criteria) this;
        }

        public Criteria andSpuDescNotBetween(String value1, String value2) {
            addCriterion("spu_desc not between", value1, value2, "spuDesc");
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

        public Criteria andSpuStateIsNull() {
            addCriterion("spu_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuStateIsNotNull() {
            addCriterion("spu_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuStateEqualTo(Byte value) {
            addCriterion("spu_state =", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotEqualTo(Byte value) {
            addCriterion("spu_state <>", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateGreaterThan(Byte value) {
            addCriterion("spu_state >", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_state >=", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateLessThan(Byte value) {
            addCriterion("spu_state <", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_state <=", value, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateIn(List<Byte> values) {
            addCriterion("spu_state in", values, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotIn(List<Byte> values) {
            addCriterion("spu_state not in", values, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_state between", value1, value2, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_state not between", value1, value2, "spuState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateIsNull() {
            addCriterion("spu_select_state is null");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateIsNotNull() {
            addCriterion("spu_select_state is not null");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateEqualTo(Byte value) {
            addCriterion("spu_select_state =", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateNotEqualTo(Byte value) {
            addCriterion("spu_select_state <>", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateGreaterThan(Byte value) {
            addCriterion("spu_select_state >", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("spu_select_state >=", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateLessThan(Byte value) {
            addCriterion("spu_select_state <", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateLessThanOrEqualTo(Byte value) {
            addCriterion("spu_select_state <=", value, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateIn(List<Byte> values) {
            addCriterion("spu_select_state in", values, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateNotIn(List<Byte> values) {
            addCriterion("spu_select_state not in", values, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateBetween(Byte value1, Byte value2) {
            addCriterion("spu_select_state between", value1, value2, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andSpuSelectStateNotBetween(Byte value1, Byte value2) {
            addCriterion("spu_select_state not between", value1, value2, "spuSelectState");
            return (Criteria) this;
        }

        public Criteria andNewTypeIsNull() {
            addCriterion("new_type is null");
            return (Criteria) this;
        }

        public Criteria andNewTypeIsNotNull() {
            addCriterion("new_type is not null");
            return (Criteria) this;
        }

        public Criteria andNewTypeEqualTo(Byte value) {
            addCriterion("new_type =", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeNotEqualTo(Byte value) {
            addCriterion("new_type <>", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeGreaterThan(Byte value) {
            addCriterion("new_type >", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("new_type >=", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeLessThan(Byte value) {
            addCriterion("new_type <", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeLessThanOrEqualTo(Byte value) {
            addCriterion("new_type <=", value, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeIn(List<Byte> values) {
            addCriterion("new_type in", values, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeNotIn(List<Byte> values) {
            addCriterion("new_type not in", values, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeBetween(Byte value1, Byte value2) {
            addCriterion("new_type between", value1, value2, "newType");
            return (Criteria) this;
        }

        public Criteria andNewTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("new_type not between", value1, value2, "newType");
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

        public Criteria andOriginIsNull() {
            addCriterion("origin is null");
            return (Criteria) this;
        }

        public Criteria andOriginIsNotNull() {
            addCriterion("origin is not null");
            return (Criteria) this;
        }

        public Criteria andOriginEqualTo(String value) {
            addCriterion("origin =", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotEqualTo(String value) {
            addCriterion("origin <>", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThan(String value) {
            addCriterion("origin >", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginGreaterThanOrEqualTo(String value) {
            addCriterion("origin >=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThan(String value) {
            addCriterion("origin <", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLessThanOrEqualTo(String value) {
            addCriterion("origin <=", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginLike(String value) {
            addCriterion("origin like", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotLike(String value) {
            addCriterion("origin not like", value, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginIn(List<String> values) {
            addCriterion("origin in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotIn(List<String> values) {
            addCriterion("origin not in", values, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginBetween(String value1, String value2) {
            addCriterion("origin between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andOriginNotBetween(String value1, String value2) {
            addCriterion("origin not between", value1, value2, "origin");
            return (Criteria) this;
        }

        public Criteria andMaterialIsNull() {
            addCriterion("material is null");
            return (Criteria) this;
        }

        public Criteria andMaterialIsNotNull() {
            addCriterion("material is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialEqualTo(String value) {
            addCriterion("material =", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialNotEqualTo(String value) {
            addCriterion("material <>", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialGreaterThan(String value) {
            addCriterion("material >", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialGreaterThanOrEqualTo(String value) {
            addCriterion("material >=", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialLessThan(String value) {
            addCriterion("material <", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialLessThanOrEqualTo(String value) {
            addCriterion("material <=", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialLike(String value) {
            addCriterion("material like", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialNotLike(String value) {
            addCriterion("material not like", value, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialIn(List<String> values) {
            addCriterion("material in", values, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialNotIn(List<String> values) {
            addCriterion("material not in", values, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialBetween(String value1, String value2) {
            addCriterion("material between", value1, value2, "material");
            return (Criteria) this;
        }

        public Criteria andMaterialNotBetween(String value1, String value2) {
            addCriterion("material not between", value1, value2, "material");
            return (Criteria) this;
        }

        public Criteria andSpuNoIsNull() {
            addCriterion("spu_no is null");
            return (Criteria) this;
        }

        public Criteria andSpuNoIsNotNull() {
            addCriterion("spu_no is not null");
            return (Criteria) this;
        }

        public Criteria andSpuNoEqualTo(String value) {
            addCriterion("spu_no =", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoNotEqualTo(String value) {
            addCriterion("spu_no <>", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoGreaterThan(String value) {
            addCriterion("spu_no >", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoGreaterThanOrEqualTo(String value) {
            addCriterion("spu_no >=", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoLessThan(String value) {
            addCriterion("spu_no <", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoLessThanOrEqualTo(String value) {
            addCriterion("spu_no <=", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoLike(String value) {
            addCriterion("spu_no like", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoNotLike(String value) {
            addCriterion("spu_no not like", value, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoIn(List<String> values) {
            addCriterion("spu_no in", values, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoNotIn(List<String> values) {
            addCriterion("spu_no not in", values, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoBetween(String value1, String value2) {
            addCriterion("spu_no between", value1, value2, "spuNo");
            return (Criteria) this;
        }

        public Criteria andSpuNoNotBetween(String value1, String value2) {
            addCriterion("spu_no not between", value1, value2, "spuNo");
            return (Criteria) this;
        }

        public Criteria andInfoFromIsNull() {
            addCriterion("info_from is null");
            return (Criteria) this;
        }

        public Criteria andInfoFromIsNotNull() {
            addCriterion("info_from is not null");
            return (Criteria) this;
        }

        public Criteria andInfoFromEqualTo(Byte value) {
            addCriterion("info_from =", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotEqualTo(Byte value) {
            addCriterion("info_from <>", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromGreaterThan(Byte value) {
            addCriterion("info_from >", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromGreaterThanOrEqualTo(Byte value) {
            addCriterion("info_from >=", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromLessThan(Byte value) {
            addCriterion("info_from <", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromLessThanOrEqualTo(Byte value) {
            addCriterion("info_from <=", value, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromIn(List<Byte> values) {
            addCriterion("info_from in", values, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotIn(List<Byte> values) {
            addCriterion("info_from not in", values, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromBetween(Byte value1, Byte value2) {
            addCriterion("info_from between", value1, value2, "infoFrom");
            return (Criteria) this;
        }

        public Criteria andInfoFromNotBetween(Byte value1, Byte value2) {
            addCriterion("info_from not between", value1, value2, "infoFrom");
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