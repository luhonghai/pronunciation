/*
 * Copyright (c) 2013. CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package com.cmg.android.bbcaccentamt.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * DOCME
 *
 * @author $Author$
 * @version $Revision$
 * @Creator LongNguyen
 * @Last changed: $LastChangedDate$
 */
public class ContentUtils {

    public static final String KEY_SCREENSHOOT = "ADD_SCREEN_SHOOT";

    /**
     * Default constructor
     */
    public ContentUtils() {

    }


    public static String generatePreviewHtmlFeedback(Map<String, String> infos) {
        StringBuffer html = new StringBuffer();
        Iterator<String> keys = infos.keySet().iterator();
        while (keys.hasNext()) {
            String k = keys.next();
            if (!k.equals(KEY_SCREENSHOOT)) {
                html.append("<h4 style=\"color:#4acd00\">" + k + "</h4>");
                html.append("<p><label>" + infos.get(k) + "</label></p>");
                html.append("<hr>");
            }
        }

        if (infos.containsKey(KEY_SCREENSHOOT)) {
            html.append("<h4 style=\"color:#4acd00\">Screenshoot</h4>");
            html.append("<p><img style=\"width:80%;border:1px solid #4acd00;\" src='" + infos.get(KEY_SCREENSHOOT) + "' /></p>");
            html.append("<hr>");
        }

        return html.toString();
    }

    public static String generatePreviewHtmlFeedback(String pathLastScreenShot, String description, String account, boolean addDataSystem, String appVersion) {
        StringBuffer html = new StringBuffer();
        html.append("<h3 style=\"color:#4acd00\">Account</h3>");
        html.append("<p><label>" + account + "</label></p>");
        html.append("<hr>");
        if (addDataSystem) {
            html.append("<h3 style=\"color:#4acd00\">System data</h3>");
            html.append("<p><label>Model : " + android.os.Build.MODEL + "</label></p>");
            html.append("<p><label>OS Version : " + System.getProperty("os.version") + "</label></p>");
            html.append("<p><label>OS API Level : " + android.os.Build.VERSION.SDK + "</label></p>");
            html.append("<p><label>Device : " + android.os.Build.DEVICE + "</label></p>");
            html.append("<p><label>NewsLetters version : " + appVersion + "</label></p>");
            html.append("<hr>");
        }
        if (description != "temp") {
            html.append("<h3 style=\"color:#4acd00\">Description</h3>");
            html.append("<p><label>" + description + "</label></p>");
            html.append("<hr>");
        }

        if (pathLastScreenShot != null && pathLastScreenShot.length() > 0) {
            html.append("<h3 style=\"color:#4acd00\">Screenshoot</h3>");
            html.append("<p><img style=\"width:80%;border:1px solid #4acd00;\" src='" + pathLastScreenShot + "' /></p>");
            html.append("<hr>");
        }

        return html.toString();
    }

    public static String getFileName(String url) {
        if (url != null && url.length() > 0) {
            url = url.replaceAll("\\\\", "/");
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        }
        return "";
    }
}
