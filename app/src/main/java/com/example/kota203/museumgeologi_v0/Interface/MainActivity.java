package com.example.kota203.museumgeologi_v0.Interface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.kota203.museumgeologi_v0.Interface.Input.PilihInput;
import com.example.kota203.museumgeologi_v0.R;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGridbermain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGridbermain = (GridLayout)findViewById(R.id.mainGridbermain);

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
                        openIntro();
                    } else if (finalI == 1) {
                        openRankingList();
                    } else if (finalI ==2) {
                        openInputData();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed()
    {
        Toast.makeText(MainActivity.this,"Klik button keluar untuk keluar dari aplikasi",Toast.LENGTH_SHORT).show();
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }

    private void openIntro() {
        Intent i = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(i);
    }

    private void openRankingList() {
        Intent i = new Intent(MainActivity.this, PeringkatActivity.class);
        startActivity(i);
    }

    private void openInputData() {
        Intent i = new Intent(MainActivity.this, PilihInput.class);
        startActivity(i);
    }
}