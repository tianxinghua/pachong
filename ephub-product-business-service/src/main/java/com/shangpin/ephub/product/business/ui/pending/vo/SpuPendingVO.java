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
    private String hub_Category;
    
    @JsonProperty("hubColor")
    private String hub_Color;
    
    @JsonProperty("hubMaterial")
    private String hub_Material;




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
    public String getHub_Category() {
        return hub_Category;
    }
    @JsonProperty("hubCategory")
    public void setHub_Category(String hub_Category) {
        this.hub_Category = hub_Category;
    }
    @JsonProperty("hubColor")
    public String getHub_Color() {
        return hub_Color;
    }
    @JsonProperty("hubColor")
    public void setHub_Color(String hub_Color) {
        this.hub_Color = hub_Color;
    }
    @JsonProperty("hubMaterial")
    public String getHub_Material() {
        return hub_Material;
    }
    @JsonProperty("hubMaterial")
    public void setHub_Material(String hub_Material) {
        this.hub_Material = hub_Material;
    }
}
