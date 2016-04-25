package com.cmg.merchant.services.generateSqlite;

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
import com.cmg.merchant.common.Sqlite;
import com.cmg.merchant.dao.teacher.TCHDAO;
import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.processor.CommandExecutor;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.cmg.vrc.util.UUIDGenerator;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by lantb on 2016-04-20.
 */
public class SqliteService extends Thread{

    @Override
    public void run(){
        try {
           clearData();
        }catch (Exception e){

        }
    }


    private static final Logger logger = Logger.getLogger(SqliteService.class.getName());

    private static final Class[] TABLES = {
            Course.class,
            CourseMappingLevel.class,
            com.cmg.lesson.data.jdo.level.Level.class,
            CourseMappingDetail.class,
            Objective.class,
            ObjectiveMapping.class,
            Test.class,
            TestMapping.class,
            LessonCollection.class,
            LessonMappingQuestion.class,
            Question.class,
            WordOfQuestion.class
    };




    private String databaseName = Configuration.getValue("databaseName");
    private String databaseHost = Configuration.getValue("databaseHost");
    private String databasePort = Configuration.getValue("databasePort");
    private String databaseUsername = Configuration.getValue("databaseUsername");
    private String databasePassword = Configuration.getValue("databasePassword");
    private String idCourse;
    private File targetDir;
    private AWSHelper awsHelper;

    /**
     *
     * @param id
     */
    public SqliteService(String id){
        this.idCourse = id;
        awsHelper = new AWSHelper();
        this.targetDir = new File(tmpDir, UUIDGenerator.generateUUID());
        if (!targetDir.exists() || !targetDir.isDirectory()) {
            targetDir.mkdirs();
        }

    }
    /**
     *
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz) {
        return PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(clazz.getCanonicalName()).getTable();
    }

    /**
     *
     * @param log
     */
    private void appendMessage(String log){
        System.out.println(log);
        logger.info(log);
    }

    /**
     *
     * @param log
     */
    private void appendError(String log){
        System.out.println(log);
        logger.info("Error - " + log);
    }

    private File tmpDir = new File(FileUtils.getTempDirectory(), "database_generator");

    private void clearDataTable(Connection conn,String sql){
        Statement statement = null;
        try {
            appendMessage("execute query " + sql);
            statement = conn.createStatement();
            statement.executeUpdate(sql);
            appendMessage("Completed.");
        } catch (Exception e) {
            appendError("Could not execute query " + sql + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {}
        }
    }

    /**
     *
     * @throws Exception
     */
    public void clearData() throws Exception {
        TCHDAO dao = new TCHDAO();
        int version = dao.getLatestVersion(idCourse)+1;
        String projectName = this.idCourse + "-v" + version + ".zip";
        List<String> tables = new ArrayList<>();
        for (Class<?> clazz : TABLES) {
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
                appendError(message+ e);
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
                        appendError("Could not clean table " + table + e.getMessage());
                    } finally {
                        try {
                            if (statement != null) statement.close();
                        } catch (Exception e) {}
                    }
                }
                //for clear data
                Statement statement = null;
                try {
                    Sqlite lite = new Sqlite(idCourse);
                    appendMessage("Clean unnecessary data in table ");
                    /*statement = conn.createStatement();*/
                    clearDataTable(conn, lite.deleteCourse());
                    clearDataTable(conn,lite.deleteCourseMapLevel());
                    clearDataTable(conn,lite.deleteLevel());
                    clearDataTable(conn,lite.deleteCourseMapDetail());
                    clearDataTable(conn,lite.deleteObj());
                    clearDataTable(conn,lite.deleteTest());
                    clearDataTable(conn,lite.deleteObjMapping());
                    clearDataTable(conn,lite.deleteTestMapping());
                    clearDataTable(conn,lite.deleteLesson());
                    clearDataTable(conn,lite.deleteLessonMap());
                    clearDataTable(conn,lite.deleteQuestion());
                    clearDataTable(conn,lite.deleteWordOfQuestion());
                    appendMessage("Completed.");
                } catch (Exception e) {
                    appendError("Could not clean unnecessary data in table " + e.getMessage());
                } finally {
                    try {
                        if (statement != null) statement.close();
                    } catch (Exception e) {}
                }
            } catch (Exception e) {
                appendError("Could not clean database " + lessonDb.getAbsolutePath() + e.getMessage());
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
                awsHelper.upload(Constant.FOLDER_DATABASE + "/" + projectName, dbZip);
                TeacherCourseHistory tch = new TeacherCourseHistory();
                tch.setIdCourse(idCourse);
                tch.setVersion(version);
                tch.setPathAws(projectName);
                dao.put(tch);
                appendMessage("Zip completed. local file in : " + dbZip.getAbsolutePath());
            } else {
                appendError("No zipped SQLite database found");
            }
        } else {
            appendError("No SQLite database found");
        }
    }


}
