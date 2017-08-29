package com.shangpin.ephub.product.business.ui.studio.slot.vo;


import java.io.Serializable;
import java.util.List;

import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioDicSupplierInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by Administrator on 2017/6/13.
 */

@Getter
@Setter
@ToString
public class StudioDicSupplierVo implements Serializable{

    private static final long serialVersionUID = 7005555312014316537L;

    private List<StudioDicSupplierInfo> studioDicSupplierInfoList;

    private int total;
}