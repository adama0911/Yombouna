package com.example.goptimus.yombouna;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PaimentActivity extends AppCompatActivity {

    String requestUrl;

    AlertDialog.Builder builder;
    LayoutInflater inflater;
    Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Scan Button

        builder = new AlertDialog.Builder(this);
        inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        timer = new Timer();
    }

    public  void scanner(View view){
        new IntentIntegrator(PaimentActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
    }

    public void generateQR (View view) throws WriterException {

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

        BarcodeFormat code = BarcodeFormat.QR_CODE;
        String codeforma = (code.values()).toString();

        Bitmap bitmap = barcodeEncoder.encodeBitmap("content",code, 400, 400);

        Log.d("Log QR","===================code= =====>"+ (codeforma.split(";"))[1]);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.qrcode, null);
        ImageView img  = (ImageView) v.findViewById(R.id.qrcodeImg);
        img.setImageBitmap(bitmap);
        builder.setView(v);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                showResultDialogue(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //method to construct dialogue with scan results
    public void showResultDialogue(final String result) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }

        builder.setTitle("QR-CODE");
        builder.setMessage(result);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getresponce (String response){
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.confirme_paiement,null);
        TextView textView = v.findViewById(R.id.confirmepaiement);
        textView.setText(response);
        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void processingScanner(String qrcode){
        Map<String,String> postMap = new HashMap<>();

        postMap.put("token","");
        postMap.put("qrcode", qrcode);
    }

    public void handleScanner (final String requestUrl, final Map<String, String> postMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
                @Override
            public void onResponse(String response) {
                Log.e("Volley Result: ", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                //builder.setTitle("response");
                //builder.setMessage(response);
                //builder.create().show();
                timer.cancel();
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

    public void handleSaveQrcode (final String requestUrl, final Map<String, String> postMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result: ", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                //builder.setTitle("response");
                //builder.setMessage(response);
                //builder.create().show();

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

    public void handleQrcode (final String requestUrl, final Map<String, String> postMap){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley Result: ", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                //builder.setTitle("response");
                //builder.setMessage(response);
                //builder.create().show();

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

    public void handleQrcodeCaller(Number idReq){
        final Map <String, String> postMap = new HashMap<>();
        postMap.put("idReq",idReq.toString());
        postMap.put("token","");

        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Log.i("tag", "");

                handleQrcode(requestUrl,postMap);
            }
        },0,5000);
    }

    public void handleSaveQrcodeCaller (String qrcode){
        final Map <String, String> postMap = new HashMap<>();
        postMap.put("utoken","");
        postMap.put("qrcode",qrcode);
        postMap.put("mytoken","");

        handleSaveQrcode( requestUrl,postMap);
    }


}

