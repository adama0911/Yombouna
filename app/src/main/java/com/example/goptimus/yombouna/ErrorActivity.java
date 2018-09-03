package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
    }

    public void okerror(View view){
        Intent myIntent = new Intent(ErrorActivity.this,VendreActivity.class);
        startActivity(myIntent);
    }
}
