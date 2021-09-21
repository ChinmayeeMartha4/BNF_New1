package com.example.mhealth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.FinalVars;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.utils.CommonMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.mhealth.helper.FinalVars.DATABASE_FILE_NAME;

@SuppressLint("NewApi")
public class
ActivitySplash extends Activity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final String TAG = "ActivitySplash";
    TextView txtAppPunchline, txtLabel, txtAppName, txtpunchLine;
    private Context context=this;
    Button btnNext;
    private SqliteHelper sqliteHelper;
    private SharedPrefHelper sharedPrefHelper;
    String langId="", language_id="";
    private String strTstDBCreate, strAppName, strNext, languageId, strDbError;
    public static String DB_PATH_ALT="data/data/com.example.mhealth/"+DATABASE_FILE_NAME; //<<<< ADDED
    Dialog change_language_alert;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static final int REQUEST = 112;
    //File dbFile = new File (DB_PATH_ALT);


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        sqliteHelper = new SqliteHelper(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        initialize();

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            //if (uiHelper.checkSelfPermissions(this)); //make pic image permission on splash
            String[] PERMISSIONS = {
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA
            };
            if (!hasPermissions(this, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions((Activity) this, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
//                callNextActivity();
                isDbExist();
                if(!sharedPrefHelper.getString("isChooseLanguage", "").equals("Yes")){
                    Change_Language();
                }
            }
        } else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
//            callNextActivity();
            isDbExist();
            if(sharedPrefHelper.getString("isChooseLanguage", "").equals("Yes")){
            }else{
                Change_Language();
            }
        }



//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//            if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            }
//            if (checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//            } else {
//                isDbExist();
//                if(sharedPrefHelper.getString("isChooseLanguage", "").equals("Yes")){
//                }else{
//                    Change_Language();
//                }
//            }
//        } else {
//            isDbExist();
//
//        }

        //isDbExist();
        /*if (languageId.equalsIgnoreCase("1")) {
            txtAppName.setText(Html.fromHtml("<font color='#ff0000'> mHealth -</font>"
                    + "" + "<font color='#ff0000'> Nutrition</font>" + ""
                    + "<font color='#ff0000'> Monitoring System</font>"
                    + "" + "<font color='#ff0000'> "));
        }

        else {

            txtAppName.setText(Html.fromHtml("<font color='#ff0000'> एमहेल्थ</font>"
                    + "" + "<font color='#ff0000'> पोषण</font>" + ""
                    + "<font color='#ff0000'> "));
        }
        if (languageId.equalsIgnoreCase("2")) {
            txtAppName.setText(Html.fromHtml("<font color='#ff0000'> एमहेल्थ</font>"
                    + "" + "<font color='#ff0000'> Nutrition</font>" + ""
                    + "<font color='#ff0000'> Monitoring System</font>"
                    + "" + "<font color='#ff0000'> "));
        } else {
        }*/




        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.side_slide);
        txtAppName.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.side_slide);
        txtpunchLine.startAnimation(animation2);
    }

    public void callNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    Intent intent = new Intent(ActivitySplash.this, ActivitySplash.class);
                    startActivity(intent);
                    finish();

            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    public void Change_Language() {
        change_language_alert = new Dialog(context);

        change_language_alert.setContentView(R.layout.changelanguage);
        change_language_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = change_language_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        RadioGroup rg_group = (RadioGroup) change_language_alert.findViewById(R.id.rg_group);


        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_eng:
                        sharedPrefHelper.setString("languageID", "1");
                        change_language_alert.dismiss();
                        sharedPrefHelper.setString("isChooseLanguage", "Yes");
                        isDbExist();
                        setRestartingAppToChangeLanguageDialog();
                        break;
                    case R.id.rb_hindi:
                        sharedPrefHelper.setString("languageID", "2");
                        change_language_alert.dismiss();
                        sharedPrefHelper.setString("isChooseLanguage", "Yes");
                        isDbExist();
                        setRestartingAppToChangeLanguageDialog();
                        break;
                }
            }
        });

        change_language_alert.show();
        change_language_alert.setCanceledOnTouchOutside(false);
    }

    private void setRestartingAppToChangeLanguageDialog() {

        isDbExist();
        Intent intent = new Intent(context, ActivitySplash.class);
        startActivity(intent);
        finish();
        /*new androidx.appcompat.app.AlertDialog.Builder(ActivitySplash.this, R.style.MyDialogTheme)
                .setMessage("App Language")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, ActivitySplash.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "@@@ PERMISSIONS grant");
//                    callNextActivity();
                    if(!sharedPrefHelper.getString("isChooseLanguage", "").equals("Yes")){
                        Change_Language();
                    }
                } else {
                    Log.d("TAG", "@@@ PERMISSIONS Denied");
                    Toast.makeText(this, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0) {
//
//
//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean finelocationAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                    boolean readAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
//
//                    if (locationAccepted && cameraAccepted && finelocationAccepted && readAccepted) {
//                        //Toast.makeText(this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
//                    } else {
//
//                        Toast.makeText(this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
//                                showMessageOKCancel("You need to allow access to both the permissions",
//                                        new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE},
//                                                            PERMISSION_REQUEST_CODE);
//                                                }
//                                            }
//                                        });
//                                return;
//                            }
//                        }
//
//                    }
//                }
//                break;
//        }
//    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivitySplash.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @SuppressLint("WrongConstant")
    public void isDbExist() {
        //File dir1 = new File(FinalVars.Anganwadi_DB);
      // DB_PATH_ALT = context.getDatabasePath(DATABASE_FILE_NAME).getPath();
        File dir2 = new File(FinalVars.Anganwadi_IMG);

        /*if (!dir1.exists()) {
            dir1.mkdirs();
        }
        if (!dir2.exists()) {
            dir2.mkdirs();
        }*/

        /*if (dir1.exists()) {
            File f_db = new File(FinalVars.Anganwadi_DB + FinalVars.DATABASE_FILE_NAME);
            if (!f_db.exists()) {
                try {
                    copyDataBase();
                    Toast.makeText(getApplicationContext(), strTstDBCreate, 200).show();
                } catch (IOException e) {
                    Log.d(strDbError, e.getMessage());
                }
            }

        }*/
        boolean dbExist = checkDataBaseAlt();
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            //this.getWritableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBaseAlt() {
        File chkdb = new File(DB_PATH_ALT); //<<<< ADDED
        return chkdb.exists();
    }

    public void initialize() {
        // txtAppDetail = (TextView) findViewById(R.id.txtAppDetail);
        txtAppPunchline = (TextView) findViewById(R.id.txtAppPunchline);
        txtpunchLine = (TextView) findViewById(R.id.txtpunchLine);
        txtLabel = (TextView) findViewById(R.id.txtLabel);
        txtAppName = (TextView) findViewById(R.id.txtAppName);
        btnNext = (Button) findViewById(R.id.btnNext);

        String languageId = sharedPrefHelper.getString("LanguageID", "");// getting languageId

//        String strAppPunchLine = sqliteHelper.LanguageChanges(ConstantValue.LANAppPunchLine, languageId);
//        String strAppContent1 = sqliteHelper.LanguageChanges(ConstantValue.LANAppDetailDesc, languageId);
//        String strAppContent2 = sqliteHelper.LanguageChanges(ConstantValue.LANAppDetailDes, languageId);
//        String strAppLabel = sqliteHelper.LanguageChanges(ConstantValue.LANAppLabel, languageId);

//        strTstDBCreate = sqliteHelper.LanguageChanges(ConstantValue.LANTstDBCreate, languageId);
//        strDbError = sqliteHelper.LanguageChanges(ConstantValue.LANDbError, languageId);
//        strAppName = sqliteHelper.LanguageChanges(ConstantValue.LANAppName, languageId);
//        strNext = sqliteHelper.LanguageChanges(ConstantValue.LANNext, languageId);

        //txtAppDetail.setText(strAppContent1 + "\n \n" + strAppContent2);
        //txtAppPunchline.setText(strAppPunchLine);
        txtLabel.setText("POWERED BY: INDEV CONSULTANCY PVT. LTD.");
        txtAppName.setText("Welcome to");
        txtpunchLine.setText("Nutrition Monitoring System");
        btnNext.setText("Next");

    }

    public void clickNext(View v) {
        String lngTypt = sharedPrefHelper.getString("languageID", "en");
        sharedPrefHelper.setString("Language", lngTypt);
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }
        Intent intent = new Intent(ActivitySplash.this, ActivityLogin.class);
        startActivity(intent);
        finish();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }


    public void copyDataBase() throws IOException {
        try {
            /*InputStream myInput = context.getAssets().open("mHealth.db");
            String outFileName = FinalVars.SDCARD + "/Anganwadi/data/" + "mHealth.db";
            Log.e(TAG, "outFileName>> "+outFileName);
            OutputStream myOutput = new FileOutputStream(outFileName);
            Log.e(TAG, "copyDataBase>>> "+myOutput.toString());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();*/
            //Open your local db as the input stream
            InputStream myInput = context.getAssets().open(DATABASE_FILE_NAME); //<<<< CHANGED

            // Path to the just created empty db
            //String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(DB_PATH_ALT); //<<<< CHANGED

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e(TAG, "copyDataBase>>> "+e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDbExist();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}