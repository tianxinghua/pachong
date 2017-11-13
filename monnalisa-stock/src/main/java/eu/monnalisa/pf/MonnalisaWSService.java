/**
 * MonnalisaWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public interface MonnalisaWSService extends javax.xml.rpc.Service {
    public java.lang.String getMonnalisaWSPortAddress();

    public eu.monnalisa.pf.MonnalisaWS getMonnalisaWSPort() throws javax.xml.rpc.ServiceException;

    public eu.monnalisa.pf.MonnalisaWS getMonnalisaWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
