package com.example.mhealth;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.helper.sevika_helper;

import java.util.ArrayList;

public class sync_sevika_helper extends AsyncTask<String, Void, String> {
    SqliteHelper sqliteHelper;
    Context context;
    ArrayList<sevika_helper> attList;
    ServerHelper servHelp;

    public sync_sevika_helper(Context con) {
        this.context = con;
        sqliteHelper = new SqliteHelper(con);
        attList = new ArrayList<sevika_helper>();
        servHelp = new ServerHelper(con);
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = "";
        attList = sqliteHelper.getPendingSevikaHelper();
        for (int i = 0; i < attList.size(); i++) {
            servHelp.uploadSevikaHelper(attList.get(i));
        }

        return result;
    }
}