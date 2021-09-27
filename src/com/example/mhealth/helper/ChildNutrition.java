package com.example.mhealth.helper;

public class ChildNutrition {

    String house_number;
    int status, server_id;
    String age_in_month;
    String migration;
    String dateCheck;
    private String Edema;
    private String photopath;
    private String latitude;
    private String longitude;
    private String date_of_monitoring;
    private int nutrition_id;
    private int child_id;
    private String anganwadi_center_id;
    private String is_age_vaccination;
    private String is_child_Premature;
    private String is_child_breastfeeding;
    private String is_mother_breastmilk;
    private String child_meal_number;
    private String child_meal;
    private String is_child_VitA6_months;
    private String is_child_deworming_6months;
    private String is_child_ifa;
    private String child_Vaccination_Status;
    private String select_child;
    private String medical_critical;
    private String medical_critical_reason;
    private String nrc_last_month;
    private String nrc_last_month_date;
    private String nutrition_bnf;
    private String nutrition_garden;
    private String nutrition_garden_kit;
    private String nrc_referral;
    private String complimentary_nutrition;
    private String registered_with_icds;

    public String getNrc_referral() {
        return nrc_referral;
    }

    public void setNrc_referral(String nrc_referral) {
        this.nrc_referral = nrc_referral;
    }

    public String getComplimentary_nutrition() {
        return complimentary_nutrition;
    }

    public void setComplimentary_nutrition(String complimentary_nutrition) {
        this.complimentary_nutrition = complimentary_nutrition;
    }

    public String getRegistered_with_icds() {
        return registered_with_icds;
    }

    public void setRegistered_with_icds(String registered_with_icds) {
        this.registered_with_icds = registered_with_icds;
    }

    public String getMedical_critical() {
        return medical_critical;
    }

    public void setMedical_critical(String medical_critical) {
        this.medical_critical = medical_critical;
    }

    public String getMedical_critical_reason() {
        return medical_critical_reason;
    }

    public void setMedical_critical_reason(String medical_critical_reason) {
        this.medical_critical_reason = medical_critical_reason;
    }

    public String getNrc_last_month() {
        return nrc_last_month;
    }

    public void setNrc_last_month(String nrc_last_month) {
        this.nrc_last_month = nrc_last_month;
    }

    public String getNrc_last_month_date() {
        return nrc_last_month_date;
    }

    public void setNrc_last_month_date(String nrc_last_month_date) {
        this.nrc_last_month_date = nrc_last_month_date;
    }

    public String getNutrition_bnf() {
        return nutrition_bnf;
    }

    public void setNutrition_bnf(String nutrition_bnf) {
        this.nutrition_bnf = nutrition_bnf;
    }

    public String getNutrition_garden() {
        return nutrition_garden;
    }

    public void setNutrition_garden(String nutrition_garden) {
        this.nutrition_garden = nutrition_garden;
    }

    public String getNutrition_garden_kit() {
        return nutrition_garden_kit;
    }

    public void setNutrition_garden_kit(String nutrition_garden_kit) {
        this.nutrition_garden_kit = nutrition_garden_kit;
    }

    public String getSelect_child() {
        return select_child;
    }

    public void setSelect_child(String select_child) {
        this.select_child = select_child;
    }

    public String getChild_Vaccination_Status() {
        return child_Vaccination_Status;
    }

    public void setChild_Vaccination_Status(String child_Vaccination_Status) {
        this.child_Vaccination_Status = child_Vaccination_Status;
    }

    public String getIs_child_ifa() {
        return is_child_ifa;
    }

    public void setIs_child_ifa(String is_child_ifa) {
        this.is_child_ifa = is_child_ifa;
    }

    public String getIs_child_deworming_6months() {
        return is_child_deworming_6months;
    }

    public void setIs_child_deworming_6months(String is_child_deworming_6months) {
        this.is_child_deworming_6months = is_child_deworming_6months;
    }

    public String getIs_child_VitA6_months() {
        return is_child_VitA6_months;
    }

    public void setIs_child_VitA6_months(String is_child_VitA6_months) {
        this.is_child_VitA6_months = is_child_VitA6_months;
    }

    public String getChild_meal() {
        return child_meal;
    }

    public void setChild_meal(String child_meal) {
        this.child_meal = child_meal;
    }

    public String getChild_meal_number() {
        return child_meal_number;
    }

    public void setChild_meal_number(String child_meal_number) {
        this.child_meal_number = child_meal_number;
    }

    public String getIs_mother_breastmilk() {
        return is_mother_breastmilk;
    }

    public void setIs_mother_breastmilk(String is_mother_breastmilk) {
        this.is_mother_breastmilk = is_mother_breastmilk;
    }

    public String getIs_child_breastfeeding() {
        return is_child_breastfeeding;
    }

    public void setIs_child_breastfeeding(String is_child_breastfeeding) {
        this.is_child_breastfeeding = is_child_breastfeeding;
    }

    public String getIs_child_Premature() {
        return is_child_Premature;
    }

    public void setIs_child_Premature(String is_child_Premature) {
        this.is_child_Premature = is_child_Premature;
    }

    public String getIs_age_vaccination() {
        return is_age_vaccination;
    }

    public void setIs_age_vaccination(String is_age_vaccination) {
        this.is_age_vaccination = is_age_vaccination;
    }

    String parentName, child_name, weight, height, muac, multimedia, parent, edema, app_version, hb, d_reason,
            server_nutrition_id, user_master_id, sick_y_n = "", sick_name = "", sick15_y_n = "", sick15_name = "", death_date = "";
    String name_of_child;
    String deathReason;
    String head_family;
    private String migration_type,have_adeama,version_code,sick,
            sick_reason,sick_15,sick_15_reason,death_reason,
            state_id,district_id,block_id,
            village_id,created_on_mobile,mobile_unique_id,img,date_of_screening,
            provision_of_edima, link_medical_facility;

