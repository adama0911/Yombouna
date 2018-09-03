package com.example.goptimus.yombouna;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AuthActivity extends AppCompatActivity {
    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);


    }


    public void authentification(View view){
        Button v = (Button) findViewById(R.id.validAuth);
        final EditText loginEdit = (EditText) findViewById(R.id.loginEdit);
        final EditText  passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        final String requestUrl = "http://10.0.2.2/test.php";

        Log.d("Connexion"," =======> " + "Login : " + loginEdit.getText() + " password : "+ passwordEdit.getText());

        Map<String, String> postMap = new HashMap<>();
        postMap.put("login", loginEdit.getText().toString());
        postMap.put("password", passwordEdit.getText().toString());

        login(requestUrl,postMap);
    }

    public void  login(String requestUrl, final Map<String, String>  postMap){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result", "=============>"+response); //the response contains the result from the server, a json string or any other object returned by your server
                Intent myIntent = new Intent(AuthActivity.this,VendreActivity.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Intent myIntent = new Intent(AuthActivity.this,VendreActivity.class);
                startActivity(myIntent);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //..... Add as many key value pairs in the map as necessary for your request
                return postMap;
            }
        };
        //make the request to your server as indicated in your request url
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}
