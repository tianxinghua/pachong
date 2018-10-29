package com.shangpin.iog.spinnaker.stock.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/7/8.
 */
@Setter
@Getter
public class SeasoncodeList {

    private Integer number_of_seasoncodes;

    private List<Seasoncode> SeasonCode;
    
    private String result;

}
