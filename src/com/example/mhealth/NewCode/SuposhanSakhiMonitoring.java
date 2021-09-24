package com.example.mhealth.NewCode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mhealth.MainMenuMonitoringActivity;
import com.example.mhealth.R;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.SuposhanSakhiMonitoringPojo;

public class SuposhanSakhiMonitoring extends AppCompatActivity {
    RadioGroup rg_garden,rg_active;
    RadioButton rb_yes,rb_no,rb_active_yes,rb_active_no;
    TextView tv_suposhan_submit;
    private Context context = this;
    SqliteHelper sqliteHelper;
    String str_rb_garden,str_rb_active;
    SuposhanSakhiMonitoringPojo suposhanSakhiMonitoringPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suposhan_sakhi_monitoring);
        init();
//        tv_suposhan_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                    suposhanSakhiMonitoringPojo.setGarden_setup(str_rb_garden);
//                    suposhanSakhiMonitoringPojo.setActive(str_rb_active);
//
//                sqliteHelper.saveSuposhanSakhiMonitoringData(suposhanSakhiMonitoringPojo);
//
//                    Intent intent = new Intent(SuposhanSakhiMonitoring.this, MainMenuMonitoringActivity.class);
//                    startActivity(intent);
//
//            }
//        });

        rg_garden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_yes:
                        str_rb_garden = "Yes";
                        break;

                    case R.id.rb_no:
                        str_rb_garden = "No";
                        break;

                }
            }
        });
        rg_active.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_active_yes:
                        str_rb_active = "Yes";
                        break;

                    case R.id.rb_active_no:
                        str_rb_active = "No";
                        break;

                }
            }
        });
    }
    private void init(){
        rg_garden = findViewById(R.id.rg_garden);
        rg_active=findViewById(R.id.rg_active);
        rb_yes=findViewById(R.id.rb_yes);
        rb_no=findViewById(R.id.rb_no);
        rb_active_yes=findViewById(R.id.rb_active_yes);
        rb_active_no=findViewById(R.id.rb_active_no);
        tv_suposhan_submit=findViewById(R.id.tv_suposhan_submit);
        suposhanSakhiMonitoringPojo=new SuposhanSakhiMonitoringPojo();
        sqliteHelper=new SqliteHelper(this);

    }
}