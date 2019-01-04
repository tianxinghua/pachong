package com.shangpin.api.airshop.util.enums;

import lombok.Getter;

/**
 * Created by ZRS on 2016/1/22.
 */
@Getter
public enum ReturnOrderCause {

    C0("0", "normal","正常"),
    C1("1", "imperfect","残次"),
    C2("2", "no bar code","无条码"),
    C3("3", "order cancel","订单取消"),
    C4("4", "More than the arrival of the goods","多到货"),
    C5("5", "other","其他"),
    C6("6", "Purchase to cancel","采购取消"),;

    private final String code;
    private final String desc;
    private final String descZh;

    ReturnOrderCause(String code, String desc,String descZh) {
        this.code = code;
        this.desc = desc;
        this.descZh = descZh;
    }

    public static String getDesc(String code) {
        if(code== null){
            return "";
        }
        ReturnOrderCause[] errCode = ReturnOrderCause.values();
        for (ReturnOrderCause returnOrderCause : errCode) {
            if (returnOrderCause.getCode().equals(code)) {
                return returnOrderCause.getDesc();
            }
        }
        return code;
    }

}
