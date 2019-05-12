package com.example.kota203.museumgeologi_v0.Interface.Peserta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Kuis;
import com.example.kota203.museumgeologi_v0.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PetunjukRuangActivity extends AppCompatActivity {
    Button btn_lanjutkan;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbkuis = db.collection("kuis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petunjuk_ruang);

        Intent intent = getIntent();
        final String textNamaPeserta = intent.getStringExtra("NAMA_PESERTA");
        final String textIdPeserta = intent.getStringExtra("ID_PESERTA");
        final String textIdKoor = intent.getStringExtra("ID_KOOR");

        TextView textViewKlasifikasi = (TextView) findViewById(R.id.klasifikasi);
        TextView textViewPetunjukRuang = (TextView) findViewById(R.id.petunjuk_ruang);
        TextView textViewNamaPeserta = (TextView) findViewById(R.id.nama_peserta_db);

        textViewNamaPeserta.setText(textNamaPeserta);
        textViewPetunjukRuang.setText("Petunjuk");

        getData(textIdKoor, textViewKlasifikasi);

        btn_lanjutkan = (Button) findViewById(R.id.btn_lanjutkan);
        btn_lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openActivityPetunjukRuang(textNamaPeserta, textIdPeserta, textIdKoor);
                Toast.makeText(PetunjukRuangActivity.this, "lanjut", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void openActivityPetunjukRuang(String textNamaPeserta, String textIdPeserta, String textIdKoor) {
//        Intent intent = new Intent(PetunjukKuisActivity.this, PetunjukRuangActivity.class);
//        intent.putExtra("NAMA_PESERTA", textNamaPeserta);
//        intent.putExtra("ID_PESERTA", textIdPeserta);
//        intent.putExtra("ID_KOOR", textIdKoor);
//        startActivity(intent);
//    }

    private void getData(String textIdKoor, final TextView textViewKlasifikasi) {
        dbkuis.whereEqualTo("id_koordinator", textIdKoor)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    textViewKlasifikasi.setText("Listen Failed");
                    return;
                }
                String klasifikasi = "";
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.get("jenis_klasifikasi") != null) {
                        Kuis data_kuis = doc.toObject(Kuis.class);

                        String jenis_klasifikasi = data_kuis.getJenis_klasifikasi();
                        klasifikasi += "Jenis Klasifikasi Kuis : " +jenis_klasifikasi;
                        textViewKlasifikasi.setText(klasifikasi);
                    }else {
                        textViewKlasifikasi.setText("Belum ditentukan");
                    }
                }
            }
        });
    }
}
