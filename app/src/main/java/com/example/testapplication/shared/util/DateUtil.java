package com.example.testapplication.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getStringDate(long date){
        Date dateTime = new Date(date);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(dateTime);
    }
}
