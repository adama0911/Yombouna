package com.example.goptimus.yombouna;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

    Bundle extras ;
    String resquest;
    Timer myTimer;
    final String requestUrl = " http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/verifierReponse.php";
    AlertDialog.Builder builder;
    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Date date = new Date();

        builder = new AlertDialog.Builder(this);

     /*   new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 300 milliseconds later");
                        Intent myIntent = new Intent(LoadingActivity.this,EndActivity.class);
                        startActivity(myIntent);
                    }
                },
                5000);*/

        extras = getIntent().getExtras();
        if (extras != null) {
            Log.d("extras","=======================+>extras");
            resquest = extras.getString("request");
            //The key argument here must match that used in the other activity

            JSONObject json = new JSONObject();

            try {
                json.put("requestParam",resquest);
                json.put("tokenParam",tokenFinal);
                json.put("cacheDisable",date.getYear()+"-"+date.getMonth()+"-"+date.getDay());

                final String requestParam = json.toString();

                myTimer = new Timer();

                myTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        Map<String, String> postMap = new HashMap<>();
                        postMap.put("requestParam",requestParam);
                        Log.i("tag", "A Kiss every 5 seconds");

                        salleCreshHandle(requestUrl, postMap);
                    }
                },0,1000);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void  salleCreshHandle(final String requestUrl, final Map<String, String> postMap) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
               // int rep = Integer.parseInt(response.toString());

               /* if(response=="1"){
                    myTimer.cancel();
                    Intent myIntent = new Intent(LoadingActivity.this,EndActivity.class);
                    startActivity(myIntent);
                }
                else if (response!="-1" && response!="1"){
                    myTimer.cancel();
                    Intent myIntent = new Intent(LoadingActivity.this,ErrorActivity.class);
                    startActivity(myIntent);
                }
                else
                {*/

               if(response.compareTo("1")==1){
                        myTimer.cancel();
                        Intent myIntent = new Intent(LoadingActivity.this,EndActivity.class);
                        startActivity(myIntent);
               }
               else   if(!(response.compareTo("1") ==1 || response.compareTo("-1") ==1)){
                        myTimer.cancel();
                        Intent myIntent = new Intent(LoadingActivity.this,ErrorActivity.class);
                        startActivity(myIntent);
               }

               /*}*/

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                myTimer.cancel();
                // go to echec activity
                Intent myIntent = new Intent(LoadingActivity.this,ErrorActivity.class);
                startActivity(myIntent);
            }
        }) {

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

