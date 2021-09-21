package com.example.mhealth.helper;

public class PregnantWomen {


    int select_family, server_id, view_absent_status;
    String name_of_women, lmp_date, img, flag, child_number, edd, anganwadi_center_id, appVer, place_of_delivery,
            latitude, longitude, pregnant_women_id, father, user_master_id, date_of_registration, age;
    int userId, serverId, languageId;
    String hhId, preWomenName, husbandName, lmpDate, height, weight, hb, bp, sysbp, diasbp;
    private int status;
    private int user_id;
    private int parent_id;
    private String c_date;
    private String house_number,name_of_pregnant_women,mobile_number,
            systolic_bp,diastolic_bp,husband_father_name,
            version_code,created_on_mobile,mobile_unique_id,state_id,district_id,
            block_id,village_id,family_id,date_of_screening;
    private String muac,education,months_of_pregnancy,out_comes,
            date_of_delivery, date_of_abortion,birth_weight_child,
            is_pregnant,mother_unique_id;

    public String getIs_pregnant() {
        return is_pregnant;
    }

    public void setIs_pregnant(String is_pregnant) {
        this.is_pregnant = is_pregnant;
    }

    public String getMother_unique_id() {
        return mother_unique_id;
    }

    public void setMother_unique_id(String mother_unique_id) {
        this.mother_unique_id = mother_unique_id;
    }

    public String getOut_comes() {
        return out_comes;
    }

    public void setOut_comes(String out_comes) {
        this.out_comes = out_comes;
    }

    public String getDate_of_delivery() {
        return date_of_delivery;
    }

    public void setDate_of_delivery(String date_of_delivery) {
        this.date_of_delivery = date_of_delivery;
    }

    public String getDate_of_abortion() {
        return date_of_abortion;
    }

    public void setDate_of_abortion(String date_of_abortion) {
        this.date_of_abortion = date_of_abortion;
    }

    public String getBirth_weight_child() {
        return birth_weight_child;
    }

    public void setBirth_weight_child(String birth_weight_child) {
        this.birth_weight_child = birth_weight_child;
    }

    public String getMuac() {
        return muac;
    }

    public void setMuac(String muac) {
        this.muac = muac;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMonths_of_pregnancy() {
        return months_of_pregnancy;
    }

    public void setMonths_of_pregnancy(String months_of_pregnancy) {
        this.months_of_pregnancy = months_of_pregnancy;
    }

    public String getDate_of_screening() {
        return date_of_screening;
    }

    public void setDate_of_screening(String date_of_screening) {
        this.date_of_screening = date_of_screening;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    private String registered_icds;
    private String supplements_icds;

    public String getRegistered_icds() {
        return registered_icds;
    }

    public void setRegistered_icds(String registered_icds) {
        this.registered_icds = registered_icds;
    }

    public String getSupplements_icds() {
        return supplements_icds;
    }

    public void setSupplements_icds(String supplements_icds) {
        this.supplements_icds = supplements_icds;
    }

    public String getName_of_women() {
        return name_of_women;
    }

    public void setName_of_women(String name_of_women) {
        this.name_of_women = name_of_women;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getChild_number() {
        return child_number;
    }

    public void setChild_number(String child_number) {
        this.child_number = child_number;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLmpDate() {
        return lmpDate;
    }

    public void setLmpDate(String lmpDate) {
        this.lmpDate = lmpDate;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getName_of_pregnant_women() {
        return name_of_pregnant_women;
    }

    public void setName_of_pregnant_women(String name_of_pregnant_women) {
        this.name_of_pregnant_women = name_of_pregnant_women;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
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

    public String getHusband_father_name() {
        return husband_father_name;
    }

    public void setHusband_father_name(String husband_father_name) {
        this.husband_father_name = husband_father_name;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
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

    public int getView_absent_status() {
        return view_absent_status;
    }

    public void setView_absent_status(int view_absent_status) {
        this.view_absent_status = view_absent_status;
    }

    public String getDate_of_registration() {
        return date_of_registration;
    }

    public void setDate_of_registration(String date_of_registration) {
        this.date_of_registration = date_of_registration;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String d) {
        this.img = d;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getSysbp() {
        return sysbp;
    }

    public void setSysbp(String sysbp) {
        this.sysbp = sysbp;
    }

    public String getDiasbp() {
        return diasbp;
    }

    public void setDiasbp(String diasbp) {
        this.diasbp = diasbp;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String f) {
        this.flag = f;
    }

    public String getChildNumber() {
        return child_number;
    }

    public void setChildNumber(String cn) {
        this.child_number = cn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAnganwadi_center_id() {
        return anganwadi_center_id;
    }

    public void setAnganwadi_center_id(String anganwadi_center_id) {
        this.anganwadi_center_id = anganwadi_center_id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getHhId() {
        return hhId;
    }

    public void setHhId(String hhId) {
        this.hhId = hhId;
    }

    public String getPreWomenName() {
        return preWomenName;
    }

    public void setPreWomenName(String preWomenName) {
        this.preWomenName = preWomenName;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getAgeofPW() {
        return age;
    }

    public void setAgeofPW(String a) {
        this.age = a;
    }

    public String getPregnant_women_id() {
        return pregnant_women_id;
    }

    public void setPregnant_women_id(String pregnant_women_id) {
        this.pregnant_women_id = pregnant_women_id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEdd() {
        return edd;
    }

    public void setEdd(String edd) {
        this.edd = edd;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getSelect_family() {
        return select_family;
    }

    public void setSelect_family(int select_family) {
        this.select_family = select_family;
    }

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {
        this.server_id = server_id;
    }

    /*public String getName_of_women() {
        return name_of_women;
    }
    public void setName_of_women(String name_of_women) {
        this.name_of_women = name_of_women;
    }*/
    public String getLmp_date() {
        return lmp_date;
    }

    public void setLmp_date(String lmp_date) {
        this.lmp_date = lmp_date;
    }

    public String getPlace_of_delivery() {
        return place_of_delivery;
    }

    public void setPlace_of_delivery(String place_of_delivery) {
        this.place_of_delivery = place_of_delivery;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getC_date() {
        return c_date;
    }

    public void setC_date(String c_date) {
        this.c_date = c_date;
    }

    public String getHb() {
        return hb;
    }

    public void setHb(String hb) {
        this.hb = hb;
    }
}