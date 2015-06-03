/**
 * 
 */
package com.shangpin.iog.webcontainer.front.conf;

import lombok.Getter;
import lombok.Setter;

/**
 * {
        "id": "fb47dd40-9a79-4ed5-b3e0-f74bbe213bd5",
        "parentId": "a5891c96-a9f6-4f8b-847b-fc6354594634",
        "sort": 1,
        "name": "CASE处理",
        "appCode": "",
        "uri": "mvc-action://css/Consultation/ConsultationProcessingList",
        "url": "~/Consultation/ConsultationProcessingList"
    },
 * @description 
 * @author 陈小峰
 * <br/>2015年6月2日
 */
@Setter
@Getter
public class MenuDTO {
	/**
     * 主键唯一标识
     */
    private String id;
    /**
     * 显示名称
     */
    private String name;
    /**
     * uri
     */
    private String uri;
    /**
     * 父级标识
     */
    private String parentId;

    /**
     * 客户端可以根据该字段排序后来显示顺序
     */
    private int sort;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 菜单要跳转的目标地址
     */
    private String url;
}
