package com.example.mhealth.helper;

public class Utility {

    public Utility() {

    }


    public static String[] split(String Data) {
        // TODO Auto-generated method stub
        String[] Edd = Data.split("\\(");
        return Edd;
    }

    public static String[] splitBySpace(String Data) {
        // TODO Auto-generated method stub
        String[] Edd = Data.split("\\ ");
        return Edd;
    }


}
