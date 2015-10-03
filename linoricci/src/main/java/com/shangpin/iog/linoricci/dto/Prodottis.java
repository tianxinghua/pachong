package com.shangpin.iog.linoricci.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Administrator on 2015/10/3.
 */
@XmlRootElement(name="DocumentElement")
@XmlAccessorType(XmlAccessType.NONE)
public class Prodottis {
    @XmlElement(name="Prodotti")
    private List<Prodotti>  prodottiList;

    public List<Prodotti> getProdottiList() {
        return prodottiList;
    }

    public void setProdottiList(List<Prodotti> prodottiList) {
        this.prodottiList = prodottiList;
    }
}
