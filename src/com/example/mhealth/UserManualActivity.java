package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class UserManualActivity extends Activity {

    TextView txtLogin, txtMainMenu, txtRegistration, txtElgFamilyReg, txtPregWomenReg, txtChildReg,
            txtAdolescentReg, txtViews, txtChildListing, txtChildView, txtPregWomenListing, txtPregWomenView,
            txtAdlGirlListing, txtMonthlyMon, txtChildMon, txtPregMon, txtAdlMon, txtSynch, txtHelp, txtSetting, txtExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);


        initialization();
    }

    public void initialization() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_manual, menu);
        return true;
    }

}
