package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Kuis {
    private String id_koordinator;
    private Integer jumlah_kelompok;
    private String jenis_klasifikasi;
    private String status_kuis;
    String documentId;

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Kuis(String id_koordinator, Integer jumlah_kelompok, String jenis_klasifikasi, String status_kuis){
        this.id_koordinator = id_koordinator;
        this.jumlah_kelompok = jumlah_kelompok;
        this.jenis_klasifikasi = jenis_klasifikasi;
        this.status_kuis = status_kuis;
    }

    public Kuis(){
        //no needed
    }

    public String getId_koordinator() {
        return id_koordinator;
    }

    public void setId_koordinator(String id_koordinator) {
        this.id_koordinator = id_koordinator;
    }

    public Integer getJumlah_kelompok() {
        return jumlah_kelompok;
    }

    public void setJumlah_kelompok(Integer jumlah_kelompok) {
        this.jumlah_kelompok = jumlah_kelompok;
    }

    public String getJenis_klasifikasi() {
        return jenis_klasifikasi;
    }

    public void setJenis_klasifikasi(String jenis_klasifikasi) {
        this.jenis_klasifikasi = jenis_klasifikasi;
    }

    public String getStatus_kuis() {
        return status_kuis;
    }

    public void setStatus_kuis(String status_kuis) {
        this.status_kuis = status_kuis;
    }
}
