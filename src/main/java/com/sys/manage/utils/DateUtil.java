package com.sys.manage.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private DateUtil(){}

    public static String currentTimeStr(){
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
        return simpleDateFormat.format(now);
    }

}
