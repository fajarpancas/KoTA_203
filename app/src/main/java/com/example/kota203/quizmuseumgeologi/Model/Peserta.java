package com.example.kota203.quizmuseumgeologi.Model;

public class Peserta {

    private String id_peserta;
    private String nama_peserta;
    private String kode_permainan;

    public Peserta() {

    }

    public Peserta(String id_peserta, String nama_peserta) {
        this.id_peserta = id_peserta;
        this.nama_peserta = nama_peserta;
//        this.kode_permainan = kode_permainan;
    }

    public String getNamaPeserta() {
        return nama_peserta;
    }

    public void setNamaPeserta(String nama_peserta) {
        this.nama_peserta = nama_peserta;
    }

    public String getIdPeserta() {
        return id_peserta;
    }

    public void setIdPeserta(String nama_peserta) {
        this.id_peserta = id_peserta;
    }

    public String getKodePermainan() {
        return kode_permainan;
    }

    public void setKodePermainan(String kode_peserta) {
        this.kode_permainan = kode_permainan;
    }

}