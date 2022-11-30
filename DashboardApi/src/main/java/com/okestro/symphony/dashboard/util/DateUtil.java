/*
 * Developed by sw.heo@okestro.com on 2021-02-08
 * Last Modified 2020-02-15 10:40:10
 */
package com.okestro.symphony.dashboard.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class DateUtil {
    private final long DAY = 24 * 60 * 60 * 1000;
    private final String PATTERN = "yyyy.MM.dd";
    private final SimpleDateFormat format = new SimpleDateFormat(PATTERN);


    public long calPeroid(String startDate, String endDate) throws ParseException {
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);

        long calDate = end.getTime() - start.getTime();
        long period = calDate / DAY;

        return period;
    }

    public double calElapsed(long startTime, long endTime) throws ParseException {
        double period = 0;
        period = ( endTime - startTime ) / 1000.0;
        log.debug( "경과 시간 : " + period + "초" );

        return period;
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
        return sdf.format(cal.getTime());
    }


    public String dateToString(Date date) {
        return format.format(date);
    }

    public Date stringToDate(String date) throws ParseException {
        return format.parse(date);
    }

    public String calDate(Date date, int period) {
        Calendar cal  = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,period);

        return format.format(cal.getTime());
    }

    public String calDate(String date, int period) throws ParseException {
        Date startDate = stringToDate(date);
        return calDate(startDate,period);
    }

    public long getBreforeWeekTimestamp(){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.DATE, -7);// 일주일 빼기

        date = cal.getTime();

        long beforeWeek = date.getTime();

        log.debug("## 일주일+(입력받은값)전 timestamp["+beforeWeek+"]");

        return beforeWeek;
    }

    public long getBreforeWeekTimestamp(String dateType, int amount){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.DATE, -7);// 일주일 빼기

        if(dateType.equalsIgnoreCase("hour")){
            cal.add(cal.HOUR, -amount);// 입력받은시간만큼 빼기
        }else if(dateType.equalsIgnoreCase("hour")){
            cal.add(cal.DATE, -amount);// 입력받은 일수만큼 빼기
        }
        date = cal.getTime();

        long beforeWeek = date.getTime();

        log.debug("## 일주일+(입력받은값)전 timestamp["+beforeWeek+"]");

        return beforeWeek;
    }

    public long getBreforeTimestamp(String dateType, int amount){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();

        if(dateType.equalsIgnoreCase("hour")){
            cal.add(cal.HOUR, -amount);// 입력받은 시간만큼 빼기
        }else if(dateType.equalsIgnoreCase("day")){
            cal.add(cal.DATE, -amount);// 입력받은 일수만큼 빼기
        }

        date = cal.getTime();

        long beforeHour = date.getTime();

        log.debug("## "+dateType+", "+amount+"전 timestamp["+beforeHour+"]");

        return beforeHour;
    }

    public long getBreforeOneHourWeekTimestamp(){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.HOUR, -1);// 한시간 빼기
        cal.add(cal.DATE, -7);// 일주일 빼기
        date = cal.getTime();

        long beforeWeek = date.getTime();

        log.debug("## 일주일+한시간전 timestamp["+beforeWeek+"]");

        return beforeWeek;
    }

    public long getBreforehourTimestamp(){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.HOUR, -1);// 한시간 빼기
        date = cal.getTime();

        long beforeHour = date.getTime();

        log.debug("## 한시간전 timestamp["+beforeHour+"]");

        return beforeHour;
    }
}
