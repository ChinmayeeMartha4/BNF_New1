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

public class Activity_View extends Activity {

    ImageView view, view_marathi, child_list, child_list_marathi, child_view, child_view_marathi,
            preg_view, preg_view_marathi, preg_list, preg_list_marathi, adol_list, adol_list_marathi, adol_view,
            adol_view_marathi, mother_listing, mother_list_marathi, mother_view, mother_view_marathi;
    TextView view_heading1, view_heading2, view_heading3, view_heading4, view_heading5, view_heading6, view_heading7, view_heading8, view_heading9,
            sub_heading1, sub_heading2, sub_heading3, sub_heading4, sub_heading5, sub_heading6, sub_heading7, sub_heading8, sub_heading9;
    String str_heading1, str_heading2, str_heading3, str_heading4, str_heading5, str_heading6, str_heading7, str_heading8, str_heading9, str_sub1, str_sub2, str_sub3, str_sub4, str_sub5, str_sub6, str_sub7, str_sub8, str_sub9, strNo, strYes, strCancel, strView;
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
        setContentView(R.layout.activity_views);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration/ View");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        if (languageId.equalsIgnoreCase("1")) {
            view.setVisibility(View.VISIBLE);
            child_list.setVisibility(View.VISIBLE);
            child_view.setVisibility(View.VISIBLE);
            preg_view.setVisibility(View.VISIBLE);
            preg_list.setVisibility(View.VISIBLE);
            adol_list.setVisibility(View.VISIBLE);
            adol_view.setVisibility(View.VISIBLE);
            mother_listing.setVisibility(View.VISIBLE);
            mother_view.setVisibility(View.VISIBLE);
        } else {
            view_marathi.setVisibility(View.VISIBLE);
            child_list_marathi.setVisibility(View.VISIBLE);
            child_view_marathi.setVisibility(View.VISIBLE);
            preg_view_marathi.setVisibility(View.VISIBLE);
            preg_list_marathi.setVisibility(View.VISIBLE);
            adol_list_marathi.setVisibility(View.VISIBLE);
            adol_view_marathi.setVisibility(View.VISIBLE);
            mother_list_marathi.setVisibility(View.VISIBLE);
            mother_view_marathi.setVisibility(View.VISIBLE);
        }

        str_heading2 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading2, languageId);
        str_heading3 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading3, languageId);
        str_heading4 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading4, languageId);
        str_heading5 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading5, languageId);
        str_heading6 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading6, languageId);
        str_heading7 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading7, languageId);
        str_heading8 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading8, languageId);
        str_heading9 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Heading9, languageId);
        str_sub1 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub1, languageId);
        str_sub2 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub2, languageId);
        str_sub3 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub3, languageId);
        str_sub4 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub4, languageId);
        str_sub5 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub5, languageId);
        str_sub6 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub6, languageId);
        str_sub7 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub7, languageId);
        str_sub8 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub8, languageId);
        str_sub9 = sqliteHelper.LanguageChanges(ConstantValue.LANView_Sub9, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strView = sqliteHelper.LanguageChanges(ConstantValue.LANView, languageId);

        //SETTING TEXT

        view_heading2.setText(str_heading2);
        view_heading3.setText(str_heading3);
        view_heading4.setText(str_heading4);
        view_heading5.setText(str_heading5);
        view_heading6.setText(str_heading6);
        view_heading7.setText(str_heading7);
        view_heading8.setText(str_heading8);
        view_heading9.setText(str_heading9);
        sub_heading1.setText(str_sub1);
        sub_heading2.setText(str_sub2);
        sub_heading3.setText(str_sub3);
        sub_heading4.setText(str_sub4);
        sub_heading5.setText(str_sub5);
        sub_heading6.setText(str_sub6);
        sub_heading7.setText(str_sub7);
        sub_heading8.setText(str_sub8);
        sub_heading9.setText(str_sub9);
        tvTitleText.setText("Views");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strView + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_View.this, ActivityHelp.class);
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
        uploadAttendance();

        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        view_heading2 = (TextView) findViewById(R.id.View_Heading2);
        view_heading3 = (TextView) findViewById(R.id.View_Heading3);
        view_heading4 = (TextView) findViewById(R.id.View_Heading4);
        view_heading5 = (TextView) findViewById(R.id.View_Heading5);
        view_heading6 = (TextView) findViewById(R.id.View_Heading6);
        view_heading7 = (TextView) findViewById(R.id.View_Heading7);
        view_heading8 = (TextView) findViewById(R.id.View_Heading8);
        view_heading9 = (TextView) findViewById(R.id.View_Heading9);
        sub_heading1 = (TextView) findViewById(R.id.sub_View1);
        sub_heading2 = (TextView) findViewById(R.id.sub_View2);
        sub_heading3 = (TextView) findViewById(R.id.sub_View3);
        sub_heading4 = (TextView) findViewById(R.id.sub_View4);
        sub_heading5 = (TextView) findViewById(R.id.sub_View5);
        sub_heading6 = (TextView) findViewById(R.id.sub_View6);
        sub_heading7 = (TextView) findViewById(R.id.sub_View7);
        sub_heading8 = (TextView) findViewById(R.id.sub_View8);
        sub_heading9 = (TextView) findViewById(R.id.sub_View9);

        view = (ImageView) findViewById(R.id.imageView_view);
        view_marathi = (ImageView) findViewById(R.id.imageView_view_marathi);
        child_list = (ImageView) findViewById(R.id.imageView_child_list);
        child_list_marathi = (ImageView) findViewById(R.id.imageView_child_list_mar);
        child_view = (ImageView) findViewById(R.id.imageView_child_view);
        child_view_marathi = (ImageView) findViewById(R.id.imageView_child_view_mar);
        preg_view = (ImageView) findViewById(R.id.imageView_women_view);
        preg_view_marathi = (ImageView) findViewById(R.id.imageView_women_view_mar);
        preg_list = (ImageView) findViewById(R.id.imageView_women_list);
        preg_list_marathi = (ImageView) findViewById(R.id.imageView_women_list_mar);
        adol_view = (ImageView) findViewById(R.id.imageView_adol_view);
        adol_view_marathi = (ImageView) findViewById(R.id.imageView_adol_view_mar);
        adol_list = (ImageView) findViewById(R.id.imageView_adol_list);
        adol_list_marathi = (ImageView) findViewById(R.id.imageView_adol_list_mar);
        mother_listing = (ImageView) findViewById(R.id.imageView_mother_list);
        mother_list_marathi = (ImageView) findViewById(R.id.imageView_mother_list_mar);
        mother_view = (ImageView) findViewById(R.id.imageView_mother_view);
        mother_view_marathi = (ImageView) findViewById(R.id.imageView_mother_view_mar);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strView + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_View.this, ActivityHelp.class);
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
