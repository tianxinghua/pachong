
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfRelTipiArticoliTaglieVO complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRelTipiArticoliTaglieVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RelTipiArticoliTaglieVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}RelTipiArticoliTaglieVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRelTipiArticoliTaglieVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "relTipiArticoliTaglieVO"
})
public class ArrayOfRelTipiArticoliTaglieVO {

    @XmlElement(name = "RelTipiArticoliTaglieVO", nillable = true)
    protected List<RelTipiArticoliTaglieVO> relTipiArticoliTaglieVO;

    /**
     * Gets the value of the relTipiArticoliTaglieVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relTipiArticoliTaglieVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelTipiArticoliTaglieVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelTipiArticoliTaglieVO }
     * 
     * 
     */
    public List<RelTipiArticoliTaglieVO> getRelTipiArticoliTaglieVO() {
        if (relTipiArticoliTaglieVO == null) {
            relTipiArticoliTaglieVO = new ArrayList<RelTipiArticoliTaglieVO>();
        }
        return this.relTipiArticoliTaglieVO;
    }

}
