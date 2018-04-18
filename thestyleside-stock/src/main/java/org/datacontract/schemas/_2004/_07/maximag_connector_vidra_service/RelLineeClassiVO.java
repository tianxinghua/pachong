
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RelLineeClassiVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RelLineeClassiVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IDClasseArticolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDLineaArticolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelLineeClassiVO", propOrder = {
    "idClasseArticolo",
    "idLineaArticolo"
})
public class RelLineeClassiVO {

    @XmlElementRef(name = "IDClasseArticolo", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idClasseArticolo;
    @XmlElementRef(name = "IDLineaArticolo", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> idLineaArticolo;

    /**
     * 获取idClasseArticolo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIDClasseArticolo() {
        return idClasseArticolo;
    }

    /**
     * 设置idClasseArticolo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIDClasseArticolo(JAXBElement<String> value) {
        this.idClasseArticolo = value;
    }

    /**
     * 获取idLineaArticolo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIDLineaArticolo() {
        return idLineaArticolo;
    }

    /**
     * 设置idLineaArticolo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIDLineaArticolo(JAXBElement<String> value) {
        this.idLineaArticolo = value;
    }

}
