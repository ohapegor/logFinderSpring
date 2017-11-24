package ru.ohapegor.logFinder.services.file.generator;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.config.Config;
import ru.ohapegor.logFinder.entities.*;
import ru.ohapegor.logFinder.services.file.reader.FileReaderService;

import javax.ejb.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.TransformerFactoryImpl;
import ru.ohapegor.logFinder.services.logSearch.SearchLogService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Stateful
//@RequestScoped
public class FileGeneratorBean implements FileGeneratorService {

    private AtomicInteger count = new AtomicInteger();

    private static final Logger logger = LogManager.getLogger();

    @EJB
    private SearchLogService searchLogService;

    @EJB
    private FileReaderService fileReader;

    private String uniqueFilePath;

    @Override
    public String getUniqueFilePath() {
        return uniqueFilePath;
    }

    @Override
    public AtomicInteger getCount() {
        return count;
    }

    @Override
    public String fileSearch(SearchInfo searchInfo) {
        logger.info("Entering FileGeneratorBean.fileSearch()");
        uniqueFilePath = null;
        try {
            File generatedLogsDirectory = new File(Config.getString("GENERATED_FILE_LOCATION"));
            File[] storedFiles = generatedLogsDirectory.listFiles();
            //check files exist
            if (storedFiles == null || storedFiles.length < 1) {
                logger.info("No files stored, exiting FileGeneratorBean.fileSearch()");
                return null;
            }
            //read required file extension first
            Arrays.sort(storedFiles, (f1, f2) -> f2.getName().endsWith(searchInfo.getFileExtension()) ? 1 : -1);
            //read all files
            for (File file : storedFiles) {
                SearchInfoResult fileInfo = fileReader.readInfoFromFile(file);
                //skip invalid file
                if (fileInfo != null && isValidInfo(fileInfo.getSearchInfo(), searchInfo)) {
                    logger.info("File successfully found - " + file.getAbsolutePath());
                    if (fileInfo.getSearchInfo().getFileExtension().equalsIgnoreCase(searchInfo.getFileExtension())) {
                        logger.info("Extension is match, returning filepath :" + file.getAbsolutePath());
                        uniqueFilePath = file.getAbsolutePath();
                    } else {
                        logger.info("Extension isn't match, generating new filepath");
                        fileInfo.setSearchInfo(searchInfo);
                        generateUniqueFilePath(searchInfo);
                        writeLogsToFile(fileInfo, getXsltPath(searchInfo.getFileExtension()));
                    }
                    logger.info("Exiting FileGeneratorBean.fileSearch(), filePath : " + file);
                    return uniqueFilePath;
                }
            }
        } catch (Exception e) {
            logger.error("Exception in FileGeneratorBean.fileSearch() :" + getStackTrace(e));
        }
        logger.info("File was not found, exiting FileGeneratorBean.fileSearch()");
        return null;
    }

    private boolean isValidInfo(SearchInfo fileSearchInfo, SearchInfo searchInfo) {
        return fileSearchInfo != null &&
                fileSearchInfo.getRegexp().equals(searchInfo.getRegexp()) &&
                fileSearchInfo.getLocation().equals(searchInfo.getLocation()) &&
                isValidIntervals(searchInfo.getDateIntervals(), fileSearchInfo.getDateIntervals());
    }

