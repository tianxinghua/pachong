package com.shangpin.api.airshop.product.d;

import java.io.Serializable;
import java.util.List;

import lombok.ToString;

/**
 * Api响应类
 */
@ToString
public class ResponseDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3056017587423357767L;

	public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * 响应代码
     * 0:参数错误
     * 1:正确
     * 2:网络连接错误
     * 3:未找到SKU对应关系
     * 4:无库存
     * 5:推送失败
     * 6:异常
     * 7:库存不足或被锁定无法销售
     */
    private Integer responseCode;

    public String getResponseMsg() {
        return responseMsg;
    }

    /*
     * 错误信息
     * */
    private List list;
    
    public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}


	public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    
    
    /**
     * 响应的消息
     * ok:正常，其他表示错误信息
     */
    private String responseMsg;



}
