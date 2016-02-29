
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureDesModelesGarantResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tLectureDesModelesGarantResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LectureDesModelesGarantResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLectureDesModelesGarantResponse", propOrder = {
    "lectureDesModelesGarantResult"
})
public class TLectureDesModelesGarantResponse {

    @XmlElement(name = "LectureDesModelesGarantResult", required = true)
    protected String lectureDesModelesGarantResult;

    /**
     * 获取lectureDesModelesGarantResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLectureDesModelesGarantResult() {
        return lectureDesModelesGarantResult;
    }

    /**
     * 设置lectureDesModelesGarantResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLectureDesModelesGarantResult(String value) {
        this.lectureDesModelesGarantResult = value;
    }

}
