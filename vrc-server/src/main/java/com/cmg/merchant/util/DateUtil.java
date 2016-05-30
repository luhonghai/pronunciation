package com.cmg.merchant.util;

import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    /**
     *
     * @param date
     * @return
     */
    public String convertDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String temp = df.format(date);
            return temp;
        }catch (Exception e){
        }
        return "";
    }
    /**
     *
     * @param date
     * @return
     */
    public Date convertStringToDate(String date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date tmp = sdf.parse(date);
            return tmp;
        }catch (Exception e){
        }
        return null;
    }

    /**
     *
     * @param startDate
     * @return
     */
    public ArrayList<Date> initHourInDay(String startDate){
        Calendar calToday = new GregorianCalendar();
        calToday.setTime(new Date());
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(convertStringToDate(startDate));
        calSelected.set(Calendar.HOUR_OF_DAY, 0);
        ArrayList<Date> tmp = new ArrayList<Date>();
        tmp.add(calSelected.getTime());
        for(int i = 1 ; i < 25 ; i++){
            Calendar calNext = new GregorianCalendar();
            Calendar calTemp = new GregorianCalendar();
            calTemp.setTime(convertStringToDate(startDate));
            calTemp.add(Calendar.HOUR_OF_DAY ,i);
            Date dateTmp = calTemp.getTime();
            calNext.setTime(dateTmp);
            if(calNext.before(calToday)){
                tmp.add(calNext.getTime());
            }else{
                tmp.add(calToday.getTime());
                break;
            }
        }
        System.out.println("total calendar " + tmp.size());
        return tmp;
    }


    /**
     *
     * @param startDate
     * @return
     */
    public ArrayList<Date> initDayInWeek(String startDate){
        Calendar calToday = new GregorianCalendar();
        calToday.setTime(new Date());
        calToday.set(Calendar.HOUR_OF_DAY, 12);
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(convertStringToDate(startDate));
        calSelected.set(Calendar.HOUR_OF_DAY, 0);
        ArrayList<Date> tmp = new ArrayList<Date>();
        tmp.add(calSelected.getTime());
        for(int i = 1 ; i < 7 ; i++){
            Calendar calNext = new GregorianCalendar();
            Calendar calTemp = new GregorianCalendar();
            calTemp.setTime(convertStringToDate(startDate));
            calTemp.add(Calendar.DAY_OF_MONTH, i);
            Date dateTmp = calTemp.getTime();
            calNext.setTime(dateTmp);
            calNext.set(Calendar.HOUR_OF_DAY, 0);
            if(calNext.before(calToday)){
                tmp.add(calNext.getTime());
            }else{
                tmp.add(calToday.getTime());
                break;
            }
        }
        System.out.println("total calendar " + tmp.size());
        return tmp;
    }


    /**
     *
     * @param startDate
     * @return
     */
    public ArrayList<Date> initDayInMonth(String startDate){
        Calendar calToday = new GregorianCalendar();
        calToday.setTime(new Date());
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(convertStringToDate(startDate));
        Calendar nextMonth = new GregorianCalendar();
        nextMonth.setTime(getDayInNextMonth(startDate));
        ArrayList<Date> tmp = new ArrayList<Date>();
        tmp.add(calSelected.getTime());
        if(nextMonth.before(calToday)){
            boolean stop = false;
            int i = 1;
            while(!stop){
                Calendar calNext = new GregorianCalendar();
                Calendar calTemp = new GregorianCalendar();
                calTemp.setTime(convertStringToDate(startDate));
                calTemp.add(Calendar.DAY_OF_MONTH ,i);
                Date dateTmp = calTemp.getTime();
                calNext.setTime(dateTmp);
                calNext.set(Calendar.HOUR_OF_DAY, 0);
                if(calNext.before(nextMonth)){
                    tmp.add(calNext.getTime());
                }else{
                    tmp.add(nextMonth.getTime());
                    stop = true;
                }
                i++;
            }
        }else{
            boolean stop = false;
            int i = 1;
            while(!stop){
                Calendar calNext = new GregorianCalendar();
                Calendar calTemp = new GregorianCalendar();
                calTemp.setTime(convertStringToDate(startDate));
                calTemp.add(Calendar.DAY_OF_MONTH ,i);
                Date dateTmp = calTemp.getTime();
                calNext.setTime(dateTmp);
                calNext.set(Calendar.HOUR_OF_DAY, 0);
                if(calNext.before(calToday)){
                    tmp.add(calNext.getTime());
                }else{
                    tmp.add(calToday.getTime());
                    stop = true;
                }
                i++;
            }
        }
        System.out.println("total calendar " + tmp.size());
        return tmp;
    }

    /**
     *
     * @param startDate
     * @return
     */
    public Date getDayInNextMonth(String startDate){
        DateUtil util = new DateUtil();
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(util.convertStringToDate(startDate));
        calSelected.add(Calendar.MONTH, 1);
        calSelected.add(Calendar.DAY_OF_MONTH, -1);
        return calSelected.getTime();
    }

    /**
     *
     * @param startDate
     * @return
     */
    public Date getDayInNextMonth(String startDate, int i){
        DateUtil util = new DateUtil();
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(util.convertStringToDate(startDate));
        calSelected.add(Calendar.MONTH, i);
        return calSelected.getTime();
    }

    public Date getDayInNextYear(String startDate){
        DateUtil util = new DateUtil();
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(util.convertStringToDate(startDate));
        calSelected.add(Calendar.YEAR, 1);
        return calSelected.getTime();
    }
    /**
     *
     * @param startDate
     * @return
     */
    public ArrayList<Date> initDayInNextYear(String startDate){
        Calendar calToday = new GregorianCalendar();
        calToday.setTime(new Date());
        calToday.set(Calendar.HOUR_OF_DAY, 0);
        Calendar calSelected = new GregorianCalendar();
        calSelected.setTime(convertStringToDate(startDate));
        calSelected.set(Calendar.HOUR_OF_DAY, 0);
        ArrayList<Date> tmp = new ArrayList<Date>();
        tmp.add(calSelected.getTime());
        for(int i = 1; i < 13; i++){
            Calendar calTemp = new GregorianCalendar();
            calTemp.setTime(getDayInNextMonth(startDate,i));
            calTemp.set(Calendar.HOUR_OF_DAY, 0);
            if(calTemp.before(calToday)){
                tmp.add(calTemp.getTime());
            }else{
                tmp.add(calToday.getTime());
                break;
            }
        }
        System.out.println("total date is : " + tmp.size());
        return tmp;
    }
}
