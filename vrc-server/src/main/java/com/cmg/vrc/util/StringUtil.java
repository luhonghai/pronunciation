package com.cmg.vrc.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
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
        try {
            String s = IOUtils.toString(StringUtil.class.getClassLoader().getResourceAsStream(resource), "UTF-8");
            if (s != null && s.length() > 0) {
                RESOURCE_CACHE.put(resource.toLowerCase(), s);
            }
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
