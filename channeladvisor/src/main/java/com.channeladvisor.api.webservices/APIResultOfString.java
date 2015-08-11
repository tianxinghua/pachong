
package com.channeladvisor.api.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>APIResultOfString complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="APIResultOfString"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Status" type="{http://api.channeladvisor.com/webservices/}ResultStatus"/&gt;
 *         &lt;element name="MessageCode" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResultData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "APIResultOfString", propOrder = {
        "status",
        "messageCode",
        "message",
        "data",
        "resultData"
})
public class APIResultOfString {

    @XmlElement(name = "Status", required = true)
    @XmlSchemaType(name = "string")
    protected ResultStatus status;
    @XmlElement(name = "MessageCode")
    protected int messageCode;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "Data")
    protected String data;
    @XmlElement(name = "ResultData")
    protected String resultData;

    /**
     * 获取status属性的值。
     *
     * @return
     *     possible object is
     *     {@link ResultStatus }
     *
     */
    public ResultStatus getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link ResultStatus }
     *
     */
    public void setStatus(ResultStatus value) {
        this.status = value;
    }

    /**
     * 获取messageCode属性的值。
     *
     */
    public int getMessageCode() {
        return messageCode;
    }

    /**
     * 设置messageCode属性的值。
     *
     */
    public void setMessageCode(int value) {
        this.messageCode = value;
    }

    /**
     * 获取message属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * 获取data属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getData() {
        return data;
    }

    /**
     * 设置data属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * 获取resultData属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResultData() {
        return resultData;
    }

    /**
     * 设置resultData属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResultData(String value) {
        this.resultData = value;
    }

}
