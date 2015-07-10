package com.shangpin.iog.brunarosso.dto;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sunny on 2015/7/10.
 */
@XmlRootElement(name="Prodotti")
public class Product {
    private String ID_ARTICOLO; //id;sku
    private String SIGLA_STAGIONE;//季节编码
    private String BRAND; //品牌
    private String CODICE_MODELLO;//spu
    private String CODICE_VARIANTE;//CODICE_MODELLO+CODICE_VARIANTE=货号
    private String SETTORE;//性别
    private String TIPO_STAGIONE;// 季节
    private String GRUPPO_SUPER;//一级品类
    private String GRUPPO;//二级品类
    private String DESCRIZIONE_MODELLO;//描述
    private String COLORE;//颜色
    private String TESSUTO;//面料
    private String MOTIVO;//图案
    private String CATEGORIA;//类别
    private String DESCRIZIONE;//描述
    private String DESCRIZIONE_BREVE;//简述
    private String PREZZO_LISTINO;//列表价格
    private String PREZZO_VENDITA;//销售价格
    private String PESO_VOLUME;//总重量
    private String PREZZO_VENDITA_SENZA_IVA;//不含增值税价格
    private String TIPO_TIPOLOGIA_TAGLIE;
    private String PREVENDITA;//是否预售
    private String CANCELLATO;//取消
    private String COLORE_SUPER;//原色
    private String COLORE_VERO;//真正的颜色
    private String SPEDIZIONE_SPECIALE;//快递
    private String MISURA_DES1;//测量
    private String MISURA_DES2;
    private String MISURA_DES3;
    private String MISURA_DES4;
    private String MISURA_DES5;
    private String MISURA_DES6;
    private String MISURA_DES7;
    private String MISURA_DES8;
    private String SIZE_AND_FIT;//尺寸
    private String DATA_PREVENDITA;//预售日期
    private String TIPO_IMBALLO;//包装的类型
    private String PREZZO_IMBALLO;//包装的价格
    private String DESCRIZIONE_SPECIALE;//特别说明
    private String PAESE_PRODUZIONE;// 产地(国家)
    private String CODICE_MID_PRODUTTORE;// 制造商
    private String COMPOSIZIONE_DETTAGLIATA;//详细构成
    private String TI_DES_ESTESA;//扩展
    private String TIPOLOGIA_TAGLIE;//尺寸类型
    private String BS_DES;//
    private String TS;
    private String AR_TS;
    private String VA_PERIODO_CAMBIO;//换货周期
    private String SuperGruppoSuper;
    private String PAGINA_PUBBLICAZIONE;//发布页面

    public String getID_ARTICOLO() {
        return ID_ARTICOLO;
    }

    public void setID_ARTICOLO(String ID_ARTICOLO) {
        this.ID_ARTICOLO = ID_ARTICOLO;
    }

    public String getSIGLA_STAGIONE() {
        return SIGLA_STAGIONE;
    }

    public void setSIGLA_STAGIONE(String SIGLA_STAGIONE) {
        this.SIGLA_STAGIONE = SIGLA_STAGIONE;
    }

    public String getBRAND() {
        return BRAND;
    }

    public void setBRAND(String BRAND) {
        this.BRAND = BRAND;
    }

    public String getCODICE_MODELLO() {
        return CODICE_MODELLO;
    }

    public void setCODICE_MODELLO(String CODICE_MODELLO) {
        this.CODICE_MODELLO = CODICE_MODELLO;
    }

    public String getCODICE_VARIANTE() {
        return CODICE_VARIANTE;
    }

    public void setCODICE_VARIANTE(String CODICE_VARIANTE) {
        this.CODICE_VARIANTE = CODICE_VARIANTE;
    }

    public String getSETTORE() {
        return SETTORE;
    }

    public void setSETTORE(String SETTORE) {
        this.SETTORE = SETTORE;
    }

    public String getTIPO_STAGIONE() {
        return TIPO_STAGIONE;
    }

    public void setTIPO_STAGIONE(String TIPO_STAGIONE) {
        this.TIPO_STAGIONE = TIPO_STAGIONE;
    }

    public String getGRUPPO_SUPER() {
        return GRUPPO_SUPER;
    }

    public void setGRUPPO_SUPER(String GRUPPO_SUPER) {
        this.GRUPPO_SUPER = GRUPPO_SUPER;
    }

    public String getGRUPPO() {
        return GRUPPO;
    }

    public void setGRUPPO(String GRUPPO) {
        this.GRUPPO = GRUPPO;
    }

    public String getDESCRIZIONE_MODELLO() {
        return DESCRIZIONE_MODELLO;
    }

    public void setDESCRIZIONE_MODELLO(String DESCRIZIONE_MODELLO) {
        this.DESCRIZIONE_MODELLO = DESCRIZIONE_MODELLO;
    }

