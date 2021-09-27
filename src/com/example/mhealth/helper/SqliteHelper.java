package com.example.mhealth.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Spinner;

import com.example.mhealth.model.AttendanceImagePojo;
import com.example.mhealth.model.ChildBehaviourChange;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SqliteHelper {
    MyDbHelper mydb = null;
    SQLiteDatabase DB = null;
    Context context;
    SharedPrefHelper sph;
    int user_id;
    int events_id;
    int user_master_id;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar c = Calendar.getInstance();
    String formattedDate = dateFormat.format(c.getTime());
    private String query;

    public SqliteHelper(Context context) {
        this.context = context;
        mydb = new MyDbHelper(context);
        sph = new SharedPrefHelper(context);

        user_id = sph.getInt("user_id", 0);
        user_master_id = sph.getInt("user_master_id", 0);
        events_id = sph.getInt("events_id", 0);
    }
    public void dropTable(String tablename) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            DB.execSQL("DELETE FROM'" + tablename + "' WHERE status<>0");
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }

    }
    public long saveMasterTable(ContentValues contentValues, String tablename) {
        mydb.openDataBase();
        DB = mydb.getDb();
        if (tablename.equals("eligible_family")
                ||tablename.equals("pregnant_women")
                ||tablename.equals("mother")
                ||tablename.equals("child")
                ||tablename.equals("adolescent")
                ||tablename.equals("pregnant_womem_monitor")
                ||tablename.equals("child_nutrition_monitoring")
                ||tablename.equals("adolescent_monitoring")
                ||tablename.equals("child_behavior_change")){
            contentValues.put("status", 1);
            //contentValues.put("server_id", serverId);
        }
        long idsds = DB.insert(tablename, null, contentValues);
        Log.d("LOG", idsds + " id");
        DB.close();
        return idsds;
    }

    public ArrayList<Weight> getAgeAndWeight() {

        Weight weight;
        ArrayList<Weight> childWeight = new ArrayList<Weight>();
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select * from(SELECT  IMONTH, M3SD, M2SD, M1SD, MEDIAN, 1SD, 2SD, 3SD,if((select max(weight) from child_nutrition_monitoring where child_id=1 and  TIMESTAMPDIFF(MONTH,(select date_of_birth from child where child_id=1),date_of_monitoring)=a.IMONTH) IS NULL,'null' ,(select max(weight) from child_nutrition_monitoring where child_id=1 and  TIMESTAMPDIFF(MONTH,(select date_of_birth from child where child_id=1),date_of_monitoring)=a.IMONTH)) weight FROM ideal_weight_boy a) b";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    weight = new Weight();
                    try {
                        weight.setIMONTH(cur.getString(cur
                                .getColumnIndex("IMONTH")));
                        weight.setM3SD(cur.getString(cur.getColumnIndex("M3SD")));
                        weight.setM2SD(cur.getString(cur.getColumnIndex("M2SD")));
                        weight.setM1SD(cur.getString(cur.getColumnIndex("M1SD")));
                        weight.setMEDIAN(cur.getString(cur
                                .getColumnIndex("MEDIAN")));
                        weight.setS1SD(cur.getString(cur.getColumnIndex("1SD")));
                        weight.setS2SD(cur.getString(cur.getColumnIndex("2SD")));
                        weight.setS3SD(cur.getString(cur.getColumnIndex("3SD")));
                        childWeight.add(weight);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return childWeight;
    }

    public long adolescentNutritionMonitoring(
            AdolescentMonitoring adolescentMonitoring) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("adolescent_id",adolescentMonitoring.getAdolescentGirlID());
                values.put("weight", adolescentMonitoring.getAdolescentWeight());
                values.put("height", adolescentMonitoring.getAdolescentHeight());
                values.put("hb", adolescentMonitoring.getAdolescentHb());
                values.put("migration_type", adolescentMonitoring.getMgrStatus());
                values.put("date_of_recording", adolescentMonitoring.getDate_of_screening());
                values.put("girl_nutrition_server_id", adolescentMonitoring.getGirl_nut_id());
                values.put("server_id", adolescentMonitoring.getServerId());
                values.put("anganwadi_center_id", user_id);
                values.put("status", adolescentMonitoring.getStatus());
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("img", adolescentMonitoring.getImage());
                values.put("marked_as", adolescentMonitoring.getMarked_as());
                values.put("is_hygiene_kit", adolescentMonitoring.getIs_hygiene_kit());
                values.put("death_date", adolescentMonitoring.getDeath_date());
                values.put("mobile_unique_id", adolescentMonitoring.getMobile_unique_id());
                values.put("created_on_mobile", adolescentMonitoring.getCreated_on_mobile());
                values.put("consumption_of_ifa", adolescentMonitoring.getConsumption_of_ifa());
                values.put("periods", adolescentMonitoring.getPeriods());
                values.put("is_adolescent_access_icds", adolescentMonitoring.getIs_adolescent_access_icds());
                values.put("is_adolescent_rececive_ifa", adolescentMonitoring.getIs_adolescent_rececive_ifa());
                values.put("is_taken_ifa", adolescentMonitoring.getIs_taken_ifa());
                values.put("is_adolescent_dewarming_tablet", adolescentMonitoring.getIs_adolescent_dewarming_tablet());
                values.put("is_health_service", adolescentMonitoring.getIs_health_service());
                values.put("deworming_done", adolescentMonitoring.getDeworming_done());
                values.put("date_of_screening", adolescentMonitoring.getDate_of_screening());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                id = DB.insert("adolescent_monitoring", null, values);
            }
        } catch (Exception s) {
        }

        return id;
    }

    public long editAbsentadolescentNutrition(AdolescentMonitoring adolescentMonitoring, String curDate) {

        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("adolescent_id",
                        adolescentMonitoring.getAdolescentGirlID());
                values.put("weight", adolescentMonitoring.getAdolescentWeight());
                values.put("height", adolescentMonitoring.getAdolescentHeight());
                values.put("hb", adolescentMonitoring.getAdolescentHb());
                values.put("migration_status", "");
                values.put("date_of_recording",
                        adolescentMonitoring.getDateOfRecord());
                values.put("status", 2);
                values.put("server_id", adolescentMonitoring.getServerId());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("girl_nutrition_server_id", adolescentMonitoring.getGirl_nut_id());
                values.put("img", adolescentMonitoring.getImage());
                values.put("marked_as", adolescentMonitoring.getMarked_as());
                id = DB.update("adolescent_monitoring", values, "adolescent_id=" + adolescentMonitoring.getAdolescentGirlID() + " and anganwadi_center_id=" + user_id + " and date_of_recording like '" + curDate + "'", null);
                //Log.d("ChildNutritionMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
        DB.close();
        return id;
    }


    // EligibleFamilyList
    public int getFamilyId(String aadhaar) {

        int familyid = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select familyid from eligible_family where adhaar_card ="
                + aadhaar;

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        familyid = cur.getInt(cur.getColumnIndex("familyid"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return familyid;

    }


    public int getPregWomenIdbyNameNHHID(String HouseNo, String PregWName) {

        int womenId = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select pregnant_women_id from pregnant_women where house_number ="
                + HouseNo + " and name_of_pregnant_women = '" + PregWName + "'";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        womenId = cur.getInt(cur
                                .getColumnIndex("pregnant_women_id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return womenId;

    }

    public int getMotherIdbyNameNHHID(String HouseNo, String MotherName) {

        int motherId = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select pregnant_women_id from pregnant_women where house_number ="
                + HouseNo + " and name_of_pregnant_women = '" + MotherName + "'";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        motherId = cur.getInt(cur
                                .getColumnIndex("pregnant_women_id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return motherId;

    }


    public ArrayList<Double> CompareHeightWeight(String IDEAl_weight_height_boys, float height) {
        ArrayList<Double> a_h = new ArrayList<Double>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                Log.e("............", "select * from " + IDEAl_weight_height_boys + " where height = " + height);
                cur = DB.rawQuery("select * from " + IDEAl_weight_height_boys + " where height = " + height, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                a_h.add(cur.getDouble(2));
                a_h.add(cur.getDouble(1));
            }
        }
        DB.close();
        return a_h;

    }

    public ArrayList<Double> CompareAgeWeight(String ideal_weight_boy, int age) {
        ArrayList<Double> a_h = new ArrayList<Double>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                Log.e("............", "select * from " + ideal_weight_boy + " where IMONTH = " + age);
                cur = DB.rawQuery("select * from " + ideal_weight_boy + " where IMONTH = " + age, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                a_h.add(cur.getDouble(3));
                a_h.add(cur.getDouble(2));
            }
        }
        DB.close();
        return a_h;

    }

    public int getAdoGirlIdbyNameNHHID(String HouseNo, String AdoGirlName) {

        int AdoGirlId = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select adolescent_id from adolescent where house_number ='"
                + HouseNo + "' and adolescent_name = '" + AdoGirlName + "'";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        AdoGirlId = cur.getInt(cur
                                .getColumnIndex("adolescent_id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return AdoGirlId;

    }


    public int getChildCount() {

        int count = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select count(child_id) from child where anganwadi_center_id= " + user_id + "";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        count = cur.getInt(cur
                                .getColumnIndex("count(child_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return count;

    }


    public int getAdolescentCount() {

        int count = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select count(adolescent_id) from adolescent where anganwadi_center_id= " + user_id + "";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        count = cur.getInt(cur.getColumnIndex("count(adolescent_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return count;

    }


    public int getPregnantCount() {

        int count = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select pregnant_women_id from pregnant_women where house_number="+
         * HouseNo+ "and pregnant_women_Name=" + PregWName ;
         */
        String query = "select count(pregnant_women_id) from pregnant_women where  flag != 'M' and anganwadi_center_id = " + user_id + " and date('now','start of month','-1 month')< edd";

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        count = cur.getInt(cur.getColumnIndex("count(pregnant_women_id)"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return count;

    }


    public ArrayList<Child> getAllAbsentChild(String curdate) {

        ArrayList<Child> childdetail = new ArrayList<Child>();
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select a.child_name,a.date_of_birth,b.father,a.birth_height,a.multimedia,b.mother from child_registration as a, parents as b on a.parent_name=b.parent_id where a.user_id="
         * + GlobalVars.UserID;
         */

        String query = "select a.name_of_child, b.child_id from child as a, child_nutrition_monitoring as b where b.migration_type = 'Absent' and b.date_of_recording like '" + curdate + "' and a.child_id = b.child_id and b.anganwadi_center_id=" + user_id + " and b.view_absent_status = 0 ";
        String query1 = "select a.name_of_child, b.child_id from child as a, child_nutrition_monitoring as b where b.migration_type = 'Temporary Migrate' and b.date_of_recording like '" + curdate + "' and a.child_id = b.child_id and b.anganwadi_center_id=" + user_id + " and b.view_absent_status = 0 ";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);

            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    Child child = new Child();
                    try {
                        child.setChild_name(cur.getString(cur
                                .getColumnIndex("name_of_child")));
                        child.setChild_id(cur.getInt(cur
                                .getColumnIndex("child_id")));
                        childdetail.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
            Cursor cur1 = DB.rawQuery(query1, null);
            if (cur1 != null && cur1.getCount() > 0) {
                cur1.move(0);
                while (cur1.moveToNext()) {

                    Child child = new Child();
                    try {
                        child.setChild_name(cur1.getString(cur
                                .getColumnIndex("name_of_child")));
                        child.setChild_id(cur1.getInt(cur
                                .getColumnIndex("child_id")));
                        childdetail.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return childdetail;


    }

    public ArrayList<PregnantWomen> getAllAbsentPregnantWomen(String curdate) {
        PregnantWomen pregnantWomen;
        ArrayList<PregnantWomen> pregnantWomenArrayListdetail = new ArrayList<PregnantWomen>();
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select a.child_name,a.date_of_birth,b.father,a.birth_height,a.multimedia,b.mother from child_registration as a, parents as b on a.parent_name=b.parent_id where a.user_id="
         * + GlobalVars.UserID;
         */

        String query = "select a.name_of_pregnant_women, b.women_id from pregnant_women as a, pregnant_womem_monitor as b where b.migration_status = 'Absent' and b.date_of_recording like '" + curdate + "' and a.pregnant_women_id = b.women_id and b.anganwadi_center_id=" + user_id + " and b.view_absent_status = 0";


        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    pregnantWomen = new PregnantWomen();
                    try {
                        pregnantWomen.setPreWomenName(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setPregnant_women_id(cur.getString(cur
                                .getColumnIndex("women_id")));
                        pregnantWomenArrayListdetail.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return pregnantWomenArrayListdetail;


    }

    public ArrayList<Adolescent> getAllAbsentAdolescentGirl(String curdate) {
        Adolescent adolescent;
        ArrayList<Adolescent> adolescentArrayList = new ArrayList<Adolescent>();
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select a.child_name,a.date_of_birth,b.father,a.birth_height,a.multimedia,b.mother from child_registration as a, parents as b on a.parent_name=b.parent_id where a.user_id="
         * + GlobalVars.UserID;
         */

        String query = "select a.girl_name, b.adolescent_id from adolescent as a, adolescent_monitoring as b where b.migration_status = 'Absent' and b.date_of_recording like '" + curdate + "' and b.adolescent_id = a.adolescent_id and b.anganwadi_center_id=" + user_id + " and b.view_absent_status = 0";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    adolescent = new Adolescent();
                    try {
                        adolescent.setNameOfTheGirl(cur.getString(cur
                                .getColumnIndex("girl_name")));
                        adolescent.setAdolescent_id(cur.getInt(cur
                                .getColumnIndex("adolescent_id")));
                        adolescentArrayList.add(adolescent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return adolescentArrayList;
    }

    public ArrayList<Child> getAllChildListing() {
        Child child;

        ArrayList<Child> childdetail = new ArrayList<Child>();
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select a.child_name,a.date_of_birth,b.father,a.birth_height,a.multimedia,b.mother from child_registration as a, parents as b on a.parent_name=b.parent_id where a.user_id="
         * + GlobalVars.UserID;
         */

        String query = "select a.photopath,a.child_id,a.house_number,a.birth_weight,a.hb,a.birth_height,a.birth_muac,a.lattitude,a.longitude,a.name_of_child,a.date_of_birth,b.name_of_pregnant_women from child as a, pregnant_women as b where a.pregnent_women_id=b.pregnant_women_id and a.anganwadi_center_id ="
                + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    child = new Child();
                    try {
                        child.setChild_name(cur.getString(cur
                                .getColumnIndex("name_of_child")));
                        child.setDate_of_birth(cur.getString(cur
                                .getColumnIndex("date_of_birth")));
                        child.setMultimedia(cur.getString(cur
                                .getColumnIndex("photopath")));
                        child.setChild_id(cur.getInt(cur
                                .getColumnIndex("child_id")));
                        child.setMother_name(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));

                        child.setHHID(cur.getString(cur.getColumnIndex("house_number")));
                        child.setChild_weight(cur.getString(cur.getColumnIndex("birth_weight")));
                        child.setHeight(cur.getString(cur.getColumnIndex("birth_height")));
                        child.setMuac(cur.getString(cur.getColumnIndex("birth_muac")));
                        child.setLatitude(cur.getString(cur.getColumnIndex("lattitude")));
                        child.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        child.setHb(cur.getString(cur.getColumnIndex("hb")));

                        childdetail.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return childdetail;

    }


    // return all registered parent list to parent listing page
    public ArrayList<Parent> allParentSearch() {
        Parent parent;

        ArrayList<Parent> parentdetail = new ArrayList<Parent>();
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select father,mother,address from eligible_family where user_id="
                + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();
                        parent.setMother(cur.getString(cur
                                .getColumnIndex("mother")));
                        parent.setFather(cur.getString(cur
                                .getColumnIndex("father")));
                        parent.setAddress(cur.getString(cur
                                .getColumnIndex("address")));
                        parentdetail.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return parentdetail;

    }

    public ArrayList<Parent> allEligibleFamilySearch(int familystatus) {
        Parent parent;

        ArrayList<Parent> EligibleFamilyDetail = new ArrayList<Parent>();
        mydb.openDataBase();

        DB = mydb.getDb();
        /*
         * String query =
         * "select house_number,head_family,adhaar_card from parents where status ="
         * +familystatus+" and anganwadi_center_id=" + user_id;
         */

        String query = "select house_number,head_family,adhaar_card,mobile_number from eligible_family where status =3 and anganwadi_center_id="
                + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();
                        parent.setHouseNo(cur.getString(cur
                                .getColumnIndex("house_number")));
                        parent.setHeadofHH(cur.getString(cur
                                .getColumnIndex("head_family")));
                        parent.setAadharCardHH(cur.getString(cur
                                .getColumnIndex("adhaar_card")));
                        parent.setMobileHH(cur.getString(cur
                                .getColumnIndex("mobile_number")));
                        //Log.e("head", parent.getHeadofHH());
                        EligibleFamilyDetail.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return EligibleFamilyDetail;

    }

    public ArrayList<Parent> parentSearchByName(String parentName) {
        Parent parent;

        ArrayList<Parent> parentdetail = new ArrayList<Parent>();
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select father,mother,address from eligible_family where father like '%"
                + parentName
                + "%' "
                + "or mother like '%"
                + parentName
                + "%' and user_id=" + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();
                        parent.setMother(cur.getString(cur
                                .getColumnIndex("mother")));
                        parent.setFather(cur.getString(cur
                                .getColumnIndex("father")));
                        parent.setAddress(cur.getString(cur
                                .getColumnIndex("address")));
                        parentdetail.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return parentdetail;

    }

    public ArrayList<PregnantWomen> getAllPregnantWomenList() {
        PregnantWomen pregnantWomen;

        ArrayList<PregnantWomen> womenList = new ArrayList<PregnantWomen>();
        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select house_number,edd,name_of_pregnant_women from pregnant_women where user_id=" + user_id + " and (flag is null or flag='')";
        // String query =
        // "select a.name_of_pregnant_women,a.lmp_date,a.edd,a.place_of_delivery,a.height,a.weight from pregnant_women as a on a.parent_id= b.parent_id and a.user_id="
        // + GlobalVars.UserID;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        pregnantWomen.setPreWomenName(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setEdd(cur.getString(cur
                                .getColumnIndex("edd")));
                        /*
                         * pregnantWomen.setl(cur.getString(cur
                         * .getColumnIndex("lmp_date")));
                         */

                        /*
                         * pregnantWomen.setHeight(cur.getString(cur
                         * .getColumnIndex("height")));
                         * pregnantWomen.setWeight(cur.getString(cur
                         * .getColumnIndex("weight")));
                         */

                        womenList.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return womenList;

    }


    public ArrayList<AdolBaseline> getBaselineDataEdit() {

        ArrayList<AdolBaseline> arrayList = new ArrayList<>();
        AdolBaseline adolBaseline;
        mydb.openDataBase();

        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select * from adolscent_baseline";
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {
                        adolBaseline = new AdolBaseline();

                        adolBaseline.setId(cur.getString(cur.getColumnIndex("id")));
                        adolBaseline.setAdolName(cur.getString(cur.getColumnIndex("adolscent_name")));
                        adolBaseline.setAddress(cur.getString(cur.getColumnIndex("address")));
                        adolBaseline.setParentName(cur.getString(cur.getColumnIndex("parent_name")));
                        adolBaseline.setContactNumber(cur.getString(cur.getColumnIndex("contact_number")));
                        adolBaseline.setHeight(cur.getString(cur.getColumnIndex("height")));
                        adolBaseline.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        adolBaseline.setAge(cur.getString(cur.getColumnIndex("age")));
                        adolBaseline.setMUAC(cur.getString(cur.getColumnIndex("muac")));
                        adolBaseline.setSchoolName(cur.getString(cur.getColumnIndex("school_name")));
                        adolBaseline.setClassName(cur.getString(cur.getColumnIndex("class")));
                        adolBaseline.setUniqueId(cur.getString(cur.getColumnIndex("unique_id")));
                        adolBaseline.setVillageName(cur.getString(cur.getColumnIndex("village_name")));
                        adolBaseline.setBlockName(cur.getString(cur.getColumnIndex("block_name")));
                        adolBaseline.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                        adolBaseline.setHeardOfAneamia(cur.getString(cur.getColumnIndex("heard_of_aneamia")));
                        adolBaseline.setSourceOfInfo(cur.getString(cur.getColumnIndex("source_of_info")));
                        adolBaseline.setWhatIsAneamia(cur.getString(cur.getColumnIndex("what_is_aneamia")));
                        adolBaseline.setWhichNutrientDef(cur.getString(cur.getColumnIndex("which_nutrient_def")));
                        adolBaseline.setCauseOfAneamia(cur.getString(cur.getColumnIndex("cause_of_aneamia")));
                        adolBaseline.setSignsOfAneamia(cur.getString(cur.getColumnIndex("signs_of_aneamia")));
                        adolBaseline.setEffectsOfAneamia(cur.getString(cur.getColumnIndex("effect_of_aneamia")));
                        adolBaseline.setMeasuresOfAneamia(cur.getString(cur.getColumnIndex("measures_of_aneamia")));
                        adolBaseline.setIronRichFood(cur.getString(cur.getColumnIndex("iron_rich_food")));
                        adolBaseline.setMoreIronNeeds(cur.getString(cur.getColumnIndex("more_iron_needs")));
                        adolBaseline.setYouAreAneamic(cur.getString(cur.getColumnIndex("you_are_aneamic")));
                        adolBaseline.setHowSeriousAneamia(cur.getString(cur.getColumnIndex("how_serious_aneamia")));
                        adolBaseline.setGetIronTablet(cur.getString(cur.getColumnIndex("get_iron_tablet")));
                        adolBaseline.setHowTakeIronTablet(cur.getString(cur.getColumnIndex("how_take_iron_tablet")));
                        adolBaseline.setLikeIronTablet(cur.getString(cur.getColumnIndex("like_iron_tablet")));
                        adolBaseline.setFoodTypeConsume(cur.getString(cur.getColumnIndex("food_type_consume")));
                        adolBaseline.setConsumePastWeek(cur.getString(cur.getColumnIndex("consume_past_week")));
                        adolBaseline.setPeas(cur.getString(cur.getColumnIndex("peas")));
                        adolBaseline.setSeafood(cur.getString(cur.getColumnIndex("seafood")));
                        adolBaseline.setEgg(cur.getString(cur.getColumnIndex("egg")));
                        adolBaseline.setMeat(cur.getString(cur.getColumnIndex("meat")));
                        adolBaseline.setGreenleaf(cur.getString(cur.getColumnIndex("greenleaf")));
                        adolBaseline.setAlmonds(cur.getString(cur.getColumnIndex("almonds")));
                        adolBaseline.setIncludeLemonInDiet(cur.getString(cur.getColumnIndex("include_lemon_in_diet")));
                        adolBaseline.setDewormingTablet(cur.getString(cur.getColumnIndex("deworming_tablet")));
                        adolBaseline.setFrequentlyAlbandazoleTablet(cur.getString(cur.getColumnIndex("frequently_albandazole_tablet")));
                        adolBaseline.setHadCheckedHBBefore(cur.getString(cur.getColumnIndex("had_checked_hb_before")));
                        adolBaseline.setHB(cur.getString(cur.getColumnIndex("hb")));
                        adolBaseline.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        adolBaseline.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        adolBaseline.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        adolBaseline.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        adolBaseline.setLattitude(cur.getString(cur.getColumnIndex("latitude")));


                        cur.moveToNext();
                        arrayList.add(adolBaseline);
                    }
                    DB.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return arrayList;
    }


    public ArrayList<AdolBaseline> getBaselineDataEdit(String id) {

        ArrayList<AdolBaseline> arrayList = new ArrayList<>();
        AdolBaseline adolBaseline;
        mydb.openDataBase();

        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select * from adolscent_baseline where id='" + id + "' and status = 0";
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {
                        adolBaseline = new AdolBaseline();

                        adolBaseline.setId(cur.getString(cur.getColumnIndex("id")));
                        adolBaseline.setAdolName(cur.getString(cur.getColumnIndex("adolscent_name")));
                        adolBaseline.setAddress(cur.getString(cur.getColumnIndex("address")));
                        adolBaseline.setParentName(cur.getString(cur.getColumnIndex("parent_name")));
                        adolBaseline.setContactNumber(cur.getString(cur.getColumnIndex("contact_number")));
                        adolBaseline.setHeight(cur.getString(cur.getColumnIndex("height")));
                        adolBaseline.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        adolBaseline.setAge(cur.getString(cur.getColumnIndex("age")));
                        adolBaseline.setMUAC(cur.getString(cur.getColumnIndex("muac")));
                        adolBaseline.setSchoolName(cur.getString(cur.getColumnIndex("school_name")));
                        adolBaseline.setClassName(cur.getString(cur.getColumnIndex("class")));
                        adolBaseline.setUniqueId(cur.getString(cur.getColumnIndex("unique_id")));
                        adolBaseline.setVillageName(cur.getString(cur.getColumnIndex("village_name")));
                        adolBaseline.setBlockName(cur.getString(cur.getColumnIndex("block_name")));
                        adolBaseline.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                        adolBaseline.setHeardOfAneamia(cur.getString(cur.getColumnIndex("heard_of_aneamia")));
                        adolBaseline.setSourceOfInfo(cur.getString(cur.getColumnIndex("source_of_info")));
                        adolBaseline.setWhatIsAneamia(cur.getString(cur.getColumnIndex("what_is_aneamia")));
                        adolBaseline.setWhichNutrientDef(cur.getString(cur.getColumnIndex("which_nutrient_def")));
                        adolBaseline.setCauseOfAneamia(cur.getString(cur.getColumnIndex("cause_of_aneamia")));
                        adolBaseline.setSignsOfAneamia(cur.getString(cur.getColumnIndex("signs_of_aneamia")));
                        adolBaseline.setEffectsOfAneamia(cur.getString(cur.getColumnIndex("effect_of_aneamia")));
                        adolBaseline.setMeasuresOfAneamia(cur.getString(cur.getColumnIndex("measures_of_aneamia")));
                        adolBaseline.setIronRichFood(cur.getString(cur.getColumnIndex("iron_rich_food")));
                        adolBaseline.setMoreIronNeeds(cur.getString(cur.getColumnIndex("more_iron_needs")));
                        adolBaseline.setYouAreAneamic(cur.getString(cur.getColumnIndex("you_are_aneamic")));
                        adolBaseline.setHowSeriousAneamia(cur.getString(cur.getColumnIndex("how_serious_aneamia")));
                        adolBaseline.setGetIronTablet(cur.getString(cur.getColumnIndex("get_iron_tablet")));
                        adolBaseline.setHowTakeIronTablet(cur.getString(cur.getColumnIndex("how_take_iron_tablet")));
                        adolBaseline.setLikeIronTablet(cur.getString(cur.getColumnIndex("like_iron_tablet")));
                        adolBaseline.setFoodTypeConsume(cur.getString(cur.getColumnIndex("food_type_consume")));
                        adolBaseline.setConsumePastWeek(cur.getString(cur.getColumnIndex("consume_past_week")));
                        adolBaseline.setPeas(cur.getString(cur.getColumnIndex("peas")));
                        adolBaseline.setSeafood(cur.getString(cur.getColumnIndex("seafood")));
                        adolBaseline.setEgg(cur.getString(cur.getColumnIndex("egg")));
                        adolBaseline.setMeat(cur.getString(cur.getColumnIndex("meat")));
                        adolBaseline.setGreenleaf(cur.getString(cur.getColumnIndex("greenleaf")));
                        adolBaseline.setAlmonds(cur.getString(cur.getColumnIndex("almonds")));
                        adolBaseline.setIncludeLemonInDiet(cur.getString(cur.getColumnIndex("include_lemon_in_diet")));
                        adolBaseline.setDewormingTablet(cur.getString(cur.getColumnIndex("deworming_tablet")));
                        adolBaseline.setFrequentlyAlbandazoleTablet(cur.getString(cur.getColumnIndex("frequently_albandazole_tablet")));
                        adolBaseline.setHadCheckedHBBefore(cur.getString(cur.getColumnIndex("had_checked_hb_before")));
                        adolBaseline.setHB(cur.getString(cur.getColumnIndex("hb")));
                        adolBaseline.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        adolBaseline.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        adolBaseline.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        adolBaseline.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        adolBaseline.setLattitude(cur.getString(cur.getColumnIndex("latitude")));


                        cur.moveToNext();
                        arrayList.add(adolBaseline);
                    }
                    DB.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return arrayList;
    }


    public void setEditChild(String[] edited) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("hb", edited[1]);
                values.put("name_of_child", edited[2]);
                values.put("house_number", edited[3]);
                values.put("status", 5);
                values.put("lattitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                id = DB.update("child", values, "child_id=" + edited[0].toString(), null);

                Log.d("Child Updation : data is updated successfully", id + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getChildInfo(int childID) {
        String hhid = "", childname = "", parent = "", dob = "", gender = "", birthweight = "", birthheight = "", birthmuac = "", childhb = "", disability = "", birthorder = "";

        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select a.name_of_child,a.date_of_birth,a.birth_height,a.birth_weight,a.birth_muac,a.hb,a.birth_status," +
                "b.name_of_pregnant_women,b.house_number,c.gender_value,d.disability_type from child as a, pregnant_women as b," +
                " gender as c, disability as d where a.pregnent_women_id=b.pregnant_women_id and a.gender=c.id and a.is_disability=d.disability_id" +
                " and a.child_id = '" + childID + "'";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {

                        birthmuac = cur.getString(cur.getColumnIndex("birth_muac"));
                        hhid = cur.getString(cur.getColumnIndex("house_number"));
                        childname = cur.getString(cur.getColumnIndex("name_of_child"));
                        parent = cur.getString(cur.getColumnIndex("name_of_pregnant_women"));
                        dob = cur.getString(cur.getColumnIndex("date_of_birth"));
                        gender = cur.getString(cur.getColumnIndex("gender_value"));
                        birthweight = cur.getString(cur.getColumnIndex("birth_weight"));
                        birthheight = cur.getString(cur.getColumnIndex("birth_height"));
                        childhb = cur.getString(cur.getColumnIndex("hb"));
                        disability = cur.getString(cur.getColumnIndex("disability_type"));
                        birthorder = cur.getString(cur.getColumnIndex("birth_status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String[] info = {hhid, childname, parent, dob, gender, birthweight, birthheight, birthmuac, childhb, disability, birthorder};
        return info;
    }

    public String[] getEligibleFamilyInfo(String Hno) {

        String hno = "", hf = "", fid = "", ttype = "", ttype1 = "", wsou = "", wsou1 = "", ac = "",
                mn = "", rid = "", rid1 = "", cc = "", cast = "", cast1 = "", lh = "", lh1 = "", cc1 = "",
                gen = "", gen1 = "", latt = "", lon = "", ses_mgr = "";

        mydb.openDataBase();

        DB = mydb.getDb();


        String query = "select * from eligible_family where house_number = '" + Hno + "' and anganwadi_center_id = " + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {

                        hno = cur.getString(cur.getColumnIndex("house_number"));
                        fid = cur.getString(cur.getColumnIndex("familyid"));
                        ttype = cur.getString(cur.getColumnIndex("toilet_type"));
                        wsou = cur.getString(cur.getColumnIndex("water_source"));
                        lh = cur.getString(cur.getColumnIndex("land_hold"));
                        hf = cur.getString(cur.getColumnIndex("head_family"));
                        rid = cur.getString(cur.getColumnIndex("religion_id"));
                        cc = cur.getString(cur.getColumnIndex("cast_category"));
                        cast = cur.getString(cur.getColumnIndex("cast_id"));
                        gen = cur.getString(cur.getColumnIndex("gender"));
                        ac = cur.getString(cur.getColumnIndex("adhaar_card"));
                        mn = cur.getString(cur.getColumnIndex("mobile_number"));
                        latt = cur.getString(cur.getColumnIndex("latitude"));
                        lon = cur.getString(cur.getColumnIndex("longitude"));
                        ses_mgr = cur.getString(cur.getColumnIndex("seasonal_migration"));

                        String query1 = "select value from religion where religion_id = " + rid + "";

                        Cursor cur1 = DB.rawQuery(query1, null);
                        if (cur1 != null && cur1.getCount() > 0) {
                            cur1.move(0);
                            while (cur1.moveToNext()) {
                                rid1 = cur1.getString(cur1.getColumnIndex("value"));
                            }
                        }

                        String query2 = "select category from caste_category where id = " + cc + "";

                        Cursor cur2 = DB.rawQuery(query2, null);
                        if (cur2 != null && cur2.getCount() > 0) {
                            cur2.move(0);
                            while (cur2.moveToNext()) {
                                cc1 = cur2.getString(cur2.getColumnIndex("category"));
                            }
                        }
                        String query3 = "select gender_value from gender where id = " + gen + "";

                        Cursor cur3 = DB.rawQuery(query3, null);
                        if (cur3 != null && cur3.getCount() > 0) {
                            cur3.move(0);
                            while (cur3.moveToNext()) {
                                gen1 = cur3.getString(cur3.getColumnIndex("gender_value"));
                            }
                        }
                        String query4 = "select value from cast where cast_id = " + cast + "";

                        Cursor cur4 = DB.rawQuery(query4, null);
                        if (cur4 != null && cur4.getCount() > 0) {
                            cur4.move(0);
                            while (cur4.moveToNext()) {
                                cast1 = cur4.getString(cur4.getColumnIndex("value"));
                            }
                        }
                        String query5 = "select toilet_avaibality_source from toilet_avaibality where toilet_avaibality_id = " + ttype + "";

                        Cursor cur5 = DB.rawQuery(query5, null);
                        if (cur5 != null && cur5.getCount() > 0) {
                            cur5.move(0);
                            while (cur5.moveToNext()) {
                                ttype1 = cur5.getString(cur5.getColumnIndex("toilet_avaibality_source"));
                            }
                        }

                        String query6 = "select drinking_water_source from drinking_water_source where drinking_water_source_id = " + wsou + "";

                        Cursor cur6 = DB.rawQuery(query6, null);
                        if (cur6 != null && cur6.getCount() > 0) {
                            cur6.move(0);
                            while (cur6.moveToNext()) {
                                wsou1 = cur6.getString(cur6.getColumnIndex("drinking_water_source"));
                            }
                        }
                        String query7 = "select value from family_land_hold where id = " + lh + "";

                        Cursor cur7 = DB.rawQuery(query7, null);
                        if (cur7 != null && cur7.getCount() > 0) {
                            cur7.move(0);
                            while (cur7.moveToNext()) {
                                lh1 = cur7.getString(cur7.getColumnIndex("value"));
                            }
                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        String[] info = {hno, hf, ac, mn, gen1, rid1, cc1, cast1, ttype1, wsou1, lh1, fid, latt, lon, ses_mgr};
        return info;
    }


    // BY SHANNI MOTHER CODE

    public ArrayList<Mother> getAllMotherList() {
        Mother mother;

        ArrayList<Mother> motherList = new ArrayList<Mother>();
        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select house_number,child_number,name_of_pregnant_women from pregnant_women where user_id=" + user_id + " and flag= 'M'";
        // String query =
        // "select a.name_of_pregnant_women,a.lmp_date,a.edd,a.place_of_delivery,a.height,a.weight from pregnant_women as a on a.parent_id= b.parent_id and a.user_id="
        // + GlobalVars.UserID;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mother = new Mother();
                        mother.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        mother.setMotherName(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        mother.setNumber_of_child(cur.getString(cur
                                .getColumnIndex("child_number")));
                        /*
                         * pregnantWomen.setl(cur.getString(cur
                         * .getColumnIndex("lmp_date")));
                         */

                        /*
                         * pregnantWomen.setHeight(cur.getString(cur
                         * .getColumnIndex("height")));
                         * pregnantWomen.setWeight(cur.getString(cur
                         * .getColumnIndex("weight")));
                         */

                        motherList.add(mother);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return motherList;

    }

    public ArrayList<EditEF> getEFList() {
        EditEF eef;

        ArrayList<EditEF> eefList = new ArrayList<EditEF>();
        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select house_number, head_family, mobile_number, adhaar_card, latitude, longitude from eligible_family where anganwadi_center_id = " + user_id;
        // String query =
        // "select a.name_of_pregnant_women,a.lmp_date,a.edd,a.place_of_delivery,a.height,a.weight from pregnant_women as a on a.parent_id= b.parent_id and a.user_id="
        // + GlobalVars.UserID;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        eef = new EditEF();
                        eef.setHhId(cur.getString(cur.getColumnIndex("house_number")));
                        eef.setHHName(cur.getString(cur.getColumnIndex("head_family")));
                        eef.setAadhaar(cur.getString(cur.getColumnIndex("adhaar_card")));
                        eef.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        eef.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        eef.setLongitude(cur.getString(cur.getColumnIndex("mobile_number")));
                        eefList.add(eef);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return eefList;

    }


    public ArrayList<PregnantWomen> pregnantWomenSearchByName(String name) {
        PregnantWomen pregnantWomen;

        ArrayList<PregnantWomen> womenList = new ArrayList<PregnantWomen>();
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select name_of_pregnant_women,lmp_date,edd,place_of_delivery,height,weight from pregnant_women where name_of_pregnant_women like '%"
                + name + "%'and user_id=" + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setPreWomenName(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setLmp_date(cur.getString(cur
                                .getColumnIndex("lmp_date")));
                        pregnantWomen.setEdd(cur.getString(cur
                                .getColumnIndex("edd")));
                        pregnantWomen.setPlace_of_delivery(cur.getString(cur
                                .getColumnIndex("place_of_delivery")));
                        pregnantWomen.setHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        pregnantWomen.setWeight(cur.getString(cur
                                .getColumnIndex("weight")));
                        pregnantWomen.setPregnant_women_id(cur.getString(cur
                                .getColumnIndex("pregnant_women_id ")));
                        womenList.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return womenList;

    }

    public ArrayList<AdolescentMonitoring> getAdolescentMonitorDataBy(
            int intAdolescentId) {
        ArrayList<AdolescentMonitoring> adolescentMonitoring = new ArrayList<AdolescentMonitoring>();
        mydb.openDataBase();
        DB = mydb.getDb();
        AdolescentMonitoring adolescentMon;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query = "select weight, height, hb, date_of_recording from adolescent_monitoring where adolescent_id="
                    + intAdolescentId;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        adolescentMon = new AdolescentMonitoring();

                        adolescentMon.setAdolescentHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        adolescentMon.setAdolescentWeight(cur.getString(cur
                                .getColumnIndex("weight")));
                        adolescentMon.setAdolescentHb(cur.getString(cur
                                .getColumnIndex("hb")));
                        adolescentMon.setDateOfRecord(cur.getString(cur
                                .getColumnIndex("date_of_recording")));
                        adolescentMonitoring.add(adolescentMon);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return adolescentMonitoring;
    }

    public ArrayList<Parent> getParents() {
        ArrayList<Parent> parents = new ArrayList<Parent>();
        mydb.openDataBase();
        Parent parent;
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            String query = "select house_number,head_family,gender,adhaar_card,mobile_number,date_or_recording,toilet_type,water_source,religion_id, "
                    + "cast_category, latitude, "
                    + "longitude, cast_id, app_version from eligible_family where status='0' and anganwadi_center_id="
                    + user_id + " and user_master_id=" + user_master_id;
        /*Cursor cur = DB
                .rawQuery(
                        "select house_number,head_family,gender,adhaar_card,mobile_number,date_or_recording,toilet_type,water_source,religion_id, "
                                + "cast_category, latitude, "
                                + "longitude, cast_id, status, app_version, img, family_land_hold, seasonal_migration from eligible_family where (status='0' or status='5') and anganwadi_center_id="
                                + user_id + " and user_master_id=" + user_master_id, null);*/
            Cursor cur = DB
                    .rawQuery(
                            "select house_number,latitude,longitude,head_family,toilet_type,water_source," +
                                    "religion_id,cast_category,adhaar_card,mobile_number,anganwadi_center_id," +
                                    "gender,date_or_recording,app_version,user_master_id,cast_id,seasonal_migration," +
                                    "family_land_hold,no_of_member_in_family,img,state_id,district_id,block_id,village_id, bpl_card, water_quality," +
                                    "Created_on_mobile,mobile_unique_id from eligible_family where (status='0' or status='5') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();

                        parent.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        parent.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        parent.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        parent.setHead_family(cur.getString(cur.getColumnIndex("head_family")));
                        parent.setToilet_type(cur.getString(cur.getColumnIndex("toilet_type")));
                        parent.setWater_source(cur.getString(cur.getColumnIndex("water_source")));
                        parent.setReligion_id(cur.getString(cur.getColumnIndex("religion_id")));
                        parent.setCast_category(cur.getString(cur.getColumnIndex("cast_category")));
                        parent.setAdhaar_card(cur.getString(cur.getColumnIndex("adhaar_card")));
                        parent.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        parent.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        parent.setGender(cur.getString(cur.getColumnIndex("gender")));
                        parent.setDate_of_recording(cur.getString(cur.getColumnIndex("date_or_recording")));
                        parent.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        parent.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        parent.setImage(cur.getString(cur.getColumnIndex("img")));
                        parent.setCast_id(Integer.parseInt(cur.getString(cur.getColumnIndex("cast_id"))));
                        parent.setSeasonal_migration(cur.getString(cur.getColumnIndex("seasonal_migration")));
                        parent.setFamily_land_hold(cur.getString(cur.getColumnIndex("family_land_hold")));
                        parent.setNo_of_member_in_family(cur.getString(cur.getColumnIndex("no_of_member_in_family")));
                        parent.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        parent.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        parent.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        parent.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        parent.setBpl_card(cur.getString(cur.getColumnIndex("bpl_card")));
                        parent.setWater_quality(cur.getString(cur.getColumnIndex("water_quality")));
                        parent.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        parent.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));

                    /*parent.setHouseNo(cur.getString(cur
                            .getColumnIndex("house_number")));

                    parent.setHeadofHH(cur.getString(cur
                            .getColumnIndex("head_family")));

                    parent.setGender(cur.getString(cur
                            .getColumnIndex("gender")));

                    parent.setAadharCardHH(cur.getString(cur
                            .getColumnIndex("adhaar_card")));

                    parent.setMobileHH(cur.getString(cur
                            .getColumnIndex("mobile_number")));

                    parent.setDateOfRecord(cur.getString(cur
                            .getColumnIndex("date_or_recording")));

                    parent.setHas_toilet(cur.getInt(cur
                            .getColumnIndex("toilet_type")));
                    parent.setHave_water(cur.getInt(cur
                            .getColumnIndex("water_source")));

                    parent.setReligion(cur.getInt(cur
                            .getColumnIndex("religion_id")));
                    parent.setCaste(cur.getString(cur
                            .getColumnIndex("cast_category")));

                    parent.setLatitude((cur.getString(cur
                            .getColumnIndex("latitude"))));
                    parent.setLongitude(cur.getString(cur
                            .getColumnIndex("longitude")));

                    parent.setCast_id(cur.getInt(cur
                            .getColumnIndex("cast_id")));

                    parent.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));
                    parent.setImage(cur.getString(cur
                            .getColumnIndex("img")));
                    parent.setLan_Value(cur.getString(cur
                            .getColumnIndex("family_land_hold")));
                    parent.setSes_migration(cur.getString(cur
                            .getColumnIndex("seasonal_migration")));*/

                        parents.add(parent);

                        Log.e("tag", "ParentData"+ parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return parents;
    }

    public ArrayList<Parent> getParentsForGPSUpdate() {
        ArrayList<Parent> parents = new ArrayList<Parent>();
        mydb.openDataBase();
        Parent parent;
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB
                    .rawQuery(
                            "select house_number,head_family,gender,adhaar_card,mobile_number,date_or_recording,toilet_type,water_source,religion_id, "
                                    + "cast_category, latitude, "
                                    + "longitude from eligible_family where status='2' and anganwadi_center_id="
                                    + user_id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();
                        parent.setHouseNo(cur.getString(cur
                                .getColumnIndex("house_number")));

                        parent.setHeadofHH(cur.getString(cur
                                .getColumnIndex("head_family")));

                        parent.setGender(cur.getString(cur
                                .getColumnIndex("gender")));

                        parent.setAadharCardHH(cur.getString(cur
                                .getColumnIndex("adhaar_card")));

                        parent.setMobileHH(cur.getString(cur
                                .getColumnIndex("mobile_number")));

                        parent.setDateOfRecord(cur.getString(cur
                                .getColumnIndex("date_or_recording")));

                        parent.setHas_toilet(cur.getInt(cur
                                .getColumnIndex("toilet_type")));
                        parent.setHave_water(cur.getInt(cur
                                .getColumnIndex("water_source")));

                        parent.setReligion(cur.getInt(cur
                                .getColumnIndex("religion_id")));
                        parent.setCaste(cur.getString(cur
                                .getColumnIndex("cast_category")));

                        parent.setLatitude((cur.getString(cur
                                .getColumnIndex("latitude"))));
                        parent.setLongitude(cur.getString(cur
                                .getColumnIndex("longitude")));
                        parents.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return parents;
    }

   /* public ArrayList<Child> getIdOfAdolChildReg(){
        mydb.openDataBase();
        DB = mydb.getDb();
        ArrayList<Child> children = new ArrayList<Child>();
        Child child;
        try{
            Cursor cur = DB.rawQuery(
                    "select pregnent_women_id from child_registration where check_adol = 1", null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        child = new Child();
                        child.setPregnent_women_id(cur.getString(cur
                                .getColumnIndex("pregnent_women_id")));
                        children.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }catch(Exception e){
            e.getMessage();
        }
        return children;
    }*/

    public ArrayList<Child> getChildren(String parentid) {
        ArrayList<Child> children = new ArrayList<Child>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Child child;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            /*Cursor cur = DB
                    .rawQuery(
                            "select a.check_adol, a.child_id, a.name_of_child, a.date_of_birth, a.birth_weight,"
                                    + " a.parent_id, a.lattitude, a.longitude, a.gender, a.status, a.birth_status, a.photopath, a.Date_of+registration, a.house_number,"
                                    + "a.birth_height, a.birth_muac, a.hb, a.have_edema, a.is_disability, a.pregnent_women_id from child as a" +
                                    " where (a.status='0' or a.status='5') and a.anganwadi_center_id=" + user_id + " and a.user_master_id=" + user_master_id, null);*/
            Cursor cur = DB
                    .rawQuery(
                            "select a.child_id,a.house_number,a.name_of_child,a.date_of_birth,a.gender,a.birth_status,a.birth_muac," +
                                    "a.hb,a.have_edema,a.lattitude,a.is_child_Premature, a.longitude,a.parent_id,a.anganwadi_center_id,a.bpl_card, a.dob_month," +
                                    "a.user_master_id,a.Date_of_registration,a.month_complementary_feeding, a.selected_women_type, a.birth_weight,a.created_on_mobile,a.mobile_unique_id," +
                                    "a.birth_height,a.is_child_breastfed, a.select_breastfeed_initiated, a.is_disability,a.photopath,a.version_code,a.check_adol,a.pregnent_women_id,a.state_id," +
                                    "a.district_id,a.block_id,a.is_complementary_food, a.village_id,a.registred_icds,a.health_facility from child as a" +
                                    " where (a.status='0' or a.status='5') and a.anganwadi_center_id=" + user_id + " and a.user_master_id=" + user_master_id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        child = new Child();
                        child.setChild_id(cur.getInt(cur.getColumnIndex("child_id")));
                        child.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        child.setName_of_child(cur.getString(cur.getColumnIndex("name_of_child")));
                        child.setDate_of_birth(cur.getString(cur.getColumnIndex("date_of_birth")));
                        child.setGender(cur.getString(cur.getColumnIndex("gender")));
                        child.setBirth_status(cur.getString(cur.getColumnIndex("birth_status")));
                        child.setBirth_muac(cur.getString(cur.getColumnIndex("birth_muac")));
                        child.setHb(cur.getString(cur.getColumnIndex("hb")));
                        child.setHave_edema(cur.getString(cur.getColumnIndex("have_edema")));
                        child.setLattitude(cur.getString(cur.getColumnIndex("lattitude")));
                        child.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        child.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        child.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        child.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        child.setImg(cur.getString(cur.getColumnIndex("photopath")));
                        child.setDate_of_registration(cur.getString(cur.getColumnIndex("Date_of_registration")));
                        child.setBirth_weight(cur.getString(cur.getColumnIndex("birth_weight")));
                        child.setBirth_height(cur.getString(cur.getColumnIndex("birth_height")));
                        child.setIs_disability(cur.getString(cur.getColumnIndex("is_disability")));
                        child.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        child.setCheck_adol(Integer.parseInt(cur.getString(cur.getColumnIndex("check_adol"))));
                        child.setPregnent_women_id(cur.getString(cur.getColumnIndex("pregnent_women_id")));
                        child.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        child.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        child.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        child.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        child.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        child.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        child.setRegistred_icds(cur.getString(cur.getColumnIndex("registred_icds")));
                        child.setHealth_facility(cur.getString(cur.getColumnIndex("health_facility")));
                        child.setBpl_card(cur.getString(cur.getColumnIndex("bpl_card")));
                        child.setSelected_women_type(cur.getString(cur.getColumnIndex("selected_women_type")));
                        child.setDob_month(cur.getString(cur.getColumnIndex("dob_month")));
                        child.setIs_child_breastfed(cur.getString(cur.getColumnIndex("is_child_breastfed")));
                        child.setSelect_breastfeed_initiated(cur.getString(cur.getColumnIndex("select_breastfeed_initiated")));
                        child.setIs_complementary_food(cur.getString(cur.getColumnIndex("is_complementary_food")));
                        child.setMonth_complementary_feeding(cur.getString(cur.getColumnIndex("month_complementary_feeding")));
                        child.setIs_child_Premature(cur.getString(cur.getColumnIndex("is_child_Premature")));
                        /*child.setParent_id(cur.getInt(cur
                                .getColumnIndex("parent_id")));

                        child.setDisablity(cur.getString(cur
                                .getColumnIndex("is_disability")));
                        child.setMuac(cur.getString(cur
                                .getColumnIndex("birth_muac")));
                        child.setHb(cur.getString(cur
                                .getColumnIndex("hb")));
                        child.setEdema(cur.getString(cur
                                .getColumnIndex("have_edema")));
                        child.setChild_id(cur.getInt(cur
                                .getColumnIndex("child_id")));
                        child.setChild_name(cur.getString(cur
                                .getColumnIndex("name_of_child")));
                        child.setDate_of_registration(cur.getString(cur
                                .getColumnIndex("Date_of_registration")));
                        child.setDate_of_birth(cur.getString(cur
                                .getColumnIndex("date_of_birth")));
                        child.setChild_weight(cur.getString(cur
                                .getColumnIndex("birth_weight")));

                        child.setHHID(cur.getString(cur
                                .getColumnIndex("house_number")));

                        child.setLatitude(cur.getString(cur
                                .getColumnIndex("lattitude")));
                        child.setLongitude(cur.getString(cur
                                .getColumnIndex("longitude")));
                        child.setMultimedia(cur.getString(cur
                                .getColumnIndex("photopath")));
                        child.setBirth_order(cur.getInt(cur
                                .getColumnIndex("birth_status")));
                        child.setGender(cur.getString(cur
                                .getColumnIndex("gender")));
                        child.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        child.setHeight(cur.getString(cur
                                .getColumnIndex("birth_height")));

                        child.setPregnent_women_id(cur.getString(cur
                                .getColumnIndex("pregnent_women_id")));

                        child.setCheck_adol(cur.getInt(cur.getColumnIndex("check_adol")));*/
                        children.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return children;
    }

    public ArrayList<PregnantWomen> getPregnantWomen() {
        ArrayList<PregnantWomen> pregnantWomens = new ArrayList<PregnantWomen>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomen pregnantWomen;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

        /*query = "select pregnant_women_id,name_of_pregnant_women, lmp_date, edd, "
                + "house_number, height, weight,hb, systolic_bp, diastolic_bp, latitude, longitude, date_of_registration, parent_id, husband_father_name, age, img, status, server_id from pregnant_women where flag IS NOT 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                + user_id + " and user_master_id=" + user_master_id;*/
            /*query = "select pregnant_women_id,house_number,name_of_pregnant_women,mobile_number," +
                    "date_of_registration,place_of_delivery,lmp_date,anganwadi_center_id,user_master_id," +
                    "edd,parent_id,weight,hb,systolic_bp,diastolic_bp,latitude,husband_father_name," +
                    "height,longitude,age,version_code,created_on_mobile,img,mobile_unique_id,state_id,district_id," +
                    " registered_icds, supplements_icds, out_comes, date_of_delivery, date_of_abortion, birth_weight_child," +
                    "block_id,village_id, status, server_id,muac,education,months_of_pregnancy,is_pregnant," +
                    "mother_unique_id from pregnant_women where flag IS NOT 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;*/
            //changed by ram sir (remove flag while sync data).
            query = "select pregnant_women_id,house_number,name_of_pregnant_women,mobile_number," +
                    "date_of_registration,place_of_delivery,lmp_date,anganwadi_center_id,user_master_id," +
                    "edd,parent_id,weight,hb,systolic_bp,diastolic_bp,latitude,husband_father_name," +
                    "height,longitude,age,version_code,created_on_mobile,img,mobile_unique_id,state_id,district_id," +
                    " registered_icds, supplements_icds, out_comes, date_of_delivery, date_of_abortion, birth_weight_child," +
                    "block_id,village_id, status, server_id,muac,education,months_of_pregnancy,is_pregnant," +
                    "mother_unique_id from pregnant_women where (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;

            Log.e("..........................", query);
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setPregnant_women_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        pregnantWomen.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        pregnantWomen.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        pregnantWomen.setLmp_date(cur.getString(cur.getColumnIndex("lmp_date")));
                        pregnantWomen.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pregnantWomen.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pregnantWomen.setEdd(cur.getString(cur.getColumnIndex("edd")));
                        pregnantWomen.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        pregnantWomen.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        pregnantWomen.setHb(cur.getString(cur.getColumnIndex("hb")));
                        pregnantWomen.setSystolic_bp(cur.getString(cur.getColumnIndex("systolic_bp")));
                        pregnantWomen.setDiastolic_bp(cur.getString(cur.getColumnIndex("diastolic_bp")));
                        pregnantWomen.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        pregnantWomen.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        pregnantWomen.setHeight(cur.getString(cur.getColumnIndex("height")));
                        pregnantWomen.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        pregnantWomen.setAge(cur.getString(cur.getColumnIndex("age")));
                        pregnantWomen.setImage(cur.getString(cur.getColumnIndex("img")));
                        pregnantWomen.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        pregnantWomen.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        pregnantWomen.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        pregnantWomen.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        pregnantWomen.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        pregnantWomen.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        pregnantWomen.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        pregnantWomen.setRegistered_icds(cur.getString(cur.getColumnIndex("registered_icds")));
                        pregnantWomen.setSupplements_icds(cur.getString(cur.getColumnIndex("supplements_icds")));
                        pregnantWomen.setMuac(cur.getString(cur.getColumnIndex("muac")));
                        pregnantWomen.setEducation(cur.getString(cur.getColumnIndex("education")));
                        pregnantWomen.setMonths_of_pregnancy(cur.getString(cur.getColumnIndex("months_of_pregnancy")));
                        pregnantWomen.setOut_comes(cur.getString(cur.getColumnIndex("out_comes")));
                        pregnantWomen.setDate_of_delivery(cur.getString(cur.getColumnIndex("date_of_delivery")));
                        pregnantWomen.setDate_of_abortion(cur.getString(cur.getColumnIndex("date_of_abortion")));
                        pregnantWomen.setPlace_of_delivery(cur.getString(cur.getColumnIndex("place_of_delivery")));
                        pregnantWomen.setBirth_weight_child(cur.getString(cur.getColumnIndex("birth_weight_child")));
                        pregnantWomen.setIs_pregnant(cur.getString(cur.getColumnIndex("is_pregnant")));
                        pregnantWomen.setMother_unique_id(cur.getString(cur.getColumnIndex("mother_unique_id")));
                        /*pregnantWomen.setHhId(cur.getString(cur
                            .getColumnIndex("house_number")));
                    pregnantWomen.setPreWomenName(cur.getString(cur
                            .getColumnIndex("name_of_pregnant_women")));
                    pregnantWomen.setLmp_date(cur.getString(cur
                            .getColumnIndex("lmp_date")));
                    pregnantWomen.setEdd(cur.getString(cur
                            .getColumnIndex("edd")));
                    pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                    pregnantWomen.setHeight(cur.getString(cur
                            .getColumnIndex("height")));
                    pregnantWomen.setWeight(cur.getString(cur
                            .getColumnIndex("weight")));
                    pregnantWomen.setHusbandName(cur.getString(cur
                            .getColumnIndex("husband_father_name")));
                    pregnantWomen.setAgeofPW(cur.getString(cur
                            .getColumnIndex("age")));
                    *//*
                         * pregnantWomen.setAnganwadi_center_id(cur.getString(cur
                         * .getColumnIndex("anganwadi_center_id")));
                         *//*
                    pregnantWomen.setHb(cur.getString(cur
                            .getColumnIndex("hb")));
                    pregnantWomen.setSysbp(cur.getString(cur
                            .getColumnIndex("systolic_bp")));
                    pregnantWomen.setDiasbp(cur.getString(cur
                            .getColumnIndex("diastolic_bp")));

                    pregnantWomen.setLatitude(GlobalVars.lattitude);
                    pregnantWomen.setLongitude(GlobalVars.longitude);

                    pregnantWomen.setPregnant_women_id(cur.getString(cur
                            .getColumnIndex("pregnant_women_id")));
                    pregnantWomen.setImage(cur.getString(cur
                            .getColumnIndex("img")));

                    pregnantWomen.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));

                    pregnantWomen.setServer_id(cur.getInt(cur
                            .getColumnIndex("server_id")));*/

                        pregnantWomens.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomens;
    }


    //get data form table if the inserted pregnant women converted to mother
    public ArrayList<PregnantWomen> getPregnantWomenToMother() {
        ArrayList<PregnantWomen> pregnantWomens = new ArrayList<PregnantWomen>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomen pregnantWomen;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

        /*query = "select pregnant_women_id,name_of_pregnant_women, lmp_date, edd, "
                + "house_number, height, weight,hb, systolic_bp, diastolic_bp, latitude, longitude, date_of_registration, parent_id, husband_father_name, age, img, status, server_id from pregnant_women where flag IS NOT 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                + user_id + " and user_master_id=" + user_master_id;*/
            query = "select pregnant_women_id,house_number,name_of_pregnant_women,mobile_number," +
                    "date_of_registration,place_of_delivery,lmp_date,anganwadi_center_id,user_master_id," +
                    "edd,parent_id,weight,hb,systolic_bp,diastolic_bp,latitude,husband_father_name," +
                    "height,longitude,age,version_code,created_on_mobile,img,mobile_unique_id,state_id,district_id, registered_icds, supplements_icds," +
                    "block_id,village_id, status, server_id from pregnant_women where flag = 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;

            Log.e("..............", query);
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setPregnant_women_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        pregnantWomen.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        pregnantWomen.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        pregnantWomen.setPlace_of_delivery(cur.getString(cur.getColumnIndex("place_of_delivery")));
                        pregnantWomen.setLmp_date(cur.getString(cur.getColumnIndex("lmp_date")));
                        pregnantWomen.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pregnantWomen.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pregnantWomen.setEdd(cur.getString(cur.getColumnIndex("edd")));
                        pregnantWomen.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        pregnantWomen.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        pregnantWomen.setHb(cur.getString(cur.getColumnIndex("hb")));
                        pregnantWomen.setImage(cur.getString(cur.getColumnIndex("img")));
                        pregnantWomen.setSystolic_bp(cur.getString(cur.getColumnIndex("systolic_bp")));
                        pregnantWomen.setDiastolic_bp(cur.getString(cur.getColumnIndex("diastolic_bp")));
                        pregnantWomen.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        pregnantWomen.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        pregnantWomen.setHeight(cur.getString(cur.getColumnIndex("height")));
                        pregnantWomen.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        pregnantWomen.setAge(cur.getString(cur.getColumnIndex("age")));
                        pregnantWomen.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        pregnantWomen.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        pregnantWomen.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        pregnantWomen.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        pregnantWomen.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        pregnantWomen.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        pregnantWomen.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        pregnantWomen.setRegistered_icds(cur.getString(cur.getColumnIndex("registered_icds")));
                        pregnantWomen.setSupplements_icds(cur.getString(cur.getColumnIndex("supplements_icds")));
                    /*pregnantWomen.setHhId(cur.getString(cur
                            .getColumnIndex("house_number")));
                    pregnantWomen.setPreWomenName(cur.getString(cur
                            .getColumnIndex("name_of_pregnant_women")));
                    pregnantWomen.setLmp_date(cur.getString(cur
                            .getColumnIndex("lmp_date")));
                    pregnantWomen.setEdd(cur.getString(cur
                            .getColumnIndex("edd")));
                    pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                    pregnantWomen.setHeight(cur.getString(cur
                            .getColumnIndex("height")));
                    pregnantWomen.setWeight(cur.getString(cur
                            .getColumnIndex("weight")));
                    pregnantWomen.setHusbandName(cur.getString(cur
                            .getColumnIndex("husband_father_name")));
                    pregnantWomen.setAgeofPW(cur.getString(cur
                            .getColumnIndex("age")));
                    *//*
                         * pregnantWomen.setAnganwadi_center_id(cur.getString(cur
                         * .getColumnIndex("anganwadi_center_id")));
                         *//*
                    pregnantWomen.setHb(cur.getString(cur
                            .getColumnIndex("hb")));
                    pregnantWomen.setSysbp(cur.getString(cur
                            .getColumnIndex("systolic_bp")));
                    pregnantWomen.setDiasbp(cur.getString(cur
                            .getColumnIndex("diastolic_bp")));

                    pregnantWomen.setLatitude(GlobalVars.lattitude);
                    pregnantWomen.setLongitude(GlobalVars.longitude);

                    pregnantWomen.setPregnant_women_id(cur.getString(cur
                            .getColumnIndex("pregnant_women_id")));
                    pregnantWomen.setImage(cur.getString(cur
                            .getColumnIndex("img")));

                    pregnantWomen.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));

                    pregnantWomen.setServer_id(cur.getInt(cur
                            .getColumnIndex("server_id")));*/

                        pregnantWomens.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomens;
    }

    // get mother list
    public ArrayList<Mother> getMother() {
        ArrayList<Mother> womens = new ArrayList<Mother>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Mother mother;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            /*query = "select pregnant_women_id,name_of_pregnant_women, date_of_registration, house_number,latitude, longitude, husband_father_name, unmarried, widow, married, age, child_number, flag, img from pregnant_women where flag='M' and status='0' and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;*/
            query = "select mother_id,parent_id,house_number,name_of_mother,anganwadi_center_id," +
                    "user_master_id,latitude,longitude,date_of_registration,husband_father_name," +
                    "age,child_number,unmarried,widow,married,version_code,state_id,img,district_id," +
                    "block_id,village_id,created_on_mobile,mobile_unique_id,flag,is_pregnant_converted,registred_icds,marital_status,education from mother where flag='M' and status='0' and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mother = new Mother();
                        mother.setMother_id(cur.getString(cur.getColumnIndex("mother_id")));
                        mother.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        mother.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        mother.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_mother")));
                        mother.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        mother.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        mother.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        mother.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        mother.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        mother.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        mother.setAge(cur.getString(cur.getColumnIndex("age")));
                        mother.setImage(cur.getString(cur.getColumnIndex("img")));
                        mother.setChild_number(cur.getString(cur.getColumnIndex("child_number")));
                        mother.setUnmarried(cur.getString(cur.getColumnIndex("unmarried")));
                        mother.setWidow(cur.getString(cur.getColumnIndex("widow")));
                        mother.setMarried(cur.getString(cur.getColumnIndex("married")));
                        mother.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        mother.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        mother.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        mother.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        mother.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        mother.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        mother.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        mother.setIs_pregnant_converted(cur.getString(cur.getColumnIndex("is_pregnant_converted")));
                        mother.setRegistred_icds(cur.getString(cur.getColumnIndex("registred_icds")));
                        mother.setMarital_status(cur.getString(cur.getColumnIndex("marital_status")));
                        mother.setEducation(cur.getString(cur.getColumnIndex("education")));
                        /*mother.setHhId(cur.getString(cur.getColumnIndex("house_number")));
                        mother.setMotherName(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        mother.setMotherhusbandName(cur.getString(cur.getColumnIndex("husband_father_name")));
                        mother.setLatitude(GlobalVars.lattitude);
                        mother.setLongitude(GlobalVars.longitude);
                        mother.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        mother.setMother_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        mother.setUnmarried(cur.getString(cur.getColumnIndex("unmarried")));
                        mother.setWidow(cur.getString(cur.getColumnIndex("widow")));
                        mother.setMarried(cur.getString(cur.getColumnIndex("married")));
                        mother.setAge(cur.getString(cur.getColumnIndex("age")));
                        mother.setNumber_of_child(cur.getString(cur.getColumnIndex("child_number")));
                        mother.setFlag(cur.getString(cur.getColumnIndex("flag")));
                        mother.setImage(cur.getString(cur.getColumnIndex("img")));*/
                        womens.add(mother);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return womens;
    }
//
//    public ArrayList<ChildNutrition> getChildMonitor() {
//        ArrayList<ChildNutrition> childMonitor = new ArrayList<ChildNutrition>();
//        mydb.openDataBase();
//        DB = mydb.getDb();
//        ChildNutrition childNutrition;
//
//        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
//
//            String s = "select nutrition_id,child_id, migration_type, death_date, weight, status, height, muac, hb, latitude, longitude, multimedia, date_of_recording, anganwadi_center_id from "
//                    + "Child_Nutrition_Monitor where (status='0' or status='5') and anganwadi_center_id="
//                    + user_id;
//
//
//            String q1= "select nutrition_id,child_id, migration_type, death_date, weight, status, height, muac, hb, d_reason, latitude, longitude, multimedia, date_of_recording,"
//                    + " anganwadi_center_id, sick_y_n, sick_name, sick_y_n15, sick_name15 from "
//                    + "Child_Nutrition_Monitor where (status='0' or status='5') and server_id = 0 and anganwadi_center_id="
//                    + user_id + " and user_master_id=" + user_master_id;
//
//            String q2 = "select nutrition_id,child_id, migration_type, death_date, weight, status, height, muac, hb, d_reason, latitude, longitude, multimedia, date_of_recording,"
//                    + " anganwadi_center_id, sick_y_n, sick_name, sick_y_n15, sick_name15 from "
//                    + "Child_Nutrition_Monitor where (status='2') and anganwadi_center_id="
//                    + user_id + " and user_master_id=" + user_master_id;
//
//            String q = "select status from Child_Nutrition_Monitor where (status='0' or status='2' or status='5') and anganwadi_center_id="
//                    + user_id + " and user_master_id=" + user_master_id;
//            Cursor cur1 = DB
//                    .rawQuery(
//                            q, null);
//            Log.e("q",""+q);
//           if(cur1 !=null && cur1.getCount() > 0 )
//           {
//               cur1.move(0);
//               int status = 0;
//               while (cur1.moveToNext()) {
//                   try {
//
//                        status = cur1.getInt(cur1.getColumnIndex("status"));
//                       Log.e("status",""+status);
//                   } catch (Exception e) {
//                       // TODO Auto-generated catch block
//                       e.printStackTrace();
//                   }
//               }
//
//
//               if(status == 2)
//               {
//
//                   Cursor cur = DB.rawQuery(q2,null);
//
//                   if (cur != null && cur.getCount() > 0) {
//                       cur.move(0);
//                       while (cur.moveToNext()) {
//
//                           try {
//                               childNutrition = new ChildNutrition();
//
//                               childNutrition.setNutrition_id(cur.getInt(cur
//                                       .getColumnIndex("nutrition_id")));
//                               childNutrition.setChild_id(cur.getInt(cur
//                                       .getColumnIndex("child_id")));
//                               childNutrition.setMigration(cur.getString(cur
//                                       .getColumnIndex("migration_type")));
//                               childNutrition.setWeight(cur.getString(cur
//                                       .getColumnIndex("weight")));
//                               childNutrition.setHeight(cur.getString(cur
//                                       .getColumnIndex("height")));
//                               childNutrition.setMuac(cur.getString(cur
//                                       .getColumnIndex("muac")));
//                               childNutrition.setStatus(cur.getInt(cur.getColumnIndex("status")));
//                               childNutrition.setHB(cur.getString(cur
//                                       .getColumnIndex("hb")));
//                               childNutrition.setDreason(cur.getString(cur
//                                       .getColumnIndex("d_reason")));
//                               childNutrition.setMultimedia(cur.getString(cur
//                                       .getColumnIndex("multimedia")));
//                               childNutrition.setDate_of_monitoring(cur.getString(cur
//                                       .getColumnIndex("date_of_recording")));
//                               childNutrition.setAnganwadi_center_id(cur.getString(cur
//                                       .getColumnIndex("anganwadi_center_id")));
//                               childNutrition.setLatitude(cur.getString(cur
//                                       .getColumnIndex("latitude")));
//                               childNutrition.setLongitude(cur.getString(cur
//                                       .getColumnIndex("longitude")));
//                               childNutrition.setSickYesNo(cur.getString(cur.getColumnIndex("sick_y_n")));
//                               childNutrition.setSickName(cur.getString(cur.getColumnIndex("sick_name")));
//                               childNutrition.setSickYesNo15(cur.getString(cur.getColumnIndex("sick_y_n15")));
//                               childNutrition.setSickName15(cur.getString(cur.getColumnIndex("sick_name15")));
//                               childNutrition.setDeathDate(cur.getString(cur.getColumnIndex("death_date")));
//
//                               childMonitor.add(childNutrition);
//                           } catch (Exception e) {
//                               e.printStackTrace();
//                           }
//                       }
//                   }
//
//
//               }
//               else if(status == 0 || status == 1 || status == 5) {
//                   Cursor cur = DB.rawQuery(q1,null);
//
//                   if (cur != null && cur.getCount() > 0) {
//                       cur.move(0);
//                       while (cur.moveToNext()) {
//
//                           try {
//                               childNutrition = new ChildNutrition();
//
//                               childNutrition.setNutrition_id(cur.getInt(cur
//                                       .getColumnIndex("nutrition_id")));
//                               childNutrition.setChild_id(cur.getInt(cur
//                                       .getColumnIndex("child_id")));
//                               childNutrition.setMigration(cur.getString(cur
//                                       .getColumnIndex("migration_type")));
//                               childNutrition.setWeight(cur.getString(cur
//                                       .getColumnIndex("weight")));
//                               childNutrition.setHeight(cur.getString(cur
//                                       .getColumnIndex("height")));
//                               childNutrition.setMuac(cur.getString(cur
//                                       .getColumnIndex("muac")));
//                               childNutrition.setStatus(cur.getInt(cur.getColumnIndex("status")));
//                               childNutrition.setHB(cur.getString(cur
//                                       .getColumnIndex("hb")));
//                               childNutrition.setDreason(cur.getString(cur
//                                       .getColumnIndex("d_reason")));
//                               childNutrition.setMultimedia(cur.getString(cur
//                                       .getColumnIndex("multimedia")));
//                               childNutrition.setDate_of_monitoring(cur.getString(cur
//                                       .getColumnIndex("date_of_recording")));
//                               childNutrition.setAnganwadi_center_id(cur.getString(cur
//                                       .getColumnIndex("anganwadi_center_id")));
//                               childNutrition.setLatitude(cur.getString(cur
//                                       .getColumnIndex("latitude")));
//                               childNutrition.setLongitude(cur.getString(cur
//                                       .getColumnIndex("longitude")));
//                               childNutrition.setSickYesNo(cur.getString(cur.getColumnIndex("sick_y_n")));
//                               childNutrition.setSickName(cur.getString(cur.getColumnIndex("sick_name")));
//                               childNutrition.setSickYesNo15(cur.getString(cur.getColumnIndex("sick_y_n15")));
//                               childNutrition.setSickName15(cur.getString(cur.getColumnIndex("sick_name15")));
//                               childNutrition.setDeathDate(cur.getString(cur.getColumnIndex("death_date")));
//
//                               childMonitor.add(childNutrition);
//                           } catch (Exception e) {
//                               e.printStackTrace();
//                           }
//                       }
//                   }
//
//
//               }
//
//
//           }
//
//
//
//
//                   }
//        return childMonitor;
//    }

    public ArrayList<ChildNutrition> getChildMonitor() {
        ArrayList<ChildNutrition> childMonitor = new ArrayList<ChildNutrition>();
        mydb.openDataBase();
        DB = mydb.getDb();
        ChildNutrition childNutrition;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String s = "select nutrition_id,child_id, migration_type, death_date, weight, status, height, muac," +
                    " hb, latitude, longitude, multimedia, date_of_recording, anganwadi_center_id from "
                    + "child_nutrition_monitoring where (status='0' or status='5') and anganwadi_center_id="
                    + user_id;
            //Log.e("s", s);
            // Cursor cur =
            // DB.rawQuery("select parent_name, child_name,child_id, weight, height, muac, multimedia from "
            // + "Child_Nutrition_Monitor where status='0' and user_id="
            // + user_id, null);

            /*Cursor cur = DB
                    .rawQuery(
                            "select nutrition_id,child_id, migration_type, death_date, death_reason, weight, status, height, muac," +
                                    " hb, d_reason, latitude, longitude, multimedia, date_of_recording,"
                                    + " anganwadi_center_id, sick, sick_reason, sick_15, sick_15_reason from "
                                    + "child_nutrition_monitoring where (status='0' or status='2' or status='5') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);*/

            String query="select nutrition_id,child_id,migration_type,height,weight,muac,hb,d_reason,latitude,longitude," +
                    "have_adeama,date_of_recording,is_child_Premature, is_child_breastfeeding, is_mother_breastmilk," +
                    "user_master_id,child_meal_number, is_age_vaccination, child_meal, is_child_VitA6_months, version_code,sick,sick_reason,sick_15,sick_15_reason," +
                    "death_date,is_child_deworming_6months, is_child_ifa, child_Vaccination_Status,death_reason,state_id,district_id,block_id,village_id,created_on_mobile," +
                    "mobile_unique_id,multimedia,provision_of_edima,link_medical_facility,anganwadi_center_id from "
                    + "child_nutrition_monitoring where (status='0' or status='2' or status='5') and anganwadi_center_id="
                    + user_id + " and user_master_id=" + user_master_id;

            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        childNutrition = new ChildNutrition();
                        childNutrition.setNutrition_id(cur.getInt(cur.getColumnIndex("nutrition_id")));
                        childNutrition.setChild_id(cur.getInt(cur.getColumnIndex("child_id")));
                        childNutrition.setMigration_type(cur.getString(cur.getColumnIndex("migration_type")));
                        childNutrition.setHeight(cur.getString(cur.getColumnIndex("height")));
                        childNutrition.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        childNutrition.setMuac(cur.getString(cur.getColumnIndex("muac")));
                        childNutrition.setHb(cur.getString(cur.getColumnIndex("hb")));
                        childNutrition.setD_reason(cur.getString(cur.getColumnIndex("d_reason")));
                        childNutrition.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        childNutrition.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        childNutrition.setHave_adeama(cur.getString(cur.getColumnIndex("have_adeama")));
                        childNutrition.setDate_of_monitoring(cur.getString(cur.getColumnIndex("date_of_recording")));
                        childNutrition.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        childNutrition.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        childNutrition.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        childNutrition.setSick(cur.getString(cur.getColumnIndex("sick")));
                        childNutrition.setSick_reason(cur.getString(cur.getColumnIndex("sick_reason")));
                        childNutrition.setSick_15(cur.getString(cur.getColumnIndex("sick_15")));
                        childNutrition.setSick_15_reason(cur.getString(cur.getColumnIndex("sick_15_reason")));
                        childNutrition.setDeath_date(cur.getString(cur.getColumnIndex("death_date")));
                        childNutrition.setDeath_reason(cur.getString(cur.getColumnIndex("death_reason")));
                        childNutrition.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        childNutrition.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        childNutrition.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        childNutrition.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        childNutrition.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        childNutrition.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        childNutrition.setImg(cur.getString(cur.getColumnIndex("multimedia")));
                        childNutrition.setProvision_of_edima(cur.getString(cur.getColumnIndex("provision_of_edima")));
                        childNutrition.setLink_medical_facility(cur.getString(cur.getColumnIndex("link_medical_facility")));
                        childNutrition.setIs_age_vaccination(cur.getString(cur.getColumnIndex("is_age_vaccination")));
                        childNutrition.setIs_child_Premature(cur.getString(cur.getColumnIndex("is_child_Premature")));
                        childNutrition.setIs_child_breastfeeding(cur.getString(cur.getColumnIndex("is_child_breastfeeding")));
                        childNutrition.setIs_mother_breastmilk(cur.getString(cur.getColumnIndex("is_mother_breastmilk")));
                        childNutrition.setChild_meal_number(cur.getString(cur.getColumnIndex("child_meal_number")));
                        childNutrition.setChild_meal(cur.getString(cur.getColumnIndex("child_meal")));
                        childNutrition.setIs_child_VitA6_months(cur.getString(cur.getColumnIndex("is_child_VitA6_months")));
                        childNutrition.setIs_child_deworming_6months(cur.getString(cur.getColumnIndex("is_child_deworming_6months")));
                        childNutrition.setIs_child_ifa(cur.getString(cur.getColumnIndex("is_child_ifa")));
                        childNutrition.setChild_Vaccination_Status(cur.getString(cur.getColumnIndex("child_Vaccination_Status")));
                        /*childNutrition.setNutrition_id(cur.getInt(cur
                                .getColumnIndex("nutrition_id")));
                        childNutrition.setChild_id(cur.getInt(cur
                                .getColumnIndex("child_id")));
                        childNutrition.setMigration(cur.getString(cur
                                .getColumnIndex("migration_type")));
                        childNutrition.setWeight(cur.getString(cur
                                .getColumnIndex("weight")));
                        childNutrition.setHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        childNutrition.setMuac(cur.getString(cur
                                .getColumnIndex("muac")));
                        childNutrition.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        childNutrition.setHB(cur.getString(cur
                                .getColumnIndex("hb")));
                        childNutrition.setDreason(cur.getString(cur
                                .getColumnIndex("d_reason")));
                        childNutrition.setMultimedia(cur.getString(cur
                                .getColumnIndex("multimedia")));
                        childNutrition.setDate_of_monitoring(cur.getString(cur
                                .getColumnIndex("date_of_recording")));
                        childNutrition.setAnganwadi_center_id(cur.getString(cur
                                .getColumnIndex("anganwadi_center_id")));
                        childNutrition.setLatitude(cur.getString(cur
                                .getColumnIndex("latitude")));
                        childNutrition.setLongitude(cur.getString(cur
                                .getColumnIndex("longitude")));
                        childNutrition.setSickYesNo(cur.getString(cur.getColumnIndex("sick")));
                        childNutrition.setSickName(cur.getString(cur.getColumnIndex("sick_reason")));
                        childNutrition.setSickYesNo15(cur.getString(cur.getColumnIndex("sick_15")));
                        childNutrition.setSickName15(cur.getString(cur.getColumnIndex("sick_15_reason")));
                        childNutrition.setDeathDate(cur.getString(cur.getColumnIndex("death_date")));
                        childNutrition.setDeathReason(cur.getString(cur.getColumnIndex("death_reason")));*/

                        childMonitor.add(childNutrition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return childMonitor;
    }

    public ArrayList<PregnantWomenMonitor> getPregnantWomenMonitor() {
        ArrayList<PregnantWomenMonitor> pregnantWomenMonitor = new ArrayList<PregnantWomenMonitor>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomenMonitor pregnantWomenMon;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            // Cursor cur =
            // DB.rawQuery("select pregnant_women_name, weight, hb, current_date, women_id "
            // + "from pregnant_womem_monitor where status='0' and user_id="
            // + user_id, null);

            /*Cursor cur = DB
                    .rawQuery(
                            "select cause_date,status, migration_status, weight, hb, current_date, systolic_bp," +
                                    "diastolic_bp, date_of_recording, women_id,monitor_id, img from "
                                    + "pregnant_womem_monitor where (status='0' or status='2') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);*/
            Cursor cur = DB
                    .rawQuery(
                            "select  monitor_id,women_id,migration_type,weight,hb,systolic_bp,diastolic_bp," +
                                    "date_of_recording,height,cause_date,version_code,anganwadi_center_id,user_master_id," +
                                    "muac_status,state_id,district_id,cough,sputum_cough,urinary_complaints,prolonged_illness,sugar_albumin,block_id,village_id,created_on_mobile,mobile_unique_id," +
                                    "visit_sequence,high_bp,convulsions,vaginal_bleeding,foul_smelling_vaginal_discharge," +
                                    "severe_anaemia,diabetes,twins,img,week_of_pregnancy,night_blindness,iodine_deficiency,fluorosis,bmi,fever_with_chilled from "
                                    + "pregnant_womem_monitor where (status='0' or status='2') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);

            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomenMon = new PregnantWomenMonitor();
                        pregnantWomenMon.setPregnant_women_id(cur.getString(cur.getColumnIndex("monitor_id")));
                        pregnantWomenMon.setWomen_id(cur.getString(cur.getColumnIndex("women_id")));
                        pregnantWomenMon.setMigration_type(cur.getString(cur.getColumnIndex("migration_type")));
                        pregnantWomenMon.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        pregnantWomenMon.setHb(cur.getString(cur.getColumnIndex("hb")));
                        pregnantWomenMon.setSystolic_bp(cur.getString(cur.getColumnIndex("systolic_bp")));
                        pregnantWomenMon.setDiastolic_bp(cur.getString(cur.getColumnIndex("diastolic_bp")));
                        pregnantWomenMon.setDate_of_recording(cur.getString(cur.getColumnIndex("date_of_recording")));
                        pregnantWomenMon.setHeight(cur.getString(cur.getColumnIndex("height")));
                        pregnantWomenMon.setCause_date(cur.getString(cur.getColumnIndex("cause_date")));
                        pregnantWomenMon.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        pregnantWomenMon.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pregnantWomenMon.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pregnantWomenMon.setMuac_status(cur.getString(cur.getColumnIndex("muac_status")));
                        pregnantWomenMon.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        pregnantWomenMon.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        pregnantWomenMon.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        pregnantWomenMon.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        pregnantWomenMon.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        pregnantWomenMon.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));

                        pregnantWomenMon.setVisit_sequence(cur.getString(cur.getColumnIndex("visit_sequence")));
                        pregnantWomenMon.setHigh_bp(cur.getString(cur.getColumnIndex("high_bp")));
                        pregnantWomenMon.setConvulsions(cur.getString(cur.getColumnIndex("convulsions")));
                        pregnantWomenMon.setVaginal_bleeding(cur.getString(cur.getColumnIndex("vaginal_bleeding")));
                        pregnantWomenMon.setFoul_smelling_vaginal_discharge(cur.getString(cur.getColumnIndex("foul_smelling_vaginal_discharge")));
                        pregnantWomenMon.setSevere_anaemia(cur.getString(cur.getColumnIndex("severe_anaemia")));
                        pregnantWomenMon.setDiabetes(cur.getString(cur.getColumnIndex("diabetes")));
                        pregnantWomenMon.setTwins(cur.getString(cur.getColumnIndex("twins")));
                        pregnantWomenMon.setImage(cur.getString(cur.getColumnIndex("img")));
                        pregnantWomenMon.setWeek_of_pregnancy(cur.getString(cur.getColumnIndex("week_of_pregnancy")));
                        pregnantWomenMon.setNight_blindness(cur.getString(cur.getColumnIndex("night_blindness")));
                        pregnantWomenMon.setIodine_deficiency(cur.getString(cur.getColumnIndex("iodine_deficiency")));
                        pregnantWomenMon.setFluorosis(cur.getString(cur.getColumnIndex("fluorosis")));
                        pregnantWomenMon.setBmi(cur.getString(cur.getColumnIndex("bmi")));
                        pregnantWomenMon.setFever_with_chilled(cur.getString(cur.getColumnIndex("fever_with_chilled")));
                        pregnantWomenMon.setCough(cur.getString(cur.getColumnIndex("cough")));
                        pregnantWomenMon.setSputum_cough(cur.getString(cur.getColumnIndex("sputum_cough")));
                        pregnantWomenMon.setUrinary_complaints(cur.getString(cur.getColumnIndex("urinary_complaints")));
                        pregnantWomenMon.setProlonged_illness(cur.getString(cur.getColumnIndex("prolonged_illness")));
                        pregnantWomenMon.setSugar_albumin(cur.getString(cur.getColumnIndex("sugar_albumin")));

                        // pregnantWomenMon.setPregnant_women_name(cur.getString(cur
                        // .getColumnIndex("pregnant_women_name")));
                        /*pregnantWomenMon.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        pregnantWomenMon.setCause_date(cur.getString(cur.getColumnIndex("cause_date")));
                        pregnantWomenMon.setWomen_monitoring_id(cur
                                .getString(cur.getColumnIndex("monitor_id")));
                        pregnantWomenMon.setMgrStatus(cur.getString(cur
                                .getColumnIndex("migration_status")));
                        pregnantWomenMon.setWeight(cur.getString(cur
                                .getColumnIndex("weight")));
                        pregnantWomenMon.setHb(cur.getString(cur
                                .getColumnIndex("hb")));
                        pregnantWomenMon.setCurrent_date(cur.getString(cur
                                .getColumnIndex("current_date")));*/
                        //pregnantWomenMon.setBp(cur.getString(cur.getColumnIndex("bp")));
                        /*pregnantWomenMon.setSysBp(cur.getString(cur
                                .getColumnIndex("systolic_bp")));
                        pregnantWomenMon.setDiasBp(cur.getString(cur
                                .getColumnIndex("diastolic_bp")));
                        pregnantWomenMon.setCurrent_date(cur.getString(cur
                                .getColumnIndex("date_of_recording")));
                        pregnantWomenMon.setPregnant_women_id(cur.getString(cur
                                .getColumnIndex("women_id")));
                        pregnantWomenMon.setImage(cur.getString(cur
                                .getColumnIndex("img")));*/
                        pregnantWomenMonitor.add(pregnantWomenMon);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomenMonitor;
    }


    public ArrayList<ChildNutrition> getStudentDataOnServer() {
        ArrayList<ChildNutrition> studentArrayList = new ArrayList<>();
        ChildNutrition childNutrition;
        mydb.openDataBase();
        DB = mydb.getDb();
        String a, b, c;
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select c.name_of_child, c.child_id, p.husband_father_name from pregnant_women p, child c where c.pregnent_women_id = p.pregnant_women_id";
                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {

                        childNutrition = new ChildNutrition();


                        childNutrition.setChild_id(cursor.getInt(cursor.getColumnIndex("child_id")));
                        childNutrition.setName_of_child(cursor.getString(cursor.getColumnIndex("name_of_child")));
                        childNutrition.setChild_name(cursor.getString(cursor.getColumnIndex("husband_father_name")));
                        studentArrayList.add(childNutrition);


                    }
                    DB.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentArrayList;
    }


    public ArrayList<String> getChildSpnnerdata() {
        ArrayList<String> arrayList = new ArrayList<>();
        mydb.openDataBase();
        DB = mydb.getDb();
        String a;
        String b, c;
        try {

            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select distinct c.name_of_child, c.house_number, p.head_family from eligible_family p, child c, pregnant_women pr where c.house_number = p.house_number and c.anganwadi_center_id=p.anganwadi_center_id and c.anganwadi_center_id=pr.anganwadi_center_id and c.anganwadi_center_id= " + user_id + " order by c.name_of_child";
                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        a = cursor.getString(cursor.getColumnIndex("house_number"));
                        b = cursor.getString(cursor.getColumnIndex("name_of_child"));
                        c = cursor.getString(cursor.getColumnIndex("head_family"));
                        cursor.moveToNext();

                        arrayList.add(a + " - " + b + " ( " + c + " )");
                        // arrayList.add(String.valueOf(d));

                    }
                    DB.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return arrayList;

    }


    public ArrayList<SpinnerHelper> getChildSpnnerforsearchdata(String searchData, String label) {
        ArrayList<SpinnerHelper> spinnerData = new ArrayList<>();
        SpinnerHelper spnHelper;
        mydb.openDataBase();
        DB = mydb.getDb();
        String a;
        String b, c;
        try {

            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                //String query = "select distinct c.name_of_child, c.house_number, p.head_family from eligible_family p, child c, pregnant_women pr where c.house_number = p.house_number and c.anganwadi_center_id=p.anganwadi_center_id and c.anganwadi_center_id=pr.anganwadi_center_id and c.anganwadi_center_id= 1 and (name_of_child like " + "'" + searchData + "%' ) order by c.name_of_child";
                String query = "select distinct c.child_id, c.name_of_child, c.house_number, p.head_family from eligible_family p, child c, mother mo where c.house_number = p.house_number and c.anganwadi_center_id=p.anganwadi_center_id and c.anganwadi_center_id=mo.anganwadi_center_id and c.anganwadi_center_id= "+user_id+" and (name_of_child like " + "'" + searchData + "%' ) order by c.name_of_child";
                Cursor cursor = DB.rawQuery(query, null);
                if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                    Cursor cur = DB.rawQuery(query, null);
                    if (cur != null && cur.getCount() > 0) {
                        cur.move(0);
                        /*spnHelper = new SpinnerHelper(label,  "0");
                        spinnerData.add(spnHelper);*/
                        while (cur.moveToNext()) {

                            try {
                                spnHelper = new SpinnerHelper(cur.getString(1) + " (" + cur.getInt(2) + ") ",
                                        cur.getInt(0) + "");
                                spinnerData.add(spnHelper);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                }
                /*if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        a = cursor.getString(cursor.getColumnIndex("house_number"));
                        b = cursor.getString(cursor.getColumnIndex("name_of_child"));
                        c = cursor.getString(cursor.getColumnIndex("head_family"));
                        cursor.moveToNext();

                        arrayList.add(a + " - " + b + " ( " + c + " )");
                        // arrayList.add(String.valueOf(d));
                    }
                    DB.close();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return spinnerData;

    }

    public ArrayList<SpinnerHelper> getChildSpnnerforsearchdata1(String searchData, String label) {
        ArrayList<SpinnerHelper> spinnerData = new ArrayList<>();
        SpinnerHelper spnHelper;
        mydb.openDataBase();
        DB = mydb.getDb();
        String a;
        String b, c;
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                //String query = "select distinct c.name_of_child, c.house_number, p.head_family from eligible_family p, child c, pregnant_women pr where c.house_number = p.house_number and c.anganwadi_center_id=p.anganwadi_center_id and c.anganwadi_center_id=pr.anganwadi_center_id and c.anganwadi_center_id= 1 order by c.name_of_child";
                String query = "select distinct c.child_id, c.name_of_child, c.house_number, p.head_family from eligible_family p, child c, mother mo where c.house_number = p.house_number and c.anganwadi_center_id=p.anganwadi_center_id and c.anganwadi_center_id=mo.anganwadi_center_id and c.anganwadi_center_id= 1 order by c.name_of_child";
                Cursor cursor = DB.rawQuery(query, null);
                if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                    Cursor cur = DB.rawQuery(query, null);
                    if (cur != null && cur.getCount() > 0) {
                        cur.move(0);
                        /*spnHelper = new SpinnerHelper(label,  "0");
                        spinnerData.add(spnHelper);*/
                        while (cur.moveToNext()) {

                            try {
                                spnHelper = new SpinnerHelper(cur.getString(1) + " (" + cur.getInt(2) + ") ",
                                        cur.getInt(0) + "");
                                spinnerData.add(spnHelper);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                    }
                }
                /*if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        a = cursor.getString(cursor.getColumnIndex("house_number"));
                        b = cursor.getString(cursor.getColumnIndex("name_of_child"));
                        c = cursor.getString(cursor.getColumnIndex("head_family"));
                        cursor.moveToNext();

                        arrayList.add(a + " - " + b + " ( " + c + " )");
                        // arrayList.add(String.valueOf(d));
                    }
                    DB.close();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return spinnerData;

    }


    public String LanguageChange(String controlName, String languageid) {
        String controlname = "";
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select reg_text from regional_language where control_id='"
                + controlName + "' " + "and language_id=" + languageid;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        controlname = cur.getString(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return controlname;
    }

    public String LanguageChanges(String controlName, String languageid) {
        String controlname = "";
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select reg_text from reg_language where control_id='"
                + controlName + "' " + "and language_id=" + languageid;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        controlname = cur.getString(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return controlname;
    }

    public int getServerId(String tablename, String colname, String localid) {
        mydb.openDataBase();
        int maxId = 0;
        DB = mydb.getDb();
        String query = "select server_id from " + tablename + " where "
                + colname + "=" + localid;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        maxId = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return maxId;
    }

    public int getServerId(String parent_id) {
        mydb.openDataBase();
        int maxId = 0;
        DB = mydb.getDb();
        String query = "select server_id from eligible_family where familyid="
                + parent_id;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        maxId = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // vill_code= cur.getString(0).toString();
                }
            }
        }
        return maxId;
    }

    public int getParentId(String houseNumber) {
        mydb.openDataBase();
        int maxId = 0;
        DB = mydb.getDb();
        String query = "select familyid from eligible_family where house_number='"+houseNumber+"'";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        maxId = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return maxId;
    }

    public int getPWMServerId(String parent_id, String mother_type) {
        mydb.openDataBase();
        int maxId = 0;
        DB = mydb.getDb();
        String query=null;

        if(mother_type.equals("EW")) {
            query = "select server_id from mother where mother_id="
                    + parent_id;
        }else {
            query = "select server_id from pregnant_women where pregnant_women_id="
                + parent_id;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        maxId = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return maxId;
    }

    public int getMaxHHId() {
        mydb.openDataBase();
        int maxId = 0;
        DB = mydb.getDb();
        String query = "select max(household_id) from household_details";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        maxId = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // vill_code= cur.getString(0).toString();
                }
            }
        }
        return maxId;
    }

    public int countFamilyMembers(String family_code) {
        mydb.openDataBase();
        int members = 0;
        DB = mydb.getDb();
        String query = "select count(family_member_id) from family_member  where family_code='"
                + family_code + "'";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        members = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return members;
    }


    public String getValueById(String tableName, String id_col,
                               String value_col, int id) {
        String value = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select " + value_col + " from " + tableName + " where "
                + id_col + "=" + id;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        value = cur.getString(0).toString();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return value;
    }

    public int getValueById2(String tableName, String whereText) {
        int value = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select count(*) from " + tableName + " " + whereText;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        value = Integer.parseInt(cur.getString(0).toString());
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return value;
    }

    public ArrayList<SpinnerHelper> populateSpinnerHHID(String tableName,
                                                        String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;

        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + ", head_family "
                    + " from " + tableName + " " + "order by " + col_value
                    + " ";
        } else {
            query = "select " + col_id + " , " + col_value + ", head_family "
                    + " from " + tableName + " " + whr + " " + " order by date_or_recording desc";
        }
        spnHelper = new SpinnerHelper(label, 0 + "");
        spinnerData.add(spnHelper);

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);

                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1) + " ("
                                + cur.getString(2) + ")", cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public String[] getYear(int childid) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select Distinct SUBSTR (date_of_recording, 1, 4) from child_nutrition_monitoring where child_id=" + childid;


        Cursor cur = DB.rawQuery(query, null);
        String[] year = new String[cur.getCount()];

        cur.move(0);
        int i = 0;
        while (cur.moveToNext()) {
            year[i] = cur.getString(cur.getColumnIndex("SUBSTR (date_of_recording, 1, 4)"));
            i++;
        }

        return year;
    }

    public String[] getMonth(int childid, String year) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select Distinct SUBSTR (date_of_recording , 6, 2 ) from child_nutrition_monitoring where child_id = '" + childid + "' and date_of_recording like '" + year + "%'";


        Cursor cur = DB.rawQuery(query, null);
        String[] month = new String[cur.getCount()];

        cur.move(0);
        int i = 0;
        while (cur.moveToNext()) {
            month[i] = cur.getString(cur.getColumnIndex("SUBSTR (date_of_recording , 6, 2 )"));
            i++;
        }

        return month;
    }

    public String getHBbyDate(int childid, String date) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select nutrition_id from child_nutrition_monitoring where child_id = " + childid + " and date_of_recording like '" + date + "%'";

        String nutris = "";
        Cursor cur = DB.rawQuery(query, null);
        cur.move(0);
        int i = 0;
        while (cur.moveToNext()) {
            nutris = cur.getString(cur.getColumnIndex("nutrition_id"));
        }
        return nutris;
    }

    public String getDOB(int childid) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select date_of_birth from child where child_id = " + childid + "";

        String dob = "";
        Cursor cur = DB.rawQuery(query, null);
        cur.move(0);
        while (cur.moveToNext()) {
            dob = cur.getString(cur.getColumnIndex("date_of_birth"));
        }
        return dob;
    }

    public String[] getLatestHeight(int childid) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "SELECT height, weight, max(date_of_recording) FROM child_nutrition_monitoring where anganwadi_center_id =" + user_id + " and child_id=" + childid;

        int dob = 0;
        String lastweight = "";
        Cursor cur = DB.rawQuery(query, null);
        cur.move(0);
        while (cur.moveToNext()) {
            dob = cur.getInt(cur.getColumnIndex("height"));
            lastweight = cur.getString(cur.getColumnIndex("weight"));
        }
        if (dob == 0) {
            String query1 = "SELECT birth_height, birth_weight FROM child where anganwadi_center_id =" + user_id + " and child_id=" + childid;
            Cursor cur1 = DB.rawQuery(query1, null);
            cur1.move(0);
            while (cur1.moveToNext()) {
                dob = cur1.getInt(cur1.getColumnIndex("birth_height"));
                lastweight = cur1.getString(cur1.getColumnIndex("birth_weight"));
            }
        }
        String[] wANDh = {Integer.toString(dob), lastweight};
        return wANDh;
    }

    public void setChildHB(String nutris, String hb) {
        mydb.openDataBase();
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("hb", hb);
                values.put("status", 5);
                DB.update("child_nutrition_monitoring", values, "nutrition_id =" + nutris, null);
                Log.d("data is inserted successfully", ".....................");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
    }

    public ArrayList<SpinnerHelper> populateSpinnerbyHouseNoOfPW(String tableName,
                                                             String col_id, String col_value,
                                                             String label, String whr) {
        user_id = sph.getInt("user_id", 0);

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;

        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " " + col_value + " "+ "WHERE flag IS NOT 'M'" + " ";
        } else {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " where house_number='" + whr
                    + "' and anganwadi_center_id=" + user_id + " " +"AND flag IS NOT 'M'"+ " ";
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper("Please Select",  "0");
                 spinnerData.add(spnHelper);

                while (cur.moveToNext()) {


                    try {
                        if (null == cur.getString(1) || cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")) {
                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");

                        } else {
                            spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                    + cur.getString(1) + "]", cur.getInt(0) + "");

                        }
                        /*spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                + cur.getString(1) + "]", cur.getInt(0) + "");*/
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateSpinnerbyHouseNoOfEM(String tableName,
                                                                 String col_id, String col_value,
                                                                 String label, String whr) {
        user_id = sph.getInt("user_id", 0);

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;

        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " " + col_value + " ";
        } else {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " where house_number='" + whr +"' and anganwadi_center_id=" + user_id + " ";
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper("Please Select", "0");
                 spinnerData.add(spnHelper);
                while (cur.moveToNext()) {


                    try {
                        if (null == cur.getString(1) || cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")) {
                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");

                        } else {
                            spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                    + cur.getString(1) + "]", cur.getInt(0) + "");

                        }
                        /*spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                + cur.getString(1) + "]", cur.getInt(0) + "");*/
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateSpinnerbyHouseNo(String tableName,String tableName2,
                                                             String col_id,String col_id2, String col_value,String col_value2,
                                                             String label, String whr) {
        user_id = sph.getInt("user_id", 0);

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;

        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " " + col_value + " "+ "WHERE flag IS NOT 'M'" + " UNION " + "select " + col_id2 + " ,edd, " + col_value2 + " " + " from "
                    + tableName2 + " " + col_value2 + " ";
        } else {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " where house_number='" + whr
                    + "' and user_id=" + user_id + " " +"AND flag IS NOT 'M'"+ " UNION " + "select " + col_id2 + " ,edd, " + col_value2 + " " + " from "
                    + tableName2 + " where house_number='" + whr
                    + "' and anganwadi_center_id=" + user_id;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {


                    try {
                        if (null == cur.getString(1) || cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")) {
                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");

                        } else {
                            spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                    + cur.getString(1) + "]", cur.getInt(0) + "");

                        }
                        /*spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                + cur.getString(1) + "]", cur.getInt(0) + "");*/
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateAdolescentSpinner(String hhn) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        query = "select adolescent_id , adolescent_name from adolescent where house_number = " + hhn;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper("Select Girl", 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1),
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateSpinner(String tableName,
                                                    String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + col_value + " order by " + col_id;
        } else {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + whr + " order by " + col_id;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                // spinnerData.add(spnHelper);
                spnHelper = new SpinnerHelper(label, 0 + "");
                spnHelper = new SpinnerHelper("Please Select", "0");

                spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1),
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateSpinnerEF(String tableName,
                                                    String col_id, String col_value, String label, String whr,
                                                    boolean isEditable) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + col_value + " order by " + col_id;
        } else {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + whr + " order by " + col_id;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                if (isEditable==true){
                    spnHelper = new SpinnerHelper(label, 0 + "");
                }else{
                    spnHelper = new SpinnerHelper("Please Select", "0");
                    spinnerData.add(spnHelper);
                }
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {
                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1),
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateSpinner1(String tableName,
                                                     String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {


            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + col_value + " order by " + col_value + " desc ";

        } else {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + whr + " order by " + col_value;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1),
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateChildSpinner(String tableName,
                                                         String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + ", server_id " + " from "
                    + tableName + " " + col_value + " order by " + col_value;
        } else {
            query = "select " + col_id + " , " + col_value + ", server_id " + " from "
                    + tableName + " " + whr + " and child_id not in (select child_id from child_nutrition_monitoring where migration_type = 'Death') order by " + col_value;

        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label,  "0");
                 spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1) + " (" + cur.getInt(2) + ") ",
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }


    public ArrayList<SpinnerHelper> populateChildSpinner1(String tableName,
                                                          String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + ", server_id " + " from "
                    + tableName + " " + col_value + " order by " + col_value;
        } else {
            query = "select " + col_id + " , " + col_value + ", server_id " + " from "
                    + tableName + " " + whr + " and child_id not in (select child_id from child_nutrition_monitoring where migration_type = 'Death') order by " + col_value;

            Log.e("...........................", query);
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1) + " (" + cur.getInt(2) + ") ",
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }


    public ArrayList<SpinnerHelper> populateWomenSpinner(String tableName,
                                                         String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {

            query = "select " + col_id + " ,edd , " + col_value + " "
                    + " from " + tableName + " " + col_value + " ";
        } else {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " " + whr;


        }

        Log.e("", query);
        // spnHelper = new SpinnerHelper(label, 0 + "");
        // spinnerData.add(spnHelper);
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper("Please Select", "0");
                 spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {

//                        if (cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")  || cur.getString(1).isEmpty() || cur.getString(1) == null) {
//
//                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");
//                        }

                        if (null == cur.getString(1) || cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")) {
                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");

                        } else {
                            spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                    + cur.getString(1) + "]", cur.getInt(0) + "");

                        }
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public ArrayList<SpinnerHelper> populateWomenSpinnerFromTwoTables(String tableName,String tableName2,
                                                         String col_id,String col_id2, String col_value,String col_value2,
                                                                      String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {

            query = "select " + col_id + " ,edd , " + col_value + " "
                    + " from " + tableName + " " + col_value + " " + "WHERE flag IS NOT 'M'" + " UNION " + "select " + col_id2 + " ,edd , " + col_value2 + " "
                    + " from " + tableName2 + " " + col_value2 + " ";
        } else {
            query = "select " + col_id + " ,edd, " + col_value + " " + " from "
                    + tableName + " " + whr + " AND flag IS NOT 'M'"+ " UNION " + "select " + col_id2 + " ,edd, " + col_value2 + " " + " from "
                    + tableName2 + " " + whr;


        }

        Log.e("", query);
        // spnHelper = new SpinnerHelper(label, 0 + "");
        // spinnerData.add(spnHelper);
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {

//                        if (cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")  || cur.getString(1).isEmpty() || cur.getString(1) == null) {
//
//                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");
//                        }

                        if (null == cur.getString(1) || cur.getString(1).equalsIgnoreCase("null") || cur.getString(1).equals("")) {
                            spnHelper = new SpinnerHelper(cur.getString(2) + "", cur.getInt(0) + "");

                        } else {
                            spnHelper = new SpinnerHelper(cur.getString(2) + " ["
                                    + cur.getString(1) + "]", cur.getInt(0) + "");

                        }
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public int getEddDate(String user_id) {

        int id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select edd from pregnant_women where user_id = " + user_id;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    id = cur.getInt(cur.getColumnIndex("sick_id"));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        return id;

    }


    public ArrayList<SpinnerHelper> populateSelectAwcSpinner(String tableName, String col_id, String col_value, String label, String whr) {

        ArrayList<SpinnerHelper> spinnerData = new ArrayList<SpinnerHelper>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select distinct " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + col_value + " order by " + col_value;
        } else {
            query = "select distinct " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + whr + " order by " + col_value;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        spnHelper = new SpinnerHelper(cur.getString(1)/* + " (" + cur.getString(2) + ") "*/,
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public long updateAndChangeServer(String table_name, String server_id, String where, String img_col) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("server_id", server_id);
                values.put("status", 1);
                values.put(img_col, "");
                id = DB.update(table_name, values, where, null);
                //Log.d("data is inserted successfully", id + "");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
        return id;
    }

    public long updateChangeServer(String table_name, String server_id,
                                   String where) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("status", 1);
                values.put("server_id", server_id);
                id = DB.update(table_name, values, where, null);
                //Log.d("data is inserted successfully", id + "");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
        return id;
    }

    public long updateAndChangeEventServer(String table_name, String Id, String where, String img_col) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", Id);
                values.put("status", 1);
                values.put(img_col, "");
                id = DB.update(table_name, values, where, null);
                //Log.d("data is inserted successfully", id + "");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
        return id;
    }


    public long updateChangeAWCServer(String table_name, String status, String where, String[] img) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("status", status);
                values.put(img[0], "");
                values.put(img[1], "");
                values.put(img[2], "");
                values.put(img[3], "");
                id = DB.update(table_name, values, where, null);
                //Log.d("data is inserted successfully", id + "");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
        return id;
    }

    public long updateAttenServer(String table_name, int server_id, String status, String where) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("server_id", server_id);
                values.put("status", status);
                id = DB.update(table_name, values, where, null);
            }
        } catch (Exception s) {
        }
        return id;
    }

    public long SaveUser(String user_name, String password, String user_master_id, String full_user_name) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_name", user_name);
                values.put("password", password);
                values.put("user_master_id", user_master_id);
                values.put("full_name", full_user_name);
                id = DB.insert("user_master", null, values);
                //Log.d("data is inserted successfully", id + "");

            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
        return id;
    }



    public void deleteReminder(String alertid) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                id = DB.delete("remindar", "url= " + alertid, null);
                //Log.d("data is ed successfully", id + "");
            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());

        }
    }

    public ArrayList<ListingHelper> BindListView(String query, String Label1) {
        ArrayList<ListingHelper> listinghelpers = new ArrayList<ListingHelper>();
        mydb.openDataBase();
        ListingHelper listingHelper;
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        listingHelper = new ListingHelper();
                        listingHelper.setTitle(cur.getString(0));
                        listingHelper.setDiscription((Label1 + " :" + cur
                                .getString(1)));
                        if (cur.getColumnIndex("child_id") > -1) {
                            listingHelper
                                    .setChildID(Integer.parseInt(cur.getString(cur
                                            .getColumnIndex("child_id"))));
                        }
                        int status = cur.getInt(3);
                        if (status == 1) {
                            listingHelper.setStatus("Status: Updated");
                        } else {
                            listingHelper.setStatus("Status: Pending");
                        }
                        listinghelpers.add(listingHelper);
                    } catch (NumberFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return listinghelpers;
    }

    public long UpdateFamilyGPS(String lat, String longitude, String houseno,
                                int status) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                // values.put("house_number", houseno);
                values.put("status", status);
                values.put("latitude", lat);
                values.put("longitude", longitude);
                id = DB.update("eligible_family", values, " house_number=" + houseno,
                        null);

                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {

        }

        return id;
    }

    public long EligibleFamilyRegistration(Parent parent, int status) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", parent.getHouseNo());
                values.put("head_family", parent.getHeadofHH());
                values.put("adhaar_card", parent.getAadharCardHH());
                values.put("mobile_number", parent.getMobileHH());
                values.put("anganwadi_center_id", user_id);
                values.put("toilet_type", parent.getHas_toilet());
                values.put("water_source", parent.getHave_water());
                values.put("server_id", parent.getServer_id());
                values.put("religion_id", parent.getReligion());
                values.put("cast_category", parent.getIntcastecat());
                values.put("cast_id", parent.getCast_id());
                values.put("gender", parent.getIntGender());
                values.put("bpl_card", parent.getBpl_card());
                values.put("water_quality", parent.getWater_quality());
                values.put("status", status);
                values.put("family_land_hold", parent.getLan_value());
                values.put("seasonal_migration", parent.getSes_migration());
                values.put("date_or_recording", formattedDate);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("mobile_unique_id", parent.getMobile_unique_id());
                values.put("created_on_mobile", parent.getCreated_on_mobile());
                values.put("img", parent.getImage());
                values.put("no_of_member_in_family", parent.getNo_of_member_in_family());
                values.put("date_of_screening", parent.getDate_of_screening());
                id = DB.insert("eligible_family", null, values);
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            Log.d("tetsts", s.getMessage());
        }

        return id;
    }

    // Synch Download Eligible family to local Db
    public long EligibleFamilyRegistration2(Parent parent) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("app_version", parent.getAppVer());
                values.put("date_or_recording", parent.getDateOfRecord());
                values.put("house_number", parent.getHouseNo());
                values.put("head_family", parent.getHeadofHH());
                values.put("adhaar_card", parent.getAadharCardHH());
                values.put("mobile_number", parent.getMobileHH());
                values.put("anganwadi_center_id", parent.getUser_id());
                values.put("toilet_type", parent.getHas_toilet());
                values.put("water_source", parent.getHave_water());
                values.put("server_id", parent.getServer_id());
                values.put("religion_id", parent.getReligion());
                values.put("cast_category", parent.getIntcastecat());
                // values.put("cast", parent.getIntcast());
                values.put("land_hold", parent.getLan_value());
                values.put("seasonal_migration", parent.getSes_migration());
                values.put("gender", parent.getIntGender());
                values.put("status", parent.getStatus());
                values.put("latitude", parent.getLatitude());
                values.put("longitude", parent.getLongitude());
                values.put("user_master_id", parent.getUser_master_id());
                id = DB.insert("eligible_family", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {

        }
        DB.close();
        return id;
    }

    public long ParentRegistration(Parent parent) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                ContentValues values = new ContentValues();

                values.put("has_toilet", parent.getHas_toilet());
                values.put("have_water", parent.getHave_water());
                values.put("latitude", parent.getLatitude());
                values.put("longitude", parent.getLongitude());
                values.put("litracy_status", parent.getLitracy_status());
                // values.put("server_id",0);
                values.put("religion", parent.getReligion());
                values.put("houser_id", parent.getHouser_id());
                values.put("father", parent.getFather());
                values.put("mother", parent.getMother());
                values.put("f_adharcard", parent.getF_adharcard());
                values.put("m_adharcard", parent.getM_adharcard());
                values.put("mobile_number", parent.getMobileHH());
                values.put("address", parent.getAddress());
                values.put("caste", parent.getCaste());
                values.put("status", 0);
                values.put("land_hold", parent.getLan_value());
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("user_id", user_id);

                id = DB.insert("eligible_family", null, values);

                //Log.d("data is inseerted successfully", id + "");
            }
        } catch (Exception s) {

        }

        return id;
    }


    public long PregnantWomenRegistration(PregnantWomen pregnantWomen, String img) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", pregnantWomen.getHhId());
                values.put("name_of_pregnant_women", pregnantWomen.getPreWomenName());
                values.put("lmp_date", pregnantWomen.getLmp_date());
                values.put("edd", pregnantWomen.getEdd());
                values.put("flag", pregnantWomen.getFlag());
                values.put("child_number", pregnantWomen.getChildNumber());
                values.put("height", pregnantWomen.getHeight());
                values.put("weight", pregnantWomen.getWeight());
                values.put("hb", pregnantWomen.getHb());
                values.put("date_of_registration", pregnantWomen.getDate_of_screening());
                values.put("systolic_bp", pregnantWomen.getSysbp());
                values.put("diastolic_bp", pregnantWomen.getDiasbp());
                values.put("status", pregnantWomen.getStatus());
                values.put("registered_icds", pregnantWomen.getRegistered_icds());
                values.put("supplements_icds", pregnantWomen.getSupplements_icds());
                values.put("user_id", user_id);
                values.put("mobile_number", "");
                values.put("place_of_delivery", "");
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("parent_id", pregnantWomen.getParent_id());
                values.put("anganwadi_center_id", user_id);
                values.put("husband_father_name", pregnantWomen.getHusbandName());
                values.put("age", pregnantWomen.getAgeofPW());
                values.put("server_id", pregnantWomen.getServer_id());
                values.put("created_on_mobile", pregnantWomen.getCreated_on_mobile());
                values.put("mobile_unique_id", pregnantWomen.getMobile_unique_id());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("date_of_screening", pregnantWomen.getDate_of_screening());
                values.put("img", img);
                values.put("muac", pregnantWomen.getMuac());
                values.put("education", pregnantWomen.getEducation());
                values.put("months_of_pregnancy", pregnantWomen.getMonths_of_pregnancy());

                id = DB.insert("pregnant_women", null, values);
                //Log.d("PregnantWomenRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }
        DB.close();
        return id;
    }

    public long ExitingPregnantWomenRegistration(PregnantWomen pregnantWomen, String img, String strWHR) {
        String stat = GetOneData("status", "mother", strWHR);
        if (Integer.parseInt(stat) == 0) {
            stat = "0";
        }
        if (Integer.parseInt(stat) == 1) {
            stat = "5";
        }

        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("lmp_date", pregnantWomen.getLmp_date());
                values.put("edd", pregnantWomen.getEdd());
                values.put("flag", "");
                values.put("child_number", pregnantWomen.getChildNumber());
                values.put("height", pregnantWomen.getHeight());
                values.put("weight", pregnantWomen.getWeight());
                values.put("hb", pregnantWomen.getHb());
                values.put("husband_father_name", pregnantWomen.getHusbandName());
                values.put("systolic_bp", pregnantWomen.getSysbp());
                values.put("diastolic_bp", pregnantWomen.getDiasbp());
                values.put("status", stat);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("parent_id", pregnantWomen.getParent_id());
                values.put("mobile_unique_id", pregnantWomen.getMobile_unique_id());
                values.put("created_on_mobile", pregnantWomen.getCreated_on_mobile());
                values.put("date_of_screening", pregnantWomen.getDate_of_screening());
                values.put("img", img);
                id = DB.update("mother", values, strWHR, null);
                Log.e("PregnantWomenRegistration : data is inserted successfully", id + "...........");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }
        DB.close();
        return id;
    }

    // save mother
    public long MotherRegistration(Mother mother) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", mother.getHhId());
                values.put("name_of_mother", mother.getMotherName());
                values.put("status", mother.getStatus());
                values.put("user_id", user_id);
                values.put("date_of_registration", formattedDate);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("parent_id", mother.getParent_id());
                values.put("anganwadi_center_id", user_id);
                values.put("husband_father_name", mother.getMotherhusbandName());
                values.put("flag", "M");
                values.put("is_pregnant_converted", 0);
                values.put("age", mother.getAge());
                values.put("unmarried", mother.getUnmarried());
                values.put("widow", mother.getWidow());
                values.put("married", mother.getMarried());
                values.put("child_number", mother.getNumber_of_child());
                values.put("server_id", mother.getServer_id());
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("img", mother.getImage());
                values.put("created_on_mobile", mother.getCreated_on_mobile());
                values.put("mobile_unique_id", mother.getMobile_unique_id());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("date_of_screening", mother.getDate_of_screening());
                values.put("registred_icds", mother.getRegistred_icds());
                values.put("marital_status", mother.getMarital_status());
                values.put("education", mother.getEducation());

                //if(motherMobileUniqueId.equals("")){
                id = DB.insert("mother", null, values);
                /*}else {
                    id = DB.update("mother", values, "mobile_unique_id=" + motherMobileUniqueId, null);
                }*/
                //Log.d("PregnantWomenRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }

        return id;
    }


    //when pregnant convert to mother insert data
    public long MotherRegistration2(ArrayList<PregnantWomen> pregnantWomenList) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", pregnantWomenList.get(0).getHouse_number());
                values.put("name_of_mother", pregnantWomenList.get(0).getName_of_pregnant_women());
                values.put("status", pregnantWomenList.get(0).getStatus());
                values.put("user_id", user_id);
                values.put("date_of_registration", formattedDate);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("parent_id", 0);
                values.put("anganwadi_center_id", user_id);
                values.put("husband_father_name", pregnantWomenList.get(0).getHusband_father_name());
                values.put("flag", "M");
                values.put("is_pregnant_converted", 1);
                values.put("age", pregnantWomenList.get(0).getAge());
                values.put("unmarried", "");
                values.put("widow", "");
                values.put("married", "");
                values.put("child_number", pregnantWomenList.get(0).getChild_number());
                values.put("server_id", pregnantWomenList.get(0).getServer_id());
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("img", pregnantWomenList.get(0).getImage());
                values.put("created_on_mobile", pregnantWomenList.get(0).getCreated_on_mobile());
                values.put("mobile_unique_id", pregnantWomenList.get(0).getMobile_unique_id());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));

                id = DB.insert("mother", null, values);
                //Log.d("PregnantWomenRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }

        return id;
    }

    public long PregnantWomenMonitor(PregnantWomenMonitor pregnantWomenMonitor) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("women_id", pregnantWomenMonitor.getPregnant_women_id());
                values.put("migration_type", pregnantWomenMonitor.getMgrStatus());
                values.put("weight", pregnantWomenMonitor.getWeight());
                values.put("current_date", pregnantWomenMonitor.getCurrent_date());
                //values.put("bp", pregnantWomenMonitor.getBp());
                values.put("systolic_bp", pregnantWomenMonitor.getSysbp());
                values.put("diastolic_bp", pregnantWomenMonitor.getDiasbp());
                values.put("hb", pregnantWomenMonitor.getHb());
                values.put("date_of_recording", pregnantWomenMonitor.getDate_of_screening());
                values.put("server_monitoring_id", pregnantWomenMonitor.getServer_mon_id());
                values.put("server_id", pregnantWomenMonitor.getServer_id());
                values.put("anganwadi_center_id", user_id);
                values.put("status", pregnantWomenMonitor.getStatus());
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("img", pregnantWomenMonitor.getImage());
                values.put("cause_date", pregnantWomenMonitor.getCause_date());
                values.put("delivery_type", pregnantWomenMonitor.getDelivery_type());
                values.put("muac_status", pregnantWomenMonitor.getMuac_status());
                values.put("mobile_unique_id", pregnantWomenMonitor.getMobile_unique_id());
                values.put("created_on_mobile", pregnantWomenMonitor.getCreated_on_mobile());
                values.put("visit_sequence", pregnantWomenMonitor.getVisit_sequence());
                values.put("high_bp", pregnantWomenMonitor.getHigh_bp());
                values.put("convulsions", pregnantWomenMonitor.getConvulsions());
                values.put("vaginal_bleeding", pregnantWomenMonitor.getVaginal_bleeding());
                values.put("foul_smelling_vaginal_discharge", pregnantWomenMonitor.getFoul_smelling_vaginal_discharge());
                values.put("severe_anaemia", pregnantWomenMonitor.getSevere_anaemia());
                values.put("diabetes", pregnantWomenMonitor.getDiabetes());
                values.put("twins", pregnantWomenMonitor.getTwins());
                values.put("date_of_screening", pregnantWomenMonitor.getDate_of_screening());
                values.put("week_of_pregnancy", pregnantWomenMonitor.getWeek_of_pregnancy());
                values.put("night_blindness", pregnantWomenMonitor.getNight_blindness());
                values.put("iodine_deficiency", pregnantWomenMonitor.getIodine_deficiency());
                values.put("fluorosis", pregnantWomenMonitor.getFluorosis());
                values.put("bmi", pregnantWomenMonitor.getBmi());
                values.put("fever_with_chilled", pregnantWomenMonitor.getFever_with_chilled());
                values.put("cough", pregnantWomenMonitor.getCough());
                values.put("sputum_cough", pregnantWomenMonitor.getSputum_cough());
                values.put("urinary_complaints", pregnantWomenMonitor.getUrinary_complaints());
                values.put("prolonged_illness", pregnantWomenMonitor.getProlonged_illness());
                values.put("sugar_albumin", pregnantWomenMonitor.getSugar_albumin());

                values.put("ifa_tablet", pregnantWomenMonitor.getIFA_tablet());
                values.put("calcuim_tablet", pregnantWomenMonitor.getCalcuim_tablet());
                values.put("nutrition_garden", pregnantWomenMonitor.getNutrition_garden());
                values.put("consumption_garden", pregnantWomenMonitor.getConsumption_garden());
                values.put("registered_icds", pregnantWomenMonitor.getRegistered_ICDS());
                values.put("newborn_delivery", pregnantWomenMonitor.getNewborn_delivery());
                values.put("mortality", pregnantWomenMonitor.getMortality());

                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                id = DB.insert("pregnant_womem_monitor", null, values);
                //Log.d("PregnantWomenMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenMonitor", s.getMessage());
        }

        return id;
    }

    public long editPregnantWomenNutrition(PregnantWomenMonitor pregnantWomenMonitor, String curDate) {

        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("women_id",
                        pregnantWomenMonitor.getPregnant_women_id());
                values.put("migration_status", "");
                values.put("weight", pregnantWomenMonitor.getWeight());
                values.put("hb", pregnantWomenMonitor.getHb());
                //values.put("bp", pregnantWomenMonitor.getBp());
                values.put("systolic_bp", pregnantWomenMonitor.getSysbp());
                values.put("diastolic_bp", pregnantWomenMonitor.getDiasbp());
                values.put("current_date",
                        pregnantWomenMonitor.getCurrent_date());
                values.put("date_of_recording",
                        pregnantWomenMonitor.getDate_of_recording());
                values.put("status", 2);
                values.put("server_id", pregnantWomenMonitor.getServer_id());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("server_monitoring_id",
                        pregnantWomenMonitor.getServer_mon_id());
                values.put("img",
                        pregnantWomenMonitor.getImage());
                id = DB.update("pregnant_womem_monitor", values, "women_id=" + pregnantWomenMonitor.getPregnant_women_id() + " and anganwadi_center_id=" + user_id + " and date_of_recording like '" + curDate + "'", null);
                //Log.d("ChildNutritionMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
        DB.close();
        return id;
    }


    public boolean childNutitionMonitorSearch(ChildNutrition childNutrition)
            throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select mother from eligible_family where mother like'%"
                + childNutrition.getParentName() + "%' or father like'%"
                + childNutrition.getParentName() + "%'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    public long editAbsentChildNutrition(ChildNutrition childNutrition, String curDate) {

        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("migration_type", "");
                values.put("weight", childNutrition.getWeight());
                values.put("height", childNutrition.getHeight());
                values.put("muac", childNutrition.getMuac());
                values.put("hb", childNutrition.getHB());
                values.put("d_reason", childNutrition.getDreason());
                values.put("have_edema", childNutrition.getEdeme());
                values.put("status", 2);
                values.put("multimedia", childNutrition.getMultimedia());
                values.put("date_of_recording", childNutrition.getDate_of_monitoring());
                values.put("longitude", GlobalVars.longitude);
                values.put("latitude", GlobalVars.lattitude);
                values.put("user_master_id", user_master_id);
                id = DB.update("child_nutrition_monitoring", values, "child_id=" + childNutrition.getChild_id() + " and anganwadi_center_id=" + user_id + " and date_of_recording like '" + curDate + "'", null);
                //Log.d("ChildNutritionMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
        DB.close();
        return id;
    }

    public long childNutitionMonitor(ChildNutrition childNutrition) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("child_id", childNutrition.getChild_id());
                values.put("weight", childNutrition.getWeight());
                values.put("height", childNutrition.getHeight());
                values.put("muac", childNutrition.getMuac());
                values.put("hb", childNutrition.getHB());
                values.put("d_reason", childNutrition.getDreason());
                values.put("have_adeama", childNutrition.getHave_adeama());
                values.put("status", childNutrition.getStatus());
                values.put("multimedia", childNutrition.getMultimedia());
                values.put("date_of_recording", childNutrition.getDate_of_screening());
                values.put("anganwadi_center_id", user_id);
                values.put("server_id", childNutrition.getServer_id());
                values.put("version_code", GlobalVars.App_Version);
                values.put("migration_type", childNutrition.getMigration());
                values.put("server_nutrition_id", childNutrition.getServer_nutrition_id());
                values.put("longitude", GlobalVars.longitude);
                values.put("latitude", GlobalVars.lattitude);
                values.put("user_master_id", user_master_id);
                values.put("sick", childNutrition.getSickYesNo());
                values.put("sick_reason", childNutrition.getSickName());
                values.put("sick_15", childNutrition.getSickYesNo15());
                values.put("sick_15_reason", childNutrition.getSickName15());
                values.put("death_date", childNutrition.getDeathDate());
                values.put("death_reason", childNutrition.getDeathReason());
                values.put("mobile_unique_id", childNutrition.getMobile_unique_id());
                values.put("created_on_mobile", childNutrition.getCreated_on_mobile());
                values.put("date_of_screening", childNutrition.getDate_of_screening());
                values.put("provision_of_edima", childNutrition.getProvision_of_edima());
                values.put("link_medical_facility", childNutrition.getLink_medical_facility());
                values.put("is_age_vaccination", childNutrition.getIs_age_vaccination());
                values.put("is_child_Premature", childNutrition.getIs_child_Premature());
                values.put("is_child_breastfeeding", childNutrition.getIs_child_breastfeeding());
                values.put("is_mother_breastmilk", childNutrition.getIs_mother_breastmilk());
                values.put("child_meal_number", childNutrition.getChild_meal_number());
                values.put("child_meal", childNutrition.getChild_meal());
                values.put("is_child_VitA6_months", childNutrition.getIs_child_VitA6_months());
                values.put("is_child_deworming_6months", childNutrition.getIs_child_deworming_6months());
                values.put("is_child_ifa", childNutrition.getIs_child_ifa());
                values.put("child_Vaccination_Status", childNutrition.getChild_Vaccination_Status());

                values.put("medical_critical", childNutrition.getMedical_critical());
                values.put("medical_critical_reason", childNutrition.getMedical_critical_reason());
                values.put("nrc_last_month", childNutrition.getNrc_last_month());
                values.put("nrc_last_month_date", childNutrition.getNrc_last_month_date());
                values.put("nutrition_bnf", childNutrition.getNutrition_bnf());
                values.put("nutrition_garden", childNutrition.getNutrition_garden());
                values.put("nutrition_garden_kit", childNutrition.getNutrition_garden_kit());
                values.put("nrc_referral", childNutrition.getNrc_referral());
                values.put("complimentary_nutrition", childNutrition.getComplimentary_nutrition());
                values.put("registered_with_icds", childNutrition.getRegistered_with_icds());

                values.put("sick_reason", childNutrition.getSick_reason());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                id = DB.insert("child_nutrition_monitoring", null, values);
                Log.d("tag","ChildNutritionMonitor : data is inserted successfully"+values + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
        DB.close();
        return id;
    }

    public void SaveEditEF(String[] info) {

        String WaterId = "", Toilettype = "";
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query1 = "select drinking_water_source_id from drinking_water_source where drinking_water_source = '" + info[2].toString() + "'";
                Cursor cur1 = DB.rawQuery(query1, null);
                if (cur1 != null && cur1.getCount() > 0) {
                    cur1.move(0);
                    while (cur1.moveToNext()) {
                        WaterId = cur1.getString(cur1.getColumnIndex("drinking_water_source_id"));
                    }
                }

                String query2 = "select toilet_avaibality_id from toilet_avaibality where toilet_avaibality_source = '" + info[1].toString() + "'";
                Cursor cur2 = DB.rawQuery(query2, null);
                if (cur2 != null && cur2.getCount() > 0) {
                    cur2.move(0);
                    while (cur2.moveToNext()) {
                        Toilettype = cur2.getString(cur2.getColumnIndex("toilet_avaibality_id"));
                    }
                }

                ContentValues values = new ContentValues();
                values.put("toilet_type", Toilettype);
                values.put("water_source", WaterId);
                values.put("status", 5);
                id = DB.update("eligible_family", values, "house_number=" + info[0].toString(), null);

                Log.d("Eligible family Updation : data is updated successfully", id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
    }

    public long NutritionRegistration(Nutrition nutrition) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("child_id", nutrition.getChild_id());
                values.put("date_of_monitoring",
                        nutrition.getDate_of_monitiring());
                values.put("height", nutrition.getHeight());
                values.put("weight", nutrition.getWeight());
                values.put("muac", nutrition.getMauc());
                values.put("latitude", nutrition.getLatitdue());
                values.put("longitude", nutrition.getLongitude());
                values.put("status", nutrition.getStatus());
                id = DB.insert("nutrition", null, values);
                //Log.d("NutritionRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("NutritionRegistration : " + s.getMessage(), "");
        }

        return id;
    }

    public long NutritionRegistration2(Nutrition nutrition) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("child_id", nutrition.getChild_id());
                values.put("date_of_monitoring",
                        nutrition.getDate_of_monitiring());
                values.put("height", nutrition.getHeight());
                values.put("weight", nutrition.getWeight());
                values.put("muac", nutrition.getMauc());
                values.put("latitude", nutrition.getLatitdue());
                values.put("longitude", nutrition.getLongitude());
                values.put("status", nutrition.getStatus());
                values.put("nutrition_id", nutrition.getNutrition_id());
                values.put("server_id", nutrition.getServer_id());
                values.put("status", "1");
                id = DB.insert("child_nutrition_monitoring", null, values);

                //Log.d("NutritionRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("NutritionRegistration : " + s.getMessage(), "");
        }

        return id;
    }

    public long setReminder(String title, String url, String type,
                            String image, String orgnization, String time, String date) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("title", title);
                values.put("url", url);
                values.put("type", type);
                values.put("image", image);
                values.put("orgnization", orgnization);
                values.put("time", time);
                values.put("date", date);
                id = DB.insert("remindar", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {
        }
        return id;
    }

    public Date getYears(String year) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String x = "2014";
        try {
            date = dateFormat.parse(x);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return date;

    }

    public void profileSave(String tableName, String colName,
                            ArrayList<Integer> selectedValues) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                for (int i = 0; i < selectedValues.size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(colName, selectedValues.get(i));
                    values.put("userid", 1);
                    long id = DB.insert(tableName, null, values);
                    System.out.println(String.valueOf(id));
                }
            }
        } catch (Exception s) {
        }
    }

    public void resetField(String tableName) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                int a = DB.delete(tableName, null, null);
                //Log.d("deleted", "" + a);
            }
        } catch (Exception s) {
        }
    }

    public int countData(String tableName) {
        mydb.openDataBase();
        int count = 0;
        DB = mydb.getDb();
        String query = "select count(userid) from " + tableName;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    count = cur.getInt(0);
                    // vill_code= cur.getString(0).toString();
                }
            }
        }
        return count;
    }

    public long updateViewAbsentStatus(int child_id, String table_name) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("view_absent_status", 1);
                id = DB.update(table_name, values, "child_id=" + child_id, null);
            }
        } catch (Exception s) {

        }
        return id;
    }

    public void deleteData(String tableName, String conditionColumn,
                           String value) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                int a = DB.delete(tableName, conditionColumn + "=" + value,
                        null);
                //Log.d("deleted", "" + a);
            }
        } catch (Exception s) {
        }
    }

    public String getSelectedValue(Spinner spn) {
        SpinnerHelper data = (SpinnerHelper) spn.getItemAtPosition((int) spn
                .getSelectedItemId());
        return data.getValue();
    }

    public String getFormattedDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format); // Set your date
        // format
        String formattedDate4 = sdf.format(date); // Get Date String according
        // to date format
        return formattedDate4;
    }

    public int HHNoExists(String hhno) {

        int familyid = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select familyid from eligible_family where anganwadi_center_id="
                + user_id + " and  house_number='" + hhno + "'";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        familyid = cur.getInt(cur.getColumnIndex("familyid"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return familyid;

    }

    public boolean PregnantWomenExists(String familyCode, int familyMemberID) {

        return false;
    }

    public byte[] ConvertToByteArray(String FilePath) {
        File file = new File(FilePath);
        byte[] bytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[(int) file.length()];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readNum);
                }
                fis.close();
            } catch (IOException ex) {
                String error = ex.getMessage();
                // Log.i(R.string.error+"", error + ex.getMessage());
            }
            bytes = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public long AdolescentRegistration(Adolescent adolescent, String img) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", adolescent.getHhId());
                values.put("adolescent_name", adolescent.getNameOfTheGirl());
                values.put("father_name", adolescent.getGirlFatherName());
                values.put("mobile_number", adolescent.getMobile_number());
                values.put("date_of_birth", adolescent.getDateOfBirth());
                values.put("gender", adolescent.getGender());
                values.put("height", adolescent.getGirlHeight());
                values.put("weight", adolescent.getGirlWeight());
                values.put("HB", adolescent.getGirlHb());
                values.put("server_id", adolescent.getServer_id());
                values.put("parent_id", adolescent.getParent_id());
                values.put("OSP", adolescent.getOSP());
                values.put("osp_year", adolescent.getospYear());
                values.put("osp_month", adolescent.getospMonth());
                values.put("chronic_disease", adolescent.getChronicDisease());
                values.put("status", adolescent.getStatus());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("created_on", adolescent.getDate_of_screening());
                values.put("is_adolescent_access_icds", adolescent.getIs_adolescent_access_icds());
                values.put("is_adolescent_rececive_ifa", adolescent.getIs_adolescent_rececive_ifa());
                values.put("is_taken_ifa", adolescent.getIs_taken_ifa());
                values.put("is_adolescent_dewarming_tablet", adolescent.getIs_adolescent_dewarming_tablet());
                values.put("is_health_service", adolescent.getIs_health_service());
                values.put("adolescent_age", adolescent.getAdolescent_age());
                values.put("version_code", GlobalVars.App_Version);
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("img", img);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("mobile_unique_id", adolescent.getMobile_unique_id());
                values.put("created_on_mobile", adolescent.getCreated_on_mobile());
                values.put("date_of_screening", adolescent.getDate_of_screening());

                id = DB.insert("adolescent", null, values);
                //Log.d("AdolescentRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("AdolescentRegistration", s.getMessage());
        }

        return id;
    }

    public void makeMother(String dob, String p_id) {
        try {
            String lmp_date = GetOneData("lmp_date", "pregnant_women", "pregnant_women_id = " + p_id).substring(0, 10);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(dob);
            Date date2 = sdf.parse(lmp_date);

            //if (date1.after(date2)) {
                mydb.openDataBase();
                long id = 0;
                DB = mydb.getDb();
                if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                    ContentValues values = new ContentValues();
                    values.put("flag", "M");

                    id = DB.update("pregnant_women", values, "pregnant_women_id = " + p_id, null);
                    Log.e("inserted successfully", id + "");
                }
            //}

        } catch (Exception e) {
        }
    }

    public long ChildRegistration(Child child) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("parent_id", child.getParent_id());
                values.put("house_number", child.getHHID());
                values.put("name_of_child", child.getChild_name());
                values.put("is_disability", child.getDisablity());
                values.put("have_edema", child.getEdema());
                values.put("date_of_birth", child.getDate_of_birth());
                values.put("birth_weight", child.getChild_weight());
                values.put("birth_status", child.getBirth_order());
                values.put("birth_height", child.getHeight());
                values.put("birth_muac", child.getMuac());
                values.put("hb", child.getHb());
                values.put("longitude", GlobalVars.lattitude);
                values.put("lattitude", GlobalVars.longitude);
                values.put("photopath", child.getMultimedia());
                values.put("gender", child.getGender());
                values.put("status", child.getStatus());
                values.put("anganwadi_center_id", user_id);
                values.put("server_id", child.getServer_id());
                values.put("Date_of_registration", child.getDate_of_screening());
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("pregnent_women_id", child.getPregnent_women_id());
                values.put("check_adol", child.getCheck_adol());
                values.put("mobile_unique_id", child.getMobile_unique_id());
                values.put("created_on_mobile", child.getCreated_on_mobile());
                values.put("selected_women_type", child.getSelected_women_type());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("date_of_screening", child.getDate_of_screening());
                values.put("registred_icds", child.getRegistred_icds());
                values.put("dob_month", child.getDob_month());
                values.put("bpl_card", child.getBpl_card());
                values.put("is_child_breastfed", child.getIs_child_breastfed());
                values.put("select_breastfeed_initiated", child.getSelect_breastfeed_initiated());
                values.put("is_complementary_food", child.getIs_complementary_food());
                values.put("month_complementary_feeding", child.getMonth_complementary_feeding());
                values.put("is_child_Premature", child.getIs_child_Premature());
                values.put("health_facility", child.getHealth_facility());
                id = DB.insert("child", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {

            //Log.e("error in child reg ", "inserting error");

        }

        return id;
    }

    public int validateUserOffline(String username, String password) {
        mydb.openDataBase();
        int user_id = 0;
        DB = mydb.getDb();
        String query = "select user_id from users where user_name='" + username
                + "' and password='" + password + "'";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        user_id = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return user_id;
    }

    public int validateUserOffline2(String username, String password) {
        mydb.openDataBase();
        int user_master_id = 0;
        DB = mydb.getDb();
        String query = "select user_master_id from user_master where user_name='" + username
                + "' and password='" + password + "'";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        user_master_id = cur.getInt(0);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return user_master_id;
    }


    public int getTotalCount(String table, String idcol, int id) {
        int total = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery("select count(*) as total from " + table
                    + " where " + idcol + "=" + id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        total = cur.getInt(cur.getColumnIndex("total"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return total;
    }

    public long ParentRegistration2(Parent parent) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                ContentValues values = new ContentValues();

                values.put("parent_id", parent.getParent_id());
                values.put("has_toilet", parent.getHas_toilet());
                values.put("have_water", parent.getHave_water());
                values.put("latitude", parent.getLatitude());
                values.put("longitude", parent.getLongitude());
                values.put("litracy_status", parent.getLitracy_status());
                values.put("server_id", parent.getServer_id());
                values.put("religion", parent.getReligion());
                values.put("houser_id", parent.getHouser_id());
                values.put("father", parent.getFather());
                values.put("mother", parent.getMother());
                values.put("f_adharcard", parent.getF_adharcard());
                values.put("m_adharcard", parent.getM_adharcard());
                values.put("mobile_number", parent.getMobileHH());
                values.put("address", parent.getAddress());
                values.put("caste", parent.getCaste());
                values.put("status", parent.getStatus());
                values.put("land_hold", parent.getLan_value());
                values.put("latitude", parent.getLatitude());
                values.put("longitude", parent.getLongitude());
                values.put("user_id", parent.getUser_id());
                id = DB.insert("eligible_family", null, values);

                //Log.d("data is inseerted successfully", id + "");
            }
        } catch (Exception s) {

        }

        return id;
    }

    public long PregnantWomenRegistration2(PregnantWomen pregnantWomen) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("pregnant_women_id",
                        pregnantWomen.getPregnant_women_id());
                values.put("parent_id", pregnantWomen.getParent_id());
                values.put("name_of_pregnant_women",
                        pregnantWomen.getPreWomenName());
                values.put("lmp_date", pregnantWomen.getLmp_date());
                values.put("edd", pregnantWomen.getEdd());
                values.put("place_of_delivery",
                        pregnantWomen.getPlace_of_delivery());
                values.put("weight", pregnantWomen.getWeight());
                values.put("height", pregnantWomen.getHeight());
                values.put("status", pregnantWomen.getStatus());
                values.put("user_id", pregnantWomen.getUser_id());
                values.put("latitude", pregnantWomen.getLatitude());
                values.put("longitude", pregnantWomen.getLongitude());
                values.put("server_id", pregnantWomen.getServer_id());

                id = DB.insert("pregnant_women", null, values);
                //Log.d("PregnantWomenRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }

        return id;
    }


    public long PregnantWomenMonitor2(PregnantWomenMonitor pregnantWomenMonitor) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("women_id",
                        pregnantWomenMonitor.getPregnant_women_id());
                values.put("pregnant_women_name",
                        pregnantWomenMonitor.getPregnant_women_name());
                values.put("weight", pregnantWomenMonitor.getWeight());
                values.put("hb", pregnantWomenMonitor.getHb());
                values.put("current_date",
                        pregnantWomenMonitor.getCurrent_date());
                values.put("server_id", pregnantWomenMonitor.getServer_id());
                values.put("status", 1);

                id = DB.insert("pregnant_womem_monitor", null, values);
                //Log.d("PregnantWomenMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenMonitor", s.getMessage());
        }

        return id;
    }


    public long childNutitionMonitor2(ChildNutrition childNutrition) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("child_id", childNutrition.getChild_id());
                values.put("parent_name", childNutrition.getParentName());
                values.put("weight", childNutrition.getWeight());
                values.put("height", childNutrition.getHeight());
                values.put("muac", childNutrition.getMuac());
                values.put("child_name", childNutrition.getChild_name());
                values.put("status", 0);
                values.put("multimedia", childNutrition.getMultimedia());
                id = DB.insert("child_nutrition_monitoring", null, values);
                //Log.d("ChildNutritionMonitor : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }

        return id;
    }

    public long ChildRegistration2(Child child) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                ContentValues values = new ContentValues();
                values.put("child_id", child.getChild_id());
                values.put("child_name", child.getChild_name());
                values.put("date_of_birth", child.getDate_of_birth());
                values.put("birth_weight", child.getChild_weight());
                values.put("birth_order", child.getBirth_order());
                values.put("birth_height", child.getHeight());
                values.put("parent_name", child.getParent_name());
                values.put("longitude", child.getLongitude());
                values.put("latitude", child.getLatitude());
                values.put("multimedia", child.getMultimedia());
                values.put("gender", child.getGender());
                values.put("server_id", child.getServer_id());
                values.put("user_id", child.getUser_id());

                values.put("status", 1);

                id = DB.insert("child", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {

        }

        return id;
    }


    // Eligible Family Detail
    public ArrayList<Parent> getDataOfFamilyId(int family_id) {
        // TODO Auto-generated method stub
        Parent parent;

        ArrayList<Parent> parentdetail = new ArrayList<Parent>();
        mydb.openDataBase();

        DB = mydb.getDb();

        query = "select a.house_number,a.head_family,a.adhaar_card,a.mobile_number,b.gender_value,c.value,d.category,e.toilet_avaibality_source,f.drinking_water_source from eligible_family as a,gender b,religion as c,caste_category as d,toilet_avaibality as e,drinking_water_source as f where a.water_source=f.drinking_water_source_id and a.toilet_type=e.toilet_avaibality_id and a.gender=b.id and a.cast_category=c.religion_id and a.cast_category=d.id  and a.familyid="
                + family_id + " and a.anganwadi_center_id=" + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();

                        parent.setHouseNo(cur.getString(cur
                                .getColumnIndex("house_number")));
                        parent.setHeadofHH(cur.getString(cur
                                .getColumnIndex("head_family")));
                        parent.setAadharCardHH(cur.getString(cur
                                .getColumnIndex("adhaar_card")));
                        parent.setMobileHH(cur.getString(cur
                                .getColumnIndex("mobile_number")));
                        parent.setGender(cur.getString(cur
                                .getColumnIndex("gender_value")));

                        parent.setTxtReligion(cur.getString(cur
                                .getColumnIndex("value")));
                        parent.setTxthave_water(cur.getString(cur
                                .getColumnIndex("drinking_water_source")));
                        parent.setTxthave_toilet(cur.getString(cur
                                .getColumnIndex("toilet_avaibality_source")));

                        parent.setCaste(cur.getString(cur
                                .getColumnIndex("category")));

                        parentdetail.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return parentdetail;
    }

    public ArrayList<Views> getDataOfMotherByWomenId(int women_id) {
        // TODO Auto-generated method stub
        ArrayList<Views> pregnantWomens = new ArrayList<Views>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Views pregnantWomen;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            /*
             * String query =
             * "select a.name_of_pregnant_women,a.husband_father_name,a.house_number,b.adhaar_card,c.value as religion,d.category,e.toilet_avaibality_source,f.drinking_water_source from pregnant_women as a,parents as b, religion as c,  caste_category as d,toilet_avaibality as e,drinking_water_source as f where a.house_number= b.house_number and b.religion_id= c.religion_id and b.cast_category= d.id and b.toilet_type= e.toilet_avaibality_id and b.water_source= f.drinking_water_source_id and a.pregnant_women_id="
             * + women_id;
             */
            String query = "select a.name_of_pregnant_women,a.husband_father_name,a.house_number,a.child_number,b.adhaar_card,c.value as religion,e.toilet_avaibality_source,f.drinking_water_source from pregnant_women as a,eligible_family as b, religion as c,toilet_avaibality as e,drinking_water_source as f where a.house_number= b.house_number and b.religion_id= c.religion_id and b.toilet_type= e.toilet_avaibality_id and b.water_source= f.drinking_water_source_id and a.pregnant_women_id="
                    + women_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        pregnantWomen = new Views();

                        pregnantWomen.setName(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setHusbandName(cur.getString(cur
                                .getColumnIndex("husband_father_name")));
                        pregnantWomen.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        pregnantWomen.setChildNumber(cur.getString(cur
                                .getColumnIndex("child_number")));
                        pregnantWomen.setAdhaar_card(cur.getString(cur
                                .getColumnIndex("adhaar_card")));
                        pregnantWomen.setReligion(cur.getString(cur
                                .getColumnIndex("religion")));
                        /*
                         * pregnantWomen.setCategory(cur.getString(cur
                         * .getColumnIndex("category")));
                         */
                        pregnantWomen
                                .setToilet_avaibality_source(cur.getString(cur
                                        .getColumnIndex("toilet_avaibality_source")));
                        pregnantWomen
                                .setDrinking_water_source(cur.getString(cur
                                        .getColumnIndex("drinking_water_source")));
                        pregnantWomens.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomens;
    }

    public ArrayList<Views> getDataByADOGirlId(int adogirl_id) {
        // TODO Auto-generated method stub
        ArrayList<Views> AdoGirlList = new ArrayList<Views>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Views AdoGirl;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            /*
             * String query =
             * "select a.girl_name,a.father_name,a.house_number,b.adhaar_card,c.value as religion,d.category,e.toilet_avaibality_source,f.drinking_water_source from adolecent as a,parents as b, religion as c,  caste_category as d,toilet_avaibality as e,drinking_water_source as f where a.house_number= b.house_number and b.religion_id= c.religion_id and b.cast_category= d.id and b.toilet_type= e.toilet_avaibality_id and b.water_source= f.drinking_water_source_id and a.adolescent_id="
             * + adogirl_id;
             */

            String query = "select a.girl_name,a.father_name,a.house_number,b.adhaar_card,c.value as religion,e.toilet_avaibality_source,f.drinking_water_source from adolecent as a,eligible_family as b, religion as c,toilet_avaibality as e,drinking_water_source as f where a.house_number= b.house_number and b.religion_id= c.religion_id and b.toilet_type= e.toilet_avaibality_id and b.water_source= f.drinking_water_source_id and a.adolescent_id="
                    + adogirl_id;

            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        AdoGirl = new Views();

                        AdoGirl.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        AdoGirl.setName(cur.getString(cur
                                .getColumnIndex("girl_name")));
                        AdoGirl.setHusbandName(cur.getString(cur
                                .getColumnIndex("father_name")));

                        AdoGirl.setAdhaar_card(cur.getString(cur
                                .getColumnIndex("adhaar_card")));
                        AdoGirl.setReligion(cur.getString(cur
                                .getColumnIndex("religion")));
                        /*
                         * AdoGirl.setCategory(cur.getString(cur
                         * .getColumnIndex("category")));
                         */
                        AdoGirl.setToilet_avaibality_source(cur.getString(cur
                                .getColumnIndex("toilet_avaibality_source")));
                        AdoGirl.setDrinking_water_source(cur.getString(cur
                                .getColumnIndex("drinking_water_source")));

                        AdoGirlList.add(AdoGirl);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return AdoGirlList;
    }

    public int getChildId(String getChildName, String getHouseNo) {
        // TODO Auto-generated method stub
        int childId = 0;
        mydb.openDataBase();

        DB = mydb.getDb();
        String query = "select child_id from child where name_of_child='"
                + getChildName + "'" + " and house_number=" + getHouseNo;

        // select reg_text from regional_language where
        // control_id='"+controlName+"' " + "and language_id="+ languageid;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        childId = cur.getInt(cur.getColumnIndex("child_id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return childId;
    }

    public ArrayList<Views> getDataByChildId(int child_id) {

        ArrayList<Views> childdetail = new ArrayList<Views>();
        mydb.openDataBase();
        Views child;
        DB = mydb.getDb();

        // String query =
        // "select a.name_of_child,b.gender_value,a.date_of_birth ,c.name_of_pregnant_women,d.adhaar_card,e.value as religion,f.value as cast,g.category,h.toilet_avaibality_source,i.drinking_water_source from child_registration as a,gender as b,pregnant_women as c,parents as d, religion as e, cast as f, caste_category as g,toilet_avaibality as h,drinking_water_source as i where a.gender=b.id and c.house_number=d.house_number and d.religion_id= e.religion_id and d.cast_id= f.cast_id and d.cast_category= g.id and d.toilet_type= toilet_avaibality_id and d.water_source= i.drinking_water_source_id and a.child_id="+
        // child_id;
        String query = "select a.name_of_child,c.gender_value,a.date_of_birth, b.name_of_pregnant_women, d.adhaar_card,e.value as religion,f.toilet_avaibality_source,g.drinking_water_source from child as a, pregnant_women as b, gender as c, eligible_family as d, religion as e, toilet_avaibality as f,drinking_water_source as g where a.pregnent_women_id=b.pregnant_women_id and a.gender=c.id and  b.house_number=d.house_number and d.religion_id= e.religion_id and d.toilet_type= f.toilet_avaibality_id and d.water_source= g.drinking_water_source_id and a.child_id="
                + child_id;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        child = new Views();

                        child.setName(cur.getString(cur
                                .getColumnIndex("name_of_child")));
                        child.setGender(cur.getString(cur
                                .getColumnIndex("gender_value")));
                        child.setDate_of_birth(cur.getString(cur
                                .getColumnIndex("date_of_birth")));
                        child.setMother_name(cur.getString(cur
                                .getColumnIndex("name_of_pregnant_women")));
                        child.setAdhaar_card(cur.getString(cur
                                .getColumnIndex("adhaar_card")));
                        child.setReligion(cur.getString(cur
                                .getColumnIndex("religion")));
                        /*
                         * child.setCategory(cur.getString(cur
                         * .getColumnIndex("category")));
                         */
                        child.setToilet_avaibality_source(cur.getString(cur
                                .getColumnIndex("toilet_avaibality_source")));
                        child.setDrinking_water_source(cur.getString(cur
                                .getColumnIndex("drinking_water_source")));

                        childdetail.add(child);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return childdetail;
    }

    public double getHeightOFPW(int women_id) {
        double height = 0.0;
        mydb.openDataBase();
        DB = mydb.getDb();
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query = "select height from pregnant_women where pregnant_women_id=" + women_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        height = Double.parseDouble(cur.getString(0));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return height;
    }

    public ArrayList<PregnantWomenMonitor> getPregnentWomenMonitorDataBy(
            int women_id) {
        ArrayList<PregnantWomenMonitor> pregnantWomenMonitor = new ArrayList<PregnantWomenMonitor>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomenMonitor pregnantWomenMon;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query = "select weight, hb, systolic_bp, diastolic_bp, date_of_recording from pregnant_womem_monitor where women_id="
                    + women_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomenMon = new PregnantWomenMonitor();
                        /*
                         * pregnantWomenMon.setPregnant_women_name(cur.getString(
                         * cur .getColumnIndex("pregnant_women_name")));
                         */
                        pregnantWomenMon.setWeight(cur.getString(cur
                                .getColumnIndex("weight")));

                        //Log.e("weight",cur.getString(cur.getColumnIndex("weight")));

                        pregnantWomenMon.setHb(cur.getString(cur
                                .getColumnIndex("hb")));
                        //pregnantWomenMon.setBp(cur.getString(cur.getColumnIndex("bp")));
                        pregnantWomenMon.setSysBp(cur.getString(cur
                                .getColumnIndex("systolic_bp")));
                        pregnantWomenMon.setDiasBp(cur.getString(cur
                                .getColumnIndex("diastolic_bp")));
                        pregnantWomenMon.setCurrent_date(cur.getString(cur
                                .getColumnIndex("date_of_recording")));

                        pregnantWomenMonitor.add(pregnantWomenMon);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return pregnantWomenMonitor;
    }

    public ArrayList<ChildNutrition> getchildNutrationMonitorDataBy(int child_id) {
        ArrayList<ChildNutrition> childMonitor = new ArrayList<ChildNutrition>();
        mydb.openDataBase();
        DB = mydb.getDb();
        ChildNutrition childNutration;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query = "select  weight, height, muac, date_of_recording, hb, migration_type from child_nutrition_monitoring where child_id ="
                    + child_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        childNutration = new ChildNutrition();

                        childNutration.setWeight(cur.getString(cur
                                .getColumnIndex("weight")));

                        //Log.e("weight",cur.getString(cur.getColumnIndex("weight")));

                        childNutration.setHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        childNutration.setMuac(cur.getString(cur
                                .getColumnIndex("muac")));
                        childNutration.setDate_of_monitoring(cur.getString(cur
                                .getColumnIndex("date_of_recording")));
                        childNutration.setHB(cur.getString(cur.getColumnIndex("hb")));

                        childNutration.setMigration(cur.getString(cur.getColumnIndex("migration_type")));
                        childMonitor.add(childNutration);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return childMonitor;
    }

    public ArrayList<Adolescent> allAdolescentGirl() {
        // TODO Auto-generated method stub

        Adolescent adoGirl;

        ArrayList<Adolescent> adoGirldetail = new ArrayList<Adolescent>();
        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select girl_name,house_number,father_name from adolescent where anganwadi_center_id="
                + user_id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        adoGirl = new Adolescent();
                        adoGirl.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        adoGirl.setNameOfTheGirl(cur.getString(cur
                                .getColumnIndex("girl_name")));
                        adoGirl.setGirlFatherName(cur.getString(cur
                                .getColumnIndex("father_name")));
                        adoGirldetail.add(adoGirl);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return adoGirldetail;
    }

    public ArrayList<Adolescent> getAdolscentGirl() {
        // TODO Auto-generated method stub
        ArrayList<Adolescent> AdoGirlList = new ArrayList<Adolescent>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Adolescent AdoGirl;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            /*String query = "select adolescent_id,house_number, girl_name, father_name, "
                    + "date_of_birth, height, weight,HB,osp_year,osp_month,chronical_id, img from adolescent where server_id= 0 and anganwadi_center_id="
                    + user_id + " and user_master_id=" + user_master_id;*/
            String query = "select adolescent_id,parent_id,house_number,adolescent_name,father_name,mobile_number,date_of_birth," +
                    "gender,height,weight,HB,OSP,chronic_disease,is_adolescent_access_icds,is_adolescent_rececive_ifa, version_code,user_master_id," +
                    "anganwadi_center_id,is_taken_ifa,is_adolescent_dewarming_tablet,adolescent_age, is_health_service,created_on_mobile,mobile_unique_id,state_id,district_id,block_id," +
                    "village_id,latitude,longitude,img from adolescent where server_id= 0 and anganwadi_center_id="
                    + user_id + " and user_master_id=" + user_master_id;
            // String query =
            // "select adolescent_id,house_number, girl_name, father_name, "
            // +
            // "date_of_birth, height, weight,HB,osp_year,osp_month,chronical_id from adolecent where server_id= 0"
            // + " and anganwadi_center_id=" + user_id;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        AdoGirl = new Adolescent();
                        AdoGirl.setAdolescent_id(cur.getInt(cur.getColumnIndex("adolescent_id")));
                        AdoGirl.setParent_id(String.valueOf(cur.getInt(cur.getColumnIndex("parent_id"))));
                        AdoGirl.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        AdoGirl.setAdolescent_name(cur.getString(cur.getColumnIndex("adolescent_name")));
                        AdoGirl.setFather_name(cur.getString(cur.getColumnIndex("father_name")));
                        AdoGirl.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        AdoGirl.setDate_of_birth(cur.getString(cur.getColumnIndex("date_of_birth")));
                        AdoGirl.setGender(cur.getString(cur.getColumnIndex("gender")));
                        AdoGirl.setHeight(cur.getString(cur.getColumnIndex("height")));
                        AdoGirl.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        AdoGirl.setHB(cur.getString(cur.getColumnIndex("HB")));
                        AdoGirl.setOSP(cur.getString(cur.getColumnIndex("OSP")));
                        AdoGirl.setImage(cur.getString(cur.getColumnIndex("img")));
                        AdoGirl.setChronic_disease(cur.getString(cur.getColumnIndex("chronic_disease")));
                        AdoGirl.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        AdoGirl.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        AdoGirl.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        AdoGirl.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        AdoGirl.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        AdoGirl.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        AdoGirl.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        AdoGirl.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        AdoGirl.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        AdoGirl.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        AdoGirl.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        AdoGirl.setIs_adolescent_access_icds(cur.getString(cur.getColumnIndex("is_adolescent_access_icds")));
                        AdoGirl.setIs_adolescent_rececive_ifa(cur.getString(cur.getColumnIndex("is_adolescent_rececive_ifa")));
                        AdoGirl.setIs_taken_ifa(cur.getString(cur.getColumnIndex("is_taken_ifa")));
                        AdoGirl.setIs_adolescent_dewarming_tablet(cur.getString(cur.getColumnIndex("is_adolescent_dewarming_tablet")));
                        AdoGirl.setIs_health_service(cur.getString(cur.getColumnIndex("is_health_service")));
                        AdoGirl.setAdolescent_age(cur.getString(cur.getColumnIndex("adolescent_age")));
                        /*AdoGirl.setAdolescent_id(cur.getInt(cur
                                .getColumnIndex("adolescent_id")));
                        AdoGirl.setHhId(cur.getString(cur
                                .getColumnIndex("house_number")));
                        AdoGirl.setNameOfTheGirl(cur.getString(cur
                                .getColumnIndex("girl_name")));
                        AdoGirl.setGirlFatherName(cur.getString(cur
                                .getColumnIndex("father_name")));

                        AdoGirl.setGirlHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        AdoGirl.setGirlWeight(cur.getString(cur
                                .getColumnIndex("weight")));
                        AdoGirl.setDateOfBirth(cur.getString(cur
                                .getColumnIndex("date_of_birth")));
                        AdoGirl.setGirlHb(cur.getString(cur
                                .getColumnIndex("HB")));

                        AdoGirl.setChronicDisease(cur.getString(cur
                                .getColumnIndex("chronical_id")));
                        AdoGirl.setospYear(cur.getString(cur
                                .getColumnIndex("osp_year")));
                        AdoGirl.setospMonth(cur.getString(cur
                                .getColumnIndex("osp_month")));
                        AdoGirl.setImage(cur.getString(cur
                                .getColumnIndex("img")));*/

                        AdoGirlList.add(AdoGirl);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return AdoGirlList;
    }

    public ArrayList<AdolescentMonitoring> getAdolescentGirlMonitor() {
        // TODO Auto-generated method stub
        ArrayList<AdolescentMonitoring> adolescentMonitoring = new ArrayList<AdolescentMonitoring>();
        mydb.openDataBase();
        DB = mydb.getDb();
        AdolescentMonitoring adolescentMon;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            // String query =
            // "select girl_id, weight, height, hb, date_of_recording from adolecent_monitor where status=0 and anganwadi_center_id="
            // +user_id;
            //
        /*Cursor cur = DB
                .rawQuery(
                        "select marked_as, status, adolescent_id, weight, height, hb, migration_status, bp, " +
                                "date_of_recording,death_date,monitor_id, img from "
                                + "adolescent_monitoring where (status='0' or status='2') and anganwadi_center_id="
                                + user_id + " and user_master_id=" + user_master_id, null);*/
            Cursor cur = DB
                    .rawQuery(
                            "select adolescent_nutrition_id,adolescent_id,weight,marked_as,hb,migration_type," +
                                    "date_of_recording,height,version_code,anganwadi_center_id,user_master_id, is_adolescent_access_icds" +
                                    ",state_id,is_adolescent_rececive_ifa,is_taken_ifa, is_adolescent_dewarming_tablet, is_health_service, district_id,block_id,village_id,created_on_mobile,mobile_unique_id,consumption_of_ifa,deworming_done,periods,img from "
                                    + "adolescent_monitoring  where (status='0' or status='2') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);
            // Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {//8881992256
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        adolescentMon = new AdolescentMonitoring();
                        adolescentMon.setAdolescent_nutrition_id(cur.getString(cur.getColumnIndex("adolescent_nutrition_id")));
                        adolescentMon.setAdolescent_id(cur.getString(cur.getColumnIndex("adolescent_id")));
                        adolescentMon.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        adolescentMon.setMarked_as(cur.getString(cur.getColumnIndex("marked_as")));
                        adolescentMon.setHb(cur.getString(cur.getColumnIndex("hb")));
                        adolescentMon.setMigration_type(cur.getString(cur.getColumnIndex("migration_type")));
                        adolescentMon.setDate_of_recording(cur.getString(cur.getColumnIndex("date_of_recording")));
                        adolescentMon.setHeight(cur.getString(cur.getColumnIndex("height")));
                        adolescentMon.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        adolescentMon.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        adolescentMon.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        adolescentMon.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        adolescentMon.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        adolescentMon.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        adolescentMon.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        adolescentMon.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        adolescentMon.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        adolescentMon.setPeriods(cur.getString(cur.getColumnIndex("periods")));
                        adolescentMon.setIs_adolescent_access_icds(cur.getString(cur.getColumnIndex("is_adolescent_access_icds")));
                        adolescentMon.setIs_adolescent_rececive_ifa(cur.getString(cur.getColumnIndex("is_adolescent_rececive_ifa")));
                        adolescentMon.setIs_taken_ifa(cur.getString(cur.getColumnIndex("is_taken_ifa")));
                        adolescentMon.setIs_adolescent_dewarming_tablet(cur.getString(cur.getColumnIndex("is_adolescent_dewarming_tablet")));
                        adolescentMon.setIs_health_service(cur.getString(cur.getColumnIndex("is_health_service")));
                        adolescentMon.setConsumption_of_ifa(cur.getString(cur.getColumnIndex("consumption_of_ifa")));
                        adolescentMon.setDeworming_done(cur.getString(cur.getColumnIndex("deworming_done")));
                        adolescentMon.setImage(cur.getString(cur.getColumnIndex("img")));
                    /*adolescentMon.setMarked_as(cur.getString(cur.getColumnIndex("marked_as")));
                    adolescentMon.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));
                    adolescentMon.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));
                    adolescentMon.setAdolescentGirlID(cur.getInt(cur
                            .getColumnIndex("adolescent_id")));
                    adolescentMon.setAdolescentHeight(cur.getString(cur
                            .getColumnIndex("height")));
                    adolescentMon.setAdolescentWeight(cur.getString(cur
                            .getColumnIndex("weight")));
                    adolescentMon.setAdolescentHb(cur.getString(cur
                            .getColumnIndex("hb")));
                    adolescentMon.setMgrStatus(cur.getString(cur.getColumnIndex("migration_status")));
                    adolescentMon.setDateOfRecord(cur.getString(cur
                            .getColumnIndex("date_of_recording")));
                    adolescentMon.setMonitorID(cur.getInt(cur
                            .getColumnIndex("monitor_id")));
                    adolescentMon.setAdolescentBp(cur.getString(cur
                            .getColumnIndex("bp")));
                    adolescentMon.setImage(cur.getString(cur
                            .getColumnIndex("img")));
                    adolescentMon.setDeath_date(cur.getString(cur
                            .getColumnIndex("death_date")));*/
                        adolescentMonitoring.add(adolescentMon);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return adolescentMonitoring;
    }

    public String[] GetPendingDataForUploading() {
        String[] mydata = new String[15];
        mydb.openDataBase();
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery("select * from anganwadi_center where status = 0 or status = 2 and center_id = " + user_id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mydata[0] = cur.getString(cur.getColumnIndex("center_id"));
                        mydata[1] = cur.getString(cur.getColumnIndex("main_mini"));
                        mydata[2] = cur.getString(cur.getColumnIndex("kuchha_pukka"));
                        mydata[3] = cur.getString(cur.getColumnIndex("owned_rented"));
                        mydata[4] = cur.getString(cur.getColumnIndex("water_source"));
                        mydata[5] = cur.getString(cur.getColumnIndex("water_safety"));
                        mydata[6] = cur.getString(cur.getColumnIndex("toilet"));
                        mydata[7] = cur.getString(cur.getColumnIndex("water"));
                        mydata[8] = cur.getString(cur.getColumnIndex("hand_washing_facility"));
                        mydata[9] = cur.getString(cur.getColumnIndex("learning_teaching"));
                        mydata[10] = cur.getString(cur.getColumnIndex("equipment"));
                        mydata[11] = cur.getString(cur.getColumnIndex("outside_awc_img"));
                        mydata[12] = cur.getString(cur.getColumnIndex("kitchen_img"));
                        mydata[13] = cur.getString(cur.getColumnIndex("toilet_img"));
                        mydata[14] = cur.getString(cur.getColumnIndex("hand_washing_img"));

                    } catch (Exception e) {
                        Log.e("..........................", e.toString());
                    }

                }
            }
        }
        return mydata;

    }

    public String[] GetPendingDataForWorkerUploading() {
        String[] mydata = new String[29];
        mydb.openDataBase();
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery("select * from anganwadi_center where status = 1 or status = 2 and center_id = " + user_id, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mydata[0] = Integer.toString(user_id);
                        mydata[1] = cur.getString(cur.getColumnIndex("awc_worker_position"));
                        mydata[2] = cur.getString(cur.getColumnIndex("temporary_in_charge"));
                        mydata[3] = cur.getString(cur.getColumnIndex("which_awc"));
                        mydata[4] = cur.getString(cur.getColumnIndex("worker_name"));
                        mydata[5] = cur.getString(cur.getColumnIndex("worker_religion"));
                        mydata[6] = cur.getString(cur.getColumnIndex("worker_cc"));
                        mydata[7] = cur.getString(cur.getColumnIndex("worker_cast"));
                        mydata[8] = cur.getString(cur.getColumnIndex("worker_dob"));
                        mydata[9] = cur.getString(cur.getColumnIndex("worker_doj"));
                        mydata[10] = cur.getString(cur.getColumnIndex("worker_qualification"));
                        mydata[11] = cur.getString(cur.getColumnIndex("worker_training"));
                        mydata[12] = cur.getString(cur.getColumnIndex("worker_residance"));
                        mydata[13] = cur.getString(cur.getColumnIndex("worker_distance_awc"));
                        mydata[14] = cur.getString(cur.getColumnIndex("worker_mobile"));
                        mydata[15] = cur.getString(cur.getColumnIndex("worker_alt_mobile"));
                        mydata[16] = cur.getString(cur.getColumnIndex("worker_img1"));
                        mydata[17] = cur.getString(cur.getColumnIndex("worker_img2"));
                        mydata[18] = cur.getString(cur.getColumnIndex("helper_name"));
                        mydata[19] = cur.getString(cur.getColumnIndex("helper_dob"));
                        mydata[20] = cur.getString(cur.getColumnIndex("helper_doj"));
                        mydata[21] = cur.getString(cur.getColumnIndex("helper_qualification"));
                        mydata[22] = cur.getString(cur.getColumnIndex("helper_training"));
                        mydata[23] = cur.getString(cur.getColumnIndex("helper_residance"));
                        mydata[24] = cur.getString(cur.getColumnIndex("helper_distance_awc"));
                        mydata[25] = cur.getString(cur.getColumnIndex("helper_mobile"));
                        mydata[26] = cur.getString(cur.getColumnIndex("helper_alt_mobile"));
                        mydata[27] = cur.getString(cur.getColumnIndex("helper_img1"));
                        mydata[28] = cur.getString(cur.getColumnIndex("helper_img2"));

                    } catch (Exception e) {
                        Log.e("..........................", e.toString());
                    }

                }
            }
        }
        return mydata;

    }

    public void deleteAllDataFromTable(String tablename, int status) {
        // TODO Auto-generated method stub
        mydb.openDataBase();
        DB = mydb.getDb();
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            // DB.delete(tablename, null, null);
            DB.delete(tablename, "status=?",
                    new String[]{Integer.toString(status)});
        }
    }

    public int checkServerId(String tablename, String houseId) {
        // TODO Auto-generated method stub
        int serverId = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select server_id from " + tablename
                + " where house_number =" + houseId;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    serverId = cur.getInt(cur.getColumnIndex("server_id"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return serverId;

    }

    public int checkWomenServerId(String id) {
        // TODO Auto-generated method stub
        int serverId = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select server_id from pregnant_women where pregnant_women_id ="
                + id;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    serverId = cur.getInt(cur.getColumnIndex("server_id"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return serverId;

    }

    public int checkChildServerId(int id) {
        // TODO Auto-generated method stub
        int serverId = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select server_id from child where child_id ="
                + id;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    serverId = cur.getInt(cur.getColumnIndex("server_id"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return serverId;

    }

    public int getChilddId(String name) {
        // TODO Auto-generated method stub
        int serverId = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select child_id from child where name_of_child = " + "'"
                + name + "'";
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    serverId = cur.getInt(cur.getColumnIndex("name_of_child"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return serverId;

    }

    public int checkAdolescentServerId(int id) {
        // TODO Auto-generated method stub
        int serverId = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select server_id from adolescent where adolescent_id ="
                + id;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    serverId = cur.getInt(cur.getColumnIndex("server_id"));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return serverId;

    }

    public ArrayList<Views> monitoringDateSearch(String tbl_name,
                                                 String col_name, String col_value) throws SQLException {

        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "";

        ArrayList<Views> dateOfRecording = new ArrayList<Views>();
        query = "select date_of_recording from " + tbl_name + " where "
                + col_name + "=" + col_value;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        Views view = new Views();
                        view.setDate(cur.getString(cur
                                .getColumnIndex("date_of_recording")));

                        dateOfRecording.add(view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dateOfRecording;
    }

    // parents downloaded or not
    public boolean CheckEligibleFamilyDownload(String house_number,
                                               String house_hold, int anganwadi_id) throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from eligible_family where house_number=" + house_number
                + " and head_family=" + "'" + house_hold + "'"
                + " and anganwadi_center_id=" + anganwadi_id;

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // ckeck women downloaded or not
    public boolean CheckWomenDownload(String house_number, String serverId,
                                      String name, int anganwadi_id) throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from pregnant_women where house_number='"
                + house_number + "' and server_id=" + serverId
                + " and anganwadi_center_id=" + anganwadi_id
                + " and name_of_pregnant_women=" + "'" + name + "'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // ckeck women downloaded or not
    public boolean CheckWomenMonitorDownload(String server_id,
                                             String date_of_recording) throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from pregnant_womem_monitor where server_id="
                + server_id + " and date_of_recording=" + "'"
                + date_of_recording + "'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // check child downloaded or not
    public boolean CheckChildDownload(String name_of_child, String server_id)
            throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from child where name_of_child=" + "'"
                + name_of_child + "'" + " and server_id=" + server_id;

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // check women downloaded or not
    public boolean CheckChildMonitorDownload(String server_id,
                                             String date_of_recording) throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from child_nutrition_monitoring where server_id="
                + server_id + " and date_of_recording=" + "'"
                + date_of_recording + "'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // check adolescent downloaded or not
    public boolean CheckAdolescentDownload(String server_id, String house_number)
            throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from adolescent where server_id=" + server_id
                + " and house_number=" + "'" + house_number + "'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    // check adolescent downloaded or not
    public boolean CheckAdolescentMonDownload(String server_id,
                                              String date_of_recording) throws SQLException {

        mydb.openDataBase();
        long id = 0;
        int c = 0;
        DB = mydb.getDb();
        String query = "";

        query = "select * from adolescent_monitoring where server_id=" + server_id
                + " and date_of_recording=" + "'" + date_of_recording + "'";

        Cursor cur = DB.rawQuery(query, null);
        if (cur != null) {
            if (cur.getCount() > 0) {
                return true;
            }
        }
        return false;

    }

    public String[] getEligibleFamilyHouseNumber() {

        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String house;


        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            try {
                cur = DB.rawQuery("select house_number from eligible_family", null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }

        String[] house_number = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                house = cur.getString(cur.getColumnIndex("house_number"));
                house_number[i] = house;
                i++;

            }
        }

        return house_number;
    }
    // down_preg

    public String[] getPregWomenServerId() {

        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            try {
                cur = DB.rawQuery(
                        "select server_id from pregnant_women where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }

        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                id = cur.getString(cur.getColumnIndex("server_id"));
                serverId[i] = id;
                i++;

            }
        }
        return serverId;
    }

    // down_preg_mon

    public String[] getPregWomenMonitoringServerId() {

        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            try {
                cur = DB.rawQuery(
                        "select server_monitoring_id from pregnant_womem_monitor where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }

        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                id = cur.getString(cur.getColumnIndex("server_monitoring_id"));
                serverId[i] = id;
                i++;

            }
        }
        return serverId;
    }

    public String getWomenId(String serverId) {

        String id = "";
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select pregnant_women_id from pregnant_women where server_id ="
                + serverId;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    id = cur.getString(cur.getColumnIndex("pregnant_women_id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return id;

    }

    // down_child

    public String[] DownloadChildServerId() {

        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            try {
                cur = DB.rawQuery(
                        "select server_id from child where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }

        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                id = cur.getString(cur.getColumnIndex("server_id"));
                serverId[i] = id;
                i++;

            }
        }
        return serverId;
    }


    public String[] DownloadChildMon() {
        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String id;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            try {
                cur = DB.rawQuery(
                        "select server_nutrition_id from child_nutrition_monitoring where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }

        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                id = cur.getString(cur.getColumnIndex("server_nutrition_id"));
                serverId[i] = id;
                i++;
            }
        }
        return serverId;
    }

    public String getSevikaHelper(String curDate) {
        String b = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select ctr from sevika_helper_status where date_of_status='" + curDate + "' and user_id=" + user_master_id + " and anganwadi_id=" + user_id, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        b = cur.getString(cur.getColumnIndex("ctr"));
                    }
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }


    public String getChildMoniterCount(String curDate, String year) {
        String b = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select count(child_id) from child_nutrition_monitoring where strftime('%m',date_of_recording) ='" + curDate + "' and strftime('%Y',date_of_recording) ='" + year + "'  and anganwadi_center_id = " + user_id + "", null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        b = cur.getString(cur.getColumnIndex("count(child_id)"));
                    }
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }

    public String getPregnantWomenMoniterCount(String curDate, String year) {
        String b = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select count(women_id) from pregnant_womem_monitor where strftime('%m',date_of_recording) ='" + curDate + "' and strftime('%Y',date_of_recording) ='" + year + "'  and anganwadi_center_id = " + user_id + "", null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        b = cur.getString(cur.getColumnIndex("count(women_id)"));
                    }
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }

    public String getAdolescentMoniterCount(String curDate, String year) {
        String b = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select count(adolescent_id) from adolescent_monitoring where strftime('%m',date_of_recording) ='" + curDate + "' and strftime('%Y',date_of_recording) ='" + year + "'  and anganwadi_center_id = " + user_id + "", null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        b = cur.getString(cur.getColumnIndex("count(adolescent_id)"));
                    }
                }
            } catch (SQLException e) {
            }
        }
        return b;
    }


    public void setSevikaHelper(String[] atten) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", user_master_id);
                values.put("anganwadi_id", user_id);
                values.put("date_of_status", atten[0]);
                values.put("sevika_status", atten[1]);
                values.put("helper_status", atten[2]);
                values.put("app_version", GlobalVars.App_Version);
                values.put("lattitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", 0);

                DB.insert("sevika_helper_status", null, values);
            }
        } catch (Exception s) {

        }
    }

    public void setInUserDateTime(attendance att) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("user_id", att.getUserID());
                values.put("date", att.getDate());
                values.put("in_time", att.getinTime());
                values.put("out_time", "00:00:00");
                values.put("anganwadi_id", att.getAnganwadiID());
                values.put("in_latitude", GlobalVars.lattitude);
                values.put("in_longitude", GlobalVars.longitude);
                values.put("out_latitude", "00.0000");
                values.put("out_longitude", "00.0000");
                values.put("status", 0);
                values.put("app_version", GlobalVars.App_Version);

                GlobalVars.LoginCount = DB.insert("users_timing", null, values);
            }
        } catch (Exception s) {

        }
    }

    public void setoutUserDateTime(String useroutdetails) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                int status = 0;
                String query = "select status from users_timing where ctr = " + GlobalVars.LoginCount + "";
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            status = cur.getInt(cur.getColumnIndex("status"));
                        } catch (Exception e) {
                        }
                    }
                }
                if (status == 5) {
                    status = 1;
                }
                ContentValues values = new ContentValues();
                values.put("out_time", useroutdetails);
                values.put("out_latitude", GlobalVars.lattitude);
                values.put("out_longitude", GlobalVars.longitude);
                values.put("status", status);
                DB.update("users_timing", values, "ctr = '" + GlobalVars.LoginCount + "'", null);
            }
        } catch (Exception s) {

        }
    }

    public ArrayList<attendance> getPendingAttendance() {
        ArrayList<attendance> attendances = new ArrayList<attendance>();
        attendance atten;
        try {
            mydb.openDataBase();
            DB = mydb.getDb();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            String query = "select ctr, user_id, date, in_time, out_time, anganwadi_id, in_latitude, in_longitude, out_latitude, out_longitude, status, server_id, app_version from users_timing where status = 0 or status = 1";
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        atten = new attendance();
                        atten.setCount(cur.getString(cur.getColumnIndex("ctr")));
                        atten.setUserID(cur.getString(cur.getColumnIndex("user_id")));
                        atten.setDate(cur.getString(cur.getColumnIndex("date")));
                        atten.setinTime(cur.getString(cur.getColumnIndex("in_time")));
                        atten.setoutTime(cur.getString(cur.getColumnIndex("out_time")));
                        atten.setAnganwadiID(cur.getString(cur.getColumnIndex("anganwadi_id")));
                        atten.setinLatitude(cur.getString(cur.getColumnIndex("in_latitude")));
                        atten.setinLongitude(cur.getString(cur.getColumnIndex("in_longitude")));
                        atten.setoutLatitude(cur.getString(cur.getColumnIndex("out_latitude")));
                        atten.setoutLongitude(cur.getString(cur.getColumnIndex("out_longitude")));
                        atten.setStatus(cur.getInt(cur.getColumnIndex("status")));
                        atten.setServerID(cur.getString(cur.getColumnIndex("server_id")));
                        attendances.add(atten);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return attendances;
    }

    public ArrayList<sevika_helper> getPendingSevikaHelper() {
        ArrayList<sevika_helper> attendances = new ArrayList<sevika_helper>();
        sevika_helper atten;

        mydb.openDataBase();
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            String query = "select * from sevika_helper_status where status = 0";
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        atten = new sevika_helper();
                        atten.setctr(cur.getString(cur.getColumnIndex("ctr")));
                        atten.setUserid(cur.getString(cur.getColumnIndex("user_id")));
                        atten.setanganwadiid(cur.getString(cur.getColumnIndex("anganwadi_id")));
                        atten.setdateofstatus(cur.getString(cur.getColumnIndex("date_of_status")));
                        atten.setsevikastatus(cur.getString(cur.getColumnIndex("sevika_status")));
                        atten.sethelperstatus(cur.getString(cur.getColumnIndex("helper_status")));
                        atten.setlattitude(cur.getString(cur.getColumnIndex("lattitude")));
                        atten.setlongitude(cur.getString(cur.getColumnIndex("longitude")));
                        atten.setappversion(cur.getString(cur.getColumnIndex("app_version")));
                        attendances.add(atten);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return attendances;
    }


    // get child_local_id
    public String getChildId(String serverId) {

        String id = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select child_id from child where server_id ="
                + serverId;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    id = cur.getString(cur.getColumnIndex("child_id"));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        DB.close();
        return id;

    }

    public String[] DownloadAdolecentGirl() {
        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String girlId;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery(
                        "select server_id from adolescent where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                girlId = cur.getString(cur.getColumnIndex("server_id"));
                serverId[i] = girlId;
                i++;
            }
        }
        return serverId;
    }

    public String[] DownloadAdolecentGirlMon() {
        int i = 0;
        DB = mydb.getDb();
        Cursor cur = null;
        String girlId;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery(
                        "select girl_nutrition_server_id from adolescent_monitoring where anganwadi_center_id="
                                + user_id, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                girlId = cur.getString(cur.getColumnIndex("girl_nutrition_server_id"));
                serverId[i] = girlId;
                i++;
            }
        }
        return serverId;
    }

    // get child_local_id
    public int getAdolescentId(String serverId) {

        int id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select adolescent_id from adolescent where server_id ="
                + serverId;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    id = cur.getInt(cur.getColumnIndex("adolescent_id"));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        return id;

    }


    // get child date of birth
    public String getBirthDate(String child_id) {

        String date = "";
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select date_of_birth from child where child_id ="
                + child_id;
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    date = cur.getString(cur.getColumnIndex("date_of_birth"));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        return date;

    }

    public ArrayList<String> getDreason() {

        ArrayList<String> reas = new ArrayList<String>();
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select d_reason from disease_reason";
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                try {
                    reas.add(cur.getString(cur.getColumnIndex("d_reason")));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        return reas;
    }

    //down anganawdi
    @SuppressLint("SimpleDateFormat")
    public long AnganwadiDownload(String[] ASDF) {


        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("center_id", ASDF[0]);
                values.put("center_name", ASDF[1]);
                values.put("user_master_id", user_master_id);
                values.put("beat_id", ASDF[26]);
                values.put("status", 5);
                /*values.put("center_id", ASDF[0]);
                values.put("center_name", ASDF[1]);
                values.put("user_master_id", user_master_id);
                values.put("main_mini", ASDF[2]);
                values.put("kuchha_pukka", ASDF[3]);
                values.put("owned_rented", ASDF[4]);
                values.put("water_source", ASDF[5]);
                values.put("water_safety", ASDF[6]);
                values.put("toilet", ASDF[7]);
                values.put("water", ASDF[8]);
                values.put("hand_washing_facility", ASDF[9]);
                values.put("learning_teaching", ASDF[10]);
                values.put("equipment", ASDF[11]);
                values.put("lattitude", ASDF[12]);
                values.put("longitude", ASDF[13]);
                values.put("outside_awc_img", ASDF[14]);
                values.put("kitchen_img", ASDF[15]);
                values.put("toilet_img", ASDF[16]);
                values.put("hand_washing_img", ASDF[17]);
                values.put("worker_name", ASDF[18]);
                values.put("worker_mobile", ASDF[19]);
                values.put("worker_alt_mobile", ASDF[20]);
                values.put("worker_dob", ASDF[21].substring(8, 10) + "-" + ASDF[21].substring(5, 7) + "-" + ASDF[21].substring(0, 4));
                values.put("worker_doj", ASDF[22].substring(8, 10) + "-" + ASDF[22].substring(5, 7) + "-" + ASDF[22].substring(0, 4));
                values.put("worker_qualification", ASDF[23]);
                values.put("awc_worker_position", ASDF[24]);
                values.put("temporary_in_charge", ASDF[25]);
                values.put("which_awc", ASDF[26]);
                values.put("worker_cast", ASDF[27]);
                values.put("worker_religion", ASDF[28]);
                values.put("worker_training", ASDF[29]);
                values.put("worker_residance", ASDF[30]);
                values.put("worker_distance_awc", ASDF[31]);
                values.put("worker_img1", ASDF[32]);
                values.put("worker_img2", ASDF[33]);
                values.put("helper_name", ASDF[34]);
                values.put("helper_img2", ASDF[35]);
                values.put("helper_img1", ASDF[36]);
                values.put("helper_mobile", ASDF[37]);
                values.put("helper_alt_mobile", ASDF[38]);
                values.put("helper_dob", ASDF[39].substring(8, 10) + "-" + ASDF[39].substring(5, 7) + "-" + ASDF[39].substring(0, 4));
                values.put("helper_doj", ASDF[40].substring(8, 10) + "-" + ASDF[40].substring(5, 7) + "-" + ASDF[40].substring(0, 4));
                values.put("helper_qualification", ASDF[41]);
                values.put("helper_training", ASDF[42]);
                values.put("helper_residance", ASDF[43]);
                values.put("helper_distance_awc", ASDF[44]);
                values.put("worker_cc", ASDF[45]);
                values.put("beat_id", ASDF[46]);
                values.put("status", 5);*/
                id = DB.insert("anganwadi_center", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {
            s.printStackTrace();
        }

        return id;

    }

    //download State
    @SuppressLint("SimpleDateFormat")
    public long StateDownload(String[] ASDF) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("state_id", ASDF[0]);
                values.put("state_name", ASDF[1]);
                values.put("status", 5);

                id = DB.insert("state", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {
            s.printStackTrace();
        }

        return id;

    }

    public void SaveUpdateEF(Parent parent, String strFid) {
        // Update Eligible family
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                ContentValues values = new ContentValues();
                values.put("house_number", parent.getHouseNo());
                values.put("head_family", parent.getHeadofHH());
                values.put("adhaar_card", parent.getAadharCardHH());
                values.put("mobile_number", parent.getMobileHH());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("toilet_type", parent.getHas_toilet());
                values.put("water_source", parent.getHave_water());
                values.put("server_id", parent.getServer_id());
                values.put("religion_id", parent.getReligion());
                values.put("cast_category", parent.getIntcastecat());
                values.put("cast_id", parent.getCast_id());
                values.put("gender", parent.getIntGender());
                values.put("status", 5);
                values.put("land_hold", parent.getLan_value());
                values.put("seasonal_migration", parent.getSes_migration());
                values.put("latitude", parent.getLatitude());
                values.put("longitude", parent.getLongitude());
                values.put("img", parent.getImage());

                id = DB.update("eligible_family", values, "familyid=" + strFid, null);

                Log.d("Eligible family Updation : data is updated successfully", id + "");
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }

    }

    public String[] GetData(String colName, String tabName, String whereCon) {
        int i = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        String id;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select " + colName + " from " + tabName + " where " + whereCon, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                id = cur.getString(0);
                serverId[i] = id;
                i++;
            }
        }
        DB.close();
        return serverId;
    }

    public String GetOneData(String colName, String tabName, String whereCon) {
        int i = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        String id = "";
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                Log.e("................................", "select " + colName + " from " + tabName + " where " + whereCon);
                cur = DB.rawQuery("select " + colName + " from " + tabName + " where " + whereCon, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                id = cur.getString(0);
            }
        }
        DB.close();
        return id;
    }

    public String[] GetManyData(String colName, String tabName, String whereCon) {
        int i = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        String id;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select " + colName + " from " + tabName + " where " + whereCon, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        String[] serverId = new String[cur.getCount()];
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                id = cur.getString(0);
                serverId[i] = id;
                i++;
            }
        }
        DB.close();
        return serverId;
    }

    public void SaveAwcDetails(String[] awcarr) {
        String sta = GetOneData("status", "anganwadi_center", "center_id=" + user_id);
        if (Integer.parseInt(sta) == 5) {
            sta = "0";
        }
        if (Integer.parseInt(sta) == 1) {
            sta = "2";
        }

        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("main_mini", awcarr[0]);
                values.put("kuchha_pukka", awcarr[1]);
                values.put("owned_rented", awcarr[2]);
                values.put("water_source", awcarr[3]);
                values.put("water_safety", awcarr[4]);
                values.put("toilet", awcarr[5]);
                values.put("water", awcarr[6]);
                values.put("hand_washing_facility", awcarr[7]);
                values.put("learning_teaching", awcarr[8]);
                values.put("equipment", awcarr[9]);
                values.put("outside_awc_img", awcarr[10]);
                values.put("kitchen_img", awcarr[11]);
                values.put("toilet_img", awcarr[12]);
                values.put("hand_washing_img", awcarr[13]);
                values.put("lattitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", sta);

                id = DB.update("anganwadi_center", values, "center_id=" + user_id, null);
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
    }

    public void SaveWorkerDetails(String[] awcarr) {
        String sta = GetOneData("status", "anganwadi_center", "center_id=" + user_id);
        if (Integer.parseInt(sta) == 5) {
            sta = "1";
        }
        if (Integer.parseInt(sta) == 0) {
            sta = "2";
        }
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("awc_worker_position", awcarr[0]);
                values.put("temporary_in_charge", awcarr[1]);
                values.put("which_awc", awcarr[2]);
                values.put("worker_name", awcarr[3]);
                values.put("worker_religion", awcarr[4]);
                values.put("worker_cc", awcarr[5]);
                values.put("worker_cast", awcarr[6]);
                values.put("worker_dob", awcarr[7]);
                values.put("worker_doj", awcarr[8]);
                values.put("worker_qualification", awcarr[9]);
                values.put("worker_training", awcarr[10]);
                values.put("worker_residance", awcarr[11]);
                values.put("worker_distance_awc", awcarr[12]);
                values.put("worker_mobile", awcarr[13]);
                values.put("worker_alt_mobile", awcarr[14]);
                values.put("worker_img1", awcarr[15]);
                values.put("worker_img2", awcarr[16]);
                values.put("helper_name", awcarr[17]);
                values.put("helper_dob", awcarr[18]);
                values.put("helper_doj", awcarr[19]);
                values.put("helper_training", awcarr[20]);
                values.put("helper_residance", awcarr[21]);
                values.put("helper_distance_awc", awcarr[22]);
                values.put("helper_mobile", awcarr[23]);
                values.put("helper_alt_mobile", awcarr[24]);
                values.put("helper_img1", awcarr[25]);
                values.put("helper_img2", awcarr[26]);
                values.put("status", sta);

                id = DB.update("anganwadi_center", values, "center_id=" + user_id, null);
            }
        } catch (Exception s) {
            //Log.d("ChildNutritionMonitor", s.getMessage());
        }
    }

    public ArrayList<String> getSickList() {
        int i = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        ArrayList<String> sicklst = new ArrayList<String>();
        sicklst.add("Select Sickness");
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select sick_id, sick_name from sickness order by sick_id asc", null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                sicklst.add(cur.getString(1));
            }
        }
        DB.close();
        return sicklst;
    }

    public int getIdOfSickList(String sick_name) {

        int id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select sick_id from sickness where sick_name = '"
                + sick_name + "'";
        Cursor cur = DB.rawQuery(query, null);
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {

                try {
                    id = cur.getInt(cur.getColumnIndex("sick_id"));
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }

        return id;

    }


    public ArrayList<Double> zScoreValue(String table_name, int age_in_month) {
        ArrayList<Double> a_h = new ArrayList<Double>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                Log.e("............", "select * from " + table_name + " where IMONTH = " + age_in_month);
                cur = DB.rawQuery("select * from " + table_name + " where IMONTH = " + age_in_month, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                a_h.add(cur.getDouble(2));
                a_h.add(cur.getDouble(3));
                a_h.add(cur.getDouble(4));
            }
        }
        DB.close();
        return a_h;
    }

    public ArrayList<ChildNutrition> getChildPrevious(int child_id) {
        ArrayList<ChildNutrition> childMonitor = new ArrayList<ChildNutrition>();
        mydb.openDataBase();
        DB = mydb.getDb();
        ChildNutrition childNutration;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query = "select height, date_of_recording from child_nutrition_monitoring where child_id="
                    + child_id + " and height !='' and height != 'NULL' and height !='Private school' and height !='Absent' and height !='Temporary Migrate' " +
                    "and height !='On-farm' and height !='Permanent Migrate' and height !='Death' order by date_of_recording desc limit 1";
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        childNutration = new ChildNutrition();
                        childNutration.setHeight(cur.getString(cur
                                .getColumnIndex("height")));
                        childNutration.setDate_of_monitoring(cur.getString(cur
                                .getColumnIndex("date_of_recording")));

                        childMonitor.add(childNutration);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return childMonitor;
    }


    public ArrayList<ChildNutrition> getMonitoringData(int user_id) {
        ArrayList<ChildNutrition> data = new ArrayList<>();
        mydb.openDataBase();
        DB = mydb.getDb();


        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            //   String query = "select child_id ,server_id, date_of_birth, name_of_child from child_registration where anganwadi_center_id= " + user_id + " and child_id not in (select child_id from Child_Nutrition_Monitor where migration_type = 'Death') order by name_of_child";
//            String query = "select distinct c.name_of_child, c.server_id, c.child_id,c.date_of_birth, pr.name_of_pregnant_women from  child c, pregnant_women pr where c.pregnent_women_id = pr.pregnant_women_id and   c.anganwadi_center_id=pr.anganwadi_center_id and c.anganwadi_center_id=" + user_id + "";
            //String query = "select distinct c.name_of_child, c.server_id, c.child_id,c.date_of_birth, mo.name_of_mother from  child c, mother mo where c.pregnent_women_id = mo.mother_id and   c.anganwadi_center_id=mo.anganwadi_center_id and c.anganwadi_center_id=" + user_id + "";
//            String query = "select distinct c.name_of_child as name_of_child, c.server_id as server_id, c.child_id as child_id,c.date_of_birth as date_of_birth, mo.name_of_mother as name_of_mother from  child c, mother mo where c.pregnent_women_id = mo.mother_id and   c.anganwadi_center_id=mo.anganwadi_center_id and c.anganwadi_center_id=" + user_id + " UNION select distinct c.name_of_child as name_of_child, c.server_id as server_id, c.child_id as child_id,c.date_of_birth as date_of_birth, mo.name_of_mother as name_of_mother from  child c, mother mo where c.pregnent_women_id = mo.mother_id and   c.anganwadi_center_id=mo.anganwadi_center_id and c.anganwadi_center_id=" + user_id + "";
            String query = "select distinct c.name_of_child as name_of_child, c.server_id as server_id, c.child_id as child_id,c.date_of_birth as date_of_birth, mo.name_of_mother as name_of_mother from child as c INNER JOIN mother as mo ON c.pregnent_women_id = mo.mother_id and c.anganwadi_center_id=mo.anganwadi_center_id and c.selected_women_type='EW' and  c.anganwadi_center_id=" + user_id + " UNION select distinct c.name_of_child as name_of_child, c.server_id as server_id, c.child_id as child_id,c.date_of_birth as date_of_birth, pr.name_of_pregnant_women as name_of_mother from child as c INNER JOIN pregnant_women as pr ON c.pregnent_women_id = pr.pregnant_women_id and c.anganwadi_center_id=pr.anganwadi_center_id and c.selected_women_type='PW' and c.anganwadi_center_id=" + user_id + "";
//           String query = "select cr.child_id ,cr.server_id,cr.name_of_child from Child_Nutrition_Monitor cn join child_registration cr on cn.child_id = cr.child_id where cn.anganwadi_center_id = "+user_id+" and cn.migration_type != 'Death' and cn.child_id in (select child_id from child_registration cr) group by cr.name_of_child";

            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        String date = cur.getString(cur.getColumnIndex("date_of_birth"));

                        Calendar dob = Calendar.getInstance();
                        Calendar today = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            Date date1 = format.parse(date);
                            dob.setTime(date1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        int year = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                        int month = 0, totalDaysDifference = 0;
                        if (today.get(Calendar.MONTH) >= dob.get(Calendar.MONTH)) {
                            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
                        } else {
                            month = today.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
                            month = 12 + month;
                            year--;
                        }

                        if (today.get(Calendar.DAY_OF_MONTH) >= dob.get(Calendar.DAY_OF_MONTH)) {
                            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
                        } else {
                            totalDaysDifference = today.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);
                            totalDaysDifference = 30 + totalDaysDifference;
                            month--;
                        }

                        double age = (year * 12) + month;
                        Integer ageInt = (int) age;

                        if (ageInt <= 72) {
                            ChildNutrition childNutrition = new ChildNutrition();
                            childNutrition.setServer_id(cur.getInt(cur.getColumnIndex("server_id")));
                            childNutrition.setChild_id(cur.getInt(cur.getColumnIndex("child_id")));
                            childNutrition.setName_of_child(cur.getString(cur.getColumnIndex("name_of_child")) + "(" + cur.getString(cur.getColumnIndex("name_of_mother")) + ")");
                            data.add(childNutrition);
                        }

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }

    public int getChildMonitoredStatus(int child_id) {

        String query = "select server_id from child_nutrition_monitoring where child_id = " + child_id + " order by date_of_recording desc limit 1";

        int id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {
                            id = cur.getInt(0);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;


    }

    public ChildNutrition getChildStatusData(int child_id) {

        String query = "select server_id,migration_type,substr(date_of_recording,0,8 ) from child_nutrition_monitoring where child_id = " + child_id + " order by date_of_recording desc limit 1";


        mydb.openDataBase();
        DB = mydb.getDb();

        ChildNutrition childNutrition = new ChildNutrition();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.move(0);
                    while (cur.moveToNext()) {
                        try {

                            childNutrition.setServer_id(cur.getInt(cur.getColumnIndex("server_id")));
                            childNutrition.setMigration(cur.getString(cur.getColumnIndex("migration_type")));
                            childNutrition.setDateCheck(cur.getString(cur.getColumnIndex("substr(date_of_recording,0,8 )")));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    cur.close();
                }

                DB.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return childNutrition;

    }

    public ArrayList<Child> getChildUnderweightHistory(String c_id) {
        ArrayList<Child> uw_history = new ArrayList<Child>();
		/*Child ch1 = new Child();
		ch1.setMyDate(GetOneData("date(dob)", "child_registration", "server_id = "+c_id));
		ch1.setWeight(GetOneData("weight", "child_registration", "server_id = "+c_id));
		uw_history.add(ch1); */
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select date((date_of_recording)), weight from child_nutrition_monitoring  where child_id = " + c_id + " and migration_type = '' order by date((date_of_recording)) asc", null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                Child ch = new Child();
                ch.setMyDate(cur.getString(0));
                ch.setWeight(cur.getString(1));
                uw_history.add(ch);
            }
        }
        DB.close();
        return uw_history;
    }

    public ArrayList<Child> getChildHeigthweightHistory(String c_id) {
        ArrayList<Child> hw_history = new ArrayList<Child>();
		/*Child ch1 = new Child();
		ch1.setMyDate(GetOneData("date(dob)", "child_registration", "server_id = "+c_id));
		ch1.setWeight(GetOneData("weight", "child_registration", "server_id = "+c_id));
		uw_history.add(ch1); */
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery("select date((date_of_recording)),weight,height from child_nutrition_monitoring  where child_id = " + c_id + " and migration_type = '' order by height asc", null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                Child ch = new Child();
                ch.setMyDate(cur.getString(0));
                ch.setWeight(cur.getString(1));
                ch.setHeight(cur.getString(2));
                hw_history.add(ch);
            }
        }
        DB.close();
        return hw_history;
    }

    public ArrayList<String> getIdealHeight2yearsBoys(String ideAl_weight_length_boys, String m3sd) {
        ArrayList<String> data = new ArrayList<>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cur = null;

        String query = "select " + m3sd + " from " + ideAl_weight_length_boys;
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            try {
                cur = DB.rawQuery(query, null);
            } catch (SQLException e) {
                //Log.d("DATABASE", e.toString());
            }
        }
        if (cur != null && cur.getCount() > 0) {
            cur.move(0);
            while (cur.moveToNext()) {
                Log.e("HEIGHT", cur.getString((cur.getColumnIndex("" + m3sd))));
                data.add(cur.getString((cur.getColumnIndex("" + m3sd))));

            }
        }
        DB.close();
        return data;

    }


    public long BeatDownload(String[] beat) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                //  dropTable("beat");
                ContentValues values = new ContentValues();
                values.put("beat_id", beat[0]);
                values.put("beat_name", beat[1]);
                values.put("user_master_id", user_master_id);
                values.put("project_id", beat[2]);
                values.put("taluka_id", beat[3]);
                values.put("district_id", beat[4]);
                values.put("state_id", beat[5]);
                id = DB.insert("beat", null, values);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {
            s.printStackTrace();
        }

        return id;

    }

    public long setAdolscentBaseline(AdolBaseline adolBaseline) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("adolscent_name", adolBaseline.getAdolName());
                values.put("address", adolBaseline.getAddress());
                values.put("parent_name", adolBaseline.getParentName());
                values.put("contact_number", adolBaseline.getContactNumber());
                values.put("height", adolBaseline.getHeight());
                values.put("weight", adolBaseline.getWeight());
                values.put("age", adolBaseline.getAge());
                values.put("muac", adolBaseline.getMUAC());
                values.put("school_name", adolBaseline.getSchoolName());
                values.put("class", adolBaseline.getClassName());
                values.put("unique_id", adolBaseline.getUniqueId());
                values.put("village_name", adolBaseline.getVillageName());
                values.put("block_name", adolBaseline.getBlockName());
                values.put("district_name", adolBaseline.getDistrictName());
                values.put("heard_of_aneamia", adolBaseline.getHeardOfAneamia());
                values.put("source_of_info", adolBaseline.getSourceOfInfo());
                values.put("what_is_aneamia", adolBaseline.getWhatIsAneamia());
                values.put("which_nutrient_def", adolBaseline.getWhichNutrientDef());
                values.put("cause_of_aneamia", adolBaseline.getCauseOfAneamia());
                values.put("signs_of_aneamia", adolBaseline.getSignsOfAneamia());
                values.put("effect_of_aneamia", adolBaseline.getEffectsOfAneamia());
                values.put("measures_of_aneamia", adolBaseline.getMeasuresOfAneamia());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", "0");


                id = DB.insert("adolscent_baseline", null, values);
            }
        } catch (Exception s) {

        }
        return id;
    }

    public String getLast_inserted_id(String table_name) {
        String id = "";
        mydb.openDataBase();
        DB = mydb.getDb();

        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

                String query = "select id from " + table_name + " order by id desc limit 0,1";
                ;

                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        id = (cursor.getString(cursor.getColumnIndex("id")));
                        cursor.moveToNext();
                    }
                }
            }
            DB.close();

        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return id;
    }

    public long updateAdolBaseline(AdolBaseline adolBaseline, AdolBaseline adolBaseline1) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();

                values.put("adolscent_name", adolBaseline1.getAdolName());
                values.put("address", adolBaseline1.getAddress());
                values.put("parent_name", adolBaseline1.getParentName());
                values.put("contact_number", adolBaseline1.getContactNumber());
                values.put("height", adolBaseline1.getHeight());
                values.put("weight", adolBaseline1.getWeight());
                values.put("age", adolBaseline1.getAge());
                values.put("muac", adolBaseline1.getMUAC());
                values.put("school_name", adolBaseline1.getSchoolName());
                values.put("class", adolBaseline1.getClassName());
                values.put("unique_id", adolBaseline1.getUniqueId());
                values.put("village_name", adolBaseline1.getVillageName());
                values.put("block_name", adolBaseline1.getBlockName());
                values.put("district_name", adolBaseline1.getDistrictName());
                values.put("heard_of_aneamia", adolBaseline1.getHeardOfAneamia());
                values.put("source_of_info", adolBaseline1.getSourceOfInfo());
                values.put("what_is_aneamia", adolBaseline1.getWhatIsAneamia());
                values.put("which_nutrient_def", adolBaseline1.getWhichNutrientDef());
                values.put("cause_of_aneamia", adolBaseline1.getCauseOfAneamia());
                values.put("signs_of_aneamia", adolBaseline1.getSignsOfAneamia());
                values.put("effect_of_aneamia", adolBaseline1.getEffectsOfAneamia());
                values.put("measures_of_aneamia", adolBaseline1.getMeasuresOfAneamia());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", "0");


                values.put("iron_rich_food", adolBaseline.getIronRichFood());
                values.put("more_iron_needs", adolBaseline.getMoreIronNeeds());
                values.put("you_are_aneamic", adolBaseline.getYouAreAneamic());
                values.put("how_serious_aneamia", adolBaseline.getHowSeriousAneamia());
                values.put("get_iron_tablet", adolBaseline.getGetIronTablet());
                values.put("how_take_iron_tablet", adolBaseline.getHowTakeIronTablet());
                values.put("like_iron_tablet", adolBaseline.getLikeIronTablet());
                values.put("food_type_consume", adolBaseline.getFoodTypeConsume());
                values.put("consume_past_week", adolBaseline.getConsumePastWeek());
                values.put("peas", adolBaseline.getPeas());
                values.put("seafood", adolBaseline.getSeafood());
                values.put("meat", adolBaseline.getMeat());
                values.put("egg", adolBaseline.getEgg());
                values.put("greenleaf", adolBaseline.getGreenleaf());
                values.put("almonds", adolBaseline.getAlmonds());
                values.put("include_lemon_in_diet", adolBaseline.getIncludeLemonInDiet());
                values.put("deworming_tablet", adolBaseline.getDewormingTablet());
                values.put("frequently_albandazole_tablet", adolBaseline.getFrequentlyAlbandazoleTablet());
                values.put("had_checked_hb_before", adolBaseline.getHadCheckedHBBefore());
                values.put("hb", adolBaseline.getHB());


                id = DB.insert("adolscent_baseline", null, values);
            }
        } catch (Exception s) {
            s.printStackTrace();

        }
        return id;

    }


    public long updateAdolBaselinee(AdolBaseline adolBaseline, AdolBaseline adolBaseline1, String ids) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();

                values.put("adolscent_name", adolBaseline1.getAdolName());
                values.put("address", adolBaseline1.getAddress());
                values.put("parent_name", adolBaseline1.getParentName());
                values.put("contact_number", adolBaseline1.getContactNumber());
                values.put("height", adolBaseline1.getHeight());
                values.put("weight", adolBaseline1.getWeight());
                values.put("age", adolBaseline1.getAge());
                values.put("muac", adolBaseline1.getMUAC());
                values.put("school_name", adolBaseline1.getSchoolName());
                values.put("class", adolBaseline1.getClassName());
                values.put("unique_id", adolBaseline1.getUniqueId());
                values.put("village_name", adolBaseline1.getVillageName());
                values.put("block_name", adolBaseline1.getBlockName());
                values.put("district_name", adolBaseline1.getDistrictName());
                values.put("heard_of_aneamia", adolBaseline1.getHeardOfAneamia());
                values.put("source_of_info", adolBaseline1.getSourceOfInfo());
                values.put("what_is_aneamia", adolBaseline1.getWhatIsAneamia());
                values.put("which_nutrient_def", adolBaseline1.getWhichNutrientDef());
                values.put("cause_of_aneamia", adolBaseline1.getCauseOfAneamia());
                values.put("signs_of_aneamia", adolBaseline1.getSignsOfAneamia());
                values.put("effect_of_aneamia", adolBaseline1.getEffectsOfAneamia());
                values.put("measures_of_aneamia", adolBaseline1.getMeasuresOfAneamia());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", "0");


                values.put("iron_rich_food", adolBaseline.getIronRichFood());
                values.put("more_iron_needs", adolBaseline.getMoreIronNeeds());
                values.put("you_are_aneamic", adolBaseline.getYouAreAneamic());
                values.put("how_serious_aneamia", adolBaseline.getHowSeriousAneamia());
                values.put("get_iron_tablet", adolBaseline.getGetIronTablet());
                values.put("how_take_iron_tablet", adolBaseline.getHowTakeIronTablet());
                values.put("like_iron_tablet", adolBaseline.getLikeIronTablet());
                values.put("food_type_consume", adolBaseline.getFoodTypeConsume());
                values.put("consume_past_week", adolBaseline.getConsumePastWeek());
                values.put("peas", adolBaseline.getPeas());
                values.put("seafood", adolBaseline.getSeafood());
                values.put("meat", adolBaseline.getMeat());
                values.put("egg", adolBaseline.getEgg());
                values.put("greenleaf", adolBaseline.getGreenleaf());
                values.put("almonds", adolBaseline.getAlmonds());
                values.put("include_lemon_in_diet", adolBaseline.getIncludeLemonInDiet());
                values.put("deworming_tablet", adolBaseline.getDewormingTablet());
                values.put("frequently_albandazole_tablet", adolBaseline.getFrequentlyAlbandazoleTablet());
                values.put("had_checked_hb_before", adolBaseline.getHadCheckedHBBefore());
                values.put("hb", adolBaseline.getHB());


                id = DB.update("adolscent_baseline", values, "id = " + ids, null);
            }
        } catch (Exception s) {
            s.printStackTrace();

        }
        return id;

    }

    public long setPregnantWomenBaseline(PW_Baseline pw_baseline) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("women_name", pw_baseline.getName());
                values.put("husband_name", pw_baseline.getHusbandName());
                values.put("village_name", pw_baseline.getVillageName());
                values.put("block_name", pw_baseline.getBlockName());
                values.put("district_name", pw_baseline.getDistrictName());
                values.put("contact", pw_baseline.getContact());
                values.put("age", pw_baseline.getAge());
                values.put("education", pw_baseline.getEducation());
                values.put("occupation", pw_baseline.getOccupation());
                values.put("husband_alive", pw_baseline.getHusbandAlive());
                values.put("no_of_children", pw_baseline.getNoOfChildren());
                values.put("no_of_boys", pw_baseline.getNoOfBoys());
                values.put("no_of_girls", pw_baseline.getNoOfGirls());
                values.put("mother_in_law", pw_baseline.getMotherInLaw());
                values.put("father_in_law", pw_baseline.getFatherInLaw());
                values.put("family_income", pw_baseline.getFamilyIncome());
                values.put("source_of_income", pw_baseline.getSourceOfIncome());
                values.put("anganwadi_center_id", user_id);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("status", "0");


                id = DB.insert("pregnant_women_baseline", null, values);
            }
        } catch (Exception s) {

        }
        return id;
    }

    public long updatePregnantBaseline(PW_Baseline pw_baseline, String last_id) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("breast_feeding_initiated", pw_baseline.getBreastFeedingInitiated());
                values.put("how_long_breast_feeding", pw_baseline.getHowLongBreastFeeding());
                values.put("when_start_food", pw_baseline.getWhenStartFood());
                values.put("decesion_to_start_feeding", pw_baseline.getDecesionToStartFeeding());
                values.put("breast_feeding_given_during", pw_baseline.getBreastFeedingGivenDuring());
                values.put("first_dose_of_tetnus", pw_baseline.getFirstDoseOfTetnus());
                values.put("second_dose_of_tetnus", pw_baseline.getSecondDoseOfTetnus());
                values.put("booster_tetnus", pw_baseline.getBoosterTetnus());
                values.put("vaccines_to_be_given", pw_baseline.getVaccinesToBeGiven());
                values.put("iron_supplement_important", pw_baseline.getIronSupplementImportant());
                values.put("trimester_of_deworming", pw_baseline.getTrimesterOfDeworming());
                values.put("often_take_iron_tablet", pw_baseline.getOftenTakeIronTablet());
                values.put("iron_rich_food", pw_baseline.getIronRichFood());
                values.put("iron_tablet_from_govt", pw_baseline.getIronTabletFromGovt());

                id = DB.update("pregnant_women_baseline", values, "id = " + last_id, null);
            }
        } catch (Exception s) {
            s.printStackTrace();

        }
        return id;
    }

    public long updatePregnantBaseline2(PW_Baseline pw_baseline, String last_id) {
        long id = 0;
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("hand_washing", pw_baseline.getHandWashing());
                values.put("hand_washing_when", pw_baseline.getHandWashingWhen());
                values.put("how_diarrhea_prevented", pw_baseline.getHowDiarrheaPrevented());
                values.put("source_of_water", pw_baseline.getSourceOfWater());
                values.put("boil_water_before_drinking", pw_baseline.getBoilWaterBeforeDrinking());
                values.put("have_toilet_at_home", pw_baseline.getHaveToiletAtHome());
                values.put("keep_surrounding_clean", pw_baseline.getKeepSurroundingClean());
                values.put("heard_aneamia", pw_baseline.getHeardAneamia());
                values.put("nutrient_deficient_in_aneamia", pw_baseline.getNutrientDeficientInAneamia());
                values.put("cause_of_aneamia", pw_baseline.getCauseOfAneamia());
                values.put("item_eat_at_home", pw_baseline.getItemEatAtHome());
                values.put("consume_veg_or_non_veg", pw_baseline.getConsumeVegOrNonVeg());
                values.put("important_of_child_nutrition", pw_baseline.getImportantOfChildNutrition());
                values.put("different_food_important", pw_baseline.getDifferentFoodImportant());
                values.put("visit_anganwadi_center", pw_baseline.getVisitAnganwadiCenter());
                values.put("attend_meeting", pw_baseline.getAttendMeeting());
                values.put("aware_of_govt_scheme", pw_baseline.getAwareOfGovtScheme());
                values.put("have_mother_card", pw_baseline.getHaveMotherCard());
                values.put("what_to_do_in_infection", pw_baseline.getWhatToDoInInfection());

                id = DB.update("pregnant_women_baseline", values, "id = " + last_id, null);
            }
        } catch (Exception s) {
            s.printStackTrace();

        }
        return id;
    }

    public ArrayList<AdolBaseline> getAdolscentBaselineData() {
        ArrayList<AdolBaseline> AdolscentBaseList = new ArrayList<>();
        mydb.openDataBase();
        AdolBaseline adolBaseline;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select * from adolscent_baseline where status='0' and anganwadi_center_id="
                        + user_id + " and user_master_id=" + user_master_id;
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {

                        adolBaseline = new AdolBaseline();
                        adolBaseline.setId(cur.getString(cur.getColumnIndex("id")));
                        adolBaseline.setAdolName(cur.getString(cur.getColumnIndex("adolscent_name")));
                        adolBaseline.setAddress(cur.getString(cur.getColumnIndex("address")));
                        adolBaseline.setParentName(cur.getString(cur.getColumnIndex("parent_name")));
                        adolBaseline.setContactNumber(cur.getString(cur.getColumnIndex("contact_number")));
                        adolBaseline.setHeight(cur.getString(cur.getColumnIndex("height")));
                        adolBaseline.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        adolBaseline.setAge(cur.getString(cur.getColumnIndex("age")));
                        adolBaseline.setMUAC(cur.getString(cur.getColumnIndex("muac")));
                        adolBaseline.setSchoolName(cur.getString(cur.getColumnIndex("school_name")));
                        adolBaseline.setClassName(cur.getString(cur.getColumnIndex("class")));
                        adolBaseline.setUniqueId(cur.getString(cur.getColumnIndex("unique_id")));
                        adolBaseline.setVillageName(cur.getString(cur.getColumnIndex("village_name")));
                        adolBaseline.setBlockName(cur.getString(cur.getColumnIndex("block_name")));
                        adolBaseline.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                        adolBaseline.setHeardOfAneamia(cur.getString(cur.getColumnIndex("heard_of_aneamia")));
                        adolBaseline.setSourceOfInfo(cur.getString(cur.getColumnIndex("source_of_info")));
                        adolBaseline.setWhatIsAneamia(cur.getString(cur.getColumnIndex("what_is_aneamia")));
                        adolBaseline.setWhichNutrientDef(cur.getString(cur.getColumnIndex("which_nutrient_def")));
                        adolBaseline.setCauseOfAneamia(cur.getString(cur.getColumnIndex("cause_of_aneamia")));
                        adolBaseline.setSignsOfAneamia(cur.getString(cur.getColumnIndex("signs_of_aneamia")));
                        adolBaseline.setEffectsOfAneamia(cur.getString(cur.getColumnIndex("effect_of_aneamia")));
                        adolBaseline.setMeasuresOfAneamia(cur.getString(cur.getColumnIndex("measures_of_aneamia")));
                        adolBaseline.setIronRichFood(cur.getString(cur.getColumnIndex("iron_rich_food")));
                        adolBaseline.setMoreIronNeeds(cur.getString(cur.getColumnIndex("more_iron_needs")));
                        adolBaseline.setYouAreAneamic(cur.getString(cur.getColumnIndex("you_are_aneamic")));
                        adolBaseline.setHowSeriousAneamia(cur.getString(cur.getColumnIndex("how_serious_aneamia")));
                        adolBaseline.setGetIronTablet(cur.getString(cur.getColumnIndex("get_iron_tablet")));
                        adolBaseline.setHowTakeIronTablet(cur.getString(cur.getColumnIndex("how_take_iron_tablet")));
                        adolBaseline.setLikeIronTablet(cur.getString(cur.getColumnIndex("like_iron_tablet")));
                        adolBaseline.setFoodTypeConsume(cur.getString(cur.getColumnIndex("food_type_consume")));
                        adolBaseline.setConsumePastWeek(cur.getString(cur.getColumnIndex("consume_past_week")));
                        adolBaseline.setPeas(cur.getString(cur.getColumnIndex("peas")));
                        adolBaseline.setSeafood(cur.getString(cur.getColumnIndex("seafood")));
                        adolBaseline.setEgg(cur.getString(cur.getColumnIndex("egg")));
                        adolBaseline.setMeat(cur.getString(cur.getColumnIndex("meat")));
                        adolBaseline.setGreenleaf(cur.getString(cur.getColumnIndex("greenleaf")));
                        adolBaseline.setAlmonds(cur.getString(cur.getColumnIndex("almonds")));
                        adolBaseline.setIncludeLemonInDiet(cur.getString(cur.getColumnIndex("include_lemon_in_diet")));
                        adolBaseline.setDewormingTablet(cur.getString(cur.getColumnIndex("deworming_tablet")));
                        adolBaseline.setFrequentlyAlbandazoleTablet(cur.getString(cur.getColumnIndex("frequently_albandazole_tablet")));
                        adolBaseline.setHadCheckedHBBefore(cur.getString(cur.getColumnIndex("had_checked_hb_before")));
                        adolBaseline.setHB(cur.getString(cur.getColumnIndex("hb")));
                        adolBaseline.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        adolBaseline.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        adolBaseline.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        adolBaseline.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        adolBaseline.setLattitude(cur.getString(cur.getColumnIndex("latitude")));

                        cur.moveToNext();
                        AdolscentBaseList.add(adolBaseline);
                    }
                    DB.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AdolscentBaseList;
    }

    public ArrayList<PW_Baseline> getPregnantwomenBaselineData() {
        ArrayList<PW_Baseline> PregnantBaselineList = new ArrayList<>();
        mydb.openDataBase();
        PW_Baseline pw_baseline;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select * from pregnant_women_baseline where status='0' and anganwadi_center_id="
                        + user_id + " and user_master_id=" + user_master_id;
                Cursor cur = DB.rawQuery(query, null);
                if (cur != null && cur.getCount() > 0) {
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {

                        pw_baseline = new PW_Baseline();
                        pw_baseline.setId(cur.getString(cur.getColumnIndex("id")));
                        pw_baseline.setWomenName(cur.getString(cur.getColumnIndex("women_name")));
                        pw_baseline.setHusbandName(cur.getString(cur.getColumnIndex("husband_name")));
                        pw_baseline.setVillageName(cur.getString(cur.getColumnIndex("village_name")));
                        pw_baseline.setBlockName(cur.getString(cur.getColumnIndex("block_name")));
                        pw_baseline.setDistrictName(cur.getString(cur.getColumnIndex("district_name")));
                        pw_baseline.setContact(cur.getString(cur.getColumnIndex("contact")));
                        pw_baseline.setAge(cur.getString(cur.getColumnIndex("age")));
                        pw_baseline.setEducation(cur.getString(cur.getColumnIndex("education")));
                        pw_baseline.setOccupation(cur.getString(cur.getColumnIndex("occupation")));
                        pw_baseline.setHusbandAlive(cur.getString(cur.getColumnIndex("husband_alive")));
                        pw_baseline.setNoOfChildren(cur.getString(cur.getColumnIndex("no_of_children")));
                        pw_baseline.setNoOfGirls(cur.getString(cur.getColumnIndex("no_of_girls")));
                        pw_baseline.setNoOfBoys(cur.getString(cur.getColumnIndex("no_of_boys")));
                        pw_baseline.setMotherInLaw(cur.getString(cur.getColumnIndex("mother_in_law")));
                        pw_baseline.setFatherInLaw(cur.getString(cur.getColumnIndex("father_in_law")));
                        pw_baseline.setFamilyIncome(cur.getString(cur.getColumnIndex("family_income")));
                        pw_baseline.setSourceOfIncome(cur.getString(cur.getColumnIndex("source_of_income")));
                        pw_baseline.setBreastFeedingInitiated(cur.getString(cur.getColumnIndex("breast_feeding_initiated")));
                        pw_baseline.setHowLongBreastFeeding(cur.getString(cur.getColumnIndex("how_long_breast_feeding")));
                        pw_baseline.setWhenStartFood(cur.getString(cur.getColumnIndex("when_start_food")));
                        pw_baseline.setDecesionToStartFeeding(cur.getString(cur.getColumnIndex("decesion_to_start_feeding")));
                        pw_baseline.setBreastFeedingGivenDuring(cur.getString(cur.getColumnIndex("breast_feeding_given_during")));
                        pw_baseline.setFirstDoseOfTetnus(cur.getString(cur.getColumnIndex("first_dose_of_tetnus")));
                        pw_baseline.setSecondDoseOfTetnus(cur.getString(cur.getColumnIndex("second_dose_of_tetnus")));
                        pw_baseline.setBoosterTetnus(cur.getString(cur.getColumnIndex("booster_tetnus")));
                        pw_baseline.setVaccinesToBeGiven(cur.getString(cur.getColumnIndex("vaccines_to_be_given")));
                        pw_baseline.setIronSupplementImportant(cur.getString(cur.getColumnIndex("iron_supplement_important")));
                        pw_baseline.setTrimesterOfDeworming(cur.getString(cur.getColumnIndex("trimester_of_deworming")));
                        pw_baseline.setOftenTakeIronTablet(cur.getString(cur.getColumnIndex("often_take_iron_tablet")));
                        pw_baseline.setIronRichFood(cur.getString(cur.getColumnIndex("iron_rich_food")));
                        pw_baseline.setIronTabletFromGovt(cur.getString(cur.getColumnIndex("iron_tablet_from_govt")));
                        pw_baseline.setHandWashing(cur.getString(cur.getColumnIndex("hand_washing")));
                        pw_baseline.setHandWashingWhen(cur.getString(cur.getColumnIndex("hand_washing_when")));
                        pw_baseline.setHowDiarrheaPrevented(cur.getString(cur.getColumnIndex("how_diarrhea_prevented")));
                        pw_baseline.setSourceOfWater(cur.getString(cur.getColumnIndex("source_of_water")));
                        pw_baseline.setBoilWaterBeforeDrinking(cur.getString(cur.getColumnIndex("boil_water_before_drinking")));
                        pw_baseline.setHaveToiletAtHome(cur.getString(cur.getColumnIndex("have_toilet_at_home")));
                        pw_baseline.setKeepSurroundingClean(cur.getString(cur.getColumnIndex("keep_surrounding_clean")));
                        pw_baseline.setHeardAneamia(cur.getString(cur.getColumnIndex("heard_aneamia")));
                        pw_baseline.setNutrientDeficientInAneamia(cur.getString(cur.getColumnIndex("nutrient_deficient_in_aneamia")));
                        pw_baseline.setCauseOfAneamia(cur.getString(cur.getColumnIndex("cause_of_aneamia")));
                        pw_baseline.setItemEatAtHome(cur.getString(cur.getColumnIndex("item_eat_at_home")));
                        pw_baseline.setConsumeVegOrNonVeg(cur.getString(cur.getColumnIndex("consume_veg_or_non_veg")));
                        pw_baseline.setImportantOfChildNutrition(cur.getString(cur.getColumnIndex("important_of_child_nutrition")));
                        pw_baseline.setDifferentFoodImportant(cur.getString(cur.getColumnIndex("different_food_important")));
                        pw_baseline.setVisitAnganwadiCenter(cur.getString(cur.getColumnIndex("visit_anganwadi_center")));
                        pw_baseline.setAttendMeeting(cur.getString(cur.getColumnIndex("attend_meeting")));
                        pw_baseline.setAwareOfGovtScheme(cur.getString(cur.getColumnIndex("aware_of_govt_scheme")));
                        pw_baseline.setHaveMotherCard(cur.getString(cur.getColumnIndex("have_mother_card")));
                        pw_baseline.setWhatToDoInInfection(cur.getString(cur.getColumnIndex("what_to_do_in_infection")));
                        pw_baseline.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        pw_baseline.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pw_baseline.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pw_baseline.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        pw_baseline.setLattitude(cur.getString(cur.getColumnIndex("latitude")));


                        cur.moveToNext();
                        PregnantBaselineList.add(pw_baseline);
                    }
                    DB.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PregnantBaselineList;
    }

    public long SaveChildBehaviourChangeDetails(ChildBehaviourChange childArray) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
//                values.put("child_behavior_change_id", childArray.getChild_behavior_change_id());
                values.put("registered_icds", childArray.getRegistered_icds());
                values.put("supplements_icds", childArray.getSupplements_icds());
                values.put("mother_educated", childArray.getMother_educated());
                values.put("vaccination", childArray.getVaccination());
                values.put("child_premature", childArray.getChild_premature());
                values.put("birth_breast_feeding", childArray.getChild_breast_feeding());
                values.put("child_breast_feeding", childArray.getChild_breast_feeding());
                values.put("disability", childArray.getDisability());
                values.put("birth_order", childArray.getBirth_order());
                values.put("edema_Odema", childArray.getEdema_Odema());
                values.put("child_id", childArray.getChild_id());
                values.put("user_master_id", user_master_id);
                values.put("anganwadi_center_id", user_id);
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("version", GlobalVars.App_Version);
                values.put("created_on_mobile", childArray.getCreated_on_mobile());
                values.put("mobile_unique_id", childArray.getMobile_unique_id());
                values.put("latitude", childArray.getLatitude());
                values.put("longitude", childArray.getLongitude());
                values.put("status", 0);

                id = DB.insert("child_behavior_change", null, values);
                Log.e("Tag", "Inserted Data of Child Behaviour Change" +values);
            }
        } catch (Exception s) {
        }

        return id;
    }

    public ArrayList<ChildBehaviourChange> getChildBehaviourChange() {
        ArrayList<ChildBehaviourChange> childBehaviourmonitor = new ArrayList<ChildBehaviourChange>();
        mydb.openDataBase();
        DB = mydb.getDb();
        ChildBehaviourChange childBehaviourChange;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            Cursor cur = DB.rawQuery(
                            "select child_behavior_change_id,registered_icds,supplements_icds,mother_educated,vaccination,child_premature," +
                                    "birth_breast_feeding,child_breast_feeding,user_master_id, anganwadi_center_id,disability,birth_order,child_id, edema_Odema, version," +
                                    "state_id,district_id,village_id,block_id,created_on_mobile,mobile_unique_id," +
                                    "latitude,longitude from "
                                    + "child_behavior_change where (status='0'or status='2') and anganwadi_center_id="
                                    + user_id + " and user_master_id=" + user_master_id, null);

            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        childBehaviourChange = new ChildBehaviourChange();
                        childBehaviourChange.setChild_behavior_change_id(cur.getInt(cur.getColumnIndex("child_behavior_change_id")));
                        childBehaviourChange.setRegistered_icds(cur.getString(cur.getColumnIndex("registered_icds")));
                        childBehaviourChange.setSupplements_icds(cur.getString(cur.getColumnIndex("supplements_icds")));
                        childBehaviourChange.setMother_educated(cur.getString(cur.getColumnIndex("mother_educated")));
                        childBehaviourChange.setVaccination(cur.getString(cur.getColumnIndex("vaccination")));
                        childBehaviourChange.setChild_premature(cur.getString(cur.getColumnIndex("child_premature")));
                        childBehaviourChange.setBirth_breast_feeding(cur.getString(cur.getColumnIndex("birth_breast_feeding")));
                        childBehaviourChange.setChild_breast_feeding(cur.getString(cur.getColumnIndex("child_breast_feeding")));
                        childBehaviourChange.setDisability(cur.getString(cur.getColumnIndex("disability")));
                        childBehaviourChange.setBirth_order(cur.getString(cur.getColumnIndex("birth_order")));
                        childBehaviourChange.setEdema_Odema(cur.getString(cur.getColumnIndex("edema_Odema")));
                        childBehaviourChange.setChild_id(cur.getString(cur.getColumnIndex("child_id")));
                        childBehaviourChange.setUser_master_id(String.valueOf(user_master_id));
                        childBehaviourChange.setAnganwadi_center_id(String.valueOf(user_id));
//                        childBehaviourChange.setStatus(cur.getString(cur.getColumnIndex("status")));
                        childBehaviourChange.setVersion(cur.getString(cur.getColumnIndex("version")));
                        childBehaviourChange.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        childBehaviourChange.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        childBehaviourChange.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        childBehaviourChange.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        childBehaviourChange.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        childBehaviourChange.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        childBehaviourChange.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        childBehaviourChange.setLongitude(cur.getString(cur.getColumnIndex("longitude")));


                        childBehaviourmonitor.add(childBehaviourChange);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return childBehaviourmonitor;
    }

    public ArrayList<EventMeetingModel> getEventMeetingData() {
        EventMeetingModel eventMeetingModel;
        ArrayList<EventMeetingModel> eventMeetingModelArrayList = new ArrayList<EventMeetingModel>();
        mydb.openDataBase();

        DB = mydb.getDb();

        String query = "select * from events";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    eventMeetingModel = new EventMeetingModel();
                    try {
                        eventMeetingModel.setEvents_id(cur.getInt(cur.getColumnIndex("events_id")));
                        eventMeetingModel.setEvents_from(cur.getString(cur.getColumnIndex("events_from")));
                        eventMeetingModel.setEvents_to(cur.getString(cur.getColumnIndex("events_to")));
                        eventMeetingModel.setEvents_title(cur.getString(cur.getColumnIndex("events_title")));

                        eventMeetingModelArrayList.add(eventMeetingModel);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return eventMeetingModelArrayList;
    }

    public EventMeetingModel getEventsData(int events_id) {
        EventMeetingModel eventMeetingModel = new EventMeetingModel();

        mydb.openDataBase();
        DB = mydb.getDb();
        String query = "select * from events where events_id='" + events_id + "'";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        eventMeetingModel = new EventMeetingModel();
                        eventMeetingModel.setEvents_id(cur.getInt(cur.getColumnIndex("events_id")));
                        eventMeetingModel.setEvents_from(cur.getString(cur.getColumnIndex("events_from")));
                        eventMeetingModel.setEvents_description(cur.getString(cur.getColumnIndex("events_description")));
                        eventMeetingModel.setEvens_location(cur.getString(cur.getColumnIndex("evens_location")));
                        eventMeetingModel.setEvents_to(cur.getString(cur.getColumnIndex("events_to")));
                        eventMeetingModel.setEvents_title(cur.getString(cur.getColumnIndex("events_title")));
                        eventMeetingModel.setNo_of_attendees(cur.getString(cur.getColumnIndex("no_of_attendees")));
                        eventMeetingModel.setNo_of_absence(cur.getString(cur.getColumnIndex("no_of_absence")));
                        eventMeetingModel.setImage1(cur.getString(cur.getColumnIndex("image1")));

                        cur.moveToNext();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        return eventMeetingModel;
    }


    public long AddEventData(EventDetailsModel eventDetailsModel, int events_id) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("image", eventDetailsModel.getImage());
                values.put("no_of_absence", eventDetailsModel.getNo_of_absence());
                values.put("event_date", eventDetailsModel.getEvent_date());
                values.put("events_title", eventDetailsModel.getEvents_title());
                values.put("event_type", eventDetailsModel.getEvent_type());
                values.put("male", eventDetailsModel.getMale());
                values.put("female", eventDetailsModel.getFemale());
                values.put("boys", eventDetailsModel.getBoys());
                values.put("girls", eventDetailsModel.getGirls());
                values.put("others", eventDetailsModel.getOthers());
                values.put("event_id", events_id);
                values.put("status", 0);


                id = DB.insert("event_details",   null, values);
            }
        } catch (Exception s) {
            //Log.d("AdolescentRegistration", s.getMessage());
        }

        return id;
    }


    public ArrayList<EventDetailsModel> getEventMeetingsData() {
        ArrayList<EventDetailsModel> eventDetailsModelArrayList = new ArrayList<EventDetailsModel>();
        mydb.openDataBase();
        DB = mydb.getDb();
        EventDetailsModel eventDetailsModel;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            String query="select id, event_id,event_date, events_title, event_type,no_of_absence, image,male,others, female,others, boys, girls from event_details where status='0'";

            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {

                    try {
                        eventDetailsModel = new EventDetailsModel();
//                        eventMeetingModel.setEvents_id(cur.getInt(cur.getColumnIndex("events_id")));
//                        eventMeetingModel.setEvents_from(cur.getString(cur.getColumnIndex("events_from")));
//                        eventMeetingModel.setEvents_to(cur.getString(cur.getColumnIndex("events_to")));
//                        eventMeetingModel.setNo_of_attendees(cur.getString(cur.getColumnIndex("no_of_attendees")));
//                        eventMeetingModel.setNo_of_absence(cur.getString(cur.getColumnIndex("no_of_absence")));
//                        eventMeetingModel.setImage1(cur.getString(cur.getColumnIndex("image1")));
//                        eventMeetingModel.setImage2(cur.getString(cur.getColumnIndex("image2")));
//                        eventMeetingModel.setImage3(cur.getString(cur.getColumnIndex("image3")));
//                        eventMeetingModel.setEvent_id(cur.getString(cur.getColumnIndex("events_id")));
//                        eventMeetingModel.setFrom_date(cur.getString(cur.getColumnIndex("events_from")));
//                        eventMeetingModel.setTo_date(cur.getString(cur.getColumnIndex("events_to")));
                        eventDetailsModel.setId(cur.getInt(cur.getColumnIndex("id")));
                        eventDetailsModel.setEvent_id(cur.getString(cur.getColumnIndex("event_id")));
                        eventDetailsModel.setEvent_date(cur.getString(cur.getColumnIndex("event_date")));
                        eventDetailsModel.setEvent_date(cur.getString(cur.getColumnIndex("event_date")));
                        eventDetailsModel.setEvents_title(cur.getString(cur.getColumnIndex("events_title")));
                        eventDetailsModel.setNo_of_absence(cur.getString(cur.getColumnIndex("no_of_absence")));
                        eventDetailsModel.setEvent_type(cur.getString(cur.getColumnIndex("event_type")));
                        eventDetailsModel.setImage(cur.getString(cur.getColumnIndex("image")));
                        eventDetailsModel.setMale(cur.getString(cur.getColumnIndex("male")));
                        eventDetailsModel.setFemale(cur.getString(cur.getColumnIndex("female")));
                        eventDetailsModel.setBoys(cur.getString(cur.getColumnIndex("boys")));
                        eventDetailsModel.setGirls(cur.getString(cur.getColumnIndex("girls")));
                        eventDetailsModel.setOthers(cur.getString(cur.getColumnIndex("others")));

                        eventDetailsModelArrayList.add(eventDetailsModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return eventDetailsModelArrayList;
    }


    public ArrayList<Parent> getAllDataOfHouseHold(String familyid) {
        ArrayList<Parent> parents = new ArrayList<Parent>();
        mydb.openDataBase();
        Parent parent;
        DB = mydb.getDb();

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB
                    .rawQuery(
                            "select house_number,latitude,longitude,head_family,toilet_type,water_source," +
                                    "religion_id,cast_category,adhaar_card,mobile_number,anganwadi_center_id," +
                                    "gender,date_or_recording,app_version,user_master_id,cast_id,seasonal_migration," +
                                    "family_land_hold,no_of_member_in_family,img,state_id,district_id,block_id,village_id, bpl_card, water_quality," +
                                    "Created_on_mobile,mobile_unique_id from eligible_family where (status='0' or status='5' or status='1') and anganwadi_center_id="
                                    + user_id + " and familyid=" +familyid, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        parent = new Parent();

                        parent.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        parent.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        parent.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        parent.setHead_family(cur.getString(cur.getColumnIndex("head_family")));
                        parent.setToilet_type(cur.getString(cur.getColumnIndex("toilet_type")));
                        parent.setWater_source(cur.getString(cur.getColumnIndex("water_source")));
                        parent.setReligion_id(cur.getString(cur.getColumnIndex("religion_id")));
                        parent.setCast_category(cur.getString(cur.getColumnIndex("cast_category")));
                        parent.setAdhaar_card(cur.getString(cur.getColumnIndex("adhaar_card")));
                        parent.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        parent.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        parent.setGender(cur.getString(cur.getColumnIndex("gender")));
                        parent.setDate_of_recording(cur.getString(cur.getColumnIndex("date_or_recording")));
                        parent.setApp_version(cur.getString(cur.getColumnIndex("app_version")));
                        parent.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        parent.setImage(cur.getString(cur.getColumnIndex("img")));
//                        parent.setCast_id(Integer.parseInt(cur.getString(cur.getColumnIndex("cast_id"))));
                        parent.setSeasonal_migration(cur.getString(cur.getColumnIndex("seasonal_migration")));
                        parent.setFamily_land_hold(cur.getString(cur.getColumnIndex("family_land_hold")));
                        parent.setNo_of_member_in_family(cur.getString(cur.getColumnIndex("no_of_member_in_family")));
                        parent.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        parent.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        parent.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        parent.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        parent.setBpl_card(cur.getString(cur.getColumnIndex("bpl_card")));
                        parent.setWater_quality(cur.getString(cur.getColumnIndex("water_quality")));
                        parent.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        parent.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        parents.add(parent);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return parents;
    }

    public ArrayList<Mother> getMotherDataForEdit(String familyId) {
        ArrayList<Mother> womens = new ArrayList<Mother>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Mother mother;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            query = "select mother_id,parent_id,house_number,name_of_mother,anganwadi_center_id," +
                    "user_master_id,latitude,longitude,date_of_registration,husband_father_name," +
                    "age,child_number,unmarried,widow,married,version_code,state_id,img,district_id," +
                    "block_id,village_id,created_on_mobile,mobile_unique_id,flag,is_pregnant_converted," +
                    "registred_icds,marital_status,education from mother where (status='0' or status='1') and flag='M'  " +
                    " and anganwadi_center_id= "
                    + user_id + "  and parent_id=" + familyId;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mother = new Mother();
                        mother.setMother_id(cur.getString(cur.getColumnIndex("mother_id")));
                        mother.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        mother.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        mother.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_mother")));
                        mother.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        mother.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        mother.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        mother.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        mother.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        mother.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        mother.setAge(cur.getString(cur.getColumnIndex("age")));
                        mother.setImage(cur.getString(cur.getColumnIndex("img")));
                        mother.setChild_number(cur.getString(cur.getColumnIndex("child_number")));
                        mother.setUnmarried(cur.getString(cur.getColumnIndex("unmarried")));
                        mother.setWidow(cur.getString(cur.getColumnIndex("widow")));
                        mother.setMarried(cur.getString(cur.getColumnIndex("married")));
                        mother.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        mother.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        mother.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        mother.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        mother.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        mother.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        mother.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        mother.setIs_pregnant_converted(cur.getString(cur.getColumnIndex("is_pregnant_converted")));
                        mother.setRegistred_icds(cur.getString(cur.getColumnIndex("registred_icds")));
                        mother.setMarital_status(cur.getString(cur.getColumnIndex("marital_status")));
                        mother.setEducation(cur.getString(cur.getColumnIndex("education")));

                        womens.add(mother);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return womens;
    }

    public String getNameById(String tableName, String colName, String whrCol, int whrId) {
        mydb.openDataBase();
        DB = mydb.getDb();
        String name = "";
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select " + colName + " from " + tableName + " where " + whrCol + " =" + whrId;
                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    name = cursor.getString(cursor.getColumnIndex(colName));
                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            DB.close();
        }
        return name;
    }

    public ArrayList<String> populateSpinnerNew(String tableName,
                                                String col_id, String col_value, String label, String whr) {

        ArrayList<String> spinnerData = new ArrayList<String>();
        mydb.openDataBase();
        SpinnerHelper spnHelper;
        DB = mydb.getDb();
        String query;
        // String query = "select " + col_id +" , " + col_value+" " + " from " +
        // tableName +" where is_active=1;";
        if (whr.equalsIgnoreCase("")) {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + col_value + " order by " + col_value;
        } else {
            query = "select " + col_id + " , " + col_value + " " + " from "
                    + tableName + " " + whr + " order by " + col_value;
        }
        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                spnHelper = new SpinnerHelper(label, 0 + "");
                // spinnerData.add(spnHelper);
                while (cur.moveToNext()) {

                    try {
                        /*spnHelper = new SpinnerHelper(cur.getString(1),
                                cur.getInt(0) + "");
                        spinnerData.add(spnHelper);*/
                        spinnerData.add(cur.getString(1));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return spinnerData;
    }

    public long EligibleFamilyRegistration(Parent parent, int status, String mobileUniqueId) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", parent.getHouseNo());
                values.put("head_family", parent.getHeadofHH());
                values.put("adhaar_card", parent.getAadharCardHH());
                values.put("mobile_number", parent.getMobileHH());
                values.put("anganwadi_center_id", user_id);
                values.put("toilet_type", parent.getHas_toilet());
                values.put("water_source", parent.getHave_water());
                values.put("server_id", parent.getServer_id());
                values.put("religion_id", parent.getReligion());
                values.put("cast_category", parent.getIntcastecat());
                values.put("cast_id", parent.getCast_id());
                values.put("gender", parent.getIntGender());
                values.put("bpl_card", parent.getBpl_card());
                values.put("water_quality", parent.getWater_quality());
                values.put("status", status);
                values.put("family_land_hold", parent.getLan_value());
                values.put("seasonal_migration", parent.getSes_migration());
                values.put("date_or_recording", formattedDate);
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("user_master_id", user_master_id);
                values.put("app_version", GlobalVars.App_Version);
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("mobile_unique_id", parent.getMobile_unique_id());
                values.put("created_on_mobile", parent.getCreated_on_mobile());
                values.put("img", parent.getImage());
                values.put("no_of_member_in_family", parent.getNo_of_member_in_family());
                values.put("date_of_screening", parent.getDate_of_screening());
                if (mobileUniqueId.equals("")) {
                    id = DB.insert("eligible_family", null, values);
                } else {
                    id = DB.update("eligible_family", values, "mobile_unique_id=" + mobileUniqueId, null);
                }
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            Log.d("tetsts", s.getMessage());
        }

        return id;
    }


    public void deleteRowFromTable(String tabelname, String COlUMN_NAME, String parent_id) {
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            DB.execSQL("DELETE FROM " +tabelname+ " WHERE "+COlUMN_NAME+"='"+parent_id+"'");
            DB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public long updatePregnantWomenTable(String table_name, String outComes, String dateOfDelivery,String dateOfAbortion,
                                         String placeOfDelivery, String birthWeightOfChild, String where) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("out_comes", outComes);
                values.put("date_of_delivery", dateOfDelivery);
                values.put("date_of_abortion", dateOfAbortion);
                values.put("place_of_delivery", placeOfDelivery);
                values.put("birth_weight_child", birthWeightOfChild);
                values.put("status", 0);
                values.put("flag", "M");
                id = DB.update(table_name, values, where, null);
                //Log.d("data is inserted successfully", id + "");
            }
        } catch (Exception s) {
            //Log.d("message", s.getMessage());
        }
        return id;
    }

    //get data form table and inserted pregnant women in to mother
    public ArrayList<PregnantWomen> getPregnantWomenToMother2(int intWomenId) {
        ArrayList<PregnantWomen> pregnantWomens = new ArrayList<PregnantWomen>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomen pregnantWomen;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

        /*query = "select pregnant_women_id,name_of_pregnant_women, lmp_date, edd, "
                + "house_number, height, weight,hb, systolic_bp, diastolic_bp, latitude, longitude, date_of_registration, parent_id, husband_father_name, age, img, status, server_id from pregnant_women where flag IS NOT 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                + user_id + " and user_master_id=" + user_master_id;*/
            query = "select pregnant_women_id,house_number,name_of_pregnant_women,mobile_number," +
                    "date_of_registration,place_of_delivery,lmp_date,anganwadi_center_id,user_master_id," +
                    "edd,parent_id,weight,hb,systolic_bp,diastolic_bp,latitude,husband_father_name," +
                    "height,longitude,age,version_code,created_on_mobile,img,mobile_unique_id,state_id,district_id, registered_icds, supplements_icds," +
                    "block_id,village_id, status, server_id from pregnant_women where flag = 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id + " and pregnant_women_id=" + intWomenId;

            Log.e("..............", query);
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setPregnant_women_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        pregnantWomen.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        pregnantWomen.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        pregnantWomen.setPlace_of_delivery(cur.getString(cur.getColumnIndex("place_of_delivery")));
                        pregnantWomen.setLmp_date(cur.getString(cur.getColumnIndex("lmp_date")));
                        pregnantWomen.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pregnantWomen.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pregnantWomen.setEdd(cur.getString(cur.getColumnIndex("edd")));
                        pregnantWomen.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        pregnantWomen.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        pregnantWomen.setHb(cur.getString(cur.getColumnIndex("hb")));
                        pregnantWomen.setImage(cur.getString(cur.getColumnIndex("img")));
                        pregnantWomen.setSystolic_bp(cur.getString(cur.getColumnIndex("systolic_bp")));
                        pregnantWomen.setDiastolic_bp(cur.getString(cur.getColumnIndex("diastolic_bp")));
                        pregnantWomen.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        pregnantWomen.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        pregnantWomen.setHeight(cur.getString(cur.getColumnIndex("height")));
                        pregnantWomen.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        pregnantWomen.setAge(cur.getString(cur.getColumnIndex("age")));
                        pregnantWomen.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        pregnantWomen.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        pregnantWomen.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        pregnantWomen.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        pregnantWomen.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        pregnantWomen.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        pregnantWomen.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        pregnantWomen.setRegistered_icds(cur.getString(cur.getColumnIndex("registered_icds")));
                        pregnantWomen.setSupplements_icds(cur.getString(cur.getColumnIndex("supplements_icds")));
                    /*pregnantWomen.setHhId(cur.getString(cur
                            .getColumnIndex("house_number")));
                    pregnantWomen.setPreWomenName(cur.getString(cur
                            .getColumnIndex("name_of_pregnant_women")));
                    pregnantWomen.setLmp_date(cur.getString(cur
                            .getColumnIndex("lmp_date")));
                    pregnantWomen.setEdd(cur.getString(cur
                            .getColumnIndex("edd")));
                    pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                    pregnantWomen.setHeight(cur.getString(cur
                            .getColumnIndex("height")));
                    pregnantWomen.setWeight(cur.getString(cur
                            .getColumnIndex("weight")));
                    pregnantWomen.setHusbandName(cur.getString(cur
                            .getColumnIndex("husband_father_name")));
                    pregnantWomen.setAgeofPW(cur.getString(cur
                            .getColumnIndex("age")));
                    *//*
                         * pregnantWomen.setAnganwadi_center_id(cur.getString(cur
                         * .getColumnIndex("anganwadi_center_id")));
                         *//*
                    pregnantWomen.setHb(cur.getString(cur
                            .getColumnIndex("hb")));
                    pregnantWomen.setSysbp(cur.getString(cur
                            .getColumnIndex("systolic_bp")));
                    pregnantWomen.setDiasbp(cur.getString(cur
                            .getColumnIndex("diastolic_bp")));

                    pregnantWomen.setLatitude(GlobalVars.lattitude);
                    pregnantWomen.setLongitude(GlobalVars.longitude);

                    pregnantWomen.setPregnant_women_id(cur.getString(cur
                            .getColumnIndex("pregnant_women_id")));
                    pregnantWomen.setImage(cur.getString(cur
                            .getColumnIndex("img")));

                    pregnantWomen.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));

                    pregnantWomen.setServer_id(cur.getInt(cur
                            .getColumnIndex("server_id")));*/

                        pregnantWomens.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomens;
    }

    //get data form table if the inserted pregnant women converted to mother
    public ArrayList<PregnantWomen> getPregnantWomenToMother2() {
        ArrayList<PregnantWomen> pregnantWomens = new ArrayList<PregnantWomen>();
        mydb.openDataBase();
        DB = mydb.getDb();
        PregnantWomen pregnantWomen;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

        /*query = "select pregnant_women_id,name_of_pregnant_women, lmp_date, edd, "
                + "house_number, height, weight,hb, systolic_bp, diastolic_bp, latitude, longitude, date_of_registration, parent_id, husband_father_name, age, img, status, server_id from pregnant_women where flag IS NOT 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                + user_id + " and user_master_id=" + user_master_id;*/
            query = "select pregnant_women_id,house_number,name_of_pregnant_women,mobile_number," +
                    "date_of_registration,place_of_delivery,lmp_date,anganwadi_center_id,user_master_id," +
                    "edd,parent_id,weight,hb,systolic_bp,diastolic_bp,latitude,husband_father_name," +
                    "height,longitude,age,version_code,created_on_mobile,img,mobile_unique_id,state_id,district_id, registered_icds, supplements_icds," +
                    "block_id,village_id, status, server_id from pregnant_women where pm_flag = 'M' and (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;

            Log.e("..............", query);
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        pregnantWomen = new PregnantWomen();
                        pregnantWomen.setPregnant_women_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        pregnantWomen.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        pregnantWomen.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        pregnantWomen.setMobile_number(cur.getString(cur.getColumnIndex("mobile_number")));
                        pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        pregnantWomen.setPlace_of_delivery(cur.getString(cur.getColumnIndex("place_of_delivery")));
                        pregnantWomen.setLmp_date(cur.getString(cur.getColumnIndex("lmp_date")));
                        pregnantWomen.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        pregnantWomen.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        pregnantWomen.setEdd(cur.getString(cur.getColumnIndex("edd")));
                        pregnantWomen.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        pregnantWomen.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        pregnantWomen.setHb(cur.getString(cur.getColumnIndex("hb")));
                        pregnantWomen.setImage(cur.getString(cur.getColumnIndex("img")));
                        pregnantWomen.setSystolic_bp(cur.getString(cur.getColumnIndex("systolic_bp")));
                        pregnantWomen.setDiastolic_bp(cur.getString(cur.getColumnIndex("diastolic_bp")));
                        pregnantWomen.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        pregnantWomen.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        pregnantWomen.setHeight(cur.getString(cur.getColumnIndex("height")));
                        pregnantWomen.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        pregnantWomen.setAge(cur.getString(cur.getColumnIndex("age")));
                        pregnantWomen.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        pregnantWomen.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        pregnantWomen.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        pregnantWomen.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        pregnantWomen.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        pregnantWomen.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        pregnantWomen.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        pregnantWomen.setRegistered_icds(cur.getString(cur.getColumnIndex("registered_icds")));
                        pregnantWomen.setSupplements_icds(cur.getString(cur.getColumnIndex("supplements_icds")));
                    /*pregnantWomen.setHhId(cur.getString(cur
                            .getColumnIndex("house_number")));
                    pregnantWomen.setPreWomenName(cur.getString(cur
                            .getColumnIndex("name_of_pregnant_women")));
                    pregnantWomen.setLmp_date(cur.getString(cur
                            .getColumnIndex("lmp_date")));
                    pregnantWomen.setEdd(cur.getString(cur
                            .getColumnIndex("edd")));
                    pregnantWomen.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                    pregnantWomen.setHeight(cur.getString(cur
                            .getColumnIndex("height")));
                    pregnantWomen.setWeight(cur.getString(cur
                            .getColumnIndex("weight")));
                    pregnantWomen.setHusbandName(cur.getString(cur
                            .getColumnIndex("husband_father_name")));
                    pregnantWomen.setAgeofPW(cur.getString(cur
                            .getColumnIndex("age")));
                    *//*
                         * pregnantWomen.setAnganwadi_center_id(cur.getString(cur
                         * .getColumnIndex("anganwadi_center_id")));
                         *//*
                    pregnantWomen.setHb(cur.getString(cur
                            .getColumnIndex("hb")));
                    pregnantWomen.setSysbp(cur.getString(cur
                            .getColumnIndex("systolic_bp")));
                    pregnantWomen.setDiasbp(cur.getString(cur
                            .getColumnIndex("diastolic_bp")));

                    pregnantWomen.setLatitude(GlobalVars.lattitude);
                    pregnantWomen.setLongitude(GlobalVars.longitude);

                    pregnantWomen.setPregnant_women_id(cur.getString(cur
                            .getColumnIndex("pregnant_women_id")));
                    pregnantWomen.setImage(cur.getString(cur
                            .getColumnIndex("img")));

                    pregnantWomen.setStatus(cur.getInt(cur
                            .getColumnIndex("status")));

                    pregnantWomen.setServer_id(cur.getInt(cur
                            .getColumnIndex("server_id")));*/

                        pregnantWomens.add(pregnantWomen);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return pregnantWomens;
    }

    // get mother list
    public ArrayList<Mother> getMotherDataAndInsertIntoPregnantWomenTable(String pwn) {
        ArrayList<Mother> womens = new ArrayList<Mother>();
        mydb.openDataBase();
        DB = mydb.getDb();
        Mother mother;

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {

            /*query = "select pregnant_women_id,name_of_pregnant_women, date_of_registration, house_number,latitude, longitude, husband_father_name, unmarried, widow, married, age, child_number, flag, img from pregnant_women where flag='M' and status='0' and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id;*/
            query = "select mother_id,parent_id,house_number,name_of_mother,anganwadi_center_id," +
                    "user_master_id,latitude,longitude,lmp_date,edd,height,weight,hb,date_of_registration,husband_father_name," +
                    "age,child_number,unmarried,widow,married,version_code,state_id,img,district_id," +
                    "block_id,village_id,created_on_mobile,mobile_unique_id,flag,is_pregnant_converted," +
                    "registred_icds,marital_status,education from mother where flag='' and (status = 0 or status = 5) and anganwadi_center_id= "
                    + user_id + " and user_master_id=" + user_master_id + " and mother_id=" + pwn;
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        mother = new Mother();
                        mother.setMother_id(cur.getString(cur.getColumnIndex("mother_id")));
                        mother.setParent_id(Integer.parseInt(cur.getString(cur.getColumnIndex("parent_id"))));
                        mother.setHouse_number(cur.getString(cur.getColumnIndex("house_number")));
                        mother.setName_of_pregnant_women(cur.getString(cur.getColumnIndex("name_of_mother")));
                        mother.setAnganwadi_center_id(cur.getString(cur.getColumnIndex("anganwadi_center_id")));
                        mother.setUser_master_id(cur.getString(cur.getColumnIndex("user_master_id")));
                        mother.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                        mother.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                        mother.setLmp_date(cur.getString(cur.getColumnIndex("lmp_date")));
                        mother.setEdd(cur.getString(cur.getColumnIndex("edd")));
                        mother.setHeight(cur.getString(cur.getColumnIndex("height")));
                        mother.setWeight(cur.getString(cur.getColumnIndex("weight")));
                        mother.setHb(cur.getString(cur.getColumnIndex("hb")));
                        mother.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        mother.setHusband_father_name(cur.getString(cur.getColumnIndex("husband_father_name")));
                        mother.setAge(cur.getString(cur.getColumnIndex("age")));
                        mother.setImage(cur.getString(cur.getColumnIndex("img")));
                        mother.setChild_number(cur.getString(cur.getColumnIndex("child_number")));
                        mother.setUnmarried(cur.getString(cur.getColumnIndex("unmarried")));
                        mother.setWidow(cur.getString(cur.getColumnIndex("widow")));
                        mother.setMarried(cur.getString(cur.getColumnIndex("married")));
                        mother.setVersion_code(cur.getString(cur.getColumnIndex("version_code")));
                        mother.setState_id(cur.getString(cur.getColumnIndex("state_id")));
                        mother.setDistrict_id(cur.getString(cur.getColumnIndex("district_id")));
                        mother.setBlock_id(cur.getString(cur.getColumnIndex("block_id")));
                        mother.setVillage_id(cur.getString(cur.getColumnIndex("village_id")));
                        mother.setCreated_on_mobile(cur.getString(cur.getColumnIndex("created_on_mobile")));
                        mother.setMobile_unique_id(cur.getString(cur.getColumnIndex("mobile_unique_id")));
                        mother.setIs_pregnant_converted(cur.getString(cur.getColumnIndex("is_pregnant_converted")));
                        mother.setRegistred_icds(cur.getString(cur.getColumnIndex("registred_icds")));
                        mother.setMarital_status(cur.getString(cur.getColumnIndex("marital_status")));
                        mother.setEducation(cur.getString(cur.getColumnIndex("education")));
                        /*mother.setHhId(cur.getString(cur.getColumnIndex("house_number")));
                        mother.setMotherName(cur.getString(cur.getColumnIndex("name_of_pregnant_women")));
                        mother.setMotherhusbandName(cur.getString(cur.getColumnIndex("husband_father_name")));
                        mother.setLatitude(GlobalVars.lattitude);
                        mother.setLongitude(GlobalVars.longitude);
                        mother.setDate_of_registration(cur.getString(cur.getColumnIndex("date_of_registration")));
                        mother.setMother_id(cur.getString(cur.getColumnIndex("pregnant_women_id")));
                        mother.setUnmarried(cur.getString(cur.getColumnIndex("unmarried")));
                        mother.setWidow(cur.getString(cur.getColumnIndex("widow")));
                        mother.setMarried(cur.getString(cur.getColumnIndex("married")));
                        mother.setAge(cur.getString(cur.getColumnIndex("age")));
                        mother.setNumber_of_child(cur.getString(cur.getColumnIndex("child_number")));
                        mother.setFlag(cur.getString(cur.getColumnIndex("flag")));
                        mother.setImage(cur.getString(cur.getColumnIndex("img")));*/
                        womens.add(mother);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return womens;
    }

    public long PregnantWomenRegistration2(ArrayList<Mother> motherList) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("house_number", motherList.get(0).getHouse_number());
                values.put("name_of_pregnant_women", motherList.get(0).getName_of_pregnant_women());
                values.put("lmp_date", motherList.get(0).getLmp_date());
                values.put("edd", motherList.get(0).getEdd());
                values.put("flag", "");
                values.put("height", motherList.get(0).getHeight());
                values.put("weight", motherList.get(0).getWeight());
                values.put("hb", motherList.get(0).getHb());
                values.put("date_of_registration", formattedDate);
                values.put("status", motherList.get(0).getStatus());
                values.put("user_id", user_id);
                values.put("mobile_number", "");
                values.put("place_of_delivery", "");
                values.put("latitude", GlobalVars.lattitude);
                values.put("longitude", GlobalVars.longitude);
                values.put("parent_id", motherList.get(0).getParent_id());
                values.put("anganwadi_center_id", user_id);
                values.put("husband_father_name", motherList.get(0).getHusband_father_name());
                values.put("age", motherList.get(0).getAge());
                values.put("created_on_mobile", motherList.get(0).getCreated_on_mobile());
                values.put("mobile_unique_id", motherList.get(0).getMobile_unique_id());
                values.put("is_pregnant", 1);
                values.put("mother_unique_id", motherList.get(0).getMobile_unique_id());
                values.put("state_id", sph.getString("state_id", ""));
                values.put("district_id", sph.getString("district_id", ""));
                values.put("block_id", sph.getString("block_id", ""));
                values.put("village_id", sph.getString("village_id", ""));
                values.put("user_master_id", user_master_id);
                values.put("version_code", GlobalVars.App_Version);
                values.put("img", motherList.get(0).getImage());
                values.put("education", motherList.get(0).getEducation());

                id = DB.insert("pregnant_women", null, values);
                //Log.d("PregnantWomenRegistration : data is inserted successfully",id + "");
            }
        } catch (Exception s) {
            //Log.d("PregnantWomenRegistration", s.getMessage());
        }
        DB.close();
        return id;
    }

    public ArrayList<EventDetailsModel> getEventList(String event_id) {
        EventDetailsModel eventDetailsModel;
        ArrayList<EventDetailsModel> eventDetailsModelArrayList = new ArrayList<EventDetailsModel>();
        mydb.openDataBase();
        DB = mydb.getDb();

        String query = "select * from event_details where event_id='" + event_id +"'";

        if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
            Cursor cur = DB.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    eventDetailsModel = new EventDetailsModel();
                    try {
                        eventDetailsModel.setEvent_date(cur.getString(cur.getColumnIndex("event_date")));
                        eventDetailsModel.setEvents_title(cur.getString(cur.getColumnIndex("events_title")));
                        eventDetailsModel.setEvent_type(cur.getString(cur.getColumnIndex("event_type")));
                        eventDetailsModel.setMale(cur.getString(cur.getColumnIndex("male")));
                        eventDetailsModel.setFemale(cur.getString(cur.getColumnIndex("female")));
                        eventDetailsModel.setBoys(cur.getString(cur.getColumnIndex("boys")));
                        eventDetailsModel.setGirls(cur.getString(cur.getColumnIndex("girls")));
                        eventDetailsModel.setOthers(cur.getString(cur.getColumnIndex("others")));
                        eventDetailsModelArrayList.add(eventDetailsModel);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return eventDetailsModelArrayList;
    }

    public String getCloumnName(String colName, String table, String whr) {
        String sum = "";
        mydb.openDataBase();
        DB = mydb.getDb();
        Cursor cursor = DB.rawQuery("select " + colName + " " + " from " + table + " " + whr , null);
        if (cursor.moveToFirst())
            sum = cursor.getString(cursor.getColumnIndex(colName)).trim();
        return sum;
    }

    public long saveSuposhanSakhiRegistrationData(SuposhanSakhiPojo suposhanSakhiPojo) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", suposhanSakhiPojo.getId());
                values.put("name", suposhanSakhiPojo.getName());
                values.put("mobile_number", suposhanSakhiPojo.getMobile_number());
                values.put("photograph", suposhanSakhiPojo.getPhotograph());
                values.put("user_master_id", user_master_id);
                values.put("user_id", user_id);
                values.put("flag", suposhanSakhiPojo.getFlag());
                values.put("created_at", suposhanSakhiPojo.getCreated_at());
                id = DB.insert("suposhan_sakhi", null, values);
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
          //  Log.d("tetsts", s.getMessage());
        }

        return id;
    }

    public long saveNutritionChampionsRegistrationData(NutritionChampionPojo nutritionChampionPojo) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", nutritionChampionPojo.getId());
                values.put("name", nutritionChampionPojo.getName());
                values.put("mobile_number", nutritionChampionPojo.getMobile_number());
                values.put("photograph", nutritionChampionPojo.getPhotograph());
                values.put("user_master_id", user_master_id);
                values.put("user_id", user_id);
                values.put("flag", nutritionChampionPojo.getFlag());
                values.put("created_at", nutritionChampionPojo.getCreated_at());
                id = DB.insert("nutrition_registration", null, values);
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            //  Log.d("tetsts", s.getMessage());
        }

        return id;
    }
    public AttendanceImagePojo getAttendanceImageUpdatedData(String local_id) {

        AttendanceImagePojo attendanceImagePojo =new AttendanceImagePojo();
//        SQLiteDatabase db = this.getWritableDatabase();
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "select id,start_image,start_time,end_time,end_image from attendance_image where id='"+local_id+"'";
                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        attendanceImagePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        attendanceImagePojo.setStart_image(cursor.getString(cursor.getColumnIndex("start_image")));
                        attendanceImagePojo.setStart_time(cursor.getString(cursor.getColumnIndex("start_time")));
                        attendanceImagePojo.setEnd_time(cursor.getString(cursor.getColumnIndex("end_time")));
                        attendanceImagePojo.setEnd_image(cursor.getString(cursor.getColumnIndex("end_image")));

                        cursor.moveToNext();
                    }
