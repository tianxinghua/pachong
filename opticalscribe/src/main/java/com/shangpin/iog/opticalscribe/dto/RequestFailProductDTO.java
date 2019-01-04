package com.shangpin.iog.opticalscribe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 9:54 2018/6/29
 * @Description:
 */
@Getter
@Setter
@ToString
public class RequestFailProductDTO {

    private String gender;

    private String categoryName;

    //商品详情url or 商品品类 url
    private String url;

    // 0 :商品url       1:商品品类url
    private String flag;

    public RequestFailProductDTO(String gender, String categoryName, String url, String flag) {
        this.gender = gender;
        this.categoryName = categoryName;
        this.url = url;
        this.flag = flag;
    }

    public RequestFailProductDTO() {
    }
}
