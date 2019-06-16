package com.example.kota203.museumgeologi_v0.Interface;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.kota203.museumgeologi_v0.Interface.Koordinator.VerifikasiKoordinatorActivity;
import com.example.kota203.museumgeologi_v0.Interface.Peserta.VerifikasiGabungKuisActivity;
import com.example.kota203.museumgeologi_v0.R;

public class PilihAktorActivity extends AppCompatActivity {

    GridLayout mainGridbermain;
    Button button_back;
//    MaterialEditText kodeGuru;
//
//    FirebaseDatabase database;
//    DatabaseReference guru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_aktor);

//        database = FirebaseDatabase.getInstance();
//        guru = database.getReference("Guru");

        mainGridbermain = (GridLayout)findViewById(R.id.mainGridbermain);
        button_back = (Button)findViewById(R.id.button_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoActivityIntro();
            }
        });
        //set event
        setSingleEvent(mainGridbermain);
    }

    private void setSingleEvent(GridLayout mainGridbermain) {
        //loop all child
        for(int i=0;i<mainGridbermain.getChildCount();i++){
            CardView cardView = (CardView)mainGridbermain.getChildAt(i);
            final int finalI = i;
            final int finalI1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        openLoginKoordinator();
                    } else if (finalI == 1) {
                        openLoginPeserta();
//                      Toast.makeText(pilihAktorActivity.this, "click : true" + finalI1, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void openLoginPeserta() {
        Intent i = new Intent(PilihAktorActivity.this, VerifikasiGabungKuisActivity.class);
        startActivity(i);
    }

    private void openLoginKoordinator() {
        Intent i = new Intent(PilihAktorActivity.this, VerifikasiKoordinatorActivity.class);
        startActivity(i);
    }

    private void backtoActivityIntro() {
        Intent i = new Intent(PilihAktorActivity.this, IntroActivity.class);
        startActivity(i);
    }
}
