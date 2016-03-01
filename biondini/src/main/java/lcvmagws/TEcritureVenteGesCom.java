
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureVenteGesCom complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureVenteGesCom"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sVente" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeMag" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeSite" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeRepresentant" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCod_Reglt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodePromo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="bCoordonees" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sAdrs_Livr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sUser" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCommentaire" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureVenteGesCom", propOrder = {
    "sVente",
    "sCodeCli",
    "sCodeMag",
    "sCodeSite",
    "sCodeRepresentant",
    "sCodReglt",
    "sCodePromo",
    "bCoordonees",
    "sAdrsLivr",
    "sUser",
    "sMdp",
    "sCommentaire"
})
public class TEcritureVenteGesCom {

    @XmlElement(required = true)
    protected String sVente;
    @XmlElement(required = true)
    protected String sCodeCli;
    @XmlElement(required = true)
    protected String sCodeMag;
    @XmlElement(required = true)
    protected String sCodeSite;
    @XmlElement(required = true)
    protected String sCodeRepresentant;
    @XmlElement(name = "sCod_Reglt", required = true)
    protected String sCodReglt;
    @XmlElement(required = true)
    protected String sCodePromo;
    @XmlElement(required = true)
    protected String bCoordonees;
    @XmlElement(name = "sAdrs_Livr", required = true)
    protected String sAdrsLivr;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;
    @XmlElement(required = true)
    protected String sCommentaire;

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
     * 获取sCodeCli属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodeCli() {
        return sCodeCli;
    }

    /**
     * 设置sCodeCli属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodeCli(String value) {
        this.sCodeCli = value;
    }

    /**
     * 获取sCodeMag属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodeMag() {
        return sCodeMag;
    }

    /**
     * 设置sCodeMag属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodeMag(String value) {
        this.sCodeMag = value;
    }

    /**
     * 获取sCodeSite属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodeSite() {
        return sCodeSite;
    }

    /**
     * 设置sCodeSite属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodeSite(String value) {
        this.sCodeSite = value;
    }

    /**
     * 获取sCodeRepresentant属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodeRepresentant() {
        return sCodeRepresentant;
    }

    /**
     * 设置sCodeRepresentant属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodeRepresentant(String value) {
        this.sCodeRepresentant = value;
    }

    /**
     * 获取sCodReglt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodReglt() {
        return sCodReglt;
    }

    /**
     * 设置sCodReglt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodReglt(String value) {
        this.sCodReglt = value;
    }

    /**
     * 获取sCodePromo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodePromo() {
        return sCodePromo;
    }

    /**
     * 设置sCodePromo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodePromo(String value) {
        this.sCodePromo = value;
    }

    /**
     * 获取bCoordonees属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBCoordonees() {
        return bCoordonees;
    }

    /**
     * 设置bCoordonees属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBCoordonees(String value) {
        this.bCoordonees = value;
    }

    /**
     * 获取sAdrsLivr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAdrsLivr() {
        return sAdrsLivr;
    }

    /**
     * 设置sAdrsLivr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAdrsLivr(String value) {
        this.sAdrsLivr = value;
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

    /**
     * 获取sCommentaire属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCommentaire() {
        return sCommentaire;
    }

    /**
     * 设置sCommentaire属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCommentaire(String value) {
        this.sCommentaire = value;
    }

}