//                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return attendanceImagePojo;

    }

    public AttendanceImagePojo getAttendanceImageData() {

        AttendanceImagePojo attendanceImagePojo =new AttendanceImagePojo();
//        SQLiteDatabase db = this.getWritableDatabase();
        mydb.openDataBase();
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                String query = "SELECT id, start_image, start_time, end_time, end_image FROM attendance_image where user_id='"+ user_master_id+"' ORDER BY id DESC LIMIT 1 ";
                Cursor cursor = DB.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        attendanceImagePojo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        attendanceImagePojo.setStart_image(cursor.getString(cursor.getColumnIndex("start_image")));
                        attendanceImagePojo.setStart_time(cursor.getString(cursor.getColumnIndex("start_time")));
                        attendanceImagePojo.setEnd_time(cursor.getString(cursor.getColumnIndex("end_time")));
                        attendanceImagePojo.setEnd_image(cursor.getString(cursor.getColumnIndex("end_image")));
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return attendanceImagePojo;

    }

    public long saveAttendanceImageData(AttendanceImagePojo attendanceImagePojo) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("start_image", attendanceImagePojo.getStart_image());
                values.put("start_time", attendanceImagePojo.getStart_time());
                values.put("user_id", user_master_id);
                values.put("end_time", attendanceImagePojo.getEnd_time());
                values.put("end_image", attendanceImagePojo.getEnd_image());
                values.put("created_at", attendanceImagePojo.getCreated_at());
                values.put("flag", "0");

                id = DB.insert("attendance_image", null, values);

                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            Log.d("tetsts", s.getMessage());
        }

        return id;
    }

    public long saveAttendanceUpdateImage(AttendanceImagePojo attendanceImagePojo, String id) {
        mydb.openDataBase();
        long ids = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", id);
                values.put("start_image", attendanceImagePojo.getStart_image());
                values.put("start_time", attendanceImagePojo.getStart_time());
                values.put("user_id", user_master_id);
                values.put("end_time", attendanceImagePojo.getEnd_time());
                values.put("end_image", attendanceImagePojo.getEnd_image());
                values.put("created_at", attendanceImagePojo.getCreated_at());
                values.put("flag", "0");

                ids = DB.update("attendance_image", values, "id=" + id, null);

                Log.d("tag","data is inserted successfully" + ids);
            }
        } catch (Exception s) {
            Log.d("tetsts", s.getMessage());
        }

        return ids;
    }

    public long saveSuposhanSakhiMonitoringData(SuposhanSakhiMonitoringPojo suposhanSakhiMonitoringPojo) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", suposhanSakhiMonitoringPojo.getId());
                values.put("garden_setup", suposhanSakhiMonitoringPojo.getGarden_setup());
                values.put("active", suposhanSakhiMonitoringPojo.getActive());
                values.put("user_master_id", user_master_id);
                values.put("user_id", user_id);
                values.put("flag", suposhanSakhiMonitoringPojo.getFlag());
                values.put("created_at", suposhanSakhiMonitoringPojo.getCreated_at());
                id = DB.insert("suposhan_sakhi_monitoring", null, values);
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            //  Log.d("tetsts", s.getMessage());
        }

        return id;
    }

    public long saveNutritionMonitoringData(NutritionMonitoringPojo nutritionMonitoringPojo) {
        mydb.openDataBase();
        long id = 0;
        DB = mydb.getDb();
        try {
            if (DB != null && DB.isOpen() && !DB.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("id", nutritionMonitoringPojo.getId());
                values.put("garden_setup", nutritionMonitoringPojo.getGarden_setup());
                values.put("active", nutritionMonitoringPojo.getActive());
                values.put("user_master_id", user_master_id);
                values.put("user_id", user_id);
                values.put("flag", nutritionMonitoringPojo.getFlag());
                values.put("created_at", nutritionMonitoringPojo.getCreated_at());
                id = DB.insert("nutrition_champions_monitoring", null, values);
                Log.d("tag","data is inserted successfully" + id);
            }
        } catch (Exception s) {
            //  Log.d("tetsts", s.getMessage());
        }

        return id;
    }

}
