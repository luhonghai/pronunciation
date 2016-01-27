package com.cmg.android.bbcaccent.data;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.http.HttpContacter;
import com.cmg.android.bbcaccent.http.ResponseData;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.utils.UUIDGenerator;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 4/10/15.
 */
public class DatabasePrepare {

    private Context context;
    private String newLessonChange="";
    private boolean check=false;

    private OnPrepraredListener prepraredListener;

    class VersionResponseData extends ResponseData<String> {
        int version;
        String lessonChange;
    }

    public DatabasePrepare(Context context, OnPrepraredListener prepraredListener) {
        this.context = context;
        this.prepraredListener = prepraredListener;
    }

    public DatabasePrepare(){}

    public interface OnPrepraredListener {
        void onComplete();
        void onError(String message, Throwable e);
    }

    public void prepare() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (loadDatabase()) {
                    loadTips();
                    //  loadHelpContent();
                    prepraredListener.onComplete();
                }
                return null;
            }
        }.execute();
    }

    private void loadHelpContent() {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        }
        for (int i = 1; i <= 11; i++)
            ImageLoader.getInstance().loadImageSync("assets://help-content/help_content_" + i + ".png");
    }

    private void loadTips() {
        TipsContainer tipsContainer = new TipsContainer(context);
        tipsContainer.loadSync();
    }

    public boolean loadDatabase() {
        String dbName = "lesson";
        File databaseDir = new File(FileHelper.getApplicationDir(context), "databases");
        File fileVersion = new File(databaseDir, "version");
        int currentVersion = 0;
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        File[] databaseFiles = databaseDir.listFiles();
        if (databaseFiles != null && databaseFiles.length > 0) {
            for (File file : databaseFiles) {
                SimpleAppLog.debug("Found database file: " + file.getAbsolutePath());
            }
        }
        File lessonDb = new File(databaseDir, dbName);
        SimpleAppLog.info("Current db version: " + currentVersion);

        if (fileVersion.exists()) {
            try {
                currentVersion = Integer.parseInt(FileUtils.readFileToString(fileVersion, "UTF-8"));
                SimpleAppLog.info("Current database version: " + currentVersion);
            } catch (IOException e) {
                SimpleAppLog.error("Could not read current version",e);
            }
        }
        if (!lessonDb.exists()) currentVersion = 0;
        String downloadUrl = "";
        int newVersion = 0;
        try {
            Gson gson = new Gson();
            HttpContacter httpContacter = new HttpContacter(context);
            Map<String, String> data = new HashMap<String, String>();
            data.put("version", Integer.toString(currentVersion));
            VersionResponseData responseData = gson.fromJson(httpContacter.post(data,
                    context.getString(R.string.check_version_url)), VersionResponseData.class);
            SimpleAppLog.logJson("Found version response data", responseData);
            if (responseData != null && responseData.isStatus()) {
                downloadUrl = responseData.getData();
                newVersion = responseData.version;
                newLessonChange=responseData.lessonChange;
                check=true;

            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not check database version",e);
        }
        if (downloadUrl != null && downloadUrl.length() > 0) {
            SimpleAppLog.info("Download new version " + newVersion + ". URL : " + downloadUrl + ". Current version: " + currentVersion);
            File tmpDir = new File(FileUtils.getTempDirectoryPath(), UUIDGenerator.generateUUID());
            if (!tmpDir.exists()) tmpDir.mkdirs();
            try {
                File tmpDb = new File(tmpDir, "tmp.zip");
                if (tmpDb.exists()) FileUtils.forceDelete(tmpDb);
                FileUtils.copyURLToFile(new URL(downloadUrl), tmpDb);
                String dbFilePath = null;
                if (tmpDb.exists()) {
                    ZipFile zipFile = new ZipFile(tmpDb);
                    zipFile.extractAll(tmpDir.getAbsolutePath());
                    dbFilePath = getDbPathFromFolder(tmpDir);
                } else {
                    SimpleAppLog.error("No tmp db found " + tmpDb);
                }
                if (dbFilePath != null) {
                    SimpleAppLog.info("Found db file path: " + dbFilePath);
                    MainApplication.getContext().closeAllDatabase();
                    if (lessonDb.exists()) {
                        if (context.deleteDatabase(dbName)) {
                            SimpleAppLog.debug("Delete database " + dbName + " completed");
                        } else {
                            SimpleAppLog.debug("Could not delete old database " + dbName);
                        }


                    }
                    if (lessonDb.exists()) {
                        FileUtils.forceDelete(lessonDb);
                    }
                    FileUtils.moveFile(new File(dbFilePath), lessonDb);
                    SimpleAppLog.debug("Update database successfully");
                    FileUtils.writeStringToFile(fileVersion, Integer.toString(newVersion), "UTF-8");
                    SimpleAppLog.info("Save new version to :" + fileVersion + " successfully");
                } else {
                    SimpleAppLog.error("No db file path found in folder " + tmpDb);
                }
            } catch (Exception e) {
                SimpleAppLog.error("Could not save database from server",e);
            } finally {
                if (tmpDir.exists()) {
                    try {
                        FileUtils.forceDelete(tmpDir);
                    } catch (Exception e) {}
                }
            }
        } else {
            SimpleAppLog.debug("Skip update database no download url found");
        }

        MainApplication.getContext().initDatabase();
        if (!lessonDb.exists()) {
            if (prepraredListener != null) {
                prepraredListener.onError(context.getString(R.string.could_not_connect_server_message),null);
            }
            return false;
        } else {
            return true;
        }
    }

    private String getDbPathFromFolder(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    String dbPath = getDbPathFromFolder(file);
                    if (dbPath != null) return dbPath;
                } else {
                    if (file.getName().toLowerCase().endsWith(".db")) return file.getAbsolutePath();
                }
            }
        }
        return null;
    }
    public String lessonChange(){
        if(check) {
            return newLessonChange;
        }else return "";
    }

}
