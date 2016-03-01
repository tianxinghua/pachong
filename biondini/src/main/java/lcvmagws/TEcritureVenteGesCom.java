
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureVenteGesCom complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡsVente���Ե�ֵ��
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
     * ����sVente���Ե�ֵ��
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
     * ��ȡsCodeMag���Ե�ֵ��
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
     * ����sCodeMag���Ե�ֵ��
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
     * ��ȡsCodeRepresentant���Ե�ֵ��
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
     * ����sCodeRepresentant���Ե�ֵ��
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
     * ��ȡsCodReglt���Ե�ֵ��
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
     * ����sCodReglt���Ե�ֵ��
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
     * ��ȡsCodePromo���Ե�ֵ��
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
     * ����sCodePromo���Ե�ֵ��
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
     * ��ȡbCoordonees���Ե�ֵ��
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
     * ����bCoordonees���Ե�ֵ��
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
     * ��ȡsAdrsLivr���Ե�ֵ��
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
     * ����sAdrsLivr���Ե�ֵ��
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
     * ��ȡsCommentaire���Ե�ֵ��
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
     * ����sCommentaire���Ե�ֵ��
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
