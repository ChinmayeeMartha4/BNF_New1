package com.example.mhealth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mhealth.helper.AdolBaseline;
import com.example.mhealth.helper.Adolescent;
import com.example.mhealth.helper.AdolescentMonitoring;
import com.example.mhealth.helper.Child;
import com.example.mhealth.helper.ChildNutrition;
import com.example.mhealth.helper.ConstantValue;
import com.example.mhealth.helper.EventDetailsModel;
import com.example.mhealth.helper.EventMeetingModel;
import com.example.mhealth.helper.GlobalVars;
import com.example.mhealth.helper.GoogleAnalyticsHelper;
import com.example.mhealth.helper.Mother;
import com.example.mhealth.helper.PW_Baseline;
import com.example.mhealth.helper.Parent;
import com.example.mhealth.helper.PregnantWomen;
import com.example.mhealth.helper.PregnantWomenMonitor;
import com.example.mhealth.helper.ServerHelper;
import com.example.mhealth.helper.SharedPrefHelper;
import com.example.mhealth.helper.SqliteHelper;
import com.example.mhealth.model.ChildBehaviourChange;
import com.example.mhealth.rest_apis.ApiClient;
import com.example.mhealth.rest_apis.M_Health_API;
import com.example.mhealth.utils.CommonMethods;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncActivity extends Activity implements android.view.View.OnClickListener {

    public static ProgressBar pbf, pbm, pbmpw, pbpwm, pbcr, pbcm, pbagr, pbagm, pbEveMee;
    public static Dialog downloadDialog;
    SqliteHelper sqliteHelper;
    ServerHelper serverhelper;
    ProgressDialog phd;
    SharedPrefHelper sph;
    String strLanguageId, strUploadData, strDownloadData,
            strEligibleFamily, strPregnantWomen, strChild, strAdolescent, strChildNut, strPregNut, strAdlNut,
            strDownElFamily, strDownloadAll, strYes, strNo, strCancel, strSync, strTryagain, strPendingDataIs,
            strUploadELFamilyData, strUploadPwData, strUploadAdGlData, strUploadChildData, strUploadChildNutData,
            strUploadPwNutData, strUploadAGNutlData, strDownELFaomilyData, strDownloadAllData,
            strUploadPendingDataToServer, strUploadEgibleFamily, strUploadChild, strUploadPregnantWomen,
            strUploadPregnantWomenMon, strUploadChildMon, strUploadAdolescent, strUploadAdolescentMon,
            strUploadLinkedPrWo, strUploadLinkedPreWnMon, strUploadLinkedChild, strUploadLinkedChildMon,
            strUploadLinkedAdolescent, strUploadLinkedAdolescentMon, strdwnPlsWait, strCheckInternet,
            strChkInternet, strOr, strMother, strUploadMotherData, strUploadMother, strUploadLinkedMother,
            strUploadAwcDetails, strUploadWorkerDetails, strUploadPendingAwcDetails, strUploadPendingWorkerDetails,strAdolescentBaseline,strPregnantWomenBaseline;


    Button uploadEligiblefamily, uploadchild, uploadchiuldnutrition,uploadchildBehaviourChange,
            uploadwomenregistration, uploadmotherregistration, uploadwomenmonitoring, btnDownloadAll,
            uploadAwcDetails, btnUploadWorkerDetails,btnUploadAdolescentBaseline,btnUploadPWBaseline, uploadEventMeeting;
    TextView txtUploadPendingData, txtDownloadData;
    ArrayList<Parent> parentList, parentGPSUpdateList;
    ArrayList<ChildNutrition> childNutritionList;
    ArrayList<ChildBehaviourChange> childBehaviourChangeList;
    ArrayList<Child> childList;
    ArrayList<PregnantWomen> pregnantWomenList;
    ArrayList<Mother> motherList;
    ArrayList<PregnantWomenMonitor> womenNutritionList;// = new
    ArrayList<Adolescent> AdolescentGirlList;
    ArrayList<AdolescentMonitoring> AdolescentNutritionMoniterList;
    ArrayList<AdolBaseline> AdolescentBaselineList;
    ArrayList<PW_Baseline> PregnantWomenBaselineList;
    ArrayList<EventDetailsModel> eventDetailsModelArrayList;
    int user_id;
    int varChunk = 10;
    private Button downloadEligibleFamily;
    private Button uploadAdolscentGirl;
    private Button uploadAdolescentMonitoring;
    TextView tvTitleText;
    ImageView ivTitleBack;
    private Context context=this;
    private int countHH=0,countPW=0,countMother=0,countChild=0,
            countAdolescent=0,countChildMonitoring=0,countPWMonitoring=0, countChildBehaviourCHangeMonitoring=0,
            countAdolescentMonitoring=0, countEventMeetings=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syncactivity);

        initialization();
        getLanguage();
        uploadAttendance();

        String lngTypt = sph.getString("Language", "");
        if (lngTypt.equals("1")) {
            setLanguage("en");
        } else if (lngTypt.equals("2")) {
            setLanguage("hi");
        }

        // onClickListner
        uploadEligiblefamily.setOnClickListener(this);
        uploadchild.setOnClickListener(this);
        uploadchiuldnutrition.setOnClickListener(this);
        uploadchildBehaviourChange.setOnClickListener(this);
        uploadwomenregistration.setOnClickListener(this);
        uploadmotherregistration.setOnClickListener(this);
        uploadwomenmonitoring.setOnClickListener(this);
        downloadEligibleFamily.setOnClickListener(this);
        uploadAdolscentGirl.setOnClickListener(this);
        uploadAdolescentMonitoring.setOnClickListener(this);
        uploadAwcDetails.setOnClickListener(this);
        btnUploadWorkerDetails.setOnClickListener(this);
        btnUploadAdolescentBaseline.setOnClickListener(this);
        btnUploadPWBaseline.setOnClickListener(this);
        uploadEventMeeting.setOnClickListener(this);

        parentList = new ArrayList<Parent>();

        parentGPSUpdateList = new ArrayList<Parent>();
        pregnantWomenList = new ArrayList<PregnantWomen>();
        motherList = new ArrayList<Mother>();
        womenNutritionList = new ArrayList<PregnantWomenMonitor>();
        childList = new ArrayList<Child>();
        childNutritionList = new ArrayList<ChildNutrition>();
        childBehaviourChangeList = new ArrayList<ChildBehaviourChange>();
        AdolescentGirlList = new ArrayList<Adolescent>();
        AdolescentNutritionMoniterList = new ArrayList<AdolescentMonitoring>();
        AdolescentBaselineList = new ArrayList<AdolBaseline>();
        PregnantWomenBaselineList = new ArrayList<PW_Baseline>();
        eventDetailsModelArrayList = new ArrayList<EventDetailsModel>();

        try {
            parentList = sqliteHelper.getParents();
            countHH=parentList.size();
            parentGPSUpdateList = sqliteHelper.getParentsForGPSUpdate();
            pregnantWomenList = sqliteHelper.getPregnantWomen();
            countPW=pregnantWomenList.size();
            motherList = sqliteHelper.getMother();
            countMother=motherList.size();
            childList = sqliteHelper.getChildren("");
            countChild=childList.size();
            AdolescentGirlList = sqliteHelper.getAdolscentGirl();
            countAdolescent=AdolescentGirlList.size();
            childNutritionList = sqliteHelper.getChildMonitor();
            countChildMonitoring=childNutritionList.size();
            eventDetailsModelArrayList = sqliteHelper.getEventMeetingsData();
            countEventMeetings=eventDetailsModelArrayList.size();
            childBehaviourChangeList = sqliteHelper.getChildBehaviourChange();
            countChildBehaviourCHangeMonitoring=childBehaviourChangeList.size();
            womenNutritionList = sqliteHelper.getPregnantWomenMonitor();
            countPWMonitoring=womenNutritionList.size();
            AdolescentNutritionMoniterList = sqliteHelper.getAdolescentGirlMonitor();
            countAdolescentMonitoring=AdolescentNutritionMoniterList.size();
            AdolescentBaselineList = sqliteHelper.getAdolscentBaselineData();
            PregnantWomenBaselineList = sqliteHelper.getPregnantwomenBaselineData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (countHH > 0) {
            uploadEligiblefamily.setText(getString(R.string.house_hold) + "\n" + strPendingDataIs + " " + countHH+"");
        }
        if (AdolescentBaselineList.size() > 0) {
            btnUploadAdolescentBaseline.setText(strAdolescentBaseline + "\n" + strPendingDataIs + " " + AdolescentBaselineList.size());
        }
        if (PregnantWomenBaselineList.size() > 0) {
            btnUploadPWBaseline.setText(strPregnantWomenBaseline + "\n" + strPendingDataIs + " " + PregnantWomenBaselineList.size());
        }

        if (eventDetailsModelArrayList.size() > 0) {
            uploadEventMeeting.setText(getString(R.string.events_or_meetings) +"\n"+ " " + strPendingDataIs + " " +countEventMeetings+"");
        }
        if (countChild > 0) {
            uploadchild.setText(strChild + "\n" + strPendingDataIs + " " + countChild+"");
        }
        if (countChildMonitoring > 0) {
            uploadchiuldnutrition.setText(strChildNut + "\n" + strPendingDataIs + " " + countChildMonitoring+"");
        }
        if (countChildBehaviourCHangeMonitoring > 0) {
            uploadchildBehaviourChange.setText("Child Behaviour Change" +"\n"+ strPendingDataIs + " " +countChildBehaviourCHangeMonitoring+"");
        }
        if (countPW > 0) {
            uploadwomenregistration.setText(strPregnantWomen + "\n" + strPendingDataIs + " " + countPW+"");
        }
        if (countMother > 0) {
            uploadmotherregistration.setText(strMother + "\n" + strPendingDataIs + " " + countMother+"");
        }

        if (countPWMonitoring > 0) {
            uploadwomenmonitoring.setText(strPregNut + "\n" + strPendingDataIs + " " + countPWMonitoring+"");
        }
        if (countAdolescent > 0) {
            uploadAdolscentGirl.setText(strAdolescent + "\n" + strPendingDataIs + " " + countAdolescent+"");
        }
        if (countAdolescentMonitoring > 0) {
            uploadAdolescentMonitoring.setText(strAdlNut + "\n" + strPendingDataIs + " " + countAdolescentMonitoring+"");
        }
        if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)) > 0) {
            uploadAwcDetails.setText(strUploadPendingAwcDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
        }
        if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)) > 0) {
            btnUploadWorkerDetails.setText(strUploadPendingWorkerDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id));
        }


        //phd = new ProgressDialog(this);
        /*phd.setIndeterminate(false);
        phd.setTitle("Upload");
        phd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        phd.setCancelable(false);
        phd.setMax(0);
        phd.setProgress(0);*/

        tvTitleText.setText(getString(R.string.synchronize));
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // builder.setTitle("Information");
                builder.setMessage(strCancel + " " + strSync + "?");

                builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(SyncActivity.this,
                                MainMenuActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    void initialization() {

        sqliteHelper = new SqliteHelper(this);
        sph = new SharedPrefHelper(this);
        user_id = sph.getInt("user_id", 0);
        serverhelper = new ServerHelper(this);
        //mProgressDialog = new ProgressDialog(context);

        uploadEligiblefamily = (Button) findViewById(R.id.uploadEligiblefamily);
        uploadchild = (Button) findViewById(R.id.uploadchild);
        uploadchiuldnutrition = (Button) findViewById(R.id.uploadchiuldnutrition);
        uploadchildBehaviourChange = (Button) findViewById(R.id.uploadChildBehaviourChange);
        uploadEventMeeting = (Button) findViewById(R.id.uploadEventMeeting);
        uploadwomenregistration = (Button) findViewById(R.id.uploadwomenregistration);
        uploadmotherregistration = (Button) findViewById(R.id.uploadmotherregistration);
        uploadwomenmonitoring = (Button) findViewById(R.id.uploadwomenmonitoring);
        downloadEligibleFamily = (Button) findViewById(R.id.btnDwnEligibleFamily);
        uploadAdolscentGirl = (Button) findViewById(R.id.uploadAdolscentGirl);
        uploadAdolescentMonitoring = (Button) findViewById(R.id.uploadAdolescentMonitoring);
        uploadAwcDetails = (Button) findViewById(R.id.btnUploadAwcDetails);
        btnUploadWorkerDetails = (Button) findViewById(R.id.btnUploadWorkerDetails);
        btnDownloadAll = (Button) findViewById(R.id.btnDownloadAll);

        btnUploadPWBaseline = (Button) findViewById(R.id.btnUploadPWBaseline);
        btnUploadAdolescentBaseline = (Button) findViewById(R.id.btnUploadAdolescentBaseline);
        txtUploadPendingData = (TextView) findViewById(R.id.txtUploadPendingData);
        txtDownloadData = (TextView) findViewById(R.id.txtDownloadData);
        tvTitleText=findViewById(R.id.tvTitleText);
        ivTitleBack=findViewById(R.id.ivTitleBack);

    }

    void getLanguage() {

        strLanguageId = sph.getString("Language", "");// getting languageId

        strUploadData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPendingData, strLanguageId);
        strDownloadData = sqliteHelper.LanguageChanges(ConstantValue.LANDownloadData, strLanguageId);
        strEligibleFamily = sqliteHelper.LanguageChanges(ConstantValue.LANElFamily, strLanguageId);
        strPregnantWomen = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomen, strLanguageId);
        strMother = sqliteHelper.LanguageChanges(ConstantValue.LANMother, strLanguageId);
        strChild = sqliteHelper.LanguageChanges(ConstantValue.LANChildren, strLanguageId);
        strAdolescent = sqliteHelper.LanguageChanges(ConstantValue.LANAdolescent, strLanguageId);
        strChildNut = sqliteHelper.LanguageChanges(ConstantValue.LANChildNut, strLanguageId);
        strAdlNut = sqliteHelper.LanguageChanges(ConstantValue.LANAdolscentMonitoring, strLanguageId);
        strPregNut = sqliteHelper.LanguageChanges(ConstantValue.LANPregWomenMonitoring, strLanguageId);
        strDownElFamily = sqliteHelper.LanguageChanges(ConstantValue.LANDwElFamily, strLanguageId);
        strDownloadAll = sqliteHelper.LanguageChanges(ConstantValue.LANDownloadAll, strLanguageId);
        strAdolescentBaseline = sqliteHelper.LanguageChanges(ConstantValue.LANAdolGirlBaseline,strLanguageId);
        strPregnantWomenBaseline = sqliteHelper.LanguageChanges(ConstantValue.LANPregnantBaseline,strLanguageId);

        strUploadAwcDetails = sqliteHelper.LanguageChanges(ConstantValue.LANUploadAwcDetails, strLanguageId);
        strUploadWorkerDetails = sqliteHelper.LanguageChanges(ConstantValue.LANUploadWorkerDetails, strLanguageId);
        strUploadPendingAwcDetails = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPendingAwcDetails, strLanguageId);
        strUploadPendingWorkerDetails = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPendingWorkerDetails, strLanguageId);

        strTryagain = sqliteHelper.LanguageChanges(ConstantValue.LANTryAgain, strLanguageId);
        strYes = sqliteHelper.LanguageChanges(ConstantValue.LANYes, strLanguageId);
        strNo = sqliteHelper.LanguageChanges(ConstantValue.LANNo, strLanguageId);
        strCancel = sqliteHelper.LanguageChanges(ConstantValue.LANCancel, strLanguageId);
        strSync = sqliteHelper.LanguageChanges(ConstantValue.LANSync, strLanguageId);
        strPendingDataIs = sqliteHelper.LanguageChanges(ConstantValue.LANPenData, strLanguageId);

        strUploadELFamilyData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadELFamilyData, strLanguageId);
        strUploadPwData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPwData, strLanguageId);
        strUploadMotherData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadMotherData, strLanguageId);
        strUploadAdGlData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadAdGlData, strLanguageId);
        strUploadChildData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadChildData, strLanguageId);
        strUploadChildNutData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadChildNutData, strLanguageId);
        strUploadPwNutData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPwNutData, strLanguageId);
        strUploadAGNutlData = sqliteHelper.LanguageChanges(ConstantValue.LANUploadAGNutlData, strLanguageId);
        strDownELFaomilyData = sqliteHelper.LanguageChanges(ConstantValue.LANDownELFaomilyData, strLanguageId);
        strDownloadAllData = sqliteHelper.LanguageChanges(ConstantValue.LANDownloadAllData, strLanguageId);
        strUploadPendingDataToServer = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPendingDataToServer, strLanguageId);
        strUploadEgibleFamily = sqliteHelper.LanguageChanges(ConstantValue.LANUploadEgibleFamily, strLanguageId);
        strUploadChild = sqliteHelper.LanguageChanges(ConstantValue.LANUploadChild, strLanguageId);
        strUploadPregnantWomen = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPregnantWomen, strLanguageId);
        strUploadMother = sqliteHelper.LanguageChanges(ConstantValue.LANUploadMother, strLanguageId);
        strUploadPregnantWomenMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadPregnantWomenMon, strLanguageId);
        strUploadChildMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadChildMon, strLanguageId);
        strUploadAdolescent = sqliteHelper.LanguageChanges(ConstantValue.LANUploadAdolescent, strLanguageId);
        strUploadAdolescentMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadAdolescentMon, strLanguageId);
        strUploadLinkedPrWo = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedPrWo, strLanguageId);
        strUploadLinkedMother = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedMother, strLanguageId);
        strUploadLinkedPreWnMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedPreWnMon, strLanguageId);
        strUploadLinkedChild = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedChild, strLanguageId);
        strUploadLinkedChildMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedChildMon, strLanguageId);
        strUploadLinkedAdolescent = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedAdolescent, strLanguageId);
        strUploadLinkedAdolescentMon = sqliteHelper.LanguageChanges(ConstantValue.LANUploadLinkedAdolescentMon, strLanguageId);
        strdwnPlsWait = sqliteHelper.LanguageChanges(ConstantValue.LANdwnPlsWait, strLanguageId);
        strCheckInternet = sqliteHelper.LanguageChanges(ConstantValue.LANCheckInternet, strLanguageId);
        strChkInternet = sqliteHelper.LanguageChanges(ConstantValue.LANChkInternet, strLanguageId);
        strOr = sqliteHelper.LanguageChanges(ConstantValue.LANOr, strLanguageId);

        //set language
        txtUploadPendingData.setText(strUploadData);
        txtDownloadData.setText(strDownloadData);

        uploadEligiblefamily.setText(getString(R.string.house_hold));
        uploadchild.setText(strChild);
        uploadchiuldnutrition.setText(strChildNut);
        uploadwomenregistration.setText(strPregnantWomen);
        uploadmotherregistration.setText(strMother);
        uploadwomenmonitoring.setText(strPregNut);
        downloadEligibleFamily.setText(strDownElFamily);
        uploadAdolscentGirl.setText(strAdolescent);
        uploadAdolescentMonitoring.setText(strAdlNut);
        btnDownloadAll.setText(R.string.download_all);
        uploadAwcDetails.setText(strUploadAwcDetails);
        btnUploadWorkerDetails.setText(strUploadWorkerDetails);
        btnUploadAdolescentBaseline.setText(strAdolescentBaseline);
        btnUploadPWBaseline.setText(strPregnantWomenBaseline);

    }


    public void SendDataOnServer() {
        parentList = sqliteHelper.getParents();
        if (parentList.size()>0){
            for (int i = 0; i < parentList.size(); i++) {
                String houseNumber=parentList.get(i).getHouse_number();
                Gson gson = new Gson();
                String data = gson.toJson(parentList.get(i));
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(JSON, data);

                sendHouseHoldDataOnServer(body, houseNumber);
            }
        }
    }

    private void sendHouseHoldDataOnServer(RequestBody body, String houseNumber) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callHouseHoldApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendHHOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int intuser_id = Integer.parseInt(server_id);
                            String inserted_id = "";
                            if (intuser_id > 0) {
                                inserted_id = intuser_id + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "eligible_family", inserted_id, "anganwadi_center_id="
                                                + user_id + " and house_number ='"+ houseNumber+"'", "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countHH>0){
                                    countHH=countHH-1;
                                    uploadEligiblefamily.setText(getString(R.string.house_hold) + "\n" + strPendingDataIs + " " + countHH+"");
                                }
                            } else {
                                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void UpdateGPSForEligible() {
    }

    public void SendChildNutrition() {
        parentList = sqliteHelper.getParents();
        pregnantWomenList = sqliteHelper.getPregnantWomen();
        motherList = sqliteHelper.getMother();
        childList = sqliteHelper.getChildren("");
        if (parentList.size() < 1 && pregnantWomenList.size() < 1 && motherList.size() < 1 && childList.size() < 1) {
            childNutritionList = sqliteHelper.getChildMonitor();
            if (childNutritionList.size()>0){
                for (int i = 0; i < childNutritionList.size(); i++) {
                    String nutritionId= String.valueOf(childNutritionList.get(i).getNutrition_id());
                    String server_id = sqliteHelper.getServerId(
                            "child", "child_id",
                            String.valueOf(childNutritionList.get(i).getChild_id()))
                            + "";
                    childNutritionList.get(i).setChild_id(Integer.parseInt(server_id));

                    Gson gson = new Gson();
                    String data = gson.toJson(childNutritionList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendChildMonitoringDataOnServer(body, nutritionId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, "Please Upload family data first.", Toast.LENGTH_LONG).show();
            }
            if (pregnantWomenList.size() > 0) {
                Toast.makeText(this, "Please Upload pregnant Women data first.", Toast.LENGTH_LONG).show();
            }
            if (motherList.size() > 0) {
                Toast.makeText(this, "Please Upload mother data first.", Toast.LENGTH_LONG).show();
            }
            if (childList.size() > 0) {
                Toast.makeText(this, "Please Upload child registration data first.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please upload family or pregnant women or mother or child registration data first", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendChildMonitoringDataOnServer(RequestBody body, String nutritionId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callChildMonitoringApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendCMOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "child_nutrition_monitoring", inserted_id,
                                        "  nutrition_id =" + nutritionId, "multimedia");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countChildMonitoring>0){
                                    countChildMonitoring=countChildMonitoring-1;
                                    uploadchiuldnutrition.setText(strChildNut + "\n" + strPendingDataIs + " " + countChildMonitoring+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendChildBehaviourChange() {
        childList = sqliteHelper.getChildren("");
        if (childList.size() < 1) {
            childBehaviourChangeList.clear();
            childBehaviourChangeList = sqliteHelper.getChildBehaviourChange();
            if (childBehaviourChangeList.size()>0){
                for (int i = 0; i < childBehaviourChangeList.size(); i++) {
                    String child_behavior_change_id= String.valueOf(childBehaviourChangeList.get(i).getChild_behavior_change_id());
                    String server_id = sqliteHelper.getServerId(
                            "child", "child_id",
                            String.valueOf(childBehaviourChangeList.get(i).getChild_behavior_change_id()))
                            + "";
                    childBehaviourChangeList.get(i).setChild_id(server_id);

                    Gson gson = new Gson();
                    String data = gson.toJson(childBehaviourChangeList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendChildBehaviourdMonitoringDataOnServer(body, child_behavior_change_id);
                }
            }
        } else {
            if (childList.size() > 0) {
                Toast.makeText(this, "Please Upload child registration data first.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please upload family or pregnant women or mother or child registration data first", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendChildBehaviourdMonitoringDataOnServer(RequestBody body, String child_behavior_change_id) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callChildBehabiourChange(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendCBCOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "child_behavior_change", inserted_id,
                                        "  child_behavior_change_id =" + child_behavior_change_id, "multimedia");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countChildBehaviourCHangeMonitoring>0){
                                    countChildBehaviourCHangeMonitoring=countChildBehaviourCHangeMonitoring-1;
                                    uploadchildBehaviourChange.setText(" Child Behaviour Change " + strPendingDataIs + " " +countChildBehaviourCHangeMonitoring+"");
                                }

                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void SendEventMeetings() {
        eventDetailsModelArrayList = sqliteHelper.getEventMeetingsData();
            if (eventDetailsModelArrayList.size()>0){
                for (int i = 0; i < eventDetailsModelArrayList.size(); i++) {
                    String id= String.valueOf(eventDetailsModelArrayList.get(i).getId());
                    String server_id = sqliteHelper.getServerId(
                            "event_details", "id",
                            String.valueOf(eventDetailsModelArrayList.get(i).getId()))
                            + "";
                    eventDetailsModelArrayList.get(i).setId(Integer.parseInt(server_id));

                    Gson gson = new Gson();
                    String data = gson.toJson(eventDetailsModelArrayList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendEventMeetingsDataOnServer(body, id);
            }
        } else {

                Toast.makeText(this, "Please upload Events or Meetings", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEventMeetingsDataOnServer(RequestBody body, String events_id) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callADDEvent(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendCBCOnServer>>>", "Events or Meetings onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String event_details_id=jsonObject.getString("event_details_id");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (event_details_id!=null) {
                            int eventId = Integer.parseInt(event_details_id);
                            String inserted_id="";
                            if (eventId > 0) {
                                inserted_id = eventId + "";
                                long id = sqliteHelper.updateAndChangeEventServer(
                                        "event_details", inserted_id,
                                        " id =" + events_id, "multimedia");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countEventMeetings>0){
                                    countEventMeetings=countEventMeetings-1;
                                    uploadEventMeeting.setText("Events or Meetings" + " "+strPendingDataIs + " " +countEventMeetings+"");
                                }

                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void SendWomenNutrition() {
        parentList = sqliteHelper.getParents();
        pregnantWomenList = sqliteHelper.getPregnantWomen();
        if (parentList.size() < 1 && pregnantWomenList.size() < 1) {
            womenNutritionList = sqliteHelper.getPregnantWomenMonitor();
            if (womenNutritionList.size()>0){
                for (int i = 0; i < womenNutritionList.size(); i++) {
                    String pregnantWomenId=womenNutritionList.get(i).getPregnant_women_id();
                    String server_id = sqliteHelper.getServerId("pregnant_women",
                            "pregnant_women_id",
                            womenNutritionList.get(i).getWomen_id())
                            + "";
                    womenNutritionList.get(i).setWomen_id(server_id);

                    Gson gson = new Gson();
                    String data = gson.toJson(womenNutritionList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendPregnantWomenMonitoringDataOnServer(body,pregnantWomenId);
                }
            }
        } else {
            if (parentList.size() > 0) {
                Toast.makeText(this, R.string.please_ipload_family_data, Toast.LENGTH_LONG).show();
            }
            if (pregnantWomenList.size() > 0) {
                Toast.makeText(this, R.string.please_registration_data, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendPregnantWomenMonitoringDataOnServer(RequestBody body, String pregnantWomenId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callPregnantMonitoringApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendPWMOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper
                                        .updateAndChangeServer(
                                                "pregnant_womem_monitor",
                                                inserted_id,
                                                " monitor_id ="
                                                        + pregnantWomenId, "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countPWMonitoring>0){
                                    countPWMonitoring=countPWMonitoring-1;
                                    uploadwomenmonitoring.setText(strPregNut + "\n" + strPendingDataIs + " " + countPWMonitoring+"");
                                }
                            } else {
                                mProgressDialog.dismiss();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendPregnantWomen() {
        parentList = sqliteHelper.getParents();
        if (parentList.size() < 1) {
            pregnantWomenList = sqliteHelper.getPregnantWomen();
            if (pregnantWomenList.size()>0){
                for (int i = 0; i < pregnantWomenList.size(); i++) {
                    String pregnantWomenId=pregnantWomenList.get(i).getPregnant_women_id();
                    String server_id = sqliteHelper.getServerId(String.valueOf(pregnantWomenList.get(i).getParent_id()))+"";
                    pregnantWomenList.get(i).setParent_id(Integer.parseInt(server_id));

                    Gson gson = new Gson();
                    String data = gson.toJson(pregnantWomenList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendPregnantWomenDataOnServer(body, pregnantWomenId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, getString(R.string.please_ipload_family_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendPregnantWomenDataOnServer(RequestBody body, String pregnantWomenId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callPregnantApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendPWOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "pregnant_women",
                                        inserted_id,
                                        " pregnant_women_id ="
                                                + pregnantWomenId, "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countPW>0){
                                    countPW=countPW-1;
                                    uploadwomenregistration.setText(strPregnantWomen + "\n" + strPendingDataIs + " " + countPW+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendMother() {
        parentList = sqliteHelper.getParents();
        if (parentList.size() < 1) {
            motherList = sqliteHelper.getMother();
            if (motherList.size()>0){
                for (int i = 0; i < motherList.size(); i++) {
                    String motherId=motherList.get(i).getMother_id();
                    String server_id = sqliteHelper.getServerId(String.valueOf(motherList.get(i).getParent_id()))+"";
                    motherList.get(i).setParent_id(Integer.parseInt(server_id));

                    Gson gson = new Gson();
                    String data = gson.toJson(motherList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendMOtherDataOnServer(body, motherId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, getString(R.string.please_ipload_family_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendMOtherDataOnServer(RequestBody body, String motherId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callMotherApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendMotherOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "mother",
                                        inserted_id,
                                        " mother_id ="
                                                + motherId, "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countMother>0){
                                    countMother=countMother-1;
                                    uploadmotherregistration.setText(strMother + "\n" + strPendingDataIs + " " + countMother+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendChildRegistrationData() {
        parentList = sqliteHelper.getParents();
        pregnantWomenList = sqliteHelper.getPregnantWomen();
        motherList = sqliteHelper.getMother();
        if (parentList.size() < 1 && pregnantWomenList.size() < 1 && motherList.size() < 1) {
            childList = sqliteHelper.getChildren("");
            if (childList.size()>0){
                for (int i = 0; i < childList.size(); i++) {
                    String childId= String.valueOf(childList.get(i).getChild_id());
                    String server_id = sqliteHelper.getServerId(String.valueOf(childList.get(i).getParent_id()))+"";
                    childList.get(i).setParent_id(Integer.parseInt(server_id));
                    String pm_server_id = sqliteHelper.getPWMServerId(childList.get(i).getPregnent_women_id(), childList.get(i).getSelected_women_type())+"";
                    childList.get(i).setPregnent_women_id(pm_server_id);

                    Gson gson = new Gson();
                    String data = gson.toJson(childList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendChildDataOnServer(body, childId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, getString(R.string.please_ipload_family_data), Toast.LENGTH_LONG).show();
            }
            if (pregnantWomenList.size() > 0) {
                Toast.makeText(this, R.string.please_upload_pregenanat_women_data, Toast.LENGTH_LONG).show();
            }
            if (motherList.size() > 0) {
                Toast.makeText(this, R.string.please_upload_mother_data, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.please_upload_pregnanat_and_mother_data, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendChildDataOnServer(RequestBody body, String childId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callChildApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendChildOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "child", inserted_id,

                                        " child_id =" + childId, "photopath");

                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if(countChild>0){
                                    countChild=countChild-1;
                                    uploadchild.setText(strChild + "\n" + strPendingDataIs + " " + countChild+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendAdolscentGirlNutrition() {
        parentList = sqliteHelper.getParents();
        AdolescentGirlList = sqliteHelper.getAdolscentGirl();
        if (parentList.size() < 1 && AdolescentGirlList.size() < 1) {
            AdolescentNutritionMoniterList = sqliteHelper.getAdolescentGirlMonitor();
            if (AdolescentNutritionMoniterList.size()>0){
                for (int i = 0; i < AdolescentNutritionMoniterList.size(); i++) {
                    String adolescentMonitoringId=AdolescentNutritionMoniterList.get(i).getAdolescent_nutrition_id();
                    String server_id = sqliteHelper.getServerId("adolescent",
                            "adolescent_id",
                            String.valueOf(AdolescentNutritionMoniterList.get(i).getAdolescent_id()))
                            + "";
                    AdolescentNutritionMoniterList.get(i).setAdolescent_id(server_id);

                    Gson gson = new Gson();
                    String data = gson.toJson(AdolescentNutritionMoniterList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendAdolescentMonitoringDataOnServer(body,adolescentMonitoringId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, getString(R.string.please_ipload_family_data), Toast.LENGTH_LONG).show();
            }
            if (AdolescentGirlList.size() > 0) {
                Toast.makeText(this, R.string.please_upload_adolescent_data, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendAdolescentMonitoringDataOnServer(RequestBody body, String adolescentMonitoringId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callAdolescentMonitoringApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendAdolMOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1")||success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer(
                                        "adolescent_monitoring", inserted_id, " adolescent_nutrition_id ="
                                                + adolescentMonitoringId, "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if (countAdolescentMonitoring>0){
                                    countAdolescentMonitoring=countAdolescentMonitoring-1;
                                    uploadAdolescentMonitoring.setText(strAdlNut + "\n" + strPendingDataIs + " " + countAdolescentMonitoring+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendPendingAwcData() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {

                //phd.setMax(1);
                String s = "";
                s = serverhelper.UploadAwcPendingData();
                return s;
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //phd.setProgress(phd.getMax());

                if (result != null) {

                    if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)) > 0) {
                        //phd.dismiss();
                        uploadAwcDetails.setText(strUploadPendingAwcDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
                    } else {
                        //phd.dismiss();
                        uploadAwcDetails.setText(strUploadAwcDetails);
                    }
                } else if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)) > 0) {
                    //phd.dismiss();
                    uploadAwcDetails.setText(strUploadPendingAwcDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
                }
                //phd.dismiss();
            }
        }.execute((String) null);

    }

    public void SendPendingWorkerData() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... arg0) {

                //phd.setMax(1);
                String s = "";
                s = serverhelper.UploadWorkerPendingData();
                return s;
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);
                //phd.setProgress(phd.getMax());

                if (result != null) {

                    if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)) > 0) {
                        //phd.dismiss();
                        btnUploadWorkerDetails.setText(strUploadPendingWorkerDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
                    } else {
                        //phd.dismiss();
                        btnUploadWorkerDetails.setText(strUploadWorkerDetails);
                    }
                } else if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)) > 0) {
                    //phd.dismiss();
                    btnUploadWorkerDetails.setText(strUploadPendingWorkerDetails + " : " + sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id));
                }
                //phd.dismiss();
            }
        }.execute((String) null);

    }

    private void SendAdolscentGirl() {
        parentList = sqliteHelper.getParents();
        if (parentList.size() < 1) {
            AdolescentGirlList = sqliteHelper.getAdolscentGirl();
            if (AdolescentGirlList.size()>0){
                for (int i = 0; i < AdolescentGirlList.size(); i++) {
                    String adolescentId= String.valueOf(AdolescentGirlList.get(i).getAdolescent_id());
                    String server_id = sqliteHelper.getServerId(AdolescentGirlList.get(i).getParent_id())+"";
                    AdolescentGirlList.get(i).setParent_id(server_id);

                    Gson gson = new Gson();
                    String data = gson.toJson(AdolescentGirlList.get(i));
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, data);

                    sendAdolescentDataOnServer(body, adolescentId);
                }
            }
        } else {
            //phd.dismiss();
            if (parentList.size() > 0) {
                Toast.makeText(this, getString(R.string.please_ipload_family_data), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendAdolescentDataOnServer(RequestBody body, String adolescentId) {
        ProgressDialog mProgressDialog = ProgressDialog.show(context, "", getString(R.string.please_wait), true);
        ApiClient.getClient().create(M_Health_API.class).callAdolescentApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.e("sendAdolOnServer>>>", "onResponse: "+jsonObject.toString());
                    mProgressDialog.dismiss();
                    String success=jsonObject.getString("success");
                    if (success.equals("1") || success.equals("2")) {
                        String message=jsonObject.getString("message");
                        String server_id=jsonObject.getString("server_id");
                        String created_on=jsonObject.getString("created_on");
                        Toast.makeText(context, R.string.save_successfully, Toast.LENGTH_SHORT).show();
                        if (server_id!=null) {
                            int serverId = Integer.parseInt(server_id);
                            String inserted_id="";
                            if (serverId > 0) {
                                inserted_id = serverId + "";
                                long id = sqliteHelper.updateAndChangeServer("adolescent",
                                        inserted_id,
                                        "adolescent_id =" + adolescentId, "img");
                                Log.e("updateServerId>>>", "onResponse: "+id);
                                if (countAdolescent>0){
                                    countAdolescent=countAdolescent-1;
                                    uploadAdolscentGirl.setText(strAdolescent + "\n" + strPendingDataIs + " " + countAdolescent+"");
                                }
                            } else {
                            }
                        }
                    } else {
                        mProgressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SyncActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.uploadEligiblefamily:
                if (parentList.size() > 0) {
                    /*phd.setMessage("Eligible family uploading.....");
                    phd.show();*/
                    SendDataOnServer();
                } else if (parentGPSUpdateList.size() > 0) {
                    UpdateGPSForEligible();
                }
                break;
            case R.id.uploadchild:
                if (childList.size() > 0) {
                    /*phd.setMessage("Child Registration Data Uploading...");
                    phd.show();*/
                    SendChildRegistrationData();
                }
                break;
            case R.id.uploadchiuldnutrition:
                if (childNutritionList.size() > 0) {
                    /*phd.setMessage("Child Nutrition Data Uploading...");
                    phd.show();*/
                    SendChildNutrition();
                }
                break;
            case R.id.uploadwomenregistration:
                if (pregnantWomenList.size() > 0) {
                    /*phd.setMessage("Pregnant Women Registration Data Uploading...");
                    phd.show();*/
                    SendPregnantWomen();
                }
                break;

                case R.id.uploadChildBehaviourChange:
                if (childBehaviourChangeList.size() > 0) {
                    /*phd.setMessage("Pregnant Women Registration Data Uploading...");
                    phd.show();*/
                    SendChildBehaviourChange();
                }
                break;

                case R.id.uploadEventMeeting:
                if (eventDetailsModelArrayList.size() > 0) {
                    /*phd.setMessage("Pregnant Women Registration Data Uploading...");
                    phd.show();*/
                    SendEventMeetings();
                }
                break;
            case R.id.uploadmotherregistration:
                if (motherList.size() > 0) {
                    /*phd.setMessage("Mother Registration Data Uploading...");
                    phd.show();*/
                    SendMother();
                }
                break;
            case R.id.uploadwomenmonitoring:
                if (womenNutritionList.size() > 0) {
                    /*phd.setMessage("Pregnant Women Monitoring Data Uploading...");
                    phd.show();*/
                    SendWomenNutrition();
                }
                break;
            case R.id.uploadAdolscentGirl:
                if (AdolescentGirlList.size() > 0) {
                    /*phd.setMessage("Adolescent Girl Registration Data Uploading...");
                    phd.show();*/
                    SendAdolscentGirl();
                }
                break;
            case R.id.uploadAdolescentMonitoring:
                if (AdolescentNutritionMoniterList.size() > 0) {
                    /*phd.setMessage("Adolescent Girl Monitoring Data Uploading...");
                    phd.show();*/
                    SendAdolscentGirlNutrition();
                }
                break;

            case R.id.btnUploadAwcDetails:
                if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)) > 0) {
                    /*phd.setMessage("Anganwadi Pending Data...");
                    phd.show();*/
                    SendPendingAwcData();
                }
                break;

            case R.id.btnUploadWorkerDetails:
                if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)) > 0) {
                    /*phd.setMessage("Anganwadi Pending Data...");
                    phd.show();*/
                    SendPendingWorkerData();
                }
                break;
            case R.id.btnUploadAdolescentBaseline:
                if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "adolscent_baseline", "(status = 0) and anganwadi_center_id = " + user_id)) > 0) {
                    /*phd.setMessage("Adolescent Girl Baseline Pending Data...");
                    phd.show();*/
                    SendAdolscentBaselineData();
                }
                break;
            case R.id.btnUploadPWBaseline:
                if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "pregnant_women_baseline", "(status = 0) and anganwadi_center_id = " + user_id)) > 0) {
                    /*phd.setMessage("Pregnant Women Baseline Pending Data...");
                    phd.show();*/
                    SendPregnantWomenBaselineData();
                }
                break;
            case R.id.btnDwnEligibleFamily:

                // Toast.makeText(this, "reached", Toast.LENGTH_SHORT).show();
                GetEligibleFamilyByAganwariId();
                break;

        }
    }

    private void SendPregnantWomenBaselineData() {
        PregnantWomenBaselineList = sqliteHelper.getPregnantwomenBaselineData();
        if (PregnantWomenBaselineList.size()>0) {
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... arg0) {
                    //phd.setMax(PregnantWomenBaselineList.size());
                    int k = 1;
                    String result = "";
                    int resultTemp = 0;
                    for (int i = 0; i < PregnantWomenBaselineList.size(); i++) {
                        String   s = serverhelper.UploadPregnantWomenBaselineData(PregnantWomenBaselineList.get(i));
                        if (s != "") {
                            resultTemp++;
                            //phd.setProgress(k);
                            k++;
                        }
                    }
                    result=String.valueOf(resultTemp) ;
                    return result;

                }

                @Override
                protected void onPostExecute(String result) {

                    super.onPostExecute(result);
                    //phd.setProgress(phd.getMax());

                    if (result != null) {

                        if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "pregnant_women_baseline", "(status = 0 ) and anganwadi_center_id = " + user_id)) > 0) {
                            //phd.dismiss();
                            btnUploadPWBaseline.setText(strPregnantWomenBaseline + " : " + sqliteHelper.GetOneData("count(*)", "pregnant_women_baseline", "(status = 0) and anganwadi_center_id = " + user_id));
                        } else {
                            //phd.dismiss();
                            btnUploadPWBaseline.setText(strPregnantWomenBaseline);
                        }
                    } else if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "pregnant_women_baseline", "(status = 0) and anganwadi_center_id = " + user_id)) > 0) {
                        //phd.dismiss();
                        btnUploadPWBaseline.setText(strPregnantWomenBaseline + " : " + sqliteHelper.GetOneData("count(*)", "pregnant_women_baseline", "(status = 0) and anganwadi_center_id = " + user_id));
                    }
                    //phd.dismiss();
                }
            }.execute((String) null);

        }
    }

    private void SendAdolscentBaselineData() {
        AdolescentBaselineList = sqliteHelper.getAdolscentBaselineData();
        if (AdolescentBaselineList.size()>0) {
            new AsyncTask<String, String, String>() {
                @Override
                protected String doInBackground(String... arg0) {
                    //phd.setMax(AdolescentNutritionMoniterList.size());
                    int k = 1;
                    String result = "";
                    int resultTemp = 0;
                    for (int i = 0; i < AdolescentBaselineList.size(); i++) {
                        String   s = serverhelper.UploadAdolscentBaselineData(AdolescentBaselineList.get(i));
                        if (s != "") {
                            resultTemp++;
                            //phd.setProgress(k);
                            k++;
                        }
                    }
                    result=String.valueOf(resultTemp) ;
                    return result;

                }

                @Override
                protected void onPostExecute(String result) {

                    super.onPostExecute(result);
                    //phd.setProgress(phd.getMax());

                    if (result != null) {

                        if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "adolscent_baseline", "(status = 0 ) and anganwadi_center_id = " + user_id)) > 0) {
                            //phd.dismiss();
                            btnUploadAdolescentBaseline.setText(strAdolescentBaseline + " : " + sqliteHelper.GetOneData("count(*)", "adolscent_baseline", "(status = 0) and anganwadi_center_id = " + user_id));
                        } else {
                            //phd.dismiss();
                            btnUploadAdolescentBaseline.setText(strAdolescentBaseline);
                        }
                    } else if (Integer.parseInt(sqliteHelper.GetOneData("count(*)", "adolscent_baseline", "(status = 0) and anganwadi_center_id = " + user_id)) > 0) {
                        //phd.dismiss();
                        btnUploadAdolescentBaseline.setText(strAdolescentBaseline + " : " + sqliteHelper.GetOneData("count(*)", "adolscent_baseline", "(status = 0) and anganwadi_center_id = " + user_id));
                    }
                    //phd.dismiss();
                }
            }.execute((String) null);

        }
    }

    private void GetEligibleFamilyByAganwariId() {
        // TODO Auto-generated method stub

        new BackwardSyncEligibleFamilyTask().execute("", "", "");

    }

    public void click_download(View v) {
        downloadDialog = new Dialog(this);
        downloadDialog.setContentView(R.layout.download_dialog);
        downloadDialog.setTitle(R.string.downloading);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        pbf = (ProgressBar) downloadDialog.findViewById(R.id.pbFamily);
        pbmpw = (ProgressBar) downloadDialog.findViewById(R.id.pbMother);
        pbm = (ProgressBar) downloadDialog.findViewById(R.id.pbmMother);
        pbpwm = (ProgressBar) downloadDialog.findViewById(R.id.pbPregMon);
        pbcr = (ProgressBar) downloadDialog.findViewById(R.id.pbChildReg);
        pbcm = (ProgressBar) downloadDialog.findViewById(R.id.pbChildMon);
        pbagr = (ProgressBar) downloadDialog.findViewById(R.id.pbAdolReg);
        pbagm = (ProgressBar) downloadDialog.findViewById(R.id.pbAdolMon);
        new BackwardSyncParentTask().execute("", "", "");
    }

    public void click_Event_download(View v) {
        downloadDialog = new Dialog(this);
        downloadDialog.setContentView(R.layout.download_event_dialog);
        downloadDialog.setTitle(R.string.downloading);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        pbEveMee = (ProgressBar) downloadDialog.findViewById(R.id.pbEveMee);
        new BackwardSyncParentTask().execute("", "", "");
    }

    public void click_list(View vw) {
        switch (vw.getId()) {

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle("Information");
        builder.setMessage(strCancel + " " + strSync + "?");

        builder.setPositiveButton(strNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(strYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(SyncActivity.this,
                        MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void uploadAttendance() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            new sync_attendance(this).execute("");
        }
    }

    public class BackwardSyncEligibleFamilyTask extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*phd.setMessage(strDownELFaomilyData);
            phd.setIndeterminate(true);
            phd.show();*/
        }

        @Override
        protected String doInBackground(String... urls) {
            // TODO Auto-generated method stub
            String response = "";
            //			response = new ServerHelper().GetEligibleFamilyList(GlobalVars.UserID);
            response = serverhelper.downloadGetEligibleFamilyListi(GlobalVars.lattitude, GlobalVars.longitude, user_id, sqliteHelper);
            GlobalVars.familystatus = 1;
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            /*phd.setIndeterminate(false);
            phd.dismiss();*/
            GlobalVars.EFposition = 1;
            Intent intent1 = new Intent(SyncActivity.this, Activity_parentList.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public class BackwardSyncParentTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String total_response = "";
            String familyrResponse = "";
            String womenResponse = "";
            String motherResponse = "";
            String womenMonResponse = "";
            String childResponse = "";
            String childMonResponse = "";
            String adolescentResponse = "";
            String adolescentMonResponse = "";
            String eventMeetingResponse = "";

            String[] house_no = sqliteHelper.GetManyData("house_number", "eligible_family", "anganwadi_center_id = " + user_id);
            String[] server_id = sqliteHelper.GetManyData("server_id", "pregnant_women", "anganwadi_center_id = " + user_id);
            String[] women_mon_id = sqliteHelper.GetManyData("server_monitoring_id", "pregnant_womem_monitor", "anganwadi_center_id = " + user_id);
            String[] child_server_id = sqliteHelper.GetManyData("server_id", "child", "anganwadi_center_id = " + user_id);
            String[] child_mon_server_id = sqliteHelper.GetManyData("server_nutrition_id", "child_nutrition_monitoring", "anganwadi_center_id = " + user_id);
            String[] adolescent_server_id = sqliteHelper.GetManyData("server_id", "adolescent", "anganwadi_center_id = " + user_id);
            String[] adolescent_server_mon_id = sqliteHelper.GetManyData("girl_nutrition_server_id", "adolescent_monitoring", "anganwadi_center_id = " + user_id);

            /*familyrResponse = serverhelper.DownloadAnganwadiData(user_id,house_no);
            womenResponse = serverhelper.DownloadPregnantWomen(user_id, server_id);
            womenMonResponse = serverhelper.DownloadPregnantWomenMon(user_id, women_mon_id);
            childResponse = serverhelper.DownloadChild(user_id, child_server_id);
            childMonResponse = serverhelper.DownloadChildMon(user_id, child_mon_server_id);
            adolescentResponse = serverhelper.DownloadAdolecent(user_id, adolescent_server_id);
            adolescentMonResponse = serverhelper.DownloadAdolecentMon(user_id, adolescent_server_mon_id);*/
            familyrResponse = serverhelper.DownloadAnganwadiData(user_id);
            womenResponse = serverhelper.DownloadPregnantWomen(user_id);
            motherResponse = serverhelper.DownloadMother(user_id);
            womenMonResponse = serverhelper.DownloadPregnantWomenMon(user_id);
            childResponse = serverhelper.DownloadChild(user_id);
            childMonResponse = serverhelper.DownloadChildMon(user_id);
            adolescentResponse = serverhelper.DownloadAdolecent(user_id);
            adolescentMonResponse = serverhelper.DownloadAdolecentMon(user_id);
            eventMeetingResponse = serverhelper.DownloadEventMeetingData(CommonMethods.getCurrentDateTime());

           /* if (familyrResponse == "" && womenResponse == "" && womenMonResponse == "" && childResponse == "" && childMonResponse == "" && adolescentResponse == "" && adolescentMonResponse == "" && eventMeetingResponse=="") {
                total_response = "already downloaded";
            } else {
                total_response = familyrResponse + "," + womenResponse + "," + womenMonResponse + "," + childResponse + "," + childMonResponse + "," + adolescentResponse + "," + adolescentMonResponse + "," + eventMeetingResponse + "downloaded successfully!";
            }*/
            return total_response;
        }

        @Override
        protected void onPostExecute(String result) {
            String lngTypt = sph.getString("languageID", "en");
            if (lngTypt.equals("1")){
                Toast.makeText(getApplicationContext(), "All data download successfully.", Toast.LENGTH_LONG).show();
        }else{
                Toast.makeText(getApplicationContext(), "सभी डेटा सफलतापूर्वक डाउनलोड।", Toast.LENGTH_LONG).show();

            }
            downloadDialog.dismiss();
        }
    }

    public class BackwardSyncPWTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            response = serverhelper.GetPregnantList(user_id);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                    .show();
        }
    }

    public class BackwardSyncChildTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*phd.setMessage(strDownloadAllData);
            phd.setIndeterminate(true);
            phd.show();*/
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            response = serverhelper.GetChildList(user_id);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            /*phd.setIndeterminate(false);
            phd.dismiss();*/
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }


    private void setLanguage(String languageToLoad) {
        if (!languageToLoad.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                /*Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Resources resources = getBaseContext().getResources();
                Configuration config = resources.getConfiguration();
                config.setLocale(locale);
                resources.updateConfiguration(config, resources.getDisplayMetrics());*/

                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = getBaseContext().getResources().getConfiguration();
                //config.locale = locale;
                Locale current = getBaseContext().getResources().getConfiguration().locale;
                String lang1=current.getLanguage();
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Locale currentn = getBaseContext().getResources().getConfiguration().locale;
                String lang=currentn.getLanguage();
                Log.e("Lang>>>","lang1>>>"+lang1+">>>lang>>>"+lang);
            } else {
                Resources resources = getBaseContext().getResources();
                Configuration configuration = resources.getConfiguration();
                //configuration.setLocale(new Locale(lang));
                configuration.locale = new Locale(languageToLoad);
                getBaseContext().getApplicationContext().createConfigurationContext(configuration);
            }
        }
    }

}
