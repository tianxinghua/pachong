package com.shangpin.iog.spinnaker.stock.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SeasoncodeList {

    private Integer number_of_seasoncodes;
    private List<Seasoncode> SeasonCode;

}