    private boolean isValidIntervals(List<SignificantDateInterval> searchIntervals, List<SignificantDateInterval> storedIntervals) {
        logger.info("Entering FileGeneratorBean.isValidIntervals()");
       /* if (storedIntervals == null || searchIntervals == null) return false;

        for (SignificantDateInterval storedInterval : storedIntervals) {
            boolean matches = false;
            for (SignificantDateInterval searchInterval : searchIntervals) {
                if (isIntervalValid(storedInterval, searchInterval)) {
                    matches = true;
                }
            }
            if (!matches) {
                logger.info("stored interval = " + storedInterval + " is not matches, exiting FileGeneratorBean.isValidIntervals()");
                return false;
            }
        }
        for (SignificantDateInterval searchInterval : searchIntervals) {
            boolean matches = false;
            for (SignificantDateInterval storedInterval : storedIntervals) {
                if (isIntervalValid(storedInterval, searchInterval)) {
                    matches = true;
                }
            }
            if (!matches) {
                logger.info("stored interval = " + searchInterval + " is not matches, exiting FileGeneratorBean.isValidIntervals()");
                return false;
            }
        }
        return true;*/

        //refactoring
        Objects.requireNonNull(storedIntervals, "received null storedIntervals in FileGeneratorBean.isValidIntervals()");
        Objects.requireNonNull(searchIntervals, "received null searchIntervals in FileGeneratorBean.isValidIntervals()");
        return storedIntervals.stream().allMatch(stored -> searchIntervals.stream().anyMatch(search -> isIntervalValid(stored, search)))
                && searchIntervals.stream().allMatch(search -> storedIntervals.stream().anyMatch(stored -> isIntervalValid(stored, search)));

    }


    //check if search interval is in stored interval and difference is less than 10%
    private boolean isIntervalValid(SignificantDateInterval storedInterval, SignificantDateInterval searchInterval) {
        logger.info("comparing search interval = " + searchInterval + " and stored interval = " + storedInterval);
        if (storedInterval.getDateFrom().after(searchInterval.getDateFrom())) {
            logger.info("storedInterval.getDateFrom()=" + storedInterval.getDateFrom().getTime() +
                    " is after searchInterval.getDateFrom()=" + searchInterval.getDateFrom().getTime());
            return false;
        }
        if (storedInterval.getDateTo().before(searchInterval.getDateTo())) {
            logger.info("storedInterval.getDateTo()=" + storedInterval.getDateTo().getTime() +
                    " is after searchInterval.getDateTo()=" + searchInterval.getDateTo().getTime());
            return false;
        }
        long storedDuration = storedInterval.getDateTo().getTime().getTime() - storedInterval.getDateFrom().getTime().getTime();
        long searchDuration = searchInterval.getDateTo().getTime().getTime() - searchInterval.getDateFrom().getTime().getTime();
        double relation = (1.0 * storedDuration) / searchDuration;

        if (relation > 1.1) {
            logger.info("search durations difference is grater than 10%");
            return false;
        }
        logger.info("search interval = " + searchInterval + " is matches stored interval = " + storedInterval);
        return true;

    }

    @Override
    public void generateUniqueFilePath(SearchInfo searchInfo) {
        logger.info("Entering FileGeneratorBean.generateUniqueFilePath(SearchInfo searchInfo) " + searchInfo);
        try {
            String uniqueFileName = String.valueOf(UUID.randomUUID());
            String fileDirectory = Config.getString("GENERATED_FILE_LOCATION");
            uniqueFilePath = fileDirectory + uniqueFileName + "." + searchInfo.getFileExtension().toLowerCase();
        } catch (Exception e) {
            logger.error("Exception in FileGeneratorBean.logSearchAsync(SearchInfo searchInfo) :" + getStackTrace(e));
        }

        logger.info("Exiting FileGeneratorBean.generateUniqueFilePath(SearchInfo searchInfo) uniqueFilePath " + uniqueFilePath);
    }

    //@Override
    public void fileGenerate(SearchInfo searchInfo) {
        logger.info("Entering FileGeneratorBean.generateFile(SearchInfo searchInfo, List<ResultLogs> resultLogsList) " + searchInfo);
        SearchInfoResult searchInfoResult = searchLogService.logSearch(searchInfo);
        writeLogsToFile(searchInfoResult, getXsltPath(searchInfo.getFileExtension()));
        logger.info("Exiting FileGeneratorBean.generateFile(SearchInfo searchInfo, List<ResultLogs> resultLogsList) " + searchInfo);
    }

