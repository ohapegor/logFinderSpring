package ru.ohapegor.logFinder.services.file.reader;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ohapegor.logFinder.entities.ResultLogs;
import ru.ohapegor.logFinder.entities.SearchInfo;
import ru.ohapegor.logFinder.entities.SearchInfoResult;
import ru.ohapegor.logFinder.entities.SignificantDateInterval;

import javax.ejb.Stateless;
import javax.swing.text.rtf.RTFEditorKit;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

import ru.ohapegor.logFinder.entities.FileExtension;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;
import static ru.ohapegor.logFinder.entities.FileExtension.*;

@Stateless
public class FileReaderBean implements FileReaderService {

    private static final Logger logger = LogManager.getLogger();

    private String[] timePatterns = {"yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ssXXX"};

    public SearchInfoResult readInfoFromFile(File file) {
        logger.info("Entering FileReaderBean.readInfoFromFile(File " + file.getName() + ") file = " + file.getAbsolutePath());
        SearchInfoResult searchInfoResult = null;
        try {
            String path = file.getAbsolutePath();
            FileExtension extension = FileExtension.valueOf(path.substring(path.lastIndexOf(".") + 1, path.length()).toUpperCase());
            switch (extension) {
                case PDF:
                    searchInfoResult = readFromPdfFile(file);
                    break;
                case RTF:
                    searchInfoResult = readFromRtfFile(file);
                    break;
                case XML:
                    searchInfoResult = readFromXmlFile(file);
                    break;
                case LOG:
                    searchInfoResult = readFromLogFile(file);
                    break;
                case HTML:
                    searchInfoResult = readFromHtmlFile(file);
                    break;
                case DOCX:
                    searchInfoResult = readFromDocxFile(file);
                    break;
                default: {
                    logger.info("Unknown file extension = " + extension);
                }
            }
            if (searchInfoResult == null || searchInfoResult.getResultLogs() == null || searchInfoResult.getResultLogs().isEmpty()) {
                searchInfoResult.setEmptyResultMessage("No logs found!");
            }
        } catch (Exception e) {
            logger.error("Exception in  FileReaderBean.readInfoFromFile(File " + file.getName() + ") e = " + getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Exiting FileReaderBean.readInfoFromFile(File file)");
        return searchInfoResult;

    }

    private SearchInfoResult readFromPdfFile(File file) {
        logger.info("Entering FileReaderBean.readFromPdfFile(File " + file.getName() + ")");
        PdfReader reader = null;
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try {

            reader = new PdfReader(file.getAbsolutePath());

            //parse searchInfo
            String[] info = PdfTextExtractor.getTextFromPage(reader, 1).replaceAll("Page \\d*", "").split("\n");

            //parse regexp
            String[] regexpInfo = info[0].split(" ");
            String regexp = regexpInfo.length > 1 ? regexpInfo[1] : "";

            //parse location
            String[] locationInfo = info[1].split(" ");
            String location = locationInfo.length > 1 ? regexpInfo[1] : "";

            //dateIntervals
            List<SignificantDateInterval> intervals = new ArrayList<>();
            for (int i = 3; i < info.length - 2; i++) {
                SignificantDateInterval dateInterval = new SignificantDateInterval();
                //dateFrom
                String[] dates = info[i].split(" ");
                dateInterval.setDateFrom(parseDate(dates[0]));
                dateInterval.setDateTo(parseDate(dates[1]));
                intervals.add(dateInterval);
            }

            //create searchInfo
            searchInfo = SearchInfo.of(regexp, location, intervals, PDF);
            searchInfoResult = SearchInfoResult.of(searchInfo);
            logger.info("SearchInfo successfully parsed in " + file.getName());

            //parse logs
            List<ResultLogs> resultLogsList = new ArrayList<>();
            StringBuilder logString = new StringBuilder();
            for (int i = 2; i <= reader.getNumberOfPages(); i++) {
                logString.append(PdfTextExtractor.getTextFromPage(reader, i).replaceAll("Page \\d*", ""));
            }

            String[] logs = logString.toString().split("Log \\d*");
            for (int i = 1; i < logs.length; i++) {//skip Header - Найденные логи
                ResultLogs resultLog = new ResultLogs();
                String[] rows = logs[i].split("\n");
                //time moment
                Calendar timeMoment = parseDate(rows[1].replaceAll("Time moment:", " ").trim());
                resultLog.setTimeMoment(timeMoment);

                //file name
                String fileName = rows[2].replaceAll("File:", " ").trim();
                resultLog.setFileName(fileName);

                //message
                String message = rows[4];
                resultLog.setContent(message);

                resultLogsList.add(resultLog);
            }

            //add logs to result
            searchInfoResult.setResultLogs(resultLogsList);

            reader.close();

        } catch (IOException e) {
            logger.error("Exception in FileReaderBean.readFromPdfFile(File " + file.getName() + "):" + getStackTrace(e));
        } finally {
            if (reader != null) reader.close();
        }
        logger.info("Exiting FileReaderBean.readFromPdfFile(File " + file.getName() + "), " + searchInfo);
        return searchInfoResult;
    }

    private SearchInfoResult readFromRtfFile(File file) {
        logger.info("Entering FileReaderBean.readFromRtfFile(File " + file.getName() + ")");
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try {
            //parse searchInfo
            String rtfTest = new String(Files.readAllBytes(file.toPath()));
            String[] table = rtfTest.split("cellx10000");

            //regexp
            String regexp = table[1].split("\\\\cell")[1].trim();

            //location
            String location = table[2].split("\\\\cell")[1].trim();

            //dateIntervals
            List<SignificantDateInterval> intervals = new ArrayList<>();
            for (int i = 4; i < table.length; i++) {
                String dateFrom = table[i].split("\\\\cell")[0].trim();
                String dateTo = table[i].split("\\\\cell")[1].trim();
                SignificantDateInterval interval = new SignificantDateInterval();
                interval.setDateFrom(parseDate(dateFrom));
                interval.setDateTo(parseDate(dateTo));
                intervals.add(interval);
            }

            //create searchInfo
            searchInfo = SearchInfo.of(regexp, location, intervals, RTF);
            searchInfoResult = SearchInfoResult.of(searchInfo);
            logger.info("SearchInfo successfully parsed in " + file.getName());

            //parse logs
            RTFEditorKit rtf = new RTFEditorKit();
            javax.swing.text.Document doc = rtf.createDefaultDocument();

            try (FileInputStream fis = new FileInputStream(file)) {
                rtf.read(fis, doc, 0);
            }
            String[] logs = doc.getText(0, doc.getLength()).split("Log # ");
            // System.out.println("logs.length = " + logs.length);
            List<ResultLogs> resultLogs = new ArrayList<>();
            for (int i = 1; i < logs.length; i++) {
                ResultLogs log = new ResultLogs();
                String logString = logs[i];

                //parse time moment
                String timeMoment = logString.substring(logString.indexOf("Time moment : "), logString.indexOf("File Name : ")).
                        split("Time moment : ")[1].trim();
                log.setTimeMoment(parseDate(timeMoment));

                //parse filename
                String fileName = logString.substring(logString.indexOf("File Name : "), logString.indexOf("Message : ")).split("File Name : ")[1].trim();
                log.setFileName(fileName);

                //parse message
                String message = logString.split("Message : ")[1].trim();
                log.setContent(message);
                resultLogs.add(log);
            }

            //add logs to result
            searchInfoResult.setResultLogs(resultLogs);
        } catch (Exception e) {
            logger.error("Exception in  FileReaderBean.readFromRtfFile(File " + file.getName() + ") :" + getStackTrace(e));
        }
        logger.info("Exiting FileReaderBean.readFromRtfFile(File " + file.getName() + "), " + searchInfo);
        return searchInfoResult;
    }

    private SearchInfoResult readFromXmlFile(File file) {
        logger.info("Entering FileReaderBean.readFromXmlFile(File " + file.getName() + ")");
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try {
            //parse SearchInfo
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            //check searchInfo exists
            Node searchInfoNode = doc.getElementsByTagName("searchInfo").item(0);
            Objects.requireNonNull(searchInfoNode, "searchInfo node not found in " + file.getName());

            //regexp
            Node regexpNode = doc.getElementsByTagName("regexp").item(0);
            Objects.requireNonNull(regexpNode, "regexp node not found in " + file.getName());
            String regexp = regexpNode.getTextContent();

            //location
            Node locationNode = doc.getElementsByTagName("location").item(0);
            Objects.requireNonNull(locationNode, "location node not found in " + file.getName());
            String location = locationNode.getTextContent();

            //dateIntervals
            NodeList dateIntervalsNodes = doc.getElementsByTagName("dateIntervals");
            List<SignificantDateInterval> dateIntervals = new ArrayList<>();
            for (int i = 0; i < dateIntervalsNodes.getLength(); i++) {
                Node dateFromNode = dateIntervalsNodes.item(i).getFirstChild();
                Objects.requireNonNull("no dateFromNode found in dateIntervals");
                Calendar dateFrom = parseDate(dateFromNode.getTextContent());
                Node dateToNode = dateIntervalsNodes.item(i).getLastChild();
                Objects.requireNonNull("no dateFromNode found in dateIntervals");
                Calendar dateTo = parseDate(dateToNode.getTextContent());
                SignificantDateInterval significantDateInterval = SignificantDateInterval.of(dateFrom, dateTo);
                dateIntervals.add(significantDateInterval);
            }

            //create searchInfo
            searchInfo = SearchInfo.of(regexp, location, dateIntervals, XML);
            logger.info("Search info successfully parsed : " + searchInfo + "\n Parsing logs");

            //create searchInfoResult
            searchInfoResult = SearchInfoResult.of(searchInfo);

            //parse logs
            NodeList logsNodes = doc.getElementsByTagName("resultLogs");
            List<ResultLogs> resultLogs = new ArrayList<>();
            if (logsNodes.getLength() < 0) {
                searchInfoResult.setEmptyResultMessage("No logs found");
            } else {
                for (int i = 0; i < logsNodes.getLength(); i++) {
                    resultLogs.add(getResultLogFromNode(logsNodes.item(i)));
                }
            }
            searchInfoResult.setResultLogs(resultLogs);
        } catch (Exception e) {
            logger.error("Exception in FileReaderBean.readFromXmlFile(File " + file.getName() + ") : " + getStackTrace(e));
            e.printStackTrace();
        }
        logger.info("Exiting FileReaderBean.readFromXmlFile(File " + file.getName() + "), " + searchInfo);
        return searchInfoResult;
    }

    private ResultLogs getResultLogFromNode(Node node) {
        NodeList nodeList = node.getChildNodes();
        ResultLogs resultLogs = new ResultLogs();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            switch (n.getNodeName()) {
                case "timeMoment":
                    resultLogs.setTimeMoment(parseDate(n.getTextContent()));
                    break;
                case "fileName":
                    resultLogs.setFileName(n.getTextContent());
                    break;
                case "content":
                    resultLogs.setContent(n.getTextContent());
                    break;
                default:
                    throw new InvalidFileException("unknown tag in xml document : " + n.getNodeName());
            }
        }
        return resultLogs;
    }


    private SearchInfoResult readFromLogFile(File file) {
        logger.info("Entering FileReaderBean.readFromLogFile(File " + file.getName() + ")");
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            //parse searchInfo

            reader.readLine();
            reader.readLine();

            //regexp
            String regexp = reader.readLine().split("\\|")[2].trim();

            //location
            String location = reader.readLine().split("\\|")[2].trim();

            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();

            //dateIntervals
            String timeLine = null;
            List<SignificantDateInterval> intervals = new ArrayList<>();
            while (!(timeLine = reader.readLine()).trim().equals("")) {
                SignificantDateInterval interval = new SignificantDateInterval();
                String[] strings = timeLine.split("\\|");
                Calendar dateFrom = parseDate(strings[1].trim());
                Calendar dateTo = parseDate(strings[2].trim());
                interval.setDateFrom(dateFrom);
                interval.setDateTo(dateTo);
                intervals.add(interval);
            }

            searchInfo = SearchInfo.of(regexp, location, intervals, LOG);
            searchInfoResult = SearchInfoResult.of(searchInfo);
            logger.info("Search info successfully parsed : " + searchInfo + ";\n Parsing logs");


            reader.readLine();
            reader.readLine();
            reader.readLine();

            //parse logs
            List<ResultLogs> resultLogs = new ArrayList<>();
            while (reader.ready()) {
                ResultLogs log = new ResultLogs();
                reader.readLine();
                String time = reader.readLine();
                String fileString = reader.readLine();
                StringBuilder msg = new StringBuilder();
                String s = null;
                while (reader.ready() && (s = reader.readLine()) != null && !s.trim().equals("")) {
                    msg.append(s);
                }

                log.setTimeMoment(parseDate(time.split(" ")[3]));
                log.setFileName(fileString.split(" ")[2]);
                log.setContent(msg.toString());
                resultLogs.add(log);
            }

            //add logs to result
            searchInfoResult.setResultLogs(resultLogs);

        } catch (Exception e) {
            logger.info("Exception in FileReaderBean.readFromLogFile(File " + file.getName() + ") e=" + getStackTrace(e));
        }
        logger.info("Exiting FileReaderBean.readFromLogFile(File " + file.getName() + ") searchInfo = " + searchInfo);
        return searchInfoResult;
    }

