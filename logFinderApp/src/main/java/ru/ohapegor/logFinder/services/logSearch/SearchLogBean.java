package ru.ohapegor.logFinder.services.logSearch;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.*;
import ru.ohapegor.logFinder.entities.FileExtension;

import javax.ejb.Stateless;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;


@Stateless
public class SearchLogBean implements SearchLogService {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogBean.logSearch(SearchInfo searchInfo)");
        List<ResultLogs> logs = getResultLogs(searchInfo);
        SearchInfoResult result = SearchInfoResult.of(searchInfo,logs);
        if (logs == null || logs.isEmpty()) {
            result.setEmptyResultMessage("No logs found");
        }
        logger.info("Exiting SearchLogBean.logSearch()");
        return result;
    }

    @Override
    public  SearchInfoResult correctionCheck(SearchInfo searchInfo) {
        logger.info("Entering SearchLogBean.correctionCheck()"+searchInfo);
        SearchInfoResult result = new SearchInfoResult();

        //3701
        //Missed async method file extension
        if (searchInfo.getRealization()) {
            try {
                FileExtension fileExtension = FileExtension.valueOf(searchInfo.getFileExtension().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                logger.info(CorrectionCheckResult._3701.getErrorMessage() + ", exiting UserInterfaceBean.correctionCheck(SearchInfo searchInfo)");
                throw new InvalidSearchInfoException(CorrectionCheckResult._3701);
            }
            logger.info(searchInfo.getFileExtension() + " - correct file extension.");

        }

        //44
        //Incorrect resource name
        if (searchInfo.getLocation() != null && !searchInfo.getLocation().equals("")
                && findServerNamesInLocation(searchInfo.getLocation()).isEmpty()) {
            logger.info(CorrectionCheckResult._44.getErrorMessage());
            throw new InvalidSearchInfoException(CorrectionCheckResult._44);
        }



        //37
        //Missed mandatory parameter
        if (searchInfo.getRegexp() == null || searchInfo.getRegexp().equals("")) {
            logger.info(CorrectionCheckResult._37.getErrorMessage() + " - regexp , exiting SearchLogBean.correctionCheck()");
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }

        if (searchInfo.getDateIntervals() == null || searchInfo.getDateIntervals().isEmpty()) {
            logger.info(CorrectionCheckResult._37.getErrorMessage() + " - dateInterval list , exiting SearchLogBean.correctionCheck()");
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }


        List<SignificantDateInterval> dateInterval = searchInfo.getDateIntervals();
        Calendar dateFrom = null;
        Calendar dateTo = null;
        for (SignificantDateInterval interval : dateInterval) {
            try {
                if (interval.getDateFrom() == null || interval.getDateTo() == null) {
                    logger.info("Date interval is missing or incorrect, exiting SearchLogBean.correctionCheck()");
                    throw new InvalidSearchInfoException(CorrectionCheckResult._37);
                }

                dateFrom = interval.getDateFrom();
                dateTo = interval.getDateTo();

                //18
                //DateFrom exceeds Present Time
                if (dateFrom.after(Calendar.getInstance())) {
                    logger.info(CorrectionCheckResult._18.getErrorMessage() + " , exiting SearchLogBean.correctionCheck()");
                    throw new InvalidSearchInfoException(CorrectionCheckResult._18);
                }

                //1
                //DateFrom exceeds DateTo
                if (dateFrom.after(dateTo)) {
                    logger.info(CorrectionCheckResult._1.getErrorMessage() + " , exiting SearchLogBean.correctionCheck()");
                    throw new InvalidSearchInfoException(CorrectionCheckResult._1);
                }

            } catch (Exception e) {
                //19
                //Incorrect time format
                logger.error("Exception in SearchLogBean.correctionCheck():" + e);
                e.printStackTrace();
                throw new InvalidSearchInfoException(CorrectionCheckResult._19);
            }
        }

        logger.info("SearchLogInfo is correct, exiting SearchLogBean.correctionCheck()");
        return result;
    }

    private Map<String, String> getAllServerNames() {
        logger.info("Entering SearchLogBean.getAllServerNamesWithCluster()");
        //searching servers in domain's config.xml
        Map<String, String> serverNames = new HashMap<>();
        try {
            String CONFIG_LOCATION = String.format(Config.getString("CONFIG_LOCATION_TEMPLATE"), Config.getString("DOMAIN_NAME"));
            File configXMLFile = new File(CONFIG_LOCATION);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(configXMLFile);
            doc.getDocumentElement().normalize();
            NodeList serverNodes = doc.getElementsByTagName("server");
            //<server> elements
            for (int i = 0; i < serverNodes.getLength(); i++) {
                Node serverNode = serverNodes.item(i);
                NodeList nodesInServer = serverNode.getChildNodes();
                String serverName = null;
                String clusterName = null;
                //nodes inside <server>
                for (int j = 0; j < nodesInServer.getLength(); j++) {
                    Node n = nodesInServer.item(j);
                    //<name>
                    if ("name".equals(n.getNodeName())){
                        serverName = n.getTextContent();
                        //<cluster>
                    } else if ("cluster".equals(n.getNodeName())){
                        clusterName = n.getTextContent();
                    }
                }
                //add server to result
                if (serverName!=null)serverNames.put(serverName,clusterName);
            }
        } catch (Exception e) {
            logger.error("Exception in SearchLogBean.getAllServerNamesWithCluster():" + getStackTrace(e));
        }
        logger.info("Exiting SearchLogBean.getAllServerNamesWithCluster(), found servers : "
                + serverNames.entrySet().stream().map(s -> ("\n[server : " + s.getKey() + "; cluster : " + s.getValue() + "]"))
                .collect(Collectors.joining(" , ")));
        return serverNames;
    }

    private Set<String> findServerNamesInLocation(String location) {
        logger.info("Entering SearchLogBean.findServerNamesInLocation()");

        Map<String, String> servers = getAllServerNames();

        Set<String> result = new HashSet<>();

        if (location == null || location.equals("")) {
            logger.info("Location is not specified, searching logs in all servers");
            result.addAll(servers.keySet());
        } else {
            for (String server : servers.keySet()) {
                if (server.equals(location)) {
                    logger.info("Location matches server name");
                    result = Collections.singleton(server);
                    break;
                    //if location matches cluster name
                } else if (location.equals(servers.get(server))) {
                    logger.info("Location matches cluster, adding server : " + server + " in result");
                    result.add(server);
                }
            }
        }

        logger.info("Exiting SearchLogBean.findServerNamesInLocation()");
        return result;
    }

    private List<ResultLogs> getResultLogs(SearchInfo searchInfo) {
        logger.info("Entering SearchLogBean.getResultLogs()");

        List<ResultLogs> resultLogsList = new ArrayList<>();
        Set<ResultLogs> resultLogsSet = new HashSet<>();
        Set<String> serverNames = findServerNamesInLocation(searchInfo.getLocation());
        try {
            for (String serverName : serverNames) {
                String location = String.format(Config.getString("LOGS_DIRECTORY_TEMPLATE"), Config.getString("DOMAIN_NAME"), serverName);
                logger.info("Searching logs in location : " + location);
                File searchDirectory = new File(location);
                if (!searchDirectory.exists()) {
                    logger.info(location + "not exists!");
                    continue;
                }

                //Searching only *.log* files, excluding access.log
                File[] logFiles = searchDirectory.listFiles(
                        (dir, name) -> !name.contains("access") && name.contains(".log"));

                //if no .log files in directory
                if (logFiles == null || logFiles.length < 1) {
                    //search next server logs
                    continue;
                }

                //0.start parsing log file
                for (File file : logFiles) {
                    logger.info("Searching logs in file : " + file.getName());
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        //Create instances of ResultLogs
                        while (reader.ready()) {
                            String string = reader.readLine();

                            Calendar timeMoment = null;
                            StringBuilder message = null;

                            //1.find start of message
                            if (string.contains("####<")) {
                                //create message
                                message = new StringBuilder(string);
                            } else continue;

                            //2.read message
                            while (message.lastIndexOf("> ") != message.length() - 2 && reader.ready()) {
                                message.append(reader.readLine());
                            }

                            //3.parse message time
                            timeMoment = getMessageTime(message.toString());

                            //4.accept messages only with time in searchInfo interval
                            if (!isInValidTimeInterval(timeMoment, searchInfo)) {
                                continue;
                            }

                            //5.accept message if matches regexp
                            String regexp = searchInfo.getRegexp();
                            Pattern pattern = Pattern.compile(regexp);
                            Matcher matcher = pattern.matcher(message);
                            if (!matcher.matches()) {
                                continue;
                            }

                            //6.create instance of ResultLogs
                            ResultLogs resultLogs = new ResultLogs();
                            resultLogs.setTimeMoment(timeMoment);
                            resultLogs.setFileName(file.getName());
                            resultLogs.setContent(message.toString());

                            //7.add log in set
                            resultLogsSet.add(resultLogs);
                        }
                        //close reader
                    }
                }
            }
            //8.sort by time moment
            resultLogsList.addAll(resultLogsSet);
            resultLogsList.sort(Comparator.comparing(ResultLogs::getTimeMoment));
            logger.info("Exiting SearchLogBean.getResultLogs(). Logs count - " + resultLogsList.size());

        } catch (Exception e) {
            logger.error("Exception in getResultLogs(SearchInfo searchInfo) : " + getStackTrace(e));
            logger.info(" Exiting getResultLogs(SearchInfo searchInfo)");
            throw new RuntimeException("Exception while parsing Logs", e);
        }
        return resultLogsList;
    }

    private Calendar getMessageTime(String message) {
        Calendar timeMoment = null;
        Pattern pattern = Pattern.compile(" <\\d{13}> ");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            timeMoment = Calendar.getInstance();
            timeMoment.setTime(new Date(Long.parseLong(matcher.group().substring(2,15))));
        }
        return timeMoment;
    }

    private boolean isInValidTimeInterval(Calendar timeMoment, SearchInfo searchInfo) {
        return searchInfo.getDateIntervals().stream().anyMatch(interval ->
                !timeMoment.before(interval.getDateFrom()) &&
                        !timeMoment.after(interval.getDateTo()));
    }
}

