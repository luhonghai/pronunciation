package com.cmg.vrc.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by cmg on 23/11/2015.
 */
public class HttpContacter {

    private UrlEncodedFormEntity getEntity(Map<String, String> data) throws UnsupportedEncodingException {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (data != null && data.size() > 0) {
            Iterator<String> keys = data.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                nvps.add(new BasicNameValuePair(key, data.get(key)));
            }
        }
        return  new UrlEncodedFormEntity(nvps, "UTF-8");
    }

    public String post(Map<String, String> data, String url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        httppost.setEntity(getEntity(data));
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity, "UTF-8");
    }

    public String get(String url) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        // Execute HTTP Post Request
        HttpResponse response = httpclient.execute(httpGet);
        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity, "UTF-8");
    }
}
