package com.example.kota203.museumgeologi_v0.Interface.Input;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.kota203.museumgeologi_v0.Interface.Peserta.VerifikasiGabungKuisActivity;
import com.example.kota203.museumgeologi_v0.Interface.PilihAktorActivity;
import com.example.kota203.museumgeologi_v0.R;

public class PilihInput extends AppCompatActivity {
    GridLayout mainGridPilihInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_input);

        mainGridPilihInput = (GridLayout)findViewById(R.id.mainGridPilihInput);
        //set event
        setSingleEvent(mainGridPilihInput);
    }

    public void setSingleEvent(GridLayout mainGridPilihInput) {
        //loop all child
        for(int i=0;i<mainGridPilihInput.getChildCount();i++){
            CardView cardView = (CardView)mainGridPilihInput.getChildAt(i);
            final int finalI = i;
            final int finalI1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        openFormInputKoleksi();
                    } else if (finalI == 1) {
                        openFormInputSoal();
//                      Toast.makeText(pilihAktorActivity.this, "click : true" + finalI1, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public void openFormInputSoal() {
        Intent i = new Intent(PilihInput.this, InputSoal.class);
        startActivity(i);
    }

    public void openFormInputKoleksi() {
        Intent i = new Intent(PilihInput.this, InputKoleksi.class);
        startActivity(i);
    }
}
