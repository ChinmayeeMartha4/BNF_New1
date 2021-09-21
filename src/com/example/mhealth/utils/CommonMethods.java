package com.example.mhealth.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommonMethods {
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static String getUUID() {
        Date date = new Date();
        //Calendar calUUID = Calendar.getInstance();
        DateFormat dateFormatUUID = new SimpleDateFormat("yyyyMMddHHmmss");
        String uuid= dateFormatUUID.format(date);

        return uuid;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String formattedDate = dateFormat.format(c.getTime());

        return formattedDate;
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String formattedDate = dateFormat.format(c.getTime());

        return formattedDate;
    }
}
