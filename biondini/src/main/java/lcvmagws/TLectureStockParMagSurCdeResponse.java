
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureStockParMagSurCdeResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tLectureStockParMagSurCdeResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LectureStockParMagSurCdeResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLectureStockParMagSurCdeResponse", propOrder = {
    "lectureStockParMagSurCdeResult"
})
public class TLectureStockParMagSurCdeResponse {

    @XmlElement(name = "LectureStockParMagSurCdeResult", required = true)
    protected String lectureStockParMagSurCdeResult;

    /**
     * 获取lectureStockParMagSurCdeResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLectureStockParMagSurCdeResult() {
        return lectureStockParMagSurCdeResult;
    }

    /**
     * 设置lectureStockParMagSurCdeResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLectureStockParMagSurCdeResult(String value) {
        this.lectureStockParMagSurCdeResult = value;
    }

}
