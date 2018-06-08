
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfRelClassiSClassiVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRelClassiSClassiVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RelClassiSClassiVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}RelClassiSClassiVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRelClassiSClassiVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "relClassiSClassiVO"
})
public class ArrayOfRelClassiSClassiVO {

    @XmlElement(name = "RelClassiSClassiVO", nillable = true)
    protected List<RelClassiSClassiVO> relClassiSClassiVO;

    /**
     * Gets the value of the relClassiSClassiVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relClassiSClassiVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelClassiSClassiVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelClassiSClassiVO }
     * 
     * 
     */
    public List<RelClassiSClassiVO> getRelClassiSClassiVO() {
        if (relClassiSClassiVO == null) {
            relClassiSClassiVO = new ArrayList<RelClassiSClassiVO>();
        }
        return this.relClassiSClassiVO;
    }

}
