package com.example.mhealth.helper;

public class Nutrition {
    String date_of_monitiring, height, weight, mauc, latitdue, longitude;
    int status, child_id, server_id;
    private String photopath;
    private int nutrition_id;

    public String getDate_of_monitiring() {
        return date_of_monitiring;
    }

    public void setDate_of_monitiring(String date_of_monitiring) {
        this.date_of_monitiring = date_of_monitiring;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMauc() {
        return mauc;
    }

    public void setMauc(String mauc) {
        this.mauc = mauc;
    }

    public String getLatitdue() {
        return latitdue;
    }

    public void setLatitdue(String latitdue) {
        this.latitdue = latitdue;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChild_id() {
        return child_id;
    }

    public void setChild_id(int child_id) {
        this.child_id = child_id;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getNutrition_id() {
        return nutrition_id;
    }

    public void setNutrition_id(int nutrition_id) {
        this.nutrition_id = nutrition_id;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

}
