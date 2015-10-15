package com.cmg.android.bbcaccent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

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
import java.util.Random;

/**
 * Created by cmg on 2/11/15.
 */
public class AndroidHelper {

    private static final boolean DEBUG = false;

    private static String ADS_ID = "";

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


    public static String getVersionCode(Context context) {
        String version;
        try {
            version = Integer.toString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            version = "Unknown";
        }
        return version;
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

    // Do not call this function from the main thread. Otherwise,
    // an IllegalStateException will be thrown.
    public static String getAdsId(Context mContext) {
        if (ADS_ID != null && ADS_ID.length() > 0) {
            return ADS_ID;
        }

        if (ADS_ID == null || ADS_ID.length() == 0) {
            AdvertisingIdClient.Info adInfo;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
                ADS_ID = adInfo == null ? "" : adInfo.getId();
            } catch (Exception e) {
                // Google Play services is not available entirely.
                SimpleAppLog.error("Could not fetch Ads ID", e);
            }
        }
        return ADS_ID;
    }


    public static Location filterLocation(Location location) {
//        if (DEBUG) {
//            return getFakeLocation();
//        }
        return location;
    }


    /**
     * @return the last know best location
     */
    public static Location getLastBestLocation(Context context) {
//        if (DEBUG) {
//            return getFakeLocation();
//        }
        LocationManager mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location locationGPS = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) return locationGPS;
            }
        } catch (SecurityException e) {
            SimpleAppLog.error("Could not get last known GPS location", e);
        } catch (Exception ex) {
            SimpleAppLog.error("Could not get last known GPS location", ex);
        }
        try {
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location locationNet = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNet != null) return locationNet;
            }
        } catch (SecurityException e) {
            SimpleAppLog.error("Could not get last known Network location", e);
        } catch (Exception ex) {
            SimpleAppLog.error("Could not get last known Network location", ex);
        }
//        Location locationPassive = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//        if (locationPassive != null) return  locationPassive;
        return null;
//
//        Location location;
//        long GPSLocationTime = 0;
//        if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }
//        long NetLocationTime = 0;
//        if (null != locationNet) {
//            NetLocationTime = locationNet.getTime();
//        }
//        if ( 0 < GPSLocationTime - NetLocationTime ) {
//            location =  locationGPS != null ? locationGPS : locationNet;
//        }
//        else {
//            location = locationNet != null ? locationNet : locationGPS;
//        }
//        if (location == null)
//            return locationPassive;
//        return location;
    }

    public static Location getRandomLocation(double longitude, double latitude, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);
        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(latitude);
        double foundLongitude = new_x + longitude;
        double foundLatitude = y + latitude;
        Location location = new Location("random");
        location.setLatitude(foundLatitude);
        location.setLongitude(foundLongitude);
        return location;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static int getScreenSize(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        return screenSize;
    }


    public static boolean isCorrectWidth(TextView textView, String text)
    {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        paint.setTypeface(textView.getTypeface());
        float textSize = textView.getTextSize();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width() <= textView.getWidth();
    }

    public static void updateMarqueeTextView(final TextView textView, boolean enable) {
        textView.setSingleLine(enable);
        textView.setHorizontallyScrolling(enable);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.setClickable(true);
        ((AlwaysMarqueeTextView) textView).setAlwaysMarquee(enable);
    }
}
