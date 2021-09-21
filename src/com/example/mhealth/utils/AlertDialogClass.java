package com.example.mhealth.utils;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.mhealth.ActivityEligibleFamilyRegistration;
import com.example.mhealth.R;

public class AlertDialogClass {
    public static android.app.Dialog info_alert;

    public static void showDialog(Context context, String infoTitle, String message) {
        info_alert = new android.app.Dialog(context);

        info_alert.setContentView(R.layout.info_alert_dialog);
        info_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = info_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        TextView tvInfoTitle = (TextView) info_alert.findViewById(R.id.tv_info_title);
        TextView tvDescription = (TextView) info_alert.findViewById(R.id.tv_description);
        Button btnOk = (Button) info_alert.findViewById(R.id.btnOk);

        tvInfoTitle.setText(infoTitle);
        tvDescription.setText(message);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                info_alert.dismiss();
            }
        });

        info_alert.show();
        info_alert.setCanceledOnTouchOutside(false);
    }

    public static void showIMageDialog(Context context) {
        info_alert = new android.app.Dialog(context);

        info_alert.setContentView(R.layout.image_dialog);
        info_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = info_alert.getWindow().getAttributes();
        params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;

        Button btnOk = (Button) info_alert.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO
                info_alert.dismiss();
            }
        });

        info_alert.show();
        info_alert.setCanceledOnTouchOutside(false);
    }


}
