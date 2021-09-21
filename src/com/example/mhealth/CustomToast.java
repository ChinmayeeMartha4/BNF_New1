package com.example.mhealth;

import android.R.string;
import android.content.Context;
import android.widget.Toast;

public class CustomToast {

    public Toast makeToast(String result, string line) {
        Toast toast = Toast.makeText(getApplicationContext(), result + " " + line, Toast.LENGTH_LONG);
        toast.show();
        return toast;

    }

    private Context getApplicationContext() {
        Context mContext = null;
        // TODO Auto-generated method stub
        return mContext;
    }

}
