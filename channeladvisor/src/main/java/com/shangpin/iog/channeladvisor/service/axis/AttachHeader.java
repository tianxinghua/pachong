package com.shangpin.iog.channeladvisor.service.axis;

/**
 * Created by lizhongren on 2015/8/11.
 */
public class AttachHeader {
    /**
     * 消息节点标签。
     */
    public static final String NODEFLAG = "tns";

    /**
     * 消息头标签。
     */
    public static final String HEADFLAG = "RequestHeader";

    /**
     * 命名空间。
     */
    public static final String NAMESPACE = "http://com.yht.msg";

    /**
     * 时间戳标签。
     */
    public static final String TIMESTAMP = "timeStamp";

    /**
     * 业务编号标签。
     */
    public static final String SERVICEID = "serviceId";

    /**
     * 业务对于的校验密码标签。
     */
    public static final String SERVPASSWORD = "servPassWord";

    /**
     * 时间戳。
     */
    private String timeStamp;

    /**
     * 业务编号。
     */
    private String serviceId;

    /**
     * 业务对于的校验密码。
     */
    private String servPassWord;

    /**
     * 获取时间戳。
     * @return 时间戳。
     */
    public String getTimeStamp()
    {
        return timeStamp;
    }

    /**
     * 设置时间戳。
     * @param timeStamp 时间戳。
     */
    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    /**
     * 获取业务编号。
     * @return 业务编号。
     */
    public String getServiceId()
    {
        return serviceId;
    }

    /**
     * 设置业务编号。
     * @param serviceId 业务编号。
     */
    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }

    /**
     * 获取校验密码。
     * @return 校验密码。
     */
    public String getServPassWord()
    {
        return servPassWord;
    }

    /**
     * 设置校验密码。
     * @param servPassWord 校验密码。
     */
    public void setServPassWord(String servPassWord)
    {
        this.servPassWord = servPassWord;
    }
}
