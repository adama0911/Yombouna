package com.example.goptimus.yombouna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class VendreActivity extends AppCompatActivity {
    final String requestUrl = "http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/airtime.php";
    AlertDialog.Builder builder;
    EditText telEdit;
    EditText montantEdit;
    Button    valider ;
    LayoutInflater inflater;
    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendre);

        telEdit = (EditText) findViewById(R.id.telephoneEdit);
        montantEdit = (EditText) findViewById(R.id.montantEdit);
        valider    =  (Button) findViewById(R.id.validerSalleEdit);
        builder = new AlertDialog.Builder(this);
        inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);


    }

    public  void salling(View view){
        String tel = telEdit.getText().toString();
        String mnt = montantEdit.getText().toString();
        String type = "";

        if(Pattern.matches("^77[0-9]{7}",tel) || Pattern.matches("^78[0-9]{7}",tel)){
            type = "ceddo";
        }else if(Pattern.matches("^76[0-9]{7}",tel)){
            type = "izi";
        }else if(Pattern.matches("^70[0-9]{7}",tel)){
            type = "yakalma";
        }
        else
        {
            builder.setTitle("Erreur");
            builder.setMessage("Votre numero est incorrect !");
            builder.create().show();
            return;
        }

        if(Integer.parseInt(mnt) <= 0){
            builder.setTitle("Erreur");
            builder.setMessage("Votre montant est incorrect !");
            builder.create().show();
            return;
        }

        JSONObject json = new JSONObject();
        String requestparam = tel + "/" + mnt;
        try {
            json.put("requestParam",requestparam);
            json.put("tokenParam",tokenFinal);
            json.put("type",type);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestParam = json.toString();

        Log.d("Salling", "=========>"+ "Tel :" + telEdit.getText() + " montant : "+ montantEdit.getText());
        Map<String, String> postMap = new HashMap<>();
        postMap.put("requestParam",requestParam);

        salleCresh(requestUrl,postMap);
    }

    public  void  gotoHistory (View v){
        Intent myIntent = new Intent(VendreActivity.this,HistoriqueActivity.class);
        startActivity(myIntent);
    }

    public void   logout (View view){
        Intent myIntent = new Intent(VendreActivity.this,AuthActivity.class);
        startActivity(myIntent);
    }

    public void  salleCresh(String requestUrl, final Map<String, String> postMap) {

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.saverequest,null);
        builder.setView(view);
        final AlertDialog alert = builder.create();
        alert.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                //builder.setTitle("response");
                //builder.setMessage(response);
                //builder.create().show();
                alert.cancel();

                Intent myIntent = new Intent(VendreActivity.this,LoadingActivity.class);
                myIntent.putExtra("request",response);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Intent myIntent = new Intent(VendreActivity.this,ErrorActivity.class);
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
