package com.example.mhealth.helper;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class GoogleAnalyticsHelper extends Application {
    private static final String PROPERTY_ID = "UA-77483705-1";
    public static int GENERAL_TRACKER = 0;
    public HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public GoogleAnalyticsHelper() {
        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {

        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) :
                    (trackerId == TrackerName.GLOBAL_TRACKER) ?
                            analytics.newTracker(com.example.mhealth.R.xml.global_tracker) :
                            analytics.newTracker(com.example.mhealth.R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        E_COMMERCE_TRACKER,
    }
}
