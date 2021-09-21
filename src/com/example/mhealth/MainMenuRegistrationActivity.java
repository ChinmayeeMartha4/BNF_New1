package com.example.mhealth;

import static android.content.Context.LOCATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.example.mhealth.NewCode.NutritionChampionsRegistration;
import com.example.mhealth.NewCode.SuposhanSakhiRegistration;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Locale;

public class MainMenuRegistrationActivity extends Activity {

    public boolean foundGPSLocation = false;
    SqliteHelper sqliteHelper;
    SharedPrefHelper sph;
    TextView txtChild, txtPregmentWomen, txtEligFamily, txtAdolescent,
            txtFooter, btnBackToMainMenu, txtViews, txtMother,tvTitleText;
    String strCancel, strYes, strNo, strChildReg, strPregWomReg, strAdlReg,
            strFooter, strBackToMainMenu, strEligFamily, strReg, strViews, strMother;
    LocationManager locationManager;
    long minTime = 1000;
    float minDistance = 1;
    String tag = "";
    MyLocationListener mylistener;
    ImageView ivTitleBack;
    private Context context=this;
    LinearLayout lnrNutrition,lnrSuposhanSakhi;

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
        setContentView(R.layout.activity_main_menu_registration);
        /*Tracker t = ((GoogleAnalyticsHelper) this.getApplication()).getTracker(GoogleAnalyticsHelper.TrackerName.APP_TRACKER);
        t.setScreenName(GlobalVars.username + "-> Registration");
        t.send(new HitBuilders.EventBuilder().setCategory("Button").setAction("Click").setLabel("CoolButton").build());*/

