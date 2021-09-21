package com.example.mhealth.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mhealth.SyncActivity;
import com.example.mhealth.model.DownloadData;
import com.example.mhealth.rest_apis.ApiClient;
import com.example.mhealth.rest_apis.M_Health_API;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerHelper {

    SqliteHelper ObjsqliteHelper;
    Context context;
    String url = "http://115.249.99.236/api/";

    SharedPrefHelper sph;
    int user_id;
    int user_master_id;
    int varDownChunk = 50;

    long parentdownId=0;
    String strParentdownId="";
    long motherid=0;
    String strMotherid="";
    long pregnentwomenid=0;
    String strPregnentwomenid="";
    long pregnentwomenNutid=0;
    String strPregnentwomenNutid="";
    long childregId=0;
    String strChildregId="";
    long childNutId = 0;
    String strChildNutId = "";
    long aId = 0;
    String strAId = "";
    long amonId = 0;
    String strAMonId = "";
    long eventID = 0;
    String streventMeetingId = "";

    long motherID = 0;
    String strmotherId = "";

    public ServerHelper(Context context) {
        this.context = context;
        ObjsqliteHelper = new SqliteHelper(context);
        sph = new SharedPrefHelper(context);
        user_id = sph.getInt("user_id", 0);
        user_master_id = sph.getInt("user_master_id", 0);
    }

    public String PregmentWomenRegistration(PregnantWomen pregnantWomen) {
        String inserted_id = "";
        if (pregnantWomen.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "preg_women_age.php");
            String line = "";
            String json = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
                nameValuePairs.add(new BasicNameValuePair("house_number",
                        pregnantWomen.getHhId()));
                Log.e("..............", pregnantWomen.getPreWomenName());
                nameValuePairs.add(new BasicNameValuePair("name_of_pregnant_women",
                        pregnantWomen.getPreWomenName()));
                nameValuePairs.add(new BasicNameValuePair("mobile_number", ""));
                nameValuePairs.add(new BasicNameValuePair("date_of_registration",
                        pregnantWomen.getDate_of_registration()));
                nameValuePairs.add(new BasicNameValuePair("place_of_delivery", ""));
                nameValuePairs.add(new BasicNameValuePair("lmp_date", pregnantWomen
                        .getLmp_date()));
                nameValuePairs.add(new BasicNameValuePair("edd", pregnantWomen
                        .getEdd()));
                nameValuePairs.add(new BasicNameValuePair("parent_id",
                        pregnantWomen.getParent_id() + ""));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("height", pregnantWomen
                        .getHeight()));
                nameValuePairs.add(new BasicNameValuePair("weight", pregnantWomen
                        .getWeight()));
                nameValuePairs.add(new BasicNameValuePair("hb", pregnantWomen
                        .getHb()));
                nameValuePairs.add(new BasicNameValuePair("systolic_bp", pregnantWomen
                        .getSysbp()));
                nameValuePairs.add(new BasicNameValuePair("diastolic_bp", pregnantWomen
                        .getDiasbp()));
                nameValuePairs.add(new BasicNameValuePair("lattitude",
                        pregnantWomen.getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude",
                        pregnantWomen.getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("husband_father_name",
                        pregnantWomen.getHusbandName()));
                Log.e(".............", pregnantWomen.getAgeofPW());
                nameValuePairs.add(new BasicNameValuePair("age",
                        pregnantWomen.getAgeofPW()));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                nameValuePairs.add(new BasicNameValuePair("img",
                        pregnantWomen.getImage()));

                for (int i = 0; i < nameValuePairs.size(); i++)
                    //Log.e("values", nameValuePairs.get(i).toString());

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    JSONObject user = new JSONObject(json);
                    if (user.has("user_id")) {
                        int user_id = user.getInt("user_id");
                        if (user_id > 0) {

                            inserted_id = user_id + "";

                            long id = ObjsqliteHelper.updateAndChangeServer(
                                    "pregnant_women",
                                    inserted_id,
                                    " pregnant_women_id ="
                                            + pregnantWomen.getPregnant_women_id(), "img");

                            if (id > 0) {
                                //Log.d("igg", "done");
                            }

                        } else {
                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }
        }
        if (pregnantWomen.getStatus() == 5) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "mother_to_preg_women.php");
            String line = "";
            String json = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);
                nameValuePairs.add(new BasicNameValuePair("lmp_date", pregnantWomen
                        .getLmp_date()));
                nameValuePairs.add(new BasicNameValuePair("edd", pregnantWomen
                        .getEdd()));
                nameValuePairs.add(new BasicNameValuePair("parent_id",
                        pregnantWomen.getParent_id() + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("height", pregnantWomen
                        .getHeight()));
                nameValuePairs.add(new BasicNameValuePair("weight", pregnantWomen
                        .getWeight()));
                nameValuePairs.add(new BasicNameValuePair("hb", pregnantWomen
                        .getHb()));
                nameValuePairs.add(new BasicNameValuePair("systolic_bp", pregnantWomen
                        .getSysbp()));
                nameValuePairs.add(new BasicNameValuePair("diastolic_bp", pregnantWomen
                        .getDiasbp()));
                nameValuePairs.add(new BasicNameValuePair("lattitude",
                        pregnantWomen.getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude",
                        pregnantWomen.getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                nameValuePairs.add(new BasicNameValuePair("img",
                        pregnantWomen.getImage()));

                nameValuePairs.add(new BasicNameValuePair("server_id",
                        Integer.toString(pregnantWomen.getServer_id())));

                for (int i = 0; i < nameValuePairs.size(); i++)
                    //Log.e("values", nameValuePairs.get(i).toString());

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();


                try {

                    ObjsqliteHelper.updateAndChangeServer("pregnant_women", Integer.toString(pregnantWomen.getServer_id()), " pregnant_women_id =" + pregnantWomen.getPregnant_women_id(), "img");

                } catch (Exception e) {
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return inserted_id;
    }

    public String MotherRegistration(Mother mother) {

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "mother_reg_img.php");
        String line = "";
        String json = "";
        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
            nameValuePairs.add(new BasicNameValuePair("house_number", mother.getHhId()));
            nameValuePairs.add(new BasicNameValuePair("name_of_pregnant_women", mother.getMotherName()));
            nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id", user_id + ""));
            nameValuePairs.add(new BasicNameValuePair("user_master_id", user_master_id + ""));
            nameValuePairs.add(new BasicNameValuePair("latitude", mother.getLatitude()));
            nameValuePairs.add(new BasicNameValuePair("longitude", mother.getLongitude()));
            nameValuePairs.add(new BasicNameValuePair("date_of_registration", mother.getDate_of_registration()));
            nameValuePairs.add(new BasicNameValuePair("husband_father_name", mother.getMotherhusbandName()));
            nameValuePairs.add(new BasicNameValuePair("age", mother.getAge()));
            nameValuePairs.add(new BasicNameValuePair("child_number", mother.getNumber_of_child()));
            nameValuePairs.add(new BasicNameValuePair("unmarried", mother.getUnmarried()));
            nameValuePairs.add(new BasicNameValuePair("widow", mother.getWidow()));
            nameValuePairs.add(new BasicNameValuePair("married", mother.getMarried()));
            nameValuePairs.add(new BasicNameValuePair("flag", mother.getFlag()));
            nameValuePairs.add(new BasicNameValuePair("app_version", GlobalVars.App_Version));
            nameValuePairs.add(new BasicNameValuePair("img", mother.getImage()));


            //for (int i = 0; i < nameValuePairs.size(); i++)
            //	Log.e("values", nameValuePairs.get(i).toString());

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            Log.e("json", json);
            rd.close();

            int a = json.lastIndexOf(":");
            int b = json.lastIndexOf("}");
            inserted_id = json.substring(a + 1, b);
            /*JSONObject user = new JSONObject(json);
            if (user.has("user_id")) {
				int user_id = user.getInt("user_id");
				if (user_id > 0) {

					inserted_id = user_id + "";*/

            long id = ObjsqliteHelper.updateAndChangeServer(
                    "pregnant_women",
                    inserted_id,
                    " pregnant_women_id ="
                            + mother.getMother_id(), "img");

            if (id > 0) {
                Log.d("igg", "done");
            }

        } catch (ClientProtocolException e) {

        } catch (IOException e) {
        }
        return inserted_id;
    }

    public String ChangeDateFormate(String fdate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(Date.parse(fdate));
        return date;
    }

    public String ChildRegistration(Child child) {

        String inserted_id = "";
        if (child.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "child_reg_adol.php");

            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);

                nameValuePairs.add(new BasicNameValuePair("name_of_child", child
                        .getChild_name()));
                nameValuePairs.add(new BasicNameValuePair("date_of_birth", child
                        .getDate_of_birth().toString()));
                nameValuePairs.add(new BasicNameValuePair("gender", child
                        .getGender()));
                nameValuePairs.add(new BasicNameValuePair("birth_status", child
                        .getBirth_order() + ""));
                nameValuePairs.add(new BasicNameValuePair("birth_muac", child
                        .getMuac() + ""));
                nameValuePairs.add(new BasicNameValuePair("hb", child
                        .getHB() + ""));
                nameValuePairs.add(new BasicNameValuePair("have_edema", child
                        .getEdema() + ""));
                nameValuePairs.add(new BasicNameValuePair("lattitude", child
                        .getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude", child
                        .getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("image", child
                        .getMultimedia()));
                int server_id = ObjsqliteHelper.getServerId(child.getParent_id()
                        + "");

                nameValuePairs.add(new BasicNameValuePair("parent_id", server_id
                        + ""));

                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("house_number", child
                        .getHHID() + ""));
                nameValuePairs.add(new BasicNameValuePair("Dateofregistration",
                        child.getDate_of_registration()));
                nameValuePairs.add(new BasicNameValuePair("birth_weight", child
                        .getChild_weight() + ""));
                nameValuePairs.add(new BasicNameValuePair("birth_height", child
                        .getHeight() + ""));
                nameValuePairs.add(new BasicNameValuePair("is_disability", child
                        .getDisablity() + ""));

                String noimage = "";
                switch (Integer.parseInt(child.getGender())) {
                    case 1:
                        noimage = "noimage_m.png";
                        break;

                    case 2:
                        noimage = "noimage_f.png";
                        break;

                    case 3:
                        noimage = "noimage_o.png";
                        break;

                    default:
                        break;
                }


                nameValuePairs.add(new BasicNameValuePair("photopath", noimage));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                if (child.getCheck_adol() != 1) {
                    int pregnent_women_id = ObjsqliteHelper.getServerId(
                            "pregnant_women", "pregnant_women_id",
                            child.getPregnent_women_id());
                    nameValuePairs.add(new BasicNameValuePair("pregnent_women_id",
                            pregnent_women_id + ""));
                } else {
                    int girl_id = ObjsqliteHelper.getServerId(
                            "adolescent", "adolescent_id",
                            child.getPregnent_women_id());
                    nameValuePairs.add(new BasicNameValuePair("girl_id",
                            girl_id + ""));
                }

                nameValuePairs.add(new BasicNameValuePair("check_adol", "" + child.getCheck_adol()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");
                    inserted_id = json.substring(a + 1, b);
                    if (!inserted_id.equalsIgnoreCase("0")) {
                        long id = ObjsqliteHelper.updateAndChangeServer(
                                "child", inserted_id,

                                " child_id =" + child.getChild_id(), "photopath");

                        if (id > 0) {
                            Log.d("Updated", "Updated successfully");
                        }
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        else {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "child_reg_edit.php");

            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);
                int child_server_id = ObjsqliteHelper.getServerId("child", "child_id", String.valueOf(child.getChild_id()));
                nameValuePairs.add(new BasicNameValuePair("child_id",
                        String.valueOf(child_server_id)));
                nameValuePairs.add(new BasicNameValuePair("name_of_child", child
                        .getChild_name()));
                nameValuePairs.add(new BasicNameValuePair("hb", child
                        .getHB() + ""));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("house_number", child
                        .getHHID() + ""));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");
                    inserted_id = json.substring(a + 1, b);

                    long id = ObjsqliteHelper.updateChangeServer(
                            "child", inserted_id,

                            " child_id =" + child.getChild_id());

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return inserted_id;
    }

    public String EligibleFamilyRegistration(Parent parent) {
        String inserted_id = "";

        if (parent.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "eligiblefamily_img_second.php");

            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        20);



                nameValuePairs.add(new BasicNameValuePair("house_number", parent
                        .getHouseNo()));
                nameValuePairs.add(new BasicNameValuePair("latitude", parent
                        .getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude", parent
                        .getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("head_family", parent
                        .getHeadofHH()));
                nameValuePairs.add(new BasicNameValuePair("toilet_type", parent
                        .getHas_toilet() + ""));
                nameValuePairs.add(new BasicNameValuePair("water_source", parent
                        .getHave_water() + ""));
                nameValuePairs.add(new BasicNameValuePair("religion_id", parent
                        .getReligion() + ""));
                nameValuePairs.add(new BasicNameValuePair("cast_category", parent
                        .getCaste()));
                nameValuePairs.add(new BasicNameValuePair("adhaar_card", parent
                        .getAadharCardHH()));

                nameValuePairs.add(new BasicNameValuePair("mobile_number", parent
                        .getMobileHH()));
                Log.e("mobile_number", parent.MobileHH);
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("gender", parent
                        .getGender()));
                //nameValuePairs.add(new BasicNameValuePair("status", Integer.toString(parent.getStatus())));
                nameValuePairs.add(new BasicNameValuePair("date_of_recording",
                        parent.getDateOfRecord()));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("img", parent.getImage()));
                int cast_id = parent.getCast_id();
                nameValuePairs.add(new BasicNameValuePair("cast_id", cast_id + ""));
                nameValuePairs.add(new BasicNameValuePair("land", parent.getLan_value()));
                nameValuePairs.add(new BasicNameValuePair("ses_mgr", parent.getSes_migration()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity data = response.getEntity();

                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }

                rd.close();

                try {
                    JSONObject user = new JSONObject(json);
                    if (user.has("user_id")) {
                        int intuser_id = user.getInt("user_id");
                        if (intuser_id > 0) {
                            inserted_id = intuser_id + "";
                            long id = ObjsqliteHelper.updateAndChangeServer(
                                    "eligible_family", inserted_id, "anganwadi_center_id="
                                            + user_id + " and house_number ="
                                            + parent.getHouseNo(), "img");
                            //Log.e("id", String.valueOf(id));
                            if (id > 0) {
                                ArrayList<Child> childList = new ArrayList<Child>();

                            }
                        } else {

                        }

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block

                Log.d("Error", e.getMessage());
            }
        }
        else {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "eligible_reg_edit.php");

            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        20);
                nameValuePairs.add(new BasicNameValuePair("house_number", parent
                        .getHouseNo()));
                nameValuePairs.add(new BasicNameValuePair("latitude", parent
                        .getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude", parent
                        .getLongitude()));
                nameValuePairs.add(new BasicNameValuePair("head_family", parent
                        .getHeadofHH()));
                nameValuePairs.add(new BasicNameValuePair("toilet_type", parent
                        .getHas_toilet() + ""));
                nameValuePairs.add(new BasicNameValuePair("water_source", parent
                        .getHave_water() + ""));
                nameValuePairs.add(new BasicNameValuePair("religion_id", parent
                        .getReligion() + ""));
                nameValuePairs.add(new BasicNameValuePair("cast_category", parent
                        .getCaste()));
                nameValuePairs.add(new BasicNameValuePair("adhaar_card", parent
                        .getAadharCardHH()));

                nameValuePairs.add(new BasicNameValuePair("mobile_number", parent
                        .getMobileHH()));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("gender", parent
                        .getGender()));
                nameValuePairs.add(new BasicNameValuePair("img", parent.getImage()));
                int cast_id = parent.getCast_id();
                nameValuePairs.add(new BasicNameValuePair("cast_id", cast_id + ""));
                nameValuePairs.add(new BasicNameValuePair("land", parent.getLan_value()));
                nameValuePairs.add(new BasicNameValuePair("ses_mgr", parent.getSes_migration()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {

                    json += line;
                }

                rd.close();

                try {
                    JSONObject user = new JSONObject(json);
                    if (user.has("user_id")) {
                        int intuser_id = user.getInt("user_id");
                        inserted_id = intuser_id + "";
                        long id = ObjsqliteHelper.updateChangeServer(
                                "eligible_family", inserted_id, "anganwadi_center_id="
                                        + user_id + " and house_number ="
                                        + parent.getHouseNo());
                        //Log.e("id", String.valueOf(id));
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block

                Log.d("Error", e.getMessage());
            }
        }
        return inserted_id;
    }

    public String LoginOnServer(String user_name, String password) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "login.php");
        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(12);
            nameValuePairs.add(new BasicNameValuePair("user_name", user_name));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }

            rd.close();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block

            //Log.d("Error", e.getMessage());
        }

        return json;
    }

    public String ChildNutitionMonitoring(ChildNutrition childnutrition) {
        String inserted_id = "";
        if (childnutrition.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "child_monitoring_dd.php");
            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);
                String server_id = ObjsqliteHelper.getServerId(
                        "child", "child_id",
                        String.valueOf(childnutrition.getChild_id()))
                        + "";

                nameValuePairs.add(new BasicNameValuePair("child_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("migration_type", childnutrition.getMigration()));
                nameValuePairs.add(new BasicNameValuePair("height", childnutrition
                        .getHeight()));
                nameValuePairs.add(new BasicNameValuePair("weight", childnutrition
                        .getWeight()));
                nameValuePairs.add(new BasicNameValuePair("muac", childnutrition
                        .getMuac()));
                nameValuePairs.add(new BasicNameValuePair("hb", childnutrition
                        .getHB()));
                nameValuePairs.add(new BasicNameValuePair("d_reason", childnutrition
                        .getDreason()));
                nameValuePairs.add(new BasicNameValuePair("latitude",
                        childnutrition.getLatitude() + ""));
                nameValuePairs.add(new BasicNameValuePair("longitude",
                        childnutrition.getLongitude() + ""));
                nameValuePairs.add(new BasicNameValuePair("nutrition_id",
                        childnutrition.getNutrition_id() + ""));
                nameValuePairs.add(new BasicNameValuePair("have_adeama",
                        childnutrition.getEdeme()));
                nameValuePairs.add(new BasicNameValuePair("date_of_monitoring",
                        childnutrition.getDate_of_monitoring()));

                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        childnutrition.getAnganwadi_center_id()));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("photopath",
                        ""));
                nameValuePairs.add(new BasicNameValuePair("image", childnutrition
                        .getMultimedia()));

                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));

                nameValuePairs.add(new BasicNameValuePair("sick_y_n", childnutrition.getSickYesNo()));
                nameValuePairs.add(new BasicNameValuePair("sick_name", childnutrition.getSickName()));
                nameValuePairs.add(new BasicNameValuePair("sick_y_n15", childnutrition.getSickYesNo15()));
                nameValuePairs.add(new BasicNameValuePair("sick_name15", childnutrition.getSickName15()));
                nameValuePairs.add(new BasicNameValuePair("dd", childnutrition.getDeathDate()));
                nameValuePairs.add(new BasicNameValuePair("death_reason", childnutrition.getDeathReason()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");
                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper.updateAndChangeServer(
                            "child_nutrition_monitoring", inserted_id,
                            "  nutrition_id =" + childnutrition.getNutrition_id(), "multimedia");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        if (childnutrition.getStatus() == 2) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "child_mon_edit_absent.php");
            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
                String server_id = ObjsqliteHelper.getServerId(
                        "child", "child_id",
                        String.valueOf(childnutrition.getChild_id()))
                        + "";
                String comDate = childnutrition.getDate_of_monitoring().substring(0, 7) + "%";
                Log.e("....................", comDate);

                nameValuePairs.add(new BasicNameValuePair("child_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("migration_type", childnutrition.getMigration()));
                nameValuePairs.add(new BasicNameValuePair("height", childnutrition
                        .getHeight()));
                nameValuePairs.add(new BasicNameValuePair("weight", childnutrition
                        .getWeight()));
                nameValuePairs.add(new BasicNameValuePair("muac", childnutrition
                        .getMuac()));
                nameValuePairs.add(new BasicNameValuePair("hb", childnutrition
                        .getHB()));
                nameValuePairs.add(new BasicNameValuePair("d_reason", childnutrition
                        .getDreason()));
                nameValuePairs.add(new BasicNameValuePair("latitude",
                        childnutrition.getLatitude() + ""));
                nameValuePairs.add(new BasicNameValuePair("longitude",
                        childnutrition.getLongitude() + ""));
                nameValuePairs.add(new BasicNameValuePair("nutrition_id",
                        childnutrition.getNutrition_id() + ""));
                nameValuePairs.add(new BasicNameValuePair("have_adeama",
                        childnutrition.getEdeme()));
                nameValuePairs.add(new BasicNameValuePair("date_of_monitoring",
                        childnutrition.getDate_of_monitoring()));
                nameValuePairs.add(new BasicNameValuePair("compare_date", comDate));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        childnutrition.getAnganwadi_center_id()));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("photopath",
                        ""));
                nameValuePairs.add(new BasicNameValuePair("image", childnutrition
                        .getMultimedia()));

                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");
                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper.updateAndChangeServer(
                            "child_nutrition_monitoring", inserted_id,
                            "  nutrition_id =" + childnutrition.getNutrition_id(), "multimedia");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        else {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "child_monitoring_hbedit.php");
            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);

                String server_id = ObjsqliteHelper.getServerId(
                        "child", "child_id",
                        String.valueOf(childnutrition.getChild_id()))
                        + "";

                nameValuePairs.add(new BasicNameValuePair("child_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("hb", childnutrition
                        .getHB()));
                nameValuePairs.add(new BasicNameValuePair("date_of_monitoring",
                        childnutrition.getDate_of_monitoring()));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        childnutrition.getAnganwadi_center_id()));
                Log.e("Child ID", server_id);
                Log.e("hb", childnutrition.getHB());
                Log.e("Date of recording", childnutrition.getDate_of_monitoring());

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();

                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");
                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper.updateChangeServer(
                            "child_nutrition_monitoring", inserted_id,
                            "  nutrition_id =" + childnutrition.getNutrition_id());

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return inserted_id;
    }

    public String WomenNutitionMonitoring(
            PregnantWomenMonitor pregnantWomenMonitor) {

        String inserted_id = null;
        if (pregnantWomenMonitor.getStatus() == 0) {
            inserted_id = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "preg_women_mon_abort.php");
            String line = "";
            String json = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);

                String server_id = ObjsqliteHelper.getServerId("pregnant_women",
                        "pregnant_women_id",
                        pregnantWomenMonitor.getPregnant_women_id())
                        + "";
                nameValuePairs.add(new BasicNameValuePair("women_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("MGR",
                        pregnantWomenMonitor.getMgrStatus()));
                nameValuePairs.add(new BasicNameValuePair("weight",
                        pregnantWomenMonitor.getWeight()));
                nameValuePairs.add(new BasicNameValuePair("hb",
                        pregnantWomenMonitor.getHb()));
                nameValuePairs.add(new BasicNameValuePair("c_date",
                        ""));
                //nameValuePairs.add(new BasicNameValuePair("bp",pregnantWomenMonitor.getBp()));
                nameValuePairs.add(new BasicNameValuePair("systolic_bp",
                        pregnantWomenMonitor.getSysBp()));
                nameValuePairs.add(new BasicNameValuePair("diastolic_bp",
                        pregnantWomenMonitor.getDiasBp()));
                nameValuePairs.add(new BasicNameValuePair("date_of_recording",
                        pregnantWomenMonitor.getCurrent_date()));// static value
                // need dynamic
                nameValuePairs.add(new BasicNameValuePair("height", ""));// static
                // value
                // need

                nameValuePairs.add(new BasicNameValuePair("cause_date",
                        pregnantWomenMonitor.getCause_date()));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));                                                            // dynamic
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("img",
                        pregnantWomenMonitor.getImage()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");

                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper
                            .updateAndChangeServer(
                                    "pregnant_womem_monitor",
                                    inserted_id,
                                    " monitor_id ="
                                            + pregnantWomenMonitor
                                            .getWomen_monitoring_id(), "img");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        if (pregnantWomenMonitor.getStatus() == 2) {
            inserted_id = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "preg_mon_edit_absent.php");
            String line = "";
            String json = "";
            try {
                String comDate = pregnantWomenMonitor.getCurrent_date().substring(0, 7) + "%";
                Log.e("....................", comDate);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);

                String server_id = ObjsqliteHelper.getServerId("pregnant_women",
                        "pregnant_women_id",
                        pregnantWomenMonitor.getPregnant_women_id())
                        + "";
                nameValuePairs.add(new BasicNameValuePair("women_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("MGR",
                        pregnantWomenMonitor.getMgrStatus()));
                nameValuePairs.add(new BasicNameValuePair("weight",
                        pregnantWomenMonitor.getWeight()));
                nameValuePairs.add(new BasicNameValuePair("hb",
                        pregnantWomenMonitor.getHb()));
                nameValuePairs.add(new BasicNameValuePair("c_date",
                        ""));
                //nameValuePairs.add(new BasicNameValuePair("bp",pregnantWomenMonitor.getBp()));
                nameValuePairs.add(new BasicNameValuePair("systolic_bp",
                        pregnantWomenMonitor.getSysBp()));
                nameValuePairs.add(new BasicNameValuePair("diastolic_bp",
                        pregnantWomenMonitor.getDiasBp()));
                nameValuePairs.add(new BasicNameValuePair("date_of_recording",
                        pregnantWomenMonitor.getCurrent_date()));// static value
                // need dynamic
                nameValuePairs.add(new BasicNameValuePair("height", ""));// static
                // value
                // need
                nameValuePairs.add(new BasicNameValuePair("compare_date", comDate));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));                                                            // dynamic
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("img",
                        pregnantWomenMonitor.getImage()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");

                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper
                            .updateAndChangeServer(
                                    "pregnant_womem_monitor",
                                    inserted_id,
                                    " monitor_id ="
                                            + pregnantWomenMonitor
                                            .getWomen_monitoring_id(), "img");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return inserted_id;
    }

    public String AdolescentNutritionMoniterList(
            AdolescentMonitoring adolescentMonitoring) {
        String inserted_id = "";
        if (adolescentMonitoring.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "adol_mon_marked.php");
            String line = "";
            String json = "";
            try {
                // women_id=1&weight=12&hb=12&c_date=12-12-2004&bp=222&date_of_recording=12-12-2003&height=2

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);
                String server_id = ObjsqliteHelper.getServerId("adolescent",
                        "adolescent_id",
                        String.valueOf(adolescentMonitoring.getAdolescentGirlID()))
                        + "";
                nameValuePairs.add(new BasicNameValuePair("girl_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("weight",
                        adolescentMonitoring.getAdolescentWeight()));
                nameValuePairs.add(new BasicNameValuePair("marked_as",
                        adolescentMonitoring.getMarked_as()));
                nameValuePairs.add(new BasicNameValuePair("hb",
                        adolescentMonitoring.getAdolescentHb()));
                nameValuePairs.add(new BasicNameValuePair("MGR",
                        adolescentMonitoring.getMgrStatus()));
                nameValuePairs.add(new BasicNameValuePair("c_date",
                        adolescentMonitoring.getCurrentDate()));
                nameValuePairs.add(new BasicNameValuePair("bp", adolescentMonitoring.getAdolescentBp()));// static
                // value
                // need
                // dynamic
                nameValuePairs.add(new BasicNameValuePair("date_of_recording",
                        adolescentMonitoring.getDateOfRecord()));// static value
                // need dynamic
                nameValuePairs.add(new BasicNameValuePair("height",
                        adolescentMonitoring.getAdolescentHeight()));// static value
                // need
                // dynamic

                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("img",
                        adolescentMonitoring.getImage()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");

                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper.updateAndChangeServer(
                            "adolescent_monitoring", inserted_id, " monitor_id ="
                                    + adolescentMonitoring.getMonitorID(), "img");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        if (adolescentMonitoring.getStatus() == 2) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "adol_mon_edit_absent.php");
            String line = "";
            String json = "";
            try {
                // women_id=1&weight=12&hb=12&c_date=12-12-2004&bp=222&date_of_recording=12-12-2003&height=2
                String comDate = adolescentMonitoring.getDateOfRecord().substring(0, 7) + "%";
                Log.e("....................", comDate);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        11);
                String server_id = ObjsqliteHelper.getServerId("adolescent",
                        "adolescent_id",
                        String.valueOf(adolescentMonitoring.getAdolescentGirlID()))
                        + "";
                nameValuePairs.add(new BasicNameValuePair("girl_id", server_id));
                nameValuePairs.add(new BasicNameValuePair("weight",
                        adolescentMonitoring.getAdolescentWeight()));
                nameValuePairs.add(new BasicNameValuePair("hb",
                        adolescentMonitoring.getAdolescentHb()));
                nameValuePairs.add(new BasicNameValuePair("MGR",
                        adolescentMonitoring.getMgrStatus()));
                nameValuePairs.add(new BasicNameValuePair("c_date",
                        adolescentMonitoring.getCurrentDate()));
                nameValuePairs.add(new BasicNameValuePair("bp", adolescentMonitoring.getAdolescentBp()));// static
                // value
                // need
                // dynamic
                nameValuePairs.add(new BasicNameValuePair("date_of_recording",
                        adolescentMonitoring.getDateOfRecord()));// static value
                // need dynamic
                nameValuePairs.add(new BasicNameValuePair("height",
                        adolescentMonitoring.getAdolescentHeight()));// static value
                // need
                // dynamic
                nameValuePairs.add(new BasicNameValuePair("compare_date", comDate));
                nameValuePairs.add(new BasicNameValuePair("app_version",
                        GlobalVars.App_Version));
                nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id",
                        user_id + ""));
                nameValuePairs.add(new BasicNameValuePair("user_master_id",
                        user_master_id + ""));
                nameValuePairs.add(new BasicNameValuePair("img",
                        adolescentMonitoring.getImage()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity data = response.getEntity();
                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }
                rd.close();

                try {
                    int a = json.lastIndexOf(":");
                    int b = json.lastIndexOf("}");

                    inserted_id = json.substring(a + 1, b);
                    long id = ObjsqliteHelper.updateAndChangeServer(
                            "adolescent_monitoring", inserted_id, " monitor_id ="
                                    + adolescentMonitoring.getMonitorID(), "img");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
        return inserted_id;
    }

    public String UploadAwcPendingData() {
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "upload_awc_details.php");
        String line = "";
        String json = "";
        try {
            //String [] awcarr = ObjsqliteHelper.GetPendingDataForUploading();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(40);

            nameValuePairs.add(new BasicNameValuePair("awc_1", ObjsqliteHelper.GetOneData("center_id", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_2", ObjsqliteHelper.GetOneData("main_mini", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_3", ObjsqliteHelper.GetOneData("kuchha_pukka", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_4", ObjsqliteHelper.GetOneData("owned_rented", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_5", ObjsqliteHelper.GetOneData("water_source", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_6", ObjsqliteHelper.GetOneData("water_safety", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_7", ObjsqliteHelper.GetOneData("toilet", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_8", ObjsqliteHelper.GetOneData("water", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_9", ObjsqliteHelper.GetOneData("hand_washing_facility", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_10", ObjsqliteHelper.GetOneData("learning_teaching", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_11", ObjsqliteHelper.GetOneData("equipment", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_12", ObjsqliteHelper.GetOneData("outside_awc_img", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_13", ObjsqliteHelper.GetOneData("kitchen_img", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_14", ObjsqliteHelper.GetOneData("toilet_img", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_15", ObjsqliteHelper.GetOneData("hand_washing_img", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            Log.e("...............", ObjsqliteHelper.GetOneData("hand_washing_img", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
            nameValuePairs.add(new BasicNameValuePair("awc_16", ObjsqliteHelper.GetOneData("lattitude", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            Log.e("...............", ObjsqliteHelper.GetOneData("lattitude", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));
            nameValuePairs.add(new BasicNameValuePair("awc_17", ObjsqliteHelper.GetOneData("longitude", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id)));
            Log.e("...............", ObjsqliteHelper.GetOneData("longitude", "anganwadi_center", "(status = 0 or status = 2) and center_id = " + user_id));


			/*
            nameValuePairs.add(new BasicNameValuePair("awc_1", awcarr[0]));
			nameValuePairs.add(new BasicNameValuePair("awc_2", awcarr[1]));
			nameValuePairs.add(new BasicNameValuePair("awc_3", awcarr[2]));
			nameValuePairs.add(new BasicNameValuePair("awc_4", awcarr[3]));
			nameValuePairs.add(new BasicNameValuePair("awc_5", awcarr[4]));
			nameValuePairs.add(new BasicNameValuePair("awc_6", awcarr[5]));
			nameValuePairs.add(new BasicNameValuePair("awc_7", awcarr[6]));
			nameValuePairs.add(new BasicNameValuePair("awc_8", awcarr[7]));
			nameValuePairs.add(new BasicNameValuePair("awc_9", awcarr[8]));
			nameValuePairs.add(new BasicNameValuePair("awc_10", awcarr[9]));
			nameValuePairs.add(new BasicNameValuePair("awc_11", awcarr[10]));
			nameValuePairs.add(new BasicNameValuePair("awc_12", awcarr[11]));
			nameValuePairs.add(new BasicNameValuePair("awc_13", awcarr[12]));
			nameValuePairs.add(new BasicNameValuePair("awc_14", awcarr[13]));
			nameValuePairs.add(new BasicNameValuePair("awc_15", awcarr[14]));  */

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                String sta = ObjsqliteHelper.GetOneData("status", "anganwadi_center", "center_id = " + user_id);
                if (Integer.parseInt(sta) == 0) {
                    sta = "5";
                }
                if (Integer.parseInt(sta) == 2) {
                    sta = "1";
                }
                String[] img_col = {"outside_awc_img", "kitchen_img", "toilet_img", "hand_washing_img"};
                long id = ObjsqliteHelper.updateChangeAWCServer("anganwadi_center", sta, "center_id = " + user_id, img_col);

                if (id > 0) {
                    Log.d("Updated", "Updated successfully");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return inserted_id;

    }

    @SuppressLint("LongLogTag")
    public String UploadWorkerPendingData() {
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "worker_helper_details_latest.php");
        String line = "";
        String json = "";
        try {
            //String [] awcarr = ObjsqliteHelper.GetPendingDataForWorkerUploading();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(40);

            nameValuePairs.add(new BasicNameValuePair("awc_1", Integer.toString(user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_16", ObjsqliteHelper.GetOneData("awc_worker_position", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            Log.e("............................", ObjsqliteHelper.GetOneData("awc_worker_position", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id));
            nameValuePairs.add(new BasicNameValuePair("awc_17", ObjsqliteHelper.GetOneData("temporary_in_charge", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_18", ObjsqliteHelper.GetOneData("which_awc", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_19", ObjsqliteHelper.GetOneData("worker_name", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_20", ObjsqliteHelper.GetOneData("worker_religion", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_21", ObjsqliteHelper.GetOneData("worker_cc", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_22", ObjsqliteHelper.GetOneData("worker_cast", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_23", ObjsqliteHelper.GetOneData("worker_dob", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_24", ObjsqliteHelper.GetOneData("worker_doj", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_25", ObjsqliteHelper.GetOneData("worker_qualification", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_26", ObjsqliteHelper.GetOneData("worker_training", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            Log.e("............................", ObjsqliteHelper.GetOneData("worker_training", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id));
            nameValuePairs.add(new BasicNameValuePair("awc_27", ObjsqliteHelper.GetOneData("worker_residance", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_28", ObjsqliteHelper.GetOneData("worker_distance_awc", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_29", ObjsqliteHelper.GetOneData("worker_mobile", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_30", ObjsqliteHelper.GetOneData("worker_alt_mobile", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_31", ObjsqliteHelper.GetOneData("worker_img1", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_32", ObjsqliteHelper.GetOneData("worker_img2", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_33", ObjsqliteHelper.GetOneData("helper_name", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_34", ObjsqliteHelper.GetOneData("helper_dob", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_35", ObjsqliteHelper.GetOneData("helper_doj", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_36", ObjsqliteHelper.GetOneData("helper_qualification", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_37", ObjsqliteHelper.GetOneData("helper_training", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_38", ObjsqliteHelper.GetOneData("helper_residance", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_39", ObjsqliteHelper.GetOneData("helper_distance_awc", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_40", ObjsqliteHelper.GetOneData("helper_mobile", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_41", ObjsqliteHelper.GetOneData("helper_alt_mobile", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_42", ObjsqliteHelper.GetOneData("helper_img1", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));
            nameValuePairs.add(new BasicNameValuePair("awc_43", ObjsqliteHelper.GetOneData("helper_img2", "anganwadi_center", "(status = 1 or status = 2) and center_id = " + user_id)));

            Log.e("..........................", "1");


			/*
            nameValuePairs.add(new BasicNameValuePair("awc_1", awcarr[0]));
			nameValuePairs.add(new BasicNameValuePair("awc_16", awcarr[1]));
			nameValuePairs.add(new BasicNameValuePair("awc_17", awcarr[2]));
			nameValuePairs.add(new BasicNameValuePair("awc_18", awcarr[3]));
			nameValuePairs.add(new BasicNameValuePair("awc_19", awcarr[4]));
			nameValuePairs.add(new BasicNameValuePair("awc_20", awcarr[5]));
			nameValuePairs.add(new BasicNameValuePair("awc_21", awcarr[6]));
			nameValuePairs.add(new BasicNameValuePair("awc_22", awcarr[7]));
			nameValuePairs.add(new BasicNameValuePair("awc_23", awcarr[8]));
			nameValuePairs.add(new BasicNameValuePair("awc_24", awcarr[9]));
			nameValuePairs.add(new BasicNameValuePair("awc_25", awcarr[10]));
			nameValuePairs.add(new BasicNameValuePair("awc_26", awcarr[11]));
			nameValuePairs.add(new BasicNameValuePair("awc_27", awcarr[12]));
			nameValuePairs.add(new BasicNameValuePair("awc_28", awcarr[13]));
			nameValuePairs.add(new BasicNameValuePair("awc_29", awcarr[14]));
			nameValuePairs.add(new BasicNameValuePair("awc_30", awcarr[15]));
			nameValuePairs.add(new BasicNameValuePair("awc_31", awcarr[16]));
			nameValuePairs.add(new BasicNameValuePair("awc_32", awcarr[17]));
			nameValuePairs.add(new BasicNameValuePair("awc_33", awcarr[18]));
			nameValuePairs.add(new BasicNameValuePair("awc_34", awcarr[19]));
			nameValuePairs.add(new BasicNameValuePair("awc_35", awcarr[20]));
			nameValuePairs.add(new BasicNameValuePair("awc_36", awcarr[21]));
			nameValuePairs.add(new BasicNameValuePair("awc_37", awcarr[22]));
			nameValuePairs.add(new BasicNameValuePair("awc_38", awcarr[23]));
			nameValuePairs.add(new BasicNameValuePair("awc_39", awcarr[24]));
			nameValuePairs.add(new BasicNameValuePair("awc_40", awcarr[25]));
			nameValuePairs.add(new BasicNameValuePair("awc_41", awcarr[26]));
			nameValuePairs.add(new BasicNameValuePair("awc_42", awcarr[27]));
			nameValuePairs.add(new BasicNameValuePair("awc_43", awcarr[28]));  */

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                String sta = ObjsqliteHelper.GetOneData("status", "anganwadi_center", "center_id = " + user_id);
                if (Integer.parseInt(sta) == 1) {
                    sta = "5";
                }
                if (Integer.parseInt(sta) == 2) {
                    sta = "0";
                }
                String[] img_col = {"worker_img1", "worker_img2", "helper_img1", "helper_img2"};
                long id = ObjsqliteHelper.updateChangeAWCServer("anganwadi_center", sta, "center_id = " + user_id, img_col);

                if (id > 0) {
                    Log.d("Updated", "Updated successfully");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return inserted_id;

    }

    //download house hold
    @SuppressLint("NewApi")
    public String DownloadAnganwadiData(int anganwadi_center_id) {
        String lastResult="";
        /*
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "download_families.php");
        String line = "";
        String json = "";
        int totalParent = 0;
        String lastResult = "";
        long parentdownId = 0;
        String strParentdownId = "";

        try {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(varDownChunk);
            nameValuePairs.add(new BasicNameValuePair("anganwadi_id",
                    anganwadi_id + ""));

            StringBuilder nameBuilder = new StringBuilder();

            if (houseList.length > 0) {
                for (String n : houseList) {
                    nameBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                nameBuilder.deleteCharAt(nameBuilder.length() - 1);

                nameValuePairs.add(new BasicNameValuePair("house_number", nameBuilder.toString()));
            } else {
                nameValuePairs.add(new BasicNameValuePair("house_number", ""));
            }


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));

            while ((line = rd.readLine()) != null) {

                json += line;
            }
            rd.close();

            if (json.length() > 0) {

                JSONArray alldata = new JSONArray(json);
                Log.e("...................", Integer.toString(alldata.length()));
                SyncActivity.pbf.setMax(alldata.length());
                for (int i = 0; i < alldata.length(); i++) {

                    JSONObject family = alldata.getJSONObject(i);
                    {

                        String serverid, hno, lat, longi, hfamily, ttype,
                                wsource, religionid, ccategory, addhar, mobile, gender,
                                app_version, user_master_id, l_hold, s_mgr;

                        serverid = family.getString("familyid");
                        hno = family.getString("house_number");
                        lat = family.getString("latitude");
                        longi = family.getString("longitude");
                        hfamily = family.getString("head_family");
                        ttype = family.getString("toilet_type");
                        wsource = family.getString("water_source");
                        religionid = family.getString("religion_id");
                        ccategory = family.getString("cast_category");
                        addhar = family.getString("adhaar_card");
                        mobile = family.getString ("mobile_number");
                        gender = family.getString("gender");
                        app_version = family.getString("app_version");
                        user_master_id = family.getString("us" +
                                "er_master_id");
                        l_hold = family.getString("family_land_hold");
                        s_mgr = family.getString("seasonal_migration");


                        Parent parent = new Parent();
                        parent.setAppVer(app_version);
                        parent.setHouseNo(hno);
                        parent.setHeadofHH(hfamily);
                        parent.setAadharCardHH(addhar);
                        parent.setMobileHH(mobile);
                        parent.setUser_id(anganwadi_id);
                        parent.setHas_toilet(Integer.parseInt(ttype));
                        parent.setHave_water(Integer.parseInt(wsource));
                        parent.setServer_id(Integer.parseInt(serverid));
                        parent.setReligion(Integer.parseInt(religionid));
                       // parent.setIntcastecat(Integer.parseInt(ccategory));
                      //  parent.setIntcastecat(0);
                        parent.setIntGender(Integer.parseInt(gender));
                        parent.setLatitude(lat);
                        parent.setLongitude(longi);
                        parent.setUser_master_id(user_master_id);
                        parent.setStatus(1);
                        parent.setLan_Value(l_hold);
                        parent.setSes_migration(s_mgr);
                        totalParent = parent.getHouseNo().length();

                        long mparentdownId = ObjsqliteHelper.EligibleFamilyRegistration2(parent);
                        SyncActivity.pbf.setProgress(i + 1);
                        if (mparentdownId > 0) {
                            parentdownId++;
                            strParentdownId = parentdownId + " eligible family";
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadHouseholdApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadHH>>>", "onResponse eligible_family>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("eligible_family");
                    SyncActivity.pbf.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("familyid");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("familyid")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mparentdownId=ObjsqliteHelper.saveMasterTable(contentValues, "eligible_family");
                        SyncActivity.pbf.setProgress(i + 1);
                        if (mparentdownId > 0) {
                            parentdownId++;
                            strParentdownId = parentdownId + " Household";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("ta", "");
            }
        });
        lastResult = strParentdownId;
        return lastResult;
    }

    //download pregnant women
    public String DownloadPregnantWomen(int anganwadi_center_id) {
        String lastResult = "";
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpClient httpwomen = new DefaultHttpClient();
        HttpPost httppostwomen = new HttpPost(url
                + "download_mmother.php");
        String linewomen = "";
        String jsonwomen = "";
        long pregnentwomenid = 0;
        String strPregnentwomenid = "";
        long mpregnentwomenid = 0;
        String lastResult = "";

        try {

            String hno, pregnant_women_id, child_number, flag, name_of_pregnant_women, date_of_registration, place_of_delivery, lmp_date, edd, parent_id, anganwadi_center_id, height, weight, hb, lattitude,
                    longitude, house_number, husband_father_name, app_version, user_master_id;

            List<NameValuePair> nameValuePairswomen = new ArrayList<NameValuePair>(
                    varDownChunk);
            nameValuePairswomen.add(new BasicNameValuePair(
                    "anganwadi_center_id", anganwadi_id
                    + ""));

            StringBuilder idBuilder = new StringBuilder();

            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairswomen.add(new BasicNameValuePair("pregnant_women_id", idBuilder.toString()));
            } else {
                nameValuePairswomen.add(new BasicNameValuePair("pregnant_women_id", ""));
            }


            httppostwomen
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairswomen));

            HttpResponse responsewomen = httpclient
                    .execute(httppostwomen);
            HttpEntity datawomen = responsewomen
                    .getEntity();
            InputStream iswomen = null;
            iswomen = datawomen.getContent();
            BufferedReader rdwomen = new BufferedReader(
                    new InputStreamReader(iswomen, "UTF-8"));

            while ((linewomen = rdwomen.readLine()) != null) {

                jsonwomen += linewomen;
            }
            rdwomen.close();

            if (jsonwomen.length() > 0) {

                JSONArray jsonwomenalldata = new JSONArray(
                        jsonwomen);
                Log.e("...................", Integer.toString(jsonwomenalldata.length()));
                SyncActivity.pbmpw.setMax(jsonwomenalldata.length());
                for (int j = 0; j < jsonwomenalldata
                        .length(); j++) {

                    JSONObject women = jsonwomenalldata
                            .getJSONObject(j);


                    pregnant_women_id = women
                            .getString("pregnant_women_id");
                    name_of_pregnant_women = women
                            .getString("name_of_pregnant_women");
                    child_number = women
                            .getString("child_number");
                    flag = women
                            .getString("flag");
                    date_of_registration = women
                            .getString("date_of_registration");
                    place_of_delivery = women
                            .getString("place_of_delivery");
                    lmp_date = women.getString("lmp_date");
                    edd = women.getString("edd");
                    parent_id = women
                            .getString("parent_id");
                    anganwadi_center_id = women
                            .getString("anganwadi_center_id");
                    height = women.getString("height");
                    weight = women.getString("weight");
                    hb = women.getString("hb");
                    lattitude = women
                            .getString("lattitude");
                    longitude = women
                            .getString("longitude");
                    house_number = women
                            .getString("house_number");
                    husband_father_name = women
                            .getString("husband_father_name");
                    app_version = women
                            .getString("version_code");
                    user_master_id = women
                            .getString("user_master_id");

                    PregnantWomen pregnantWomen = new PregnantWomen();
                    pregnantWomen.setHhId(house_number);
                    pregnantWomen.setPreWomenName(name_of_pregnant_women);
                    pregnantWomen.setLmp_date(lmp_date);
                    pregnantWomen.setEdd(edd);
                    pregnantWomen.setHeight(height);
                    pregnantWomen.setWeight(weight);
                    pregnantWomen.setHb(hb);
                    pregnantWomen.setBp("0");
                    pregnantWomen.setStatus(1);
                    pregnantWomen.setAppVer(app_version);
                    pregnantWomen.setUser_master_id(user_master_id);

                    pregnantWomen.setFlag(flag);
                    pregnantWomen.setChildNumber(child_number);

                    GlobalVars.lattitude = lattitude;
                    GlobalVars.longitude = longitude;
                    pregnantWomen.setServer_id(Integer
                            .parseInt(pregnant_women_id));

                    pregnantWomen
                            .setHusbandName(husband_father_name);

                    mpregnentwomenid = ObjsqliteHelper
                            .PregnantWomenRegistration(pregnantWomen, "");
                    SyncActivity.pbmpw.setProgress(j + 1);
                    if (mpregnentwomenid > 0) {

                        pregnentwomenid++;

                        strPregnentwomenid = pregnentwomenid + " pregnant women";
                    }
                }
            }

        } catch (Exception e) {

        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadPregnantApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadPW>>>", "onResponse pregnant_women>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("pregnant_women");
                    SyncActivity.pbmpw.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("pregnant_women_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("pregnant_women_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mpregnentwomenid=ObjsqliteHelper.saveMasterTable(contentValues, "pregnant_women");
                        SyncActivity.pbmpw.setProgress(i + 1);
                        if (mpregnentwomenid > 0) {
                            pregnentwomenid++;
                            strPregnentwomenid = pregnentwomenid + " pregnant women";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });
        lastResult = strPregnentwomenid;
        return lastResult;
    }

    //download mother
    public String DownloadMother(int anganwadi_center_id) {
        String lastResult = "";

        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadMotherApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadMother>>>", "onResponse mother>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("mother");
                    SyncActivity.pbm.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("mother_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("mother_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mpomtherid=ObjsqliteHelper.saveMasterTable(contentValues, "mother");
                        SyncActivity.pbm.setProgress(i + 1);
                        if (mpomtherid > 0) {
                            motherid++;
                            strMotherid = motherid + " mother";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });
        lastResult = strMotherid;
        return lastResult;
    }

    //download pregnant women monitor
    public String DownloadPregnantWomenMon(int anganwadi_center_id) {
        String lastResult;
        /*HttpClient httpwomennutrition = new DefaultHttpClient();
        HttpPost httppostwomennutrition = new HttpPost(
                url
                        + "download_wnutrition.php");
        String linewomennutrition = "";
        String jsonwomennutrition = "";
        long mpregnentwomenNutid = 0;
        long pregnentwomenNutid = 0;
        String strPregnentwomenNutid = "";
        String app_version;
        String lastResult;
        try {
            List<NameValuePair> nameValuePairswomennutrition = new ArrayList<NameValuePair>(varDownChunk);
            nameValuePairswomennutrition.add(new BasicNameValuePair(
                    "anganwadi_center_id", anganwadi_id
                    + ""));
            StringBuilder idBuilder = new StringBuilder();
            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairswomennutrition.add(new BasicNameValuePair("id", idBuilder.toString()));
            } else {
                nameValuePairswomennutrition.add(new BasicNameValuePair("id", ""));
            }

            httppostwomennutrition
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairswomennutrition));
            HttpResponse responsewomennutrition = httpwomennutrition
                    .execute(httppostwomennutrition);
            HttpEntity datawomennutrition = responsewomennutrition
                    .getEntity();
            InputStream iswomennutrition = null;
            iswomennutrition = datawomennutrition
                    .getContent();
            BufferedReader rdwomennutrition = new BufferedReader(
                    new InputStreamReader(
                            iswomennutrition,
                            "UTF-8"));
            while ((linewomennutrition = rdwomennutrition
                    .readLine()) != null) {
                jsonwomennutrition += linewomennutrition;
            }
            rdwomennutrition.close();
            if (jsonwomennutrition.length() > 0) {

                JSONArray jsonwomennutritionalldata = new JSONArray(
                        jsonwomennutrition);

                SyncActivity.pbpwm.setMax(jsonwomennutritionalldata.length());
                ;
                for (int k = 0; k < jsonwomennutritionalldata.length(); k++) {
                    JSONObject womennutrition = jsonwomennutritionalldata.getJSONObject(k);

                    String women_id, pwomen_id, wweight, hb, c_date, bp, date_of_recording, wheight, sysbp, diasbp, server_mon_id, user_master_id, migration_status;


                    server_mon_id = womennutrition.getString("id");
                    women_id = womennutrition
                            .getString("women_id");
                    Log.e("................", women_id);
                    wweight = womennutrition
                            .getString("weight");
                    hb = womennutrition
                            .getString("hb");
                    date_of_recording = womennutrition
                            .getString("date_of_recording");
                    wheight = womennutrition
                            .getString("height");
                    //bp = womennutrition.getString("bp");
                    //sysbp = womennutrition.getString("sysbp");
                    //diasbp = womennutrition.getString("diasbp");
                    app_version = womennutrition
                            .getString("version_code");
                    user_master_id = womennutrition
                            .getString("user_master_id");
                    migration_status = womennutrition
                            .getString("migration_type");
                    pwomen_id = ObjsqliteHelper.getWomenId(women_id);

                    PregnantWomenMonitor pregnantWomenMonitor = new PregnantWomenMonitor();
                    //pregnantWomenMonitor.setBp(bp);
                    pregnantWomenMonitor.setSysBp("");
                    pregnantWomenMonitor.setDiasBp("");
                    pregnantWomenMonitor.setServer_mon_id(server_mon_id);
                    pregnantWomenMonitor.setPregnant_women_id(pwomen_id);
                    pregnantWomenMonitor.setServer_id(Integer.parseInt(women_id));
                    pregnantWomenMonitor.setHb(hb);
                    pregnantWomenMonitor.setDate_of_recording(date_of_recording);
                    pregnantWomenMonitor.setWeight(wweight);
                    pregnantWomenMonitor.setApp_version(app_version);
                    pregnantWomenMonitor.setUser_master_id(user_master_id);
                    pregnantWomenMonitor.setMgrStatus(migration_status);
                    pregnantWomenMonitor.setStatus(1);

                    mpregnentwomenNutid = ObjsqliteHelper.PregnantWomenMonitor(pregnantWomenMonitor);
                    SyncActivity.pbpwm.setProgress(k + 1);
                    if (mpregnentwomenNutid > 0) {

                        pregnentwomenNutid++;
                        strPregnentwomenNutid = pregnentwomenNutid + " pregnant women monitoring";
                    }
                }
            }
        } catch (Exception ew) {
            ew.printStackTrace();
        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadPregnantMonitoringApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadPWM>>>", "onResponse pregnant_womem_monitor>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("pregnant_womem_monitor");
                    SyncActivity.pbpwm.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("monitor_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("monitor_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mpregnentwomenNutid=ObjsqliteHelper.saveMasterTable(contentValues, "pregnant_womem_monitor");
                        SyncActivity.pbpwm.setProgress(i + 1);
                        if (mpregnentwomenNutid > 0) {
                            pregnentwomenNutid++;
                            strPregnentwomenNutid = pregnentwomenNutid + " pregnant women monitoring";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });
        lastResult = strPregnentwomenNutid;
        return lastResult;
    }

    //download child_reg
    public String DownloadChild(int anganwadi_center_id) {
        String lastResult = "";
        /*HttpClient httpchild = new DefaultHttpClient();
        HttpPost httppostchild = new HttpPost(
                url + "download_child_reg.php");
        String linechild = "";
        String jsonchild = "";
        String strChildregId = "";
        long childregId = 0;
        long cchildregId = 0;
        String lastResult = "";

        try {
            List<NameValuePair> nameValuePairsChild = new ArrayList<NameValuePair>(
                    varDownChunk);
            nameValuePairsChild.add(new BasicNameValuePair("anganwadi_center_id", anganwadi_id + ""));

            StringBuilder idBuilder = new StringBuilder();
            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairsChild.add(new BasicNameValuePair("child_id", idBuilder.toString()));
            } else {
                nameValuePairsChild.add(new BasicNameValuePair("child_id", ""));
            }


            httppostchild
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairsChild));
            HttpResponse responsechild = httpchild
                    .execute(httppostchild);
            HttpEntity datachild = responsechild
                    .getEntity();
            InputStream ischild = null;
            ischild = datachild.getContent();
            BufferedReader rdchild = new BufferedReader(
                    new InputStreamReader(
                            ischild, "UTF-8"));
            while ((linechild = rdchild
                    .readLine()) != null) {
                jsonchild += linechild;
            }
            rdchild.close();

            if (jsonchild.length() > 0) {

                String child_id, name_of_child, h_no, gender, app_version, date_of_birth, birth_status, Dateofregistration, birth_weight, birth_height, is_disability, pregnent_women_id, user_master_id,lattitude,longitude,hb;

                JSONArray jsonChild = new JSONArray(
                        jsonchild);
                SyncActivity.pbcr.setMax(jsonChild.length());
                for (int k = 0; k < jsonChild
                        .length(); k++) {
                    JSONObject child = jsonChild.getJSONObject(k);

                    child_id = child
                            .getString("child_id");
                    name_of_child = child
                            .getString("name_of_child");
                    h_no = child
                            .getString("house_number");
                    date_of_birth = child
                            .getString("date_of_birth");
                    Dateofregistration = child
                            .getString("Dateofregistration");
                    birth_weight = child
                            .getString("birth_weight");
                    birth_height = child
                            .getString("birth_height");
                    is_disability = child
                            .getString("is_disability");
                    pregnent_women_id = child
                            .getString("pregnent_women_id");
                    gender = child
                            .getString("gender");
                    birth_status = child
                            .getString("birth_status");
                    app_version = child
                            .getString("version_code");
                    user_master_id = child
                            .getString("user_master_id");
                    lattitude=child.getString("lattitude");
                    longitude=child.getString("longitude");
                    hb=child.getString("hb");


                    Child childdata = new Child();
                    childdata
                            .setChild_name(name_of_child);
                    childdata
                            .setHHID(h_no);
                    childdata
                            .setDate_of_birth(date_of_birth);
                    childdata
                            .setHeight(birth_height);
                    childdata.setGender(gender);
                    childdata
                            .setBirth_order(Integer
                                    .parseInt(birth_status));
                    childdata
                            .setDisablity(is_disability);
                    childdata
                            .setChild_weight(birth_weight);
                    childdata
                            .setApp_version(app_version);

                    childdata.setStatus(1);
                    childdata
                            .setLatitude(lattitude);
                    childdata
                            .setLongitude(longitude);
                    childdata
                            .setUser_master_id(user_master_id);
                    childdata.setHb(hb);

                    String store_women_id = ObjsqliteHelper.getWomenId(pregnent_women_id);
                    childdata
                            .setPregnent_women_id(store_women_id);
                    childdata.setServer_id(Integer.parseInt(child_id));


                    childregId = ObjsqliteHelper.ChildRegistration(childdata);
                    SyncActivity.pbcr.setProgress(k + 1);
                    if (childregId > 0) {

                        cchildregId++;
                        strChildregId = cchildregId + " child";
                    }

                }
            }
        } catch (Exception ew) {
            ew.printStackTrace();
        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadChildApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadChild>>>", "onResponse child>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("child");
                    SyncActivity.pbcr.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("child_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("child_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mchildregId=ObjsqliteHelper.saveMasterTable(contentValues, "child");
                        SyncActivity.pbcr.setProgress(i + 1);
                        if (mchildregId > 0) {
                            childregId++;
                            strChildregId = childregId + " child";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });
        lastResult = strChildregId;

        return lastResult;
    }

    //download child mon
    public String DownloadChildMon(int anganwadi_center_id) {
        String lastResult = "";
        /*HttpClient httpchildnutrion = new DefaultHttpClient();
        HttpPost httppostchildnutrition = new HttpPost(
                url
                        + "download_child_mon.php");
        String linechildnutrition = "";
        String jsonchildnutrition = "";
        String app_version;
        String lastResult = "";
        long childNutId = 0;
        long cchildNutId = 0;
        String strChildNutId = "";

        try {
            List<NameValuePair> nameValuePairschildnutrition = new ArrayList<NameValuePair>(varDownChunk);

            nameValuePairschildnutrition.add(new BasicNameValuePair("anganwadi_center_id", anganwadi_id + ""));

            StringBuilder idBuilder = new StringBuilder();
            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairschildnutrition.add(new BasicNameValuePair("nutrition_id", idBuilder.toString()));
            } else {
                nameValuePairschildnutrition.add(new BasicNameValuePair("nutrition_id", ""));
            }

            httppostchildnutrition
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairschildnutrition));
            HttpResponse responsechildnutrition = httpchildnutrion
                    .execute(httppostchildnutrition);
            HttpEntity datachildnutrition = responsechildnutrition
                    .getEntity();
            InputStream ischildnutrition = null;
            ischildnutrition = datachildnutrition
                    .getContent();
            BufferedReader rdchildnutrtion = new BufferedReader(
                    new InputStreamReader(
                            ischildnutrition,
                            "UTF-8"));
            while ((linechildnutrition = rdchildnutrtion
                    .readLine()) != null) {
                jsonchildnutrition += linechildnutrition;
            }
            rdchildnutrtion.close();

            if (jsonchildnutrition
                    .length() > 0) {
                String cchild_id, cheight, cweight, cmuac, chb, date_of_monitoring, server_nutrition_id, migration_type, edeme, user_master_id;

                JSONArray jsonChildNutrition = new JSONArray(
                        jsonchildnutrition);
                Log.e("..................", String.valueOf(jsonChildNutrition.length()));
                SyncActivity.pbcm.setMax(jsonChildNutrition.length());
                for (int l = 0; l < jsonChildNutrition
                        .length(); l++) {
                    JSONObject childnutrtion = jsonChildNutrition
                            .getJSONObject(l);

                    cchild_id = childnutrtion
                            .getString("child_id");
                    cheight = childnutrtion
                            .getString("height");
                    cweight = childnutrtion
                            .getString("weight");
                    cmuac = childnutrtion
                            .getString("muac");
                    chb = childnutrtion
                            .getString("hb");
                    date_of_monitoring = childnutrtion
                            .getString("date_of_monitoring");
                    app_version = childnutrtion
                            .getString("version_code");
                    server_nutrition_id = childnutrtion
                            .getString("nutrition_id");
                    migration_type = childnutrtion
                            .getString("migration_type");
                    edeme = childnutrtion
                            .getString("have_adeama");
                    user_master_id = childnutrtion.getString("user_master_id");

                    ChildNutrition childnutrition = new ChildNutrition();
                    String store_local_chilgId = ObjsqliteHelper.getChildId(cchild_id);
                    childnutrition.setChild_id(Integer.parseInt(store_local_chilgId));
                    //childnutrition.setChild_name("");
                    childnutrition.setWeight(cweight);
                    childnutrition.setHeight(cheight);
                    childnutrition.setMuac(cmuac);
                    childnutrition.setHB(chb);
                    childnutrition.setApp_version(app_version);
                    childnutrition.setServer_id(Integer.parseInt(cchild_id));
                    childnutrition.setStatus(1);
                    childnutrition.setDate_of_monitoring(date_of_monitoring);
                    childnutrition.setEdeme(edeme);
                    childnutrition.setMultimedia("");
                    childnutrition.setServer_nutrition_id(server_nutrition_id);
                    childnutrition.setMigration(migration_type);
                    childnutrition.setUser_master_id(user_master_id);


                    childNutId = ObjsqliteHelper.childNutitionMonitor(childnutrition);
                    Log.e(".....................", "..............1");
                    SyncActivity.pbcm.setProgress(l + 1);

                    if (childNutId > 0) {


                        cchildNutId++;
                        strChildNutId = cchildNutId + " child monitoring";

                    }
                }

            }

        } catch (Exception d) {
            d.printStackTrace();

        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadChildMonitoringApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadCNM>>>", "onResponse child_nutrition_monitoring>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("child_nutrition_monitoring");
                    SyncActivity.pbcm.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("nutrition_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("nutrition_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mchildNutId=ObjsqliteHelper.saveMasterTable(contentValues, "child_nutrition_monitoring");
                        SyncActivity.pbcm.setProgress(i + 1);
                        if (mchildNutId > 0) {
                            childNutId++;
                            strChildNutId = childNutId + " child monitoring";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });

        lastResult = strChildNutId;
        return lastResult;
    }

    public String DownloadAdolecent(int anganwadi_center_id) {
        String lastResult = "";
        /*String house_number;
        String height;
        String weight;
        long aId = 0;
        long hno = 0;
        String app_version;
        long aaId = 0;
        String strAId = "";
        String lastResult = "";
        HttpClient httpadolcent = new DefaultHttpClient();
        HttpPost httppostadolcent = new HttpPost(
                url + "download_adolcent.php");
        String lineadolcent = "";
        String jsonadolcent = "";

        try {
            List<NameValuePair> nameValuePairsadolcent = new ArrayList<NameValuePair>(
                    varDownChunk);
            nameValuePairsadolcent
                    .add(new BasicNameValuePair(
                            "anganwadi_id",
                            anganwadi_id + ""));


            StringBuilder idBuilder = new StringBuilder();
            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairsadolcent.add(new BasicNameValuePair("adolescent", idBuilder.toString()));
            } else {
                nameValuePairsadolcent.add(new BasicNameValuePair("adolescent", ""));
            }

            httppostadolcent
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairsadolcent));

            HttpResponse responseadolcent = httpadolcent
                    .execute(httppostadolcent);
            HttpEntity dataadolcent = responseadolcent
                    .getEntity();
            InputStream isadolcent = null;
            isadolcent = dataadolcent.getContent();
            BufferedReader rdadolcent = new BufferedReader(
                    new InputStreamReader(
                            isadolcent, "UTF-8"));
            while ((lineadolcent = rdadolcent
                    .readLine()) != null) {
                jsonadolcent += lineadolcent;
            }
            rdadolcent.close();

            if (jsonadolcent.length() > 0) {

                //Log.e("length", String.valueOf(jsonadolcent.length()));

                JSONArray jsonAdolcent = new JSONArray(
                        jsonadolcent);
                SyncActivity.pbagr.setMax(jsonAdolcent.length());
                for (int l = 0; l < jsonAdolcent
                        .length(); l++) {
                    //Log.e("l", String.valueOf(l));
                    Adolescent adolescent = new Adolescent();
                    JSONObject AdolcentObject = jsonAdolcent.getJSONObject(l);


                    String server_id, girl_name, father_name, HB, OSP, chronical, date_of_birth, user_master_id;


                    server_id = AdolcentObject.getString("adolescent");
                    house_number = AdolcentObject.getString("house_number");
                    girl_name = AdolcentObject.getString("girl_name");
                    father_name = AdolcentObject.getString("father_name");
                    date_of_birth = AdolcentObject.getString("date_of_birth");
                    height = AdolcentObject.getString("height");
                    weight = AdolcentObject.getString("weight");
                    HB = AdolcentObject.getString("HB");
                    OSP = AdolcentObject.getString("OSP");
                    chronical = AdolcentObject.getString("chronical");
                    app_version = AdolcentObject
                            .getString("version_code");
                    user_master_id = AdolcentObject
                            .getString("user_master_id");

                    adolescent.setChronicDisease(chronical);
                    adolescent.setHhId(house_number);
                    adolescent.setNameOfTheGirl(girl_name);
                    adolescent.setGirlFatherName(father_name);
                    adolescent.setDateOfBirth(date_of_birth);
                    adolescent.setGirlHeight(height);
                    adolescent.setGirlWeight(weight);
                    adolescent.setGirlHb(HB);
                    adolescent.setospYear(OSP);
                    adolescent.setApp_version(app_version);
                    adolescent.setospMonth(OSP);
                    adolescent.setServer_id(Integer.parseInt(server_id));
                    adolescent.setUser_master_id(user_master_id);
                    adolescent.setStatus(1);

                    aId = ObjsqliteHelper.AdolescentRegistration(adolescent, "");
                    SyncActivity.pbagr.setProgress(l + 1);

                    if (aId > 0) {
                        aaId++;
                        strAId = aaId + " adolescent registration";

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadAdolescentApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadAdol>>>", "onResponse adolescent>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("adolescent");
                    SyncActivity.pbagr.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("adolescent_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("adolescent_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long maId=ObjsqliteHelper.saveMasterTable(contentValues, "adolescent");
                        SyncActivity.pbagr.setProgress(i + 1);
                        if (maId > 0) {
                            aId++;
                            strAId = aId + " adolescent registration";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });

        lastResult = strAId;
        return lastResult;
    }

    public String DownloadAdolecentMon(int anganwadi_center_id) {
        String lastResult = "";
        /*long amonId = 0;
        String strAMonId = "";
        String lastResult = "";

        HttpClient httpadolcentnutrion = new DefaultHttpClient();
        HttpPost httppostadolcentnutrition = new HttpPost(
                url
                        + "download_anutrition.php");
        String lineadolcentnutrition = "";
        String jsonadolcentnutrition = "";

        try {
            List<NameValuePair> nameValuePairsadolcentnutrition = new ArrayList<NameValuePair>(varDownChunk);


            nameValuePairsadolcentnutrition
                    .add(new BasicNameValuePair(
                            "anganwadi_center_id", anganwadi_id + ""));

            StringBuilder idBuilder = new StringBuilder();
            if (serverIdArray.length > 0) {
                for (String n : serverIdArray) {
                    idBuilder.append("'").append(n.replace("'", "\\'")).append("',");
                }

                idBuilder.deleteCharAt(idBuilder.length() - 1);

                nameValuePairsadolcentnutrition.add(new BasicNameValuePair("girl_nutrition_id", idBuilder.toString()));
            } else {
                nameValuePairsadolcentnutrition.add(new BasicNameValuePair("girl_nutrition_id", ""));
            }


            httppostadolcentnutrition
                    .setEntity(new UrlEncodedFormEntity(
                            nameValuePairsadolcentnutrition));

            HttpResponse responseadolcentnutrition = httpadolcentnutrion
                    .execute(httppostadolcentnutrition);
            HttpEntity dataadolcentnutrition = responseadolcentnutrition
                    .getEntity();
            InputStream isadolcentnutrition = null;
            isadolcentnutrition = dataadolcentnutrition
                    .getContent();
            BufferedReader rdadolcentnutrtion = new BufferedReader(
                    new InputStreamReader(
                            isadolcentnutrition,
                            "UTF-8"));
            while ((lineadolcentnutrition = rdadolcentnutrtion
                    .readLine()) != null) {
                jsonadolcentnutrition += lineadolcentnutrition;
            }
            rdadolcentnutrtion
                    .close();

            if (jsonadolcentnutrition
                    .length() > 0) {

                String girl_id, hb, c_date, bp, date_of_recording, weight, height, app_version, girl_nutrition_id, migration_status;


                JSONArray jsonadolcentnutritionarray = new JSONArray(
                        jsonadolcentnutrition);
                SyncActivity.pbagm.setMax(jsonadolcentnutritionarray.length());
                for (int m = 0; m < jsonadolcentnutritionarray
                        .length(); m++) {

                    JSONObject AdolcentNObject = jsonadolcentnutritionarray.getJSONObject(m);

                    girl_id = AdolcentNObject.getString("girl_id");
                    weight = AdolcentNObject.getString("weight");
                    height = AdolcentNObject.getString("height");
                    hb = AdolcentNObject.getString("hb");
                    c_date = AdolcentNObject.getString("c_date");
                    bp = AdolcentNObject.getString("bp");
                    date_of_recording = AdolcentNObject.getString("date_of_recording");
                    app_version = AdolcentNObject.getString("version_code");
                    girl_nutrition_id = AdolcentNObject.getString("girl_nutrition_id");
                    migration_status = AdolcentNObject.getString("migration_type");
                    AdolescentMonitoring adolescentMonitoring = new AdolescentMonitoring();


                    int store_girl_local_id = ObjsqliteHelper.getAdolescentId(girl_id);
                    adolescentMonitoring.setAdolescentGirlID((int) store_girl_local_id);
                    adolescentMonitoring.setAdolscentName("");
                    adolescentMonitoring.setAdolescentWeight(weight);
                    adolescentMonitoring.setAdolescentHeight(height);
                    adolescentMonitoring.setAdolescentHb(hb);
                    adolescentMonitoring.setApp_version(app_version);
                    adolescentMonitoring.setDateOfRecord(date_of_recording);
                    adolescentMonitoring.setServerId(Integer.parseInt(girl_id));
                    adolescentMonitoring.setStatus(1);
                    adolescentMonitoring.setMgrStatus(migration_status);
                    adolescentMonitoring.setGirl_nut_id(girl_nutrition_id);


                    long aamonId = ObjsqliteHelper.adolescentNutritionMonitoring(adolescentMonitoring);
                    SyncActivity.pbagm.setProgress(m + 1);
                    if (aamonId > 0) {

                        amonId++;
                        strAMonId = amonId + " adolescent monitoring";

                    }

                }

            }


        } catch (Exception s) {
            s.printStackTrace();
        }*/
        DownloadData downloadData = new DownloadData();
        downloadData.setAnganwadi_center_id(String.valueOf(anganwadi_center_id));
        //downloadData.setCreated_on("-1");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadAdolescentMonitoringApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadAdolMonitoring>>>", "onResponse adolescent monitoring>>> "+jsonArray.toString());
                    ObjsqliteHelper.dropTable("adolescent_monitoring");
                    SyncActivity.pbagm.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("adolescent_nutrition_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("adolescent_nutrition_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long mamonId=ObjsqliteHelper.saveMasterTable(contentValues, "adolescent_monitoring");
                        SyncActivity.pbagm.setProgress(i + 1);
                        if (mamonId > 0) {
                            amonId++;
                            strAMonId = amonId + " adolescent monitoring";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });

        lastResult = strAMonId;
        return lastResult;
    }


    /**
     * @param userID
     * @return
     */
    @SuppressLint("NewApi")
    public String GetPregnantList(int userID) {
        PregnantWomen[] parents = null;

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "pwomen_anganwadi.php");
        String line = "";
        String json = "";
        int totalPW = 0;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("anganwadi_id", userID
                    + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                if (json.length() > 0) {
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        int pregnant_women_id, parent_id, anganwadi_center_id;
                        String name_of_pregnant_women, mobile_number, date_of_registration, lmp_date, edd, lattitude, longitude, height, weight;

                        pregnant_women_id = Integer.parseInt(jsonChildNode
                                .optString("pregnant_women_id").toString());
                        parent_id = Integer.parseInt(jsonChildNode.optString(
                                "parent_id").toString());
                        anganwadi_center_id = Integer.parseInt(jsonChildNode
                                .optString("anganwadi_center_id").toString());

                        name_of_pregnant_women = jsonChildNode.optString(
                                "name_of_pregnant_women").toString();
                        mobile_number = jsonChildNode
                                .optString("mobile_number").toString();
                        date_of_registration = jsonChildNode.optString(
                                "date_of_registration").toString();
                        lmp_date = jsonChildNode.optString("lmp_date")
                                .toString();
                        edd = jsonChildNode.optString("edd").toString();
                        lattitude = jsonChildNode.optString("lattitude")
                                .toString();
                        longitude = jsonChildNode.optString("longitude")
                                .toString();
                        height = jsonChildNode.optString("height").toString();
                        weight = jsonChildNode.optString("weight").toString();

                        PregnantWomen parent = new PregnantWomen();
                        parent.setPregnant_women_id(pregnant_women_id + "");
                        parent.setParent_id(parent_id);
                        parent.setPreWomenName(name_of_pregnant_women);
                        parent.setLmp_date(lmp_date);
                        parent.setEdd(edd);
                        parent.setLatitude(lattitude);
                        parent.setLongitude(longitude);
                        parent.setHeight(height);
                        parent.setWeight(weight);
                        parent.setStatus(1);
                        parent.setUser_id(userID);
                        parent.setServer_id(pregnant_women_id);

                        if (ObjsqliteHelper.getTotalCount("pregnant_women",
                                "server_id", pregnant_women_id) == 0) {
                            if (ObjsqliteHelper
                                    .PregnantWomenRegistration2(parent) > 0) {
                                totalPW++;
                                GetPWMonitoringList(pregnant_women_id);
                            }
                        }

                        // Log.i("JSON parse", song_name);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalPW + " pregnant women data synced successfully!";
    }

    @SuppressLint("NewApi")
    public String GetChildList(int userID) {
        Child[] chidren = null;
        int totalPW = 0;
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "child_anganwadi.php");

        String line = "";
        String json = "";

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("anganwadi_id", userID
                    + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                // JSONObject user = new JSONObject(json);
                if (json.length() > 0) {
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        int child_id, gender, birth_status, parent_id, anganwadi_center_id, houser_id, has_toilet, have_water, litracy_status, religion, status;
                        String parent_name, name_of_child, date_of_birth, lattitude, longitude, photopath, father, mother, cast, f_adhaar_card, m_adhaar_card, address;

                        child_id = Integer.parseInt(jsonChildNode.optString(
                                "child_id").toString());
                        gender = Integer.parseInt(jsonChildNode.optString(
                                "gender").toString());
                        parent_id = Integer.parseInt(jsonChildNode.optString(
                                "parent_id").toString());
                        date_of_birth = jsonChildNode
                                .optString("date_of_birth").toString();
                        lattitude = jsonChildNode.optString("lattitude")
                                .toString();
                        longitude = jsonChildNode.optString("longitude")
                                .toString();
                        photopath = jsonChildNode.optString("photopath")
                                .toString();
                        name_of_child = jsonChildNode
                                .optString("name_of_child").toString();
                        parent_name = String.valueOf(jsonChildNode
                                .optString("parent_id"));

                        Child child = new Child();
                        child.setChild_id(child_id);
                        child.setGender(gender + "");
                        child.setParent_id(parent_id);
                        child.setDate_of_birth(date_of_birth);
                        child.setLatitude(lattitude);
                        child.setLongitude(longitude);
                        child.setMultimedia(photopath);
                        child.setChild_name(name_of_child);
                        child.setServer_id(child_id);
                        child.setParent_name(parent_name);
                        child.setUser_id(userID);

                        if (ObjsqliteHelper.getTotalCount("child",
                                "server_id", child_id) == 0) {
                            if (ObjsqliteHelper.ChildRegistration2(child) > 0) {
                                totalPW++;
                                GetChildMonitoringList(child.child_id);
                            }
                        }

                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalPW + " children data synced successfully!";
    }

    /*
     * private String String.valueOf((String optString) { // TODO Auto-generated
     * method stub return null; }
     */
    public String GetChildMonitoringList(int childID) {
        Child[] chidren = null;
        int totalPW = 0;
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "child_monitoring_list.php");
        String line = "";
        String json = "";

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs
                    .add(new BasicNameValuePair("child_id", childID + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                // JSONObject user = new JSONObject(json);
                if (json.length() > 0) {
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        int nutrition_id, child_id, gender, birth_status, parent_id, anganwadi_center_id, houser_id, has_toilet, have_water, litracy_status, religion, status;
                        String height, weight, muac, date_of_monitoring, latitude, longitude, photopath, father, mother, cast, f_adhaar_card, m_adhaar_card, address;

                        child_id = Integer.parseInt(jsonChildNode.optString(
                                "child_id").toString());
                        date_of_monitoring = jsonChildNode.optString(
                                "date_of_monitoring").toString();
                        height = jsonChildNode.optString("height").toString();
                        weight = jsonChildNode.optString("weight").toString();
                        muac = jsonChildNode.optString("muac").toString();
                        latitude = jsonChildNode.optString("latitude")
                                .toString();
                        longitude = jsonChildNode.optString("longitude")
                                .toString();
                        nutrition_id = Integer.parseInt(jsonChildNode
                                .optString("nutrition_id"));
                        photopath = jsonChildNode.optString("photopath")
                                .toString();

                        Nutrition child = new Nutrition();
                        child.setChild_id(child_id);
                        child.setDate_of_monitiring(date_of_monitoring);
                        child.setHeight(height);
                        child.setWeight(weight);
                        child.setMauc(muac);
                        child.setLatitdue(latitude);
                        child.setLongitude(longitude);
                        child.setNutrition_id(nutrition_id);
                        child.setPhotopath(photopath);
                        child.setServer_id(nutrition_id);

                        if (ObjsqliteHelper.getTotalCount("nutrition",
                                "nutrition_id", child_id) == 0) {
                            if (ObjsqliteHelper.NutritionRegistration2(child) > 0) {
                                totalPW++;
                            }
                        }

                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalPW + " children monitoring data synced successfully!";
    }

    public String GetPWMonitoringList(int womenID) {
        PregnantWomenMonitor[] women = null;

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url
                + "pregnent_women_monitoring_list.php");
        String line = "";
        String json = "";
        int totalPW = 0;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs
                    .add(new BasicNameValuePair("women_id", womenID + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                if (json.length() > 0) {
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        String women_id;
                        String hb, c_date, weight;

                        women_id = jsonChildNode.optString("women_id")
                                .toString();
                        weight = jsonChildNode.optString("weight").toString();

                        hb = jsonChildNode.optString("hb").toString();
                        c_date = jsonChildNode.optString("c_date").toString();

                        PregnantWomenMonitor pwm = new PregnantWomenMonitor();
                        pwm.setPregnant_women_id(women_id);
                        pwm.setHb(hb);
                        pwm.setWeight(weight);
                        pwm.setCurrent_date(c_date);
                        pwm.setServer_id(Integer.parseInt(women_id));

                        if (ObjsqliteHelper.getTotalCount(
                                "pregnant_womem_monitor", "server_id",
                                pwm.getServer_id()) == 0) {
                            if (ObjsqliteHelper.PregnantWomenMonitor2(pwm) > 0)
                                totalPW++;
                        }
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalPW + " pregnant women data synced successfully!";
    }

    public String GetEligibleFamilyList(int userID) {
        // TODO Auto-generated method stub
        Parent[] parents = null;

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "eligiblefamily.php");
        String line = "";
        String json = "";

        int totalEligibleFamily = 0;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("anganwadi_id", user_id
                    + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                if (json.length() > 0) {
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        int cast, caste_category, family_id, religion, has_toilet, have_water, gender, aganwari_id;
                        String date_or_recording, houseno, head_family, aadhar_card, mobile_num, latitude, longitude, app_version;

                        family_id = Integer.parseInt(jsonChildNode.optString(
                                "familyid").toString());
                        houseno = jsonChildNode.optString("house_number")
                                .toString();
                        latitude = jsonChildNode.optString("latitude")
                                .toString();
                        longitude = jsonChildNode.optString("longitude")
                                .toString();
                        head_family = jsonChildNode.optString("head_family")
                                .toString();

                        has_toilet = Integer.parseInt(jsonChildNode.optString(
                                "toilet_type").toString());

                        have_water = Integer.parseInt(jsonChildNode.optString(
                                "water_source").toString());

                        religion = Integer.parseInt(jsonChildNode.optString(
                                "religion_id").toString());

                        caste_category = Integer.parseInt(jsonChildNode
                                .optString("cast_category").toString());
                        cast = Integer.parseInt(jsonChildNode.optString("cast")
                                .toString());

                        aganwari_id = Integer.parseInt(jsonChildNode.optString(
                                "anganwadi_center_id").toString());

                        aadhar_card = jsonChildNode.optString("adhaar_card")
                                .toString();

                        //mobile_num = jsonChildNode.optString("mobile_num")
                        //		.toString();
                        gender = Integer.parseInt(jsonChildNode.optString(
                                "gender").toString());

                        app_version = jsonChildNode.optString(
                                jsonChildNode.optString("app_version")
                                        .toString()).toString();

                        date_or_recording = jsonChildNode.optString(
                                "date_or_recording").toString();

                        Parent parent = new Parent();
                        parent.setFamily_id(family_id);
                        parent.setHouseNo(houseno);
                        parent.setHeadofHH(head_family);
                        parent.setLatitude(latitude);
                        parent.setLongitude(longitude);
                        parent.setHas_toilet(has_toilet);
                        parent.setHave_water(have_water);
                        parent.setAadharCardHH(aadhar_card);
                        //	parent.setMobileHH(mobile_num);
                        parent.setIntcastecat(caste_category);
                        // parent.setIntcast(cast);
                        parent.setIntGender(gender);
                        parent.setReligion(religion);
                        parent.setAppVer(app_version);
                        parent.setDateOfRecord(date_or_recording);

                        parent.setUser_id(aganwari_id);

                        parent.setServer_id(family_id);
                        parent.setStatus(1);

                        if (ObjsqliteHelper.getTotalCount("eligible_family",
                                "server_id", family_id) == 0) {
                            if (ObjsqliteHelper
                                    .EligibleFamilyRegistration2(parent) > 0)
                                totalEligibleFamily++;
                        }

                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalEligibleFamily
                + " EligibleFamily data synced successfully!";
    }

    /*
     * Adolcent Registration
     * -------------------------------------------------------------------
     * http://jsw-np.in/anganwadi/apies/adolcent_reg.php
     * ---------------------------------------------------- house_number
     * girl_name father_name date_of_birth height weight HB OSP chronical
     */
    public String AdolescentGirlRegisteration(Adolescent adolescent) {
        // TODO Auto-generated method stub

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "adolescent_reg_img.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    11);
            // String
            // date=ChangeDateFormate(child.getDate_of_birth().toString());
            // right
            nameValuePairs.add(new BasicNameValuePair("house_number",
                    adolescent.getHhId()));
            nameValuePairs.add(new BasicNameValuePair("adolescent_name", adolescent
                    .getNameOfTheGirl().toString()));
            nameValuePairs.add(new BasicNameValuePair("father_name", adolescent
                    .getGirlFatherName()));
            nameValuePairs.add(new BasicNameValuePair("date_of_birth",
                    adolescent.getDateOfBirth()));
            nameValuePairs.add(new BasicNameValuePair("height", adolescent
                    .getGirlHeight()));
            nameValuePairs.add(new BasicNameValuePair("weight", adolescent
                    .getGirlWeight()));
            nameValuePairs.add(new BasicNameValuePair("HB", adolescent
                    .getGirlHb()));
            nameValuePairs.add(new BasicNameValuePair("OSP", adolescent
                    .getospYear() + adolescent.getospMonth()));
            nameValuePairs.add(new BasicNameValuePair("chronical", adolescent
                    .getChronicDisease()));
            nameValuePairs.add(new BasicNameValuePair("app_version", GlobalVars.App_Version
            ));

            nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id", user_id + ""));
            nameValuePairs.add(new BasicNameValuePair("user_master_id",
                    user_master_id + ""));
            nameValuePairs.add(new BasicNameValuePair("img",
                    adolescent.getImage()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();
            try {

                // {"success":"1","message":"Save Succssesful","user_id":17}
                /*JSONObject ojdata = new JSONObject(json);
                String inserted_id1 = ojdata.getString("user_id");
				 */

                int a = json.lastIndexOf(":");
                int b = json.lastIndexOf("}");
                inserted_id = json.substring(a + 1, b);
                if (!inserted_id.equalsIgnoreCase("0")) {
                    long id = ObjsqliteHelper.updateAndChangeServer("adolescent",
                            inserted_id,
                            "adolescent_id =" + adolescent.getAdolescent_id(), "img");

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return inserted_id;
    }

    public String EFamilyDataUpdate(String latitude, String longitude,
                                    String house_no) {
        // TODO Auto-generated method stub

        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "eligiblefamily_update.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    11);

            nameValuePairs
                    .add(new BasicNameValuePair("house_number", house_no));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            nameValuePairs.add(new BasicNameValuePair("AppV",
                    GlobalVars.App_Version));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return inserted_id;
    }

    public String downloadGetEligibleFamilyListi(String lat, String longi,
                                                 int ang_id, SqliteHelper sqliteHelper) {
        // TODO Auto-generated method stub
        // Parent[] parents = null;

        // String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "eligiblefamily.php");
        String line = "";
        String json = "";

        int totalEligibleFamily = 0;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Lat", lat));
            nameValuePairs.add(new BasicNameValuePair("Long", longi));
            nameValuePairs.add(new BasicNameValuePair("anganwadi_id", ang_id
                    + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();

            try {
                if (json.length() > 0) {
                    sqliteHelper.deleteAllDataFromTable("eligible_family", 1);
                    JSONArray jsonMainNode = new JSONArray(json);

                    int lengthJsonArr = jsonMainNode.length();

                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(i);

                        int caste_category, family_id, religion, has_toilet, have_water, gender, aganwari_id;
                        String date_or_recording, houseno, head_family, aadhar_card, mobile_num, latitude, longitude, app_version;

                        family_id = Integer.parseInt(jsonChildNode.optString(
                                "familyid").toString());
                        houseno = jsonChildNode.optString("house_number")
                                .toString();
                        latitude = jsonChildNode.optString("latitude")
                                .toString();
                        longitude = jsonChildNode.optString("longitude")
                                .toString();
                        head_family = jsonChildNode.optString("head_family")
                                .toString();
                        has_toilet = Integer.parseInt(jsonChildNode.optString(
                                "toilet_type").toString());
                        have_water = Integer.parseInt(jsonChildNode.optString(
                                "water_source").toString());
                        // religion =
                        // Integer.parseInt(jsonChildNode.optString("religion_id").toString());
                        religion = Integer.parseInt(jsonChildNode.optString(
                                "religion_id").toString());
                        caste_category = Integer.parseInt(jsonChildNode
                                .optString("cast_category").toString());
                        // caste=
                        // Integer.parseInt(jsonChildNode.optString("cast").toString());
                        aganwari_id = Integer.parseInt(jsonChildNode.optString(
                                "anganwadi_center_id").toString());
                        aadhar_card = jsonChildNode.optString("adhaar_card")
                                .toString();
                        //mobile_num = jsonChildNode.optString("mobile_num")
                        //	.toString();
                        gender = Integer.parseInt(jsonChildNode.optString(
                                "gender").toString());
                        app_version = jsonChildNode.optString(
                                jsonChildNode.optString("app_version")
                                        .toString()).toString();
                        date_or_recording = jsonChildNode.optString(
                                "date_or_recording").toString();

                        Parent parent = new Parent();
                        parent.setFamily_id(family_id);
                        parent.setHouseNo(houseno);
                        parent.setHeadofHH(head_family);
                        parent.setLatitude(latitude);
                        parent.setLongitude(longitude);
                        parent.setHas_toilet(has_toilet);
                        parent.setHave_water(have_water);
                        parent.setAadharCardHH(aadhar_card);
                        //parent.setMobileHH(mobile_num);
                        parent.setIntcastecat(caste_category);
                        // parent.setIntcast(caste_category);
                        parent.setIntGender(gender);
                        parent.setReligion(religion);
                        parent.setAppVer(app_version);
                        parent.setDateOfRecord(date_or_recording);

                        parent.setUser_id(aganwari_id);

                        parent.setServer_id(family_id);
                        parent.setStatus(3);

                        // if (ObjsqliteHelper.getTotalCount("parents",
                        // "server_id", family_id) == 0) {
                        // if
                        // (ObjsqliteHelper.EligibleFamilyRegistration2(parent)
                        // > 0)totalEligibleFamily++;
                        // }
                        sqliteHelper.EligibleFamilyRegistration(parent, 3, "");
//                        sqliteHelper.EligibleFamilyRegistration(parent, 3);
                    }

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return totalEligibleFamily
                + " EligibleFamily data synced successfully!";
    }


    //get all anganwadi center names and id
    public String getAnganwadiNames() {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "download_anganwadi_center_details.php");
        String line = "";
        String json = "";
        String result = "0";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_master_id", String.valueOf(user_master_id)));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));

            while ((line = rd.readLine()) != null) {

                json += line;
            }
            rd.close();

            if (json.length() > 0) {

                JSONArray alldata = new JSONArray(json);
                for (int i = 0; i < alldata.length(); i++) {

                    JSONObject family = alldata.getJSONObject(i);
                    {
                        String[] ASDF = new String[47];
                        ASDF[0] = family.getString("id");
                        ASDF[1] = family.getString("anganwadi_center_name");
                        ASDF[2] = family.getString("awc_type");
                        ASDF[3] = family.getString("building_status");
                        ASDF[4] = family.getString("building_type");
                        ASDF[5] = family.getString("water_source");
                        ASDF[6] = family.getString("water_safety");
                        ASDF[7] = family.getString("toilet_availability");
                        ASDF[8] = family.getString("water_availability");
                        ASDF[9] = family.getString("hand_washing_facility");
                        ASDF[10] = family.getString("learning_teaching");
                        ASDF[11] = family.getString("equipment");
                        ASDF[12] = family.getString("latitude");
                        ASDF[13] = family.getString("longitude");
                        ASDF[14] = ""; //family.getString("anganwadi_photo1");
                        ASDF[15] = ""; //family.getString("anganwadi_photo2");
                        ASDF[16] = ""; //family.getString("anganwadi_photo3");
                        ASDF[17] = ""; //family.getString("anganwadi_photo4");
                        ASDF[18] = family.getString("sevika_name");
                        ASDF[19] = family.getString("sevika_mobileno");
                        ASDF[20] = family.getString("sevika_alternate_mobileno");
                        ASDF[21] = family.getString("sevika_date_of_birth");
                        ASDF[22] = family.getString("sevika_date_of_joining");
                        ASDF[23] = family.getString("sevika_education");
                        ASDF[24] = family.getString("awc_sevika_position");
                        ASDF[25] = family.getString("temporary_incharge");
                        ASDF[26] = family.getString("which_awc");
                        ASDF[27] = family.getString("sevika_cast");
                        ASDF[28] = family.getString("sevika_religion");
                        ASDF[29] = family.getString("sevika_training");
                        ASDF[30] = family.getString("sevika_residence");
                        ASDF[31] = family.getString("sevika_distance_from_awc");
                        ASDF[32] = ""; //family.getString("sevika_passport_photo");
                        ASDF[33] = ""; //family.getString("sevika_photo");
                        ASDF[34] = family.getString("helper_name");
                        ASDF[35] = ""; //family.getString("helper_photo");
                        ASDF[36] = ""; //family.getString("helper_passport_photo");
                        ASDF[37] = family.getString("helper_mobileno");
                        ASDF[38] = family.getString("helper_alternate_mobileno");
                        ASDF[39] = family.getString("helper_date_of_birth");
                        ASDF[40] = family.getString("helper_date_of_joining");
                        ASDF[41] = family.getString("helper_education");
                        ASDF[42] = family.getString("helper_training");
                        ASDF[43] = family.getString("helper_residence");
                        ASDF[44] = family.getString("helper_awc_distance");
                        ASDF[45] = family.getString("sevika_cast_category");
                        ASDF[46] = family.getString("beat_id");

                        long downAnganwadi = ObjsqliteHelper.AnganwadiDownload(ASDF);
                        if (downAnganwadi > 1) {
                            result = "1";
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public String beatDownload() {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "download_beat.php");
        String line = "";
        String json = "";
        String result = "0";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_master_id", String.valueOf(user_master_id)));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));

            while ((line = rd.readLine()) != null) {

                json += line;
            }
            rd.close();

            if (json.length() > 0) {

                JSONArray alldata = new JSONArray(json);
                for (int i = 0; i < alldata.length(); i++) {

                    JSONObject beat = alldata.getJSONObject(i);
                    {
                        String[] Beat = new String[47];
                        Beat[0] = beat.getString("beat_id");
                        Beat[1] = beat.getString("beat_name");
                        Beat[2] = beat.getString("project_id");
                        Beat[3] = beat.getString("taluka_id");
                        Beat[4] = beat.getString("district_id");
                        Beat[5] = beat.getString("state_id");

                        long beatDownload = ObjsqliteHelper.BeatDownload(Beat);
                        if (beatDownload > 1) {
                            result = "1";
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }


    public String uploadOutAttendance(attendance atten) {
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "yashoda_attendance_out.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            nameValuePairs.add(new BasicNameValuePair("outTime", atten.getoutTime()));
            nameValuePairs.add(new BasicNameValuePair("outLatitude", atten.getoutLatitude()));
            nameValuePairs.add(new BasicNameValuePair("outLongitude", atten.getoutLongitude()));
            nameValuePairs.add(new BasicNameValuePair("serverID", atten.getServerID()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            ObjsqliteHelper.updateAttenServer("users_timing", Integer.parseInt(atten.getServerID()), "5", "ctr = " + atten.getCount());
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return inserted_id;
    }

    public String uploadSevikaHelper(sevika_helper atten) {
        String inserted_id = "";

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "sevika_helper.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
            nameValuePairs.add(new BasicNameValuePair("userID", atten.getUserid()));
            nameValuePairs.add(new BasicNameValuePair("anganwadiID", atten.getanganwadiid()));
            nameValuePairs.add(new BasicNameValuePair("date", atten.getdateofstatus()));
            nameValuePairs.add(new BasicNameValuePair("sevika", atten.getsevikastatus()));
            nameValuePairs.add(new BasicNameValuePair("helper", atten.gethelperstatus()));
            nameValuePairs.add(new BasicNameValuePair("Latitude", atten.getlattitude()));
            nameValuePairs.add(new BasicNameValuePair("Longitude", atten.getlongitude()));
            nameValuePairs.add(new BasicNameValuePair("appVersion", atten.getappversion()));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            HttpEntity data = response.getEntity();

            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }

            rd.close();

            try {
                JSONObject user = new JSONObject(json);
                if (user.has("user_id")) {
                    int intuser_id = user.getInt("user_id");
                    if (intuser_id > 0) {
                        inserted_id = intuser_id + "";
                        long id = ObjsqliteHelper.updateChangeServer("sevika_helper_status", "1", "ctr = " + atten.getctr());
                    }
                } else {
                }
            } catch (JSONException e) {
            }

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
            Log.d("Error", e.getMessage());
        }
        return inserted_id;
    }

    public String uploadAttendance(attendance atten) {
        String inserted_id = "";
        if (atten.getStatus() == 0) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "yashoda_attendance.php");

            String line = "";
            String json = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                nameValuePairs.add(new BasicNameValuePair("userID", atten.getUserID()));
                nameValuePairs.add(new BasicNameValuePair("anganwadiID", atten.getAnganwadiID()));
                nameValuePairs.add(new BasicNameValuePair("date", atten.getDate()));
                nameValuePairs.add(new BasicNameValuePair("inTime", atten.getinTime()));
                nameValuePairs.add(new BasicNameValuePair("outTime", atten.getoutTime()));
                nameValuePairs.add(new BasicNameValuePair("inLatitude", atten.getinLatitude()));
                nameValuePairs.add(new BasicNameValuePair("inLongitude", atten.getinLongitude()));
                nameValuePairs.add(new BasicNameValuePair("outLatitude", atten.getoutLatitude()));
                nameValuePairs.add(new BasicNameValuePair("outLongitude", atten.getoutLongitude()));
                nameValuePairs.add(new BasicNameValuePair("appVersion", GlobalVars.App_Version));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity data = response.getEntity();

                InputStream is = null;
                is = data.getContent();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = rd.readLine()) != null) {
                    json += line;
                }

                rd.close();

                try {
                    JSONObject user = new JSONObject(json);
                    if (user.has("user_id")) {
                        int intuser_id = user.getInt("user_id");
                        if (intuser_id > 0) {
                            inserted_id = intuser_id + "";
                            long id = ObjsqliteHelper.updateAttenServer("users_timing", intuser_id, "5", "ctr = " + atten.getCount());
                        }
                    } else {
                    }
                } catch (JSONException e) {
                }

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
            }
        } else {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url + "yashoda_attendance_out.php");

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
                nameValuePairs.add(new BasicNameValuePair("serverID", atten.getServerID()));
                nameValuePairs.add(new BasicNameValuePair("outTime", atten.getoutTime()));
                nameValuePairs.add(new BasicNameValuePair("outLatitude", atten.getoutLatitude()));
                nameValuePairs.add(new BasicNameValuePair("outLongitude", atten.getoutLongitude()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);

                HttpEntity data = response.getEntity();

                ObjsqliteHelper.updateAttenServer("users_timing", Integer.parseInt(atten.getServerID()), "5", "ctr = " + atten.getCount());

            } catch (ClientProtocolException e) {
            } catch (IOException e) {
                Log.d("Error", e.getMessage());
            }
        }
        return inserted_id;
    }

    public String UploadAdolscentBaselineData(AdolBaseline adolBaseline) {
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "insert_general.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    11);
            // String
            // date=ChangeDateFormate(child.getDate_of_birth().toString());
            // right
            nameValuePairs.add(new BasicNameValuePair("table_name", "adolscent_baseline"));
         //   nameValuePairs.add(new BasicNameValuePair("id", adolBaseline.getId()));
            nameValuePairs.add(new BasicNameValuePair("adolscent_name", adolBaseline.getAdolName()));
            nameValuePairs.add(new BasicNameValuePair("address", adolBaseline.getAddress()));
            nameValuePairs.add(new BasicNameValuePair("parent_name", adolBaseline.getParentName()));
            nameValuePairs.add(new BasicNameValuePair("contact_number", adolBaseline.getContactNumber()));
            nameValuePairs.add(new BasicNameValuePair("height", adolBaseline.getHeight()));
            nameValuePairs.add(new BasicNameValuePair("weight", adolBaseline.getWeight()));
            nameValuePairs.add(new BasicNameValuePair("age", adolBaseline.getAge()));
            nameValuePairs.add(new BasicNameValuePair("muac", adolBaseline.getMUAC()));
            nameValuePairs.add(new BasicNameValuePair("school_name", adolBaseline.getSchoolName()));
            nameValuePairs.add(new BasicNameValuePair("class", adolBaseline.getClassName()));
            nameValuePairs.add(new BasicNameValuePair("unique_id", adolBaseline.getUniqueId()));
            nameValuePairs.add(new BasicNameValuePair("village_name", adolBaseline.getVillageName()));
            nameValuePairs.add(new BasicNameValuePair("block_name", adolBaseline.getBlockName()));
            nameValuePairs.add(new BasicNameValuePair("district_name", adolBaseline.getDistrictName()));
            nameValuePairs.add(new BasicNameValuePair("heard_of_aneamia", adolBaseline.getHeardOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("source_of_info", adolBaseline.getSourceOfInfo()));
            nameValuePairs.add(new BasicNameValuePair("what_is_aneamia", adolBaseline.getWhatIsAneamia()));
            nameValuePairs.add(new BasicNameValuePair("which_nutrient_def", adolBaseline.getWhichNutrientDef()));
            nameValuePairs.add(new BasicNameValuePair("cause_of_aneamia", adolBaseline.getCauseOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("signs_of_aneamia", adolBaseline.getSignsOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("effect_of_aneamia", adolBaseline.getEffectsOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("measures_of_aneamia", adolBaseline.getMeasuresOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("iron_rich_food", adolBaseline.getIronRichFood()));
            nameValuePairs.add(new BasicNameValuePair("more_iron_needs", adolBaseline.getMoreIronNeeds()));
            nameValuePairs.add(new BasicNameValuePair("you_are_aneamic", adolBaseline.getYouAreAneamic()));
            nameValuePairs.add(new BasicNameValuePair("how_serious_aneamia", adolBaseline.getHowSeriousAneamia()));
            nameValuePairs.add(new BasicNameValuePair("get_iron_tablet", adolBaseline.getGetIronTablet()));
            nameValuePairs.add(new BasicNameValuePair("how_take_iron_tablet", adolBaseline.getHowTakeIronTablet()));
            nameValuePairs.add(new BasicNameValuePair("like_iron_tablet", adolBaseline.getLikeIronTablet()));
            nameValuePairs.add(new BasicNameValuePair("food_type_consume", adolBaseline.getFoodTypeConsume()));
            nameValuePairs.add(new BasicNameValuePair("consume_past_week", adolBaseline.getConsumePastWeek()));
            nameValuePairs.add(new BasicNameValuePair("peas", adolBaseline.getPeas()));
            nameValuePairs.add(new BasicNameValuePair("seafood", adolBaseline.getSeafood()));
            nameValuePairs.add(new BasicNameValuePair("egg", adolBaseline.getEgg()));
            nameValuePairs.add(new BasicNameValuePair("meat", adolBaseline.getMeat()));
            nameValuePairs.add(new BasicNameValuePair("greenleaf", adolBaseline.getGreenleaf()));
            nameValuePairs.add(new BasicNameValuePair("almonds", adolBaseline.getAlmonds()));
            nameValuePairs.add(new BasicNameValuePair("include_lemon_in_diet", adolBaseline.getIncludeLemonInDiet()));
            nameValuePairs.add(new BasicNameValuePair("deworming_tablet", adolBaseline.getDewormingTablet()));
            nameValuePairs.add(new BasicNameValuePair("frequently_albandazole_tablet", adolBaseline.getFrequentlyAlbandazoleTablet()));
            nameValuePairs.add(new BasicNameValuePair("had_checked_hb_before", adolBaseline.getHadCheckedHBBefore()));
            nameValuePairs.add(new BasicNameValuePair("hb", adolBaseline.getHB()));
            nameValuePairs.add(new BasicNameValuePair("app_version", adolBaseline.getApp_version()));
            nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id", adolBaseline.getAnganwadi_center_id()));
            nameValuePairs.add(new BasicNameValuePair("user_master_id", adolBaseline.getUser_master_id()));
            nameValuePairs.add(new BasicNameValuePair("longitude", adolBaseline.getLongitude()));
            nameValuePairs.add(new BasicNameValuePair("latitude", adolBaseline.getLattitude()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();
            try {

                // {"success":"1","message":"Save Succssesful","user_id":17}
                /*JSONObject ojdata = new JSONObject(json);
                String inserted_id1 = ojdata.getString("user_id");
				 */

                int a = json.lastIndexOf(":");
                int b = json.lastIndexOf("}");
                inserted_id = json.substring(a + 1, b);
                if (!inserted_id.equalsIgnoreCase("0")) {
                    long id = ObjsqliteHelper.updateChangeServer("adolscent_baseline", inserted_id, "id =" + adolBaseline.getId());

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inserted_id;
    }

    public String UploadPregnantWomenBaselineData(PW_Baseline pw_baseline) {
        String inserted_id = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "insert_general.php");

        String line = "";
        String json = "";
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                    11);
            // String
            // date=ChangeDateFormate(child.getDate_of_birth().toString());
            // right
            nameValuePairs.add(new BasicNameValuePair("table_name", "pregnant_women_baseline"));
          //  nameValuePairs.add(new BasicNameValuePair("id", pw_baseline.getId()));
            nameValuePairs.add(new BasicNameValuePair("women_name", pw_baseline.getWomenName()));
            nameValuePairs.add(new BasicNameValuePair("husband_name", pw_baseline.getHusbandName()));
            nameValuePairs.add(new BasicNameValuePair("village_name", pw_baseline.getVillageName()));
            nameValuePairs.add(new BasicNameValuePair("block_name", pw_baseline.getBlockName()));
            nameValuePairs.add(new BasicNameValuePair("district_name", pw_baseline.getDistrictName()));
            nameValuePairs.add(new BasicNameValuePair("contact", pw_baseline.getContact()));
            nameValuePairs.add(new BasicNameValuePair("age", pw_baseline.getAge()));
            nameValuePairs.add(new BasicNameValuePair("education", pw_baseline.getEducation()));
            nameValuePairs.add(new BasicNameValuePair("occupation", pw_baseline.getOccupation()));
            nameValuePairs.add(new BasicNameValuePair("husband_alive", pw_baseline.getHusbandAlive()));
            nameValuePairs.add(new BasicNameValuePair("no_of_children", pw_baseline.getNoOfChildren()));
            nameValuePairs.add(new BasicNameValuePair("no_of_girls", pw_baseline.getNoOfGirls()));
            nameValuePairs.add(new BasicNameValuePair("no_of_boys", pw_baseline.getNoOfBoys()));
            nameValuePairs.add(new BasicNameValuePair("mother_in_law", pw_baseline.getMotherInLaw()));
            nameValuePairs.add(new BasicNameValuePair("father_in_law", pw_baseline.getFatherInLaw()));
            nameValuePairs.add(new BasicNameValuePair("family_income", pw_baseline.getFamilyIncome()));
            nameValuePairs.add(new BasicNameValuePair("source_of_income", pw_baseline.getSourceOfIncome()));
            nameValuePairs.add(new BasicNameValuePair("breast_feeding_initiated", pw_baseline.getBreastFeedingInitiated()));
            nameValuePairs.add(new BasicNameValuePair("how_long_breast_feeding", pw_baseline.getHowLongBreastFeeding()));
            nameValuePairs.add(new BasicNameValuePair("when_start_food", pw_baseline.getWhenStartFood()));
            nameValuePairs.add(new BasicNameValuePair("decesion_to_start_feeding", pw_baseline.getDecesionToStartFeeding()));
            nameValuePairs.add(new BasicNameValuePair("breast_feeding_given_during", pw_baseline.getBreastFeedingGivenDuring()));
            nameValuePairs.add(new BasicNameValuePair("first_dose_of_tetnus", pw_baseline.getFirstDoseOfTetnus()));
            nameValuePairs.add(new BasicNameValuePair("second_dose_of_tetnus", pw_baseline.getSecondDoseOfTetnus()));
            nameValuePairs.add(new BasicNameValuePair("booster_tetnus", pw_baseline.getBoosterTetnus()));
            nameValuePairs.add(new BasicNameValuePair("vaccines_to_be_given", pw_baseline.getVaccinesToBeGiven()));
            nameValuePairs.add(new BasicNameValuePair("iron_supplement_important", pw_baseline.getIronSupplementImportant()));
            nameValuePairs.add(new BasicNameValuePair("trimester_of_deworming", pw_baseline.getTrimesterOfDeworming()));
            nameValuePairs.add(new BasicNameValuePair("often_take_iron_tablet", pw_baseline.getOftenTakeIronTablet()));
            nameValuePairs.add(new BasicNameValuePair("iron_rich_food", pw_baseline.getIronRichFood()));
            nameValuePairs.add(new BasicNameValuePair("iron_tablet_from_govt", pw_baseline.getIronTabletFromGovt()));
            nameValuePairs.add(new BasicNameValuePair("hand_washing", pw_baseline.getHandWashing()));
            nameValuePairs.add(new BasicNameValuePair("hand_washing_when", pw_baseline.getHandWashingWhen()));
            nameValuePairs.add(new BasicNameValuePair("how_diarrhea_prevented", pw_baseline.getHowDiarrheaPrevented()));
            nameValuePairs.add(new BasicNameValuePair("source_of_water", pw_baseline.getSourceOfWater()));
            nameValuePairs.add(new BasicNameValuePair("boil_water_before_drinking", pw_baseline.getBoilWaterBeforeDrinking()));
            nameValuePairs.add(new BasicNameValuePair("have_toilet_at_home", pw_baseline.getHaveToiletAtHome()));
            nameValuePairs.add(new BasicNameValuePair("keep_surrounding_clean", pw_baseline.getKeepSurroundingClean()));
            nameValuePairs.add(new BasicNameValuePair("heard_aneamia", pw_baseline.getHeardAneamia()));
            nameValuePairs.add(new BasicNameValuePair("nutrient_deficient_in_aneamia", pw_baseline.getNutrientDeficientInAneamia()));
            nameValuePairs.add(new BasicNameValuePair("cause_of_aneamia", pw_baseline.getCauseOfAneamia()));
            nameValuePairs.add(new BasicNameValuePair("item_eat_at_home", pw_baseline.getItemEatAtHome()));
            nameValuePairs.add(new BasicNameValuePair("consume_veg_or_non_veg", pw_baseline.getConsumeVegOrNonVeg()));
            nameValuePairs.add(new BasicNameValuePair("important_of_child_nutrition", pw_baseline.getImportantOfChildNutrition()));
            nameValuePairs.add(new BasicNameValuePair("different_food_important", pw_baseline.getDifferentFoodImportant()));
            nameValuePairs.add(new BasicNameValuePair("visit_anganwadi_center", pw_baseline.getVisitAnganwadiCenter()));
            nameValuePairs.add(new BasicNameValuePair("attend_meeting", pw_baseline.getAttendMeeting()));
            nameValuePairs.add(new BasicNameValuePair("aware_of_govt_scheme", pw_baseline.getAwareOfGovtScheme()));
            nameValuePairs.add(new BasicNameValuePair("have_mother_card", pw_baseline.getHaveMotherCard()));
            nameValuePairs.add(new BasicNameValuePair("what_to_do_in_infection", pw_baseline.getWhatToDoInInfection()));
            nameValuePairs.add(new BasicNameValuePair("app_version", pw_baseline.getApp_version()));
            nameValuePairs.add(new BasicNameValuePair("anganwadi_center_id", pw_baseline.getAnganwadi_center_id()));
            nameValuePairs.add(new BasicNameValuePair("user_master_id", pw_baseline.getUser_master_id()));
            nameValuePairs.add(new BasicNameValuePair("longitude", pw_baseline.getLongitude()));
            nameValuePairs.add(new BasicNameValuePair("latitude", pw_baseline.getLattitude()));



            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity data = response.getEntity();
            InputStream is = null;
            is = data.getContent();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            rd.close();
            try {

                // {"success":"1","message":"Save Succssesful","user_id":17}
                /*JSONObject ojdata = new JSONObject(json);
                String inserted_id1 = ojdata.getString("user_id");
				 */

                int a = json.lastIndexOf(":");
                int b = json.lastIndexOf("}");
                inserted_id = json.substring(a + 1, b);
                if (!inserted_id.equalsIgnoreCase("0")) {
                    long id = ObjsqliteHelper.updateChangeServer("pregnant_women_baseline", inserted_id, "id =" + pw_baseline.getId());

                    if (id > 0) {
                        Log.d("Updated", "Updated successfully");
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inserted_id;
    }


    //download EventMeeting
    @SuppressLint("NewApi")
    public String DownloadEventMeetingData(String created_on) {
        String lastResult="";
        DownloadData downloadData = new DownloadData();
        downloadData.setCreated_on("");
        Gson gson = new Gson();
        String data = gson.toJson(downloadData);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        final M_Health_API apiService = ApiClient.getClient().create(M_Health_API.class);
        Call<JsonArray> call = apiService.callDownloadEventMeetingApi(body);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    JSONArray jsonArray=new JSONArray(response.body().toString());
                    Log.e("downloadEventMeeting>>>", "onResponse events >>>"+jsonArray.toString());
                    ObjsqliteHelper.dropTable("events");
                    SyncActivity.pbEveMee.setMax(jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                        String serverId=jsonObject.getString("events_id");

                        Iterator keys = jsonObject.keys();
                        ContentValues contentValues = new ContentValues();
                        while (keys.hasNext()) {
                            String currentDynamicKey = (String) keys.next();
                            if (currentDynamicKey.equals("events_id")){
                                contentValues.put("server_id", jsonObject.get(currentDynamicKey).toString());
                            }
                            contentValues.put(currentDynamicKey, jsonObject.get(currentDynamicKey).toString());
                        }

                        long eventMeetingId=ObjsqliteHelper.saveMasterTable(contentValues, "events");
                        SyncActivity.pbEveMee.setProgress(i + 1);
                        if (eventMeetingId > 0) {
                            eventID++;
                            streventMeetingId = eventID + "Events";
                        }
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("", "");
            }
        });
        lastResult = streventMeetingId;
        return lastResult;
    }

}
