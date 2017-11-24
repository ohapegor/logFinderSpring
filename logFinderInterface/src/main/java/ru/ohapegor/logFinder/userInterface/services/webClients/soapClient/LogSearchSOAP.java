
package ru.ohapegor.logFinder.userInterface.services.webClients.soapClient;

import ru.ohapegor.logFinder.entities.SearchInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for logSearch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="logSearch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://soap.web_services.services.logFinder.ohapegor.ru/}searchInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogSearchSOAP", propOrder = {
    "arg0"
})
public class LogSearchSOAP {

    protected SearchInfo arg0;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link SearchInfo }
     *     
     */
    public SearchInfo getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchInfo }
     *     
     */
    public void setArg0(SearchInfo value) {
        this.arg0 = value;
    }

}
