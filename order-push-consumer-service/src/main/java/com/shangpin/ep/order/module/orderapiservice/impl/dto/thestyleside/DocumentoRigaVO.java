
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DocumentoRigaVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="DocumentoRigaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BarCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Descrizione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Discount" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="DiscountAmount" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="IDDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDDocumentoContatore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDDocumentoContatoreRif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Price" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="Qty" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoRigaVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "barCode",
    "descrizione",
    "discount",
    "discountAmount",
    "idDocumento",
    "idDocumentoContatore",
    "idDocumentoContatoreRif",
    "price",
    "qty"
})
public class DocumentoRigaVO {

    @XmlElementRef(name = "BarCode", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> barCode;
    @XmlElementRef(name = "Descrizione", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> descrizione;
    @XmlElement(name = "Discount")
    protected Float discount;
    @XmlElement(name = "DiscountAmount")
    protected Float discountAmount;
    @XmlElementRef(name = "IDDocumento", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idDocumento;
    @XmlElementRef(name = "IDDocumentoContatore", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idDocumentoContatore;
    @XmlElementRef(name = "IDDocumentoContatoreRif", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idDocumentoContatoreRif;
    @XmlElement(name = "Price")
    protected Float price;
    @XmlElement(name = "Qty")
    protected Integer qty;

    /**
     * 获取barCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBarCode() {
        return barCode;
    }

    /**
     * 设置barCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBarCode(JAXBElement<String> value) {
        this.barCode = value;
    }

    /**
     * 获取descrizione属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescrizione() {
        return descrizione;
    }

    /**
     * 设置descrizione属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescrizione(JAXBElement<String> value) {
        this.descrizione = value;
    }

    /**
     * 获取discount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getDiscount() {
        return discount;
    }

    /**
     * 设置discount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDiscount(Float value) {
        this.discount = value;
    }

    /**
     * 获取discountAmount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getDiscountAmount() {
        return discountAmount;
    }

    /**
     * 设置discountAmount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDiscountAmount(Float value) {
        this.discountAmount = value;
    }

    /**
     * 获取idDocumento属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIDDocumento() {
        return idDocumento;
    }

    /**
     * 设置idDocumento属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIDDocumento(JAXBElement<String> value) {
        this.idDocumento = value;
    }

    /**
     * 获取idDocumentoContatore属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIDDocumentoContatore() {
        return idDocumentoContatore;
    }

    /**
     * 设置idDocumentoContatore属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIDDocumentoContatore(JAXBElement<String> value) {
        this.idDocumentoContatore = value;
    }

    /**
     * 获取idDocumentoContatoreRif属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIDDocumentoContatoreRif() {
        return idDocumentoContatoreRif;
    }

    /**
     * 设置idDocumentoContatoreRif属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIDDocumentoContatoreRif(JAXBElement<String> value) {
        this.idDocumentoContatoreRif = value;
    }

    /**
     * 获取price属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPrice() {
        return price;
    }

    /**
     * 设置price属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPrice(Float value) {
        this.price = value;
    }

    /**
     * 获取qty属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQty() {
        return qty;
    }

    /**
     * 设置qty属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQty(Integer value) {
        this.qty = value;
    }

}
