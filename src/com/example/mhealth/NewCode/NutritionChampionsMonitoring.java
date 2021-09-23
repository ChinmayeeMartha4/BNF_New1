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

public class NutritionChampionsMonitoring extends AppCompatActivity {
    RadioGroup rg_garden,rg_active;
    RadioButton rb_yes,rb_no,rb_active_yes,rb_active_no;
    TextView tv_suposhan_submit;
    private Context context = this;
    SqliteHelper sqliteHelper;
    String str_rb_garden,str_rb_active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_champions_monitoring);
        init();
//        tv_suposhan_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(NutritionChampionsMonitoring.this, MainMenuMonitoringActivity.class);
//                startActivity(intent);
//
//            }
//        });
    }
    private void init(){
        rg_garden = findViewById(R.id.rg_garden);
        rg_active=findViewById(R.id.rg_active);
        rb_yes=findViewById(R.id.rb_yes);
        rb_no=findViewById(R.id.rb_no);
        rb_active_yes=findViewById(R.id.rb_active_yes);
        rb_active_no=findViewById(R.id.rb_active_no);
        tv_suposhan_submit=findViewById(R.id.tv_suposhan_submit);


    }
}