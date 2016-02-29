
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureRetourSKU complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureRetourSKU"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sVente" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sEncaissement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sTVA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMagasin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="scodeCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sUser" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureRetourSKU", propOrder = {
    "sVente",
    "sEncaissement",
    "stva",
    "sMagasin",
    "sDate",
    "scodeCli",
    "sUser",
    "sMdp"
})
public class TEcritureRetourSKU {

    @XmlElement(required = true)
    protected String sVente;
    @XmlElement(required = true)
    protected String sEncaissement;
    @XmlElement(name = "sTVA", required = true)
    protected String stva;
    @XmlElement(required = true)
    protected String sMagasin;
    @XmlElement(required = true)
    protected String sDate;
    @XmlElement(required = true)
    protected String scodeCli;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * 获取sVente属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSVente() {
        return sVente;
    }

    /**
     * 设置sVente属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSVente(String value) {
        this.sVente = value;
    }

    /**
     * 获取sEncaissement属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEncaissement() {
        return sEncaissement;
    }

    /**
     * 设置sEncaissement属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEncaissement(String value) {
        this.sEncaissement = value;
    }

    /**
     * 获取stva属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTVA() {
        return stva;
    }

    /**
     * 设置stva属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTVA(String value) {
        this.stva = value;
    }

    /**
     * 获取sMagasin属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMagasin() {
        return sMagasin;
    }

    /**
     * 设置sMagasin属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMagasin(String value) {
        this.sMagasin = value;
    }

    /**
     * 获取sDate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSDate() {
        return sDate;
    }

    /**
     * 设置sDate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSDate(String value) {
        this.sDate = value;
    }

    /**
     * 获取scodeCli属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScodeCli() {
        return scodeCli;
    }

    /**
     * 设置scodeCli属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScodeCli(String value) {
        this.scodeCli = value;
    }

    /**
     * 获取sUser属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUser() {
        return sUser;
    }

    /**
     * 设置sUser属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUser(String value) {
        this.sUser = value;
    }

    /**
     * 获取sMdp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMdp() {
        return sMdp;
    }

    /**
     * 设置sMdp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMdp(String value) {
        this.sMdp = value;
    }

}
