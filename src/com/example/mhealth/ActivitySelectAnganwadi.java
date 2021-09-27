package com.example.mhealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mhealth.app_drawer.AppDrawer;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.attendance;
import com.example.mhealth.model.AttendanceImagePojo;
import com.example.mhealth.model.LoginModel;
import com.example.mhealth.rest_apis.ApiClient;
import com.example.mhealth.rest_apis.M_Health_API;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySelectAnganwadi extends Activity {

    SqliteHelper sqliteHelper;
    ServerHelper serverHelper;
    SharedPrefHelper sph;
    Spinner spnAnganwadi;
    Integer anganwadi_id;
    Button btnLoginGo;

    TextView txtFooter, txtAnganwadi, txtWelcomeMsg,tvTitleText;
    String strFooter, strSelAng, strGo, strPleaseWait,strCancel, strYes, strNo, strFullName;
    int user_master_id;
    private ProgressDialog mProgressDialog;
    private ImageView ivDownloadAnganwadi;
    public static android.app.Dialog submit_alert;
    private Context context=this;
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + "Anganwadi" + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ActivitySelectAnganwadi.this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);

        attendanceImagePojo=new AttendanceImagePojo();

        spnAnganwadi = (Spinner) findViewById(R.id.spnAnganwadi);
        btnLoginGo = (Button) findViewById(R.id.btnLoginGo);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        txtAnganwadi = (TextView) findViewById(R.id.txtAnganwadi);
        txtWelcomeMsg = (TextView) findViewById(R.id.txtWelcomeMsg);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivDownloadAnganwadi=findViewById(R.id.ivDownloadAnganwadi);
        sqliteHelper = new SqliteHelper(this);
        serverHelper = new ServerHelper(this);
        sph = new SharedPrefHelper(this);



        String languageId = sph.getString("Language", "1");// getting languageId


        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguages("en");
        } else if (lngTypt.equals("2")) {
            setLanguages("hi");
        }


        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                languageId);
        strSelAng = sqliteHelper.LanguageChanges(ConstantValue.LANSelAng,
                languageId);
        strGo = sqliteHelper.LanguageChanges(ConstantValue.LANGo,
                languageId).trim();
        strPleaseWait = sqliteHelper.LanguageChanges(
                ConstantValue.LANPleaseWait, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        user_master_id = sph.getInt("user_master_id", 0);

        strFullName = sqliteHelper.getCloumnName("full_name", "user_master", "where user_master_id=" + user_master_id);

        tvTitleText.setText(R.string.select_anganwadi);
        txtWelcomeMsg.setText(getString(R.string.welcome)+ " " +strFullName);
        txtAnganwadi.setText(R.string.select_anganwadi);
        btnLoginGo.setText(strGo);


        String text = "<a href='http://indevjobs.org'>" + strFooter + "</a>";
        Spannable spannedText = Spannable.Factory.getInstance().newSpannable(
                Html.fromHtml(text));
        Spannable processedText = removeUnderlines(spannedText);
        txtFooter.setText(processedText);
        txtFooter.setLinkTextColor(Color.BLACK);
        txtFooter.setClickable(true);
        txtFooter.setMovementMethod(LinkMovementMethod.getInstance());

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage(strPleaseWait + "!!! ");
        mProgressDialog.setCanceledOnTouchOutside(false);




        populateList(spnAnganwadi, "anganwadi_center as a", "a.center_id", "a.center_name",
                getString(R.string.select_anganwadi), "where a.user_master_id=" + user_master_id);

        if (spnAnganwadi.getSelectedItemPosition() == -1) {
            getName();

        }

        //download anganwadi by download icon on toolbar
        ivDownloadAnganwadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getName();
                sph.setString("download","1");
            }
        });


        /*Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Select Anganwadi page");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());*/
    }



    public void getName() {
        mProgressDialog.show();
        /*new AsyncTask<String, String, String>() {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                populateList(spnAnganwadi, "anganwadi_center as a, beat as b", "a.center_id", "a.center_name, b.beat_name",
                        "Select Anganwadi", "where a.beat_id = b.beat_id and a.user_master_id=" + user);

                if (result.equals("1")) {
                }

                mProgressDialog.dismiss();
            }

            @Override
            protected String doInBackground(String... arg0) {

                String result = serverHelper.getAnganwadiNames();
                String result2 = serverHelper.beatDownload();

                return result;
            }

        }.execute((String) null);*/
        LoginModel loginModel=new LoginModel();
        loginModel.setUser_master_id(String.valueOf(user_master_id));
        loginModel.setCreated_on("");

        Gson gson = new Gson();
        String data = gson.toJson(loginModel);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        ApiClient.getClient().create(M_Health_API.class).callAnganwadiCenterApi(body).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadAnganwadi>>>", "onResponse>>> "+jsonArray.toString());
                    sqliteHelper.dropTable("anganwadi_center");
                    mProgressDialog.dismiss();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject family = jsonArray.getJSONObject(i);
                        {
                            String[] ASDF = new String[66];
                            ASDF[0] = family.getString("id");
                            ASDF[1] = family.getString("anganwadi_center_name");
                            ASDF[2] = family.getString("anganwadi_type");
                            ASDF[3] = family.getString("status");
                            ASDF[4] = family.getString("latitude");
                            ASDF[5] = family.getString("longitude");
                            ASDF[6] = family.getString("user_name");
                            ASDF[7] = family.getString("mobile_number");
                            ASDF[8] = family.getString("email_id");
                            ASDF[9] = family.getString("date_of_creation");
                            ASDF[10] = family.getString("last_login");
                            ASDF[11] = family.getString("password");
                            ASDF[12] = family.getString("sevika_name");
                            ASDF[13] = family.getString("sevika_photo");
                            ASDF[14] = family.getString("sevika_mobileno");
                            ASDF[15] = family.getString("sevika_date_of_birth");
                            ASDF[16] = family.getString("sevika_date_of_joining");
                            ASDF[17] = family.getString("sevika_education");
                            ASDF[18] = family.getString("sevika_service_periond");
                            ASDF[19] = family.getString("helper_name");
                            ASDF[20] = family.getString("helper_photo");
                            ASDF[21] = family.getString("helper_mobileno");
                            ASDF[22] = family.getString("helper_date_of_birth");
                            ASDF[23] = family.getString("helper_date_of_joining");
                            ASDF[24] = family.getString("helper_education");
                            ASDF[25] = family.getString("helper_service_period");
                            ASDF[26] = family.getString("beat_id");
                            ASDF[27] = family.getString("project_id");
                            ASDF[28] = family.getString("taluka_id");
                            ASDF[29] = family.getString("district_id");
                            ASDF[30] = family.getString("state_id");
                            ASDF[31] = family.getString("block_id");
                            ASDF[32] = family.getString("village_id");
                            ASDF[33] = family.getString("anganwadi_photo1");
                            ASDF[34] = family.getString("anganwadi_photo2");
                            ASDF[35] = family.getString("anganwadi_photo3");
                            ASDF[36] = family.getString("ref_id");
                            ASDF[37] = family.getString("awc_type");
                            ASDF[38] = family.getString("building_status");
                            ASDF[39] = family.getString("building_type");
                            ASDF[40] = family.getString("water_source");
                            ASDF[41] = family.getString("water_safety");
                            ASDF[42] = family.getString("toilet_availability");
                            ASDF[43] = family.getString("water_availability");
                            ASDF[44] = family.getString("hand_washing_facility");
                            ASDF[45] = family.getString("learning_teaching");
                            ASDF[46] = family.getString("equipment");
                            ASDF[47] = family.getString("anganwadi_photo4");
                            ASDF[48] = family.getString("awc_sevika_position");
                            ASDF[49] = family.getString("temporary_incharge");
                            ASDF[50] = family.getString("which_awc");
                            ASDF[51] = family.getString("sevika_cast");
                            ASDF[52] = family.getString("sevika_religion");
                            ASDF[53] = family.getString("sevika_training");
                            ASDF[54] = family.getString("sevika_residence");
                            ASDF[55] = family.getString("sevika_distance_from_awc");
                            ASDF[56] = family.getString("sevika_alternate_mobileno");
                            ASDF[57] = family.getString("sevika_passport_photo");
                            ASDF[58] = family.getString("helper_training");
                            ASDF[59] = family.getString("helper_residence");
                            ASDF[60] = family.getString("helper_alternate_mobileno");
                            ASDF[61] = family.getString("helper_passport_photo");
                            ASDF[62] = family.getString("sevika_cast_category");
                            ASDF[63] = family.getString("helper_awc_distance");
                            ASDF[64] = family.getString("village_population");
                            ASDF[65] = family.getString("no_of_household");
                            long downAnganwadi = sqliteHelper.AnganwadiDownload(ASDF);
                            if (downAnganwadi > 0) {
                                sph.setString("state_id",ASDF[30]);
                                sph.setString("district_id",ASDF[29]);
                                sph.setString("block_id",ASDF[31]);
                                sph.setString("village_id",ASDF[32]);

                            }


                        }
                    }
                    populateList(spnAnganwadi, "anganwadi_center as a", "a.center_id", "a.center_name",
                            getString(R.string.select_anganwadi), "where a.user_master_id=" + user_master_id);
                    if(sph.getString("download","").equals("1")){
                        showSubmitDialog(context, getString(R.string.anganwadi),
                                getString(R.string.anganwadi_updated));
                    }else{
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(ActivitySelectAnganwadi.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }


    private void setLanguages(String languageToLoad) {
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


    public static void showSubmitDialog(Context context, String infoTitle, String message) {
        submit_alert = new android.app.Dialog(context);

        submit_alert.setContentView(R.layout.submit_alert_dialog);
        submit_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = submit_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvInfoTitle = (TextView) submit_alert.findViewById(R.id.tv_info_title);
        TextView tvDescription = (TextView) submit_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) submit_alert.findViewById(R.id.btnOk);

        tvInfoTitle.setText(infoTitle);
        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                submit_alert.dismiss();
            }
        });

        submit_alert.show();
        submit_alert.setCanceledOnTouchOutside(false);
    }


    public void click_Go(View v) {

        anganwadi_id = Integer.parseInt(getSelectedValue(spnAnganwadi));
        setUserInTiming(Integer.toString(user_master_id), Integer.toString(anganwadi_id));
        sph.setInt("user_id", anganwadi_id);
        attendanceImagePojo=sqliteHelper.getAttendanceImageData();
        String id= String.valueOf(attendanceImagePojo.getId());
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("Id",id);
        startActivity(intent);
        this.finish();
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn // spinner
                // class
                // method....always
                // use
                .getSelectedItemId());
        return data.getValue();
    }

    public void populateList(Spinner spinner, String tableName, String col_id,
                             String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        items = sqliteHelper.populateSelectAwcSpinner(tableName, col_id, col_value,
                label, whr);
        Log.e("items", "populateList: "+ items );
        ArrayAdapter<SpinnerHelper> adapter = new ArrayAdapter<SpinnerHelper>(
                ActivitySelectAnganwadi.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_new_login, menu);
        return true;
    }

    public void setUserInTiming(String uid, String awid) {
        attendance att = new attendance();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cdatetime = df.format(cal.getTime());
        att.setUserID(uid);
        att.setAnganwadiID(awid);
        att.setDate(cdatetime.substring(0, 10));
        att.setinTime(cdatetime.substring(10, 19));
        sqliteHelper.setInUserDateTime(att);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
