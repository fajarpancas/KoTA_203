package com.example.kota203.quizmuseumgeologi;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.ViewGroupUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.kota203.quizmuseumgeologi.Model.Guru;
import com.example.kota203.quizmuseumgeologi.Model.Permainan;
import com.example.kota203.quizmuseumgeologi.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Random;

public class LoginGuruActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_NAMA = "com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_KODE";

    MaterialEditText entrnama_guru, entr_kode_guru;
    String kode_permainan;
    String enable = "false";
    Button btn_sign_in_guru;

    FirebaseDatabase database;
    DatabaseReference permainan, guru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guru);

        database = FirebaseDatabase.getInstance();
        guru = database.getReference("Guru");
        permainan = database.getReference("Permainan");

        entrnama_guru = (MaterialEditText)findViewById(R.id.entrnama_guru);
        entr_kode_guru = (MaterialEditText)findViewById(R.id.entr_kode_guru);

        btn_sign_in_guru = (Button)findViewById(R.id.btn_sign_in_guru);

        btn_sign_in_guru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kode_permainan = generateString(6);
                Toast.makeText(LoginGuruActivity.this, kode_permainan, Toast.LENGTH_SHORT).show();
                signIn(entrnama_guru.getText().toString(), entr_kode_guru.getText().toString());
            }
        });
    }

    private void signIn(final String namaGuru, final String kodeGuru) {
        guru.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!kodeGuru.isEmpty()){
                    Guru login = dataSnapshot.child(kodeGuru).getValue(Guru.class);
                    if (login.getKodeGuru().equals(kodeGuru)) {
                        addNametoDB(namaGuru);
                        openManajemenKuis(kode_permainan, namaGuru);
                    }
                    else {
                        Toast.makeText(LoginGuruActivity.this, "Kode salah", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(LoginGuruActivity.this, "Kode harus diisi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void openManajemenKuis(String kode_permainan, String namaGuru) {
        Intent intent = new Intent(LoginGuruActivity.this, ManajemenKuisActivity.class);
        intent.putExtra(EXTRA_TEXT_KODE, kode_permainan);
        intent.putExtra(EXTRA_TEXT_NAMA, namaGuru);
        startActivity(intent);
    }

    private void addNametoDB(String namaGuru) {
        if (!namaGuru.isEmpty()) {
            final Permainan permainan1 = new Permainan(kode_permainan, namaGuru, enable);
            permainan.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    permainan.child(permainan1.getKodePermainan()).setValue(permainan1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(LoginGuruActivity.this, "Nama harus diisi", Toast.LENGTH_SHORT).show();
        }
    }
}
