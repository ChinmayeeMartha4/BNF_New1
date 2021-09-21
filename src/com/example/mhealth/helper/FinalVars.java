package com.example.mhealth.helper;

import android.os.Environment;

public class FinalVars {


    public static String DS = "/";
    public static String SDCARD = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.example.anganwarisupport";
    public static String Anganwadi_DIR = SDCARD + DS + "Anganwadi";
    public static String Anganwadi_DB = Anganwadi_DIR + DS + "data" + DS;
    public static String Anganwadi_IMG = Anganwadi_DIR + DS + "image" + DS;
    public static String DATABASE_FILE_NAME = "mHealth.db";

    public static String DATABASE_FILE_PATH = Anganwadi_DB + DATABASE_FILE_NAME;
}
