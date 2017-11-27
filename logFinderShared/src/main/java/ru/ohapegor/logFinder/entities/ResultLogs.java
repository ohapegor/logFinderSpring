package ru.ohapegor.logFinder.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.Calendar;


@XmlRootElement
@JsonRootName("resultLogs")
public class ResultLogs {

    private Calendar timeMoment;

    private String fileName;


    private String content;

    @XmlElement
    @JsonProperty
    @XmlSchemaType(name = "dateTime")
    public Calendar getTimeMoment() {
        return timeMoment;
    }

    public void setTimeMoment(Calendar timeMoment) {
        this.timeMoment = timeMoment;
    }

    @XmlElement
    @JsonProperty
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlElement
    @JsonProperty
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static ResultLogs of(String filename, Calendar timeMoment, String message){
        ResultLogs resultLogs = new ResultLogs();
        resultLogs.setFileName(filename);
        resultLogs.setTimeMoment(timeMoment);
        resultLogs.setContent(message);
        return resultLogs;
    }

    @Override
    public String toString() {
        return "ResultLogs{" +
                "timeMoment=" + timeMoment.getTime() +
                ", fileName='" + fileName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultLogs)) return false;

        ResultLogs that = (ResultLogs) o;

        if (timeMoment != null ? !timeMoment.equals(that.timeMoment) : that.timeMoment != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        int result = timeMoment != null ? timeMoment.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

}
