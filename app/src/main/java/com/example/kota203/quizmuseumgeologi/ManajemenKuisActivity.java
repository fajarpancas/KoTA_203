package com.example.kota203.quizmuseumgeologi;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.Interface.Peserta.InfoKelompokActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ManajemenKuisActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference permainan, peserta;

    int count= 0;

    Button btn_generate_kelompok, btn_keluar;
    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manajemen_kuis);

        database = FirebaseDatabase.getInstance();
        peserta = database.getReference("Peserta");
        permainan = database.getReference("Permainan");

        Intent intent = getIntent();
        final String textNama = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_NAMA);
        final String textKode = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_KODE);

        TextView textViewNama = (TextView)findViewById(R.id.nama_guru_db);
        TextView textViewKode = (TextView)findViewById(R.id.kode_permainan_db);
        final TextView textViewTotal = (TextView)findViewById(R.id.jumlah_peserta_masuk);

        textViewNama.setText("Hi, "+ textNama);
        textViewKode.setText("Kode Permainan : "+ textKode);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_klasifikasi);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,
                R.array.klasifikasi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinnerkelompok = (Spinner) findViewById(R.id.spinner_kelompok);
        ArrayAdapter<CharSequence> adapterkelompok = ArrayAdapter.createFromResource(ManajemenKuisActivity.this,
                R.array.kelompok_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerkelompok.setAdapter(adapterkelompok);

        spinner.setOnItemSelectedListener(this);
        spinnerkelompok.setOnItemSelectedListener(this);

        btn_generate_kelompok = (Button) findViewById(R.id.btn_generate_kelompok);
        btn_keluar = (Button) findViewById(R.id.btn_keluar);

        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        btn_generate_kelompok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityMulai(textNama, textKode);
            }
        });

        peserta.child(textKode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                textViewTotal.setText(Integer.toString(count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Keluar Permainan ?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(ManajemenKuisActivity.this, "Not Logout", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(ManajemenKuisActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openActivityMulai(String nama, String kode) {
        Intent intent = new Intent(ManajemenKuisActivity.this, MulaiPermaiananActivity.class);
        intent.putExtra(EXTRA_TEXT_KODE, kode);
        intent.putExtra(EXTRA_TEXT_NAMA, nama);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sSelected = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(ManajemenKuisActivity.this,sSelected,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(ManajemenKuisActivity.this,"logout",Toast.LENGTH_SHORT).show();
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }

}
