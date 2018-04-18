
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfTagliaVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTagliaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TagliaVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}TagliaVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTagliaVO", propOrder = {
    "tagliaVO"
})
public class ArrayOfTagliaVO {

    @XmlElement(name = "TagliaVO", nillable = true)
    protected List<TagliaVO> tagliaVO;

    /**
     * Gets the value of the tagliaVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tagliaVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTagliaVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TagliaVO }
     * 
     * 
     */
    public List<TagliaVO> getTagliaVO() {
        if (tagliaVO == null) {
            tagliaVO = new ArrayList<TagliaVO>();
        }
        return this.tagliaVO;
    }

}
