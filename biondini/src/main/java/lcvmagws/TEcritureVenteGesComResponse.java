
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureVenteGesComResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureVenteGesComResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureVenteGesComResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureVenteGesComResponse", propOrder = {
    "ecritureVenteGesComResult"
})
public class TEcritureVenteGesComResponse {

    @XmlElement(name = "EcritureVenteGesComResult", required = true)
    protected String ecritureVenteGesComResult;

    /**
     * 获取ecritureVenteGesComResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureVenteGesComResult() {
        return ecritureVenteGesComResult;
    }

    /**
     * 设置ecritureVenteGesComResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureVenteGesComResult(String value) {
        this.ecritureVenteGesComResult = value;
    }

}
