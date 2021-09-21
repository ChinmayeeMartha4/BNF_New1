package com.example.mhealth.helper;

public class sevika_helper {
    String ctr, userid, anganwadiid, sevikastatus, helperstatus, longitude, lattitude, appversion, dateofstatus;

    public void setctr(String uid) {
        this.ctr = uid;
    }

    public String getctr() {
        return ctr;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String uid) {
        this.userid = uid;
    }

    public void setanganwadiid(String anganwadi) {
        this.anganwadiid = anganwadi;
    }

    public String getanganwadiid() {
        return anganwadiid;
    }

    public void setdateofstatus(String dateofstatus) {
        this.dateofstatus = dateofstatus;
    }

    public String getdateofstatus() {
        return dateofstatus;
    }

    public void setsevikastatus(String sevikastatus) {
        this.sevikastatus = sevikastatus;
    }

    public String getsevikastatus() {
        return sevikastatus;
    }

    public void sethelperstatus(String helperstatus) {
        this.helperstatus = helperstatus;
    }

    public String gethelperstatus() {
        return helperstatus;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public void setlattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getlattitude() {
        return lattitude;
    }

    public void setappversion(String appversion) {
        this.appversion = appversion;
    }

    public String getappversion() {
        return appversion;
    }

}
