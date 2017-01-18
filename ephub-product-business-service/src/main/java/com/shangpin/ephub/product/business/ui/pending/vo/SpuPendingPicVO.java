package com.shangpin.ephub.product.business.ui.pending.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by loyalty on 16/12/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingPicVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 9154713134184232038L;

	/**
     * spuPengding表主键
     */
    @JsonProperty("spuPendingId")
    private Long spuPendingId;

    /**
     * 图片存储的主键
     */
    @JsonProperty("picId")
    private Long  picId;
    /**
     * 尚品的图片链接
     */
    @JsonProperty("spPicUrl")
    private String spPicUrl;


    @JsonProperty("spuPendingId")
    public Long getSpuPendingId() {
        return spuPendingId;
    }
    @JsonProperty("spuPendingId")
    public void setSpuPendingId(Long spuPendingId) {
        this.spuPendingId = spuPendingId;
    }
    @JsonProperty("picId")
    public Long getPicId() {
        return picId;
    }
    @JsonProperty("picId")
    public void setPicId(Long picId) {
        this.picId = picId;
    }
    @JsonProperty("spPicUrl")
    public String getSpPicUrl() {
        return spPicUrl;
    }
    @JsonProperty("spPicUrl")
    public void setSpPicUrl(String spPicUrl) {
        this.spPicUrl = spPicUrl;
    }
}
