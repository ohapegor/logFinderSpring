package ru.ohapegor.logFinder.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
@JsonRootName("searchInfoResult")
public class SearchInfoResult {

    private long errorCode = 0;

    private String errorMessage = CorrectionCheckResult._0.getErrorMessage();

    private String emptyResultMessage;

    private String filePath;

    private SearchInfo searchInfo;

    private List<ResultLogs> resultLogs;

    public SearchInfoResult() {
    }


    public SearchInfoResult(CorrectionCheckResult correctionCheckResult) {
        this.errorMessage = correctionCheckResult.getErrorMessage();
        this.errorCode = correctionCheckResult.getErrorCode();
    }

    @XmlElement
    @JsonProperty
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @XmlElement
    @JsonProperty
    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    @XmlElement
    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @XmlElement
    @JsonProperty
    public String getEmptyResultMessage() {
        return emptyResultMessage;
    }

    public void setEmptyResultMessage(String emptyResultMessage) {
        this.emptyResultMessage = emptyResultMessage;
    }

    @XmlElement
    @JsonProperty
    public List<ResultLogs> getResultLogs() {
        return resultLogs;
    }

    public void setResultLogs(List<ResultLogs> resultLogs) {
        this.resultLogs = resultLogs;
    }

    @XmlElement
    @JsonProperty
    public SearchInfo getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    private SearchInfoResult(SearchInfo searchInfo) {
        this.searchInfo = searchInfo;
    }

    private SearchInfoResult(SearchInfo searchInfo, List<ResultLogs> resultLogs) {
        this.searchInfo = searchInfo;
        this.resultLogs = resultLogs;
    }

    public static SearchInfoResult of(SearchInfo searchInfo){
        return new SearchInfoResult(searchInfo);
    }

    public static SearchInfoResult of(SearchInfo searchInfo, List<ResultLogs> resultLogs){
        return new SearchInfoResult(searchInfo,resultLogs);
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
