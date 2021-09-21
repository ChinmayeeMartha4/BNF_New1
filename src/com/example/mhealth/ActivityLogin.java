package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import androidx.core.app.NotificationCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.model.LoginModel;
import com.example.mhealth.rest_apis.ApiClient;
import com.example.mhealth.rest_apis.M_Health_API;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NewApi")
public class ActivityLogin extends Activity {
    public boolean foundGPSLocation = false;
    EditText etxtUserName, extxPassword;
    TextView txtLogin, txtUser, txtPass, txtLat, txtLang, txtType,
            txtFooter;
    String strUserName="", strPassword="", strFooter, strEnterUserPass,
            strPleaseWait, strAuthOnline, strEnableInternet, strNoValidUser,
            strGPSEnable, strGPSSetting, strConnectingAGPS, strYes, strNo,
            strAGPS, strType, strGPS, strLat="", strLang="", strOnline;
    SharedPrefHelper sph;
    CheckBox cbOnline;
    LocationManager locationManager;
    long minTime = 1000;
    float minDistance = 1;
    String tag = "";
    MyLocationListener mylistener;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    private int flag = 0;
    private ProgressDialog mProgressDialog;

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
        setContentView(R.layout.activity_login);
        initialize();
        startGPS();
        txtLogin.setText("\t \tNutrition \n \t \tMonitoring System");
        //not used
        //enableGPS();

        Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName("-> " + "SBA LOGIN");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskWrites().penaltyLog().build());

        try {
            startGettingLocationUsingGPSProvider();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkboxClicked(View v) {
        if (((CheckBox) v).isChecked()) {

            flag = 1;
        } else {

            flag = 0;
        }
    }

    public void initialize() {
        sqliteHelper = new SqliteHelper(this);
        serverhelper = new ServerHelper(this);
        sph = new SharedPrefHelper(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mylistener = new MyLocationListener();

        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtPass = (TextView) findViewById(R.id.txtPass);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLang = (TextView) findViewById(R.id.txtLang);
        txtType = (TextView) findViewById(R.id.txtType);
        etxtUserName = (EditText) findViewById(R.id.etxtUserName);
        extxPassword = (EditText) findViewById(R.id.extxPassword);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        cbOnline = (CheckBox) findViewById(R.id.cbOnline);

        String languageId = sph.getString("Language", "");// getting languageId

        String AppName = sqliteHelper.LanguageChanges(ConstantValue.LANAppName, languageId);
        String User = sqliteHelper.LanguageChanges(ConstantValue.LANUserName, languageId);
        String Pass = sqliteHelper.LanguageChanges(ConstantValue.LANPassword, languageId);
        strLat = sqliteHelper.LanguageChanges(ConstantValue.LANLat, languageId);
        strLang = sqliteHelper.LanguageChanges(ConstantValue.LANLong, languageId);
        strType = sqliteHelper.LanguageChanges(ConstantValue.LANType, languageId);
        String Login = sqliteHelper.LanguageChanges(ConstantValue.LANLogin, languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC, languageId);
        strEnterUserPass = sqliteHelper.LanguageChanges(
                ConstantValue.LANEnterUserPass, languageId);
        strPleaseWait = sqliteHelper.LanguageChanges(
                ConstantValue.LANPleaseWait, languageId);
        strAuthOnline = sqliteHelper.LanguageChanges(
                ConstantValue.LANAuthOnline, languageId);
        strEnableInternet = sqliteHelper.LanguageChanges(
                ConstantValue.LANEnableInternet, languageId);
        strNoValidUser = sqliteHelper.LanguageChanges(
                ConstantValue.LANNovalidUser, languageId);
        strGPSEnable = sqliteHelper.LanguageChanges(ConstantValue.LANGPSEnable,
                languageId);
        strGPSSetting = sqliteHelper.LanguageChanges(
                ConstantValue.LANGPSSetting, languageId);
        strConnectingAGPS = sqliteHelper.LanguageChanges(
                ConstantValue.LANConnectAGPS, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strAGPS = sqliteHelper.LanguageChanges(ConstantValue.LANAGPS,
                languageId);
        strGPS = sqliteHelper.LanguageChanges(ConstantValue.LANGPSTag,
                languageId);
        strOnline = sqliteHelper.LanguageChanges(ConstantValue.LANOnline,
                languageId);

        btnLogin.setText(Login);
        txtUser.setText(User);
        txtPass.setText(Pass);
        txtLat.setText(strLat);
        txtLang.setText(strLang);
        txtType.setText(strType);
        txtLogin.setText(AppName);
        cbOnline.setText(strOnline);

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
    }

    private void startGPS() {
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            showSettingsAlert();
        }

    }

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public void click_login(View vw) {
        strUserName = etxtUserName.getText().toString().trim();
        strPassword = extxPassword.getText().toString().trim();

        if (strUserName.equalsIgnoreCase("") || (strPassword.equalsIgnoreCase(""))) {
            Toast.makeText(getApplicationContext(), strEnterUserPass, Toast.LENGTH_LONG).show();
        }
        else {

            if (sqliteHelper.validateUserOffline2(strUserName, strPassword) > 0) {
                int user_master_id = sqliteHelper.validateUserOffline2(strUserName, strPassword);
                sph.setInt("user_master_id", user_master_id);
                GlobalVars.username = strUserName;
                Intent intent = new Intent(this, ActivitySelectAnganwadi.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(getApplicationContext(), "Authenticating online" + "!!!", Toast.LENGTH_LONG).show();
                if (isInternetOn() == false) {
                    Toast.makeText(getApplicationContext(), strEnableInternet + "!!!", Toast.LENGTH_LONG).show();
                }
                else {
                    mProgressDialog.show();
                    /*new AsyncTask<String, String, String>() {

                        @Override
                        protected void onPostExecute(String result) {
                            // TODO Auto-generated method stub
                            super.onPostExecute(result);

                            if (result != null) {
                                try {
                                    JSONObject user = new JSONObject(result);
                                    if (user.has("user_master_id")) {
                                        int user_master_id = user.getInt("user_master_id");
                                        if (user_master_id > 0) {

                                            sqliteHelper.SaveUser(strUserName,strPassword, user_master_id + "");

                                            sph.setInt("user_master_id", user_master_id);
                                            //sph.setInt("user_id", 30);
                                            GlobalVars.username = strUserName;
                                            Intent intent = new Intent(ActivityLogin.this, ActivitySelectAnganwadi.class);
                                            startActivity(intent);

                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), strNoValidUser, 200).show();
                                    }
                                    mProgressDialog.dismiss();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        protected String doInBackground(String... arg0) {
                            // TODO Auto-generated method stub
                            return serverhelper.LoginOnServer(strUserName, strPassword);
                        }
                    }.execute((String) null);*/
                    LoginModel loginModel=new LoginModel();
                    loginModel.setUser_name(strUserName);
                    loginModel.setPassword(strPassword);

                    Gson gson = new Gson();
                    String data = gson.toJson(loginModel);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);
                    ApiClient.getClient().create(M_Health_API.class).loginApi(body).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                Log.e("LoginApi>>>", "onResponse>>> "+jsonObject);
                                String success=jsonObject.getString("success");
                                if (success.equals("1")) {
                                    String user_id=jsonObject.getString("user_id");
                                    String user_name=jsonObject.getString("user_name");
                                    String full_user_name=jsonObject.getString("full_user_name");
                                    sqliteHelper.SaveUser(strUserName, strPassword, user_id + "", full_user_name);
                                    sph.setInt("user_master_id", Integer.parseInt(user_id));
                                    sph.setString("user_name", user_name);
                                    sph.setString("full_user_name", full_user_name);
                                    GlobalVars.username = strUserName;
                                    Intent intent = new Intent(ActivityLogin.this, ActivitySelectAnganwadi.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), strNoValidUser, Toast.LENGTH_LONG).show();
                                    mProgressDialog.dismiss();
                                }
                                mProgressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(ActivityLogin.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            }
        }
    }

    String getProviderName() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired
        // power consumption
        // level.
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy
        // requirement.
        criteria.setSpeedRequired(true); // Chose if speed for first location
        // fix is required.
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);


        return locationManager.getBestProvider(criteria, true);
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle(strGPSSetting);
        // Setting Dialog Message
        alertDialog.setMessage(strGPSEnable + "?");
        // On pressing Settings button
        alertDialog.setPositiveButton(strYes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent gpsOptionsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(strNo,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public void Notify(String notificationTitle, String notificationMessage,
                       String path) {
        /*
         * Uri soundUri =
         * Uri.parse("android.resource://com.example.anganwarisupport/raw/"+
         * path);
         */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.ic_launcher) // notification icon
                .setContentTitle("Geo-Location Updates") // title for
                // notification
                .setContentText("Geo-Location Updates") // message for
                // notification
                .setAutoCancel(true);
        /* .setAutoCancel(true).setSound(soundUri); */
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public void startGettingLocationUsingGPSProvider() {
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime, minDistance, mylistener);
        CountDownTimer count = new CountDownTimer(5000, 1) {
            @Override
            public void onFinish() {

                tag = strAGPS;
                startGettingLocationUsingNetworkProvider();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
            }

        };
        count.start();

    }

    public void startGettingLocationUsingNetworkProvider() {
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, minTime, minDistance,
                    mylistener);
    }

    public boolean isGPSEnabled() {
        LocationManager mlocManager = (LocationManager) this
                .getSystemService(this.LOCATION_SERVICE);

        boolean enabled = mlocManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
    }

    public void enableGPS() {
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", true);
        sendBroadcast(intent);
    }

    public void disableGPS() {
        // Disable GPS
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        sendBroadcast(intent);
    }

    class MyLocationListener implements LocationListener {
        String provider_tag = "";

        @Override
        public void onLocationChanged(Location location) {
            String sss = location.getLongitude() + "";
            if (sss.length() > 10) {
                txtLat.setText(strLat + " : " + location.getLatitude());
                txtLang.setText(strLang + " : " + location.getLongitude());
                txtType.setText(strType + " : " + strGPS);
            } else {
                txtLat.setText(strLat + " : " + location.getLatitude());
                txtLang.setText(strLang + " : " + location.getLongitude());
                txtType.setText(strType + " : " + strAGPS);
            }

            GlobalVars.lattitude = location.getLatitude() + "";
            GlobalVars.longitude = location.getLongitude() + "";

            Toast GpsToast = Toast.makeText(getApplicationContext(),
                    GlobalVars.lattitude + ", " + GlobalVars.longitude, Toast.LENGTH_LONG);
            GpsToast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            //GpsToast.show();


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            if (provider.equalsIgnoreCase("gps")) {
                Notify("a", "aa", "gps");
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 10, 1, mylistener);
                tag = strAGPS;
            }
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}