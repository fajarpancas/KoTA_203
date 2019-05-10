package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Klasifikasi {
    private String documentId;
    private int id_ruang;
    private int id_klasifikasi;
    private String nama_klasifikasi;

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Klasifikasi() {
    //no needed constructor
    }

    public Klasifikasi(int id_ruang, int id_klasifikasi, String nama_klasifikasi) {
        this.id_ruang = id_ruang;
        this.id_klasifikasi = id_klasifikasi;
        this.nama_klasifikasi = nama_klasifikasi;
    }

    public int getId_ruang() {
        return id_ruang;
    }

    public void setId_ruang(int id_ruang) {
        this.id_ruang = id_ruang;
    }

    public int getId_klasifikasi() {
        return id_klasifikasi;
    }

    public void setId_klasifikasi(int id_klasifikasi) {
        this.id_klasifikasi = id_klasifikasi;
    }

    public String getNama_klasifikasi() {
        return nama_klasifikasi;
    }

    public void setNama_klasifikasi(String nama_klasifikasi) {
        this.nama_klasifikasi = nama_klasifikasi;
    }
}
