package com.example.mhealth.rest_apis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface M_Health_API {
    @POST(WebApis.LOGIN)
    Call<JsonObject> loginApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_ANGANWADI_CENTER)
    Call<JsonArray> callAnganwadiCenterApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_STATE)
    Call<JsonArray> callStateApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_DISTRICT)
    Call<JsonArray> callDistrictApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_BLOCK)
    Call<JsonArray> callBlockApi(@Body RequestBody body);

    @POST(WebApis.HOUSEHOLD_REGISTRATION)
    Call<JsonObject> callHouseHoldApi(@Body RequestBody body);

    @POST(WebApis.MOTHER_REGISTRATION)
    Call<JsonObject> callMotherApi(@Body RequestBody body);

    @POST(WebApis.PREGNANT_REGISTRATION)
    Call<JsonObject> callPregnantApi(@Body RequestBody body);

    @POST(WebApis.ADOLESCENT_REGISTRATION)
    Call<JsonObject> callAdolescentApi(@Body RequestBody body);

    @POST(WebApis.CHILD_REGISTRATION)
    Call<JsonObject> callChildApi(@Body RequestBody body);

    @POST(WebApis.CHILD_BEHAVIOUR_CHANGE)
    Call<JsonObject> callChildBehabiourChange(@Body RequestBody body);

    @POST(WebApis.CHILD_MONITORING)
    Call<JsonObject> callChildMonitoringApi(@Body RequestBody body);

    @POST(WebApis.PREGNANT_MONITORING)
    Call<JsonObject> callPregnantMonitoringApi(@Body RequestBody body);

    @POST(WebApis.ADOLESCENT_MONITORING)
    Call<JsonObject> callAdolescentMonitoringApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_HOUSEHOLD)
    Call<JsonArray> callDownloadHouseholdApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_PREGNANT)
    Call<JsonArray> callDownloadPregnantApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_MOTHER)
    Call<JsonArray> callDownloadMotherApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_PREGNANT_MONITORING)
    Call<JsonArray> callDownloadPregnantMonitoringApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_CHILD)
    Call<JsonArray> callDownloadChildApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_CHILD_MONITORING)
    Call<JsonArray> callDownloadChildMonitoringApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_ADOLESCENT)
    Call<JsonArray> callDownloadAdolescentApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_ADOLESCENT_MONITORING)
    Call<JsonArray> callDownloadAdolescentMonitoringApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_EVENT_MEETING)
    Call<JsonArray> callDownloadEventMeetingApi(@Body RequestBody body);

    @POST(WebApis.DOWNLOAD_ADD_EVENT)
    Call<JsonObject> callADDEvent(@Body RequestBody body);
}
