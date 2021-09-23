package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.NewCode.ViewMenu;
import com.example.mhealth.app_drawer.AppDrawer;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.model.AttendanceImagePojo;
import com.example.mhealth.model.DownloadData;
import com.example.mhealth.rest_apis.ApiClient;
import com.example.mhealth.rest_apis.M_Health_API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppDrawer {

    TextView txtRegistration, txtMonitoring, txtSynchronization,
            txtadolscentBaseline,txtPWBaseline,txtHelp, txtSettings,
            txtExit, txtEdit, txtFooter, tvAwcAndMemDetails,txtEventsOrMeetings;
    String strYes, strNo, AppmPunchline, strReg, strMonthlyMon, strSync, strHelp, strSetting, strExit, strEdit,
            strCancel, strFooter, strMainMenu, strAwcAndMemDetails,strAdolBaseline,strPWBaseline;

    Menu menuEdit;
    LinearLayout lnrEventsOrMeetings,lnrEdit,lnrView,lnrSetting;


    String curDate, Sstatus = "", Hstatus = "";

    TextView start,end,tv_start_time1,tv_end_time1;
    String screen_type="";
    String start1="";
    String start_time="";
    String end_time="";
    String currentTimeStamp;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    AttendanceImagePojo attendanceImagePojo;

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
        setContentView(R.layout.activity_main_menu);
        initialize();
        Bundle bundle =getIntent().getExtras();
        if (bundle!=null) {
            screen_type = bundle.getString("screen_type", "");
            start1 = bundle.getString("start1", "");
            start_time = bundle.getString("start_time", "");
            end_time = bundle.getString("end_time", "");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        currentTimeStamp = dateFormat.format(new Date());
        end.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);
        tv_start_time1.setText(start_time);
        tv_end_time1.setText(end_time);
        if(screen_type.equals("AttendanceImage"))
        {
            end.setVisibility(View.VISIBLE);
            start.setVisibility(View.GONE);

        }
        attendanceImagePojo=sqliteHelper.getAttendanceImageData();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (attendanceImagePojo.getLocal_id() != 1) {

                    Intent intent = new Intent(MainMenuActivity.this, AttendanceImage.class);
                    intent.putExtra("screen_type", "MainMenu");

                    startActivity(intent);
                }

                else {
                    Toast.makeText(MainMenuActivity.this, "", Toast.LENGTH_SHORT).show();

                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, AttendanceImage.class);
                startActivity(intent);
            }
        });
        //showPresentDialog();
        String languageId = sph.getString("Language", "1");// getting languageId

        strReg = sqliteHelper.LanguageChanges(ConstantValue.LANReg, languageId);
        strMonthlyMon = sqliteHelper.LanguageChanges(ConstantValue.LANMonthlyMonitoring, languageId);
        strSync = sqliteHelper.LanguageChanges(ConstantValue.LANSync, languageId);
        strHelp = sqliteHelper.LanguageChanges(ConstantValue.LANHelp, languageId);
        strSetting = sqliteHelper.LanguageChanges(ConstantValue.LANSetting, languageId);
        strExit = sqliteHelper.LanguageChanges(ConstantValue.LANExit, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strEdit = sqliteHelper.LanguageChanges(ConstantValue.LANEdit, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANMM, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strAdolBaseline = sqliteHelper.LanguageChanges(ConstantValue.LANAdBase,languageId);
        strPWBaseline = sqliteHelper.LanguageChanges(ConstantValue.LANPWBASE,languageId);
        //  strAwcAndMemDetails = sqliteHelper.LanguageChanges(ConstantValue.LANmmAwc, languageId);

        //setting text
        // txtRegistration.setText(strReg);
        txtSettings.setText(strSetting);
        //  txtHelp.setText(strHelp);
        //   txtMonitoring.setText(R.string.monitoring);
        txtExit.setText(strExit);
        txtEdit.setText(strEdit);
        txtSynchronization.setText(R.string.synchronize);
        // tvAwcAndMemDetails.setText(strAwcAndMemDetails);
        //  txtadolscentBaseline.setText(strAdolBaseline);
        //  txtPWBaseline.setText(strPWBaseline);
        setTitle("");
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.main_menu);

        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        uploadAttendance();

        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }

        lnrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, EditMenu.class);
                startActivity(intent);
            }
        });
        lnrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, ViewMenu.class);
                startActivity(intent);
            }
        });
        lnrSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, ActivitySettings.class);
                startActivity(intent);
            }
        });

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

    private void setPreRightVaDialog() {
// custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_layout);
        dialog.setTitle("Adolescent Baseline");


// set the custom dialog components - text, image and button
        Button btnBaseline = (Button) dialog.findViewById(R.id.btnBaseline);
        Button btnEditBaseline = (Button) dialog.findViewById(R.id.btnEditBaseline);


// if button is clicked, close the custom dialog
        btnBaseline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMenuActivity.this, Adolscent_Baseline.class);
                startActivity(intent);
                //dialog.dismiss();
            }
        });

        btnEditBaseline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AdolescentBaselineAndView.class);
                startActivity(intent);
                //  dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void showPresentDialog() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        curDate = df.format(c.getTime());
        String a = sqliteHelper.getSevikaHelper(curDate);
        if (a == "" && GlobalVars.hpresent == 1) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Anganwadi Sevika attendance:-");
            String[] re = {"Absent", "Present"};
            builder1.setSingleChoiceItems(re, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Sstatus = "Absent";
                            break;

                        case 1:
                            Sstatus = "Present";
                            break;

                        default:
                            break;
                    }
                }
            });
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainMenuActivity.this);
                    builder2.setTitle("Anganwadi Helper attendance:-");
                    String[] ren = {"Absent", "Present"};
                    builder2.setSingleChoiceItems(ren, -1, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Hstatus = "Absent";
                                    break;

                                case 1:
                                    Hstatus = "Present";
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
                    builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] atten = {curDate, Sstatus, Hstatus};
                            sqliteHelper.setSevikaHelper(atten);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder2.create();
                    alert.show();
                }
            });
            builder1.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GlobalVars.hpresent = 2;
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder1.create();
            alert.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        /*MenuInflater me = getMenuInflater();
        me.inflate(R.menu.main_menu, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.menuEdit:

                Intent editInt = new Intent(this, ActivityEdit.class);
                startActivity(editInt);
                break;
            default:
                break;
        }*/
        return false;
    }

    public void initialize() {

        sph = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        txtRegistration = (TextView) findViewById(R.id.txtRegistration);
        // txtMonitoring = (TextView) findViewById(R.id.txtMonitoring);
        txtSynchronization = (TextView) findViewById(R.id.txtSynchronization);
        txtHelp = (TextView) findViewById(R.id.txtHelp);
        txtSettings = (TextView) findViewById(R.id.txtSettings);
        txtExit = (TextView) findViewById(R.id.txtExit);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        txtEdit = (TextView) findViewById(R.id.txtEditOption);
        txtPWBaseline = findViewById(R.id.txtPWBaseline);
        txtadolscentBaseline = findViewById(R.id.txtadolscentBaseline);
        //tvAwcAndMemDetails = (TextView) findViewById(R.id.tvAwcAndMemDetails);
        //txtEventsOrMeetings = findViewById(R.id.txtEventsOrMeetings);
        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        tv_end_time1=findViewById(R.id.tv_end_time1);
        tv_start_time1=findViewById(R.id.tv_start_time1);

        lnrEdit = findViewById(R.id.lnrEdit);
        lnrView = findViewById(R.id.lnrEdit);
        lnrSetting= findViewById(R.id.lnrEdit);
    }

    public void click_registration(View vw) {
        Intent intent = new Intent(this, MainMenuRegistrationActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrRegistration:
                Intent intent = new Intent(this, MainMenuRegistrationActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent, bndlanimation);
                break;
            case R.id.lnrMonitoring:

                Intent intentListing = new Intent(this, MainMenuMonitoringActivity.class);
                Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentListing, bndlanimation1);
                break;

//            case R.id.lnrEventsOrMeetings:
//                Intent intentList = new Intent(this, EventMeetingActivity.class);
//                Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
//                        .toBundle();
//                startActivity(intentList, bndlanimation4);
//                break;
            case R.id.lnrSync:
                Intent intentInt = new Intent(this, SyncActivity.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intentInt, bndlanimation2);
                break;

            case R.id.lnrHelp:
                Intent intent2 = new Intent(this, ActivityHelp.class);
                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(intent2, bndlanimation3);

            case R.id.lnrUtilities:
                Intent i = new Intent(this, MainMenuUtilityActivity.class);
                Bundle bndlanimation5 = ActivityOptions.makeCustomAnimation(this, R.anim.animation1, R.anim.animation2)
                        .toBundle();
                startActivity(i,bndlanimation5);

                break;
            case R.id.lnrSetting:
                Intent intent3 = new Intent(this, ActivitySettings.class);
                startActivity(intent3);
                break;
//            case R.id.lnrEdit:
//                Intent intentEdit = new Intent(this, EditMenu.class);
//                startActivity(intentEdit);
//                break;

            case R.id.lnrClose:
                setouttime();
                uploadAttendance();
                Intent intent10 = new Intent(this, ActivityLogin.class);
                startActivity(intent10);
                finish();
                break;

            case R.id.lnrEditOption:
                Intent editInt = new Intent(this, ActivityEdit.class);
                startActivity(editInt);
                break;

           /* case R.id.lnrAwc:
                Intent editInt1 = new Intent(this, awc_main_menu.class);
                startActivity(editInt1);
                break;*/
            case R.id.lnrAdolBaseline:
                //  setPreRightVaDialog();
                Intent editInt1 = new Intent(this, ActivityBMI_Calculator.class);
                startActivity(editInt1);

                break;
            case R.id.lnrPWBaseline:
                Intent editInt2 = new Intent(this, PregnantWomenBaseline.class);
                startActivity(editInt2);
                break;

            case R.id.lnrSAMCalculator:
                Intent intent14 = new Intent(this, ActivitySAM_Calculator.class);
                startActivity(intent14);
                break;
            case R.id.lnrViews:
                Intent intent15 = new Intent(this, ViewMonitoringDetails.class);
                startActivity(intent15);
                break;
            case R.id.lnrUnderWeightCal:
                Intent intent16 = new Intent(this, ActivityUnderWeight_Calculator.class);
                startActivity(intent16);
                break;

            default:
                break;
        }
    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strMainMenu + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setouttime();
                uploadAttendance();
                Intent intent = new Intent(MainMenuActivity.this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setouttime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        sqliteHelper.setoutUserDateTime(df.format(cal.getTime()));
    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
            new sync_sevika_helper(this).execute("");
        }
    }

