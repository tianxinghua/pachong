package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Getter
@Setter
public class SlotProductEditVo {

    private String supplierId ;
    private String slotNo;
    private int count;
    private int successCount;
    private int failCount;
    private List<ErrorConent> errorConent;
    private SlotInfo slotInfo;


    public void addErrorConent(ErrorConent item){

        if(this.errorConent ==null){
            this.errorConent = new ArrayList<ErrorConent>();
        }
        this.errorConent.add(item);
    }
}


