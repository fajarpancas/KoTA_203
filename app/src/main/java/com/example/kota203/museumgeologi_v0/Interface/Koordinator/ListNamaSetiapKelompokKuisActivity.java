package com.example.kota203.museumgeologi_v0.Interface.Koordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Model.Klasifikasi;
import com.example.kota203.museumgeologi_v0.Model.Peserta;
import com.example.kota203.museumgeologi_v0.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListNamaSetiapKelompokKuisActivity extends AppCompatActivity {
    private TextView textViewKelompok;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbpeserta = db.collection("peserta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nama_kelompok);

        Intent intent = getIntent();
        final String nama_koor = intent.getStringExtra("NAMA_KOOR");
        final String id_koor = intent.getStringExtra("ID_KOOR");
        int kelompok = getIntent().getExtras().getInt("KELOMPOK");
//        Toast.makeText(ListNamaSetiapKelompokKuisActivity.this, StringKelompok, Toast.LENGTH_SHORT).show();

        TextView textViewNama = (TextView)findViewById(R.id.nama_koordinator_db);
        TextView textViewId = (TextView)findViewById(R.id.id_koordinator_db);
        TextView textViewKelompokKe = (TextView)findViewById(R.id.kelompok_ke);
        textViewKelompok = (TextView)findViewById(R.id.kelompok);

        textViewNama.setText("Hi, "+ nama_koor);
        textViewId.setText("Kode Permainan : "+ id_koor);
        textViewKelompokKe.setText("Kelompok" + kelompok);

        getDataPesertabyKelompok(kelompok, id_koor);
    }

    public void getDataPesertabyKelompok(int kelompok,String id_koor) {
        dbpeserta.whereEqualTo("kelompok", kelompok).whereEqualTo("id_koordinator", id_koor)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        int i = 1;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Peserta peserta = documentSnapshot.toObject(Peserta.class);

                            String nama = peserta.getNama_peserta();

                            data += i + ". " + nama + "\n\n";
                            i++;
                        }
                        textViewKelompok.setText(data);
                    }
                });
    }
}
