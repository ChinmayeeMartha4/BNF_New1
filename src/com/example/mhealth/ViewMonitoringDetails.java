package com.example.mhealth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mhealth.R;
import com.example.mhealth.helper.SqliteHelper;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewMonitoringDetails extends Activity {


    TextView textChildMonitorViewNumber,tvTitleText,textChildMonitorViewNumber1,textPregnantMonitorViewNumber,textPregnantMonitorViewNumber1,textAdolescentMonitorViewNumber,textAdolescentMonitorViewNumber1;
    SqliteHelper sqliteHelper;
    TextView months1,months;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monitoring_details);
        getActionBar().hide();
        init();
        ;
        tvTitleText.setText(R.string.viewMenu);

        String dob2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH);
        String firstMonth="";
        String SecondMonth="";
        String SecondMonth1="";
        firstMonth= String.valueOf(month);
        if (firstMonth.length()>1){
            SecondMonth=firstMonth;
        }else {
            SecondMonth="0"+firstMonth;
        }

        String firstMonth1= String.valueOf(month+1);
        if (firstMonth1.length()>1){
            SecondMonth1=firstMonth1;
        }else {
            SecondMonth1="0"+firstMonth1;
        }

        String num=  sqliteHelper.getChildMoniterCount(SecondMonth, String.valueOf(year));
        String num1=  sqliteHelper.getChildMoniterCount(SecondMonth1, String.valueOf(year));
        String num2=  sqliteHelper.getPregnantWomenMoniterCount(SecondMonth, String.valueOf(year));
        String num3=  sqliteHelper.getPregnantWomenMoniterCount(SecondMonth1, String.valueOf(year));
        String num4=  sqliteHelper.getAdolescentMoniterCount(SecondMonth, String.valueOf(year));
        String num5=  sqliteHelper.getAdolescentMoniterCount(SecondMonth1, String.valueOf(year));
        String monss=new DateFormatSymbols().getMonths()[month-1];
        String monss1=new DateFormatSymbols().getMonths()[month];
        months.setText(monss+" "+year);
        months1.setText(monss1+" "+year);
        textChildMonitorViewNumber.setText(num);
        textChildMonitorViewNumber1.setText(num1);
        textPregnantMonitorViewNumber.setText(num2);
        textPregnantMonitorViewNumber1.setText(num3);
        textAdolescentMonitorViewNumber.setText(num4);
        textAdolescentMonitorViewNumber1.setText(num5);

    }

    private void init() {
        textChildMonitorViewNumber=findViewById(R.id.textChildMonitorViewNumber);
        textChildMonitorViewNumber1=findViewById(R.id.textChildMonitorViewNumber1);
        textPregnantMonitorViewNumber=findViewById(R.id.textpregnantMonitorView);
        textPregnantMonitorViewNumber1=findViewById(R.id.textPregnatMonitorViewNumber1);
        textAdolescentMonitorViewNumber=findViewById(R.id.textAdolescentMonitorView);
        textAdolescentMonitorViewNumber1=findViewById(R.id.textAdolescentMonitorViewNumber1);
        tvTitleText=findViewById(R.id.tvTitleText);
        months1=findViewById(R.id.months1);
        months=findViewById(R.id.months);

        sqliteHelper=new SqliteHelper(this);
    }
}
