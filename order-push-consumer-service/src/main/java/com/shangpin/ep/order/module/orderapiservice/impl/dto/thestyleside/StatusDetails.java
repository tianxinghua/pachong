
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>statusDetails complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="statusDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CarrierStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusCode" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}TrackingStatus" minOccurs="0"/>
 *         &lt;element name="StatusDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusDepot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StatusDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statusDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", propOrder = {
    "carrierStatusCode",
    "statusCode",
    "statusDate",
    "statusDepot",
    "statusDescription"
})
public class StatusDetails {

    @XmlElementRef(name = "CarrierStatusCode", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> carrierStatusCode;
    @XmlElement(name = "StatusCode")
    @XmlSchemaType(name = "string")
    protected TrackingStatus statusCode;
    @XmlElementRef(name = "StatusDate", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> statusDate;
    @XmlElementRef(name = "StatusDepot", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> statusDepot;
    @XmlElementRef(name = "StatusDescription", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> statusDescription;

    /**
     * ��ȡcarrierStatusCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCarrierStatusCode() {
        return carrierStatusCode;
    }

    /**
     * ����carrierStatusCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCarrierStatusCode(JAXBElement<String> value) {
        this.carrierStatusCode = value;
    }

    /**
     * ��ȡstatusCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrackingStatus }
     *     
     */
    public TrackingStatus getStatusCode() {
        return statusCode;
    }

    /**
     * ����statusCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrackingStatus }
     *     
     */
    public void setStatusCode(TrackingStatus value) {
        this.statusCode = value;
    }

    /**
     * ��ȡstatusDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatusDate() {
        return statusDate;
    }

    /**
     * ����statusDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatusDate(JAXBElement<String> value) {
        this.statusDate = value;
    }

    /**
     * ��ȡstatusDepot���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatusDepot() {
        return statusDepot;
    }

    /**
     * ����statusDepot���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatusDepot(JAXBElement<String> value) {
        this.statusDepot = value;
    }

    /**
     * ��ȡstatusDescription���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatusDescription() {
        return statusDescription;
    }

    /**
     * ����statusDescription���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatusDescription(JAXBElement<String> value) {
        this.statusDescription = value;
    }

}
