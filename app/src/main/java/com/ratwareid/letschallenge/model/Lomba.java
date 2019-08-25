package com.ratwareid.letschallenge.model;

import java.util.HashMap;

public class Lomba {
    private String alamat_lomba,deskripsi_lomba,nama_lomba,penyelenggara,tanggal_lomba,jenis_lomba;
    private String logo_lomba,jumlah_peserta,nama_jenis;
    private Long biaya_pendaftaran,total_hadiah;
    private HashMap<String,String> pendaftar;
    private String key;

    public Lomba(){}

    public Lomba(String alamat_lomba,String deskripsi_lomba,String nama_lomba,String penyelenggara,String tanggal_lomba,String jenis_lomba,
                    Long biaya_pendaftaran,String jumlah_peserta,Long total_hadiah,String logo_lomba,String nama_jenis, HashMap<String,String> pendaftar){
        this.alamat_lomba = alamat_lomba;
        this.deskripsi_lomba = deskripsi_lomba;
        this.nama_lomba = nama_lomba;
        this.penyelenggara = penyelenggara;
        this.tanggal_lomba = tanggal_lomba;
        this.jenis_lomba = jenis_lomba;
        this.logo_lomba = logo_lomba;
        this.biaya_pendaftaran = biaya_pendaftaran;
        this.jumlah_peserta = jumlah_peserta;
        this.total_hadiah = total_hadiah;
        this.nama_jenis = nama_jenis;
        this.pendaftar =  new HashMap<>();
        if (pendaftar != null && pendaftar.size() > 0){
            this.pendaftar.putAll(pendaftar);
        }
    }

    public String getAlamat_lomba() {
        return alamat_lomba;
    }

    public void setAlamat_lomba(String alamat_lomba) {
        this.alamat_lomba = alamat_lomba;
    }

    public String getDeskripsi_lomba() {
        return deskripsi_lomba;
    }

    public void setDeskripsi_lomba(String deskripsi_lomba) {
        this.deskripsi_lomba = deskripsi_lomba;
    }

    public String getNama_lomba() {
        return nama_lomba;
    }

    public void setNama_lomba(String nama_lomba) {
        this.nama_lomba = nama_lomba;
    }

    public String getPenyelenggara() {
        return penyelenggara;
    }

    public void setPenyelenggara(String penyelenggara) {
        this.penyelenggara = penyelenggara;
    }

    public Long getBiaya_pendaftaran() {
        return biaya_pendaftaran;
    }

    public void setBiaya_pendaftaran(Long biaya_pendaftaran) {
        this.biaya_pendaftaran = biaya_pendaftaran;
    }

    public String getJumlah_peserta() {
        return jumlah_peserta;
    }

    public void setJumlah_peserta(String jumlah_peserta) {
        this.jumlah_peserta = jumlah_peserta;
    }

    public Long getTotal_hadiah() {
        return total_hadiah;
    }

    public void setTotal_hadiah(Long total_hadiah) {
        this.total_hadiah = total_hadiah;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTanggal_lomba() {
        return tanggal_lomba;
    }

    public void setTanggal_lomba(String tanggal_lomba) {
        this.tanggal_lomba = tanggal_lomba;
    }

    public String getJenis_lomba() {
        return jenis_lomba;
    }

    public void setJenis_lomba(String jenis_lomba) {
        this.jenis_lomba = jenis_lomba;
    }

    public String getLogo_lomba() {
        return logo_lomba;
    }

    public void setLogo_lomba(String logo_lomba) {
        this.logo_lomba = logo_lomba;
    }

    public String getNama_jenis() {
        return nama_jenis;
    }

    public void setNama_jenis(String nama_jenis) {
        this.nama_jenis = nama_jenis;
    }

    public HashMap<String, String> getPendaftar() {
        return pendaftar;
    }

    public void setPendaftar(HashMap<String, String> pendaftar) {
        this.pendaftar = pendaftar;
    }
}
