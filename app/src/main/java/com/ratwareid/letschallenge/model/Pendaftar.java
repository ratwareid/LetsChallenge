package com.ratwareid.letschallenge.model;

//***********************************//
// Created by Jerry Erlangga         //
// My Repo: www.github.com/ratwareid //
// Email : jerryerlangga82@gmail.com //
//***********************************//


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Pendaftar implements Serializable {

    private String loginid,email,kodependaftaran,fHadir;

    public Pendaftar(){}

    public Pendaftar(String loginid,String email,String kodependaftaran,String fHadir){
        this.loginid = loginid;
        this.email = email;
        this.kodependaftaran = kodependaftaran;
        this.fHadir = fHadir;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKodependaftaran() {
        return kodependaftaran;
    }

    public void setKodependaftaran(String kodependaftaran) {
        this.kodependaftaran = kodependaftaran;
    }

    public String getfHadir() {
        return fHadir;
    }

    public void setfHadir(String fHadir) {
        this.fHadir = fHadir;
    }
}
