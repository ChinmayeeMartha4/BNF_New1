package com.example.mhealth.model;

public class AttendanceImagePojo {
    private String user_id;
    private String start_image;
    private String start_time;
    private String end_time;
    private String end_image;
    private String flag;
    private String created_at;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStart_image() {
        return start_image;
    }

    public void setStart_image(String start_image) {
        this.start_image = start_image;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_image() {
        return end_image;
    }

    public void setEnd_image(String end_image) {
        this.end_image = end_image;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

//    public String getLocal_id() {
//        return local_id;
//    }
//
//    public void setLocal_id(String local_id) {
//        this.local_id = local_id;
//    }
//
//    public static final String TABLE_NAME = "attendance_image";
//    public static final String COLUMN_LOCAL_ID = "local_id";
//    public static final String COLUMN_USER_ID = "user_id";
//    public static final String COLUMN_START_IMAGE = "start_image";
//    public static final String COLUMN_START_TIME = "start_time";
//    public static final String COLUMN_END_IMAGE = "end_image";
//    public static final String COLUMN_END_TIME = "end_time";
//    public static final String COLUMN_CREATED_AT = "created_at";
//    public static final String COLUMN_FLAG = "flag";
//
//
//    public static String CREATE_TABLE =
//            " CREATE TABLE " + TABLE_NAME + "("
//                    + COLUMN_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + COLUMN_USER_ID + " TEXT ,"
//                    + COLUMN_START_IMAGE + " TEXT ,"
//                    + COLUMN_START_TIME + " TEXT ,"
//                    + COLUMN_END_IMAGE + " TEXT ,"
//                    + COLUMN_END_TIME + " TEXT ,"
//                    + COLUMN_CREATED_AT + " TEXT ,"
//                    + COLUMN_FLAG + " TEXT "
//                    + ")";
}