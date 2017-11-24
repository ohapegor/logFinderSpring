package ru.ohapegor.logFinder.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "searchInfoResult")
@JsonRootName("searchInfoResult")
public class SearchInfoResult {

    private long errorCode = CorrectionCheckResult._0.getErrorCode();

    private String errorMessage = CorrectionCheckResult._0.getErrorMessage();

    private String emptyResultMessage;

    private String filePath;

    private SearchInfo searchInfo;

    private List<ResultLogs> resultLogs;

    public SearchInfoResult() {
    }

    public SearchInfoResult(CorrectionCheckResult checkResult) {
        this.errorCode = checkResult.getErrorCode();
        this.errorMessage = checkResult.getErrorMessage();
    }


    @XmlElement(name = "fileLocation")
    @JsonProperty("fileLocation")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @XmlElement(name = "errorCode")
    @JsonProperty("errorCode")
    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    @XmlElement(name = "errorMessage")
    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @XmlElement(name = "emptyResultMessage")
    @JsonProperty("emptyResultMessage")
    public String getEmptyResultMessage() {
        return emptyResultMessage;
    }

    public void setEmptyResultMessage(String emptyResultMessage) {
        this.emptyResultMessage = emptyResultMessage;
    }

    @XmlElement(name = "resultLogs")
    @JsonProperty("resultLogs")
    public List<ResultLogs> getResultLogs() {
        return resultLogs;
    }

    public void setResultLogs(List<ResultLogs> resultLogs) {
        this.resultLogs = resultLogs;
    }

    @XmlElement(name = "searchInfo")
    @JsonProperty("searchInfo")
    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    public static SearchInfoResult of(SearchInfo searchInfo){
        SearchInfoResult searchInfoResult = new SearchInfoResult();
        searchInfoResult.setSearchInfo(searchInfo);
        return searchInfoResult;
    }


    public static SearchInfoResult of(SearchInfo searchInfo, List<ResultLogs> resultLogs){
        SearchInfoResult searchInfoResult = new SearchInfoResult();
        searchInfoResult.setSearchInfo(searchInfo);
        searchInfoResult.setResultLogs(resultLogs);
        return searchInfoResult;
    }


    @Override
    public String toString() {
        return "SearchInfoResult{" +
                "errorCode=" + errorCode +
                ", errorMessage='" + errorMessage + '\'' +
                ", emptyResultMessage='" + emptyResultMessage + '\'' +
                ", resultLogs=" + resultLogs +
                ", filePath='" + filePath + '\'' +
                ", searchInfo=" + searchInfo +
                '}';
    }
}
