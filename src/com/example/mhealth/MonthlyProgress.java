package com.example.mhealth;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

public class MonthlyProgress extends Activity {

    static String data_of_birth, child;
    private static int child_id;
    LinearLayout lnr_underweight_chart, weightHeight;
    SqliteHelper sqliteHelper;
    Spinner spnChildName;
    ArrayList<ChildNutrition> monitor_child;
    SharedPrefHelper sph;
    GraphicalView mChart;
    int age_diff;
    private int user_id;
    private int child_gender;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_progress);
        register();


        populateList(spnChildName, "child", "child_id",
                "name_of_child", getString(R.string.select_child), "where anganwadi_center_id=" + user_id + " and child_id not in (select child_id from child_nutrition_monitoring where migration_type = 'Death')");

        tvTitleText.setText(R.string.monthly_progress);
ivTitleBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
    }

    @Override
    protected void onResume() {
        super.onResume();

        spnChildName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1,
                                       int pos, long arg3) {
                spnChildName.getSelectedItem().toString();

                child = getSelectedValue(spnChildName);
                if (!child.equalsIgnoreCase("")) {

                    child_id = Integer.parseInt(child);
                    data_of_birth = sqliteHelper.getDOB(child_id);
                    Log.e("data_of_birth", data_of_birth);

                    setUnderWeightGraph(String.valueOf(child_id), data_of_birth);
                    setHeightWeightGraph(String.valueOf(child_id), data_of_birth);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


    }

    private void populateList(Spinner spinner, String tableName, String col_id,
                              String col_value, String label, String whr) {
        ArrayList<SpinnerHelper> items = new ArrayList<SpinnerHelper>();
        ArrayAdapter<SpinnerHelper> adapter = null;
        items = sqliteHelper.populateChildSpinner(tableName, col_id, col_value,
                label, whr);
        if (items.size() == 0) {
            SpinnerHelper spinnerHelper = new SpinnerHelper("", "");
            items.add(spinnerHelper);
        }
        adapter = new ArrayAdapter<SpinnerHelper>(
                MonthlyProgress.this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPrompt(label);
        spinner.setAdapter(adapter);
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = null;
        try {
            data = (SpinnerHelper) spn.getItemAtPosition((int) spn
                    .getSelectedItemId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.getValue();
    }

    private void register() {

        sqliteHelper = new SqliteHelper(getApplicationContext());
        spnChildName = (Spinner) findViewById(R.id.spnChildName);
        monitor_child = new ArrayList<>();
        sph = new SharedPrefHelper(this);
        user_id = sph.getInt("user_id", 0);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);

    }

    public void setUnderWeightGraph(String c_id, String dob) {
        try {
            ArrayList<Child> uw_history = new ArrayList<Child>();
            ArrayList<Child> uw_history1 = new ArrayList<Child>();
            uw_history = sqliteHelper.getChildUnderweightHistory(c_id);

            String b_year = dob.substring(0, 4);
            String b_month = dob.substring(5, 7);
            int b_age = (Integer.parseInt(b_year) * 12) + (Integer.parseInt(b_month));

            for (int i = 0; i < uw_history.size(); i++) {
                String m_year = uw_history.get(i).getMyDate().substring(0, 4);
                String m_month = uw_history.get(i).getMyDate().substring(5, 7);
                int m_age = (Integer.parseInt(m_year) * 12) + (Integer.parseInt(m_month));
                int age_diff = m_age - b_age;
                Child ch = new Child();
                ch.setMyDate(String.valueOf(age_diff));
                ch.setWeight(uw_history.get(i).getWeight());
                uw_history1.add(ch);
            }


            int[] mMonth = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60};
            Double[] b_suw = {2.1, 2.9, 3.8, 4.4, 4.9, 5.3, 5.7, 5.9, 6.2, 6.4, 6.6, 6.8, 6.9, 7.1, 7.2, 7.4, 7.5, 7.7, 7.8, 8.0, 8.1, 8.2, 8.4, 8.5, 8.6, 8.8, 8.9, 9.0, 9.1, 9.2, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10.0, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9, 12.0, 12.1, 12.2, 12.3, 12.4};
            Double[] b_muw = {2.5, 3.4, 4.3, 5.0, 5.6, 6.0, 6.4, 6.7, 6.9, 7.1, 7.4, 7.6, 7.7, 7.9, 8.1, 8.3, 8.4, 8.6, 8.8, 8.9, 9.1, 9.2, 9.4, 9.5, 9.7, 9.8, 10.0, 10.1, 10.2, 10.4, 10.5, 10.7, 10.8, 10.9, 11.0, 11.2, 11.3, 11.4, 11.5, 11.6, 11.8, 11.9, 12.0, 12.1, 12.2, 12.4, 12.5, 12.6, 12.7, 12.8, 12.9, 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7, 13.8, 14.0, 14.1};
            Double[] b_normal = {3.3, 4.5, 5.6, 6.4, 7.0, 7.5, 7.9, 8.3, 8.6, 8.9, 9.2, 9.4, 9.6, 9.9, 10.1, 10.3, 10.5, 10.7, 10.9, 11.1, 11.3, 11.5, 11.8, 12.0, 12.2, 12.4, 12.5, 12.7, 12.9, 13.1, 13.3, 13.5, 13.7, 13.8, 14.0, 14.2, 14.3, 14.5, 14.7, 14.8, 15.0, 15.2, 15.3, 15.5, 15.7, 15.8, 16.0, 16.2, 16.3, 16.5, 16.7, 16.8, 17.0, 17.2, 17.3, 17.5, 17.7, 17.8, 18.0, 18.2, 18.3};

            Double[] g_suw = {2.0, 2.7, 3.4, 4.0, 4.4, 4.8, 5.1, 5.3, 5.6, 5.8, 5.9, 6.1, 6.3, 6.4, 6.6, 6.7, 6.9, 7.0, 7.2, 7.3, 7.5, 7.6, 7.8, 7.9, 8.1, 8.2, 8.4, 8.5, 8.6, 8.8, 8.9, 9.0, 9.1, 9.3, 9.4, 9.5, 9.6, 9.7, 9.8, 9.9, 10.1, 10.2, 10.3, 10.4, 10.5, 10.6, 10.7, 10.8, 10.9, 11.0, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 11.7, 11.8, 11.9, 12.0, 12.1};
            Double[] g_muw = {2.4, 3.2, 3.9, 4.5, 5.0, 5.4, 5.7, 6.0, 6.3, 6.5, 6.7, 6.9, 7.0, 7.2, 7.4, 7.6, 7.7, 7.9, 8.1, 8.2, 8.4, 8.6, 8.7, 8.9, 9.0, 9.2, 9.4, 9.5, 9.7, 9.8, 10.0, 10.1, 10.3, 10.4, 10.5, 10.7, 10.8, 10.9, 11.1, 11.2, 11.3, 11.5, 11.6, 11.7, 11.8, 12.0, 12.1, 12.2, 12.3, 12.4, 12.6, 12.7, 12.8, 12.9, 13.0, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7};
            Double[] g_normal = {3.2, 4.2, 5.1, 5.8, 6.4, 6.9, 7.3, 7.6, 7.9, 8.2, 8.5, 8.7, 8.9, 9.2, 9.4, 9.6, 9.8, 10.0, 10.2, 10.4, 10.6, 10.9, 11.1, 11.3, 11.5, 11.7, 11.9, 12.1, 12.3, 12.5, 12.7, 12.9, 13.1, 13.3, 13.5, 13.7, 13.9, 14.0, 14.2, 14.4, 14.6, 14.8, 15.0, 15.2, 15.3, 15.5, 15.7, 15.9, 16.1, 16.3, 16.4, 16.6, 16.8, 17.0, 17.2, 17.3, 17.5, 17.7, 17.9, 18.0, 18.2};


            XYSeries suwSeries = new XYSeries("SUW");
            XYSeries muwSeries = new XYSeries("MUW");
            XYSeries normalSeries = new XYSeries("Normal");
            XYSeries ChildGrowthSeries = new XYSeries("Child Growth");

            if (sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("1")) {
                Log.e("................", "Male");
                for (int i = 0; i < mMonth.length; i++) {
                    suwSeries.add(mMonth[i], b_suw[i]);
                    muwSeries.add(mMonth[i], b_muw[i]);
                    normalSeries.add(mMonth[i], b_normal[i]);

                    for (int j = 0; j < uw_history1.size(); j++) {
                        if (i == Integer.parseInt(uw_history1.get(j).getMyDate())) {

                            ChildGrowthSeries.add(mMonth[i], Double.parseDouble(uw_history1.get(j).getWeight()));
                            //Log.e("..........................", uw_history1.get(i).getWeight()+"->"+uw_history1.get(i).getMyDate());
                        }
                    }

                    if (i < uw_history1.size()) {
                        Log.e("..........................", uw_history1.get(i).getMyDate());
                        if (i == Integer.parseInt(uw_history1.get(i).getMyDate())) {
                            ChildGrowthSeries.add(mMonth[i], Double.parseDouble(uw_history1.get(i).getWeight()));
                            Log.e("..........................", uw_history1.get(i).getWeight() + "->" + uw_history1.get(i).getMyDate());
                        }
                    }
                }
            } else {
                Log.e("................", "Female");
                for (int i = 0; i < mMonth.length; i++) {
                    suwSeries.add(mMonth[i], g_suw[i]);
                    muwSeries.add(mMonth[i], g_muw[i]);
                    normalSeries.add(mMonth[i], g_normal[i]);

                    for (int j = 0; j < uw_history1.size(); j++) {
                        if (i == Integer.parseInt(uw_history1.get(j).getMyDate())) {
                            ChildGrowthSeries.add(mMonth[i], Double.parseDouble(uw_history1.get(j).getWeight()));
                            //Log.e("..........................", uw_history1.get(i).getWeight()+"->"+uw_history1.get(i).getMyDate());
                        }
                    }

                    if (i < uw_history1.size()) {
                        Log.e("..........................", uw_history1.get(i).getMyDate());
                        if (i == Integer.parseInt(uw_history1.get(i).getMyDate())) {
                            ChildGrowthSeries.add(mMonth[i], Double.parseDouble(uw_history1.get(i).getWeight()));
                            Log.e("..........................", uw_history1.get(i).getWeight() + "->" + uw_history1.get(i).getMyDate());
                        }
                    }
                }
            }


            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(normalSeries);
            dataset.addSeries(muwSeries);
            dataset.addSeries(suwSeries);
            dataset.addSeries(ChildGrowthSeries);


            // Creating XYSeriesRenderer to customize incomeSeries
            XYSeriesRenderer normalRenderer = new XYSeriesRenderer();
            normalRenderer.setColor(Color.parseColor("#027E02"));
            normalRenderer.setPointStyle(PointStyle.CIRCLE);
            normalRenderer.setChartValuesTextSize(16);
            normalRenderer.setFillBelowLine(true);
            normalRenderer.setFillBelowLineColor(Color.parseColor("#027E02"));
            normalRenderer.setFillPoints(true);
            normalRenderer.setLineWidth(2);
            normalRenderer.setDisplayChartValues(true);

            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer muwRenderer = new XYSeriesRenderer();
            muwRenderer.setColor(Color.parseColor("#C0CA00"));
            muwRenderer.setPointStyle(PointStyle.CIRCLE);
            muwRenderer.setFillPoints(true);
            muwRenderer.setChartValuesTextSize(18);
            muwRenderer.setFillBelowLine(true);
            muwRenderer.setFillBelowLineColor(Color.parseColor("#C0CA00"));
            muwRenderer.setLineWidth(2);
            muwRenderer.setDisplayChartValues(true);

            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer suwRenderer = new XYSeriesRenderer();
            suwRenderer.setColor(Color.parseColor("#D12A0F"));
            suwRenderer.setPointStyle(PointStyle.CIRCLE);
            suwRenderer.setFillPoints(true);
            suwRenderer.setChartValuesTextSize(18);
            suwRenderer.setFillBelowLine(true);
            suwRenderer.setFillBelowLineColor(Color.parseColor("#D12A0F"));
            suwRenderer.setLineWidth(2);
            suwRenderer.setDisplayChartValues(true);

            XYSeriesRenderer cgRenderer = new XYSeriesRenderer();
            cgRenderer.setColor(Color.parseColor("#0300D7"));
            cgRenderer.setPointStyle(PointStyle.CIRCLE);
            cgRenderer.setFillPoints(true);
            cgRenderer.setChartValuesTextSize(18);
            cgRenderer.setLineWidth(2);
            cgRenderer.setDisplayChartValues(true);

            // Creating a XYMultipleSeriesRenderer to customize the whole chart
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setBackgroundColor(Color.parseColor("#CCE2E8"));
            multiRenderer.setXLabels(0);
            multiRenderer.setChartTitle("Age vs Weight");
            multiRenderer.setXTitle("Age ( In Month )");
            multiRenderer.setYTitle("Weight (In Kgs and Grams)");
            multiRenderer.setZoomButtonsVisible(true);
            for (int i = 0; i < mMonth.length; i++) {
                multiRenderer.addXTextLabel(i, String.valueOf(mMonth[i]));
            }

            // Adding incomeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
            // should be same
            multiRenderer.addSeriesRenderer(normalRenderer);
            multiRenderer.addSeriesRenderer(muwRenderer);
            multiRenderer.addSeriesRenderer(suwRenderer);
            multiRenderer.addSeriesRenderer(cgRenderer);

            lnr_underweight_chart = (LinearLayout) findViewById(R.id.lnr_underweight_chart);
            lnr_underweight_chart.removeAllViews();
            // Creating a Line Chart
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);

            mChart.setBackgroundColor(Color.parseColor("#CCE2E8"));

            lnr_underweight_chart.addView(mChart);


        } catch (Exception e) {
            Log.e(".......................", "Child View Underweight graph -> " + e.toString());
        }
    }


    public void setHeightWeightGraph(String c_id, String dob) {
        try {
            ArrayList<Child> hw_history = new ArrayList<Child>();
            ArrayList<Child> hw_history1 = new ArrayList<Child>();
            hw_history = sqliteHelper.getChildHeigthweightHistory(c_id);

            String b_year = dob.substring(0, 4);
            String b_month = dob.substring(5, 7);
            int b_age = (Integer.parseInt(b_year) * 12) + (Integer.parseInt(b_month));

            for (int i = 0; i < hw_history.size(); i++) {
                String m_year = hw_history.get(i).getMyDate().substring(0, 4);
                String m_month = hw_history.get(i).getMyDate().substring(5, 7);
                int m_age = (Integer.parseInt(m_year) * 12) + (Integer.parseInt(m_month));
                age_diff = m_age - b_age;
                Log.e("age in month :=", "" + age_diff);
                Child ch = new Child();
                ch.setMyDate(String.valueOf(age_diff));
                ch.setHeight(hw_history.get(i).getHeight());
                ch.setWeight(hw_history.get(i).getWeight());
                hw_history1.add(ch);
            }


            XYSeries samSeries = new XYSeries("SAM");
            XYSeries mamSeries = new XYSeries("MAM");
            XYSeries normalSeries = new XYSeries("Normal");
            XYSeries ChildGrowthSeries = new XYSeries("Child Growth");

            if (sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("1")) {
                Log.e("................", "Male");


                if (age_diff <= 24) {

                    ArrayList<String> length = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys", "length");
                    ArrayList<String> h_m3sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys", "m3sd");
                    ArrayList<String> h_m2sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys", "m2sd");
//                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys","m1sd");
                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys", "median");

                    Log.e("age in month less then equal to 24 :=", "" + age_diff);
                    for (int i = 0; i < length.size(); i++) {
                        samSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m3sd.get(i)));
                        mamSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m2sd.get(i)));
                        normalSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m1sd.get(i)));

                        for (int j = 0; j < hw_history1.size(); j++) {
                            if (Double.parseDouble(length.get(i)) == Double.parseDouble(hw_history1.get(j).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(hw_history1.get(j).getWeight()));
                            }
                        }

                        if (i < hw_history1.size()) {
                            if (i == Double.parseDouble(hw_history1.get(i).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(hw_history1.get(i).getWeight()));

                            }
                        }


                    }
                } else
