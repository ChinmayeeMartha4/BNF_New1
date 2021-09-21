package com.example.mhealth.helper;

public class SpinnerHelper {
    String spinnerText;
    String value;

    public SpinnerHelper(String spinnerText, String value) {
        this.spinnerText = spinnerText;
        this.value = value;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return spinnerText;
    }
}