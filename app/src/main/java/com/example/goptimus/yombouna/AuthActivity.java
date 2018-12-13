package com.example.goptimus.yombouna;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AuthActivity extends AppCompatActivity {
    String tokenFinal = "4cd6526371c082310bb1ff05affe63eb3f84ea457";
    final String requestUrl = "http://10.0.2.2/test.php";
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    PreferenceSharer pfShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        pfShare = new PreferenceSharer(this) ;
        pfShare.worksOnFirstRun() ;

        inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        builder = new AlertDialog.Builder(this);

    }

    // valider l'autentification
    public void authentification(View view) {
        showloadNext();
        Button v = (Button) findViewById(R.id.validAuth);
        final EditText loginEdit = (EditText) findViewById(R.id.loginEdit);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);

        Log.d("Connexion", " =======> " + "Login : " + loginEdit.getText() + " password : " + passwordEdit.getText());

        String imei = "";

        String shareImei = pfShare.qiraPreference("imei");

        if(shareImei.compareTo("fouraas")!=0){
            imei = shareImei;
            Log.d("IMEI : ", "==================================++>" + imei);
        }
        else {
            imei = getDeviceIMEI();
            pfShare.katabPreference("imei",imei);
            Log.d("IMEI : ", "==================================++>" + imei);
        }

        Map<String, String> postMap = new HashMap<>();
        postMap.put("login", loginEdit.getText().toString());
        postMap.put("password", passwordEdit.getText().toString());
        postMap.put("imei",imei);

        login(requestUrl, postMap);
    }

    // Login ajax function
    public void login(String requestUrl, final Map<String, String> postMap) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideloadNext();

               /* try {
                    // Converter response to JSON object
                    JSONObject respJson = new JSONObject(response);
                    String timestamp    = respJson.getString("timestamp");
                    String token        = respJson.getString("token");

                    String imei      = "";

                    String shareImei = pfShare.qiraPreference("imei");

                    if(shareImei.compareTo("fouraas")!=0){
                        imei = shareImei;
                        Log.d("IMEI : ", "==================================++>" + imei);
                    }

                    String selfImei = toSha1(imei);

                    String selfToken = toSha1(selfImei + timestamp);

                    if(selfToken.compareTo(token)==0){
                        Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                        Intent myIntent = new Intent(AuthActivity.this, VendreActivity.class);
                        myIntent.putExtra("token", response);
                        startActivity(myIntent);
                    }
                    else{
                        Log.e("Volley Result", "=============>" + response); //the response contains the result from the server, a json string or any other object returned by your server
                        builder.setMessage("Vos parametres d'identification ne sont pas correct !");
                        builder.create().show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } */

                Intent myIntent = new Intent(AuthActivity.this, UtileActivity.class);
                myIntent.putExtra("token", response);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideloadNext();
                error.printStackTrace(); //log the error resulting from the request for diagnosis/debugging
                Intent myIntent = new Intent(AuthActivity.this, UtileActivity.class);
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

    public void showloadNext() {

        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.paginationloader, null);

        builder.setView(v);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void hideloadNext() {
        dialog.cancel();
    }

    public String getDeviceIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //   TODO: Consider calling
            //   ActivityCompat#requestPermissions
            //   here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //   int[] grantResults)
            //   to handle the case where the user grants the permission. See the documentation
            //   for ActivityCompat#requestPermissions for more details.
            return "Permission not garanted";
        }
        return telephonyManager.getDeviceId();
    }

    // Fonction de as
    String toSha1( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }

        return new String( hexChars );
    }
}
