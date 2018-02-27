/**
 * ArticleFull.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class ArticleFull  implements java.io.Serializable {
    private java.util.Calendar dataExp;

    private java.util.Calendar dataIns;

    private eu.monnalisa.pf.Article dettaglioSku;

    private java.lang.String mag_codice;

    private int qta;

    private java.lang.String sku;

    private java.lang.String stato;

    public ArticleFull() {
    }

    public ArticleFull(
           java.util.Calendar dataExp,
           java.util.Calendar dataIns,
           eu.monnalisa.pf.Article dettaglioSku,
           java.lang.String mag_codice,
           int qta,
           java.lang.String sku,
           java.lang.String stato) {
           this.dataExp = dataExp;
           this.dataIns = dataIns;
           this.dettaglioSku = dettaglioSku;
           this.mag_codice = mag_codice;
           this.qta = qta;
           this.sku = sku;
           this.stato = stato;
    }


    /**
     * Gets the dataExp value for this ArticleFull.
     * 
     * @return dataExp
     */
    public java.util.Calendar getDataExp() {
        return dataExp;
    }


    /**
     * Sets the dataExp value for this ArticleFull.
     * 
     * @param dataExp
     */
    public void setDataExp(java.util.Calendar dataExp) {
        this.dataExp = dataExp;
    }


    /**
     * Gets the dataIns value for this ArticleFull.
     * 
     * @return dataIns
     */
    public java.util.Calendar getDataIns() {
        return dataIns;
    }


    /**
     * Sets the dataIns value for this ArticleFull.
     * 
     * @param dataIns
     */
    public void setDataIns(java.util.Calendar dataIns) {
        this.dataIns = dataIns;
    }


    /**
     * Gets the dettaglioSku value for this ArticleFull.
     * 
     * @return dettaglioSku
     */
    public eu.monnalisa.pf.Article getDettaglioSku() {
        return dettaglioSku;
    }


    /**
     * Sets the dettaglioSku value for this ArticleFull.
     * 
     * @param dettaglioSku
     */
    public void setDettaglioSku(eu.monnalisa.pf.Article dettaglioSku) {
        this.dettaglioSku = dettaglioSku;
    }


    /**
     * Gets the mag_codice value for this ArticleFull.
     * 
     * @return mag_codice
     */
    public java.lang.String getMag_codice() {
        return mag_codice;
    }


    /**
     * Sets the mag_codice value for this ArticleFull.
     * 
     * @param mag_codice
     */
    public void setMag_codice(java.lang.String mag_codice) {
        this.mag_codice = mag_codice;
    }


    /**
     * Gets the qta value for this ArticleFull.
     * 
     * @return qta
     */
    public int getQta() {
        return qta;
    }


    /**
     * Sets the qta value for this ArticleFull.
     * 
     * @param qta
     */
    public void setQta(int qta) {
        this.qta = qta;
    }


    /**
     * Gets the sku value for this ArticleFull.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this ArticleFull.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }


    /**
     * Gets the stato value for this ArticleFull.
     * 
     * @return stato
     */
    public java.lang.String getStato() {
        return stato;
    }


    /**
     * Sets the stato value for this ArticleFull.
     * 
     * @param stato
     */
    public void setStato(java.lang.String stato) {
        this.stato = stato;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArticleFull)) return false;
        ArticleFull other = (ArticleFull) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataExp==null && other.getDataExp()==null) || 
             (this.dataExp!=null &&
              this.dataExp.equals(other.getDataExp()))) &&
            ((this.dataIns==null && other.getDataIns()==null) || 
             (this.dataIns!=null &&
              this.dataIns.equals(other.getDataIns()))) &&
            ((this.dettaglioSku==null && other.getDettaglioSku()==null) || 
             (this.dettaglioSku!=null &&
              this.dettaglioSku.equals(other.getDettaglioSku()))) &&
            ((this.mag_codice==null && other.getMag_codice()==null) || 
             (this.mag_codice!=null &&
              this.mag_codice.equals(other.getMag_codice()))) &&
            this.qta == other.getQta() &&
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku()))) &&
            ((this.stato==null && other.getStato()==null) || 
             (this.stato!=null &&
              this.stato.equals(other.getStato())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDataExp() != null) {
            _hashCode += getDataExp().hashCode();
        }
        if (getDataIns() != null) {
            _hashCode += getDataIns().hashCode();
        }
        if (getDettaglioSku() != null) {
            _hashCode += getDettaglioSku().hashCode();
        }
        if (getMag_codice() != null) {
            _hashCode += getMag_codice().hashCode();
        }
        _hashCode += getQta();
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        if (getStato() != null) {
            _hashCode += getStato().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArticleFull.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "articleFull"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataExp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataExp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataIns");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataIns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dettaglioSku");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dettaglioSku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "article"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mag_codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mag_codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sku");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sku"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stato");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stato"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
