package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class UtileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utile);
    }

    public  void airtime(View view){
        Intent myIntent = new Intent(UtileActivity.this, VendreActivity.class);
        startActivity(myIntent);
    }

    public  void transfert(View view){
       Intent myIntent = new Intent(UtileActivity.this, TransfertActivity.class);
       startActivity(myIntent);
    }

    public  void paiement(View view){
        Intent myIntent = new Intent(UtileActivity.this, PaimentActivity.class);
        startActivity(myIntent);
    }
}
