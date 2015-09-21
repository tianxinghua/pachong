package com.shangpin.iog.dante5.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Shipping
 * Created by kelseo on 15/9/18.
 */
public class Tax {
    @SerializedName("g:country")
    String country;
    @SerializedName("g:rate")
    String rate;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "country='" + country + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
