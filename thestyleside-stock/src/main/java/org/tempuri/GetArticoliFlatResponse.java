
package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatVO;


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
 *         &lt;element name="GetArticoliFlatResult" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArrayOfArticoloFlatVO" minOccurs="0"/>
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
    "getArticoliFlatResult"
})
@XmlRootElement(name = "GetArticoliFlatResponse")
public class GetArticoliFlatResponse {

    @XmlElementRef(name = "GetArticoliFlatResult", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfArticoloFlatVO> getArticoliFlatResult;

    /**
     * ��ȡgetArticoliFlatResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatVO }{@code >}
     *     
     */
    public JAXBElement<ArrayOfArticoloFlatVO> getGetArticoliFlatResult() {
        return getArticoliFlatResult;
    }

    /**
     * ����getArticoliFlatResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatVO }{@code >}
     *     
     */
    public void setGetArticoliFlatResult(JAXBElement<ArrayOfArticoloFlatVO> value) {
        this.getArticoliFlatResult = value;
    }

}