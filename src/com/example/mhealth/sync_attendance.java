package com.example.mhealth;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.attendance;

import java.util.ArrayList;

public class sync_attendance extends AsyncTask<String, Void, String> {
    SqliteHelper sqliteHelper;
    Context context;
    ArrayList<attendance> attList;
    ServerHelper servHelp;

    public sync_attendance(Context con) {
        this.context = con;
        sqliteHelper = new SqliteHelper(con);
        attList = new ArrayList<attendance>();
        servHelp = new ServerHelper(con);
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        try {
            attList = sqliteHelper.getPendingAttendance();
            for (int i = 0; i < attList.size(); i++) {
                servHelp.uploadAttendance(attList.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
