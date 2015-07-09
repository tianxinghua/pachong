package com.shangpin.iog.apennine.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by sunny on 2015/7/7.
 */

public class StockDTO {
    private int Hkstock;
    private String Scode;

    public int getHkstock() {
        return Hkstock;
    }

    public void setHkstock(int hkstock) {
        Hkstock = hkstock;
    }

    public String getScode() {
        return Scode;
    }

    public void setScode(String scode) {
        Scode = scode;
    }
}
