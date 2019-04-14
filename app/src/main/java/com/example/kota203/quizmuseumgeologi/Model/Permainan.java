package com.example.kota203.quizmuseumgeologi.Model;

public class Permainan {

    private String kode_permainan;
    private String nama_guru;
    private String btn_enable;

    public Permainan() {

    }

//    public Permainan(String kode_permainan, String nama_guru) {
//        this.kode_permainan = kode_permainan;
//        this.nama_guru = nama_guru;
//    }

    public Permainan(String kode_permainan, String nama_guru, String btn_enable){
        this.kode_permainan = kode_permainan;
        this.nama_guru = nama_guru;
        this.btn_enable = btn_enable;
    }

    public String getKodePermainan() {
        return kode_permainan;
    }

    public void setKodePermainan(String kode_permainan) {
        this.kode_permainan = kode_permainan;
    }

    public String getNamaGuru() {
        return nama_guru;
    }

    public void setNamaGuru(String nama_guru) {
        this.nama_guru = nama_guru;
    }

    public String getEnable() {
        return btn_enable;
    }

    public void setEnable(String btn_enable) {
        this.btn_enable = btn_enable;
    }

}