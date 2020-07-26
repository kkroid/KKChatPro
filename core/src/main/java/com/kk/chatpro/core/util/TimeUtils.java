package com.kk.chatpro.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("unused")
public final class TimeUtils {
    private static final String FORMAT_YEAR = "yyyy-MM-dd";
    private static final String FORMAT_MONTH = "MM-dd HH:mm";
    private static final String FORMAT_DAY = "MM-dd HH:mm";
    private static final String FORMAT_HOUR = "HH:mm";
    private static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private TimeUtils() {
    }

    public static String readableTime(long time) {
        long timeNow = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(timeNow);
        Calendar pending = Calendar.getInstance();
        pending.setTimeInMillis(time);
        int yearNow = now.get(Calendar.YEAR);
        int yearPending = pending.get(Calendar.YEAR);
        int monthNow = now.get(Calendar.MONTH);
        int monthPending = pending.get(Calendar.MONTH);
        int dayNow = now.get(Calendar.DAY_OF_MONTH);
        int dayPending = pending.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat dateFormat;
        if (yearNow == yearPending) {
            if (monthNow == monthPending) {
                if (dayNow == dayPending) { // hour format
                    dateFormat = new SimpleDateFormat(FORMAT_HOUR, Locale.getDefault());
                } else { // day format
                    dateFormat = new SimpleDateFormat(FORMAT_DAY, Locale.getDefault());
                }
            } else { // month format
                dateFormat = new SimpleDateFormat(FORMAT_MONTH, Locale.getDefault());
            }
        } else { // year format
            dateFormat = new SimpleDateFormat(FORMAT_YEAR, Locale.getDefault());
        }
        return dateFormat.format(pending.getTime());
    }

    public static String yyyyMMddHHmmss(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_MM_SS, Locale.getDefault());
        Calendar pending = Calendar.getInstance();
        pending.setTimeInMillis(time);
        return dateFormat.format(pending.getTime());
    }
}
