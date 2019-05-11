package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Koordinator;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class LoginKoordinatorActivity extends AppCompatActivity {
//    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
//    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    MaterialEditText namaKoor, kodeKoor;
    String id_koordinator;
    String status = "belum mulai";
    Button btn_sign_in_koordinator;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference permainan = db.collection("permainan");
    private CollectionReference dbkoordinator = db.collection("koordinator");
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_koordinator);

        namaKoor = (MaterialEditText)findViewById(R.id.namaKoor);
        kodeKoor = (MaterialEditText)findViewById(R.id.kodeKoor);

        btn_sign_in_koordinator = (Button)findViewById(R.id.btn_sign_in_koordinator);

        btn_sign_in_koordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_koordinator = generateString(4);
                Toast.makeText(LoginKoordinatorActivity.this, id_koordinator, Toast.LENGTH_SHORT).show();
                signIn(namaKoor.getText().toString(), kodeKoor.getText().toString());
            }
        });
    }

    private void signIn(final String namaKoor, final String kodeKoor) {
        permainan.whereEqualTo("kode_verifikasi", kodeKoor)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!namaKoor.isEmpty()) {
                        if (!kodeKoor.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Permainan kodeVerifikasi = documentSnapshot.toObject(Permainan.class);
                                String kodeVer = kodeVerifikasi.getKode_verifikasi();
                                if (!kodeVer.equals(kodeKoor)) {
                                    Toast.makeText(LoginKoordinatorActivity.this, "Kode Salah", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginKoordinatorActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    addNametoDB(namaKoor);
//                                    addKoortoKuisDB(namaKoor);
                                    openActivityManajemenKuis(namaKoor);
                                }
                            }
                        } else {
                            Toast.makeText(LoginKoordinatorActivity.this, "Kode harus diisi", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if(!kodeKoor.isEmpty()){
                            Toast.makeText(LoginKoordinatorActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginKoordinatorActivity.this, "Nama dan Kode harus diisi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    private void openActivityManajemenKuis(String namaKoor) {
        Intent intent = new Intent(LoginKoordinatorActivity.this, ManajemenKuisActivity.class);
        intent.putExtra("ID_KOOR", id_koordinator);
        intent.putExtra("NAMA_KOOR", namaKoor);
        startActivity(intent);
    }

    private String generateString(int lenght){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<lenght; i++){
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private void addNametoDB(String namaKoor){
        final Koordinator koordinator = new Koordinator(id_koordinator, namaKoor);
        dbkoordinator.add(koordinator);
    }

//    private void addKoortoKuisDB(String namaKoor) {
//        final KuisKoordinator koordinator = new KuisKoordinator(id_koordinator, status);
//        dbkuis.add(koordinator);
//    }
}
