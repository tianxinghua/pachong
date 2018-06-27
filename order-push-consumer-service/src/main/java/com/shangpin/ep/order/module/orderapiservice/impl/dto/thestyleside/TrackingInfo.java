
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TrackingInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="TrackingInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CollectionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ConsigmentDetails" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}consigmentDetails" minOccurs="0"/>
 *         &lt;element name="ConsigmentNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeliveryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HeldIn" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OriginDepot" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReceiverDetails" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}receiverDetails" minOccurs="0"/>
 *         &lt;element name="SenderDetails" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}senderDetails" minOccurs="0"/>
 *         &lt;element name="StatusDetails" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common}ArrayOfstatusDetails" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrackingInfo", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", propOrder = {
    "collectionName",
    "consigmentDetails",
    "consigmentNumber",
    "deliveryDate",
    "heldIn",
    "originDepot",
    "receiverDetails",
    "senderDetails",
    "statusDetails"
})
public class TrackingInfo {

    @XmlElementRef(name = "CollectionName", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> collectionName;
    @XmlElementRef(name = "ConsigmentDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<ConsigmentDetails> consigmentDetails;
    @XmlElementRef(name = "ConsigmentNumber", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> consigmentNumber;
    @XmlElementRef(name = "DeliveryDate", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deliveryDate;
    @XmlElement(name = "HeldIn")
    protected Boolean heldIn;
    @XmlElementRef(name = "OriginDepot", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<String> originDepot;
    @XmlElementRef(name = "ReceiverDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<ReceiverDetails> receiverDetails;
    @XmlElementRef(name = "SenderDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<SenderDetails> senderDetails;
    @XmlElementRef(name = "StatusDetails", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfstatusDetails> statusDetails;

    /**
     * 获取collectionName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCollectionName() {
        return collectionName;
    }

    /**
     * 设置collectionName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCollectionName(JAXBElement<String> value) {
        this.collectionName = value;
    }

    /**
     * 获取consigmentDetails属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ConsigmentDetails }{@code >}
     *     
     */
    public JAXBElement<ConsigmentDetails> getConsigmentDetails() {
        return consigmentDetails;
    }

    /**
     * 设置consigmentDetails属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ConsigmentDetails }{@code >}
     *     
     */
    public void setConsigmentDetails(JAXBElement<ConsigmentDetails> value) {
        this.consigmentDetails = value;
    }

    /**
     * 获取consigmentNumber属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getConsigmentNumber() {
        return consigmentNumber;
    }

    /**
     * 设置consigmentNumber属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setConsigmentNumber(JAXBElement<String> value) {
        this.consigmentNumber = value;
    }

    /**
     * 获取deliveryDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * 设置deliveryDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDeliveryDate(JAXBElement<String> value) {
        this.deliveryDate = value;
    }

    /**
     * 获取heldIn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHeldIn() {
        return heldIn;
    }

    /**
     * 设置heldIn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHeldIn(Boolean value) {
        this.heldIn = value;
    }

    /**
     * 获取originDepot属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOriginDepot() {
        return originDepot;
    }

    /**
     * 设置originDepot属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOriginDepot(JAXBElement<String> value) {
        this.originDepot = value;
    }

    /**
     * 获取receiverDetails属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ReceiverDetails }{@code >}
     *     
     */
    public JAXBElement<ReceiverDetails> getReceiverDetails() {
        return receiverDetails;
    }

    /**
     * 设置receiverDetails属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ReceiverDetails }{@code >}
     *     
     */
    public void setReceiverDetails(JAXBElement<ReceiverDetails> value) {
        this.receiverDetails = value;
    }

    /**
     * 获取senderDetails属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SenderDetails }{@code >}
     *     
     */
    public JAXBElement<SenderDetails> getSenderDetails() {
        return senderDetails;
    }

    /**
     * 设置senderDetails属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SenderDetails }{@code >}
     *     
     */
    public void setSenderDetails(JAXBElement<SenderDetails> value) {
        this.senderDetails = value;
    }

    /**
     * 获取statusDetails属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstatusDetails }{@code >}
     *     
     */
    public JAXBElement<ArrayOfstatusDetails> getStatusDetails() {
        return statusDetails;
    }

    /**
     * 设置statusDetails属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfstatusDetails }{@code >}
     *     
     */
    public void setStatusDetails(JAXBElement<ArrayOfstatusDetails> value) {
        this.statusDetails = value;
    }

}
