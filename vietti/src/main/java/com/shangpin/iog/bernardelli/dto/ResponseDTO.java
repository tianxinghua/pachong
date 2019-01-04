package com.shangpin.iog.bernardelli.dto;

import com.shangpin.iog.bernardelli.dto.org.apache.commons.httpclient.auth.CsvDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ResponseDTO{

    private List<Product> catalog_data;

}
