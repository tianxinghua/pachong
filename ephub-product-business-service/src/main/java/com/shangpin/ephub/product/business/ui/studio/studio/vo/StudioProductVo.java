package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/8.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioProductVo implements Serializable {

    private static final long serialVersionUID = 9106346918684398530L;
    public String supplierId;


}