    private String getXsltPath(String fileExtension) {
        String xsltStylesheetsLocation = Config.getString("XSLT_LOCATION");
        return xsltStylesheetsLocation + "resultTo" + fileExtension.toUpperCase() + ".xsl";
    }

    private void writeLogsToFile(SearchInfoResult searchInfoResult, String xsltPath) {
        logger.info("Entering FileGeneratorBean.writeLogsToFile(SearchInfoResult searchInfoResult; uniqueFilePath = " + uniqueFilePath);
        try {
            Path filesDirectory = Paths.get(uniqueFilePath).getParent();
            if (Files.notExists(filesDirectory)) {
                Files.createDirectories(filesDirectory);
            }

            //pdf
            if (searchInfoResult.getSearchInfo().getFileExtension().equalsIgnoreCase(FileExtension.PDF.toString())) {
                //writeLogsToPdfFile(searchInfoResult);
                writeLogsToPdfFileWithFO(searchInfoResult, xsltPath);
                return;
            }

            TransformerFactory tf = TransformerFactoryImpl.newInstance();
            StreamSource xsltStylesheet = new StreamSource(new File(xsltPath));
            Transformer transformer = tf.newTransformer(xsltStylesheet);

            // Source
            JAXBContext jc = JAXBContext.newInstance(SearchInfoResult.class);
            JAXBSource source = new JAXBSource(jc, searchInfoResult);

            // Result
            StreamResult result = new StreamResult(new File(uniqueFilePath));

            // Transform
            transformer.transform(source, result);

            if (searchInfoResult.getSearchInfo().getFileExtension().equalsIgnoreCase(FileExtension.DOCX.toString())) {
                createDocx();
            }
        } catch (Exception e) {
            logger.info("Exception in FileGeneratorBean.writeLogsToFile(); e : " + getStackTrace(e));
        }
        logger.info("Exiting FileGeneratorBean.writeLogsToFile()");
    }


    private void createDocx() throws Exception {
        logger.info("Entering FileGeneratorBean.createDocx()");
        Path zipTemplate = Paths.get(Config.getString("DOCX_TEMLATE_LOCATION"));
        Path zipFile = Paths.get("g:/searchLogResults/myDocx.zip");
        if (Files.exists(zipFile)) Files.delete(zipFile);
        Files.copy(zipTemplate, zipFile);
        ZipFileManager zipFileManager = new ZipFileManager(zipFile);
        Path docXML = Paths.get("G:/searchLogResults/document.xml");
        Files.copy(Paths.get(uniqueFilePath), docXML, StandardCopyOption.REPLACE_EXISTING);
        zipFileManager.addFile(docXML);
        Files.delete(docXML);
        Files.move(zipFile, Paths.get(uniqueFilePath), StandardCopyOption.REPLACE_EXISTING);
        logger.info("Exiting FileGeneratorBean.createDocx()");
    }


