package com.girls.scanize.selfiecamera.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.girls.scanize.selfiecamera.R;
import com.girls.scanize.selfiecamera.Utils.Constans;
import com.girls.scanize.selfiecamera.Utils.PrefrenceHandler;

public class MainActivity extends AppCompatActivity {

    WebView wv1;
    ProgressDialog progressDialog;
    PrefrenceHandler prefrenceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Constans.isOnline(MainActivity.this)) {
            Constans.NoInternetDialog(MainActivity.this);
        }

        prefrenceHandler = new PrefrenceHandler(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        wv1 = (WebView) findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                String webUrl = wv1.getUrl();
            }

        });
        if (!Constans.isOnline(MainActivity.this)) {
            Constans.NoInternetDialog(MainActivity.this);
        } else {
            wv1.loadUrl(Constans.HOMEURL);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}