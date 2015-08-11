
package com.channeladvisor.api.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RequestAccessResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfBoolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "requestAccessResult"
})
@XmlRootElement(name = "RequestAccessResponse")
public class RequestAccessResponse {

    @XmlElement(name = "RequestAccessResult")
    protected APIResultOfBoolean requestAccessResult;

    /**
     * 获取requestAccessResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link APIResultOfBoolean }
     *
     */
    public APIResultOfBoolean getRequestAccessResult() {
        return requestAccessResult;
    }

    /**
     * 设置requestAccessResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link APIResultOfBoolean }
     *
     */
    public void setRequestAccessResult(APIResultOfBoolean value) {
        this.requestAccessResult = value;
    }

}
