package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Activity_Monitor extends Activity {

    ImageView monitor, monitor_marathi, child_mon, child_mon_marathi, preg_mon, preg_mon_marathi, adol_mon,
            adol_mon_marathi;
    TextView Monitor_Heading2, Monitor_Heading3, Monitor_Heading4, sub_Monitor1,
            sub_Monitor2, sub_Monitor3, sub_Monitor4;
    String strHeading2, strHeading3, strHeading4, strSubHeading1, strSubHeading2, strSubHeading3, strSubHeading4, strNo, strYes, strCancel, strMonitor;
    SqliteHelper sqliteHelper;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    SharedPrefHelper sph;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_monitor);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthly Monitoring");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        if (languageId.equalsIgnoreCase("1")) {
            monitor.setVisibility(View.VISIBLE);
            child_mon.setVisibility(View.VISIBLE);
            preg_mon.setVisibility(View.VISIBLE);
            adol_mon.setVisibility(View.VISIBLE);
        } else {
            monitor_marathi.setVisibility(View.VISIBLE);
            child_mon_marathi.setVisibility(View.VISIBLE);
            preg_mon_marathi.setVisibility(View.VISIBLE);
            adol_mon_marathi.setVisibility(View.VISIBLE);
        }
        strHeading2 = sqliteHelper.LanguageChanges(ConstantValue.LANMonitor_Heading2, languageId);
        strHeading3 = sqliteHelper.LanguageChanges(ConstantValue.LANMonitor_Heading3, languageId);
        strHeading4 = sqliteHelper.LanguageChanges(ConstantValue.LANMonitor_Heading4, languageId);
        strSubHeading1 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Monitor1, languageId);
        strSubHeading2 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Monitor2, languageId);
        strSubHeading3 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Monitor3, languageId);
        strSubHeading4 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Monitor4, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strMonitor = sqliteHelper.LanguageChanges(ConstantValue.LANMonthMonitor, languageId);
        Log.e("strSubHeading1", strSubHeading1);
        //SETTING TEXT

        Monitor_Heading2.setText(strHeading2);
        Monitor_Heading3.setText(strHeading3);
        Monitor_Heading4.setText(strHeading4);
        sub_Monitor1.setText(strSubHeading1);
        sub_Monitor2.setText(strSubHeading2);
        sub_Monitor3.setText(strSubHeading3);
        sub_Monitor4.setText(strSubHeading4);
        tvTitleText.setText("Monthly Monitoring");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strMonitor + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_Monitor.this, ActivityHelp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void initialize() {
        uploadAttendance();
        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);

        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        Monitor_Heading2 = (TextView) findViewById(R.id.Monitor_Heading2);
        Monitor_Heading3 = (TextView) findViewById(R.id.Monitor_Heading3);
        Monitor_Heading4 = (TextView) findViewById(R.id.Monitor_Heading4);
        sub_Monitor1 = (TextView) findViewById(R.id.sub_Monitor1);
        sub_Monitor2 = (TextView) findViewById(R.id.sub_Monitor2);
        sub_Monitor3 = (TextView) findViewById(R.id.sub_Monitor3);
        sub_Monitor4 = (TextView) findViewById(R.id.sub_Monitor4);

        monitor = (ImageView) findViewById(R.id.imageView_monitor);
        monitor_marathi = (ImageView) findViewById(R.id.imageView_monitor_marathi);
        child_mon = (ImageView) findViewById(R.id.imageView_child_mon);
        child_mon_marathi = (ImageView) findViewById(R.id.imageView_child_mon_mar);
        preg_mon = (ImageView) findViewById(R.id.imageView_preg_mon);
        preg_mon_marathi = (ImageView) findViewById(R.id.imageView_preg_mon_mar);
        adol_mon = (ImageView) findViewById(R.id.imageView_adol);
        adol_mon_marathi = (ImageView) findViewById(R.id.imageView_adol_mar);

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strMonitor + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_Monitor.this, ActivityHelp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }
}
