package com.example.mhealth.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.ActivityEligibleFamilyRegistration;
import com.example.mhealth.R;
import com.example.mhealth.helper.Mother;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SpinnerHelper;
import com.example.mhealth.helper.SqliteHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOperationLayoutAddMother {
    static SharedPrefHelper sharedPrefHelper;
    Activity activity;
    SqliteHelper sqliteHelper = new SqliteHelper(activity);
     Context context;

    public static HashMap<String, ArrayList<String>> display(final Activity activity) {
        LinearLayout scrollViewlinerLayout = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMother);
        scrollViewlinerLayout.setVerticalGravity(View.TEXT_ALIGNMENT_CENTER);
        HashMap<String, ArrayList<String>> prescriptionHM = new HashMap<>();
        ArrayList<String> nameAl = new ArrayList<>();
        ArrayList<String> ageAl = new ArrayList<>();
        ArrayList<String> maritalStatusAl = new ArrayList<>();
        ArrayList<String> educationAl = new ArrayList<>();
        for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
            final EditText et_name = innerLayout.findViewById(R.id.etxtMother);
            final EditText et_Age = innerLayout.findViewById(R.id.etxtAge);
            final Spinner spn_MaritalStatus = innerLayout.findViewById(R.id.spnMaritalStatus);
            final Spinner spn_Education = innerLayout.findViewById(R.id.spnEducation);

            sharedPrefHelper = new SharedPrefHelper(activity);
            nameAl.add(et_name.getText().toString().trim());
            ageAl.add(et_Age.getText().toString().trim());
            maritalStatusAl.add(spn_MaritalStatus.getSelectedItem().toString().trim());
            educationAl.add(spn_Education.getSelectedItem().toString().trim());
        }

        prescriptionHM.put("name", nameAl);
        prescriptionHM.put("age", ageAl);
        prescriptionHM.put("maritalStatus", maritalStatusAl);
        prescriptionHM.put("education", educationAl);

        return prescriptionHM;
    }



    public static HashMap<String, String> add(final Activity activity, Button btn) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        String[] maritalStatusAL = {"Please Select","Single", "Married", "Widow"};
        String[] maritalStatusHindiAL = {"कृपया चयन कीजिए", "एकल", "विवाहित", "विधवा"};

        String[] educationAL = {"Please Select", "Never gone to school", "Primary 1 to 5", "Upper Primary 6 to 8",
                "Secondary 9 to 10", "Higher Secondary 11 to 12", "Graduate", "Post Graduate and Above"};
        String[] educationHindiAL = {"कृपया चयन कीजिए","कभी स्कूल नहीं गया", "प्राथमिक 1 से 5", "उच्च प्राथमिक 6 से 8",
                "माध्यमिक 9 से 10", "हायर सेकेंडरी 11 से 12", "स्नातक", "स्नातकोत्तर और उससे ऊपर"};

        sharedPrefHelper = new SharedPrefHelper(activity);

        boolean isEditable = false;

        final LinearLayout linearLayoutAddMother = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMother);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_more_mother, null);
                newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                final EditText et_name = newView.findViewById(R.id.etxtMother);
                final EditText et_Age = newView.findViewById(R.id.etxtAge);
                final ImageView family_remove = newView.findViewById(R.id.mother_remove);
                final Spinner spnMaritalStatus = newView.findViewById(R.id.spnMaritalStatus);
                final Spinner spn_Education = newView.findViewById(R.id.spnEducation);
                String languageId = sharedPrefHelper.getString("Language", "");// getting languageId

                if(languageId.equals("1")) {
                    ArrayAdapter adapterMS = new ArrayAdapter(activity,
                            android.R.layout.simple_spinner_item, maritalStatusAL);
                    adapterMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnMaritalStatus.setPrompt("Select Marital Status");
                    spnMaritalStatus.setAdapter(adapterMS);
                }else if(languageId.equals("2")){
                    ArrayAdapter adapterMS = new ArrayAdapter(activity,
                            android.R.layout.simple_spinner_item, maritalStatusHindiAL);
                    adapterMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnMaritalStatus.setPrompt("वैवाहिक स्थिति का चयन करें");
                    spnMaritalStatus.setAdapter(adapterMS);
                }

                if(languageId.equals("1")) {

                    ArrayAdapter adapterEducation = new ArrayAdapter(activity,
                            android.R.layout.simple_spinner_item, educationAL);
                    adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_Education.setPrompt("Select Education");
                    spn_Education.setAdapter(adapterEducation);
                }else if(languageId.equals("2")) {
                    ArrayAdapter adapterEducation = new ArrayAdapter(activity,
                            android.R.layout.simple_spinner_item, educationHindiAL);
                    adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_Education.setPrompt("शिक्षा का चयन करें");
                    spn_Education.setAdapter(adapterEducation);
                }
                ImageView btn_remove = (ImageView) newView.findViewById(R.id.mother_remove);
                btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linearLayoutAddMother.removeView(newView);
                        final EditText et_name = newView.findViewById(R.id.etxtMother);
                        final EditText et_Age = newView.findViewById(R.id.etxtAge);
                        final ImageView family_remove = newView.findViewById(R.id.mother_remove);
                        final Spinner spnMaritalStatus = newView.findViewById(R.id.spnMaritalStatus);
                        final Spinner spn_Education = newView.findViewById(R.id.spnEducation);
                        String ssss = et_name.getText().toString().trim();
                        String sssss = et_Age.getText().toString().trim();
                        hashMap.remove(ssss);
                        hashMap.remove(sssss);

                    }
                });
                linearLayoutAddMother.addView(newView);
            }
        });
        return hashMap;
    }

    public static HashMap<String, String> addForEdit(final Activity activity, ArrayList<Mother> motherArrayList) {
        final HashMap<String, String> hashMap = new HashMap<>();
        SqliteHelper sqliteHelper = new SqliteHelper(activity);
        String[] maritalStatusAL = {"Please Select", "Single", "Married", "Widow"};
        String[] maritalStatusHindiAL = {"कृपया चयन कीजिए", "एकल", "विवाहित", "विधवा"};
        Context context;


        String[] educationAL = {"Please Select", "Never gone to school", "Primary 1 to 5", "Upper Primary 6 to 8",
                "Secondary 9 to 10", "Higher Secondary 11 to 12", "Graduate", "Post Graduate and Above"};

        String[] educationHindiAL = {"कृपया चयन कीजिए", "कभी स्कूल नहीं गया", "प्राथमिक 1 से 5", "उच्च प्राथमिक 6 से 8",
                "माध्यमिक 9 से 10", "हायर सेकेंडरी 11 से 12", "स्नातक", "स्नातकोत्तर और उससे ऊपर"};
        boolean isEditable = false;
        sharedPrefHelper = new SharedPrefHelper(activity);
        String languageId = sharedPrefHelper.getString("Language", "");// getting languageId
        Log.e("TAG", "addForEdit: "+ languageId);

        final LinearLayout linearLayoutAddMedicine = (LinearLayout) activity.findViewById(R.id.linearLayoutAddMother);

        linearLayoutAddMedicine.removeAllViews();
        for (int k = 0; k < motherArrayList.size() ; k++) {

            final LinearLayout newView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_more_mother, null);
            newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final EditText et_name = newView.findViewById(R.id.etxtMother);
            final EditText et_Age = newView.findViewById(R.id.etxtAge);
            final ImageView family_remove = newView.findViewById(R.id.mother_remove);
            final Spinner spnMaritalStatus = newView.findViewById(R.id.spnMaritalStatus);
            final Spinner spn_Education = newView.findViewById(R.id.spnEducation);
            et_name.setText(motherArrayList.get(k).getName_of_pregnant_women());
            et_Age.setText(motherArrayList.get(k).getAge());


            if(languageId.equals("1")) {
                ArrayAdapter adapterMS = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, maritalStatusAL);
                adapterMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMaritalStatus.setPrompt("Select Marital Status");
                spnMaritalStatus.setAdapter(adapterMS);
                if ( motherArrayList.get(k).getMarital_status()!=null) {
                    if (motherArrayList.get(k).getMarital_status().equals("Please Select")) {
                        spnMaritalStatus.setSelection(0);
                    } else if (motherArrayList.get(k).getMarital_status().equals("Single")) {
                        spnMaritalStatus.setSelection(1);
                    } else if (motherArrayList.get(k).getMarital_status().equals("Married")) {
                        spnMaritalStatus.setSelection(2);
                    } else if (motherArrayList.get(k).getMarital_status().equals("Widow")) {
                        spnMaritalStatus.setSelection(3);
                    }
                }
                ArrayAdapter adapterEducation = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, educationAL);
                adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Education.setPrompt("Select Education");
                spn_Education.setAdapter(adapterEducation);

                if ( motherArrayList.get(k).getEducation()!=null) {
                    if (motherArrayList.get(k).getEducation().equals("Please Select")) {
                        spn_Education.setSelection(0);
                    } else if (motherArrayList.get(k).getEducation().equals("Never gone to school")) {
                        spn_Education.setSelection(1);
                    } else if (motherArrayList.get(k).getEducation().equals("Primary 1 to 5")) {
                        spn_Education.setSelection(2);
                    } else if (motherArrayList.get(k).getEducation().equals("Upper Primary 6 to 8")) {
                        spn_Education.setSelection(3);
                    } else if (motherArrayList.get(k).getEducation().equals("Secondary 9 to 10")) {
                        spn_Education.setSelection(4);
                    } else if (motherArrayList.get(k).getEducation().equals("Higher Secondary 11 to 12")) {
                        spn_Education.setSelection(5);
                    } else if (motherArrayList.get(k).getEducation().equals("Graduate")) {
                        spn_Education.setSelection(6);
                    } else if (motherArrayList.get(k).getEducation().equals("Post Graduate and Above")) {
                        spn_Education.setSelection(7);
                    }
                }
            }
            else if(languageId.equals("2")) {
                ArrayAdapter adapterMS = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, maritalStatusHindiAL);
                adapterMS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnMaritalStatus.setPrompt("वैवाहिक स्थिति का चयन करें");
                spnMaritalStatus.setAdapter(adapterMS);

                if ( motherArrayList.get(k).getMarital_status()!=null) {
                    if (motherArrayList.get(k).getMarital_status().equals("कृपया चयन कीजिए")) {
                        spnMaritalStatus.setSelection(0);
                    } else if (motherArrayList.get(k).getMarital_status().equals("एकल")) {
                        spnMaritalStatus.setSelection(1);
                    } else if (motherArrayList.get(k).getMarital_status().equals("विवाहित")) {
                        spnMaritalStatus.setSelection(2);
                    }else if (motherArrayList.get(k).getMarital_status().equals("विधवा")) {
                        spnMaritalStatus.setSelection(2);
                    }
                }


                ArrayAdapter adapterEducation = new ArrayAdapter(activity,
                android.R.layout.simple_spinner_item, educationHindiAL);
                adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_Education.setPrompt("शिक्षा का चयन करें");
                spn_Education.setAdapter(adapterEducation);

                if ( motherArrayList.get(k).getEducation()!=null) {
                    if (motherArrayList.get(k).getEducation().equals("कृपया चयन कीजिए")) {
                        spn_Education.setSelection(0);
                    } else if (motherArrayList.get(k).getEducation().equals("कभी स्कूल नहीं गया")) {
                        spn_Education.setSelection(1);
                    } else if (motherArrayList.get(k).getEducation().equals("प्राथमिक 1 से 5")) {
                        spn_Education.setSelection(2);
                    } else if (motherArrayList.get(k).getEducation().equals("उच्च प्राथमिक 6 से 8")) {
                        spn_Education.setSelection(3);
                    } else if (motherArrayList.get(k).getEducation().equals("माध्यमिक 9 से 10")) {
                        spn_Education.setSelection(4);
                    } else if (motherArrayList.get(k).getEducation().equals("हायर सेकेंडरी 11 से 12")) {
                        spn_Education.setSelection(5);
                    } else if (motherArrayList.get(k).getEducation().equals("स्नातक")) {
                        spn_Education.setSelection(6);
                    }else if (motherArrayList.get(k).getEducation().equals("स्नातकोत्तर और उससे ऊपर")) {
                        spn_Education.setSelection(6);
                    }
                }

            }

            ImageView btn_remove = (ImageView) newView.findViewById(R.id.mother_remove);
            btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    linearLayoutAddMedicine.removeView(newView);
                    final EditText et_name = newView.findViewById(R.id.etxtMother);
                    final EditText et_Age = newView.findViewById(R.id.etxtAge);
                    final ImageView family_remove = newView.findViewById(R.id.mother_remove);
                    final Spinner spnMaritalStatus = newView.findViewById(R.id.spnMaritalStatus);
                    final Spinner spn_Education = newView.findViewById(R.id.spnEducation);
                    String ssss = et_name.getText().toString().trim();
                    String sssss = et_Age.getText().toString().trim();
                    hashMap.remove(ssss);
                    hashMap.remove(sssss);

                }
            });
            linearLayoutAddMedicine.addView(newView);
//            Toast.makeText(activity, "Mother Added Succefully" + k,200).show();

        }

        return hashMap;
    }
}
