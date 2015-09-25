package com.shangpin.iog.dante5.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Shipping
 * Created by kelseo on 15/9/18.
 */
public class Shipping {
    @SerializedName("g:country")
    String country;
    @SerializedName("g:price")
    String price;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Shipping{" +
                "country='" + country + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
