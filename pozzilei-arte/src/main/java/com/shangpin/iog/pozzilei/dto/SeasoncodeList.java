package com.shangpin.iog.pozzilei.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SeasoncodeList {

    public List<Seasoncode> getSeasonCode() {
        return SeasonCode;
    }

    public void setSeasonCode(List<Seasoncode> seasonCode) {
        SeasonCode = seasonCode;
    }

    public Integer getNumber_of_seasoncodes() {
        return number_of_seasoncodes;
    }

    public void setNumber_of_seasoncodes(Integer number_of_seasoncodes) {
        this.number_of_seasoncodes = number_of_seasoncodes;
    }

    private Integer number_of_seasoncodes;

    private List<Seasoncode> SeasonCode;

}
