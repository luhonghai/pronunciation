package com.cmg.lesson.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by CMG Dev156 on 10/15/2015.
 */
public class DateSearchParse {
    /**
     *
     * @param date
     * @param isEndOfDay
     * @return if isEndOfDay=true return end date of day, else return date firstly of day
     */
    public static Date parseDate(String date, boolean isEndOfDay) throws Exception{
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
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseDate(String date) throws Exception {
        return parseDate(date, false);
    }
}
