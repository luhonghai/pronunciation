package com.cmg.vrc.service;

import com.cmg.lesson.dao.country.CountryDAO;
import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.GcmMessage;
import com.cmg.vrc.data.dao.impl.DatabaseVersionDAO;
import com.cmg.vrc.data.dao.impl.UserDeviceDAO;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.data.jdo.UserDevice;
import com.cmg.vrc.processor.CommandExecutor;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.android.gcm.server.Message;
import com.google.gson.Gson;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cmg on 07/10/2015.
 */
public class DatabaseGeneratorService {

    private static final Class[] LESSON_TABLES = {
            IpaMapArpabet.class,
            Course.class,
            CourseMappingDetail.class,
            CourseMappingLevel.class,
            LessonCollection.class,
            LessonMappingQuestion.class,
            com.cmg.lesson.data.jdo.level.Level.class,
            Objective.class,
            ObjectiveMapping.class,
            Question.class,
            WordCollection.class,
            WordOfQuestion.class,
            Country.class,
            CountryMappingCourse.class,
            Test.class,
            TestMapping.class
    };

    private static final Logger logger = Logger.getLogger(DatabaseGeneratorService.class.getName());

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final DatabaseGeneratorService instance = new DatabaseGeneratorService();

    public static synchronized DatabaseGeneratorService getInstance() {
        return instance;
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private String admin;

    private boolean running;

    private boolean stopping;

    private File targetDir;

    private File currentLogFile;

    private String projectName = "empty";

    private WeakReference<Future<?>> runningFuture;

    private File tmpDir = new File(FileUtils.getTempDirectory(), "database_generator");

    private AWSHelper awsHelper = new AWSHelper();

    final DatabaseVersionDAO dao = new DatabaseVersionDAO();

    private String databaseName = Configuration.getValue("databaseName");
    private String databaseHost = Configuration.getValue("databaseHost");
    private String databasePort = Configuration.getValue("databasePort");
    private String databaseUsername = Configuration.getValue("databaseUsername");
    private String databasePassword = Configuration.getValue("databasePassword");

    int version;

    private DatabaseGeneratorService() {
    }

    public void generate(String admin) {
        if (!isRunning()) {
            synchronized (this) {
                setRunning(true);
                this.admin = admin;
                try {
                    cleanup();
                    if (!tmpDir.exists() || !tmpDir.isDirectory())
                        tmpDir.mkdirs();
                    currentLogFile = new File(tmpDir, "current.log");
                    if (currentLogFile.exists())
                        try {
                            FileUtils.forceDelete(currentLogFile);
                        } catch (IOException e) {
                        }
                    targetDir = new File(tmpDir, UUIDGenerator.generateUUID());
                    if (!targetDir.exists() || !targetDir.isDirectory()) {
                        targetDir.mkdirs();
                    }
                    runningFuture = new WeakReference<Future<?>>(executorService.submit(new GeneratorRunnable()));
                } catch (Exception e) {
                    appendError("Could not start training", e);
                    setRunning(false);
                }
            }
        }
    }


    private void cleanup() {
        if (tmpDir != null && tmpDir.exists()) {
            File[] files = tmpDir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = file.getName();
                    if (!(name.equalsIgnoreCase("current.log")
                            || name.equalsIgnoreCase("extra"))) {
                        try {
                            FileUtils.forceDelete(file);
                        } catch (Exception e) {}
                    }
                }
            }
        }
    }

    public void forceStop() {
        synchronized (this) {
            stopping = true;
            appendMessage("Force stop generating ...");
            if (runningFuture != null) {
                final Future future = runningFuture.get();
                if (future != null) {
                    while (!future.isDone()) {
                        future.cancel(true);
                    }
                }
                try {
                    executorService.shutdownNow();
                    executorService = Executors.newFixedThreadPool(1);
                } catch (Exception e) {}
            }
            cleanup();
//            if (currentLogFile != null && currentLogFile.exists())
//                try {
//                    FileUtils.forceDelete(currentLogFile);
//                } catch (IOException e) {}
            this.admin = null;
            running = false;
            stopping = false;
        }
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isStopping() {
        return stopping;
    }

    private class GeneratorRunnable implements Runnable {
        @Override
        public void run() {
            appendMessage("Preparing ... Please wait.");
            long start = System.currentTimeMillis();
            try {
                version = dao.getMaxVersion() + 1;
                projectName = "database-v" +version +".zip";
                generateDatabase();
            } catch (Exception e) {
                appendError("Could not complete generate database",e);
            } finally {
                appendMessage("Execution time: " + (System.currentTimeMillis() - start) + "ms");
                appendMessage("Clean up ...");
                cleanup();
                appendMessage("Done.");
                if (currentLogFile != null && currentLogFile.exists()) {
                    appendMessage("Save running log " + currentLogFile + " to AWS S3 " + projectName + ".log");
                    try {
                        File targetLog = new File(targetDir, projectName + ".running.log");
                        if (targetLog.exists())
                            FileUtils.forceDelete(targetLog);
                        FileUtils.copyFile(currentLogFile, targetLog);
                        awsHelper.upload(Constant.FOLDER_DATABASE
                                + "/" +projectName + ".log", targetLog);
                        awsHelper.upload(Constant.FOLDER_DATABASE
                                + "/" + "latest.running.log", targetLog);
                    } catch (Exception e) {
                        appendError("Could not save running log", e);
                    }
                }
                setRunning(false);
            }
        }
    }

    private void generateDatabase() throws Exception {
        List<String> tables = new ArrayList<>();
        for (Class<?> clazz : LESSON_TABLES) {
            String tableName = getTableName(clazz).toUpperCase();
            appendMessage("Sync table: " + tableName);
            tables.add(tableName);
        }
        String scriptTemplate = "";
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("database/mysql2sqlite.sh");
            scriptTemplate = IOUtils.toString(is, "UTF-8");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
        File script = new File(targetDir, "generate.sh");
        File lessonDb = new File(targetDir, "lesson.db");
        if (lessonDb.exists()) FileUtils.forceDelete(lessonDb);
        StringBuffer sb = new StringBuffer();
        sb.append("-u ").append(databaseUsername)
                .append(" -p").append(databasePassword)
                .append(" -P ").append(databasePort)
                .append(" -h ").append(databaseHost)
                .append(" ").append(databaseName).append(" ")
                .append(StringUtils.join(tables, " "));
        scriptTemplate = scriptTemplate.replace("%EXTRA_ARGS%", sb.toString());
        appendMessage("Execute generate database script:\n" + scriptTemplate);
        FileUtils.writeStringToFile(script, scriptTemplate, "UTF-8");
        File runScript = new File(targetDir, "run.sh");
        String runScriptContent = "chmod +x " + script.getAbsolutePath() + "\n" + script.getAbsolutePath() + " | sqlite3 " + lessonDb.getAbsoluteFile();
        appendMessage("Execute command: " + runScriptContent);
        FileUtils.writeStringToFile(runScript,
                runScriptContent
                , "UTF-8");
        CommandExecutor.execute(null, new CommandExecutor.CommandListener() {
            @Override
            public void onMessage(String message) {
                appendMessage(message);
            }

            @Override
            public void onError(String message, Throwable e) {
                appendError(message, e);
            }
        }, "sh", runScript.getAbsolutePath());
        if (lessonDb.exists()) {
            appendMessage("Found lesson.db");
            appendMessage("Clear all deleted data");
            Connection conn = null;
            try {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection("jdbc:sqlite:" + lessonDb.getAbsolutePath());
                for (String table : tables) {
                    Statement statement = null;
                    try {
                        appendMessage("Clean table " + table);
                        statement = conn.createStatement();
                        statement.executeUpdate("DELETE FROM [" + table + "] where ISDELETED != '\\0' ");
                        appendMessage("Completed.");
                    } catch (Exception e) {
                        appendError("Could not clean table " + table,e);
                    } finally {
                        try {
                            if (statement != null) statement.close();
                        } catch (Exception e) {}
                    }
                }
            } catch (Exception e) {
                appendError("Could not clean database " + lessonDb.getAbsolutePath(),e);
            } finally {
                try {
                    if (conn != null)
                        conn.close();
                } catch (Exception e) {}
            }
            appendMessage("Start zip database file.");
            File dbZip = new File(targetDir, projectName);
            ZipFile zipFile = new ZipFile(dbZip);
            ZipParameters zp = new ZipParameters();
            zp.setCompressionLevel(9);
            zipFile.createZipFile(lessonDb, zp);
            if (dbZip.exists()) {
                appendMessage("Zip completed. Insert new version to database and upload to AWS S3");
                DatabaseVersion databaseVersion = new DatabaseVersion();
                databaseVersion.setSelected(true);
                databaseVersion.setAdmin(admin);
                Date now = new Date(System.currentTimeMillis());
                databaseVersion.setSelectedDate(now);
                databaseVersion.setCreatedDate(now);
                databaseVersion.setFileName(projectName);
                databaseVersion.setVersion(version);
                awsHelper.upload(Constant.FOLDER_DATABASE + "/" + projectName, dbZip);
                dao.removeSelected();
                dao.createObj(databaseVersion);
                sendGcmMessage();
            } else {
                appendError("No zipped SQLite database found");
            }
        } else {
            appendError("No SQLite database found");
        }
    }

    private void appendMessage(String message) {
        appendLog("INFO - " + message);
    }

    private void appendError(String message) {
        appendError(message, null);
    }

    private void appendError(String message, Throwable e) {
        appendLog("ERROR - " + message);
        if (e != null)
            appendLog(ExceptionUtils.getStackTrace(e));
    }

    private void appendLog(String log) {
        if (currentLogFile != null) {
            try {
                logger.info(log);
                FileUtils.writeStringToFile(currentLogFile,
                        sdf.format(new Date(System.currentTimeMillis())) + " " + log + "\n",
                        "UTF-8", true);
            } catch (Exception e) {}
        }
    }

    public File getCurrentLogFile() {
        return currentLogFile;
    }

    public String getCurrentLog(int lines) {
        try {
            if (currentLogFile != null && currentLogFile.exists()) {
                if (lines <= 0) {
                    return FileUtils.readFileToString(currentLogFile, "UTF-8");
                } else {
                    int counter = 0;
                    ReversedLinesFileReader object = new ReversedLinesFileReader(currentLogFile, 4096, "UTF-8");
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = object.readLine()) != null && counter < lines) {
                        builder.append(line).append("\n");
                        counter++;
                    }
                    return builder.toString();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not fetch current log");
        }
        return "";
    }

    private String getTableName(Class<?> clazz) {
        return PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(clazz.getCanonicalName()).getTable();
    }

    private void sendGcmMessage() {
        try {
            appendMessage("Send notification to all user devices");
            GcmMessage message = new GcmMessage(GcmMessage.TYPE_DATABASE);
            CountryDAO countryDAO = new CountryDAO();
            List<Country> countries = countryDAO.listAll();
            if (countries != null && countries.size() > 0) {
                for (Country country : countries) {
                    if (!country.isDeleted()) {
                        appendMessage("Add country id to message: " + country.getId() + ". Name: " + country.getName());
                        GcmMessage.Language language = new GcmMessage.Language();
                        language.setId(country.getId());
                        language.setMessage("Lesson database is updated. Download now with accenteasy!");
                        message.getLanguages().add(language);
                    }
                }
            }
            UserDeviceDAO userDeviceDAO = new UserDeviceDAO();
            List<UserDevice> userDevices = userDeviceDAO.listAll();
            List<String> gcmIds = new ArrayList<>();
            if (userDevices != null && userDevices.size() > 0) {
                for (UserDevice userDevice : userDevices) {
                    String gcmId = userDevice.getGcmId();
                    if (gcmId != null && gcmId.length() > 0 && !gcmIds.contains(gcmId)) {
                        gcmIds.add(gcmId);
                    }
                }
            }
            appendMessage("Send message to " + gcmIds.size() + " device(s)");
            if (gcmIds.size() > 0) {
                Message mMessage = new Message.Builder().addData("data", new Gson().toJson(message)).build();
                MessageService messageService = new MessageService(mMessage);
                messageService.doPostMessage(gcmIds);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not send gcm message", e);
            appendError("Could not send notification", e);
        }
    }
}
