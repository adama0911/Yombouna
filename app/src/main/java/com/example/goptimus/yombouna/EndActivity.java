package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndActivity extends AppCompatActivity {
    Bundle extras ;
    int globalCommissions = 0;
    int globalCaution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        extras = getIntent().getExtras();
        if (extras != null) {

            globalCaution = Integer.parseInt(extras.getString("globalCaution"));
            globalCommissions = Integer.parseInt(extras.getString("globalCommissions"));
        }
    }

    public void okend(View view){
        Intent myIntent = new Intent(EndActivity.this,VendreActivity.class);
        myIntent.putExtra("globalCommissions",globalCommissions);
        myIntent.putExtra("globalCommissions",globalCommissions);
        startActivity(myIntent);
    }
}


