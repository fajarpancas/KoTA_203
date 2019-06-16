package com.example.kota203.museumgeologi_v0.Model;

import com.google.firebase.firestore.Exclude;

public class Koleksi{
    int id_ruang;
    int id_klasifikasi;
    int id_koleksi;
    String nama_koleksi;
    String deskripsi_koleksi;
    String gambar;
    String DokumenId;

    public Koleksi(){

    }

    @Exclude
    public String getDokumenId() {
        return DokumenId;
    }

    public void setDokumenId(String dokumenId) {
        DokumenId = dokumenId;
    }

    public Koleksi(int id_ruang, int id_klasifikasi, int id_koleksi, String nama_koleksi, String deskripsi_koleksi, String gambar) {
        this.id_ruang = id_ruang;
        this.id_klasifikasi = id_klasifikasi;
        this.id_koleksi = id_koleksi;
        this.nama_koleksi = nama_koleksi;
        this.deskripsi_koleksi = deskripsi_koleksi;
        this.gambar = gambar;
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

    public int getId_koleksi() {
        return id_koleksi;
    }

    public void setId_koleksi(int id_koleksi) {
        this.id_koleksi = id_koleksi;
    }

    public String getNama_koleksi() {
        return nama_koleksi;
    }

    public void setNama_koleksi(String nama_koleksi) {
        this.nama_koleksi = nama_koleksi;
    }

    public String getDeskripsi_koleksi() {
        return deskripsi_koleksi;
    }

    public void setDeskripsi_koleksi(String deskripsi_koleksi) {
        this.deskripsi_koleksi = deskripsi_koleksi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}

