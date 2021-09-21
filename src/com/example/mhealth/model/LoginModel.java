package com.example.mhealth.model;

public class LoginModel {
    private String user_name;
    private String password;
    private String user_master_id;
    private String created_on;

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
