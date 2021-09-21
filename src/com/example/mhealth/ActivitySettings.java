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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;


public class ActivitySettings extends Activity {


    SharedPrefHelper sph;
    SqliteHelper sqliteHelper;
    TextView txtSelectLanguage, txtSelectServer, txtFooter;
    Spinner spnLanguage, spnServer;
    Button btnSave;
    String strYes, strNo, strFooter, strCancel, strSelLang, strSelServer, strTryAgain, strSettings, strSave, strlanguageId;

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
        setContentView(R.layout.activity_settings);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Setting");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        strlanguageId = sph.getString("Language", "1");// getting languageId
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, strlanguageId);
        strTryAgain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strlanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strlanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strlanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strlanguageId);
        strSelLang = sqliteHelper.LanguageChanges(ConstantValue.LANSelectLan, strlanguageId);
        strSelServer = sqliteHelper.LanguageChanges(ConstantValue.LANSelectServer, strlanguageId);
        strSettings = sqliteHelper.LanguageChanges(ConstantValue.LANSetting, strlanguageId);
        strSave = sqliteHelper.LanguageChanges(ConstantValue.LANsv, strlanguageId);


        txtSelectLanguage.setText(strSelLang);
        txtSelectServer.setText(strSelServer);
        btnSave.setText(strSave);
        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());


//        populateLandholding(spnLanguage, "language", "id", "value", "Select Languange", "");
//        populateLandholding(spnServer, "server", "id", "value", "Select server", "");
    }

    public void initialize() {
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        txtSelectLanguage = (TextView) findViewById(R.id.txtSelectLanguage);
        txtSelectServer = (TextView) findViewById(R.id.txtSelectServer);
        spnLanguage = (Spinner) findViewById(R.id.spnLanguage);
        spnServer = (Spinner) findViewById(R.id.spnServer);
        btnSave = (Button) findViewById(R.id.btnSave);
        txtFooter = (TextView) findViewById(R.id.txtFooter);

    }

    @SuppressLint("NewApi")
    public void click_save(View vw) {
        sph.setString("Languageid", getSelectedValue(spnLanguage));
        sph.setString("Server", "Live");

		/*Intent intent= new Intent(this, MainMenuActivity.class);
		startActivity(intent);*/

        Intent intent1 = new Intent(this, MainMenuActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                .toBundle();
        startActivity(intent1, bndlanimation4);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        return data.getValue();
    }

    @SuppressLint("NewApi")
    public void populateLandholding(Spinner spinner, String tableName,
                                    String col_id, String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();

//        items = sqliteHelper.populateSpinner(tableName, col_id, col_value,
//                label, whr, languageId);


        ArrayList<SpinnerHelper> items1 = new ArrayList<SpinnerHelper>();

        items1 = sqliteHelper.populateSpinner1(tableName, col_id, col_value,
                label, whr);
        if (strlanguageId.equals("1")) {

            ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                    ActivitySettings.this,
                    android.R.layout.simple_spinner_item, items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setPrompt(label);
            spinner.setAdapter(adapter);
        } else if (strlanguageId.equals("2")) {


            ArrayAdapter<SpinnerHelper> adapter1 = new ArrayAdapter<SpinnerHelper>(
                    ActivitySettings.this,
                    android.R.layout.simple_spinner_item, items1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setPrompt(label);
            spinner.setAdapter(adapter1);

        }
    }

    @SuppressLint("NewApi")
    public void click_btnLogout(View vw) {
		/*Intent intent= new Intent(this, ActivityLogin.class);
		startActivity(intent);*/

        Intent intent1 = new Intent(this, ActivityLogin.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                .toBundle();
        startActivity(intent1, bndlanimation4);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strSettings + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ActivitySettings.this, MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
