package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class Activity_System_specification extends Activity {

    TextView System_data;
    String strSystem, strNo, strYes, strCancel, str;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_specification);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Home/Help/System_specification");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId

        strSystem = sqliteHelper.LanguageChanges(ConstantValue.LANSystem_Data, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        str = sqliteHelper.LanguageChanges(ConstantValue.LANSystem, languageId);

        //SETTING TEXT
        System_data.setText(strSystem);
        tvTitleText.setText("System Specification");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + str + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_System_specification.this, ActivityHelp.class);
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
        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);

        System_data = (TextView) findViewById(R.id.System_data);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + str + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_System_specification.this, ActivityHelp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
