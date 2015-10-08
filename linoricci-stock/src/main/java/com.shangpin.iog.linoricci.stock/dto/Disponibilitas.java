package com.shangpin.iog.linoricci.stock.dto;

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
public class Disponibilitas {

    @XmlElement(name="Disponibilita")
    private List<Disponibilita> disponibilitaList;

    public List<Disponibilita> getDisponibilitaList() {
        return disponibilitaList;
    }

    public void setDisponibilitaList(List<Disponibilita> disponibilitaList) {
        this.disponibilitaList = disponibilitaList;
    }
}
