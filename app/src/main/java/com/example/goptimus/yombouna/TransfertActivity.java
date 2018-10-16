package com.example.goptimus.yombouna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransfertActivity extends AppCompatActivity {

    final String requestUrl = "http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/airtime.php";
    final String requestUrlCaution = "http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/airtimeCaution.php";

    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";

    private final JSONObject servicesNumbers = new JSONObject();
    LayoutInflater inflater;
    AlertDialog.Builder builder;

    Bundle extras;

    int globalCaution = 0;
    int globalCommissions = 0;

    Spinner serviceEdit;
    EditText telEdit;
    EditText mntEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert);

        builder = new AlertDialog.Builder(this);
        inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        Spinner spinner = (Spinner) findViewById(R.id.serviceEdit);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Object item = parent.getItemAtPosition(pos);
                String serv = item.toString();
                Log.d("Log", "=================+++>>>>"+ serv.toLowerCase());
                try {
                    if(servicesNumbers.has(serv.toLowerCase())){
                        Log.d("Log", "--------------------->>"+ servicesNumbers.toString());
                        String number = servicesNumbers.getString(serv.toLowerCase());
                        EditText tel = (EditText) findViewById(R.id.numtEdit);
                        tel.setText(number);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("it works...   ");

            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        try {
            getServiceNumber();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (extras != null) {
            if (extras.getString("globalCaution") != null) {
                globalCaution = Integer.parseInt(extras.getString("globalCaution"));
                globalCommissions = Integer.parseInt(extras.getString("globalCommissions"));

                TextView caution = (TextView) findViewById(R.id.accountAmount);
                TextView commissions = (TextView) findViewById(R.id.commissions);

                caution.setText("Caution : " + globalCaution);
                commissions.setText("Commissions : " + globalCommissions);
            } else {
                caution();
            }
        } else
            caution();

        serviceEdit = (Spinner) this.findViewById(R.id.serviceEdit);
        telEdit =   (EditText) this.findViewById(R.id.numtEdit);
        mntEdit =   (EditText) this.findViewById(R.id.numtEdit);
    }

    public  void getServiceNumber () throws JSONException {
        servicesNumbers.put("orange money",776537639);
        servicesNumbers.put("tigo cash",765093421);
        servicesNumbers.put("wizall",704098456);
        servicesNumbers.put("e-money", 713445676);
    }

    public  void transfertHistry (View view){
        Intent myIntent = new Intent(TransfertActivity.this,TransfertHistoryActivity.class);
        startActivity(myIntent);
    }


    public void transfort(View view){
        String service =  (serviceEdit.getSelectedItem()).toString();
        String tel     =  (telEdit.getText()).toString();
        String montant =  (mntEdit.getText()).toString();

        Map<String,String> postMap = new HashMap<>();
        postMap.put("service",service);
        postMap.put("telephone",tel);
        postMap.put("montant",montant);

        transfortHandle(requestUrlCaution, postMap);
    }

    public void transfortHandle(String requestUrl, final Map<String, String> postMap) {

        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.saverequest, null);
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

                Intent myIntent = new Intent(TransfertActivity.this, LoadingActivity.class);
                myIntent.putExtra("request", response);
                myIntent.putExtra("caution", globalCaution);
                myIntent.putExtra("commissions", globalCommissions);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Intent myIntent = new Intent(TransfertActivity.this, ErrorActivity.class);
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


    public void cautionHandle(final String requestUrl, final Map<String, String> postMap) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result: ", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                //builder.setTitle("response");
                //builder.setMessage(response);
                //builder.create().show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    TextView caution = (TextView) findViewById(R.id.accountAmount);
                    TextView commissions = (TextView) findViewById(R.id.commissions);
                    globalCaution = Integer.parseInt((String) jsonObject.get("caution"));
                    Log.e("Volley Result:Con", "=============>" + jsonObject.getString("commissions")); //the response contains the result from the server, a json string or any other object returned by your server

                    if ((jsonObject.getString("commissions")).toString().compareTo("null") != 0)
                        globalCommissions = Integer.parseInt((String) jsonObject.get("commissions"));
                    else
                        globalCommissions = 0;
                    caution.setText("Caution : " + globalCaution);
                    commissions.setText("Commissions : " + globalCommissions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
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

    public void caution() {
        Map<String, String> postMap = new HashMap<>();
        postMap.put("token", tokenFinal);

        cautionHandle(requestUrlCaution, postMap);
    }
}
