
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
 *         &lt;element name="usr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customer" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}AnagraficaVO" minOccurs="0"/>
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
    "usr",
    "pwd",
    "customer"
})
@XmlRootElement(name = "InsertCustomer")
public class InsertCustomer {

    @XmlElementRef(name = "usr", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> usr;
    @XmlElementRef(name = "pwd", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> pwd;
    @XmlElementRef(name = "customer", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<AnagraficaVO> customer;

    /**
     * ��ȡusr���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUsr() {
        return usr;
    }

    /**
     * ����usr���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUsr(JAXBElement<String> value) {
        this.usr = value;
    }

    /**
     * ��ȡpwd���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPwd() {
        return pwd;
    }

    /**
     * ����pwd���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPwd(JAXBElement<String> value) {
        this.pwd = value;
    }

    /**
     * ��ȡcustomer���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AnagraficaVO }{@code >}
     *     
     */
    public JAXBElement<AnagraficaVO> getCustomer() {
        return customer;
    }

    /**
     * ����customer���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AnagraficaVO }{@code >}
     *     
     */
    public void setCustomer(JAXBElement<AnagraficaVO> value) {
        this.customer = value;
    }

}
