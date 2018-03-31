package com.shangpin.iog.redi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Spu implements Serializable {


    // skuno
    private String item_id;


    private String item_created;
    private String item_update;

    private List<Sku> item_sizes;

}
