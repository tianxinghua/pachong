
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
 *         &lt;element name="GetSottoSottoGruppoResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfSottoSottoGruppoVO" minOccurs="0"/>
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
    "getSottoSottoGruppoResult"
})
@XmlRootElement(name = "GetSottoSottoGruppoResponse")
public class GetSottoSottoGruppoResponse {

    @XmlElementRef(name = "GetSottoSottoGruppoResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfSottoSottoGruppoVO> getSottoSottoGruppoResult;

    /**
     * ��ȡgetSottoSottoGruppoResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoGruppoVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfSottoSottoGruppoVO> getGetSottoSottoGruppoResult() {
        return getSottoSottoGruppoResult;
    }

    /**
     * ����getSottoSottoGruppoResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoGruppoVO }{@code >}
     *     
     */
    public void setGetSottoSottoGruppoResult(JAXBElement<ArrayOfSottoSottoGruppoVO> value) {
        this.getSottoSottoGruppoResult = value;
    }

}
