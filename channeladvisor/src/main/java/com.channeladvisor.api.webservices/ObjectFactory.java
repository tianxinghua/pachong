
package com.channeladvisor.api.webservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.channeladvisor.api.webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _APICredentials_QNAME = new QName("http://api.channeladvisor.com/webservices/", "APICredentials");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.channeladvisor.api.webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAuthorizationList }
     * 
     */
    public GetAuthorizationList createGetAuthorizationList() {
        return new GetAuthorizationList();
    }

    /**
     * Create an instance of {@link GetAuthorizationListResponse }
     * 
     */
    public GetAuthorizationListResponse createGetAuthorizationListResponse() {
        return new GetAuthorizationListResponse();
    }

    /**
     * Create an instance of {@link APIResultOfArrayOfAuthorizationResponse }
     * 
     */
    public APIResultOfArrayOfAuthorizationResponse createAPIResultOfArrayOfAuthorizationResponse() {
        return new APIResultOfArrayOfAuthorizationResponse();
    }

    /**
     * Create an instance of {@link APICredentials }
     * 
     */
    public APICredentials createAPICredentials() {
        return new APICredentials();
    }

    /**
     * Create an instance of {@link RequestAccess }
     * 
     */
    public RequestAccess createRequestAccess() {
        return new RequestAccess();
    }

    /**
     * Create an instance of {@link RequestAccessResponse }
     * 
     */
    public RequestAccessResponse createRequestAccessResponse() {
        return new RequestAccessResponse();
    }

    /**
     * Create an instance of {@link APIResultOfBoolean }
     * 
     */
    public APIResultOfBoolean createAPIResultOfBoolean() {
        return new APIResultOfBoolean();
    }

    /**
     * Create an instance of {@link Ping }
     * 
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link APIResultOfString }
     * 
     */
    public APIResultOfString createAPIResultOfString() {
        return new APIResultOfString();
    }

    /**
     * Create an instance of {@link ArrayOfAuthorizationResponse }
     * 
     */
    public ArrayOfAuthorizationResponse createArrayOfAuthorizationResponse() {
        return new ArrayOfAuthorizationResponse();
    }

    /**
     * Create an instance of {@link AuthorizationResponse }
     * 
     */
    public AuthorizationResponse createAuthorizationResponse() {
        return new AuthorizationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link APICredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://api.channeladvisor.com/webservices/", name = "APICredentials")
    public JAXBElement<APICredentials> createAPICredentials(APICredentials value) {
        return new JAXBElement<APICredentials>(_APICredentials_QNAME, APICredentials.class, null, value);
    }

}
