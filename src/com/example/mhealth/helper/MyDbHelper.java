package com.example.mhealth.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.mhealth.ActivitySplash;

public class MyDbHelper {
    private static final String TAG = "MyDbHelper";
    private static String DB_FULL_PATH = FinalVars.DATABASE_FILE_PATH;
    private final Context myContext;
    private SQLiteDatabase myDataBase;

    public MyDbHelper(Context context) {
        this.myContext = context;
    }

    public static boolean isSdPresent() {
        Log.e(TAG, "isSdPresent>>> "+Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED));
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    private boolean checkDataBase() {
        //this.getReadableDatabase();
        SQLiteDatabase checkDB = null;
        /*File dbpath=null;
        if (isSdPresent()) {
            try {
                dbpath = (new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.anganwarisupport/Anganwadi/data/mHealth.db"));
                if (!(new File(dbpath.getParent()).exists())) {
                    new File(dbpath.getParent()).mkdirs();
                }
                //checkDB=SQLiteDatabase.openDatabase(dbpath.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
                //checkDB =  SQLiteDatabase.openOrCreateDatabase(dbpath,null);
                //checkDB = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
            } catch (SQLiteException ex) {
                //showMsg(ex.getMessage());
            }*/
            /*if (checkDB != null) {
                checkDB.close();
            }*/
        /*}
        else {
            showMsg("Please Insert SDCARD first!");
            Log.e(TAG, "checkDataBase>>> "+"Please Insert SDCARD first!");
        }

        //return checkDB != null ? true : false;
        return dbpath.exists();
        */
        try {
            //String myPath = DB_PATH; //<<<< RMVD so no open error 14 uses alt method
            checkDB = SQLiteDatabase.openDatabase(
                    ActivitySplash.DB_PATH_ALT, //<<<< CHANGED
                    null,
                    SQLiteDatabase.OPEN_READWRITE
            );

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null; //<<<< simplified
    }

    public void openDataBase() throws SQLException {
        /*if (checkDataBase()) {
            try {
                File dbpath = (new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.anganwarisupport/Anganwadi/data/mHealth.db"));
                if (!(new File(dbpath.getParent()).exists())) {
                    new File(dbpath.getParent()).mkdirs();
                }
                myDataBase=SQLiteDatabase.openDatabase(dbpath.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
                //myDataBase =  SQLiteDatabase.openOrCreateDatabase(dbpath,null);
                //myDataBase = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
            } catch (IllegalStateException e) {
                //myDataBase = SQLiteDatabase.openOrCreateDatabase(DB_FULL_PATH, null);
            }
        } else {
             //showMsg("Database Error");
            Log.e(TAG, "openDataBase>>> "+"Database Error");
        }*/
        //Open the database
        //String myPath = DB_PATH; //<<<< RMVD
        try{
            myDataBase = SQLiteDatabase.openDatabase(
                    ActivitySplash.DB_PATH_ALT, //<<<< CHANGED
                    null,
                    SQLiteDatabase.OPEN_READWRITE
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getDb() {
        Log.e(TAG, "getDb>>> "+myDataBase);
        return myDataBase;
    }

    public void showMsg(String msg) {
        Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show();
    }
}
