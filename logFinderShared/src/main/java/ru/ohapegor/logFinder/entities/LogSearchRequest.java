package ru.ohapegor.logFinder.entities;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class LogSearchRequest {


    private SearchInfo searchInfo;

    public LogSearchRequest(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    public LogSearchRequest() {
    }

    @XmlElement
    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    @Override
    public String toString() {
        return "LogSearchRequest{" +
                "searchInfo=" + searchInfo +
                '}';
    }

}
