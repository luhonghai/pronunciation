package com.cmg.android.bbcaccent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by cmg on 2/11/15.
 */
public class AndroidHelper {

    public static int getScreenWidth(Context context) {
        Point size = new Point();
        WindowManager w = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            return size.x;
        }else{
            Display d = w.getDefaultDisplay();
            return d.getWidth();
        }
    }

    public static int getScreenHeight(Context context) {
        Point size = new Point();
        WindowManager w = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)    {
            w.getDefaultDisplay().getSize(size);
            return size.y;
        }else{
            Display d = w.getDefaultDisplay();
            return d.getHeight();
        }
    }

    public static String getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.cmg.android.bbcaccent", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return "";
    }


    private static File getLatestScreenshoot(Context context) {
        String folder = FileHelper.getFolderPath(FileHelper.SCREENSHOOTS_FOLDER, context);
        File folderScreenshoots = new File(folder);
        if (folderScreenshoots.exists() && folderScreenshoots.isDirectory()) {
            File[] files = folderScreenshoots.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File o1, File o2) {

                        if (o1.lastModified() > o2.lastModified()) {
                            return -1;
                        } else if (o1.lastModified() < o2.lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }

                });
            }
            return files[0];
        }
        return null;
    }

    public static String getLatestScreenShootPath(Context context) {
        File file = getLatestScreenshoot(context);
        if (file != null)
            return file.getAbsolutePath();
        return "";

    }

    public static void takeScreenShot(Activity activity) {
        try {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap b1 = view.getDrawingCache();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            int width = -1;
            int height = -1;
            Display display = activity.getWindowManager().getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                Point size = new Point();
                display.getSize(size);
                width = size.x;
                height = size.y;
            } else {
                width = display.getWidth();
                height = display.getHeight();
            }
            Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                    - statusBarHeight);
            view.destroyDrawingCache();
            b1.recycle();
            savePic(b, activity.getApplicationContext());
        } catch (Exception ignored) {

        }
    }

    private static void savePic(Bitmap b, Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String folder = FileHelper.getFolderPath(FileHelper.SCREENSHOOTS_FOLDER, context);
        File folderScreenshoots = new File(folder);
        if (!folderScreenshoots.exists() && !folderScreenshoots.isDirectory()) {
            folderScreenshoots.mkdirs();
        }

        File[] files = folderScreenshoots.listFiles();
        if (files != null && files.length > 3) {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File o1, File o2) {

                    if (o1.lastModified() > o2.lastModified()) {
                        return -1;
                    } else if (o1.lastModified() < o2.lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });

            files[files.length - 1].delete();
        }

        String fileName = FileHelper.getFilePath(FileHelper.SCREENSHOOTS_FOLDER, sdf.format(new Date(System.currentTimeMillis())) + ".png", context);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ex) {

                }
            }
            if (b != null) {
                b.recycle();
            }
        }

    }

    public static String getLatestScreenShootURL(Context context) {
        File file = getLatestScreenshoot(context);
        if (file != null)
            try {
                return file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                return "";
            }
        return "";
    }

    public static String getVersionName(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "Unknown";
        }
        return version;
    }
}
