package com.ratwareid.letschallenge.model;

public class Lomba {
    private String alamat_lomba,deskripsi_lomba,nama_lomba,penyelenggara,pilihan_lomba,tanggal_lomba,jenis_lomba;
    private String logo_lomba;
    private Long biaya_pendaftaran,id_lomba,jumlah_peserta,total_hadiah;
    private String key;

    public Lomba(){ }

    public Lomba(String alamat_lomba,String deskripsi_lomba,String nama_lomba,String penyelenggara,String pilihan_lomba,String tanggal_lomba,String jenis_lomba,
                    Long biaya_pendaftaran,Long id_lomba,Long jumlah_peserta,Long total_hadiah,String logo_lomba){
        this.alamat_lomba = alamat_lomba;
        this.deskripsi_lomba = deskripsi_lomba;
        this.nama_lomba = nama_lomba;
        this.penyelenggara = penyelenggara;
        this.pilihan_lomba = pilihan_lomba;
        this.tanggal_lomba = tanggal_lomba;
        this.jenis_lomba = jenis_lomba;
        this.logo_lomba = logo_lomba;
        this.biaya_pendaftaran = biaya_pendaftaran;
        this.id_lomba = id_lomba;
        this.jumlah_peserta = jumlah_peserta;
        this.total_hadiah = total_hadiah;
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

    public String getPilihan_lomba() {
        return pilihan_lomba;
    }

    public void setPilihan_lomba(String pilihan_lomba) {
        this.pilihan_lomba = pilihan_lomba;
    }

    public Long getBiaya_pendaftaran() {
        return biaya_pendaftaran;
    }

    public void setBiaya_pendaftaran(Long biaya_pendaftaran) {
        this.biaya_pendaftaran = biaya_pendaftaran;
    }

    public Long getId_lomba() {
        return id_lomba;
    }

    public void setId_lomba(Long id_lomba) {
        this.id_lomba = id_lomba;
    }

    public Long getJumlah_peserta() {
        return jumlah_peserta;
    }

    public void setJumlah_peserta(Long jumlah_peserta) {
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
}
