package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ActivityViews extends Activity {


    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    TextView txtChildView, txtPregntWomenmView, txtAdolescentGirlView, txtBackToMenuView, txtFooter, txtMotherView;
    String strChildView, strViews, strPregntWomenmView, strAdolescentGirlView, strBackToMenuView, strFooter, strCancel, strYes, strNo, strMotherView;

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
        setContentView(R.layout.activity_activity_views);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Views");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialization();
        String languageId = sph.getString("Language", "1");// getting languageId

        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strViews = sqliteHelper.LanguageChanges(ConstantValue.LANViews, languageId);
        strBackToMenuView = sqliteHelper.LanguageChanges(ConstantValue.LANMM, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strChildView = sqliteHelper.LanguageChanges(ConstantValue.LANChild, languageId);
        strPregntWomenmView = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomen, languageId);
        strAdolescentGirlView = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescent, languageId);
        strMotherView = sqliteHelper.LanguageChanges(ConstantValue.LANMotherView, languageId);


        txtChildView.setText(strChildView);
        txtPregntWomenmView.setText(strPregntWomenmView);
        txtAdolescentGirlView.setText(strAdolescentGirlView);
        txtMotherView.setText(strMotherView);
        txtBackToMenuView.setText(strBackToMenuView);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public void initialization() {

        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);

        txtChildView = (TextView) findViewById(R.id.txtChildView);
        txtPregntWomenmView = (TextView) findViewById(R.id.txtPregntWomenmView);
        txtAdolescentGirlView = (TextView) findViewById(R.id.txtAdolescentGirlView);
        txtMotherView = (TextView) findViewById(R.id.txtMotherView);
        txtBackToMenuView = (TextView) findViewById(R.id.txtBackToMenuView);
        txtFooter = (TextView) findViewById(R.id.txtFooter);


    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrChildView:

                Intent intent = new Intent(this, Activity_childListing.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent, bndlanimation);

                break;
            case R.id.lnrPregntWomenView:
			/*Intent intentListing = new Intent(this, ActivityPregnantWomenMonitoring.class);
			startActivity(intentListing);*/

                Intent intentListing = new Intent(this, Activity_womenListing.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentListing, bndlanimation2);

                break;
            case R.id.lnrAdolescentView:

                Intent intent1 = new Intent(this, Activity_AdlGirlList.class);
                Bundle bnd = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent1, bnd);

                break;
            case R.id.lnrMotherView:
			/*Intent intentListing = new Intent(this, ActivityPregnantWomenMonitoring.class);
			startActivity(intentListing);*/

                Intent intentListin = new Intent(this, Activity_motherListing.class);
                Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentListin, bndlanimation4);

                break;
            case R.id.lnrBcktoMenuView:
			/*Intent intentInt = new Intent(this, MainMenuActivity.class);
			startActivity(intentInt);*/


                Intent intentInt = new Intent(this, MainMenuActivity.class);
                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentInt, bndlanimation3);

                break;


        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strCancel +" "+ strViews + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ActivityViews.this, MainMenuRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
