package com.cmg.android.bbcaccent.fragment.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

import org.apache.commons.io.FileUtils;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 4/10/15.
 */
public abstract class BaseInfoFragment extends BaseFragment {

    private static final String INFO_VERSION = "2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.info_activity, null);
        try {
            ((HtmlTextView) root.findViewById(R.id.txtInfo)).setHtmlFromString(getHtml(), true);
        } catch (IOException e) {
            SimpleAppLog.error("Could not load info html",e);
        }
        return root;
    }

    private String getHtml() throws IOException {
        String path = getAssetPath();
        String cachedName = Base64.encodeToString(path.getBytes(), Base64.DEFAULT);
        File cachedFile = new File(FileHelper.getApplicationDir(MainApplication.getContext()), cachedName + INFO_VERSION + ".html");
        if (!cachedFile.exists()) {
            FileUtils.copyInputStreamToFile(MainApplication.getContext().getAssets().open(path), cachedFile);
        }
        if (cachedFile.exists()) {
            String html =  FileUtils.readFileToString(cachedFile, "UTF-8");
            String version = AndroidHelper.getVersionName(MainApplication.getContext());
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
