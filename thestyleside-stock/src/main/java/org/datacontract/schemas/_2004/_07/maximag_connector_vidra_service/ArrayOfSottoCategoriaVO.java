
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSottoCategoriaVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSottoCategoriaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SottoCategoriaVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}SottoCategoriaVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSottoCategoriaVO", propOrder = {
    "sottoCategoriaVO"
})
public class ArrayOfSottoCategoriaVO {

    @XmlElement(name = "SottoCategoriaVO", nillable = true)
    protected List<SottoCategoriaVO> sottoCategoriaVO;

    /**
     * Gets the value of the sottoCategoriaVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sottoCategoriaVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSottoCategoriaVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SottoCategoriaVO }
     * 
     * 
     */
    public List<SottoCategoriaVO> getSottoCategoriaVO() {
        if (sottoCategoriaVO == null) {
            sottoCategoriaVO = new ArrayList<SottoCategoriaVO>();
        }
        return this.sottoCategoriaVO;
    }

}
