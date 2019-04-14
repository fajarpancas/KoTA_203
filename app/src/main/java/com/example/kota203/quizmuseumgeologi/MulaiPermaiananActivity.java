package com.example.kota203.quizmuseumgeologi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kota203.quizmuseumgeologi.Model.Permainan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MulaiPermaiananActivity extends AppCompatActivity {

    Button btn_mulai_permainan_en;
    String enable = "true";

    FirebaseDatabase database;
    DatabaseReference permainan, guru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulai_permainan);

        database = FirebaseDatabase.getInstance();
        permainan = database.getReference("Permainan");

        Intent intent = getIntent();
        final String textNama = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_NAMA);
        final String textKode = intent.getStringExtra(LoginGuruActivity.EXTRA_TEXT_KODE);

//        TextView textViewNama = (TextView)findViewById(R.id.nama_guru_db_mulai);
//        TextView textViewKode = (TextView)findViewById(R.id.kode_permainan_db_mulai);
//
//        textViewNama.setText("Hi, "+ textNama);
//        textViewKode.setText("Kode Permainan : "+ textKode);

        btn_mulai_permainan_en = (Button) findViewById(R.id.mulai_permainan_enable);
        btn_mulai_permainan_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToDB(textKode, textNama);
            }
        });
    }

    private void setToDB(String textKode, String textNama) {
        final Permainan permainan1 = new Permainan(textKode, textNama, enable);
        permainan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                permainan.child(permainan1.getKodePermainan()).setValue(permainan1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
