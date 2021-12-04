package com.girls.scanize.selfiecamera.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.girls.scanize.selfiecamera.Model.SendOtpModel;
import com.girls.scanize.selfiecamera.R;
import com.girls.scanize.selfiecamera.Services.ApiInterface;
import com.girls.scanize.selfiecamera.Services.RetrofitClient;
import com.girls.scanize.selfiecamera.Utils.Constans;
import com.girls.scanize.selfiecamera.Utils.PrefrenceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSIONS = 1001;
    private static final String TAG = "data-->";
    EditText edt_otp;
    TextView tv_submit, tv_no_label;
    String MobileNo, OTP, Type;
    ProgressDialog progressDialog;
    PrefrenceHandler prefrenceHandler;
    Intent intent = null;

    String[] permissions = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        checkPermissions();
        intent = getIntent();
        if (intent != null) {
            MobileNo = intent.getStringExtra("num");
            Type = intent.getStringExtra("type");
        }

        if (!Constans.isOnline(OTPActivity.this)) {
            Constans.NoInternetDialog(OTPActivity.this);
        }

        progressDialog = new ProgressDialog(OTPActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        prefrenceHandler = new PrefrenceHandler(OTPActivity.this);
        edt_otp = findViewById(R.id.edt_otp);
        tv_submit = findViewById(R.id.tv_submit);
        tv_no_label = findViewById(R.id.tv_no_label);
        tv_no_label.setText("A code is sent to " + MobileNo + ". Please enter it here.");

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constans.isOnline(OTPActivity.this)) {
                    Constans.NoInternetDialog(OTPActivity.this);
                } else {
                    if (edt_otp.getText().length() != 0) {
                        OTP = edt_otp.getText().toString();
                        if (Type.equals("jawwal")) {
                            if (OTP.length() == 3) {
                                VerifyOTP(getConvertJson("pal_jawwal_meme", MobileNo, OTP));
                            } else {
                                Toast.makeText(OTPActivity.this, "Enter 4 digit OTP", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (OTP.length() == 5) {
                                VerifyOTP(getConvertJson("pal_ooredoo_drawit", MobileNo, OTP));
                            } else {
                                Toast.makeText(OTPActivity.this, "Enter 6 digit OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(OTPActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        edt_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 5) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void VerifyOTP(String object) {
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object));

        final ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.verifyOtp(body).enqueue(new Callback<SendOtpModel>() {
            @Override
            public void onResponse(Call<SendOtpModel> call, Response<SendOtpModel> response) {

                SendOtpModel adsDataModel = response.body();
                if (response.isSuccessful()) {
                    if (adsDataModel.getResponse().equals("SUCCESS")) {
                        progressDialog.dismiss();

                        if (Type.equals("Jawwal")) {
                            SendErrorOTP(MobileNo, getConvertJson1(MobileNo));
                        } else {
                            prefrenceHandler.setString(Constans.TYPE, "ooredoo");
                            prefrenceHandler.setString(Constans.MOBILE_NO, MobileNo);
                            prefrenceHandler.setBoolean(Constans.IsLogin, true);
                            startActivity(new Intent(OTPActivity.this, MainActivity.class)
                                    .putExtra("num", MobileNo));
                            killActivity();
                        }

                    } else {
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SendOtpModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
            }
        });

    }

    private void killActivity() {
        finish();
    }

    public String getConvertJson(String appName, String number, String otp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appname", appName);
            jsonObject.put("apiname", "verifyotp");
            jsonObject.put("msisdn", number);
            jsonObject.put("otp", otp);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG-->", "getConvertJson: " + jsonObject.toString());
        return jsonObject.toString();
    }


    public void SendErrorOTP(String num, String object) {
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object));

        final ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.sendAPI1Otp(body).enqueue(new Callback<SendOtpModel>() {
            @Override
            public void onResponse(Call<SendOtpModel> call, Response<SendOtpModel> response) {

                SendOtpModel adsDataModel = response.body();

                if (response.isSuccessful()) {
                    if (adsDataModel.getResponse().equals("SUCCESS")) {
                        progressDialog.dismiss();
                        startActivity(new Intent(OTPActivity.this, OTPErrorActivity.class).putExtra("num", num));
                        killActivity();

                    } else {
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SendOtpModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
            }
        });

    }

    public String getConvertJson1(String number) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appname", "pal_jawwal_mtoon");
            jsonObject.put("apiname", "sendotp");
            jsonObject.put("msisdn", number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG-->", "getConvertJson: " + jsonObject.toString());
        return jsonObject.toString();
    }


    @Override
    public void onResume() {
        if (checkPermissions()) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (checkPermissions()) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                edt_otp.setText(message);
            }
        }
    };


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;
                        }
                    }
                } else {

                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OTPActivity.this, LoginActivity.class));
        finish();
    }
}