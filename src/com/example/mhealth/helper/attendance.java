package com.example.mhealth.helper;

public class attendance {
    String UserID, AnganwadiID, Date, inTime, outTime, inLatitude,
            inLongitude, outLatitude, outLongitude, count, serverid;

    int status;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String uid) {
        this.UserID = uid;
    }

    public String getAnganwadiID() {
        return AnganwadiID;
    }

    public void setAnganwadiID(String awid) {
        this.AnganwadiID = awid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public void setinTime(String intime) {
        this.inTime = intime;
    }

    public String getinTime() {
        return inTime;
    }

    public void setoutTime(String outtime) {
        this.outTime = outtime;
    }

    public String getoutTime() {
        return outTime;
    }

    public void setinLatitude(String inlat) {
        this.inLatitude = inlat;
    }

    public String getinLatitude() {
        return inLatitude;
    }

    public void setinLongitude(String inlong) {
        this.inLongitude = inlong;
    }

    public String getinLongitude() {
        return inLongitude;
    }

    public void setoutLatitude(String outlat) {
        this.outLatitude = outlat;
    }

    public String getoutLatitude() {
        return outLatitude;
    }

    public void setoutLongitude(String outlong) {
        this.outLongitude = outlong;
    }

    public String getoutLongitude() {
        return outLongitude;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getServerID() {
        return serverid;
    }

    public void setServerID(String serv) {
        this.serverid = serv;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int stat) {
        this.status = stat;
    }
}