    //pdf with framework
    private void writeLogsToPdfFile(SearchInfoResult searchInfoResult) {
        logger.info("Entering FileGeneratorBean.writeLogsToPdfFile()");
        //File logsFile = new File(uniqueFilePath);
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter pdfWriter = null;
        int logNumber = 1;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(uniqueFilePath);
            BaseFont bf = BaseFont.createFont("C:\\windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //подключаем файл шрифта, который поддерживает кириллицу
            Font font = new Font(bf, 8);
            pdfWriter = PdfWriter.getInstance(document, fos);
            pdfWriter.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfPTable table = new PdfPTable(1);
                    table.setTotalWidth(80);
                    table.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    PdfPCell cell = new PdfPCell(new Phrase(String.format("Page %d", writer.getPageNumber()), font));
                    cell.setBorder(Rectangle.NO_BORDER);
                    table.addCell(cell);
                    PdfContentByte canvas = writer.getDirectContent();
                    canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                    table.writeSelectedRows(0, -1, 300, 30, canvas);
                    canvas.endMarkedContentSequence();
                }
            });
            document.open();
            Font titleFont = new Font(bf, 12);

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(40);
            table.setHorizontalAlignment(Element.ALIGN_RIGHT);
            // the cell object
            PdfPCell cell;
            // we add a cell with colspan 3
            cell = new PdfPCell(new Phrase("Search info", font));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            //regexp
            table.addCell(new PdfPCell(new Phrase("Regular Expression", font)));
            table.addCell(new PdfPCell(new Phrase(searchInfoResult.getSearchInfo().getRegexp(), font)));

            //Location
            table.addCell(new PdfPCell(new Phrase("Location", font)));
            table.addCell(new PdfPCell(new Phrase(searchInfoResult.getSearchInfo().getLocation(), font)));

            //dateIntervals
            for (SignificantDateInterval interval : searchInfoResult.getSearchInfo().getDateIntervals()) {
                table.addCell(new PdfPCell(new Phrase(interval.getDateFrom().getTime().toString(), font)));
                table.addCell(new PdfPCell(new Phrase(interval.getDateTo().getTime().toString(), font)));
            }
            document.add(table);

            Paragraph paragraph = new Paragraph("Приложение LogFinder \n Создатель Охапкин Егор, ООО «Siblion»", titleFont);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(paragraph);

            if (searchInfoResult.getResultLogs() == null || searchInfoResult.getResultLogs().isEmpty()) {
                paragraph = new Paragraph("\n" + searchInfoResult.getEmptyResultMessage(), font);
                paragraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(paragraph);
            } else {
                for (ResultLogs nextLog : searchInfoResult.getResultLogs()) {
                    paragraph = new Paragraph("\nLog №" + logNumber++, font);
                    paragraph.add("\nTime moment : " + nextLog.getTimeMoment().getTime());
                    paragraph.add("\nFile name : " + nextLog.getFileName());
                    paragraph.add("\nMessage : " + nextLog.getContent());
                    document.add(paragraph);
                }
            }
        } catch (IOException | DocumentException e) {
            logger.error("Exception in FileGeneratorBean.writeLogsToPdfFile() e=" + e);
            e.printStackTrace();
        } finally {
            try {
                document.close();
            } catch (Exception ignored) {
            }
            try {
                pdfWriter.flush();
            } catch (Exception ignored) {
            }
            try {
                pdfWriter.close();
            } catch (Exception ignored) {
            }
            try {
                fos.close();
            } catch (Exception ignored) {
            }
        }
        logger.info("Exiting FileGeneratorBean.writeLogsToPdfFile()");

    }

    private void writeLogsToPdfFileWithFO(SearchInfoResult searchInfoResult, String xsltPath) {
        logger.info("Entering FileGenerationBean.writeLogsToPdfFileWithFO()");
        try {
            // FopFactory fopFactory = FopFactory.newInstance(new File(Thread.currentThread().getContextClassLoader().getResource("fopConfig.xml").getFile()));
            FopFactory fopFactory = FopFactory.newInstance(new File("G:\\Egor\\Work\\Projects\\LogReader\\logFinder\\logFinderApp\\src\\main\\resources\\fopConfig.xml"));
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Templates templates = tFactory.newTemplates(
                    new StreamSource(new File(xsltPath)));

            try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(uniqueFilePath))) {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
                Transformer transformer = templates.newTransformer();
                // Source
                JAXBContext jc = JAXBContext.newInstance(SearchInfoResult.class);
                JAXBSource source = new JAXBSource(jc, searchInfoResult);

                transformer.transform(source, new SAXResult(fop.getDefaultHandler()));
            }
        } catch (Exception e) {
            logger.error("Exception in  FileGenerationBean.writeLogsToPdfFileWithFO(); e : " + getStackTrace(e));
        }
        logger.info("Exiting FileGenerationBean.writeLogsToPdfFileWithFO()");
    }

}
