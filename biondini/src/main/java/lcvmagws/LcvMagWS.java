package lcvmagws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.5
 * 2016-02-26T11:57:41.578+08:00
 * Generated source version: 3.1.5
 * 
 */
@WebServiceClient(name = "LcvMagWS", 
                  wsdlLocation = "http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?wsdl",
                  targetNamespace = "urn:LcvMagWS") 
public class LcvMagWS extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:LcvMagWS", "LcvMagWS");
    public final static QName LcvMagWSSOAPPort = new QName("urn:LcvMagWS", "LcvMagWSSOAPPort");
    static {
        URL url = null;
        try {
            url = new URL("http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(LcvMagWS.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://80.12.82.220:8080/LCVMAGWS_WEB/awws/LcvMagWS.awws?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public LcvMagWS(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public LcvMagWS(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public LcvMagWS() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public LcvMagWS(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public LcvMagWS(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public LcvMagWS(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns LcvMagWSSOAPPortType
     */
    @WebEndpoint(name = "LcvMagWSSOAPPort")
    public LcvMagWSSOAPPortType getLcvMagWSSOAPPort() {
        return super.getPort(LcvMagWSSOAPPort, LcvMagWSSOAPPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns LcvMagWSSOAPPortType
     */
    @WebEndpoint(name = "LcvMagWSSOAPPort")
    public LcvMagWSSOAPPortType getLcvMagWSSOAPPort(WebServiceFeature... features) {
        return super.getPort(LcvMagWSSOAPPort, LcvMagWSSOAPPortType.class, features);
    }

}
