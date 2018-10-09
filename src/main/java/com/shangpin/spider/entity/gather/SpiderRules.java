package com.shangpin.spider.entity.gather;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Transient;

import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverClickPool;

public class SpiderRules implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long whiteId;

	private Integer threadNum;

	private Integer retryNum;

	private Integer sleep;

	private Integer maxPageGather;

	private Integer timeout;

	private String cookie;

	private String channelRules;

	private String userAgent;

	private String lieUrlReg;

	private String detailUrlReg;

	private String filterUrlReg;

	private String charset;

	private Boolean gatherFirst;
	
	private Boolean headless;

	private Boolean ajaxFlag;
	
	private String nextPageFlag;
	
	private String nextPageTag;
	
	private Boolean firstAjaxFlag;

	private String genderRules;

	private String brandRules;

	private String brandStrategy;

	private String categoryRules;

	private String categoryStrategy;

	private String spuRules;

	private String spuStrategy;

	private String seasonRules;

	private String materialRules;

	private String materialStrategy;

	private String colorRules;

	private String colorStrategy;

	private String sizeRules;

	private String sizeStrategy;

	private String proNameRules;

	private String proNameStrategy;

	private String foreignPriceRules;

	private String foreignPriceStrategy;

	private String domesticPriceRules;

	private String domesticPriceStrategy;

	private String salePriceRules;

	private String salePriceStrategy;

	private String detailLinkRules;

	private String detailLinkStrategy;

	private String madeRules;

	private String madeStrategy;

	private String descriptionRules;

	private String descriptionStrategy;

	private String picsRules;

	private String picsStrategy;

	private String measurementRules;

	private String measurementStrategy;

	private String qtyRules;

	private String qtyStrategy;

	private String jsMenuRules;

	private String jsMenuStrategy;

	private String supplierId;

	private String supplierNo;

	private String clickFieldStr;

	private Boolean status;

	private Date createTime;

	private Date updateTime;

	@Transient
	private String sppuHashRules;
	@Transient
	private String chromeDriverPath;
	@Transient
	private String whiteName;
	@Transient
	private String sourceUrl;
	@Transient
	private SpChromeDriverClickPool driverPool;
	@Transient
	private String[] needClickFieldAry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWhiteId() {
		return whiteId;
	}

	public void setWhiteId(Long whiteId) {
		this.whiteId = whiteId;
	}

	public Integer getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(Integer threadNum) {
		this.threadNum = threadNum;
	}

	public Integer getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}

	public Integer getSleep() {
		return sleep;
	}

	public void setSleep(Integer sleep) {
		this.sleep = sleep;
	}

	public Integer getMaxPageGather() {
		return maxPageGather;
	}

	public void setMaxPageGather(Integer maxPageGather) {
		this.maxPageGather = maxPageGather;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getUserAgent() {
		if (StringUtils.isBlank(userAgent)) {
			userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
		}
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getLieUrlReg() {
		return lieUrlReg;
	}

	public void setLieUrlReg(String lieUrlReg) {
		this.lieUrlReg = lieUrlReg;
	}

	public String getDetailUrlReg() {
		return detailUrlReg;
	}

	public void setDetailUrlReg(String detailUrlReg) {
		this.detailUrlReg = detailUrlReg;
	}

	public String getFilterUrlReg() {
		return filterUrlReg;
	}

	public void setFilterUrlReg(String filterUrlReg) {
		this.filterUrlReg = filterUrlReg;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Boolean getGatherFirst() {
		return gatherFirst;
	}

	public void setGatherFirst(Boolean gatherFirst) {
		this.gatherFirst = gatherFirst;
	}
	
	public Boolean getHeadless() {
		return headless;
	}

	public void setHeadless(Boolean headless) {
		this.headless = headless;
	}

	public Boolean getAjaxFlag() {
		return ajaxFlag;
	}

	public void setAjaxFlag(Boolean ajaxFlag) {
		this.ajaxFlag = ajaxFlag;
	}
	
	public String getNextPageFlag() {
		return nextPageFlag;
	}

	public void setNextPageFlag(String nextPageFlag) {
		this.nextPageFlag = nextPageFlag;
	}
	
	public String getNextPageTag() {
		return nextPageTag;
	}

	public void setNextPageTag(String nextPageTag) {
		this.nextPageTag = nextPageTag;
	}

	public Boolean getFirstAjaxFlag() {
		return firstAjaxFlag;
	}

	public void setFirstAjaxFlag(Boolean firstAjaxFlag) {
		this.firstAjaxFlag = firstAjaxFlag;
	}

	public String getGenderRules() {
		return genderRules;
	}

	public void setGenderRules(String genderRules) {
		this.genderRules = genderRules;
	}

	public String getBrandRules() {
		return brandRules;
	}

	public void setBrandRules(String brandRules) {
		this.brandRules = brandRules;
	}

	public String getBrandStrategy() {
		return brandStrategy;
	}

	public void setBrandStrategy(String brandStrategy) {
		this.brandStrategy = brandStrategy;
	}

	public String getCategoryRules() {
		return categoryRules;
	}

	public void setCategoryRules(String categoryRules) {
		this.categoryRules = categoryRules;
	}

	public String getCategoryStrategy() {
		return categoryStrategy;
	}

	public void setCategoryStrategy(String categoryStrategy) {
		this.categoryStrategy = categoryStrategy;
	}

	public String getSpuRules() {
		return spuRules;
	}

	public void setSpuRules(String spuRules) {
		this.spuRules = spuRules;
	}

	public String getSpuStrategy() {
		return spuStrategy;
	}

	public void setSpuStrategy(String spuStrategy) {
		this.spuStrategy = spuStrategy;
	}

	public String getSeasonRules() {
		return seasonRules;
	}

	public void setSeasonRules(String seasonRules) {
		this.seasonRules = seasonRules;
	}

	public String getMaterialRules() {
		return materialRules;
	}

	public void setMaterialRules(String materialRules) {
		this.materialRules = materialRules;
	}

	public String getMaterialStrategy() {
		return materialStrategy;
	}

	public void setMaterialStrategy(String materialStrategy) {
		this.materialStrategy = materialStrategy;
	}

	public String getColorRules() {
		return colorRules;
	}

	public void setColorRules(String colorRules) {
		this.colorRules = colorRules;
	}

	public String getColorStrategy() {
		return colorStrategy;
	}

	public void setColorStrategy(String colorStrategy) {
		this.colorStrategy = colorStrategy;
	}

	public String getSizeRules() {
		return sizeRules;
	}

	public void setSizeRules(String sizeRules) {
		this.sizeRules = sizeRules;
	}

	public String getSizeStrategy() {
		return sizeStrategy;
	}

	public void setSizeStrategy(String sizeStrategy) {
		this.sizeStrategy = sizeStrategy;
	}

	public String getProNameRules() {
		return proNameRules;
	}

	public void setProNameRules(String proNameRules) {
		this.proNameRules = proNameRules;
	}

	public String getProNameStrategy() {
		return proNameStrategy;
	}

	public void setProNameStrategy(String proNameStrategy) {
		this.proNameStrategy = proNameStrategy;
	}

	public String getForeignPriceRules() {
		return foreignPriceRules;
	}

	public void setForeignPriceRules(String foreignPriceRules) {
		this.foreignPriceRules = foreignPriceRules;
	}

	public String getForeignPriceStrategy() {
		return foreignPriceStrategy;
	}

	public void setForeignPriceStrategy(String foreignPriceStrategy) {
		this.foreignPriceStrategy = foreignPriceStrategy;
	}

	public String getDomesticPriceRules() {
		return domesticPriceRules;
	}

	public void setDomesticPriceRules(String domesticPriceRules) {
		this.domesticPriceRules = domesticPriceRules;
	}

	public String getDomesticPriceStrategy() {
		return domesticPriceStrategy;
	}

	public void setDomesticPriceStrategy(String domesticPriceStrategy) {
		this.domesticPriceStrategy = domesticPriceStrategy;
	}

	public String getSalePriceRules() {
		return salePriceRules;
	}

	public void setSalePriceRules(String salePriceRules) {
		this.salePriceRules = salePriceRules;
	}

	public String getSalePriceStrategy() {
		return salePriceStrategy;
	}

	public void setSalePriceStrategy(String salePriceStrategy) {
		this.salePriceStrategy = salePriceStrategy;
	}

	public String getDetailLinkRules() {
		return detailLinkRules;
	}

	public void setDetailLinkRules(String detailLinkRules) {
		this.detailLinkRules = detailLinkRules;
	}

	public String getDetailLinkStrategy() {
		return detailLinkStrategy;
	}

	public void setDetailLinkStrategy(String detailLinkStrategy) {
		this.detailLinkStrategy = detailLinkStrategy;
	}

	public String getMadeRules() {
		return madeRules;
	}

	public void setMadeRules(String madeRules) {
		this.madeRules = madeRules;
	}

	public String getMadeStrategy() {
		return madeStrategy;
	}

	public void setMadeStrategy(String madeStrategy) {
		this.madeStrategy = madeStrategy;
	}

	public String getDescriptionRules() {
		return descriptionRules;
	}

	public void setDescriptionRules(String descriptionRules) {
		this.descriptionRules = descriptionRules;
	}

	public String getDescriptionStrategy() {
		return descriptionStrategy;
	}

	public void setDescriptionStrategy(String descriptionStrategy) {
		this.descriptionStrategy = descriptionStrategy;
	}

	public String getPicsRules() {
		return picsRules;
	}

	public void setPicsRules(String picsRules) {
		this.picsRules = picsRules;
	}

	public String getPicsStrategy() {
		return picsStrategy;
	}

	public void setPicsStrategy(String picsStrategy) {
		this.picsStrategy = picsStrategy;
	}

	public String getMeasurementRules() {
		return measurementRules;
	}

	public void setMeasurementRules(String measurementRules) {
		this.measurementRules = measurementRules;
	}

	public String getMeasurementStrategy() {
		return measurementStrategy;
	}

	public void setMeasurementStrategy(String measurementStrategy) {
		this.measurementStrategy = measurementStrategy;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierNo() {
		return supplierNo;
	}

	public void setSupplierNo(String supplierNo) {
		this.supplierNo = supplierNo;
	}

	public String getClickFieldStr() {
		return clickFieldStr;
	}

	public void setClickFieldStr(String clickFieldStr) {
		this.clickFieldStr = clickFieldStr;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSppuHashRules() {
		return sppuHashRules;
	}

	public void setSppuHashRules(String sppuHashRules) {
		this.sppuHashRules = sppuHashRules;
	}
	
	public String getChromeDriverPath() {
		return chromeDriverPath;
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public String getWhiteName() {
		return whiteName;
	}

	public void setWhiteName(String whiteName) {
		this.whiteName = whiteName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getChannelRules() {
		return channelRules;
	}

	public void setChannelRules(String channelRules) {
		this.channelRules = channelRules;
	}

	public String getQtyRules() {
		return qtyRules;
	}

	public void setQtyRules(String qtyRules) {
		this.qtyRules = qtyRules;
	}

	public String getQtyStrategy() {
		return qtyStrategy;
	}

	public void setQtyStrategy(String qtyStrategy) {
		this.qtyStrategy = qtyStrategy;
	}

	public String getJsMenuRules() {
		return jsMenuRules;
	}

	public void setJsMenuRules(String jsMenuRules) {
		this.jsMenuRules = jsMenuRules;
	}

	public String getJsMenuStrategy() {
		return jsMenuStrategy;
	}

	public void setJsMenuStrategy(String jsMenuStrategy) {
		this.jsMenuStrategy = jsMenuStrategy;
	}

	public SpChromeDriverClickPool getDriverPool() {
		return driverPool;
	}

	public void setDriverPool(SpChromeDriverClickPool driverPool) {
		this.driverPool = driverPool;
	}

	public String[] getNeedClickFieldAry() {
		return needClickFieldAry;
	}

	public void setNeedClickFieldAry(String[] needClickFieldAry) {
		this.needClickFieldAry = needClickFieldAry;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}