    public String getCOLORE() {
        return COLORE;
    }

    public void setCOLORE(String COLORE) {
        this.COLORE = COLORE;
    }

    public String getTESSUTO() {
        return TESSUTO;
    }

    public void setTESSUTO(String TESSUTO) {
        this.TESSUTO = TESSUTO;
    }

    public String getMOTIVO() {
        return MOTIVO;
    }

    public void setMOTIVO(String MOTIVO) {
        this.MOTIVO = MOTIVO;
    }

    public String getCATEGORIA() {
        return CATEGORIA;
    }

    public void setCATEGORIA(String CATEGORIA) {
        this.CATEGORIA = CATEGORIA;
    }

    public String getDESCRIZIONE() {
        return DESCRIZIONE;
    }

    public void setDESCRIZIONE(String DESCRIZIONE) {
        this.DESCRIZIONE = DESCRIZIONE;
    }

    public String getDESCRIZIONE_BREVE() {
        return DESCRIZIONE_BREVE;
    }

    public void setDESCRIZIONE_BREVE(String DESCRIZIONE_BREVE) {
        this.DESCRIZIONE_BREVE = DESCRIZIONE_BREVE;
    }

    public String getPREZZO_LISTINO() {
        return PREZZO_LISTINO;
    }

    public void setPREZZO_LISTINO(String PREZZO_LISTINO) {
        this.PREZZO_LISTINO = PREZZO_LISTINO;
    }

    public String getPREZZO_VENDITA() {
        return PREZZO_VENDITA;
    }

    public void setPREZZO_VENDITA(String PREZZO_VENDITA) {
        this.PREZZO_VENDITA = PREZZO_VENDITA;
    }

    public String getPESO_VOLUME() {
        return PESO_VOLUME;
    }

    public void setPESO_VOLUME(String PESO_VOLUME) {
        this.PESO_VOLUME = PESO_VOLUME;
    }

    public String getPREZZO_VENDITA_SENZA_IVA() {
        return PREZZO_VENDITA_SENZA_IVA;
    }

    public void setPREZZO_VENDITA_SENZA_IVA(String PREZZO_VENDITA_SENZA_IVA) {
        this.PREZZO_VENDITA_SENZA_IVA = PREZZO_VENDITA_SENZA_IVA;
    }

    public String getTIPO_TIPOLOGIA_TAGLIE() {
        return TIPO_TIPOLOGIA_TAGLIE;
    }

    public void setTIPO_TIPOLOGIA_TAGLIE(String TIPO_TIPOLOGIA_TAGLIE) {
        this.TIPO_TIPOLOGIA_TAGLIE = TIPO_TIPOLOGIA_TAGLIE;
    }

    public String getPREVENDITA() {
        return PREVENDITA;
    }

    public void setPREVENDITA(String PREVENDITA) {
        this.PREVENDITA = PREVENDITA;
    }

    public String getCANCELLATO() {
        return CANCELLATO;
    }

    public void setCANCELLATO(String CANCELLATO) {
        this.CANCELLATO = CANCELLATO;
    }

    public String getCOLORE_SUPER() {
        return COLORE_SUPER;
    }

    public void setCOLORE_SUPER(String COLORE_SUPER) {
        this.COLORE_SUPER = COLORE_SUPER;
    }

    public String getCOLORE_VERO() {
        return COLORE_VERO;
    }

    public void setCOLORE_VERO(String COLORE_VERO) {
        this.COLORE_VERO = COLORE_VERO;
    }

    public String getSPEDIZIONE_SPECIALE() {
        return SPEDIZIONE_SPECIALE;
    }

    public void setSPEDIZIONE_SPECIALE(String SPEDIZIONE_SPECIALE) {
        this.SPEDIZIONE_SPECIALE = SPEDIZIONE_SPECIALE;
    }

    public String getMISURA_DES1() {
        return MISURA_DES1;
    }

    public void setMISURA_DES1(String MISURA_DES1) {
        this.MISURA_DES1 = MISURA_DES1;
    }

    public String getMISURA_DES2() {
        return MISURA_DES2;
    }

    public void setMISURA_DES2(String MISURA_DES2) {
        this.MISURA_DES2 = MISURA_DES2;
    }

    public String getMISURA_DES3() {
        return MISURA_DES3;
    }

    public void setMISURA_DES3(String MISURA_DES3) {
        this.MISURA_DES3 = MISURA_DES3;
    }

    public String getMISURA_DES4() {
        return MISURA_DES4;
    }

    public void setMISURA_DES4(String MISURA_DES4) {
        this.MISURA_DES4 = MISURA_DES4;
    }

    public String getMISURA_DES5() {
        return MISURA_DES5;
    }

    public void setMISURA_DES5(String MISURA_DES5) {
        this.MISURA_DES5 = MISURA_DES5;
    }

