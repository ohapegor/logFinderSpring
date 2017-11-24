package ru.ohapegor.logFinder.entities;


public enum CorrectionCheckResult {

    _0("Success",0),
    _1("DateFrom exceeds DateTo",1),
    _18("DateFrom exceeds Present Time",18),
    _19("Incorrect time format",19),
    _37("Missed mandatory parameter",37),
    _44("Incorrect resource name",44),
    _3701("Missed async method file extension",3701);
    private String errorMessage;
    private int errorCode;

    CorrectionCheckResult(String comment, int errorCode) {
        this.errorMessage = comment;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
