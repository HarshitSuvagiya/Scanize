package com.girls.scanize.selfiecamera.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceHandler {
    SharedPreferences SelfiExpert;
    SharedPreferences.Editor SelfiExpertEditor;

    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "SelfieCamera";

    public PrefrenceHandler(Context context) {
        try {
            this._context = context;
            SelfiExpert = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            SelfiExpertEditor = SelfiExpert.edit();
            SelfiExpertEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String KEY) {
        return SelfiExpert.getBoolean(KEY, false);
    }

    public void setBoolean(String KEY, boolean is_purchased) {
        SelfiExpertEditor.putBoolean(KEY, is_purchased);
        SelfiExpertEditor.commit();
    }

    public String getString(String KEY) {
        return SelfiExpert.getString(KEY, "");
    }

    public void setString(String KEY, String value) {
        SelfiExpertEditor.putString(KEY, value);
        SelfiExpertEditor.commit();
    }
}

