
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArticoloFlatExtLocaleVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArticoloFlatExtLocaleVO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArticoloFlatExtVO">
 *       &lt;sequence>
 *         &lt;element name="Locales" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfArticoloLocaleVO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticoloFlatExtLocaleVO", propOrder = {
    "locales"
})
public class ArticoloFlatExtLocaleVO
    extends ArticoloFlatExtVO
{

    @XmlElementRef(name = "Locales", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticoloLocaleVO> locales;

    /**
     * 获取locales属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticoloLocaleVO> getLocales() {
        return locales;
    }

    /**
     * 设置locales属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}
     *     
     */
    public void setLocales(JAXBElement<ArrayOfArticoloLocaleVO> value) {
        this.locales = value;
    }

}
