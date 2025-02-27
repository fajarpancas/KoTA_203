package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Alur {
    private String id_koordinator;
    private int kelompok;
    private int alur;
    private int alur_ke;
    private String documentId;

    public Alur(){
        //
    }

    public Alur(String id_koordinator, int kelompok, int alur, int alur_ke){
        this.id_koordinator = id_koordinator;
        this.kelompok = kelompok;
        this.alur = alur;
        this.alur_ke = alur_ke;
    }

    public String getId_koordinator() {
        return id_koordinator;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setId_koordinator(String id_koordinator) {
        this.id_koordinator = id_koordinator;
    }

    public int getKelompok() {
        return kelompok;
    }

    public void setKelompok(int kelompok) {
        this.kelompok = kelompok;
    }

    public int getAlur() {
        return alur;
    }

    public void setAlur(int alur) {
        this.alur = alur;
    }

    public int getAlur_ke() {
        return alur_ke;
    }

    public void setAlur_ke(int alur_ke) {
        this.alur_ke = alur_ke;
    }
}
