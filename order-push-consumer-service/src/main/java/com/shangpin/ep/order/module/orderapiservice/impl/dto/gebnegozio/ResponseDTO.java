package com.shangpin.ep.order.module.orderapiservice.impl.dto.gebnegozio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class ResponseDTO {
    private List<GebnegozioDTO> items = new ArrayList<GebnegozioDTO>();//产品
}
