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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ActivityEdit extends Activity {

    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;

    TextView txtEditChild, txtEditEF, txtEditChildNutrition, txtEditChildAbsent, txtEditFooter;
    String strCancel, strYes, strNo, strEditChild, strEditEF, strEditChildNutrition, strEditChildAbsent, strEditFooter;

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
        setContentView(R.layout.activity_edit);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Edit");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();

        String languageId = sph.getString("Language", "1");

        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strEditChild = sqliteHelper.LanguageChanges(ConstantValue.LANChild, languageId);
        strEditEF = sqliteHelper.LanguageChanges(ConstantValue.LANElFamily, languageId);
        strEditFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strEditChildNutrition = sqliteHelper.LanguageChanges(ConstantValue.LANEditChildNutrition, languageId);
        strEditChildAbsent = sqliteHelper.LanguageChanges(ConstantValue.LANEditChildAbsent, languageId);

        txtEditChild.setText(strEditChild);
        txtEditEF.setText(strEditEF);
        txtEditChildNutrition.setText(strEditChildNutrition);
        txtEditChildAbsent.setText(strEditChildAbsent);
        txtEditFooter.setText(strEditFooter);

        String text = "<a href='http://indevjobs.org'>" + strEditFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtEditFooter.setText(processedText);
        txtEditFooter.setLinkTextColor(Color.BLACK);
        txtEditFooter.setClickable(true);
        txtEditFooter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void initialize() {
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);

        txtEditChild = (TextView) findViewById(R.id.txtEditChild);
        txtEditEF = (TextView) findViewById(R.id.txtEditEligibleFamily);
        txtEditChildNutrition = (TextView) findViewById(R.id.txtEditChildNutrition);
        txtEditChildAbsent = (TextView) findViewById(R.id.txtEditChildAbsent);
        txtEditFooter = (TextView) findViewById(R.id.txtEditFooter);
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrEditEligibleFamily:

                final Intent intent = new Intent(this, Activity_EditEligibleFamilyListing.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent, bndlanimation);
                break;

            case R.id.lnrEditChild:

                Intent intent1 = new Intent(this, Activity_EditChildListing.class);
                Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent1, bndlanimation1);
                break;

            case R.id.lnrEditChildNutritionHB:

                Intent intent2 = new Intent(this, Activity_EditChildNutritionListing.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent2, bndlanimation2);
                break;

            case R.id.lnrEditChildAbsent:

                LayoutInflater inflater = this.getLayoutInflater();
                View convertView = inflater.inflate(R.layout.select_absent_option, null);

                LinearLayout child_btn = (LinearLayout) convertView.findViewById(R.id.child_btn);
                LinearLayout pregnant_women_btn = (LinearLayout) convertView.findViewById(R.id.pregnant_women_btn);
                LinearLayout adolescent_girl_btn = (LinearLayout) convertView.findViewById(R.id.adolescent_girl_btn);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setView(convertView);
                alertDialog.setNegativeButton("Cancel", null);

                final AlertDialog alert = alertDialog.create();
                alertDialog.show();
                child_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passActivity_EditChildAbsentListing("child");
                        alert.dismiss();
                    }
                });
                pregnant_women_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passActivity_EditChildAbsentListing("pregnant_women");
                        alert.dismiss();
                    }
                });

                adolescent_girl_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passActivity_EditChildAbsentListing("adolescent_girl");
                        alert.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }

    public void passActivity_EditChildAbsentListing(String option) {
        Intent intent3 = new Intent(ActivityEdit.this, Activity_EditChildAbsentListing.class);
        Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(ActivityEdit.this,
                R.anim.animation1, R.anim.animation2).toBundle();
        intent3.putExtra("option", option);
        startActivity(intent3, bndlanimation3);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(strCancel + " Edit " + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(ActivityEdit.this, MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
