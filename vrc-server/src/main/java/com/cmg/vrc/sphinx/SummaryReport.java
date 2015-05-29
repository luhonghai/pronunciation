package com.cmg.vrc.sphinx;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.processor.AudioCleaner;
import com.cmg.vrc.http.FileCommon;
import com.cmg.vrc.http.FileUploader;
import com.cmg.vrc.http.exception.UploaderException;
import com.cmg.vrc.processor.SoXCleaner;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/17/14.
 */
public class SummaryReport {

    public static interface MessageListener {
        public void onMessage(String message);

        public void onError(String error);
    }

    private static Object lock = new Object();

    public static boolean analyze() {
        return analyze(null);
    }

    public static boolean analyze(final MessageListener listener) {
        synchronized (lock) {
            SummaryReport instance;
            File recordDir = new File(FileUtils.getTempDirectoryPath(), UUIDGenerator.generateUUID());
                    //Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            if (!recordDir.exists() || !recordDir.isDirectory()) {
                recordDir.mkdirs();
            }
            instance = new SummaryReport(new File(recordDir, "summary.xlsx"), new File(FileHelper.getTmpSphinx4DataDir(), Constant.FOLDER_RECORDED_VOICES));
            try {
                if (listener != null)
                    instance.setMessageListener(listener);
                instance.execute();
                return true;
            } catch (Exception ex) {

                if (listener != null)
                    listener.onError("Could not execute summary report. Message: " + ExceptionUtils.getFullStackTrace(ex));
                logger.log(Level.SEVERE, "Could not execute summary report", ex);
            } finally {
            }
            return false;
        }
    }

    private static final Logger logger = Logger.getLogger(SummaryReport.class.getName());
    private final File reportFile;
    private final File targetDir;

    private long start;
    private String[] recipients;
    private MessageListener messageListener;
    private Gson gson;

    private int totalWord = 0;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private SimpleDateFormat sdf;



    private SummaryReport(File reportFile, File targetDir) {
        this.reportFile = reportFile;
        this.targetDir = targetDir;
    }

    private void log(String message) {
        log(Level.INFO, message, null);
    }

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, String message, Throwable ex) {
        if (messageListener != null) {
            if (ex != null || level != Level.INFO) {
                if (ex != null) {
                    String error = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex);
                    //String error = ex.getMessage();
                    error = error.replace("\\n", "<br/>");
                    messageListener.onError(message + ". Message: " + error);
                } else {
                    messageListener.onError(message );
                }

            } else {
                messageListener.onMessage(message);
            }
        }
        if (ex != null) {
            logger.log(level, message, ex);
        } else {
            logger.log(level, message);
        }
    }

    private void execute() {
        log("Looking for target dir " + targetDir);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            log(Level.SEVERE, "Could not found target dir " + targetDir);
            return;
        }
        totalWord = 0;
        gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = System.currentTimeMillis();
        log("Start analyzing voice data. Target dir " + targetDir);
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("summary");
        final Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Username");
        header.createCell(2).setCellValue("Native English");
        header.createCell(3).setCellValue("Gender");
        header.createCell(4).setCellValue("DoB");
        header.createCell(5).setCellValue("Country");
        header.createCell(6).setCellValue("English Proficiency (1-10)");
        header.createCell(7).setCellValue("Time (ms)");
        header.createCell(8).setCellValue("Time");
        header.createCell(9).setCellValue("Duration");
        //https://www.google.com/maps?q=24.197611,120.780512&z=15
        header.createCell(10).setCellValue("Location");
        header.createCell(11).setCellValue("Device UUID");
        header.createCell(12).setCellValue("Word");
        header.createCell(13).setCellValue("Raw WAV name");
        header.createCell(14).setCellValue("Raw WAV - Score");
        header.createCell(15).setCellValue("Raw WAV - JSON");
