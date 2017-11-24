package ru.ohapegor.logFinder.entities;


public class InvalidSearchInfoException extends RuntimeException {

    private CorrectionCheckResult correctionCheckResult;

    public InvalidSearchInfoException(CorrectionCheckResult correctionCheckResult){
        this.correctionCheckResult = correctionCheckResult;
    }

    public CorrectionCheckResult getCorrectionCheckResult() {
        return correctionCheckResult;
    }

    public Integer getErrorCode(){
        return correctionCheckResult.getErrorCode();
    }

    public String getErrorMessage(){
        return correctionCheckResult.getErrorMessage();
    }


}
