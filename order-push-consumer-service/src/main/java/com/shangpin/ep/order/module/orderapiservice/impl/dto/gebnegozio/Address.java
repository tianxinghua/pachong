package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/14.
 */
@Getter
@Setter
public class Address {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("region_id")
    @Expose
    private Integer regionId;
    @SerializedName("region_code")
    @Expose
    private String regionCode;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("street")
    @Expose
    private List<String>  street = new ArrayList<String>();
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("middlename")
    @Expose
    private String middlename;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("suffix")
    @Expose
    private String suffix;
    @SerializedName("vat_id")
    @Expose
    private String vatId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("same_as_billing")
    @Expose
    private Integer sameAsBilling;
    @SerializedName("customer_address_id")
    @Expose
    private String customerAddressId;
    @SerializedName("save_in_address_book")
    @Expose
    private String saveInAddressBook;
    @SerializedName("extension_attributes")
    @Expose
    private ExtensionAttributes extensionAttributes;
    @SerializedName("custom_attributes")
    @Expose
    private CustomAttributes customAttributes;
}
