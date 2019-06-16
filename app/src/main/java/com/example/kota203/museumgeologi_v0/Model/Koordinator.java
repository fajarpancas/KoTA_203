package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Koordinator {
    private String id_koordinator;
    private String nama_koordinator;
    private String nama_sekolah;
    String documentId;

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Koordinator(){
        //no needed construct
    }

    public Koordinator(String id_koordinator, String nama_koordinator, String nama_sekolah){
        this.id_koordinator = id_koordinator;
        this.nama_koordinator = nama_koordinator;
        this.nama_sekolah = nama_sekolah;
    }

    public String getId_koordinator() {
        return id_koordinator;
    }

    public void setId_koordinator(String id_koordinator) {
        this.id_koordinator = id_koordinator;
    }

    public String getNama_koordinator() {
        return nama_koordinator;
    }

    public void setNama_koordinator(String nama_koordinator) {
        this.nama_koordinator = nama_koordinator;
    }

    public String getNama_sekolah() {
        return nama_sekolah;
    }

    public void setNama_sekolah(String nama_sekolah) {
        this.nama_sekolah = nama_sekolah;
    }
}
