package com.shangpin.ephub.price.consumer.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lizhongren on 2017/5/9.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierContractDTO {

    @JsonIgnore
    private Integer quoteMode;

    @JsonProperty("QuoteMode")
    public Integer getQuoteMode() {
        return quoteMode;
    }

    @JsonProperty("QuoteMode")
    public void setQuoteMode(Integer quoteMode) {
        this.quoteMode = quoteMode;
    }
}
