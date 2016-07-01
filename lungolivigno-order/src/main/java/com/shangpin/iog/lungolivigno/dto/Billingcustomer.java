/**
  * Copyright 2016 aTool.org 
  */
package com.shangpin.iog.lungolivigno.dto;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
/**
 * Auto-generated: 2016-05-31 9:41:17
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
@Getter
@Setter
public class Billingcustomer {

    @JsonProperty("ID")
    private String ID;
    @JsonProperty("FirstName")
    private String FirstName;
    @JsonProperty("LastName")
    private String LastName;
    @JsonProperty("Address")
    private String Address;
    @JsonProperty("ZipCode")
    private String ZipCode;
    @JsonProperty("City")
    private String City;
    @JsonProperty("State")
    private String State;
    @JsonProperty("Country")
    private String Country;
    @JsonProperty("Phone")
    private String Phone;
    @JsonProperty("Email")
    private String Email;
    @JsonProperty("VatNumber")
    private String VatNumber;
    @JsonProperty("FiscalCode")
    private String FiscalCode;
}