    public String getProvision_of_edima() {
        return provision_of_edima;
    }

    public void setProvision_of_edima(String provision_of_edima) {
        this.provision_of_edima = provision_of_edima;
    }

    public String getLink_medical_facility() {
        return link_medical_facility;
    }

    public void setLink_medical_facility(String link_medical_facility) {
        this.link_medical_facility = link_medical_facility;
    }

    public String getDate_of_screening() {
        return date_of_screening;
    }

    public void setDate_of_screening(String date_of_screening) {
        this.date_of_screening = date_of_screening;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHb() {
        return hb;
    }

    public void setHb(String hb) {
        this.hb = hb;
    }

    public String getD_reason() {
        return d_reason;
    }

    public void setD_reason(String d_reason) {
        this.d_reason = d_reason;
    }

    public String getSick_y_n() {
        return sick_y_n;
    }

    public void setSick_y_n(String sick_y_n) {
        this.sick_y_n = sick_y_n;
    }

    public String getSick_name() {
        return sick_name;
    }

    public void setSick_name(String sick_name) {
        this.sick_name = sick_name;
    }

    public String getSick15_y_n() {
        return sick15_y_n;
    }

    public void setSick15_y_n(String sick15_y_n) {
        this.sick15_y_n = sick15_y_n;
    }

    public String getSick15_name() {
        return sick15_name;
    }

    public void setSick15_name(String sick15_name) {
        this.sick15_name = sick15_name;
    }

    public String getDeath_date() {
        return death_date;
    }

    public void setDeath_date(String death_date) {
        this.death_date = death_date;
    }

    public String getMigration_type() {
        return migration_type;
    }

    public void setMigration_type(String migration_type) {
        this.migration_type = migration_type;
    }

    public String getHave_adeama() {
        return have_adeama;
    }

    public void setHave_adeama(String have_adeama) {
        this.have_adeama = have_adeama;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public String getSick_reason() {
        return sick_reason;
    }

    public void setSick_reason(String sick_reason) {
        this.sick_reason = sick_reason;
    }

    public String getSick_15() {
        return sick_15;
    }

    public void setSick_15(String sick_15) {
        this.sick_15 = sick_15;
    }

    public String getSick_15_reason() {
        return sick_15_reason;
    }

    public void setSick_15_reason(String sick_15_reason) {
        this.sick_15_reason = sick_15_reason;
    }

    public String getDeath_reason() {
        return death_reason;
    }

    public void setDeath_reason(String death_reason) {
        this.death_reason = death_reason;
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

    public String getHead_family() {
        return head_family;
    }

    public void setHead_family(String head_family) {
        this.head_family = head_family;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getName_of_child() {
        return name_of_child;
    }

    public void setName_of_child(String name_of_child) {
        this.name_of_child = name_of_child;
    }

    public String getDeathReason() {
        return deathReason;
    }

    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }

    public String getDateCheck() {
        return dateCheck;
    }

    public void setDateCheck(String dateCheck) {
        this.dateCheck = dateCheck;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getDeathDate() {
        return death_date;
    }

    public void setDeathDate(String d) {
        this.death_date = d;
    }

    public String getEdema() {
        return Edema;
    }

    public void setEdema(String edema) {
        Edema = edema;
    }

    public String getMigration() {
        return migration;
    }

    public void setMigration(String migration) {
        this.migration = migration;
    }

    public String getAge_in_month() {
        return age_in_month;
    }

    public void setAge_in_month(String age_in_month) {
        this.age_in_month = age_in_month;
    }

    public String getAnganwadi_center_id() {
        return anganwadi_center_id;
    }

    public void setAnganwadi_center_id(String anganwadi_center_id) {
        this.anganwadi_center_id = anganwadi_center_id;
    }

    public String getEdeme() {
        return edema;
    }

    public void setEdeme(String edeme) {
        this.edema = edeme;
    }

    public String getHB() {
        return hb;
    }

    public void setHB(String h) {
        this.hb = h;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMuac() {
        return muac;
    }

    public void setMuac(String muac) {
        this.muac = muac;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
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

    public int getChild_id() {
        return child_id;
    }

    public void setChild_id(int child_id) {
        this.child_id = child_id;
    }

    public String getDate_of_monitoring() {
        return date_of_monitoring;
    }

    public void setDate_of_monitoring(String date_of_monitoring) {
        this.date_of_monitoring = date_of_monitoring;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
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

    public String getServer_nutrition_id() {
        return server_nutrition_id;
    }

    public void setServer_nutrition_id(String server_nutrition_id) {
        this.server_nutrition_id = server_nutrition_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getDreason() {
        return d_reason;
    }

    public void setDreason(String d_reas) {
        this.d_reason = d_reas;
    }

    public String getSickYesNo() {
        return sick_y_n;
    }

    public void setSickYesNo(String sick_y_n) {
        this.sick_y_n = sick_y_n;
    }

    public String getSickName() {
        return sick_name;
    }

    public void setSickName(String sick_name) {
        this.sick_name = sick_name;
    }

    public String getSickYesNo15() {
        return sick15_y_n;
    }

    public void setSickYesNo15(String sick_y_n) {
        this.sick15_y_n = sick_y_n;
    }

    public String getSickName15() {
        return sick15_name;
    }

    public void setSickName15(String sick_name) {
        this.sick15_name = sick_name;
    }


}
