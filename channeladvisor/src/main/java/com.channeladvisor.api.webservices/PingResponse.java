
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
 *         &lt;element name="PingResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfString" minOccurs="0"/&gt;
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
        "pingResult"
})
@XmlRootElement(name = "PingResponse")
public class PingResponse {

    @XmlElement(name = "PingResult")
    protected APIResultOfString pingResult;

    /**
     * 获取pingResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link APIResultOfString }
     *
     */
    public APIResultOfString getPingResult() {
        return pingResult;
    }

    /**
     * 设置pingResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link APIResultOfString }
     *
     */
    public void setPingResult(APIResultOfString value) {
        this.pingResult = value;
    }

}
