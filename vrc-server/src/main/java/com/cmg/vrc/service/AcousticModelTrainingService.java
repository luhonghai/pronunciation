package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.AcousticModelVersionDAO;
import com.cmg.vrc.data.jdo.AcousticModelVersion;
import com.cmg.vrc.sphinx.training.AcousticModelTraining;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cmg on 07/10/2015.
 */
public class AcousticModelTrainingService {

    private static final Logger logger = Logger.getLogger(AcousticModelTrainingService.class.getName());

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final AcousticModelTrainingService instance = new AcousticModelTrainingService();

    public static synchronized AcousticModelTrainingService getInstance() {
        return instance;
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private String admin;

    private Map<String, String> configuration;

    private boolean running;

    private boolean useExtraData;

    private File targetDir;

    private File extraDir;

    private File currentLogFile;

    private AcousticModelTraining training;

    private WeakReference<Future<?>> runningFuture;

    private File tmpDir = new File(FileUtils.getTempDirectory(), "acoustic_model_training");

    private AWSHelper awsHelper = new AWSHelper();

    private AcousticModelTrainingService() {
    }

    public void train(String admin, boolean useExtraData, Map<String, String> configuration) {
        if (!isRunning()) {
            synchronized (this) {
                setRunning(true);
                this.admin = admin;
                this.useExtraData = useExtraData;
                this.configuration = configuration;
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
                    extraDir = new File(tmpDir, "extra" + File.separator + "ext-training");
                    targetDir = new File(tmpDir, UUIDGenerator.generateUUID());
                    runningFuture = new WeakReference<Future<?>>(executorService.submit(new TrainingRunnable()));
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
            appendMessage("Force stop training ...");
            if (runningFuture != null) {
                final Future future = runningFuture.get();
                if (future != null)
                    future.cancel(true);
            }
            cleanup();
            if (currentLogFile != null && currentLogFile.exists())
                try {
                    FileUtils.forceDelete(currentLogFile);
                } catch (IOException e) {}
            this.useExtraData = false;
            this.admin = null;
            running = false;
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

    public boolean isUseExtraData() {
        return useExtraData;
    }

    private class TrainingRunnable implements Runnable {
        @Override
        public void run() {
            String projectName = "empty";
            try {
                final AcousticModelVersionDAO dao = new AcousticModelVersionDAO();
                final int version = dao.getMaxVersion() + 1;
                 projectName = "acoustic_model_v" + version;
                training = new AcousticModelTraining(targetDir, projectName, configuration, new AcousticModelTraining.TrainingListener() {
                    @Override
                    public void onMessage(String message) {
                        appendMessage(message);
                    }

                    @Override
                    public void onError(String message, Throwable e) {
                        appendError(message, e);
                    }

                    @Override
                    public void onSuccess(AcousticModelVersion amv) {
                        amv.setAdmin(admin);

                        amv.setVersion(version);
                        try {
                            appendMessage("Save acoustic model version " + version + " to database");
                            //dao.removeSelected();
                            amv.setSelected(dao.getCount() == 0);
                            dao.put(amv);
                        } catch (Exception e) {
                            appendError("Could not save to database",e);
                        }
                    }
                });
                if (isUseExtraData()) {
                    appendMessage("Enable extra data. Check extra data directory at " + extraDir);
                    try {
                        File status = new File(extraDir, ".completed");
                        if (!status.exists()) {
                            appendMessage("No extra data found. Try to download from S3");
                            File rootExtra = new File(tmpDir, "extra");
                            if (rootExtra.exists())
                                FileUtils.forceDelete(rootExtra);
                            rootExtra.mkdirs();
                            awsHelper.downloadAndUnzip("training/ext-training.zip", rootExtra);
                            if (extraDir.exists()) {
                                appendMessage("Download completed");
                                FileUtils.write(status, "Completed at " + sdf.format(new Date(System.currentTimeMillis())));
                                training.setExtraDir(extraDir);
                            } else {
                                appendError("Could not not extra data directory. Skip include extra data");
                            }
                        } else {
                            appendMessage("Valid extra data. Include to training script");
                            training.setExtraDir(extraDir);
                        }
                    } catch (Exception e) {
                        appendError("Could not download extra data", e);
                    }
                }
                training.train();

            } catch (Exception e) {
                appendError("Could not complete training acoustic model",e);
            } finally {
                appendMessage("Completed. Clean up ...");
                cleanup();
                appendMessage("Done.");
                if (currentLogFile != null && currentLogFile.exists()) {
                    appendMessage("Save running log " + currentLogFile + " to AWS S3 " + training.getS3KeyRunningLog());
                    try {
                        File targetLog = new File(targetDir, projectName + ".running.log");
                        if (targetLog.exists())
                            FileUtils.forceDelete(targetLog);
                        FileUtils.copyFile(currentLogFile, targetLog);
                        awsHelper.upload(training.getS3KeyRunningLog(), targetLog);
                    } catch (Exception e) {
                        appendError("Could not save running log", e);
                    }
                }
                setRunning(false);
            }
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

}