    private SearchInfoResult readFromHtmlFile(File file) {
        logger.info("Entering FileReaderBean.readFromHtmlFile(File " + file.getName() + ")");
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try {
            //get document
            org.jsoup.nodes.Document html = Jsoup.parse(file, "UTF-8");

            //read searchInfo
            Element searchInfoTable = html.getElementById("searchInfo");

            //regexp
            Element regexpElem = html.getElementById("regexp");
            Objects.requireNonNull(regexpElem, "no regexp element found in " + file.getName());
            String regexp = regexpElem.text();

            //location
            Element locationElem = html.getElementById("location");
            Objects.requireNonNull(regexpElem, "no location element found in " + file.getName());
            String location = locationElem.text();

            //dateIntervals
            Elements searchInfoRows = searchInfoTable.getElementsByTag("tr");
            List<SignificantDateInterval> dateIntervals = new ArrayList<>();
            for (int i = 4; i < searchInfoRows.size(); i++) {
                Elements dates = searchInfoRows.get(i).getElementsByTag("td");
                SignificantDateInterval dateInterval = new SignificantDateInterval();
                dateInterval.setDateFrom(parseDate(dates.get(0).text()));
                dateInterval.setDateTo(parseDate(dates.get(1).text()));
                dateIntervals.add(dateInterval);
            }
            searchInfo = SearchInfo.of(regexp, location, dateIntervals, HTML);
            searchInfoResult = SearchInfoResult.of(searchInfo);
            logger.info("Search info successfully parsed : " + searchInfo + ";\n Parsing logs");

            //parse logs
            List<ResultLogs> resultLogs = new ArrayList<>();
            Element logsTable = html.getElementById("logs");
            Elements logRows = logsTable.getElementsByTag("tr");
            for (int i = 1; i < logRows.size(); i++) {
                Elements logContents = logRows.get(i).getElementsByTag("td");
                ResultLogs log = new ResultLogs();
                log.setTimeMoment(parseDate(logContents.get(0).text()));
                log.setFileName(logContents.get(1).text());
                log.setContent(logContents.get(2).text());
                resultLogs.add(log);
            }

            //add logs to result
            searchInfoResult.setResultLogs(resultLogs);
        } catch (Exception e) {
            logger.error("Exception in FileReaderBean.readFromHtmlFile(File " + file.getName() + "):" + getStackTrace(e));
        }
        logger.info("Exiting FileReaderBean.readFromHtmlFile(File " + file.getName() + "), " + searchInfo);
        return searchInfoResult;
    }


