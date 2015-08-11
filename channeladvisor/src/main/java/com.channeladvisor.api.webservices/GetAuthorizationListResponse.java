
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
 *         &lt;element name="GetAuthorizationListResult" type="{http://api.channeladvisor.com/webservices/}APIResultOfArrayOfAuthorizationResponse" minOccurs="0"/&gt;
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
        "getAuthorizationListResult"
})
@XmlRootElement(name = "GetAuthorizationListResponse")
public class GetAuthorizationListResponse {

    @XmlElement(name = "GetAuthorizationListResult")
    protected APIResultOfArrayOfAuthorizationResponse getAuthorizationListResult;

    /**
     * 获取getAuthorizationListResult属性的值。
     *
     * @return
     *     possible object is
     *     {@link APIResultOfArrayOfAuthorizationResponse }
     *
     */
    public APIResultOfArrayOfAuthorizationResponse getGetAuthorizationListResult() {
        return getAuthorizationListResult;
    }

    /**
     * 设置getAuthorizationListResult属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link APIResultOfArrayOfAuthorizationResponse }
     *
     */
    public void setGetAuthorizationListResult(APIResultOfArrayOfAuthorizationResponse value) {
        this.getAuthorizationListResult = value;
    }

}