//                    if(age_diff>24)
                {
                    ArrayList<String> height = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "height");
                    ArrayList<String> h_m3sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "m3sd");
                    ArrayList<String> h_m2sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "m2sd");
//                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys","m1sd");
                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "median");
                    Log.e("age in month greater then 24 :=", "" + age_diff);
                    for (int i = 0; i < height.size(); i++) {
                        samSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m3sd.get(i)));
                        mamSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m2sd.get(i)));
                        normalSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m1sd.get(i)));

                        for (int j = 0; j < hw_history1.size(); j++) {
                            if (Double.parseDouble(height.get(i)) == Double.parseDouble(hw_history1.get(j).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(hw_history1.get(j).getWeight()));
                            }
                        }

                        if (i < hw_history1.size()) {
                            if (i == Double.parseDouble(hw_history1.get(i).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(hw_history1.get(i).getHeight()), Double.parseDouble(hw_history1.get(i).getWeight()));

                            }
                        }
                    }
                }
            } else {

                if (age_diff <= 24) {
                    Log.e("................", "Female");
                    ArrayList<String> length = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "length");
                    ArrayList<String> h_m3sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "m3sd");
                    ArrayList<String> h_m2sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "m2sd");
//                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "m1sd");
                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "median");

                    Log.e("age in month less then equal to 24 :=", "" + age_diff);
                    for (int i = 0; i < length.size(); i++) {
                        samSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m3sd.get(i)));
                        mamSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m2sd.get(i)));
                        normalSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(h_m1sd.get(i)));

                        for (int j = 0; j < hw_history1.size(); j++) {
                            if (Double.parseDouble(length.get(i)) == Double.parseDouble(hw_history1.get(j).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(hw_history1.get(j).getWeight()));
                            }
                        }

                        if (i < hw_history1.size()) {
                            if (i == Double.parseDouble(hw_history1.get(i).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(length.get(i)), Double.parseDouble(hw_history1.get(i).getWeight()));

                            }
                        }


                    }

                } else

