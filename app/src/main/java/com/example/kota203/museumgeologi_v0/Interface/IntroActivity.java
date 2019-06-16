package com.example.kota203.museumgeologi_v0.Interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.kota203.museumgeologi_v0.R;


public class IntroActivity extends AppCompatActivity {

    public static final String EXTRA_TEXT_KODE = "com.example.application.example.EXTRA_TEXT_KODE";
    GridLayout mainGridintro;
    int ruang_geologi_indonesia = 1;
    int ruang_sumber_daya_geologi = 2;
    int ruang_manfaat_dan_bencana = 3;
    int ruang_sejarah_kehidupan = 4;

    Button btn_lanjutkan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

//        database = FirebaseDatabase.getInstance();
//        guru = database.getReference("Guru");
        btn_lanjutkan = (Button)findViewById(R.id.btn_lanjutkan);
        btn_lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityPilihAktor();
            }
        });

        mainGridintro = (GridLayout)findViewById(R.id.mainGridintro);

        //set event
        setSingleEvent(mainGridintro);
    }

    private void setSingleEvent(GridLayout mainGridintro) {
        //loop all child
        for(int i=0;i<mainGridintro.getChildCount();i++){
            CardView cardView = (CardView)mainGridintro.getChildAt(i);
            final int finalI = i;
            final int finalI1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        openInfoRuang(ruang_geologi_indonesia);
                    } else if (finalI == 1) {
                        openInfoRuang(ruang_sumber_daya_geologi);
                    } else if (finalI == 2) {
                        openInfoRuang(ruang_manfaat_dan_bencana);
                    } else if (finalI == 3) {
                        openInfoRuang(ruang_sejarah_kehidupan);
                    }
                }
            });
        }
    }

    private void openInfoRuang(int id_ruang) {
        Intent intent = new Intent(IntroActivity.this, InfoRuangActivity.class);
        intent.putExtra("Nomor_Ruang", id_ruang);
        startActivity(intent);
    }

    private void openActivityPilihAktor() {
        Intent intent = new Intent(IntroActivity.this, PilihAktorActivity.class);
        startActivity(intent);
    }
}