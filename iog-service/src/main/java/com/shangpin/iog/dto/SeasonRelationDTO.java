package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by loyalty on 15/10/2.
 * 供货商Season和尚品季节关系
 */
@Setter
@Getter
public class SeasonRelationDTO {
    private String supplierId;//供货商门户ID
    private String supplierNo;//供货商门户ID
    private String supplierSeason;
    private String spYear;
    private String spSeason;
    private String currentSeason;
   
}
