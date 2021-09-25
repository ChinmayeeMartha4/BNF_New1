package com.example.mhealth.helper;

public class AdolescentMonitoring {

    String hhId, adolscentName, adolescentWeight, adolescentHeight, adolescentBp,
            adolescentHb, dateOfRecord, currentDate, app_version, girl_nut_id, MgrStatus, img;
    String marked_as;
    String periods;
    String death_date = "";
    int status, server_id;
    int adolescentGirlID, serverId;
    private int monitorID;

    private String adolescent_nutrition_id,adolescent_id,weight,hb,migration_type,c_date,bp,
            date_of_recording,height,version_code,anganwadi_center_id,user_master_id
            ,state_id,district_id,block_id,village_id,created_on_mobile,mobile_unique_id;
    private String consumption_of_ifa,deworming_done,date_of_screening;
    private String is_adolescent_access_icds;
    private String is_adolescent_rececive_ifa;
    private String is_taken_ifa;
    private String is_adolescent_dewarming_tablet;
    private String is_health_service;
    private String is_hygiene_kit;

    public String getIs_hygiene_kit() {
        return is_hygiene_kit;
    }

    public void setIs_hygiene_kit(String is_hygiene_kit) {
        this.is_hygiene_kit = is_hygiene_kit;
    }

    public String getIs_adolescent_access_icds() {
        return is_adolescent_access_icds;
    }

    public void setIs_adolescent_access_icds(String is_adolescent_access_icds) {
        this.is_adolescent_access_icds = is_adolescent_access_icds;
    }

    public String getIs_adolescent_rececive_ifa() {
        return is_adolescent_rececive_ifa;
    }

    public void setIs_adolescent_rececive_ifa(String is_adolescent_rececive_ifa) {
        this.is_adolescent_rececive_ifa = is_adolescent_rececive_ifa;
    }

    public String getIs_taken_ifa() {
        return is_taken_ifa;
    }

    public void setIs_taken_ifa(String is_taken_ifa) {
        this.is_taken_ifa = is_taken_ifa;
    }

    public String getIs_adolescent_dewarming_tablet() {
        return is_adolescent_dewarming_tablet;
    }

    public void setIs_adolescent_dewarming_tablet(String is_adolescent_dewarming_tablet) {
        this.is_adolescent_dewarming_tablet = is_adolescent_dewarming_tablet;
    }

    public String getIs_health_service() {
        return is_health_service;
    }

    public void setIs_health_service(String is_health_service) {
        this.is_health_service = is_health_service;
    }

    public String getDate_of_screening() {
        return date_of_screening;
    }

    public void setDate_of_screening(String date_of_screening) {
        this.date_of_screening = date_of_screening;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getConsumption_of_ifa() {
        return consumption_of_ifa;
    }

    public void setConsumption_of_ifa(String consumption_of_ifa) {
        this.consumption_of_ifa = consumption_of_ifa;
    }

    public String getDeworming_done() {
        return deworming_done;
    }

    public void setDeworming_done(String deworming_done) {
        this.deworming_done = deworming_done;
    }

    public String getAdolescent_nutrition_id() {
        return adolescent_nutrition_id;
    }

    public void setAdolescent_nutrition_id(String adolescent_nutrition_id) {
        this.adolescent_nutrition_id = adolescent_nutrition_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAdolescent_id() {
        return adolescent_id;
    }

    public void setAdolescent_id(String adolescent_id) {
        this.adolescent_id = adolescent_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHb() {
        return hb;
    }

    public void setHb(String hb) {
        this.hb = hb;
    }

    public String getMigration_type() {
        return migration_type;
    }

    public void setMigration_type(String migration_type) {
        this.migration_type = migration_type;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getDate_of_recording() {
        return date_of_recording;
    }

    public void setDate_of_recording(String date_of_recording) {
        this.date_of_recording = date_of_recording;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getAnganwadi_center_id() {
        return anganwadi_center_id;
    }

    public void setAnganwadi_center_id(String anganwadi_center_id) {
        this.anganwadi_center_id = anganwadi_center_id;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public void setBlock_id(String block_id) {
        this.block_id = block_id;
    }

    public String getVillage_id() {
        return village_id;
    }

    public void setVillage_id(String village_id) {
        this.village_id = village_id;
    }

    public String getCreated_on_mobile() {
        return created_on_mobile;
    }

    public void setCreated_on_mobile(String created_on_mobile) {
        this.created_on_mobile = created_on_mobile;
    }

    public String getMobile_unique_id() {
        return mobile_unique_id;
    }

    public void setMobile_unique_id(String mobile_unique_id) {
        this.mobile_unique_id = mobile_unique_id;
    }

    public String getDeath_date() {
        return death_date;
    }

    public void setDeath_date(String death_date) {
        this.death_date = death_date;
    }

    public String getMarked_as() {
        return marked_as;
    }

    public void setMarked_as(String marked_as) {
        this.marked_as = marked_as;
    }

    public String getGirl_nut_id() {
        return girl_nut_id;
    }

    public void setGirl_nut_id(String girl_nut_id) {
        this.girl_nut_id = girl_nut_id;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String g) {
        this.img = g;
    }

    public String getAdolescentBp() {
        return adolescentBp;
    }

    public void setAdolescentBp(String adolescentBp) {
        this.adolescentBp = adolescentBp;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getHhId() {
        return hhId;
    }

    public void setHhId(String hhId) {
        this.hhId = hhId;
    }


    public String getAdolscentName() {
        return adolscentName;
    }

    public void setAdolscentName(String adolscentName) {
        this.adolscentName = adolscentName;
    }

    public String getAdolescentWeight() {
        return adolescentWeight;
    }

    public void setAdolescentWeight(String adolescentWeight) {
        this.adolescentWeight = adolescentWeight;
    }

    public String getAdolescentHeight() {
        return adolescentHeight;
    }

    public void setAdolescentHeight(String adolescentHeight) {
        this.adolescentHeight = adolescentHeight;
    }

    public String getAdolescentHb() {
        return adolescentHb;
    }

    public void setAdolescentHb(String adolescentHb) {
        this.adolescentHb = adolescentHb;
    }

    public String getDateOfRecord() {
        return dateOfRecord;
    }

    public void setDateOfRecord(String dateOfRecord) {
        this.dateOfRecord = dateOfRecord;
    }

    public String getMgrStatus() {
        return MgrStatus;
    }

    public void setMgrStatus(String mgstat) {
        this.MgrStatus = mgstat;
    }

    public int getAdolescentGirlID() {
        return adolescentGirlID;
    }

    public void setAdolescentGirlID(int adolescentGirlID) {
        this.adolescentGirlID = adolescentGirlID;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getMonitorID() {
        return monitorID;
    }

    public void setMonitorID(int monitorID) {
        // TODO Auto-generated method stub
        this.monitorID = monitorID;
    }


}