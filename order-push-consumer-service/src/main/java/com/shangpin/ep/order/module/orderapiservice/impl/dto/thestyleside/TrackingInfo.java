
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TrackingInfo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡcollectionName���Ե�ֵ��
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
     * ����collectionName���Ե�ֵ��
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
     * ��ȡconsigmentDetails���Ե�ֵ��
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
     * ����consigmentDetails���Ե�ֵ��
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
     * ��ȡconsigmentNumber���Ե�ֵ��
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
     * ����consigmentNumber���Ե�ֵ��
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
     * ��ȡdeliveryDate���Ե�ֵ��
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
     * ����deliveryDate���Ե�ֵ��
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
     * ��ȡheldIn���Ե�ֵ��
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
     * ����heldIn���Ե�ֵ��
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
     * ��ȡoriginDepot���Ե�ֵ��
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
     * ����originDepot���Ե�ֵ��
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
     * ��ȡreceiverDetails���Ե�ֵ��
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
     * ����receiverDetails���Ե�ֵ��
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
     * ��ȡsenderDetails���Ե�ֵ��
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
     * ����senderDetails���Ե�ֵ��
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
     * ��ȡstatusDetails���Ե�ֵ��
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
     * ����statusDetails���Ե�ֵ��
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
