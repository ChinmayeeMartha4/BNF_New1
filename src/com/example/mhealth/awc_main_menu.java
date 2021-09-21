package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
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

public class awc_main_menu extends Activity {
    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;

    TextView tvAddEditAwcDetails, tvViewAwcDetails, tvAddEditWorkerDetails, tvViewWorkerDetails, txtFooter;

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
        setContentView(R.layout.activity_awc_menu);

        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Anganwadi Main Menu");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        tvAddEditAwcDetails = (TextView) findViewById(R.id.tvAddEditAwcDetails);
        tvViewAwcDetails = (TextView) findViewById(R.id.tvViewAwcDetails);
        tvAddEditWorkerDetails = (TextView) findViewById(R.id.tvAddEditWorkerDetails);
        tvViewWorkerDetails = (TextView) findViewById(R.id.tvViewWorkerDetails);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

        String languageId = sph.getString("Language", "1");
        tvAddEditAwcDetails.setText(sqliteHelper.LanguageChanges(ConstantValue.LANAddEditAwc, languageId));
        tvViewAwcDetails.setText(sqliteHelper.LanguageChanges(ConstantValue.LANViewAwcDetails, languageId));
        tvAddEditWorkerDetails.setText(sqliteHelper.LanguageChanges(ConstantValue.LANAddEditWorker, languageId));
        tvViewWorkerDetails.setText(sqliteHelper.LanguageChanges(ConstantValue.LANViewWorkerDetails, languageId));

        String text = "<a href='http://indevjobs.org'>" + sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId) + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @SuppressLint("NewApi")
    public void click_list(View v) {
        switch (v.getId()) {
            case R.id.lnrAddAWC:
                Intent i = new Intent(this, Activity_Add_Awc.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(i, bndlanimation);
                break;
            case R.id.lnrViewAWC:
                Intent i1 = new Intent(this, Activity_View_Awc.class);
                Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(i1, bndlanimation1);
                break;
            case R.id.lnrAddWorkers:
                Intent i2 = new Intent(this, Activity_Add_Worker.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(i2, bndlanimation2);
                break;
            case R.id.lnrViewWorker:
                Intent i3 = new Intent(this, Activity_View_Worker.class);
                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(i3, bndlanimation3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
        finish();
    }
}
