
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetTrackingResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}ArrayOfTrackingInfo" minOccurs="0"/>
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
    "getTrackingResult"
})
@XmlRootElement(name = "GetTrackingResponse")
public class GetTrackingResponse {

    @XmlElementRef(name = "GetTrackingResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTrackingInfo> getTrackingResult;

    /**
     * ��ȡgetTrackingResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTrackingInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTrackingInfo> getGetTrackingResult() {
        return getTrackingResult;
    }

    /**
     * ����getTrackingResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTrackingInfo }{@code >}
     *     
     */
    public void setGetTrackingResult(JAXBElement<ArrayOfTrackingInfo> value) {
        this.getTrackingResult = value;
    }

}
