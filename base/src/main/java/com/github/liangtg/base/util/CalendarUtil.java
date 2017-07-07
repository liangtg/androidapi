package com.github.liangtg.base.util;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by liangtg on 17-1-19.
 */

public class CalendarUtil {
    private static Calendar chinaCalendar;

    private CalendarUtil() {
    }

    public static Calendar getChinaCalendar() {
        if (null == chinaCalendar) {
            chinaCalendar = Calendar.getInstance(Locale.CHINA);
        } else {
            chinaCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        return chinaCalendar;
    }
}
