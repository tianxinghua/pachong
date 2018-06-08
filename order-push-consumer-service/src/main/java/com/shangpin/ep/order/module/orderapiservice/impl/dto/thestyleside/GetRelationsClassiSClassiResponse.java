
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetRelationsClassiSClassiResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfRelClassiSClassiVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getRelationsClassiSClassiResult"
})
@XmlRootElement(name = "GetRelationsClassiSClassiResponse")
public class GetRelationsClassiSClassiResponse {

    @XmlElementRef(name = "GetRelationsClassiSClassiResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfRelClassiSClassiVO> getRelationsClassiSClassiResult;

    /**
     * 获取getRelationsClassiSClassiResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRelClassiSClassiVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRelClassiSClassiVO> getGetRelationsClassiSClassiResult() {
        return getRelationsClassiSClassiResult;
    }

    /**
     * 设置getRelationsClassiSClassiResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRelClassiSClassiVO }{@code >}
     *     
     */
    public void setGetRelationsClassiSClassiResult(JAXBElement<ArrayOfRelClassiSClassiVO> value) {
        this.getRelationsClassiSClassiResult = value;
    }

}
