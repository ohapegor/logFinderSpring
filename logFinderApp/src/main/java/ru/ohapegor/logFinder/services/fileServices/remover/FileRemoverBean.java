package ru.ohapegor.logFinder.services.fileServices.remover;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ohapegor.logFinder.config.Config;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

@Service
public class FileRemoverBean {

    private static final Logger logger = LogManager.getLogger();

    //@Scheduled(fixedDelayString = "${DELETE_INTERVAL}")
    @Scheduled(cron = "*/30 * * * * *")//every 30 seconds
    public void removeOldFiles(){
        try {
            logger.info("Invoking FileRemoverBean.removeOldFiles()");

            // properties.load(new FileReader(new File("g:/properties.properties")));
            long currentTime = new Date().getTime();
            File directory = new File(Config.getString("GENERATED_FILE_LOCATION"));
            File[] files = null;
            if (Files.exists(directory.toPath())){
                files= directory.listFiles();
            }
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if ((currentTime - file.lastModified()) > Config.getLong("FILE_LIFE_INTERVAL")) {
                        logger.info("Trying to remove file - " + file.getAbsolutePath() + " success = " + file.delete());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception in FileRemoverBean.removeOldFiles() :" + e);
            e.printStackTrace();
        }
    }

}
