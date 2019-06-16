package com.example.kota203.museumgeologi_v0.Interface.Input;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Koleksi;
import com.example.kota203.museumgeologi_v0.Model.Soal;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

public class InputSoal extends AppCompatActivity {
    MaterialEditText idSoal, idKlasifikasi, jenisKlasifikasi, questionType, questionImg, questionTxt, answerA, answerB, answerC, answerD, correctAnswer;
    Button btn_input_data;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbsoal = db.collection("soal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_soal);

        idSoal = (MaterialEditText)findViewById(R.id.idSoal);
        idKlasifikasi = (MaterialEditText)findViewById(R.id.idKlas);
        jenisKlasifikasi = (MaterialEditText)findViewById(R.id.jenisKlasifikasi);
        questionType = (MaterialEditText)findViewById(R.id.questionType);
        questionImg = (MaterialEditText)findViewById(R.id.imgQuestion);
        questionTxt = (MaterialEditText)findViewById(R.id.txtQuestion);
        answerA = (MaterialEditText)findViewById(R.id.answerA);
        answerB = (MaterialEditText)findViewById(R.id.answerB);
        answerC = (MaterialEditText)findViewById(R.id.answerC);
        answerD = (MaterialEditText)findViewById(R.id.answerD);
        correctAnswer = (MaterialEditText)findViewById(R.id.correctAnswer);

        btn_input_data = (Button)findViewById(R.id.btn_input_data_soal);
        btn_input_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData(idSoal.getText().toString(), idKlasifikasi.getText().toString(), jenisKlasifikasi.getText().toString(),
                        questionType.getText().toString(), questionImg.getText().toString(), questionTxt.getText().toString(), answerA.getText().toString(),
                        answerB.getText().toString(), answerC.getText().toString(), answerD.getText().toString(), correctAnswer.getText().toString());
            }
        });

    }

    private void inputData(String idSoal, String idKlasifikasi, String jenisKlas, String questionType, String questionImg, String questionTxt,
                           String answerA, String answerB, String answerC, String answerD, String correctAnswer) {

        int IDsoal = Integer.parseInt(idSoal);
        int IDklas = Integer.parseInt(idKlasifikasi);
        if(!idSoal.isEmpty() || !idKlasifikasi.isEmpty() || !jenisKlas.isEmpty() || !questionType.isEmpty() || !answerA.isEmpty() || !answerB.isEmpty()
                || !answerC.isEmpty() || !answerD.isEmpty() || !correctAnswer.isEmpty()){
            Soal soal = new Soal(IDsoal, IDklas, jenisKlas, questionType, questionImg, questionTxt, answerA, answerB, answerC, answerD, correctAnswer);
            dbsoal.add(soal);
            Toast.makeText(this, "Input Data Berhasil", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InputSoal.this, PilihInput.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Data Belum Terisi Semua", Toast.LENGTH_LONG).show();
        }
    }
}
