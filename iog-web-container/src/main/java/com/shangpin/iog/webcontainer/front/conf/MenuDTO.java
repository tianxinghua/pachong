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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
