/**
 * PrenotazioneResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class PrenotazioneResult  implements java.io.Serializable {
    private int result;

    private java.lang.String message;

    private eu.monnalisa.pf.Article[] articlesToRemove;

    public PrenotazioneResult() {
    }

    public PrenotazioneResult(
           int result,
           java.lang.String message,
           eu.monnalisa.pf.Article[] articlesToRemove) {
           this.result = result;
           this.message = message;
           this.articlesToRemove = articlesToRemove;
    }


    /**
     * Gets the result value for this PrenotazioneResult.
     * 
     * @return result
     */
    public int getResult() {
        return result;
    }


    /**
     * Sets the result value for this PrenotazioneResult.
     * 
     * @param result
     */
    public void setResult(int result) {
        this.result = result;
    }


    /**
     * Gets the message value for this PrenotazioneResult.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this PrenotazioneResult.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the articlesToRemove value for this PrenotazioneResult.
     * 
     * @return articlesToRemove
     */
    public eu.monnalisa.pf.Article[] getArticlesToRemove() {
        return articlesToRemove;
    }


    /**
     * Sets the articlesToRemove value for this PrenotazioneResult.
     * 
     * @param articlesToRemove
     */
    public void setArticlesToRemove(eu.monnalisa.pf.Article[] articlesToRemove) {
        this.articlesToRemove = articlesToRemove;
    }

    public eu.monnalisa.pf.Article getArticlesToRemove(int i) {
        return this.articlesToRemove[i];
    }

    public void setArticlesToRemove(int i, eu.monnalisa.pf.Article _value) {
        this.articlesToRemove[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PrenotazioneResult)) return false;
        PrenotazioneResult other = (PrenotazioneResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.result == other.getResult() &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.articlesToRemove==null && other.getArticlesToRemove()==null) || 
             (this.articlesToRemove!=null &&
              java.util.Arrays.equals(this.articlesToRemove, other.getArticlesToRemove())));
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
        _hashCode += getResult();
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getArticlesToRemove() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getArticlesToRemove());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getArticlesToRemove(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PrenotazioneResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "prenotazioneResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("articlesToRemove");
        elemField.setXmlName(new javax.xml.namespace.QName("", "articlesToRemove"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "article"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
