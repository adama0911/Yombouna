package com.example.goptimus.yombouna;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HIT on 26/05/2018.
 */

public class PreferenceSharer {

    public Context context ;

    public PreferenceSharer(Context context){
        this.context = context ;
    }

    public void katabPreference(String key, String value){
        SharedPreferences.Editor kaatib = context.getSharedPreferences("samay_ndafa", MODE_PRIVATE).edit() ;

        String currentValue = qiraPreference(key) ;
        if (!currentValue.matches(value)) {
            if (currentValue.compareTo("fouraas") == 0)
                kaatib.putString(key, value);
            else
                kaatib.putString(key, currentValue + "#" + value);
            kaatib.apply();
        }
    }

    public String qiraPreference(String key){
        SharedPreferences prefs = context.getSharedPreferences("samay_ndafa", MODE_PRIVATE) ;
        String restoredValue = prefs.getString(key, "fouraas") ;
        return restoredValue ;
    }

    public void worksOnFirstRun(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.getBoolean("ontimecode", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("ontimecode", true);
            editor.putInt("appInstanceID", 123);
            editor.apply();
        }
    }

    public String retrieveAppInstanceId(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String restoredValue = prefs.getString("ontimecode", "fouraas") ;
        return restoredValue ;
    }

}