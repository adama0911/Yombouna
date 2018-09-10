package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrConnexionActivity extends AppCompatActivity {
    Bundle extras ;
    int globalCommissions = 0;
    int globalCaution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_err_connexion);

        extras = getIntent().getExtras();
        if (extras != null) {

            globalCaution = Integer.parseInt(extras.getString("globalCaution"));
            globalCommissions = Integer.parseInt(extras.getString("globalCommissions"));
        }

    }

    public void buttonWarning (View view){
        Intent i = new Intent(this,VendreActivity.class);
        i.putExtra("globalCommissions",globalCommissions);
        i.putExtra("globalCommissions",globalCommissions);
        startActivity(i);
    }
}
