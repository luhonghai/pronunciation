package com.cmg.android.bbcaccent.activity.info;

import android.app.Activity;
import android.os.Bundle;
import android.util.Base64;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;

import org.apache.commons.io.FileUtils;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 4/10/15.
 */
public abstract class BaseInfoActivity extends SherlockActivity {

    private HtmlTextView textView;

    private static final String INFO_VERSION = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = (HtmlTextView) findViewById(R.id.txtInfo);
        try {
            textView.setHtmlFromString(getHtml(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private String getHtml() throws IOException {
        String path = getAssetPath();
        String cachedName = Base64.encodeToString(path.getBytes(), Base64.DEFAULT);
        File cachedFile = new File(FileHelper.getApplicationDir(this), cachedName + INFO_VERSION + ".html");
        if (!cachedFile.exists()) {
            FileUtils.copyInputStreamToFile(getAssets().open(path), cachedFile);
        }
        if (cachedFile.exists()) {
            String html =  FileUtils.readFileToString(cachedFile, "UTF-8");
            String version = AndroidHelper.getVersionName(this);
            if (html.contains("%APP_VERSION%")) {
                html = html.replace("%APP_VERSION%", "Release " + version);
            }
            return html;
        } else {
            return "";
        }
    }

    protected abstract String getAssetPath();
}