//        header.createCell(16).setCellValue("Clean WAV name");
//        header.createCell(17).setCellValue("Clean WAV - Score");
//        header.createCell(18).setCellValue("Clean WAV - JSON");
        walk(targetDir);
        save();
        sendMail();
        log("complete");
    }

    private void save() {
        FileOutputStream fos = null;
        try {
            log("Save report to file " + reportFile);
            fos = new FileOutputStream(reportFile);
            workbook.write(fos);
        } catch (IOException ex) {
            log(Level.SEVERE, "Could not save report file", ex);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {

            }
        }
    }

    private void walk(File dir) {
        if (dir != null && dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        walk(f);
                    } else {
                        analyze(dir, f);
                    }
                }
            }
        }
    }

    private void analyze(File dir, File file) {
        if (file == null || dir == null) return;
        try {
            if (!(dir.getName().equalsIgnoreCase("dominic")
                    || dir.getName().equalsIgnoreCase("elaine")
                    || dir.getName().equalsIgnoreCase("hai_lu")
                    || dir.getName().equalsIgnoreCase("lan_ta")
                    || dir.getName().equalsIgnoreCase("my_vu")
                    || dir.getName().equalsIgnoreCase("xuan_bui")
                    || dir.getName().equalsIgnoreCase("anh_nguyen")
            )) {
                return;
            }
        } catch (Exception e) {

        }
        try {
            String fileName = file.getName();
            if (fileName.toLowerCase().endsWith(".json")) {
                log("Detect json data " + file);
                String rawName = fileName.substring(0, fileName.toLowerCase().lastIndexOf(".json"));
                String cleanWav = rawName + "_clean.wav";
                String rawWav = rawName + "_raw.wav";
                //log("Clean WAV file");


                String modelData = FileUtils.readFileToString(file);
                log("Read model " + modelData);
                UserVoiceModel model = gson.fromJson(modelData, UserVoiceModel.class);
                if (model == null || model.getWord() == null || model.getWord().length() == 0)
                    return;
             //   File targetClean = new File(dir, cleanWav);
                File targetRaw = new File(dir, rawWav);
//                AudioCleaner cleaner = new SoXCleaner(targetClean, targetRaw);
//                log("Clean wav to " + targetClean);
//                cleaner.clean();
                //SphinxResult resultClean = null;
                SphinxResult resultRaw = null;

//                try {
//                    log("Start analyze clean WAV " + targetClean);
//                    PhonemesDetector detector = new PhonemesDetector(targetClean, model.getWord());
//                    resultClean = detector.analyze();
//                    if (resultClean != null) {
//                        log("Score: " + resultClean.getScore());
//                    } else {
//                        log("No score found");
//                    }
//                } catch (Exception e) {
//                    log(Level.SEVERE, "Could not analyze clean WAV", e);
//                }
                try {
                    log("Start analyze raw WAV " + targetRaw);
                    PhonemesDetector detector = new PhonemesDetector(targetRaw, model.getWord());
                    resultRaw = detector.analyze();
                    if (resultRaw != null) {
                        log("Score: " + resultRaw.getScore());
                    } else {
                        log("No score found");
                    }
                } catch (Exception e) {
                    log(Level.SEVERE, "Could not analyze raw WAV", e);
                }
                final Row row = sheet.createRow(++totalWord);
                row.createCell(0).setCellValue(model.getId());
                row.createCell(1).setCellValue(model.getUsername());
                row.createCell(2).setCellValue(model.isNativeEnglish() ? "Yes" : "No");
                row.createCell(3).setCellValue(model.isGender() ? "Male" : "Female");
                row.createCell(4).setCellValue(model.getDob());
                row.createCell(5).setCellValue(model.getCountry());
                row.createCell(6).setCellValue(model.getEnglishProficiency());
                row.createCell(7).setCellValue(model.getServerTime());
                row.createCell(8).setCellValue(sdf.format(new Date(model.getServerTime())));
                row.createCell(9).setCellValue(model.getDuration());
                //https://www.google.com/maps?q=24.197611,120.780512&z=15
                row.createCell(10).setCellValue("https://www.google.com/maps?q=" + model.getLatitude() + "," + model.getLongitude() + "&z=15");
                row.createCell(11).setCellValue(model.getUuid());
                row.createCell(12).setCellValue(model.getWord());
                row.createCell(13).setCellValue(rawWav);
                if (resultRaw != null) {
                    row.createCell(14).setCellValue(resultRaw.getScore());
                    row.createCell(15).setCellValue(gson.toJson(resultRaw));
                }
//                row.createCell(16).setCellValue("Clean WAV name");
//                if (resultClean != null) {
//                    row.createCell(17).setCellValue(resultClean.getScore());
//                    row.createCell(18).setCellValue(gson.toJson(resultClean));
//                }
                log("================");
            }
        } catch (Exception ex) {
            log(Level.SEVERE, "Could not analyze file " + file, ex);
        }
    }

//    private PhonemesDetector.Result analyzeVoice(File file, String modelData) {
//        Map<String, String> data = new HashMap<String,String>();
//        data.put(FileCommon.PARA_FILE_NAME, file.getName());
//        data.put(FileCommon.PARA_FILE_PATH, file.getAbsolutePath());
//        data.put("key", Configuration.getValue(Configuration.API_KEY));
//        data.put("model", modelData);
//        try {
//            String resData = FileUploader.upload(data, Configuration.getValue(Configuration.VOICE_ANALYZE_SERVER));
//            log("Analyze " + file + " .Result: " + resData);
//            return gson.fromJson(resData, PhonemesDetector.Result.class);
//        } catch (UploaderException e) {
//            log(Level.SEVERE, "Could not upload file to voice analyzing server", e);
//        } catch (FileNotFoundException e) {
//            log(Level.SEVERE, "Could not upload file to voice analyzing server", e);
//        } catch (Exception ex) {
//            log(Level.SEVERE, "Could not parsing data");
//        }
//        return null;
//    }


    private void sendMail() {
        if (Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER).length() == 0
                || Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD).length() == 0
                || Configuration.getValue(Configuration.RECIPIENTS).length() == 0)
            return;
        log("Send report via email");
        recipients = Configuration.getValue(Configuration.RECIPIENTS).split(",");

        // SMTP info
        String host = "smtp.gmail.com";
        String port = "465";
        long executionTime = System.currentTimeMillis() - start;
        String subject = "Voice analyzing results";
        StringBuffer message = new StringBuffer("<p>Dear Sir,<p>\n");
        message.append("<p>The process is completed with result: </p>\n");
        message.append("Total word found: " + totalWord + " <br/>\n");
        message.append("Execution time: " + executionTime + "ms.\n");
        message.append("<p>Please check the attachment for more detail.</p>\n");
        message.append("Thank you & Best regards,<br/>\n");
        message.append("CMG Automator");
        log("Message: " + message.toString());
        // attachments
        try {
            sendEmailWithAttachments(host, port,
                    subject, message.toString(), reportFile.getAbsolutePath());
            log("Completed. Execution time: " + executionTime);
        } catch (Exception ex) {
            log(Level.SEVERE, "Could not send email.", ex);
        }
    }

    private void sendEmailWithAttachments(String host, String port, String subject, String message, String attachFiles)
            throws javax.mail.MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.user", Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER));
        properties.put("mail.password", Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD));

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER),
                        Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD));
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER)));
        for (String recipient : recipients) {
            recipient = recipient.trim();
            if (recipient.length() > 0) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
                        recipient));
            }
        }
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        MimeBodyPart attachPart = new MimeBodyPart();
        try {
            attachPart.attachFile(attachFiles);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        multipart.addBodyPart(attachPart);
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg, msg.getAllRecipients());
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
