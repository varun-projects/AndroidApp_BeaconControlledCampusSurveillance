package com.attendancesolution.bams.singletonlClasses;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akshay on 28-Apr-16.
 */
public class Utilities {

    public static String getDateInString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String getDayInString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        return dayOfTheWeek;
    }

    public static String getOnlyDateInString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public static String getOnlyMonthInString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

}
