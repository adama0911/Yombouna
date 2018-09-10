package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrorActivity extends AppCompatActivity {
    Bundle extras ;
    int globalCommissions = 0;
    int globalCaution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        extras = getIntent().getExtras();
        if (extras != null) {

            globalCaution = Integer.parseInt(extras.getString("globalCaution"));
            globalCommissions = Integer.parseInt(extras.getString("globalCommissions"));
        }

    }

    public void okerror(View view){
        Intent myIntent = new Intent(ErrorActivity.this,VendreActivity.class);
        myIntent.putExtra("globalCommissions",globalCommissions);
        myIntent.putExtra("globalCommissions",globalCommissions);
        startActivity(myIntent);
    }
}
