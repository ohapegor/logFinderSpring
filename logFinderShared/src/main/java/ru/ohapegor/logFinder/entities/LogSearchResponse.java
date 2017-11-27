package ru.ohapegor.logFinder.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogSearchResponse {

    private SearchInfoResult searchInfoResult;

    public SearchInfoResult getSearchInfoResult() {
        return searchInfoResult;
    }

    public void setSearchInfoResult(SearchInfoResult searchInfoResult) {
        this.searchInfoResult = searchInfoResult;
    }

    public LogSearchResponse(SearchInfoResult searchInfoResult) {
        this.searchInfoResult = searchInfoResult;
    }

    public LogSearchResponse() {
    }
}
