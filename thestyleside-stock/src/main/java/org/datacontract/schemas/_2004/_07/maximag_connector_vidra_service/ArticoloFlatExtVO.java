
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>ArticoloFlatExtVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArticoloFlatExtVO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArticoloFlatVO">
 *       &lt;sequence>
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ExtendedNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExtendedNote1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ImageURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ItemClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ItemLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Listini" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfListinoVO" minOccurs="0"/>
 *         &lt;element name="Quantita" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}MgDispo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticoloFlatExtVO", propOrder = {
    "creationDate",
    "extendedNote",
    "extendedNote1",
    "imageURL",
    "itemClass",
    "itemLine",
    "listini",
    "quantita"
})
@XmlSeeAlso({
    ArticoloFlatExtLocaleVO.class
})
public class ArticoloFlatExtVO
    extends ArticoloFlatVO
{

    @XmlElement(name = "CreationDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    @XmlElementRef(name = "ExtendedNote", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> extendedNote;
    @XmlElementRef(name = "ExtendedNote1", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> extendedNote1;
    @XmlElementRef(name = "ImageURL", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> imageURL;
    @XmlElementRef(name = "ItemClass", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> itemClass;
    @XmlElementRef(name = "ItemLine", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> itemLine;
    @XmlElementRef(name = "Listini", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfListinoVO> listini;
    @XmlElementRef(name = "Quantita", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<MgDispo> quantita;

    /**
     * 获取creationDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDate() {
        return creationDate;
    }

    /**
     * 设置creationDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDate(XMLGregorianCalendar value) {
        this.creationDate = value;
    }

    /**
     * 获取extendedNote属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getExtendedNote() {
        return extendedNote;
    }

    /**
     * 设置extendedNote属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setExtendedNote(JAXBElement<String> value) {
        this.extendedNote = value;
    }

    /**
     * 获取extendedNote1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getExtendedNote1() {
        return extendedNote1;
    }

    /**
     * 设置extendedNote1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setExtendedNote1(JAXBElement<String> value) {
        this.extendedNote1 = value;
    }

    /**
     * 获取imageURL属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getImageURL() {
        return imageURL;
    }

    /**
     * 设置imageURL属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setImageURL(JAXBElement<String> value) {
        this.imageURL = value;
    }

    /**
     * 获取itemClass属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getItemClass() {
        return itemClass;
    }

    /**
     * 设置itemClass属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setItemClass(JAXBElement<String> value) {
        this.itemClass = value;
    }

    /**
     * 获取itemLine属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getItemLine() {
        return itemLine;
    }

    /**
     * 设置itemLine属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setItemLine(JAXBElement<String> value) {
        this.itemLine = value;
    }

    /**
     * 获取listini属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfListinoVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfListinoVO> getListini() {
        return listini;
    }

    /**
     * 设置listini属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfListinoVO }{@code >}
     *     
     */
    public void setListini(JAXBElement<ArrayOfListinoVO> value) {
        this.listini = value;
    }

    /**
     * 获取quantita属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MgDispo }{@code >}
     *     
     */
    public JAXBElement<MgDispo> getQuantita() {
        return quantita;
    }

    /**
     * 设置quantita属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MgDispo }{@code >}
     *     
     */
    public void setQuantita(JAXBElement<MgDispo> value) {
        this.quantita = value;
    }

}