//                    if (age_diff > 24)
                {
                    Log.e("................", "Female");
                    ArrayList<String> height = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "height");
                    ArrayList<String> h_m3sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "m3sd");
                    ArrayList<String> h_m2sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "m2sd");
//                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys","m1sd");
                    ArrayList<String> h_m1sd = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "median");
                    Log.e("age in month greater then 24 :=", "" + age_diff);

                    for (int i = 0; i < height.size(); i++) {
                        samSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m3sd.get(i)));
                        mamSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m2sd.get(i)));
                        normalSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(h_m1sd.get(i)));

                        for (int j = 0; j < hw_history1.size(); j++) {
                            if (Double.parseDouble(height.get(i)) == Double.parseDouble(hw_history1.get(j).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(height.get(i)), Double.parseDouble(hw_history1.get(j).getWeight()));
                            }
                        }

                        if (i < hw_history1.size()) {
                            if (i == Double.parseDouble(hw_history1.get(i).getHeight())) {
                                ChildGrowthSeries.add(Double.parseDouble(hw_history1.get(i).getHeight()), Double.parseDouble(hw_history1.get(i).getWeight()));

                            }
                        }
                    }

                }
            }

            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

            dataset.addSeries(normalSeries);
            dataset.addSeries(mamSeries);
            dataset.addSeries(samSeries);
            dataset.addSeries(ChildGrowthSeries);


            // Creating XYSeriesRenderer to customize incomeSeries
            XYSeriesRenderer normalRenderer = new XYSeriesRenderer();
            normalRenderer.setColor(Color.parseColor("#027E02"));
            normalRenderer.setPointStyle(PointStyle.CIRCLE);
            normalRenderer.setChartValuesTextSize(16);
            normalRenderer.setFillBelowLine(true);
            normalRenderer.setFillBelowLineColor(Color.parseColor("#027E02"));
            normalRenderer.setFillPoints(true);
            normalRenderer.setLineWidth(2);
            normalRenderer.setDisplayChartValues(true);

            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer mawRenderer = new XYSeriesRenderer();
            mawRenderer.setColor(Color.parseColor("#C0CA00"));
            mawRenderer.setPointStyle(PointStyle.CIRCLE);
            mawRenderer.setFillPoints(true);
            mawRenderer.setChartValuesTextSize(18);
            mawRenderer.setFillBelowLine(true);
            mawRenderer.setFillBelowLineColor(Color.parseColor("#C0CA00"));
            mawRenderer.setLineWidth(2);
            mawRenderer.setDisplayChartValues(true);

            // Creating XYSeriesRenderer to customize expenseSeries
            XYSeriesRenderer sawRenderer = new XYSeriesRenderer();
            sawRenderer.setColor(Color.parseColor("#D12A0F"));
            sawRenderer.setPointStyle(PointStyle.CIRCLE);
            sawRenderer.setFillPoints(true);
            sawRenderer.setChartValuesTextSize(18);
            sawRenderer.setFillBelowLine(true);
            sawRenderer.setFillBelowLineColor(Color.parseColor("#D12A0F"));
            sawRenderer.setLineWidth(2);
            sawRenderer.setDisplayChartValues(true);

            XYSeriesRenderer cgRenderer = new XYSeriesRenderer();
            cgRenderer.setColor(Color.parseColor("#0300D7"));
            cgRenderer.setPointStyle(PointStyle.CIRCLE);
            cgRenderer.setFillPoints(true);
            cgRenderer.setChartValuesTextSize(18);
            cgRenderer.setLineWidth(2);
            cgRenderer.setDisplayChartValues(true);

            // Creating a XYMultipleSeriesRenderer to customize the whole chart
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setBackgroundColor(Color.parseColor("#CCE2E8"));
            multiRenderer.setXLabels(0);
            multiRenderer.setChartTitle("Height vs Weight");
            multiRenderer.setXTitle("Height ( In Cm )");
            multiRenderer.setYTitle("Weight (In Kgs and Grams)");

            multiRenderer.setZoomButtonsVisible(true);


            if (age_diff <= 24 && sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("1")) {
                ArrayList<String> length = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_boys", "length");

                for (int i = 0; i < length.size(); i++) {
                    multiRenderer.addXTextLabel(Double.parseDouble(length.get(i)), length.get(i));
                }
            }
            if (age_diff > 24 && sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("1")) {
                ArrayList<String> height = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_boys", "height");

                for (int i = 0; i < height.size(); i++) {
                    multiRenderer.addXTextLabel(Double.parseDouble(height.get(i)), height.get(i));
                }
            }
            if (age_diff <= 24 && sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("2")) {
                ArrayList<String> length = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_length_girls", "length");

                for (int i = 0; i < length.size(); i++) {
                    multiRenderer.addXTextLabel(Double.parseDouble(length.get(i)), length.get(i));
                }
            }
            if (age_diff > 24 && sqliteHelper.GetOneData("gender", "child", "child_id = " + c_id).equalsIgnoreCase("2")) {
                ArrayList<String> height = sqliteHelper.getIdealHeight2yearsBoys("IDEAl_weight_height_girls", "height");

                for (int i = 0; i < height.size(); i++) {
                    multiRenderer.addXTextLabel(Double.parseDouble(height.get(i)), height.get(i));
                }
            }


            // Adding incomeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
            // should be same
            multiRenderer.addSeriesRenderer(normalRenderer);
            multiRenderer.addSeriesRenderer(mawRenderer);
            multiRenderer.addSeriesRenderer(sawRenderer);
            multiRenderer.addSeriesRenderer(cgRenderer);


            weightHeight = (LinearLayout) findViewById(R.id.weightHeight);
            weightHeight.removeAllViews();
            // Creating a Line Chart
            mChart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);

            mChart.setBackgroundColor(Color.parseColor("#CCE2E8"));


            weightHeight.addView(mChart);


        } catch (Exception e) {
            Log.e(".......................", "Child View Underweight graph -> " + e.toString());
        }
    }


}

