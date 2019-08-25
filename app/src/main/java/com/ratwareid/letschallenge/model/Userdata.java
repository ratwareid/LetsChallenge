package com.ratwareid.letschallenge.model;

public class Userdata {
    private String alamat,bio,email,nama,no_tlp,photo;
    private Long status;

    public Userdata(){}

    public Userdata(String alamat,String bio,String email,String nama,String no_tlp,String photo,Long status){
        this.alamat = alamat;
        this.bio = bio;
        this.email = email;
        this.nama = nama;
        this.no_tlp = no_tlp;
        this.photo = photo;
        this.status = status;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNo_tlp() {
        return no_tlp;
    }

    public void setNo_tlp(String no_tlp) {
        this.no_tlp = no_tlp;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}
