package com.okestro.symphony.dashboard.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class DateForm {

    public static long getCurrentDateTime() {
        return new Date().getTime();
    }

    public static long getCurrentUnixTime() {
        long unixTime = System.currentTimeMillis()/1000;
        return unixTime;
    }

    public static long ConvertUnixToDateFormat(long unixTime) {

        String result = null;
        long ret = 0;

        try {

            Date date = new Date(unixTime*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            result = sdf.format(date);
            ret = Long.parseLong(result);

        } catch(Exception e) {

            log.error(e.getMessage(), e);

        }

        return ret;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

}
