package com.cmg.vrc.sphinx;

import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.dsp.WavCleaner;
import com.cmg.vrc.http.FileCommon;
import com.cmg.vrc.http.FileUploader;
import com.cmg.vrc.http.exception.UploaderException;
import com.cmg.vrc.properties.Configuration;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
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
    private static SummaryReport instance;

    public static boolean analyze() {
        if (instance == null) {
            String recordDir = Configuration.getValue(Configuration.VOICE_RECORD_DIR);
            instance = new SummaryReport(new File(recordDir, "summary.xlsx"), new File(recordDir));
        }
        synchronized (instance) {
            try {
                instance.execute();
                return true;
            } catch (Exception ex) {
                logger.log(Level.SEVERE,"Could not execute summary report" ,ex);
            } finally {
            }
            return false;
        }

    }


    private static final Logger logger = Logger.getLogger(SummaryReport.class.getName());
    private final File reportFile;
    private final File targetDir;

    private long start;
    private Gson gson;
    private String[] recipients;
    private int totalWord = 0;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private SimpleDateFormat sdf;

    private SummaryReport(File reportFile, File targetDir) {
        this.reportFile = reportFile;
        this.targetDir = targetDir;
    }

    private void execute() {
        logger.info("Looking for target dir " + targetDir);
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            logger.log(Level.SEVERE, "Could not found target dir " + targetDir);
        }
        gson = new Gson();
        sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        start = System.currentTimeMillis();
        logger.info("Start analyzing voice data. Target dir " + targetDir);
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
        header.createCell(14).setCellValue("Raw WAV - Phonemes");
        header.createCell(15).setCellValue("Raw WAV - Hypothesis");
        header.createCell(16).setCellValue("Clean WAV name");
        header.createCell(17).setCellValue("Clean WAV - Phonemes");
        header.createCell(18).setCellValue("Clean WAV - Hypothesis");
        walk(targetDir);
        save();
        sendMail();
        logger.info("complete");
    }

    private void save() {
        FileOutputStream fos = null;
        try {
            logger.info("Save report to file " + reportFile);
            fos = new FileOutputStream(reportFile);
            workbook.write(fos);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save report file", ex);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {

            }
        }
    }

    private void walk(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                walk(f);
            } else {
                analyze(dir, f);
            }
        }
    }

    private void analyze(File dir, File file) {
        String fileName = file.getName();
        if (fileName.toLowerCase().endsWith(".json")) {
            logger.info("Detect json data " + file);
            String rawName = fileName.substring(0, fileName.toLowerCase().lastIndexOf(".json"));
            String cleanWav = rawName + "_clean.wav";
            String rawWav = rawName + "_raw.wav";
            logger.info("Clean WAV file");
            try {
                logger.info("read model");
                String modelData = FileUtils.readFileToString(file);
                UserVoiceModel model = gson.fromJson(modelData, UserVoiceModel.class);
                File targetClean = new File(dir, cleanWav);
                File targetRaw = new File(dir, rawWav);
                WavCleaner cleaner = new WavCleaner(targetClean, targetRaw);
                logger.info("clean wav to " + targetClean);
                cleaner.clean();
                PhonemesDetector.Result cleanResult = analyzeVoice(targetClean,modelData);
                PhonemesDetector.Result rawResult = analyzeVoice(targetRaw, modelData);

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
                if (rawResult != null) {
                    row.createCell(14).setCellValue(rawResult.getPhonemes());
                    row.createCell(15).setCellValue(rawResult.getHypothesis());
                }
                row.createCell(16).setCellValue("Clean WAV name");
                if (cleanResult != null) {
                    row.createCell(17).setCellValue(cleanResult.getPhonemes());
                    row.createCell(18).setCellValue(cleanResult.getHypothesis());
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Could not analyze file " + file, ex);
            }
        }
    }

    private PhonemesDetector.Result analyzeVoice(File file, String modelData) {
        Map<String, String> data = new HashMap<String,String>();
        data.put(FileCommon.PARA_FILE_NAME, file.getName());
        data.put(FileCommon.PARA_FILE_PATH, file.getAbsolutePath());
        data.put("key", Configuration.getValue(Configuration.API_KEY));
        data.put("model", modelData);
        try {
            String resData = FileUploader.upload(data, Configuration.getValue(Configuration.VOICE_ANALYZE_SERVER));
            logger.info("Analyze " + file + " .Result: " + resData);
            return gson.fromJson(resData, PhonemesDetector.Result.class);
        } catch (UploaderException e) {
            logger.log(Level.SEVERE, "Could not upload file to voice analyzing server",e);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Could not upload file to voice analyzing server", e);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not parsing data");
        }
        return null;
    }


    private void sendMail() {
        if (Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER).length() == 0
                || Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD).length() == 0
                || Configuration.getValue(Configuration.RECIPIENTS).length() == 0)
            return;
        recipients = Configuration.getValue(Configuration.RECIPIENTS).split(",");
        logger.info("Send report via email");
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
        logger.info("Message: " + message.toString());
        // attachments
        try {
            sendEmailWithAttachments(host, port,
                    subject, message.toString(), reportFile.getAbsolutePath());
            logger.info("Completed. Execution time: " + executionTime);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not send email.", ex);
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

    public static void main(String[] args) {
        String fileName = "awry_4bb999f5-5143-40c2-8469-60db659cae18.json";
        String rawName = fileName.substring(0, fileName.toLowerCase().lastIndexOf(".json"));
        System.out.println(rawName);
    }
}
