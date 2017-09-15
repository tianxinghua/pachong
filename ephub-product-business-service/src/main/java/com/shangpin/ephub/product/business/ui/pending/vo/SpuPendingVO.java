package com.shangpin.ephub.product.business.ui.pending.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("isHaveHubSpu")
    private Boolean isHaveHubSpu;
    @JsonProperty("isHaveShootPic")
    private  Boolean isHaveShootPic;

    //hub的品类
    @JsonProperty("hubCategory")
    private String hubCategory;
    @JsonProperty("hubColor")
    private String hubColor;
    @JsonProperty("hubMaterial")
    private String hubMaterial;




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


    @JsonProperty("isHaveHubSpu")
    public Boolean getHaveHubSpu() {
        return isHaveHubSpu;
    }
    @JsonProperty("isHaveHubSpu")
    public void setHaveHubSpu(Boolean haveHubSpu) {
        isHaveHubSpu = haveHubSpu;
    }

    @JsonProperty("isHaveShootPic")
    public Boolean getHaveShootPic() {
        return isHaveShootPic;
    }
    @JsonProperty("isHaveShootPic")
    public void setHaveShootPic(Boolean haveShootPic) {
        isHaveShootPic = haveShootPic;
    }

    @JsonProperty("hubCategory")
    public String getHubCategory() {
        return hubCategory;
    }
    @JsonProperty("hubCategory")
    public void setHubCategory(String hubCategory) {
        this.hubCategory = hubCategory;
    }
    @JsonProperty("hubColor")
    public String getHubColor() {
        return hubColor;
    }
    @JsonProperty("hubColor")
    public void setHubColor(String hubColor) {
        this.hubColor = hubColor;
    }
    @JsonProperty("hubMaterial")
    public String getHubMaterial() {
        return hubMaterial;
    }
    @JsonProperty("hubMaterial")
    public void setHubMaterial(String hubMaterial) {
        this.hubMaterial = hubMaterial;
    }
}
