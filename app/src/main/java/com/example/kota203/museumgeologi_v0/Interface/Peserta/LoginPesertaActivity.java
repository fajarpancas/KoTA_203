package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Koordinator.LoginKoordinatorActivity;
import com.example.kota203.museumgeologi_v0.Model.Koordinator;
import com.example.kota203.museumgeologi_v0.Model.Permainan;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class LoginPesertaActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";
//    public static final String EXTRA_TEXT_IDPESERTA = "";

    MaterialEditText namaPeserta, idKoor;
    Button btn_sign_in_peserta;
    String id_pesertaS;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");
    private CollectionReference dbpeserta = db.collection("peserta");
    private CollectionReference dbkoordinator = db.collection("koordinator");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_peserta);

        namaPeserta = (MaterialEditText)findViewById(R.id.namaPeserta);
        idKoor = (MaterialEditText)findViewById(R.id.idKoor);

        btn_sign_in_peserta = (Button)findViewById(R.id.btn_sign_in_peserta);

        btn_sign_in_peserta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_pesertaS = generate_id_peserta(4);
                signInPeserta(namaPeserta.getText().toString(), idKoor.getText().toString());
            }
        });
    }

    private void signInPeserta(final String namaPeserta, final String idKoor) {
        if(!namaPeserta.isEmpty()) {
            if (!idKoor.isEmpty()) {
                dbkoordinator.whereEqualTo("id_koordinator", idKoor)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Koordinator koordinator = documentSnapshot.toObject(Koordinator.class);
                                String idKoordinator = koordinator.getId_koordinator();

                                if (!idKoordinator.equals(idKoor)) {
                                    Toast.makeText(LoginPesertaActivity.this, "Kode Kuis Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginPesertaActivity.this, "Gabung Kuis Berhasil", Toast.LENGTH_SHORT).show();
                                    setDataPesertatoDB(idKoor, namaPeserta);
//                                getDocIdKuis(idKoor, namaPeserta);
                                    openActivityInfoKelompok(namaPeserta, idKoor);
                                }
                            }
                        }
                    });
            } else Toast.makeText(LoginPesertaActivity.this, "Kode harus diisi", Toast.LENGTH_SHORT).show();
        } else if(!idKoor.isEmpty()){
            Toast.makeText(LoginPesertaActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(LoginPesertaActivity.this, "Nama dan Kode harus diisi", Toast.LENGTH_SHORT).show();
    }

    private void openActivityInfoKelompok(String namaPeserta, String id_koordinator) {
        Toast.makeText(LoginPesertaActivity.this, namaPeserta + id_pesertaS + id_koordinator, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginPesertaActivity.this, InfoKelompokActivity.class);
        intent.putExtra("NAMA_PESERTA", namaPeserta);
        intent.putExtra("ID_PESERTA", id_pesertaS);
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

    private void setDataPesertatoDB(String idKoor, final String namaPeserta) {
        final Peserta peserta = new Peserta(idKoor, id_pesertaS, namaPeserta);
        dbpeserta.add(peserta);
    }

//    private void getDocIdKuis(final String idKoor, final String namaPeserta){
//        dbkuis.whereEqualTo("id_koordinator", idKoor)
//        .get()
//        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    KuisKoordinator docIdKuis = documentSnapshot.toObject(KuisKoordinator.class);
//                    docIdKuis.setDocumentId(documentSnapshot.getId());
//                    String documentId = docIdKuis.getDocumentId();
//                    addPesertatoKuisDB(documentId, namaPeserta );
//                }
//            }
//        });
//    }
//
//    private void addPesertatoKuisDB(String documentId, String namaPeserta) {
//        final Peserta peserta = new Peserta(id_pesertaS, namaPeserta);
//        dbkuis.document(documentId).collection("peserta").add(peserta);
}

