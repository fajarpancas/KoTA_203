package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Koordinator {
    private String id_koordinator;
    private String namaKoor;
//    private String status_kuis;
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

    public Koordinator(String id_koordinator, String namaKoor){
        this.id_koordinator = id_koordinator;
        this.namaKoor = namaKoor;
//        this.status_kuis = status_kuis;
    }

    public String getId_koordinator() {
        return id_koordinator;
    }

    public void setId_koordinator(String id_koordinator) {
        this.id_koordinator = id_koordinator;
    }

    public String getNamaKoor() {
        return namaKoor;
    }

    public void setNamaKoor(String namaKoor) {
        this.namaKoor = namaKoor;
    }

//    public String getStatus_kuis() {
//        return status_kuis;
//    }
//
//    public void setStatus_kuis(String status_kuis) {
//        this.status_kuis = status_kuis;
//    }
}
