package com.remote.tools.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;

public class TimeConvert {
    //将"yyyy-MM-dd"格式的字符串转换为LocalDate的时间
    static public LocalDate convertStringToLocalDate(String strTime){
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
        LocalDate time= null;
        ZoneId zoneId=ZoneId.systemDefault();
        try {
            time = fmt.parse(strTime).toInstant().atZone(zoneId).toLocalDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  time;
    }

    //获取本地时间
    public static Instant getNowTime()
    {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+0"));
    }


}
