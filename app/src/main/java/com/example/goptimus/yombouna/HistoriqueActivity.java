package com.example.goptimus.yombouna;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.Map;

public class HistoriqueActivity extends AppCompatActivity {

    final String requestUrl = "http://10.0.2.2/test.php";
    TableLayout tableLayout;

    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);


        tableLayout = (TableLayout) findViewById(R.id.tableView);

        inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);


        handleHistary(requestUrl,null);
    }

    public  void  gotoVente(View v){
        Intent myIntent = new Intent(HistoriqueActivity.this,VendreActivity.class);
        startActivity(myIntent);
    }

    public void   logout (View view){
        Intent myIntent = new Intent(HistoriqueActivity.this,AuthActivity.class);
        startActivity(myIntent);
    }

    public void rowAdapter (String s, String t, String m, String d){
        TableRow row = (TableRow) inflater.inflate(R.layout.table_row,null);

        TextView service = (TextView) row.findViewById(R.id.service);
        TextView tel = (TextView) row.findViewById(R.id.telephone);
        TextView montant = (TextView) row.findViewById(R.id.montant);
        TextView date = (TextView) row.findViewById(R.id.date);

        service.setText(s);
        tel.setText(t);
        montant.setText(m);
        date.setText(d);

        tableLayout.addView(row);
    }

    public void  handleHistary(String requestUrl, final Map<String, String> postMap) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArr = new JSONArray(response);
                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        rowAdapter(jsonObj.getString("service"),jsonObj.getString("telephone"), jsonObj.getString("montant"), jsonObj.getString("date"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server

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
}
