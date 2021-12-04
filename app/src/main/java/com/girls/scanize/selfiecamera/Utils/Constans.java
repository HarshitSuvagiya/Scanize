package com.girls.scanize.selfiecamera.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.girls.scanize.selfiecamera.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Constans {

    //Preference keys
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String IsLogin = "IsLogin";
    public static final String IS_STATUD = "IsStatus";
    public static final String KEY = "key";
    public static final String MOBILE_NO = "mobile_no";
    public static final String TYPE = "type";


    //api url
    public static final String BASEURL = "https://88oepzritf.execute-api.eu-west-2.amazonaws.com/";
    public static final String KEYURL = "default/auth";

    public static final String TOCKENURL = "https://d2xfb39vs7ku24.cloudfront.net/";

    public static final String COUNTRYCODEURL = "https://d2xfb39vs7ku24.cloudfront.net";
    public static final String CODEKEYURL = "/country_code_cf.php";

    public static final String HOMEURL = "https://dyeuby9hsdbht.cloudfront.net/index.html";

    public static boolean is_online;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting() || !connectivityManager.getActiveNetworkInfo().isAvailable() || !connectivityManager.getActiveNetworkInfo().isConnected()) {
            is_online = false;
            return is_online;
        }
        is_online = true;
        return is_online;
    }

    static AlertDialog.Builder builder;

    public static void NoInternetDialog(Context context) {
        builder = new AlertDialog.Builder(context);
        //Setting message manually and performing action on button click
        builder.setMessage("Please check Internet connection !!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error");
        alert.show();

    }

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmss", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(99999);
        // this will convert any number sequence into 6 character.
        return String.format("%05d", number);
    }
}
