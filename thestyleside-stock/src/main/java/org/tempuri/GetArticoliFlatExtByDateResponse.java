
package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtVO;


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
 *         &lt;element name="GetArticoliFlatExtByDateResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfArticoloFlatExtVO" minOccurs="0"/>
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
    "getArticoliFlatExtByDateResult"
})
@XmlRootElement(name = "GetArticoliFlatExtByDateResponse")
public class GetArticoliFlatExtByDateResponse {

    @XmlElementRef(name = "GetArticoliFlatExtByDateResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticoloFlatExtVO> getArticoliFlatExtByDateResult;

    /**
     * 获取getArticoliFlatExtByDateResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticoloFlatExtVO> getGetArticoliFlatExtByDateResult() {
        return getArticoliFlatExtByDateResult;
    }

    /**
     * 设置getArticoliFlatExtByDateResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtVO }{@code >}
     *     
     */
    public void setGetArticoliFlatExtByDateResult(JAXBElement<ArrayOfArticoloFlatExtVO> value) {
        this.getArticoliFlatExtByDateResult = value;
    }

}
