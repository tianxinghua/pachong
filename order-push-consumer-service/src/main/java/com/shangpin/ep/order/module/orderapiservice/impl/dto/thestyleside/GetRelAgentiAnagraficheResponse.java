
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetRelAgentiAnagraficheResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfRelAgentiAnagraficheVO" minOccurs="0"/>
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
    "getRelAgentiAnagraficheResult"
})
@XmlRootElement(name = "GetRelAgentiAnagraficheResponse")
public class GetRelAgentiAnagraficheResponse {

    @XmlElementRef(name = "GetRelAgentiAnagraficheResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfRelAgentiAnagraficheVO> getRelAgentiAnagraficheResult;

    /**
     * ��ȡgetRelAgentiAnagraficheResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRelAgentiAnagraficheVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfRelAgentiAnagraficheVO> getGetRelAgentiAnagraficheResult() {
        return getRelAgentiAnagraficheResult;
    }

    /**
     * ����getRelAgentiAnagraficheResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfRelAgentiAnagraficheVO }{@code >}
     *     
     */
    public void setGetRelAgentiAnagraficheResult(JAXBElement<ArrayOfRelAgentiAnagraficheVO> value) {
        this.getRelAgentiAnagraficheResult = value;
    }

}
