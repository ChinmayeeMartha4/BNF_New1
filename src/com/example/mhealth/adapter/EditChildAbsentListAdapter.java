package com.example.mhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mhealth.Activity_EditAbsentChildNutrition;
import com.example.mhealth.R;
import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.GlobalVars;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditChildAbsentListAdapter extends ArrayAdapter {
    public ArrayList<Child> childList;
    Context context;

    public EditChildAbsentListAdapter(Context context, ArrayList<Child> list) {
        // TODO Auto-generated constructor stub
        super(context, 0);
        this.context = context;
        this.childList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return childList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return childList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup child) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final Child childObject = this.childList.get(position);
        View rowView = null;

        rowView = inflater.inflate(R.layout.edit_item_layout, child, false);

        TextView name_label = (TextView) rowView.findViewById(R.id.name_label);
        name_label.setText("Child Name");

        TextView name_txt = (TextView) rowView.findViewById(R.id.name_txt);
        name_txt.setText(childObject.getChild_name());

        if (position % 2 == 0) {
            rowView.setBackgroundColor(Color.parseColor("#D8BFD8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#98FB98"));
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //intChildId = helper.getChildId(getChildName,getChildId);

                    Intent ChildData = new Intent(context, Activity_EditAbsentChildNutrition.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", childObject.getChild_id());
                    bundle.putString("nameofchild", childObject.getChild_name());
                    bundle.putString("currDate", getCurrDate());

                    GlobalVars.ChildViewID = childObject.getChild_id();
                    ChildData.putExtras(bundle);
                    context.startActivity(ChildData);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e("error", e.toString());
                    e.printStackTrace();
                }
            }
        });

        return rowView;
    }

    public String dateFormatConvert(String dateOfBirth) {
        String convertedDate = "";
        try {
            String dateReceivedFromUser = dateOfBirth;
            DateFormat userDateFormat = new SimpleDateFormat("yyyy-mm-dd");
            DateFormat dateFormatNeeded = new SimpleDateFormat("dd-mm-yyyy");
            Date date = userDateFormat.parse(dateReceivedFromUser);
            convertedDate = dateFormatNeeded.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public String getCurrDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String fdate = df.format(c.getTime());
        String curDate = fdate.substring(0, 7) + "%";
        return curDate;
    }
}
