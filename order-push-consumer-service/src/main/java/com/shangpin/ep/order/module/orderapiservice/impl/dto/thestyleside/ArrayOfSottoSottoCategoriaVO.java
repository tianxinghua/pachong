
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSottoSottoCategoriaVO complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSottoSottoCategoriaVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SottoSottoCategoriaVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}SottoSottoCategoriaVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSottoSottoCategoriaVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "sottoSottoCategoriaVO"
})
public class ArrayOfSottoSottoCategoriaVO {

    @XmlElement(name = "SottoSottoCategoriaVO", nillable = true)
    protected List<SottoSottoCategoriaVO> sottoSottoCategoriaVO;

    /**
     * Gets the value of the sottoSottoCategoriaVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sottoSottoCategoriaVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSottoSottoCategoriaVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SottoSottoCategoriaVO }
     * 
     * 
     */
    public List<SottoSottoCategoriaVO> getSottoSottoCategoriaVO() {
        if (sottoSottoCategoriaVO == null) {
            sottoSottoCategoriaVO = new ArrayList<SottoSottoCategoriaVO>();
        }
        return this.sottoSottoCategoriaVO;
    }

}
