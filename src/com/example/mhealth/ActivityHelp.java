package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
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


public class ActivityHelp extends Activity {

    TextView txtBackground, txtSystemspec, txtGuideline,
            txtRegistration, txtMonitor, txtViews, txtSync,
            txtConclusion, tvTitleText;
    String strBack, strSystem, strGuideline, strRegister, strMonitor, strView, strSync, strConclusion;
    ImageView ivTitleBack;
    private Context context=this;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    TextView txtFooter;

    String strYes, strNo, strCancel, strHelp, strFooter;

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

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Help");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);

        txtFooter = (TextView) findViewById(R.id.txtFooter);
        String languageId = sph.getString("Language", "1");// getting languageId

        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel,
                languageId);
        strHelp = sqliteHelper.LanguageChanges(ConstantValue.LANHelp, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                languageId);

        strBack = sqliteHelper.LanguageChanges(ConstantValue.LANBackground, languageId);
        strSystem = sqliteHelper.LanguageChanges(ConstantValue.LANSystem, languageId);
        strGuideline = sqliteHelper.LanguageChanges(ConstantValue.LANGuideline, languageId);
        strRegister = sqliteHelper.LanguageChanges(ConstantValue.LANRegister, languageId);
        strMonitor = sqliteHelper.LanguageChanges(ConstantValue.LANMonthMonitor, languageId);
        strView = sqliteHelper.LanguageChanges(ConstantValue.LANView, languageId);
        strSync = sqliteHelper.LanguageChanges(ConstantValue.LANSynchronization, languageId);
        strConclusion = sqliteHelper.LanguageChanges(ConstantValue.LANConclusion, languageId);


        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        //SETTING TEXT
        txtBackground.setText(strBack);
        txtSystemspec.setText(strSystem);
        txtGuideline.setText(strGuideline);
        txtRegistration.setText(strRegister);
        txtMonitor.setText(strMonitor);
        txtViews.setText(strView);
        txtSync.setText(strSync);
        txtConclusion.setText(strConclusion);
        tvTitleText.setText("Help");

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                String str = "abcd=0; efgh=1";

                builder.setMessage(strCancel + " " + strHelp + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(
                                        ActivityHelp.this,
                                        MainMenuActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        txtBackground = (TextView) findViewById(R.id.txtBackground);
        txtSystemspec = (TextView) findViewById(R.id.txtSystemspec);
        txtGuideline = (TextView) findViewById(R.id.txtGuideline);
        txtRegistration = (TextView) findViewById(R.id.txtRegistration);
        txtMonitor = (TextView) findViewById(R.id.txtMonitor);
        txtViews = (TextView) findViewById(R.id.txtViews);
        txtSync = (TextView) findViewById(R.id.txtSync);
        txtConclusion = (TextView) findViewById(R.id.txtConclusion);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        String str = "abcd=0; efgh=1";

        builder.setMessage(strCancel + " " + strHelp + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                ActivityHelp.this,
                                MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {

//
//        switch (vw.getId()) {
//            case R.id.lnrBackground:
//			/*	Intent intent = new Intent(this, MainMenuRegistrationActivity.class);
//			startActivity(intent);*/
//
//                Intent intent = new Intent(this, Activity_Background.class);
//                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent, bndlanimation);
//
//
//                break;
//            case R.id.lnrSystem:
//			/*Intent intentListing = new Intent(this, MainMenuMonitoringActivity.class);
//			startActivity(intentListing);*/
//
//                Intent intentSystem = new Intent(this, Activity_System_specification.class);
//                Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intentSystem, bndlanimation1);
//                break;
//            case R.id.lnrGuideline:
//			/*Intent intentInt = new Intent(this, SyncActivity.class);
//			startActivity(intentInt);
//			 */
//                Intent intentGuide = new Intent(this, Activity_Guideline.class);
//                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intentGuide, bndlanimation2);
//
//
//                break;
//            case R.id.lnrRegistration:
//			/*Intent intent2 = new Intent(this, Activity.class);
//			startActivity(intent2);*/
//                Intent intent_Reg = new Intent(this, Activity_Registration.class);
//                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent_Reg, bndlanimation3);
//                break;
//            case R.id.lnrMonitor:
//                Intent intent_mon = new Intent(this, Activity_Monitor.class);
//                Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent_mon, bndlanimation4);
//                break;
//
//            case R.id.lnrViews:
//                Intent intent_view = new Intent(this, Activity_View.class);
//                Bundle bndlanimation5 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent_view, bndlanimation5);
//                break;
//            case R.id.lnrSynchronization:
//                Intent intent_sync = new Intent(this, Activity_Sync.class);
//                Bundle bndlanimation6 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent_sync, bndlanimation6);
//                break;
//            case R.id.lnrConclusion:
//                Intent intent_conclusion = new Intent(this, Activity_Conclusion.class);
//                Bundle bndlanimation7 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intent_conclusion, bndlanimation7);
//                break;
//            default:
//                break;
//        }
    }


}
