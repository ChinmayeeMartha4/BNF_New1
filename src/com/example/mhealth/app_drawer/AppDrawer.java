package com.example.mhealth.app_drawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.ActivityEligibleFamilyRegistration;
import com.example.mhealth.ActivityLogin;
import com.example.mhealth.ActivitySelectAnganwadi;
import com.example.mhealth.ActivitySplash;
import com.example.mhealth.MainMenuActivity;
import com.example.mhealth.R;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;

public class AppDrawer extends AppCompatActivity {
    public View view;
    public FrameLayout frame;
    public DrawerLayout drawer;
    public NavigationView navigationView;
    public Toolbar toolbar;
    public ActionBarDrawerToggle drawerToggle;
    public Menu menu;
    public Menu nav_menu;
    public TextView tv_name, tv_email;
    public ImageView iv_imageView;
    public TextView tvTitle;
    SharedPrefHelper sharedPrefHelper;
    private  Context context = this;
    ProgressDialog mProgressDialog;
    SqliteHelper sqliteHelper;
    android.app.Dialog change_language_alert;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_app_drawer, null);
        frame = view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);
        super.setContentView(view);

        drawer = findViewById(R.id.drawer);
        iv_imageView = findViewById(R.id.iv_imageView);
        navigationView = findViewById(R.id.navigationView);
        tvTitle = findViewById(R.id.tvTitle);

        sharedPrefHelper = new SharedPrefHelper(this);
        sqliteHelper = new SqliteHelper(this);
        mProgressDialog = new ProgressDialog(this);

        //make condition for admin and user
        navigationView.inflateMenu(R.menu.user_menu_list);
        menu = navigationView.getMenu();
        /*hide and show navigation items*/

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        View header = navigationView.getHeaderView(0);

        tv_name = (TextView) header.findViewById(R.id.tv_header_name);
        tv_email = (TextView) header.findViewById(R.id.tv_headerSubName);
        iv_imageView = (ImageView) header.findViewById(R.id.iv_imageView);

        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);//when using our custom drawer icon
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        //call method
        initializeView();

        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer);
                nav_menu = navigationView.getMenu();
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public void Change_Language() {
        change_language_alert = new android.app.Dialog(context);

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
                        setRestartingAppToChangeLanguageDialog();
                        break;
                    case R.id.rb_hindi:
                        sharedPrefHelper.setString("languageID", "2");
                        change_language_alert.dismiss();
                        sharedPrefHelper.setString("isChooseLanguage", "Yes");
                        setRestartingAppToChangeLanguageDialog();
                        break;
                }
            }
        });

        change_language_alert.show();
        change_language_alert.setCanceledOnTouchOutside(false);
    }

    //initialize view and set click on navigation item
    private void initializeView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawer.closeDrawers();
                int id = item.getItemId();
                switch (id) {
                    case R.id.option_home:
                    drawer.closeDrawers();
                    break;
                    case R.id.option_select_anganwadi:
                        String lngTypt = sharedPrefHelper.getString("languageID", "en");
                        sharedPrefHelper.setString("languageID", lngTypt);

                        Intent intentSelectAnganwadi=new Intent(context, ActivitySelectAnganwadi.class);
                        startActivity(intentSelectAnganwadi);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_logout:
                        Intent intentLogin=new Intent(context, ActivityLogin.class);
                        startActivity(intentLogin);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_change_language:
                         Change_Language();
                         break;
                }
                return true;
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



    private void setRestartingAppToChangeLanguageDialog() {
        new AlertDialog.Builder(AppDrawer.this, R.style.MyDialogTheme)
                .setMessage(getString(R.string.app_language) )
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(AppDrawer.this, ActivitySplash.class);
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
                .show();
    }

}
