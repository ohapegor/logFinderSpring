package ru.ohapegor.logFinder.services.file.remover;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.config.Config;


import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.io.File;
import java.nio.file.Files;
import java.util.Date;

@Stateless
public class FileRemoverDeclarativeBean {

    private static final Logger logger = LogManager.getLogger();

    @Schedule(hour = "*",minute = "*/10")
    public void removeOldFiles(){
        logger.info("Entering FileRemoverProgrammaticBean.removeOldFiles()");
        try {
            // properties.load(new FileReader(new File("g:/properties.properties")));
            File directory = new File(Config.getString("GENERATED_FILE_LOCATION"));
            File[] files = null;

            if (Files.exists(directory.toPath())){
                files = directory.listFiles();
            }

            if (files != null && files.length > 0) {
                long currentTime = new Date().getTime();
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
