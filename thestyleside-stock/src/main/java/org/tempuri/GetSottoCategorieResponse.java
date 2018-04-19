
package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfSottoCategoriaVO;


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
 *         &lt;element name="GetSottoCategorieResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfSottoCategoriaVO" minOccurs="0"/>
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
    "getSottoCategorieResult"
})
@XmlRootElement(name = "GetSottoCategorieResponse")
public class GetSottoCategorieResponse {

    @XmlElementRef(name = "GetSottoCategorieResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSottoCategoriaVO> getSottoCategorieResult;

    /**
     * 获取getSottoCategorieResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSottoCategoriaVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSottoCategoriaVO> getGetSottoCategorieResult() {
        return getSottoCategorieResult;
    }

    /**
     * 设置getSottoCategorieResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSottoCategoriaVO }{@code >}
     *     
     */
    public void setGetSottoCategorieResult(JAXBElement<ArrayOfSottoCategoriaVO> value) {
        this.getSottoCategorieResult = value;
    }

}
