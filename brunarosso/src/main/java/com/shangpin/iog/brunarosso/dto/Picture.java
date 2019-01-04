package com.shangpin.iog.brunarosso.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sunny on 2015/7/20.
 */
@XmlRootElement(name="Riferimenti")
public class Picture {
    private String RF_RECORD_ID;//id
    private String RIFERIMENTO;//图片
    private String RF_CANCELLATO;//撤销
    private String RF_TABELLA;//表
    private String RF_TIPO;//型

    public String getRF_RECORD_ID() {
        return RF_RECORD_ID;
    }

    public void setRF_RECORD_ID(String RF_RECORD_ID) {
        this.RF_RECORD_ID = RF_RECORD_ID;
    }

    public String getRIFERIMENTO() {
        return RIFERIMENTO;
    }

    public void setRIFERIMENTO(String RIFERIMENTO) {
        this.RIFERIMENTO = RIFERIMENTO;
    }

    public String getRF_CANCELLATO() {
        return RF_CANCELLATO;
    }

    public void setRF_CANCELLATO(String RF_CANCELLATO) {
        this.RF_CANCELLATO = RF_CANCELLATO;
    }

    public String getRF_TABELLA() {
        return RF_TABELLA;
    }

    public void setRF_TABELLA(String RF_TABELLA) {
        this.RF_TABELLA = RF_TABELLA;
    }

    public String getRF_TIPO() {
        return RF_TIPO;
    }

    public void setRF_TIPO(String RF_TIPO) {
        this.RF_TIPO = RF_TIPO;
    }
}
