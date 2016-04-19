package com.cmg.merchant.util;

import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lantb on 2016-04-14.
 */
public class DateUtil {

    /**
     *
     * @param date
     * @param isEndOfDay
     * @return if isEndOfDay=true return end date of day, else return date firstly of day
     */
    public Date parseDate(String date, boolean isEndOfDay) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try{
            Date d = df.parse(date);
            if (isEndOfDay) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(d);
                cal.set(Calendar.HOUR, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                return cal.getTime();
            } else {
                return d;
            }
        }catch(Exception e){
        }
        return null;
    }

    /**
     *
     * @param date
     * @return
     */
    public String convertDate(String date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date tmp = sdf.parse(date);
            String temp = df.format(tmp);
            return temp;
        }catch (Exception e){
        }
        return "";
    }
}
