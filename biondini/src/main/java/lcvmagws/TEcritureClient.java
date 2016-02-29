
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureClient complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡsEmail���Ե�ֵ��
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
     * ����sEmail���Ե�ֵ��
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
     * ��ȡsNomCli���Ե�ֵ��
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
     * ����sNomCli���Ե�ֵ��
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
     * ��ȡsPrenomCli���Ե�ֵ��
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
     * ����sPrenomCli���Ե�ֵ��
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
     * ��ȡsAdr1���Ե�ֵ��
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
     * ����sAdr1���Ե�ֵ��
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
     * ��ȡsAdr2���Ե�ֵ��
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
     * ����sAdr2���Ե�ֵ��
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
     * ��ȡscp���Ե�ֵ��
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
     * ����scp���Ե�ֵ��
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
     * ��ȡsVille���Ե�ֵ��
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
     * ����sVille���Ե�ֵ��
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
     * ��ȡsCodePays���Ե�ֵ��
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
     * ����sCodePays���Ե�ֵ��
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
     * ��ȡsMdPInternet���Ե�ֵ��
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
     * ����sMdPInternet���Ե�ֵ��
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
     * ��ȡsSociete���Ե�ֵ��
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
     * ����sSociete���Ե�ֵ��
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
     * ��ȡsNumTVA���Ե�ֵ��
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
     * ����sNumTVA���Ե�ֵ��
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
     * ��ȡsTel���Ե�ֵ��
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
     * ����sTel���Ե�ֵ��
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
     * ��ȡsAdrLivr���Ե�ֵ��
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
     * ����sAdrLivr���Ե�ֵ��
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
     * ��ȡsDateNaissance���Ե�ֵ��
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
     * ����sDateNaissance���Ե�ֵ��
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
     * ��ȡsCodeCli���Ե�ֵ��
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
     * ����sCodeCli���Ե�ֵ��
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
     * ��ȡsCodeSite���Ե�ֵ��
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
     * ����sCodeSite���Ե�ֵ��
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
     * ��ȡsUser���Ե�ֵ��
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
     * ����sUser���Ե�ֵ��
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
     * ��ȡsMdp���Ե�ֵ��
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
     * ����sMdp���Ե�ֵ��
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
     * ��ȡsCiv���Ե�ֵ��
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
     * ����sCiv���Ե�ֵ��
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
     * ��ȡsTypClient���Ե�ֵ��
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
     * ����sTypClient���Ե�ֵ��
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
