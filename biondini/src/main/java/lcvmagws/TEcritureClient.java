
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureClient complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureClient"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sEmail" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sNomCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sPrenomCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sAdr1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sAdr2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCP" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sVille" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodePays" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdPInternet" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sSociete" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sNumTVA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sTel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sAdr_Livr" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sDateNaissance" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCodeSite" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sUser" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sCiv" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sTypClient" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureClient", propOrder = {
    "sEmail",
    "sNomCli",
    "sPrenomCli",
    "sAdr1",
    "sAdr2",
    "scp",
    "sVille",
    "sCodePays",
    "sMdPInternet",
    "sSociete",
    "sNumTVA",
    "sTel",
    "sAdrLivr",
    "sDateNaissance",
    "sCodeCli",
    "sCodeSite",
    "sUser",
    "sMdp",
    "sCiv",
    "sTypClient"
})
public class TEcritureClient {

    @XmlElement(required = true)
    protected String sEmail;
    @XmlElement(required = true)
    protected String sNomCli;
    @XmlElement(required = true)
    protected String sPrenomCli;
    @XmlElement(required = true)
    protected String sAdr1;
    @XmlElement(required = true)
    protected String sAdr2;
    @XmlElement(name = "sCP", required = true)
    protected String scp;
    @XmlElement(required = true)
    protected String sVille;
    @XmlElement(required = true)
    protected String sCodePays;
    @XmlElement(required = true)
    protected String sMdPInternet;
    @XmlElement(required = true)
    protected String sSociete;
    @XmlElement(required = true)
    protected String sNumTVA;
    @XmlElement(required = true)
    protected String sTel;
    @XmlElement(name = "sAdr_Livr", required = true)
    protected String sAdrLivr;
    @XmlElement(required = true)
    protected String sDateNaissance;
    @XmlElement(required = true)
    protected String sCodeCli;
    @XmlElement(required = true)
    protected String sCodeSite;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;
    @XmlElement(required = true)
    protected String sCiv;
    @XmlElement(required = true)
    protected String sTypClient;

    /**
     * 获取sEmail属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEmail() {
        return sEmail;
    }

    /**
     * 设置sEmail属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEmail(String value) {
        this.sEmail = value;
    }

    /**
     * 获取sNomCli属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNomCli() {
        return sNomCli;
    }

    /**
     * 设置sNomCli属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNomCli(String value) {
        this.sNomCli = value;
    }

    /**
     * 获取sPrenomCli属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPrenomCli() {
        return sPrenomCli;
    }

    /**
     * 设置sPrenomCli属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPrenomCli(String value) {
        this.sPrenomCli = value;
    }

    /**
     * 获取sAdr1属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAdr1() {
        return sAdr1;
    }

    /**
     * 设置sAdr1属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAdr1(String value) {
        this.sAdr1 = value;
    }

    /**
     * 获取sAdr2属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAdr2() {
        return sAdr2;
    }

    /**
     * 设置sAdr2属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAdr2(String value) {
        this.sAdr2 = value;
    }

    /**
     * 获取scp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCP() {
        return scp;
    }

    /**
     * 设置scp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCP(String value) {
        this.scp = value;
    }

    /**
     * 获取sVille属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSVille() {
        return sVille;
    }

    /**
     * 设置sVille属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSVille(String value) {
        this.sVille = value;
    }

    /**
     * 获取sCodePays属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodePays() {
        return sCodePays;
    }

    /**
     * 设置sCodePays属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodePays(String value) {
        this.sCodePays = value;
    }

    /**
     * 获取sMdPInternet属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMdPInternet() {
        return sMdPInternet;
    }

    /**
     * 设置sMdPInternet属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMdPInternet(String value) {
        this.sMdPInternet = value;
    }

    /**
     * 获取sSociete属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSociete() {
        return sSociete;
    }

    /**
     * 设置sSociete属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSociete(String value) {
        this.sSociete = value;
    }

    /**
     * 获取sNumTVA属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNumTVA() {
        return sNumTVA;
    }

    /**
     * 设置sNumTVA属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNumTVA(String value) {
        this.sNumTVA = value;
    }

    /**
     * 获取sTel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTel() {
        return sTel;
    }

    /**
     * 设置sTel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTel(String value) {
        this.sTel = value;
    }

    /**
     * 获取sAdrLivr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAdrLivr() {
        return sAdrLivr;
    }

    /**
     * 设置sAdrLivr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAdrLivr(String value) {
        this.sAdrLivr = value;
    }

    /**
     * 获取sDateNaissance属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSDateNaissance() {
        return sDateNaissance;
    }

    /**
     * 设置sDateNaissance属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSDateNaissance(String value) {
        this.sDateNaissance = value;
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
     * 获取sCiv属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCiv() {
        return sCiv;
    }

    /**
     * 设置sCiv属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCiv(String value) {
        this.sCiv = value;
    }

    /**
     * 获取sTypClient属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTypClient() {
        return sTypClient;
    }

    /**
     * 设置sTypClient属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTypClient(String value) {
        this.sTypClient = value;
    }

}
