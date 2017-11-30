package ru.ohapegor.logFinder.services.logSearchService;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;


@Service("searchLogService")
public class SearchLogBean implements SearchLogService {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public SearchInfoResult logSearch(SearchInfo searchInfo) {
        logger.info("Entering SearchLogBean.logSearch()");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<ResultLogs>> future = executor.submit(() -> getResultLogs(searchInfo));
        SearchInfoResult result = null;
        try {
            List<ResultLogs> logs = searchInfo.getRealization() ?
                    future.get(30, TimeUnit.SECONDS) :
                    future.get(10, TimeUnit.SECONDS);
            Objects.requireNonNull(logs, "SearchLogService.getResultLogs() should not return null!");
            result = SearchInfoResult.of(searchInfo, logs);
            result.setEmptyResultMessage(logs.isEmpty() ? "No logs Found" : null);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Exception in SearchLogBean.logSearch() : " + getStackTrace(e));
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            logger.error("Reached execution timeout, exiting SearchLogBean.logSearch();" +
                    " cancel allowed = "+future.cancel(true));
            throw new TooLongExecutionException("Searching log exceeded allowed time interval");
        }
        logger.info("Exiting SearchLogBean.logSearch()");
        return result;
    }

    @Override
    public CorrectionCheckResult correctionCheck(SearchInfo searchInfo) {
        logger.info("Entering SearchLogBean.correctionCheck()");
        //3701
        //Missed async method file extension
        if (searchInfo.getRealization()) {
            try {
                FileExtension.valueOf(searchInfo.getFileExtension().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                throw new InvalidSearchInfoException(CorrectionCheckResult._3701);
            }
        }

        //44
        //Incorrect resource name
        if (searchInfo.getLocation() != null && !searchInfo.getLocation().equals("")
                && findServerNamesInLocation(searchInfo.getLocation()).isEmpty()) {
            throw new InvalidSearchInfoException(CorrectionCheckResult._44);
        }

        //37
        //Missed mandatory parameter
        if (searchInfo.getRegexp() == null || searchInfo.getRegexp().equals("")) {
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }

        if (searchInfo.getDateIntervals() == null || searchInfo.getDateIntervals().isEmpty()) {
            throw new InvalidSearchInfoException(CorrectionCheckResult._37);
        }


        List<SignificantDateInterval> dateInterval = searchInfo.getDateIntervals();
        Calendar dateFrom = null;
        Calendar dateTo = null;
        for (SignificantDateInterval interval : dateInterval) {
            //set min time if time not specified
            if (interval.getDateFrom() == null) {
                interval.setDateFrom(Calendar.getInstance());
                interval.getDateFrom().setTime(new Date(0));
            }

            //set max time if time not specified
            if (interval.getDateTo() == null) {
                interval.setDateTo(Calendar.getInstance());
                interval.getDateTo().setTime(new Date(Long.MAX_VALUE));
            }

            dateFrom = interval.getDateFrom();
            dateTo = interval.getDateTo();

            //18
            //DateFrom exceeds Present Time
            if (dateFrom.after(Calendar.getInstance())) {
                throw new InvalidSearchInfoException(CorrectionCheckResult._18);
            }

            //1
            //DateFrom exceeds DateTo
            if (dateFrom.after(dateTo)) {
                throw new InvalidSearchInfoException(CorrectionCheckResult._1);
            }
        }
        logger.info("SearchLogInfo is correct, exiting SearchLogBean.correctionCheck()");
        return CorrectionCheckResult._0;
    }

    private Set<String> findServerNamesInLocation(String location) {
        logger.info("Entering SearchLogBean.findServerNamesInLocation()");

        Map<String, String> allServers = getAllServerNamesWithCluster();

        Set<String> result = new HashSet<>();

        if (location == null || location.equals("")) {
            logger.info("Location is not specified, searching logs in all servers");
            result.addAll(allServers.keySet());
        } else {
            for (String server : allServers.keySet()) {
                if (server.equals(location)) {
                    result = Collections.singleton(server);
                    break;
                    //if location matches cluster name
                } else if (location.equals(allServers.get(server))) {
                    result.add(server);
                }
            }
        }

        logger.info("Exiting SearchLogBean.findServerNamesInLocation(), searching Logs in servers : " + result);
        return result;
    }

    private Map<String, String> getAllServerNamesWithCluster() {
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
                    if ("name".equals(n.getNodeName())) {
                        serverName = n.getTextContent();
                        //<cluster>
                    } else if ("cluster".equals(n.getNodeName())) {
                        clusterName = n.getTextContent();
                    }
                }
                //add server to result
                if (serverName != null) serverNames.put(serverName, clusterName);
            }
        } catch (Exception e) {
            logger.error("Exception in SearchLogBean.getAllServerNamesWithCluster():" + getStackTrace(e));
        }
        logger.info("Exiting SearchLogBean.getAllServerNamesWithCluster(), found servers : "
                + serverNames.entrySet().stream().map(s -> ("\n[server : " + s.getKey() + "; cluster : " + s.getValue() + "]"))
                .collect(Collectors.joining(" , ")));
        return serverNames;
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
                    if (Thread.currentThread().isInterrupted()){
                        logger.info("Execution of SearchLogBean.getResultLogs() is interrupted");
                        throw new InterruptedException();
                    }
                    logger.info("Searching logs in file : " + file.getName());
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        //Create instances of ResultLogs
                        while (reader.ready()) {
                            String startLine = reader.readLine();

                            Calendar timeMoment = null;
                            StringBuilder message = null;

                            //1.find start of message
                            if (startLine.startsWith("####<")) {
                                //create message
                                message = new StringBuilder(startLine);
                            } else {
                                continue;
                            }

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
                            if (!Pattern.compile(searchInfo.getRegexp()).matcher(message).matches()) {
                                continue;
                            }

                            //6.add log in set
                            resultLogsSet.add(ResultLogs.of(file.getName(), timeMoment, message.toString()));
                        }
                        //close reader
                    }
                }
            }
            //8.sort by time moment
            resultLogsList.addAll(resultLogsSet);
            resultLogsList.sort(Comparator.comparing(ResultLogs::getTimeMoment));

        } catch (Exception e) {
            logger.error("Exception in SearchLogBean.getResultLogs() : " + getStackTrace(e));
            throw new RuntimeException("Exception while parsing Logs", e);
        }
        logger.info(" Exiting SearchLogBean.getResultLogs(); Logs count - " + resultLogsList.size());
        return resultLogsList;
    }

    private Calendar getMessageTime(String message) {
        Calendar timeMoment = null;
        Pattern pattern = Pattern.compile(" <\\d{13}> ");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            timeMoment = Calendar.getInstance();
            timeMoment.setTime(new Date(Long.parseLong(matcher.group().substring(2, 15))));
        }
        return timeMoment;
    }

    private boolean isInValidTimeInterval(Calendar timeMoment, SearchInfo searchInfo) {
        return searchInfo.getDateIntervals().stream().anyMatch(interval ->
                !timeMoment.before(interval.getDateFrom()) &&
                        !timeMoment.after(interval.getDateTo()));
    }

}

