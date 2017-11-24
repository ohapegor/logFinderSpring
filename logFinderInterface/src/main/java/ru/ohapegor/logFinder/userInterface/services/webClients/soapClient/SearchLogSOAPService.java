
package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SearchLogSOAPService",
        targetNamespace = "http://soap.web_services.services.logFinder.ohapegor.ru/",
        wsdlLocation = "http://localhost:7003/logFinderEjbApp/SearchLogSOAPService?wsdl")
public class SearchLogSOAPService
    extends Service
{

    private final static URL SEARCHLOGSOAPSERVICE_WSDL_LOCATION;
    private final static WebServiceException SEARCHLOGSOAPSERVICE_EXCEPTION;
    private final static QName SEARCHLOGSOAPSERVICE_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "SearchLogSOAPService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:7001/logFinder/SearchLogSOAPService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SEARCHLOGSOAPSERVICE_WSDL_LOCATION = url;
        SEARCHLOGSOAPSERVICE_EXCEPTION = e;
    }

    public SearchLogSOAPService() {
        super(__getWsdlLocation(), SEARCHLOGSOAPSERVICE_QNAME);
    }

    public SearchLogSOAPService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SEARCHLOGSOAPSERVICE_QNAME, features);
    }

    public SearchLogSOAPService(URL wsdlLocation) {
        super(wsdlLocation, SEARCHLOGSOAPSERVICE_QNAME);
    }

    public SearchLogSOAPService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SEARCHLOGSOAPSERVICE_QNAME, features);
    }

    public SearchLogSOAPService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SearchLogSOAPService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SearchLogSOAP
     */
    @WebEndpoint(name = "SearchLogSOAPPort")
    public SearchLogSOAPClient getSearchLogSOAPPort() {
        return super.getPort(new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "SearchLogSOAPPort"), SearchLogSOAPClient.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SearchLogSOAP
     */
    @WebEndpoint(name = "SearchLogSOAPPort")
    public SearchLogSOAPClient getSearchLogSOAPPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "SearchLogSOAPPort"), SearchLogSOAPClient.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SEARCHLOGSOAPSERVICE_EXCEPTION!= null) {
            throw SEARCHLOGSOAPSERVICE_EXCEPTION;
        }
        return SEARCHLOGSOAPSERVICE_WSDL_LOCATION;
    }

}
