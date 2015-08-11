import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.handlers.AbstractTemplatedHandler;
import org.apache.log4j.Logger;

public class AuthenticationHandler extends AbstractTemplatedHandler {


    @Override
    public boolean shouldInvoke(MessageContext messageContext) throws AxisFault {
        return false;
    }

    @Override
    public InvocationResponse doInvoke(MessageContext messageContext) throws AxisFault {

        try {
            MessageContext smc = (MessageContext) messageContext;
            SOAPMessage message =  smc.get
            QName APICredentialsName = new QName("http://api.channeladvisor.com/webservices/","APICredentials");
            QName DeveloperKeyName = new QName("http://api.channeladvisor.com/webservices/","DeveloperKey");
            QName PasswordName = new QName("http://api.channeladvisor.com/webservices/", "Password");

            SOAPHeader header = message.getSOAPHeader();
            SOAPHeaderElement cred = new SOAPHeaderElement(APICredentialsName);
            SOAPHeaderElement key = new SOAPHeaderElement(DeveloperKeyName);
            key.setObjectValue("DevKey");
            SOAPHeaderElement pw = new SOAPHeaderElement(PasswordName);
            pw.setObjectValue("Pass");
            cred.addChildElement(key);
            cred.addChildElement(pw);
            header.appendChild(cred);
        } catch (Exception e) {
            throw new AxisFault("Error during authentication", e);
        }
        return null;
    }
}