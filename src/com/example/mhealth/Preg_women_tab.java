package com.example.mhealth;


import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.mhealth.helper.SqliteHelper;

public class Preg_women_tab extends TabActivity {

    SqliteHelper sqlitehelper;
    TabHost tabHost;
    TabSpec NewPregTab, OldPregTab;
    TextView tvTitleText;
    static ImageView ivTitleBack;
    private Context context=this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preg_women_tab);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);

        NewPregTab = tabHost.newTabSpec("NewPregTab");
        OldPregTab = tabHost.newTabSpec("OldPregTab");

        NewPregTab.setIndicator(getString(R.string.add_new_pregnant_women));
        NewPregTab.setContent(new Intent(this, ActivityPregnantWomenRegistration.class));

        OldPregTab.setIndicator(getString(R.string.select_from_existing_mother));
        OldPregTab.setContent(new Intent(this, Activity_Exiting_Mother.class));

        tabHost.addTab(NewPregTab);
        tabHost.addTab(OldPregTab);

        tvTitleText.setText(R.string.pregnant_women_registration);
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(getString(R.string.pregnant_women_registration_exit)+"?");

                builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(
                                context,
                                MainMenuRegistrationActivity.class);
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
}
