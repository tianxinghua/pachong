package com.shangpin.iog.linoricci.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Administrator on 2015/10/3.
 */
@XmlRootElement(name="Riferimenti")
public class Riferimenti {
    private String RF_RECORD_ID;
    private String RIFERIMENTO;
    private String RF_CANCELLATO;
    private String RF_TABELLA;
    private String RF_TIPO;

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
