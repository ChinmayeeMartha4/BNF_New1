package com.example.mhealth.helper;

public class PregnantWomenMonitor {

    int status, server_id;

    String pregnant_women_name, weight, hb, app_version, bp, current_date, pregnant_women_id, women_monitoring_id,
            date_of_recording, server_mon_id, user_master_id,
            sysbp, diasbp, MgrStatus, img, cause_date;
    String delivery_type;
    String muac_status;
    private String women_id,migration_type,c_date,systolic_bp,diastolic_bp,
            height,version_code,anganwadi_center_id,monitor_id,
            state_id,district_id,block_id,village_id,created_on_mobile,mobile_unique_id;
    private String visit_sequence,high_bp,convulsions,vaginal_bleeding,foul_smelling_vaginal_discharge,
            severe_anaemia,diabetes,twins,date_of_screening;
    private String week_of_pregnancy,night_blindness,iodine_deficiency,fluorosis,bmi,fever_with_chilled;
    private String cough,sputum_cough,urinary_complaints,prolonged_illness,sugar_albumin;

    public String getCough() {
        return cough;
    }

    public void setCough(String cough) {
        this.cough = cough;
    }

    public String getSputum_cough() {
        return sputum_cough;
    }

    public void setSputum_cough(String sputum_cough) {
        this.sputum_cough = sputum_cough;
    }

    public String getUrinary_complaints() {
        return urinary_complaints;
    }

    public void setUrinary_complaints(String urinary_complaints) {
        this.urinary_complaints = urinary_complaints;
    }

    public String getProlonged_illness() {
        return prolonged_illness;
    }

    public void setProlonged_illness(String prolonged_illness) {
        this.prolonged_illness = prolonged_illness;
    }

    public String getSugar_albumin() {
        return sugar_albumin;
    }

    public void setSugar_albumin(String sugar_albumin) {
        this.sugar_albumin = sugar_albumin;
    }

    public String getWeek_of_pregnancy() {
        return week_of_pregnancy;
    }

    public void setWeek_of_pregnancy(String week_of_pregnancy) {
        this.week_of_pregnancy = week_of_pregnancy;
    }

    public String getNight_blindness() {
        return night_blindness;
    }

    public void setNight_blindness(String night_blindness) {
        this.night_blindness = night_blindness;
    }

    public String getIodine_deficiency() {
        return iodine_deficiency;
    }

    public void setIodine_deficiency(String iodine_deficiency) {
        this.iodine_deficiency = iodine_deficiency;
    }

    public String getFluorosis() {
        return fluorosis;
    }

    public void setFluorosis(String fluorosis) {
        this.fluorosis = fluorosis;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getFever_with_chilled() {
        return fever_with_chilled;
    }

    public void setFever_with_chilled(String fever_with_chilled) {
        this.fever_with_chilled = fever_with_chilled;
    }

    public String getDate_of_screening() {
        return date_of_screening;
    }

    public void setDate_of_screening(String date_of_screening) {
        this.date_of_screening = date_of_screening;
    }

    public String getVisit_sequence() {
        return visit_sequence;
    }

    public void setVisit_sequence(String visit_sequence) {
        this.visit_sequence = visit_sequence;
    }

    public String getHigh_bp() {
        return high_bp;
    }

    public void setHigh_bp(String high_bp) {
        this.high_bp = high_bp;
    }

    public String getConvulsions() {
        return convulsions;
    }

    public void setConvulsions(String convulsions) {
        this.convulsions = convulsions;
    }

    public String getVaginal_bleeding() {
        return vaginal_bleeding;
    }

    public void setVaginal_bleeding(String vaginal_bleeding) {
        this.vaginal_bleeding = vaginal_bleeding;
    }

    public String getFoul_smelling_vaginal_discharge() {
        return foul_smelling_vaginal_discharge;
    }

    public void setFoul_smelling_vaginal_discharge(String foul_smelling_vaginal_discharge) {
        this.foul_smelling_vaginal_discharge = foul_smelling_vaginal_discharge;
    }

    public String getSevere_anaemia() {
        return severe_anaemia;
    }

    public void setSevere_anaemia(String severe_anaemia) {
        this.severe_anaemia = severe_anaemia;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getTwins() {
        return twins;
    }

    public void setTwins(String twins) {
        this.twins = twins;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWomen_id() {
        return women_id;
    }

    public void setWomen_id(String women_id) {
        this.women_id = women_id;
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

    public String getSystolic_bp() {
        return systolic_bp;
    }

    public void setSystolic_bp(String systolic_bp) {
        this.systolic_bp = systolic_bp;
    }

    public String getDiastolic_bp() {
        return diastolic_bp;
    }

    public void setDiastolic_bp(String diastolic_bp) {
        this.diastolic_bp = diastolic_bp;
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

    public String getMonitor_id() {
        return monitor_id;
    }

    public void setMonitor_id(String monitor_id) {
        this.monitor_id = monitor_id;
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

    public String getMuac_status() {
        return muac_status;
    }

    public void setMuac_status(String muac_status) {
        this.muac_status = muac_status;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getCause_date() {
        return cause_date;
    }

    public void setCause_date(String cause_date) {
        this.cause_date = cause_date;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getSysbp() {
        return sysbp;
    }

    public void setSysbp(String sysbp) {
        this.sysbp = sysbp;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String s) {
        this.img = s;
    }

    public String getDiasbp() {
        return diasbp;
    }

    public void setDiasbp(String diasbp) {
        this.diasbp = diasbp;
    }

    public String getServer_mon_id() {
        return server_mon_id;
    }

    public void setServer_mon_id(String server_mon_id) {
        this.server_mon_id = server_mon_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDate_of_recording() {
        return date_of_recording;
    }

    public void setDate_of_recording(String date_of_recording) {
        this.date_of_recording = date_of_recording;
    }

    public String getWomen_monitoring_id() {
        return women_monitoring_id;
    }

    public void setWomen_monitoring_id(String women_monitoring_id) {
        this.women_monitoring_id = women_monitoring_id;
    }

    public String getPregnant_women_id() {
        return pregnant_women_id;
    }

    public void setPregnant_women_id(String pregnant_women_id) {
        this.pregnant_women_id = pregnant_women_id;
    }

    public String getSysBp() {
        return sysbp;
    }

    public void setSysBp(String sysbp) {
        this.sysbp = sysbp;
    }

    public String getDiasBp() {
        return diasbp;
    }

    public void setDiasBp(String diasbp) {
        this.diasbp = diasbp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    public String getPregnant_women_name() {
        return pregnant_women_name;
    }

    public void setPregnant_women_name(String pregnant_women_name) {
        this.pregnant_women_name = pregnant_women_name;
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

    public String getMgrStatus() {
        return MgrStatus;
    }

    public void setMgrStatus(String mgs) {
        this.MgrStatus = mgs;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }


}
