package com.example.mhealth.helper;

import android.content.Context;
import android.widget.Spinner;

public class Helper {

    Context context;

    public Helper(Context _ctx) {
        context = _ctx;
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn.getSelectedItemId());
        return data.getValue();
    }
}
