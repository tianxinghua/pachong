
package com.channeladvisor.api.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>AuthorizationResponse complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="AuthorizationResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AccountID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LocalID" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="AccountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AccountType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ResourceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthorizationResponse", propOrder = {
        "accountID",
        "localID",
        "accountName",
        "accountType",
        "resourceName",
        "status"
})
public class AuthorizationResponse {

    @XmlElement(name = "AccountID")
    protected String accountID;
    @XmlElement(name = "LocalID")
    protected int localID;
    @XmlElement(name = "AccountName")
    protected String accountName;
    @XmlElement(name = "AccountType")
    protected String accountType;
    @XmlElement(name = "ResourceName")
    protected String resourceName;
    @XmlElement(name = "Status")
    protected String status;

    /**
     * 获取accountID属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountID() {
        return accountID;
    }

    /**
     * 设置accountID属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountID(String value) {
        this.accountID = value;
    }

    /**
     * 获取localID属性的值。
     *
     */
    public int getLocalID() {
        return localID;
    }

    /**
     * 设置localID属性的值。
     *
     */
    public void setLocalID(int value) {
        this.localID = value;
    }

    /**
     * 获取accountName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置accountName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * 获取accountType属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * 设置accountType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAccountType(String value) {
        this.accountType = value;
    }

    /**
     * 获取resourceName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * 设置resourceName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setResourceName(String value) {
        this.resourceName = value;
    }

    /**
     * 获取status属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置status属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

}
