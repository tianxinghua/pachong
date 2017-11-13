/**
 * Article.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class Article  implements java.io.Serializable {
    private java.lang.String cod_articolo;

    private java.lang.String cod_collezione;

    private java.lang.String cod_colore;

    private java.lang.String cod_modello;

    private java.lang.String cod_taglia;

    public Article() {
    }

    public Article(
           java.lang.String cod_articolo,
           java.lang.String cod_collezione,
           java.lang.String cod_colore,
           java.lang.String cod_modello,
           java.lang.String cod_taglia) {
           this.cod_articolo = cod_articolo;
           this.cod_collezione = cod_collezione;
           this.cod_colore = cod_colore;
           this.cod_modello = cod_modello;
           this.cod_taglia = cod_taglia;
    }


    /**
     * Gets the cod_articolo value for this Article.
     * 
     * @return cod_articolo
     */
    public java.lang.String getCod_articolo() {
        return cod_articolo;
    }


    /**
     * Sets the cod_articolo value for this Article.
     * 
     * @param cod_articolo
     */
    public void setCod_articolo(java.lang.String cod_articolo) {
        this.cod_articolo = cod_articolo;
    }


    /**
     * Gets the cod_collezione value for this Article.
     * 
     * @return cod_collezione
     */
    public java.lang.String getCod_collezione() {
        return cod_collezione;
    }


    /**
     * Sets the cod_collezione value for this Article.
     * 
     * @param cod_collezione
     */
    public void setCod_collezione(java.lang.String cod_collezione) {
        this.cod_collezione = cod_collezione;
    }


    /**
     * Gets the cod_colore value for this Article.
     * 
     * @return cod_colore
     */
    public java.lang.String getCod_colore() {
        return cod_colore;
    }


    /**
     * Sets the cod_colore value for this Article.
     * 
     * @param cod_colore
     */
    public void setCod_colore(java.lang.String cod_colore) {
        this.cod_colore = cod_colore;
    }


    /**
     * Gets the cod_modello value for this Article.
     * 
     * @return cod_modello
     */
    public java.lang.String getCod_modello() {
        return cod_modello;
    }


    /**
     * Sets the cod_modello value for this Article.
     * 
     * @param cod_modello
     */
    public void setCod_modello(java.lang.String cod_modello) {
        this.cod_modello = cod_modello;
    }


    /**
     * Gets the cod_taglia value for this Article.
     * 
     * @return cod_taglia
     */
    public java.lang.String getCod_taglia() {
        return cod_taglia;
    }


    /**
     * Sets the cod_taglia value for this Article.
     * 
     * @param cod_taglia
     */
    public void setCod_taglia(java.lang.String cod_taglia) {
        this.cod_taglia = cod_taglia;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Article)) return false;
        Article other = (Article) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cod_articolo==null && other.getCod_articolo()==null) || 
             (this.cod_articolo!=null &&
              this.cod_articolo.equals(other.getCod_articolo()))) &&
            ((this.cod_collezione==null && other.getCod_collezione()==null) || 
             (this.cod_collezione!=null &&
              this.cod_collezione.equals(other.getCod_collezione()))) &&
            ((this.cod_colore==null && other.getCod_colore()==null) || 
             (this.cod_colore!=null &&
              this.cod_colore.equals(other.getCod_colore()))) &&
            ((this.cod_modello==null && other.getCod_modello()==null) || 
             (this.cod_modello!=null &&
              this.cod_modello.equals(other.getCod_modello()))) &&
            ((this.cod_taglia==null && other.getCod_taglia()==null) || 
             (this.cod_taglia!=null &&
              this.cod_taglia.equals(other.getCod_taglia())));
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
        if (getCod_articolo() != null) {
            _hashCode += getCod_articolo().hashCode();
        }
        if (getCod_collezione() != null) {
            _hashCode += getCod_collezione().hashCode();
        }
        if (getCod_colore() != null) {
            _hashCode += getCod_colore().hashCode();
        }
        if (getCod_modello() != null) {
            _hashCode += getCod_modello().hashCode();
        }
        if (getCod_taglia() != null) {
            _hashCode += getCod_taglia().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Article.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "article"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_articolo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_articolo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_collezione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_collezione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_colore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_colore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_modello");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_modello"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_taglia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cod_taglia"));
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
