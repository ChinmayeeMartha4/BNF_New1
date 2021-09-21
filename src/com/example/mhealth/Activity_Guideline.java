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

public class Activity_Guideline extends Activity {
    ImageView image1, imageView1, login_first, login_marathi_f, login_marathi_s, main_marathi, setting_marathi,
            login_second, mainmenu, setting, imageView0, imageView0_marathi;
    TextView Info_guideline, Heading_Guideline1, Heading_Guideline2, Heading_Guideline3, Heading_Guideline4, Heading_Guideline5, Heading_Guideline0,
            Heading_Guideline6, sub_Guideline1, sub_Guideline2, sub_Guideline3, sub_Guideline4, sub_Guideline5, sub_Guideline6, sub_Guideline7, sub_Guideline0;
    String strInfo, strHeading0, strHeading1, strHeading2, strHeading3, strHeading4, strHeading5, strHeading6, strSub0, strSub1, strSub2, strSub3, strSub4,
            strSub5, strSub6, strSub7, strNo, strYes, strCancel, strGuideline;
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
        setContentView(R.layout.activity_guideline);
        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Help/Guideline");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());
        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        if (languageId.equalsIgnoreCase("1")) {
            //image1.setVisibility(View.GONE);
            imageView1.setVisibility(View.VISIBLE);
            login_first.setVisibility(View.VISIBLE);
            login_second.setVisibility(View.VISIBLE);
            mainmenu.setVisibility(View.VISIBLE);
            setting.setVisibility(View.VISIBLE);
            imageView0.setVisibility(View.VISIBLE);
        } else {
            image1.setVisibility(View.VISIBLE);
            login_marathi_f.setVisibility(View.VISIBLE);
            login_marathi_s.setVisibility(View.VISIBLE);
            main_marathi.setVisibility(View.VISIBLE);
            setting_marathi.setVisibility(View.VISIBLE);
            imageView0_marathi.setVisibility(View.VISIBLE);

        }
        strInfo = sqliteHelper.LanguageChanges(ConstantValue.LANGuidelineData, languageId);
        strHeading0 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide0, languageId);
        strHeading1 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide1, languageId);
        strHeading2 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide2, languageId);
        strHeading3 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide3, languageId);
        strHeading4 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide4, languageId);
        strHeading5 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide5, languageId);
        strHeading6 = sqliteHelper.LanguageChanges(ConstantValue.LANHeadingGuide6, languageId);
        strSub0 = sqliteHelper.LanguageChanges(ConstantValue.LANSub0, languageId);
        strSub1 = sqliteHelper.LanguageChanges(ConstantValue.LANSub1, languageId);
        strSub2 = sqliteHelper.LanguageChanges(ConstantValue.LANSub2, languageId);
        strSub3 = sqliteHelper.LanguageChanges(ConstantValue.LANSub3, languageId);
        strSub4 = sqliteHelper.LanguageChanges(ConstantValue.LANSub4, languageId);
        strSub5 = sqliteHelper.LanguageChanges(ConstantValue.LANSub5, languageId);
        strSub6 = sqliteHelper.LanguageChanges(ConstantValue.LANSub6, languageId);
        strSub7 = sqliteHelper.LanguageChanges(ConstantValue.LANSub7, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strGuideline = sqliteHelper.LanguageChanges(ConstantValue.LANGuideline, languageId);
        //SETTING TEXT
        Info_guideline.setText(strInfo);
        Heading_Guideline0.setText(strHeading0);
        Heading_Guideline1.setText(strHeading1);
        Heading_Guideline2.setText(strHeading2);
        Heading_Guideline3.setText(strHeading3);
        Heading_Guideline4.setText(strHeading4);
        Heading_Guideline5.setText(strHeading5);
        Heading_Guideline6.setText(strHeading6);
        sub_Guideline0.setText(strSub0);
        sub_Guideline1.setText(strSub1);
        sub_Guideline2.setText(strSub2);
        sub_Guideline3.setText(strSub3);
        sub_Guideline4.setText(strSub4);
        sub_Guideline5.setText(strSub5);
        sub_Guideline6.setText(strSub6);
        sub_Guideline7.setText(strSub7);
        tvTitleText.setText("Guideline");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strGuideline + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_Guideline.this, ActivityHelp.class);
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

        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        Info_guideline = (TextView) findViewById(R.id.Info_guideline);
        Heading_Guideline0 = (TextView) findViewById(R.id.Heading_Guideline0);
        Heading_Guideline1 = (TextView) findViewById(R.id.Heading_Guideline1);
        Heading_Guideline2 = (TextView) findViewById(R.id.Heading_Guideline2);
        Heading_Guideline3 = (TextView) findViewById(R.id.Heading_Guideline3);
        Heading_Guideline4 = (TextView) findViewById(R.id.Heading_Guideline4);
        Heading_Guideline5 = (TextView) findViewById(R.id.Heading_Guideline5);
        Heading_Guideline6 = (TextView) findViewById(R.id.Heading_Guideline6);
        sub_Guideline0 = (TextView) findViewById(R.id.sub_Guideline0);
        sub_Guideline1 = (TextView) findViewById(R.id.sub_Guideline1);
        sub_Guideline2 = (TextView) findViewById(R.id.sub_Guideline2);
        sub_Guideline3 = (TextView) findViewById(R.id.sub_Guideline3);
        sub_Guideline4 = (TextView) findViewById(R.id.sub_Guideline4);
        sub_Guideline5 = (TextView) findViewById(R.id.sub_Guideline5);
        sub_Guideline6 = (TextView) findViewById(R.id.sub_Guideline6);
        sub_Guideline7 = (TextView) findViewById(R.id.sub_Guideline7);
        image1 = (ImageView) findViewById(R.id.imageView1_marathi);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        login_first = (ImageView) findViewById(R.id.imageView_loginfirst);
        login_marathi_f = (ImageView) findViewById(R.id.imageView_loginmf);
        login_second = (ImageView) findViewById(R.id.imageView_loginsecond);
        login_marathi_s = (ImageView) findViewById(R.id.imageView_loginms);
        mainmenu = (ImageView) findViewById(R.id.imageView_main_menu);
        main_marathi = (ImageView) findViewById(R.id.imageView_main_marathi);
        setting = (ImageView) findViewById(R.id.imageView_setting);
        setting_marathi = (ImageView) findViewById(R.id.imageView_setting_marathi);
        imageView0_marathi = (ImageView) findViewById(R.id.imageView0_marathi);
        imageView0 = (ImageView) findViewById(R.id.imageView0);

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strGuideline + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_Guideline.this, ActivityHelp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
