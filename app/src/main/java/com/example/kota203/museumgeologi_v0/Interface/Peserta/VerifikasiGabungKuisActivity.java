package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class VerifikasiGabungKuisActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    MaterialEditText nama_peserta, id_koordinator;
    Button btn_sign_in_peserta;
    String id_peserta;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference db_kuis = db.collection("kuis");
    private CollectionReference db_peserta = db.collection("peserta");
    private CollectionReference db_koordinator = db.collection("koordinator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_gabung_kuis);

        nama_peserta = (MaterialEditText)findViewById(R.id.form_nama_peserta);
        id_koordinator = (MaterialEditText)findViewById(R.id.form_id_koordinator);

        btn_sign_in_peserta = (Button)findViewById(R.id.btn_sign_in_peserta);

        btn_sign_in_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInPeserta(nama_peserta.getText().toString(), id_koordinator.getText().toString());
            }
        });
    }

    private void signInPeserta(final String nama_peserta, final String id_koordinator) {
        if(!nama_peserta.isEmpty()) {
            if (!id_koordinator.isEmpty()) {
                db_kuis.whereEqualTo("id_koordinator", id_koordinator)
                    .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Kuis data_kuis = documentSnapshot.toObject(Kuis.class);
                                String idKoordinator = data_kuis.getId_koordinator();
                                String statusKuis = data_kuis.getStatus_kuis();

                                if (!idKoordinator.equals(id_koordinator)) {
                                    Toast.makeText(VerifikasiGabungKuisActivity.this, "Kode verifikasi yang anda masukkan salah", Toast.LENGTH_SHORT).show();
                                } else if(statusKuis.equals("Belum manajemen")){
                                    Toast.makeText(VerifikasiGabungKuisActivity.this, "Gabung Kuis Berhasil", Toast.LENGTH_SHORT).show();
                                    id_peserta = generate_id_peserta(4);
                                    setDataPesertatoDB(id_koordinator, nama_peserta);
                                    openActivityInfoKelompok(nama_peserta, id_koordinator);
                                } else if(idKoordinator.equals(id_koordinator) && !statusKuis.equals("Belum manajemen")){
                                    Toast.makeText(VerifikasiGabungKuisActivity.this, "Anda tidak dapat bergabung kuis dikarenakan kuis sudah dimulai", Toast.LENGTH_SHORT).show();
                                }
                            }
                            }
                        });
            } else Toast.makeText(VerifikasiGabungKuisActivity.this, "Kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
        } else if(!id_koordinator.isEmpty()){
            Toast.makeText(VerifikasiGabungKuisActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(VerifikasiGabungKuisActivity.this, "Nama dan kode verifikasi harus diisi", Toast.LENGTH_SHORT).show();
    }

    private void openActivityInfoKelompok(String nama_peserta, String id_koordinator) {
//        Toast.makeText(VerifikasiGabungKuisActivity.this, nama_peserta + id_peserta + id_koordinator, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerifikasiGabungKuisActivity.this, InfoKelompokActivity.class);
        intent.putExtra("NAMA_PESERTA", nama_peserta);
        intent.putExtra("ID_PESERTA", id_peserta);
        intent.putExtra("ID_KOOR", id_koordinator);
        startActivity(intent);
    }

    private String generate_id_peserta(int lenght){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void setDataPesertatoDB(String id_koordinator, final String nama_peserta) {
        final Peserta peserta = new Peserta(id_koordinator, id_peserta, nama_peserta);
        db_peserta.add(peserta);
    }
}

