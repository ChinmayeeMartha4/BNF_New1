package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
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
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

public class MainMenuUtilityActivity extends Activity {
    private Context context=this;
    private String strFooter="",strCancel="", strYes="", strNo="",strReg="";
    private TextView txtFooter,tvTitleText;
    private ImageView ivTitleBack;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sph;
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
        setContentView(R.layout.activity_main_menu_utility);

        initViews();
        String languageId = sph.getString("Language", "1");// getting languageId
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strReg = sqliteHelper.LanguageChanges(ConstantValue.LANReg, languageId);

        setText();

        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(strCancel + " " + strReg + "?");
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
                                        MainMenuUtilityActivity.this,
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

    private void setText() {
        tvTitleText.setText(R.string.utikities);
    }

    private void initViews() {
        sqliteHelper=new SqliteHelper(this);
        sph=new SharedPrefHelper(this);
        txtFooter=findViewById(R.id.txtFooter);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        tvTitleText=findViewById(R.id.tvTitleText);
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrBMICalculator:
                Intent intentBMICalculator = new Intent(this, ActivityBMI_Calculator.class);
                Bundle animationBMICalculator = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentBMICalculator, animationBMICalculator);
                break;
            case R.id.lnrSAMCalculator:
                Intent intentSAMCalculator = new Intent(this, ActivitySAM_Calculator.class);
                Bundle animationSAMCalculator = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentSAMCalculator, animationSAMCalculator);
                break;
            case R.id.lnrBackToMenu:
                Intent intentBackToMenu = new Intent(this, MainMenuActivity.class);
                Bundle animationBackToMenu = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intentBackToMenu, animationBackToMenu);
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(strCancel + " " + strReg + "?");
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
                                MainMenuUtilityActivity.this,
                                MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}