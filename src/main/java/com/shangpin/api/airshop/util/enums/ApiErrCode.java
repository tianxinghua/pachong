package com.shangpin.api.airshop.util.enums;

import lombok.Getter;

@Getter
public enum ApiErrCode {

    E400("400", "Parameter Error"),
    E421("421", "Please Select Goods!"),
    E422("422", "Can't Find Goods!"),
    E423("423", "Goods Is Not In Same Warehouse!"),
    E424("424", "Goods have Delivered!"),
    E425("425", "Logistics And Goods Info Is Null!"),;

    private final String code;
    private final String desc;

    ApiErrCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getErrorMsg(String code) {
        ApiErrCode[] errCode = ApiErrCode.values();
        for (ApiErrCode apiErrCode : errCode) {
            if (apiErrCode.getCode().equals(code)) {
                return apiErrCode.getDesc();
            }
        }
        return "System error !";
    }
}
