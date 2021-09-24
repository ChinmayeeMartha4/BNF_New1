package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.example.mhealth.NewCode.NutritionChampionsMonitoring;
import com.example.mhealth.NewCode.SuposhanSakhiMonitoring;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

public class MainMenuMonitoringActivity extends Activity {

    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    TextView txtChildNutritionm, txtPregntWomen, txtBackToMainMenu,
            txtAdolescentGirl, txtFooter, txtChildCount,txtPregnantWomenCount,
            txtAdolescentCOunt, tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    String strCancel, strYes, strNo, strMainMenu, strFooter, strChildNut, strPregWomNut,
            strAdlNut, strMonthlyMon;
    LinearLayout lnrSuposhanSakhi,lnrNutrition;

    public static Spannable removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
        return p_Text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_monitoring);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Monthly Monitoring");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        uploadAttendance();

        String languageId = sph.getString("Language", "1");// getting languageId

        strChildNut = sqliteHelper.LanguageChanges(ConstantValue.LANChildNut, languageId);
        strPregWomNut = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomen, languageId);
        strAdlNut = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescent, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANMM, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strMonthlyMon = sqliteHelper.LanguageChanges(ConstantValue.LANMonthlyMonitoring, languageId);

        txtChildNutritionm.setText(strChildNut);
        txtPregntWomen.setText(strPregWomNut);
        txtAdolescentGirl.setText(strAdlNut);
        txtBackToMainMenu.setText(strMainMenu);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        int count= sqliteHelper.getChildCount();
        int count1= sqliteHelper.getAdolescentCount();
        int count2= sqliteHelper.getPregnantCount();
        txtChildCount.setText("("+count+")");
        txtAdolescentCOunt.setText("("+count1+")");
        txtPregnantWomenCount.setText("("+count2+")");
        tvTitleText.setText(getString(R.string.monitoring));

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(strCancel + " " + strMonthlyMon + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(MainMenuMonitoringActivity.this, MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        lnrSuposhanSakhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainMenuMonitoringActivity.this, SuposhanSakhiMonitoring.class);
                startActivity(intent);

            }
        });
        lnrNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainMenuMonitoringActivity.this, NutritionChampionsMonitoring.class);
                startActivity(intent);

            }
        });

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }
    }

    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                /*Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Resources resources = getBaseContext().getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());*/

                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                Locale current = getBaseContext().getResources().getConfiguration().locale;
                String lang1=current.getLanguage();
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Locale currentn = getBaseContext().getResources().getConfiguration().locale;
                String lang=currentn.getLanguage();
                Log.e("Lang>>>","lang1>>>"+lang1+">>>lang>>>"+lang);
            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }


    public void initialize() {

        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);

        txtChildCount=(TextView) findViewById(R.id.txtChildCount);
        txtAdolescentCOunt=(TextView) findViewById(R.id.txtAdolescentGirlcount);
        txtPregnantWomenCount=(TextView) findViewById(R.id.txtPregntWomenmcount);
        txtChildNutritionm = (TextView) findViewById(R.id.txtChildNutritionm);
        txtPregntWomen = (TextView) findViewById(R.id.txtPregntWomenm);
        txtAdolescentGirl = (TextView) findViewById(R.id.txtAdolescentGirl);
        txtBackToMainMenu = (TextView) findViewById(R.id.txtBackToMenu);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);

        lnrNutrition=findViewById(R.id.lnrNutrition);
        lnrSuposhanSakhi=findViewById(R.id.lnrSuposhanSakhi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_monitoring, menu);
        return true;
    }

    public void click_child(View vw) {
        Intent intent = new Intent(this, ActivityChildNutritionMonitor.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrChildNutrn:

                Intent intent = new Intent(this, ActivityChildNutritionMonitor.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent, bndlanimation);

                break;
            case R.id.lnrPregntWomen:
         /*Intent intentListing = new Intent(this, ActivityPregnantWomenMonitoring.class);
         startActivity(intentListing);*/

                Intent intentListing = new Intent(this, ActivityPregnantWomenMonitoring.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentListing, bndlanimation2);

                break;
            case R.id.lnrAdolescent:
                Intent intent1 = new Intent(this, AdolescentMonitoringActivity.class);
                startActivity(intent1);


                break;
            case R.id.lnrBcktoMenu:
         /*Intent intentInt = new Intent(this, MainMenuActivity.class);
         startActivity(intentInt);*/


                Intent intentInt = new Intent(this, MainMenuActivity.class);
                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentInt, bndlanimation3);

                break;


            case R.id.lnrchildbehivourchange:
                Intent intent4 = new Intent(this, Activity_ChildBehivourChange.class);
                Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent4, bndlanimation4);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strCancel + " " + strMonthlyMon + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(MainMenuMonitoringActivity.this, MainMenuActivity.class);
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
            new sync_sevika_helper(this).execute("");
        }
    }

    public void monthlyProgress(View view) {

        Intent intent = new Intent(this, MonthlyProgress.class);
        startActivity(intent);
    }
}
