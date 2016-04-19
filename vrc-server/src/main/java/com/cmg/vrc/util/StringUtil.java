package com.cmg.vrc.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lantb on 2014-04-21.
 */
public class StringUtil {

    private static final Map<String, String> RESOURCE_CACHE = new HashMap<String, String>();

    public static Object isNull(final Object o, final Object dflt) {
        if (o == null) {
            return dflt;
        } else {
            return o;
        }
    }

    public static Boolean switchBoolean (String params, boolean isNull){
        if(params == null){
            return isNull;
        }else{
            if(params.equalsIgnoreCase("false")){
                return false;
            }else{
                return true;
            }
        }
    }

	public static String list2String(ArrayList<String> list){
       if(list.size() > 0 ){
           String temp = new String();
           for(String s : list){
               temp+= s +"|";
               System.out.println("temp : " + temp);
           }
           return temp;
       }else{
           return null;
       }
    }

    public static String md5(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        md.update(input.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }


        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String readResource(String resource) {
        if (RESOURCE_CACHE.containsKey(resource.toLowerCase())) {
            return RESOURCE_CACHE.get(resource.toLowerCase());
        }
        InputStream is = StringUtil.class.getClassLoader().getResourceAsStream(resource);
        try {
            String s = IOUtils.toString(is, "UTF-8");
            if (s != null && s.length() > 0) {
                RESOURCE_CACHE.put(resource.toLowerCase(), s);
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static String convertListToString(List<String> list, String separator){
        if(list!= null && list.size() > 0){
            String converter = "";
            for(String word : list){
                converter = converter + word + separator;
            }
            return converter.substring(0,converter.length()-1);
        }
        return  "";
    }

    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }
}
