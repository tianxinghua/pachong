/**
 * 
 */
package com.shangpin.framework.page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @description 分页对象
 * @author 陈小峰
 * <br/>2014年12月18日
 */
@Setter
@Getter
@NoArgsConstructor
public class Page<T> implements Serializable{

	/**
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	/**总记录数*/
    private long total=0;
    /**分页结果*/
    private List<T> items;
    /**总页数*/
    private int totalPage=0;
    /**当前页码*/
    private int currentPage=0;
    /**每页显示数量*/
    private int pageSize=0;

    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}
