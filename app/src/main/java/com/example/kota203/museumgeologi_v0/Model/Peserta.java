package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Peserta {
    private String id_koordinator;
    private String id_peserta;
    private String nama_peserta;
    private Integer kelompok;
    private Integer poin;
    private String documentId;

    public Peserta(){
        //no needed construct
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Peserta(String id_koordinator, String id_peserta, String nama_peserta, Integer kelompok, Integer poin){
        this.id_koordinator = id_koordinator;
        this.id_peserta = id_peserta;
        this.nama_peserta = nama_peserta;
        this.kelompok = kelompok;
        this.poin = poin;
    }

    public Peserta(String id_koordinator, String id_peserta, String nama_peserta, Integer kelompok){
        this.id_koordinator = id_koordinator;
        this.id_peserta = id_peserta;
        this.nama_peserta = nama_peserta;
        this.kelompok = kelompok;
    }

    public Peserta(String id_koordinator, String id_peserta, String nama_peserta){
        this.id_koordinator = id_koordinator;
        this.id_peserta = id_peserta;
        this.nama_peserta = nama_peserta;
    }

    public String getId_koordinator() {
        return id_koordinator;
    }

    public void setId_koordinator(String id_koordinator) {
        this.id_koordinator = id_koordinator;
    }

    public String getId_peserta() {
        return id_peserta;
    }

    public void setId_peserta(String id_peserta) {
        this.id_peserta = id_peserta;
    }

    public String getNama_peserta() {
        return nama_peserta;
    }

    public void setNama_peserta(String nama_peserta) {
        this.nama_peserta = nama_peserta;
    }

    public Integer getKelompok() {
        return kelompok;
    }

    public void setKelompok(Integer kelompok) {
        this.kelompok = kelompok;
    }

    public Integer getPoin() {
        return poin;
    }

    public void setPoin(Integer poin) {
        this.poin = poin;
    }
}
