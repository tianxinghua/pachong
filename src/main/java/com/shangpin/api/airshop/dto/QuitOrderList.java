package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 返货列表
 * Created by ZRS on 2016/1/14.
 */
@Setter
@Getter
@ToString
public class QuitOrderList implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer total; //按条件查询的返货单的总数量
    private List<QuitOrders> secondReturnOrders; //查询的返货单的列
}
