package com.cmg.vrc.dictionary;

import com.cmg.vrc.dictionary.walker.DictionaryItem;
import com.cmg.vrc.dictionary.walker.DictionaryListener;
import com.cmg.vrc.dictionary.walker.DictionaryWalker;
import com.cmg.vrc.dictionary.walker.OxfordDictionaryWalker;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.CustomSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.linguist.acoustic.Unit;
import edu.cmu.sphinx.linguist.dictionary.Pronunciation;
import edu.cmu.sphinx.result.WordResult;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 9/19/14.
 */
public class DictionaryFetcher {
    private static final Logger logger = Logger.getLogger(DictionaryFetcher.class.getName());

    private final Runner.Configuration configuration;
    private List<String> words;

    private XSSFWorkbook workbook;

    private int count;

    private int exCount;

    private long start;

    private int totalCorrect = 0;

    private int totalIncorrect = 0;

    private String[] recipients;

    private Configuration conf;

    private CustomSpeechRecognizer recognizer;

    public DictionaryFetcher(Runner.Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean validate() {
        start = System.currentTimeMillis();
        File audioDir = new File(configuration.getAudioDir());
        if (!audioDir.exists() || !audioDir.isDirectory()) {
            audioDir.mkdirs();
        }
        File wordList = new File(configuration.getWordList());
        if (wordList.exists() && !wordList.isDirectory()) {
            try {
                words = FileUtils.readLines(wordList);
                if (words.size() == 0) {
                    logger.log(Level.SEVERE, "No word found");
                } else {
                    logger.info("Found " + words.size() + " words");
                    logger.info("Init workbook ...");
                    workbook = new XSSFWorkbook();

                    logger.info("Init stream speech recognizer...");
                    conf = new Configuration();
                    conf.setAcousticModelPath(configuration.getAcousticModelPath());
                    conf.setDictionaryPath(configuration.getDictionaryPath());
                    conf.setLanguageModelPath(configuration.getLanguageModelPath());
                    recognizer =
                            new CustomSpeechRecognizer(conf);
                    recognizer.allocate();
                    return true;
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not read word list", e);
            }
        } else {
            logger.log(Level.SEVERE, "Could not found word list file " + wordList);
        }
        return false;
    }

    public void execute() {
        count = 0;
        exCount = 0;
        logger.info("Init walker ...");
        DictionaryWalker walker = new OxfordDictionaryWalker(new File(configuration.getAudioDir()));
        logger.info("Init sheet " + configuration.getSheetName() + ". Exception sheet: " + configuration.getSheetException());
        final XSSFSheet sheet = workbook.createSheet(configuration.getSheetName());

        final XSSFSheet exSheet = workbook.createSheet(configuration.getSheetException());


        logger.info("Init all header");
        Row row = sheet.createRow(count++);
        row.createCell(0).setCellValue("Word");
        row.createCell(1).setCellValue("Pronunciation");
        row.createCell(2).setCellValue("Line breaks");
        row.createCell(3).setCellValue("Audio url");
        row.createCell(4).setCellValue("Audio file");
        row.createCell(5).setCellValue("Voice recognition status");
        row.createCell(6).setCellValue("Hypothesis");
        row.createCell(7).setCellValue("Phonemes");
        row.createCell(8).setCellValue("Raw Words");
        row.createCell(9).setCellValue("Best 3 hypothesis");
        row.createCell(10).setCellValue("Lattice nodes size");

        Row exRow = exSheet.createRow(exCount++);
        exRow.createCell(0).setCellValue("Word");
        exRow.createCell(1).setCellValue("Exception");
        exRow.createCell(2).setCellValue("Message");

        final File audioDir = new File(configuration.getAudioDir());

        walker.setListener(new DictionaryListener() {
            @Override
            public void onDetectWord(DictionaryItem item) {
                Row tmp = sheet.createRow(count++);
                tmp.createCell(0).setCellValue(item.getWord());
                tmp.createCell(1).setCellValue(item.getPronunciation());
                tmp.createCell(2).setCellValue(item.getLineBreaks());
                tmp.createCell(3).setCellValue(item.getAudioUrl());
                tmp.createCell(4).setCellValue(item.getAudioFile());

                InputStream stream = null;
                try {
                    if (recognizer == null)
                        return;
                    if (item.getAudioFile() != null && item.getAudioFile().length() > 0) {
                        File wavFile = new File(audioDir, item.getAudioFile());
                        if (wavFile.exists() && !wavFile.isDirectory()) {
                            logger.info("Start analyze WAV record: " + wavFile);
                            try {
                                stream = new FileInputStream(wavFile);
                                recognizer.startRecognition(stream);
                                SpeechResult result;
                                // Only get the first result
                                if ((result = recognizer.getResult()) != null) {
                                    String hypothesis = result.getHypothesis().trim();
                                    logger.info("Hypothesis: " + hypothesis);
                                    if (hypothesis.equalsIgnoreCase(item.getWord())) {
                                        logger.info("Compare with " + item.getWord() + ": Correct");
                                        tmp.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue("Correct");
                                        totalCorrect++;
                                    } else {
                                        logger.info("Compare with " + item.getWord() + ": Incorrect");
                                        tmp.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue("Incorrect");
                                        totalIncorrect++;
                                    }
                                    tmp.createCell(6).setCellValue(hypothesis);
                                    String wordReults = "";
                                    String phonemes = "";
                                    boolean flag = false;
                                    for (WordResult r : result.getWords()) {
                                        wordReults += (r + "\n");
                                        // Only get phonemes of first word
                                        Pronunciation pron = r.getPronunciation();
                                        Unit[] units = pron.getUnits();
                                        for (Unit u : units) {
                                            phonemes += (u.getName() + " ");
                                        }
                                    }

                                    phonemes = phonemes.trim();
                                    logger.info("Phonemes: " + phonemes);

                                    tmp.createCell(7).setCellValue(phonemes);

                                    tmp.createCell(8).setCellValue(wordReults);

                                    logger.info("Words: " + wordReults);

                                    String bestHypothesis = "";
                                    for (String s : result.getNbest(3))
                                        bestHypothesis += (s + "\n");

                                    logger.info("Best 3 hypothesis: " + bestHypothesis);
                                    tmp.createCell(9).setCellValue(bestHypothesis);
                                    int size = result.getLattice().getNodes().size();
                                    logger.info("Lattice nodes size: " + size);
                                    tmp.createCell(10).setCellValue(size);

                                } else {
                                    tmp.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue("No result found");
                                }

                            } finally {
                                try {
                                    stream.close();
                                } catch (IOException iox) {
                                    iox.printStackTrace();
                                }
                            }
                        } else {
                            tmp.createCell(5).setCellValue("No WAV found");
                            logger.log(Level.SEVERE, "Could not found WAV file");
                        }

                    } else {
                        tmp.createCell(5).setCellValue("No WAV found");
                        logger.log(Level.SEVERE, "Could not found WAV file");
                    }
                } catch (FileNotFoundException e) {
                    tmp.createCell(5).setCellValue("No WAV found");
                    logger.log(Level.SEVERE, "Could not found WAV file");
                } finally {
                    try {
                        if (stream != null)
                            stream.close();
                    } catch (IOException ex) {

                    }
                }
            }

            @Override
            public void onWordNotFound(DictionaryItem item, FileNotFoundException ex) {
                Row tmp = exSheet.createRow(exCount++);
                tmp.createCell(0).setCellValue(item.getWord());
                tmp.createCell(1).setCellValue("Word not found");
                tmp.createCell(2).setCellValue(ex.getMessage());
            }

            @Override
            public void onError(DictionaryItem item, Exception ex) {
                Row tmp = exSheet.createRow(exCount++);
                tmp.createCell(0).setCellValue(item.getWord());
                tmp.createCell(1).setCellValue("Error");
                tmp.createCell(2).setCellValue(ex.getMessage());
            }
        });
        try {
            recognizer.stopRecognition();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        walker.execute(words);
        save();
        sendMail();

    }

    private void save() {
        FileOutputStream fos = null;
        try {
            File report = new File(configuration.getReportXlsx());
            logger.info("Save report to file " + report);
            fos = new FileOutputStream(report);
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

    private void sendMail() {
        if (configuration.getEmailReceivers().length() == 0
                || configuration.getEmailUsername().length() == 0
                || configuration.getEmailPassword().length() == 0)
            return;
        recipients = configuration.getEmailReceivers().split(",");
        logger.info("Send report via email");
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "465";
        long executionTime = System.currentTimeMillis() - start;
        String subject = "Dictionary generator results";
        StringBuffer message = new StringBuffer("<p>Dear Sir,<p>\n");
        message.append("<p>The process is completed with result: </p>\n");
        message.append("Total word: " + words.size() + " <br/>\n");
        message.append("Correct: " + totalCorrect + " <br/>\n");
        message.append("Incorrect: " + totalIncorrect + " <br/>\n");
        message.append("Exception: " + (exCount - 1) + " <br/>\n");
        message.append("Execution time: " + executionTime + "ms.\n");
        message.append("<p>Please check the attachment for more detail.</p>\n");
        message.append("Thank you & Best regards,<br/>\n");
        message.append("CMG Automator");
        logger.info("Message: " + message.toString());
        // attachments
        try {
            sendEmailWithAttachments(host, port,
                    subject, message.toString(), new File(configuration.getReportXlsx()).getAbsolutePath());
            logger.info("Completed. Execution time: " + executionTime);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not send email.", ex);
        }
    }

    public void sendEmailWithAttachments(String host, String port, String subject, String message, String attachFiles)
            throws javax.mail.MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.user", configuration.getEmailUsername());
        properties.put("mail.password", configuration.getEmailPassword());

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        configuration.getEmailUsername(),
                        configuration.getEmailPassword());
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(configuration.getEmailUsername()));
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

}
