package com.example.mhealth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;

public class Activity_Sync extends Activity {

    TextView Sync_sub1;
    ImageView sync, sync_marathi;
    String strHeading1, strSub1, strNo, strYes, strCancel, strSync;
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
        setContentView(R.layout.activity_sync);
        initialize();
        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        String languageId = sph.getString("Language", "1");// getting languageId
        if (languageId.equalsIgnoreCase("1")) {
            sync.setVisibility(View.VISIBLE);
        } else {
            sync_marathi.setVisibility(View.VISIBLE);
        }
        strSub1 = sqliteHelper.LanguageChanges(ConstantValue.LANSync_Sub1, languageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, languageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, languageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, languageId);
        strSync = sqliteHelper.LanguageChanges(ConstantValue.LANSynchronization, languageId);

        //SETTING TEXT
        Sync_sub1.setText(strSub1);
        tvTitleText.setText("Synchronization");
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strSync + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Activity_Sync.this, ActivityHelp.class);
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

        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);
        Sync_sub1 = (TextView) findViewById(R.id.sub_Sync1);
        sync = (ImageView) findViewById(R.id.imageView_sync);
        sync_marathi = (ImageView) findViewById(R.id.imageView_sync_mar);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strSync + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Activity_Sync.this, ActivityHelp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
