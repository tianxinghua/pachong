
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureDuStock complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tLectureDuStock"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sArticle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="bCData" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "tLectureDuStock", propOrder = {
    "sArticle",
    "bcData",
    "sUser",
    "sMdp"
})
public class TLectureDuStock {

    @XmlElement(required = true)
    protected String sArticle;
    @XmlElement(name = "bCData", required = true)
    protected String bcData;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * 获取sArticle属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSArticle() {
        return sArticle;
    }

    /**
     * 设置sArticle属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSArticle(String value) {
        this.sArticle = value;
    }

    /**
     * 获取bcData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBCData() {
        return bcData;
    }

    /**
     * 设置bcData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBCData(String value) {
        this.bcData = value;
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
