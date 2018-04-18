
package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloLocaleVO;


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
 *         &lt;element name="GetArticoliLocaleResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfArticoloLocaleVO" minOccurs="0"/>
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
    "getArticoliLocaleResult"
})
@XmlRootElement(name = "GetArticoliLocaleResponse")
public class GetArticoliLocaleResponse {

    @XmlElementRef(name = "GetArticoliLocaleResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticoloLocaleVO> getArticoliLocaleResult;

    /**
     * 获取getArticoliLocaleResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticoloLocaleVO> getGetArticoliLocaleResult() {
        return getArticoliLocaleResult;
    }

    /**
     * 设置getArticoliLocaleResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}
     *     
     */
    public void setGetArticoliLocaleResult(JAXBElement<ArrayOfArticoloLocaleVO> value) {
        this.getArticoliLocaleResult = value;
    }

}