    private SearchInfoResult readFromDocxFile(File file) {
        logger.info("Entering FileReaderBean.readFromDocxFile(File " + file.getName() + ")");
        SearchInfoResult searchInfoResult = null;
        SearchInfo searchInfo = null;
        try (FileInputStream fis = new FileInputStream(file)) {

            //parse searchInfo
            XWPFDocument document = new XWPFDocument(fis);
            XWPFTable infoTable = document.getTables().get(0);

            //regexp
            String regexp = infoTable.getRow(0).getCell(1).getText();

            //location
            String location = infoTable.getRow(1).getCell(1).getText();

            //dateIntervals
            List<SignificantDateInterval> intervals = new ArrayList<>();
            for (int i = 3; i < infoTable.getRows().size(); i++) {
                String dateFrom = infoTable.getRow(i).getCell(0).getText().trim();
                String dateTo = infoTable.getRow(i).getCell(1).getText().trim();
                SignificantDateInterval interval = new SignificantDateInterval();
                interval.setDateFrom(parseDate(dateFrom));
                interval.setDateTo(parseDate(dateTo));
                intervals.add(interval);
            }

            searchInfo = searchInfo.of(regexp, location, intervals, DOCX);
            searchInfoResult = SearchInfoResult.of(searchInfo);
            logger.info("Search info successfully parsed : " + searchInfo + ";\n Parsing logs");

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            List<ResultLogs> resultLogs = new ArrayList<>();
            for (XWPFParagraph paragraph : paragraphs) {
                if (!paragraph.getText().trim().startsWith("Log")) continue;
                ResultLogs log = new ResultLogs();
                String[] logLines = paragraph.getText().split("\n");

                //time moment
                String timeMoment = logLines[1].split("Time moment : ")[1];
                log.setTimeMoment(parseDate(timeMoment));

                //filename
                String fileName = logLines[2].split("File : ")[1];
                log.setFileName(fileName);

                //message
                String message = logLines[3];
                log.setContent(message);

                resultLogs.add(log);
            }

            //add logs to result
            searchInfoResult.setResultLogs(resultLogs);
        } catch (Exception e) {
            logger.error("Exception in FileReaderBean.readFromDocxFile(File " + file.getName() + "):" + getStackTrace(e));
        }
        logger.info("Exiting FileReaderBean.readFromDocxFile(File " + file.getName() + "), " + searchInfo);
        return searchInfoResult;
    }

    private Calendar parseDate(String dateString) {
        Calendar result = null;
        for (String pattern : timePatterns) {
            try {
                Calendar timeMoment = Calendar.getInstance();
                timeMoment.setTime(new SimpleDateFormat(pattern).parse(dateString));
                result = timeMoment;
            } catch (Exception ignored) {

            }
        }
        if (result == null) {
            logger.error("Failed parsing time in FileReaderBean.parseDate(); dateString = " + dateString);
        }
        return result;
    }
}