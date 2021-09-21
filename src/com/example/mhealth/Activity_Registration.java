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

public class Activity_Registration extends Activity {

    ImageView i_reg, i_reg_marathi, fam_reg, fam_reg_marathi, preg_reg, preg_reg_marathi, child_reg,
            child_reg_marathi, adol_reg, adol_reg_marathi;
    TextView Register_Heading2, Register_Heading3, Register_Heading4, Register_Heading5, sub_Register1, sub_Register2, sub_Register3, sub_Register4, sub_Register5;
    String strHeading2, strHeading3, strHeading4, strHeading5, strSub1, strSub2, strSub3, strSub4, strSub5, strNo, strYes,
            strCancel, strRegister;
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
        setContentView(R.layout.activity_registration);

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());

        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        if (languageId.equalsIgnoreCase("1")) {
            i_reg.setVisibility(View.VISIBLE);
            fam_reg.setVisibility(View.VISIBLE);
            preg_reg.setVisibility(View.VISIBLE);
            child_reg.setVisibility(View.VISIBLE);
            adol_reg.setVisibility(View.VISIBLE);
        } else {
            i_reg_marathi.setVisibility(View.VISIBLE);
            fam_reg_marathi.setVisibility(View.VISIBLE);
            preg_reg_marathi.setVisibility(View.VISIBLE);
            child_reg_marathi.setVisibility(View.VISIBLE);
            adol_reg_marathi.setVisibility(View.VISIBLE);
        }
        strHeading2 = sqliteHelper.LanguageChanges(ConstantValue.LANRegister_Heading2, languageId);
        strHeading3 = sqliteHelper.LanguageChanges(ConstantValue.LANRegister_Heading3, languageId);
        strHeading4 = sqliteHelper.LanguageChanges(ConstantValue.LANRegister_Heading4, languageId);
        strHeading5 = sqliteHelper.LanguageChanges(ConstantValue.LANRegister_Heading5, languageId);
        strSub1 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Register1, languageId);
        strSub2 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Register2, languageId);
        strSub3 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Register3, languageId);
        strSub4 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Register4, languageId);
        strSub5 = sqliteHelper.LanguageChanges(ConstantValue.LANSub_Register5, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strRegister = sqliteHelper.LanguageChanges(ConstantValue.LANRegister, languageId);

        //SETTING TEXT
        Register_Heading2.setText(strHeading2);
        Register_Heading3.setText(strHeading3);
        Register_Heading4.setText(strHeading4);
        Register_Heading5.setText(strHeading5);
        sub_Register1.setText(strSub1);
        sub_Register2.setText(strSub2);
        sub_Register3.setText(strSub3);
        sub_Register4.setText(strSub4);
        sub_Register5.setText(strSub5);
        tvTitleText.setText("Registration");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strRegister + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_Registration.this, ActivityHelp.class);
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
        Register_Heading2 = (TextView) findViewById(R.id.Register_Heading2);
        Register_Heading3 = (TextView) findViewById(R.id.Register_Heading3);
        Register_Heading4 = (TextView) findViewById(R.id.Register_Heading4);
        Register_Heading5 = (TextView) findViewById(R.id.Register_Heading5);
        sub_Register1 = (TextView) findViewById(R.id.sub_Register1);
        sub_Register2 = (TextView) findViewById(R.id.sub_Register2);
        sub_Register3 = (TextView) findViewById(R.id.sub_Register3);
        sub_Register4 = (TextView) findViewById(R.id.sub_Register4);
        sub_Register5 = (TextView) findViewById(R.id.sub_Register5);

        i_reg = (ImageView) findViewById(R.id.imageView_reg_main);
        i_reg_marathi = (ImageView) findViewById(R.id.imageView_reg_main_marathi);
        fam_reg = (ImageView) findViewById(R.id.imageView_elg_family);
        fam_reg_marathi = (ImageView) findViewById(R.id.imageView_family_marathi);
        preg_reg = (ImageView) findViewById(R.id.imageView_preg_reg);
        preg_reg_marathi = (ImageView) findViewById(R.id.imageView_preg_marathi);
        child_reg = (ImageView) findViewById(R.id.imageView_child_reg);
        child_reg_marathi = (ImageView) findViewById(R.id.imageView_child_marathi);
        adol_reg = (ImageView) findViewById(R.id.imageView_adol_reg);
        adol_reg_marathi = (ImageView) findViewById(R.id.imageView_adol_marathi);


    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strRegister + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_Registration.this, ActivityHelp.class);
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
