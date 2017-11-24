
package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;


import ru.ohapegor.logFinder.entities.ResultLogs;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.entities.SignificantDateInterval;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
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

    private final static QName _SearchInfoResult_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "search-info-result");
    private final static QName _FileSearch_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "fileSearch");
    private final static QName _FileGenerateResponse_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "fileGenerateResponse");
    private final static QName _LogSearch_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "logSearch");
    private final static QName _FileSearchResponse_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "fileSearchResponse");
    private final static QName _LogSearchResponse_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "logSearchResponse");
    private final static QName _SearchInfo_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "search-info");
    private final static QName _ResultLogs_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "result-logs");
    private final static QName _FileGenerate_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "fileGenerate");
    private final static QName _SignificantDateInterval_QNAME = new QName("http://soap.web_services.services.logFinder.ohapegor.ru/", "significant-date-interval");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }


    /**
     * Create an instance of {@link SignificantDateInterval }
     * 
     */
    public SignificantDateInterval createSignificantDateInterval() {
        return new SignificantDateInterval();
    }

    /**
     * Create an instance of {@link SearchInfo }
     * 
     */
    public SearchInfo createSearchInfo() {
        return new SearchInfo();
    }

    /**
     * Create an instance of {@link ResultLogs }
     * 
     */
    public ResultLogs createResultLogs() {
        return new ResultLogs();
    }


    /**
     * Create an instance of {@link LogSearchResponseClient }
     * 
     */
    public LogSearchResponseClient createLogSearchResponse() {
        return new LogSearchResponseClient();
    }


    /**
     * Create an instance of {@link SearchInfoResult }
     * 
     */
    public SearchInfoResult createSearchInfoResult() {
        return new SearchInfoResult();
    }

    /**
     * Create an instance of {@link LogSearchSOAP }
     * 
     */
    public LogSearchSOAP createLogSearch() {
        return new LogSearchSOAP();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchInfoResult }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "search-info-result")
    public JAXBElement<SearchInfoResult> createSearchInfoResult(SearchInfoResult value) {
        return new JAXBElement<SearchInfoResult>(_SearchInfoResult_QNAME, SearchInfoResult.class, null, value);
    }



    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogSearchSOAP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "logSearch")
    public JAXBElement<LogSearchSOAP> createLogSearch(LogSearchSOAP value) {
        return new JAXBElement<LogSearchSOAP>(_LogSearch_QNAME, LogSearchSOAP.class, null, value);
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogSearchResponseClient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "logSearchResponse")
    public JAXBElement<LogSearchResponseClient> createLogSearchResponse(LogSearchResponseClient value) {
        return new JAXBElement<LogSearchResponseClient>(_LogSearchResponse_QNAME, LogSearchResponseClient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "search-info")
    public JAXBElement<SearchInfo> createSearchInfo(SearchInfo value) {
        return new JAXBElement<SearchInfo>(_SearchInfo_QNAME, SearchInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultLogs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "result-logs")
    public JAXBElement<ResultLogs> createResultLogs(ResultLogs value) {
        return new JAXBElement<ResultLogs>(_ResultLogs_QNAME, ResultLogs.class, null, value);
    }



    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignificantDateInterval }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soap.web_services.services.logFinder.ohapegor.ru/", name = "significant-date-interval")
    public JAXBElement<SignificantDateInterval> createSignificantDateInterval(SignificantDateInterval value) {
        return new JAXBElement<SignificantDateInterval>(_SignificantDateInterval_QNAME, SignificantDateInterval.class, null, value);
    }

}
