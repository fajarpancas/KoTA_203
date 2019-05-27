package com.example.kota203.museumgeologi_v0.Model;

public class Soal {
    private int id_klasifikasi;
    private String jenis_klasifikasi;
    private int id_soal;
    private String img_question;
    private String txt_question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correctAnswer;

    public Soal(){
        //
    }

    public int getId_klasifikasi() {
        return id_klasifikasi;
    }

    public void setId_klasifikasi(int id_klasifikasi) {
        this.id_klasifikasi = id_klasifikasi;
    }

    public String getJenis_klasifikasi() {
        return jenis_klasifikasi;
    }

    public void setJenis_klasifikasi(String jenis_klasifikasi) {
        this.jenis_klasifikasi = jenis_klasifikasi;
    }

    public int getId_soal() {
        return id_soal;
    }

    public void setId_soal(int id_soal) {
        this.id_soal = id_soal;
    }

    public String getImg_question() {
        return img_question;
    }

    public void setImg_question(String img_question) {
        this.img_question = img_question;
    }

    public String getTxt_question() {
        return txt_question;
    }

    public void setTxt_question(String txt_question) {
        this.txt_question = txt_question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