    public String getMISURA_DES6() {
        return MISURA_DES6;
    }

    public void setMISURA_DES6(String MISURA_DES6) {
        this.MISURA_DES6 = MISURA_DES6;
    }

    public String getMISURA_DES7() {
        return MISURA_DES7;
    }

    public void setMISURA_DES7(String MISURA_DES7) {
        this.MISURA_DES7 = MISURA_DES7;
    }

    public String getMISURA_DES8() {
        return MISURA_DES8;
    }

    public void setMISURA_DES8(String MISURA_DES8) {
        this.MISURA_DES8 = MISURA_DES8;
    }

    public String getSIZE_AND_FIT() {
        return SIZE_AND_FIT;
    }

    public void setSIZE_AND_FIT(String SIZE_AND_FIT) {
        this.SIZE_AND_FIT = SIZE_AND_FIT;
    }

    public String getDATA_PREVENDITA() {
        return DATA_PREVENDITA;
    }

    public void setDATA_PREVENDITA(String DATA_PREVENDITA) {
        this.DATA_PREVENDITA = DATA_PREVENDITA;
    }

    public String getTIPO_IMBALLO() {
        return TIPO_IMBALLO;
    }

    public void setTIPO_IMBALLO(String TIPO_IMBALLO) {
        this.TIPO_IMBALLO = TIPO_IMBALLO;
    }

    public String getPREZZO_IMBALLO() {
        return PREZZO_IMBALLO;
    }

    public void setPREZZO_IMBALLO(String PREZZO_IMBALLO) {
        this.PREZZO_IMBALLO = PREZZO_IMBALLO;
    }

    public String getDESCRIZIONE_SPECIALE() {
        return DESCRIZIONE_SPECIALE;
    }

    public void setDESCRIZIONE_SPECIALE(String DESCRIZIONE_SPECIALE) {
        this.DESCRIZIONE_SPECIALE = DESCRIZIONE_SPECIALE;
    }

    public String getPAESE_PRODUZIONE() {
        return PAESE_PRODUZIONE;
    }

    public void setPAESE_PRODUZIONE(String PAESE_PRODUZIONE) {
        this.PAESE_PRODUZIONE = PAESE_PRODUZIONE;
    }

    public String getCODICE_MID_PRODUTTORE() {
        return CODICE_MID_PRODUTTORE;
    }

    public void setCODICE_MID_PRODUTTORE(String CODICE_MID_PRODUTTORE) {
        this.CODICE_MID_PRODUTTORE = CODICE_MID_PRODUTTORE;
    }

    public String getCOMPOSIZIONE_DETTAGLIATA() {
        return COMPOSIZIONE_DETTAGLIATA;
    }

    public void setCOMPOSIZIONE_DETTAGLIATA(String COMPOSIZIONE_DETTAGLIATA) {
        this.COMPOSIZIONE_DETTAGLIATA = COMPOSIZIONE_DETTAGLIATA;
    }

    public String getTI_DES_ESTESA() {
        return TI_DES_ESTESA;
    }

    public void setTI_DES_ESTESA(String TI_DES_ESTESA) {
        this.TI_DES_ESTESA = TI_DES_ESTESA;
    }

    public String getTIPOLOGIA_TAGLIE() {
        return TIPOLOGIA_TAGLIE;
    }

    public void setTIPOLOGIA_TAGLIE(String TIPOLOGIA_TAGLIE) {
        this.TIPOLOGIA_TAGLIE = TIPOLOGIA_TAGLIE;
    }

    public String getBS_DES() {
        return BS_DES;
    }

    public void setBS_DES(String BS_DES) {
        this.BS_DES = BS_DES;
    }

    public String getTS() {
        return TS;
    }

    public void setTS(String TS) {
        this.TS = TS;
    }

    public String getAR_TS() {
        return AR_TS;
    }

    public void setAR_TS(String AR_TS) {
        this.AR_TS = AR_TS;
    }

    public String getVA_PERIODO_CAMBIO() {
        return VA_PERIODO_CAMBIO;
    }

    public void setVA_PERIODO_CAMBIO(String VA_PERIODO_CAMBIO) {
        this.VA_PERIODO_CAMBIO = VA_PERIODO_CAMBIO;
    }

    public String getSuperGruppoSuper() {
        return SuperGruppoSuper;
    }

    public void setSuperGruppoSuper(String superGruppoSuper) {
        SuperGruppoSuper = superGruppoSuper;
    }

    public String getPAGINA_PUBBLICAZIONE() {
        return PAGINA_PUBBLICAZIONE;
    }

    public void setPAGINA_PUBBLICAZIONE(String PAGINA_PUBBLICAZIONE) {
        this.PAGINA_PUBBLICAZIONE = PAGINA_PUBBLICAZIONE;
    }
}
