
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureDesModelesGarantResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     * ��ȡlectureDesModelesGarantResult���Ե�ֵ��
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
     * ����lectureDesModelesGarantResult���Ե�ֵ��
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
