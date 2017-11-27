package ru.ohapegor.logFinder.services.fileServices.remover;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ru.ohapegor.logFinder.config.Config;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//programmatic file remover
/*
@Configuration
@EnableScheduling
public class ConfigBean implements SchedulingConfigurer {

    private static final Logger logger = LogManager.getLogger();


    private void removeOldFiles(){
        try {
            logger.info("Invoking ConfigBean.removeOldFiles()");

            // properties.load(new FileReader(new File("g:/properties.properties")));
            long currentTime = new Date().getTime();
            File[] files = new File(Config.getString("GENERATED_FILE_LOCATION")).listFiles();
            if (files == null || files.length < 1) return;
            for (File file : files) {
                if ((currentTime - file.lastModified()) > Config.getLong("FILE_LIFE_INTERVAL")) {
                    logger.info("Trying to remove file - " + file.getAbsolutePath() + "; Result = " + file.delete());
                }
            }
        } catch (Exception e) {
            logger.error("Exception in ConfigOld.removeOldFiles() :" + e);
            e.printStackTrace();
        }
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                new Runnable() {
                    @Override public void run() {
                        removeOldFiles();
                    }
                },
                new Trigger() {
                    @Override public Date nextExecutionTime(TriggerContext triggerContext) {
                        Calendar nextExecutionTime =  new GregorianCalendar();
                        Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                        nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                        nextExecutionTime.add(Calendar.MILLISECOND, Config.getInt("DELETE_INTERVAL"));
                        return nextExecutionTime.getTime();
                    }
                }
        );
    }
}*/
