
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>receiverDetails complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="receiverDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DestinationCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DestinationDepot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DestinationTown" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "receiverDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", propOrder = {
    "destinationCountry",
    "destinationDepot",
    "destinationTown"
})
public class ReceiverDetails {

    @XmlElementRef(name = "DestinationCountry", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> destinationCountry;
    @XmlElementRef(name = "DestinationDepot", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> destinationDepot;
    @XmlElementRef(name = "DestinationTown", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> destinationTown;

    /**
     * 获取destinationCountry属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDestinationCountry() {
        return destinationCountry;
    }

    /**
     * 设置destinationCountry属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDestinationCountry(JAXBElement<String> value) {
        this.destinationCountry = value;
    }

    /**
     * 获取destinationDepot属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDestinationDepot() {
        return destinationDepot;
    }

    /**
     * 设置destinationDepot属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDestinationDepot(JAXBElement<String> value) {
        this.destinationDepot = value;
    }

    /**
     * 获取destinationTown属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDestinationTown() {
        return destinationTown;
    }

    /**
     * 设置destinationTown属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDestinationTown(JAXBElement<String> value) {
        this.destinationTown = value;
    }

}
