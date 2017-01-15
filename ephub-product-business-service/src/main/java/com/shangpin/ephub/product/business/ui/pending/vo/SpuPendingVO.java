package com.shangpin.ephub.product.business.ui.pending.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingVO extends  SpuPendingCommonVO implements Serializable {




    /**
	 * 
	 */
	private static final long serialVersionUID = 8208612922999639039L;

	/**
     * 是否默认供货商
     */
    @JsonProperty("isDefaultSupplier")
    private Boolean isDefaultSupplier;

    /**
     * 供货商图片信息
     */
    @JsonProperty("picVOs")
    private List<SpuPendingPicVO>  picVOs;


    @JsonProperty("isDefaultSupplier")
    public Boolean getIsDefaultSupplier() {
        return isDefaultSupplier;
    }
    @JsonProperty("isDefaultSupplier")
    public void setIsDefaultSupplier(Boolean defaultSupplier) {
        isDefaultSupplier = defaultSupplier;
    }
    @JsonProperty("picVOs")
    public List<SpuPendingPicVO> getPicVOs() {
        return picVOs;
    }
    @JsonProperty("picVOs")
    public void setPicVOs(List<SpuPendingPicVO> picVOs) {
        this.picVOs = picVOs;
    }
}
