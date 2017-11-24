
package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import ru.ohapegor.logFinder.entities.SearchInfoResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for logSearchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="logSearchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://soap.web_services.services.logFinder.ohapegor.ru/}searchInfoResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogSearchResponseClient", propOrder = {
    "_return"
})
public class LogSearchResponseClient {

    @XmlElement(name = "return")
    protected SearchInfoResult _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link SearchInfoResult }
     *     
     */
    public SearchInfoResult getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchInfoResult }
     *     
     */
    public void setReturn(SearchInfoResult value) {
        this._return = value;
    }

}
