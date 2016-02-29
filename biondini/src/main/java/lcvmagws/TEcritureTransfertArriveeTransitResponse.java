
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertArriveeTransitResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureTransfertArriveeTransitResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureTransfertArriveeTransitResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureTransfertArriveeTransitResponse", propOrder = {
    "ecritureTransfertArriveeTransitResult"
})
public class TEcritureTransfertArriveeTransitResponse {

    @XmlElement(name = "EcritureTransfertArriveeTransitResult", required = true)
    protected String ecritureTransfertArriveeTransitResult;

    /**
     * 获取ecritureTransfertArriveeTransitResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureTransfertArriveeTransitResult() {
        return ecritureTransfertArriveeTransitResult;
    }

    /**
     * 设置ecritureTransfertArriveeTransitResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureTransfertArriveeTransitResult(String value) {
        this.ecritureTransfertArriveeTransitResult = value;
    }

}
