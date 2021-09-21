package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.Weight;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class ShowWebViewChart extends Activity {

    WebView webView;
    SqliteHelper sqliteHelper;
    ArrayList<Weight> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_view_chart);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthly_Monitoring/ Child Nutrition Monitor");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        sqliteHelper = new SqliteHelper(this);
        list = new ArrayList<Weight>();
        webView = (WebView) findViewById(R.id.web);
        // webView.addJavascriptInterface(new WebAppInterface(), "Android");

        list = sqliteHelper.getAgeAndWeight();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/chart.html");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_web_view_chart, menu);
        return true;
    }

}
