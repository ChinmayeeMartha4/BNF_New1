package com.example.mhealth.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.mhealth.R;
import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;


public class SpinnerAdapter1 extends BaseAdapter {

    Context context;
    ArrayList<ChildNutrition> monitor_child, migrate_id;
    SqliteHelper sqliteHelper;
    ArrayList<String> date_child;
    private String formattedDate;

    public SpinnerAdapter1(Context applicationContext, ArrayList<ChildNutrition> monitor_child, ArrayList<ChildNutrition> migrate_child, String formattedDate) {
        this.context = applicationContext;
        this.monitor_child = monitor_child;
        sqliteHelper = new SqliteHelper(context);
        this.migrate_id = migrate_child;
        this.formattedDate = formattedDate;
//        this.date_child = date_child;

    }

    @Override
    public int getCount() {
        return monitor_child.size();
    }

    @Override
    public Object getItem(int arg0) {
        return monitor_child.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.child_spinner, null);

        TextView childname = (TextView) view.findViewById(R.id.childname);
        RadioButton childradio = (RadioButton) view.findViewById(R.id.childradio);
        childname.setText(monitor_child.get(pos).getName_of_child() + " (" + monitor_child.get(pos).getServer_id() + ")");
        int ch = migrate_id.get(pos).getServer_id();
        Log.e("ch", "" + ch);
        String migration_type_a = "Absent";
        String migration_type_t = "Temporary Migrate";

        String migration_type_death = "To Be Verified";
        String migration_type_p = "Permanent Migrate";
        String migration_type_onfarm = "On-farm";
        String migration_type_privateschool = "Private school";
        String migration_type = "";


        try {
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && (null == migrate_id.get(pos).getMigration() || migrate_id.get(pos).getMigration().equalsIgnoreCase(""))) {
                childname.setBackgroundResource(R.color.faqs);

            }
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_t.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.discussion);

            }
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_a.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.discussion);

            }
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_p.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.discussion);

            }
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_onfarm.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.faqs);

            }
            if (ch > 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_privateschool.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.faqs);

            }
            if (ch > 0 && !migrate_id.get(pos).getDateCheck().equals(formattedDate)) {

            }
            if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && (null == migrate_id.get(pos).getMigration() || migrate_id.get(pos).getMigration().equalsIgnoreCase(""))) {
                childname.setBackgroundResource(R.color.faqs);

            }
            if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && (migration_type_a.equalsIgnoreCase(migrate_id.get(pos).getMigration()) || migration_type_t.equalsIgnoreCase(migrate_id.get(pos).getMigration()) || migration_type_p.equalsIgnoreCase(migrate_id.get(pos).getMigration()))) {
                childname.setBackgroundResource(R.color.discussion);
            } else if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_onfarm.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.faqs);
            } else if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_privateschool.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.faqs);
            } else if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_p.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
                childname.setBackgroundResource(R.color.discussion);
            } else if (ch == 0 && migrate_id.get(pos).getDateCheck().equals(formattedDate) && migration_type_death.equalsIgnoreCase(migrate_id.get(pos).getMigration())) {
//                childname.setBackgroundResource(R.color.faqs);
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