//    //download house hold
//    @SuppressLint("NewApi")
//    public String DownloadEventMeetingData(String created_on) {
////        String lastResult="";
//        DownloadData downloadData = new DownloadData();
//        downloadData.setCreated_on(created_on);
//        Gson gson = new Gson();
//        String data = gson.toJson(downloadData);
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(JSON, data);
//
//        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
//        Call<JsonArray> call = apiService.callDownloadEventMeetingApi(body);
//        call.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                try {
//                    JSONArray jsonArray=new JSONArray(response.body().toString());
//                    Log.e("downloadHH>>>", "onResponse events >>>"+jsonArray.toString());
//                    sqliteHelper.dropTable("events");
//                    SyncActivity.pbEveMee.setMax(jsonArray.length());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
//
//                        Iterator keys = jsonObject.keys();
//                        ContentValues contentValues = new ContentValues();
//                        while (keys.hasNext()) {
//                            String currentDynamicKey = (String) keys.next();
//                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
//                        }
//
//                        long eventMeetingId=sqliteHelper.saveMasterTable(contentValues, "events");
////                        SyncActivity.pbEveMee.setProgress(i + 1);
////                        if (eventMeetingId > 0) {
////                            eventID++;
////                            streventMeetingId = eventID + "Events";
////                        }
//                    }
//                } catch (Exception s) {
//                    s.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//                Log.d("", "");
//            }
//        });
////        lastResult = streventMeetingId;
////        return lastResult;
//
//    }
}
