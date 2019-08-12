package com.ratwareid.letschallenge.model;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Jenis implements Serializable {

    private String kodejenis,namajenis,key;
    private String imgjenis;

    public Jenis(){}

    public Jenis(String namajenis,String kodejenis,String imgjenis){
        this.kodejenis = kodejenis;
        this.namajenis = namajenis;
        this.imgjenis = imgjenis;
    }

    public String getImgjenis() {
        return imgjenis;
    }

    public void setImgjenis(String imgjenis) {
        this.imgjenis = imgjenis;
    }

    public String getKodejenis() {
        return kodejenis;
    }

    public void setKodejenis(String kodejenis) {
        this.kodejenis = kodejenis;
    }

    public String getNamajenis() {
        return namajenis;
    }

    public void setNamajenis(String namajenis) {
        this.namajenis = namajenis;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+namajenis+"\n" +
                " "+kodejenis;
    }
}
