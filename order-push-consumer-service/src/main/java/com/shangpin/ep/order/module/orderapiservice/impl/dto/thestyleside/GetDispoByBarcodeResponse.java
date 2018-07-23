
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="GetDispoByBarcodeResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}MgDispo" minOccurs="0"/>
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
    "getDispoByBarcodeResult"
})
@XmlRootElement(name = "GetDispoByBarcodeResponse")
public class GetDispoByBarcodeResponse {

    @XmlElementRef(name = "GetDispoByBarcodeResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<MgDispo> getDispoByBarcodeResult;

    /**
     * 获取getDispoByBarcodeResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MgDispo }{@code >}
     *     
     */
    public JAXBElement<MgDispo> getGetDispoByBarcodeResult() {
        return getDispoByBarcodeResult;
    }

    /**
     * 设置getDispoByBarcodeResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MgDispo }{@code >}
     *     
     */
    public void setGetDispoByBarcodeResult(JAXBElement<MgDispo> value) {
        this.getDispoByBarcodeResult = value;
    }

}
