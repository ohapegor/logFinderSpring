package ru.ohapegor.logFinder.services.file.remover;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.config.Config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Date;

//@Singleton
//@Startup
public class FileRemoverProgrammaticBean {

    private static final Logger logger = LogManager.getLogger();

    @Resource
    private TimerService timerService;

    @PostConstruct
    private void init(){
        ScheduleExpression schedule = new ScheduleExpression().second(30).minute("*").hour("*");
        timerService.createCalendarTimer(schedule);
        logger.info("timer initialized in FileRemoverProgrammaticBean.init()");
    }

    //@PostConstruct
    private void setTimer(long intervalDuration) {
        if (timerService.getAllTimers().size() == 0) {
            logger.info("Setting a programmatic timeout for " + intervalDuration + " milliseconds from now.");
            timerService.createTimer(intervalDuration, "Created new programmatic timer");
        }
    }

    @Timeout
    private void removeOldFiles(Timer timer) {
        logger.info("Entering FileRemoverProgrammaticBean.removeOldFiles()");
        try {
            timerService.createTimer(Config.getLong("DELETE_INTERVAL"), "Created new programmatic timer");

            // properties.load(new FileReader(new File("g:/properties.properties")));
            long currentTime = new Date().getTime();
            File directory = new File(Config.getString("GENERATED_FILE_LOCATION"));
            File[] files = null;

            if (Files.exists(directory.toPath())){
                files = directory.listFiles();
            }

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if ((currentTime - file.lastModified()) > Config.getLong("FILE_LIFE_INTERVAL")) {
                        logger.info("Trying to remove file - " + file.getAbsolutePath() + " success = " + file.delete());
                    }
                }
            } else {
                logger.info("No files to remove in FileRemoverProgrammaticBean.removeOldFiles()");
            }
        } catch (Exception e) {
            logger.error("Exception in FileRemoverProgrammaticBean.removeOldFiles() :" + e);
            e.printStackTrace();
        }
        logger.info("Exiting FileRemoverProgrammaticBean.removeOldFiles()");
    }


}
