package com.springboot.hotelbookingsystem.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BookingUtils {

    public static Date convertStringDate(String dateString, String format) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        Date result = new Date();
        try {
            result = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
        }
        return result;
    }

    public static String convertDateString(Date date, String format) {
        if (format == null) {
            format = "yyyy-MM-dd";
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static long calculateDaysDiff(String checkin, String checkout) {
        long diff = 0;
        try {
            long diffInMillies = Math.abs(new SimpleDateFormat("yyyy-MM-dd").parse(checkout).getTime()
                    - new SimpleDateFormat("yyyy-MM-dd").parse(checkin).getTime());
            diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
        }
        return diff;
    }

    public static long calculateDaysDiff(Date checkin, Date checkout) {
        long diffInMillies = Math.abs(checkout.getTime() - checkin.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
