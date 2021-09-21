package com.example.mhealth.helper;

import java.io.Serializable;

public class AdolBaseline implements Serializable {
    String adolName,address,parentName,contactNumber,height,weight,age,MUAC,schoolName,className,uniqueId,villageName,blockName;
    String districtName,heardOfAneamia,sourceOfInfo,whatIsAneamia,whichNutrientDef,causeOfAneamia,signsOfAneamia,effectsOfAneamia,measuresOfAneamia;

    String ironRichFood,moreIronNeeds,youAreAneamic,howSeriousAneamia,getIronTablet,howTakeIronTablet,likeIronTablet,foodTypeConsume,consumePastWeek;

    String anganwadi_center_id;
    String lattitude;
    String longitude;
    String user_master_id;
    String app_version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getAnganwadi_center_id() {
        return anganwadi_center_id;
    }

    public void setAnganwadi_center_id(String anganwadi_center_id) {
        this.anganwadi_center_id = anganwadi_center_id;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUser_master_id() {
        return user_master_id;
    }

    public void setUser_master_id(String user_master_id) {
        this.user_master_id = user_master_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }


    public String getIronRichFood() {
        return ironRichFood;
    }

    public void setIronRichFood(String ironRichFood) {
        this.ironRichFood = ironRichFood;
    }

    public String getMoreIronNeeds() {
        return moreIronNeeds;
    }

    public void setMoreIronNeeds(String moreIronNeeds) {
        this.moreIronNeeds = moreIronNeeds;
    }

    public String getYouAreAneamic() {
        return youAreAneamic;
    }

    public void setYouAreAneamic(String youAreAneamic) {
        this.youAreAneamic = youAreAneamic;
    }

    public String getHowSeriousAneamia() {
        return howSeriousAneamia;
    }

    public void setHowSeriousAneamia(String howSeriousAneamia) {
        this.howSeriousAneamia = howSeriousAneamia;
    }

    public String getGetIronTablet() {
        return getIronTablet;
    }

    public void setGetIronTablet(String getIronTablet) {
        this.getIronTablet = getIronTablet;
    }

    public String getHowTakeIronTablet() {
        return howTakeIronTablet;
    }

    public void setHowTakeIronTablet(String howTakeIronTablet) {
        this.howTakeIronTablet = howTakeIronTablet;
    }

    public String getLikeIronTablet() {
        return likeIronTablet;
    }

    public void setLikeIronTablet(String likeIronTablet) {
        this.likeIronTablet = likeIronTablet;
    }

    public String getFoodTypeConsume() {
        return foodTypeConsume;
    }

    public void setFoodTypeConsume(String foodTypeConsume) {
        this.foodTypeConsume = foodTypeConsume;
    }

    public String getConsumePastWeek() {
        return consumePastWeek;
    }

    public void setConsumePastWeek(String consumePastWeek) {
        this.consumePastWeek = consumePastWeek;
    }

    public String getFrequencyOfConsumption() {
        return frequencyOfConsumption;
    }

    public void setFrequencyOfConsumption(String frequencyOfConsumption) {
        this.frequencyOfConsumption = frequencyOfConsumption;
    }

    public String getPeas() {
        return peas;
    }

    public void setPeas(String peas) {
        this.peas = peas;
    }

    public String getMeat() {
        return meat;
    }

    public void setMeat(String meat) {
        this.meat = meat;
    }

    public String getSeafood() {
        return seafood;
    }

    public void setSeafood(String seafood) {
        this.seafood = seafood;
    }

    public String getEgg() {
        return egg;
    }

    public void setEgg(String egg) {
        this.egg = egg;
    }

    public String getAlmonds() {
        return almonds;
    }

    public void setAlmonds(String almonds) {
        this.almonds = almonds;
    }

    public String getGreenleaf() {
        return greenleaf;
    }

    public void setGreenleaf(String greenleaf) {
        this.greenleaf = greenleaf;
    }

    public String getIncludeLemonInDiet() {
        return includeLemonInDiet;
    }

    public void setIncludeLemonInDiet(String includeLemonInDiet) {
        this.includeLemonInDiet = includeLemonInDiet;
    }

    public String getDewormingTablet() {
        return dewormingTablet;
    }

    public void setDewormingTablet(String dewormingTablet) {
        this.dewormingTablet = dewormingTablet;
    }

    public String getFrequentlyAlbandazoleTablet() {
        return frequentlyAlbandazoleTablet;
    }

    public void setFrequentlyAlbandazoleTablet(String frequentlyAlbandazoleTablet) {
        this.frequentlyAlbandazoleTablet = frequentlyAlbandazoleTablet;
    }

    public String getHadCheckedHBBefore() {
        return hadCheckedHBBefore;
    }

    public void setHadCheckedHBBefore(String hadCheckedHBBefore) {
        this.hadCheckedHBBefore = hadCheckedHBBefore;
    }

    public String getHB() {
        return HB;
    }

    public void setHB(String HB) {
        this.HB = HB;
    }

    String frequencyOfConsumption,peas,meat,seafood,egg,almonds,greenleaf,includeLemonInDiet,dewormingTablet,frequentlyAlbandazoleTablet;
    String hadCheckedHBBefore,HB;
    public String getAdolName() {
        return adolName;
    }

    public void setAdolName(String adolName) {
        this.adolName = adolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMUAC() {
        return MUAC;
    }

    public void setMUAC(String MUAC) {
        this.MUAC = MUAC;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String bockName) {
        this.blockName = bockName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getHeardOfAneamia() {
        return heardOfAneamia;
    }

    public void setHeardOfAneamia(String heardOfAneamia) {
        this.heardOfAneamia = heardOfAneamia;
    }

    public String getSourceOfInfo() {
        return sourceOfInfo;
    }

    public void setSourceOfInfo(String sourceOfInfo) {
        this.sourceOfInfo = sourceOfInfo;
    }

    public String getWhatIsAneamia() {
        return whatIsAneamia;
    }

    public void setWhatIsAneamia(String whatIsAneamia) {
        this.whatIsAneamia = whatIsAneamia;
    }

    public String getWhichNutrientDef() {
        return whichNutrientDef;
    }

    public void setWhichNutrientDef(String whichNutrientDef) {
        this.whichNutrientDef = whichNutrientDef;
    }

    public String getCauseOfAneamia() {
        return causeOfAneamia;
    }

    public void setCauseOfAneamia(String causeOfAneamia) {
        this.causeOfAneamia = causeOfAneamia;
    }

    public String getSignsOfAneamia() {
        return signsOfAneamia;
    }

    public void setSignsOfAneamia(String signsOfAneamia) {
        this.signsOfAneamia = signsOfAneamia;
    }

    public String getEffectsOfAneamia() {
        return effectsOfAneamia;
    }

    public void setEffectsOfAneamia(String effectsOfAneamia) {
        this.effectsOfAneamia = effectsOfAneamia;
    }

    public String getMeasuresOfAneamia() {
        return measuresOfAneamia;
    }

    public void setMeasuresOfAneamia(String measuresOfAneamia) {
        this.measuresOfAneamia = measuresOfAneamia;
    }

}