        initialize();
        uploadAttendance();
        String languageId = sph.getString("Language", "1");// getting languageId

        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel,
                languageId);
        strBackToMainMenu = sqliteHelper.LanguageChanges(ConstantValue.LANMM,
                languageId);
        strFooter = sqliteHelper.LanguageChanges(ConstantValue.LANTPIC,
                languageId);
        strChildReg = sqliteHelper.LanguageChanges(ConstantValue.LANChild,
                languageId);
        strPregWomReg = sqliteHelper.LanguageChanges(
                ConstantValue.LANPregWomen, languageId);
        strAdlReg = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescent,
                languageId);
        strEligFamily = sqliteHelper.LanguageChanges(ConstantValue.LANElFamily,
                languageId);
        strReg = sqliteHelper.LanguageChanges(ConstantValue.LANReg, languageId);
        strViews = sqliteHelper.LanguageChanges(ConstantValue.LANViews, languageId);
        strMother = sqliteHelper.LanguageChanges(ConstantValue.LANMother, languageId);

        txtChild.setText(strChildReg);
        txtPregmentWomen.setText(strPregWomReg);
        txtEligFamily.setText(R.string.household);
        txtAdolescent.setText(R.string.adolescent_girl);
        btnBackToMainMenu.setText(R.string.back_main_menu);
        txtViews.setText(strViews);
        txtMother.setText(strMother);
        tvTitleText.setText(R.string.registration);

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
                // builder.setTitle("Information");
                //String str = "abcd=0; efgh=1";

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
                                        MainMenuRegistrationActivity.this,
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
        lnrSuposhanSakhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuRegistrationActivity.this, SuposhanSakhiRegistration.class);
                startActivity(intent);
            }
        });
        lnrNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuRegistrationActivity.this, NutritionChampionsRegistration.class);
                startActivity(intent);
            }
        });


        String lngTypt = sph.getString("languageID", "en");
        sph.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }
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


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        try {
            startGettingLocationUsingGPSProvider();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void initialize() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mylistener = new MyLocationListener();

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);

        txtChild = (TextView) findViewById(R.id.txtChild);
        txtPregmentWomen = (TextView) findViewById(R.id.txtPregmentWomen);
        txtEligFamily = (TextView) findViewById(R.id.txtEligFamily);
        btnBackToMainMenu = (TextView) findViewById(R.id.btnBackToMain);
        txtAdolescent = (TextView) findViewById(R.id.txtAdolescent);
        txtFooter = (TextView) findViewById(R.id.txtFooter);
        txtViews = (TextView) findViewById(R.id.txtViews);
        txtMother = (TextView) findViewById(R.id.txtMother);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        lnrSuposhanSakhi=findViewById(R.id.lnrSuposhanSakhi);
        lnrNutrition=findViewById(R.id.lnrNutrition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_registration, menu);
        return true;
    }

    public void click_child(View vw) {
        Intent intent = new Intent(this, ActivityEligibleFamilyRegistration.class);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void click_list(View vw) {
        switch (vw.getId()) {
            case R.id.lnrChild:
                /*
                 * Intent intent = new Intent(this, Activity_childListing.class);
                 * startActivity(intent);
                 */

                Intent intent1 = new Intent(this, ActivityChildReg.class);
                Bundle bndlanimation1 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent1, bndlanimation1);

                break;
            case R.id.lnrPregnantWomen:
                /*
                 * Intent intentListing = new Intent(this,
                 * Activity_womenListing.class); startActivity(intentListing);
                 */

                Intent intent2 = new Intent(this,
                        Preg_women_tab.class);
                Bundle bndlanimation2 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent2, bndlanimation2);

                break;
            case R.id.lnrParent:
                Intent intent3 = new Intent(this, ActivityEligibleFamilyRegistration.class);
                Bundle bndlanimation3 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent3, bndlanimation3);

                /*
                 * Intent intentInt = new Intent(this, Activity_parentList.class);
                 * startActivity(intentInt);
                 */

                break;
            case R.id.lnrAdolescent:

                Intent intent4 = new Intent(this, ActivityAdolescentGirlReg.class);
                Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent4, bndlanimation4);

                // Intent intent2 = new Intent(this, MainMenuActivity.class);
                // Bundle bndlanimation4 = ActivityOptions.makeCustomAnimation(this,
                // R.anim.animation1, R.anim.animation2)
                // .toBundle();
                // startActivity(intent2, bndlanimation4);
                break;
            case R.id.lnrMother:
                Intent intent5 = new Intent(this,
                        ActivityMotherRegistration.class);
                Bundle bndlanimation5 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent5, bndlanimation5);

                /*
                 * Intent intentInt = new Intent(this, Activity_parentList.class);
                 * startActivity(intentInt);
                 */

                break;
            case R.id.lnrBackToMenu:

                Intent intent6 = new Intent(this, MainMenuActivity.class);
                Bundle bndlanimation6 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent6, bndlanimation6);

                break;

            case R.id.lnrViews:

                GlobalVars.EFposition = 1;
                Intent intent7 = new Intent(this, ActivityViews.class);
                intent7.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bndlanimation7 = ActivityOptions.makeCustomAnimation(this,
                        R.anim.animation1, R.anim.animation2).toBundle();
                startActivity(intent7, bndlanimation7);


                break;

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        //String str = "abcd=0; efgh=1";

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
                                MainMenuRegistrationActivity.this,
                                MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void startGettingLocationUsingGPSProvider() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime, minDistance, mylistener);
        CountDownTimer count = new CountDownTimer(5000, 1) {
            @Override
            public void onFinish() {

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

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
            new sync_sevika_helper(this).execute("");
        }
    }

    class MyLocationListener implements LocationListener {
        String provider_tag = "";

        @Override
        public void onLocationChanged(Location location) {
            String sss = location.getLongitude() + "";
            if (sss.length() > 10) {
                /*
                 * txtLat.setText(strLat + " : " + location.getLatitude());
                 * txtLang.setText(strLang + " : " + location.getLongitude());
                 * txtType.setText(strType + " : " + strGPS);
                 */
            } else {
                /*
                 * txtLat.setText(strLat + " : " + location.getLatitude());
                 * txtLang.setText(strLang + " : " + location.getLongitude());
                 * txtType.setText(strType + " : " + strAGPS);
                 */
            }

            GlobalVars.lattitude = location.getLatitude() + "";
            GlobalVars.longitude = location.getLongitude() + "";

            Toast t = Toast.makeText(getApplicationContext(),
                    GlobalVars.lattitude + ", " + GlobalVars.longitude, Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP, 0, 0);
            //t.show();

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
                /*
                 * Notify("a", "aa", "gps");
                 * locationManager.requestLocationUpdates(
                 * LocationManager.GPS_PROVIDER, 10, 1, mylistener); tag =
                 * strAGPS;
                 */
            }
        }
    }

}
