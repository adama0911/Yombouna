package com.example.goptimus.yombouna;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransfertHistoryActivity extends AppCompatActivity {

    final String requestUrl = "http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/airtimeHisto.php";
    final String requestUrlNext = "http://51.254.200.129/backendprod/horsSentiersBattus/scripts/airtime/airtimeHistoNext.php";
    TableLayout tableLayout;
    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    JSONArray history = null;
    int indexHistory;
    int Maxrow = 5;
    String  targetDate = "";
    AlertDialog connexionDialog;
    String lastID;

    Bundle extras ;
    int globalCommissions = 0;
    int globalCaution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfert_history);


        indexHistory = 0;
        tableLayout = (TableLayout) findViewById(R.id.tableView);

        inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        builder = new AlertDialog.Builder(this);

        history = new JSONArray();

        extras = getIntent().getExtras();
        if (extras != null) {

            globalCaution = Integer.parseInt(extras.getString("globalCaution"));
            globalCommissions = Integer.parseInt(extras.getString("globalCommissions"));
        }

        history();
    }


    public  void gotoTransfert(View view){
        Intent myIntent = new Intent(TransfertHistoryActivity.this,TransfertActivity.class);
        startActivity(myIntent);
    }

    public void logout(View view) {
        Intent myIntent = new Intent(TransfertHistoryActivity.this, AuthActivity.class);
        startActivity(myIntent);

       /* Map<String, String> postMap = new HashMap<>();
        postMap.put("token",tokenFinal);*/
    }


    public void rowAdapter (String s, String t, String m, String d){
        TableRow row = (TableRow) inflater.inflate(R.layout.table_row,null);

        TextView service = (TextView) row.findViewById(R.id.service);
        TextView tel = (TextView) row.findViewById(R.id.telephone);
        TextView montant = (TextView) row.findViewById(R.id.montant);
        TextView date = (TextView) row.findViewById(R.id.date);
        ImageView avatar = (ImageView) row.findViewById(R.id.avatar);
        //avatar.setImageResource(R.drawable.orange);

        if((s.toLowerCase().trim()).compareTo("seddo")==0)
            avatar.setImageResource(R.drawable.orange);
        else if((s.toLowerCase().trim()).compareTo("izi")==0)
            avatar.setImageResource(R.drawable.tigo);
        else if((s.toLowerCase().trim()).compareTo("yakalma")==0)
            avatar.setImageResource(R.drawable.expresso);

        service.setText(s);
        tel.setText(t);
        montant.setText(m);
        date.setText(d);

        tableLayout.addView(row);
    }

    public void longrowAdapter (String d){
        TableRow row = (TableRow) inflater.inflate(R.layout.table_longrow,null);

        TextView daterow = (TextView) row.findViewById(R.id.daterow);

        //avatar.setImageResource(R.drawable.orange);
        daterow.setText(d);

        tableLayout.addView(row);
    }

    public void  handleHistary(final String requestUrl, final Map<String, String> postMap) {

        showloadNext();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server;
                hideloadNext();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("indexHistory","----------------"+indexHistory+"------------------");
                    Log.d("lastID","----------------"+lastID+"------------------");
                    lastID = (jsonArray.getJSONObject(jsonArray.length() -1)).getString("id");
                    Log.d("lastID","----------------"+lastID+"------------------");

                    for (int i = indexHistory, max = indexHistory + Maxrow, j= 0 ; (j < jsonArray.length() ) && (i < max); i++, j++){
                        JSONObject obj = jsonArray.getJSONObject(j);

                        indexHistory = indexHistory + 1;
                        history.put(obj);
                        String traitement = obj.getString("traitement");
                        String infoclient = obj.getString("infoclient");
                        String montant = obj.getString("montant");
                        String dateoperation = obj.getString("dateoperation");

                        String[] splitStr = dateoperation.split("\\s+");

                        if(targetDate.compareTo(splitStr[0]) != 0) {
                            longrowAdapter(splitStr[0]);
                            targetDate = splitStr[0];
                        }
                        rowAdapter(traitement,infoclient,montant,splitStr[1]);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                hideloadNext();
                showConnextionWarning();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //..... Add as many key value pairs in the map as necessary for your request
                //Log.d("postMap",(postMap.toString()));
                return postMap;
            }
        };
        //make the request to your server as indicated in your request url

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public  void history(){
        Map<String, String> post = new HashMap<>();
        post.put("token",tokenFinal);

        if(post != null){
            Log.d("POST","============>+++++++"+ post.toString());
            handleHistary(requestUrl, post);
        }
    }

    public void nextPage(View view) throws JSONException {
        targetDate = "";
        tableLayout.removeAllViews();
        if ((history.length() > 0) && (indexHistory < (history.length() - 1)) && ((indexHistory + Maxrow) < history.length())){
            for (int i = indexHistory, max = indexHistory + Maxrow; i < max; i++){
                JSONObject obj = history.getJSONObject(i);
                indexHistory++;
                String traitement = obj.getString("traitement");
                String infoclient = obj.getString("infoclient");
                String montant = obj.getString("montant");
                String dateoperation = obj.getString("dateoperation");

                String[] splitStr = dateoperation.split("\\s+");

                if(targetDate.compareTo(splitStr[0]) != 0) {
                    longrowAdapter(splitStr[0]);
                    targetDate = splitStr[0];
                }
                rowAdapter(traitement,infoclient,montant,splitStr[1]);            }
        }
        else if((history.length() > 0) && (indexHistory < (history.length() - 1))){
            Log.d("Handle1","================++> [2]" );
            for (int i = indexHistory; i < history.length(); i++){
                JSONObject obj = history.getJSONObject(i);
                indexHistory++;
                String traitement = obj.getString("traitement");
                String infoclient = obj.getString("infoclient");
                String montant = obj.getString("montant");
                String dateoperation = obj.getString("dateoperation");

                String[] splitStr = dateoperation.split("\\s+");

                if(targetDate.compareTo(splitStr[0]) != 0) {
                    longrowAdapter(splitStr[0]);
                    targetDate = splitStr[0];
                }
                rowAdapter(traitement,infoclient,montant,splitStr[1]);            }
        }else{
            Log.d("Handle1","================++> [3]" );


            Map<String, String> post = new HashMap<>();
            post.put("token",tokenFinal);
            Log.d("lasId","----------------------"+lastID+"------------------");
            post.put("lastId",lastID);

            if(post != null){
                Log.d("POST","============>+++++++"+ post.toString());
                handleHistary(requestUrlNext,post);
            }

        }
    }


    public void backPage(View view) throws JSONException {
        targetDate = "";
        tableLayout.removeAllViews();
        JSONArray jsonArr = new JSONArray();
        int ii = 0;

        while (indexHistory > 0 && ii < Maxrow){
            indexHistory--;
            ii++;
            jsonArr.put(history.getJSONObject(indexHistory));
        }

        for (int i = 0; i < jsonArr.length(); i++)
        {
            JSONObject obj = jsonArr.getJSONObject(i);
            String traitement = obj.getString("traitement");
            String infoclient = obj.getString("infoclient");
            String montant = obj.getString("montant");
            String dateoperation = obj.getString("dateoperation");

            String[] splitStr = dateoperation.split("\\s+");

            if(targetDate.compareTo(splitStr[0]) != 0) {
                longrowAdapter(splitStr[0]);
                targetDate = splitStr[0];
            }
            rowAdapter(traitement,infoclient,montant,splitStr[1]);
        }

    }


    public void showloadNext(){

        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.paginationloader,null);

        builder.setView(v);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void hideloadNext(){
        dialog.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void historySort () throws JSONException {
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();

        for (int i = 0; i < history.length(); i++) {
            jsonValues.add(history.getJSONObject(i));
        }

        jsonValues.sort(new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject jo1, JSONObject jo2) {
                String date1="";
                String date2="";
                try {
                    date1 = jo1.getString("dateoperation");
                    date2 = jo2.getString("dateoperation");
                    return date1.compareTo(date2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return (date2.compareTo(date1));
            }
        });

        history = new JSONArray();

        for (int i = 0; i < jsonValues.size(); i++) {
            history.put(jsonValues.get(i));
        }
    }

    public   void showConnextionWarning (){
        builder.setTitle("Connexion");
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.errorconnexion,null);
        Button button = linearLayout.findViewById(R.id.errorconnexion);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connexionDialog.cancel();
            }
        });

        builder.setView(linearLayout);
        connexionDialog = builder.create();
        connexionDialog.setCanceledOnTouchOutside(false);
        connexionDialog.show();
    }
}
